package main.authentication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.NewCookie;

public class CookiesHandler {

	public static void addSessionCookie(String sessionID, HttpServletResponse response) {
		String encodedSessionID = encodeValue(sessionID);
		Cookie sessionIDCookie = new Cookie(AuthenticationConstants.COOKIE_NAME_SESSION_ID, encodedSessionID);
		sessionIDCookie.setMaxAge(604800);
		response.addHeader("SET-COOKIE", getCookieWithHttpOnlyAsString(sessionIDCookie));
	}

	public static String getSessionID(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().contentEquals(AuthenticationConstants.COOKIE_NAME_SESSION_ID)) {
				return decodeValue(cookie.getValue());
			}
		}
		return null;
	}
	
	public static void deleteSessionCookie(String sessionID, HttpServletResponse response) {
		Cookie sessionIDCookie = new Cookie(AuthenticationConstants.COOKIE_NAME_SESSION_ID, sessionID);
		sessionIDCookie.setMaxAge(604800);
		response.addHeader("SET-COOKIE", getCookieWithHttpOnlyAsString(sessionIDCookie));
	}

	private static String encodeValue(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getCause());
		}
	}

	private static String decodeValue(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getCause());
		}
	}

	private static String getCookieWithHttpOnlyAsString(Cookie cookie) {
		return new NewCookie(cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain(),
				cookie.getVersion(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure()).toString() + ";"
				+ AuthenticationConstants.COOKIE_FLAG_HTTP_ONLY;
	}
}
