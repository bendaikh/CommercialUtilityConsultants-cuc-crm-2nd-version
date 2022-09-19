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
@Table(name = "water_contract")
public class WaterContract extends Auditable<User>{
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

    @Field
    @Column(columnDefinition = "varchar(255) default ''")
    private String spid;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Field
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Field
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @SortableField
    private Date duration;

    @Field
    @Column(columnDefinition = "varchar(255) default ''")
    private String comms;

    @Field
    @Column(columnDefinition = "varchar(255) default ''")
    private String savingsMade;

    @Field
    private String logType;

    private String contractPriority;

    private String contractRenewal;

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

    @Column(nullable = true, length = 5000)
    private String notes;

    public String getSupplyType() {
        return "water";
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
