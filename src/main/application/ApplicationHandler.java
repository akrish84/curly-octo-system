package main.application;

import java.util.ArrayList;
import java.util.List;

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
	
	public static void addDefaultOptionsForUser(Long userID) throws Exception {
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

}
