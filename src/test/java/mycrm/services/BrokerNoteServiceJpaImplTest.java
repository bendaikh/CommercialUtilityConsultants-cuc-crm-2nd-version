package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.BrokerNote;
import mycrm.models.User;
import mycrm.repositories.BrokerNoteRepository;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BrokerNoteServiceJpaImplTest {

    @InjectMocks
    private BrokerNoteServiceJpaImpl brokerNoteServiceJpaImpl;

    @Mock
    private BrokerNote mockedBrokerNote;

    @Mock
    private BrokerNoteRepository mockedBrokerNoteRepository;

    @Mock
    private UserService mockedUserService;

    @Test
    public void shouldFindAllBrokerNotes() {
        List<BrokerNote> brokerNoteList = new ArrayList<>();
        brokerNoteList.add(new BrokerNote());

        when(mockedBrokerNoteRepository.findAll()).thenReturn(brokerNoteList);

        List<BrokerNote> findAll = brokerNoteServiceJpaImpl.findAll();

        assertEquals(brokerNoteList, findAll);
    }

    @Test
    public void shouldFindBrokerNoteById() {
        Broker broker = new Broker();
        
        User taggedUser = new User();
        taggedUser.setId(65l);
        
        Date dateCompleted = new Date(2017, 1, 15);
        
        BrokerNote brokerNote = new BrokerNote();
        brokerNote.setId(Long.valueOf(5));
        brokerNote.setBroker(broker);
        brokerNote.setSubject("Subject");
        brokerNote.setNote("Note");
        brokerNote.setTaggedUser(taggedUser);
        brokerNote.setCompletedBy(taggedUser);
        brokerNote.setCompleted(true);
        brokerNote.setVersion(1);
        brokerNote.setDateCompleted(dateCompleted);

        when(mockedBrokerNoteRepository.findOne(Long.valueOf(5))).thenReturn(brokerNote);

        BrokerNote findById = brokerNoteServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(brokerNote, findById);
        assertEquals(brokerNote.getDateCompleted(), findById.getDateCompleted());
        assertEquals(brokerNote.getVersion(), findById.getVersion());
        assertEquals(brokerNote.getBroker(), findById.getBroker());
        assertEquals(brokerNote.getId(), findById.getId());
        assertEquals(brokerNote.getSubject(), findById.getSubject());
        assertEquals(brokerNote.getNote(), findById.getNote());
        assertEquals(brokerNote.getTaggedUser(), findById.getTaggedUser());
        assertEquals(brokerNote.getCompletedBy(), findById.getCompletedBy());
        assertEquals(brokerNote.isCompleted(), findById.isCompleted());
    }

    @Test
    public void shouldSaveBrokerNote() {
        BrokerNote brokerNote = new BrokerNote();
        brokerNote.setId(Long.valueOf(5));

        when(mockedBrokerNoteRepository.save(any(BrokerNote.class))).thenReturn(brokerNote);

        BrokerNote savedBrokerNote = brokerNoteServiceJpaImpl.create(brokerNote);

        assertEquals(brokerNote, savedBrokerNote);
    }

    @Test
    public void shouldEditAndSaveBrokerNote() {
        BrokerNote brokerNote = new BrokerNote();
        brokerNote.setId(Long.valueOf(5));

        when(mockedBrokerNoteRepository.save(any(BrokerNote.class))).thenReturn(brokerNote);

        BrokerNote savedBrokerNote = brokerNoteServiceJpaImpl.edit(brokerNote);

        assertEquals(brokerNote, savedBrokerNote);
    }

    @Test
    public void shouldDeleteBrokerNote() {
        brokerNoteServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedBrokerNoteRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldReturnBrokerNotesByBroker() {
        Broker broker = new Broker();
        broker.setId(Long.valueOf(4));

        BrokerNote brokerNote = new BrokerNote();
        brokerNote.setBroker(broker);
        brokerNote.setId(Long.valueOf(97));

        List<BrokerNote> brokerNoteList = new ArrayList<>();
        brokerNoteList.add(brokerNote);

        when(mockedBrokerNoteRepository.findByBroker(broker)).thenReturn(brokerNoteList);

        List<BrokerNote> findByBroker = brokerNoteServiceJpaImpl.findByBroker(broker);

        assertEquals(brokerNoteList, findByBroker);
    }
    
    @Test
    public void shouldReturnAllIncompleteBrokerNotesByTaggedUser() {
        Broker broker = new Broker();
        broker.setId(Long.valueOf(4));

        BrokerNote brokerNote = new BrokerNote();
        brokerNote.setBroker(broker);
        brokerNote.setId(Long.valueOf(97));

        List<BrokerNote> brokerNoteList = new ArrayList<>();
        brokerNoteList.add(brokerNote);
   
        User user = new User();
        user.setId(Long.valueOf(96));
        
        when(mockedBrokerNoteRepository.findAllIncompleteByTaggedUser(user)).thenReturn(brokerNoteList );
        
        List<BrokerNote> findAllByTaggedUser = brokerNoteServiceJpaImpl.findAllIncompleteBrokerNotesByTaggedUser(user);
        
        assertEquals(brokerNoteList, findAllByTaggedUser);
    }
    
    @Test
    public void shouldUpdateBrokerNote() {
        
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        DateTime dateTime = new DateTime(2017, 1, 15, 0, 0, 0, 0);
        
        long id = 54;
        
        User completedByUser = new User();
        completedByUser.setId(Long.valueOf(id));
        completedByUser.setUsername("imran.miskeen");
               
        when(mockedUserService.findUserByUsername("imran.miskeen")).thenReturn(completedByUser);
        
        brokerNoteServiceJpaImpl.updateBrokerNote(id);
        
        //verify(mockedBrokerNoteRepository, times(1)).updateBrokerNote(id, dateTime.toDate(), completedByUser);
    }

}
