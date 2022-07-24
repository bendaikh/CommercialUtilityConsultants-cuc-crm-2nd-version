package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.MerchantServicesContract;
import mycrm.models.MerchantServicesContractSearch;
import mycrm.models.MerchantServicesSearchResult;
import mycrm.models.User;
import mycrm.search.MerchantServicesContractSearchService;
import mycrm.services.BrokerService;
import mycrm.services.BrokerTransferHistoryService;
import mycrm.services.CustomerSiteService;
import mycrm.services.MerchantServicesService;
import mycrm.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class MerchantServicesController {

    private static final Logger logger = LogManager.getLogger();

    private final CustomerSiteService customerSiteService;
    private final BrokerService brokerService;
    private final MerchantServicesService merchantServicesService;
    private final MerchantServicesContractSearchService merchantServicesContractSearchService;
    private final UserService userService;
    private final BrokerTransferHistoryService brokerTransferHistoryService;

    @Autowired
    public MerchantServicesController(CustomerSiteService customerSiteService,
                                      BrokerService brokerService,
                                      MerchantServicesService merchantServicesService,
                                      MerchantServicesContractSearchService merchantServicesContractSearchService,
                                      UserService userService,
                                      BrokerTransferHistoryService brokerTransferHistoryService) {
        this.customerSiteService = customerSiteService;
        this.brokerService = brokerService;
        this.merchantServicesService = merchantServicesService;
        this.merchantServicesContractSearchService = merchantServicesContractSearchService;
        this.userService = userService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
    }

    @RequestMapping("/admin/customer/manage-merchant-services/{customerSiteID}")
    public String merchantServicesContract(@PathVariable String customerSiteID, Model model) {
        List<Broker> brokers = brokerService.findAll();

        MerchantServicesContract merchantServicesContract = new MerchantServicesContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("merchantServicesContract", merchantServicesContract);
        return "admin/customer/manage-merchant-services";
    }

    @RequestMapping("/admin/customer/edit-merchant-services/{id}")
    public String editMerchantServicesContract(@PathVariable("id") Long id, Model model) {
        List<Broker> brokers = brokerService.findAll();

        MerchantServicesContract merchantServicesContract = merchantServicesService.findById(id);

        if (merchantServicesContract == null) {
            return "admin/error/404";
        }

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isBroker()) {
            if (!merchantServicesContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract",
                        merchantServicesContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isExternalBroker()) {
            if (!merchantServicesContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract",
                        merchantServicesContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit {} Contract - User is not super admin", merchantServicesContract.getSupplyType());
            return "redirect:/unauthorised";
        }

        List<String> transferMessageList =
                brokerTransferHistoryService.findLatestMerchantServicesBrokerTransferHistory(merchantServicesContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("brokers", brokers);
        model.addAttribute("customerSite", customerSiteService.findById(merchantServicesContract.getCustomerSite().getId()));
        model.addAttribute("merchantServicesContract", merchantServicesContract);
        model.addAttribute("users", userService.findAll());

        return "admin/customer/manage-merchant-services";
    }

    @RequestMapping(value = "/merchantServicesContract", method = RequestMethod.POST)
    public String saveMerchantServicesContract(MerchantServicesContract merchantServicesContract) {
        MerchantServicesContract contract = merchantServicesService.save(merchantServicesContract);

        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }

    @RequestMapping("/admin/merchant-services/index/{pageNumber}")
    public String viewMerchantServicesHomePage(MerchantServicesContractSearch merchantServicesContractSearch,
                                               Model model,
                                               @PathVariable("pageNumber") int pageNumber) {
        long startTime = System.currentTimeMillis();
        MerchantServicesSearchResult merchantServicesSearchResult =
                merchantServicesContractSearchService.searchMerchantServicesContract(merchantServicesContractSearch,
                        pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        merchantServicesSearchResult.getReturnedContracts().forEach(merchantServicesContract -> {
            logger.info("{}", merchantServicesContract.getId());
        });

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("contracts", merchantServicesSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", merchantServicesSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", merchantServicesSearchResult.getTotalPages());
        model.addAttribute("totalContracts", merchantServicesSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/merchant-services/index";
    }

    @RequestMapping("/admin/merchant-services/renewals/{page}")
    public String viewMerchantServiceRenewals(MerchantServicesContractSearch merchantServicesContractSearch,
                                              Model model,
                                              @PathVariable("page") int page) {

        merchantServicesContractSearch.setRenewalSearch(true);

        long startTime = System.currentTimeMillis();
        MerchantServicesSearchResult merchantServicesSearchResult =
                merchantServicesContractSearchService.searchMerchantServicesContract(merchantServicesContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("renewals", merchantServicesSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", merchantServicesSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", merchantServicesSearchResult.getTotalPages());
        model.addAttribute("totalContracts", merchantServicesSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);

        return "admin/merchant-services/renewals";
    }

    @RequestMapping("/admin/merchant-services/callbacks/{page}")
    public String viewMerchantServiceCallbacks(MerchantServicesContractSearch merchantServicesContractSearch,
                                               Model model,
                                               @PathVariable("page") int page) {

        merchantServicesContractSearch.setCallbackSearch(true);

        long startTime = System.currentTimeMillis();
        MerchantServicesSearchResult merchantServicesSearchResult =
                merchantServicesContractSearchService.searchMerchantServicesContract(merchantServicesContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("callbacks", merchantServicesSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", merchantServicesSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", merchantServicesSearchResult.getTotalPages());
        model.addAttribute("totalContracts", merchantServicesSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);

        return "admin/merchant-services/callbacks";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/customer/deleteMerchantContract/{id}")
    public String deleteMerchantContract(@PathVariable("id") Long id) {
        MerchantServicesContract merchantServicesContract = merchantServicesService.findById(id);

        if (merchantServicesContract != null) {
            merchantServicesService.deleteById(id);
            return "redirect:/admin/customer/viewsite/" + merchantServicesContract.getCustomerSite().getId();
        }
        return "redirect:/admin/index";
    }
}
