package mycrm.models;

import java.util.Date;

public interface Contract {

    String getContractID();

    String getSupplyType();

    Broker getBroker();

    void setBroker(Broker broker);

    String getAccountNumber();

    void setAccountNumber(String accountNumber);

    Supplier getSupplier();

    void setSupplier(Supplier supplier);

    Customer getCustomer();

    void setCustomer(Customer customer);

    CustomerSite getCustomerSite();

    void setCustomerSite(CustomerSite customerSite);

    Date getStartDate();

    void setStartDate(Date startDate);

    Date getEndDate();

    void setEndDate(Date endDate);

    Date getDateCreated();

    void setDateCreated(Date dateCreated);

    String getLogType();

    void setLogType(String logType);

    User getCreatedBy();

    void setCreatedBy(User createdBy);

    String getContractStatus();

    Date getCallbackDateTime();

    int getMonthsRemaining();

    boolean isRenewable();

    boolean isLead();

    boolean isCallback();

    boolean isContractRenewed();

    ContractReason getContractReason();

    boolean isDoNotRenew();

    boolean isLostRenewal();

    String getCampaign();

    void setCampaign(String campaign);

}
