package mycrm.services;

import mycrm.models.*;

import java.util.List;

public interface WaterContractService {
    WaterContract save(WaterContract waterContract);

    WaterContract findById(Long id);
    List<WaterContract> findWaterContractByCustomerSite(CustomerSite customerSite);

    List<WaterContract> findByCustomerSite(CustomerSite customerSite);
    WaterSearchResult getWaterContracts(WaterContractSearch waterContractSearch, int pageNumber);
}
