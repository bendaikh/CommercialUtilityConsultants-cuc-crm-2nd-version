package mycrm.controllers;

import mycrm.services.UploadService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UploadControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private UploadController uploadController;

    @Mock
    private UploadService mockUploadService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(uploadController).build();
    }

    @Test
    public void shouldReturnDataUploadView() throws Exception {

        mockMvc.perform(get("/admin/data/upload"))
                .andExpect(view().name("admin/data/upload"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUploadFileView() throws Exception {
        MockMultipartFile csv = new MockMultipartFile("xxx", "", "application/text", "{\"json\": \"someValue\"}".getBytes());

        when(mockUploadService.uploadFile(any(MultipartFile.class))).thenReturn("http://link.to.me");

        mockMvc.perform(fileUpload("/uploadCustomers")
                .file("uploadDataFile", csv.getBytes()))
                .andExpect(view().name("admin/data/importStatus"))
                .andExpect(model().attribute("unsuccessfulImportsLink", "http://link.to.me"))
                .andExpect(status().isOk());
    }
}
