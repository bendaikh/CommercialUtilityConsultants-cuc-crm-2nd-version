package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.configuration.UtilityType;
import mycrm.models.Broker;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ProcessingPack;
import mycrm.models.TerminationTask;
import mycrm.models.UtilityContract;
import mycrm.models.WelcomePack;

import java.util.Date;
import java.util.List;

public interface UtilityContractService {
    UtilityContract save(UtilityContract utilityContract);

    List<UtilityContract> findByCustomerSiteAndUtilityType(CustomerSite customerSite, UtilityType utilityType);

    List<UtilityContract> findByCustomerSite(CustomerSite customerSite);

    List<UtilityContract> findSolarContractByCustomerSite(CustomerSite customerSite);

    List<UtilityContract> findWaterContractByCustomerSite(CustomerSite customerSite);

    List<UtilityContract> findVoipContractByCustomerSite(CustomerSite customerSite);

    List<UtilityContract> findLandlineContractByCustomerSite(CustomerSite customerSite);

    List<UtilityContract> findMobileContractByCustomerSite(CustomerSite customerSite);

    List<UtilityContract> findBroadbandContractByCustomerSite(CustomerSite customerSite);

    UtilityContract findById(Long id);

    List<UtilityContract> findByCustomerOrderByEndDateDesc(Customer customer);

    List<UtilityContract> findAllAdminUtilityContractNewSalesTasks();

    void updateWelcomePack(WelcomePack welcomePack);

    List<UtilityContract> findAllUtilityContractsToTerminate();

    void updateTerminationTask(TerminationTask terminationTask);

    List<UtilityContract> findAllAdminContractProcessingTasks();

    void updateProcessingPack(ProcessingPack processingPack);

    List<UtilityContract> findAllObjectedUtilityContracts();

    List<UtilityContract> findByCustomer(Customer customer);

    void deleteByCustomerSite(CustomerSite customerSite);

    List<UtilityContract> findByBrokerAndLogTypeAndUtilityType(Broker broker, LogType logType, UtilityType utilityType);

    int countByBrokerAndLogTypeAndUtilityType(Broker broker, LogType logType, UtilityType utilityType);

    void transferBrokerContracts(List<UtilityContract> utilityContracts, BrokerTransferUpdate brokerTransferUpdate);

    List<UtilityContract> findByLogTypeAndBrokerAndCallbackDate(Broker broker, Date callbackDate, LogType callback);

    List<UtilityContract> findAllVerbalUtilityContracts();
}
