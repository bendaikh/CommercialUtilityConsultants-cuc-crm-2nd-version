package mycrm.controllers;

import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.search.LeadSearchService;
import mycrm.services.BrokerService;
import mycrm.services.ContractService;
import mycrm.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LeadController {

    private final SupplierService supplierService;
    private final BrokerService brokerService;
    private final LeadSearchService leadSearchService;
    private final ContractService contractService;

    @Autowired
    public LeadController(SupplierService supplierService, BrokerService brokerService,
                          LeadSearchService leadSearchService, ContractService contractService) {
        this.supplierService = supplierService;
        this.brokerService = brokerService;
        this.leadSearchService = leadSearchService;
        this.contractService = contractService;
    }

    @RequestMapping("/admin/leads/index/{pageNumber}")
    public String leadsHomepage(ContractSearch contractSearch, Model model, @PathVariable("pageNumber") int pageNumber) {

        long startTime = System.currentTimeMillis();
        ContractSearchResult contractSearchResult = leadSearchService.searchCustomerContract(contractSearch, pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("suppliers", supplierService.findAllOrderByBusinessName());
        model.addAttribute("leads", contractSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", contractSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", contractSearchResult.getTotalPages());
        model.addAttribute("totalContracts", contractSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("timeTaken", timeTaken);

        return "admin/leads/index";
    }

}
