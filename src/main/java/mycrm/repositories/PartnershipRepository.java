package mycrm.repositories;

import mycrm.models.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnershipRepository extends JpaRepository<Partnership, Long> {

}
