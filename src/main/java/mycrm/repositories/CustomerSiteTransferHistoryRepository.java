package mycrm.repositories;

import mycrm.models.CustomerSiteTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerSiteTransferHistoryRepository extends JpaRepository<CustomerSiteTransferHistory, Long> {

    @Query("SELECT c from CustomerSiteTransferHistory c where c.customerSiteId=(:id) order by c.dateCreated DESC")
    List<CustomerSiteTransferHistory> findCustomerSiteTransferHistoryByCustomerSite(@Param("id") Long id);
}
