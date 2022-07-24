package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.Contract;
import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.models.Customer;
import mycrm.models.GasCustomerContract;
import mycrm.models.Supplier;
import mycrm.search.LostRenewalSearchService;
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

public class LostRenewalControllerTest {

    @InjectMocks
    private LostRenewalController lostRenewalController;

    @Mock
    private LostRenewalSearchService mockLostRenewalSearchService;

    @Mock
    private BrokerService mockBrokerService;

    @Mock
    private SupplierService mockSupplierService;

    @Mock
    private ContractService mockContractService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(lostRenewalController).build();
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
        customerContract.setLostRenewal(true);

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


        when(mockLostRenewalSearchService.searchCustomerContract(any(ContractSearch.class), eq(1))).thenReturn(contractSearchResult);
        when(mockBrokerService.findAll()).thenReturn(brokers);
        when(mockSupplierService.findAllOrderByBusinessName()).thenReturn(suppliers);

        mockMvc
                .perform(get("/admin/lost-renewals/index/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("brokers", hasSize(1)))
                .andExpect(model().attribute("lostRenewals", hasSize(1)))
                .andExpect(model().attribute("suppliers", hasSize(1)))
                .andExpect(model().attribute("totalResults", 1l))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("pageNumber", 1))
                .andExpect(model().attribute("totalContracts", 1l))
                .andExpect(view().name("admin/lost-renewals/index"));
    }
}
