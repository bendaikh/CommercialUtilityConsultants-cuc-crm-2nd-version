package mycrm.repositories;

import mycrm.upload.DiallerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiallerResponseRepository extends JpaRepository<DiallerResponse, Long> {
}
