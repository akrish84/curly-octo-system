package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.db.QueryProvider;
import main.beans.User;

/**
 * Executes Queries on Users table.
 * 
 * @author akhilesh
 *
 */
public class UsersTable {
	
	
	private static final String INSERT_USER = "main.db.tables.UsersTable.insertUser";
	private static final String FETCH_USER = "main.db.tables.UsersTabls.fetchUser";
	
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_EMAIL = "email";
	private static final String COLUMN_FIRST_NAME = "first_name";
	private static final String COLUMN_LAST_NAME = "last_name";
	private static final String COLUMN_PASSWORD = "password";
	
	
	
	/***
	 * INSERT FUNCTIONS
	 */
	
	/**
	 * adds user details to Users table and sets userID to user object.
	 * @param user
	 * @throws Exception
	 */
	public static void add(User user, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long lastInsertId = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(INSERT_USER), Statement.RETURN_GENERATED_KEYS);
	        statement.setString(1, user.getEmail());
	        statement.setString(2, user.getPassword());
	        statement.setString(3, user.getFirstName());
	        statement.setString(4, user.getLastName());
	        int rowsInserted = statement.executeUpdate();
        		if(rowsInserted > 0){
	            resultSet = statement.getGeneratedKeys();
	            resultSet.next();
	            lastInsertId = resultSet.getLong(1);
        	} else {
	    		throw new SQLException("User not added ");
	    	}
        } finally {
    		if(resultSet != null) {
    			resultSet.close();
    		}
    		if(statement != null) {
    			statement.close();
    		}
        }
        user.setId(lastInsertId);
	}
	
	/***
	 * FETCH FUNCTIONS
	 */
	
	/**
	 * Fetches user's information from user's table
	 * @param email
	 * @return User
	 * @throws Exception
	 */
	public static User fetchUser(String email, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_USER));
	        statement.setString(1, email);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	User user = new User();
	        	user.setId(resultSet.getLong(COLUMN_ID));
	            user.setEmail(resultSet.getString(COLUMN_EMAIL));
	            user.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
	            user.setLastName(resultSet.getString(COLUMN_LAST_NAME));
	            user.setPassword(resultSet.getString(COLUMN_PASSWORD));
	            return user;
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
}
