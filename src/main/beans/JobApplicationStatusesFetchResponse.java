package main.beans;

import java.util.Map;

public class JobApplicationStatusesFetchResponse {

	private Map<Long, ApplicationStatus> statusesMap;

	public Map<Long, ApplicationStatus> getStatusesMap() {
		return statusesMap;
	}

	public void setStatusesMap(Map<Long, ApplicationStatus> statusesMap) {
		this.statusesMap = statusesMap;
	}


}
