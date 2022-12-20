package mycrm.controllers;

import mycrm.models.BillingDetail;
import mycrm.models.Contact;
import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.models.Customer;
import mycrm.models.CustomerSearch;
import mycrm.models.CustomerSearchResult;
import mycrm.models.DoNotContactNumber;
import mycrm.models.EmailHistory;
import mycrm.models.TpsContact;
import mycrm.search.ContractSearchService;
import mycrm.search.CustomerSearchService;
import mycrm.services.BrokerService;
import mycrm.services.ContactPreferencesService;
import mycrm.services.ContactService;
import mycrm.services.ContractService;
import mycrm.services.CustomerService;
import mycrm.services.EmailHistoryService;
import mycrm.services.LinkedAccountService;
import mycrm.services.NewCustomerService;
import mycrm.services.SupplierService;
import mycrm.services.TpsCheckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class CustomerController {

    private static final Logger logger = LogManager.getLogger();

    private final CustomerService customerService;
    private final SupplierService supplierService;
    private final BrokerService brokerService;
    private final ContractSearchService contractSearchService;
    private final CustomerSearchService customerSearchService;
    private final NewCustomerService newCustomerService;
    private final ContactService contactService;
    private final ContractService contractService;
    private final TpsCheckService tpsCheckService;
    private final ContactPreferencesService contactPreferencesService;
    private final EmailHistoryService emailHistoryService;
    private final LinkedAccountService linkedAccountService;

    @Autowired
    public CustomerController(CustomerService customerService,
                              SupplierService supplierService,
                              BrokerService brokerService,
                              ContractSearchService contractSearchService,
                              CustomerSearchService customerSearchService,
                              NewCustomerService newCustomerService,
                              ContactService contactService,
                              ContractService contractService,
                              TpsCheckService tpsCheckService,
                              ContactPreferencesService contactPreferencesService,
                              EmailHistoryService emailHistoryService,
                              LinkedAccountService linkedAccountService) {
        this.customerService = customerService;
        this.supplierService = supplierService;
        this.brokerService = brokerService;
        this.contractSearchService = contractSearchService;
        this.customerSearchService = customerSearchService;
        this.newCustomerService = newCustomerService;
        this.contactService = contactService;
        this.contractService = contractService;
        this.tpsCheckService = tpsCheckService;
        this.contactPreferencesService = contactPreferencesService;
        this.emailHistoryService = emailHistoryService;
        this.linkedAccountService = linkedAccountService;
    }

    // list all customers
    @RequestMapping("/admin/customer/customercontracts/{pageNumber}")
    public String customersContracts(ContractSearch contractSearch, Model model, @PathVariable("pageNumber") int pageNumber) throws ParseException {

        long startTime = System.currentTimeMillis();
        ContractSearchResult contractSearchResult = contractSearchService.searchCustomerContract(contractSearch, pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("customerContracts", contractSearchResult.getReturnedContracts());
        model.addAttribute("suppliers", supplierService.findAllOrderByBusinessName());
        model.addAttribute("totalResults", contractSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", contractSearchResult.getTotalPages());
        model.addAttribute("totalContracts", contractSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("timeTaken", timeTaken);

        return "admin/customer/customercontracts";
    }

    // list all of my customers
    @RequestMapping("/admin/customer/mycustomers")
    public String myCustomers(ContractSearch contractSearch, Model model) {

        long startTime = System.currentTimeMillis();

        Set<Customer> searchResults = contractSearchService.searchMyCustomers(contractSearch);

        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("customers", searchResults);
        model.addAttribute("timeTaken", timeTaken);

        return "admin/customer/mycustomers";
    }

    // list all customers by page number
    @RequestMapping("/admin/customer/customers/{pageNumber}")
    public String customers(CustomerSearch customerSearch, Model model, @PathVariable("pageNumber") int pageNumber) {
        long startTime = System.currentTimeMillis();
        CustomerSearchResult customerSearchResult = customerSearchService.searchCustomers(customerSearch, pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("searchResults", customerSearchResult.getReturnedCustomers());
        System.out.println(customerSearch);
        System.out.println(customerSearchResult.getReturnedCustomers());
        model.addAttribute("totalResults", customerSearchResult.getReturnedCustomerCount());
        model.addAttribute("totalPages", customerSearchResult.getTotalPages());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalCustomers", customerSearchResult.getTotalCustomerCount());
        model.addAttribute("timeTaken", timeTaken);
        return "admin/customer/customers";
    }

    @RequestMapping("/customerSearching")
    @ResponseBody
    public String customersSearching(CustomerSearch customerSearch, Model model) throws Exception{

        CustomerSearchResult customerSearchResult = customerSearchService.searchCustomers(customerSearch, 1);
        model.addAttribute("searchResults", customerSearchResult.getReturnedCustomers());
        String jsonStr = JSONArray.toJSONString(customerSearchResult.getReturnedCustomers());
        System.out.println(customerSearchResult.getReturnedCustomers());
        return jsonStr;
    }

    // view customer
    @RequestMapping("/admin/customer/view/{id}")
    public String viewCustomer(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            List<Contact> contactList = contactService.findByCustomer(customer);
            List<TpsContact> tpsContacts = tpsCheckService.buildContactList(contactList);
            Set<DoNotContactNumber> doNotContactNumbers = contactPreferencesService.checkCustomer(customer);
            boolean customerWithinCheckPeriod = tpsCheckService.customerWithinCheckPeriod(customer);
            String contactNumberStyle = tpsCheckService.customerContactNumberStyle(customer);
            String mobileNumberStyle = tpsCheckService.customerMobileNumberStyle(customer);
            List<EmailHistory> emailHistory = emailHistoryService.findByCustomer(customer);

            model.addAttribute("customer", customer);
            model.addAttribute("contact", new Contact());
            model.addAttribute("tpsHistory", tpsCheckService.findByCustomer(customer));
            model.addAttribute("tpsContacts", tpsContacts);
            model.addAttribute("doNotContactNumbers", doNotContactNumbers);
            model.addAttribute("customerWithinCheckPeriod", customerWithinCheckPeriod);
            model.addAttribute("contactNumberStyle", contactNumberStyle);
            model.addAttribute("mobileNumberStyle", mobileNumberStyle);
            model.addAttribute("emailHistory", emailHistory);
            model.addAttribute("linkedAccountsSize", linkedAccountService.findByCustomer(customer).size());
            return "admin/customer/view";
        }

        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/contactslist/{id}")
    public String showTpsContactsList(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            List<Contact> contactList = contactService.findByCustomer(customer);
            List<TpsContact> tpsContacts = tpsCheckService.buildContactList(contactList);
            Set<DoNotContactNumber> doNotContactNumbers = contactPreferencesService.checkCustomer(customer);
            boolean customerWithinCheckPeriod = tpsCheckService.customerWithinCheckPeriod(customer);
            String contactNumberStyle = tpsCheckService.customerContactNumberStyle(customer);
            String mobileNumberStyle = tpsCheckService.customerMobileNumberStyle(customer);

            model.addAttribute("customer", customer);
            model.addAttribute("contact", new Contact());
            model.addAttribute("tpsHistory", tpsCheckService.findByCustomer(customer));
            model.addAttribute("tpsContacts", tpsContacts);
            model.addAttribute("doNotContactNumbers", doNotContactNumbers);
            model.addAttribute("customerWithinCheckPeriod", customerWithinCheckPeriod);
            model.addAttribute("contactNumberStyle", contactNumberStyle);
            model.addAttribute("mobileNumberStyle", mobileNumberStyle);
            return "admin/customer/contactslist";
        }
        return "redirect:/admin/index";
    }

    //create billing details
    @RequestMapping("/admin/customer/editbillingdetails/{id}")
    public String editBillingDetails(@PathVariable("id") Long id, Model model) {

        Customer customer = customerService.findById(id);
        if (customer.getBillingDetail() == null) {
            model.addAttribute("billingDetail", new BillingDetail());
        } else {
            model.addAttribute("billingDetail", customer.getBillingDetail());
        }
        model.addAttribute("customer", customer);
        return "admin/customer/editbillingdetails";
    }

    // create billing detail
    @RequestMapping(value = "/billingDetail", method = RequestMethod.POST)
    public String saveBillingDetail(BillingDetail billingDetail) {
        BillingDetail savedBillingDetails = customerService.saveBillingDetail(billingDetail);
        return "redirect:/admin/customer/view/" + savedBillingDetails.getCustomer().getId();
    }

    // create contact
    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String saveContact(Contact contact) {
        Contact savedContact = contactService.save(contact);
        return "redirect:/admin/customer/view/" + savedContact.getCustomer().getId();
    }

    //edit contact
    @RequestMapping("/admin/customer/contact/edit/{id}")
    public String editContact(@PathVariable("id") Long id, Model model) {
        Contact contact = contactService.findById(id);

        model.addAttribute("contact", contact);
        return "admin/customer/contact/edit";
    }

    // delete customer contact
    @RequestMapping("/admin/customer/contact/deleteContact/{id}")
    public String deleteContact(@PathVariable("id") Long id) {
        Contact contact = contactService.findById(id);

        if (contact != null) {
            Long customerId = contact.getCustomer().getId();
            contactService.deleteById(id);

            logger.info("Contact deleted");
            return "redirect:/admin/customer/view/" + customerId;
        }
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findById(id);

        model.addAttribute("customer", customer);
        return "admin/customer/newcustomer";
    }

    @RequestMapping("/admin/customer/newcustomer")
    public String newCustomer(Model model) {
        Customer customer = new Customer();

        model.addAttribute("customer", customer);
        return "admin/customer/newcustomer";
    }

    @RequestMapping(value = "customer", method = RequestMethod.POST)
    public String saveCustomer(Customer customer,
                               @RequestParam(value = "createGas", defaultValue = "false", required = false) Boolean createGas,
                               @RequestParam(value = "createElectricity", defaultValue = "false", required = false) Boolean createElectricity) {

        try {

            if (customer.getId() != null) {
                logger.info("Editing customer={}", customer.getId());
                customerService.save(customer);

                return "redirect:/admin/customer/view/" + customer.getId();
            } else {
                logger.info("Creating customer - customerId={}", customer.getId());

                logger.info("Creating customer with gas {}", createGas);

                logger.info("Creating customer with elec {}", createElectricity);

                Customer newCustomer = customerService.save(customer);

                logger.info("Creating customer - newCustomerId={}", newCustomer.getId());
                // if (newCustomer.getId() != null) {

                newCustomerService.createNewCustomerLead(newCustomer, createGas, createElectricity);
                // logger.info("Created customer={}", newCustomer.getId());
                //
                // } else {
                // logger.info("When creating customer, the site and contracts
                // were not created");
                // }
                return "redirect:/admin/customer/view/" + newCustomer.getId();
            }

        } catch (Exception e) {
            logger.info("Error creating customer {}", e.getMessage());
            return "redirect:/admin/customer/newcustomer";
        }
    }

}
