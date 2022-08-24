package mycrm.models;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Indexed
@Table(name = "elec_customer_contract")
public class ElecCustomerContract extends EnergyContract<User> {

    @Field
    @Column(nullable = false, length = 4)
    private final String supplyType = "ELEC";

    @Column(nullable = true, length = 10)
    private double dayRate;

    @Column(nullable = true, length = 10)
    private double nightRate;

    @Column(nullable = true, length = 10)
    private double eveningWeekendRate;

    @Field
    @Column(nullable = true, length = 2)
    private String mpanLineOne;

    @Field
    @Column(nullable = true, length = 3)
    private String mpanLineTwo;

    @Field
    @Column(nullable = true, length = 3)
    private String mpanLineThree;

    public double getDayRate() {
        return dayRate;
    }

    public void setDayRate(double dayRate) {
        this.dayRate = dayRate;
    }

    public double getNightRate() {
        return nightRate;
    }

    public void setNightRate(double nightRate) {
        this.nightRate = nightRate;
    }

    public double getEveningWeekendRate() {
        return eveningWeekendRate;
    }

    public void setEveningWeekendRate(double eveningWeekendRate) {
        this.eveningWeekendRate = eveningWeekendRate;
    }

    public ElecCustomerContract() {
    }

    @Override
    public String getContractID() {
        return "ELEC" + getId();
    }

    @Override
    public String getSupplyType() {
        return supplyType;
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
}
