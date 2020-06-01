package main.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.NewCookie;

/**
 * Handles all operations on HttpCookies
 * 
 * @author akhilesh
 *
 */
public class CookiesHandler {

	/**
	 * Creates a session cookie.
	 * Cookie attributes 
	 * 	:age (7 days), 
	 * 	:http_only (To prevent access to cookie from java script).
	 * Session ID is 
	 * @param sessionID
	 * @param response
	 */
	public static void addSessionCookie(String encodedSessionID, HttpServletResponse response) {
		Cookie sessionIDCookie = new Cookie(AuthenticationConstants.COOKIE_NAME_SESSION_ID, encodedSessionID);
		sessionIDCookie.setMaxAge(604800);
		response.addHeader("SET-COOKIE", getCookieWithHttpOnlyAsString(sessionIDCookie));
	}

	/**
	 * Session is stored in encoded format.
	 * function returns the encoded session id stored in the session cookie
	 * @param request
	 * @return sessionID stored in HttpCookie
	 */
	public static String getEncodedSessionFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().contentEquals(AuthenticationConstants.COOKIE_NAME_SESSION_ID)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * Deletes sessionID from HttpCookie by setting age to 0
	 * @param sessionID
	 * @param response
	 */
	public static void deleteSessionCookie(String sessionID, HttpServletResponse response) {
		Cookie sessionIDCookie = new Cookie(AuthenticationConstants.COOKIE_NAME_SESSION_ID, sessionID);
		sessionIDCookie.setMaxAge(0);
		response.addHeader("SET-COOKIE", getCookieWithHttpOnlyAsString(sessionIDCookie));
	}

	/**
	 * Since HttpCookie class does not have function to add http_only flag,
	 * using javax.ws.rs.core.NewCookie to get a cookie in string format
	 * http_only flag attribute is added to string format of cookie.
	 * 
	 * @param cookie
	 * @return Cookie as string with http_only flag
	 */
	private static String getCookieWithHttpOnlyAsString(Cookie cookie) {
		return new NewCookie(cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain(),
				cookie.getVersion(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure()).toString() + ";"
				+ AuthenticationConstants.COOKIE_FLAG_HTTP_ONLY;
	}
}
