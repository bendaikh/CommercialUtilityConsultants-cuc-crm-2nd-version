package mycrm.repositories;

import mycrm.upload.DiallerExportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiallerExportRepository extends JpaRepository<DiallerExportEntity, Long> {

    @Query(value = "SELECT * FROM dialler_export WHERE unique_id= ?1 AND contract_type= ?2 " +
            "AND lead_status= ?3 AND phone= ?4 AND mobile= ?5 AND dataset= ?6 LIMIT 1", nativeQuery = true)
    DiallerExportEntity filterExistingDiallerRecords(
            String uniqueId,
            String contractType,
            String leadStatus,
            String phone,
            String mobile,
            String dataset);
}
