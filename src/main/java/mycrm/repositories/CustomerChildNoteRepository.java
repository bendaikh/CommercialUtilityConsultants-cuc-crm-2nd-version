package mycrm.repositories;

import mycrm.models.Customer;
import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerChildNoteRepository extends JpaRepository<CustomerChildNote, Long> {
    List<CustomerChildNote> findByCustomerNoteOrderByDateCreatedAsc(CustomerNote customerNote);

    @Query("SELECT cn FROM CustomerChildNote cn WHERE customer=(:customer)")
    List<CustomerChildNote> findByCustomer(@Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerChildNote c WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);

    @Query("SELECT cn FROM CustomerChildNote cn WHERE replyTaggedUser=(:taggedUser) and replyCompleted!=1 ORDER BY cn.replyDueByDate ASC NULLS LAST")
    List<CustomerChildNote> findAllIncompleteByTaggedUserOrderByDueDateAsc(@Param("taggedUser") User taggedUser);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE CustomerChildNote SET replyCompleted=1, replyDateCompleted=(:dateCompleted), replyCompletedBy=(:completedBy) WHERE id=(:id)")
    void updateCustomerNote(@Param("id") long id, @Param("dateCompleted") Date dateCompleted, @Param("completedBy") User user);

    @Query("SELECT count(*) FROM CustomerChildNote cn WHERE replyTaggedUser=(:taggedUser)")
    int countTaggedChildNotesByUser(@Param("taggedUser") User taggedUser);

    @Query("SELECT cn FROM CustomerChildNote cn WHERE replyTaggedUser=(:taggedUser)")
    List<CustomerChildNote> findByTaggedUser(@Param("taggedUser") User taggedUser);
}
