package main.util;

public class Response {
	
	public static String getErrorMessage(String message) {
		return "Error: " + message;
	}
	
	public static String getSuccessMessage(String message) {
		return "Success: " + message;
	}

}
