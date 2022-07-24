package mycrm.models;

import mycrm.audit.Auditable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "contact")
public class Contact extends Auditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    @Column
    private String contactName;

    @Column(length = 50)
    private String contactNumber;

    @Column(length = 30)
    private String mobileNumber;

    @Column(length = 150)
    private String emailAddress;

    @Column(length = 150)
    private String note;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", version=" + version +
                ", contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", note='" + note + '\'' +
                ", createdBy=" + createdBy +
                ", dateCreated=" + dateCreated +
                ", lastModifiedBy=" + lastModifiedBy +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
