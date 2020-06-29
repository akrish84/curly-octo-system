package main.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import main.db.tables.CompanySuggestionsTable;
import main.db.tables.JobTitleSuggestionsTable;
import main.db.tables.UserApplicationStatusesTable;
import main.db.tables.UsersTable;
import main.beans.Application;
import main.beans.ApplicationStatus;
import main.beans.User;
import main.db.tables.APSSuggestionsTable;
import main.db.tables.ApplicationRankTable;
import main.db.tables.ApplicationTable;

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
				throw new SQLException("Connection is null in between transaction");
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
	
	public void addStatusesForUser(List<ApplicationStatus> statuses, Long userID) throws SQLException {
		Connection con = getConnection();
		UserApplicationStatusesTable.addStatusesForUser(statuses, userID, con);
		safeClose(con);
	}
	
	
	public void updateStatusForUser(ApplicationStatus status, Long userID) throws SQLException {
		Connection con = getConnection();
		UserApplicationStatusesTable.updateStatusForUser(status, userID, con);
		safeClose(con);
	}
	
	/**
	 * Updates statuses in DB for user.
	 * 
	 * @param statuses
	 * @param userID
	 * @throws SQLException
	 */
	
	public void updateStatusesForUser(List<ApplicationStatus> statuses, Long userID) throws SQLException {
		Connection con = getConnection();
		UserApplicationStatusesTable.updateStatusesForUser(statuses, userID, con);
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
	public ApplicationStatus fetchStatus(Long userID, Long statusID) throws SQLException {
		Connection con = getConnection();
		ApplicationStatus status = UserApplicationStatusesTable.fetchStatus(userID, statusID, con);
		safeClose(con);
		return status;
	}
	
	/**
	 * Fetches max rank of status for given user
	 * @param userID
	 * @return max rank of status for user.
	 * @throws SQLException
	 */
	public int fetchMaxStatusRankForUser(Long userID) throws SQLException {
		Connection con = getConnection();
		int maxRank = UserApplicationStatusesTable.fetchMaxStatusRankForUser(userID, con);
		safeClose(con);
		return maxRank;
	}
	
	/**
	 * Fetches user's application statuses.
	 * @param userID
	 * @return Map of statusID to ApplicationStatus
	 * @throws SQLException
	 */
	public Map<Long, ApplicationStatus> fetchApplicationStatusesForUser(Long userID) throws SQLException {
		Connection con = getConnection();
		Map<Long, ApplicationStatus> applicationStatusesForUser = UserApplicationStatusesTable.fetchAllApplicationStatusForUser(userID, con);
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
	public void addUserApplication(Application application) throws SQLException, ParseException {
		Connection con = getConnection();
		ApplicationTable.addUserApplication(application, con);
		safeClose(con);
	}
	
	/**
	 * Updates status for application
	 * 
	 * @param applicationID
	 * @param statusID
	 * @throws SQLException
	 */
	public void updateApplicationStatus(Long applicationID, Long statusID) throws SQLException {
		Connection con = getConnection();
		ApplicationTable.updateApplicationStatus(applicationID, statusID, con);
		safeClose(con);
	}
	 
	/**
	 * 
	 * Fetches user's job applications' details
	 * 
	 * @param userID
	 * @return List of user's Applications
	 * @throws SQLException
	 */
	public List<Application> fetchUserApplications(Long userID) throws SQLException {
		Connection con = getConnection();
		List<Application> applications = ApplicationTable.fetchUserApplications(userID, con);
		safeClose(con);
		return applications;
		
	}
	
	/**
	 * Fetches user's application IDs having given status.
	 * 
	 * @param userID
	 * @param statusID
	 * @return List of application IDs
	 * @throws SQLException
	 */
	public List<Long> fetchUserApplicationIDsForStatus(Long userID, Long statusID) throws SQLException {
		Connection con = getConnection();
		List<Long> applicationIDs = ApplicationTable.fetchUserApplicationIDsForStatus(userID, statusID, con);
		safeClose(con);
		return applicationIDs;
	}
	
	//*************************************
	// Application Rank Table
	//*************************************
	
	public void addApplicationRank(Long applicationID, int rank) throws SQLException {
		Connection con = getConnection();
		ApplicationRankTable.addApplicationRank(applicationID, rank, con);
		safeClose(con);
	}
	
	/**
	 * Updates rank for given applicationIDs
	 * 
	 * @param appIDToRank
	 * @throws SQLException
	 */
	public void updateUserApplicationsRanks(Map<Long, Integer> appIDToRank) throws SQLException {
		Connection con = getConnection();
		ApplicationRankTable.updateApplicationsRanks(appIDToRank, con);		
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
		int maxRank = ApplicationRankTable.fetchUsersMaxRankForStatus(userID, statusID, con);		
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
			throw new SQLException("Transaction already started");
		}
		connection = DataSourceConnector.getConnection();
		connection.setAutoCommit(false);
		TRANSACTION_IN_PROGRESS = true;
	}

	public void commit() throws SQLException {
		if(connection != null) {
			connection.commit();
		}
	}
	
	public void rollback() throws SQLException {
		if(connection != null) {
			connection.rollback();
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

