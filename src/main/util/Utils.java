package main.util;

import java.io.File;
import java.net.URL;
import java.util.regex.Pattern;

public class Utils {
	// File has to be in classes directory
	private static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
	private static final Pattern emailPattern = Pattern.compile(emailRegex);
	
	public static File getFileFromResources(String fileName) {
		ClassLoader classLoader = new Utils().getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
	}
	
	public static boolean isValidEmailID(String email) {
		return emailPattern.matcher(email).matches();
	}

}
