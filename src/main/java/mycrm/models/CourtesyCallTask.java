package mycrm.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "courtesy_call_tasks")
public class CourtesyCallTask {

    @Id
    private String id;

    @Column(name = "contract_id")
    private Long contractId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    private String supplyType;

    private boolean courtesyCall;

    private Date startDate;

    private Date endDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CustomerSite customerSite;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Broker broker;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getSupplyType() {
        return supplyType;
    }

    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }

    public boolean isCourtesyCall() {
        return courtesyCall;
    }

    public void setCourtesyCall(boolean courtesyCall) {
        this.courtesyCall = courtesyCall;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public CustomerSite getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(CustomerSite customerSite) {
        this.customerSite = customerSite;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }
}
