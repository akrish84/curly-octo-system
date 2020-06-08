package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.db.DataSourceConnector;
import main.db.QueryProvider;

public class JobTitleSuggestionsTable {
	
	private static String INSERT_JOB_TITLE_FOR_SUGGESTION = "main.db.tables.JobTitleSuggestionsTable.insertJobTitleForSuggestion";
	
	private static String FETCH_JOB_TITLE_SUGGESTIONS = "main.db.tables.JobTitleSuggestionsTable.fetchJobTitleSuggestions";
	
	
	/***
	 * INSERT FUNCTIONS
	 */
	
	/**
	 * Adds job title to job_title_suggestions table
	 * @param jobTitle
	 * @throws SQLException
	 */

	public static void addJobTitleForSuggestion(String jobTitle) throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        try {
	        statement = connection.prepareStatement(INSERT_JOB_TITLE_FOR_SUGGESTION);
	        statement.setString(1, jobTitle);
	        statement.executeUpdate();
        } finally {
        	if(statement != null) {
    			statement.close();
    		}
        }
	}
	
	/***
	 * SELECT FUNCTIONS
	 */
	
	/**
	 * Fetches all job titles from job_title_suggestions table
	 * 
	 * @return List<String> list of company names.
	 * @throws SQLException
	 */
	public static List<String> fetchJobTitleSuggestions() throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> suggestions = new ArrayList<>();
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_JOB_TITLE_SUGGESTIONS));
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	suggestions.add(resultSet.getString("JOB_TITLE"));
	        }
	        
        } finally {
        		if(resultSet != null) {
        			resultSet.close();
        		}
        		if(statement != null) {
        			statement.close();
        		}
        }
        return suggestions;
	}

}
