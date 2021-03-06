package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.db.QueryProvider;

public class CompanySuggestionsTable {
	
	private static final String INSERT_COMPANY_FOR_SUGGESTION = "main.db.tables.CompanySuggestions.insertCompanyForSuggestion";
	private static final String FETCH_COMPANY_SUGGESTIONS = "main.db.tables.CompanySuggestions.getCompanySuggestions";
	
	/***
	 * INSERT FUNCTIONS
	 */
	
	/**
	 * Adds company to company_suggestions table
	 * @param companyName
	 * @throws SQLException
	 */
	public static void addCompanyForSuggestion(String companyName, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(INSERT_COMPANY_FOR_SUGGESTION));
	        statement.setString(1, companyName);
	        statement.executeUpdate();
        } finally {
        	if(statement != null) {
    			statement.close();
    		}
        }
	}
	
	/***
	 * FETCH FUNCTIONS
	 */
	
	/**
	 * Fetches all company names in company_suggestions table
	 * 
	 * @return List<String> list of company names.
	 * @throws SQLException
	 */
	public static List<String> fetchCompanySuggestions(Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> suggestions = new ArrayList<>();
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_COMPANY_SUGGESTIONS));
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	suggestions.add(resultSet.getString("NAME"));
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
