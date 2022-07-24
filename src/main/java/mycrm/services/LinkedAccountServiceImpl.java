package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.LinkedAccount;
import mycrm.repositories.LinkedAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class LinkedAccountServiceImpl implements LinkedAccountService {
    private final LinkedAccountRepository linkedAccountRepository;

    @Autowired
    public LinkedAccountServiceImpl(LinkedAccountRepository linkedAccountRepository) {
        this.linkedAccountRepository = linkedAccountRepository;
    }

    @Override
    public LinkedAccount createAndSaveLinkedAccount(Customer customer, Customer linkedCustomer) {
        return this.linkedAccountRepository.save(LinkedAccount.builder()
                .customer(customer)
                .linkedCustomer(linkedCustomer)
                .build());
    }

    @Override
    public List<LinkedAccount> findByCustomer(Customer customer) {
        return this.linkedAccountRepository.findAllLinkedAccountsByCustomerReference(customer);
    }

    @Override
    public void deleteLinkedAccount(Long linkedAccountId) {
        this.linkedAccountRepository.delete(linkedAccountId);
    }

    @Override
    public void deleteLinkedAccountWithReferences(Customer customer) {
        this.linkedAccountRepository.deleteByLinkedCustomer(customer);
        this.linkedAccountRepository.deleteByCustomer(customer);
    }
}
