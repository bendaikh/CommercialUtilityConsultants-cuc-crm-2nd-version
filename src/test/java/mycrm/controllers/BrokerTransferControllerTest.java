package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.BrokerContractPackage;
import mycrm.services.BrokerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class BrokerTransferControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BrokerTransferController controller;

    @Mock
    private BrokerService brokerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldReturnBrokerContracts() throws Exception {
        BrokerContractPackage brokerContracts = BrokerContractPackage.builder().build();

        Broker selectedBroker = new Broker();
        selectedBroker.setId(61l);

        List<Broker> brokers = new ArrayList<>();
        brokers.add(selectedBroker);

        when(brokerService.findById(any(Long.class))).thenReturn(selectedBroker);
        when(brokerService.findAll()).thenReturn(brokers);
        when(brokerService.findBrokerContractStats(any(Broker.class))).thenReturn(brokerContracts);

        mockMvc.perform(get("/admin/broker/broker-transfer/contracts/61"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/broker/broker-transfer/contracts"))
                .andExpect(model().attribute("selectedBroker", selectedBroker))
                .andExpect(model().attribute("brokers", brokers))
                .andExpect(model().attribute("brokerContracts", brokerContracts));
    }

    @Test
    public void shouldTransferContractsAndShowSuccessMessage() throws Exception {
        when(brokerService.transferBrokerContracts(anyObject())).thenReturn(true);

        mockMvc.perform(post("/admin/broker/broker-transfer/transfer-broker-contracts"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/broker/broker-transfer/confirmation?success=true"));
    }

    @Test
    public void shouldTransferContractsAndShowFailureMessage() throws Exception {
        when(brokerService.transferBrokerContracts(anyObject())).thenReturn(false);

        mockMvc.perform(post("/admin/broker/broker-transfer/transfer-broker-contracts"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/broker/broker-transfer/confirmation?success=false"));
    }

    @Test
    public void shouldShowSuccessfulConfirmationPage() throws Exception {
        mockMvc.perform(get("/admin/broker/broker-transfer/confirmation?success=true"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/broker/broker-transfer/confirmation"))
                .andExpect(model().attribute("message", "Updated"));
    }

    @Test
    public void shouldShowFailedConfirmationPage() throws Exception {
        mockMvc.perform(get("/admin/broker/broker-transfer/confirmation?success=false"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/broker/broker-transfer/confirmation"))
                .andExpect(model().attribute("message", "Failed"));
    }
}
