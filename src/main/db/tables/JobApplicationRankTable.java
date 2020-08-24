package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import main.db.QueryProvider;

public class JobApplicationRankTable {
	
	private static final String INSERT_APPLICATION_RANK = "main.db.tables.ApplicationRankTable.insertUserApplicationRank";
	private static final String UPDATE_APPLICATIONS_RANKS = "main.db.tables.ApplicationRankTable.updateUserApplicationsRanks";
	private static final String FETCH_MAX_RANK_FOR_USER = "main.db.tables.ApplicationRankTable.fetchMaxRankForUser";
	
	
	// INSERT FUNCTIONS
	
	/**
	 * Adds rank for application
	 * 
	 * @param applicationID
	 * @param rank
	 * @param connection
	 * @throws SQLException
	 */
	public static void addJobApplicationRank(Long applicationID, int rank, Connection connection) throws SQLException {
		PreparedStatement statement = null;
        try {
        	statement = connection.prepareStatement(QueryProvider.getQuery(INSERT_APPLICATION_RANK));
        	statement.setLong(1, applicationID);
        	statement.setLong(2, rank);
        	statement.executeUpdate();
        } finally {
        	if(statement != null) {
        		statement.close();
    		}
        }
	}
	
	/**
	 * Updates ranks for appIDs
	 * 
	 * @param appIDToRank
	 * @param connection
	 * @throws SQLException
	 */
	public static void updateJobApplicationsRanks(Map<Long, Integer> appIDToRank, Connection connection) throws SQLException {        
        PreparedStatement statement = null;
        try {
        	statement = connection.prepareStatement(QueryProvider.getQuery(UPDATE_APPLICATIONS_RANKS));
        	for(Long appID : appIDToRank.keySet()) {
        		int rank = appIDToRank.get(appID);
        		statement.setInt(1, rank);
        		statement.setLong(2, appID);
                statement.addBatch();
        	}
        	int[] results = statement.executeBatch();
	        for(int result: results) {
	        	if(result == PreparedStatement.EXECUTE_FAILED) {
	        		throw new SQLException("Failed to update applications statuses: " + appIDToRank);
	        	}
	        }
        } finally {
        	if(statement != null) {
        		statement.close();
    		}
        }
	}
	
	
	// FETCH FUNCTIONS

	/**
	 * Fetches max rank of applications for given status for user.
	 * 
	 * @param userID
	 * @param statusID
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public static int fetchUsersMaxRankForJobStatus(Long userID, Long statusID, Connection connection) throws SQLException {
		PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_MAX_RANK_FOR_USER));
	        statement.setLong(1, userID);
	        statement.setLong(2, statusID);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	return resultSet.getInt("max_rank");
	        }
        } finally {
    		if(resultSet != null) {
    			resultSet.close();
    		}
    		if(statement != null) {
    			statement.close();
    		}
        }
        return 1;
	}
}
