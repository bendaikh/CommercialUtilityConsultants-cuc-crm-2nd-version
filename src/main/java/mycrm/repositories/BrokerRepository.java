package mycrm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mycrm.models.Broker;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Long> {
    //@Query("SELECT b FROM Broker b LEFT JOIN FETCH b.createdBy ORDER BY b.dateCreated DESC")
    //List<Broker>findLatest5Posts(Pageable pageable);    
}
