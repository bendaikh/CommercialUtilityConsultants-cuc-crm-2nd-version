package mycrm.controllers;

import mycrm.models.ContractReason;
import mycrm.services.ContractReasonService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ContractReasonControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ContractReasonController contractReasonController;

    @Mock
    private ContractReasonService mockContractReasonService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(contractReasonController).build();
    }

    @Test
    public void shouldReturnListOfContractReasons() throws Exception {
        List<ContractReason> contractReasonList = new ArrayList<>();
        contractReasonList.add(notApplicableContractReason());

        when(mockContractReasonService.findAll()).thenReturn(contractReasonList);

        mockMvc.perform(get("/admin/contractreason/index"))
                .andExpect(model().attribute("contractReason", instanceOf(ContractReason.class)))
                .andExpect(model().attribute("contractReasons", hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/contractreason/index"));
    }


    @Test
    public void shouldCreateNewContractReason() throws Exception {

        verifyZeroInteractions(mockContractReasonService);

        when(mockContractReasonService.save(any(ContractReason.class))).thenReturn(notApplicableContractReason());

        mockMvc.perform(post("/contractReason"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/contractreason/index"));
    }

    @Test
    public void shouldThrowAnErrorWhenCreateNewContractReason() throws Exception {

        verifyZeroInteractions(mockContractReasonService);

        when(mockContractReasonService.save(any(ContractReason.class))).thenThrow(Exception.class);

        mockMvc.perform(post("/contractReason"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/contractreason/index"));
    }

    @Test
    public void shouldEditContractReason() throws Exception {

        verifyZeroInteractions(mockContractReasonService);

        when(mockContractReasonService.findById(56l)).thenReturn(notApplicableContractReason());

        mockMvc.perform(get("/admin/contractreason/edit/56"))
                .andExpect(view().name("admin/contractreason/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("contractReason", instanceOf(ContractReason.class)));
    }

    private ContractReason notApplicableContractReason() {
        ContractReason contractReason = new ContractReason();
        contractReason.setDescription("Not Applicable");
        contractReason.setId(1l);
        return contractReason;
    }

}
