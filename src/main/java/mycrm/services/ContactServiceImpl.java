package mycrm.services;

import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public List<Contact> findByCustomer(Customer customer) {
        return this.contactRepository.findByCustomer(customer);
    }

    @Override
    public Contact findById(Long id) {
        return this.contactRepository.getOne(id);
    }

    @Override
    public Contact save(Contact contact) {
        return this.contactRepository.save(contact);
    }

    @Override
    public void deleteById(Long id) {
        this.contactRepository.delete(id);
    }

    @Override
    public void deleteByCustomer(Customer customer) {
        this.contactRepository.deleteByCustomer(customer);
    }
}
