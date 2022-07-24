package mycrm.search;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import mycrm.models.Customer;
import mycrm.models.CustomerSearch;
import mycrm.models.CustomerSearchResult;
import mycrm.models.User;
import mycrm.repositories.CustomerSearchRepository;
import mycrm.services.UserService;

@RunWith(MockitoJUnitRunner.class)
public class CustomerSearchServiceImplTest {

    @InjectMocks
    private CustomerSearchServiceImpl customerSearchServiceImpl;

    @Mock
    private CustomerSearchRepository customerSearchRepo;
    
    @Mock
    private UserService userService;

    @Mock
    private User mockedUser;
    
    @Mock
    private SecurityContext securityContext;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private Principal principal1;
    
    @Before
    public void setup() {
        
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userService.findUserByUsername(any(String.class))).thenReturn(mockedUser);       
    }

    @Test
    public final void shouldReturnSearchedListOfCustomersWhenSearching() throws Exception {

        List<Customer> returnedCustomers = new ArrayList<>();
        returnedCustomers.add(new Customer());
        
        CustomerSearchResult customerSearchResult = new CustomerSearchResult();
        customerSearchResult.setReturnedCustomerCount(1);
        customerSearchResult.setReturnedCustomers(returnedCustomers);
        customerSearchResult.setTotalCustomers(1);
        
        CustomerSearch customerSearch = new CustomerSearch();
        customerSearch.setPageNumber(1);
        customerSearch.setQ("searchstring");

        when(customerSearchRepo.search(customerSearch, 1)).thenReturn(customerSearchResult);

        CustomerSearchResult searchReturnedCustomers = customerSearchServiceImpl.searchCustomers(customerSearch, 1);

        assertEquals(customerSearchResult, searchReturnedCustomers);
        assertEquals(1, searchReturnedCustomers.getReturnedCustomerCount());
        assertEquals(1, searchReturnedCustomers.getTotalCustomerCount());
        assertEquals(customerSearchResult.getReturnedCustomers(), searchReturnedCustomers.getReturnedCustomers());
    }
}
