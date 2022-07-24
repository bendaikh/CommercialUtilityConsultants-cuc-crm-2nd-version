package mycrm.controllers;

import mycrm.models.MyCallback;
import mycrm.services.MyService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class HomeControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private HomeController homeController;

    @Mock
    private MyService myService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    public void shouldReturnIndexPage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void shouldReturnAdminIndexPage() throws Exception {
        List<MyCallback> listOfCallbacks = new ArrayList<>();

        when(myService.getMyTodaysCallbacks()).thenReturn(listOfCallbacks);

        this.mockMvc.perform(get("/admin/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"))
                .andExpect(model().attribute("myCallbacks", listOfCallbacks));
    }

    @Test
    public void shouldReturnAdminPage() throws Exception {
        List<MyCallback> listOfCallbacks = new ArrayList<>();

        when(myService.getMyTodaysCallbacks()).thenReturn(listOfCallbacks);

        this.mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"))
                .andExpect(model().attribute("myCallbacks", listOfCallbacks));
    }

}
