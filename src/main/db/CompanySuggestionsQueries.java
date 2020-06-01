package main.db;

public interface CompanySuggestionsQueries {
	
	// INSERT / UPDATE QUERIES
		public static final String INSERT_COMPANY_FOR_SUGGESTION = "insert into company_suggestions(name) values (?)";
		
		
		
		
		// SELECT Queries
		public static final String SELECT_COMPANY_SUGGESTIONS = "Select name from company_suggestions";
		

}
