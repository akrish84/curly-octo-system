package main.startup;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import main.db.DataSourceConnector;
import main.db.QueryProvider;
import main.util.ConfigurationProperties;
import main.util.Utils;

public class Startup extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public void init() throws ServletException {
		try {
			System.out.println("-----------------------------------------");
			System.out.println("STARTING SERVER");
			System.out.println("-----------------------------------------");
			Utils.setRunningOnServer(true);
			
			
			System.out.println("-----------------------------------------");
			System.out.println("INITIALIZING CONFIGURATION PROPERTIES");
			ConfigurationProperties.init();
			System.out.println("SUCCESSFUL");
			System.out.println("-----------------------------------------");
		
			
			System.out.println("-----------------------------------------");
			System.out.println("INITIALIZING DATABASE");
			DataSourceConnector.createConnectionPool();
			System.out.println("SUCCESSFUL");
			System.out.println("-----------------------------------------");
			
			System.out.println("-----------------------------------------");
			System.out.println("INITIALIZING QUERY MODULE");
			QueryProvider.init();
			System.out.println("SUCCESSFUL");
			System.out.println("-----------------------------------------");
			
			System.out.println("------------------------------------------------------------------------------------------------------------------------");
			System.out.println("                                       SERVER STARTUP SUCCESSFUL                                                        ");
			System.out.println("------------------------------------------------------------------------------------------------------------------------");
			
			
			
		} catch (Exception e) {
			System.out.println("SERVER STARTUP FAIELD");
			throw new ServletException(e);
		}
	}

}
