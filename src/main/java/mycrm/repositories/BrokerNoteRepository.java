package mycrm.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mycrm.models.Broker;
import mycrm.models.BrokerNote;
import mycrm.models.User;

@Repository
public interface BrokerNoteRepository extends JpaRepository<BrokerNote, Long> {
    @Query("SELECT bn FROM BrokerNote bn LEFT JOIN FETCH bn.createdBy ORDER BY bn.dateCreated DESC")
    List<BrokerNote> findLatest5Posts(Pageable pageable);   
    
    @Query("SELECT bn FROM BrokerNote bn WHERE broker=(:broker)")
    List<BrokerNote> findByBroker(@Param("broker") Broker broker);
    
    @Query("SELECT bn FROM BrokerNote bn WHERE taggedUser=(:taggedUser) and completed!=1")
    List<BrokerNote> findAllIncompleteByTaggedUser(@Param("taggedUser") User user);
    
    @Query("SELECT bn FROM BrokerNote bn WHERE taggedUser=(:taggedUser)")
    List<BrokerNote> findAllByTaggedUser(@Param("taggedUser") User user);
    
    @Transactional
	@Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BrokerNote SET completed=1, dateCompleted=(:dateCompleted), completedBy=(:completedBy) WHERE id=(:id)")
	void updateBrokerNote(@Param("id") long id, @Param("dateCompleted") Date dateCompleted, @Param("completedBy") User user);
}
