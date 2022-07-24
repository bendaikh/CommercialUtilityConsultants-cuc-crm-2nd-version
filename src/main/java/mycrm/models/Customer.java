package mycrm.models;

import mycrm.audit.Auditable;
import mycrm.entitylisteners.CustomerEntityListener;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(CustomerEntityListener.class)
@Indexed
@Table(name = "customer")
public class Customer extends Auditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Field
    @Column(nullable = true, length = 30)
    private String customerReference;

    @Field
    @Column(nullable = true, length = 150)
    private String businessName;

    @Field
    @Column(nullable = true, length = 150)
    private String businessAddr;

    @Column(nullable = true, length = 150)
    private String businessAddr1;

    @Field
    @Column(nullable = true, length = 150)
    private String businessCity;

    @Field
    @Column(nullable = true, length = 4)
    private String businessPostcodeIn;

    @Field
    @Column(nullable = true, length = 4)
    private String businessPostcodeOut;

    @Column(nullable = true, length = 50)
    private String ltdRegNo;

    @Column(nullable = true, length = 1)
    private boolean soleTrader;

    @Field
    @Column(nullable = true, length = 100)
    private String firstName;

    @Field
    @Column(nullable = true, length = 100)
    private String lastName;

    @Field
    @Column(nullable = true, length = 150)
    private String stAddr;

    @Column(nullable = true, length = 150)
    private String stAddr1;

    @Field
    @Column(nullable = true, length = 150)
    private String stCity;

    @Field
    @Column(nullable = true, length = 4)
    private String stPostcodeIn;

    @Field
    @Column(nullable = true, length = 4)
    private String stPostcodeOut;

    @Column(nullable = true, length = 50)
    private String contactNumber;

    @Column(nullable = true, length = 30)
    private String mobileNumber;

    @Column(nullable = true, length = 150)
    private String emailAddress;

    @Column(nullable = true, length = 255)
    private String webAddress;

    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @OneToMany(mappedBy = "customer")
    private Set<GasCustomerContract> gasCustomerContracts = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<ElecCustomerContract> elecCustomerContracts = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<CustomerSite> customerSite = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<CustomerNote> customerNote = new HashSet<>();

    @OneToOne(mappedBy = "customer")
    private BillingDetail billingDetail;

    public boolean contactByPost;

    public boolean contactByEmail;

    public boolean contactByText;

    public boolean contactByFax;

    public boolean contactByPhone;

    @Field
    public boolean doNotContact;

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

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddr() {
        return businessAddr;
    }

    public void setBusinessAddr(String businessAddr) {
        this.businessAddr = businessAddr;
    }

    public String getBusinessAddr1() {
        return businessAddr1;
    }

    public void setBusinessAddr1(String businessAddr1) {
        this.businessAddr1 = businessAddr1;
    }

    public String getBusinessCity() {
        return businessCity;
    }

    public void setBusinessCity(String businessCity) {
        this.businessCity = businessCity;
    }

    public String getBusinessPostcodeIn() {
        return businessPostcodeIn;
    }

    public void setBusinessPostcodeIn(String businessPostcodeIn) {
        this.businessPostcodeIn = businessPostcodeIn;
    }

    public String getBusinessPostcodeOut() {
        return businessPostcodeOut;
    }

    public void setBusinessPostcodeOut(String businessPostcodeOut) {
        this.businessPostcodeOut = businessPostcodeOut;
    }

    public String getLtdRegNo() {
        return ltdRegNo;
    }

    public void setLtdRegNo(String ltdRegNo) {
        this.ltdRegNo = ltdRegNo;
    }

    public boolean isSoleTrader() {
        return soleTrader;
    }

    public void setSoleTrader(boolean soleTrader) {
        this.soleTrader = soleTrader;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStAddr() {
        return stAddr;
    }

    public void setStAddr(String stAddr) {
        this.stAddr = stAddr;
    }

    public String getStAddr1() {
        return stAddr1;
    }

    public void setStAddr1(String stAddr1) {
        this.stAddr1 = stAddr1;
    }

    public String getStCity() {
        return stCity;
    }

    public void setStCity(String stCity) {
        this.stCity = stCity;
    }

    public String getStPostcodeIn() {
        return stPostcodeIn;
    }

    public void setStPostcodeIn(String stPostcodeIn) {
        this.stPostcodeIn = stPostcodeIn;
    }

    public String getStPostcodeOut() {
        return stPostcodeOut;
    }

    public void setStPostcodeOut(String stPostcodeOut) {
        this.stPostcodeOut = stPostcodeOut;
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

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Set<GasCustomerContract> getGasCustomerContracts() {
        return gasCustomerContracts;
    }

    public void setGasCustomerContracts(Set<GasCustomerContract> gasCustomerContracts) {
        this.gasCustomerContracts = gasCustomerContracts;
    }

    public Set<ElecCustomerContract> getElecCustomerContracts() {
        return elecCustomerContracts;
    }

    public void setElecCustomerContracts(Set<ElecCustomerContract> elecCustomerContracts) {
        this.elecCustomerContracts = elecCustomerContracts;
    }

    public Set<CustomerSite> getCustomerSites() {
        return customerSite;
    }

    public void setCustomerSites(Set<CustomerSite> customerSite) {
        this.customerSite = customerSite;
    }

    public Set<CustomerNote> getCustomerNotes() {
        return customerNote;
    }

    public void setCustomerNotes(Set<CustomerNote> customerNote) {
        this.customerNote = customerNote;
    }

    public boolean isContactByPost() {
        return contactByPost;
    }

    public void setContactByPost(boolean contactByPost) {
        this.contactByPost = contactByPost;
    }

    public boolean isContactByEmail() {
        return contactByEmail;
    }

    public void setContactByEmail(boolean contactByEmail) {
        this.contactByEmail = contactByEmail;
    }

    public boolean isContactByText() {
        return contactByText;
    }

    public void setContactByText(boolean contactByText) {
        this.contactByText = contactByText;
    }

    public boolean isContactByFax() {
        return contactByFax;
    }

    public void setContactByFax(boolean contactByFax) {
        this.contactByFax = contactByFax;
    }

    public boolean isContactByPhone() {
        return contactByPhone;
    }

    public void setContactByPhone(boolean contactByPhone) {
        this.contactByPhone = contactByPhone;
    }

    public boolean isDoNotContact() {
        return doNotContact;
    }

    public void setDoNotContact(boolean doNotContact) {
        this.doNotContact = doNotContact;
    }

    public Set<CustomerSite> getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(Set<CustomerSite> customerSite) {
        this.customerSite = customerSite;
    }

    public Set<CustomerNote> getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(Set<CustomerNote> customerNote) {
        this.customerNote = customerNote;
    }

    public BillingDetail getBillingDetail() {
        return billingDetail;
    }

    public void setBillingDetail(BillingDetail billingDetail) {
        this.billingDetail = billingDetail;
    }

    public String getFullBusinessNameAndAddress() {
        StringBuilder s = new StringBuilder();

        if (StringUtils.hasText(businessName)) {
            s.append(StringUtils.trimWhitespace(businessName) + ", ");
        }

        if (StringUtils.hasText(businessAddr)) {
            s.append(StringUtils.trimWhitespace(businessAddr) + ", ");
        }

        if (StringUtils.hasText(businessAddr1)) {
            s.append(StringUtils.trimWhitespace(businessAddr1) + ", ");
        }

        if (StringUtils.hasText(businessCity)) {
            s.append(StringUtils.trimWhitespace(businessCity) + " ");
        }

        if (StringUtils.hasText(businessPostcodeOut)) {
            s.append(StringUtils.trimWhitespace(businessPostcodeOut) + " ");
        }

        if (StringUtils.hasText(businessPostcodeIn)) {
            s.append(StringUtils.trimWhitespace(businessPostcodeIn));
        }
        return s.toString();
    }

    public String getFullBusinessAddress() {
        StringBuilder s = new StringBuilder();

        if (StringUtils.hasText(businessAddr)) {
            s.append(StringUtils.trimWhitespace(businessAddr) + ", ");
        }

        if (StringUtils.hasText(businessAddr1)) {
            s.append(StringUtils.trimWhitespace(businessAddr1) + ", ");
        }

        if (StringUtils.hasText(businessCity)) {
            s.append(StringUtils.trimWhitespace(businessCity) + " ");
        }

        if (StringUtils.hasText(businessPostcodeOut)) {
            s.append(StringUtils.trimWhitespace(businessPostcodeOut) + " ");
        }

        if (StringUtils.hasText(businessPostcodeIn)) {
            s.append(StringUtils.trimWhitespace(businessPostcodeIn));
        }
        return s.toString();
    }

    public String getSoleTraderFullAddress() {
        StringBuilder s = new StringBuilder();

        if (StringUtils.hasText(stAddr)) {
            s.append(StringUtils.trimWhitespace(stAddr) + ", ");
        }

        if (StringUtils.hasText(stAddr1)) {
            s.append(StringUtils.trimWhitespace(stAddr1) + ", ");
        }

        if (StringUtils.hasText(stCity)) {
            s.append(StringUtils.trimWhitespace(stCity) + " ");
        }

        if (StringUtils.hasText(stPostcodeOut)) {
            s.append(StringUtils.trimWhitespace(stPostcodeOut) + " ");
        }

        if (StringUtils.hasText(stPostcodeIn)) {
            s.append(StringUtils.trimWhitespace(stPostcodeIn));
        }
        return s.toString();
    }

    public Customer() {
        super();
    }

    public Customer(String businessName) {
        super();
        this.businessName = businessName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", version=" + version +
                ", customerReference='" + customerReference + '\'' +
                ", businessName='" + businessName + '\'' +
                ", businessAddr='" + businessAddr + '\'' +
                ", businessAddr1='" + businessAddr1 + '\'' +
                ", businessCity='" + businessCity + '\'' +
                ", businessPostcodeIn='" + businessPostcodeIn + '\'' +
                ", businessPostcodeOut='" + businessPostcodeOut + '\'' +
                ", ltdRegNo='" + ltdRegNo + '\'' +
                ", soleTrader=" + soleTrader +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", stAddr='" + stAddr + '\'' +
                ", stAddr1='" + stAddr1 + '\'' +
                ", stCity='" + stCity + '\'' +
                ", stPostcodeIn='" + stPostcodeIn + '\'' +
                ", stPostcodeOut='" + stPostcodeOut + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", webAddress='" + webAddress + '\'' +
                ", dob=" + dob +
                ", contactByPost=" + contactByPost +
                ", contactByEmail=" + contactByEmail +
                ", contactByText=" + contactByText +
                ", contactByFax=" + contactByFax +
                ", contactByPhone=" + contactByPhone +
                ", doNotContact=" + doNotContact +
                '}';
    }
}
