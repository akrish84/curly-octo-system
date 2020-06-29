package main.beans;

public class Application {
	
	private Long id;
	private String companyName;
	private String jobTitle;
	private String jobDescription;
	private String aps;
	private Long statusID;
	private String appliedDate;
	private Long resumeID;
	private Long userID;
	private int rank;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public Long getStatusID() {
		return statusID;
	}
	public void setStatusID(Long statusID) {
		this.statusID = statusID;
	}
	public String getAppliedDate() {
		return appliedDate;
	}
	public void setAppliedDate(String appliedDate) {
		this.appliedDate = appliedDate;
	}
	public Long getResumeID() {
		return resumeID;
	}
	public void setResumeID(Long resumeID) {
		this.resumeID = resumeID;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@Override
	public String toString() {
		return "Application [id=" + id + ", companyName=" + companyName + ", jobTitle=" + jobTitle + ", jobDescription="
				+ jobDescription + ", aps=" + aps + ", statusID=" + statusID + ", appliedDate=" + appliedDate
				+ ", resumeID=" + resumeID + ", userID=" + userID + ", rank=" + rank + "]";
	}

}
