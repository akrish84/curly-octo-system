package main.application;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.opensymphony.xwork2.Action;

import main.authentication.SessionHandler;
import main.beans.JobApplication;
import main.beans.ApplicationStatus;
import main.util.Utils;
import main.util.Validator;

public class ApplicationAction {
	
	private static Logger LOGGER = Logger.getLogger(ApplicationAction.class.getName());
	
	
	private Map<Long, ApplicationStatus> statusesMap;
	private List<JobApplication> applications;
	private String statuses;
	private ApplicationStatus status;
	private String responseMessage;
	private Long applicationID;
	private Long newStatusID;
	private Long[] applicationIDs;
	
	private String companyName;
	private String jobTitle;
	private String jobDescription;
	private String aps;
	private String appliedDate;
	private Long statusID;
	

	public String fetchUserStatuses() {
		Long userID = 0l;
		try {
			userID = SessionHandler.getLoggedInUserID();
			statusesMap = JobApplicationHandler.fetchApplicationStatusesForUser(userID);
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch statuses for user for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to fetch user statuses");
		}
		return Action.SUCCESS;
	}

	public String fetchUserApplications() {
		Long userID = 0l;
		try {
			userID = SessionHandler.getLoggedInUserID();
			applications = JobApplicationHandler.fetchUserApplications(userID);
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch applications for user for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to fetch user applications");
		}
		return Action.SUCCESS;
	}
	
	
	public String addStatus() {
		Long userID = 0l;
		try {
			userID = SessionHandler.getLoggedInUserID();
			if(status == null) {
				throw new IllegalArgumentException();
			}
			JobApplicationHandler.addStatusForUser(status, userID);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to add status for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to add status for user");
		}
		return Action.SUCCESS;
		
	}
	
	public String updateStatus() {
		Long userID = 0l;
		try {
			userID = SessionHandler.getLoggedInUserID();
			if(Validator.isNull(status, status.getId(), status.getStatus()) || status.getStatus().isEmpty()) {
				throw new IllegalArgumentException();
			}
			JobApplicationHandler.updateStatusForUser(status, userID);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to add status for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to add status for user");
		}
		return Action.SUCCESS;
		
	}
	
	public String updateStatuses() {
		Long userID = 0l;
		try {
			userID = SessionHandler.getLoggedInUserID();
			if(statuses == null) {
				throw new IllegalArgumentException();
			}
			JobApplicationHandler.updateStatusesForUser(statuses, userID);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to add status for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to update statuses");
		}
		return Action.SUCCESS;
		
	}
	
	public String updateApplication() {
		Long userID = 0l;
		try {
			userID = SessionHandler.getLoggedInUserID();
			if(Validator.isNull(applicationID, newStatusID, applicationIDs)) {
				throw new IllegalArgumentException();
			}
			JobApplicationHandler.updateApplication(userID, applicationID, newStatusID, applicationIDs);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to update application " + applicationID + " for user " + userID, e);
			responseMessage = Utils.getErrorMessage("Failed to update application");
		}
		return Action.SUCCESS;
	}

	public String addApplication() {
		Long userID = 0l;
		try {
			userID = SessionHandler.getLoggedInUserID();
			JobApplication application = new JobApplication();
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
			JobApplicationHandler.addApplication(application);
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

	public List<JobApplication> getApplications() {
		return applications;
	}

	public void setApplications(List<JobApplication> applications) {
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