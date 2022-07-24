package mycrm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mycrm.models.GasConsumption;
import mycrm.models.GasCustomerContract;

@Repository
public interface GasConsumptionRepository extends JpaRepository<GasConsumption, Long> {
	
	List<GasConsumption> findByGasCustomerContract(GasCustomerContract gasCustomerContract);
    
}
