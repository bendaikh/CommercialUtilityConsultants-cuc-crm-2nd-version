package mycrm.controllers;

import junit.framework.TestCase;
import mycrm.models.*;
import mycrm.services.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class BroadbandControllerTest {
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;
    @Mock
    private BrokerTransferHistoryService mockBrokerTransferHistoryService;
    @Mock
    private DoNotRenewReasonService doNotRenewReasonService;
    @Mock
    private CustomerSiteService customerSiteService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private BrokerService brokerService;
    @Mock
    private Authentication authentication;
    @Mock
    private UserService mockUserService;
    @Mock
    private Principal principal1;
    @Mock
    private SupplierService supplierService;
    @InjectMocks
    private BroadbandController broadbandController;

    @Mock
    private BroadbandContractService broadbandContractService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(broadbandController).build();
    }

    @Test
    public void shouldNewBroadbandContract() throws Exception {

        mockMvc.perform(get("/admin/customer/broadband/6"))
                .andExpect(view().name("admin/customer/manage-broadband-contract"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldPostNewBroadbandContract() throws Exception {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(6l);

        BroadbandContract broadbandContract = new BroadbandContract();
        broadbandContract.setCustomerSite(customerSite);

        when(broadbandContractService.save(any(BroadbandContract.class))).thenReturn(broadbandContract);

        mockMvc.perform(post("/broadbandContract"))
                .andExpect(redirectedUrl("/admin/customer/viewsite/6"))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void shouldReturnEditBroadbandContract() throws Exception {
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

        BroadbandContract broadbandContract = new BroadbandContract();
        broadbandContract.setId(6l);
        broadbandContract.setCustomerSite(customerSite);
        broadbandContract.setBroker(broker);

        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(mockUserService.findUserByUsername(any(String.class))).thenReturn(user);
        when(broadbandContractService.findById(any(Long.class))).thenReturn(broadbandContract);

        List<String> list = new ArrayList<>();
        list.add("Hello");

        when(mockBrokerTransferHistoryService.findLatestBroadbandBrokerTransferHistory(any(BroadbandContract.class))).thenReturn(list);

        mockMvc.perform(get("/admin/customer/broadband/6"))
                .andExpect(view().name("admin/customer/manage-broadband-contract"))
                .andExpect(status().isOk());
    }
}