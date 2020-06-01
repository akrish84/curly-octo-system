package main.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationProperties {
	
	private final static String PROPERTY_FILE = "config.properties";
	private static Map<String, String> configurations;
	
	/**
	 * Loads config file and stores key:value paris in memory.
	 * For testing purpose, uncomment second propertyFile initialization
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * 
	 * @throws Exception
	 */
	public static void init() throws FileNotFoundException, IOException {

		File propertyFile = Utils.getFileFromResources(PROPERTY_FILE);

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
	
	/**
	 * Returns value of key stored in config.properties
	 * @param key
	 * @return value - stored for the given key
	 * @throws Exception - If key is not present in config.properties
	 */
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
