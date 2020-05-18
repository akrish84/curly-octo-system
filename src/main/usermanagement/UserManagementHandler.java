package main.usermanagement;

import main.db.DbUtil;
import main.util.PasswordManager;

public class UserManagementHandler {
	
	public static void signup(User user) throws Exception{
		String passwordHash = PasswordManager.getPasswordHash(user.getPassword());
		user.setPassword(passwordHash);
		DbUtil.addUser(user);
	}
}