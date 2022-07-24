package mycrm.search;

import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;

public interface RenewalSearchService {
    ContractSearchResult searchCustomerContract(ContractSearch contractSearch, int pageNumber);
}
