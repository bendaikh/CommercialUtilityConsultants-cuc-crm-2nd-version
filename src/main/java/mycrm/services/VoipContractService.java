package mycrm.services;

import mycrm.models.*;

import java.util.List;

public interface VoipContractService {

    VoipContract save(VoipContract voipContract);

    VoipContract findById(Long id);
    List<VoipContract> findVoipContractByCustomerSite(CustomerSite customerSite);

    List<VoipContract> findByCustomerSite(CustomerSite customerSite);
    VoipSearchResult getVoipContracts(VoipContractSearch voipContractSearch, int pageNumber);

}
