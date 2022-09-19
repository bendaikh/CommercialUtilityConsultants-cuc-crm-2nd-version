package mycrm.services;

import mycrm.models.*;

import java.util.List;

public interface BroadbandContractService {

    BroadbandContract save(BroadbandContract broadbandContract);

    BroadbandContract findById(Long id);
    List<BroadbandContract> findBroadbandContractByCustomerSite(CustomerSite customerSite);

    List<BroadbandContract> findByCustomerSite(CustomerSite customerSite);
    BroadbandSearchResult getBroadbandContracts(BroadbandContractSearch broadbandContractSearch, int pageNumber);

}
