package mycrm.controllers;

import mycrm.models.*;
import mycrm.search.VoipContractSearchService;
import mycrm.search.WaterContractSearchService;
import mycrm.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class WaterController {
    @Autowired
    private ContractReasonService contractReasonService;
    @Autowired
    private DoNotRenewReasonService doNotRenewReasonService;
    @Autowired
    private CustomerSiteService customerSiteService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private BrokerService brokerService;
    @Autowired
    private UserService userService;
    @Autowired
    private BrokerTransferHistoryService brokerTransferHistoryService;
    @Autowired
    private WaterContractService waterContractService;
    @Autowired
    private WaterContractSearchService waterContractSearchService;
    private static final Logger logger = LogManager.getLogger();
    @RequestMapping(value = "/waterContract", method = RequestMethod.POST)
    public String saveWaterContract(WaterContract waterContract) {
        WaterContract contract = waterContractService.save(waterContract);
        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }

    @RequestMapping("/admin/customer/water/{customerSiteID}")
    public String newWaterContract(@PathVariable String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        WaterContract waterContract = new WaterContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("contractReasons", doNotRenewReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("waterContract", waterContract);
        return "admin/customer/manage-water-contract";
    }

    @RequestMapping("/admin/customer/edit-water-contract/{id}")
    public String editWaterContract(@PathVariable("id") Long id, Model model) {
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        WaterContract waterContract = waterContractService.findById(id);

        if (waterContract == null) {
            return "admin/error/404";
        }

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isBroker()) {
            if (!waterContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", waterContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isExternalBroker()) {
            if (!waterContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", waterContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit {} Contract - User is not super admin", waterContract.getSupplyType());
            return "redirect:/unauthorised";
        }

        List<String> transferMessageList = brokerTransferHistoryService.findLatestWaterBrokerTransferHistory(waterContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("customerSite", customerSiteService.findById(waterContract.getCustomerSite().getId()));
        model.addAttribute("waterContract", waterContract);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("contractReasons", contractReasonService.findAll());

        return "admin/customer/manage-water-contract";
    }

    @RequestMapping("/admin/water/index/{pageNumber}")
    public String viewWaterHomePage(WaterContractSearch waterContractSearch,
                                   Model model,
                                   @PathVariable("pageNumber") int pageNumber) {
        long startTime = System.currentTimeMillis();
        WaterSearchResult waterSearchResult =
                waterContractSearchService.searchWaterContract(waterContractSearch,
                        pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        waterSearchResult.getReturnedContracts().forEach(waterContract -> {
            logger.info("{}", waterContract.getId());
        });

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("contracts", waterSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", waterSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", waterSearchResult.getTotalPages());
        model.addAttribute("totalContracts", waterSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/water/index";
    }

    @RequestMapping("/admin/water/callbacks/{page}")
    public String viewWaterCallbacks(WaterContractSearch waterContractSearch,
                                    Model model,
                                    @PathVariable("page") int page) {

        waterContractSearch.setCallbackSearch(true);

        long startTime = System.currentTimeMillis();
        WaterSearchResult waterSearchResult =
                waterContractSearchService.searchWaterContract(waterContractSearch, page);

        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("callbacks", waterSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", waterSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", waterSearchResult.getTotalPages());
        model.addAttribute("totalContracts", waterSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/water/callbacks";
    }

    @RequestMapping("/admin/water/lost-renewals/{page}")
    public String viewLostWaterRenewals(WaterContractSearch waterContractSearch, Model model, @PathVariable("page") int page) {

        waterContractSearch.setLostRenewalSearch(true);
        waterContractSearch.setLostRenewal(true);

        long startTime = System.currentTimeMillis();
        WaterSearchResult waterSearchResult =
                waterContractSearchService.searchWaterContract(waterContractSearch, page);

        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("lostRenewals", waterSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", waterSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", waterSearchResult.getTotalPages());
        model.addAttribute("totalContracts", waterSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/water/lost-renewals";
    }

    @RequestMapping("/admin/water/renewals/{page}")
    public String viewWaterRenewals(WaterContractSearch waterContractSearch,
                                   Model model,
                                   @PathVariable("page") int page) {

        waterContractSearch.setRenewalSearch(true);
        long startTime = System.currentTimeMillis();
        WaterSearchResult waterSearchResult =
                waterContractSearchService.searchWaterContract(waterContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("renewals", waterSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", waterSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", waterSearchResult.getTotalPages());
        model.addAttribute("totalContracts", waterSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/water/renewals";
    }

    @RequestMapping("/admin/water/leads/{page}")
    public String viewWaterLeads(WaterContractSearch waterContractSearch, Model model, @PathVariable("page") int page) {

        waterContractSearch.setLeadSearch(true);
        long startTime = System.currentTimeMillis();
        WaterSearchResult waterSearchResult =
                waterContractSearchService.searchWaterContract(waterContractSearch, page);
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("leads", waterSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", waterSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", waterSearchResult.getTotalPages());
        model.addAttribute("totalContracts", waterSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/water/leads";
    }
}
