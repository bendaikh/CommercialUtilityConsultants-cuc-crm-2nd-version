package mycrm.repositories;

import mycrm.models.Contact;
import mycrm.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT cn FROM Contact cn WHERE customer=(:customer)")
    List<Contact> findByCustomer(@Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("DELETE FROM Contact c WHERE customer=(:customer)")
    void deleteByCustomer(@Param("customer") Customer customer);
}
