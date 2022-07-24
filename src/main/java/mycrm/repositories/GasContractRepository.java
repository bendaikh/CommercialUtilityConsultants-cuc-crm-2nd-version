package mycrm.repositories;

import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.GasCustomerContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface GasContractRepository extends JpaRepository<GasCustomerContract, Long> {

    String CURRENT_SUPPLY_TERMINATED_QUERY = "SELECT * FROM gas_customer_contract gcc WHERE gcc.current_supply_terminated = 0 AND gcc.end_date >= DATE_SUB(now(), INTERVAL 4 " +
            "MONTH) AND gcc.end_date <= DATE_ADD(now(), INTERVAL 1 MONTH) AND gcc.log_type = 'LIVE'";
    String ADMIN_CONTRACT_NEW_SALES_TASKS_QUERY = "SELECT * FROM gas_customer_contract gcc WHERE (gcc.welcome_pack_sent_to_customer = 0 OR " +
            "gcc.previous_supply_terminated = 0) AND (gcc.log_type = 'LIVE' OR gcc.log_type = 'SOLD') AND gcc.start_date IS NOT NULL";

    // -- old and unused @Query(value = "SELECT g1.* from gas_customer_contract g1 where g1.id in (SELECT max(id) from gas_customer_contract group by customer_site_id)",
    // nativeQuery = true)
//    @Query(value = "SELECT g.* from latest_gas_contracts_by_site g where g.customer_id= ?1", nativeQuery = true)
//    List<GasCustomerContract> findLatestGasCustomerContractByCustomer(Long customerId);

    @Query("SELECT gcc FROM GasCustomerContract gcc WHERE broker=(:broker)")
    List<GasCustomerContract> findByBroker(@Param("broker") Broker broker);

    @Query("SELECT gcc FROM GasCustomerContract gcc WHERE customerSite=(:customerSite)")
    List<GasCustomerContract> findByCustomerSite(@Param("customerSite") CustomerSite customerSite);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE GasCustomerContract g SET currentSupplyTerminated=(:currentSupplyTerminated) WHERE id=(:id)")
    void updateTerminationTask(@Param("currentSupplyTerminated") boolean currentSupplyTerminated, @Param("id") Long id);

    List<GasCustomerContract> findByCustomerOrderByEndDateDesc(Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM GasCustomerContract g WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);

    @Query("SELECT DISTINCT(g.campaign) FROM GasCustomerContract g WHERE g.campaign <> ''")
    List<String> getDistinctCampaigns();

    String ADMIN_CONTRACT_PROCESSING_TASKS_QUERY = "SELECT * FROM gas_customer_contract gcc WHERE (gcc.contract_sent_to_customer = 0 OR gcc.contract_received = 0) AND gcc" +
            ".log_type = 'PROCESSING'";

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE GasCustomerContract g SET welcomePackSentToCustomer=(:welcomePackSentToCustomer), previousSupplyTerminated=(:previousSupplyTerminated) WHERE id=(:id)")
    void updateWelcomePack(@Param("welcomePackSentToCustomer") boolean welcomePackSentToCustomer,
                           @Param("previousSupplyTerminated") boolean previousSupplyTerminated, @Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE GasCustomerContract g SET contractSentToCustomer=(:contractSentToCustomer), contractReceived=(:contractReceived) WHERE id=(:id)")
    void updateProcessingPack(@Param("contractSentToCustomer") boolean contractSentToCustomer, @Param("contractReceived") boolean contractReceived, @Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE GasCustomerContract g SET contractSentToCustomer=(:contractSentToCustomer), contractReceived=(:contractReceived), logType='SOLD' WHERE id=(:id)")
    void updateProcessingPackAsSold(@Param("contractSentToCustomer") boolean contractSentToCustomer, @Param("contractReceived") boolean contractReceived, @Param("id") Long id);

    @Query("SELECT g FROM GasCustomerContract g WHERE g.logType='OBJECTED'")
    List<GasCustomerContract> findAllObjectedGasContracts();

    @Query(value = CURRENT_SUPPLY_TERMINATED_QUERY, nativeQuery = true)
    List<GasCustomerContract> findAllGasContractsToTerminate();

    @Query(value = ADMIN_CONTRACT_NEW_SALES_TASKS_QUERY, nativeQuery = true)
    List<GasCustomerContract> findAllGasAdminContractNewSalesTasks();

    @Query(value = ADMIN_CONTRACT_PROCESSING_TASKS_QUERY, nativeQuery = true)
    List<GasCustomerContract> findAllGasAdminContractProcessingTasks();

    @Query("SELECT g FROM GasCustomerContract g WHERE g.logType=(:logType) and g.customer.doNotContact='0' and g.broker=(:broker)")
    List<GasCustomerContract> findAllContactableByLogTypeAndBroker(@Param("logType") String logType, @Param("broker") Broker broker);

    @Query("SELECT g FROM GasCustomerContract g WHERE g.logType=(:logType) and g.customer.doNotContact='0'")
    List<GasCustomerContract> findAllContactableByLogType(@Param("logType") String logType);

    @Query("SELECT g FROM GasCustomerContract g WHERE g.logType=(:logType) and g.customer.doNotContact='0' and g.broker=(:broker) and (g.lastModifiedDate>=(:twoDaysAgo)" +
            " or g.dateCreated>=(:twoDaysAgo))")
    List<GasCustomerContract> findAllLatestContactableByLogTypeAndBroker(@Param("logType") String logType,
                                                                         @Param("broker") Broker broker,
                                                                         @Param("twoDaysAgo") Date twoDaysAgo);

    @Query("SELECT g FROM GasCustomerContract g WHERE g.logType=(:logType) and g.customer.doNotContact='0'" +
            " and (g.lastModifiedDate>=(:twoDaysAgo) or g.dateCreated>=(:twoDaysAgo))")
    List<GasCustomerContract> findAllLatestContactableByLogType(@Param("logType") String logType, @Param("twoDaysAgo") Date twoDaysAgo);

    // @Query(value = "SELECT * FROM gas_customer_contract g WHERE g.log_type='LIVE' and DATE_SUB(g.end_date, INTERVAL 6 MONTH) = ?1", nativeQuery = true)
    @Query(value = "SELECT * FROM gas_customer_contract g WHERE g.log_type='LIVE' and g.end_date = ?1", nativeQuery = true)
    List<GasCustomerContract> findAllRenewableContracts(String sixMonthsInFuture);

    @Query(value = "SELECT * FROM gas_customer_contract g WHERE g.customer_site_id = ?1 ORDER BY g.log_type = 'LIVE' DESC, g.id DESC", nativeQuery = true)
    List<GasCustomerContract> findLatestGasCustomerContractBySite(Long customerSiteId);

    @Query("SELECT g from GasCustomerContract g WHERE g.broker=(:broker) and g.logType=(:logType)")
    List<GasCustomerContract> findByBrokerAndLogType(@Param("broker") Broker broker, @Param("logType") String logType);

    @Query("SELECT count(*) from GasCustomerContract g WHERE g.broker=(:broker) and g.logType=(:logType)")
    int countByBrokerAndLogType(@Param("broker") Broker broker, @Param("logType") String logType);

    @Query(nativeQuery = true, value = "SELECT * FROM gas_customer_contract g WHERE g.log_type=?1 " +
            " and g.callback_date_time like (?2%) and g.broker_id=?3 ORDER BY g.callback_date_time ASC")
    List<GasCustomerContract> findByLogTypeAndBrokerAndCallbackDate(String logType,
                                                                    String callbackDate,
                                                                    Long broker);

    @Query("SELECT g FROM GasCustomerContract g WHERE g.logType='VERBAL'")
    List<GasCustomerContract> findAllVerbalGasContracts();
}
