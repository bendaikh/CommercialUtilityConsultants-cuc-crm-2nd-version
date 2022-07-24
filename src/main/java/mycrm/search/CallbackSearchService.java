package mycrm.search;

import mycrm.models.CallbackSearchResult;
import mycrm.models.ContractSearch;

public interface CallbackSearchService {
    CallbackSearchResult searchCustomerContract(ContractSearch contractSearch, int pageNumber);
}
