package mycrm.controllers;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class BroadbandControllerTest {

    @InjectMocks
    private BroadbandController broadbandController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(broadbandController).build();
    }

    @Test
    public void shouldNewBroadbandContract() throws Exception {

        mockMvc.perform(get("/admin/customer/broadband/6"))
                .andExpect(view().name("admin/customer/broadband"))
                .andExpect(status().isOk());
    }
}