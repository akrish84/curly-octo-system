package main.authentication;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationFilter implements Filter {
	private static Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletReq, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletReq;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		try {
			if(isExcludedURL(request)) {
				chain.doFilter(servletReq, servletResponse);
			}
			String sessionID = CookiesHandler.getSessionID(request);
			if(!AuthenticationUtil.validateSession(sessionID)) {
				// handle invalid session
				throw new Exception("Invalid Session");
			} else {
				
			}
			chain.doFilter(servletReq, servletResponse);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error" , e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Failed to authenticate user");
		}
		
	}

	private boolean isExcludedURL(HttpServletRequest request) {
		String path = request.getRequestURI();
		for(String excludedURL : AuthenticationConstants.EXCLUDED_URLS) {
			if(path.contains(excludedURL)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	

}
