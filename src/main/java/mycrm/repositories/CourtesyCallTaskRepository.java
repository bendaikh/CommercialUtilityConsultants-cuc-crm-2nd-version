package mycrm.repositories;

import mycrm.models.Broker;
import mycrm.models.CourtesyCallTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtesyCallTaskRepository extends PagingAndSortingRepository<CourtesyCallTask, Long> {

    Iterable<CourtesyCallTask> findByBroker(Broker broker);

    Page<CourtesyCallTask> findByBroker(Broker broker, Pageable pageable);
}
