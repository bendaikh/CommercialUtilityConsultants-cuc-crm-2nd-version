package mycrm.controllers;

import mycrm.functions.UserHelper;
import mycrm.models.BrokerNote;
import mycrm.models.CourtesyCallSearch;
import mycrm.models.CourtesyCallTask;
import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.NewSaleTask;
import mycrm.models.PendingTerminationTask;
import mycrm.models.ProcessingPack;
import mycrm.models.Role;
import mycrm.models.TerminationTask;
import mycrm.models.User;
import mycrm.models.UtilityContract;
import mycrm.models.WelcomePack;
import mycrm.services.AdminContractTaskService;
import mycrm.services.AdminContractTerminationTaskService;
import mycrm.services.BrokerNoteService;
import mycrm.services.CourtesyCallTaskService;
import mycrm.services.CustomerChildNoteService;
import mycrm.services.CustomerNoteService;
import mycrm.services.DocumentService;
import mycrm.services.ElecContractService;
import mycrm.services.GasContractService;
import mycrm.services.UserService;
import mycrm.services.UtilityContractService;
import mycrm.services.WorkflowTaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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

public class TaskControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private CustomerNoteService mockCustomerNoteService;
    @Mock
    private UserService mockUserService;
    @Mock
    private BrokerNoteService mockBrokerNoteService;
    @Mock
    private AdminContractTaskService mockAdminContractTaskService;
    @Mock
    private AdminContractTerminationTaskService mockAdminContractTerminationTaskService;
    @Mock
    private GasContractService mockGasContractService;
    @Mock
    private ElecContractService mockElecContractService;
    @Mock
    private CourtesyCallTaskService courtesyCallTaskService;
    @Mock
    private CustomerChildNoteService customerChildNoteService;
    @Mock
    private WorkflowTaskService workflowTaskService;
    @Mock
    private UtilityContractService mockUtilityContractService;
    @Mock
    private UserHelper userHelper;
    @Mock
    private DocumentService documentService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void shouldReturnTaskList() throws Exception {

        Role role = new Role();
        role.setId(1);
        role.setRole("BROKER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(Long.valueOf(54));
        user.setUsername("default.user");
        user.setRoles(roles);

        List<CustomerNote> customerNotes = new ArrayList<>();
        customerNotes.add(new CustomerNote());

        List<BrokerNote> brokerNotes = new ArrayList<>();
        brokerNotes.add(new BrokerNote());

        List<PendingTerminationTask> adminContractTerminationTaskService = new ArrayList<>();

        List<NewSaleTask> adminContractTaskService = new ArrayList<>();

        when(userHelper.getLoggedInUser()).thenReturn(user);
        when(mockCustomerNoteService.findAllIncompleteByTaggedUserOrderByDueDateAsc(user)).thenReturn(customerNotes);
        when(mockBrokerNoteService.findAllIncompleteBrokerNotesByTaggedUser(user)).thenReturn(brokerNotes);
        when(mockAdminContractTaskService.findAllAdminContractNewSalesTasks()).thenReturn(adminContractTaskService);
        when(mockAdminContractTerminationTaskService.findAllContractsToTerminate()).thenReturn(adminContractTerminationTaskService);

        mockMvc.perform(get("/admin/tasks/index"))
                .andExpect(model().attribute("findAllIncompleteCustomerNotesByTaggedUser", hasSize(1)))
                .andExpect(model().attribute("findAllBrokerNotesByUser", hasSize(1)))
                .andExpect(model().attribute("findAllAdminContractNewSalesTasks", hasSize(0)))
                .andExpect(model().attribute("findAllContractsToTerminate", hasSize(0)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/index"));
    }

    @Test
    public void shouldShowGasWelcomePack() throws Exception {

        GasCustomerContract gasContract = new GasCustomerContract();
        when(mockGasContractService.findById(Long.valueOf(154))).thenReturn(gasContract);

        mockMvc.perform(get("/admin/tasks/welcomepack")
                .param("id", "154")
                .param("supplyType", "GAS"))
                .andExpect(model().attribute("welcomePack", gasContract))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/welcomepack"));
    }

    @Test
    public void shouldShowElectricityWelcomePack() throws Exception {

        ElecCustomerContract elecContract = new ElecCustomerContract();
        when(mockElecContractService.findById(Long.valueOf(18))).thenReturn(elecContract);

        mockMvc.perform(get("/admin/tasks/welcomepack")
                .param("id", "18")
                .param("supplyType", "ELEC"))
                .andExpect(model().attribute("welcomePack", elecContract))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/welcomepack"));
    }

    @Test
    public void shouldShowUtilityWelcomePack() throws Exception {

        UtilityContract utilityContract = new UtilityContract();
        utilityContract.setUtilityType("WATER");
        utilityContract.setId(18l);
        when(mockUtilityContractService.findById(Long.valueOf(18))).thenReturn(utilityContract);

        mockMvc.perform(get("/admin/tasks/welcomepack")
                .param("id", "18")
                .param("supplyType", "WATER"))
                .andExpect(model().attribute("welcomePack", utilityContract))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/welcomepack"));
    }

    @Test
    public void shouldShowGasTerminationTask() throws Exception {

        GasCustomerContract gasContract = new GasCustomerContract();
        when(mockGasContractService.findById(Long.valueOf(111))).thenReturn(gasContract);

        mockMvc.perform(get("/admin/tasks/terminationtask")
                .param("id", "111")
                .param("supplyType", "GAS"))
                .andExpect(model().attribute("terminationTask", gasContract))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/terminationtask"));
    }

    @Test
    public void shouldShowElectricityTerminationTask() throws Exception {

        ElecCustomerContract elecContract = new ElecCustomerContract();
        when(mockElecContractService.findById(Long.valueOf(188))).thenReturn(elecContract);

        mockMvc.perform(get("/admin/tasks/terminationtask")
                .param("id", "188")
                .param("supplyType", "ELEC"))
                .andExpect(model().attribute("terminationTask", elecContract))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/terminationtask"));
    }

    @Test
    public void shouldShowUtilityTerminationTask() throws Exception {

        UtilityContract utilityContract = new UtilityContract();
        when(mockUtilityContractService.findById(Long.valueOf(188))).thenReturn(utilityContract);

        mockMvc.perform(get("/admin/tasks/terminationtask")
                .param("id", "188")
                .param("supplyType", "SOLAR"))
                .andExpect(model().attribute("terminationTask", utilityContract))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/terminationtask"));
    }

    @Test
    public void shouldShowCustomerNoteTask() throws Exception {

        CustomerNote customerNoteTask = new CustomerNote();
        when(mockCustomerNoteService.findById(Long.valueOf(874))).thenReturn(customerNoteTask);

        mockMvc.perform(get("/admin/tasks/customernote").param("id", "874"))
                .andExpect(model().attribute("customerNote", customerNoteTask))
                .andExpect(view().name("admin/tasks/customernote"));
    }

    @Test
    public void shouldShowBrokerNoteTask() throws Exception {

        BrokerNote brokerNoteTask = new BrokerNote();
        when(mockBrokerNoteService.findById(Long.valueOf(855))).thenReturn(brokerNoteTask);

        mockMvc.perform(get("/admin/tasks/brokernote").param("id", "855"))
                .andExpect(model().attribute("brokerNote", brokerNoteTask))
                .andExpect(view().name("admin/tasks/brokernote"));
    }

    @Test
    public void shouldUpdateGasWelcomePack() throws Exception {

        WelcomePack welcomePack = new WelcomePack();
        welcomePack.setId(Long.valueOf(74));
        welcomePack.setSupplyType("GAS");

        mockMvc.perform(post("/updateWelcomePack").param("id", "74")
                .param("supplyType", "GAS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));
    }

    @Test
    public void shouldUpdateElectricityWelcomePack() throws Exception {

        WelcomePack welcomePack = new WelcomePack();
        welcomePack.setId(Long.valueOf(94));
        welcomePack.setSupplyType("ELEC");

        mockMvc.perform(post("/updateWelcomePack").param("id", "94")
                .param("supplyType", "ELEC"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));
    }

    @Test
    public void shouldUpdateUtilityWelcomePack() throws Exception {

        WelcomePack welcomePack = new WelcomePack();
        welcomePack.setId(Long.valueOf(94));
        welcomePack.setSupplyType("WATER");

        mockMvc.perform(post("/updateWelcomePack").param("id", "94")
                .param("supplyType", "WATER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));
    }

    @Test
    public void shouldUpdateGasTermination() throws Exception {

        TerminationTask terminationTask = new TerminationTask();
        terminationTask.setId(Long.valueOf(74));
        terminationTask.setSupplyType("GAS");

        mockMvc.perform(post("/updateTermination").param("id", "74")
                .param("supplyType", "GAS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));
    }

    @Test
    public void shouldUpdateElectricityTermination() throws Exception {

        TerminationTask terminationTask = new TerminationTask();
        terminationTask.setId(Long.valueOf(174));
        terminationTask.setSupplyType("ELEC");

        mockMvc.perform(post("/updateTermination")
                .param("id", "174")
                .param("supplyType", "ELEC"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));
    }

    @Test
    public void shouldUpdateUtilityTermination() throws Exception {

        TerminationTask terminationTask = new TerminationTask();
        terminationTask.setId(Long.valueOf(74));
        terminationTask.setSupplyType("SOLAR");

        mockMvc.perform(post("/updateTermination").param("id", "74")
                .param("supplyType", "SOLAR"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));
    }

    @Test
    public void shouldUpdateCompletedCustomerNote() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setRole("BROKER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(Long.valueOf(54));
        user.setUsername("default.user");
        user.setRoles(roles);

        when(userHelper.getLoggedInUser()).thenReturn(user);

        mockMvc.perform(post("/updateCompletedCustomerNote")
                .param("id", "174"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));
    }

    @Test
    public void shouldUpdateCompletedBrokerNote() throws Exception {
        mockMvc.perform(post("/updateCompletedBrokerNote")
                .param("id", "114"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));
    }

    @Test
    public void shouldShowCourtesyCallsPage() throws Exception {

        List<CourtesyCallTask> courtesyCallTaskList = new ArrayList<>();

        CourtesyCallSearch courtesyCallSearch = new CourtesyCallSearch(1, 1, courtesyCallTaskList);

        when(courtesyCallTaskService.findAll(any(int.class))).thenReturn(courtesyCallSearch);

        mockMvc.perform(get("/admin/tasks/courtesy/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/courtesy"))
                .andExpect(model().attribute("pageNumber", courtesyCallSearch.getPageNumber()))
                .andExpect(model().attribute("totalPages", courtesyCallSearch.getTotalPages()))
                .andExpect(model().attribute("incompleteCourtesyCalls", courtesyCallSearch.getCourtesyCallTaskList()));
    }

    @Test
    public void shouldReturnGasProcessingPack() throws Exception {

        GasCustomerContract gasContract = new GasCustomerContract();
        when(mockGasContractService.findById(any(Long.class))).thenReturn(gasContract);

        mockMvc.perform(get("/admin/tasks/processing-pack?id=2&supplyType=GAS"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/processing-pack"))
                .andExpect(model().attribute("processingPack", gasContract));
    }

    @Test
    public void shouldReturnElectricProcessingPack() throws Exception {

        ElecCustomerContract contract = new ElecCustomerContract();
        when(mockElecContractService.findById(any(Long.class))).thenReturn(contract);

        mockMvc.perform(get("/admin/tasks/processing-pack?id=2&supplyType=ELEC"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/processing-pack"))
                .andExpect(model().attribute("processingPack", contract));
    }

    @Test
    public void shouldReturnUtilityProcessingPack() throws Exception {

        UtilityContract contract = new UtilityContract();
        when(mockUtilityContractService.findById(any(Long.class))).thenReturn(contract);

        mockMvc.perform(get("/admin/tasks/processing-pack?id=2&supplyType=WATER"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/processing-pack"))
                .andExpect(model().attribute("processingPack", contract));
    }

    @Test
    public void shouldReturnCustomerChildNote() throws Exception {
        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(12l);

        CustomerChildNote customerChildNote = new CustomerChildNote();
        customerChildNote.setId(55l);
        customerChildNote.setCustomerNote(customerNote);

        when(customerChildNoteService.findById(any(Long.class))).thenReturn(customerChildNote);

        when(mockCustomerNoteService.findById(any(Long.class))).thenReturn(customerNote);

        mockMvc.perform(get("/admin/tasks/customerchildnote?id=2"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tasks/customerchildnote"))
                .andExpect(model().attribute("customerChildNote", customerChildNote))
                .andExpect(model().attribute("customerNote", customerNote));
    }

    @Test
    public void shouldUpdateGasProcessingPack() throws Exception {
        mockMvc.perform(post("/updateProcessingPack")
                .param("id", "1")
                .param("supplyType", "GAS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));

        verify(mockGasContractService, times(1)).updateProcessingPack(any(ProcessingPack.class));
    }

    @Test
    public void shouldUpdateElectricProcessingPack() throws Exception {
        mockMvc.perform(post("/updateProcessingPack")
                .param("id", "1")
                .param("supplyType", "ELEC"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));

        verify(mockElecContractService, times(1)).updateProcessingPack(any(ProcessingPack.class));
    }

    @Test
    public void shouldUpdateUtilityProcessingPack() throws Exception {
        mockMvc.perform(post("/updateProcessingPack")
                .param("id", "1")
                .param("supplyType", "WATER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));

        verify(mockUtilityContractService, times(1)).updateProcessingPack(any(ProcessingPack.class));
    }

    @Test
    public void shouldUpdateCompletedCustomerChildNote() throws Exception {
        mockMvc.perform(post("/updateCompletedCustomerChildNote")
                .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks/index"));

        verify(customerChildNoteService, times(1)).updateCustomerNote(any(Long.class));
    }

}
