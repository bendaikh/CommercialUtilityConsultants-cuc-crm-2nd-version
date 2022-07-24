package mycrm.services;

import mycrm.models.GasConsumption;
import mycrm.models.GasCustomerContract;

import java.util.List;

public interface GasConsumptionService {

    List<GasConsumption> findAll();

    GasConsumption findById(Long id);

    List<GasConsumption> findByGasCustomerContract(GasCustomerContract gasCustomerContract);

    GasConsumption create(GasConsumption gasConsumption);

    GasConsumption edit(GasConsumption gasConsumption);

    void deleteById(Long id);
    
}
