package mycrm.models;

import mycrm.audit.Auditable;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Indexed
@Table
public class UtilityContract extends Auditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field
    private String utilityType;

    @Field
    private String logType;

    @Field
    private String productHeld;

    @Field
    private String accountNumber;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Supplier supplier;

    @Field
    private double currentSupplierPrice;

    @Field
    private String campaign;

    @Field
    private boolean contractRenewed;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private ContractReason contractReason;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Broker broker;

    private int numberOfLines;

    @Field
    @Column(nullable = false, length = 1)
    private boolean doNotRenew;

    @Field
    @Column(nullable = false, length = 1)
    private boolean lostRenewal;

    @Column(nullable = true, length = 5000)
    private String notes;

    @Column(nullable = true)
    private int contractMonthlyDuration;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @IndexedEmbedded(depth = 2)
    private CustomerSite customerSite;

    private String contractPriority;

    @Field
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @SortableField
    private Date callbackDateTime;

    @Column(nullable = false, length = 1)
    private boolean welcomePackSentToCustomer;

    @Column(nullable = false, length = 1)
    private boolean previousSupplyTerminated;

    @Column(nullable = false, length = 1)
    private boolean contractSentToCustomer;

    @Column(nullable = false, length = 1)
    private boolean currentSupplyTerminated;

    @Column(nullable = false, length = 1)
    private boolean contractReceived;

    @Column(nullable = false, length = 1)
    private boolean courtesyCall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUtilityType() {
        return utilityType;
    }

    public void setUtilityType(String utilityType) {
        this.utilityType = utilityType;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getProductHeld() {
        return productHeld;
    }

    public void setProductHeld(String productHeld) {
        this.productHeld = productHeld;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public double getCurrentSupplierPrice() {
        return currentSupplierPrice;
    }

    public void setCurrentSupplierPrice(double currentSupplierPrice) {
        this.currentSupplierPrice = currentSupplierPrice;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public boolean isContractRenewed() {
        return contractRenewed;
    }

    public void setContractRenewed(boolean contractRenewed) {
        this.contractRenewed = contractRenewed;
    }

    public ContractReason getContractReason() {
        return contractReason;
    }

    public void setContractReason(ContractReason contractReason) {
        this.contractReason = contractReason;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public boolean isDoNotRenew() {
        return doNotRenew;
    }

    public void setDoNotRenew(boolean doNotRenew) {
        this.doNotRenew = doNotRenew;
    }

    public boolean isLostRenewal() {
        return lostRenewal;
    }

    public void setLostRenewal(boolean lostRenewal) {
        this.lostRenewal = lostRenewal;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public int getContractMonthlyDuration() {
        return contractMonthlyDuration;
    }

    public void setContractMonthlyDuration(int contractMonthlyDuration) {
        this.contractMonthlyDuration = contractMonthlyDuration;
    }

    public CustomerSite getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(CustomerSite customerSite) {
        this.customerSite = customerSite;
    }

    public String getContractPriority() {
        return contractPriority;
    }

    public void setContractPriority(String contractPriority) {
        this.contractPriority = contractPriority;
    }

    public Date getCallbackDateTime() {
        return callbackDateTime;
    }

    public void setCallbackDateTime(Date callbackDateTime) {
        this.callbackDateTime = callbackDateTime;
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

    public boolean isContractSentToCustomer() {
        return contractSentToCustomer;
    }

    public void setContractSentToCustomer(boolean contractSentToCustomer) {
        this.contractSentToCustomer = contractSentToCustomer;
    }

    public boolean isCurrentSupplyTerminated() {
        return currentSupplyTerminated;
    }

    public void setCurrentSupplyTerminated(boolean currentSupplyTerminated) {
        this.currentSupplyTerminated = currentSupplyTerminated;
    }

    public boolean isContractReceived() {
        return contractReceived;
    }

    public void setContractReceived(boolean contractReceived) {
        this.contractReceived = contractReceived;
    }

    public boolean isCourtesyCall() {
        return courtesyCall;
    }

    public void setCourtesyCall(boolean courtesyCall) {
        this.courtesyCall = courtesyCall;
    }

    public Date getToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);

        return today.getTime();
    }

    public String getContractStatus() {
        if (getEndDate() != null && getEndDate().before(getToday())) {
            return "ENDED";
        }
        return "UNKNOWN";
    }

    public boolean isCurrent() {
        //if any dates are missing then return null
        if (getStartDate() == null || getEndDate() == null) {
            return false;
        }
        // otherwise return the truth
        return getToday().after(getStartDate()) && getToday().before(getEndDate());
    }

    public int getMonthsRemaining() {

        if (getEndDate() != null && getEndDate().after(getToday())) {
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(getToday());
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(getEndDate());

            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

            return diffMonth;
        }
        return 0;
    }

    public boolean isRenewable() {
        if (getLogType() != null && "LIVE".equals(getLogType())) {
            return getEndDate() != null && getEndDate().after(getToday());
        }
        return false;
    }

    public boolean isLead() {
        return getLogType() != null && "POTENTIAL".equals(getLogType());
    }

    public boolean isCallback() {
        return getLogType() != null && "CALLBACK".equals(getLogType());
    }

    public boolean isObjected() {
        return getLogType() != null && "OBJECTED".equals(getLogType());
    }

    public boolean isProcessing() {
        return getLogType() != null && "PROCESSING".equals(getLogType());
    }

    public String getContractPriorityClass() {
        if (StringUtils.hasText(contractPriority)) {
            switch (contractPriority) {
                case ("HIGH"):
                    return "danger";
                case ("MEDIUM"):
                    return "success";
                default:
                    return "";
            }
        }
        return "";
    }
}
