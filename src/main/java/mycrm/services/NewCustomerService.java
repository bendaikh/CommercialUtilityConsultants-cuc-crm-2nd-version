package mycrm.services;

import mycrm.models.Customer;

public interface NewCustomerService {
    void createNewCustomerLead(Customer customer, Boolean createGas, Boolean createElectricity);

    void updateCustomerReferenceNumber(Customer customer);
}
