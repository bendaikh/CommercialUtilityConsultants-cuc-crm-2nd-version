package mycrm.repositories;

import mycrm.models.BroadbandContract;
import mycrm.models.LandlineContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BroadbandContractRepository extends JpaRepository<BroadbandContract, Long> {

    @Query(value = "SELECT * FROM broadband_contract m WHERE m.customer_site_id = ?1 ORDER BY m.log_type = 'LIVE' DESC, m.id DESC", nativeQuery = true)
    List<BroadbandContract> findLatestBroadbandContractByCustomerSite(Long id);
}
