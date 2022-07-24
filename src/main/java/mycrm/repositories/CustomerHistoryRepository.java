package mycrm.repositories;

import mycrm.audit.history.CustomerHistory;
import mycrm.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerHistoryRepository extends JpaRepository<CustomerHistory, Long> {
    List<CustomerHistory> findAllByCustomer(Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerHistory c WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);
}
