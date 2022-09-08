package mycrm.repositories;

import mycrm.models.VoipContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoipContractRepository extends JpaRepository<VoipContract, Long> {
}
