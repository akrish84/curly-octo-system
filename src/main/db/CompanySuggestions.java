package main.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanySuggestions implements CompanySuggestionsQueries {
	
	
	
	/**
	 * Fetches all company names in CompanySuggestions table
	 * 
	 * @return List<String> list of company names.
	 * @throws SQLException
	 */
	public static List<String> getCompanySuggestions() throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> suggestions = new ArrayList<>();
        try {
	        statement = connection.prepareStatement(SELECT_COMPANY_SUGGESTIONS);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	suggestions.add(resultSet.getString("NAME"));
	        }
	        
        } finally {
        		if(resultSet != null) {
        			resultSet.close();
        		}
        		statement.close();
        }
        return suggestions;
	}
	
	
	/**
	 * Adds company to CompanySuggestions table
	 * @param companyName
	 * @throws SQLException
	 */
	public static void addCompanyForSuggestion(String companyName) throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(INSERT_COMPANY_FOR_SUGGESTION);
	        statement.setString(1, companyName);
	        statement.executeUpdate();
        } finally {
        		if(resultSet != null) {
        			resultSet.close();
        		}
        		statement.close();
        }
	}

}
