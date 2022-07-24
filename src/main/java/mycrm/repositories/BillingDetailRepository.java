package mycrm.repositories;

import mycrm.models.BillingDetail;
import mycrm.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BillingDetailRepository extends JpaRepository<BillingDetail, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM BillingDetail cn WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);
}
