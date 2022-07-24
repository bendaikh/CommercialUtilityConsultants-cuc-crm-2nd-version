package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.BrokerDocument;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BrokerDocumentService {
    
    List<BrokerDocument> findAll();
    BrokerDocument findById(Long id);
    BrokerDocument create(BrokerDocument document);
    BrokerDocument edit(BrokerDocument document);
    List<BrokerDocument> findByBroker(Broker broker);

    void deleteById(Long id);
    
    void storeFile(MultipartFile file, BrokerDocument brokerDocument);
    Resource loadAsResource(BrokerDocument brokerDocument);    
}
