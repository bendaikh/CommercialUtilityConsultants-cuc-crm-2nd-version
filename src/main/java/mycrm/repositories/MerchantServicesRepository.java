package mycrm.repositories;

import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.MerchantServicesContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MerchantServicesRepository extends JpaRepository<MerchantServicesContract, Long> {

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
}
