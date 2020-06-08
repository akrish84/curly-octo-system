package main.application;

public enum DefaultApplicationStatuses {
	
	WISHLIST("Wishlist", 2),
	APPLIED("Applied", 1024),
	INTERVIEW("Interview", 2048),
	OFFER("Offers", 4096);
	
	private String status;
	private int rank;
	
	DefaultApplicationStatuses(String status, int rank) {
		this.setStatus(status);
		this.setRank(rank);
		
		
	}

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

}
