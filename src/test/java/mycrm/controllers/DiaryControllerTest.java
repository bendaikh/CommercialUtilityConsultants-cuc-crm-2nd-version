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
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class DiaryControllerTest {

    @InjectMocks
    private DiaryController controller;

    @Mock
    private MyService myService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldReturnDiaryIndexPageWithSearchedDate() throws Exception {
        List<MyCallback> list = new ArrayList<>();

        when(myService.getCallbacksForDate(any(String.class))).thenReturn(list);

        mockMvc.perform(get("/admin/customer/diary/index?callbackSearchDate=2020-12-12"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/diary/index"))
                .andExpect(model().attribute("callbacksMap", list))
                .andExpect(model().attribute("callbackSearchDate",
                        new Date(120, 11, 12)));
    }

    @Test
    public void shouldReturnDiaryIndexPageWithoutASearchDate() throws Exception {
        mockMvc.perform(get("/admin/customer/diary/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/diary/index"));

        verify(myService, times(0)).getCallbacksForDate(any(String.class));
    }
}
