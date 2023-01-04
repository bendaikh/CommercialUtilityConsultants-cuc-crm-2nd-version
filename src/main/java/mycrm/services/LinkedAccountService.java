package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.LinkedAccount;

import java.util.List;
import java.util.Map;

public interface LinkedAccountService {
    LinkedAccount createAndSaveLinkedAccount(Customer customer, Customer linkedCustomer);

    List<LinkedAccount> findByCustomer(Customer customer);

    void deleteLinkedAccount(Long linkedAccountId);

    void deleteLinkedAccountWithReferences(Customer customer);
}
