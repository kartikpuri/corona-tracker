package models;

public class LocationStats {

	private String state;
	private String country;
	private int latestCasesCount;
	private int previousCasesCount;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getLatestCasesCount() {
		return latestCasesCount;
	}
	public void setLatestCasesCount(int latestCasesCount) {
		this.latestCasesCount = latestCasesCount;
	}
	public int getPreviousCasesCount() {
		return previousCasesCount;
	}
	public void setPreviousCasesCount(int previousCasesCount) {
		this.previousCasesCount = previousCasesCount;
	}
	
	@Override
	public String toString() {
		return "LocationStats [state=" + state + ", country=" + country + ", latestCasesCount=" + latestCasesCount
				+ ", previousCasesCount=" + previousCasesCount + "]";
	}
	
}
