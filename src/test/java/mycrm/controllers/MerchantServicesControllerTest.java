package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.CustomerSite;
import mycrm.models.MerchantServicesContract;
import mycrm.services.BrokerService;
import mycrm.services.CustomerSiteService;
import mycrm.services.MerchantServicesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

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
    private BrokerService mockBrokerService;

    @Mock
    private MerchantServicesService mockMerchantServicesService;

    @Mock
    private CustomerSite mockCustomerSite;

    @InjectMocks
    private MerchantServicesController merchantServicesController;

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
        contract.setId(0L);
        contract.setCustomerSite(mockCustomerSite);

        when(mockMerchantServicesService.save(any(MerchantServicesContract.class))).thenReturn(contract);

        mockMvc.perform(post("/merchantServicesContract"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/viewsite/0"));
    }
}
