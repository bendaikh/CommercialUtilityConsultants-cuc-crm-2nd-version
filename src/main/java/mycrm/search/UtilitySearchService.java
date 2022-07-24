package mycrm.search;

import mycrm.models.ContractSearch;
import mycrm.models.UtilityCallbackSearchResult;
import mycrm.models.UtilitySearchResult;

public interface UtilitySearchService {
    UtilitySearchResult searchUtilityContracts(ContractSearch contractSearch, int pageNumber);

    UtilityCallbackSearchResult searchNotedCallbackContracts(ContractSearch contractSearch, int page);
}
