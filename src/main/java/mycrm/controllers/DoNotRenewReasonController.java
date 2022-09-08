package mycrm.controllers;

import mycrm.models.DoNotRenewReason;
import mycrm.services.DoNotRenewReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DoNotRenewReasonController {

    private final DoNotRenewReasonService doNotRenewReasonService;

    @Autowired
    public DoNotRenewReasonController(DoNotRenewReasonService doNotRenewReasonService) {
        this.doNotRenewReasonService = doNotRenewReasonService;
    }
    //new form, list contract reasons
    @RequestMapping("/admin/doNotRenewReason/index")
    public String doNotRenewReasonIndex(Model model) {
        DoNotRenewReason doNotRenewReason = new DoNotRenewReason();

        model.addAttribute("doNotRenewReason", doNotRenewReason);
        model.addAttribute("doNotRenewReasons", doNotRenewReasonService.findAll());

        return "admin/doNotRenewReason/index";
    }

    //create/edit contract reason
    @RequestMapping(value = "/doNotRenewreason", method = RequestMethod.POST)
    public String saveDoNotRenewReason(DoNotRenewReason doNotRenewReason) {

        try {
            doNotRenewReasonService.save(doNotRenewReason);
        } catch (Exception e) {
            return "redirect:/admin/doNotRenewReason/index";
        }

        return "redirect:/admin/doNotRenewReason/index";
    }

    @RequestMapping("/admin/doNotRenewReason/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        DoNotRenewReason doNotRenewReason = doNotRenewReasonService.findById(id);

        model.addAttribute("doNotRenewReason", doNotRenewReason);
        return "admin/doNotRenewReason/edit";
    }
}
