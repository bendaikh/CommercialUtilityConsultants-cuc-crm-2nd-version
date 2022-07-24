package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.User;

import java.util.List;

public interface CustomerChildNoteService {
    List<CustomerChildNote> findCustomerChildNotes(CustomerNote customerNote);

    CustomerChildNote save(CustomerChildNote customerChildNote);

    void deleteById(Long id);

    CustomerChildNote findById(Long id);

    List<CustomerChildNote> findByCustomer(Customer customer);

    void deleteByCustomer(Customer customer);

    List<CustomerChildNote> findAllIncompleteByTaggedUserOrderByDueDateAsc(User taggedUser);

    void updateCustomerNote(long id);

    List<CustomerChildNote> findAllIncompleteByAdminOrderByDueDateAsc(List<User> adminStaff);

    int countTaggedChildNotesByUser(User user);

    List<CustomerChildNote> findByTaggedUser(User user);
}
