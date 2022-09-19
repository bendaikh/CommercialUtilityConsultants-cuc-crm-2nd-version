package mycrm.repositories;

import mycrm.models.LandlineContract;
import mycrm.models.MerchantServicesContract;
import mycrm.models.UtilityContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandlineContractRepository extends JpaRepository<LandlineContract, Long> {
    @Query(value = "SELECT * FROM landline_contract m WHERE m.customer_site_id = ?1 ORDER BY m.log_type = 'LIVE' DESC, m.id DESC", nativeQuery = true)
    List<LandlineContract> findLatestLandlineContractByCustomerSite(Long id);
}
