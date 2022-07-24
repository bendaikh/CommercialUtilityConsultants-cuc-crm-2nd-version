package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.models.Broker;
import mycrm.models.BrokerTransferHistory;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.GasCustomerContract;
import mycrm.models.LogTypeHistory;
import mycrm.models.Supplier;
import mycrm.models.TerminationTask;
import mycrm.models.WelcomePack;
import mycrm.repositories.GasContractRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GasContractServiceJpaImplTest {

    @InjectMocks
    private GasContractServiceJpaImpl gasContractServiceJpaImpl;

    @Mock
    private GasCustomerContract mockedGasCustomerContract;

    @Mock
    private GasContractRepository mockedGasContractRepository;

    @Mock
    private LogTypeHistoryService mockLogTypeHistoryService;

    @Mock
    private BrokerTransferHistoryService brokerTransferHistoryService;

    @Test
    public void shouldFindAllGasCustomerContract() {
        List<GasCustomerContract> gasCustomerContractList = new ArrayList<>();
        gasCustomerContractList.add(new GasCustomerContract());

        when(mockedGasContractRepository.findAll()).thenReturn(gasCustomerContractList);

        List<GasCustomerContract> findAll = gasContractServiceJpaImpl.findAll();

        assertEquals(findAll, gasCustomerContractList);
    }

    @Test
    public void shouldFindGasCustomerContractById() {
        GasCustomerContract gasCustomerContract = aGasContract();

        when(mockedGasContractRepository.findOne(Long.valueOf(5))).thenReturn(gasCustomerContract);

        GasCustomerContract findById = gasContractServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(findById, gasCustomerContract);
        assertEquals(gasCustomerContract.getId(), findById.getId());
        assertEquals(gasCustomerContract.getVersion(), findById.getVersion());
        assertEquals(gasCustomerContract.getBroker(), findById.getBroker());
        assertEquals(gasCustomerContract.getAccountNumber(), findById.getAccountNumber());
        assertEquals(gasCustomerContract.getSupplier(), findById.getSupplier());
        assertEquals(gasCustomerContract.getCustomer(), findById.getCustomer());
        assertEquals(gasCustomerContract.getCustomerSite(), findById.getCustomerSite());
        assertEquals(gasCustomerContract.getUnitRate(), findById.getUnitRate(), 1);
        assertEquals(gasCustomerContract.getStandingCharge(), findById.getStandingCharge(), 1);
        assertEquals(gasCustomerContract.getStartDate(), findById.getStartDate());
        assertEquals(gasCustomerContract.getEndDate(), findById.getEndDate());
        assertEquals(gasCustomerContract.getBrokerUplift(), findById.getBrokerUplift(), 1);
        assertEquals(gasCustomerContract.getEstimatedAnnualQuantity(), findById.getEstimatedAnnualQuantity());
        assertEquals(gasCustomerContract.getCommissionDuration(), findById.getCommissionDuration());
        assertEquals(gasCustomerContract.getLogType(), findById.getLogType());
        assertEquals(gasCustomerContract.getUpfrontPercentage(), findById.getUpfrontPercentage(), 1);
        assertEquals(gasCustomerContract.getNotes(), findById.getNotes());
        assertEquals(gasCustomerContract.isWelcomePackSentToCustomer(), findById.isWelcomePackSentToCustomer());
        assertEquals(gasCustomerContract.isPreviousSupplyTerminated(), findById.isPreviousSupplyTerminated());
        assertEquals(gasCustomerContract.isContractSentToCustomer(), findById.isContractSentToCustomer());
        assertEquals(gasCustomerContract.isCurrentSupplyTerminated(), findById.isCurrentSupplyTerminated());
        assertEquals(gasCustomerContract.getCallbackDateTime(), findById.getCallbackDateTime());
        assertEquals(gasCustomerContract.getContractMonthlyDuration(), findById.getContractMonthlyDuration());
    }

    @Test
    public void shouldSaveGasCustomerContract() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(Long.valueOf(5));
        gasCustomerContract.setLogType("POTENTIAL");

        when(mockedGasContractRepository.save(any(GasCustomerContract.class))).thenReturn(gasCustomerContract);

        GasCustomerContract savedGasCustomerContract = gasContractServiceJpaImpl.save(gasCustomerContract);

        assertEquals(savedGasCustomerContract, gasCustomerContract);

        verify(mockLogTypeHistoryService, times(1)).save(any(LogTypeHistory.class));
    }

    @Test
    public void shouldEditAndSaveGasCustomerContract() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(Long.valueOf(5));
        gasCustomerContract.setLogType("POTENTIAL");

        when(mockedGasContractRepository.save(any(GasCustomerContract.class))).thenReturn(gasCustomerContract);

        GasCustomerContract savedGasCustomerContract = gasContractServiceJpaImpl.save(gasCustomerContract);

        assertEquals(savedGasCustomerContract, gasCustomerContract);
        verify(mockLogTypeHistoryService, times(1)).save(any(LogTypeHistory.class));
    }

    @Test
    public void shouldDeleteGasCustomerContract() {
        gasContractServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedGasContractRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldFindAllGasCustomerContractByBroker() {
        List<GasCustomerContract> gasCustomerContractList = new ArrayList<>();
        gasCustomerContractList.add(new GasCustomerContract());

        Broker broker = new Broker();

        when(mockedGasContractRepository.findByBroker(broker)).thenReturn(gasCustomerContractList);

        List<GasCustomerContract> findByBroker = gasContractServiceJpaImpl.findByBroker(broker);

        assertEquals(findByBroker, gasCustomerContractList);
    }

    @Test
    public void shouldFindAllGasCustomerContractByCustomerSite() {
        List<GasCustomerContract> gasCustomerContractList = new ArrayList<>();
        gasCustomerContractList.add(new GasCustomerContract());

        CustomerSite customerSite = new CustomerSite();

        when(mockedGasContractRepository.findByCustomerSite(customerSite)).thenReturn(gasCustomerContractList);

        List<GasCustomerContract> findByCustomerSite = gasContractServiceJpaImpl.findByCustomerSite(customerSite);

        assertEquals(findByCustomerSite, gasCustomerContractList);
    }

//    @Test
//    public void shouldFindAllGasCustomerContractByLatestGasCustomerContract() {
//        List<GasCustomerContract> gasCustomerContractList = new ArrayList<>();
//        gasCustomerContractList.add(new GasCustomerContract());
//
//        when(mockedGasContractRepository.findLatestGasCustomerContractByCustomer(any(Long.class))).thenReturn(gasCustomerContractList);
//
//        List<GasCustomerContract> findLatestGasCustomerContract = gasContractServiceJpaImpl
//                .findLatestGasCustomerContractByCustomer(1l);
//
//        assertEquals(findLatestGasCustomerContract, gasCustomerContractList);
//    }

    @Test
    public void shouldUpdateWelcomePack() {
        WelcomePack welcomePack = new WelcomePack();
        welcomePack.setId(Long.valueOf(2));
        welcomePack.setWelcomePackSentToCustomer(true);
        welcomePack.setPreviousSupplyTerminated(true);

        gasContractServiceJpaImpl.updateWelcomePack(welcomePack);

        verify(mockedGasContractRepository, times(1)).updateWelcomePack(welcomePack.isWelcomePackSentToCustomer(),
                welcomePack.isPreviousSupplyTerminated(), welcomePack.getId());
    }

    @Test
    public void shouldUpdateTerminationTask() {
        TerminationTask terminationTask = new TerminationTask();
        terminationTask.setId(Long.valueOf(12));
        terminationTask.setCurrentSupplyTerminated(true);

        gasContractServiceJpaImpl.updateTerminationTask(terminationTask);

        verify(mockedGasContractRepository, times(1)).updateTerminationTask(terminationTask.isCurrentSupplyTerminated(),
                terminationTask.getId());
    }

    private GasCustomerContract aGasContract() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(Long.valueOf(5));
        gasCustomerContract.setVersion(1);
        gasCustomerContract.setBroker(aBroker());
        gasCustomerContract.setAccountNumber("123546");
        gasCustomerContract.setSupplier(aSupplier());
        gasCustomerContract.setCustomer(aCustomer());
        gasCustomerContract.setCustomerSite(aCustomerSite());
        gasCustomerContract.setUnitRate(1);
        gasCustomerContract.setStandingCharge(2);
        gasCustomerContract.setStartDate(aRandomDate());
        gasCustomerContract.setEndDate(aRandomDate());
        gasCustomerContract.setBrokerUplift(24);
        gasCustomerContract.setEstimatedAnnualQuantity(45120l);
        gasCustomerContract.setCommissionDuration(4l);
        gasCustomerContract.setLogType("POTENTIAL");
        gasCustomerContract.setUpfrontPercentage(25);
        gasCustomerContract.setNotes("notes");
        gasCustomerContract.setWelcomePackSentToCustomer(true);
        gasCustomerContract.setPreviousSupplyTerminated(true);
        gasCustomerContract.setContractSentToCustomer(true);
        gasCustomerContract.setCurrentSupplyTerminated(true);
        gasCustomerContract.setCallbackDateTime(callbackDateTime());
        gasCustomerContract.setContractMonthlyDuration(12);
        return gasCustomerContract;
    }

    @Test
    public void shouldTransferBrokerContracts() {
        GasCustomerContract contract = new GasCustomerContract();
        contract.setId(2L);

        List<GasCustomerContract> gasContracts = new ArrayList<>();
        gasContracts.add(contract);

        Broker newBroker = new Broker();
        newBroker.setId(12L);

        BrokerTransferUpdate brokerTransferUpdate = new BrokerTransferUpdate();
        brokerTransferUpdate.setNewBroker(newBroker);
        brokerTransferUpdate.setPreviousBroker(aBroker());

        gasContractServiceJpaImpl.transferBrokerContracts(gasContracts, brokerTransferUpdate);

        verify(mockedGasContractRepository, times(1)).save(any(GasCustomerContract.class));
        verify(brokerTransferHistoryService, times(1)).save(any(BrokerTransferHistory.class));
    }

    @Test
    public void shouldFindByBrokerAndLogType() {
        Broker broker = new Broker();
        broker.setId(22L);

        GasCustomerContract contract = new GasCustomerContract();
        contract.setId(33L);
        contract.setBroker(broker);
        contract.setLogType("LIVE");

        List<GasCustomerContract> list = new ArrayList<>();
        list.add(contract);

        when(mockedGasContractRepository.findByBrokerAndLogType(any(Broker.class), anyObject())).thenReturn(list);

        List<GasCustomerContract> byBrokerAndLogType = gasContractServiceJpaImpl.findByBrokerAndLogType(broker, LogType.LIVE);

        assertEquals(list, byBrokerAndLogType);
    }

    private Date callbackDateTime() {
        return new Date();
    }

    private Date aRandomDate() {
        return new Date();
    }

    private CustomerSite aCustomerSite() {
        return new CustomerSite();
    }

    private Customer aCustomer() {
        return new Customer();
    }

    private Supplier aSupplier() {
        return new Supplier();
    }

    private Broker aBroker() {
        return new Broker();
    }

}
