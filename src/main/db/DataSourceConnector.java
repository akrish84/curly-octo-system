package main.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.zaxxer.hikari.HikariDataSource;

import main.util.ConfigurationProperties;

/**
 * Creates a DataSource with connection pooling
 * 
 * @author akhilesh
 *
 */
public class DataSourceConnector {
	
	private static HikariDataSource source = null;
	private static String KEY_DRIVER_CLASS_NAME = "datasource.driver.class.name";
	private static String KEY_URL = "datasource.url";
	private static String KEY_DB_NAME = "datasource.database.name";
	private static String KEY_USERNAME = "datasource.username";
	private static String KEY_PASSWORD = "datasource.password";
	private static String KEY_POOL_SIZE = "datasource.pool.size";

	

	/**
	 * Create a connection pool to a database with configurations specified in config.properties
	 * @throws Exception
	 */
	private static void createConnectionPool() throws Exception {
		try {
			String driverClassName = ConfigurationProperties.getConfiguration(KEY_DRIVER_CLASS_NAME);
			String url = ConfigurationProperties.getConfiguration(KEY_URL);
			String dbName = ConfigurationProperties.getConfiguration(KEY_DB_NAME);
			String username = ConfigurationProperties.getConfiguration(KEY_USERNAME);
			String password = ConfigurationProperties.getConfiguration(KEY_PASSWORD);
			int poolSize = Integer.parseInt(ConfigurationProperties.getConfiguration(KEY_POOL_SIZE));
			url = url + "/" + dbName+"?autoReconnect=true";
			
			source = new HikariDataSource();
			source.setJdbcUrl(url);
			source.setUsername(username);
			source.setPassword(password);
			source.setDriverClassName(driverClassName);
			source.addDataSourceProperty("cachePrepStmts", "true");
			source.addDataSourceProperty("prepStmtCacheSize", "250");
			source.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			source.setMaximumPoolSize(poolSize);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Internal Server Error");
		}
	}
	
	/**
	 * 
	 * @return Instance of the DataSource connection created
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		if(source == null) {
			createConnectionPool();
		}
		return source.getConnection();
	}
	
	/**
	 * Used to test connection
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			createConnectionPool();
			String sql = "insert into temp values ( 1)";
			Connection connection = source.getConnection();
	        PreparedStatement statement = null;
	        ResultSet resultSet = null;
	        try {
		        statement = connection.prepareStatement(sql);
		        int rowsInserted = statement.executeUpdate();
	        } finally {
	        		if(resultSet != null) {
	        			resultSet.close();
	        		}
	        		statement.close();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
