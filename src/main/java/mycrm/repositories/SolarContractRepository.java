package mycrm.repositories;


import mycrm.models.BroadbandContract;
import mycrm.models.SolarContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolarContractRepository extends JpaRepository<SolarContract, Long> {
    @Query(value = "SELECT * FROM solar_contract m WHERE m.customer_site_id = ?1 ORDER BY m.log_type = 'LIVE' DESC, m.id DESC", nativeQuery = true)
    List<SolarContract> findLatestSolarContractByCustomerSite(Long id);
}
