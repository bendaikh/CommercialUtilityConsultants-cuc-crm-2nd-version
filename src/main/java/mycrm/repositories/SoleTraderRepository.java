package mycrm.repositories;

import mycrm.models.SoleTrader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoleTraderRepository extends JpaRepository<SoleTrader,Long> {

}
