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
	
	private static boolean RUNNIN_ON_SERVER = false;
	
	
	/**
	 * Sets running on server flag.
	 * Flag is used to read files from the right directory based on context.
	 * 
	 * @param flag
	 */
	public static void setRunningOnServer(boolean flag) {
		RUNNIN_ON_SERVER = flag;
	}
	
	/**
	 * Returns file instance for files stored in classes folder in WEB-INF dir.
	 * @param fileName
	 * @return
	 * @throws Exception IllegalArgumentException if FileNotFound
	 */
	
	public static File getFileFromResources(String fileName) throws IllegalArgumentException {
		if(RUNNIN_ON_SERVER) {
			return getFileFromServerContext(fileName);
		} else {
			return getFileFromUserDirContext(fileName);
		}
		
	}
	
	private static File getFileFromServerContext(String fileName) throws IllegalArgumentException{
		ClassLoader classLoader = new Utils().getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("FileNotFound!");
        } else {
            return new File(resource.getFile());
        }
	}
	
	private static File getFileFromUserDirContext(String fileName) {
		String currentDirectory = System.getProperty("user.dir");
		return new File(currentDirectory + "/WebContent/WEB-INF/classes/" + fileName);
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
