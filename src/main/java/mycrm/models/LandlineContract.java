package mycrm.models;

import lombok.Getter;
import lombok.Setter;
import mycrm.audit.Auditable;
import mycrm.configuration.TerminalConnectionType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Indexed
@Getter
@Setter
@Entity
@Table(name = "landline_contract")
public class LandlineContract extends Auditable<User>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @IndexedEmbedded(depth = 2)
    private CustomerSite customerSite;

    @ManyToOne(fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Broker broker;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Supplier supplier;

    @Column(nullable = true)
    private int contractMonthlyDuration;

    @Field
    @Column(columnDefinition = "varchar(255) default ''")
    private String phoneNumber;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(length = 10)
    private double terminationFee;

    @Column(length = 10)
    private double monthlyCharge;

    private boolean keepExistingLineNumber;

    private boolean callRecording;

    private boolean autoAttendant;

    private String additionalQuestions;

    @Field
    private String logType;

    private String contractPriority;

    private String contractRenewal;

    private String noOflines;

    @Field
    @Column(nullable = false, length = 1)
    private boolean doNotRenew;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private ContractReason contractReason;

    @Field
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @SortableField
    private Date callbackDateTime;
    @Field
    @Column(nullable = false, length = 1)
    private boolean lostRenewal;

    public boolean isLostRenewal() {
     return lostRenewal;
    }

    public void setLostRenewal(boolean lostRenewal) {
     this.lostRenewal = lostRenewal;
    }

    public CustomerSite getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(CustomerSite customerSite) {
        this.customerSite = customerSite;
    }

    public Long getId() {
        return id;
    }
    public String getSupplyType() {
     return "Landline";
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

    public int getContractMonthlyDuration() {
        return contractMonthlyDuration;
    }

    public void setContractMonthlyDuration(int contractMonthlyDuration) {
        this.contractMonthlyDuration = contractMonthlyDuration;
    }

    public boolean isCurrent() {
     //if any dates are missing then return null
     if (getStartDate() == null || getEndDate() == null) {
      return false;
     }
     // otherwise return the truth
     return getToday().after(getStartDate()) && getToday().before(getEndDate());
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
}
