package mycrm.controllers;

import mycrm.models.Customer;
import mycrm.models.CustomerNote;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.User;
import mycrm.services.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class CustomerNoteControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CustomerNoteController customerNoteController;

    @Mock
    private CustomerNoteService mockCustomerNoteService;

    @Mock
    private UserService mockUserService;

    @Mock
    private CustomerService mockCustomerService;

    @Mock
    private GasContractService mockGasContractService;

    @Mock
    private ElecContractService mockElecContractService;

    @Mock
    private MerchantServicesService merchantServicesService;

    @Mock
    private ContactService mockContactService;

    @Mock
    private UtilityContractService mockUtilityContractService;

    @Mock
    private LinkedAccountService linkedAccountService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerNoteController).build();
    }

    @Test
    public void shouldReturnCustomersNote() throws Exception {
        Long id = 50l;

        Customer customer = new Customer("Mahmoods");

        List<CustomerNote> customerNote = new ArrayList<>();
        customerNote.add(new CustomerNote());

        List<User> users = new ArrayList<>();
        users.add(new User());

        List<GasCustomerContract> gasContractList = new ArrayList<>();
        List<ElecCustomerContract> elecContractList = new ArrayList<>();

        when(mockCustomerService.findById(id)).thenReturn(customer);
        when(mockCustomerNoteService.findByCustomerOrderByDateCreatedDesc(customer)).thenReturn(customerNote);
        when(mockUserService.findAll()).thenReturn(users);

        when(mockGasContractService.findByCustomerOrderByEndDateDesc(customer)).thenReturn(gasContractList);
        when(mockElecContractService.findByCustomerOrderByEndDateDesc(customer)).thenReturn(elecContractList);

        mockMvc
                .perform(get("/admin/customer/customernotes/50"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/customernotes"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)))
                .andExpect(model().attribute("customerNotes", hasSize(1)))
                .andExpect(model().attribute("users", hasSize(1)));
    }

//	@Test
//	public void shouldAddCustomerNote() throws Exception {
//
//		CustomerNote customerNote = new CustomerNote();
//
//        when(mockCustomerNoteService.save(any(CustomerNote.class))).thenReturn(customerNote);
//
//		mockMvc.perform(post("/customerNote")
//				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				.param("createdBy.id", "1")
//				.param("customer.id", "984")
//				.param("subject", "ASubject")
//				.param("note", "ANote")
//				.param("completed", "on")
//				.param("taggedUser.id", "42"))
//				.andExpect(redirectedUrl("/admin/customer/customernotes/984")).andDo(print());
//	}

    @Test
    public void shouldDeleteCustomerNote() throws Exception {
        Long id = 1l;
        Customer customer = new Customer();
        customer.setId(Long.valueOf(99));

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(Long.valueOf(22));
        customerNote.setCustomer(customer);

        when(mockCustomerNoteService.findById(id)).thenReturn(customerNote);

        mockMvc.perform(get("/admin/customer/deleteNote/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/customernotes/99"));
    }

    @Test
    public void shouldNotDeleteCustomerNoteWhenNotFound() throws Exception {

        when(mockCustomerNoteService.findById(Long.valueOf(1))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/deleteNote/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

}
