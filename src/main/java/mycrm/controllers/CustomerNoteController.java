package mycrm.controllers;

import mycrm.models.*;
import mycrm.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CustomerNoteController {

    private static final Logger logger = LogManager.getLogger();

    private final CallLogsService callLogsService;

    private final CustomerService customerService;
    private final CustomerNoteService customerNoteService;
    private final UserService userService;
    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final ContactService contactService;
    private final CustomerChildNoteService customerChildNoteService;
    private final EmailTemplateService emailTemplateService;
    private final UtilityContractService utilityContractService;

    private final MerchantServicesService merchantServicesService;
    private final LinkedAccountService linkedAccountService;

    @Autowired
    public CustomerNoteController(CallLogsService callLogsService, CustomerService customerService,
                                  CustomerNoteService customerNoteService,
                                  UserService userService,
                                  GasContractService gasContractService,
                                  ElecContractService elecContractService,
                                  ContactService contactService, CustomerChildNoteService customerChildNoteService, EmailTemplateService emailTemplateService,
                                  UtilityContractService utilityContractService, LinkedAccountService linkedAccountService, MerchantServicesService merchantServicesService) {
        this.callLogsService = callLogsService;
        this.customerService = customerService;
        this.customerNoteService = customerNoteService;
        this.userService = userService;
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.contactService = contactService;
        this.customerChildNoteService = customerChildNoteService;
        this.emailTemplateService = emailTemplateService;
        this.utilityContractService = utilityContractService;
        this.linkedAccountService = linkedAccountService;
        this.merchantServicesService = merchantServicesService;
    }

    // view customer notes
    @RequestMapping("/admin/customer/customernotes/{id}")
    public String viewCustomerNotes(@PathVariable("id") Long id,
                                    @RequestParam(name = "noteId", required = false) String noteId,
                                    @RequestParam(name = "childNoteId", required = false) String childNoteId,
                                    Model model) {
        Customer customer = customerService.findById(id);
        List<CustomerNote> customerNotes = customerNoteService.findByCustomerOrderByDateCreatedDesc(customer);
        List<Contact> contactList = contactService.findByCustomer(customer);
        model.addAttribute("customerNotes", customerNotes);
        model.addAttribute("customer", customer);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("gasCustomerContracts", gasContractService.findByCustomerOrderByEndDateDesc(customer));
        model.addAttribute("elecCustomerContracts", elecContractService.findByCustomerOrderByEndDateDesc(customer));
        model.addAttribute("utilityContracts", utilityContractService.findByCustomerOrderByEndDateDesc(customer));
        model.addAttribute("merchantServiceContract", merchantServicesService.findByCustomerOrderByEndDateDesc(customer));
        model.addAttribute("contactList", contactList);
        model.addAttribute("noteId", noteId);
        model.addAttribute("childNoteId", childNoteId);
        model.addAttribute("linkedAccountsSize", linkedAccountService.findByCustomer(customer).size());
        List<MerchantServicesContract> merchant = merchantServicesService.findByCustomerOrderByEndDateDesc(customer);
        List<UtilityContract> utility = utilityContractService.findByCustomerOrderByEndDateDesc(customer);
        return "admin/customer/customernotes";
    }



    // create customer notes
    @RequestMapping(value = "/customerNote", method = RequestMethod.POST)
    public String saveNote(CustomerNote customerNote) {
        CustomerNote createdNote = customerNoteService.save(customerNote);
        if (createdNote == null) {
            return "redirect:/admin/customer/customernotes/" + customerNote.getCustomer().getId();
        }

        if (createdNote.getTaggedUser() != null) {
            try {
                if (!createdNote.isCompleted()) {
                    logger.info("Sending email to tagged user");
                    emailTemplateService.sendTaggedCustomerNoteNotification(createdNote);
                }
            } catch (Exception e) {
                logger.info("Failed to send email notification to staff.");
            }
        }

        logger.info("Customer Note - new customer note created");

        return "redirect:/admin/customer/customernotes/" + createdNote.getCustomer().getId() + "/?noteId=" + createdNote.getId();
    }

    // delete customer note
    @RequestMapping("admin/customer/deleteNote/{id}")
    public String deleteNote(@PathVariable("id") Long id) {
        CustomerNote customerNote = customerNoteService.findById(id);

        if (customerNote != null) {
            Long customerId = customerNote.getCustomer().getId();
            customerNoteService.deleteById(id);

            logger.info("Customer Note - customer note deleted");
            return "redirect:/admin/customer/customernotes/" + customerId;
        }
        return "redirect:/admin/index";
    }

    /*CHILD NOTES*/
    @RequestMapping(value = "/addChildNote", method = RequestMethod.POST)
    public String saveChildNote(CustomerChildNote customerChildNote) {
        CustomerChildNote createdNote = customerChildNoteService.save(customerChildNote);

        if (createdNote == null) {
            return "redirect:/admin/customer/customernotes/" + createdNote.getCustomer().getId() + "/?noteId=" + createdNote.getCustomerNote().getId();
        }

        if (createdNote.getReplyTaggedUser() != null) {
            try {
                if (!createdNote.isReplyCompleted()) {
                    logger.info("Sending email to tagged user");
                    emailTemplateService.sendTaggedCustomerChildNoteNotification(createdNote);
                }
            } catch (Exception e) {
                logger.info("Failed to send email notification to staff for a child note.");
            }
        }

        return "redirect:/admin/customer/customernotes/" + createdNote.getCustomer().getId() + "/?noteId=" + createdNote.getCustomerNote().getId() + "&childNoteId=" + createdNote.getId();
    }

    @RequestMapping("admin/customer/deleteChildNote/{id}")
    public String deleteChildNote(@PathVariable("id") Long id) {
        CustomerChildNote customerChildNote = customerChildNoteService.findById(id);

        if (customerChildNote != null) {
            Long customerId = customerChildNote.getCustomer().getId();
            customerChildNoteService.deleteById(id);
            return "redirect:/admin/customer/customernotes/" + customerId;
        }
        return "redirect:/admin/index";
    }

    /*CHILD ELECTRIC NOTES*/
    @RequestMapping(value = "/addElectricChildNote", method = RequestMethod.POST)
    public String saveElectricChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/editeleccontract/" + customerChildNote.getCustomerNote().getElecCustomerContract().getId();
    }

    @RequestMapping(value = "/addUtilityChildNote", method = RequestMethod.POST)
    public String saveUtilityChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/editutilitycontract/" + customerChildNote.getCustomerNote().getUtilityContract().getId();
    }

    @RequestMapping(value = "/addMerchantServiceChildNote", method = RequestMethod.POST)
    public String saveMerchantServiceChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/editMerchantServicecontract/" + customerChildNote.getCustomerNote().getMerchantServicesContract().getId();
    }

    @RequestMapping(value = "/addSolarChildNote", method = RequestMethod.POST)
    public String saveSolarChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/edit-solar-contract/" + customerChildNote.getCustomerNote().getSolarContract().getId();
    }

    @RequestMapping(value = "/addWaterChildNote", method = RequestMethod.POST)
    public String saveWaterChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/edit-water-contract/" + customerChildNote.getCustomerNote().getWaterContract().getId();
    }

    @RequestMapping(value = "/addVoipChildNote", method = RequestMethod.POST)
    public String saveVoipChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/edit-voip-contract/" + customerChildNote.getCustomerNote().getVoipContract().getId();
    }

    @RequestMapping(value = "/addLandlineChildNote", method = RequestMethod.POST)
    public String saveLandlineChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/edit-landline-contract/" + customerChildNote.getCustomerNote().getLandlineContract().getId();
    }

    @RequestMapping(value = "/addBroadbandChildNote", method = RequestMethod.POST)
    public String saveBroadbandChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/edit-broadband-contract/" + customerChildNote.getCustomerNote().getBroadbandContract().getId();
    }

    @RequestMapping(value = "/addMobileChildNote", method = RequestMethod.POST)
    public String saveMobileChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/edit-mobile-contract/" + customerChildNote.getCustomerNote().getMobileContract().getId();
    }

    /*CHILD GAS NOTES*/
    @RequestMapping(value = "/addGasChildNote", method = RequestMethod.POST)
    public String saveGasChildNote(CustomerChildNote customerChildNote) {
        customerChildNoteService.save(customerChildNote);
        return "redirect:/admin/customer/editgascontract/" + customerChildNote.getCustomerNote().getGasCustomerContract().getId();
    }

    //edit customer note
    @RequestMapping(value = "/admin/customer/editcustomernote/{id}")
    public String editCustomerNote(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findById(id);
        CustomerNote customerNote = customerNoteService.findById(id);
        model.addAttribute("merchantServiceContract", merchantServicesService.findByCustomerOrderByEndDateDesc(customerNote.getCustomer()));
        model.addAttribute("customerNote", customerNote);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("gasCustomerContracts", gasContractService.findByCustomerOrderByEndDateDesc(customerNote.getCustomer()));
        model.addAttribute("elecCustomerContracts", elecContractService.findByCustomerOrderByEndDateDesc(customerNote.getCustomer()));
        model.addAttribute("utilityContracts", utilityContractService.findByCustomerOrderByEndDateDesc(customerNote.getCustomer()));
        return "admin/customer/editCustomerNote";
    }

    @PostMapping(value = "/editcustomernoteCompleted")
    public String editCustomerNoteMethod(Long id) {
        System.out.println("#############################");
        CustomerNote customerNote = customerNoteService.findById(id);
        customerNote.setCompleted(true);
        this.customerNoteService.save(customerNote);
        return "redirect:/";
    }

    @PostMapping(value = "/editcustomerChildnoteCompleted")
    public String editCustomeChildrNoteMethod(Long id) {
        CustomerChildNote customerChildNote = customerChildNoteService.findById(id);
        customerChildNote.setReplyCompleted(true);
        this.customerChildNoteService.save(customerChildNote);
        return "redirect:/";
    }

    //edit customer note
    @RequestMapping(value = "/admin/customer/editcustomerchildnote/{id}")
    public String editCustomerChildNote(@PathVariable("id") Long id, Model model) {
        CustomerChildNote customerChildNote = customerChildNoteService.findById(id);

        model.addAttribute("customerChildNote", customerChildNote);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("gasCustomerContracts", gasContractService.findByCustomerOrderByEndDateDesc(customerChildNote.getCustomer()));
        model.addAttribute("elecCustomerContracts", elecContractService.findByCustomerOrderByEndDateDesc(customerChildNote.getCustomer()));
        return "admin/customer/editCustomerChildNote";
    }
}
