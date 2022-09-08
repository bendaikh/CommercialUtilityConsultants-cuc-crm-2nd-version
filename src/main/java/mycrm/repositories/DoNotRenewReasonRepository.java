package mycrm.repositories;

import mycrm.models.DoNotRenewReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoNotRenewReasonRepository extends JpaRepository<DoNotRenewReason, Long> {

}
