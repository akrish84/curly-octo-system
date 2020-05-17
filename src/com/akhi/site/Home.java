package com.akhi.site;

import com.opensymphony.xwork2.Action;

public class Home {

	private String message;

	public String printHome() {
		System.out.println("Printing home");
		return Action.SUCCESS;
	}

	public String getMsg() {
		message = "Hey how are u";
		return Action.SUCCESS;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
