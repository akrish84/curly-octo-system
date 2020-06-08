package main.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * Class to safely handle a db transaction.
 * Sets auto_commit to false when begin is invoked., and turns it back on when transaciton ends.
 * 
 * 
 * @author akhilesh
 *
 */

public class Transaction {

	Connection con = null;

	public Transaction(Connection con){
		this.con = con;
	}
	
	public void begin() throws SQLException {
		con.setAutoCommit(false);
	}

	public void commit() throws SQLException {
		con.commit();
	}
	
	public void rollback() throws SQLException {
		
	}
	
	public void end() throws SQLException {
		con.setAutoCommit(true);
	}

}
