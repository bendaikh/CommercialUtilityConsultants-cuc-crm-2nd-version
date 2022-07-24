package mycrm.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "do_not_contact_numbers")
public class DoNotContactNumber {

    @Id
    @Column
    private Long customerId;

    @Column
    private String customerReference;

    @Column
    private String number;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "DoNotCallNumber{" +
                "customerId=" + customerId +
                ", customerReference='" + customerReference + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
