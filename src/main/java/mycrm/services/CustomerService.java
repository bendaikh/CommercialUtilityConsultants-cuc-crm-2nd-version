package mycrm.services;

import mycrm.models.BillingDetail;
import mycrm.models.Customer;

import java.util.List;

public interface CustomerService {
    
    List<Customer> findAll();

    List<Customer> findLatest5();

    Customer findById(Long id);

    Customer save(Customer customer);

    Customer edit(Customer customer);

    Customer findByBusinessNameAndBusinessPostcodeOutAndBusinessPostcodeIn(String businessName, String postcodeOut, String postCodeIn);

    void deleteById(Long id);

    BillingDetail saveBillingDetail(BillingDetail billingDetail);

    void deleteBillingDetailByCustomer(Customer customer);

    Customer findNonLinkedCustomer(String customerReference, Customer customer);

    Customer findByCustomerReference(String customerReference);
}
