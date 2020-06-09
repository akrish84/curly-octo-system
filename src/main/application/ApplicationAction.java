package main.application;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.FailedLoginException;

import com.opensymphony.xwork2.Action;

import main.authentication.AuthenticationConstants;
import main.authentication.SessionHandler;
import main.beans.Application;
import main.beans.ApplicationStatus;
import main.util.Utils;

public class ApplicationAction {
	
	private static Logger LOGGER = Logger.getLogger(ApplicationAction.class.getName());
	
	
	private Map<Long, ApplicationStatus> statusesMap;
	private List<Application> applications;
	private String statuses;
	private ApplicationStatus status;
	private String responseMessage;
	
	

	public String fetchUserStatuses() {
		Long userID = null;
		try {
			userID = SessionHandler.getLoggedInUserID();
		} catch(FailedLoginException e) {
			LOGGER.log(Level.SEVERE, "Unauthenticated access");
			return AuthenticationConstants.ACTION_AUTH_ERROR;
		}
		try {
			statusesMap = ApplicationHandler.fetchApplicationStatusesForUser(userID);
		}catch(SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch statuses for user for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to fetch user statuses");
		}
		return Action.SUCCESS;
	}

	public String fetchUserApplications() {
		Long userID = null;
		try {
			userID = SessionHandler.getLoggedInUserID();
		} catch(FailedLoginException e) {
			LOGGER.log(Level.SEVERE, "Unauthenticated access");
			return AuthenticationConstants.ACTION_AUTH_ERROR;
		}
		try {
			applications = ApplicationHandler.fetchUserApplications(userID);
		}catch(SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch applications for user for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to fetch user applications");
		}
		return Action.SUCCESS;
	}
	
	
	public String addStatus() {
		Long userID = null;
		try {
			userID = SessionHandler.getLoggedInUserID();
		} catch(FailedLoginException e) {
			LOGGER.log(Level.SEVERE, "Unauthenticated access");
			return AuthenticationConstants.ACTION_AUTH_ERROR;
		}
		try {
			if(status == null) {
				throw new IllegalArgumentException();
			}
			ApplicationHandler.addStatusForUser(status, userID);
		} catch(SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to add status for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to add status for user");
		}
		return Action.SUCCESS;
		
	}
	
	public String updateStatuses() {
		Long userID = null;
		try {
			userID = SessionHandler.getLoggedInUserID();
		} catch(FailedLoginException e) {
			LOGGER.log(Level.SEVERE, "Unauthenticated access");
			return AuthenticationConstants.ACTION_AUTH_ERROR;
		}
		try {
			if(statuses == null) {
				throw new IllegalArgumentException();
			}
			ApplicationHandler.updateStatusesForUser(statuses, userID);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to add status for user for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to update statuses");
		}
		return Action.SUCCESS;
		
	}

	public String pageDispatcher() {
		return Action.SUCCESS;
	}

	public Map<Long, ApplicationStatus> getStatusesMap() {
		return statusesMap;
	}

	public void setStatusesMap(Map<Long, ApplicationStatus> statusesMap) {
		this.statusesMap = statusesMap;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getStatuses() {
		return statuses;
	}

	public void setStatuses(String statuses) {
		this.statuses = statuses;
	}

}