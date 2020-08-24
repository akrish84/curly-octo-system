package main.jobapplication;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.beans.JobApplication;
import main.beans.JobApplicationStatus;
import main.db.DatabaseManager;

/**
 * Handler class to handle all operations regarding user's job application
 * 
 * @author akhilesh
 *
 */

public class JobApplicationHandler {
	
	private static Logger LOGGER = Logger.getLogger(JobApplicationHandler.class.getName());
	
	private static final String KEY_STATUS_ID = "id";
	private static final String KEY_STATUS_NAME = "status";
	
	/**
	 * Adds default job statuses (WISHLIST, APPLIED, INTERVIEW, OFFER) for user.
	 * 
	 * @param userID
	 * @throws Exception
	 */
	
	public static void addDefaultOptionsForUser(Long userID) throws SQLException {
		List<JobApplicationStatus> statuses = new ArrayList<>();
		JobApplicationStatus wishlist = new JobApplicationStatus();
		wishlist.setStatus(DefaultJobApplicationStatuses.WISHLIST.getStatus());
		wishlist.setRank(DefaultJobApplicationStatuses.WISHLIST.getRank());
		
		JobApplicationStatus applied = new JobApplicationStatus();
		applied.setStatus(DefaultJobApplicationStatuses.APPLIED.getStatus());
		applied.setRank(DefaultJobApplicationStatuses.APPLIED.getRank());
		
		JobApplicationStatus interview = new JobApplicationStatus();
		interview.setStatus(DefaultJobApplicationStatuses.INTERVIEW.getStatus());
		interview.setRank(DefaultJobApplicationStatuses.INTERVIEW.getRank());
		
		JobApplicationStatus offer = new JobApplicationStatus();
		offer.setStatus(DefaultJobApplicationStatuses.OFFER.getStatus());
		offer.setRank(DefaultJobApplicationStatuses.OFFER.getRank());
		
		statuses.add(wishlist);
		statuses.add(applied);
		statuses.add(interview);
		statuses.add(offer);
		LOGGER.log(Level.INFO, "Adding default statuses " + statuses + " for user " + userID);
		DatabaseManager.getInstance().addJobStatusesForUser(statuses, userID);
	}
	
	/**
	 * Adds job status for user. Rank of job status is assigned -> previous max rank + DEFAULT_RANK_GAP (1024)
	 * 
	 * @param status
	 * @param userID
	 * @throws SQLException
	 */
	public static void addJobStatusForUser(JobApplicationStatus status, Long userID) throws SQLException {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		int maxStatusRank = dbManager.fetchMaxJobStatusRankForUser(userID);
		status.setRank(maxStatusRank + JobApplicationStatus.DEFAULT_RANK_GAP);
		List<JobApplicationStatus> statuses = new ArrayList<>();
		statuses.add(status);
		LOGGER.log(Level.INFO, "Adding status " + status.getStatus() + " for user " + userID);
		dbManager.addJobStatusesForUser(statuses, userID);
		LOGGER.log(Level.INFO, "Successful");
	}
	
	
	/**
	 * Updates job status for user
	 * @param status
	 * @param userID
	 * @throws SQLException
	 */
	public static void updateJobStatusForUser(JobApplicationStatus status , Long userID) throws SQLException, IllegalArgumentException {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JobApplicationStatus existingStatus = dbManager.fetchJobStatus(userID, status.getId());
		if(existingStatus == null) {
			throw new IllegalArgumentException("Status with id " + status.getId() + " does not exist for user " + userID);
		}
		String oldStatus = existingStatus.getStatus();
		existingStatus.setStatus(status.getStatus());
//		List<ApplicationStatus> statuses = new ArrayList<>();
//		statuses.add(existingStatus);
		LOGGER.log(Level.INFO, "Updating status from " + oldStatus + " to " + status.getStatus() + " for user " + userID);
		dbManager.updateJobStatusForUser(existingStatus, userID);
		LOGGER.log(Level.INFO, "Successful");
	}
	
	/**
	 * All job statuses are re-ranked.
	 * New job statuses are added.
	 * Existing job statuses are updated.
	 * job statuses list should include all existingStatuses for that user.
	 * @param statuses
	 * @param userID
	 * @throws SQLException
	 * @throws JSONException 
	 */
	public static void updateJobStatusesForUser(String stasusesAsJSONString, Long userID) throws SQLException, JSONException, IllegalArgumentException {
		List<JobApplicationStatus> jobStatuses = buildJobStatusesFromJSONString(stasusesAsJSONString);
		DatabaseManager dbManager = DatabaseManager.getInstance();
		int rank = 0;
		List<JobApplicationStatus> addJobStatuses = new ArrayList<>();
		List<JobApplicationStatus> updateJobStatuses = new ArrayList<>();
		Map<Long, JobApplicationStatus> existingJobStatuses = dbManager.fetchJobApplicationStatusesForUser(userID);
		for(JobApplicationStatus status : jobStatuses) {
			status.setRank(rank);
			rank += JobApplicationStatus.DEFAULT_RANK_GAP;
			if(status.getId() == null || !existingJobStatuses.containsKey(status.getId())) {
				addJobStatuses.add(status);
			} else {
				updateJobStatuses.add(status);
				existingJobStatuses.remove(status.getId());
			}
		}
		if(!existingJobStatuses.isEmpty()) {
			LOGGER.log(Level.SEVERE, "Missing old Job statuses " + existingJobStatuses);
			throw new IllegalArgumentException("Input missing all old statuses, Can not rerank statuses");
		}
		dbManager.beginTransaction();
		try {
			LOGGER.log(Level.INFO, "Adding Job statuses " + addJobStatuses + " for user " + userID);
			dbManager.addJobStatusesForUser(addJobStatuses, userID);
			LOGGER.log(Level.INFO, "Added successfully");
			LOGGER.log(Level.INFO, "Updating Job statuses " + addJobStatuses + " for user " + userID);
			dbManager.updateJobStatusesForUser(updateJobStatuses, userID);
			LOGGER.log(Level.INFO, "Updated successfully");
			dbManager.commit();
		} catch(SQLException e) {
			dbManager.rollback();
			dbManager.endTransaction();
			throw e;
		}
		dbManager.endTransaction();
	}
	
	
	/**
	 * 
	 * Builds JobApplicationStatus object using information from the jobStatusesAsJsonString.
	 * 
	 * @param jobStatusesAsJSONString
	 * @return return a JobApplicationStatus object 
	 * @throws JSONException
	 * @throws IllegalArgumentException
	 */
	private static List<JobApplicationStatus> buildJobStatusesFromJSONString(String jobStatusesAsJSONString) throws JSONException, IllegalArgumentException {
		List<JobApplicationStatus> jobStatuses = new ArrayList<>();
		JSONArray arr = new JSONArray(jobStatusesAsJSONString);
		for(int i = 0 ; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			JobApplicationStatus status = new JobApplicationStatus();
			if(obj.has(KEY_STATUS_ID)) {
				status.setId(obj.getLong(KEY_STATUS_ID));
			}
			if(obj.has(KEY_STATUS_NAME)) {
				status.setStatus(obj.getString(KEY_STATUS_NAME));
			} else {
				LOGGER.log(Level.SEVERE, "Invalid json " + jobStatusesAsJSONString);
				throw new IllegalArgumentException("Invalid job statuses json format " + jobStatusesAsJSONString);
			}
			jobStatuses.add(status);
		}
		return jobStatuses;
	}

	/**
	 * Fetches user's job applicationstatuses
	 * 
	 * @param userID
	 * @return Map of job statusID to Job Status Object for user
	 * @throws SQLException
	 */
	public static Map<Long, JobApplicationStatus> fetchJobApplicationStatusesForUser(Long userID) throws SQLException {
		return DatabaseManager.getInstance().fetchJobApplicationStatusesForUser(userID);
	}
	
	/**
	 * 
	 * Fetches user's job applications' details
	 * 
	 * @param userID
	 * @return List of user's Applications
	 * @throws SQLException
	 */
	public static List<JobApplication> fetchUserJobApplications(Long userID) throws SQLException, IllegalArgumentException {
		return DatabaseManager.getInstance().fetchUserJobApplications(userID);
	}
	
	/**
	 * 
	 * Updates application status, rank.
	 * If oldStatusID and newStatusID is the same, application is reordered within the same status.
	 * All application IDs are fetched and re ranked. 
	 * 
	 * [Note] App ID with higher rank is displayed on top in the UI.
	 * 
	 * @param userID
	 * @param applicationID
	 * @param oldStatusID
	 * @param newStatusID
	 * @param applicationIDs
	 * @throws Exception
	 */
	public static void updateJobApplication(Long userID, Long applicationID, Long newStatusID, Long[] applicationIDs) throws Exception {
		LOGGER.log(Level.INFO, "Updating application " + applicationID + " for user " + userID);
		LOGGER.log(Level.INFO, "Reordering application " + Arrays.toString(applicationIDs));
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JobApplication application = dbManager.fetchJobApplication(applicationID);
		Map<Long, Integer> applicationIDToRankMap = dbManager.fetchUserJobApplicationIDsWithRankForJobStatusID(userID, newStatusID);
		dbManager.beginTransaction();
		try {
			Long oldStatusID = application.getStatusID();
			boolean updateDB = false;
			if(oldStatusID != newStatusID) {
				updateDB = true;
				dbManager.updateJobApplicationStatus(applicationID, newStatusID);
			}
			Map<Long, Integer> appIDToRank = new HashMap<>();
			int rank = 0;
			for(int i = applicationIDs.length-1; i >= 0 ; i--) {
				Long id = applicationIDs[i];
				if(appIDToRank.containsKey(id)) {
					throw new IllegalArgumentException("Application ID repeated multiple time: " + Arrays.toString(applicationIDs));
				}
				rank = rank+1;
				appIDToRank.put(id, rank);
				if(!updateDB && applicationIDToRankMap.get(id) != rank) {
					updateDB = true;
				}
				applicationIDToRankMap.remove(id);
			}
			if(!applicationIDToRankMap.isEmpty()) {
				LOGGER.log(Level.SEVERE, "Input missing all old applications, Can not rerank applications. Remaining app ids to rank map: " + applicationIDToRankMap);
				throw new IllegalArgumentException("Input missing all old applications, Can not rerank applications");
			}
			if(updateDB) {
				dbManager.updateUserJobApplicationsRanks(appIDToRank);
				dbManager.commit();
			} else {
				LOGGER.log(Level.INFO, "[Action: Update Application] Not updating application since new app order same as old order");
			}
			LOGGER.log(Level.INFO, "[Action: Update Application] Successfully Updated Application " + applicationID);
			dbManager.endTransaction();
		} catch(Exception e) {
			dbManager.rollback();
			dbManager.endTransaction();
			throw e;
		}
	}
	
	/**
	 * Adds application to db
	 * Sets rank for application for given status (maxRank + 1)
	 * 
	 * @param application
	 * @throws Exception
	 */
	public static void addJobApplication(JobApplication application) throws Exception {
		LOGGER.log(Level.INFO, "Adding application " + application.getCompanyName() + " for user " + application.getUserID());
		DatabaseManager dbManager = DatabaseManager.getInstance();
		int maxRank = dbManager.fetchUsersMaxRankForStatus(application.getUserID(), application.getStatusID());
		dbManager.beginTransaction();
		try {
			dbManager.addUserJobApplication(application);
			application.setRank(maxRank+1);
			dbManager.addJobApplicationRank(application.getId(), maxRank+1);
			dbManager.commit();
			dbManager.endTransaction();
			LOGGER.log(Level.INFO, "Successfully Added Application " + application.toString());
		} catch(Exception e) {
			dbManager.rollback();
			dbManager.endTransaction();
			throw e;
		}
	}
	
	
//	public Application buildApplicationFromJSONString(String applicationAsJSONString) {
//		Application application = new Application();
//	}

}
