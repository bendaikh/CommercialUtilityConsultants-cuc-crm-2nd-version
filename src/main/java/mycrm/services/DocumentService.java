package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.Document;
import mycrm.models.DocumentFolder;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface DocumentService {

    List<Document> findAll();

    Document findById(Long id);

    Document save(Document document);

    Document edit(Document document);

    List<Document> findByCustomer(Customer customer);

    List<Document> findByCustomerAndDirectory(Customer customer, Long folder);

    void deleteById(Long id);

    void storeFile(MultipartFile file, Document document);

    Resource loadAsResource(Document document);

    void deleteByCustomer(Customer customer);

    int getNumberOfDocumentsInDocumentFolder(DocumentFolder documentFolder);

    Document updateDocument(Long id, String fileTitle, Long documentFolder, Date validFrom, Date validTo, String documentStatus);

    List<Document> findDocumentsExpiringWithinAMonth();
}
