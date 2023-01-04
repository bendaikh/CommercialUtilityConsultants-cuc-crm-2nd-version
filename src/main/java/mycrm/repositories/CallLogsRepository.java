package mycrm.repositories;

import mycrm.models.CallLogs;
import mycrm.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallLogsRepository extends JpaRepository<CallLogs,Long> {


    List<CallLogs> findCallLogsByCustomer(Customer customer);

}
