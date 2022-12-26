package mycrm.repositories;

import mycrm.models.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.createdBy ORDER BY c.dateCreated DESC")
    List<Customer> findLatest5(Pageable pageable);

    List<Customer> findByBusinessName(String businessName, Pageable pageable);

    Integer countByBusinessName(String q);

    @Query("SELECT c FROM Customer c WHERE businessName=(:businessName) AND businessPostcodeOut=(:businessPostcodeOut) AND businessPostcodeIn=(:businessPostcodeIn)")
    Customer findByBusinessNameAndBusinessPostcodeOutAndBusinessPostcodeIn(
            @Param("businessName") String businessName,
            @Param("businessPostcodeOut") String businessPostcodeOut,
            @Param("businessPostcodeIn") String businessPostcodeIn);

    @Query("SELECT c FROM Customer c WHERE customerReference=(:customerReference)")
    Customer findByCustomerReference(@Param("customerReference") String customerReference);

    @Query(value = "SELECT c FROM Customer c WHERE customerReference=(:inputString) OR businessName=(:inputString) OR businessAddr=(:inputString) OR businessCity=(:inputString) OR firstName=(:inputString) OR lastName=(:inputString)"
    )
    List<Customer> findAllByInputString(@Param("inputString") String inputString);
}
