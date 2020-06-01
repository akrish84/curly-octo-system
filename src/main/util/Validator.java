package main.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

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

}
