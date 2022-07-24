package mycrm.repositories;

import mycrm.models.Customer;
import mycrm.models.ElecCustomerContract;
import mycrm.models.EmailHistory;
import mycrm.models.GasCustomerContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long> {

    @Query("SELECT e FROM EmailHistory e WHERE e.emailAddress=(:emailAddress) and e.emailType=(:emailType) and e.gasCustomerContract=(:gasCustomerContract)" +
            " and e.process='AUTOMATED'")
    List<EmailHistory> checkGasRenewalEmailSentAutomatically(@Param("emailAddress") String emailAddress,
                                                             @Param("emailType") String emailType,
                                                             @Param("gasCustomerContract") GasCustomerContract gasCustomerContract);

    @Query("SELECT e FROM EmailHistory e WHERE e.emailAddress=(:emailAddress) and e.emailType=(:emailType) and e.elecCustomerContract=(:elecCustomerContract) " +
            " and e.process='AUTOMATED'")
    List<EmailHistory> checkElectricRenewalEmailSentAutomatically(@Param("emailAddress") String emailAddress,
                                                                  @Param("emailType") String emailType,
                                                                  @Param("elecCustomerContract") ElecCustomerContract elecCustomerContract);

    @Query("SELECT e FROM EmailHistory e WHERE e.customer=(:customer)")
    List<EmailHistory> findByCustomer(@Param("customer") Customer customer);

    @Query(value = "SELECT * FROM email_history WHERE customer_id=:customerId and date_created LIKE :dateCreated% and process='AUTOMATED'", nativeQuery = true)
    List<EmailHistory> findByCustomerAndDateCreated(@Param("customerId") String customerId, @Param("dateCreated") String dateCreated);

    @Query(value = "SELECT * FROM email_history WHERE customer_id=:customerId and gas_customer_contract_id IS NOT NULL and date_created LIKE :dateCreated% and " +
            "process='AUTOMATED'",
            nativeQuery = true)
    List<EmailHistory> findByGasContractCustomerAndDateCreated(@Param("customerId") String customerId,
                                                               @Param("dateCreated") String dateCreated);

    @Query(value = "SELECT * FROM email_history WHERE customer_id=:customerId and elec_customer_contract_id IS NOT NULL and date_created LIKE :dateCreated% and " +
            "process='AUTOMATED'",
            nativeQuery = true)
    List<EmailHistory> findByElectricContractCustomerAndDateCreated(@Param("customerId") String customerId,
                                                                    @Param("dateCreated") String dateCreated);
}
