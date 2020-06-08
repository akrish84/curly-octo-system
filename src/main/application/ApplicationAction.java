package main.application;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.FailedLoginException;

import com.opensymphony.xwork2.Action;

import main.authentication.AuthenticationConstants;
import main.authentication.SessionHandler;
import main.beans.ApplicationStatus;

public class ApplicationAction {
	
	private static Logger LOGGER = Logger.getLogger(ApplicationAction.class.getName());
	
	
	private Map<Long, ApplicationStatus> statuses;

	public String getUserStatuses() {
		Long userID = null;
		try {
			userID = SessionHandler.getLoggedInUserID();
		} catch(FailedLoginException e) {
			LOGGER.log(Level.SEVERE, "Unauthenticated access");
			return AuthenticationConstants.ACTION_AUTH_ERROR;
		}
		try {
			statuses = ApplicationHandler.getApplicationStatusesForUser(userID);
		}catch(SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch statuses for user: ", e);
		}
		return Action.SUCCESS;
	}
	
	public String pageDispatcher() {
		return Action.SUCCESS;
	}

	public Map<Long, ApplicationStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(Map<Long, ApplicationStatus> statuses) {
		this.statuses = statuses;
	}

}