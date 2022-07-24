package mycrm.repositories;

import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ElecContractRepository extends JpaRepository<ElecCustomerContract, Long> {

    String CURRENT_SUPPLY_TERMINATED_QUERY = "SELECT * FROM elec_customer_contract ecc WHERE ecc.current_supply_terminated = 0 AND ecc.end_date >= DATE_SUB(now(), INTERVAL " +
            "4 MONTH) AND ecc.end_date <= DATE_ADD(now(), INTERVAL 1 MONTH) AND ecc.log_type = 'LIVE'";
    String ADMIN_CONTRACT_NEW_SALES_TASKS_QUERY = "SELECT * FROM elec_customer_contract ecc WHERE (ecc.welcome_pack_sent_to_customer = 0 OR ecc.previous_supply_terminated = 0) " +
            "AND (ecc.log_type = 'LIVE' OR ecc.log_type = 'SOLD') AND ecc.start_date IS NOT NULL";

    // -- old and unused @Query(value="SELECT * from elec_customer_contract where id in (SELECT max(id) from elec_customer_contract group by customer_site_id)", nativeQuery=true)
    // @Query(value = "SELECT e.* from latest_elec_contracts_by_site e where e.customer_id= ?1", nativeQuery = true)
    // List<ElecCustomerContract> findLatestElecCustomerContractByCustomer(Long customerId);

    @Query("SELECT ecc FROM ElecCustomerContract ecc WHERE broker=(:broker)")
    List<ElecCustomerContract> findByBroker(@Param("broker") Broker broker);

    @Query("SELECT ecc FROM ElecCustomerContract ecc WHERE customerSite=(:customerSite)")
    List<ElecCustomerContract> findByCustomerSite(@Param("customerSite") CustomerSite customerSite);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ElecCustomerContract e SET currentSupplyTerminated=(:currentSupplyTerminated) WHERE id=(:id)")
    void updateTerminationTask(@Param("currentSupplyTerminated") boolean currentSupplyTerminated, @Param("id") Long id);

    List<ElecCustomerContract> findByCustomerOrderByEndDateDesc(Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM ElecCustomerContract e WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);

    @Query("SELECT DISTINCT(e.campaign) FROM ElecCustomerContract e WHERE e.campaign <> ''")
    List<String> getDistinctCampaigns();

    String ADMIN_CONTRACT_PROCESSING_TASKS_QUERY = "SELECT * FROM elec_customer_contract ecc WHERE (ecc.contract_sent_to_customer = 0 OR ecc.contract_received = 0)  AND ecc" +
            ".log_type = 'PROCESSING'";

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ElecCustomerContract e SET welcomePackSentToCustomer=(:welcomePackSentToCustomer), previousSupplyTerminated=(:previousSupplyTerminated) WHERE id=(:id)")
    void updateWelcomePack(@Param("welcomePackSentToCustomer") boolean welcomePackSentToCustomer,
                           @Param("previousSupplyTerminated") boolean previousSupplyTerminated, @Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ElecCustomerContract e SET contractSentToCustomer=(:contractSentToCustomer), contractReceived=(:contractReceived) WHERE id=(:id)")
    void updateProcessingPack(@Param("contractSentToCustomer") boolean contractSentToCustomer, @Param("contractReceived") boolean contractReceived, @Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ElecCustomerContract e SET contractSentToCustomer=(:contractSentToCustomer), contractReceived=(:contractReceived), logType='SOLD' WHERE id=(:id)")
    void updateProcessingPackAsSold(@Param("contractSentToCustomer") boolean contractSentToCustomer, @Param("contractReceived") boolean contractReceived, @Param("id") Long id);

    @Query("SELECT e FROM ElecCustomerContract e WHERE e.logType='OBJECTED'")
    List<ElecCustomerContract> findAllObjectedElectricityContracts();

    @Query(value = CURRENT_SUPPLY_TERMINATED_QUERY, nativeQuery = true)
    List<ElecCustomerContract> findAllElectricContractsToTerminate();

    @Query(value = ADMIN_CONTRACT_NEW_SALES_TASKS_QUERY, nativeQuery = true)
    List<ElecCustomerContract> findAllElectricAdminContractNewSalesTasks();

    @Query(value = ADMIN_CONTRACT_PROCESSING_TASKS_QUERY, nativeQuery = true)
    List<ElecCustomerContract> findAllElectricAdminContractProcessingTasks();

    @Query("SELECT e FROM ElecCustomerContract e WHERE e.logType=(:logType) and e.customer.doNotContact='0' and e.broker=(:broker)")
    List<ElecCustomerContract> findAllContactableByLogTypeAndBroker(@Param("logType") String logType, @Param("broker") Broker broker);

    @Query("SELECT e FROM ElecCustomerContract e WHERE e.logType=(:logType) and e.customer.doNotContact='0'")
    List<ElecCustomerContract> findAllContactableByLogType(@Param("logType") String logType);

    @Query("SELECT e FROM ElecCustomerContract e WHERE e.logType=(:logType) and e.customer.doNotContact='0' and e.broker=(:broker) " +
            "and (e.lastModifiedDate>=(:twoDaysAgo) or e.dateCreated>=(:twoDaysAgo))")
    List<ElecCustomerContract> findAllLatestContactableByLogTypeAndBroker(@Param("logType") String logType, @Param("broker") Broker broker, @Param("twoDaysAgo") Date twoDaysAgo);

    @Query("SELECT e FROM ElecCustomerContract e WHERE e.logType=(:logType) and e.customer.doNotContact='0'" +
            " and (e.lastModifiedDate>=(:twoDaysAgo) or e.dateCreated>=(:twoDaysAgo))")
    List<ElecCustomerContract> findAllLatestContactableByLogType(@Param("logType") String logType, @Param("twoDaysAgo") Date twoDaysAgo);

    //    @Query(value = "SELECT * FROM elec_customer_contract e WHERE e.log_type='LIVE' and DATE_SUB(e.end_date, INTERVAL 6 MONTH) = ?1", nativeQuery = true)
    @Query(value = "SELECT * FROM elec_customer_contract e WHERE e.log_type='LIVE' and e.end_date = ?1", nativeQuery = true)
    List<ElecCustomerContract> findAllRenewableContracts(String sixMonthsInFuture);

    @Query(value = "SELECT * FROM elec_customer_contract e WHERE e.customer_site_id = ?1 ORDER BY e.log_type = 'LIVE' DESC, e.id DESC", nativeQuery = true)
    List<ElecCustomerContract> findLatestElecCustomerContractBySite(Long customerSiteId);

    @Query("SELECT e from ElecCustomerContract e WHERE e.broker=(:broker) and e.logType=(:logType)")
    List<ElecCustomerContract> findByBrokerAndLogType(@Param("broker") Broker broker, @Param("logType") String logType);

    @Query("SELECT count(*) from ElecCustomerContract e WHERE e.broker=(:broker) and e.logType=(:logType)")
    int countByBrokerAndLogType(@Param("broker") Broker broker, @Param("logType") String logType);

    @Query(nativeQuery = true, value = "SELECT * FROM elec_customer_contract g WHERE g.log_type=?1 " +
            " and g.callback_date_time like (?2%) and g.broker_id=?3 ORDER BY g.callback_date_time ASC")
    List<ElecCustomerContract> findByLogTypeAndBrokerAndCallbackDate(String logType,
                                                                     String callbackDate,
                                                                     Long broker);

    @Query("SELECT e FROM ElecCustomerContract e WHERE e.logType='VERBAL'")
    List<ElecCustomerContract> findAllVerbalElectricityContracts();
}
