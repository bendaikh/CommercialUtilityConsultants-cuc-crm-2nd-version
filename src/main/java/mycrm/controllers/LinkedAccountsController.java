package mycrm.controllers;

import mycrm.models.Customer;
import mycrm.models.LinkedAccount;
import mycrm.services.CustomerService;
import mycrm.services.LinkedAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LinkedAccountsController {
    private static final Logger logger = LogManager.getLogger();

    private final CustomerService customerService;
    private final LinkedAccountService linkedAccountService;

    @Autowired
    public LinkedAccountsController(CustomerService customerService,
                                    LinkedAccountService linkedAccountService) {
        this.customerService = customerService;
        this.linkedAccountService = linkedAccountService;
    }


    @RequestMapping("/admin/customer/customer-linked-accounts/{id}")
    public String showCustomerLinkedAccounts(@PathVariable("id") Long id, Model model) {
        Long customerVar = null;
        Customer customer = customerService.findById(id);
        if (customer != null) {
            List<LinkedAccount> linkedAccounts = linkedAccountService.findByCustomer(customer);
/*            if(linkedAccounts.get(0).getCustomer().getId() == customer.getId()){
                customerVar = linkedAccounts.get(0).getLinkedCustomer().getId();
            }
            else if (linkedAccounts.get(0).getLinkedCustomer().getId() == customer.getId()){
                customerVar = linkedAccounts.get(0).getCustomer().getId();
            }
            System.out.println("§§§§§§§§§§§§§§§§§§§§§§§§§§§§§");
            System.out.println(customerVar);
            model.addAttribute("customerVar", customerVar);*/
            model.addAttribute("linkedAccounts", linkedAccounts);
            model.addAttribute("customer", customer);
            model.addAttribute("linkedAccountsSize", linkedAccounts.size());
            return "admin/customer/customer-linked-accounts";
        }

        return "redirect:admin/customer/view/" + id;
    }

    @RequestMapping("/admin/customer/linked-accounts/{id}")
    public String showLinkedAccounts(@PathVariable("id") Long id, Model model) {
        Long customerVar = null;
        Customer customer = customerService.findById(id);
        if (customer != null) {
            List<LinkedAccount> linkedAccounts = linkedAccountService.findByCustomer(customer);
            if(!linkedAccounts.isEmpty()){
                if(linkedAccounts.get(0).getCustomer().getId() == customer.getId()){
                    customerVar = linkedAccounts.get(0).getLinkedCustomer().getId();
                }
                else if (linkedAccounts.get(0).getLinkedCustomer().getId() == customer.getId()){
                    customerVar = linkedAccounts.get(0).getCustomer().getId();
                }
            }
            model.addAttribute("customerVar", customerVar);
            model.addAttribute("linkedAccounts", linkedAccounts);
            model.addAttribute("customer", customer);
            return "admin/customer/linked-accounts";
        }

        return "redirect:admin/customer/view/" + id;
    }

    @RequestMapping("/admin/customer/create-linked-accounts/{id}")
    public String createLinkedAccounts(@PathVariable("id") Long id,
                                       @RequestParam(name = "searchParam", required = false) String searchParam,
                                       Model model) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return "redirect:admin/customer/view/" + id;
        }

        if (StringUtils.hasText(searchParam)) {
            model.addAttribute("searchResult",
                    customerService.findNonLinkedCustomer(searchParam, customer));
        }

        model.addAttribute("customer", customer);
        return "admin/customer/create-linked-accounts";
    }

    @RequestMapping(value = "/saveLinkedAccount", method = RequestMethod.POST)
    public String saveLinkedAccount(Long id, Long linkedAccountId) {
        Customer linkedCustomer = customerService.findById(linkedAccountId);
        Customer customer = customerService.findById(id);
        if (linkedCustomer == null || customer == null) {
            return "redirect:admin/customer/view/" + id;
        }

        linkedAccountService.createAndSaveLinkedAccount(customer, linkedCustomer);

        return "redirect:/admin/customer/linked-accounts/" + customer.getId();
    }

    @RequestMapping("/admin/customer/delete-linked-account/{id}")
    public String deleteLinkedAccount(@PathVariable("id") Long id,
                                      @RequestParam("linkedAccountId") Long linkedAccountId) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return "redirect:admin/customer/view/" + id;
        }

        try {
            linkedAccountService.deleteLinkedAccount(linkedAccountId);
        } catch (Exception e) {
            logger.info("Unable to delete linked account linkedAccountId={}", linkedAccountId);
        }

        return "redirect:/admin/customer/linked-accounts/" + id;
    }
}
