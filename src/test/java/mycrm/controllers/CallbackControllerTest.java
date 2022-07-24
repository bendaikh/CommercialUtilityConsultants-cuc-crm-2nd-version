package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.CallbackSearchResult;
import mycrm.models.ContractSearch;
import mycrm.models.CustomerNote;
import mycrm.models.ElecCustomerContract;
import mycrm.models.NotedCallback;
import mycrm.models.Supplier;
import mycrm.search.CallbackSearchService;
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
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class CallbackControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CallbackController callbackController;

    @Mock
    private SupplierService mockSupplierService;

    @Mock
    private BrokerService mockBrokerService;

    @Mock
    private CallbackSearchService mockCallbackSearchService;

    @Mock
    private ContractService mockContractService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(callbackController).build();
    }

    @Test
    public void shouldReturnCallbacksHomepage() throws Exception {

        NotedCallback notedCallback = new NotedCallback();
        notedCallback.setContract(new ElecCustomerContract());
        notedCallback.setCustomerNotes(Arrays.asList(new CustomerNote()));

        List<Broker> brokers = new ArrayList<>();
        brokers.add(new Broker());

        List<Supplier> suppliers = new ArrayList<>();
        suppliers.add(new Supplier());

        List<NotedCallback> notedCallbacks = new ArrayList<>();
        notedCallbacks.add(notedCallback);

        CallbackSearchResult callbackSearchResult = new CallbackSearchResult();
        callbackSearchResult.setReturnedContracts(notedCallbacks);

        when(mockCallbackSearchService.searchCustomerContract(any(ContractSearch.class), eq(1))).thenReturn(callbackSearchResult);
        when(mockBrokerService.findAll()).thenReturn(brokers);
        when(mockSupplierService.findAllOrderByBusinessName()).thenReturn(suppliers);

        mockMvc
                .perform(get("/admin/callbacks/index/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("brokers", hasSize(1)))
                .andExpect(model().attribute("suppliers", hasSize(1)))
                .andExpect(model().attribute("callbacks", hasSize(1)))
                .andExpect(view().name("admin/callbacks/index"));
    }

}
