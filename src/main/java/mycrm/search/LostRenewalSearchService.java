package mycrm.search;

import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;

public interface LostRenewalSearchService {
    ContractSearchResult searchCustomerContract(ContractSearch contractSearch, int pageNumber);
}
