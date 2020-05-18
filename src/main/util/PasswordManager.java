package main.util;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class PasswordManager {
	
	public static String getPasswordHash(String password) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		return DatatypeConverter.printHexBinary(digest).toUpperCase();
	}
	
	public static boolean verifyPassword(String hash, String password) throws Exception {
		String passwordHash = getPasswordHash(password);
		return passwordHash.equals(hash);
	}

	
}
