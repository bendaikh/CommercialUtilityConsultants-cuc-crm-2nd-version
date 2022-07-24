package mycrm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mycrm.models.Broker;
import mycrm.models.BrokerDocument;

@Repository
public interface BrokerDocumentRepository extends JpaRepository<BrokerDocument, Long> {
	
	@Query("SELECT d from BrokerDocument d WHERE broker=(:broker) ORDER BY dateCreated DESC")
	List<BrokerDocument> findByBroker(@Param("broker") Broker broker);
     
}
