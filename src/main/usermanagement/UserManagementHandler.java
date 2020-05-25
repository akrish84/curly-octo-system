package main.usermanagement;

import main.db.DbUtil;
import main.util.PasswordManager;
import main.util.Response;

public class UserManagementHandler {
	
	public static void signup(User user) throws Exception{
		String passwordHash = PasswordManager.getPasswordHash(user.getPassword());
		user.setPassword(passwordHash);
		DbUtil.addUser(user);
	}
	
	public static Response login(String email, String password) throws Exception {
		return PasswordManager.verifyPassword(email, password);
	}
	
}
