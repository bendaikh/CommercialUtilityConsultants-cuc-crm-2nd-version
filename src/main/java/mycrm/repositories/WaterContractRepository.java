package mycrm.repositories;

import mycrm.models.LandlineContract;
import mycrm.models.VoipContract;
import mycrm.models.WaterContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterContractRepository extends JpaRepository<WaterContract, Long> {
    @Query(value = "SELECT * FROM water_contract m WHERE m.customer_site_id = ?1 ORDER BY m.log_type = 'LIVE' DESC, m.id DESC", nativeQuery = true)
    List<WaterContract> findLatestWaterContractByCustomerSite(Long id);
}
