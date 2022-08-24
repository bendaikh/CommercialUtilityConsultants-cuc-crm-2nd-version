package mycrm.services;

import mycrm.models.BillingDetail;
import mycrm.models.Customer;
import mycrm.repositories.BillingDetailRepository;
import mycrm.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class CustomerServiceJpaImpl implements CustomerService {

    private final CustomerRepository customerRepo;
    private final BillingDetailRepository billingDetailRepository;
    private final LinkedAccountService linkedAccountService;

    @Autowired
    public CustomerServiceJpaImpl(CustomerRepository customerRepo,
                                  BillingDetailRepository billingDetailRepository,
                                  LinkedAccountService linkedAccountService) {
        this.customerRepo = customerRepo;
        this.billingDetailRepository = billingDetailRepository;
        this.linkedAccountService = linkedAccountService;
    }

    @Override
    public List<Customer> findAll() {
        return this.customerRepo.findAll();
    }

    @Override
    public List<Customer> findLatest5() {
        return this.customerRepo.findLatest5(new PageRequest(0, 5));
    }

    @Override
    public Customer findById(Long id) {
        return this.customerRepo.findOne(id);
    }

    @Override
    public Customer save(Customer customer) {
        return this.customerRepo.save(customer);
    }

    @Override
    public Customer edit(Customer customer) {
        return this.customerRepo.save(customer);
    }

    @Override
    public Customer findByBusinessNameAndBusinessPostcodeOutAndBusinessPostcodeIn(String businessName, String postcodeOut, String postCodeIn) {
        return this.customerRepo.findByBusinessNameAndBusinessPostcodeOutAndBusinessPostcodeIn(
                businessName, postcodeOut, postCodeIn);
    }

    @Override
    public void deleteById(Long id) {
        this.customerRepo.delete(id);
    }

    @Override
    public BillingDetail saveBillingDetail(BillingDetail billingDetail) {
        return this.billingDetailRepository.save(billingDetail);
    }

    @Override
    public void deleteBillingDetailByCustomer(Customer customer) {
        this.billingDetailRepository.deleteByCustomer(customer);
    }

    @Override
    public Customer findNonLinkedCustomer(String customerReference, Customer customer) {
        Customer result = this.customerRepo.findByCustomerReference(customerReference);
        if (result == null) {
            return null;
        }

        // if the linked account doesn't already exist for the customer
        if (!linkedAccountsList(customer).contains(result.getCustomerReference())) {
            return result;
        }

        return null;
    }

    @Override
    public Customer findByCustomerReference(String customerReference) {
        Customer result = this.customerRepo.findByCustomerReference(customerReference);

        return result;
    }

    private List<String> linkedAccountsList(Customer customer) {
        List<String> linkedAccountsList = new ArrayList<>();
        linkedAccountsList.add(customer.getCustomerReference());
        linkedAccountService.findByCustomer(customer).forEach(linkedAccount -> linkedAccountsList.add(linkedAccount.getLinkedCustomer().getCustomerReference()));
        return linkedAccountsList;
    }
}
