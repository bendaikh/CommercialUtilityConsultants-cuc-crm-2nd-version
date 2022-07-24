package mycrm.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_history")
public class EmailHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String emailAddress;

    @Column
    private String emailType;

    @ManyToOne
    private GasCustomerContract gasCustomerContract;

    @ManyToOne
    private ElecCustomerContract elecCustomerContract;

    @Column
    private LocalDateTime dateCreated;

    @Column
    private String process;

    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
