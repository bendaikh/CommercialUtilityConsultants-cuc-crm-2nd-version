package mycrm.search;

import mycrm.models.MobileContractSearch;
import mycrm.models.MobileSearchResult;

public interface MobileContractSearchService {
    MobileSearchResult searchMobileContract(MobileContractSearch mobileContractSearch, int pageNumber);
}
