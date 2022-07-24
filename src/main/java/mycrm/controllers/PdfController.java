package mycrm.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.services.ElecContractService;
import mycrm.services.GasContractService;
import mycrm.views.pdf.ElectricWelcomeLetter;
import mycrm.views.pdf.GasWelcomeLetter;

@Controller
public class PdfController {

	private final ElecContractService elecContractService;
	private final GasContractService gasContractService;

	@Autowired
	public PdfController(ElecContractService elecContractService, GasContractService gasContractService) {
		this.elecContractService = elecContractService;
		this.gasContractService = gasContractService;
	}

	@RequestMapping("/electricWelcomeLetter/{id}")
	public ModelAndView electricWelcomeLetter(@PathVariable("id") Long id) {

		ElecCustomerContract elecCustomerContract = elecContractService.findById(id);

		Map<String, Object> model = new HashMap<>();
		model.put("elecCustomerContract", elecCustomerContract);

		return new ModelAndView(new ElectricWelcomeLetter(), model);
	}

	@RequestMapping("/gasWelcomeLetter/{id}")
	public ModelAndView gasWelcomeLetter(@PathVariable("id") Long id) {

		GasCustomerContract gasCustomerContract = gasContractService.findById(id);

		Map<String, Object> model = new HashMap<>();
		model.put("gasCustomerContract", gasCustomerContract);

		return new ModelAndView(new GasWelcomeLetter(), model);
	}

}
