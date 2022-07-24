package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.LogTypeHistory;
import mycrm.models.Supplier;
import mycrm.models.TerminationTask;
import mycrm.models.WelcomePack;
import mycrm.repositories.ElecContractRepository;
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
public class ElecContractServiceJpaImplTest {

    @InjectMocks
    private ElecContractServiceJpaImpl elecContractServiceJpaImpl;

    @Mock
    private ElecCustomerContract mockedElecCustomerContract;

    @Mock
    private ElecContractRepository mockedElecContractRepository;

    @Mock
    private LogTypeHistoryService mockLogTypeHistoryService;

    @Test
    public void shouldFindAllElecCustomerContract() {
        List<ElecCustomerContract> elecCustomerContractList = new ArrayList<>();
        elecCustomerContractList.add(new ElecCustomerContract());

        when(mockedElecContractRepository.findAll()).thenReturn(elecCustomerContractList);

        List<ElecCustomerContract> findAll = elecContractServiceJpaImpl.findAll();

        assertEquals(elecCustomerContractList, findAll);
    }

    @Test
    public void shouldFindElecCustomerContractById() {
        ElecCustomerContract elecCustomerContract = anElectricityContract();

        when(mockedElecContractRepository.findOne(Long.valueOf(5))).thenReturn(elecCustomerContract);

        ElecCustomerContract findById = elecContractServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(elecCustomerContract, findById);
        assertEquals(elecCustomerContract.getId(), findById.getId());
        assertEquals(elecCustomerContract.getVersion(), findById.getVersion());
        assertEquals(elecCustomerContract.getBroker(), findById.getBroker());
        assertEquals(elecCustomerContract.getAccountNumber(), findById.getAccountNumber());
        assertEquals(elecCustomerContract.getSupplier(), findById.getSupplier());
        assertEquals(elecCustomerContract.getCustomer(), findById.getCustomer());
        assertEquals(elecCustomerContract.getCustomerSite(), findById.getCustomerSite());
        assertEquals(elecCustomerContract.getUnitRate(), findById.getUnitRate(), 1);
        assertEquals(elecCustomerContract.getStandingCharge(), findById.getStandingCharge(), 1);
        assertEquals(elecCustomerContract.getStartDate(), findById.getStartDate());
        assertEquals(elecCustomerContract.getEndDate(), findById.getEndDate());
        assertEquals(elecCustomerContract.getBrokerUplift(), findById.getBrokerUplift(), 1);
        assertEquals(elecCustomerContract.getEstimatedAnnualQuantity(), findById.getEstimatedAnnualQuantity());
        assertEquals(elecCustomerContract.getCommissionDuration(), findById.getCommissionDuration());
        assertEquals(elecCustomerContract.getLogType(), findById.getLogType());
        assertEquals(elecCustomerContract.getUpfrontPercentage(), findById.getUpfrontPercentage(), 1);
        assertEquals(elecCustomerContract.getNotes(), findById.getNotes());
        assertEquals(elecCustomerContract.isWelcomePackSentToCustomer(), findById.isWelcomePackSentToCustomer());
        assertEquals(elecCustomerContract.isPreviousSupplyTerminated(), findById.isPreviousSupplyTerminated());
        assertEquals(elecCustomerContract.isContractSentToCustomer(), findById.isContractSentToCustomer());
        assertEquals(elecCustomerContract.isCurrentSupplyTerminated(), findById.isCurrentSupplyTerminated());
        assertEquals(elecCustomerContract.getCallbackDateTime(), findById.getCallbackDateTime());
        assertEquals(elecCustomerContract.getContractMonthlyDuration(), findById.getContractMonthlyDuration());
        assertEquals(elecCustomerContract.getDayRate(), findById.getDayRate(), 1);
        assertEquals(elecCustomerContract.getNightRate(), findById.getNightRate(), 1);
        assertEquals(elecCustomerContract.getEveningWeekendRate(), findById.getEveningWeekendRate(), 1);
    }

    @Test
    public void shouldSaveElecCustomerContract() {
        ElecCustomerContract elecCustomerContract = anElectricityContract();

        when(mockedElecContractRepository.save(any(ElecCustomerContract.class))).thenReturn(elecCustomerContract);

        ElecCustomerContract savedElecCustomerContract = elecContractServiceJpaImpl.save(elecCustomerContract);

        assertEquals(elecCustomerContract, savedElecCustomerContract);

        verify(mockLogTypeHistoryService, times(1)).save(any(LogTypeHistory.class));
    }

    @Test
    public void shouldEditAndSaveElecCustomerContract() {
        ElecCustomerContract elecCustomerContract = anElectricityContract();


        when(mockedElecContractRepository.save(any(ElecCustomerContract.class))).thenReturn(elecCustomerContract);

        ElecCustomerContract savedElecCustomerContract = elecContractServiceJpaImpl.save(elecCustomerContract);

        assertEquals(elecCustomerContract, savedElecCustomerContract);

        verify(mockLogTypeHistoryService, times(1)).save(any(LogTypeHistory.class));
    }

    @Test
    public void shouldDeleteElecCustomerContract() {
        elecContractServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedElecContractRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldFindAllElecCustomerContractByBroker() {
        List<ElecCustomerContract> elecCustomerContractList = new ArrayList<>();
        elecCustomerContractList.add(new ElecCustomerContract());

        Broker broker = new Broker();

        when(mockedElecContractRepository.findByBroker(broker)).thenReturn(elecCustomerContractList);

        List<ElecCustomerContract> findByBroker = elecContractServiceJpaImpl.findByBroker(broker);

        assertEquals(elecCustomerContractList, findByBroker);
    }

    @Test
    public void shouldFindAllElecCustomerContractByCustomerSite() {
        List<ElecCustomerContract> elecCustomerContractList = new ArrayList<>();
        elecCustomerContractList.add(new ElecCustomerContract());

        CustomerSite customerSite = new CustomerSite();

        when(mockedElecContractRepository.findByCustomerSite(customerSite)).thenReturn(elecCustomerContractList);

        List<ElecCustomerContract> findByCustomerSite = elecContractServiceJpaImpl.findByCustomerSite(customerSite);

        assertEquals(elecCustomerContractList, findByCustomerSite);
    }

//    @Test
//    public void shouldFindAllElecCustomerContractByLatestElecCustomerContract() {
//        List<ElecCustomerContract> elecCustomerContractList = new ArrayList<>();
//        elecCustomerContractList.add(new ElecCustomerContract());
//
//        when(mockedElecContractRepository.findLatestElecCustomerContractByCustomer(any(Long.class))).thenReturn(elecCustomerContractList);
//
//        List<ElecCustomerContract> findLatestElecCustomerContract = elecContractServiceJpaImpl
//                .findLatestElecCustomerContractByCustomer(1l);
//
//        assertEquals(elecCustomerContractList, findLatestElecCustomerContract);
//    }

    @Test
    public void shouldUpdateWelcomePack() {
        WelcomePack welcomePack = new WelcomePack();
        welcomePack.setId(Long.valueOf(2));
        welcomePack.setWelcomePackSentToCustomer(true);
        welcomePack.setPreviousSupplyTerminated(true);

        elecContractServiceJpaImpl.updateWelcomePack(welcomePack);

        verify(mockedElecContractRepository, times(1)).updateWelcomePack(welcomePack.isWelcomePackSentToCustomer(),
                welcomePack.isPreviousSupplyTerminated(), welcomePack.getId());
    }

    @Test
    public void shouldUpdateTerminationTask() {
        TerminationTask terminationTask = new TerminationTask();
        terminationTask.setId(Long.valueOf(12));
        terminationTask.setCurrentSupplyTerminated(true);

        elecContractServiceJpaImpl.updateTerminationTask(terminationTask);

        verify(mockedElecContractRepository, times(1))
                .updateTerminationTask(terminationTask.isCurrentSupplyTerminated(), terminationTask.getId());
    }


    private ElecCustomerContract anElectricityContract() {
        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setId(Long.valueOf(5));
        elecCustomerContract.setVersion(1);
        elecCustomerContract.setBroker(aBroker());
        elecCustomerContract.setAccountNumber("123546");
        elecCustomerContract.setSupplier(aSupplier());
        elecCustomerContract.setCustomer(aCustomer());
        elecCustomerContract.setCustomerSite(aCustomerSite());
        elecCustomerContract.setUnitRate(1);
        elecCustomerContract.setStandingCharge(2);
        elecCustomerContract.setStartDate(aRandomDate());
        elecCustomerContract.setEndDate(aRandomDate());
        elecCustomerContract.setBrokerUplift(24);
        elecCustomerContract.setEstimatedAnnualQuantity(45120l);
        elecCustomerContract.setCommissionDuration(4l);
        elecCustomerContract.setLogType("POTENTIAL");
        elecCustomerContract.setUpfrontPercentage(25);
        elecCustomerContract.setNotes("notes");
        elecCustomerContract.setWelcomePackSentToCustomer(true);
        elecCustomerContract.setPreviousSupplyTerminated(true);
        elecCustomerContract.setContractSentToCustomer(true);
        elecCustomerContract.setCurrentSupplyTerminated(true);
        elecCustomerContract.setCallbackDateTime(callbackDateTime());
        elecCustomerContract.setContractMonthlyDuration(12);
        elecCustomerContract.setDayRate(1);
        elecCustomerContract.setNightRate(1);
        elecCustomerContract.setEveningWeekendRate(1);
        return elecCustomerContract;
    }

    @Test
    public void shouldFindByBrokerAndLogType() {
        Broker broker = new Broker();
        broker.setId(22L);

        ElecCustomerContract contract = new ElecCustomerContract();
        contract.setId(33L);
        contract.setBroker(broker);
        contract.setLogType("LIVE");

        List<ElecCustomerContract> list = new ArrayList<>();
        list.add(contract);

        when(mockedElecContractRepository.findByBrokerAndLogType(any(Broker.class), anyObject())).thenReturn(list);

        List<ElecCustomerContract> byBrokerAndLogType = elecContractServiceJpaImpl.findByBrokerAndLogType(broker, LogType.LIVE);

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
