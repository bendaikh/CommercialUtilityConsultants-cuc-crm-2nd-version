package mycrm.controllers;

import mycrm.models.*;
import mycrm.search.BroadbandContractSearchService;
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
public class BroadbandController {

    private final ContractReasonService contractReasonService;
    private final DoNotRenewReasonService doNotRenewReasonService;
    private final SupplierService supplierService;
    private final BrokerTransferHistoryService brokerTransferHistoryService;
    private final BrokerService brokerService;
    private final CustomerSiteService customerSiteService;
    private static final Logger logger = LogManager.getLogger();
    private final BroadbandContractService broadbandContractService;
    private final UserService userService;
    private final BroadbandContractSearchService broadbandContractSearchService;

    @Autowired
    public BroadbandController(BroadbandContractSearchService broadbandContractSearchService,CustomerSiteService customerSiteService,BrokerTransferHistoryService brokerTransferHistoryService,UserService userService,BrokerService brokerService,ContractReasonService contractReasonService,SupplierService supplierService,BroadbandContractService broadbandContractService,DoNotRenewReasonService doNotRenewReasonService){
        this.doNotRenewReasonService = doNotRenewReasonService;
        this.supplierService = supplierService;
        this.broadbandContractService=broadbandContractService;
        this.contractReasonService = contractReasonService;
        this.brokerService = brokerService;
        this.customerSiteService = customerSiteService;
        this.userService = userService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
        this.broadbandContractSearchService = broadbandContractSearchService;
    }

    @RequestMapping(value = "/broadbandContract", method = RequestMethod.POST)
    public String saveBroadbandContract(BroadbandContract broadbandContract) {
        BroadbandContract contract = broadbandContractService.save(broadbandContract);
        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }
    @RequestMapping("/admin/customer/broadband/{customerSiteID}")
    public String newBroadbandContract(@PathVariable String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        BroadbandContract broadbandContract = new BroadbandContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("doNotRenewReasons",doNotRenewReasonService.findAll());
        model.addAttribute("lostRenewalReasons",contractReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("broadbandContract", broadbandContract);
        return "admin/customer/manage-broadband-contract";
    }
    @RequestMapping("/admin/customer/edit-broadband-contract/{id}")
    public String editBroadbandContract(@PathVariable("id") Long id, Model model) {
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        BroadbandContract broadbandContract = broadbandContractService.findById(id);

        if (broadbandContract == null) {
            return "admin/error/404";
        }

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isBroker()) {
            if (!broadbandContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", broadbandContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isExternalBroker()) {
            if (!broadbandContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", broadbandContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit {} Contract - User is not super admin", broadbandContract.getSupplyType());
            return "redirect:/unauthorised";
        }

        List<String> transferMessageList = brokerTransferHistoryService.findLatestBroadbandBrokerTransferHistory(broadbandContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("customerSite", customerSiteService.findById(broadbandContract.getCustomerSite().getId()));
        model.addAttribute("broadbandContract", broadbandContract);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("contractReasons", contractReasonService.findAll());

        return "admin/customer/manage-broadband-contract";
    }

    @RequestMapping("/admin/broadband/index/{pageNumber}")
    public String viewBroadbandHomePage(BroadbandContractSearch broadbandContractSearch,
                                       Model model,
                                       @PathVariable("pageNumber") int pageNumber) {
        long startTime = System.currentTimeMillis();
        BroadbandSearchResult broadbandSearchResult =
                broadbandContractSearchService.searchBroadbandContract(broadbandContractSearch,
                        pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        broadbandSearchResult.getReturnedContracts().forEach(broadbandContract -> {
            logger.info("{}", broadbandContract.getId());
        });

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("contracts", broadbandSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", broadbandSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", broadbandSearchResult.getTotalPages());
        model.addAttribute("totalContracts", broadbandSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/broadband/index";
    }

    @RequestMapping("/admin/broadband/callbacks/{page}")
    public String viewLandlineCallbacks(BroadbandContractSearch broadbandContractSearch,
                                        Model model,
                                        @PathVariable("page") int page) {

        broadbandContractSearch.setCallbackSearch(true);

        long startTime = System.currentTimeMillis();
        BroadbandSearchResult broadbandSearchResult =
                broadbandContractSearchService.searchBroadbandContract(broadbandContractSearch, page);

        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("callbacks", broadbandSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", broadbandSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", broadbandSearchResult.getTotalPages());
        model.addAttribute("totalContracts", broadbandSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/broadband/callbacks";
    }

    @RequestMapping("/admin/broadband/lost-renewals/{page}")
    public String viewLostBroadbandRenewals(BroadbandContractSearch broadbandContractSearch, Model model, @PathVariable("page") int page) {

        broadbandContractSearch.setLostRenewalSearch(true);
        broadbandContractSearch.setLostRenewal(true);

        long startTime = System.currentTimeMillis();
        BroadbandSearchResult broadbandSearchResult =
                broadbandContractSearchService.searchBroadbandContract(broadbandContractSearch, page);

        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("lostRenewals", broadbandSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", broadbandSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", broadbandSearchResult.getTotalPages());
        model.addAttribute("totalContracts", broadbandSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/broadband/lost-renewals";
    }

    @RequestMapping("/admin/broadband/renewals/{page}")
    public String viewBroadbandRenewals(BroadbandContractSearch broadbandContractSearch,
                                       Model model,
                                       @PathVariable("page") int page) {

        broadbandContractSearch.setRenewalSearch(true);
        long startTime = System.currentTimeMillis();
        BroadbandSearchResult broadbandSearchResult =
                broadbandContractSearchService.searchBroadbandContract(broadbandContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("renewals", broadbandSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", broadbandSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", broadbandSearchResult.getTotalPages());
        model.addAttribute("totalContracts", broadbandSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/broadband/renewals";
    }

    @RequestMapping("/admin/broadband/leads/{page}")
    public String viewBroadbandLeads(BroadbandContractSearch broadbandContractSearch, Model model, @PathVariable("page") int page) {

        broadbandContractSearch.setLeadSearch(true);
        long startTime = System.currentTimeMillis();
        BroadbandSearchResult broadbandSearchResult =
                broadbandContractSearchService.searchBroadbandContract(broadbandContractSearch, page);
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("leads", broadbandSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", broadbandSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", broadbandSearchResult.getTotalPages());
        model.addAttribute("totalContracts", broadbandSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/broadband/leads";
    }

}
