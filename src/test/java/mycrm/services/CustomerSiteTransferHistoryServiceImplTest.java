package mycrm.services;

import mycrm.models.CustomerSite;
import mycrm.models.CustomerSiteTransferHistory;
import mycrm.models.User;
import mycrm.repositories.CustomerSiteTransferHistoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerSiteTransferHistoryServiceImplTest {
    @InjectMocks
    private CustomerSiteTransferHistoryServiceImpl service;

    @Mock
    private CustomerSiteTransferHistoryRepository repository;

    @Test
    public void shouldSaveTransferHistory() {
        CustomerSiteTransferHistory history = CustomerSiteTransferHistory
                .builder()
                .customerSiteId(22L)
                .customerId(1L)
                .previousCustomerId(44L)
                .previousBusinessName("Test")
                .build();

        when(repository.save(any(CustomerSiteTransferHistory.class))).thenReturn(history);

        CustomerSiteTransferHistory savedHistory = service.save(history);

        assertEquals(history.getCustomerSiteId(), savedHistory.getCustomerSiteId());
    }

    @Test
    public void shouldReturnListOfTransfers() {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(22L);

        CustomerSiteTransferHistory history = CustomerSiteTransferHistory
                .builder()
                .customerSiteId(22L)
                .customerId(1L)
                .previousCustomerId(44L)
                .previousBusinessName("Test")
                .build();

        history.setCreatedBy(createdBy("Jimbo", "Derek"));
        history.setDateCreated(new Date(1609887797));

        when(repository.findCustomerSiteTransferHistoryByCustomerSite(any(Long.class)))
                .thenReturn(Arrays.asList(history));

        List<String> list = service.findCustomerSiteTransferHistoryByCustomerSite(customerSite);

        assertEquals("This site was moved from Test by Jimbo Derek on 19/01/1970 04:11:27 PM", list.get(0));
    }

    private User createdBy(String firstName, String lastName) {
        User user = new User();
        user.setId(99L);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return user;
    }
}
