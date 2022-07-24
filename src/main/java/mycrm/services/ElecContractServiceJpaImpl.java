package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.models.Broker;
import mycrm.models.BrokerTransferHistory;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.LogTypeHistory;
import mycrm.models.ProcessingPack;
import mycrm.models.TerminationTask;
import mycrm.models.WelcomePack;
import mycrm.repositories.ElecContractRepository;
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
public class ElecContractServiceJpaImpl implements ElecContractService {
    private static final Logger logger = LogManager.getLogger();
    private final ElecContractRepository elecContractRepo;
    private final LogTypeHistoryService logTypeHistoryService;
    private final BrokerTransferHistoryService brokerTransferHistoryService;

    @Autowired
    public ElecContractServiceJpaImpl(ElecContractRepository elecContractRepo,
                                      LogTypeHistoryService logTypeHistoryService,
                                      BrokerTransferHistoryService brokerTransferHistoryService) {
        this.elecContractRepo = elecContractRepo;
        this.logTypeHistoryService = logTypeHistoryService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
    }

    @Override
    public List<ElecCustomerContract> findAll() {
        return this.elecContractRepo.findAll();
    }

    @Override
    public ElecCustomerContract findById(Long id) {
        return this.elecContractRepo.findOne(id);
    }

    @Override
    public ElecCustomerContract save(ElecCustomerContract elecCustomerContract) {
        String logType = elecCustomerContract.getLogType();
        String previousLogType = null;

        if (elecCustomerContract.getId() != null && findById(elecCustomerContract.getId()) != null) {
            previousLogType = findById(elecCustomerContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);

        ElecCustomerContract savedCustomerContract = elecContractRepo.save(elecCustomerContract);

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
        this.elecContractRepo.delete(id);
    }

    @Override
    public List<ElecCustomerContract> findByBroker(Broker broker) {
        return this.elecContractRepo.findByBroker(broker);
    }

    @Override
    public List<ElecCustomerContract> findByCustomerSite(CustomerSite customerSite) {
        return this.elecContractRepo.findByCustomerSite(customerSite);
    }

//    @Override
//    public List<ElecCustomerContract> findLatestElecCustomerContractByCustomer(Long customerId) {
//        return this.elecContractRepo.findLatestElecCustomerContractByCustomer(customerId);
//    }

    @Override
    public List<ElecCustomerContract> findLatestElecCustomerContractBySiteOrderedByLiveFirst(Long customerSiteId) {
        return this.elecContractRepo.findLatestElecCustomerContractBySite(customerSiteId);
    }

    @Override
    public void updateWelcomePack(WelcomePack welcomePack) {
        this.elecContractRepo.updateWelcomePack(welcomePack.isWelcomePackSentToCustomer(),
                welcomePack.isPreviousSupplyTerminated(), welcomePack.getId());
    }

    @Override
    public void updateProcessingPack(ProcessingPack processingPack) {
        if (processingPack.isContractReceived() && processingPack.isContractSentToCustomer()) {
            // mark as sold
            this.elecContractRepo.updateProcessingPackAsSold(processingPack.isContractSentToCustomer(), processingPack.isContractReceived(), processingPack.getId());
        } else {
            this.elecContractRepo.updateProcessingPack(processingPack.isContractSentToCustomer(), processingPack.isContractReceived(), processingPack.getId());
        }
    }

    @Override
    public void updateTerminationTask(TerminationTask terminationTask) {
        this.elecContractRepo.updateTerminationTask(terminationTask.isCurrentSupplyTerminated(),
                terminationTask.getId());

    }

    @Override
    public List<ElecCustomerContract> findByCustomerOrderByEndDateDesc(Customer customer) {
        return this.elecContractRepo.findByCustomerOrderByEndDateDesc(customer);
    }

    @Override
    public void deleteByCustomer(Customer customer) {
        this.elecContractRepo.deleteByCustomer(customer);
    }

    @Override
    public List<ElecCustomerContract> findAllContactableDiallerElectricContracts(String logType, Broker broker) {
        if (broker != null) {
            return this.elecContractRepo.findAllContactableByLogTypeAndBroker(logType, broker);
        }
        return this.elecContractRepo.findAllContactableByLogType(logType);
    }

    @Override
    public List<ElecCustomerContract> findAllLatestContactableDiallerElectricContracts(String logType, Broker broker) {
        Date twoDaysAgo = Date.from(LocalDate.now().minusDays(2).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        if (broker != null) {
            return this.elecContractRepo.findAllLatestContactableByLogTypeAndBroker(logType, broker, twoDaysAgo);
        }
        return this.elecContractRepo.findAllLatestContactableByLogType(logType, twoDaysAgo);
    }

    @Override
    public List<ElecCustomerContract> findAllRenewableContracts() {
        Date sixMonthsInFuture = Date.from(LocalDate
                .now()
                .plusMonths(6)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        logger.info("Get Electricity renewals for: {}", format.format(sixMonthsInFuture));

        return this.elecContractRepo.findAllRenewableContracts(format.format(sixMonthsInFuture));
    }

    @Override
    public List<ElecCustomerContract> findByBrokerAndLogType(Broker broker, LogType logType) {
        return this.elecContractRepo.findByBrokerAndLogType(broker, logType.toString());
    }

    @Override
    public int countByBrokerAndLogType(Broker broker, LogType logType) {
        return this.elecContractRepo.countByBrokerAndLogType(broker, logType.toString());
    }

    @Override
    public void transferBrokerContracts(List<ElecCustomerContract> electricContracts, BrokerTransferUpdate brokerTransferUpdate) {
        electricContracts.forEach(elecCustomerContract -> {
            elecCustomerContract.setBroker(brokerTransferUpdate.getNewBroker());

            // save contract
            save(elecCustomerContract);

            // save history
            brokerTransferHistoryService.save(BrokerTransferHistory
                    .builder()
                    .contractId(elecCustomerContract.getId())
                    .supplyType(elecCustomerContract.getSupplyType())
                    .previousBroker(brokerTransferUpdate.getPreviousBroker().getId())
                    .broker(brokerTransferUpdate.getNewBroker().getId())
                    .build());
        });
    }

    @Override
    public List<ElecCustomerContract> findByLogTypeAndBrokerAndCallbackDate(Broker broker, Date callbackDate, LogType logType) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return this.elecContractRepo.findByLogTypeAndBrokerAndCallbackDate(logType.toString(), format.format(callbackDate), broker.getId());
    }
}
