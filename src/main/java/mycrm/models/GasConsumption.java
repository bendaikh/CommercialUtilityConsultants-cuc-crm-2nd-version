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
@Table(name = "gas_consumption")
public class GasConsumption extends Auditable<User> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private GasCustomerContract gasCustomerContract;

	@Column(nullable = false)
	private Long annualConsumption;

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

	public GasCustomerContract getGasCustomerContract() {
		return gasCustomerContract;
	}

	public void setGasCustomerContract(GasCustomerContract gasCustomerContract) {
		this.gasCustomerContract = gasCustomerContract;
	}

	public Long getAnnualConsumption() {
		return annualConsumption;
	}

	public void setAnnualConsumption(Long annualConsumption) {
		this.annualConsumption = annualConsumption;
	}

	public String getConsumptionType() {
		return consumptionType;
	}

	public void setConsumptionType(String consumptionType) {
		this.consumptionType = consumptionType;
	}

	public GasConsumption() {
		super();
	}

}
