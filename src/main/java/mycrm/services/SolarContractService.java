package mycrm.services;

import mycrm.models.*;

import java.util.List;

public interface SolarContractService {
    SolarContract save(SolarContract solarContract);

    SolarContract findById(Long id);
    List<SolarContract> findSolarContractByCustomerSite(CustomerSite customerSite);

    List<SolarContract> findByCustomerSite(CustomerSite customerSite);
    SolarSearchResult getSolarContracts(SolarContractSearch solarContractSearch, int pageNumber);
}
