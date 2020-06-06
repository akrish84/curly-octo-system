package main.application;

import java.sql.SQLException;
import java.util.List;

import main.db.DataManager;

public class SuggestionsHandler {
	
	public static List<String> fetchCompanySuggestions() throws SQLException {
		return DataManager.fetchCompanySuggestions();
	}
	
	public static List<String> fetchJobTitleSuggestions() throws SQLException {
		return DataManager.fetchJobTitleSuggestions();
	}
	
	public static List<String> fetchAPSSuggestions() throws SQLException {
		return DataManager.fetchAPSSuggestions();
	}

}
