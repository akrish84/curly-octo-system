package main.db;

import main.usermanagement.User;

public class DbUtil {
	
	public static void addUser(User user) throws Exception {
		UsersTable.add(user);
	}

}
