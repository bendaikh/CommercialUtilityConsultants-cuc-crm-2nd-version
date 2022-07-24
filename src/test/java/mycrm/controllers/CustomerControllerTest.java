package mycrm.controllers;

import mycrm.models.BillingDetail;
import mycrm.models.Broker;
import mycrm.models.Contact;
import mycrm.models.Contract;
import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.models.Customer;
import mycrm.models.CustomerSearch;
import mycrm.models.CustomerSearchResult;
import mycrm.models.GasCustomerContract;
import mycrm.models.Supplier;
import mycrm.models.User;
import mycrm.search.ContractSearchService;
import mycrm.search.CustomerSearchService;
import mycrm.services.BrokerService;
import mycrm.services.ContactPreferencesService;
import mycrm.services.ContactService;
import mycrm.services.ContractService;
import mycrm.services.CustomerService;
import mycrm.services.EmailHistoryService;
import mycrm.services.IdGeneratorService;
import mycrm.services.LinkedAccountService;
import mycrm.services.NewCustomerService;
import mycrm.services.SupplierService;
import mycrm.services.TpsCheckService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService mockCustomerService;

    @Mock
    private CustomerSearchService mockCustomerSearchService;

    @Mock
    private CustomerSearchResult mockCustomerSearchResult;

    @Mock
    private ContractSearchService mockCustomerContractSearchService;

    @Mock
    private BrokerService mockBrokerService;

    @Mock
    private SupplierService mockSupplierService;

    @Mock
    private IdGeneratorService idGeneratorService;

    @Mock
    private NewCustomerService mockNewCustomerService;

    @Mock
    private ContactService contactService;

    @Mock
    private ContractService mockContractService;

    @Mock
    private TpsCheckService mockTpsCheckService;

    @Mock
    private ContactPreferencesService mockContactPreferencesService;

    @Mock
    private EmailHistoryService mockEmailHistoryService;

    @Mock
    private LinkedAccountService mockLinkedAccountService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void shouldReturnSearchedListOfCustomers() throws Exception {

        // create dummy customers
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Mahmoods"));

        CustomerSearch customerSearch = new CustomerSearch();
        customerSearch.setQ("Mahmoods");
        customerSearch.setPageNumber(1);

        when(mockCustomerSearchService.searchCustomers(any(CustomerSearch.class), any(Integer.class)))
                .thenReturn(mockCustomerSearchResult);
        when(mockCustomerSearchResult.getReturnedCustomers()).thenReturn(customers);
        when(mockCustomerSearchResult.getReturnedCustomerCount()).thenReturn((long) customers.size());
        when(mockCustomerSearchResult.getTotalCustomerCount()).thenReturn((long) customers.size());

        mockMvc
                .perform(get("/admin/customer/customers/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("searchResults", hasSize(1)))
                .andExpect(model().attribute("totalResults", Long.valueOf(1)))
                .andExpect(model().attribute("pageNumber", 1))
                .andExpect(model().attribute("totalCustomers", Long.valueOf(1)))
                .andExpect(view().name("admin/customer/customers"));
    }

    @Test
    public void shouldReturnViewOfCustomers() throws Exception {
        Long id = 50l;

        Customer customer = new Customer("Mahmoods");

        when(mockCustomerService.findById(id)).thenReturn(customer);

        mockMvc
                .perform(get("/admin/customer/view/50"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/view"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)));
    }

    @Test
    public void shouldReturnViewOfMyCustomers() throws Exception {

        Set<Customer> searchResultSet = new HashSet<>();
        searchResultSet.add(new Customer("NewCustomer"));

        when(mockCustomerContractSearchService.searchMyCustomers(any(ContractSearch.class))).thenReturn(searchResultSet);

        mockMvc
                .perform(get("/admin/customer/mycustomers"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/mycustomers"))
                .andExpect(model().attribute("customers", searchResultSet))
                .andExpect(model().attribute("timeTaken", instanceOf(long.class)));
    }

    @Test
    public void shouldReturnEditCustomerPageWithCustomer() throws Exception {
        Long id = 1l;

        Customer customer = new Customer();
        customer.setId(id);
        customer.setCustomerReference("CEC");

        when(mockCustomerService.findById(id)).thenReturn(customer);
        when(idGeneratorService.generateId()).thenReturn("blah");

        mockMvc
                .perform(get("/admin/customer/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/newcustomer"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)));
    }

    @Test
    public void shouldReturnNewCustomerFormWithNewCustomerObject() throws Exception {
        verifyZeroInteractions(mockCustomerService);

        mockMvc
                .perform(get("/admin/customer/newcustomer"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/newcustomer"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)));
    }

    @Test
    public void shouldSaveACustomerWithAnIdViaNewCustomerForm() throws Exception {

        mockMvc
                .perform(post("/customer").param("id", "56"))
                .andExpect(redirectedUrl("/admin/customer/view/56"))
                .andExpect(status().is3xxRedirection());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRedirectIfErrorCreatingCustomerViaNewCustomerForm() throws Exception {

        when(mockCustomerService.save(new Customer())).thenThrow(Exception.class);

        mockMvc
                .perform(post("/customer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/newcustomer"));
    }

    @Test
    public void shouldCreateANewCustomerViaNewCustomerForm() throws Exception {
        User user = new User();
        user.setId(3l);
        user.setUsername("john.doe");

        Customer customer = new Customer();
        customer.setId(Long.valueOf(43));
        customer.setFirstName("Default");
        customer.setLastName("User");
        customer.setCreatedBy(user);
        customer.setLastModifiedBy(user);

        when(mockCustomerService.save(any(Customer.class))).thenReturn(customer);

        mockMvc
                .perform(post("/customer")
                        .param("firstName", "Default")
                        .param("lastName", "User"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/view/43"));

        verify(mockNewCustomerService, times(1))
                .createNewCustomerLead(any(Customer.class), any(boolean.class), any(boolean.class));
    }

    @Test
    public void shouldCreateANewCustomerButNotReturnID() throws Exception {

        Customer customer = new Customer("Business");

        Customer newCustomer = new Customer("New Customer");
        newCustomer.setId(Long.valueOf(124));

        when(mockCustomerService.save(customer)).thenReturn(newCustomer);

        mockMvc
                .perform(post("/customer"))
                .andExpect(redirectedUrl("/admin/customer/newcustomer"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldReturnListOfCustomerContracts() throws Exception {

        Customer customer = new Customer("Restaurant");

        Broker broker = new Broker();
        broker.setId(54l);
        broker.setBusinessName("Dumb broker");

        GasCustomerContract customerContract = new GasCustomerContract();
        customerContract.setLogType("LIVE");
        customerContract.setBroker(broker);
        customerContract.setCustomer(customer);

        List<Contract> searchResults = new ArrayList<>();
        searchResults.add(customerContract);

        List<Broker> brokers = new ArrayList<>();
        brokers.add(broker);

        List<Supplier> suppliers = new ArrayList<>();
        suppliers.add(new Supplier());

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setLogType("LIVE");
        contractSearch.setBroker(broker);
        contractSearch.setSupplyType("GAS");
        contractSearch.setQ("Restaurant");
        contractSearch.setFromDate("2017-01-01");
        contractSearch.setToDate("2017-12-30");

        ContractSearchResult contractSearchResult = new ContractSearchResult();
        contractSearchResult.setReturnedContracts(searchResults);
        contractSearchResult.setTotalPages(1);
        contractSearchResult.setReturnedContractCount(1);
        contractSearchResult.setTotalContractCount(1);


        when(mockCustomerContractSearchService.searchCustomerContract(any(ContractSearch.class), eq(1))).thenReturn(contractSearchResult);
        when(mockBrokerService.findAll()).thenReturn(brokers);
        when(mockSupplierService.findAllOrderByBusinessName()).thenReturn(suppliers);

        mockMvc
                .perform(get("/admin/customer/customercontracts/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("brokers", hasSize(1)))
                .andExpect(model().attribute("customerContracts", hasSize(1)))
                .andExpect(model().attribute("suppliers", hasSize(1)))
                .andExpect(model().attribute("totalResults", 1l))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("pageNumber", 1))
                .andExpect(model().attribute("totalContracts", 1l))
                .andExpect(view().name("admin/customer/customercontracts"));
    }

    @Test
    public void shouldCreateNewContact() throws Exception {

        Customer customer = new Customer();
        customer.setId(Long.valueOf(56));

        Contact contact = new Contact();
        contact.setCustomer(customer);
        contact.setContactName("Blah");

        when(contactService.save(any(Contact.class))).thenReturn(contact);

        mockMvc
                .perform(post("/contact"))
                .andExpect(redirectedUrl("/admin/customer/view/56"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldEditNewContact() throws Exception {

        Contact contact = new Contact();
        contact.setId(Long.valueOf(1));
        contact.setContactName("Hiya");

        when(contactService.findById(any(Long.class))).thenReturn(contact);

        mockMvc
                .perform(get("/admin/customer/contact/edit/1"))
                .andExpect(view().name("admin/customer/contact/edit"))
                .andExpect(model().attribute("contact", contact))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteNewContact() throws Exception {
        Customer customer = new Customer("Customer");
        customer.setId(Long.valueOf(34));

        Contact contact = new Contact();
        contact.setId(Long.valueOf(1));
        contact.setContactName("Hiya");
        contact.setCustomer(customer);

        when(contactService.findById(any(Long.class))).thenReturn(contact);
        //verify(contactService, times(1)).deleteById(any(Long.class));

        mockMvc
                .perform(get("/admin/customer/contact/deleteContact/1"))
                .andExpect(redirectedUrl("/admin/customer/view/34"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldNotDeleteNewContact() throws Exception {
        when(contactService.findById(any(Long.class))).thenReturn(null);
        verifyZeroInteractions(contactService);

        mockMvc
                .perform(get("/admin/customer/contact/deleteContact/1"))
                .andExpect(redirectedUrl("/admin/index"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldReturnContactsList() throws Exception {

        when(mockCustomerService.findById(any(Long.class))).thenReturn(happyCustomer());

        mockMvc
                .perform(get("/admin/customer/contactslist/1"))
                .andExpect(view().name("admin/customer/contactslist"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotReturnContactsListWhenCustomerNotFounc() throws Exception {
        when(mockCustomerService.findById(any(Long.class))).thenReturn(null);

        mockMvc
                .perform(get("/admin/customer/contactslist/1"))
                .andExpect(redirectedUrl("/admin/index"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldReturnBillingDetailWhenCustomerBillingDetailFound() throws Exception {

        Customer bdCustomer = happyCustomer();
        bdCustomer.setBillingDetail(billingDetails());

        when(mockCustomerService.findById(any(Long.class))).thenReturn(bdCustomer);

        mockMvc
                .perform(get("/admin/customer/editbillingdetails/1"))
                .andExpect(model().attribute("billingDetail", instanceOf(BillingDetail.class)))
                .andExpect(view().name("admin/customer/editbillingdetails"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNewBillingDetailWhenCustomerBillingDetailNotFound() throws Exception {
        Customer bdCustomer = happyCustomer();

        when(mockCustomerService.findById(any(Long.class))).thenReturn(bdCustomer);

        mockMvc
                .perform(get("/admin/customer/editbillingdetails/1"))
                .andExpect(model().attribute("billingDetail", instanceOf(BillingDetail.class)))
                .andExpect(view().name("admin/customer/editbillingdetails"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateBillingDetailAndRedirectToCustomerView() throws Exception {

        when(mockCustomerService.saveBillingDetail(any(BillingDetail.class)))
                .thenReturn(billingDetails());

        mockMvc
                .perform(post("/billingDetail")
                        .param("id", "56"))
                .andExpect(redirectedUrl("/admin/customer/view/1"))
                .andExpect(status().is3xxRedirection());
    }

    private Customer happyCustomer() {
        Customer customer = new Customer();
        customer.setId(Long.valueOf(1));

        return customer;
    }

    private BillingDetail billingDetails() {
        BillingDetail billingDetail = new BillingDetail();
        billingDetail.setId(Long.valueOf(1));
        billingDetail.setCustomer(happyCustomer());

        return billingDetail;
    }

}
