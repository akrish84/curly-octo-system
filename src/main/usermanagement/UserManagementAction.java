package main.usermanagement;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.Action;

import main.authentication.AuthenticationUtil;
import main.util.Response;
import main.util.Utils;
import main.util.Validator;


public class UserManagementAction {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String responseMessage;
	
	private static Logger LOGGER = Logger.getLogger(UserManagementAction.class.getName());
	
	public String pageDispatcher() {
		return Action.SUCCESS;
	}

	public String signup() {
		if(firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
			responseMessage = Utils.getErrorMessage("First Name and Last Name cannot be empty");
			LOGGER.log(Level.SEVERE, responseMessage);
			return Action.SUCCESS;
		}
		if(email == null || email.isEmpty()) {
			responseMessage = Utils.getErrorMessage("Email cannot be empty");
			LOGGER.log(Level.SEVERE, responseMessage);
			return Action.SUCCESS;
		}
		if(password == null || password.isEmpty()) {
			responseMessage = Utils.getErrorMessage("Password cannot be empty");
			LOGGER.log(Level.SEVERE, responseMessage);
			return Action.SUCCESS;
		}
		if(!Validator.isValidEmailAddress(email)) {
			responseMessage = Utils.getErrorMessage("Invalid Email Address " + email);
			return Action.SUCCESS;
		}
		LOGGER.log(Level.INFO, "Action: SignUp. User email: " + email);
		try {
			User user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setPassword(password);
			UserManagementHandler.signup(user);
			responseMessage = Utils.getSuccessMessage("User " + email + " Successfully signed up");
			LOGGER.log(Level.INFO, responseMessage);
		} catch(SQLIntegrityConstraintViolationException e ) {
			if(e.getMessage().contains("email")) {
				responseMessage = Utils.getErrorMessage("Email ID already exists");
			} else {
				responseMessage = Utils.getErrorMessage("Failed to sign up, please try again later");
			}
			LOGGER.log(Level.INFO, responseMessage);
		}
		catch(Exception e) {
			responseMessage = Utils.getErrorMessage("Failed to sign up, please try again later");
			LOGGER.log(Level.SEVERE, responseMessage, e);
		}
		return Action.SUCCESS;
	}

	public String login() {
		
		if(email == null || email.isEmpty()) {
			responseMessage = Utils.getErrorMessage("Email cannot be empty");
			LOGGER.log(Level.SEVERE, responseMessage);
			return Action.SUCCESS;
		}
		if(password == null || password.isEmpty()) {
			responseMessage = Utils.getErrorMessage("Password cannot be empty");
			LOGGER.log(Level.SEVERE, responseMessage);
			return Action.SUCCESS;
		}
		if(!Validator.isValidEmailAddress(email)) {
			responseMessage = Utils.getErrorMessage("Invalid Email Address " + email);
			LOGGER.log(Level.SEVERE, responseMessage);
			return Action.SUCCESS;
		}
		LOGGER.log(Level.INFO, "Action: Login. User email: " + email);
		try {
			Response response = UserManagementHandler.login(email, password);
			if(response.getStatus()) {
				AuthenticationUtil.createSessionForUser(email);
				responseMessage = Utils.getSuccessMessage(response.getMessage());
			} else {
				responseMessage = Utils.getErrorMessage(response.getMessage());
			}
			System.out.println(responseMessage);
		} catch(Exception e) {
			responseMessage = Utils.getErrorMessage("Action Login: Failed. User Email: " + email);
			LOGGER.log(Level.SEVERE, responseMessage, e);
		}
		return Action.SUCCESS;
	}
	
	public static void main(String[] args) {
		UserManagementAction action = new UserManagementAction();
		action.setEmail("test@test1.com");
		action.setPassword("test");
		action.login();
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

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

}
