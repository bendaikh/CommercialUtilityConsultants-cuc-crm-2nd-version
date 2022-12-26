package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.LinkedAccount;
import mycrm.repositories.LinkedAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<LinkedAccount> linkedAccounts = this.linkedAccountRepository.findAllLinkedAccountsByCustomerReference(customer);
        ArrayList<LinkedAccount> linkedAccountsTosave = new ArrayList<>();
        linkedAccountsTosave.addAll(linkedAccounts);
        for(LinkedAccount l : linkedAccounts){
            if(l.getCustomer().getId() != customer.getId()){
                List<LinkedAccount> linkedAccounts1 = this.linkedAccountRepository.findAllLinkedAccountsByCustomerReference(l.getCustomer());
                linkedAccounts1.removeIf(t->t.getLinkedCustomer().getId() == customer.getId() || t.getCustomer().getId() == customer.getId());
                linkedAccountsTosave.addAll(linkedAccounts1);
            }
            else if(l.getCustomer().getId() == customer.getId()){
                List<LinkedAccount> linkedAccounts2 = this.linkedAccountRepository.findAllLinkedAccountsByCustomerReference(l.getLinkedCustomer());
                linkedAccounts2.removeIf(t->t.getLinkedCustomer().getId() == customer.getId() || t.getCustomer().getId() == customer.getId());
                linkedAccountsTosave.addAll(linkedAccounts2);
            }

        }

        return linkedAccountsTosave;
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
