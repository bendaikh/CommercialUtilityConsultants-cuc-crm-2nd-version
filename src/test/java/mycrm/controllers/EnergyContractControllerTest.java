package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.CustomerNote;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.Role;
import mycrm.models.Supplier;
import mycrm.models.User;
import mycrm.services.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class EnergyContractControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EnergyContractController energyContractController;

    @Mock
    private GasContractService gasContractService;

    @Mock
    private CustomerSiteService customerSiteService;
    @Mock
    private ElecContractService elecContractService;

    @Mock
    private DoNotRenewReasonService doNotRenewReasonService;

    @Mock
    private SupplierService supplierService;
    @Mock
    private BrokerService brokerService;
    @Mock
    private UserService userService;
    @Mock
    private ContractReasonService contractReasonService;

    @Mock
    private CustomerNoteService customerNoteService;
    @Mock
    private ContactService mockContactService;
    @Mock
    private ContractService contractService;
    @Mock
    private BrokerTransferHistoryService brokerTransferHistoryService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(energyContractController).build();

        ReflectionTestUtils.setField(energyContractController, "defaultBrokerId", Long.valueOf(5));
    }

    @Test
    public void shouldReturnGasContract() throws Exception {

        List<Broker> brokers = new ArrayList<>();
        brokers.add(new Broker());

        List<Supplier> suppliers = new ArrayList<>();
        suppliers.add(new Supplier());

        when(brokerService.findAll()).thenReturn(brokers);
        when(supplierService.findAllOrderByBusinessName()).thenReturn(suppliers);

        mockMvc
                .perform(get("/admin/customer/gascontract").param("customerID", "123").param("customerSiteID", "456"))
                .andExpect(model().attribute("brokers", hasSize(1)))
                .andExpect(model().attribute("suppliers", hasSize(1)))
                .andExpect(model().attribute("gasCustomerContract", instanceOf(GasCustomerContract.class)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/newgascontract"));
    }

    @Test
    public void shouldReturnElectricityContract() throws Exception {

        List<Broker> brokers = new ArrayList<>();
        brokers.add(new Broker());

        List<Supplier> suppliers = new ArrayList<>();
        suppliers.add(new Supplier());

        when(brokerService.findAll()).thenReturn(brokers);
        when(supplierService.findAllOrderByBusinessName()).thenReturn(suppliers);

        mockMvc
                .perform(get("/admin/customer/eleccontract").param("customerID", "123").param("customerSiteID", "456"))
                .andExpect(model().attribute("brokers", hasSize(1)))
                .andExpect(model().attribute("suppliers", hasSize(1)))
                .andExpect(model().attribute("elecCustomerContract", instanceOf(ElecCustomerContract.class)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/neweleccontract"));
    }

    @Test
    public void shouldReturnSaveGasContract() throws Exception {

        mockMvc
                .perform(post("/gasCustomerContract")
                        .param("accountNumber", "1234658")
                        .param("broker.id", "54")
                        .param("supplier.id", "94")
                        .param("customer.id", "32")
                        .param("customerSite.id", "111"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/viewsite/111"));
    }

    @Test
    public void shouldReturnSaveElectricityContract() throws Exception {
        mockMvc
                .perform(post("/elecCustomerContract")
                        .param("accountNumber", "1234658")
                        .param("broker.id", "54")
                        .param("supplier.id", "94")
                        .param("customer.id", "32")
                        .param("customerSite.id", "111"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/viewsite/111"));
    }

    @Test
    public void shouldReturnEditGasContractViewWhenAccessedByNonDefaultBroker() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Role role = new Role();
        role.setId(3);
        role.setRole("BROKER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setBroker(broker);
        user.setRoles(roles);

        assertEquals(true, user.isBroker());

        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(Long.valueOf(258));
        gasCustomerContract.setBroker(broker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(gasContractService.findById(Long.valueOf(258))).thenReturn(gasCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        mockMvc
                .perform(get("/admin/customer/editgascontract/258"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/editgascontract"));
    }

    @Test
    public void shouldReturnEditGasContractViewWhenAccessedByDefaultBrokerAndUserBrokerIsNotNull() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Broker defaultBroker = new Broker();
        defaultBroker.setId(Long.valueOf(5));
        defaultBroker.setBusinessName("Default CEC Broker");

        Role role = new Role();
        role.setId(3);
        role.setRole("BROKER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setBroker(broker);
        user.setRoles(roles);

        assertEquals(true, user.isBroker());

        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(Long.valueOf(258));
        gasCustomerContract.setBroker(defaultBroker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(gasContractService.findById(Long.valueOf(258))).thenReturn(gasCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);
        when(gasContractService.save(any(GasCustomerContract.class))).thenReturn(gasCustomerContract);

        mockMvc
                .perform(get("/admin/customer/editgascontract/258"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/editgascontract"));
    }

    @Test
    public void shouldGasRedirectToCustomerContractsAccessedByBrokerWhoIsntOnTheContract() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Broker randomBroker = new Broker();
        randomBroker.setId(999l);

        Broker defaultBroker = new Broker();
        defaultBroker.setId(Long.valueOf(5));
        defaultBroker.setBusinessName("Default CEC Broker");

        Role role = new Role();
        role.setId(3);
        role.setRole("BROKER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setBroker(randomBroker);
        user.setRoles(roles);

        assertEquals(true, user.isBroker());

        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(Long.valueOf(258));
        gasCustomerContract.setBroker(broker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(gasContractService.findById(Long.valueOf(258))).thenReturn(gasCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        mockMvc
                .perform(get("/admin/customer/editgascontract/258"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/unauthorised"));
    }

    @Test
    public void shouldGasRedirectToCustomerContractsWhenUserIsNotBrokerOrSuperAdmin() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Role role = new Role();
        role.setId(1);
        role.setRole("ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setRoles(roles);

        assertEquals(false, user.isBroker());

        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(Long.valueOf(258));
        gasCustomerContract.setBroker(broker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(gasContractService.findById(Long.valueOf(258))).thenReturn(gasCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        mockMvc
                .perform(get("/admin/customer/editgascontract/258"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldGasRedirectToCustomerContractsWhenUserIsSuperAdmin() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Role role = new Role();
        role.setId(2);
        role.setRole("SUPERADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setRoles(roles);

        assertEquals(false, user.isBroker());

        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(Long.valueOf(258));
        gasCustomerContract.setBroker(broker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(gasContractService.findById(Long.valueOf(258))).thenReturn(gasCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        mockMvc
                .perform(get("/admin/customer/editgascontract/258"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/editgascontract"));
    }


    //start electricity tests here
    @Test
    public void shouldReturnEditElectricityContractViewWhenAccessedByNonDefaultBroker() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Role role = new Role();
        role.setId(3);
        role.setRole("BROKER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setBroker(broker);
        user.setRoles(roles);

        assertEquals(true, user.isBroker());

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setId(Long.valueOf(258));
        elecCustomerContract.setBroker(broker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(elecContractService.findById(Long.valueOf(258))).thenReturn(elecCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        mockMvc
                .perform(get("/admin/customer/editeleccontract/258"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/editeleccontract"));
    }

    @Test
    public void shouldReturnEditElectricityContractViewWhenAccessedByDefaultBrokerAndUserBrokerIsNotNull() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Broker defaultBroker = new Broker();
        defaultBroker.setId(Long.valueOf(5));
        defaultBroker.setBusinessName("Default CEC Broker");

        Role role = new Role();
        role.setId(3);
        role.setRole("BROKER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setBroker(broker);
        user.setRoles(roles);

        assertEquals(true, user.isBroker());

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setId(Long.valueOf(258));
        elecCustomerContract.setBroker(defaultBroker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(elecContractService.findById(Long.valueOf(258))).thenReturn(elecCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);
        when(elecContractService.save(any(ElecCustomerContract.class))).thenReturn(elecCustomerContract);

        mockMvc
                .perform(get("/admin/customer/editeleccontract/258"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/editeleccontract"));
    }


    @Test
    public void shouldElectricityRedirectToCustomerContractsAccessedByBrokerWhoIsntOnTheContract() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Broker randomBroker = new Broker();
        randomBroker.setId(999l);

        Broker defaultBroker = new Broker();
        defaultBroker.setId(Long.valueOf(5));
        defaultBroker.setBusinessName("Default CEC Broker");

        Role role = new Role();
        role.setId(3);
        role.setRole("BROKER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setBroker(randomBroker);
        user.setRoles(roles);

        assertEquals(user.isBroker(), true);

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setId(Long.valueOf(258));
        elecCustomerContract.setBroker(broker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(elecContractService.findById(Long.valueOf(258))).thenReturn(elecCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        mockMvc
                .perform(get("/admin/customer/editeleccontract/258"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/unauthorised"));
    }

    @Test
    public void shouldElectricityRedirectToCustomerContractsWhenUserIsNotBrokerOrSuperAdmin() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Role role = new Role();
        role.setId(1);
        role.setRole("ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setRoles(roles);

        assertEquals(user.isBroker(), false);

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setId(Long.valueOf(258));
        elecCustomerContract.setBroker(broker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(elecContractService.findById(Long.valueOf(258))).thenReturn(elecCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        mockMvc
                .perform(get("/admin/customer/editeleccontract/258"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldElectricityRedirectToCustomerContractsWhenUserIsSuperAdmin() throws Exception {

        Broker broker = new Broker();
        broker.setId(874l);

        Role role = new Role();
        role.setId(2);
        role.setRole("SUPERADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(451l);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran.miskeen@cec.com");
        user.setPasswordHash("########################");
        user.setActive(1);
        user.setRoles(roles);

        assertEquals(user.isBroker(), false);

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setId(Long.valueOf(258));
        elecCustomerContract.setBroker(broker);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        SecurityContextHolder.setContext(securityContext);

        when(elecContractService.findById(Long.valueOf(258))).thenReturn(elecCustomerContract);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        mockMvc
                .perform(get("/admin/customer/editeleccontract/258"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/editeleccontract"));
    }

    @Test
    public void shouldDeleteElectricContract() throws Exception {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(52l);

        ElecCustomerContract electricContract = new ElecCustomerContract();
        electricContract.setId(Long.valueOf(258));
        electricContract.setCustomerSite(customerSite);

        when(elecContractService.findById(any(Long.class))).thenReturn(electricContract);

        mockMvc
                .perform(get("/admin/customer/deleteElecContract/258"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/viewsite/52"));
    }

    @Test
    public void shouldNotDeleteElectricContract() throws Exception {
        when(elecContractService.findById(any(Long.class))).thenReturn(null);

        mockMvc
                .perform(get("/admin/customer/deleteElecContract/258"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldDeleteGasContract() throws Exception {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(52l);

        GasCustomerContract gasContract = new GasCustomerContract();
        gasContract.setId(Long.valueOf(258));
        gasContract.setCustomerSite(customerSite);

        when(gasContractService.findById(any(Long.class))).thenReturn(gasContract);

        mockMvc
                .perform(get("/admin/customer/deleteGasContract/258"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/viewsite/52"));
    }

    @Test
    public void shouldNotDeleteGasContract() throws Exception {

        when(gasContractService.findById(any(Long.class))).thenReturn(null);

        mockMvc
                .perform(get("/admin/customer/deleteGasContract/258"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldGetGasContractNotes() throws Exception {

        GasCustomerContract gasContract = new GasCustomerContract();
        gasContract.setId(5l);

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(12l);
        customerNote.setGasCustomerContract(gasContract);

        List<CustomerNote> customerNotes = Arrays.asList(customerNote);

        when(gasContractService.findById(any(Long.class))).thenReturn(gasContract);
        when(customerNoteService.findByGasCustomerContractOrderByDateCreatedDesc(any(GasCustomerContract.class))).thenReturn(customerNotes);

        mockMvc
                .perform(get("/admin/customer/gasContractNotes").param("id", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/gasContractNotes"))
                .andExpect(model().attribute("gasContractNotes", customerNotes));
    }

    @Test
    public void shouldGetElectricContractNotes() throws Exception {

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setId(5l);

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(12l);
        customerNote.setElecCustomerContract(elecCustomerContract);

        List<CustomerNote> customerNotes = Arrays.asList(customerNote);

        when(elecContractService.findById(any(Long.class))).thenReturn(elecCustomerContract);
        when(customerNoteService.findByElecCustomerContractOrderByDateCreatedDesc(any(ElecCustomerContract.class))).thenReturn(customerNotes);

        mockMvc
                .perform(get("/admin/customer/elecContractNotes").param("id", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/elecContractNotes"))
                .andExpect(model().attribute("elecContractNotes", customerNotes));
    }

    @Test
    public void shouldSaveElectricContractNote() throws Exception {
        ElecCustomerContract contract = new ElecCustomerContract();
        contract.setId(Long.valueOf(41));

        CustomerNote customerNote = new CustomerNote();
        customerNote.setElecCustomerContract(contract);

        when(customerNoteService.save(any(CustomerNote.class))).thenReturn(customerNote);

        mockMvc
                .perform(post("/elecContractNote")
                        .param("note", "hello")
                        .param("relatedTo", "POTENTIAL"))
                .andExpect(redirectedUrl("/admin/customer/editeleccontract/41"))
                .andExpect(status().is3xxRedirection());

        verify(customerNoteService, times(1)).save(any(CustomerNote.class));
    }

    @Test
    public void shouldSaveGasContractNote() throws Exception {
        GasCustomerContract contract = new GasCustomerContract();
        contract.setId(Long.valueOf(41));

        CustomerNote customerNote = new CustomerNote();
        customerNote.setGasCustomerContract(contract);

        when(customerNoteService.save(any(CustomerNote.class))).thenReturn(customerNote);

        mockMvc
                .perform(post("/gasContractNote")
                        .param("note", "hello")
                        .param("relatedTo", "POTENTIAL"))
                .andExpect(redirectedUrl("/admin/customer/editgascontract/41"))
                .andExpect(status().is3xxRedirection());

        verify(customerNoteService, times(1)).save(any(CustomerNote.class));
    }

}
