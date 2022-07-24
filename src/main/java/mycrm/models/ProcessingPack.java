package mycrm.models;

public class ProcessingPack {

    private Long id;
    private boolean contractSentToCustomer;
    private boolean contractReceived;
    private String supplyType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isContractSentToCustomer() {
        return contractSentToCustomer;
    }

    public void setContractSentToCustomer(boolean contractSentToCustomer) {
        this.contractSentToCustomer = contractSentToCustomer;
    }

    public boolean isContractReceived() {
        return contractReceived;
    }

    public void setContractReceived(boolean contractReceived) {
        this.contractReceived = contractReceived;
    }

    public String getSupplyType() {
        return supplyType;
    }

    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }
}
