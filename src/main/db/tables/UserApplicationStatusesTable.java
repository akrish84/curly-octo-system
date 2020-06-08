package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.beans.ApplicationStatus;
import main.db.DataSourceConnector;
import main.db.QueryProvider;
import main.db.Transaction;

public class UserApplicationStatusesTable {
	
	private static final String INSER_USER_STATUS = "main.db.tables.UserApplicationStatusesTable.addUserStatus";
	private static final String FETCH_ALL_APPLICATION_STATUSES_FOR_USER = "main.db.tables.UserApplicationStatusesTable.fetchAllApplicationStatusForUser";
	
	
	/***
	 * INSERT FUNCTIONS
	 */


	/**
	 * Adds/Updates statuses in DB for user.
	 * If status ID is not null or if status.id does not exist in existing user statuses -> status is inserted as new status
	 * If status already present in existing user statuses -> status, rank, userID is updated.
	 * 
	 * @param statuses
	 * @param userID
	 * @throws SQLException
	 */
	public static void addStatusesForUser(List<ApplicationStatus> statuses, long userID) throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement insertStatement = null;
        PreparedStatement updateStatement = null;
        Transaction transaction = new Transaction(connection);
        try {
        	transaction.begin();
        	insertStatement = connection.prepareStatement(QueryProvider.getQuery(INSER_USER_STATUS));
        	updateStatement = connection.prepareStatement(QueryProvider.getQuery(INSER_USER_STATUS));
        	Map<Long, ApplicationStatus> existingStatuses = fetchAllApplicationStatusForUser(userID);
        	
        	
	        for(ApplicationStatus status : statuses) {
	        	if(status.getId() == null || !existingStatuses.containsKey(status.getId())) {
	        		buildStatement(status, userID, insertStatement);
	        	} else {
	        		buildStatement(status, userID, updateStatement);
	        	}
	        }
	        int[] results = insertStatement.executeBatch();
	        for(int result: results) {
	        	if(result == PreparedStatement.EXECUTE_FAILED) {
	        		throw new SQLException("Failed to insert status for user " + userID);
	        	}
	        }
	        results = updateStatement.executeBatch();
	        for(int result: results) {
	        	if(result == PreparedStatement.EXECUTE_FAILED) {
	        		throw new SQLException("Failed to update status for user " + userID);
	        	}
	        }
	        transaction.commit();
	        
        } catch (SQLException e) {
        	transaction.rollback();
        	throw e;
        }
        finally {
        	if(insertStatement != null) {
        		insertStatement.close();
    		}
        	if(updateStatement != null) {
        		updateStatement.close();
    		}
        	transaction.end();
        }
	}
	
	/**
	 * Populates statement with given status details.
	 * 
	 * @param status
	 * @param userID
	 * @param statement
	 * @throws SQLException
	 */
	private static void buildStatement(ApplicationStatus status, long userID, PreparedStatement statement) throws SQLException {
        statement.setString(1, status.getStatus());
        statement.setLong(2, userID);
        statement.setInt(3, status.getRank());
        statement.addBatch();
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
	public static Map<Long, ApplicationStatus> fetchAllApplicationStatusForUser(Long userID) throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
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
	        	status.setRank(resultSet.getInt("`rank`"));
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

}
