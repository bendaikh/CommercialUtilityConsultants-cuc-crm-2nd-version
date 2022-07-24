package mycrm.services;

import mycrm.audit.history.CustomerHistory;
import mycrm.models.Customer;
import mycrm.repositories.CustomerHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class CustomerHistoryServiceImpl implements CustomerHistoryService {

    private final CustomerHistoryRepository customerHistoryRepo;

    @Autowired
    public CustomerHistoryServiceImpl(CustomerHistoryRepository customerHistoryRepo) {
        this.customerHistoryRepo = customerHistoryRepo;
    }

    @Override
    public CustomerHistory create(CustomerHistory customerHistory) {
        return this.customerHistoryRepo.save(customerHistory);
    }

    @Override
    public List<CustomerHistory> findByCustomer(Customer customer) {
        return this.customerHistoryRepo.findAllByCustomer(customer);
    }

    @Override
    public void deleteById(Long id) {
        this.customerHistoryRepo.delete(id);
    }

    @Override
    public CustomerHistory findById(Long id) {
        return this.customerHistoryRepo.findOne(id);
    }

    @Override
    public void deleteByCustomer(Customer customer) {
        this.customerHistoryRepo.deleteByCustomer(customer);
    }
}
