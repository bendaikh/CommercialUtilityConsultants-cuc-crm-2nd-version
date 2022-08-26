package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.models.*;

import java.util.List;

public interface MerchantServicesService {
    MerchantServicesContract findById(Long id);

    MerchantServicesContract save(MerchantServicesContract merchantServicesContract);

    MerchantServicesSearchResult getMerchantServicesContracts(MerchantServicesContractSearch merchantServicesContractSearch, int pageNumber);

    List<MerchantServicesContract> findMerchantServicesContractByCustomerSite(CustomerSite customerSite);

    List<MerchantServicesContract> findByCustomerSite(CustomerSite customerSite);

    void deleteById(Long id);

    void deleteByCustomerSite(CustomerSite customerSite);

    void transferBrokerContracts(List<MerchantServicesContract> contracts, BrokerTransferUpdate brokerTransferUpdate);

    List<MerchantServicesContract> findByCustomerOrderByEndDateDesc(Customer customer);

    List<MerchantServicesContract> findByCustomer(Customer customer);

    List<MerchantServicesContract> findByBrokerAndLogTypeAndUtilityType(Broker broker, LogType logType);

    List<MerchantServicesContract> findAllObjectedMerchantServiceContracts();

    List<MerchantServicesContract> findAllVerbalMerchantServiceContracts();
    List<MerchantServicesContract> findAllAdminContractProcessingTasks();

    List<MerchantServicesContract> findAllMerchantContractsToTerminate();

    List<MerchantServicesContract> findAllAdminMerchantServiceContractNewSalesTasks();

    int countByBrokerAndLogType(Broker broker, LogType logType);
}
