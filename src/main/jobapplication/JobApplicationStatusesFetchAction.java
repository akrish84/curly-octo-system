package main.jobapplication;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.Action;

import main.application.ApplicationHandler;
import main.authentication.SessionHandler;
import main.beans.ApplicationStatus;
import main.beans.JobApplicationStatusesFetchResponse;


public class JobApplicationStatusesFetchAction {
	
	private static Logger LOGGER = Logger.getLogger(JobApplicationStatusesFetchAction.class.getName());
	
	private JobApplicationStatusesFetchResponse response;
	
	public String fetchUsersJobApplicationStatuses() {
		Long userID = 0l;
		try {
			userID = SessionHandler.getLoggedInUserID();
			Map<Long, ApplicationStatus> statusesMap = ApplicationHandler.fetchApplicationStatusesForUser(userID);
			response = new JobApplicationStatusesFetchResponse();
			response.setStatusesMap(statusesMap);
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch statuses for user for user " + userID, e);
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public JobApplicationStatusesFetchResponse getResponse() {
		return response;
	}
}
