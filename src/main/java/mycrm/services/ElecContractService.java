package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.models.Broker;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.ProcessingPack;
import mycrm.models.TerminationTask;
import mycrm.models.WelcomePack;

import java.util.Date;
import java.util.List;

public interface ElecContractService {

    List<ElecCustomerContract> findAll();

    ElecCustomerContract findById(Long id);

    List<ElecCustomerContract> findByBroker(Broker broker);

    List<ElecCustomerContract> findByCustomerSite(CustomerSite customerSite);

    ElecCustomerContract save(ElecCustomerContract elecCustomerContract);

    // List<ElecCustomerContract> findLatestElecCustomerContractByCustomer(Long customerId);

    List<ElecCustomerContract> findLatestElecCustomerContractBySiteOrderedByLiveFirst(Long customerSiteId);

    void deleteById(Long id);

    void updateWelcomePack(WelcomePack welcomePack);

    void updateProcessingPack(ProcessingPack processingPack);

    void updateTerminationTask(TerminationTask terminationTask);

    List<ElecCustomerContract> findByCustomerOrderByEndDateDesc(Customer customer);

    void deleteByCustomer(Customer customer);

    List<ElecCustomerContract> findAllContactableDiallerElectricContracts(String logType, Broker broker);

    List<ElecCustomerContract> findAllLatestContactableDiallerElectricContracts(String logType, Broker broker);

    List<ElecCustomerContract> findAllRenewableContracts();

    List<ElecCustomerContract> findByBrokerAndLogType(Broker broker, LogType logType);

    int countByBrokerAndLogType(Broker broker, LogType logType);

    void transferBrokerContracts(List<ElecCustomerContract> electricContracts, BrokerTransferUpdate brokerTransferUpdate);

    List<ElecCustomerContract> findByLogTypeAndBrokerAndCallbackDate(Broker broker, Date callbackDate, LogType logType);
}
