package main.application;

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
import main.beans.ApplicationStatus;
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
	 * Adds default statuses (WISHLIST, APPLIED, INTERVIEW, OFFER) for user.
	 * 
	 * @param userID
	 * @throws Exception
	 */
	
	public static void addDefaultOptionsForUser(Long userID) throws SQLException {
		List<ApplicationStatus> statuses = new ArrayList<>();
		ApplicationStatus wishlist = new ApplicationStatus();
		wishlist.setStatus(DefaultApplicationStatuses.WISHLIST.getStatus());
		wishlist.setRank(DefaultApplicationStatuses.WISHLIST.getRank());
		
		ApplicationStatus applied = new ApplicationStatus();
		applied.setStatus(DefaultApplicationStatuses.APPLIED.getStatus());
		applied.setRank(DefaultApplicationStatuses.APPLIED.getRank());
		
		ApplicationStatus interview = new ApplicationStatus();
		interview.setStatus(DefaultApplicationStatuses.INTERVIEW.getStatus());
		interview.setRank(DefaultApplicationStatuses.INTERVIEW.getRank());
		
		ApplicationStatus offer = new ApplicationStatus();
		offer.setStatus(DefaultApplicationStatuses.OFFER.getStatus());
		offer.setRank(DefaultApplicationStatuses.OFFER.getRank());
		
		statuses.add(wishlist);
		statuses.add(applied);
		statuses.add(interview);
		statuses.add(offer);
		LOGGER.log(Level.INFO, "Adding default statuses " + statuses + " for user " + userID);
		DatabaseManager.getInstance().addStatusesForUser(statuses, userID);
	}
	
	/**
	 * Adds status for user. Rank of status is assigned -> previous max rank + DEFAULT_RANK_GAP (1024)
	 * 
	 * @param status
	 * @param userID
	 * @throws SQLException
	 */
	public static void addStatusForUser(ApplicationStatus status, Long userID) throws SQLException {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		int maxStatusRank = dbManager.fetchMaxStatusRankForUser(userID);
		status.setRank(maxStatusRank + ApplicationStatus.DEFAULT_RANK_GAP);
		List<ApplicationStatus> statuses = new ArrayList<>();
		statuses.add(status);
		LOGGER.log(Level.INFO, "Adding status " + status.getStatus() + " for user " + userID);
		dbManager.addStatusesForUser(statuses, userID);
		LOGGER.log(Level.INFO, "Successful");
	}
	
	
	/**
	 * Updates status for user
	 * @param status
	 * @param userID
	 * @throws SQLException
	 */
	public static void updateStatusForUser(ApplicationStatus status , Long userID) throws SQLException, IllegalArgumentException {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		ApplicationStatus existingStatus = dbManager.fetchStatus(userID, status.getId());
		if(existingStatus == null) {
			throw new IllegalArgumentException("Status with id " + status.getId() + " does not exist for user " + userID);
		}
		String oldStatus = existingStatus.getStatus();
		existingStatus.setStatus(status.getStatus());
//		List<ApplicationStatus> statuses = new ArrayList<>();
//		statuses.add(existingStatus);
		LOGGER.log(Level.INFO, "Updating status from " + oldStatus + " to " + status.getStatus() + " for user " + userID);
		dbManager.updateStatusForUser(existingStatus, userID);
		LOGGER.log(Level.INFO, "Successful");
	}
	
	/**
	 * All status is Re Ranked.
	 * New Statuses are inserted.
	 * Existing statuses are updated.
	 * statuses list should include all existingStatuses for that user.
	 * @param statuses
	 * @param userID
	 * @throws SQLException
	 * @throws JSONException 
	 */
	public static void updateStatusesForUser(String stasusesAsJSONString, Long userID) throws SQLException, JSONException, IllegalArgumentException {
		List<ApplicationStatus> statuses = buildStatusesFromJSONString(stasusesAsJSONString);
		DatabaseManager dbManager = DatabaseManager.getInstance();
		int rank = 0;
		List<ApplicationStatus> addStatuses = new ArrayList<>();
		List<ApplicationStatus> updateStatuses = new ArrayList<>();
		Map<Long, ApplicationStatus> existingStatuses = dbManager.fetchApplicationStatusesForUser(userID);
		for(ApplicationStatus status : statuses) {
			status.setRank(rank);
			rank += ApplicationStatus.DEFAULT_RANK_GAP;
			if(status.getId() == null || !existingStatuses.containsKey(status.getId())) {
				addStatuses.add(status);
			} else {
				updateStatuses.add(status);
				existingStatuses.remove(status.getId());
			}
		}
		if(!existingStatuses.isEmpty()) {
			LOGGER.log(Level.SEVERE, "Missing old statuses " + existingStatuses);
			throw new IllegalArgumentException("Input missing all old statuses, Can not rerank statuses");
		}
		dbManager.beginTransaction();
		try {
			LOGGER.log(Level.INFO, "Adding statuses " + addStatuses + " for user " + userID);
			dbManager.addStatusesForUser(addStatuses, userID);
			LOGGER.log(Level.INFO, "Added successfully");
			LOGGER.log(Level.INFO, "Updating statuses " + addStatuses + " for user " + userID);
			dbManager.updateStatusesForUser(updateStatuses, userID);
			LOGGER.log(Level.INFO, "Updated successfully");
			dbManager.commit();
		} catch(SQLException e) {
			dbManager.rollback();
			dbManager.endTransaction();
			throw e;
		}
		dbManager.endTransaction();
	}
	
	private static List<ApplicationStatus> buildStatusesFromJSONString(String statusesAsJSONString) throws JSONException, IllegalArgumentException {
		List<ApplicationStatus> statuses = new ArrayList<>();
		JSONArray arr = new JSONArray(statusesAsJSONString);
		for(int i = 0 ; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			ApplicationStatus status = new ApplicationStatus();
			if(obj.has(KEY_STATUS_ID)) {
				status.setId(obj.getLong(KEY_STATUS_ID));
			}
			if(obj.has(KEY_STATUS_NAME)) {
				status.setStatus(obj.getString(KEY_STATUS_NAME));
			} else {
				LOGGER.log(Level.SEVERE, "Invalid json " + statusesAsJSONString);
				throw new IllegalArgumentException("Invalid statusesAsJsonString input " + statusesAsJSONString);
			}
			statuses.add(status);
		}
		return statuses;
	}

	/**
	 * Fetches user's statuses
	 * 
	 * @param userID
	 * @return Map of statusID to ApplicationStatus for user
	 * @throws SQLException
	 */
	public static Map<Long, ApplicationStatus> fetchApplicationStatusesForUser(Long userID) throws SQLException {
		return DatabaseManager.getInstance().fetchApplicationStatusesForUser(userID);
	}
	
	/**
	 * 
	 * Fetches user's job applications' details
	 * 
	 * @param userID
	 * @return List of user's Applications
	 * @throws SQLException
	 */
	public static List<JobApplication> fetchUserApplications(Long userID) throws SQLException, IllegalArgumentException {
		return DatabaseManager.getInstance().fetchUserApplications(userID);
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
	public static void updateApplication(Long userID, Long applicationID, Long newStatusID, Long[] applicationIDs) throws Exception {
		LOGGER.log(Level.INFO, "Updating application " + applicationID + " for user " + userID);
		LOGGER.log(Level.INFO, "Reordering application " + Arrays.toString(applicationIDs));
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JobApplication application = dbManager.fetchApplication(applicationID);
		Map<Long, Integer> applicationIDToRankMap = dbManager.fetchUserApplicationIDSWithRankHavingStatusID(userID, newStatusID);
		dbManager.beginTransaction();
		try {
			Long oldStatusID = application.getStatusID();
			boolean updateDB = false;
			if(oldStatusID != newStatusID) {
				updateDB = true;
				dbManager.updateApplicationStatus(applicationID, newStatusID);
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
				dbManager.updateUserApplicationsRanks(appIDToRank);
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
	public static void addApplication(JobApplication application) throws Exception {
		LOGGER.log(Level.INFO, "Adding application " + application.getCompanyName() + " for user " + application.getUserID());
		DatabaseManager dbManager = DatabaseManager.getInstance();
		int maxRank = dbManager.fetchUsersMaxRankForStatus(application.getUserID(), application.getStatusID());
		dbManager.beginTransaction();
		try {
			dbManager.addUserApplication(application);
			application.setRank(maxRank+1);
			dbManager.addApplicationRank(application.getId(), maxRank+1);
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
