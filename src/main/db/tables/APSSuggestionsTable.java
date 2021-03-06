package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.db.QueryProvider;

public class APSSuggestionsTable {

	
	
	private static final String INSERT_APS_FOR_SUGGESTION = "main.db.tables.ApsSuggestionsTable.insertApsForSuggestion";
	private static final String FETCH_APS_SUGGESTIONS = "main.db.tables.ApsSuggestionsTable.getApsSuggestions";
	
	/***
	 * INSERT FUNCTIONS
	 */
	
	/**
	 * Adds aps to aps_suggestions table
	 * @param apsName
	 * @throws SQLException
	 */
	public static void addApsForSuggestion(String apsName, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(INSERT_APS_FOR_SUGGESTION));
	        statement.setString(1, apsName);
	        statement.executeUpdate();
        } finally {
        	if(statement != null) {
    			statement.close();
    		}
        }
	}
	
	
	/***
	 * GET FUNCTIONS
	 */
	
	/**
	 * Fetches all aps names in aps_suggestions table
	 * 
	 * @return List<String> list of company names.
	 * @throws SQLException
	 */
	public static List<String> fetchApsSuggestions(Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> suggestions = new ArrayList<>();
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_APS_SUGGESTIONS));
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
