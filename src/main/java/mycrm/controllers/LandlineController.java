package mycrm.controllers;

import mycrm.models.*;
import mycrm.search.LandlineContractSearchService;
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
public class LandlineController {
    @Autowired
    private ContractReasonService contractReasonService;
    private static final Logger logger = LogManager.getLogger();
    @Autowired
    private BrokerTransferHistoryService brokerTransferHistoryService;



    @Autowired
    private DoNotRenewReasonService doNotRenewReasonService;
    @Autowired
    private CustomerSiteService customerSiteService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private BrokerService brokerService;
    @Autowired
    private LandlineContractService landlineContractService;
    @Autowired
    private UserService userService;
    @Autowired
    private LandlineContractSearchService landlineContractSearchService;
    @RequestMapping(value = "/landlineContract", method = RequestMethod.POST)
    public String saveLandlineContract(LandlineContract landlineContract) {
        LandlineContract contract = landlineContractService.save(landlineContract);
        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }

    @RequestMapping("/admin/customer/landline/{customerSiteID}")
    public String newLandlineContract(@PathVariable String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        LandlineContract landlineContract = new LandlineContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("contractReasons", doNotRenewReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("landlineContract", landlineContract);
        return "admin/customer/manage-landline-contract";
    }
    @RequestMapping("/admin/landline/index/{pageNumber}")
    public String viewLandlineHomePage(LandlineContractSearch landlineContractSearch,
                                       Model model,
                                       @PathVariable("pageNumber") int pageNumber) {
        long startTime = System.currentTimeMillis();
        LandlineSearchResult landlineSearchResult =
                landlineContractSearchService.searchLandlinesContract(landlineContractSearch,
                        pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        landlineSearchResult.getReturnedContracts().forEach(landlineContract -> {
            logger.info("{}", landlineContract.getId());
        });

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("contracts", landlineSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", landlineSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", landlineSearchResult.getTotalPages());
        model.addAttribute("totalContracts", landlineSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/landline/index";
    }

    @RequestMapping("/admin/customer/edit-landline-contract/{id}")
    public String editLandlineContract(@PathVariable("id") Long id, Model model) {
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        LandlineContract landlineContract = landlineContractService.findById(id);

        if (landlineContract == null) {
            return "admin/error/404";
        }

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isBroker()) {
            if (!landlineContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", landlineContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isExternalBroker()) {
            if (!landlineContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", landlineContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit {} Contract - User is not super admin", landlineContract.getSupplyType());
            return "redirect:/unauthorised";
        }

        List<String> transferMessageList = brokerTransferHistoryService.findLatestLandlineBrokerTransferHistory(landlineContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("customerSite", customerSiteService.findById(landlineContract.getCustomerSite().getId()));
        model.addAttribute("landlineContract", landlineContract);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("contractReasons", contractReasonService.findAll());

        return "admin/customer/manage-landline-contract";
    }
    @RequestMapping("/admin/landline/callbacks/{page}")
    public String viewLandlineCallbacks(LandlineContractSearch landlineContractSearch,
                                               Model model,
                                               @PathVariable("page") int page) {

        landlineContractSearch.setCallbackSearch(true);

        long startTime = System.currentTimeMillis();
        LandlineSearchResult landlineSearchResult =
                landlineContractSearchService.searchLandlinesContract(landlineContractSearch, page);

        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("callbacks", landlineSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", landlineSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", landlineSearchResult.getTotalPages());
        model.addAttribute("totalContracts", landlineSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/landline/callbacks";
    }
    @RequestMapping("/admin/landline/lost-renewals/{page}")
    public String viewLostLandlineRenewals(LandlineContractSearch landlineContractSearch, Model model, @PathVariable("page") int page) {

        landlineContractSearch.setLostRenewalSearch(true);
        landlineContractSearch.setLostRenewal(true);

        long startTime = System.currentTimeMillis();
        LandlineSearchResult landlineSearchResult =
                landlineContractSearchService.searchLandlinesContract(landlineContractSearch, page);

        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("lostRenewals", landlineSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", landlineSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", landlineSearchResult.getTotalPages());
        model.addAttribute("totalContracts", landlineSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/landline/lost-renewals";
    }
    @RequestMapping("/admin/landline/renewals/{page}")
    public String viewLandlineRenewals(LandlineContractSearch landlineContractSearch,
                                       Model model,
                                       @PathVariable("page") int page) {

        landlineContractSearch.setRenewalSearch(true);
        long startTime = System.currentTimeMillis();
        LandlineSearchResult landlineSearchResult =
                landlineContractSearchService.searchLandlinesContract(landlineContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("renewals", landlineSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", landlineSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", landlineSearchResult.getTotalPages());
        model.addAttribute("totalContracts", landlineSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/landline/renewals";
    }

    @RequestMapping("/admin/landline/leads/{page}")
    public String viewLandlineLeads(LandlineContractSearch landlineContractSearch, Model model, @PathVariable("page") int page) {

        landlineContractSearch.setLeadSearch(true);
        long startTime = System.currentTimeMillis();
        LandlineSearchResult landlineSearchResult =
                landlineContractSearchService.searchLandlinesContract(landlineContractSearch, page);
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("leads", landlineSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", landlineSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", landlineSearchResult.getTotalPages());
        model.addAttribute("totalContracts", landlineSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/landline/leads";
    }
}
