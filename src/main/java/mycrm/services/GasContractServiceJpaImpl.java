package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.models.Broker;
import mycrm.models.BrokerTransferHistory;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.GasCustomerContract;
import mycrm.models.LogTypeHistory;
import mycrm.models.ProcessingPack;
import mycrm.models.TerminationTask;
import mycrm.models.WelcomePack;
import mycrm.repositories.GasContractRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Primary
public class GasContractServiceJpaImpl implements GasContractService {
    private final static Logger logger = LogManager.getLogger();
    private final GasContractRepository gasContractRepo;
    private final LogTypeHistoryService logTypeHistoryService;
    private final BrokerTransferHistoryService brokerTransferHistoryService;

    @Autowired
    public GasContractServiceJpaImpl(GasContractRepository gasContractRepo,
                                     LogTypeHistoryService logTypeHistoryService,
                                     BrokerTransferHistoryService brokerTransferHistoryService) {
        this.gasContractRepo = gasContractRepo;
        this.logTypeHistoryService = logTypeHistoryService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
    }

    @Override
    public List<GasCustomerContract> findAll() {
        return this.gasContractRepo.findAll();
    }

    @Override
    public GasCustomerContract findById(Long id) {
        return this.gasContractRepo.findOne(id);
    }

    @Override
    public GasCustomerContract save(GasCustomerContract gasCustomerContract) {
        String logType = gasCustomerContract.getLogType();
        String previousLogType = null;

        if (gasCustomerContract.getId() != null && findById(gasCustomerContract.getId()) != null) {
            previousLogType = findById(gasCustomerContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);

        GasCustomerContract savedCustomerContract = gasContractRepo.save(gasCustomerContract);

        if (logTypeChanged) {
            saveLogTypeHistory(LogTypeHistory
                    .builder()
                    .contractId(savedCustomerContract.getId())
                    .supplyType(savedCustomerContract.getSupplyType())
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
    public void deleteById(Long id) {
        this.gasContractRepo.delete(id);
    }

    @Override
    public List<GasCustomerContract> findByBroker(Broker broker) {
        return this.gasContractRepo.findByBroker(broker);
    }

    @Override
    public List<GasCustomerContract> findByCustomerSite(CustomerSite customerSiteId) {
        return this.gasContractRepo.findByCustomerSite(customerSiteId);
    }

//    @Override
//    public List<GasCustomerContract> findLatestGasCustomerContractByCustomer(Long customerId) {
//        return this.gasContractRepo.findLatestGasCustomerContractByCustomer(customerId);
//    }

    @Override
    public List<GasCustomerContract> findLatestGasCustomerContractBySiteOrderedByLiveFirst(Long customerSiteId) {
        return this.gasContractRepo.findLatestGasCustomerContractBySite(customerSiteId);
    }

    @Override
    public void updateWelcomePack(WelcomePack welcomePack) {
        this.gasContractRepo.updateWelcomePack(welcomePack.isWelcomePackSentToCustomer(),
                welcomePack.isPreviousSupplyTerminated(), welcomePack.getId());
    }

    @Override
    public void updateProcessingPack(ProcessingPack processingPack) {
        if (processingPack.isContractReceived() && processingPack.isContractSentToCustomer()) {
            // mark as sold
            this.gasContractRepo.updateProcessingPackAsSold(processingPack.isContractSentToCustomer(), processingPack.isContractReceived(), processingPack.getId());
        } else {
            this.gasContractRepo.updateProcessingPack(processingPack.isContractSentToCustomer(), processingPack.isContractReceived(), processingPack.getId());
        }
    }

    @Override
    public void updateTerminationTask(TerminationTask terminationTask) {
        this.gasContractRepo.updateTerminationTask(terminationTask.isCurrentSupplyTerminated(),
                terminationTask.getId());
    }

    @Override
    public List<GasCustomerContract> findByCustomerOrderByEndDateDesc(Customer customer) {
        return this.gasContractRepo.findByCustomerOrderByEndDateDesc(customer);
    }

    @Override
    public void deleteByCustomer(Customer customer) {
        this.gasContractRepo.deleteByCustomer(customer);
    }

    @Override
    public List<GasCustomerContract> findAllContactableDiallerGasContracts(String logType, Broker broker) {
        if (broker != null) {
            return this.gasContractRepo.findAllContactableByLogTypeAndBroker(logType, broker);
        }
        return this.gasContractRepo.findAllContactableByLogType(logType);
    }

    @Override
    public List<GasCustomerContract> findAllLatestContactableDiallerGasContracts(String logType, Broker broker) {
        Date twoDaysAgo = Date.from(LocalDate.now().minusDays(2).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        if (broker != null) {
            return this.gasContractRepo.findAllLatestContactableByLogTypeAndBroker(logType, broker, twoDaysAgo);
        }
        return this.gasContractRepo.findAllLatestContactableByLogType(logType, twoDaysAgo);
    }

    @Override
    public List<GasCustomerContract> findAllRenewableContracts() {
        Date sixMonthsInFuture = Date.from(LocalDate
                .now()
                .plusMonths(6)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        logger.info("Get Gas renewals for: {}", format.format(sixMonthsInFuture));

        return this.gasContractRepo.findAllRenewableContracts(format.format(sixMonthsInFuture));
    }

    @Override
    public List<GasCustomerContract> findByBrokerAndLogType(Broker broker, LogType logType) {
        return this.gasContractRepo.findByBrokerAndLogType(broker, logType.toString());
    }

    @Override
    public void transferBrokerContracts(List<GasCustomerContract> gasContracts, BrokerTransferUpdate brokerTransferUpdate) {
        gasContracts.forEach(gasCustomerContract -> {
            gasCustomerContract.setBroker(brokerTransferUpdate.getNewBroker());
            // save contract
            save(gasCustomerContract);

            // save history
            brokerTransferHistoryService.save(BrokerTransferHistory
                    .builder()
                    .contractId(gasCustomerContract.getId())
                    .supplyType(gasCustomerContract.getSupplyType())
                    .previousBroker(brokerTransferUpdate.getPreviousBroker().getId())
                    .broker(brokerTransferUpdate.getNewBroker().getId())
                    .build());
        });
    }

    @Override
    public int countByBrokerAndLogType(Broker broker, LogType logType) {
        return this.gasContractRepo.countByBrokerAndLogType(broker, logType.toString());
    }

    @Override
    public List<GasCustomerContract> findByLogTypeAndBrokerAndCallbackDate(Broker broker, Date callbackDate, LogType callback) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return this.gasContractRepo.findByLogTypeAndBrokerAndCallbackDate(callback.toString(), format.format(callbackDate), broker.getId());
    }

}
