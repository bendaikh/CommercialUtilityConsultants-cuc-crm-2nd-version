package mycrm.repositories;

import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.UtilityContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UtilityContractRepository extends JpaRepository<UtilityContract, Long> {
    String ADMIN_CONTRACT_NEW_SALES_TASKS_QUERY = "SELECT * FROM utility_contract uc WHERE (uc.welcome_pack_sent_to_customer = 0 OR uc.previous_supply_terminated = 0) " +
            "AND (uc.log_type = 'LIVE' OR uc.log_type = 'SOLD') AND uc.start_date IS NOT NULL";

    String CURRENT_SUPPLY_TERMINATED_QUERY = "SELECT * FROM utility_contract uc WHERE uc.current_supply_terminated = 0 AND uc.end_date >= DATE_SUB(now(), INTERVAL " +
            "4 MONTH) AND uc.end_date <= DATE_ADD(now(), INTERVAL 1 MONTH) AND uc.log_type = 'LIVE'";

    @Query(value = "SELECT * FROM utility_contract u WHERE u.customer_site_id = ?1 ORDER BY u.log_type = 'LIVE' DESC, u.id DESC", nativeQuery = true)
    List<UtilityContract> findLatestUtilityContractByCustomerSite(Long customerSiteId);

    @Query(value = "SELECT * FROM utility_contract u WHERE u.customer_site_id = ?1 and u.utility_type = ?2 ORDER BY u.log_type = 'LIVE' DESC, u.id DESC", nativeQuery = true)
    List<UtilityContract> findLatestUtilityContractByCustomerSiteAndUtilityType(Long customerSiteId, String utilityType);

    @Query("SELECT u FROM UtilityContract u WHERE u.customerSite.customer=(:customer)")
    List<UtilityContract> findByCustomerOrderByEndDateDesc(@Param("customer") Customer customer);

    @Query(value = ADMIN_CONTRACT_NEW_SALES_TASKS_QUERY, nativeQuery = true)
    List<UtilityContract> findAllAdminUtilityContractNewSalesTasks();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UtilityContract u SET welcomePackSentToCustomer=(:welcomePackSentToCustomer), previousSupplyTerminated=(:previousSupplyTerminated) WHERE id=(:id)")
    void updateWelcomePack(@Param("welcomePackSentToCustomer") boolean welcomePackSentToCustomer,
                           @Param("previousSupplyTerminated") boolean previousSupplyTerminated,
                           @Param("id") Long id);

    @Query(value = CURRENT_SUPPLY_TERMINATED_QUERY, nativeQuery = true)
    List<UtilityContract> findAllUtilityContractsToTerminate();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UtilityContract uc SET currentSupplyTerminated=(:currentSupplyTerminated) WHERE id=(:id)")
    void updateTerminationTask(@Param("currentSupplyTerminated") boolean currentSupplyTerminated, @Param("id") Long id);

    @Query(value = "SELECT * FROM utility_contract uc WHERE (uc.contract_sent_to_customer = 0 OR uc.contract_received = 0) AND uc.log_type = 'PROCESSING'", nativeQuery = true)
    List<UtilityContract> findAllAdminContractProcessingTasks();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UtilityContract u SET contractSentToCustomer=(:contractSentToCustomer), contractReceived=(:contractReceived), logType='SOLD' WHERE id=(:id)")
    void updateProcessingPackAsSold(@Param("contractSentToCustomer") boolean contractSentToCustomer, @Param("contractReceived") boolean contractReceived, @Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UtilityContract u SET contractSentToCustomer=(:contractSentToCustomer), contractReceived=(:contractReceived) WHERE id=(:id)")
    void updateProcessingPack(@Param("contractSentToCustomer") boolean contractSentToCustomer, @Param("contractReceived") boolean contractReceived, @Param("id") Long id);

    @Query("SELECT u FROM UtilityContract u WHERE u.logType='OBJECTED'")
    List<UtilityContract> findAllObjectedUtilityContracts();

    @Query("SELECT u FROM UtilityContract u WHERE customerSite.customer=(:customer)")
    List<UtilityContract> findByCustomer(@Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM UtilityContract u WHERE customerSite=(:customerSite)")
    void deleteByCustomerSite(@Param("customerSite") CustomerSite customerSite);

    @Query("SELECT u from UtilityContract u WHERE u.broker=(:broker) and u.logType=(:logType) and u.utilityType=(:utilityType)")
    List<UtilityContract> findByBrokerAndLogType(@Param("broker") Broker broker, @Param("logType") String logType, @Param("utilityType") String utilityType);

    @Query("SELECT count(*) from UtilityContract u WHERE u.broker=(:broker) and u.logType=(:logType) and u.utilityType=(:utilityType)")
    int countByBrokerAndLogTypeAndUtilityType(@Param("broker") Broker broker, @Param("logType") String logType, @Param("utilityType") String utilityType);

    @Query(nativeQuery = true, value = "SELECT * FROM utility_contract g WHERE g.log_type=?1 " +
            " and g.callback_date_time like (?2%) and g.broker_id=?3 ORDER BY g.callback_date_time ASC")
    List<UtilityContract> findByLogTypeAndBrokerAndCallbackDate(String logType,
                                                                String callbackDate,
                                                                Long broker);

    @Query("SELECT u FROM UtilityContract u WHERE u.logType='VERBAL'")
    List<UtilityContract> findAllVerbalUtilityContracts();
}
