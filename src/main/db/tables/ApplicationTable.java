package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.beans.Application;
import main.db.DataSourceConnector;
import main.db.QueryProvider;

public class ApplicationTable {
	

	private static final String FETCH_USER_APPLICATIONS = "main.db.tables.ApplicationTabls.fetchUserApplications";
	
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_COMPANY_NAME = "company_name";
	private static final String COLUMN_JOB_TITLE = "job_title";
	private static final String COLUMN_JOB_DESCRIPTION = "job_description";
	private static final String COLUMN_APS = "aps";
	private static final String COLUMN_STATUS_ID = "status_id";
	private static final String COLUMN_APPLIED_DATE = "applied_date";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_RESUME_ID = "resume_id";
	
	
	/***
	 * INSERT FUNCTIONS
	 */

	
	/***
	 * FETCH FUNCTIONS
	 */
	
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
}
