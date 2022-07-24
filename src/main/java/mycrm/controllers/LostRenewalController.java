package mycrm.controllers;

import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.search.LostRenewalSearchService;
import mycrm.services.BrokerService;
import mycrm.services.ContractService;
import mycrm.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LostRenewalController {
    private final SupplierService supplierService;
    private final BrokerService brokerService;
    private final LostRenewalSearchService lostRenewalSearchService;
    private final ContractService contractService;

    @Autowired
    public LostRenewalController(SupplierService supplierService,
                                 BrokerService brokerService,
                                 LostRenewalSearchService lostRenewalSearchService,
                                 ContractService contractService) {
        this.supplierService = supplierService;
        this.brokerService = brokerService;
        this.lostRenewalSearchService = lostRenewalSearchService;
        this.contractService = contractService;
    }

    @RequestMapping("/admin/lost-renewals/index/{pageNumber}")
    public String lostRenewalsHomepage(ContractSearch contractSearch, Model model, @PathVariable("pageNumber") int pageNumber) {

        long startTime = System.currentTimeMillis();
        ContractSearchResult contractSearchResult = lostRenewalSearchService.searchCustomerContract(contractSearch, pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("suppliers", supplierService.findAllOrderByBusinessName());
        model.addAttribute("lostRenewals", contractSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", contractSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", contractSearchResult.getTotalPages());
        model.addAttribute("totalContracts", contractSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("timeTaken", timeTaken);

        return "admin/lost-renewals/index";
    }
}
