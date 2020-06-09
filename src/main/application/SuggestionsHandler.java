package main.application;

import java.sql.SQLException;
import java.util.List;

import main.db.DatabaseManager;

public class SuggestionsHandler {
	
	public static List<String> fetchCompanySuggestions() throws SQLException {
		return new DatabaseManager().fetchCompanySuggestions();
	}
	
	public static List<String> fetchJobTitleSuggestions() throws SQLException {
		return new DatabaseManager().fetchJobTitleSuggestions();
	}
	
	public static List<String> fetchAPSSuggestions() throws SQLException {
		return new DatabaseManager().fetchAPSSuggestions();
	}

}
