package mycrm.repositories;

import mycrm.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MerchantServicesRepository extends JpaRepository<MerchantServicesContract, Long> {

    String ADMIN_CONTRACT_NEW_SALES_TASKS_QUERY = "SELECT * FROM merchant_services_contract uc WHERE (uc.welcome_pack_sent_to_customer = 0 OR uc.previous_supply_terminated = 0) " +
            "AND (uc.log_type = 'LIVE' OR uc.log_type = 'SOLD') AND uc.start_date IS NOT NULL";
    String CURRENT_SUPPLY_TERMINATED_QUERY = "SELECT * FROM merchant_services_contract uc WHERE uc.current_supply_terminated = 0 AND uc.end_date >= DATE_SUB(now(), INTERVAL " +
            "4 MONTH) AND uc.end_date <= DATE_ADD(now(), INTERVAL 1 MONTH) AND uc.log_type = 'LIVE'";
    @Query(value = "SELECT * FROM merchant_services_contract m WHERE m.customer_site_id = ?1 ORDER BY m.log_type = 'LIVE' DESC, m.id DESC", nativeQuery = true)
    List<MerchantServicesContract> findLatestMerchantServicesContractByCustomerSite(Long id);

    @Query("SELECT m FROM MerchantServicesContract m WHERE m.customerSite.customer=(:customer)")
    List<MerchantServicesContract> findByCustomerOrderByEndDateDesc(@Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM MerchantServicesContract m WHERE customerSite=(:customerSite)")
    void deleteByCustomerSite(@Param("customerSite") CustomerSite customerSite);

    @Query("SELECT m FROM MerchantServicesContract m WHERE customerSite.customer=(:customer)")
    List<MerchantServicesContract> findByCustomer(@Param("customer") Customer customer);

    @Query("SELECT m from MerchantServicesContract m WHERE m.broker=(:broker) and m.logType=(:logType)")
    List<MerchantServicesContract> findByBrokerAndLogType(@Param("broker") Broker broker, @Param("logType") String logType);

    @Query("SELECT count(*) from MerchantServicesContract m WHERE m.broker=(:broker) and m.logType=(:logType)")
    int countByBrokerAndLogType(@Param("broker") Broker broker, @Param("logType") String logType);

    @Query("SELECT u FROM MerchantServicesContract u WHERE u.logType='OBJECTED'")
    List<MerchantServicesContract> findAllObjectedMerchantServiceContracts();

    @Query("SELECT u FROM MerchantServicesContract u WHERE u.logType='VERBAL'")
    List<MerchantServicesContract> findAllVerbalMerchantServiceContracts();

    @Query(value = "SELECT * FROM merchant_services_contract uc WHERE (uc.contract_sent_to_customer = 0 OR uc.contract_received = 0) AND uc.log_type = 'PROCESSING'", nativeQuery = true)
    List<MerchantServicesContract> findAllAdminContractProcessingTasks();

    @Query(value = CURRENT_SUPPLY_TERMINATED_QUERY, nativeQuery = true)
    List<MerchantServicesContract> findAllMerchantServiceContractsToTerminate();

    @Query(value = ADMIN_CONTRACT_NEW_SALES_TASKS_QUERY, nativeQuery = true)
    List<MerchantServicesContract> findAllAdminMerchantServiceContractNewSalesTasks();
}
