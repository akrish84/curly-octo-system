package main.beans;

import java.util.List;

public class JobApplicationsFetchResponse {
	private List<JobApplication> jobApplications;

	public List<JobApplication> getJobApplications() {
		return jobApplications;
	}

	public void setJobApplications(List<JobApplication> jobApplications) {
		this.jobApplications = jobApplications;
	}

}
