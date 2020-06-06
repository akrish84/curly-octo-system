package main.util;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import main.beans.User;

public class PasswordManager {
	
	public static String getPasswordHash(String password) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		return DatatypeConverter.printHexBinary(digest).toUpperCase();
	}
	
	public static boolean verifyPassword(User user, String password) throws Exception {
		String passwordHashFromDB = user.getPassword();
		if(passwordHashFromDB == null) {
			return false;
		}
		String passwordHash = getPasswordHash(password);
		if(!passwordHash.equals(passwordHashFromDB)) {
			return false;
		}
		return true;
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
