package mycrm.services;

import mycrm.models.*;

import java.util.List;

public interface LandlineContractService {
    LandlineContract save(LandlineContract landlineContract);

    LandlineContract findById(Long id);
    List<LandlineContract> findLandlineContractByCustomerSite(CustomerSite customerSite);

    List<LandlineContract> findByCustomerSite(CustomerSite customerSite);
    LandlineSearchResult getLandlineContracts(LandlineContractSearch landlineContractSearch, int pageNumber);


}
