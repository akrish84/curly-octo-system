package main.jobapplication;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.Action;

import main.authentication.SessionHandler;
import main.beans.JobApplicationStatus;
import main.beans.JobApplicationStatusesFetchResponse;


public class FetchJobApplicationStatusesAction {
	
	private static Logger LOGGER = Logger.getLogger(FetchJobApplicationStatusesAction.class.getName());
	
	private JobApplicationStatusesFetchResponse response;
	
	public String fetchLoggedInUserJobApplicationStatuses() {
		Long userID = SessionHandler.getLoggedInUserID();
		try { 
			Map<Long, JobApplicationStatus> jobStatusesMap = JobApplicationHandler.fetchJobApplicationStatusesForUser(userID);
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
