package mycrm.search;

import mycrm.models.MerchantServicesContractSearch;
import mycrm.models.MerchantServicesSearchResult;

public interface MerchantServicesContractSearchService {
    MerchantServicesSearchResult searchMerchantServicesContract(MerchantServicesContractSearch merchantServicesContractSearch, int pageNumber);
    MerchantServicesSearchResult searchMerchantServiceContract(MerchantServicesContractSearch contractSearch, int pageNumber);
}
