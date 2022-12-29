package mycrm.repositories;

import mycrm.models.Document;
import mycrm.models.MerchantServicesDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantDocumentRepository extends JpaRepository<MerchantServicesDocuments, Long> {
}
