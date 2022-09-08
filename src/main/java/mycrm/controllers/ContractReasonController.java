package mycrm.controllers;

import mycrm.models.ContractReason;
import mycrm.services.ContractReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ContractReasonController {

    private final ContractReasonService contractReasonService;

    @Autowired
    public ContractReasonController(ContractReasonService contractReasonService) {
        this.contractReasonService = contractReasonService;
    }

    //new form, list contract reasons
    @RequestMapping("/admin/contractreason/index")
    public String contractReasonIndex(Model model) {
        System.out.println("entered here in contract reason");
        ContractReason contractReason = new ContractReason();

        model.addAttribute("contractReason", contractReason);
        model.addAttribute("contractReasons", contractReasonService.findAll());

        return "admin/contractreason/index";
    }


    //create/edit contract reason
    @RequestMapping(value = "/contractReason", method = RequestMethod.POST)
    public String saveContractReason(ContractReason contractReason) {

        try {
            contractReasonService.save(contractReason);
        } catch (Exception e) {
            return "redirect:/admin/contractreason/index";
        }

        return "redirect:/admin/contractreason/index";
    }

    @RequestMapping("/admin/contractreason/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        ContractReason contractReason = contractReasonService.findById(id);

        model.addAttribute("contractReason", contractReason);
        return "admin/contractreason/edit";
    }


}
