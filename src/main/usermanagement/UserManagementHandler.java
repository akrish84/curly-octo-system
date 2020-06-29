package main.usermanagement;


import java.util.logging.Level;
import java.util.logging.Logger;

import main.application.ApplicationHandler;
import main.authentication.SessionHandler;
import main.db.DatabaseManager;
import main.util.PasswordManager;
import main.beans.*;

/**
 * Handler class to perform UserManagement operations
 * @author akhilesh
 *
 */
public class UserManagementHandler {
	
	private static Logger LOGGER = Logger.getLogger(UserManagementHandler.class.getName());
	
	/**
	 * 
	 * Stores user details in db.
	 * password is hashed before saving.
	 * Adds default statuses for user
	 * 
	 * @param user
	 * @throws Exception
	 */
	public static void signup(User user) throws Exception{
		String passwordHash = PasswordManager.getPasswordHash(user.getPassword());
		user.setPassword(passwordHash);
		DatabaseManager.getInstance().addUser(user);
		try {
			ApplicationHandler.addDefaultOptionsForUser(user.getId());
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to add default statuses for user " + user.getId(), e);
		}
	}
	
	/**
	 * Logs in user if password matches db-stored password and returns true
	 * If password does not match, returns false
	 * @param user
	 * @param password
	 * @return true if password matches db-stored password else returns false
	 * @throws Exception
	 */
	public static boolean login(User user, String password) throws Exception {
		if(!PasswordManager.verifyPassword(user, password)) {
			return false;
		}
		SessionHandler.createSessionForUser(user.getId());
		return true;
	}
	
	public static void logout() {
		SessionHandler.logoutCurrentSession();
	}
	
}
