package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.ContractSearch;
import mycrm.models.CustomerSite;
import mycrm.models.Role;
import mycrm.models.User;
import mycrm.models.UtilityCallbackSearchResult;
import mycrm.models.UtilityContract;
import mycrm.models.UtilitySearchResult;
import mycrm.search.UtilitySearchService;
import mycrm.services.BrokerService;
import mycrm.services.BrokerTransferHistoryService;
import mycrm.services.ContractService;
import mycrm.services.CustomerSiteService;
import mycrm.services.SupplierService;
import mycrm.services.UserService;
import mycrm.services.UtilityContractService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UtilityContractControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private UtilityContractController utilityContractController;

    @Mock
    private CustomerSiteService mockCustomerSiteService;

    @Mock
    private UtilityContractService utilityContractService;

    @Mock
    private SupplierService mockSupplierService;

    @Mock
    private BrokerService mockBrokerService;

    @Mock
    private UtilitySearchService mockUtilitySearchService;

    @Mock
    private ContractService mockContractService;

    @Mock
    private UserService mockUserService;

    @Mock
    private BrokerTransferHistoryService mockBrokerTransferHistoryService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Principal principal1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(utilityContractController).build();
    }

    @Test
    public void shouldNewUtilityContract() throws Exception {

        mockMvc.perform(get("/admin/customer/manage-utility-contract/6"))
                .andExpect(view().name("admin/customer/manage-utility-contract"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldPostNewUtilityContract() throws Exception {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(6l);

        UtilityContract utilityContract = new UtilityContract();
        utilityContract.setCustomerSite(customerSite);

        when(utilityContractService.save(any(UtilityContract.class))).thenReturn(utilityContract);

        mockMvc.perform(post("/utilityContract"))
                .andExpect(redirectedUrl("/admin/customer/viewsite/6"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldReturnEditUtilityContract() throws Exception {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(6l);

        Broker broker = new Broker();
        broker.setId(2l);
        broker.setBrokerReference("JOHN");

        Role role = new Role();
        role.setId(1);
        role.setRole("BROKER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(1l);
        user.setUsername("we.web");
        user.setBroker(broker);
        user.setRoles(roles);

        UtilityContract utilityContract = new UtilityContract();
        utilityContract.setId(6l);
        utilityContract.setCustomerSite(customerSite);
        utilityContract.setBroker(broker);

        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(mockUserService.findUserByUsername(any(String.class))).thenReturn(user);
        when(utilityContractService.findById(any(Long.class))).thenReturn(utilityContract);

        List<String> list = new ArrayList<>();
        list.add("Hello");

        when(mockBrokerTransferHistoryService.findLatestUtilityBrokerTransferHistory(any(UtilityContract.class))).thenReturn(list);

        mockMvc.perform(get("/admin/customer/edit-utility-contract/6"))
                .andExpect(view().name("admin/customer/manage-utility-contract"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn404ErrorWhenCallingEditUtilityContract() throws Exception {

        when(utilityContractService.findById(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/customer/edit-utility-contract/65"))
                .andExpect(view().name("admin/error/404"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAdminUtilityLeadsPage() throws Exception {

        UtilitySearchResult utilitySearchResult = new UtilitySearchResult();
        Set<String> leads = new HashSet<>();

        when(mockUtilitySearchService.searchUtilityContracts(
                any(ContractSearch.class), any(Integer.class)))
                .thenReturn(utilitySearchResult);

        when(mockContractService.getCampaigns()).thenReturn(leads);

        mockMvc.perform(get("/admin/utility/leads/1"))
                .andExpect(view().name("admin/utility/leads"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAdminUtilityIndexPage() throws Exception {

        UtilitySearchResult utilitySearchResult = new UtilitySearchResult();
        Set<String> leads = new HashSet<>();

        when(mockUtilitySearchService.searchUtilityContracts(
                any(ContractSearch.class), any(Integer.class)))
                .thenReturn(utilitySearchResult);

        when(mockContractService.getCampaigns()).thenReturn(leads);

        mockMvc.perform(get("/admin/utility/index/1"))
                .andExpect(view().name("admin/utility/index"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnLostUtilityRenewalsIndexPage() throws Exception {

        UtilitySearchResult utilitySearchResult = new UtilitySearchResult();
        Set<String> leads = new HashSet<>();

        when(mockUtilitySearchService.searchUtilityContracts(
                any(ContractSearch.class), any(Integer.class)))
                .thenReturn(utilitySearchResult);

        when(mockContractService.getCampaigns()).thenReturn(leads);

        mockMvc.perform(get("/admin/utility/lost-renewals/1"))
                .andExpect(view().name("admin/utility/lost-renewals"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAdminUtilityRenewalsPage() throws Exception {

        UtilitySearchResult utilitySearchResult = new UtilitySearchResult();
        Set<String> leads = new HashSet<>();

        when(mockUtilitySearchService.searchUtilityContracts(
                any(ContractSearch.class), any(Integer.class)))
                .thenReturn(utilitySearchResult);

        when(mockContractService.getCampaigns()).thenReturn(leads);

        mockMvc.perform(get("/admin/utility/renewals/1"))
                .andExpect(view().name("admin/utility/renewals"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAdminUtilityCallbacksPage() throws Exception {

        UtilityCallbackSearchResult utilitySearchResult = new UtilityCallbackSearchResult();

        when(mockUtilitySearchService.searchNotedCallbackContracts(
                any(ContractSearch.class), any(Integer.class)))
                .thenReturn(utilitySearchResult);

        Set<String> leads = new HashSet<>();
        when(mockContractService.getCampaigns()).thenReturn(leads);

        mockMvc.perform(get("/admin/utility/callbacks/1"))
                .andExpect(view().name("admin/utility/callbacks"))
                .andExpect(status().isOk());
    }
}
