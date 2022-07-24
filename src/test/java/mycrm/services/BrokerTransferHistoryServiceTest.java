package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.BrokerTransferHistory;
import mycrm.models.GasCustomerContract;
import mycrm.models.User;
import mycrm.repositories.BrokerRepository;
import mycrm.repositories.BrokerTransferHistoryRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BrokerTransferHistoryServiceTest {

    @InjectMocks
    private BrokerTransferHistoryServiceImpl service;

    @Mock
    private BrokerTransferHistoryRepository repository;

    @Mock
    private BrokerRepository brokerRepository;

    @Test
    public void shouldSaveBrokerTransferHistory() {
        Broker broker = new Broker();
        broker.setId(1L);

        Broker previousBroker = new Broker();
        previousBroker.setId(2L);

        BrokerTransferHistory transferHistory = BrokerTransferHistory
                .builder()
                .broker(broker.getId())
                .previousBroker(previousBroker.getId())
                .supplyType("GAS")
                .contractId(1L)
                .build();

        service.save(transferHistory);

        verify(repository, times(1)).save(any(BrokerTransferHistory.class));
    }

    @Test
    public void shouldReturnTransferMessagesListForGas() {
        Broker broker = new Broker();
        broker.setId(1L);
        broker.setFirstName("Jon");
        broker.setLastName("Smith");

        Broker previousBroker = new Broker();
        previousBroker.setId(2L);
        previousBroker.setFirstName("Jon2");
        previousBroker.setLastName("Smith2");

        GasCustomerContract contract = new GasCustomerContract();
        contract.setId(22L);
        contract.setBroker(broker);

        BrokerTransferHistory brokerTransferHistory = BrokerTransferHistory
                .builder()
                .contractId(contract.getId())
                .broker(broker.getId())
                .previousBroker(previousBroker.getId())
                .build();

        brokerTransferHistory.setCreatedBy(createdBy("Jimbo", "Derek"));
        brokerTransferHistory.setDateCreated(new Date(1609887797));

        List<BrokerTransferHistory> listOfTransfers = Arrays.asList(brokerTransferHistory);

        when(brokerRepository.findOne(any(Long.class))).thenReturn(broker);
        when(repository.findBrokerTransferHistoryOrderByDateCreatedDesc(
                any(Long.class), any(String.class))).thenReturn(listOfTransfers);

        List<String> history = service.findLatestGasBrokerTransferHistory(contract);

        assertEquals(1, history.size());
        assertEquals("Contract transferred from Jon Smith by Jimbo Derek on 19/01/1970 04:11:27 PM", history.get(0));
    }

    private User createdBy(String firstName, String lastName) {
        User user = new User();
        user.setId(99L);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return user;
    }

}
