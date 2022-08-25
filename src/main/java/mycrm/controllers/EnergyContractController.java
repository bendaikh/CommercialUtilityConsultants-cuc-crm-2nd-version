package mycrm.controllers;

import mycrm.models.*;
import mycrm.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EnergyContractController {

    private static final Logger logger = LogManager.getLogger();

    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final SupplierService supplierService;
    private final BrokerService brokerService;
    private final UserService userService;
    private final ContractReasonService contractReasonService;
    private final CustomerSiteService customerSiteService;
    private final CustomerNoteService customerNoteService;
    private final ContactService contactService;
    private final ContractService contractService;
    private final UtilityContractService utilityContractService;
    private final BrokerTransferHistoryService brokerTransferHistoryService;

    private final MerchantServicesService merchantServicesService;

    @Value("${default.broker.id}")
    private Long defaultBrokerId;

    @Autowired
    public EnergyContractController(GasContractService gasContractService, ElecContractService elecContractService,
                                    SupplierService supplierService, BrokerService brokerService, UserService userService, ContractReasonService contractReasonService,
                                    CustomerSiteService customerSiteService, CustomerNoteService customerNoteService, ContactService contactService,
                                    ContractService contractService, UtilityContractService utilityContractService, BrokerTransferHistoryService brokerTransferHistoryService,
                                    MerchantServicesService merchantServicesService) {
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.supplierService = supplierService;
        this.brokerService = brokerService;
        this.userService = userService;
        this.contractReasonService = contractReasonService;
        this.customerSiteService = customerSiteService;
        this.customerNoteService = customerNoteService;
        this.contactService = contactService;
        this.contractService = contractService;
        this.utilityContractService = utilityContractService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
        this.merchantServicesService = merchantServicesService;
    }

    // new gas contract
    @RequestMapping("/admin/customer/gascontract")
    public String gasContract(@RequestParam String customerID, @RequestParam String customerSiteID, Model model) {

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();

        GasCustomerContract gasCustomerContract = new GasCustomerContract();

        model.addAttribute("customerSiteDetails", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("gasCustomerContract", gasCustomerContract);
        model.addAttribute("contractReasons", contractReasonService.findAll());
        model.addAttribute("campaigns", contractService.getCampaigns());
        return "admin/customer/newgascontract";

    }

    // create gas customer contract
    @RequestMapping(value = "/gasCustomerContract", method = RequestMethod.POST)
    public String saveGasContract(GasCustomerContract gasCustomerContract) {
        gasContractService.save(gasCustomerContract);

        return "redirect:/admin/customer/viewsite/" + gasCustomerContract.getCustomerSite().getId();
    }

    // open gas customer contract
    @RequestMapping("/admin/customer/editgascontract/{id}")
    public String editGasContract(@PathVariable("id") Long id, Model model) {

        GasCustomerContract gasCustomerContract = gasContractService.findById(id);

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

//        if(contractLocked(gasCustomerContract) && !user.isSuperAdmin()) {
//            logger.info("Contract locked gasContractId={}", gasCustomerContract.getId());
//            return "redirect:/unauthorised";
//        }

        if (user.isBroker() || user.isExternalBroker()) {

            if (gasCustomerContract.getBroker().getId() == defaultBrokerId) {

                gasCustomerContract.setBroker(user.getBroker());
                gasCustomerContract = gasContractService.save(gasCustomerContract);

            } else if (!gasCustomerContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit Gas Contract - User is not the broker associated to this contract");
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit Gas Contract - User is not super admin");
            return "redirect:/unauthorised";
        }

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();
        List<Contact> contactList = contactService.findByCustomer(gasCustomerContract.getCustomer());
        List<String> transferMessageList = brokerTransferHistoryService.findLatestGasBrokerTransferHistory(gasCustomerContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("contactList", contactList);
        model.addAttribute("gasContractNotes", customerNoteService.findByGasCustomerContractOrderByDateCreatedDesc(gasCustomerContract));
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("gasCustomerContract", gasCustomerContract);
        model.addAttribute("contractReasons", contractReasonService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("campaigns", contractService.getCampaigns());
        return "admin/customer/editgascontract";
    }

    // create electricity contract
    @RequestMapping("/admin/customer/eleccontract")
    public String elecContract(@RequestParam String customerID, @RequestParam String customerSiteID, Model model) {
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();
        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();

        model.addAttribute("customerSiteDetails", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("elecCustomerContract", elecCustomerContract);
        model.addAttribute("contractReasons", contractReasonService.findAll());
        model.addAttribute("campaigns", contractService.getCampaigns());
        return "admin/customer/neweleccontract";

    }

    // create new electricity customer contract
    @RequestMapping(value = "/elecCustomerContract", method = RequestMethod.POST)
    public String saveElectContract(ElecCustomerContract elecCustomerContract) {
        elecContractService.save(elecCustomerContract);
        return "redirect:/admin/customer/viewsite/" + elecCustomerContract.getCustomerSite().getId();
    }

    @RequestMapping("/admin/customer/gasContractNotes")
    public String getGasContractNotes(@RequestParam Long id, Model model) {

        GasCustomerContract gasCustomerContract = gasContractService.findById(id);

        model.addAttribute("gasContractNotes", customerNoteService.findByGasCustomerContractOrderByDateCreatedDesc(gasCustomerContract));
        model.addAttribute("users", userService.findAll());
        return "admin/customer/gasContractNotes";
    }

    @RequestMapping("/admin/customer/elecContractNotes")
    public String getElecContractNotes(@RequestParam Long id, Model model) {

        ElecCustomerContract elecCustomerContract = elecContractService.findById(id);

        model.addAttribute("elecContractNotes", customerNoteService.findByElecCustomerContractOrderByDateCreatedDesc(elecCustomerContract));
        model.addAttribute("users", userService.findAll());
        return "admin/customer/elecContractNotes";
    }

    @RequestMapping("/admin/customer/utility-contract-notes")
    public String getUtilityContractNotes(@RequestParam Long id, Model model) {

        UtilityContract utilityContract = utilityContractService.findById(id);

        model.addAttribute("utilityContractNotes", customerNoteService.findByUtilityContractOrderByDateCreatedDesc(utilityContract));
        model.addAttribute("users", userService.findAll());
        return "admin/customer/utility-contract-notes";
    }
    @RequestMapping("/admin/customer/merchant-services-contract-notes")
    public String getMerchantServiceContractNotes(@RequestParam Long id, Model model) {

        MerchantServicesContract merchantServicesContract = merchantServicesService.findById(id);

        model.addAttribute("merchantServiceContractNotes", customerNoteService.findByMerchantServicesContractOrderByDateCreatedDesc(merchantServicesContract));
        model.addAttribute("users", userService.findAll());
        return "admin/customer/merchant-services-contract-notes";
    }

    // create elec contract notes
    @RequestMapping(value = "/elecContractNote", method = RequestMethod.POST)
    public String saveElecContractNote(CustomerNote customerNote) {
        CustomerNote savedNote = customerNoteService.save(customerNote);
        return "redirect:/admin/customer/editeleccontract/" + savedNote.getElecCustomerContract().getId();
    }

    // create gas contract notes
    @RequestMapping(value = "/gasContractNote", method = RequestMethod.POST)
    public String saveGasContractNote(CustomerNote customerNote) {
        CustomerNote savedNote = customerNoteService.save(customerNote);
        return "redirect:/admin/customer/editgascontract/" + savedNote.getGasCustomerContract().getId();
    }

    // create utility contract notes
    @RequestMapping(value = "/utilityContractNote", method = RequestMethod.POST)
    public String saveUtilityContractNote(CustomerNote customerNote) {
        CustomerNote savedNote = customerNoteService.save(customerNote);
        return "redirect:/admin/customer/edit-utility-contract/" + savedNote.getUtilityContract().getId();
    }
    @RequestMapping(value = "/merchantServiceContractNote", method = RequestMethod.POST)
    public String saveMerchantServiceContractNote(CustomerNote customerNote) {
        CustomerNote savedNote = customerNoteService.save(customerNote);
        return "redirect:/admin/customer/edit-merchant-services/" + savedNote.getMerchantServicesContract().getId();
    }

    @RequestMapping("/admin/customer/editeleccontract/{id}")
    public String editElecContract(@PathVariable("id") Long id, Model model) {

        ElecCustomerContract elecCustomerContract = elecContractService.findById(id);

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//        if(contractLocked(elecCustomerContract) && !user.isSuperAdmin()) {
//            logger.info("Contract locked elecContractId={}", elecCustomerContract.getId());
//            return "redirect:/unauthorised";
//        }

        // if the broker is CEC pending then give to broker who accesses it
        if (user.isBroker() || user.isExternalBroker()) {

            if (elecCustomerContract.getBroker().getId() == defaultBrokerId) {
                elecCustomerContract.setBroker(user.getBroker());
                elecCustomerContract = elecContractService.save(elecCustomerContract);
            } else if (!elecCustomerContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit Electricity Contract - User is not the broker associated to this contract");
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit Electricity Contract - User is not super admin");
            return "redirect:/unauthorised";
        }

        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();
        List<Contact> contactList = contactService.findByCustomer(elecCustomerContract.getCustomer());
        List<String> transferMessageList = brokerTransferHistoryService.findLatestElectricBrokerTransferHistory(elecCustomerContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("contactList", contactList);
        model.addAttribute("elecContractNotes", customerNoteService.findByElecCustomerContractOrderByDateCreatedDesc(elecCustomerContract));
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("elecCustomerContract", elecCustomerContract);
        model.addAttribute("contractReasons", contractReasonService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("campaigns", contractService.getCampaigns());
        return "admin/customer/editeleccontract";
    }

    @RequestMapping("/admin/customer/editutilitycontract/{id}")
    public String editUtilityContract(@PathVariable("id") Long id, Model model) {

        UtilityContract utilityContract = utilityContractService.findById(id);

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//        if(contractLocked(elecCustomerContract) && !user.isSuperAdmin()) {
//            logger.info("Contract locked elecContractId={}", elecCustomerContract.getId());
//            return "redirect:/unauthorised";
//        }

        // if the broker is CEC pending then give to broker who accesses it
        if (user.isBroker() || user.isExternalBroker()) {

            if (utilityContract.getBroker().getId() == defaultBrokerId) {
                utilityContract.setBroker(user.getBroker());
                utilityContract = utilityContractService.save(utilityContract);
            } else if (!utilityContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit Electricity Contract - User is not the broker associated to this contract");
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit Electricity Contract - User is not super admin");
            return "redirect:/unauthorised";
        }
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();
        System.out.println("ßßßßßßßßßßßßßßßßßßßßßßß custemer id");
        System.out.println(utilityContract.getCustomerSite().getCustomer());
        List<Contact> contactList = contactService.findByCustomer(utilityContract.getCustomerSite().getCustomer());
        List<String> transferMessageList = brokerTransferHistoryService.findLatestUtilityBrokerTransferHistory(utilityContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("contactList", contactList);
        model.addAttribute("utilityContractNotes", customerNoteService.findByUtilityContractOrderByDateCreatedDesc(utilityContract));
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("utilityContract", utilityContract);
        model.addAttribute("contractReasons", contractReasonService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("customerSite", customerSiteService.findById(utilityContract.getCustomerSite().getId()));
        return "admin/customer/manage-utility-contract";
    }
    @RequestMapping("/admin/customer/editMerchantServicecontract/{id}")
    public String editMerchantServiceContract(@PathVariable("id") Long id, Model model) {

        MerchantServicesContract merchantServicesContract = merchantServicesService.findById(id);

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//        if(contractLocked(elecCustomerContract) && !user.isSuperAdmin()) {
//            logger.info("Contract locked elecContractId={}", elecCustomerContract.getId());
//            return "redirect:/unauthorised";
//        }

        // if the broker is CEC pending then give to broker who accesses it
        if (user.isBroker() || user.isExternalBroker()) {

            if (merchantServicesContract.getBroker().getId() == defaultBrokerId) {
                merchantServicesContract.setBroker(user.getBroker());
                merchantServicesContract = merchantServicesService.save(merchantServicesContract);
            } else if (!merchantServicesContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit Merchant Contract - User is not the broker associated to this contract");
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit Merchant Contract - User is not super admin");
            return "redirect:/unauthorised";
        }
        List<Broker> brokers = brokerService.findAll();
        List<Supplier> suppliers = supplierService.findAllOrderByBusinessName();
        List<Contact> contactList = contactService.findByCustomer(merchantServicesContract.getCustomerSite().getCustomer());
        List<String> transferMessageList = brokerTransferHistoryService.findLatestMerchantServicesBrokerTransferHistory(merchantServicesContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("contactList", contactList);
        model.addAttribute("merchantServiceContractNotes", customerNoteService.findByMerchantServicesContractOrderByDateCreatedDesc(merchantServicesContract));
        model.addAttribute("brokers", brokers);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("merchantServicesContract", merchantServicesContract);
        model.addAttribute("contractReasons", contractReasonService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("campaigns", contractService.getCampaigns());
        model.addAttribute("customerSite", customerSiteService.findById(merchantServicesContract.getCustomerSite().getId()));
        return "admin/customer/manage-merchant-services";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/customer/deleteElecContract/{id}")
    public String deleteElecContract(@PathVariable("id") Long id) {
        ElecCustomerContract elecCustomerContract = elecContractService.findById(id);

        if (elecCustomerContract != null) {
            elecContractService.deleteById(id);
            return "redirect:/admin/customer/viewsite/" + elecCustomerContract.getCustomerSite().getId();
        }
        return "redirect:/admin/index";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/customer/deleteGasContract/{id}")
    public String deleteGasContract(@PathVariable("id") Long id) {
        GasCustomerContract gasCustomerContract = gasContractService.findById(id);

        if (gasCustomerContract != null) {
            gasContractService.deleteById(id);
            return "redirect:/admin/customer/viewsite/" + gasCustomerContract.getCustomerSite().getId();
        }
        return "redirect:/admin/index";
    }

//    private boolean contractLocked(EnergyContract<?> energyContract) {
//        if (("LIVE".equals(energyContract.getLogType()) || "INACTIVE".equals(energyContract.getLogType())
//                || "ENDED".equals(energyContract.getLogType())) && "ENDED".equals(energyContract.getContractStatus())) {
//            return true;
//        }
//        return false;
//    }

}
