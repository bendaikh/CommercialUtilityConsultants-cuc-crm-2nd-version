package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.Supplier;
import mycrm.models.VoipContract;
import mycrm.models.WaterContract;
import mycrm.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class WaterController {
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
    private WaterContractService waterContractService;
    @RequestMapping(value = "/waterContract", method = RequestMethod.POST)
    public String saveWaterContract(WaterContract waterContract) {
        WaterContract contract = waterContractService.save(waterContract);
        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }

    @RequestMapping("/admin/customer/water/{customerSiteID}")
    public String newWaterContract(@PathVariable String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        WaterContract waterContract = new WaterContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("contractReasons", doNotRenewReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("waterContract", waterContract);
        return "admin/customer/manage-water-contract";
    }
}
