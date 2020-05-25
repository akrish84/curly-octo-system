package main.util;

public class Response {
	private boolean status;
	private String message;

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}

	public void setError(String message) {
		this.status = false;
		this.message = message;
	}
	
	public void setSuccess(String message) {
		this.status = true;
		this.message = message;
	}
}
