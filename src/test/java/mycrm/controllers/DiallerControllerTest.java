package mycrm.controllers;

import mycrm.models.Plugin;
import mycrm.services.DiallerExportService;
import mycrm.services.PluginService;
import mycrm.upload.DiallerDataset;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class DiallerControllerTest {

    @InjectMocks
    private DiallerController diallerController;

    @Mock
    private DiallerExportService mockDiallerExportService;

    @Mock
    private PluginService mockPluginService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(diallerController).build();
    }

    @Test
    public void shouldReturnDataExportView() throws Exception {
        mockMvc.perform(get("/admin/data/export"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/data/export"));
    }

    @Test
    public void shouldReturnGetAllDiallerData() throws Exception {
        mockMvc.perform(get("/runDialler"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/data/export"));
    }

    @Test
    public void shouldReturnGetLatestDiallerData() throws Exception {
        mockMvc.perform(get("/runLatestDialler"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/data/export"));
    }

    @Test
    public void shouldReturnEditDiallerConfig() throws Exception {

        Plugin plugin = new Plugin();
        plugin.setPrimaryDataset("01");

        List<DiallerDataset> datasetList = new ArrayList<>();

        when(mockPluginService.getPluginByApi(any(String.class))).thenReturn(plugin);

        when(mockDiallerExportService.getDatasets()).thenReturn(datasetList);

        mockMvc.perform(get("/admin/data/edit-dialler-config"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("datasets", datasetList))
                .andExpect(model().attribute("primaryDataset", plugin.getPrimaryDataset()))
                .andExpect(view().name("admin/data/edit-dialler-config"));
    }

    @Test
    public void shouldReturnUpdateDataset() throws Exception {

        mockMvc.perform(get("/admin/data/update-dataset/14/LIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/data/edit-dialler-config"));

        verify(mockDiallerExportService, times(1)).updateDataset("14", "LIVE");
    }

    @Test
    public void shouldReturnUpdatePrimaryDataset() throws Exception {
        ReflectionTestUtils.setField(diallerController, "diallerProviderName", "8x8 Dialler API");

        mockMvc.perform(get("/admin/data/update-primary-dataset/14"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/data/edit-dialler-config"));

        verify(mockPluginService, times(1)).updatePrimaryDataset("8x8 Dialler API", "14");
    }
}
