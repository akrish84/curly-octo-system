package main.authentication;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

/**
 * Handles User Authentication 
 * 
 * @author akhilesh
 *
 */

public class SessionHandler {

	
// Storing session in db so that when server restarts session can be restored, we have cookie age for 7 days.
// Store session in db and load values from db to map when session starts
private static final Map<String, Long> SESSION_ID_TO_USER = new HashMap<>();
	
	
	/**
	 * Creates a session for the given userID id
	 * A random session id is generate, It is signed with a private key.
	 * the signed session id is then added to the cookie. 
	 * The session id is mapped to a user and is stored in SESSION_ID_TO_USER.
	 * @param userID
	 * @throws Exception
	 */
	public static void createSessionForUser(Long userID) throws Exception {
		String sessionID = null;
		for(int i = 0 ; sessionID == null && i < AuthenticationConstants.SESSION_ID_GENERATION_MAX_ATTEMPT; i++) {
			sessionID = SessionIDGenerator.generateID();
		}
		if(sessionID == null) {
			throw new Exception("Session ID Creation reached max attempt");
		}
		HMACSignature sessionIDsignature = new HMACSignature(sessionID);
		String signedSessionID = sessionIDsignature.sign();
		// Also store value in db
		SESSION_ID_TO_USER.put(signedSessionID, userID);
		CookiesHandler.addSessionCookie(signedSessionID, ServletActionContext.getResponse());
	}
	
	/**
	 * Deletes the logged in user's session by removing the user's cookie.
	 * Cookie is removed from the request and from the SESSION_ID_TO_USER map.
	 */
	public static void logoutCurrentSession() {
		HttpServletRequest request = (HttpServletRequest) ServletActionContext.getRequest();
		String sessionID = getSessionID(request);
		CookiesHandler.deleteSessionCookie(sessionID, ServletActionContext.getResponse());
		SESSION_ID_TO_USER.remove(sessionID);
	}
	
	/**
	 * Returns the session id from the sessionCookie from the request
	 * @param request
	 * @return
	 */
	public static String getSessionID(HttpServletRequest request) {
		String sessionID = CookiesHandler.getEncodedSessionFromCookie(request);
		return sessionID;
	}
	
	/**
	 * Validates current session 
	 * @param request
	 * @return true if current session is valid, false if current session is invalid
	 */
	public static boolean validateCurrentSession(HttpServletRequest request) {
		String sessionID = getSessionID(request);
		return validateSession(sessionID);
	}
	
	/**
	 * Validates session by checking if the given sessionID is present in the SESSION_ID_TO_USER map.
	 * All valid sessions are present in SESSION_ID_TO_USER.
	 * @param sessionID
	 * @return true - valid session, false - invalid session
	 */
	public static boolean validateSession(String sessionID) {
		if(sessionID == null || SESSION_ID_TO_USER.get(sessionID) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * Checks sessionID stored in request is valid.
	 * userID mapped to sessionID in SESSION_ID_TO_USER is returned.
	 * 
	 * @return userID - ID of logged in user, else null if user is not logged in.
	 */
	public static Long getLoggedInUserID() {
		HttpServletRequest request = (HttpServletRequest) ServletActionContext.getRequest();
		String sessionID = getSessionID(request);
		if(sessionID == null || SESSION_ID_TO_USER.get(sessionID) == null) {
			return null;
		}
		return SESSION_ID_TO_USER.get(sessionID);
	}
	
//	/**
//	 * URL Encodes the value
//	 * @param value
//	 * @return URL encoded value
//	 */
//	private static String encodeValue(String value) {
//		try {
//			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException(e.getCause());
//		}
//	}
//
//	/**
//	 * Performs URL Decoding
//	 * @param value
//	 * @return URL Decoded value
//	 */
//	private static String decodeValue(String value) {
//		try {
//			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException(e.getCause());
//		}
//	}
}
