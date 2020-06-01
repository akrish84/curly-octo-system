package main.authentication;

/**
 * Generates a random 8 character session id
 * 
 * @author akhilesh
 *
 */
public class SessionIDGenerator {
	public static String generateID() {
		int n = 8;
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random()); 
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

}
