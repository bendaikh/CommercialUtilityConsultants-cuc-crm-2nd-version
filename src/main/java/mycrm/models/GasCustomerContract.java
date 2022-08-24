package mycrm.models;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Indexed
@Table(name = "gas_customer_contract")
public class GasCustomerContract extends EnergyContract<User> {

    @Field
    @Column(nullable = false, length = 4)
    private final String supplyType = "GAS";

    public GasCustomerContract() {
    }

    @Override
    public String getContractID() {
        return "GAS" + getId();
    }

    @Override
    public String getSupplyType() {
        return supplyType;
    }
}
