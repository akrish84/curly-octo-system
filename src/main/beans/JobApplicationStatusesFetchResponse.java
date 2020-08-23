package main.beans;

import java.util.Map;

public class JobApplicationStatusesFetchResponse {

	private Map<Long, ApplicationStatus> jobStatusesMap;

	public Map<Long, ApplicationStatus> getJobStatusesMap() {
		return jobStatusesMap;
	}

	public void setJobStatusesMap(Map<Long, ApplicationStatus> jobStatusesMap) {
		this.jobStatusesMap = jobStatusesMap;
	}


}
