package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.beans.JobApplicationStatus;
import main.db.QueryProvider;

public class UserJobApplicationStatusesTable {
	
	private static final String INSER_USER_JOB_STATUS = "main.db.tables.UserApplicationStatusesTable.addUserStatus";
	private static final String UPDATE_USER_JOB_STATUS = "main.db.tables.UserApplicationStatusesTable.updateUserStatus";
	private static final String FETCH_ALL_JOB_APPLICATION_STATUSES_FOR_USER = "main.db.tables.UserApplicationStatusesTable.fetchAllApplicationStatusForUser";
	private static final String FETCH_MAX_JOB_STATUS_RANK_FOR_USER = "main.db.tables.UserApplicationStatusesTable.fetchMaxRankForUser";
	private static final String FETCH_JOB_STATUS = "main.db.tables.UserApplicationStatusesTable.fetchStatus";
	
	
	
	//INSERT FUNCTIONS
	 


	/**
	 * Adds statuses for user.
	 * @param statuses
	 * @param userID
	 * @param connection
	 * @throws SQLException
	 */
	public static void addJobStatusesForUser(List<JobApplicationStatus> statuses, long userID, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        try {
        	statement = connection.prepareStatement(QueryProvider.getQuery(INSER_USER_JOB_STATUS));
        	for(JobApplicationStatus status : statuses) {
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
	 * Updates status for user
	 * @param status
	 * @param userID
	 * @param connection
	 * @throws SQLException
	 */
	public static void updateJobStatusForUser(JobApplicationStatus status, long userID, Connection connection) throws SQLException {
		PreparedStatement statement = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(UPDATE_USER_JOB_STATUS));
	        statement.setString(1, status.getStatus());
            statement.setInt(2, status.getRank());
            statement.setLong(3, userID);
            statement.setLong(4, status.getId());
	        statement.executeUpdate();
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
	public static void updateJobStatusesForUser(List<JobApplicationStatus> statuses, long userID, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        try {
        	statement = connection.prepareStatement(QueryProvider.getQuery(UPDATE_USER_JOB_STATUS));
        	for(JobApplicationStatus status : statuses) {
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
	
	// FETCH FUNCTIONS
	
	/**
	 * Fetches all statuses as Map<statusID , ApplicationStatus> for given user.
	 * 
	 * @param userID
	 * @return a map of user statuses -> Map<statusID , ApplicationStatus>
	 * @throws SQLException
	 */
	public static Map<Long, JobApplicationStatus> fetchAllJobApplicationStatusForUser(Long userID, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<Long, JobApplicationStatus> statuses = new HashMap<>();
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_ALL_JOB_APPLICATION_STATUSES_FOR_USER));
	        statement.setLong(1, userID);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	JobApplicationStatus status = new JobApplicationStatus();
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
	 * Fetch status details
	 * 
	 * @param userID
	 * @param statusID
	 * @param connection
	 * @return ApplicationStatus details
	 * @throws SQLException
	 */
	public static JobApplicationStatus fetchJobApplicationStatus(Long userID, Long statusID, Connection connection) throws SQLException {
		PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_JOB_STATUS));
	        statement.setLong(1, userID);
	        statement.setLong(2, statusID);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	JobApplicationStatus status = new JobApplicationStatus();
	        	status.setId(resultSet.getLong("id"));
	        	status.setStatus(resultSet.getString("status"));
	        	status.setRank(resultSet.getInt("rank"));
	        	return status;
	        }
        } finally {
    		if(resultSet != null) {
    			resultSet.close();
    		}
    		if(statement != null) {
    			statement.close();
    		}
        }
        return null;
		
	}
	
	/**
	 * Fetches max rank of status for given user
	 * @param userID
	 * @return max rank of status for user.
	 * @throws SQLException
	 */
	public static int fetchMaxJobStatusRankForUser(Long userID, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_MAX_JOB_STATUS_RANK_FOR_USER));
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
