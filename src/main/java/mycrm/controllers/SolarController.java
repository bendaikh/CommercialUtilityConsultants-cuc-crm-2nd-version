package mycrm.controllers;


import mycrm.models.*;
import mycrm.search.BroadbandContractSearchService;
import mycrm.search.SolarContractSearchService;
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
public class SolarController {
    private final ContractReasonService contractReasonService;
    private final DoNotRenewReasonService doNotRenewReasonService;
    private final SupplierService supplierService;
    private final BrokerTransferHistoryService brokerTransferHistoryService;
    private final BrokerService brokerService;
    private final CustomerSiteService customerSiteService;
    private static final Logger logger = LogManager.getLogger();
    private final SolarContractService solarContractService;
    private final UserService userService;
    private final SolarContractSearchService solarContractSearchService;

    @Autowired
    public SolarController(SolarContractSearchService solarContractSearchService,CustomerSiteService customerSiteService,BrokerTransferHistoryService brokerTransferHistoryService,UserService userService,BrokerService brokerService,ContractReasonService contractReasonService,SupplierService supplierService,SolarContractService solarContractService,DoNotRenewReasonService doNotRenewReasonService){
        this.doNotRenewReasonService = doNotRenewReasonService;
        this.supplierService = supplierService;
        this.solarContractService=solarContractService;
        this.contractReasonService = contractReasonService;
        this.brokerService = brokerService;
        this.customerSiteService = customerSiteService;
        this.userService = userService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
        this.solarContractSearchService = solarContractSearchService;
    }

    @RequestMapping(value = "/solarContract", method = RequestMethod.POST)
    public String saveSolarContract(SolarContract solarContract) {
        SolarContract contract = solarContractService.save(solarContract);
        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }
    @RequestMapping("/admin/customer/solar/{customerSiteID}")
    public String newSolarContract(@PathVariable String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        SolarContract solarContract = new SolarContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("doNotRenewReasons",doNotRenewReasonService.findAll());
        model.addAttribute("lostRenewalReasons",contractReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("solarContract", solarContract);
        return "admin/customer/manage-solar-contract";
    }
    @RequestMapping("/admin/customer/edit-solar-contract/{id}")
    public String editSolarContract(@PathVariable("id") Long id, Model model) {
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        SolarContract solarContract = solarContractService.findById(id);

        if (solarContract == null) {
            return "admin/error/404";
        }

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isBroker()) {
            if (!solarContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", solarContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isExternalBroker()) {
            if (!solarContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", solarContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit {} Contract - User is not super admin", solarContract.getSupplyType());
            return "redirect:/unauthorised";
        }

        List<String> transferMessageList = brokerTransferHistoryService.findLatestSolarBrokerTransferHistory(solarContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("customerSite", customerSiteService.findById(solarContract.getCustomerSite().getId()));
        model.addAttribute("solarContract", solarContract);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("contractReasons", contractReasonService.findAll());

        return "admin/customer/manage-solar-contract";
    }

    @RequestMapping("/admin/solar/index/{pageNumber}")
    public String viewSolarHomePage(SolarContractSearch solarContractSearch,
                                        Model model,
                                        @PathVariable("pageNumber") int pageNumber) {
        long startTime = System.currentTimeMillis();
        SolarSearchResult solarSearchResult =
                solarContractSearchService.searchSolarContract(solarContractSearch,
                        pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        solarSearchResult.getReturnedContracts().forEach(solarContract -> {
            logger.info("{}", solarContract.getId());
        });

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("contracts", solarSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", solarSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", solarSearchResult.getTotalPages());
        model.addAttribute("totalContracts", solarSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/solar/index";
    }

    @RequestMapping("/admin/solar/callbacks/{page}")
    public String viewSolarCallbacks(SolarContractSearch solarContractSearch,
                                        Model model,
                                        @PathVariable("page") int page) {

        solarContractSearch.setCallbackSearch(true);

        long startTime = System.currentTimeMillis();
        SolarSearchResult solarSearchResult =
                solarContractSearchService.searchSolarContract(solarContractSearch, page);

        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("callbacks", solarSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", solarSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", solarSearchResult.getTotalPages());
        model.addAttribute("totalContracts", solarSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/solar/callbacks";
    }

    @RequestMapping("/admin/solar/lost-renewals/{page}")
    public String viewLostSolarRenewals(SolarContractSearch solarContractSearch, Model model, @PathVariable("page") int page) {

        solarContractSearch.setLostRenewalSearch(true);
        solarContractSearch.setLostRenewal(true);

        long startTime = System.currentTimeMillis();
        SolarSearchResult solarSearchResult =
                solarContractSearchService.searchSolarContract(solarContractSearch, page);

        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("lostRenewals", solarSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", solarSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", solarSearchResult.getTotalPages());
        model.addAttribute("totalContracts", solarSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/solar/lost-renewals";
    }

    @RequestMapping("/admin/solar/renewals/{page}")
    public String viewSolarRenewals(SolarContractSearch solarContractSearch,
                                        Model model,
                                        @PathVariable("page") int page) {

        solarContractSearch.setRenewalSearch(true);
        long startTime = System.currentTimeMillis();
        SolarSearchResult solarSearchResult =
                solarContractSearchService.searchSolarContract(solarContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("renewals", solarSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", solarSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", solarSearchResult.getTotalPages());
        model.addAttribute("totalContracts", solarSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/solar/renewals";
    }

    @RequestMapping("/admin/solar/leads/{page}")
    public String viewSolarLeads(SolarContractSearch solarContractSearch, Model model, @PathVariable("page") int page) {

        solarContractSearch.setLeadSearch(true);
        long startTime = System.currentTimeMillis();
        SolarSearchResult solarSearchResult =
                solarContractSearchService.searchSolarContract(solarContractSearch, page);
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("leads", solarSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", solarSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", solarSearchResult.getTotalPages());
        model.addAttribute("totalContracts", solarSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/solar/leads";
    }
}
