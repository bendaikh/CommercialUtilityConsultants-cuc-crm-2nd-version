package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.BrokerDocument;
import mycrm.services.BrokerDocumentService;
import mycrm.services.BrokerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class BrokerDocumentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BrokerDocumentController brokerDocumentController;

    @Mock
    private BrokerDocumentService mockBrokerDocumentService;

    @Mock
    private BrokerService mockBrokerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(brokerDocumentController).build();
    }

    @Test
    public void shouldShowBrokerUploadDocumentPage() throws Exception {

        Broker broker = new Broker();
        broker.setId(1l);

        when(mockBrokerService.findById(1l)).thenReturn(broker);

        mockMvc
                .perform(get("/admin/broker/uploaddocument/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("broker", broker))
                .andExpect(view().name("admin/broker/uploaddocument"));
    }

    @Test
    public void shouldUploadFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("uploadfile", "test.txt", "text/plain",
                "Spring Framework".getBytes());

        Broker broker = new Broker();
        broker.setId(43l);

        BrokerDocument brokerDocument = new BrokerDocument();
        brokerDocument.setBroker(broker);
        brokerDocument.setFileName(multipartFile.getOriginalFilename());
        brokerDocument.setFilePath("tests");
        brokerDocument.setFileTitle("testTitle");

//        ReflectionTestUtils.setField(brokerDocumentController, "fileLocation", "C:\\cecfiles\\broker\\");

        when(mockBrokerDocumentService.create(any(BrokerDocument.class))).thenReturn(brokerDocument);

        mockMvc
                .perform(fileUpload("/uploadBrokerFile")
                        .file(multipartFile)
                        .param("broker.id", "43")
                        .param("fileTitle", "test.txt")
                        .param("fileName", "test")
                        .param("filePath", "file/path"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/broker/brokerdocuments/43"));
    }

    // @Test(expected = IOException.class)
    // public void shouldThrowIOException() throws Exception {
    // MockMultipartFile multipartFile = new MockMultipartFile("uploadfile",
    // "gg", "text/plain", "".getBytes());
    //
    //
    // Files files = mock(Files.class);
    //
    // String directory = "filedirectory";
    //
    // //when(Paths.get(any(String.class))).thenThrow(IOException.class);
    // //when(Files.createDirectories(any(Paths.class))).
    //
    // //doThrow(IOException.class).when(files.createDirectories(Paths.get(directory)));
    //
    //
    // mockMvc
    // .perform(fileUpload("/uploadBrokerFile")
    // .file(multipartFile)
    // .param("broker.id", "43")
    // .param("fileTitle", "test.txt")
    // .param("fileName", "test")
    // .param("filePath", "file/path"))
    // .andExpect(status().is3xxRedirection())
    // .andExpect(redirectedUrl("/admin/broker/view/43"))
    // .andDo(print());
    // }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException() throws Exception {

        MockMultipartFile multipartFile = null;

        mockMvc
                .perform(fileUpload("/uploadBrokerFile")
                        .file(multipartFile)
                        .param("broker.id", "43")
                        .param("fileTitle", "test.txt")
                        .param("fileName", "test")
                        .param("filePath", "file/path"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/broker/view/43"));
    }
}
