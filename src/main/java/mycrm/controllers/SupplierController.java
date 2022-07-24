package mycrm.controllers;

import mycrm.models.Supplier;
import mycrm.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class SupplierController {

	private final SupplierService supplierService;

	@Autowired
	public SupplierController(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	@RequestMapping("/admin/supplier/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("supplier", supplierService.findById(id));
		return "admin/supplier/newsupplier";
	}

	@RequestMapping("/admin/supplier/newsupplier")
	public String newSupplier(Model model) {
		model.addAttribute("supplier", new Supplier());
		return "admin/supplier/newsupplier";
	}

	@RequestMapping(value = "/supplier", method = RequestMethod.POST)
	public String saveSupplier(Supplier supplier) {
		supplierService.create(supplier);
		return "redirect:/admin/supplier/suppliers";
	}

	@RequestMapping("/admin/supplier/view/{id}")
	public String viewSupplier(@PathVariable("id") Long id, Model model) {
		Supplier supplier = supplierService.findById(id);
		model.addAttribute("supplier", supplier);

		return "admin/supplier/view";
	}

	@RequestMapping("/admin/supplier/suppliers")
	public String suppliers(Model model) {
        List<Supplier> findAll = supplierService.findAllOrderByBusinessName();
		model.addAttribute("findAll", findAll);

		return "admin/supplier/suppliers";
	}
}
