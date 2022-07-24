package mycrm.repositories;

import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerSiteRepository extends JpaRepository<CustomerSite, Long> {
    @Query("SELECT cs FROM CustomerSite cs LEFT JOIN FETCH cs.createdBy ORDER BY cs.dateCreated DESC")
    List<CustomerSite>findLatest5Posts(Pageable pageable);    
    
    @Query("SELECT cs FROM CustomerSite cs WHERE customer=(:customer)")
    List<CustomerSite>findByCustomer(@Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerSite c WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);
}
