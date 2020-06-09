package main.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.beans.Application;
import main.beans.ApplicationStatus;
import main.db.DatabaseManager;

/**
 * Handler class to handle all operations regarding user's job application
 * 
 * @author akhilesh
 *
 */

public class ApplicationHandler {
	
	private static Logger LOGGER = Logger.getLogger(ApplicationHandler.class.getName());
	
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
		new DatabaseManager().addStatusesForUser(statuses, userID);
	}
	
	/**
	 * Adds status for user. Rank of status is assigned -> previous max rank + DEFAULT_RANK_GAP (1024)
	 * 
	 * @param status
	 * @param userID
	 * @throws SQLException
	 */
	public static void addStatusForUser(ApplicationStatus status, Long userID) throws SQLException {
		DatabaseManager dbManager = new DatabaseManager();
		int maxStatusRank = dbManager.fetchMaxStatusRankForUser(userID);
		status.setRank(maxStatusRank + ApplicationStatus.DEFAULT_RANK_GAP);
		List<ApplicationStatus> statuses = new ArrayList<>();
		statuses.add(status);
		LOGGER.log(Level.INFO, "Adding status " + status.getStatus() + " for user " + userID);
		dbManager.addStatusesForUser(statuses, userID);
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
	public static void updateStatusesForUser(String stasusesAsJSONString, Long userID) throws SQLException, JSONException {
		List<ApplicationStatus> statuses = buildStatusesFromJSONString(stasusesAsJSONString);
		DatabaseManager dbManager = new DatabaseManager();
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
			throw new IllegalArgumentException("Input missing all old statuses, Cannot rerank statuses");
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
			throw e;
		}
		dbManager.endTransaction();
	}
	
	private static List<ApplicationStatus> buildStatusesFromJSONString(String statusesAsJSONString) throws JSONException {
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
		return new DatabaseManager().fetchApplicationStatusesForUser(userID);
	}
	
	/**
	 * 
	 * Fetches user's job applications' details
	 * 
	 * @param userID
	 * @return List of user's Applications
	 * @throws SQLException
	 */
	public static List<Application> fetchUserApplications(Long userID) throws SQLException {
		return new DatabaseManager().fetchUserApplications(userID);
	}

}
