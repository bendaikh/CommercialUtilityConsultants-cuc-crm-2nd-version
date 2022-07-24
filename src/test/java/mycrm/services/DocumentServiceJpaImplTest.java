package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.Document;
import mycrm.models.DocumentFolder;
import mycrm.repositories.DocumentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceJpaImplTest {

    @InjectMocks
    private DocumentServiceJpaImpl documentServiceJpaImpl;

    @Mock
    private Document mockedDocument;

    @Mock
    private DocumentRepository mockedDocumentRepository;

//  ReflectionTestUtils.setField(documentController, "fileLocation", "C:\\cecfiles\\broker\\");

    @Test
    public void shouldFindAllDocuments() {
        List<Document> documentList = new ArrayList<>();
        documentList.add(new Document());

        when(mockedDocumentRepository.findAll()).thenReturn(documentList);

        List<Document> findAll = documentServiceJpaImpl.findAll();

        assertEquals(documentList, findAll);
    }

    @Test
    public void shouldFindDocumentsById() {
        Customer customer = new Customer();

        Document document = new Document();
        document.setId(5L);
        document.setVersion(1);
        document.setCustomer(customer);
        document.setFileName("filename");
        document.setFileTitle("filetitle");
        document.setFilePath("filepath");

        when(mockedDocumentRepository.findOne(Long.valueOf(5))).thenReturn(document);

        Document findById = documentServiceJpaImpl.findById(5L);

        assertEquals(document, findById);
        assertEquals(document.getId(), findById.getId());
        assertEquals(document.getVersion(), findById.getVersion());
        assertEquals(document.getCustomer(), findById.getCustomer());
        assertEquals(document.getFileName(), findById.getFileName());
        assertEquals(document.getFileTitle(), findById.getFileTitle());
        assertEquals(document.getFilePath(), findById.getFilePath());
    }

    @Test
    public void shouldSaveDocuments() {
        Document document = new Document();
        document.setId(5L);

        when(mockedDocumentRepository.save(any(Document.class))).thenReturn(document);

        Document savedDocument = documentServiceJpaImpl.save(document);

        assertEquals(document, savedDocument);
    }

    @Test
    public void shouldEditAndSaveDocuments() {
        Document document = new Document();
        document.setId(5L);

        when(mockedDocumentRepository.save(any(Document.class))).thenReturn(document);

        Document savedDocument = documentServiceJpaImpl.edit(document);

        assertEquals(document, savedDocument);
    }

    @Test
    public void shouldDeleteDocuments() {
        documentServiceJpaImpl.deleteById(541L);

        verify(mockedDocumentRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldReturnListOfDocumentsByCustomer() {
        Document document = new Document();
        document.setId(54L);

        List<Document> documentList = new ArrayList<>();
        documentList.add(document);

        Customer customer = new Customer();
        customer.setId(21L);

        when(mockedDocumentRepository.findByCustomer(customer)).thenReturn(documentList);

        List<Document> returnedDocuments = documentServiceJpaImpl.findByCustomer(customer);

        assertEquals(documentList, returnedDocuments);
    }

    @Test
    public void shouldFindByCustomerAndDirectory() {
        Document document = new Document();
        document.setId(54L);
        document.setDocumentFolder(0L);

        List<Document> documentList = new ArrayList<>();
        documentList.add(document);

        Customer customer = new Customer();
        customer.setId(21L);

        when(mockedDocumentRepository.findByCustomerAndDirectory(any(Customer.class), any(Long.class))).thenReturn(documentList);

        List<Document> returnedDirectory = documentServiceJpaImpl.findByCustomerAndDirectory(customer, 0L);

        assertEquals(documentList.get(0).getDocumentFolder(), returnedDirectory.get(0).getDocumentFolder());
    }

    @Test
    public void shouldReturnNumberOfDocumentsInFolder() {
        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .folderName("Folder")
                .parentFolderId(0L)
                .build();

        when(mockedDocumentRepository.getNumberOfDocumentsInDocumentFolder(any(Long.class))).thenReturn(0);

        int numberOfDocumentsInDocumentFolder = documentServiceJpaImpl.getNumberOfDocumentsInDocumentFolder(documentFolder);

        assertEquals(0, numberOfDocumentsInDocumentFolder);
    }

    @Test
    public void shouldUpdateDocument() {
        Document document = new Document();
        document.setId(2L);
        document.setFileTitle("Before File Title");
        document.setDocumentFolder(0L);

        Document savedDocument = new Document();
        savedDocument.setId(2L);
        savedDocument.setFileTitle("After File Title");
        savedDocument.setDocumentFolder(22L);

        when(mockedDocumentRepository.findOne(any(Long.class))).thenReturn(document);
        when(mockedDocumentRepository.save(any(Document.class))).thenReturn(savedDocument);

        Document updatedDocument = documentServiceJpaImpl.updateDocument(2L, "After File Title", 22L, new Date(2021, 1, 1), new Date(2022, 1, 1), "");

        assertEquals(22L, updatedDocument.getDocumentFolder());
        assertEquals("After File Title", updatedDocument.getFileTitle());
    }

    @Test
    public void shouldReturnOldDocumentWhenUpdateDocumentFailed() {
        Document document = new Document();
        document.setId(2L);
        document.setFileTitle("Before File Title");
        document.setDocumentFolder(0L);

        when(mockedDocumentRepository.findOne(any(Long.class))).thenReturn(document);
        when(mockedDocumentRepository.save(any(Document.class))).thenThrow(Exception.class);

        Document updatedDocument = documentServiceJpaImpl.updateDocument(2L, "After File Title", 22L, new Date(2021, 1, 1), new Date(2022, 1, 1), "");

        assertEquals(22L, updatedDocument.getDocumentFolder());
        assertEquals("After File Title", updatedDocument.getFileTitle());
    }
}
