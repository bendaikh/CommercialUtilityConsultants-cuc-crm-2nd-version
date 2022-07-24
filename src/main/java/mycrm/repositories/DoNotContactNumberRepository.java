package mycrm.repositories;

import mycrm.models.DoNotContactNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoNotContactNumberRepository extends JpaRepository<DoNotContactNumber, Long> {

    @Query(value = "SELECT d.* FROM do_not_contact_numbers d WHERE d.number= ?1 AND d.number!=''", nativeQuery = true)
    List<DoNotContactNumber> findByNumber(String number);
}
