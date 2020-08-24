package main.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import main.db.tables.CompanySuggestionsTable;
import main.db.tables.JobTitleSuggestionsTable;
import main.db.tables.UserJobApplicationStatusesTable;
import main.db.tables.UsersTable;
import main.beans.JobApplication;
import main.beans.JobApplicationStatus;
import main.beans.User;
import main.db.tables.APSSuggestionsTable;
import main.db.tables.JobApplicationRankTable;
import main.db.tables.JobApplicationTable;

/**
 * 
 * Class to handle database transactions.
 * Allows user to consider each request as a separate transaction.
 * If user uses begin function, all requests invoked until end is invoked is considered as a single transaction.
 * 
 * @author akhilesh
 *
 */
public class DatabaseManager {
	
	private Connection connection = null;
	private boolean TRANSACTION_IN_PROGRESS = false;

	/**
	 * Not a singleton class, using getInstance() a much cleaner way of creating instance for unary db function executions.
	 * To maintain object creation consistency through out the code having getInstance function. 
	 */
	
	private DatabaseManager() {
		// empty function
	}
	
	
	/**
	 * Returns a new instance of the class.
	 * [Note] class is not a singleton, function is a cleaner way of creating instance for unary db function executions.
	 * To maintain object creation consistency through out the code having getInstance() function. 
	 * @return
	 */
	public static DatabaseManager getInstance() {
		return new DatabaseManager();
	}
	
	//*************************************
	// Basic connection handling functions
	//*************************************
	
	private Connection getConnection() throws SQLException {
		if(!TRANSACTION_IN_PROGRESS) {
			return DataSourceConnector.getConnection();
		} else {
			if(connection == null) {
				throw new SQLException("Connection is null in between a transaction");
			}
			return connection;
		}
	}

	private void safeClose(Connection connection) throws SQLException {
		if(!TRANSACTION_IN_PROGRESS) {
			connection.close();
		}
	}

	
	//*************************************
	// Users Table
	//*************************************
	
	/**
	 * Forwards request to User Table to add user.
	 * @param user
	 * @throws SQLException
	 */
	public void addUser(User user) throws SQLException {
		Connection con = getConnection();
		UsersTable.add(user, con);
		safeClose(con);
	}

	/**
	 * Forwards request to User Table to fetch user information.
	 * 
	 * @param email
	 * @return User details for given email as object
	 * @throws SQLException
	 */
	public User fetchUser(String email) throws SQLException {
		Connection con = getConnection();
		User user = UsersTable.fetchUser(email, con);
		safeClose(con);
		return user;
	}
	
	
	//*************************************
	// Suggestions Table
	//*************************************
	/**
	 * Returns a list of company suggestions stored in DB
	 * @return list of company names
	 * @throws SQLException
	 */
	public List<String> fetchCompanySuggestions() throws SQLException {
		Connection con = getConnection();
		List<String> companySuggestions = CompanySuggestionsTable.fetchCompanySuggestions(con);
		safeClose(con);
		return companySuggestions;
		
	}
	
	/**
	 * Returns a list of job title suggestions stored in DB
	 * @return List of job titles
	 * @throws SQLException
	 */
	public List<String> fetchJobTitleSuggestions() throws SQLException {
		Connection con = getConnection();
		List<String> jobTitleSuggestions = JobTitleSuggestionsTable.fetchJobTitleSuggestions(con);
		safeClose(con);
		return jobTitleSuggestions;
	}
	
	/**
	 * Returns a list of APS suggestions stored in DB
	 * @return List of APS Suggestions
	 * @throws SQLException
	 */
	public List<String> fetchAPSSuggestions() throws SQLException {
		Connection con = getConnection();
		List<String> apsSuggestions = APSSuggestionsTable.fetchApsSuggestions(con);
		safeClose(con);
		return apsSuggestions;
	}
	
	
	//*************************************
	// UserApplicationStatuses Table
	//*************************************

	/**
	 * Adds statuses in DB for user.
	 * 
	 * @param statuses
	 * @param userID
	 * @throws SQLException
	 */
	
	public void addJobStatusesForUser(List<JobApplicationStatus> statuses, Long userID) throws SQLException {
		Connection con = getConnection();
		UserJobApplicationStatusesTable.addJobStatusesForUser(statuses, userID, con);
		safeClose(con);
	}
	
	
	public void updateJobStatusForUser(JobApplicationStatus status, Long userID) throws SQLException {
		Connection con = getConnection();
		UserJobApplicationStatusesTable.updateJobStatusForUser(status, userID, con);
		safeClose(con);
	}
	
	/**
	 * Updates statuses in DB for user.
	 * 
	 * @param statuses
	 * @param userID
	 * @throws SQLException
	 */
	
	public void updateJobStatusesForUser(List<JobApplicationStatus> statuses, Long userID) throws SQLException {
		Connection con = getConnection();
		UserJobApplicationStatusesTable.updateJobStatusesForUser(statuses, userID, con);
		safeClose(con);
	}
	
	/**
	 * 
	 * Fetches Application Status details for the given statusID and userID 
	 * 
	 * @param userID
	 * @param statusID
	 * @return ApplicationStatus
	 * @throws SQLException
	 */
	public JobApplicationStatus fetchJobStatus(Long userID, Long statusID) throws SQLException {
		Connection con = getConnection();
		JobApplicationStatus status = UserJobApplicationStatusesTable.fetchJobApplicationStatus(userID, statusID, con);
		safeClose(con);
		return status;
	}
	
	/**
	 * Fetches max rank of status for given user
	 * @param userID
	 * @return max rank of status for user.
	 * @throws SQLException
	 */
	public int fetchMaxJobStatusRankForUser(Long userID) throws SQLException {
		Connection con = getConnection();
		int maxRank = UserJobApplicationStatusesTable.fetchMaxJobStatusRankForUser(userID, con);
		safeClose(con);
		return maxRank;
	}
	
	/**
	 * Fetches user's application statuses.
	 * @param userID
	 * @return Map of statusID to ApplicationStatus
	 * @throws SQLException
	 */
	public Map<Long, JobApplicationStatus> fetchJobApplicationStatusesForUser(Long userID) throws SQLException {
		Connection con = getConnection();
		Map<Long, JobApplicationStatus> applicationStatusesForUser = UserJobApplicationStatusesTable.fetchAllJobApplicationStatusForUser(userID, con);
		safeClose(con);
		return applicationStatusesForUser;
	}
	
	//*************************************
	// Application Table
	//*************************************
	
	/**
	 * 
	 * Adds application for user to db. 
	 * 
	 * @param application
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void addUserJobApplication(JobApplication application) throws SQLException, ParseException {
		Connection con = getConnection();
		JobApplicationTable.addUserJobApplication(application, con);
		safeClose(con);
	}
	
	/**
	 * Updates status for application
	 * 
	 * @param applicationID
	 * @param statusID
	 * @throws SQLException
	 */
	public void updateJobApplicationStatus(Long applicationID, Long statusID) throws SQLException {
		Connection con = getConnection();
		JobApplicationTable.updateJobApplicationStatus(applicationID, statusID, con);
		safeClose(con);
	}
	
	/**
	 * Fetches application details for given applicationID from applications table.
	 * App details are populated in an application object.
	 * 
	 * @param applicationID
	 * @return Application object with app details.
	 * @return null if there is no application with given applicationID
	 * @throws SQLException
	 */
	public JobApplication fetchJobApplication(Long applicationID) throws SQLException {
		Connection con = getConnection();
		JobApplication application = JobApplicationTable.fetchJobApplication(applicationID, con);
		safeClose(con);
		return application;
	}
	
	/**
	 * 
	 * Fetches user's job applications' details
	 * 
	 * @param userID
	 * @return List of user's Applications
	 * @throws SQLException
	 */
	public List<JobApplication> fetchUserJobApplications(Long userID) throws SQLException {
		Connection con = getConnection();
		List<JobApplication> applications = JobApplicationTable.fetchUserJobApplications(userID, con);
		safeClose(con);
		return applications;
		
	}
	
	/**
	 * Fetches user's application IDs To Rank mapping having given status.
	 * 
	 * @param userID
	 * @param statusID
	 * @return Map of applicationID to Rank
	 * @throws SQLException
	 */
	public Map<Long, Integer> fetchUserJobApplicationIDsWithRankForJobStatusID(Long userID, Long statusID) throws SQLException {
		Connection con = getConnection();
		Map<Long, Integer> applicationIDToRankMap = JobApplicationTable.fetchUserJobApplicationIDsWithRankForJobStatusID(userID, statusID, con);
		safeClose(con);
		return applicationIDToRankMap;
	}
	
	//*************************************
	// Application Rank Table
	//*************************************
	
	public void addJobApplicationRank(Long applicationID, int rank) throws SQLException {
		Connection con = getConnection();
		JobApplicationRankTable.addJobApplicationRank(applicationID, rank, con);
		safeClose(con);
	}
	
	/**
	 * Updates rank for given applicationIDs
	 * 
	 * @param appIDToRank
	 * @throws SQLException
	 */
	public void updateUserJobApplicationsRanks(Map<Long, Integer> appIDToRank) throws SQLException {
		Connection con = getConnection();
		JobApplicationRankTable.updateJobApplicationsRanks(appIDToRank, con);		
		safeClose(con);
	}
	
	/**
	 * Fetches user's max application rank having statusID 
	 * @param userID
	 * @param statusID
	 * @return max rank of application under having statusID
	 * @throws SQLException
	 */
	public int fetchUsersMaxRankForStatus(Long userID, Long statusID) throws SQLException {
		Connection con = getConnection();
		int maxRank = JobApplicationRankTable.fetchUsersMaxRankForJobStatus(userID, statusID, con);		
		safeClose(con);
		return maxRank;
	}
	
	//*************************************
	// Transaction handling functions
	//*************************************
	
	/**
	 * Starts a transaction by creating a connection and setting autoCommit false.
	 * @throws SQLException
	 */
	public void beginTransaction() throws SQLException {
		if(connection != null) {
			throw new SQLException("A transaction is already in progress");
		}
		connection = DataSourceConnector.getConnection();
		connection.setAutoCommit(false);
		TRANSACTION_IN_PROGRESS = true;
	}

	public void commit() throws SQLException {
		if(connection != null) {
			connection.commit();
		} else {
			throw new SQLException("Empty Connection");
		}
	}
	
	public void rollback() throws SQLException {
		if(connection != null) {
			connection.rollback();
		} else {
			throw new SQLException("Empty Connection");
		}
		
	}
	
	public void endTransaction() throws SQLException {
		if(connection != null) {
			connection.setAutoCommit(true);
			connection.close();
		}
		TRANSACTION_IN_PROGRESS = false;
	}

}

