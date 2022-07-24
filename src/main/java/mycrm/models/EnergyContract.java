package mycrm.models;

import mycrm.audit.Auditable;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@MappedSuperclass
public abstract class EnergyContract<U> extends Auditable<User> implements Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Broker broker;

    @Field
    @Column(nullable = false, length = 30, unique = false)
    private String accountNumber;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Supplier supplier;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    // @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    @IndexedEmbedded
    private Customer customer;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    // @JoinColumn(name = "customer_site_id", insertable = false, updatable = false)
    @IndexedEmbedded
    private CustomerSite customerSite;

    @Column(nullable = true, length = 10)
    private double unitRate;

    @Column(nullable = true, length = 10)
    private double standingCharge;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Field
    @SortableField
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(nullable = true, length = 10)
    private double brokerUplift;

    @Column(nullable = true)
    private long estimatedAnnualQuantity;

    @Column(nullable = true, length = 10)
    private Long commissionDuration;

    @Field
    @Column(nullable = false, length = 40)
    private String logType;

    @Column(nullable = true, length = 40)
    private double upfrontPercentage;

    @Column(nullable = true, length = 5000)
    private String notes;

    @Column(nullable = true, length = 1)
    private boolean welcomePackSentToCustomer;

    @Column(nullable = true, length = 1)
    private boolean previousSupplyTerminated;

    @Column(nullable = true, length = 1)
    private boolean contractSentToCustomer;

    @Column(nullable = true, length = 1)
    private boolean currentSupplyTerminated;

    @Column(nullable = false, length = 1)
    private boolean contractReceived;

    @Column(nullable = false, length = 1)
    private boolean courtesyCall;

    @Field
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @SortableField
    private Date callbackDateTime;

    @Column(nullable = true)
    private int contractMonthlyDuration;

    @Column(nullable = true)
    private String standingChargeFrequency;

    @Field
    @Column(nullable = true, length = 1)
    private boolean contractRenewed;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private ContractReason contractReason;

    @Field
    @Column(nullable = false, length = 1)
    private boolean doNotRenew;

    @Field
    @Column(nullable = false, length = 1)
    private boolean lostRenewal;

    @Field
    @Column(columnDefinition = "varchar(255) default ''")
    private String campaign;

    @Column(columnDefinition = "varchar(255) default ''")
    private String meterSerialNumber;

    @Column(columnDefinition = "varchar(255) default ''")
    private String supplierCustomerReferenceNumber;

    @Column(nullable = false, length = 1)
    private boolean directDebit;

    @Column(nullable = false, length = 1)
    private boolean smartMeter;

    @Column(nullable = false)
    private String contractPriority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerSite getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(CustomerSite customerSite) {
        this.customerSite = customerSite;
    }

    public double getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(double unitRate) {
        this.unitRate = unitRate;
    }

    public double getStandingCharge() {
        return standingCharge;
    }

    public void setStandingCharge(double standingCharge) {
        this.standingCharge = standingCharge;
    }

    public double getBrokerUplift() {
        return brokerUplift;
    }

    public void setBrokerUplift(double brokerUplift) {
        this.brokerUplift = brokerUplift;
    }

    public long getEstimatedAnnualQuantity() {
        return estimatedAnnualQuantity;
    }

    public void setEstimatedAnnualQuantity(long estimatedAnnualQuantity) {
        this.estimatedAnnualQuantity = estimatedAnnualQuantity;
    }

    public Long getCommissionDuration() {
        return commissionDuration;
    }

    public void setCommissionDuration(Long commissionDuration) {
        this.commissionDuration = commissionDuration;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public double getUpfrontPercentage() {
        return upfrontPercentage;
    }

    public void setUpfrontPercentage(double upfrontPercentage) {
        this.upfrontPercentage = upfrontPercentage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Date getCallbackDateTime() {
        return callbackDateTime;
    }

    public void setCallbackDateTime(Date callbackDateTime) {
        this.callbackDateTime = callbackDateTime;
    }

    public Date getStartDate() {
        return startDate;
        // return startDate !=null ? startDate : new Date();
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

    public String getStandingChargeFrequency() {
        return standingChargeFrequency;
    }

    public void setStandingChargeFrequency(String standingChargeFrequency) {
        this.standingChargeFrequency = standingChargeFrequency;
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

    public boolean isCourtesyCall() {
        return courtesyCall;
    }

    public void setCourtesyCall(boolean courtesyCall) {
        this.courtesyCall = courtesyCall;
    }

    public boolean isContractReceived() {
        return contractReceived;
    }

    public void setContractReceived(boolean contractReceived) {
        this.contractReceived = contractReceived;
    }

    @Override
    public String getCampaign() {
        return campaign;
    }

    @Override
    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getMeterSerialNumber() {
        return meterSerialNumber;
    }

    public void setMeterSerialNumber(String meterSerialNumber) {
        this.meterSerialNumber = meterSerialNumber;
    }

    public String getSupplierCustomerReferenceNumber() {
        return supplierCustomerReferenceNumber;
    }

    public void setSupplierCustomerReferenceNumber(String supplierCustomerReferenceNumber) {
        this.supplierCustomerReferenceNumber = supplierCustomerReferenceNumber;
    }

    public boolean isDirectDebit() {
        return directDebit;
    }

    public void setDirectDebit(boolean directDebit) {
        this.directDebit = directDebit;
    }

    public boolean isSmartMeter() {
        return smartMeter;
    }

    public void setSmartMeter(boolean smartMeter) {
        this.smartMeter = smartMeter;
    }

    public String getContractPriority() {
        return contractPriority;
    }

    public void setContractPriority(String contractPriority) {
        this.contractPriority = contractPriority;
    }

    public String getContractStatus() {
        if (getEndDate() != null && getEndDate().before(getToday())) {
            return "ENDED";
        }
        return "UNKNOWN";
    }

    public Date getToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);

        return today.getTime();
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
            if (getEndDate() != null && getEndDate().after(getToday())) {
                return true;
            }
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
