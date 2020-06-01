package main.usermanagement;

import main.db.DataManager;
import main.util.PasswordManager;
import main.util.Response;

/**
 * Handler class to perform UserManagement operations
 * @author akhilesh
 *
 */
public class UserManagementHandler {
	
	/**
	 * 
	 * Stores user details in db.
	 * password is hased before saving.
	 * 
	 * @param user
	 * @throws Exception
	 */
	public static void signup(User user) throws Exception{
		String passwordHash = PasswordManager.getPasswordHash(user.getPassword());
		user.setPassword(passwordHash);
		DataManager.addUser(user);
	}
	
	/**
	 * Verifies if user is logged in.
	 * Login status with message is returned as Response object.
	 * 
	 * @param email
	 * @param password
	 * @return Response: true -> login success, false -> login failed
	 * @throws Exception
	 */
	public static Response login(String email, String password) throws Exception {
		return PasswordManager.verifyPassword(email, password);
	}
	
}
