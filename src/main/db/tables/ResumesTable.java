package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.beans.Resume;
import main.db.DataSourceConnector;
import main.db.QueryProvider;

public class ResumesTable {
	private static final String INSERT_RESUME_DETAILS = "main.db.tables.ResumesTable.insertResumeDetails";
	private static final String FETCH_RESUME_DETAILS = "main.db.tables.ResumesTable.getResumeDetails";
	
	/***
	 * INSERT FUNCTIONS
	 */
	
	/**
	 * Adds resume details to resume table
	 * @param resume
	 * @throws SQLException
	 */
	public static void addResumeDetails(Resume resume) throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(INSERT_RESUME_DETAILS));
	        statement.setString(1, resume.getFilePath());
	        statement.setString(2, resume.getFileName());
	        statement.setString(3, resume.getHash());
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
	 * Fetches all resume details in aps_suggestions table
	 * 
	 * @return List<String> list of company names.
	 * @throws SQLException
	 */
	public static Resume fetchResumeDetails() throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_RESUME_DETAILS));
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	Resume resume = new Resume();
	        	resume.setFilePath(resultSet.getString("file_path"));
	        	resume.setFileName(resultSet.getString("file_name"));
	        	resume.setHash(resultSet.getString("hash"));
	        }
	        
        } finally {
        		if(resultSet != null) {
        			resultSet.close();
        		}
        		statement.close();
        }
        return null;
	}

}
