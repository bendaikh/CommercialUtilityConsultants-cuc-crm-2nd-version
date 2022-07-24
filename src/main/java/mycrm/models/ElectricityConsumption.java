package mycrm.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import mycrm.audit.Auditable;

@Entity
@Table(name = "elec_consumption")
public class ElectricityConsumption extends Auditable<User> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ElecCustomerContract elecCustomerContract;

	@Column(nullable = false)
	private double annualConsumption;

	@Column(nullable = false)
	private String consumptionType; // actual or estimate

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

	public ElecCustomerContract getElecCustomerContract() {
		return elecCustomerContract;
	}

	public void setElecCustomerContract(ElecCustomerContract elecCustomerContract) {
		this.elecCustomerContract = elecCustomerContract;
	}

	public double getAnnualConsumption() {
		return annualConsumption;
	}

	public void setAnnualConsumption(double annualConsumption) {
		this.annualConsumption = annualConsumption;
	}

	public String getConsumptionType() {
		return consumptionType;
	}

	public void setConsumptionType(String consumptionType) {
		this.consumptionType = consumptionType;
	}

	public ElectricityConsumption() {
		super();
	}
}
