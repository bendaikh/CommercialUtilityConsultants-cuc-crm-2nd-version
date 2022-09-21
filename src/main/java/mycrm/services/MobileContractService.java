package mycrm.services;

import mycrm.models.*;

import java.util.List;

public interface MobileContractService {

    MobileContract save(MobileContract mobileContract);

    MobileContract findById(Long id);
    List<MobileContract> findMobileContractByCustomerSite(CustomerSite customerSite);

    List<MobileContract> findByCustomerSite(CustomerSite customerSite);
    MobileSearchResult getMobileContracts(MobileContractSearch mobileContractSearch, int pageNumber);
}
