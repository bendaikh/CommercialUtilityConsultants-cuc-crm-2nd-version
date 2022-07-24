package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.LinkedAccount;
import mycrm.repositories.LinkedAccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinkedAccountServiceTest {

    @InjectMocks
    private LinkedAccountServiceImpl linkedAccountService;

    @Mock
    private LinkedAccountRepository linkedAccountRepository;

    @Test
    public void shouldCreateAndSaveLinkedAccount() {
        Customer customer = aCustomer();
        Customer linkedCustomer = new Customer();

        LinkedAccount linkedAccount = LinkedAccount.builder()
                .customer(customer)
                .linkedCustomer(linkedCustomer)
                .build();

        when(linkedAccountRepository.save(any(LinkedAccount.class))).thenReturn(linkedAccount);

        linkedAccountService.createAndSaveLinkedAccount(customer, linkedCustomer);

        verify(linkedAccountRepository, times(1)).save(any(LinkedAccount.class));
    }

    @Test
    public void shouldFindLinkedAccountsByCustomer() {
        List<LinkedAccount> linkedAccountsList = new ArrayList<>();

        when(linkedAccountRepository
                .findAllLinkedAccountsByCustomerReference(any(Customer.class)))
                .thenReturn(linkedAccountsList);

        linkedAccountService.findByCustomer(aCustomer());
    }

    @Test
    public void shouldDeleteLinkedAccounts() {
        linkedAccountService.deleteLinkedAccount(1l);

        verify(linkedAccountRepository, times(1)).delete(any(Long.class));
    }

    private Customer aCustomer() {
        Customer customer = new Customer();
        customer.setId(42l);

        return customer;
    }
}
