package mycrm.controllers;

import mycrm.audit.history.CustomerHistory;
import mycrm.models.Customer;
import mycrm.services.CustomerHistoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomerHistoryController {

    private static Logger logger = LogManager.getLogger();

    private final CustomerHistoryService customerHistoryService;

    @Autowired
    public CustomerHistoryController(CustomerHistoryService customerHistoryService) {
        this.customerHistoryService = customerHistoryService;
    }

    @RequestMapping("/deleteHistory/{id}")
    public String deleteHistory(@PathVariable("id") Long id) {
        CustomerHistory customerHistory = customerHistoryService.findById(id);
        Customer customer = customerHistory.getCustomer();

        try {
            customerHistoryService.deleteById(id);
            logger.info("History deleted {} ", id);
            return "redirect:/admin/removal/index/" + customer.getId();
        } catch (Exception e) {
            logger.info("Unable to delete history {} ", id);
            return "redirect:/admin/removal/index/" + customer.getId();
        }
    }
}
