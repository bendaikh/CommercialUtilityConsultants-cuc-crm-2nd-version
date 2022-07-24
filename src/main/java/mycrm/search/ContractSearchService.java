package mycrm.search;

import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.models.Customer;

import java.text.ParseException;
import java.util.Set;

public interface ContractSearchService {

    ContractSearchResult searchCustomerContract(ContractSearch contractSearch, int pageNumber) throws ParseException;

    Set<Customer> searchMyCustomers(ContractSearch contractSearch);
}
