package main.util;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import main.db.DataManager;

public class PasswordManager {
	
	public static String getPasswordHash(String password) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		return DatatypeConverter.printHexBinary(digest).toUpperCase();
	}
	
	public static Response verifyPassword(String email, String password) throws Exception {
		Response response = new Response();
		String passwordHashFromDB = DataManager.getUserPassword(email);
		if(passwordHashFromDB == null || passwordHashFromDB.isEmpty()) {
			response.setError("Email " + email + " does not exist");
			return response;
		}
		String passwordHash = getPasswordHash(password);
		if(!passwordHash.equals(passwordHashFromDB)) {
			response.setError("Wrong password");
		} else {
			response.setSuccess("Password matched");
		}
		return response;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getPasswordHash("test"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
