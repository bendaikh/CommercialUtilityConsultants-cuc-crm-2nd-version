package mycrm.repositories;

import mycrm.models.TpsCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TpsCheckRepository extends JpaRepository<TpsCheck, Long> {
    List<TpsCheck> findOkChecksByNumber(String number);

    @Query(value = "SELECT t.* FROM tps_check t WHERE t.number= ?1 AND t.status_code='200' ORDER BY t.date_created DESC LIMIT 1", nativeQuery = true)
    TpsCheck findLatestTpsCheck(String number);
}
