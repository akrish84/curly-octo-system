package main.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import main.usermanagement.User;

public class UsersTable implements SqlStatements{
	
	public static Long add(User user) throws Exception {
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
	        System.out.println(statement.toString());
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

}
