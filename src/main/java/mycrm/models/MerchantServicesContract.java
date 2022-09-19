package mycrm.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mycrm.audit.Auditable;
import mycrm.configuration.ContractPriority;
import mycrm.configuration.ProofOfBusiness;
import mycrm.configuration.ProofOfIdentity;
import mycrm.configuration.TerminalConnectionType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.springframework.format.annotation.DateTimeFormat;

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

@Indexed
@Getter
@Setter
@Entity
@Table(name = "merchant_services_contract")
public class MerchantServicesContract extends Auditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @IndexedEmbedded(depth = 2)
    private CustomerSite customerSite;

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

    @Column(nullable = false, length = 1)
    private boolean verbal;

    // below are from merchant form

    @Field
    @Column(columnDefinition = "varchar(255) default ''")
    private String merchantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Broker broker;

    @Field
    @Column(columnDefinition = "varchar(255) default ''")
    private String currentSupplier;

    @Column(length = 10)
    private double analyticCharge;

    @Column(length = 10)
    private double complianceCharge;

    @Column(length = 10)
    private double fasterPayment;

    @Column(length = 10)
    private double transactionAuthorisationCharge;

    @Column(length = 10)
    private double creditCardCharge; // "%"

    @Column(length = 10)
    private double debitCardCharge; // "%"

    private boolean preAuthorisationCustomerNotPresent;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String terminalMakeModel;

    private TerminalConnectionType terminalConnectionType;

    private String chargingStation;

    private int numberOfTerminals;

    private int averageTransaction;

    private int annualTurnover;

    private double f2fPercentage;

    private double overPhonePercentage;

    private String customerName;

    private String legalTradingName;

    private String legalAddress;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date incorporationDate;

    private String companyRegistration;

    private String vatNumber;

    private String nationalInsuranceNumber;

    private String corporationTaxNumber;

    private String businessDescription;

    private ProofOfIdentity proofOfIdentity;

    private ProofOfBusiness proofOfBusiness;

    private boolean proofOfBankStatementProvided;

    @Field
    private String logType;

    private ContractPriority contractPriority;

    private String contractRenewal;

    @Field
    @Column(nullable = false, length = 1)
    private boolean doNotRenew;

    private String contractReason;

    @Field
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @SortableField
    private Date callbackDateTime;

    private boolean machineAppliesPaymentsAutomatically;

    @Column(length = 5000)
    private String notes;

    @Field
    @Column(nullable = false, length = 1)
    private boolean lostRenewal;

    public boolean isLostRenewal() {
        return lostRenewal;
    }

    public void setLostRenewal(boolean lostRenewal) {
        this.lostRenewal = lostRenewal;
    }

    public String getSupplyType() {
        return "MERCHANT_SERVICES";
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
        if (contractPriority != null) {
            switch (contractPriority) {
                case HIGH:
                    return "danger";
                case MEDIUM:
                    return "success";
                default:
                    return "";
            }
        }
        return "";
    }
}
