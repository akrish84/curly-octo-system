package main.beans;

public class Job {
	private Long id;
	private String company;
	private String jobTitle;
	private String jobDescription;
	private String aps;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getAps() {
		return aps;
	}
	public void setAps(String aps) {
		this.aps = aps;
	}


}
