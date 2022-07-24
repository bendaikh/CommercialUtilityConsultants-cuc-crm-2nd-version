package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.CustomerNote;
import mycrm.models.User;
import mycrm.repositories.CustomerNoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerNoteServiceJpaImplTest {

    @InjectMocks
    private CustomerNoteServiceJpaImpl customerNoteServiceJpaImpl;

    @Mock
    private CustomerNoteRepository mockedCustomerNoteRepository;

    @Mock
    private UserService mockedUserService;

    @Test
    public void shouldFindAllCustomerNotes() {
        List<CustomerNote> customerNoteList = new ArrayList<>();
        customerNoteList.add(new CustomerNote());

        when(mockedCustomerNoteRepository.findAll()).thenReturn(customerNoteList);

        List<CustomerNote> findAll = customerNoteServiceJpaImpl.findAll();

        assertEquals(customerNoteList, findAll);
    }

    @Test
    public void shouldFindCustomerNoteById() {
        User taggedUser = new User();
        taggedUser.setId(65l);

        @SuppressWarnings("deprecation")
        Date dateCompleted = new Date(2017, 1, 15);

        Customer customer = new Customer();

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(Long.valueOf(5));
        customerNote.setCustomer(customer);
        customerNote.setSubject("Subject");
        customerNote.setNote("Note");
        customerNote.setTaggedUser(taggedUser);
        customerNote.setCompletedBy(taggedUser);
        customerNote.setCompleted(true);
        customerNote.setVersion(1);
        customerNote.setDateCompleted(dateCompleted);
        customerNote.setLastModifiedBy(taggedUser);
        customerNote.setCreatedBy(taggedUser);
        customerNote.setLastModifiedDate(dateCompleted);
        customerNote.setDateCreated(dateCompleted);

        when(mockedCustomerNoteRepository.findOne(Long.valueOf(5))).thenReturn(customerNote);

        CustomerNote findById = customerNoteServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(customerNote, findById);
        assertEquals(customerNote.getDateCompleted(), findById.getDateCompleted());
        assertEquals(customerNote.getVersion(), findById.getVersion());
        assertEquals(customerNote.getCustomer(), findById.getCustomer());
        assertEquals(customerNote.getId(), findById.getId());
        assertEquals(customerNote.getSubject(), findById.getSubject());
        assertEquals(customerNote.getNote(), findById.getNote());
        assertEquals(customerNote.getTaggedUser(), findById.getTaggedUser());
        assertEquals(customerNote.getCompletedBy(), findById.getCompletedBy());
        assertEquals(customerNote.isCompleted(), findById.isCompleted());
        assertEquals(customerNote.getLastModifiedBy(), findById.getLastModifiedBy());
        assertEquals(customerNote.getLastModifiedDate(), findById.getLastModifiedDate());
        assertEquals(customerNote.getCreatedBy(), findById.getCreatedBy());
        assertEquals(customerNote.getDateCreated(), findById.getDateCreated());
    }

    @Test
    public void shouldSaveCustomerNote() {
        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(Long.valueOf(5));

        when(mockedCustomerNoteRepository.save(any(CustomerNote.class))).thenReturn(customerNote);

        CustomerNote savedCustomerNote = customerNoteServiceJpaImpl.save(customerNote);

        assertEquals(customerNote, savedCustomerNote);
    }

    @Test
    public void shouldEditAndSaveCustomerNote() {
        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(Long.valueOf(5));

        when(mockedCustomerNoteRepository.save(any(CustomerNote.class))).thenReturn(customerNote);

        CustomerNote savedCustomerNote = customerNoteServiceJpaImpl.edit(customerNote);

        assertEquals(customerNote, savedCustomerNote);
    }

    @Test
    public void shouldDeleteCustomerNote() {
        customerNoteServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedCustomerNoteRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldReturnCustomerNoteByBroker() {
        Customer customer = new Customer();
        customer.setId(Long.valueOf(4));

        CustomerNote customerNote = new CustomerNote();
        customerNote.setCustomer(customer);
        customerNote.setId(Long.valueOf(97));

        List<CustomerNote> customerNoteList = new ArrayList<>();
        customerNoteList.add(customerNote);

        when(mockedCustomerNoteRepository.findByCustomer(customer)).thenReturn(customerNoteList);

        List<CustomerNote> findByCustomer = customerNoteServiceJpaImpl.findByCustomer(customer);

        assertEquals(customerNoteList, findByCustomer);
    }

    @Test
    public void shouldReturnAllIncompleteCustomerNoteByTaggedUser() {
        Customer customer = new Customer();
        customer.setId(Long.valueOf(4));

        CustomerNote customerNote = new CustomerNote();
        customerNote.setCustomer(customer);
        customerNote.setId(Long.valueOf(97));

        List<CustomerNote> customerNoteList = new ArrayList<>();
        customerNoteList.add(customerNote);

        User user = new User();
        user.setId(Long.valueOf(96));

        when(mockedCustomerNoteRepository.findAllIncompleteByTaggedUser(user)).thenReturn(customerNoteList);

        List<CustomerNote> findAllByTaggedUser = customerNoteServiceJpaImpl.findAllIncompleteByTaggedUser(user);

        assertEquals(customerNoteList, findAllByTaggedUser);
    }

    @Test
    public void shouldUpdateCustomerNote() {

        long id = 54;

        User completedByUser = new User();
        completedByUser.setId(Long.valueOf(id));
        completedByUser.setUsername("imran.miskeen");

        customerNoteServiceJpaImpl.updateCustomerNote(id, completedByUser);

        verify(mockedCustomerNoteRepository, times(1)).updateCustomerNote(any(Long.class), any(Date.class),
                any(User.class));
    }

    @Test
    public void shouldFindLatest5posts() {
        PageRequest pageRequest = new PageRequest(0, 5);

        List<CustomerNote> listOfCustomerNotes = new ArrayList<>();
        listOfCustomerNotes.add(new CustomerNote());
        listOfCustomerNotes.add(new CustomerNote());
        listOfCustomerNotes.add(new CustomerNote());
        listOfCustomerNotes.add(new CustomerNote());
        listOfCustomerNotes.add(new CustomerNote());

        when(mockedCustomerNoteRepository.findLatest5Posts(pageRequest)).thenReturn(listOfCustomerNotes);

        List<CustomerNote> findLatest5 = customerNoteServiceJpaImpl.findLatest5();

        assertEquals(listOfCustomerNotes, findLatest5);
    }

}
