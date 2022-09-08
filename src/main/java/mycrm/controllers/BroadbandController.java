package mycrm.controllers;

import mycrm.models.*;
import mycrm.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class BroadbandController {

    @Autowired
    private ContractReasonService contractReasonService;

    @Autowired
    private DoNotRenewReasonService doNotRenewReasonService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private BrokerService brokerService;
    @Autowired
    private CustomerSiteService customerSiteService;
    @Autowired
    private BroadbandContractService broadbandContractService;

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
        model.addAttribute("contractReasons", doNotRenewReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("broadbandContract", broadbandContract);
        return "admin/customer/manage-broadband-contract";
    }
}
