package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.BrokerDocument;
import mycrm.repositories.BrokerDocumentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BrokerDocumentServiceJpaImplTest {

    @InjectMocks
    private BrokerDocumentServiceJpaImpl brokerDocumentServiceJpaImpl;

    @Mock
    private BrokerDocument mockedBrokerDocument;

    @Mock
    private BrokerDocumentRepository mockedBrokerDocumentRepo;
    
//  ReflectionTestUtils.setField(brokerDocumentController, "fileLocation", "C:\\cecfiles\\broker\\");

    @Test
    public void shouldReturnListOfAllBrokerDocument() {

        List<BrokerDocument> brokerDocuments = new ArrayList<>();
        brokerDocuments.add(new BrokerDocument());

        when(mockedBrokerDocumentRepo.findAll()).thenReturn(brokerDocuments);

        List<BrokerDocument> findAll = brokerDocumentServiceJpaImpl.findAll();

        assertEquals(brokerDocuments, findAll);
    }

    @Test
    public void shouldReturnBrokerDocumentById() {
        Broker broker = new Broker();
        
        BrokerDocument brokerDocument = new BrokerDocument();
        brokerDocument.setId(Long.valueOf(6));
        brokerDocument.setVersion(1);
        brokerDocument.setBroker(broker);
        brokerDocument.setFileName("filename");
        brokerDocument.setFileTitle("filetitle");
        brokerDocument.setFilePath("filepath");

        when(mockedBrokerDocumentRepo.findOne(Long.valueOf(6))).thenReturn(brokerDocument);

        BrokerDocument findById = brokerDocumentServiceJpaImpl.findById(Long.valueOf(6));

        assertEquals(brokerDocument, findById);
        assertEquals(brokerDocument.getId(), findById.getId());
        assertEquals(brokerDocument.getVersion(), findById.getVersion());
        assertEquals(brokerDocument.getBroker(), findById.getBroker());
        assertEquals(brokerDocument.getFileName(), findById.getFileName());
        assertEquals(brokerDocument.getFileTitle(), findById.getFileTitle());
        assertEquals(brokerDocument.getFilePath(), findById.getFilePath());
    }

    @Test
    public void shouldCreateBrokerDocument() {

        BrokerDocument brokerDocument = new BrokerDocument();
        brokerDocument.setId(Long.valueOf(1));

        when(mockedBrokerDocumentRepo.save(brokerDocument)).thenReturn(brokerDocument);

        BrokerDocument createdDocument = brokerDocumentServiceJpaImpl.create(brokerDocument);

        assertEquals(brokerDocument, createdDocument);
    }

    @Test
    public void shouldEditBrokerDocument() {

        BrokerDocument brokerDocument = new BrokerDocument();
        brokerDocument.setId(Long.valueOf(1));

        when(mockedBrokerDocumentRepo.save(brokerDocument)).thenReturn(brokerDocument);

        BrokerDocument createdDocument = brokerDocumentServiceJpaImpl.edit(brokerDocument);

        assertEquals(brokerDocument, createdDocument);
    }
    
    @Test
    public void shouldDeleteBrokerDocument() {

        brokerDocumentServiceJpaImpl.deleteById(Long.valueOf(152));

        verify(mockedBrokerDocumentRepo, times(1)).delete(Long.valueOf(152));
    }

    @Test
    public void shouldFindBrokerDocumentByBrokerId() {
        Broker broker = new Broker();
        broker.setId(2l);

        BrokerDocument brokerDocument = new BrokerDocument();
        brokerDocument.setBroker(broker);

        List<BrokerDocument> brokerDocuments = new ArrayList<>();
        brokerDocuments.add(brokerDocument);

        when(mockedBrokerDocumentRepo.findByBroker(any(Broker.class))).thenReturn(brokerDocuments);

        List<BrokerDocument> findByBroker = brokerDocumentServiceJpaImpl.findByBroker(broker);

        assertEquals(brokerDocuments, findByBroker);
    }

}
