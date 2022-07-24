package mycrm.search;

import mycrm.models.CustomerSearch;
import mycrm.models.CustomerSearchResult;

public interface CustomerSearchService {
	
	CustomerSearchResult searchCustomers(CustomerSearch customerSearch, int pageNumber);

}
