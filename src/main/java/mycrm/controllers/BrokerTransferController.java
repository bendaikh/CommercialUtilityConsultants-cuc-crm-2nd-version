package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.BrokerContractPackage;
import mycrm.models.BrokerTransferUpdate;
import mycrm.services.BrokerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BrokerTransferController {
    private static final Logger logger = LogManager.getLogger();

    private final BrokerService brokerService;

    @Autowired
    public BrokerTransferController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    // show all the contracts for Gas/electric and utility - show these split into log types
    @RequestMapping("/admin/broker/broker-transfer/contracts/{id}")
    public String showContractsForBroker(@PathVariable("id") Long id, Model model) {

        Broker selectedBroker = brokerService.findById(id);

        BrokerContractPackage brokerContracts = brokerService.findBrokerContractStats(selectedBroker);

        List<Broker> brokers = brokerService.findAll();
        brokers.remove(selectedBroker);

        model.addAttribute("selectedBroker", selectedBroker);
        model.addAttribute("brokers", brokers);
        model.addAttribute("brokerContracts", brokerContracts);
        return "admin/broker/broker-transfer/contracts";
    }

    // update the contracts and log all the transferred items
    @RequestMapping(value = "/admin/broker/broker-transfer/transfer-broker-contracts", method = RequestMethod.POST)
    public String updateBrokerContracts(BrokerTransferUpdate brokerTransferUpdate) {
        boolean successful = brokerService.transferBrokerContracts(brokerTransferUpdate);

        return "redirect:/admin/broker/broker-transfer/confirmation?success=" + successful;
    }

    // show transfer confirmation
    @RequestMapping("/admin/broker/broker-transfer/confirmation")
    public String brokerTransferConfirmation(@RequestParam boolean success, Model model) {
        if (success) {
            model.addAttribute("message", "Updated");
        } else {
            model.addAttribute("message", "Failed");
        }

        return "admin/broker/broker-transfer/confirmation";
    }
}
