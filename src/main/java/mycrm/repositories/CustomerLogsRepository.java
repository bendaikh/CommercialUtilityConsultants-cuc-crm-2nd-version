package mycrm.repositories;

import mycrm.models.CallLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLogsRepository extends JpaRepository<CallLogs,Long> {

}
