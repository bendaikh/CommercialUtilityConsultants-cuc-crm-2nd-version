package mycrm.repositories;


import mycrm.models.MobileContract;
import mycrm.models.SolarContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileContractRepository extends JpaRepository<MobileContract,Long> {
    @Query(value = "SELECT * FROM mobile_contract m WHERE m.customer_site_id = ?1 ORDER BY m.log_type = 'LIVE' DESC, m.id DESC", nativeQuery = true)
    List<MobileContract> findLatestMobileContractByCustomerSite(Long id);
}
