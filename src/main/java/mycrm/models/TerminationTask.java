package mycrm.models;

public class TerminationTask {
	
	private Long id;
	private boolean currentSupplyTerminated;
	private String supplyType;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isCurrentSupplyTerminated() {
		return currentSupplyTerminated;
	}
	
	public void setCurrentSupplyTerminated(boolean currentSupplyTerminated) {
		this.currentSupplyTerminated = currentSupplyTerminated;
	}
	
	public String getSupplyType() {
		return supplyType;
	}
	
	public void setSupplyType(String supplyType) {
		this.supplyType = supplyType;
	}
	
}
