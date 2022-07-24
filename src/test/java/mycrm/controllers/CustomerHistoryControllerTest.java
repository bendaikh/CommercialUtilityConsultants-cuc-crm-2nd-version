package mycrm.controllers;

import mycrm.audit.history.CustomerHistory;
import mycrm.models.Customer;
import mycrm.services.CustomerHistoryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CustomerHistoryControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private CustomerHistoryController customerHistoryController;

    @Mock
    private CustomerHistoryService mockCustomerHistoryService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(customerHistoryController)
                .build();
    }

    @Test
    public void shouldDeleteHistory() throws Exception {
        Customer customer = new Customer();
        customer.setId(65l);

        CustomerHistory customerHistory = new CustomerHistory();
        customerHistory.setId(34l);
        customerHistory.setCustomer(customer);

        when(mockCustomerHistoryService.findById(any(Long.class)))
                .thenReturn(customerHistory);

        mockMvc.perform(get("/deleteHistory/34"))
                .andExpect(redirectedUrl("/admin/removal/index/65"))
                .andExpect(status().is3xxRedirection());

        verify(mockCustomerHistoryService, times(1))
                .deleteById(any(Long.class));
    }

    @Test
    public void shouldNotDeleteHistory() throws Exception {
        Customer customer = new Customer();
        customer.setId(65l);

        CustomerHistory customerHistory = new CustomerHistory();
        customerHistory.setId(34l);
        customerHistory.setCustomer(customer);

        when(mockCustomerHistoryService.findById(any(Long.class)))
                .thenReturn(customerHistory);

        //throw an error
        doThrow(Exception.class)
                .when(mockCustomerHistoryService)
                .deleteById(any(Long.class));

        mockMvc.perform(get("/deleteHistory/34"))
                .andExpect(redirectedUrl("/admin/removal/index/65"))
                .andExpect(status().is3xxRedirection());

        verify(mockCustomerHistoryService, times(1))
                .deleteById(any(Long.class));
    }
}
