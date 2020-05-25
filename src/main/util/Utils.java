package main.util;

import java.io.File;
import java.net.URL;

public class Utils {
	// File has to be in classes directory	
	public static File getFileFromResources(String fileName) {
		ClassLoader classLoader = new Utils().getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
	}
	
	public static String getErrorMessage(String message) {
		return "Error: " + message;
	}
	
	public static String getSuccessMessage(String message) {
		return "Success: " + message;
	}

}
