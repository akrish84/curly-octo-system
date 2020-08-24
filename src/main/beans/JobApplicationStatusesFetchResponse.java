package main.beans;

import java.util.Map;

public class JobApplicationStatusesFetchResponse {

	private Map<Long, JobApplicationStatus> jobStatusesMap;

	public Map<Long, JobApplicationStatus> getJobStatusesMap() {
		return jobStatusesMap;
	}

	public void setJobStatusesMap(Map<Long, JobApplicationStatus> jobStatusesMap) {
		this.jobStatusesMap = jobStatusesMap;
	}


}
