package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.models.Broker;
import mycrm.models.BrokerContractPackage;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.UtilityContract;
import mycrm.repositories.BrokerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BrokerServiceJpaImplTest {

    @InjectMocks
    private BrokerServiceJpaImpl service;

    @Mock
    private BrokerRepository mockedBrokerRepository;

    @Mock
    private GasContractService gasContractService;

    @Mock
    private ElecContractService electricContractService;

    @Mock
    private UtilityContractService utilityContractService;

    @Mock
    private MerchantServicesService merchantServicesService;

    @Test
    public void shouldFindAllBrokers() {
        List<Broker> brokersList = new ArrayList<>();
        brokersList.add(new Broker());

        when(mockedBrokerRepository.findAll()).thenReturn(brokersList);

        List<Broker> findAll = service.findAll();

        assertEquals(brokersList, findAll);
    }

    @Test
    public void shouldFindLatestFiveBrokers() {
        List<Broker> findLatest5 = service.findLatest5();

        assertNull(findLatest5);
    }

    @Test
    public void shouldFindBrokerById() {
        Broker broker = aBroker();

        when(mockedBrokerRepository.findOne(Long.valueOf(5))).thenReturn(broker);

        Broker findById = service.findById(5L);

        assertEquals(broker, findById);
        assertEquals(broker.getId(), findById.getId());
        assertEquals(broker.getVersion(), findById.getVersion());
        assertEquals(broker.getBrokerReference(), findById.getBrokerReference());
        assertEquals(broker.getBusinessName(), findById.getBusinessName());
        assertEquals(broker.getBusinessAddr(), findById.getBusinessAddr());
        assertEquals(broker.getBusinessAddr1(), findById.getBusinessAddr1());
        assertEquals(broker.getBusinessCity(), findById.getBusinessCity());
        assertEquals(broker.getBusinessPostcodeIn(), findById.getBusinessPostcodeIn());
        assertEquals(broker.getBusinessPostcodeOut(), findById.getBusinessPostcodeOut());
        assertEquals(broker.getFirstName(), findById.getFirstName());
        assertEquals(broker.getLastName(), findById.getLastName());
        assertEquals(broker.getContactNumber(), findById.getContactNumber());
        assertEquals(broker.getMobileNumber(), findById.getMobileNumber());
        assertEquals(broker.getEmailAddress(), findById.getEmailAddress());
        assertEquals(broker.getDob(), findById.getDob());
        assertEquals(broker.getElecCustomerContracts(), findById.getElecCustomerContracts());
        assertEquals(broker.getGasCustomerContracts(), findById.getGasCustomerContracts());
        assertEquals(broker.getSoleTrader(), findById.getSoleTrader());
        assertEquals(broker.getLtdRegNo(), findById.getLtdRegNo());
        assertEquals(broker.getStAddr(), findById.getStAddr());
        assertEquals(broker.getStAddr1(), findById.getStAddr1());
        assertEquals(broker.getStCity(), findById.getStCity());
        assertEquals(broker.getStPostcodeIn(), findById.getStPostcodeIn());
        assertEquals(broker.getStPostcodeOut(), findById.getStPostcodeOut());
        assertEquals(broker.getWebAddress(), findById.getWebAddress());
        assertEquals(broker.getConnectedUser(), findById.getConnectedUser());
        assertEquals(broker.toString(), findById.toString());
    }

    private Broker aBroker() {
        Broker broker = new Broker();
        broker.setId(5L);
        broker.setVersion(1);
        broker.setBrokerReference("reference");
        broker.setBusinessName("businessname");
        broker.setBusinessAddr("addr");
        broker.setBusinessAddr1("addr1");
        broker.setBusinessCity("city");
        broker.setBusinessPostcodeIn("IN1");
        broker.setBusinessPostcodeOut("OUT1");
        broker.setFirstName("firstname");
        broker.setLastName("lastname");
        broker.setContactNumber("012374");
        broker.setMobileNumber("07548");
        broker.setEmailAddress("email@email.com");
        broker.setDob("1984-01-01");
        broker.setElecCustomerContracts(new HashSet<>());
        broker.setGasCustomerContracts(new HashSet<>());
        broker.setLtdRegNo("3214");
        broker.setSoleTrader(true);
        broker.setStAddr("addr");
        broker.setStAddr1("addr1");
        broker.setStCity("city");
        broker.setStPostcodeIn("IN1");
        broker.setStPostcodeOut("OUT1");
        broker.setWebAddress("web.web.com");
        broker.setConnectedUser(new HashSet<>());

        return broker;
    }

    @Test
    public void shouldSaveBroker() {
        Broker broker = aBroker();

        when(mockedBrokerRepository.save(any(Broker.class))).thenReturn(broker);

        Broker savedBroker = service.save(broker);

        assertEquals(broker, savedBroker);
    }

    @Test
    public void shouldEditAndSaveBroker() {
        Broker broker = aBroker();

        when(mockedBrokerRepository.save(any(Broker.class))).thenReturn(broker);

        Broker savedBroker = service.edit(broker);

        assertEquals(broker, savedBroker);
    }

    @Test
    public void shouldDeleteBroker() {
        service.deleteById(541L);

        verify(mockedBrokerRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldFindCustomersByBroker() {
        Broker broker = aBroker();
        broker.setId(33L);

        List<GasCustomerContract> list = new ArrayList<>();

        when(gasContractService.findByBroker(any(Broker.class))).thenReturn(list);

        List<Customer> customersByBroker = service.findCustomersByBroker(broker);

        assertEquals(0, customersByBroker.size());
    }

    @Test
    public void shouldFindBrokerContractsForEachLogTypeEightForEachSet() {
        Broker broker = aBroker();
        broker.setId(33L);

        when(gasContractService.findByBrokerAndLogType(any(Broker.class), anyObject())).thenReturn(new ArrayList<>());
        when(electricContractService.findByBrokerAndLogType(any(Broker.class), anyObject())).thenReturn(new ArrayList<>());
        when(utilityContractService.findByBrokerAndLogTypeAndUtilityType(any(Broker.class), anyObject(), anyObject())).thenReturn(new ArrayList<>());

        BrokerContractPackage contracts = service.findBrokerContracts(broker);

        assertEquals(15, contracts.getGasContracts().size());
        assertEquals(15, contracts.getElectricityContracts().size());
        assertEquals(15, contracts.getSolarContracts().size());
        assertEquals(15, contracts.getWaterContracts().size());
        assertEquals(15, contracts.getVoipContracts().size());
        assertEquals(15, contracts.getLandlineContracts().size());
        assertEquals(15, contracts.getMobileContracts().size());
        assertEquals(15, contracts.getBroadbandContracts().size());
    }

    @Test
    public void shouldFindBrokerContractStatsForEachLogTypeEightForEachSet() {
        Broker broker = aBroker();
        broker.setId(33L);

        when(gasContractService.countByBrokerAndLogType(any(Broker.class), anyObject())).thenReturn(0);
        when(electricContractService.countByBrokerAndLogType(any(Broker.class), anyObject())).thenReturn(0);
        when(utilityContractService.countByBrokerAndLogTypeAndUtilityType(any(Broker.class), anyObject(), anyObject())).thenReturn(0);

        BrokerContractPackage contracts = service.findBrokerContractStats(broker);

        assertEquals(15, contracts.getGasContracts().size());
        assertEquals(15, contracts.getElectricityContracts().size());
        assertEquals(15, contracts.getSolarContracts().size());
        assertEquals(15, contracts.getWaterContracts().size());
        assertEquals(15, contracts.getVoipContracts().size());
        assertEquals(15, contracts.getLandlineContracts().size());
        assertEquals(15, contracts.getMobileContracts().size());
        assertEquals(15, contracts.getBroadbandContracts().size());
    }

    @Test
    public void shouldTransferGasBrokerContracts() {
        Broker broker = new Broker();
        broker.setId(55L);

        Broker previousBroker = new Broker();
        previousBroker.setId(66L);

        BrokerTransferUpdate brokerTransferUpdate = new BrokerTransferUpdate();
        brokerTransferUpdate.setPreviousBroker(previousBroker);
        brokerTransferUpdate.setNewBroker(broker);
        brokerTransferUpdate.setLogType(LogType.LIVE);
        brokerTransferUpdate.setSupplyType("GAS");

        List<GasCustomerContract> contracts = new ArrayList<>();

        when(gasContractService.findByBrokerAndLogType(anyObject(), anyObject())).thenReturn(contracts);

        boolean successResult = service.transferBrokerContracts(brokerTransferUpdate);

        verify(gasContractService, times(1)).transferBrokerContracts(anyObject(), anyObject());

        assertTrue(successResult);
    }

    @Test
    public void shouldNotTransferGasBrokerContracts() {
        Broker broker = new Broker();
        broker.setId(55L);

        Broker previousBroker = new Broker();
        previousBroker.setId(66L);

        BrokerTransferUpdate brokerTransferUpdate = new BrokerTransferUpdate();
        brokerTransferUpdate.setPreviousBroker(previousBroker);
        brokerTransferUpdate.setNewBroker(broker);
        brokerTransferUpdate.setLogType(LogType.LIVE);
        brokerTransferUpdate.setSupplyType("GAS");

        when(gasContractService.findByBrokerAndLogType(anyObject(), anyObject())).thenThrow(Exception.class);

        boolean successResult = service.transferBrokerContracts(brokerTransferUpdate);

        verify(gasContractService, times(0)).transferBrokerContracts(anyObject(), anyObject());

        assertFalse(successResult);
    }

    @Test
    public void shouldTransferElectricBrokerContracts() {
        Broker broker = new Broker();
        broker.setId(55L);

        Broker previousBroker = new Broker();
        previousBroker.setId(66L);

        BrokerTransferUpdate brokerTransferUpdate = new BrokerTransferUpdate();
        brokerTransferUpdate.setPreviousBroker(previousBroker);
        brokerTransferUpdate.setNewBroker(broker);
        brokerTransferUpdate.setLogType(LogType.LIVE);
        brokerTransferUpdate.setSupplyType("ELEC");

        List<ElecCustomerContract> contracts = new ArrayList<>();

        when(electricContractService.findByBrokerAndLogType(anyObject(), anyObject())).thenReturn(contracts);

        boolean successResult = service.transferBrokerContracts(brokerTransferUpdate);

        verify(electricContractService, times(1)).transferBrokerContracts(anyObject(), anyObject());

        assertTrue(successResult);
    }

    @Test
    public void shouldNotTransferElectricBrokerContracts() {
        Broker broker = new Broker();
        broker.setId(55L);

        Broker previousBroker = new Broker();
        previousBroker.setId(66L);

        BrokerTransferUpdate brokerTransferUpdate = new BrokerTransferUpdate();
        brokerTransferUpdate.setPreviousBroker(previousBroker);
        brokerTransferUpdate.setNewBroker(broker);
        brokerTransferUpdate.setLogType(LogType.LIVE);
        brokerTransferUpdate.setSupplyType("ELEC");

        when(electricContractService.findByBrokerAndLogType(anyObject(), anyObject())).thenThrow(Exception.class);

        boolean successResult = service.transferBrokerContracts(brokerTransferUpdate);

        verify(electricContractService, times(0)).transferBrokerContracts(anyObject(), anyObject());

        assertFalse(successResult);
    }

    @Test
    public void shouldTransferUtilityBrokerContracts() {
        Broker broker = new Broker();
        broker.setId(55L);

        Broker previousBroker = new Broker();
        previousBroker.setId(66L);

        BrokerTransferUpdate brokerTransferUpdate = new BrokerTransferUpdate();
        brokerTransferUpdate.setPreviousBroker(previousBroker);
        brokerTransferUpdate.setNewBroker(broker);
        brokerTransferUpdate.setLogType(LogType.LIVE);
        brokerTransferUpdate.setSupplyType("SOLAR");

        List<UtilityContract> contracts = new ArrayList<>();

        when(utilityContractService.findByBrokerAndLogTypeAndUtilityType(anyObject(), anyObject(), anyObject())).thenReturn(contracts);

        boolean successResult = service.transferBrokerContracts(brokerTransferUpdate);

        verify(utilityContractService, times(1)).transferBrokerContracts(anyObject(), anyObject());

        assertTrue(successResult);
    }

    @Test
    public void shouldNotTransferUtilityBrokerContracts() {
        Broker broker = new Broker();
        broker.setId(55L);

        Broker previousBroker = new Broker();
        previousBroker.setId(66L);

        BrokerTransferUpdate brokerTransferUpdate = new BrokerTransferUpdate();
        brokerTransferUpdate.setPreviousBroker(previousBroker);
        brokerTransferUpdate.setNewBroker(broker);
        brokerTransferUpdate.setLogType(LogType.LIVE);
        brokerTransferUpdate.setSupplyType("SOLAR");

        when(utilityContractService.findByBrokerAndLogTypeAndUtilityType(anyObject(), anyObject(), anyObject())).thenThrow(Exception.class);

        boolean successResult = service.transferBrokerContracts(brokerTransferUpdate);

        verify(electricContractService, times(0)).transferBrokerContracts(anyObject(), anyObject());

        assertFalse(successResult);
    }

    @Test
    public void shouldNotTransferUtilityBrokerContractsWhenInvalidUtilityTyupe() {
        Broker broker = new Broker();
        broker.setId(55L);

        Broker previousBroker = new Broker();
        previousBroker.setId(66L);

        BrokerTransferUpdate brokerTransferUpdate = new BrokerTransferUpdate();
        brokerTransferUpdate.setPreviousBroker(previousBroker);
        brokerTransferUpdate.setNewBroker(broker);
        brokerTransferUpdate.setLogType(LogType.LIVE);
        brokerTransferUpdate.setSupplyType("DISCO");

        when(utilityContractService.findByBrokerAndLogTypeAndUtilityType(anyObject(), anyObject(), anyObject())).thenThrow(Exception.class);

        boolean successResult = service.transferBrokerContracts(brokerTransferUpdate);

        // verify(electricContractService, times(0)).transferBrokerContracts(anyObject(), anyObject());

        assertFalse(successResult);
    }
}
