package mycrm.controllers;

import mycrm.models.*;
import mycrm.search.UtilitySearchService;
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
public class UtilityContractController {

    private static final Logger logger = LogManager.getLogger();

    private final CustomerSiteService customerSiteService;
    private final UtilityContractService utilityContractService;
    private final SupplierService supplierService;
    private final BrokerService brokerService;
    private final ContractService contractService;
    private final UtilitySearchService utilitySearchService;
    private final UserService userService;

    private final ContractReasonService contractReasonService;

    private final BrokerTransferHistoryService brokerTransferHistoryService;

    @Autowired
    public UtilityContractController(CustomerSiteService customerSiteService,
                                     UtilityContractService utilityContractService,
                                     SupplierService supplierService,
                                     BrokerService brokerService,
                                     ContractService contractService,
                                     UtilitySearchService utilitySearchService,
                                     UserService userService,
                                     BrokerTransferHistoryService brokerTransferHistoryService,
                                     ContractReasonService contractReasonService) {
        this.customerSiteService = customerSiteService;
        this.utilityContractService = utilityContractService;
        this.supplierService = supplierService;
        this.brokerService = brokerService;
        this.contractService = contractService;
        this.utilitySearchService = utilitySearchService;
        this.userService = userService;
        this.contractReasonService = contractReasonService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
    }
    // comment this after finish the code
    @RequestMapping("/admin/customer/manage-utility-contract/{customerSiteID}")
    public String newUtilityContract(@PathVariable String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        UtilityContract utilityContract = new UtilityContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("contractReasons", contractReasonService.findAll());
        model.addAttribute("utilityContract", utilityContract);
        return "admin/customer/manage-utility-contract";
    }

    @RequestMapping(value = "/utilityContract", method = RequestMethod.POST)
    public String saveUtilityContract(UtilityContract utilityContract) {
        UtilityContract contract = utilityContractService.save(utilityContract);

        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }

    @RequestMapping("/admin/customer/edit-utility-contract/{id}")
    public String editUtilityContract(@PathVariable("id") Long id, Model model) {
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        UtilityContract utilityContract = utilityContractService.findById(id);

        if (utilityContract == null) {
            return "admin/error/404";
        }

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isBroker()) {
            if (!utilityContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", utilityContract.getUtilityType());
                return "redirect:/unauthorised";
            }
        } else if (user.isExternalBroker()) {
            if (!utilityContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", utilityContract.getUtilityType());
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit {} Contract - User is not super admin", utilityContract.getUtilityType());
            return "redirect:/unauthorised";
        }

        List<String> transferMessageList = brokerTransferHistoryService.findLatestUtilityBrokerTransferHistory(utilityContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("customerSite", customerSiteService.findById(utilityContract.getCustomerSite().getId()));
        model.addAttribute("utilityContract", utilityContract);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("contractReasons", contractReasonService.findAll());

        return "admin/customer/manage-utility-contract";
    }

    @RequestMapping("/admin/utility/index/{page}")
    public String viewUtilityContracts(ContractSearch contractSearch, Model model, @PathVariable("page") int page) {

        contractSearch.setCustomerContractSearch(true);

        long startTime = System.currentTimeMillis();
        UtilitySearchResult utilitySearchResult = utilitySearchService.searchUtilityContracts(contractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("suppliers", supplierService.findAllOrderByBusinessName());
        model.addAttribute("contracts", utilitySearchResult.getReturnedContracts());
        model.addAttribute("totalResults", utilitySearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", utilitySearchResult.getTotalPages());
        model.addAttribute("totalContracts", utilitySearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("timeTaken", timeTaken);

        return "admin/utility/index";
    }

    @RequestMapping("/admin/utility/lost-renewals/{page}")
    public String viewLostUtilityRenewals(ContractSearch contractSearch, Model model, @PathVariable("page") int page) {

        contractSearch.setLostRenewalSearch(true);
        contractSearch.setLostRenewal(true);

        long startTime = System.currentTimeMillis();
        UtilitySearchResult utilitySearchResult = utilitySearchService.searchUtilityContracts(contractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("suppliers", supplierService.findAllOrderByBusinessName());
        model.addAttribute("lostRenewals", utilitySearchResult.getReturnedContracts());
        model.addAttribute("totalResults", utilitySearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", utilitySearchResult.getTotalPages());
        model.addAttribute("totalContracts", utilitySearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("timeTaken", timeTaken);

        return "admin/utility/lost-renewals";
    }

    @RequestMapping("/admin/utility/leads/{page}")
    public String viewUtilityLeads(ContractSearch contractSearch, Model model, @PathVariable("page") int page) {

        contractSearch.setLeadSearch(true);

        long startTime = System.currentTimeMillis();
        UtilitySearchResult utilitySearchResult = utilitySearchService.searchUtilityContracts(contractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("suppliers", supplierService.findAllOrderByBusinessName());
        model.addAttribute("leads", utilitySearchResult.getReturnedContracts());
        model.addAttribute("totalResults", utilitySearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", utilitySearchResult.getTotalPages());
        model.addAttribute("totalContracts", utilitySearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("timeTaken", timeTaken);

        return "admin/utility/leads";
    }

    @RequestMapping("/admin/utility/renewals/{page}")
    public String viewUtilityRenewals(ContractSearch contractSearch, Model model, @PathVariable("page") int page) {

        contractSearch.setRenewalSearch(true);

        long startTime = System.currentTimeMillis();
        UtilitySearchResult utilitySearchResult = utilitySearchService.searchUtilityContracts(contractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("suppliers", supplierService.findAllOrderByBusinessName());
        model.addAttribute("renewals", utilitySearchResult.getReturnedContracts());
        model.addAttribute("totalResults", utilitySearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", utilitySearchResult.getTotalPages());
        model.addAttribute("totalContracts", utilitySearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("timeTaken", timeTaken);

        return "admin/utility/renewals";
    }

    @RequestMapping("/admin/utility/callbacks/{page}")
    public String viewUtilityCallbacks(ContractSearch contractSearch, Model model, @PathVariable("page") int page) {

        contractSearch.setCallbackSearch(true);

        long startTime = System.currentTimeMillis();
        UtilityCallbackSearchResult utilitySearchResult = utilitySearchService.searchNotedCallbackContracts(contractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("suppliers", supplierService.findAllOrderByBusinessName());
        model.addAttribute("callbacks", utilitySearchResult.getReturnedContracts());
        model.addAttribute("totalResults", utilitySearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", utilitySearchResult.getTotalPages());
        model.addAttribute("totalContracts", utilitySearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("timeTaken", timeTaken);

        return "admin/utility/callbacks";
    }
}
