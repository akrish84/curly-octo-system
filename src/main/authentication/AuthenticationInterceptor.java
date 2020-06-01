package main.authentication;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Struts interceptor to authenticate every requests.
 * It excludes urls added to EXCLUDED_URLS list.
 * It checks if the request has a valid session cookie set.
 * 
 * @author akhilesh
 *
 */
public class AuthenticationInterceptor implements Interceptor {
	
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(AuthenticationInterceptor.class.getName());

	/**
	 * Chcecks if url should be excluded from authentication.
	 * Links which don't need authentication are excluded (html/jsp/login_page/signup_page etc) 
	 * @param request
	 * @return
	 */
	private boolean isExcludedURL(HttpServletRequest request) {
		String path = request.getRequestURI();
		for(String excludedURL : AuthenticationConstants.EXCLUDED_URLS) {
			if(path.contains(excludedURL)) {
				LOGGER.log(Level.FINE, "Excluding url " + path);
				return true;
			}
		}
		return false;
	}
	@Override
	public void destroy() {
	}

	@Override
	public void init() {
		
	}

	@Override
	public String intercept(ActionInvocation action) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			if(isExcludedURL(request)) {
				return action.invoke();
			}
			if(!AuthenticationHandler.validateCurrentSession(request)) {
				throw new Exception("Invalid Session");
			}
			LOGGER.log(Level.INFO, "Valid Session");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Invalid Session" , e);
			response.setHeader("response", "Unauthenticated");
			return AuthenticationConstants.ACTION_AUTH_ERROR;
		}
		return action.invoke();
	}
	

}
