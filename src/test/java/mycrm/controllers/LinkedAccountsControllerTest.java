package mycrm.controllers;

import mycrm.models.Customer;
import mycrm.models.LinkedAccount;
import mycrm.services.CustomerService;
import mycrm.services.LinkedAccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class LinkedAccountsControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private LinkedAccountsController controller;

    @Mock
    private CustomerService customerService;

    @Mock
    private LinkedAccountService linkedAccountService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldReturnCustomerLinkedAccountsPage() throws Exception {

        Customer customer = aCustomer();
        List<LinkedAccount> linkedAccounts = new ArrayList<>();

        when(customerService.findById(any(Long.class))).thenReturn(customer);
        when(linkedAccountService.findByCustomer(any(Customer.class))).thenReturn(linkedAccounts);

        mockMvc
                .perform(get("/admin/customer/customer-linked-accounts/42"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/customer-linked-accounts"))
                .andExpect(model().attribute("linkedAccounts", linkedAccounts))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)))
                .andExpect(model().attribute("linkedAccountsSize", 0));
    }

    @Test
    public void shouldNotReturnCustomerLinkedAccountsPageWhenCustomerNotFound() throws Exception {

        when(customerService.findById(any(Long.class))).thenReturn(null);

        mockMvc
                .perform(get("/admin/customer/customer-linked-accounts/42"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("admin/customer/view/42"));
    }

    @Test
    public void shouldReturnLinkedAccountsPage() throws Exception {

        Customer customer = aCustomer();
        List<LinkedAccount> linkedAccounts = new ArrayList<>();

        when(customerService.findById(any(Long.class))).thenReturn(customer);
        when(linkedAccountService.findByCustomer(any(Customer.class))).thenReturn(linkedAccounts);

        mockMvc
                .perform(get("/admin/customer/linked-accounts/42"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/linked-accounts"))
                .andExpect(model().attribute("linkedAccounts", linkedAccounts))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)));
    }

    @Test
    public void shouldNotReturnLinkedAccountsPageWhenCustomerNotFound() throws Exception {

        when(customerService.findById(any(Long.class))).thenReturn(null);

        mockMvc
                .perform(get("/admin/customer/linked-accounts/42"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("admin/customer/view/42"));
    }

    @Test
    public void shouldNotReturnCreateLinkedAccountsPageWhenCustomerNotFound() throws Exception {

        when(customerService.findById(any(Long.class))).thenReturn(null);

        mockMvc
                .perform(get("/admin/customer/create-linked-accounts/42"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("admin/customer/view/42"));
    }

    @Test
    public void shouldReturnCreateLinkedAccountsPageWithoutSearching() throws Exception {

        Customer customer = aCustomer();

        when(customerService.findById(any(Long.class))).thenReturn(customer);

        mockMvc
                .perform(get("/admin/customer/create-linked-accounts/42"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/create-linked-accounts"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)));
    }

    @Test
    public void shouldReturnCreateLinkedAccountsPageWithSearching() throws Exception {

        Customer customer = aCustomer();

        Customer foundCustomer = new Customer();
        foundCustomer.setId(12l);
        foundCustomer.setCustomerReference("CUC1");

        when(customerService.findById(any(Long.class))).thenReturn(customer);
        when(customerService.findNonLinkedCustomer(any(String.class), any(Customer.class))).thenReturn(foundCustomer);

        mockMvc
                .perform(get("/admin/customer/create-linked-accounts/42?searchParam=CUC1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/create-linked-accounts"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)))
                .andExpect(model().attribute("searchResult", instanceOf(Customer.class)));
    }

    @Test
    public void shouldNotSaveLinkedAccountWhenCustomerNotFound() throws Exception {

        when(customerService.findById(any(Long.class))).thenReturn(null);

        MultiValueMap<String, String> linkedAccountDetails = new LinkedMultiValueMap<>();
        linkedAccountDetails.add("id", "42");
        linkedAccountDetails.add("linkedAccountId", "56");

        mockMvc
                .perform(post("/saveLinkedAccount").params(linkedAccountDetails))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("admin/customer/view/42"));
    }

    @Test
    public void shouldSaveLinkedAccount() throws Exception {
        when(customerService.findById(any(Long.class))).thenReturn(aCustomer());

        MultiValueMap<String, String> linkedAccountDetails = new LinkedMultiValueMap<>();
        linkedAccountDetails.add("id", "42");
        linkedAccountDetails.add("linkedAccountId", "56");

        mockMvc
                .perform(post("/saveLinkedAccount").params(linkedAccountDetails))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/linked-accounts/42"));

        verify(linkedAccountService, times(1))
                .createAndSaveLinkedAccount(any(Customer.class), any(Customer.class));
    }

    @Test
    public void shouldDeleteLinkedAccount() throws Exception {
        when(customerService.findById(any(Long.class))).thenReturn(aCustomer());

        mockMvc
                .perform(post("/admin/customer/delete-linked-account/42?linkedAccountId=22"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/linked-accounts/42"));

        verify(linkedAccountService, times(1))
                .deleteLinkedAccount(any(Long.class));
    }

    @Test
    public void shouldNotDeleteLinkedAccountWhenCustomerNotFound() throws Exception {
        when(customerService.findById(any(Long.class))).thenReturn(null);

        mockMvc
                .perform(post("/admin/customer/delete-linked-account/42?linkedAccountId=22"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("admin/customer/view/42"));

        verify(linkedAccountService, times(0))
                .deleteLinkedAccount(any(Long.class));
    }

    private Customer aCustomer() {
        Customer customer = new Customer();
        customer.setId(42l);

        return customer;
    }

}
