package main.util;

import java.io.File;
import java.net.URL;

/**
 * 
 * Performs Utility functions
 * 
 * @author akhilesh
 *
 */
public class Utils {
	/**
	 * Returns file instance for files stored in classes folder in WEB-INF dir.
	 * @param fileName
	 * @return
	 * @throws Exception IllegalArgumentException if FileNotFound
	 */
	
	public static File getFileFromResources(String fileName) throws Exception{
		ClassLoader classLoader = new Utils().getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("FileNotFound!");
        } else {
            return new File(resource.getFile());
        }
	}
	
	/**
	 * Adds Error tag before message.
	 * 
	 * @param message
	 * @return message with Error tag
	 */
	public static String getErrorMessage(String message) {
		return "Error: " + message;
	}
	
	/**
	 * Adds Success tag before message.
	 * 
	 * @param message
	 * @return message with Success tag
	 */
	public static String getSuccessMessage(String message) {
		return "Success: " + message;
	}

}
