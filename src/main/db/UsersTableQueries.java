package main.db;

public interface UsersTableQueries {
	
	// INSERT / UPDATE QUERIES
	public static final String INSERT_USER = "insert into users(email, password, first_name, last_name) values (?, ?, ?, ?)";
	
	
	
	
	// SELECT Queries
	public static final String SELECT_USER_PASSWORD = "Select password from users where email = ?";
	
	
	
	

}
