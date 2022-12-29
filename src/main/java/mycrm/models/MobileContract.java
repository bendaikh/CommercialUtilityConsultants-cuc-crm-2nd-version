package mycrm.models;


import lombok.Getter;
import lombok.Setter;
import mycrm.audit.Auditable;
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
@Table(name = "mobile_contract")
public class MobileContract extends Auditable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1)
    private boolean welcomePackSentToCustomer;

    @Column(nullable = false, length = 1)
    private boolean keepExisting;

    @Column(columnDefinition = "varchar(255) default ''")
    private String keepExistingNumber;

    @Column(nullable = false, length = 1)
    private boolean previousSupplyTerminated;

    @Column(nullable = false, length = 1)
    private boolean contractSentToCustomer;

    @Column(nullable = false, length = 1)
    private boolean currentSupplyTerminated;

    @Column(nullable = false, length = 1)
    private boolean contractReceived;

    @Column(columnDefinition = "varchar(255) default ''")
    private String businessName;

    @Column(columnDefinition = "varchar(255) default ''")
    private String fullName;

    @Column(columnDefinition = "varchar(255) default ''")
    private String firstLineAddress;

    @Column(columnDefinition = "varchar(255) default ''")
    private String phone;

    @Column(columnDefinition = "varchar(255) default ''")
    private String email;


    @Column(nullable = false, length = 1)
    private boolean courtesyCall;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Broker broker;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Supplier supplier;

    @Column(nullable = true, length = 10)
    private double monthlyCharge;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Field
    @SortableField
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(nullable = true)
    private int contractMonthlyDuration;

    @Column(nullable = true, length = 10)
    private double brokerUplift;

    @Field
    @Column(nullable = false, length = 40)
    private String logType;

    @Field
    @Column(nullable = true, length = 1)
    private boolean contractRenewed;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private ContractReason contractReason;

    @Field
    @Column(columnDefinition = "varchar(255) default ''")
    private String campaign;

    @Column(columnDefinition = "varchar(255) default ''")
    private String supplierCustomerReferenceNumber;

    @Column(nullable = false, length = 1)
    private boolean directDebit;

    @Column(columnDefinition = "varchar(255) default ''")
    private String accountName;

    @Column(columnDefinition = "varchar(255) default ''")
    private String sortCode;

    @Column(columnDefinition = "varchar(255) default ''")
    private String accountNumber;

    @Field
    @Column(nullable = false, length = 1)
    private boolean doNotRenew;

    @Field
    @Column(nullable = false, length = 1)
    private boolean lostRenewal;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @IndexedEmbedded(depth = 2)
    private CustomerSite customerSite;

    public String getSupplyType() {
        return "Mobile";
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
