package mycrm.services;

import mycrm.models.ElecCustomerContract;
import mycrm.models.ElectricityConsumption;

import java.util.List;

public interface ElectricityConsumptionService {

    List<ElectricityConsumption> findAll();

    ElectricityConsumption findById(Long id);

    List<ElectricityConsumption> findByElecCustomerContract(ElecCustomerContract elecCustomerContract);

    ElectricityConsumption create(ElectricityConsumption electricityConsumption);

    ElectricityConsumption edit(ElectricityConsumption electricityConsumption);

    void deleteById(Long id);
    
}
