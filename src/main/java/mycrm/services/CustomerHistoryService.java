package mycrm.services;

import mycrm.audit.history.CustomerHistory;
import mycrm.models.Customer;

import java.util.List;

public interface CustomerHistoryService {
    CustomerHistory create(CustomerHistory customerHistory);

    List<CustomerHistory> findByCustomer(Customer customer);

    void deleteById(Long id);

    CustomerHistory findById(Long id);

    void deleteByCustomer(Customer customer);
}
