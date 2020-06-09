package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.beans.ApplicationStatus;
import main.db.QueryProvider;

public class UserApplicationStatusesTable {
	
	private static final String INSER_USER_STATUS = "main.db.tables.UserApplicationStatusesTable.addUserStatus";
	private static final String UPDATE_USER_STATUS = "main.db.tables.UserApplicationStatusesTable.updateUserStatus";
	private static final String FETCH_ALL_APPLICATION_STATUSES_FOR_USER = "main.db.tables.UserApplicationStatusesTable.fetchAllApplicationStatusForUser";
	private static final String FETCH_MAX_RANK_FOR_USER = "main.db.tables.UserApplicationStatusesTable.fetchMaxRankForUser";
	
	
	/***
	 * INSERT FUNCTIONS
	 */


	/**
	 * Adds statuses for user.
	 * @param statuses
	 * @param userID
	 * @param connection
	 * @throws SQLException
	 */
	public static void addStatusesForUser(List<ApplicationStatus> statuses, long userID, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        try {
        	statement = connection.prepareStatement(QueryProvider.getQuery(INSER_USER_STATUS));
        	for(ApplicationStatus status : statuses) {
        		statement.setString(1, status.getStatus());
                statement.setLong(2, userID);
                statement.setInt(3, status.getRank());
                statement.addBatch();
        	}
        	int[] results = statement.executeBatch();
	        for(int result: results) {
	        	if(result == PreparedStatement.EXECUTE_FAILED) {
	        		throw new SQLException("Failed to add status for user " + userID);
	        	}
	        }
        } finally {
        	if(statement != null) {
        		statement.close();
    		}
        }
	}

	
	/**
	 * Updates statuses for user.
	 * @param statuses
	 * @param userID
	 * @param connection
	 * @throws SQLException
	 */
	public static void updateStatusesForUser(List<ApplicationStatus> statuses, long userID, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        try {
        	statement = connection.prepareStatement(QueryProvider.getQuery(UPDATE_USER_STATUS));
        	for(ApplicationStatus status : statuses) {
        		System.out.println("In rs " + status.getStatus());
        		statement.setString(1, status.getStatus());
                statement.setInt(2, status.getRank());
                statement.setLong(3, userID);
                statement.setLong(4, status.getId());
                statement.addBatch();
        	}
        	int[] results = statement.executeBatch();
	        for(int result: results) {
	        	if(result == PreparedStatement.EXECUTE_FAILED) {
	        		throw new SQLException("Failed to add status for user " + userID);
	        	}
	        }
        } finally {
        	if(statement != null) {
        		statement.close();
    		}
        }
	}
	
	/***
	 * FETCH FUNCTIONS
	 *
	 */
	
	/**
	 * Fetches all statuses as Map<statusID , ApplicationStatus> for given user.
	 * 
	 * @param userID
	 * @return a map of user statuses -> Map<statusID , ApplicationStatus>
	 * @throws SQLException
	 */
	public static Map<Long, ApplicationStatus> fetchAllApplicationStatusForUser(Long userID, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<Long, ApplicationStatus> statuses = new HashMap<>();
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_ALL_APPLICATION_STATUSES_FOR_USER));
	        statement.setLong(1, userID);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	ApplicationStatus status = new ApplicationStatus();
	        	status.setId(resultSet.getLong("id"));
	        	status.setStatus(resultSet.getString("status"));
	        	status.setRank(resultSet.getInt("rank"));
	        	statuses.put(status.getId(), status);
	        }
        } finally {
    		if(resultSet != null) {
    			resultSet.close();
    		}
    		if(statement != null) {
    			statement.close();
    		}
        }
        return statuses;
	}

	
	/**
	 * Fetches max rank of status for given user
	 * @param userID
	 * @return max rank of status for user.
	 * @throws SQLException
	 */
	public static int fetchMaxStatusRankForUser(Long userID, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_MAX_RANK_FOR_USER));
	        statement.setLong(1, userID);
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
