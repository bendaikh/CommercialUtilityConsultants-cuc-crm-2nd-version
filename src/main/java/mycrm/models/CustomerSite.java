package mycrm.models;

import mycrm.audit.Auditable;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
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
import java.util.HashSet;
import java.util.Set;

@Entity
@Indexed
@Table(name = "customer_sites")
public class CustomerSite extends Auditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @IndexedEmbedded
    private Customer customer;

    @Field
    @Column(nullable = true, length = 15)
    private String mpr;

    @Field
    @Column(nullable = true, length = 2)
    private String mpanLineOne;

    @Field
    @Column(nullable = true, length = 3)
    private String mpanLineTwo;

    @Field
    @Column(nullable = true, length = 3)
    private String mpanLineThree;

    @Field
    // @FieldBridge(impl = IntegerBridge.class)
    @Column(nullable = true, length = 2)
    private String mpanLineFour;

    @Field
    // @FieldBridge(impl = IntegerBridge.class)
    @Column(nullable = true, length = 4)
    private String mpanLineFive;

    @Field
    // @FieldBridge(impl = IntegerBridge.class)
    @Column(nullable = true, length = 4)
    private String mpanLineSix;

    @Field
    // @FieldBridge(impl = IntegerBridge.class)
    @Column(nullable = true, length = 3)
    private String mpanLineSeven;

    @Field
    @Column(nullable = true, length = 50)
    private String mpanBottomLine;

    @Field
    @Column(nullable = true, length = 150)
    private String siteName;

    @Field
    @Column(nullable = true, length = 150)
    private String siteAddr;

    @Column(nullable = true, length = 150)
    private String siteAddr1;

    @Field
    @Column(nullable = true, length = 150)
    private String siteCity;

    @Field
    @Column(nullable = true, length = 4)
    private String sitePostcodeIn;

    @Field
    @Column(nullable = true, length = 4)
    private String sitePostcodeOut;

    @Column(nullable = true, length = 50)
    private String siteTelephone;

    @OneToMany(mappedBy = "customerSite")
    private Set<GasCustomerContract> gasCustomerContracts = new HashSet<>();

    @OneToMany(mappedBy = "customerSite")
    private Set<ElecCustomerContract> elecCustomerContracts = new HashSet<>();

    @OneToMany(mappedBy = "customerSite")
    private Set<UtilityContract> utilityContracts = new HashSet<>();

    @Field
    @Column(nullable = true, length = 55)
    private String registrationNo;

    @Field
    @Column(columnDefinition = "varchar(55) default 'UNKNOWN'")
    private String propertyType;

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

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddr() {
        return siteAddr;
    }

    public void setSiteAddr(String siteAddr) {
        this.siteAddr = siteAddr;
    }

    public String getSiteAddr1() {
        return siteAddr1;
    }

    public void setSiteAddr1(String siteAddr1) {
        this.siteAddr1 = siteAddr1;
    }

    public String getSiteCity() {
        return siteCity;
    }

    public void setSiteCity(String siteCity) {
        this.siteCity = siteCity;
    }

    public String getSitePostcodeIn() {
        return sitePostcodeIn;
    }

    public void setSitePostcodeIn(String sitePostcodeIn) {
        this.sitePostcodeIn = sitePostcodeIn;
    }

    public String getSitePostcodeOut() {
        return sitePostcodeOut;
    }

    public void setSitePostcodeOut(String sitePostcodeOut) {
        this.sitePostcodeOut = sitePostcodeOut;
    }

    public String getSiteTelephone() {
        return siteTelephone;
    }

    public void setSiteTelephone(String siteTelephone) {
        this.siteTelephone = siteTelephone;
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

    public Set<UtilityContract> getUtilityContracts() {
        return utilityContracts;
    }

    public void setUtilityContracts(Set<UtilityContract> utilityContracts) {
        this.utilityContracts = utilityContracts;
    }

    public String getMpr() {
        return mpr;
    }

    public void setMpr(String mpr) {
        this.mpr = mpr;
    }

    public String getMpanLineOne() {
        return mpanLineOne;
    }

    public void setMpanLineOne(String mpanLineOne) {
        this.mpanLineOne = mpanLineOne;
    }

    public String getMpanLineTwo() {
        return mpanLineTwo;
    }

    public void setMpanLineTwo(String mpanLineTwo) {
        this.mpanLineTwo = mpanLineTwo;
    }

    public String getMpanLineThree() {
        return mpanLineThree;
    }

    public void setMpanLineThree(String mpanLineThree) {
        this.mpanLineThree = mpanLineThree;
    }

    public String getMpanLineFour() {
        return mpanLineFour;
    }

    public void setMpanLineFour(String mpanLineFour) {
        this.mpanLineFour = mpanLineFour;
    }

    public String getMpanLineFive() {
        return mpanLineFive;
    }

    public void setMpanLineFive(String mpanLineFive) {
        this.mpanLineFive = mpanLineFive;
    }

    public String getMpanLineSix() {
        return mpanLineSix;
    }

    public void setMpanLineSix(String mpanLineSix) {
        this.mpanLineSix = mpanLineSix;
    }

    public String getMpanLineSeven() {
        return mpanLineSeven;
    }

    public void setMpanLineSeven(String mpanLineSeven) {
        this.mpanLineSeven = mpanLineSeven;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public CustomerSite() {
    }

    public CustomerSite(Customer customer) {
        super();
        this.customer = customer;
    }

    public String getMpanBottomLine() {
        return mpanBottomLine;
    }

    public void setMpanBottomLine(String mpanBottomLine) {
        this.mpanBottomLine = mpanBottomLine;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getFullSiteNameAndAddress() {
        StringBuilder s = new StringBuilder();

        if (StringUtils.hasText(siteName)) {
            s.append(StringUtils.trimWhitespace(siteName)).append(", ");
        }
        if (StringUtils.hasText(siteAddr)) {
            s.append(StringUtils.trimWhitespace(siteAddr)).append(", ");
        }
        if (StringUtils.hasText(siteAddr1)) {
            s.append(StringUtils.trimWhitespace(siteAddr1)).append(", ");
        }
        if (StringUtils.hasText(siteCity)) {
            s.append(StringUtils.trimWhitespace(siteCity)).append(", ");
        }
        if (StringUtils.hasText(sitePostcodeOut)) {
            s.append(StringUtils.trimWhitespace(sitePostcodeOut)).append(" ");
        }
        if (StringUtils.hasText(sitePostcodeIn)) {
            s.append(StringUtils.trimWhitespace(sitePostcodeIn));
        }
        return s.toString();
    }

    public String getFullAddress() {
        StringBuilder s = new StringBuilder();

        if (StringUtils.hasText(siteAddr)) {
            s.append(StringUtils.trimWhitespace(siteAddr)).append(", ");
        }
        if (StringUtils.hasText(siteAddr1)) {
            s.append(StringUtils.trimWhitespace(siteAddr1)).append(", ");
        }
        if (StringUtils.hasText(siteCity)) {
            s.append(StringUtils.trimWhitespace(siteCity)).append(", ");
        }
        if (StringUtils.hasText(sitePostcodeOut)) {
            s.append(StringUtils.trimWhitespace(sitePostcodeOut)).append(" ");
        }
        if (StringUtils.hasText(sitePostcodeIn)) {
            s.append(StringUtils.trimWhitespace(sitePostcodeIn));
        }
        return s.toString();
    }

    public String getFullMpan() {
        StringBuilder s = new StringBuilder();

        if (StringUtils.hasText(mpanLineOne)) {
            s.append(StringUtils.trimWhitespace(mpanLineOne));
        }
        if (StringUtils.hasText(mpanLineTwo)) {
            s.append(StringUtils.trimWhitespace(mpanLineTwo));
        }
        if (StringUtils.hasText(mpanLineThree)) {
            s.append(StringUtils.trimWhitespace(mpanLineThree)).append(" - ");
        }
        if (StringUtils.hasText(mpanBottomLine)) {
            s.append(StringUtils.trimWhitespace(mpanBottomLine)).append(" ");
        }
        return s.toString();
    }

}
