package mycrm.controllers;

import mycrm.models.CallbackSearchResult;
import mycrm.models.ContractSearch;
import mycrm.models.NotedCallback;
import mycrm.search.CallbackSearchService;
import mycrm.services.BrokerService;
import mycrm.services.ContractService;
import mycrm.services.SupplierService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CallbackController {

    private static final Logger logger = LogManager.getLogger();

    private final SupplierService supplierService;
    private final BrokerService brokerService;
    private final CallbackSearchService callbackSearchService;
    private final ContractService contractService;

    @Autowired
    public CallbackController(SupplierService supplierService, BrokerService brokerService,
                              CallbackSearchService callbackSearchService, ContractService contractService) {
        this.supplierService = supplierService;
        this.brokerService = brokerService;
        this.callbackSearchService = callbackSearchService;
        this.contractService = contractService;
    }

    @RequestMapping("/admin/callbacks/index/{pageNumber}")
    public String callbacksHomepage(ContractSearch contractSearch, Model model, @PathVariable("pageNumber") int pageNumber) {

        long startTime = System.currentTimeMillis();
        CallbackSearchResult callbackSearchResult = callbackSearchService.searchCustomerContract(contractSearch, pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        List<NotedCallback> returnedContracts = callbackSearchResult.getReturnedContracts();

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("suppliers", supplierService.findAllOrderByBusinessName());
        model.addAttribute("callbacks", returnedContracts);
        model.addAttribute("totalResults", callbackSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", callbackSearchResult.getTotalPages());
        model.addAttribute("totalContracts", callbackSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("timeTaken", timeTaken);

        return "admin/callbacks/index";
    }

}
