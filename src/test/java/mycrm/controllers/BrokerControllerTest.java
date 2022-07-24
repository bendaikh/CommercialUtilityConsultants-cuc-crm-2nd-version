package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.BrokerNote;
import mycrm.models.Customer;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.User;
import mycrm.notifications.NotificationService;
import mycrm.services.BrokerNoteService;
import mycrm.services.BrokerService;
import mycrm.services.ElecContractService;
import mycrm.services.GasContractService;
import mycrm.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
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

public class BrokerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BrokerService mockBrokerService;

    @Mock
    private GasContractService gasContractService;

    @Mock
    private ElecContractService elecContractService;

    @Mock
    private NotificationService mockNotifyService;

    @Mock
    private BrokerNoteService brokerNoteService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BrokerController brokerController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(brokerController).build();
    }

    @Test
    public void shouldReturnEditBrokerPage() throws Exception {
        Long id = 1L;

        when(mockBrokerService.findById(id)).thenReturn(new Broker());

        mockMvc.perform(get("/admin/broker/edit/" + id + ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/broker/newbroker"))
                .andExpect(model().attribute("broker", instanceOf(Broker.class)));
    }

    @Test
    public void shouldReturnNewBrokerPage() throws Exception {

        mockMvc.perform(get("/admin/broker/newbroker")).andExpect(status().isOk())
                .andExpect(view().name("admin/broker/newbroker"))
                .andExpect(model().attribute("broker", instanceOf(Broker.class)));
    }

    @Test
    public void shouldReturnViewOfBrokers() throws Exception {
        Long id = 2L;

        Broker broker = new Broker();

        User taggedUser = new User();
        taggedUser.setId(65L);

        BrokerNote brokerNote = new BrokerNote();
        brokerNote.setId(54L);
        brokerNote.setBroker(broker);
        brokerNote.setSubject("Subject");
        brokerNote.setNote("Note");
        brokerNote.setTaggedUser(taggedUser);
        brokerNote.setCompletedBy(taggedUser);
        brokerNote.setCompleted(true);

        when(mockBrokerService.findById(id)).thenReturn(broker);

        mockMvc.perform(get("/admin/broker/view/" + id + "")).andExpect(status().isOk())
                .andExpect(view().name("admin/broker/view"))
                .andExpect(model().attribute("broker", instanceOf(Broker.class)));
    }

    @Test
    public void shouldRedirectViewWhenBrokerNotFound() throws Exception {
        Long id = 2L;

        when(mockBrokerService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/admin/broker/view/" + id + "")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/brokers"));
    }

    @Test
    public void shouldRedirectToBrokerViewWhenEdittedBrokerSaved() throws Exception {

        mockMvc.perform(post("/broker").param("id", "24")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/broker/view/24"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRedirectToNewBrokerViewWhenFailingBrokerSave() throws Exception {
        when(mockBrokerService.save(any(Broker.class))).thenThrow(Exception.class);
        mockMvc.perform(post("/broker")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/broker/newBroker"));
    }

    @Test
    public void shouldReturnListOfBrokers() throws Exception {
        List<Broker> brokers = new ArrayList<>();
        brokers.add(new Broker());

        when(mockBrokerService.findAll()).thenReturn(brokers);

        mockMvc.perform(get("/admin/broker/brokers")).andExpect(status().isOk())
                .andExpect(model().attribute("findall", hasSize(1))).andExpect(view().name("admin/broker/brokers"));
    }

    @Test
    public void shouldNotFindBroker() throws Exception {

        when(mockBrokerService.findById(any(long.class))).thenReturn(null);

        mockMvc.perform(get("/admin/broker/brokercustomers/93"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/brokers"));

        verify(mockNotifyService, times(1)).addErrorMessage(any(String.class));
    }

    @Test
    public void shouldReturnBrokerCustomers() throws Exception {

        Broker broker = new Broker();
        broker.setId(93L);

        Customer customer = new Customer("JohnDoe");
        customer.setId(66L);

        List<Customer> brokerCustomerList = new ArrayList<>();
        brokerCustomerList.add(customer);

        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setCustomer(customer);
        gasCustomerContract.setBroker(broker);

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setCustomer(customer);
        elecCustomerContract.setBroker(broker);

        List<GasCustomerContract> gasCustomerContractList = Collections.singletonList(gasCustomerContract);
        List<ElecCustomerContract> elecCustomerContractList = Collections.singletonList(elecCustomerContract);

        when(mockBrokerService.findById(any(long.class))).thenReturn(broker);
        when(mockBrokerService.findCustomersByBroker(any(Broker.class))).thenReturn(brokerCustomerList);
        when(gasContractService.findByBroker(any(Broker.class))).thenReturn(gasCustomerContractList);
        when(elecContractService.findByBroker(any(Broker.class))).thenReturn(elecCustomerContractList);

        mockMvc.perform(get("/admin/broker/brokercustomers/93"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("broker", broker))
                .andExpect(model().attribute("gasCustomerContracts", gasCustomerContractList))
                .andExpect(model().attribute("elecCustomerContracts", elecCustomerContractList))
                .andExpect(model().attribute("customers", brokerCustomerList))
                .andExpect(view().name("admin/broker/brokercustomers"));
    }
}
