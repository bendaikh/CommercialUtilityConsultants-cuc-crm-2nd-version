package mycrm.controllers;

import mycrm.models.DocumentFolder;
import mycrm.models.EmailSetting;
import mycrm.models.Plugin;
import mycrm.services.DocumentFolderService;
import mycrm.services.DocumentService;
import mycrm.services.EmailSettingService;
import mycrm.services.PluginService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class SystemSettingControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private SystemSettingController systemSettingController;

    @Mock
    private PluginService mockPluginService;

    @Mock
    private EmailSettingService mockEmailSettingService;

    @Mock
    private DocumentFolderService mockDocumentFolderService;

    @Mock
    private DocumentService mockDocumentService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(systemSettingController).build();
    }

    @Test
    public void shouldReturnSettingsIndexPage() throws Exception {
        mockMvc.perform(get("/admin/system-setting/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/system-setting/index"));
    }

    @Test
    public void shouldReturnPluginsPage() throws Exception {

        List<Plugin> plugins = new ArrayList<>();
        plugins.add(new Plugin());

        when(mockPluginService.findAll()).thenReturn(plugins);

        mockMvc.perform(get("/admin/system-setting/plugins"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/system-setting/plugins"))
                .andExpect(model().attribute("plugins", plugins));
    }

    @Test
    public void shouldSavePlugin() throws Exception {

        MultiValueMap<String, String> plugin = new LinkedMultiValueMap<>();
        plugin.add("url", "http://ccc");
        plugin.add("api", "new api");


        mockMvc.perform(post("/plugin").params(plugin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-setting/plugins"));

        verify(mockPluginService, times(1)).save(any(Plugin.class));
    }

    @Test
    public void shouldNotSavePluginWhenError() throws Exception {

        MultiValueMap<String, String> plugin = new LinkedMultiValueMap<>();
        plugin.add("url", "http://ccc");
        plugin.add("api", "new api");

        when(mockPluginService.save(any(Plugin.class))).thenThrow(Exception.class);

        mockMvc.perform(post("/plugin").params(plugin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-setting/plugins"));
    }

    @Test
    public void shouldReturnPluginEditPage() throws Exception {

        Plugin plugin = new Plugin();
        plugin.setId(65L);

        when(mockPluginService.findById(any(Long.class))).thenReturn(plugin);

        mockMvc.perform(get("/admin/system-setting/plugin/edit-plugin/65"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/system-setting/edit-plugin"))
                .andExpect(model().attribute("plugin", plugin));
    }

    @Test
    public void shouldReturnEmailSettingIndex() throws Exception {

        List<EmailSetting> emailSettingsList = new ArrayList<>();

        when(mockEmailSettingService.findAll()).thenReturn(emailSettingsList);

        mockMvc.perform(get("/admin/system-setting/email-settings/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/system-setting/email-settings/index"))
                .andExpect(model().attribute("emailSettingsList", emailSettingsList));
    }

    @Test
    public void shouldReturnEmailSettingsEditPage() throws Exception {

        EmailSetting emailSetting = new EmailSetting();
        when(mockEmailSettingService.findById(any(Long.class))).thenReturn(emailSetting);

        mockMvc.perform(get("/admin/system-setting/email-settings/edit/65"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/system-setting/email-settings/edit"))
                .andExpect(model().attribute("emailSetting", emailSetting));
    }

    @Test
    public void shouldSaveEmailSetting() throws Exception {

        MultiValueMap<String, String> emailSetting = new LinkedMultiValueMap<>();
        emailSetting.add("url", "http://ccc");
        emailSetting.add("api", "new api");


        mockMvc.perform(post("/admin/system-setting/email-settings/save").params(emailSetting))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-setting/email-settings/index"));

        verify(mockEmailSettingService, times(1)).save(any(EmailSetting.class));
    }

    @Test
    public void shouldNotSaveEmailSettingPluginWhenError() throws Exception {

        MultiValueMap<String, String> plugin = new LinkedMultiValueMap<>();
        plugin.add("id", "61");
        plugin.add("url", "http://ccc");
        plugin.add("api", "new api");

        when(mockEmailSettingService.save(any(EmailSetting.class))).thenThrow(Exception.class);

        mockMvc.perform(post("/admin/system-setting/email-settings/save").params(plugin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-setting/email-settings/edit/61"));
    }

    @Test
    public void shouldReturnTheSaveFolderPage() throws Exception {

        MultiValueMap<String, String> documentFolder = new LinkedMultiValueMap<>();
        documentFolder.add("id", "61");
        documentFolder.add("folderName", "Name");
        documentFolder.add("parentFolderId", "0");

        mockMvc.perform(post("/admin/system-setting/document-settings/save-folder").params(documentFolder))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-setting/document-settings/index"));

        verify(mockDocumentFolderService, times(1)).save(any(DocumentFolder.class));
    }

    @Test
    public void shouldReturnTheSaveSubFolderPage() throws Exception {

        MultiValueMap<String, String> subfolder = new LinkedMultiValueMap<>();
        subfolder.add("subfolderName", "SubFolderName");
        subfolder.add("parentId", "1");

        mockMvc.perform(post("/admin/system-setting/document-settings/save-subfolder").params(subfolder))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-setting/document-settings/index"));

        verify(mockDocumentFolderService, times(1))
                .saveSubfolder(any(Long.class), any(String.class));
    }

    @Test
    public void shouldReturnFolderPage() throws Exception {

        mockMvc.perform(get("/admin/system-setting/document-settings/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/system-setting/document-settings/index"));
    }

    @Test
    public void shouldReturnEditDocumentFolderPage() throws Exception {

        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .id(25L)
                .folderName("Folder name")
                .parentFolderId(0L)
                .build();

        List<DocumentFolder> listOfParents = new ArrayList<>();
        listOfParents.add(documentFolder);

        when(mockDocumentFolderService.findById(any(Long.class))).thenReturn(documentFolder);
        when(mockDocumentService.getNumberOfDocumentsInDocumentFolder(any(DocumentFolder.class))).thenReturn(5);
        when(mockDocumentFolderService.findParentFolders()).thenReturn(listOfParents);

        mockMvc.perform(get("/admin/system-setting/document-settings/edit/25"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("numberOfDocuments", 5))
                .andExpect(model().attribute("parentFolders", listOfParents))
                .andExpect(model().attribute("documentFolder", documentFolder))
                .andExpect(model().attribute("hasSubFolders", false))
                .andExpect(view().name("admin/system-setting/document-settings/edit"));
    }

    @Test
    public void shouldDeleteFolder() throws Exception {
        mockMvc.perform(get("/admin/system-setting/document-settings/delete-folder/25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-setting/document-settings/index"));

        verify(mockDocumentFolderService, times(1)).deleteFolder(any(Long.class));
    }
}
