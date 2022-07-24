package mycrm.repositories;

import mycrm.models.Customer;
import mycrm.models.CustomerNote;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.User;
import mycrm.models.UtilityContract;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerNoteRepository extends JpaRepository<CustomerNote, Long> {
    @Query("SELECT cn FROM CustomerNote cn LEFT JOIN FETCH cn.createdBy ORDER BY cn.dateCreated DESC")
    List<CustomerNote> findLatest5Posts(Pageable pageable);

    @Query("SELECT cn FROM CustomerNote cn WHERE customer=(:customer)")
    List<CustomerNote> findByCustomer(@Param("customer") Customer customer);

    @Query("SELECT cn FROM CustomerNote cn WHERE taggedUser=(:taggedUser) and completed!=1")
    List<CustomerNote> findAllIncompleteByTaggedUser(@Param("taggedUser") User user);

    @Query("SELECT cn FROM CustomerNote cn WHERE taggedUser=(:taggedUser) and completed!=1 ORDER BY cn.dueByDate ASC NULLS LAST")
    List<CustomerNote> findAllIncompleteByTaggedUserOrderByDueDateAsc(@Param("taggedUser") User user);

    @Query("SELECT cn FROM CustomerNote cn WHERE taggedUser=(:taggedUser)")
    List<CustomerNote> findByTaggedUser(@Param("taggedUser") User user);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE CustomerNote SET completed=1, dateCompleted=(:dateCompleted), completedBy=(:completedBy) WHERE id=(:id)")
    void updateCustomerNote(@Param("id") long id, @Param("dateCompleted") Date dateCompleted, @Param("completedBy") User user);

    List<CustomerNote> findByCustomerOrderByDateCreatedDesc(Customer customer);

    @Query("SELECT cn from CustomerNote cn WHERE cn.gasCustomerContract=(:gasCustomerContract) ORDER BY cn.dateCreated DESC")
    List<CustomerNote> findByGasCustomerContractOrderByDateCreatedDesc(@Param("gasCustomerContract") GasCustomerContract gasCustomerContract);

    @Query("SELECT cn from CustomerNote cn WHERE cn.elecCustomerContract=(:elecCustomerContract) ORDER BY cn.dateCreated DESC")
    List<CustomerNote> findByElecCustomerContractOrderByDateCreatedDesc(@Param("elecCustomerContract") ElecCustomerContract elecCustomerContract);

    @Query("SELECT cn from CustomerNote cn WHERE cn.gasCustomerContract=(:gasCustomerContract) ORDER BY cn.dateCreated DESC")
    List<CustomerNote> findTop3ByGasCustomerContractOrderByDateCreatedDesc(@Param("gasCustomerContract") GasCustomerContract gasCustomerContract, Pageable pageable);

    @Query("SELECT cn from CustomerNote cn WHERE cn.elecCustomerContract=(:elecCustomerContract) ORDER BY cn.dateCreated DESC")
    List<CustomerNote> findTopByElecCustomerContractOrderByDateCreatedDesc(@Param("elecCustomerContract") ElecCustomerContract elecCustomerContract, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerNote cn WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);

    @Query("SELECT cn from CustomerNote cn WHERE cn.utilityContract=(:contract) ORDER BY cn.dateCreated DESC")
    List<CustomerNote> findTopByUtilityContractOrderByDateCreatedDesc(@Param("contract") UtilityContract contract, Pageable pageable);

    @Query("SELECT cn from CustomerNote cn WHERE cn.utilityContract=(:utilityContract) ORDER BY cn.dateCreated DESC")
    List<CustomerNote> findByUtilityContractOrderByDateCreatedDesc(@Param("utilityContract") UtilityContract utilityContract);

    @Query("SELECT count(*) from CustomerNote cn WHERE taggedUser=(:taggedUser)")
    int countTaggedNotesByUser(@Param("taggedUser") User taggedUser);
}
