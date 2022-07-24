package mycrm.services;

import mycrm.audit.history.CustomerHistory;
import mycrm.entitylisteners.Action;
import mycrm.models.Customer;
import mycrm.models.User;
import mycrm.repositories.CustomerHistoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerHistoryServiceTest {

    @InjectMocks
    private CustomerHistoryServiceImpl customerHistoryServiceImpl;

    @Mock
    private CustomerHistoryRepository customerHistoryRepo;

    @Test
    public void shouldReturnListOfAdminContractTasks() {
        
        Customer customer = new Customer();

        User modifiedBy = new User();

        Date modifiedDate = new Date(2018, 01, 01);

        CustomerHistory customerHistory = new CustomerHistory(customer, Action.UPDATED);
        customerHistory.setId(Long.valueOf(5));
        customerHistory.setCustomer(customer);
        customerHistory.setContent("My content");
        customerHistory.setModifiedBy(modifiedBy);
        customerHistory.setAction(Action.UPDATED);
        customerHistory.setModifiedDate(modifiedDate);

        when(customerHistoryRepo.save(any(CustomerHistory.class))).thenReturn(customerHistory);

        CustomerHistory createdCustomerHistory = customerHistoryServiceImpl.create(customerHistory);

        assertEquals(customerHistory, createdCustomerHistory);
        assertEquals(customerHistory.getId(), createdCustomerHistory.getId());
        assertEquals(customerHistory.getCustomer(), createdCustomerHistory.getCustomer());
        assertEquals(customerHistory.getContent(), createdCustomerHistory.getContent());
        assertEquals(customerHistory.getModifiedBy(), createdCustomerHistory.getModifiedBy());
        assertEquals(customerHistory.getAction(), createdCustomerHistory.getAction());
        assertEquals(customerHistory.getModifiedDate(), createdCustomerHistory.getModifiedDate());
        assertEquals(customerHistory.toString(), createdCustomerHistory.toString());
    }

}
