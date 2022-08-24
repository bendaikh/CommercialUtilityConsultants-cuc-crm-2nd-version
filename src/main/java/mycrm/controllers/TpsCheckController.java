package mycrm.controllers;

import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.services.ContactService;
import mycrm.services.CustomerService;
import mycrm.services.TpsCheckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TpsCheckController {
    private static final Logger logger = LogManager.getLogger();

    private final TpsCheckService tpsCheckService;
    private final CustomerService customerService;
    private final ContactService contactService;

    @Autowired
    public TpsCheckController(TpsCheckService tpsCheckService, CustomerService customerService, ContactService contactService) {
        this.tpsCheckService = tpsCheckService;
        this.customerService = customerService;
        this.contactService = contactService;
    }

    @RequestMapping("/admin/customer/tps-check/{customerId}")
    public String tpsCheckByCustomer(@PathVariable Long customerId) {
        Customer customer = customerService.findById(customerId);
        if (customer != null) {
            tpsCheckService.checkCustomerNumbers(customer);
        }
        return "redirect:/admin/customer/view/" + customerId;
    }

    @RequestMapping("/admin/customer/tps-check/{customerId}/{contactId}")
    public String tpsCheckByContact(@PathVariable Long customerId, @PathVariable Long contactId) {
        Contact contact = contactService.findById(contactId);
        if (contact != null) {
            tpsCheckService.checkContactNumbers(contact);
        }
        return "redirect:/admin/customer/view/" + customerId;
    }
}
