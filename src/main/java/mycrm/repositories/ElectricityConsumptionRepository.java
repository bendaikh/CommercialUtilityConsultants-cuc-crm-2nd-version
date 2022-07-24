package mycrm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mycrm.models.ElecCustomerContract;
import mycrm.models.ElectricityConsumption;

@Repository
public interface ElectricityConsumptionRepository extends JpaRepository<ElectricityConsumption, Long> {
	
	List<ElectricityConsumption> findByElecCustomerContract(ElecCustomerContract elecCustomerContract);
    
}
