package main.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.beans.Application;
import main.beans.ApplicationStatus;
import main.db.DataManager;

/**
 * Handler class to handle all operations regarding user's job application
 * 
 * @author akhilesh
 *
 */

public class ApplicationHandler {
	
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
		
		DataManager.addStatusesForUser(statuses, userID);
	}
	
	/**
	 * Fetches user's statuses
	 * 
	 * @param userID
	 * @return Map of statusID to ApplicationStatus for user
	 * @throws SQLException
	 */
	public static Map<Long, ApplicationStatus> fetchApplicationStatusesForUser(Long userID) throws SQLException {
		return DataManager.fetchApplicationStatusesForUser(userID);
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
		return DataManager.fetchUserApplications(userID);
	}

}
