package main.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import main.db.tables.CompanySuggestionsTable;
import main.db.tables.JobTitleSuggestionsTable;
import main.db.tables.UserApplicationStatusesTable;
import main.db.tables.UsersTable;
import main.beans.ApplicationStatus;
import main.beans.User;
import main.db.tables.APSSuggestionsTable;

/**
 * 
 * A Facade class which handles all database operations.
 * Operations are forwarded to its respective Table class to process the operation
 * @author akhilesh
 *
 */
public class DataManager {
	
	/**
	 * Forwards request to User Table to add user.
	 * @param user
	 * @throws SQLException
	 */
	public static void addUser(User user) throws SQLException {
		UsersTable.add(user);
	}

	/**
	 * Forwards request to User Table to fetch user information.
	 * 
	 * @param email
	 * @return User details for given email as object
	 * @throws SQLException
	 */
	public static User fetchUser(String email) throws SQLException {
		return UsersTable.fetchUser(email);
	}
	
	/**
	 * Returns a list of company suggestions stored in DB
	 * @return list of company names
	 * @throws SQLException
	 */
	public static List<String> fetchCompanySuggestions() throws SQLException {
		return CompanySuggestionsTable.fetchCompanySuggestions();
	}
	
	/**
	 * Returns a list of job title suggestions stored in DB
	 * @return List of job titles
	 * @throws SQLException
	 */
	public static List<String> fetchJobTitleSuggestions() throws SQLException {
		return JobTitleSuggestionsTable.fetchJobTitleSuggestions();
	}
	
	/**
	 * Returns a list of APS suggestions stored in DB
	 * @return List of APS Suggestions
	 * @throws SQLException
	 */
	public static List<String> fetchAPSSuggestions() throws SQLException {
		return APSSuggestionsTable.fetchApsSuggestions();
	}
	
	/**
	 * Adds/Updates statuses in DB for user.
	 * If status ID is not null or if status.id does not exist in existing user statuses -> status is inserted as new status
	 * If status already present in existing user statuses -> status, rank, userID is updated.
	 * 
	 * @param statuses
	 * @param userID
	 * @throws SQLException
	 */
	
	public static void addStatusesForUser(List<ApplicationStatus> statuses, Long userID) throws SQLException {
		UserApplicationStatusesTable.addStatusesForUser(statuses, userID);
	}
	

	public static Map<Long, ApplicationStatus> getApplicationStatusesForUser(Long userID) throws SQLException {
		return UserApplicationStatusesTable.fetchAllApplicationStatusForUser(userID);
	}

}

