package main.authentication;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationConstants {
	
	// AuthenticationUtil
	
	public static final String ATTR_NAME_USER_SESSION_ID = "USER_SESSION_ID";
	public static final String ATTR_NAME_EMAIL = "EMAIL";
	public static final int AUTHENTICATION_FAILED_CODE = 601;
	public static final List<String> EXCLUDED_URLS;
	
	
	// HMACSignature
	public static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static final String CONFIG_KEY_HMAC_SHA1 = "hmac.key";
	
	
	// CookiesHandler
	public static final String COOKIE_NAME_SESSION_ID = "SESSION_ID";
	public static final String HTTP_HEADER_SET_COOKIE = "Set-Cookie";
	public static final String COOKIE_FLAG_HTTP_ONLY = "HttpOnly";
	
	
	// AuthenticationInterceptor
	
	public static final String ACTION_AUTH_ERROR = "auth_error";
	
	
	// Authentication Filter
	static {
		EXCLUDED_URLS = new ArrayList<>();
		EXCLUDED_URLS.add("jsp");
		EXCLUDED_URLS.add("css");
		EXCLUDED_URLS.add("js");
		EXCLUDED_URLS.add("html");
		EXCLUDED_URLS.add("signup");
		EXCLUDED_URLS.add("login");
		EXCLUDED_URLS.add("header");
		EXCLUDED_URLS.add("footer");
	}

}
