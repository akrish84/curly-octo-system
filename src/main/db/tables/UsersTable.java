package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.db.DataSourceConnector;
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
	private static final String FETCH_USER = "main.db.tables.UsersTabls.getUser";
	
	
	/***
	 * INSERT FUNCTIONS
	 */
	
	/**
	 * adds user details to Users table and sets userID to user object.
	 * @param user
	 * @throws Exception
	 */
	public static void add(User user) throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
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
	 * GET FUNCTIONS
	 */
	
	/**
	 * Fetches user's information from user's table
	 * @param email
	 * @return User
	 * @throws Exception
	 */
	public static User fetchUser(String email) throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(FETCH_USER));
	        statement.setString(1, email);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	User user = new User();
	        	user.setId(resultSet.getLong("id"));
	            user.setEmail(resultSet.getString("email"));
	            user.setFirstName(resultSet.getString("first_name"));
	            user.setLastName(resultSet.getString("last_name"));
	            user.setPassword(resultSet.getString("password"));
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
