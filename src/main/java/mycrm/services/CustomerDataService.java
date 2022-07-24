package mycrm.services;

import mycrm.models.Contact;
import mycrm.models.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomerDataService {
    private static Logger logger = LogManager.getLogger();

    private final ContactService contactService;

    @Autowired
    public CustomerDataService(ContactService contactService) {
        this.contactService = contactService;
    }

    public List<String> getCustomerEmail(Customer customer) {
        logger.info("Getting primary email address for {}", customer.getBusinessName());

        if (StringUtils.hasText(customer.getEmailAddress())) {
            return Arrays.asList(customer.getEmailAddress());
        }
        return new ArrayList<>();
    }

    public List<String> getAllCustomerEmails(Customer customer) {
        logger.info("Getting email addresses for {}", customer.getBusinessName());

        List<String> customerEmails = new ArrayList<>();

        if (StringUtils.hasText(customer.getEmailAddress())) {
            customerEmails.add(customer.getEmailAddress());
        }

        List<Contact> customerContacts = contactService.findByCustomer(customer);
        customerContacts.forEach(contact -> {
            if (StringUtils.hasText(contact.getEmailAddress())) {
                customerEmails.add(contact.getEmailAddress());
            }
        });

        return customerEmails;
    }
}
