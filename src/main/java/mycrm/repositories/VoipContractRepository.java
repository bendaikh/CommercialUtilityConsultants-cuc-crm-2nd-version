package mycrm.repositories;

import mycrm.models.LandlineContract;
import mycrm.models.VoipContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoipContractRepository extends JpaRepository<VoipContract, Long> {
    @Query(value = "SELECT * FROM voip_contract m WHERE m.customer_site_id = ?1 ORDER BY m.log_type = 'LIVE' DESC, m.id DESC", nativeQuery = true)
    List<VoipContract> findLatestVoipContractByCustomerSite(Long id);

}
