package mycrm.repositories;

import mycrm.models.LandlineContract;
import mycrm.models.UtilityContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandlineContractRepository extends JpaRepository<LandlineContract, Long> {
}
