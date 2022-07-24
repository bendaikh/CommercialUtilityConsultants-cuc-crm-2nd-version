package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.Document;
import mycrm.models.DocumentFolder;
import mycrm.repositories.DocumentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
@Primary
public class DocumentServiceJpaImpl implements DocumentService {

    private final DocumentRepository documentRepo;

    private static final Logger logger = LogManager.getLogger();

    @Value("${customer.file.upload.location}")
    private String FILE_UPLOAD_LOCATION;

    @Autowired
    public DocumentServiceJpaImpl(DocumentRepository documentRepo) {
        this.documentRepo = documentRepo;
    }

    @Override
    public List<Document> findAll() {
        return this.documentRepo.findAll();
    }

    @Override
    public Document findById(Long id) {
        return this.documentRepo.findOne(id);
    }

    @Override
    public Document save(Document document) {
        return this.documentRepo.save(document);
    }

    @Override
    public Document edit(Document document) {
        return this.documentRepo.save(document);
    }

    @Override
    public void deleteById(Long id) {
        this.documentRepo.delete(id);
    }

    @Override
    public List<Document> findByCustomer(Customer customer) {
        return this.documentRepo.findByCustomer(customer);
    }

    @Override
    public List<Document> findByCustomerAndDirectory(Customer customer, Long folder) {
        return this.documentRepo.findByCustomerAndDirectory(customer, folder);
    }

    @Override
    public void storeFile(MultipartFile file, Document document) {
        String filename = StringUtils.getFilename(file.getOriginalFilename());

        Path uploadLocation = Paths.get(FILE_UPLOAD_LOCATION + document.getCustomer().getId() + "\\");

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }

            // This is a security check
            if (filename.contains("..")) {
                throw new RuntimeException(
                        "Cannot store file with relative path outside current directory " + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.createDirectories(uploadLocation);

                document.setFileName(filename);
                document.setFilePath(uploadLocation.toString());

//                logger.info("Filename to upload: {}", filename);
//                logger.info("Upload location: {}", uploadLocation.toString());

                // save document details to database
                save(document);

                Files.copy(inputStream, uploadLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }

    }

    @Override
    public Resource loadAsResource(Document document) {
        try {

            Path uploadLocation = Paths.get(document.getFilePath());
            Path file = uploadLocation.resolve(document.getFileName());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not find file: " + document.getFileName());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + document.getFileName(), e);
        }
    }

    @Override
    public void deleteByCustomer(Customer customer) {
        this.documentRepo.deleteByCustomer(customer);
    }

    @Override
    public int getNumberOfDocumentsInDocumentFolder(DocumentFolder documentFolder) {
        if (documentFolder.getId() != null) {
            return this.documentRepo.getNumberOfDocumentsInDocumentFolder(documentFolder.getId());
        }
        return 0;
    }

    @Override
    public Document updateDocument(Long id, String fileTitle, Long documentFolder, Date validFrom, Date validTo, String documentStatus) {
        Document document = this.documentRepo.findOne(id);

        try {
            document.setDocumentFolder(documentFolder);
            document.setFileTitle(fileTitle);
            document.setValidFrom(validFrom);
            document.setValidTo(validTo);
            document.setDocumentStatus(documentStatus);
            return this.documentRepo.save(document);
        } catch (Exception e) {
            logger.info("Unable to update document");
            return document;
        }
    }

    @Override
    public List<Document> findDocumentsExpiringWithinAMonth() {
        return this.documentRepo.findDocumentsExpiringWithinAMonth();
    }
}
