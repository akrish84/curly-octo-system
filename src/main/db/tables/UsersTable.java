package main.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.db.DataSourceConnector;
import main.db.QueryProvider;
import main.usermanagement.User;

/**
 * Executes Queries on Users table.
 * 
 * @author akhilesh
 *
 */
public class UsersTable {
	
	
	private static final String INSERT_USER = "main.db.tables.UsersTable.insertUser";
	private static final String GET_USER_PASSWORD = "main.db.tables.UsersTabls.getUserPassword";
	
	
	/***
	 * INSERT FUNCTIONS
	 */
	
	/**
	 * adds user details to Users table.
	 * @param user
	 * @return id of user.
	 * @throws Exception
	 */
	public static Long add(User user) throws SQLException {
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
        		statement.close();
        }
        return lastInsertId;
	}
	
	/***
	 * GET FUNCTIONS
	 */
	
	/**
	 * Fetches user's password from user's table
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static String getUserPassword(String email) throws SQLException {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String password = "";
        try {
	        statement = connection.prepareStatement(QueryProvider.getQuery(GET_USER_PASSWORD));
	        statement.setString(1, email);
	        resultSet = statement.executeQuery();
	        while(resultSet.next()){
	            password = resultSet.getString(1);
	        }
	        
        } finally {
        		if(resultSet != null) {
        			resultSet.close();
        		}
        		statement.close();
        }
        return password;
	}
}
