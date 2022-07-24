package mycrm.controllers;

import mycrm.models.Customer;
import mycrm.models.Document;
import mycrm.models.DocumentFolderList;
import mycrm.models.LinkedAccount;
import mycrm.services.ContactService;
import mycrm.services.CustomerService;
import mycrm.services.DocumentFolderService;
import mycrm.services.DocumentService;
import mycrm.services.LinkedAccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class DocumentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private DocumentController documentController;

    @Mock
    private DocumentService mockDocumentService;

    @Mock
    private CustomerService mockCustomerService;

    @Mock
    private ContactService mockContactService;

    @Mock
    private LinkedAccountService mockLinkedAccountService;

    @Mock
    private DocumentFolderService mockDocumentFolderService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
    }

    @Test
    public void shouldReturnCustomerDocumentsOnHomePage() throws Exception {
        Long id = 50L;

        Customer customer = new Customer("Mahmoods");

        Document document = new Document();
        document.setDocumentFolder(0);
        document.setCustomer(customer);
        document.setFileTitle("Hello");

        List<Document> documents = new ArrayList<>();
        documents.add(document);

        List<DocumentFolderList> folders = new ArrayList<>();

        when(mockCustomerService.findById(id)).thenReturn(customer);
        when(mockDocumentService.findByCustomerAndDirectory(customer, 0L)).thenReturn(documents);
        when(mockDocumentFolderService.documentsFolderList()).thenReturn(folders);

        mockMvc
                .perform(get("/admin/customer/customerdocuments/50"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/customerdocuments"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)))
                .andExpect(model().attribute("documents", hasSize(1)))
                .andExpect(model().attribute("folders", folders));
    }

    @Test
    public void shouldReturnCustomerDocumentsOnParticularFolder() throws Exception {
        Long id = 50L;

        Customer customer = new Customer("Mahmoods");

        Document document = new Document();
        document.setDocumentFolder(1);
        document.setCustomer(customer);
        document.setFileTitle("Hello");

        List<Document> documents = new ArrayList<>();
        documents.add(document);

        List<DocumentFolderList> folders = new ArrayList<>();

        when(mockCustomerService.findById(id)).thenReturn(customer);
        when(mockDocumentService.findByCustomerAndDirectory(any(Customer.class), any(Long.class))).thenReturn(documents);
        when(mockDocumentFolderService.documentsFolderList()).thenReturn(folders);

        mockMvc
                .perform(get("/admin/customer/customerdocuments/50/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/customerdocuments"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)))
                .andExpect(model().attribute("documents", hasSize(1)))
                .andExpect(model().attribute("folders", folders));
    }

    @Test
    public void shouldShowCustomerUploadDocumentHomePage() throws Exception {

        Customer customer = new Customer();

        when(mockCustomerService.findById(1L)).thenReturn(customer);

        mockMvc
                .perform(get("/admin/customer/uploaddocument/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", customer))
                .andExpect(view().name("admin/customer/uploaddocument"));
    }

    @Test
    public void shouldShowCustomerUploadDocumentFolderPage() throws Exception {

        Customer customer = new Customer();

        when(mockCustomerService.findById(1L)).thenReturn(customer);

        mockMvc
                .perform(get("/admin/customer/uploaddocument/1/2"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", customer))
                .andExpect(view().name("admin/customer/uploaddocument"));
    }

    @Test
    public void shouldUploadCustomerFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("uploadfile", "test.txt", "text/plain",
                "Spring Framework".getBytes());

        Customer customer = new Customer();
        customer.setId(43L);

        Document document = new Document();
        document.setCustomer(customer);
        document.setFileName(multipartFile.getOriginalFilename());
        document.setFilePath("tests");
        document.setFileTitle("testTitle");

        when(mockDocumentService.save(any(Document.class))).thenReturn(document);

        mockMvc
                .perform(fileUpload("/uploadFile")
                        .file(multipartFile)
                        .param("customer.id", "43")
                        .param("fileTitle", "test.txt")
                        .param("fileName", "test")
                        .param("filePath", "file/path"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/customerdocuments/43"));
    }
    
//    @Test
//    public void shouldReturnCustomerFile() throws Exception {
//
//        Document document = new Document();
//        document.setId(Long.valueOf(12));
//
//        when(mockDocumentService.findById(any(Long.class))).thenReturn(document);
//
//        Resource resource = new UrlResource("blah");
//
//        when(mockDocumentService.loadAsResource(any(Document.class))).thenReturn(resource);
//
//        mockMvc
//        .perform(get("/admin/customer/opendocument/12"))
//        .andExpect(status().is2xxSuccessful())
//        .andDo(print());
//    }

    @Test
    public void shouldShowEditDocumentPage() throws Exception {

        Customer customer = new Customer();
        customer.setId(1L);

        Document document = new Document();
        document.setCustomer(customer);
        document.setFileName("Name");
        document.setFilePath("tests");
        document.setFileTitle("testTitle");

        List<LinkedAccount> folders = new ArrayList<>();

        when(mockDocumentService.findById(any(Long.class))).thenReturn(document);

        when(mockLinkedAccountService.findByCustomer(any(Customer.class))).thenReturn(folders);

        mockMvc
                .perform(get("/admin/customer/edit-document/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("linkedAccountsSize", 0))
                .andExpect(model().attribute("folders", folders))
                .andExpect(model().attribute("customer", customer))
                .andExpect(model().attribute("document", document))
                .andExpect(view().name("admin/customer/edit-document"));
    }

    @Test
    public void shouldRedirectToAdminPageWhenEditDocumentNotFound() throws Exception {
        when(mockDocumentService.findById(any(Long.class))).thenReturn(null);

        mockMvc
                .perform(get("/admin/customer/edit-document/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/"));
    }

    @Test
    public void shouldSaveDocumentAndRedirect() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);

        Document document = new Document();
        document.setCustomer(customer);
        document.setFileName("Name");
        document.setFilePath("tests");
        document.setFileTitle("testTitle");

        when(mockDocumentService.updateDocument(any(Long.class), any(String.class), any(Long.class), any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(document);

        mockMvc
                .perform(post("/admin/customer/save-document")
                        .param("id", "1")
                        .param("fileTitle", "new title")
                        .param("documentFolder", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/customerdocuments/1/0"));
    }

    @Test
    public void shouldDeleteDocument() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);

        Document document = new Document();
        document.setCustomer(customer);
        document.setFileName("Name");
        document.setFilePath("tests");
        document.setFileTitle("testTitle");

        when(mockDocumentService.findById(any(Long.class))).thenReturn(document);

        mockMvc
                .perform(get("/admin/customer/deleteDocument/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/customer/customerdocuments/1"));

        verify(mockDocumentService, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void shouldNotDeleteDocumentWhenDocumentNotFound() throws Exception {
        when(mockDocumentService.findById(any(Long.class))).thenReturn(null);

        mockMvc
                .perform(get("/admin/customer/deleteDocument/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));

        verify(mockDocumentService, times(0)).deleteById(any(Long.class));
    }

}
