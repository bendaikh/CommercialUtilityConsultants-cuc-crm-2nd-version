package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.BrokerNote;
import mycrm.models.User;
import mycrm.services.BrokerNoteService;
import mycrm.services.BrokerService;
import mycrm.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class BrokerNoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BrokerNoteService mockBrokerNoteService;

    @Mock
    private BrokerService mockBrokerService;

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private BrokerNoteController brokerNoteController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(brokerNoteController).build();
    }

    @Test
    public void shouldSaveBrokerNote() throws Exception {
        Broker broker = new Broker();
        broker.setId(9L);

        User taggedUser = new User();
        taggedUser.setId(65L);

        BrokerNote brokerNote = new BrokerNote();
        brokerNote.setId(54L);
        brokerNote.setBroker(broker);
        brokerNote.setSubject("Subject");
        brokerNote.setNote("Note");
        brokerNote.setTaggedUser(taggedUser);
        brokerNote.setCompletedBy(taggedUser);
        brokerNote.setCompleted(true);

        when(mockBrokerNoteService.create(any(BrokerNote.class))).thenReturn(brokerNote);

        mockMvc
                .perform(post("/brokerNote")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("createdBy.id", "1")
                        .param("broker.id", broker.getId().toString())
                        .param("subject", "ASubject")
                        .param("note", "ANote")
                        .param("completed", "on")
                        .param("taggedUser.id", taggedUser.getId().toString()))
                .andExpect(redirectedUrl("/admin/broker/brokernotes/9"));
    }

    @Test
    public void shouldNotSaveBrokerNoteAndReturnBrokerList() throws Exception {

        BrokerNote brokerNote = new BrokerNote();
        when(mockBrokerNoteService.create(any(BrokerNote.class))).thenReturn(brokerNote);

        mockMvc.perform(post("/brokerNote"))
                .andExpect(redirectedUrl("/admin/broker/brokers"));
    }

    @Test
    public void shouldDeleteBrokerNote() throws Exception {
        Long id = 1L;
        Broker broker = new Broker();
        broker.setId(99L);

        BrokerNote brokerNote = new BrokerNote();
        brokerNote.setId(22L);
        brokerNote.setBroker(broker);

        when(mockBrokerNoteService.findById(id)).thenReturn(brokerNote);

        mockMvc
                .perform(get("/admin/broker/deleteNote/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/broker/brokernotes/99"));
    }

    @Test
    public void shouldNotDeleteBrokerNoteWhenNotFound() throws Exception {

        when(mockBrokerNoteService.findById(1L)).thenReturn(null);

        mockMvc
                .perform(get("/admin/broker/deleteNote/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/index"));
    }

    @Test
    public void shouldGetBrokerNotes() throws Exception {
        Broker broker = new Broker();
        broker.setId(1L);

        BrokerNote brokerNote = new BrokerNote();
        brokerNote.setBroker(broker);

        List<BrokerNote> listOfNotes = new ArrayList<>();
        listOfNotes.add(brokerNote);

        List<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(new User());

        when(mockBrokerService.findById(any(Long.class))).thenReturn(broker);
        when(mockBrokerNoteService.findByBroker(any(Broker.class))).thenReturn(listOfNotes);
        when(mockUserService.findAll()).thenReturn(listOfUsers);

        mockMvc
                .perform(get("/admin/broker/brokernotes/1"))
                .andExpect(view().name("admin/broker/brokernotes"))
                .andExpect(model().attribute("brokerNotes", listOfNotes))
                .andExpect(model().attribute("users", listOfUsers))
                .andExpect(model().attribute("broker", broker))
                .andExpect(status().isOk());
    }
}
