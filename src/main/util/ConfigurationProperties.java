package main.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationProperties {
	
	private final static String PROPERTY_FILE = "config.properties";
	private static Map<String, String> configurations;
	
	
	public static void init() throws Exception {
		//Uncomment below line while running project in webserver
		File propertyFile = Utils.getFileFromResources(PROPERTY_FILE);
		
		//Uncomment below  two lines to test using java directly.
		//String currentDirectory = System.getProperty("user.dir");
		//File propertyFile = new File(currentDirectory + "/WebContent/WEB-INF/config/" + PROPERTY_FILE);

		configurations = new HashMap<>();
		try (InputStream input = new FileInputStream(propertyFile)) {
			Properties prop = new Properties();
			prop.load(input);
			for(Object key : prop.keySet()) {
				String value = (String) prop.get(key);
				configurations.put((String)key, value);
			}
		}
	}
	
	public static String getConfiguration(String key) throws Exception {
		String value = null;
		if(configurations == null) {
			init();
		}
		value = configurations.get(key);
		if (value == null) {
			throw new Exception("Key Undefined");
		}

		return value;
	}

}
