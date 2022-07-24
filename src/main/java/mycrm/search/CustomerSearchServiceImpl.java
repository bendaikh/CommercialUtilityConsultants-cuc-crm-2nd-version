package mycrm.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import mycrm.models.CustomerSearch;
import mycrm.models.CustomerSearchResult;
import mycrm.repositories.CustomerSearchRepository;

@Service
@Primary
public class CustomerSearchServiceImpl implements CustomerSearchService {

	private final CustomerSearchRepository customerSearchRepo;

	@Autowired
	public CustomerSearchServiceImpl(CustomerSearchRepository customerSearchRepo) {
		this.customerSearchRepo = customerSearchRepo;
	}

	@Override
	public CustomerSearchResult searchCustomers(CustomerSearch customerSearch, int pageNumber) {
		return this.customerSearchRepo.search(customerSearch, pageNumber);
	}
}
