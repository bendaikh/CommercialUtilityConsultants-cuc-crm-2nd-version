package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.BrokerDocument;
import mycrm.repositories.BrokerDocumentRepository;
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
import java.util.List;

@Service
@Primary
public class BrokerDocumentServiceJpaImpl implements BrokerDocumentService {

    private final BrokerDocumentRepository brokerDocumentRepo;

    @Value("${broker.file.upload.location}")
    private String FILE_UPLOAD_LOCATION;

    @Autowired
    public BrokerDocumentServiceJpaImpl(BrokerDocumentRepository brokerDocumentRepo) {
        this.brokerDocumentRepo = brokerDocumentRepo;
    }

    @Override
    public List<BrokerDocument> findAll() {
        return this.brokerDocumentRepo.findAll();
    }

    @Override
    public BrokerDocument findById(Long id) {
        return this.brokerDocumentRepo.findOne(id);
    }

    @Override
    public BrokerDocument create(BrokerDocument brokerDocument) {
        return this.brokerDocumentRepo.save(brokerDocument);
    }

    @Override
    public BrokerDocument edit(BrokerDocument brokerDocument) {
        return this.brokerDocumentRepo.save(brokerDocument);
    }

    @Override
    public void deleteById(Long id) {
        this.brokerDocumentRepo.delete(id);
    }

    @Override
    public List<BrokerDocument> findByBroker(Broker broker) {
        return this.brokerDocumentRepo.findByBroker(broker);
    }

    @Override
    public void storeFile(MultipartFile file, BrokerDocument brokerDocument) {
        String filename = StringUtils.getFilename(file.getOriginalFilename());

        Path uploadLocation = Paths.get(FILE_UPLOAD_LOCATION + brokerDocument.getBroker().getId() + "\\");

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

                brokerDocument.setFileName(filename);
                brokerDocument.setFilePath(uploadLocation.toString());

                // save document details to database
                create(brokerDocument);

                Files.copy(inputStream, uploadLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Resource loadAsResource(BrokerDocument brokerDocument) {
        try {

            Path uploadLocation = Paths.get(brokerDocument.getFilePath());
            Path file = uploadLocation.resolve(brokerDocument.getFileName());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not find file: " + brokerDocument.getFileName());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + brokerDocument.getFileName(), e);
        }
    }

}
