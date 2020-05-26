package main.authentication;

import java.security.Principal;

public class UserPrincipal implements Principal {

	private String email;
	
	UserPrincipal(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	@Override
	public String getName() {
		return getEmail();
	}
	

}
