package mycrm.controllers;

import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.services.ElecContractService;
import mycrm.services.GasContractService;
import mycrm.services.LiveEmailService;
import mycrm.services.ObjectedEmailService;
import mycrm.services.RenewalEmailService;
import mycrm.services.SoldEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailServiceController {

    private static final Logger logger = LogManager.getLogger();

    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final RenewalEmailService renewalEmailService;
    private final LiveEmailService liveEmailService;
    private final ObjectedEmailService objectedEmailService;
    private final SoldEmailService soldEmailService;

    @Autowired
    public EmailServiceController(GasContractService gasContractService,
                                  ElecContractService elecContractService,
                                  RenewalEmailService renewalEmailService,
                                  LiveEmailService liveEmailService,
                                  ObjectedEmailService objectedEmailService,
                                  SoldEmailService soldEmailService) {
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.renewalEmailService = renewalEmailService;
        this.liveEmailService = liveEmailService;
        this.objectedEmailService = objectedEmailService;
        this.soldEmailService = soldEmailService;
    }


    @RequestMapping("/admin/customer/send-gas-renewal-email/{id}")
    public String sendGasManualRenewalEmail(@PathVariable("id") Long id, @RequestParam(name = "emailAddress", required = true) String emailAddress) {
        GasCustomerContract contract = gasContractService.findById(id);
        try {
            renewalEmailService.sendManualRenewalEmail(contract, emailAddress);
        } catch (Exception e) {
            logger.error("Did not send renewal email. {}", e.getMessage());
        }
        return "redirect:/admin/customer/customersites/" + contract.getCustomer().getId();
    }

    @RequestMapping("/admin/customer/send-electric-renewal-email/{id}")
    public String sendElectricManualRenewalEmail(@PathVariable("id") Long id, @RequestParam(name = "emailAddress", required = true) String emailAddress) {
        ElecCustomerContract contract = elecContractService.findById(id);
        try {
            renewalEmailService.sendManualRenewalEmail(contract, emailAddress);
        } catch (Exception e) {
            logger.error("Did not send renewal email. {}", e.getMessage());
        }
        return "redirect:/admin/customer/customersites/" + contract.getCustomer().getId();
    }

    @RequestMapping("/admin/customer/send-gas-live-email/{id}")
    public String sendGasManualLiveEmail(@PathVariable("id") Long id, @RequestParam(name = "emailAddress", required = true) String emailAddress) {
        GasCustomerContract contract = gasContractService.findById(id);
        try {
            liveEmailService.sendLiveEmail(contract, emailAddress);
        } catch (Exception e) {
            logger.error("Did not send live contract email. {}", e.getMessage());
        }
        return "redirect:/admin/customer/customersites/" + contract.getCustomer().getId();
    }

    @RequestMapping("/admin/customer/send-electric-live-email/{id}")
    public String sendElectricManualLiveEmail(@PathVariable("id") Long id, @RequestParam(name = "emailAddress", required = true) String emailAddress) {
        ElecCustomerContract contract = elecContractService.findById(id);
        try {
            liveEmailService.sendLiveEmail(contract, emailAddress);
        } catch (Exception e) {
            logger.error("Did not send live contract email. {}", e.getMessage());
        }
        return "redirect:/admin/customer/customersites/" + contract.getCustomer().getId();
    }

    @RequestMapping("/admin/customer/send-gas-objected-email/{id}")
    public String sendGasObjectedEmail(@PathVariable("id") Long id, @RequestParam(name = "emailAddress", required = true) String emailAddress) {
        GasCustomerContract contract = gasContractService.findById(id);
        try {
            objectedEmailService.sendObjectedEmail(contract, emailAddress);
        } catch (Exception e) {
            logger.error("Did not send Objected contract email. {}", e.getMessage());
        }
        return "redirect:/admin/customer/customersites/" + contract.getCustomer().getId();
    }

    @RequestMapping("/admin/customer/send-electric-objected-email/{id}")
    public String sendElectricObjectedEmail(@PathVariable("id") Long id, @RequestParam(name = "emailAddress", required = true) String emailAddress) {
        ElecCustomerContract contract = elecContractService.findById(id);
        try {
            objectedEmailService.sendObjectedEmail(contract, emailAddress);
        } catch (Exception e) {
            logger.error("Did not send Objected contract email. {}", e.getMessage());
        }
        return "redirect:/admin/customer/customersites/" + contract.getCustomer().getId();
    }

    @RequestMapping("/admin/customer/send-gas-sold-email/{id}")
    public String sendGasSoldEmail(@PathVariable("id") Long id, @RequestParam(name = "emailAddress", required = true) String emailAddress) {
        GasCustomerContract contract = gasContractService.findById(id);
        try {
            soldEmailService.sendSoldEmail(contract, emailAddress);
        } catch (Exception e) {
            logger.error("Did not send Sold contract email. {}", e.getMessage());
        }
        return "redirect:/admin/customer/customersites/" + contract.getCustomer().getId();
    }

    @RequestMapping("/admin/customer/send-electric-sold-email/{id}")
    public String sendElectricSoldEmail(@PathVariable("id") Long id, @RequestParam(name = "emailAddress", required = true) String emailAddress) {
        ElecCustomerContract contract = elecContractService.findById(id);
        try {
            soldEmailService.sendSoldEmail(contract, emailAddress);
        } catch (Exception e) {
            logger.error("Did not send Sold contract email. {}", e.getMessage());
        }
        return "redirect:/admin/customer/customersites/" + contract.getCustomer().getId();
    }
}
