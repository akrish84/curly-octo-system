package main.application;

import java.sql.SQLException;
import java.util.List;

import main.db.DatabaseManager;

public class SuggestionsHandler {
	
	public static List<String> fetchCompanySuggestions() throws SQLException {
		return DatabaseManager.getInstance().fetchCompanySuggestions();
	}
	
	public static List<String> fetchJobTitleSuggestions() throws SQLException {
		return DatabaseManager.getInstance().fetchJobTitleSuggestions();
	}
	
	public static List<String> fetchAPSSuggestions() throws SQLException {
		return DatabaseManager.getInstance().fetchAPSSuggestions();
	}

}
