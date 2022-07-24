package mycrm.services;

import mycrm.models.ElecCustomerContract;
import mycrm.models.ElectricityConsumption;
import mycrm.repositories.ElectricityConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ElectricityConsumptionServiceJpaImpl implements ElectricityConsumptionService {

	private final ElectricityConsumptionRepository electricityConsumptionRepo;

	@Autowired
	public ElectricityConsumptionServiceJpaImpl(ElectricityConsumptionRepository electricityConsumptionRepo) {
		this.electricityConsumptionRepo = electricityConsumptionRepo;
	}

	@Override
	public List<ElectricityConsumption> findAll() {
		return this.electricityConsumptionRepo.findAll();
	}

	@Override
	public ElectricityConsumption findById(Long id) {
		return this.electricityConsumptionRepo.findOne(id);
	}

	@Override
	public ElectricityConsumption create(ElectricityConsumption electricityConsumption) {
		return this.electricityConsumptionRepo.save(electricityConsumption);
	}

	@Override
	public ElectricityConsumption edit(ElectricityConsumption electricityConsumption) {
		return this.electricityConsumptionRepo.save(electricityConsumption);
	}

	@Override
	public void deleteById(Long id) {
		this.electricityConsumptionRepo.delete(id);
	}

	@Override
	public List<ElectricityConsumption> findByElecCustomerContract(ElecCustomerContract elecCustomerContract) {
		return this.electricityConsumptionRepo.findByElecCustomerContract(elecCustomerContract);
	}

}
