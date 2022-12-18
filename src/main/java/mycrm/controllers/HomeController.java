package mycrm.controllers;

import mycrm.functions.UserHelper;
import mycrm.models.User;
import mycrm.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

	private final MyService myService;
	private static final Logger logger = LogManager.getLogger();

	private final CustomerNoteService customerNoteService;
	private final UserService userService;
	private final BrokerNoteService brokerNoteService;
	private final AdminContractTaskService adminContractTaskService;
	private final AdminContractTerminationTaskService adminContractTerminationTaskService;
	private final GasContractService gasContractService;
	private final ElecContractService elecContractService;
	private final CustomerChildNoteService customerChildNoteService;
	private final WorkflowTaskService workflowTaskService;
	private final UtilityContractService utilityContractService;
	private final UserHelper userHelper;
	private final DocumentService documentService;


	@Autowired
	public HomeController(MyService myService, CustomerNoteService customerNoteService, UserService userService, BrokerNoteService brokerNoteService, AdminContractTaskService adminContractTaskService, AdminContractTerminationTaskService adminContractTerminationTaskService, GasContractService gasContractService, ElecContractService elecContractService, CustomerChildNoteService customerChildNoteService, WorkflowTaskService workflowTaskService, UtilityContractService utilityContractService, UserHelper userHelper, DocumentService documentService) {
		this.myService = myService;
		this.customerNoteService = customerNoteService;
		this.userService = userService;
		this.brokerNoteService = brokerNoteService;
		this.adminContractTaskService = adminContractTaskService;
		this.adminContractTerminationTaskService = adminContractTerminationTaskService;
		this.gasContractService = gasContractService;
		this.elecContractService = elecContractService;
		this.customerChildNoteService = customerChildNoteService;
		this.workflowTaskService = workflowTaskService;
		this.utilityContractService = utilityContractService;
		this.userHelper = userHelper;
		this.documentService = documentService;
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/admin/index")
	public String adminIndex(Model model) {
		model.addAttribute("myCallbacks", myService.getMyTodaysCallbacks());
		User user = userHelper.getLoggedInUser();
		List<User> adminStaff = userService.findAllAdmin();

		if (user.isAdmin()) {
			model.addAttribute("findAllIncompleteCustomerNotesByTaggedUser", customerNoteService.findAllIncompleteByAdminOrderByDueDateAsc(adminStaff));
			model.addAttribute("findAllIncompleteCustomerChildNotesByTaggedUser", customerChildNoteService.findAllIncompleteByAdminOrderByDueDateAsc(adminStaff));
		} else {
			model.addAttribute("findAllIncompleteCustomerNotesByTaggedUser", customerNoteService.findAllIncompleteByTaggedUserOrderByDueDateAsc(user));
			model.addAttribute("findAllIncompleteCustomerChildNotesByTaggedUser", customerChildNoteService.findAllIncompleteByTaggedUserOrderByDueDateAsc(user));
		}

		model.addAttribute("users", userService.findAll());
		model.addAttribute("findAllBrokerNotesByUser", brokerNoteService.findAllIncompleteBrokerNotesByTaggedUser(user));
		model.addAttribute("findAllAdminContractNewSalesTasks", adminContractTaskService.findAllAdminContractNewSalesTasks());
		model.addAttribute("findAllContractsToTerminate", adminContractTerminationTaskService.findAllContractsToTerminate());
		model.addAttribute("findAllObjectedContracts", workflowTaskService.findAllObjectedContracts());
		model.addAttribute("findAllContractsToProcess", adminContractTaskService.findAllContractsToProcess());
		model.addAttribute("findAllVerbalContracts", workflowTaskService.findAllVerbalContracts());
		model.addAttribute("expiringDocuments", documentService.findDocumentsExpiringWithinAMonth());
		return "admin/index";
	}

	@RequestMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("myCallbacks", myService.getMyTodaysCallbacks());
		return "admin/index";
	}

}
