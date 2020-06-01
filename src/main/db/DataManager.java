package main.db;

import main.db.tables.UsersTable;
import main.usermanagement.User;

/**
 * 
 * A Facade class which handles all database operations.
 * Operations are forwarded to its respective Table class to process the operation
 * @author akhilesh
 *
 */
public class DataManager {
	
	/**
	 * Forwards request to User Table to add user.
	 * @param user
	 * @throws Exception
	 */
	public static void addUser(User user) throws Exception {
		UsersTable.add(user);
	}

	/**
	 * Forwards request to User Table to fetch user password.
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static String getUserPassword(String email) throws Exception {
		return UsersTable.getUserPassword(email);
	}
}
