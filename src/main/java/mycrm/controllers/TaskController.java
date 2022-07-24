package mycrm.controllers;

import mycrm.configuration.UtilityType;
import mycrm.functions.UserHelper;
import mycrm.models.CourtesyCallSearch;
import mycrm.models.CustomerChildNote;
import mycrm.models.ProcessingPack;
import mycrm.models.TerminationTask;
import mycrm.models.User;
import mycrm.models.WelcomePack;
import mycrm.services.AdminContractTaskService;
import mycrm.services.AdminContractTerminationTaskService;
import mycrm.services.BrokerNoteService;
import mycrm.services.CourtesyCallTaskService;
import mycrm.services.CustomerChildNoteService;
import mycrm.services.CustomerNoteService;
import mycrm.services.DocumentService;
import mycrm.services.ElecContractService;
import mycrm.services.GasContractService;
import mycrm.services.UserService;
import mycrm.services.UtilityContractService;
import mycrm.services.WorkflowTaskService;
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
public class TaskController {

    private static Logger logger = LogManager.getLogger();

    private final CustomerNoteService customerNoteService;
    private final UserService userService;
    private final BrokerNoteService brokerNoteService;
    private final AdminContractTaskService adminContractTaskService;
    private final AdminContractTerminationTaskService adminContractTerminationTaskService;
    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final CourtesyCallTaskService courtesyCallTaskService;
    private final CustomerChildNoteService customerChildNoteService;
    private final WorkflowTaskService workflowTaskService;
    private final UtilityContractService utilityContractService;
    private final UserHelper userHelper;
    private final DocumentService documentService;

    @Autowired
    public TaskController(CustomerNoteService customerNoteService,
                          UserService userService,
                          BrokerNoteService brokerNoteService,
                          AdminContractTaskService adminContractTaskService,
                          AdminContractTerminationTaskService adminContractTerminationTaskService,
                          GasContractService gasContractService,
                          ElecContractService elecContractService,
                          CourtesyCallTaskService courtesyCallTaskService,
                          CustomerChildNoteService customerChildNoteService,
                          WorkflowTaskService workflowTaskService,
                          UtilityContractService utilityContractService,
                          UserHelper userHelper, DocumentService documentService) {
        this.customerNoteService = customerNoteService;
        this.userService = userService;
        this.brokerNoteService = brokerNoteService;
        this.adminContractTaskService = adminContractTaskService;
        this.adminContractTerminationTaskService = adminContractTerminationTaskService;
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.courtesyCallTaskService = courtesyCallTaskService;
        this.customerChildNoteService = customerChildNoteService;
        this.workflowTaskService = workflowTaskService;
        this.utilityContractService = utilityContractService;
        this.userHelper = userHelper;
        this.documentService = documentService;
    }

    @RequestMapping("/admin/tasks/index")
    public String taskList(Model model) {

        User user = userHelper.getLoggedInUser();
        List<User> adminStaff = userService.findAllAdmin();

        if (user.isAdmin()) {
            model.addAttribute("findAllIncompleteCustomerNotesByTaggedUser", customerNoteService.findAllIncompleteByAdminOrderByDueDateAsc(adminStaff));
            model.addAttribute("findAllIncompleteCustomerChildNotesByTaggedUser", customerChildNoteService.findAllIncompleteByAdminOrderByDueDateAsc(adminStaff));
        } else {
            model.addAttribute("findAllIncompleteCustomerNotesByTaggedUser", customerNoteService.findAllIncompleteByTaggedUserOrderByDueDateAsc(user));
            model.addAttribute("findAllIncompleteCustomerChildNotesByTaggedUser", customerChildNoteService.findAllIncompleteByTaggedUserOrderByDueDateAsc(user));
        }

        model.addAttribute("findAllBrokerNotesByUser", brokerNoteService.findAllIncompleteBrokerNotesByTaggedUser(user));
        model.addAttribute("findAllAdminContractNewSalesTasks", adminContractTaskService.findAllAdminContractNewSalesTasks());
        model.addAttribute("findAllContractsToTerminate", adminContractTerminationTaskService.findAllContractsToTerminate());
        model.addAttribute("findAllObjectedContracts", workflowTaskService.findAllObjectedContracts());
        model.addAttribute("findAllContractsToProcess", adminContractTaskService.findAllContractsToProcess());
        model.addAttribute("findAllVerbalContracts", workflowTaskService.findAllVerbalContracts());
        model.addAttribute("expiringDocuments", documentService.findDocumentsExpiringWithinAMonth());

        return "admin/tasks/index";
    }

    @RequestMapping("/admin/tasks/welcomepack")
    public String welcomePack(@RequestParam long id, @RequestParam String supplyType, Model model) {

        if (supplyType.equals("GAS")) {
            model.addAttribute("welcomePack", gasContractService.findById(id));
        } else if (supplyType.equals("ELEC")) {
            model.addAttribute("welcomePack", elecContractService.findById(id));
        } else if (validUtilityType(supplyType)) {
            model.addAttribute("welcomePack", utilityContractService.findById(id));
        }

        return "admin/tasks/welcomepack";
    }

    @RequestMapping("/admin/tasks/processing-pack")
    public String processingPack(@RequestParam long id, @RequestParam String supplyType, Model model) {

        if (supplyType.equals("GAS")) {
            model.addAttribute("processingPack", gasContractService.findById(id));
        } else if (supplyType.equals("ELEC")) {
            model.addAttribute("processingPack", elecContractService.findById(id));
        } else if (validUtilityType(supplyType)) {
            model.addAttribute("processingPack", utilityContractService.findById(id));
        }

        return "admin/tasks/processing-pack";
    }

    @RequestMapping("/admin/tasks/terminationtask")
    public String terminationTask(@RequestParam long id, @RequestParam String supplyType, Model model) {

        if (supplyType.equals("GAS")) {
            model.addAttribute("terminationTask", gasContractService.findById(id));
        } else if (supplyType.equals("ELEC")) {
            model.addAttribute("terminationTask", elecContractService.findById(id));
        } else if (validUtilityType(supplyType)) {
            model.addAttribute("terminationTask", utilityContractService.findById(id));
        }

        return "admin/tasks/terminationtask";
    }

    @RequestMapping("/admin/tasks/customernote")
    public String showCustomerNote(@RequestParam long id, Model model) {
        model.addAttribute("customerNote", customerNoteService.findById(id));
        return "admin/tasks/customernote";
    }

    @RequestMapping("/admin/tasks/customerchildnote")
    public String showCustomerChildNote(@RequestParam long id, Model model) {
        CustomerChildNote customerChildNote = customerChildNoteService.findById(id);
        model.addAttribute("customerChildNote", customerChildNote);
        model.addAttribute("customerNote", customerNoteService.findById(customerChildNote.getCustomerNote().getId()));
        return "admin/tasks/customerchildnote";
    }

    @RequestMapping("/admin/tasks/brokernote")
    public String showBrokerNote(@RequestParam long id, Model model) {
        model.addAttribute("brokerNote", brokerNoteService.findById(id));
        return "admin/tasks/brokernote";
    }

    @RequestMapping(value = "/updateWelcomePack", method = RequestMethod.POST)
    public String updateWelcomePack(WelcomePack welcomePack) {

        logger.info("Welcome pack customer contract id={}, supplyType={}", welcomePack.getId(),
                welcomePack.getSupplyType());

        if (welcomePack.getSupplyType().equals("GAS")) {
            gasContractService.updateWelcomePack(welcomePack);
        } else if (welcomePack.getSupplyType().equals("ELEC")) {
            elecContractService.updateWelcomePack(welcomePack);
        } else if (validUtilityType(welcomePack.getSupplyType())) {
            utilityContractService.updateWelcomePack(welcomePack);
        }

        return "redirect:/admin/tasks/index";
    }

    @RequestMapping(value = "/updateProcessingPack", method = RequestMethod.POST)
    public String updateProcessingPack(ProcessingPack processingPack) {

        logger.info("Processing pack customer contract id={},supplyType={}", processingPack.getId(),
                processingPack.getSupplyType());

        if (processingPack.getSupplyType().equals("GAS")) {
            gasContractService.updateProcessingPack(processingPack);
        } else if (processingPack.getSupplyType().equals("ELEC")) {
            elecContractService.updateProcessingPack(processingPack);
        } else if (validUtilityType(processingPack.getSupplyType())) {
            utilityContractService.updateProcessingPack(processingPack);
        }

        return "redirect:/admin/tasks/index";
    }

    @RequestMapping(value = "/updateTermination", method = RequestMethod.POST)
    public String updateTerminations(TerminationTask terminationTask) {

        logger.info("Termination task customer contract id={},supplyType={}", terminationTask.getId(),
                terminationTask.getSupplyType());

        if (terminationTask.getSupplyType().equals("GAS")) {
            gasContractService.updateTerminationTask(terminationTask);
        } else if (terminationTask.getSupplyType().equals("ELEC")) {
            elecContractService.updateTerminationTask(terminationTask);
        } else if (validUtilityType(terminationTask.getSupplyType())) {
            utilityContractService.updateTerminationTask(terminationTask);
        }

        return "redirect:/admin/tasks/index";
    }

    @RequestMapping(value = "/updateCompletedCustomerNote", method = RequestMethod.POST)
    public String updateCompletedCustomerNote(long id) {
        customerNoteService.updateCustomerNote(id, userHelper.getLoggedInUser());
        return "redirect:/admin/tasks/index";
    }

    @RequestMapping(value = "/updateCompletedCustomerChildNote", method = RequestMethod.POST)
    public String updateCompletedCustomerChildNote(long id) {
        customerChildNoteService.updateCustomerNote(id);
        return "redirect:/admin/tasks/index";
    }

    @RequestMapping(value = "/updateCompletedBrokerNote", method = RequestMethod.POST)
    public String updateCompletedBrokerNote(long id) {
        brokerNoteService.updateBrokerNote(id);
        return "redirect:/admin/tasks/index";
    }

    @RequestMapping("/admin/tasks/courtesy/{pageNumber}")
    public String viewCourtesyCalls(Model model, @PathVariable("pageNumber") int pageNumber) {
        CourtesyCallSearch courtesyCallSearch = courtesyCallTaskService.findAll(pageNumber);

        model.addAttribute("pageNumber", courtesyCallSearch.getPageNumber());
        model.addAttribute("totalPages", courtesyCallSearch.getTotalPages());
        model.addAttribute("incompleteCourtesyCalls", courtesyCallSearch.getCourtesyCallTaskList());
        return "admin/tasks/courtesy";
    }

    private boolean validUtilityType(String utilityType) {
        for (UtilityType u : UtilityType.values()) {
            if (u.name().equals(utilityType)) {
                return true;
            }
        }
        return false;
    }
}
