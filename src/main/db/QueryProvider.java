package main.db;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import main.util.Utils;


public class QueryProvider {

	private static final String QUERIES_DIR = "queries";
	private static final String QUERY_FILE_SUFFIX = "query.xml";
	
	private static Map<String, String> QUERIES;
	
	
	/**
	 * Loads queries defined in classes/queries/*_query.xml file to a map.
	 * Each query has a corresponding key, used to fetch the query.
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void init() throws ParserConfigurationException, SAXException, IOException {
		QUERIES = new HashMap<>();
		File folder = Utils.getFileFromResources(QUERIES_DIR);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().contains(QUERY_FILE_SUFFIX)) {
		    	QueryFileParser.parseFileAndLoadQueries(file, QUERIES);
		    }
		}
	}
	
	/**
	 * returns String - query defined for the given key in a *_query.xml file in class/queries/ folder.
	 * returns null if key not found.
	 * 
	 * @param key
	 * @return query defined for the given key
	 */
	public static String getQuery(String key) {
		return QUERIES.get(key);
	}

	
}
