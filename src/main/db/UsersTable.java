package main.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import main.usermanagement.User;

public class UsersTable implements UsersTableQueries{
	
	static Long add(User user) throws Exception {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long lastInsertId = null;
        try {
	        statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
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
	
	static String getUserPassword(String email) throws Exception {
		Connection connection = DataSourceConnector.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String password = "";
        try {
	        statement = connection.prepareStatement(SELECT_USER_PASSWORD);
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
