package mycrm.controllers;


import mycrm.models.*;
import mycrm.search.MobileContractSearchService;
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
public class MobileController {
    private final ContractReasonService contractReasonService;
    private final DoNotRenewReasonService doNotRenewReasonService;
    private final SupplierService supplierService;
    private final BrokerTransferHistoryService brokerTransferHistoryService;
    private final BrokerService brokerService;
    private final CustomerSiteService customerSiteService;
    private static final Logger logger = LogManager.getLogger();
    private final MobileContractService mobileContractService;
    private final UserService userService;

    private final MobileContractSearchService mobileContractSearchService;

    @Autowired
    public MobileController(MobileContractSearchService mobileContractSearchService,CustomerSiteService customerSiteService,BrokerTransferHistoryService brokerTransferHistoryService,UserService userService,BrokerService brokerService,ContractReasonService contractReasonService,SupplierService supplierService,MobileContractService mobileContractService,DoNotRenewReasonService doNotRenewReasonService){
        this.doNotRenewReasonService = doNotRenewReasonService;
        this.supplierService = supplierService;
        this.mobileContractService = mobileContractService;
        this.contractReasonService = contractReasonService;
        this.brokerService = brokerService;
        this.customerSiteService = customerSiteService;
        this.userService = userService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
        this.mobileContractSearchService = mobileContractSearchService;
    }

    @RequestMapping(value = "/mobileContract", method = RequestMethod.POST)
    public String saveSolarContract(MobileContract mobileContract) {
        MobileContract contract = mobileContractService.save(mobileContract);
        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }
    @RequestMapping("/admin/customer/mobile/{customerSiteID}")
    public String newMobileContract(@PathVariable String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        MobileContract mobileContract = new MobileContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("doNotRenewReasons",doNotRenewReasonService.findAll());
        model.addAttribute("lostRenewalReasons",contractReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("mobileContract", mobileContract);
        return "admin/customer/manage-mobile-contract";
    }
    @RequestMapping("/admin/customer/edit-mobile-contract/{id}")
    public String editMobileContract(@PathVariable("id") Long id, Model model) {
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        MobileContract mobileContract = mobileContractService.findById(id);

        if (mobileContract == null) {
            return "admin/error/404";
        }

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isBroker()) {
            if (!mobileContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", mobileContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isExternalBroker()) {
            if (!mobileContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", mobileContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit {} Contract - User is not super admin", mobileContract.getSupplyType());
            return "redirect:/unauthorised";
        }

        List<String> transferMessageList = brokerTransferHistoryService.findLatestMobileBrokerTransferHistory(mobileContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("customerSite", customerSiteService.findById(mobileContract.getCustomerSite().getId()));
        model.addAttribute("mobileContract", mobileContract);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("contractReasons", contractReasonService.findAll());

        return "admin/customer/manage-mobile-contract";
    }

    @RequestMapping("/admin/mobile/index/{pageNumber}")
    public String viewMobileHomePage(MobileContractSearch mobileContractSearch,
                                    Model model,
                                    @PathVariable("pageNumber") int pageNumber) {
        long startTime = System.currentTimeMillis();
        MobileSearchResult mobileSearchResult =
                mobileContractSearchService.searchMobileContract(mobileContractSearch,
                        pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        mobileSearchResult.getReturnedContracts().forEach(mobileContract -> {
            logger.info("{}", mobileContract.getId());
        });

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("contracts", mobileSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", mobileSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", mobileSearchResult.getTotalPages());
        model.addAttribute("totalContracts", mobileSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/mobile/index";
    }

    @RequestMapping("/admin/mobile/callbacks/{page}")
    public String viewMobileCallbacks(MobileContractSearch mobileContractSearch,
                                     Model model,
                                     @PathVariable("page") int page) {

        mobileContractSearch.setCallbackSearch(true);

        long startTime = System.currentTimeMillis();
        MobileSearchResult mobileSearchResult =
                mobileContractSearchService.searchMobileContract(mobileContractSearch, page);

        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("callbacks", mobileSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", mobileSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", mobileSearchResult.getTotalPages());
        model.addAttribute("totalContracts", mobileSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/mobile/callbacks";
    }

    @RequestMapping("/admin/mobile/lost-renewals/{page}")
    public String viewLostMobileRenewals(MobileContractSearch mobileContractSearch, Model model, @PathVariable("page") int page) {

        mobileContractSearch.setLostRenewalSearch(true);
        mobileContractSearch.setLostRenewal(true);

        long startTime = System.currentTimeMillis();
        MobileSearchResult mobileSearchResult =
                mobileContractSearchService.searchMobileContract(mobileContractSearch, page);

        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("lostRenewals", mobileSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", mobileSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", mobileSearchResult.getTotalPages());
        model.addAttribute("totalContracts", mobileSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/mobile/lost-renewals";
    }

    @RequestMapping("/admin/mobile/renewals/{page}")
    public String viewMobileRenewals(MobileContractSearch mobileContractSearch,
                                    Model model,
                                    @PathVariable("page") int page) {

        mobileContractSearch.setRenewalSearch(true);
        long startTime = System.currentTimeMillis();
        MobileSearchResult mobileSearchResult =
                mobileContractSearchService.searchMobileContract(mobileContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("renewals", mobileSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", mobileSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", mobileSearchResult.getTotalPages());
        model.addAttribute("totalContracts", mobileSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/mobile/renewals";
    }

    @RequestMapping("/admin/mobile/leads/{page}")
    public String viewMobileLeads(MobileContractSearch mobileContractSearch, Model model, @PathVariable("page") int page) {

        mobileContractSearch.setLeadSearch(true);
        long startTime = System.currentTimeMillis();
        MobileSearchResult mobileSearchResult =
                mobileContractSearchService.searchMobileContract(mobileContractSearch, page);
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("leads", mobileSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", mobileSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", mobileSearchResult.getTotalPages());
        model.addAttribute("totalContracts", mobileSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/mobile/leads";
    }
}
