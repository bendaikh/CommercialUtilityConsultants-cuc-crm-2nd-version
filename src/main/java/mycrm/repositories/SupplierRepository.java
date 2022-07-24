package mycrm.repositories;

import mycrm.models.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.createdBy ORDER BY s.businessName ASC")
    List<Supplier> findLatest5Posts(Pageable pageable);

    Supplier findByBusinessName(String gasSupplier);

    @Query("SELECT s FROM Supplier s ORDER BY s.businessName ASC")
    List<Supplier> findAllOrderByBusinessName();
}
