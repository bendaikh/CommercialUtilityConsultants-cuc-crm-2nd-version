package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.models.Broker;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.GasCustomerContract;
import mycrm.models.ProcessingPack;
import mycrm.models.TerminationTask;
import mycrm.models.WelcomePack;

import java.util.Date;
import java.util.List;

public interface GasContractService {

    List<GasCustomerContract> findAll();

    GasCustomerContract findById(Long id);

    List<GasCustomerContract> findByBroker(Broker broker);

    List<GasCustomerContract> findByCustomerSite(CustomerSite customerSiteId);

    GasCustomerContract save(GasCustomerContract gasCustomerContract);

    // List<GasCustomerContract> findLatestGasCustomerContractByCustomer(Long customerId);

    List<GasCustomerContract> findLatestGasCustomerContractBySiteOrderedByLiveFirst(Long customerSiteId);

    void updateWelcomePack(WelcomePack welcomepack);

    void deleteById(Long id);

    void updateProcessingPack(ProcessingPack processingPack);

    void updateTerminationTask(TerminationTask terminationTask);

    List<GasCustomerContract> findByCustomerOrderByEndDateDesc(Customer customer);

    void deleteByCustomer(Customer customer);

    List<GasCustomerContract> findAllContactableDiallerGasContracts(String logType, Broker broker);

    List<GasCustomerContract> findAllLatestContactableDiallerGasContracts(String logType, Broker broker);

    List<GasCustomerContract> findAllRenewableContracts();

    List<GasCustomerContract> findByBrokerAndLogType(Broker broker, LogType logType);

    void transferBrokerContracts(List<GasCustomerContract> gasContracts, BrokerTransferUpdate brokerTransferUpdate);

    int countByBrokerAndLogType(Broker broker, LogType logType);

    List<GasCustomerContract> findByLogTypeAndBrokerAndCallbackDate(Broker broker, Date callbackDate, LogType callback);
}
