package mycrm.models;

import mycrm.audit.Auditable;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "supplier")
@Indexed
public class Supplier extends Auditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column(nullable = true, length = 30, unique = true)
    private String supplierReference;

    @Field
    @Column(nullable = true, length = 150)
    private String businessName;
    @Column(nullable = true, length = 150)
    private String businessAddr;
    @Column(nullable = true, length = 150)
    private String businessAddr1;
    @Column(nullable = true, length = 150)
    private String businessCity;
    @Column(nullable = true, length = 4)
    private String businessPostcodeIn;
    @Column(nullable = true, length = 4)
    private String businessPostcodeOut;
    @Column(nullable = true, length = 100)
    private String firstName;
    @Column(nullable = true, length = 100)
    private String lastName;
    @Column(nullable = true, length = 150)
    private String contactNumber;
    @Column(nullable = true, length = 30)
    private String mobileNumber;
    @Column(nullable = true, length = 150)
    private String emailAddress;
    @Column(nullable = true)
    private String dob;

    @OneToMany(mappedBy = "supplier")
    private Set<GasCustomerContract> gasCustomerContracts = new HashSet<>();

    @OneToMany(mappedBy = "supplier")
    private Set<ElecCustomerContract> elecCustomerContracts = new HashSet<>();

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

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
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

    public Supplier() {

    }
}
