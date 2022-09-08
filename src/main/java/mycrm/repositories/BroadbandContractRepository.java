package mycrm.repositories;

import mycrm.models.BroadbandContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadbandContractRepository extends JpaRepository<BroadbandContract, Long> {

}
