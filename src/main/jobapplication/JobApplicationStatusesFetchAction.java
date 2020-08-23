package main.jobapplication;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.Action;

import main.application.JobApplicationHandler;
import main.authentication.SessionHandler;
import main.beans.ApplicationStatus;
import main.beans.JobApplicationStatusesFetchResponse;


public class JobApplicationStatusesFetchAction {
	
	private static Logger LOGGER = Logger.getLogger(JobApplicationStatusesFetchAction.class.getName());
	
	private JobApplicationStatusesFetchResponse response;
	
	public String fetchLoggedInUserJobApplicationStatuses() {
		Long userID = SessionHandler.getLoggedInUserID();
		try { 
			Map<Long, ApplicationStatus> jobStatusesMap = JobApplicationHandler.fetchApplicationStatusesForUser(userID);
			response = new JobApplicationStatusesFetchResponse();
			response.setJobStatusesMap(jobStatusesMap);
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch statuses for user " + userID, e);
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public JobApplicationStatusesFetchResponse getResponse() {
		return response;
	}
}
