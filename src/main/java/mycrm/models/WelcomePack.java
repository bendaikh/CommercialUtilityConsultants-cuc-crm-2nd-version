package mycrm.models;

public class WelcomePack {

	private Long id;
	private boolean welcomePackSentToCustomer;
	private boolean previousSupplyTerminated;
	private String supplyType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isWelcomePackSentToCustomer() {
		return welcomePackSentToCustomer;
	}

	public void setWelcomePackSentToCustomer(boolean welcomePackSentToCustomer) {
		this.welcomePackSentToCustomer = welcomePackSentToCustomer;
	}

	public boolean isPreviousSupplyTerminated() {
		return previousSupplyTerminated;
	}

	public void setPreviousSupplyTerminated(boolean previousSupplyTerminated) {
		this.previousSupplyTerminated = previousSupplyTerminated;
	}

	public String getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(String supplyType) {
		this.supplyType = supplyType;
	}
}
