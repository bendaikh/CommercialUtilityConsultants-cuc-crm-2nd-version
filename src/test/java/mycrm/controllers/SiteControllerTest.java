package mycrm.controllers;

import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.UtilityContract;
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

public class SiteControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private SiteController siteController;

    @Mock
    private CustomerSiteService mockCustomerSiteService;

    @Mock
    private GasContractService mockGasContractService;

    @Mock
    private ElecContractService mockElecContractService;

    @Mock
    private CustomerService mockCustomerService;

    @Mock
    private ContactService mockContactService;

    @Mock
    private CustomerDataService mockCustomerDataService;

    @Mock
    private UtilityContractService mockUtilityContractService;

    @Mock
    private LinkedAccountService linkedAccountService;

    @Mock
    private CustomerSiteTransferHistoryService customerSiteTransferHistoryService;

    @Mock
    private MerchantServicesService mockMerchantServicesService;

    @Mock
    private VoipContractService voipContractService;
    @Mock
    private WaterContractService waterContractService;
    @Mock
    private SolarContractService solarContractService;
    @Mock
    private LandlineContractService landlineContractService;
    @Mock
    private BroadbandContractService broadbandContractService;

    @Mock
    private MobileContractService mobileContractService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(siteController).build();
    }

    @Test
    public void shouldReturnCustomerSite() throws Exception {

        Long id = 50L;

        Customer customer = new Customer("Mahmoods");

        List<CustomerSite> customerSite = new ArrayList<>();
        customerSite.add(new CustomerSite(customer));

        List<GasCustomerContract> latestGasCustomerContract = new ArrayList<>();
        latestGasCustomerContract.add(new GasCustomerContract());

        List<ElecCustomerContract> latestElecCustomerContract = new ArrayList<>();
        latestElecCustomerContract.add(new ElecCustomerContract());

        when(mockCustomerService.findById(id)).thenReturn(customer);
        when(mockCustomerSiteService.findByCustomer(customer)).thenReturn(customerSite);
//        when(mockGasContractService.findLatestGasCustomerContractByCustomer(any(Long.class))).thenReturn(latestGasCustomerContract);
//        when(mockElecContractService.findLatestElecCustomerContractByCustomer(any(Long.class))).thenReturn(latestElecCustomerContract);

        mockMvc
                .perform(get("/admin/customer/customersites/50"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/customersites"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)))
//              .andExpect(model().attribute("latestGasCustomerContract", latestGasCustomerContract))
//              .andExpect(model().attribute("latestElecCustomerContract", latestElecCustomerContract))
                .andExpect(model().attribute("customerSites", hasSize(1)));
    }

    @Test
    public void shouldReturnSiteView() throws Exception {

        CustomerSite customerSite = new CustomerSite();
        List<GasCustomerContract> gasCustomerContracts = new ArrayList<>();
        gasCustomerContracts.add(new GasCustomerContract());
        List<ElecCustomerContract> elecCustomerContracts = new ArrayList<>();
        elecCustomerContracts.add(new ElecCustomerContract());

        when(mockCustomerSiteService.findById(1L)).thenReturn(customerSite);
        when(mockGasContractService.findByCustomerSite(customerSite)).thenReturn(gasCustomerContracts);
        when(mockElecContractService.findByCustomerSite(customerSite)).thenReturn(elecCustomerContracts);
        when(customerSiteTransferHistoryService.findCustomerSiteTransferHistoryByCustomerSite(any(CustomerSite.class))).thenReturn(new ArrayList<>());

        mockMvc
                .perform(get("/admin/customer/viewsite/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customerSite", customerSite))
                .andExpect(model().attribute("gasCustomerContracts", hasSize(1)))
                .andExpect(model().attribute("elecCustomerContracts", hasSize(1)))
                .andExpect(model().attribute("siteTransferMessage", hasSize(0)))
                .andExpect(view().name("admin/customer/viewsite"));
    }

    @Test
    public void shouldReturnEditSitePage() throws Exception {
        Customer customer = new Customer();
        customer.setId(98L);

        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(675L);
        customerSite.setCustomer(customer);

        when(mockCustomerSiteService.findById(675L)).thenReturn(customerSite);

        mockMvc
                .perform(get("/admin/customer/editsite/675"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customerSite", customerSite))
                .andExpect(model().attribute("customerId", 98L))
                .andExpect(view().name("admin/customer/newsite"));
    }

    @Test
    public void shouldReturnNewSitePage() throws Exception {
        mockMvc
                .perform(get("/admin/customer/newsite/98"))
                .andExpect(model().attribute("customerSite", instanceOf(CustomerSite.class)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/newsite"));
    }

    @Test
    public void shouldCreateNewCustomerSite() throws Exception {
        Customer customer = new Customer();
        customer.setId(98L);

        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(675L);
        customerSite.setCustomer(customer);

        when(mockCustomerSiteService.save(any(CustomerSite.class))).thenReturn(customerSite);

        mockMvc
                .perform(post("/customerSite")
                        .param("customer.id", "98")
                        .param("mpr", "987456321")
                        .param("mpanLineOne", "12")
                        .param("mpanLineTwo", "345")
                        .param("mpanLineThree", "678")
                        .param("mpanBottomLine", "90")
                        .param("siteName", "CEC Commercial")
                        .param("siteAddr", "Little Germany Road")
                        .param("siteAddr1", "Little Horton")
                        .param("siteCity", "Bradford")
                        .param("sitePostcodeIn", "BD1")
                        .param("sitePostcodeOut", "1AA")
                        .param("siteTelephone", "01274987456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/viewsite/675"));
    }

    @Test
    public void shouldDeleteCustomerSite() throws Exception {

        Customer customer = new Customer("New Customer");
        customer.setId(54L);

        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(145L);
        customerSite.setCustomer(customer);

        when(mockCustomerSiteService.findById(any(long.class))).thenReturn(customerSite);

        mockMvc.perform(get("/admin/customer/deleteSite/145"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/customersites/54"));

        verify(mockCustomerSiteService, times(1)).deleteById(any(long.class));
    }

    @Test
    public void shouldRedirectWhenUnableToFindSite() throws Exception {

        when(mockCustomerSiteService.findById(any(long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/deleteSite/145"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldReturnElectricContractPopup() throws Exception {

        ElecCustomerContract electricContract = new ElecCustomerContract();
        electricContract.setId(66L);

        when(mockElecContractService.findById(any(long.class))).thenReturn(electricContract);

        mockMvc.perform(get("/admin/customer/electric-contract-popup/66"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("electricContract", electricContract))
                .andExpect(view().name("admin/customer/electric-contract-popup"));
    }

    @Test
    public void shouldNotReturnElectricContractPopup() throws Exception {

        when(mockElecContractService.findById(any(long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/electric-contract-popup/66"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldReturnGasContractPopup() throws Exception {

        GasCustomerContract gasContract = new GasCustomerContract();
        gasContract.setId(66L);

        when(mockGasContractService.findById(any(long.class))).thenReturn(gasContract);

        mockMvc.perform(get("/admin/customer/gas-contract-popup/66"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("gasContract", gasContract))
                .andExpect(view().name("admin/customer/gas-contract-popup"));
    }

    @Test
    public void shouldNotReturnGasContractPopup() throws Exception {

        when(mockGasContractService.findById(any(long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/gas-contract-popup/66"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldReturnUtilityContractPopup() throws Exception {

        UtilityContract utilityContract = new UtilityContract();
        utilityContract.setId(66L);

        when(mockUtilityContractService.findById(any(long.class))).thenReturn(utilityContract);

        mockMvc.perform(get("/admin/customer/utility-contract-popup/66"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("utilityContract", utilityContract))
                .andExpect(view().name("admin/customer/utility-contract-popup"));
    }

    @Test
    public void shouldNotReturnUtilityContractPopup() throws Exception {

        when(mockUtilityContractService.findById(any(long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/utility-contract-popup/66"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldReturnUtilityCommunicationPopup() throws Exception {

        UtilityContract utilityContract = new UtilityContract();
        utilityContract.setId(66L);
        utilityContract.setCustomerSite(aCustomerSite());

        when(mockUtilityContractService.findById(any(long.class))).thenReturn(utilityContract);

        List<String> allCustomerEmails = new ArrayList<>();
        allCustomerEmails.add("email@email.com");

        when(mockCustomerDataService.getAllCustomerEmails(any(Customer.class))).thenReturn(allCustomerEmails);

        mockMvc.perform(get("/admin/customer/utility-communication-popup/66"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allCustomerEmails", allCustomerEmails))
                .andExpect(model().attribute("utilityContract", utilityContract))
                .andExpect(view().name("admin/customer/utility-communication-popup"));
    }

    @Test
    public void shouldNotReturnUtilityCommunicationPopup() throws Exception {

        when(mockUtilityContractService.findById(any(long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/utility-communication-popup/66"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldReturnGasCommunicationPopup() throws Exception {

        GasCustomerContract gasContract = new GasCustomerContract();
        gasContract.setId(66L);
        gasContract.setCustomerSite(aCustomerSite());

        when(mockGasContractService.findById(any(long.class))).thenReturn(gasContract);

        List<String> allCustomerEmails = new ArrayList<>();
        allCustomerEmails.add("email@email.com");

        when(mockCustomerDataService.getAllCustomerEmails(any(Customer.class))).thenReturn(allCustomerEmails);

        mockMvc.perform(get("/admin/customer/gas-communication-popup/66"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allCustomerEmails", allCustomerEmails))
                .andExpect(model().attribute("gasContract", gasContract))
                .andExpect(view().name("admin/customer/gas-communication-popup"));
    }

    @Test
    public void shouldNotReturnGasCommunicationPopup() throws Exception {

        when(mockGasContractService.findById(any(long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/gas-communication-popup/66"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldReturnElectricCommunicationPopup() throws Exception {

        ElecCustomerContract electricContract = new ElecCustomerContract();
        electricContract.setId(66L);
        electricContract.setCustomerSite(aCustomerSite());

        when(mockElecContractService.findById(any(long.class))).thenReturn(electricContract);

        List<String> allCustomerEmails = new ArrayList<>();
        allCustomerEmails.add("email@email.com");

        when(mockCustomerDataService.getAllCustomerEmails(any(Customer.class))).thenReturn(allCustomerEmails);

        mockMvc.perform(get("/admin/customer/electric-communication-popup/66"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allCustomerEmails", allCustomerEmails))
                .andExpect(model().attribute("electricContract", electricContract))
                .andExpect(view().name("admin/customer/electric-communication-popup"));
    }

    @Test
    public void shouldNotReturnElectricCommunicationPopup() throws Exception {

        when(mockElecContractService.findById(any(long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/electric-communication-popup/66"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldReturnTransferSitePage() throws Exception {
        when(mockCustomerSiteService.findById(any(Long.class))).thenReturn(aCustomerSite());

        mockMvc.perform(get("/admin/customer/transfer-site/123"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customerDetails", instanceOf(Customer.class)))
                .andExpect(model().attribute("customerSite", instanceOf(CustomerSite.class)))
                .andExpect(view().name("admin/customer/transfer-site"));
    }

    @Test
    public void shouldNotReturnTransferSitePage() throws Exception {
        when(mockCustomerSiteService.findById(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/transfer-site/123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/editsite/123"));
    }

    @Test
    public void shouldNotTransferACustomerSiteButReturnUserBack() throws Exception {

        mockMvc.perform(get("/admin/customer/transfer-customer-site")
                .param("selectedCustomerSite", "999")
                .param("selectedCustomer", "23"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/viewsite/999"));
    }

    @Test
    public void shouldTransferACustomerSite() throws Exception {

        when(mockCustomerSiteService.findById(any(Long.class))).thenReturn(aCustomerSite());
        when(mockCustomerService.findById(any(Long.class))).thenReturn(aHappyCustomer());

        when(mockCustomerSiteService.transferCustomerSite(
                any(CustomerSite.class),
                any(Customer.class)))
                .thenReturn(aCustomerSite());

        mockMvc.perform(get("/admin/customer/transfer-customer-site")
                .param("selectedCustomerSite", "999")
                .param("selectedCustomer", "23"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/viewsite/999"));
    }

    @Test
    public void shouldReturnCustomerSearch() throws Exception {
        when(mockCustomerService.findByCustomerReference(any(String.class))).thenReturn(aHappyCustomer());
        when(mockCustomerSiteService.findById(any(Long.class))).thenReturn(aCustomerSite());

        mockMvc.perform(get("/admin/customer/customer-search?q=123&customerSite=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("searchResult", instanceOf(Customer.class)))
                .andExpect(model().attribute("customerSite", instanceOf(CustomerSite.class)))
                .andExpect(view().name("admin/customer/components/customer-search"));
    }


    private CustomerSite aCustomerSite() {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(999L);
        customerSite.setCustomer(aHappyCustomer());
        return customerSite;
    }

    private Customer aHappyCustomer() {
        Customer customer = new Customer();
        customer.setId(23L);
        return customer;
    }


}
