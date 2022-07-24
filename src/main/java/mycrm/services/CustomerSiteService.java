package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.CustomerSiteWithContracts;

import java.util.List;


public interface CustomerSiteService {

    List<CustomerSite> findAll();

    List<CustomerSite> findLatest5();

    CustomerSite findById(Long id);

    List<CustomerSite> findByCustomer(Customer customer);

    CustomerSite save(CustomerSite customerSite);

    List<CustomerSiteWithContracts> customerSitesWithContracts(Customer customer);

    void deleteById(Long id);

    void deleteByCustomer(Customer customer);

    CustomerSite transferCustomerSite(CustomerSite customerSite, Customer selectedCustomer);

}
