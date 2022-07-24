package mycrm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mycrm.functions.GasConsumptionCalculation;
import mycrm.models.ElectricityConsumption;
import mycrm.models.GasConsumption;
import mycrm.models.GasCustomerContract;
import mycrm.services.ElecContractService;
import mycrm.services.ElectricityConsumptionService;
import mycrm.services.GasConsumptionService;
import mycrm.services.GasContractService;

@Controller
public class ConsumptionController {

	private final GasConsumptionService gasConsumptionService;
	private final ElectricityConsumptionService electricityConsumptionService;
	private final GasContractService gasContractService;
	private final ElecContractService elecContractService;

	@Autowired
	public ConsumptionController(GasConsumptionService gasConsumptionService,
			ElectricityConsumptionService electricityConsumptionService, GasContractService gasContractService,
			ElecContractService elecContractService) {
		this.gasConsumptionService = gasConsumptionService;
		this.electricityConsumptionService = electricityConsumptionService;
		this.gasContractService = gasContractService;
		this.elecContractService = elecContractService;
	}

	// ajax call to call annual gas consumption input form
	@RequestMapping("/admin/customer/annualGasConsumption")
	public String annualGasConsumption(@RequestParam Long id, Model model) {
		// customer object here or contract object...
		model.addAttribute("passedVariable", id);
		model.addAttribute("annualGasConsumption", new GasConsumption());

		return "/admin/customer/annualconsumption :: gas";
	}

	// ajax call to call annual electricity consumption input form
	@RequestMapping("/admin/customer/annualElectricityConsumption")
	public String annualElectricityConsumption(@RequestParam Long id, Model model) {
		// customer object here or contract object...
		model.addAttribute("passedVariable", id);
		model.addAttribute("annualElectricityConsumption", new ElectricityConsumption());

		return "/admin/customer/annualconsumption :: electricity";
	}

	@RequestMapping(value = "/updateAnnualGasConsumption", method = RequestMethod.POST)
	public String updateAnnualGasConsumption(GasConsumption gasConsumption) {

		gasConsumptionService.create(gasConsumption);

		return "redirect:/admin/customer/gasconsumption/" + gasConsumption.getGasCustomerContract().getId();
	}

	@RequestMapping(value = "/updateAnnualElectricityConsumption", method = RequestMethod.POST)
	public String updateAnnualElecConsumption(ElectricityConsumption electricityConsumption) {

		electricityConsumptionService.create(electricityConsumption);

		return "redirect:/admin/customer/electicityconsumption/"
				+ electricityConsumption.getElecCustomerContract().getId();
	}

	// annual consumption for site
	@RequestMapping("/admin/customer/gasconsumption/{id}")
	public String gasConsumption(@PathVariable("id") Long id, Model model) {

		GasCustomerContract gasCustomerContract = gasContractService.findById(id);

		List<GasConsumption> gasConsumptionList = gasConsumptionService.findByGasCustomerContract(gasCustomerContract);

		Long totalGasConsumption = new GasConsumptionCalculation().calculateTotalGasConsumption(gasConsumptionList);

		model.addAttribute("gasCustomerContract", gasCustomerContract);
		model.addAttribute("gasCustomerContractId", gasCustomerContract.getId());
		model.addAttribute("gasConsumptionList", gasConsumptionList);
		model.addAttribute("annualGasConsumption", new GasConsumption());
		model.addAttribute("totalGasConsumption", totalGasConsumption);

		return "/admin/customer/gasconsumption";
	}

	// annual consumption for site
	@RequestMapping("/admin/customer/elecconsumption/{id}")
	public String electricityConsumption(@PathVariable("id") Long id, Model model) {

		model.addAttribute("elecCustomerContract", elecContractService.findById(id));
		model.addAttribute("annualElectricityConsumption", new ElectricityConsumption());

		return "/admin/customer/elecconsumption";
	}
}
