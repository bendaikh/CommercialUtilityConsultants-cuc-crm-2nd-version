package mycrm.controllers;

import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.services.ContactService;
import mycrm.services.CustomerService;
import mycrm.services.TpsCheckService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TpsCheckControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TpsCheckController tpsCheckController;

    @Mock
    private TpsCheckService tpsCheckService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ContactService contactService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(tpsCheckController).build();
    }

    @Test
    public void shouldTpsCheckByCustomer() throws Exception {

        Customer customer = new Customer();
        customer.setId(541l);

        when(customerService.findById(any(Long.class))).thenReturn(customer);

        mockMvc.perform(get("/admin/customer/tps-check/541"))
                .andExpect(redirectedUrl("/admin/customer/view/541"))
                .andExpect(status().is3xxRedirection());

        verify(tpsCheckService, times(1)).checkCustomerNumbers(customer);
    }

    @Test
    public void shouldNotTpsCheckByCustomer() throws Exception {

        when(customerService.findById(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/tps-check/541"))
                .andExpect(redirectedUrl("/admin/customer/view/541"))
                .andExpect(status().is3xxRedirection());

        verify(tpsCheckService, times(0))
                .checkCustomerNumbers(any(Customer.class));
    }

    @Test
    public void shouldTpsCheckByContact() throws Exception {

        Customer customer = new Customer();
        customer.setId(541l);

        Contact contact = new Contact();
        contact.setId(645l);
        contact.setCustomer(customer);

        when(contactService.findById(any(Long.class))).thenReturn(contact);

        mockMvc.perform(get("/admin/customer/tps-check/541/645"))
                .andExpect(redirectedUrl("/admin/customer/view/541"))
                .andExpect(status().is3xxRedirection());

        verify(tpsCheckService, times(1))
                .checkContactNumbers(any(Contact.class));
    }

    @Test
    public void shouldNotTpsCheckByContact() throws Exception {

        when(contactService.findById(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/tps-check/541/645"))
                .andExpect(redirectedUrl("/admin/customer/view/541"))
                .andExpect(status().is3xxRedirection());

        verify(tpsCheckService, times(0))
                .checkContactNumbers(any(Contact.class));
    }

}
