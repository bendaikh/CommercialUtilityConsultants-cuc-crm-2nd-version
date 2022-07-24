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
@Table(name = "brokers")
@Indexed
public class Broker extends Auditable<User> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;

    @Field
    @Column(nullable = false, length = 30)
    private String brokerReference;

    @Field
    @Column(nullable = false, length = 150)
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

	@Column(nullable = true, length = 150)
	private String stAddr;

	@Column(nullable = true, length = 150)
	private String stAddr1;

	@Column(nullable = true, length = 150)
	private String stCity;

	@Column(nullable = true, length = 4)
	private String stPostcodeIn;

	@Column(nullable = true, length = 4)
	private String stPostcodeOut;

	@Column(nullable = true, length = 30)
	private String contactNumber;

	@Column(nullable = true, length = 30)
	private String mobileNumber;

	@Column(nullable = true, length = 150)
	private String emailAddress;

	@Column(nullable = true, length = 255)
	private String webAddress;

	@Column(nullable = true)
	private String dob;

	@OneToMany(mappedBy = "broker")
	private Set<GasCustomerContract> gasCustomerContracts = new HashSet<>();

	@OneToMany(mappedBy = "broker")
	private Set<ElecCustomerContract> elecCustomerContracts = new HashSet<>();

	@OneToMany(mappedBy = "broker")
	private Set<User> connectedUser = new HashSet<>();

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

	public boolean getSoleTrader() {
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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getBrokerReference() {
		return brokerReference;
	}

	public void setBrokerReference(String brokerReference) {
		this.brokerReference = brokerReference;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
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

	public Set<User> getConnectedUser() {
		return connectedUser;
	}

	public void setConnectedUser(Set<User> connectedUser) {
		this.connectedUser = connectedUser;
	}

	public Broker() {
	}

	@Override
	public String toString() {
		return "Broker [id=" + id + "]";
	}

}
