package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.models.Broker;
import mycrm.models.BrokerTransferHistory;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.LogTypeHistory;
import mycrm.models.MerchantServicesContract;
import mycrm.models.MerchantServicesContractSearch;
import mycrm.models.MerchantServicesSearchResult;
import mycrm.repositories.MerchantServicesRepository;
import mycrm.repositories.MerchantServicesSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Primary
public class MerchantServicesServiceImpl implements MerchantServicesService {

    private final MerchantServicesRepository merchantServicesRepository;
    private final LogTypeHistoryService logTypeHistoryService;
    private final MerchantServicesSearchRepository merchantServicesSearchRepository;
    private final BrokerTransferHistoryService brokerTransferHistoryService;

    @Autowired
    public MerchantServicesServiceImpl(MerchantServicesRepository merchantServicesRepository,
                                       LogTypeHistoryService logTypeHistoryService, MerchantServicesSearchRepository merchantServicesSearchRepository,
                                       BrokerTransferHistoryService brokerTransferHistoryService) {
        this.merchantServicesRepository = merchantServicesRepository;
        this.logTypeHistoryService = logTypeHistoryService;
        this.merchantServicesSearchRepository = merchantServicesSearchRepository;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
    }


    @Override
    public MerchantServicesContract findById(Long id) {
        return this.merchantServicesRepository.findOne(id);
    }

    @Override
    public MerchantServicesContract save(MerchantServicesContract merchantServicesContract) {
        String logType = merchantServicesContract.getLogType();
        String previousLogType = null;

        if (merchantServicesContract.getId() != null && findById(merchantServicesContract.getId()) != null) {
            previousLogType = findById(merchantServicesContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);

        MerchantServicesContract savedCustomerContract = merchantServicesRepository.save(merchantServicesContract);

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

    public MerchantServicesSearchResult getMerchantServicesContracts(MerchantServicesContractSearch merchantServicesContractSearch, int pageNumber) {
        return this.merchantServicesSearchRepository.search(merchantServicesContractSearch, pageNumber);
    }

    @Override
    public List<MerchantServicesContract> findMerchantServicesContractByCustomerSite(CustomerSite customerSite) {
        List<MerchantServicesContract> contracts = this.findByCustomerSite(customerSite);
        contracts.sort(Comparator.comparing(MerchantServicesContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<MerchantServicesContract> findByCustomerSite(CustomerSite customerSite) {
        return this.merchantServicesRepository.findLatestMerchantServicesContractByCustomerSite(customerSite.getId());
    }

    @Override
    public void deleteById(Long id) {
        this.merchantServicesRepository.delete(id);
    }

    @Override
    public void deleteByCustomerSite(CustomerSite customerSite) {
        this.merchantServicesRepository.deleteByCustomerSite(customerSite);
    }

    @Override
    public void transferBrokerContracts(List<MerchantServicesContract> contracts, BrokerTransferUpdate brokerTransferUpdate) {
        contracts.forEach(contract -> {
            contract.setBroker(brokerTransferUpdate.getNewBroker());
            // save contract
            save(contract);

            // save history
            brokerTransferHistoryService.save(BrokerTransferHistory
                    .builder()
                    .contractId(contract.getId())
                    .supplyType(contract.getSupplyType())
                    .previousBroker(brokerTransferUpdate.getPreviousBroker().getId())
                    .broker(brokerTransferUpdate.getNewBroker().getId())
                    .build());
        });
    }

    @Override
    public List<MerchantServicesContract> findByCustomerOrderByEndDateDesc(Customer customer) {
        return this.merchantServicesRepository.findByCustomerOrderByEndDateDesc(customer);
    }

    @Override
    public List<MerchantServicesContract> findByCustomer(Customer customer) {
        return this.merchantServicesRepository.findByCustomer(customer);
    }

    @Override
    public List<MerchantServicesContract> findByBrokerAndLogTypeAndUtilityType(Broker broker, LogType logType) {
        return this.merchantServicesRepository.findByBrokerAndLogType(broker, logType.toString());
    }

    @Override
    public int countByBrokerAndLogType(Broker broker, LogType logType) {
        return this.merchantServicesRepository.countByBrokerAndLogType(broker, logType.toString());
    }
}
