package main.beans;

public class ApplicationStatus implements Comparable<ApplicationStatus>{
	private String status;
	private int rank;
	private Long id;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
