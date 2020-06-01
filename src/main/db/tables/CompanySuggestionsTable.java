package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.db.DataSourceConnector;
import main.db.QueryProvider;

public class CompanySuggestionsTable {
	
	private static final String INSERT_COMPANY_FOR_SUGGESTION = "main.db.tables.CompanySuggestions.insertCompanyForSuggestion";
	private static final String GET_COMPANY_SUGGESTIONS = "main.db.tables.CompanySuggestions.getCompanySuggestions";
	
	/***
	 * INSERT FUNCTIONS
	 */
	
	/**
	 * Adds company to company_suggestions table
	 * @param companyName
	 * @throws SQLException
	 */
	public static void addCompanyForSuggestion(String companyName) throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(INSERT_COMPANY_FOR_SUGGESTION));
	        statement.setString(1, companyName);
	        statement.executeUpdate();
        } finally {
        		if(resultSet != null) {
        			resultSet.close();
        		}
        		statement.close();
        }
	}
	
	/***
	 * GET FUNCTIONS
	 */
	
	/**
	 * Fetches all company names in company_suggestions table
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
	        statement = connection.prepareStatement(QueryProvider.getQuery(GET_COMPANY_SUGGESTIONS));
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

}
