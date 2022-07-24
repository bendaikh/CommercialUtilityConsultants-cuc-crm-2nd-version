package mycrm.services;

import mycrm.models.GasConsumption;
import mycrm.models.GasCustomerContract;
import mycrm.repositories.GasConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class GasConsumptionServiceJpaImpl implements GasConsumptionService {

	private final GasConsumptionRepository gasConsumptionRepo;

	@Autowired
	public GasConsumptionServiceJpaImpl(GasConsumptionRepository gasConsumptionRepo) {
		this.gasConsumptionRepo = gasConsumptionRepo;
	}

	@Override
	public List<GasConsumption> findAll() {
		return this.gasConsumptionRepo.findAll();
	}

	@Override
	public GasConsumption findById(Long id) {
		return this.gasConsumptionRepo.findOne(id);
	}

	@Override
	public GasConsumption create(GasConsumption gasConsumption) {
		return this.gasConsumptionRepo.save(gasConsumption);
	}

	@Override
	public GasConsumption edit(GasConsumption gasConsumption) {
		return this.gasConsumptionRepo.save(gasConsumption);
	}

	@Override
	public void deleteById(Long id) {
		this.gasConsumptionRepo.delete(id);
	}

	@Override
	public List<GasConsumption> findByGasCustomerContract(GasCustomerContract gasCustomerContract) {
		return this.gasConsumptionRepo.findByGasCustomerContract(gasCustomerContract);
	}

}
