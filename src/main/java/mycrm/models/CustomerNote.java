package mycrm.models;

import mycrm.audit.Auditable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customerNotes")
public class CustomerNote extends Auditable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(nullable = false, length = 5000)
    private String note;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User taggedUser;

    @Column(nullable = true, length = 1)
    private boolean completed;

    @Column(nullable = false, length = 1)
    private boolean quickWin;

    @Column(nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCompleted;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User completedBy;

    @Column(nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dueByDate;

    @Column
    private String priority;

    @Column
    private String relatedTo;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private GasCustomerContract gasCustomerContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private ElecCustomerContract elecCustomerContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private UtilityContract utilityContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private MerchantServicesContract merchantServicesContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private SolarContract solarContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private WaterContract waterContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private VoipContract voipContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private LandlineContract landlineContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private BroadbandContract broadbandContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private MobileContract mobileContract;

    public MobileContract getMobileContract() {
        return mobileContract;
    }

    public void setMobileContract(MobileContract mobileContract) {
        this.mobileContract = mobileContract;
    }

    public BroadbandContract getBroadbandContract() {
        return broadbandContract;
    }

    public void setBroadbandContract(BroadbandContract broadbandContract) {
        this.broadbandContract = broadbandContract;
    }

    public LandlineContract getLandlineContract() {
        return landlineContract;
    }

    public void setLandlineContract(LandlineContract landlineContract) {
        this.landlineContract = landlineContract;
    }

    public VoipContract getVoipContract() {
        return voipContract;
    }

    public void setVoipContract(VoipContract voipContract) {
        this.voipContract = voipContract;
    }

    public WaterContract getWaterContract() {
        return waterContract;
    }

    public void setWaterContract(WaterContract waterContract) {
        this.waterContract = waterContract;
    }

    @OneToMany(mappedBy = "customerNote")
    List<CustomerChildNote> customerChildNoteList;

    public SolarContract getSolarContract() {
        return solarContract;
    }

    public void setSolarContract(SolarContract solarContract) {
        this.solarContract = solarContract;
    }

    public CustomerNote() {
    }

    public MerchantServicesContract getMerchantServicesContract() {
        return merchantServicesContract;
    }

    public void setMerchantServicesContract(MerchantServicesContract merchantServicesContract) {
        this.merchantServicesContract = merchantServicesContract;
    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public User getTaggedUser() {
        return taggedUser;
    }

    public void setTaggedUser(User taggedUser) {
        this.taggedUser = taggedUser;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isQuickWin() {
        return quickWin;
    }

    public void setQuickWin(boolean quickWin) {
        this.quickWin = quickWin;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public User getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(User completedBy) {
        this.completedBy = completedBy;
    }

    public Date getDueByDate() {
        return dueByDate;
    }

    public void setDueByDate(Date dueByDate) {
        this.dueByDate = dueByDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public GasCustomerContract getGasCustomerContract() {
        return gasCustomerContract;
    }

    public void setGasCustomerContract(GasCustomerContract gasCustomerContract) {
        this.gasCustomerContract = gasCustomerContract;
    }

    public ElecCustomerContract getElecCustomerContract() {
        return elecCustomerContract;
    }

    public void setElecCustomerContract(ElecCustomerContract elecCustomerContract) {
        this.elecCustomerContract = elecCustomerContract;
    }

    public UtilityContract getUtilityContract() {
        return utilityContract;
    }

    public void setUtilityContract(UtilityContract utilityContract) {
        this.utilityContract = utilityContract;
    }

    public String getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(String relatedTo) {
        this.relatedTo = relatedTo;
    }

    public List<CustomerChildNote> getCustomerChildNoteList() {
        return customerChildNoteList;
    }

    public void setCustomerChildNoteList(List<CustomerChildNote> customerChildNoteList) {
        this.customerChildNoteList = customerChildNoteList;
    }

    public String getPriorityClass() {
        // show due today in green and older than today in red
        if (getDueByDate() != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            if (format.format(getDueByDate()).equals(format.format(getToday()))) {
                return "bright-green-bg";
            }

            if (getDueByDate().before(getToday())) {
                return "bright-red-bg";
            }
        }

        if (!StringUtils.hasText(priority)) {
            return "";
        }

        switch (priority) {
            case "HIGH":
                return "danger";

            case "MEDIUM":
                return "warning";

            case "LOW":
                return "success";

            default:
                return "";
        }
    }


    public Date getToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);

        return today.getTime();
    }

    @Override
    public String toString() {
        return "CustomerNote{" +
                "id=" + id +
                ", version=" + version +
                ", subject='" + subject + '\'' +
                ", note='" + note + '\'' +
                ", taggedUser=" + taggedUser +
                ", completed=" + completed +
                ", dateCompleted=" + dateCompleted +
                ", completedBy=" + completedBy +
                ", dueByDate=" + dueByDate +
                ", priority='" + priority + '\'' +
                ", relatedTo='" + relatedTo + '\'' +
                '}';
    }
}
