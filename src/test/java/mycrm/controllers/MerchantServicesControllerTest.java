package mycrm.controllers;

import mycrm.models.*;
import mycrm.search.MerchantServicesContractSearchService;
import mycrm.search.UtilitySearchService;
import mycrm.services.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class MerchantServicesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerSiteService mockCustomerSiteService;

    @Mock
    private DoNotRenewReasonService doNotRenewReasonService;

    @Mock
    private BrokerService mockBrokerService;

    @Mock
    private MerchantServicesService mockMerchantServicesService;

    @Mock
    private CustomerSite mockCustomerSite;

    @Mock
    private MerchantServicesContractSearchService merchantServicesContractSearchService;

    @InjectMocks
    private MerchantServicesController merchantServicesController;

    @Mock
    private ContractService mockContractService;

    @Mock
    private ContractReasonService contractReasonService;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(merchantServicesController).build();
    }

    @Test
    public void shouldReturnCreateMerchantServicesForm() throws Exception {
        List<Broker> listOfBrokers = Collections.singletonList(new Broker());

        when(mockBrokerService.findAll()).thenReturn(listOfBrokers);

        when(mockCustomerSiteService.findById(1L)).thenReturn(mockCustomerSite);

        mockMvc.perform(get("/admin/customer/manage-merchant-services/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/manage-merchant-services"))
                .andExpect(model().attribute("brokers", listOfBrokers))
                .andExpect(model().attribute("customerSite", mockCustomerSite))
                .andExpect(model().attribute("merchantServicesContract", instanceOf(MerchantServicesContract.class)));
    }

    @Test
    public void createMerchantServicesContract() throws Exception {
        MerchantServicesContract contract = new MerchantServicesContract();
        contract.setId(0L);/**/
        contract.setCustomerSite(mockCustomerSite);

        when(mockMerchantServicesService.save(any(MerchantServicesContract.class))).thenReturn(contract);

        mockMvc.perform(post("/merchantServicesContract"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/viewsite/0"));
    }

    @Test
    public void shouldReturnAdminMerchantServiceLeadsPage() throws Exception {

        MerchantServicesSearchResult merchantServicesSearchResult = MerchantServicesSearchResult.builder().build();
        Set<String> leads = new HashSet<>();

        when(merchantServicesContractSearchService.searchMerchantServicesContract(
                any(MerchantServicesContractSearch.class), any(Integer.class)))
                .thenReturn(merchantServicesSearchResult);

        when(mockContractService.getCampaigns()).thenReturn(leads);

        mockMvc.perform(get("/admin/merchant-services/leads/1"))
                .andExpect(view().name("admin/merchant-services/leads"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnLostMerchantRenewalsIndexPage() throws Exception {

        MerchantServicesSearchResult merchantServicesSearchResult = MerchantServicesSearchResult.builder().build();
        Set<String> leads = new HashSet<>();

        when(merchantServicesContractSearchService.searchMerchantServicesContract(
                any(MerchantServicesContractSearch.class), any(Integer.class)))
                .thenReturn(merchantServicesSearchResult);

        when(mockContractService.getCampaigns()).thenReturn(leads);

        mockMvc.perform(get("/admin/merchant-services/lost-renewals/1"))
                .andExpect(view().name("admin/merchant-services/lost-renewals"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnRenewalsMerchantRenewalsIndexPage() throws Exception {

        MerchantServicesSearchResult merchantServicesSearchResult = MerchantServicesSearchResult.builder().build();
        Set<String> leads = new HashSet<>();

        when(merchantServicesContractSearchService.searchMerchantServicesContract(
                any(MerchantServicesContractSearch.class), any(Integer.class)))
                .thenReturn(merchantServicesSearchResult);

        when(mockContractService.getCampaigns()).thenReturn(leads);

        mockMvc.perform(get("/admin/merchant-services/renewals/1"))
                .andExpect(view().name("admin/merchant-services/renewals"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCallbacksMerchantRenewalsIndexPage() throws Exception {

        MerchantServicesSearchResult merchantServicesSearchResult = MerchantServicesSearchResult.builder().build();
        Set<String> leads = new HashSet<>();

        when(merchantServicesContractSearchService.searchMerchantServicesContract(
                any(MerchantServicesContractSearch.class), any(Integer.class)))
                .thenReturn(merchantServicesSearchResult);

        when(mockContractService.getCampaigns()).thenReturn(leads);

        mockMvc.perform(get("/admin/merchant-services/callbacks/1"))
                .andExpect(view().name("admin/merchant-services/callbacks"))
                .andExpect(status().isOk());
    }
}
