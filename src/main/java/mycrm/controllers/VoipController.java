package mycrm.controllers;

import mycrm.models.*;
import mycrm.search.VoipContractSearchService;
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
public class VoipController {

    @Autowired
    private ContractReasonService contractReasonService;
    @Autowired
    private DoNotRenewReasonService doNotRenewReasonService;
    @Autowired
    private CustomerSiteService customerSiteService;
    @Autowired
    private BrokerTransferHistoryService brokerTransferHistoryService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private BrokerService brokerService;
    @Autowired
    private UserService userService;
    @Autowired
    private VoipContractSearchService voipContractSearchService;
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private VoipContractService voipContractService;
    @RequestMapping(value = "/voipContract", method = RequestMethod.POST)
    public String saveLandlineContract(VoipContract voipContract) {
        VoipContract contract = voipContractService.save(voipContract);
        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }

    @RequestMapping("/admin/customer/voip/{customerSiteID}")
    public String newVoipContract(@PathVariable String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        VoipContract voipContract = new VoipContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("contractReasons", doNotRenewReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("voipContract", voipContract);
        return "admin/customer/manage-voip-contract";
    }

    @RequestMapping("/admin/customer/edit-voip-contract/{id}")
    public String editVoipContract(@PathVariable("id") Long id, Model model) {
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        VoipContract voipContract = voipContractService.findById(id);

        if (voipContract == null) {
            return "admin/error/404";
        }

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isBroker()) {
            if (!voipContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", voipContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isExternalBroker()) {
            if (!voipContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract", voipContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit {} Contract - User is not super admin", voipContract.getSupplyType());
            return "redirect:/unauthorised";
        }

        List<String> transferMessageList = brokerTransferHistoryService.findLatestVoipBrokerTransferHistory(voipContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("customerSite", customerSiteService.findById(voipContract.getCustomerSite().getId()));
        model.addAttribute("voipContract", voipContract);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("contractReasons", contractReasonService.findAll());

        return "admin/customer/manage-voip-contract";
    }

    @RequestMapping("/admin/voip/index/{pageNumber}")
    public String viewVoipHomePage(VoipContractSearch voipContractSearch,
                                        Model model,
                                        @PathVariable("pageNumber") int pageNumber) {
        long startTime = System.currentTimeMillis();
        VoipSearchResult voipSearchResult =
                voipContractSearchService.searchVoipContract(voipContractSearch,
                        pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        voipSearchResult.getReturnedContracts().forEach(voipContract -> {
            logger.info("{}", voipContract.getId());
        });

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("contracts", voipSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", voipSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", voipSearchResult.getTotalPages());
        model.addAttribute("totalContracts", voipSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/voip/index";
    }

    @RequestMapping("/admin/voip/callbacks/{page}")
    public String viewVoipCallbacks(VoipContractSearch voipContractSearch,
                                        Model model,
                                        @PathVariable("page") int page) {

        voipContractSearch.setCallbackSearch(true);

        long startTime = System.currentTimeMillis();
        VoipSearchResult voipSearchResult =
                voipContractSearchService.searchVoipContract(voipContractSearch, page);

        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("callbacks", voipSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", voipSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", voipSearchResult.getTotalPages());
        model.addAttribute("totalContracts", voipSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/voip/callbacks";
    }

    @RequestMapping("/admin/voip/lost-renewals/{page}")
    public String viewLostVoipRenewals(VoipContractSearch voipContractSearch, Model model, @PathVariable("page") int page) {

        voipContractSearch.setLostRenewalSearch(true);
        voipContractSearch.setLostRenewal(true);

        long startTime = System.currentTimeMillis();
        VoipSearchResult voipSearchResult =
                voipContractSearchService.searchVoipContract(voipContractSearch, page);

        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("lostRenewals", voipSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", voipSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", voipSearchResult.getTotalPages());
        model.addAttribute("totalContracts", voipSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/voip/lost-renewals";
    }

    @RequestMapping("/admin/voip/renewals/{page}")
    public String viewVoipRenewals(VoipContractSearch voipContractSearch,
                                       Model model,
                                       @PathVariable("page") int page) {

        voipContractSearch.setRenewalSearch(true);
        long startTime = System.currentTimeMillis();
        VoipSearchResult voipSearchResult =
                voipContractSearchService.searchVoipContract(voipContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("renewals", voipSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", voipSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", voipSearchResult.getTotalPages());
        model.addAttribute("totalContracts", voipSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/voip/renewals";
    }

    @RequestMapping("/admin/voip/leads/{page}")
    public String viewVoipLeads(VoipContractSearch voipContractSearch, Model model, @PathVariable("page") int page) {

        voipContractSearch.setLeadSearch(true);
        long startTime = System.currentTimeMillis();
        VoipSearchResult voipSearchResult =
                voipContractSearchService.searchVoipContract(voipContractSearch, page);
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("leads", voipSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", voipSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", voipSearchResult.getTotalPages());
        model.addAttribute("totalContracts", voipSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/voip/leads";
    }
}
