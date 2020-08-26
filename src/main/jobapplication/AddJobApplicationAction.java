package main.jobapplication;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.Action;
import main.authentication.SessionHandler;
import main.beans.AddJobApplicatinoRequest;
import main.beans.JobApplication;
import main.util.Validator;

public class AddJobApplicationAction {

	private static Logger LOGGER = Logger.getLogger(AddJobApplicationAction.class.getName());

	private AddJobApplicatinoRequest request;

	public String addJobApplication() {
		Long userID = SessionHandler.getLoggedInUserID();
		if (!validateInput(userID)) {
			return Action.INPUT;
		}
		try {

			JobApplication application = new JobApplication();
			application.setCompanyName(request.getCompanyName());
			application.setJobTitle(request.getJobTitle());
			application.setJobDescription(request.getJobDescription());
			application.setAps(request.getAps());
			application.setAppliedDate(request.getAppliedDate());
			application.setStatusID(request.getStatusID());
			application.setUserID(userID);

			JobApplicationHandler.addJobApplication(application);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to add application for user " + userID, e);
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}

	private boolean validateInput(Long userID) {
		if (request == null) {
			return false;
		} else if (Validator.isNull(request.getCompanyName(), request.getJobTitle(), request.getJobDescription(),
				request.getAps(), request.getAppliedDate(), request.getStatusID())) {
			return false;
		} else if (Validator.isEmpty(request.getCompanyName(), request.getJobTitle(), request.getJobDescription(),
				request.getAps(), request.getAppliedDate())) {
			return false;
		} else if (!Validator.isValidStatusID(request.getStatusID(), userID)) {
			return false;
		} else {
			return true;
		}
	}

}
