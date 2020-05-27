package main.authentication;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationInterceptor implements Interceptor {
	
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(AuthenticationInterceptor.class.getName());

	private boolean isExcludedURL(HttpServletRequest request) {
		String path = request.getRequestURI();
		System.out.println("Path is " + path);
		for(String excludedURL : AuthenticationConstants.EXCLUDED_URLS) {
			if(path.contains(excludedURL)) {
				System.out.println("Excluding");
				return true;
			}
		}
		System.out.println("Not excluding");
		return false;
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation action) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			if(isExcludedURL(request)) {
				return action.invoke();
			}
			String sessionID = CookiesHandler.getSessionID(request);
			if(!AuthenticationUtil.validateSession(sessionID)) {
				// handle invalid session
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
