package main.usermanagement;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.Action;

import main.util.Response;
import main.util.Validator;

public class UserManagementAction {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String response;
	
	private static Logger LOGGER = Logger.getLogger(UserManagementAction.class.getName());
	
	public String pageDispatcher() {
		return Action.SUCCESS;
	}

	public String signup() {
		if(firstName == null || lastName == null || email == null || password == null) {
			response = Response.getErrorMessage("Invalid Input");
			return Action.SUCCESS;
		}
		if(!Validator.isValidEmailAddress(email)) {
			response = Response.getErrorMessage("Invalid Email Address " + email);
			return Action.SUCCESS;
		}
		LOGGER.log(Level.INFO, "Signing up user - email: " + email);
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(password);
		try {
			UserManagementHandler.signup(user);
			response = Response.getSuccessMessage("User " + email + " Successfully signed up");
		} catch(SQLIntegrityConstraintViolationException e ) {
			if(e.getMessage().contains("email")) {
				response = Response.getErrorMessage("Email ID already exists");
			} else {
				response = Response.getErrorMessage("Failed to sign up, please try again later");
			}
			LOGGER.log(Level.INFO, response);
		}
		catch(Exception e) {
			e.printStackTrace();
			response = Response.getErrorMessage("Failed to sign up, please try again later");
			LOGGER.log(Level.SEVERE, response);
		}
		return Action.SUCCESS;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	

}
