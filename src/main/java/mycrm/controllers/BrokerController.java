package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.notifications.NotificationService;
import mycrm.services.BrokerService;
import mycrm.services.ElecContractService;
import mycrm.services.GasContractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class BrokerController {

	private static final Logger logger = LogManager.getLogger();

	private final BrokerService brokerService;
	private final GasContractService gasContractService;
	private final ElecContractService elecContractService;
	private final NotificationService notifyService;

	@Autowired
	public BrokerController(BrokerService brokerService, GasContractService gasContractService,
							ElecContractService elecContractService, NotificationService notifyService) {
		this.brokerService = brokerService;
		this.gasContractService = gasContractService;
		this.elecContractService = elecContractService;
		this.notifyService = notifyService;
	}

	@RequestMapping("admin/broker/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("broker", brokerService.findById(id));
		return "admin/broker/newbroker";
	}

	@RequestMapping("/admin/broker/newbroker")
	public String newBroker(Model model) {
		model.addAttribute("broker", new Broker());
		return "admin/broker/newbroker";
	}

	@RequestMapping(value = "broker", method = RequestMethod.POST)
	public String saveBroker(Broker broker) {
		try {
			brokerService.save(broker);
			logger.info("Broker successfully saved, broker={}", broker.toString());
		} catch (Exception e) {
			logger.info("Unable to save broker");
			return "redirect:/admin/broker/newBroker";
		}
		return "redirect:/admin/broker/view/" + broker.getId();
	}

	// view broker details
	@RequestMapping("/admin/broker/view/{id}")
	public String view(@PathVariable("id") Long id, Model model) {
		Broker broker = brokerService.findById(id);

		if (broker == null) {
			notifyService.addErrorMessage("Cannot find broker #" + id);
			return "redirect:/admin/brokers";
		}

		model.addAttribute("broker", broker);

		return "admin/broker/view";
	}
	
	// view broker customers
	@RequestMapping("/admin/broker/brokercustomers/{id}")
    public String viewBrokerCustomers(@PathVariable("id") Long id, Model model) {
        Broker broker = brokerService.findById(id);

        if (broker == null) {
            notifyService.addErrorMessage("Cannot find broker #" + id);
            return "redirect:/admin/brokers";
        }
        
        List<Customer> customers = brokerService.findCustomersByBroker(broker);

        List<GasCustomerContract> gasCustomerContracts = gasContractService.findByBroker(broker);
        List<ElecCustomerContract> elecCustomerContracts = elecContractService.findByBroker(broker);

        model.addAttribute("broker", broker);
        model.addAttribute("gasCustomerContracts", gasCustomerContracts);
        model.addAttribute("elecCustomerContracts", elecCustomerContracts);
        model.addAttribute("customers", customers);

        return "admin/broker/brokercustomers";
    }

	@RequestMapping("/admin/broker/brokers")
	public String brokers(Model model) {
		List<Broker> brokers = brokerService.findAll();
		model.addAttribute("findall", brokers);

		return "admin/broker/brokers";
	}

}
