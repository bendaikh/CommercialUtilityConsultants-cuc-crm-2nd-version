package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.Contract;
import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.models.GasCustomerContract;
import mycrm.models.Supplier;
import mycrm.search.RenewalSearchService;
import mycrm.services.BrokerService;
import mycrm.services.ContractService;
import mycrm.services.SupplierService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class RenewalControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private RenewalController renewalController;

    @Mock
    private SupplierService mockSupplierService;

    @Mock
    private BrokerService mockBrokerService;

    @Mock
    private RenewalSearchService mockRenewalSearchService;

    @Mock
    private ContractService mockContractService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(renewalController).build();
    }

    @Test
    public void shouldReturnRenewalHomepage() throws Exception {

        List<Contract> searchResults = new ArrayList<>();
        searchResults.add(new GasCustomerContract());

        List<Supplier> supplierList = new ArrayList<>();
        supplierList.add(new Supplier());

        List<Broker> brokerList = new ArrayList<>();
        brokerList.add(new Broker());

        ContractSearchResult contractSearchResult = new ContractSearchResult();
        contractSearchResult.setReturnedContracts(searchResults);

        when(mockRenewalSearchService.searchCustomerContract(any(ContractSearch.class), eq(1))).thenReturn(contractSearchResult);
        when(mockSupplierService.findAllOrderByBusinessName()).thenReturn(supplierList);
        when(mockBrokerService.findAll()).thenReturn(brokerList);

        mockMvc
                .perform(get("/admin/renewals/index/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("brokers", hasSize(1)))
                .andExpect(model().attribute("suppliers", hasSize(1)))
                .andExpect(model().attribute("renewals", hasSize(1)))
                .andExpect(view().name("admin/renewals/index"));
    }

}
