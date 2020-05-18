package main.db;

public interface SqlStatements {
	
	public static final String INSERT_USER = "insert into users(email, password, first_name, last_name) values (?, ?, ?, ?)";

}
