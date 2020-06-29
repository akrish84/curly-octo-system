package main.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import main.beans.Application;
import main.db.QueryProvider;

public class ApplicationTable {
	

	private static final String INSERT_USER_APPLICATION = "main.db.tables.ApplicationTabls.insertUserApplication";
	private static final String UPDATE_APPLICATION_STATUS = "main.db.tables.ApplicationTabls.updateApplicationStatus";
	private static final String FETCH_USER_APPLICATIONS = "main.db.tables.ApplicationTabls.fetchUserApplications";
	private static final String FETCH_ALL_APPLICATION_IDS__OF_STATUS_FOR_USER = "main.db.tables.ApplicationTable.fetchAllApplicationIDsOfStatusForUser";
	
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_COMPANY_NAME = "company_name";
	private static final String COLUMN_JOB_TITLE = "job_title";
	private static final String COLUMN_JOB_DESCRIPTION = "job_description";
	private static final String COLUMN_APS = "aps";
	private static final String COLUMN_STATUS_ID = "status_id";
	private static final String COLUMN_APPLIED_DATE = "applied_date";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_RESUME_ID = "resume_id";
	private static final String COLUMN_RANK = "rank";
	
	
	// INSERT FUNCTIONS
	
	
	/**
	 * Inserts application to DB and returns insertedApplication's ID
	 * 
	 * @param application
	 * @param connection
	 * @return inserted applications ID
	 * @throws SQLException
	 * @throws ParseException
	 */
	
	public static Long addUserApplication(Application application, Connection connection) throws SQLException, ParseException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Long lastInsertId = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(INSERT_USER_APPLICATION), Statement.RETURN_GENERATED_KEYS);
	        statement.setString(1, application.getCompanyName());
	        statement.setString(2, application.getJobTitle());
	        statement.setString(3, application.getJobDescription());
	        statement.setString(4, application.getAps());
	        statement.setLong(5, application.getStatusID());
	        String appliedDate = application.getAppliedDate();
	        java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(appliedDate);
	        statement.setDate(6, new java.sql.Date(date.getTime()));
	        statement.setLong(7, application.getUserID());
		    int rowsInserted = statement.executeUpdate();
		    if(rowsInserted > 0){
	            resultSet = statement.getGeneratedKeys();
	            resultSet.next();
	            lastInsertId = resultSet.getLong(1);
	    	} else {
	    		throw new SQLException("Application not added ");
	    	}
        } finally {
        	if(statement != null) {
    			statement.close();
    		}
        }
        application.setId(lastInsertId);
        return lastInsertId;
	}

	/**
	 * Updates statusID for application
	 * 
	 * @param applicationID
	 * @param statusID
	 * @param connection
	 * @throws SQLException
	 */
	public static void updateApplicationStatus(Long applicationID, Long statusID, Connection connection) throws SQLException {
		PreparedStatement statement = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(UPDATE_APPLICATION_STATUS));
	        statement.setLong(1, statusID);
	        statement.setLong(2, applicationID);
	        statement.executeUpdate();
        } finally {
        	if(statement != null) {
    			statement.close();
    		}
        }
	}
	
	// FETCH FUNCTIONS
	
	/**
	 * Fetches user's job applications' details
	 * 
	 * @param userID
	 * @return List of User's Applications
	 * @throws SQLException
	 */
	public static List<Application> fetchUserApplications(Long userID, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Application> applications = new ArrayList<>();
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_USER_APPLICATIONS));
	        statement.setLong(1, userID);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	Application application = new Application();
	        	application.setId(resultSet.getLong(COLUMN_ID));
	        	application.setCompanyName(resultSet.getString(COLUMN_COMPANY_NAME));
	        	application.setJobTitle(resultSet.getString(COLUMN_JOB_TITLE));
	        	application.setJobDescription(resultSet.getString(COLUMN_JOB_DESCRIPTION));
	        	application.setAps(resultSet.getString(COLUMN_APS));
	        	application.setStatusID(resultSet.getLong(COLUMN_STATUS_ID));
	        	application.setAppliedDate(resultSet.getString(COLUMN_APPLIED_DATE));
	        	application.setUserID(resultSet.getLong(COLUMN_USER_ID));
	        	application.setResumeID(resultSet.getLong(COLUMN_RESUME_ID));
	        	application.setRank(resultSet.getInt(COLUMN_RANK));
	            applications.add(application);
	        }
	        
        } finally {
    		if(resultSet != null) {
    			resultSet.close();
    		}
    		if(statement != null) {
    			statement.close();
    		}
        }
        return applications;
	}
	
	
	/**
	 * Fetches all application IDs of given statusID for user
	 * 
	 * @param userID
	 * @param statusID
	 * @param connection
	 * @return List of application IDs 
	 * @throws SQLException
	 */
	public static List<Long> fetchUserApplicationIDsForStatus(Long userID, Long statusID, Connection connection) throws SQLException {
		PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Long> applicationIDs = new ArrayList<>();
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_ALL_APPLICATION_IDS__OF_STATUS_FOR_USER));
	        statement.setLong(1, userID);
	        statement.setLong(2, statusID);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	applicationIDs.add(resultSet.getLong("id"));
	        }
        } finally {
    		if(resultSet != null) {
    			resultSet.close();
    		}
    		if(statement != null) {
    			statement.close();
    		}
        }
        return applicationIDs;
	}
}
