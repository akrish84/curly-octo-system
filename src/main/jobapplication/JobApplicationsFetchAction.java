package main.jobapplication;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.Action;

import main.application.JobApplicationHandler;
import main.authentication.SessionHandler;
import main.beans.JobApplication;
import main.beans.JobApplicationsFetchResponse;

public class JobApplicationsFetchAction {
	
	private static Logger LOGGER = Logger.getLogger(JobApplicationsFetchAction.class.getName());
	
	private JobApplicationsFetchResponse response;
	
	public String fetchLoggedInUserApplications() {
		Long userID = SessionHandler.getLoggedInUserID();;
		try {
			List<JobApplication> jobApplications = JobApplicationHandler.fetchUserApplications(userID);
			response = new JobApplicationsFetchResponse();
			response.setJobApplications(jobApplications);
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch applications for user for user " + userID, e);
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}

	public JobApplicationsFetchResponse getResponse() {
		return response;
	}

	public void setResponse(JobApplicationsFetchResponse response) {
		this.response = response;
	}

}
