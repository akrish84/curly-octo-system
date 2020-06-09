package main.beans;

import java.io.Serializable;

public class ApplicationStatus implements Comparable<ApplicationStatus>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5738067862182112950L;

	public static final int DEFAULT_RANK_GAP = 2014;
	
	private String status;
	private int rank;
	private Long id;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ApplicationStatus [status=" + status + ", rank=" + rank + ", id=" + id + "]";
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public int compareTo(ApplicationStatus o) {
		return Integer.compare(this.rank, o.rank);
	}	
	
}
