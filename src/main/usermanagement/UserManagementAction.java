package main.usermanagement;

import com.opensymphony.xwork2.Action;

import main.util.Response;

public class UserManagementAction {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String response;
	
	public String pageDispatcher() {
		return Action.SUCCESS;
	}

	public String signup() {
		System.out.println("Adding user " + firstName + " " + lastName);
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(password);
		try {
			UserManagementHandler.signup(user);
			response = Response.getSuccessMessage("Successfully Signed up");
		} catch(Exception e) {
			e.printStackTrace();
			response = Response.getErrorMessage("Failed to sign up, please try again later");
		}
		System.out.println(response);
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
