package mycrm.repositories;

import mycrm.models.ContractReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractReasonRepository extends JpaRepository<ContractReason, Long> {
}
