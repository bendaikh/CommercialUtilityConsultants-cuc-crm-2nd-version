package mycrm.services;

import mycrm.models.Contact;
import mycrm.models.Customer;

import java.util.List;

public interface ContactService {
    List<Contact> findByCustomer(Customer customer);

    Contact findById(Long id);

    Contact save(Contact contact);

    void deleteById(Long id);

    void deleteByCustomer(Customer customer);
}
