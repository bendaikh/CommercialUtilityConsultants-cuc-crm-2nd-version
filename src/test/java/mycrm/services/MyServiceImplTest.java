package mycrm.services;

import mycrm.functions.UserHelper;
import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.MyCallback;
import mycrm.models.User;
import mycrm.models.UtilityContract;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyServiceImplTest {
    @InjectMocks
    private MyServiceImpl service;

    @Mock
    private GasContractService gasContractService;

    @Mock
    private ElecContractService elecContractService;

    @Mock
    private UtilityContractService utilityContractService;

    @Mock
    private UserHelper userHelper;

    @Test
    public void shouldReturnListOfTimes() {
        List<Date> callbackOpeningTimes = service.getCallbackOpeningTimes("2021-01-13");
        assertEquals(40, callbackOpeningTimes.size());
    }

    @Test
    public void shouldReturnEmptyListOfTodaysCallbacksIfUserIsNotBroker() {

        User user = new User();
        user.setId(1L);

        when(userHelper.getLoggedInUser()).thenReturn(user);
        List<MyCallback> myTodaysCallbacks = service.getMyTodaysCallbacks();

        assertEquals(0, myTodaysCallbacks.size());
    }

    @Test
    public void shouldGetMyTodaysCallbacks() {
        Broker broker = new Broker();
        broker.setId(2L);

        User user = new User();
        user.setId(1L);
        user.setBroker(broker);

        when(userHelper.getLoggedInUser()).thenReturn(user);

        when(gasContractService.findByLogTypeAndBrokerAndCallbackDate(any(Broker.class),
                any(Date.class),
                anyObject())).thenReturn(aGasCallbackList());

        when(elecContractService.findByLogTypeAndBrokerAndCallbackDate(any(Broker.class),
                any(Date.class),
                anyObject())).thenReturn(aElectricCallbackList());

        when(utilityContractService.findByLogTypeAndBrokerAndCallbackDate(any(Broker.class),
                any(Date.class),
                anyObject())).thenReturn(aUtilityCallbackList());

        List<MyCallback> myTodaysCallbacks = service.getMyTodaysCallbacks();
    }

    @Test
    public void shouldNotGetCallbacksForADateWhenNoBroker() {
        User user = new User();
        user.setId(1L);

        when(userHelper.getLoggedInUser()).thenReturn(user);
        List<MyCallback> myCallbacks = service.getCallbacksForDate("2020-12-12");

        assertEquals(0, myCallbacks.size());
    }

    @Test
    public void shouldGetCallbacksForADateWhenThereIsBroker() {
        Broker broker = new Broker();
        broker.setId(2L);

        User user = new User();
        user.setId(1L);
        user.setBroker(broker);

        when(userHelper.getLoggedInUser()).thenReturn(user);

        when(gasContractService.findByLogTypeAndBrokerAndCallbackDate(any(Broker.class),
                any(Date.class),
                anyObject())).thenReturn(aGasCallbackList());

        when(elecContractService.findByLogTypeAndBrokerAndCallbackDate(any(Broker.class),
                any(Date.class),
                anyObject())).thenReturn(aElectricCallbackList());

        when(utilityContractService.findByLogTypeAndBrokerAndCallbackDate(any(Broker.class),
                any(Date.class),
                anyObject())).thenReturn(aUtilityCallbackList());

        List<MyCallback> myCallbackTimeSlots = service.getCallbacksForDate("2020-12-12");

        assertEquals(40, myCallbackTimeSlots.size());
    }

    private List<GasCustomerContract> aGasCallbackList() {
        GasCustomerContract contract = new GasCustomerContract();
        contract.setId(2L);
        contract.setCustomerSite(aCustomerSite());
        contract.setCustomer(aCustomer());
        contract.setLogType("CALLBACK");
        contract.setCallbackDateTime(new Date(120, 12, 12, 15, 45, 0));

        return Arrays.asList(contract);
    }

    private List<ElecCustomerContract> aElectricCallbackList() {
        ElecCustomerContract contract = new ElecCustomerContract();
        contract.setId(2L);
        contract.setCustomerSite(aCustomerSite());
        contract.setCustomer(aCustomer());
        contract.setLogType("CALLBACK");
        contract.setCallbackDateTime(new Date(120, 12, 12, 15, 45, 0));

        return Arrays.asList(contract);
    }

    private List<UtilityContract> aUtilityCallbackList() {
        UtilityContract contract = new UtilityContract();
        contract.setId(2L);
        contract.setCustomerSite(aCustomerSite());
        contract.setLogType("CALLBACK");
        contract.setCallbackDateTime(new Date(120, 12, 12, 15, 45, 0));

        return Arrays.asList(contract);
    }

    private Customer aCustomer() {
        Customer customer = new Customer();
        customer.setId(33L);
        customer.setBusinessName("Blah");

        return customer;
    }

    private CustomerSite aCustomerSite() {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(99L);
        customerSite.setCustomer(aCustomer());

        return customerSite;
    }
}
