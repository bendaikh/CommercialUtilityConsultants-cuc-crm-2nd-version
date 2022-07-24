package mycrm.repositories;

import mycrm.models.LogTypeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogTypeHistoryRepository extends JpaRepository<LogTypeHistory, Long> {
}
