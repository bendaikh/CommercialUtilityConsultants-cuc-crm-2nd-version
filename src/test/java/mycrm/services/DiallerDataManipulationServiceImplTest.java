package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.Plugin;
import mycrm.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiallerDataManipulationServiceImplTest {

    @InjectMocks
    private DiallerDataManipulationServiceImpl diallerDataManipulationService;

    @Mock
    private UserService userService;

    @Mock
    private ElecContractService elecContractService;

    @Mock
    private GasContractService gasContractService;

    @Mock
    private ContactService contactService;

    @Mock
    private PluginService pluginService;


    @Test
    public void shouldGetAllDiallerDataForActiveUsers() {
        ReflectionTestUtils.setField(diallerDataManipulationService, "diallerProviderName", "8x8 Dialler API");

        List<User> activeUsers = new ArrayList<>();
        activeUsers.add(activeBrokerUser());

        when(userService.findAllActiveUsers()).thenReturn(activeUsers);
        when(gasContractService.findAllContactableDiallerGasContracts(any(String.class), any(Broker.class))).thenReturn(gasContractsList());
        when(elecContractService.findAllContactableDiallerElectricContracts(any(String.class), any(Broker.class))).thenReturn(elecContractsList());

        when(pluginService.getPluginByApi(any(String.class))).thenReturn(plugin());

        diallerDataManipulationService.getAllDiallerData();
    }

    @Test
    public void shouldAvoidCustomersWithNoNumbers() {

    }

    private Plugin plugin() {
        Plugin plugin = new Plugin();
        plugin.setApi("8x8 Dialler API");
        plugin.setPrimaryDataset("14");
        return plugin;
    }

    private List<GasCustomerContract> gasContractsList() {
        List<GasCustomerContract> contractList = new ArrayList<>();
        contractList.add(gasContractOne());
        contractList.add(gasContractTwo());
        return contractList;
    }

    private List<ElecCustomerContract> elecContractsList() {
        List<ElecCustomerContract> contractList = new ArrayList<>();
        contractList.add(elecContractOne());
        contractList.add(elecContractTwo());
        return contractList;
    }

    private Customer customer() {
        Customer customer = new Customer();
        customer.setId(32l);
        customer.setContactNumber("01274987456");
        customer.setMobileNumber("07597412541");
        return customer;
    }

    private GasCustomerContract gasContractOne() {
        GasCustomerContract contract = new GasCustomerContract();
        contract.setId(1l);
        contract.setLogType("CALLBACK");
        contract.setCustomer(customer());
        contract.setCustomerSite(customerSite());
        return contract;
    }

    private GasCustomerContract gasContractTwo() {
        GasCustomerContract contract = new GasCustomerContract();
        contract.setId(2l);
        contract.setLogType("POTENTIAL");
        contract.setCustomer(customer());
        contract.setCustomerSite(customerSite());
        return contract;
    }

    private ElecCustomerContract elecContractOne() {
        ElecCustomerContract contract = new ElecCustomerContract();
        contract.setId(1l);
        contract.setLogType("CALLBACK");
        contract.setCustomer(customer());
        contract.setCustomerSite(customerSite());
        return contract;
    }

    private ElecCustomerContract elecContractTwo() {
        ElecCustomerContract contract = new ElecCustomerContract();
        contract.setId(2l);
        contract.setLogType("POTENTIAL");
        contract.setCustomer(customer());
        contract.setCustomerSite(customerSite());
        return contract;
    }

    private CustomerSite customerSite() {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(99l);
        customerSite.setCustomer(customer());
        customerSite.setSiteAddr("Line 1");
        customerSite.setSiteAddr1("Line 2");
        customerSite.setSiteCity("Leeds");
        return customerSite;
    }

    private User activeBrokerUser() {
        Broker broker = new Broker();
        broker.setId(43l);
        broker.setBusinessName("Broker");

        User user = new User();
        user.setId(1l);
        user.setBroker(broker);
        user.setDiallerAgentReference("111");
        return user;
    }

}
