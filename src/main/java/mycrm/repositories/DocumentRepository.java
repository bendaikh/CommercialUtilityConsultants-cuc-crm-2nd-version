package mycrm.repositories;

import mycrm.models.Customer;
import mycrm.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d from Document d WHERE customer=(:customer) ORDER BY dateCreated DESC")
    List<Document> findByCustomer(@Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM Document d WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);

    @Query("SELECT d from Document d WHERE customer=(:customer) and documentFolder=(:folder) ORDER BY dateCreated DESC")
    List<Document> findByCustomerAndDirectory(@Param("customer") Customer customer, @Param("folder") Long folder);

    @Query("SELECT count(*) from Document d where d.documentFolder=(:id)")
    int getNumberOfDocumentsInDocumentFolder(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT * from documents WHERE document_status = '' " +
            "AND valid_to IS NOT NULL AND valid_to <= DATE_ADD(NOW(), INTERVAL 1 MONTH) ORDER BY valid_to ASC")
    List<Document> findDocumentsExpiringWithinAMonth();
}
