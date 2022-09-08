package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.LandlineContract;
import mycrm.models.MerchantServicesContract;
import mycrm.models.Supplier;
import mycrm.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class LandlineController {
    @Autowired
    private ContractReasonService contractReasonService;

    @Autowired
    private DoNotRenewReasonService doNotRenewReasonService;
    @Autowired
    private CustomerSiteService customerSiteService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private BrokerService brokerService;
    @Autowired
    private LandlineContractService landlineContractService;
    @RequestMapping(value = "/landlineContract", method = RequestMethod.POST)
    public String saveLandlineContract(LandlineContract landlineContract) {
        LandlineContract contract = landlineContractService.save(landlineContract);
        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }

    @RequestMapping("/admin/customer/landline/{customerSiteID}")
    public String newLandlineContract(@PathVariable String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        LandlineContract landlineContract = new LandlineContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("contractReasons", doNotRenewReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("landlineContract", landlineContract);
        return "admin/customer/manage-landline-contract";
    }
}
