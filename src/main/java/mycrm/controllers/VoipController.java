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
public class VoipController {

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
}
