package main.authentication;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

public class AuthenticationUtil {
	
// Storing session in db so that when server restarts session can be restored, we have cookie age for 7 days.
// Store session in db and load values from db to map when session starts
private static final Map<String, String> SESSION_ID_TO_USER = new HashMap<>();
	
	
	public static void createSessionForUser(String email) throws Exception {
		String sessionID = SessionIDGenerator.generateID();
		HMACSignature sessionIDsignature = new HMACSignature(sessionID);
		String signedSessionID = sessionIDsignature.sign();
		// Also store value in db
		SESSION_ID_TO_USER.put(signedSessionID, email);
		CookiesHandler.addSessionCookie(signedSessionID, ServletActionContext.getResponse());
	}
	
	public static void deleteCurrentSession() {
		HttpServletRequest request = (HttpServletRequest) ServletActionContext.getRequest();
		String sessionID = CookiesHandler.getSessionID(request);
		CookiesHandler.deleteSessionCookie(sessionID, ServletActionContext.getResponse());
		SESSION_ID_TO_USER.remove(sessionID);
	}
	
	public static boolean validateSession(String sessionID) {
		//HttpSession session = ServletActionContext.getRequest();
		if(sessionID == null || SESSION_ID_TO_USER.get(sessionID) == null) {
			return false;
		} else {
			return true;
		}
	}

	public static String getLoggedInUserEmail() throws FailedLoginException {
		HttpServletRequest request = (HttpServletRequest) ServletActionContext.getRequest();
		HttpSession session = ServletActionContext.getRequest().getSession();
		String email = (String) session.getAttribute(AuthenticationConstants.ATTR_NAME_EMAIL); 
		if(email == null || email.isEmpty()) {
			String sessionID = CookiesHandler.getSessionID(request);
			if(sessionID == null || SESSION_ID_TO_USER.get(sessionID) == null) {
				throw new FailedLoginException("Invalid Session");
			}
			email = SESSION_ID_TO_USER.get(sessionID);
		}
		return email;
	}
}
