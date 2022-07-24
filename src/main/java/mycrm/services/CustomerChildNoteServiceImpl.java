package mycrm.services;

import mycrm.functions.UserHelper;
import mycrm.models.Customer;
import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.User;
import mycrm.repositories.CustomerChildNoteRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Primary
public class CustomerChildNoteServiceImpl implements CustomerChildNoteService {
    private final CustomerChildNoteRepository customerChildNoteRepository;
    private final UserHelper userHelper;

    @Autowired
    public CustomerChildNoteServiceImpl(CustomerChildNoteRepository customerChildNoteRepository,
                                        UserHelper userHelper) {
        this.customerChildNoteRepository = customerChildNoteRepository;
        this.userHelper = userHelper;
    }


    @Override
    public List<CustomerChildNote> findCustomerChildNotes(CustomerNote customerNote) {
        return this.customerChildNoteRepository.findByCustomerNoteOrderByDateCreatedAsc(customerNote);
    }

    @Override
    public CustomerChildNote save(CustomerChildNote customerChildNote) {
        return this.customerChildNoteRepository.save(customerChildNote);
    }

    @Override
    public void deleteById(Long id) {
        this.customerChildNoteRepository.delete(id);
    }

    @Override
    public CustomerChildNote findById(Long id) {
        return this.customerChildNoteRepository.findOne(id);
    }

    @Override
    public List<CustomerChildNote> findByCustomer(Customer customer) {
        return this.customerChildNoteRepository.findByCustomer(customer);
    }

    @Override
    public void deleteByCustomer(Customer customer) {
        this.customerChildNoteRepository.deleteByCustomer(customer);
    }

    @Override
    public List<CustomerChildNote> findAllIncompleteByTaggedUserOrderByDueDateAsc(User taggedUser) {
        return this.customerChildNoteRepository.findAllIncompleteByTaggedUserOrderByDueDateAsc(taggedUser);
    }

    @Override
    public void updateCustomerNote(long id) {
        User completedBy = userHelper.getLoggedInUser();

        DateTime dateTime = new org.joda.time.DateTime();

        this.customerChildNoteRepository.updateCustomerNote(id, dateTime.toDate(), completedBy);
    }

    @Override
    public List<CustomerChildNote> findAllIncompleteByAdminOrderByDueDateAsc(List<User> adminStaff) {
        List<CustomerChildNote> childNotes = new ArrayList<>();
        adminStaff.forEach(admin -> childNotes.addAll(this.customerChildNoteRepository.findAllIncompleteByTaggedUserOrderByDueDateAsc(admin)));
        childNotes.sort(Comparator.comparing(CustomerChildNote::getReplyDueByDate, Comparator.nullsLast(Comparator.naturalOrder())));
        return childNotes;
    }

    @Override
    public int countTaggedChildNotesByUser(User user) {
        return this.customerChildNoteRepository.countTaggedChildNotesByUser(user);
    }

    @Override
    public List<CustomerChildNote> findByTaggedUser(User user) {
        return this.customerChildNoteRepository.findByTaggedUser(user);
    }


}
