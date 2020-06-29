package main.application;

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
import main.util.Validator;

public class ApplicationAction {
	
	private static Logger LOGGER = Logger.getLogger(ApplicationAction.class.getName());
	
	
	private Map<Long, ApplicationStatus> statusesMap;
	private List<Application> applications;
	private String statuses;
	private ApplicationStatus status;
	private String responseMessage;
	private Long applicationID;
	private Long oldStatusID;
	private Long newStatusID;
	private Long[] applicationIDs;
	
	private String companyName;
	private String jobTitle;
	private String jobDescription;
	private String aps;
	private String appliedDate;
	private Long statusID;
	

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
		}catch(Exception e) {
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
		}catch(Exception e) {
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
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to add status for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to add status for user");
		}
		return Action.SUCCESS;
		
	}
	
	public String updateStatus() {
		Long userID = null;
		try {
			userID = SessionHandler.getLoggedInUserID();
		} catch(FailedLoginException e) {
			LOGGER.log(Level.SEVERE, "Unauthenticated access");
			return AuthenticationConstants.ACTION_AUTH_ERROR;
		}
		try {
			if(Validator.isNull(status, status.getId(), status.getStatus()) || status.getStatus().isEmpty()) {
				throw new IllegalArgumentException();
			}
			ApplicationHandler.updateStatusForUser(status, userID);
		} catch(Exception e) {
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
			LOGGER.log(Level.SEVERE, "Failed to add status for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to update statuses");
		}
		return Action.SUCCESS;
		
	}
	
	public String updateApplication() {
		Long userID = null;
		try {
			userID = SessionHandler.getLoggedInUserID();
		} catch(FailedLoginException e) {
			LOGGER.log(Level.SEVERE, "Unauthenticated access");
			return AuthenticationConstants.ACTION_AUTH_ERROR;
		}
		try {
			if(Validator.isNull(applicationID, oldStatusID, newStatusID, applicationIDs)) {
				throw new IllegalArgumentException();
			}
			ApplicationHandler.updateApplication(userID, applicationID, oldStatusID, newStatusID, applicationIDs);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to update application " + applicationID + " for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to update application");
		}
		return Action.SUCCESS;
	}

	public String addApplication() {
		Long userID = null;
		try {
			userID = SessionHandler.getLoggedInUserID();
		} catch(FailedLoginException e) {
			LOGGER.log(Level.SEVERE, "Unauthenticated access");
			return AuthenticationConstants.ACTION_AUTH_ERROR;
		}
		try {
			Application application = new Application();
			application.setCompanyName(companyName);
			application.setJobTitle(jobTitle);
			application.setJobDescription(jobDescription);
			application.setAps(aps);
			application.setAppliedDate(appliedDate);
			application.setStatusID(statusID);
			application.setUserID(userID);
			
			if(Validator.isNull(application.getCompanyName(), application.getJobTitle(), application.getJobDescription(), application.getAps(), application.getAppliedDate(), application.getStatusID())) {
				throw new IllegalArgumentException();
			}
			if(Validator.isEmpty(application.getCompanyName(), application.getJobTitle(), application.getJobDescription(), application.getAps(), application.getAppliedDate())) {
				throw new IllegalArgumentException();
			}
			if(!Validator.isValidStatusID(application.getStatusID(), userID)) {
				throw new IllegalArgumentException();
			}
			ApplicationHandler.addApplication(application);
			responseMessage= Utils.getSuccessMessage("Successfully added application");
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to add application for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to add application");
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

	public Long getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(Long applicationID) {
		this.applicationID = applicationID;
	}

	public Long getOldStatusID() {
		return oldStatusID;
	}

	public void setOldStatusID(Long oldStatusID) {
		this.oldStatusID = oldStatusID;
	}

	public Long getNewStatusID() {
		return newStatusID;
	}

	public void setNewStatusID(Long newStatusID) {
		this.newStatusID = newStatusID;
	}

	public Long[] getApplicationIDs() {
		return applicationIDs;
	}

	public void setApplicationIDs(Long[] applicationIDs) {
		this.applicationIDs = applicationIDs;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getAps() {
		return aps;
	}

	public void setAps(String aps) {
		this.aps = aps;
	}

	public String getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(String appliedDate) {
		this.appliedDate = appliedDate;
	}

	public Long getStatusID() {
		return statusID;
	}

	public void setStatusID(Long statusID) {
		this.statusID = statusID;
	}

}