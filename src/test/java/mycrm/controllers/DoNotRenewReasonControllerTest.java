package mycrm.controllers;

import junit.framework.TestCase;
import mycrm.models.ContractReason;
import mycrm.models.DoNotRenewReason;
import mycrm.services.ContractReasonService;
import mycrm.services.DoNotRenewReasonService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DoNotRenewReasonControllerTest {


    private MockMvc mockMvc;

    @InjectMocks
    private DoNotRenewReasonController doNotRenewReasonController;

    @Mock
    private DoNotRenewReasonService mockDoNotRenewReasonService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(doNotRenewReasonController).build();
    }

    @Test
    public void shouldReturnListOfContractReasons() throws Exception {
        List<DoNotRenewReason> doNotRenewReasonList = new ArrayList<>();
        doNotRenewReasonList.add(notApplicableDoNotRenewReason());

        when(mockDoNotRenewReasonService.findAll()).thenReturn(doNotRenewReasonList);

        mockMvc.perform(get("/admin/doNotRenewReason/index"))
                .andExpect(model().attribute("doNotRenewReason", instanceOf(DoNotRenewReason.class)))
                .andExpect(model().attribute("doNotRenewReasons", hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/doNotRenewReason/index"));
    }
    private DoNotRenewReason notApplicableDoNotRenewReason() {
        DoNotRenewReason doNotRenewReason = new DoNotRenewReason();
        doNotRenewReason.setDescription("do not renew applicable");
        doNotRenewReason.setId(1l);
        return doNotRenewReason;
    }

    @Test
    public void shouldCreateNewDoNotRenewReason() throws Exception {

        verifyZeroInteractions(mockDoNotRenewReasonService);

        when(mockDoNotRenewReasonService.save(any(DoNotRenewReason.class))).thenReturn(notApplicableDoNotRenewReason());

        mockMvc.perform(post("/doNotRenewreason"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/doNotRenewReason/index"));
    }

    @Test
    public void shouldThrowAnErrorWhenCreateNewDoNotRenewReason() throws Exception {

        verifyZeroInteractions(mockDoNotRenewReasonService);

        when(mockDoNotRenewReasonService.save(any(DoNotRenewReason.class))).thenThrow(Exception.class);

        mockMvc.perform(post("/doNotRenewreason"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/doNotRenewReason/index"));
    }

    @Test
    public void shouldEditDoNotRenewReason() throws Exception {

        verifyZeroInteractions(mockDoNotRenewReasonService);

        when(mockDoNotRenewReasonService.findById(56l)).thenReturn(notApplicableDoNotRenewReason());

        mockMvc.perform(get("/admin/doNotRenewReason/edit/56"))
                .andExpect(view().name("admin/doNotRenewReason/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("doNotRenewReason", instanceOf(DoNotRenewReason.class)));
    }


}