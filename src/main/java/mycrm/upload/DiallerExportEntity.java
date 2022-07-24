package mycrm.upload;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "dialler_export")
public class DiallerExportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String leadOwner;
    private String salutation;
    private String firstName;
    private String lastName;
    private String phone;
    private String mobile;
    private String addr1;
    private String addr2;
    private String city;
    private String country;
    private String postCode;
    private String email;
    private String company;
    private String leadSource;
    private String leadStatus;
    private String customerReference;
    private String crmLink;
    private String outcomeCode;
    private String agentSpecific;
    private String agentRef;
    private String callback;
    private String campaign;
    private String dataset;
    private String uniqueId;
    private String contractType;

    @Column
    private LocalDateTime dateCreated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getLeadOwner() {
        return leadOwner;
    }

    public void setLeadOwner(String leadOwner) {
        this.leadOwner = leadOwner;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public String getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(String leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getCrmLink() {
        return crmLink;
    }

    public void setCrmLink(String crmLink) {
        this.crmLink = crmLink;
    }

    public String getOutcomeCode() {
        return outcomeCode;
    }

    public void setOutcomeCode(String outcomeCode) {
        this.outcomeCode = outcomeCode;
    }

    public String getAgentSpecific() {
        return agentSpecific;
    }

    public void setAgentSpecific(String agentSpecific) {
        this.agentSpecific = agentSpecific;
    }

    public String getAgentRef() {
        return agentRef;
    }

    public void setAgentRef(String agentRef) {
        this.agentRef = agentRef;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "DiallerExportEntity{" +
                "id=" + id +
                ", leadOwner='" + leadOwner + '\'' +
                ", salutation='" + salutation + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", addr1='" + addr1 + '\'' +
                ", addr2='" + addr2 + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", postCode='" + postCode + '\'' +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                ", leadSource='" + leadSource + '\'' +
                ", leadStatus='" + leadStatus + '\'' +
                ", customerReference='" + customerReference + '\'' +
                ", crmLink='" + crmLink + '\'' +
                ", outcomeCode='" + outcomeCode + '\'' +
                ", agentSpecific='" + agentSpecific + '\'' +
                ", agentRef='" + agentRef + '\'' +
                ", callback='" + callback + '\'' +
                ", campaign='" + campaign + '\'' +
                ", dataset='" + dataset + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                ", contractType='" + contractType + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
