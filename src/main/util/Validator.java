package main.util;

import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import main.beans.JobApplicationStatus;
import main.db.DatabaseManager;

/**
 * Performs input validation
 * 
 * @author akhilesh
 *
 */
public class Validator {

	/**
	 * Checks if given email is of valid email id format
	 * 
	 * @param email
	 * @return True - valid Email ID, False invalid Email ID
	 */
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	public static boolean isNull(Object... obj) {
		for(Object arg : obj) {
			if(arg == null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEmpty(String... args) {
		for(String arg : args) {
			if(arg.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidStatusID(Long statusID, Long userID) {
		try {
			Map<Long, JobApplicationStatus> applicationStatuses = DatabaseManager.getInstance().fetchJobApplicationStatusesForUser(userID);
			if(!applicationStatuses.containsKey(statusID)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
