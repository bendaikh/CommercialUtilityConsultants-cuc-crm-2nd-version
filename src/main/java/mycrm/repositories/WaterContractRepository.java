package mycrm.repositories;

import mycrm.models.LandlineContract;
import mycrm.models.WaterContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterContractRepository extends JpaRepository<WaterContract, Long> {

}
