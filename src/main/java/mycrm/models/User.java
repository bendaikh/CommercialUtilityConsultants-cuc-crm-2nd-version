package mycrm.models;

import mycrm.audit.Auditable;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends Auditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column(nullable = false, length = 30, unique = true)
    @NotEmpty(message = "*Please provide a username")
    private String username;

    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(length = 255)
    private String passwordHash;

    @Column(length = 100)
    @NotEmpty(message = "*Please provide the user's first name")
    private String firstName;

    @Column(length = 100)
    @NotEmpty(message = "*Please provide the user's last name")
    private String lastName;

    @Column(columnDefinition = "varchar(100) default ''")
    private String diallerAgentReference;

    @Column(name = "active")
    private int active;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    // @JoinColumn(name = "broker_id", insertable = false, updatable = false)
    private Broker broker;

    @OneToMany(mappedBy = "taggedUser")
    private Set<CustomerNote> customerNote = new HashSet<>();

    @OneToMany(mappedBy = "taggedUser")
    private Set<BrokerNote> brokerNote = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Broker> brokers = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Supplier> suppliers = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserStat> userStat = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
        super();
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<Broker> getBrokers() {
        return brokers;
    }

    public void setBrokers(Set<Broker> brokers) {
        this.brokers = brokers;
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

    public String getDiallerAgentReference() {
        return diallerAgentReference;
    }

    public void setDiallerAgentReference(String diallerAgentReference) {
        this.diallerAgentReference = diallerAgentReference;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Broker getBroker() {
        return broker;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public Set<UserStat> getUserStats() {
        return userStat;
    }

    public void setUserStats(Set<UserStat> userStat) {
        this.userStat = userStat;
    }

    public Set<CustomerNote> getCustomerNotes() {
        return customerNote;
    }

    public void setCustomerNotes(Set<CustomerNote> customerNote) {
        this.customerNote = customerNote;
    }

    public Set<BrokerNote> getBrokerNotes() {
        return brokerNote;
    }

    public void setBrokerNotes(Set<BrokerNote> brokerNote) {
        this.brokerNote = brokerNote;
    }

    public boolean isBroker() {
        for (Role r : roles) {
            return r.getRole().equals("BROKER") && broker != null;
        }
        return false;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public boolean isSuperAdmin() {
        for (Role r : roles) {
            return r.getRole().equals("SUPERADMIN");
        }
        return false;
    }

    public boolean isAdmin() {
        for (Role r : roles) {
            return r.getRole().equals("ADMIN");
        }
        return false;
    }

    public boolean isExternalBroker() {
        for (Role r : roles) {
            return r.getRole().equals("EXTERNAL_BROKER");
        }
        return false;
    }

    public boolean isLeads() {
        for (Role r : roles) {
            return r.getRole().equals("LEADS");
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", diallerAgentReference='" + diallerAgentReference + '\'' +
                '}';
    }
}
