package mycrm.repositories;

import mycrm.models.Customer;
import mycrm.models.LinkedAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LinkedAccountRepository extends JpaRepository<LinkedAccount, Long> {
    @Query("SELECT l FROM LinkedAccount l where customer=(:customer) OR linkedCustomer=(:customer)")
    List<LinkedAccount> findAllLinkedAccountsByCustomerReference(@Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM LinkedAccount cn WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM LinkedAccount cn WHERE linkedCustomer=(:customer)")
    void deleteByLinkedCustomer(@Param("customer") Customer customer);
}
