package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.configuration.UtilityType;
import mycrm.models.Broker;
import mycrm.models.BrokerTransferHistory;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.LogTypeHistory;
import mycrm.models.ProcessingPack;
import mycrm.models.TerminationTask;
import mycrm.models.UtilityContract;
import mycrm.models.WelcomePack;
import mycrm.repositories.UtilityContractRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Primary
public class UtilityContractServiceImpl implements UtilityContractService {

    private static final Logger logger = LogManager.getLogger();

    private final UtilityContractRepository utilityContractRepository;
    private final LogTypeHistoryService logTypeHistoryService;
    private final BrokerTransferHistoryService brokerTransferHistoryService;

    @Autowired
    public UtilityContractServiceImpl(UtilityContractRepository utilityContractRepository,
                                      LogTypeHistoryService logTypeHistoryService,
                                      BrokerTransferHistoryService brokerTransferHistoryService) {
        this.utilityContractRepository = utilityContractRepository;
        this.logTypeHistoryService = logTypeHistoryService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
    }

    @Override
    public UtilityContract save(UtilityContract utilityContract) {
        String logType = utilityContract.getLogType();
        String previousLogType = null;

        if (utilityContract.getId() != null && findById(utilityContract.getId()) != null) {
            previousLogType = findById(utilityContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);

        UtilityContract savedCustomerContract = utilityContractRepository.save(utilityContract);

        if (logTypeChanged) {
            saveLogTypeHistory(LogTypeHistory
                    .builder()
                    .contractId(savedCustomerContract.getId())
                    .supplyType(savedCustomerContract.getUtilityType())
                    .previousLogType(previousLogType)
                    .logType(logType)
                    .build());
        }

        return savedCustomerContract;
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }

    @Override
    public List<UtilityContract> findByCustomerSiteAndUtilityType(CustomerSite customerSite, UtilityType utilityType) {
        return this.utilityContractRepository.findLatestUtilityContractByCustomerSiteAndUtilityType(customerSite.getId(), utilityType.toString());
    }

    @Override
    public List<UtilityContract> findByCustomerSite(CustomerSite customerSite) {
        return this.utilityContractRepository.findLatestUtilityContractByCustomerSite(customerSite.getId());
    }

    @Override
    public List<UtilityContract> findSolarContractByCustomerSite(CustomerSite customerSite) {
        List<UtilityContract> contracts = this.findByCustomerSiteAndUtilityType(customerSite, UtilityType.SOLAR);
        contracts.sort(Comparator.comparing(UtilityContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<UtilityContract> findWaterContractByCustomerSite(CustomerSite customerSite) {
        List<UtilityContract> contracts = this.findByCustomerSiteAndUtilityType(customerSite, UtilityType.WATER);
        contracts.sort(Comparator.comparing(UtilityContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<UtilityContract> findVoipContractByCustomerSite(CustomerSite customerSite) {
        List<UtilityContract> contracts = this.findByCustomerSiteAndUtilityType(customerSite, UtilityType.VOIP);
        contracts.sort(Comparator.comparing(UtilityContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<UtilityContract> findLandlineContractByCustomerSite(CustomerSite customerSite) {
        List<UtilityContract> contracts = this.findByCustomerSiteAndUtilityType(customerSite, UtilityType.LANDLINE);
        contracts.sort(Comparator.comparing(UtilityContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<UtilityContract> findMobileContractByCustomerSite(CustomerSite customerSite) {
        List<UtilityContract> contracts = this.findByCustomerSiteAndUtilityType(customerSite, UtilityType.MOBILE);
        contracts.sort(Comparator.comparing(UtilityContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<UtilityContract> findBroadbandContractByCustomerSite(CustomerSite customerSite) {
        List<UtilityContract> contracts = this.findByCustomerSiteAndUtilityType(customerSite, UtilityType.BROADBAND);
        contracts.sort(Comparator.comparing(UtilityContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public UtilityContract findById(Long id) {
        return this.utilityContractRepository.findOne(id);
    }

    @Override
    public List<UtilityContract> findByCustomerOrderByEndDateDesc(Customer customer) {
        return this.utilityContractRepository.findByCustomerOrderByEndDateDesc(customer);
    }

    @Override
    public List<UtilityContract> findAllAdminUtilityContractNewSalesTasks() {
        return this.utilityContractRepository.findAllAdminUtilityContractNewSalesTasks();
    }

    @Override
    public void updateWelcomePack(WelcomePack welcomePack) {
        this.utilityContractRepository.updateWelcomePack(welcomePack.isWelcomePackSentToCustomer(),
                welcomePack.isPreviousSupplyTerminated(), welcomePack.getId());
    }

    @Override
    public List<UtilityContract> findAllUtilityContractsToTerminate() {
        return this.utilityContractRepository.findAllUtilityContractsToTerminate();
    }

    @Override
    public void updateTerminationTask(TerminationTask terminationTask) {
        this.utilityContractRepository.updateTerminationTask(terminationTask.isCurrentSupplyTerminated(),
                terminationTask.getId());
    }

    @Override
    public List<UtilityContract> findAllAdminContractProcessingTasks() {
        return this.utilityContractRepository.findAllAdminContractProcessingTasks();
    }

    @Override
    public void updateProcessingPack(ProcessingPack processingPack) {
        if (processingPack.isContractReceived() && processingPack.isContractSentToCustomer()) {
            // mark as sold
            this.utilityContractRepository.updateProcessingPackAsSold(processingPack.isContractSentToCustomer(), processingPack.isContractReceived(), processingPack.getId());
        } else {
            this.utilityContractRepository.updateProcessingPack(processingPack.isContractSentToCustomer(), processingPack.isContractReceived(), processingPack.getId());
        }
    }

    @Override
    public List<UtilityContract> findAllObjectedUtilityContracts() {
        return this.utilityContractRepository.findAllObjectedUtilityContracts();
    }

    @Override
    public List<UtilityContract> findByCustomer(Customer customer) {
        return this.utilityContractRepository.findByCustomer(customer);
    }

    @Override
    public void deleteByCustomerSite(CustomerSite customerSite) {
        this.utilityContractRepository.deleteByCustomerSite(customerSite);
    }

    @Override
    public List<UtilityContract> findByBrokerAndLogTypeAndUtilityType(Broker broker, LogType logType, UtilityType utilityType) {
        return this.utilityContractRepository.findByBrokerAndLogType(broker, logType.toString(), utilityType.toString());
    }

    @Override
    public int countByBrokerAndLogTypeAndUtilityType(Broker broker, LogType logType, UtilityType utilityType) {
        return this.utilityContractRepository.countByBrokerAndLogTypeAndUtilityType(broker, logType.toString(), utilityType.toString());
    }

    @Override
    public void transferBrokerContracts(List<UtilityContract> utilityContracts, BrokerTransferUpdate brokerTransferUpdate) {
        utilityContracts.forEach(utilityContract -> {
            utilityContract.setBroker(brokerTransferUpdate.getNewBroker());
            // save contract
            save(utilityContract);

            // save history
            brokerTransferHistoryService.save(BrokerTransferHistory
                    .builder()
                    .contractId(utilityContract.getId())
                    .supplyType(utilityContract.getUtilityType())
                    .previousBroker(brokerTransferUpdate.getPreviousBroker().getId())
                    .broker(brokerTransferUpdate.getNewBroker().getId())
                    .build());
        });
    }

    @Override
    public List<UtilityContract> findByLogTypeAndBrokerAndCallbackDate(Broker broker, Date callbackDate, LogType callback) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return this.utilityContractRepository.findByLogTypeAndBrokerAndCallbackDate(callback.toString(), format.format(callbackDate), broker.getId());
    }

    @Override
    public List<UtilityContract> findAllVerbalUtilityContracts() {
        return this.utilityContractRepository.findAllVerbalUtilityContracts();
    }
}
