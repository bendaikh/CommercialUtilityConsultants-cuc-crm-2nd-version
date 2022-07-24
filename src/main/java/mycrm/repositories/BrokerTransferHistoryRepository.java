package mycrm.repositories;

import mycrm.models.BrokerTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrokerTransferHistoryRepository extends JpaRepository<BrokerTransferHistory, Long> {

    @Query(nativeQuery = true, value = "SELECT * from broker_transfer_history b WHERE b.contract_id=?1 and b.supply_type=?2 ORDER BY b.date_created DESC")
    List<BrokerTransferHistory> findBrokerTransferHistoryOrderByDateCreatedDesc(Long contractId, String supplyType);
}
