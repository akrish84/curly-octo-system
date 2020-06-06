package main.application;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.Action;

import main.util.Utils;

public class SuggestionsAction {
	
	private List<String> companySuggestions;
	private List<String> jobTitleSuggestions;
	private List<String> apsSuggestions;
	
	private String responseMessage;
	
	
	private static Logger LOGGER = Logger.getLogger(SuggestionsAction.class.getName());
	
	
	public String fetchCompanySuggestions() {
		try {
			companySuggestions = SuggestionsHandler.fetchCompanySuggestions();
			responseMessage = Utils.getSuccessMessage("Successfully fetched company suggesstions");
		} catch(Exception e) {
			responseMessage = Utils.getErrorMessage("Failed to get company suggestions");
			LOGGER.log(Level.SEVERE, "Failed to get company suggestions: ", e);
		}
		return Action.SUCCESS;
	}
	
	public String fetchJobTitleSuggestions() {
		try {
			jobTitleSuggestions = SuggestionsHandler.fetchJobTitleSuggestions();
			responseMessage = Utils.getSuccessMessage("Successfully fetched job title suggesstions");
		} catch(Exception e) {
			responseMessage = Utils.getErrorMessage("Failed to get company suggestions");
			LOGGER.log(Level.SEVERE, "Failed to get job title suggestions: ", e);
		}
		return Action.SUCCESS;
	}
	
	public String fetchAPSSuggestions() {
		try {
			apsSuggestions = SuggestionsHandler.fetchAPSSuggestions();
			responseMessage = Utils.getSuccessMessage("Successfully fetched aps suggesstions");
		} catch(Exception e) {
			responseMessage = Utils.getErrorMessage("Failed to get aps suggestions");
			LOGGER.log(Level.SEVERE, "Failed to get company suggestions: ", e);
		}
		return Action.SUCCESS;
	}
	
	
	public List<String> getCompanySuggestions() {
		return companySuggestions;
	}
	public void setCompanySuggestions(List<String> companySuggestions) {
		this.companySuggestions = companySuggestions;
	}
	public List<String> getJobTitleSuggestions() {
		return jobTitleSuggestions;
	}
	public void setJobTitleSuggestions(List<String> jobTitleSuggestions) {
		this.jobTitleSuggestions = jobTitleSuggestions;
	}
	public List<String> getApsSuggestions() {
		return apsSuggestions;
	}
	public void setApsSuggestions(List<String> apsSuggestions) {
		this.apsSuggestions = apsSuggestions;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
