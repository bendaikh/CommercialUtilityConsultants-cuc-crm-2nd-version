package mycrm.controllers;

import mycrm.audit.history.CustomerHistory;
import mycrm.models.BillingDetail;
import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.CustomerSite;
import mycrm.models.Document;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.LinkedAccount;
import mycrm.models.MerchantServicesContract;
import mycrm.models.UtilityContract;
import mycrm.services.ContactService;
import mycrm.services.CustomerChildNoteService;
import mycrm.services.CustomerHistoryService;
import mycrm.services.CustomerNoteService;
import mycrm.services.CustomerService;
import mycrm.services.CustomerSiteService;
import mycrm.services.DocumentService;
import mycrm.services.ElecContractService;
import mycrm.services.GasContractService;
import mycrm.services.LinkedAccountService;
import mycrm.services.MerchantServicesService;
import mycrm.services.UtilityContractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerRemovalController {
    private static final Logger logger = LogManager.getLogger();

    private final CustomerService customerService;
    private final CustomerHistoryService customerHistoryService;
    private final ContactService contactService;
    private final CustomerSiteService customerSiteService;
    private final DocumentService documentService;
    private final CustomerNoteService customerNoteService;
    private final ElecContractService elecContractService;
    private final GasContractService gasContractService;
    private final CustomerChildNoteService customerChildNoteService;
    private final LinkedAccountService linkedAccountService;
    private final UtilityContractService utilityContractService;
    private final MerchantServicesService merchantServicesService;

    @Autowired
    public CustomerRemovalController(CustomerService customerService, CustomerHistoryService customerHistoryService,
                                     ContactService contactService, CustomerSiteService customerSiteService, DocumentService documentService,
                                     CustomerNoteService customerNoteService, ElecContractService elecContractService, GasContractService gasContractService,
                                     CustomerChildNoteService customerChildNoteService, LinkedAccountService linkedAccountService, UtilityContractService utilityContractService, MerchantServicesService merchantServicesService) {
        this.customerService = customerService;
        this.customerHistoryService = customerHistoryService;
        this.contactService = contactService;
        this.customerSiteService = customerSiteService;
        this.documentService = documentService;
        this.customerNoteService = customerNoteService;
        this.elecContractService = elecContractService;
        this.gasContractService = gasContractService;
        this.customerChildNoteService = customerChildNoteService;
        this.linkedAccountService = linkedAccountService;
        this.utilityContractService = utilityContractService;
        this.merchantServicesService = merchantServicesService;
    }

    @RequestMapping("/admin/removal/index/{id}")
    public String removeCustomerPage(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findById(id);

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("customer", customer);
        modelMap.put("customerHistoryList", customerHistoryService.findByCustomer(customer));
        modelMap.put("customerContacts", contactService.findByCustomer(customer));
        modelMap.put("customerSites", customerSiteService.findByCustomer(customer));
        modelMap.put("customerDocuments", documentService.findByCustomer(customer));
        modelMap.put("customerNotes", customerNoteService.findByCustomer(customer));
        modelMap.put("electricContracts", elecContractService.findByCustomerOrderByEndDateDesc(customer));
        modelMap.put("gasContracts", gasContractService.findByCustomerOrderByEndDateDesc(customer));
        modelMap.put("billingDetails", customer.getBillingDetail());
        modelMap.put("linkedAccounts", linkedAccountService.findByCustomer(customer));
        modelMap.put("utilityContracts", utilityContractService.findByCustomer(customer));
        modelMap.put("merchantServicesContracts", merchantServicesService.findByCustomer(customer));

        model.addAllAttributes(modelMap);
        return "admin/removal/index";
    }

    @RequestMapping("/forceDeleteCustomer/{id}")
    public String forceDeleteCustomer(@PathVariable("id") Long id) {
        Customer customer = customerService.findById(id);

        boolean successfullyDeleted = true;

        if (customer != null) {

            List<CustomerChildNote> childNotes = customerChildNoteService.findByCustomer(customer);
            if (childNotes.size() != 0) {
                try {
                    customerChildNoteService.deleteByCustomer(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete child notes. {}", e.getMessage());
                }
            }

            List<CustomerNote> notes = customerNoteService.findByCustomer(customer);
            if (notes.size() != 0) {
                try {
                    customerNoteService.deleteByCustomer(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete notes. {}", e.getMessage());
                }
            }

            List<Contact> contacts = contactService.findByCustomer(customer);
            if (contacts.size() != 0) {
                try {
                    contactService.deleteByCustomer(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete contacts. {}", e.getMessage());
                }
            }

            List<Document> documents = documentService.findByCustomer(customer);
            if (documents.size() != 0) {
                try {
                    documentService.deleteByCustomer(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete documents. {}", e.getMessage());
                }
            }

            List<ElecCustomerContract> electricContracts = elecContractService.findByCustomerOrderByEndDateDesc(customer);
            if (electricContracts.size() != 0) {
                try {
                    elecContractService.deleteByCustomer(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete electric contracts. {}", e.getMessage());
                }
            }

            List<GasCustomerContract> gasContracts = gasContractService.findByCustomerOrderByEndDateDesc(customer);
            if (gasContracts.size() != 0) {
                try {
                    gasContractService.deleteByCustomer(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete gas contracts. {}", e.getMessage());
                }
            }

            List<MerchantServicesContract> merchantServicesContracts = merchantServicesService.findByCustomerOrderByEndDateDesc(customer);
            if (merchantServicesContracts.size() != 0) {
                try {
                    customerSiteService.findByCustomer(customer).forEach(merchantServicesService::deleteByCustomerSite);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete merchant services contracts. {}", e.getMessage());
                }
            }

            List<UtilityContract> utilityContracts = utilityContractService.findByCustomer(customer);
            if (utilityContracts.size() != 0) {
                try {
                    customerSiteService.findByCustomer(customer).forEach(utilityContractService::deleteByCustomerSite);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete utility contracts. {}", e.getMessage());
                }
            }

            List<CustomerSite> customerSites = customerSiteService.findByCustomer(customer);
            if (customerSites.size() != 0) {
                try {
                    customerSiteService.deleteByCustomer(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete customer sites. {}", e.getMessage());
                }
            }

            List<CustomerHistory> customerHistories = customerHistoryService.findByCustomer(customer);
            if (customerHistories.size() != 0) {
                try {
                    customerHistoryService.deleteByCustomer(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete customer history. {}", e.getMessage());
                }
            }

            BillingDetail billingDetail = customer.getBillingDetail();
            if (billingDetail != null) {
                try {
                    customerService.deleteBillingDetailByCustomer(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete customer billing detail. {}", e.getMessage());
                }
            }

            List<LinkedAccount> linkedAccounts = linkedAccountService.findByCustomer(customer);
            if (linkedAccounts.size() != 0) {
                try {
                    linkedAccountService.deleteLinkedAccountWithReferences(customer);
                } catch (Exception e) {
                    successfullyDeleted = false;
                    logger.info("Unable to delete linked accounts. {}", e.getMessage());
                }
            }

            if (successfullyDeleted) {
                customerService.deleteById(id);

                if (customerService.findById(id) != null) {
                    logger.info("Unable to delete customer ID: CUC{}", id);
                    return "redirect:/admin/removal/index/" + id;
                }

                logger.info("Customer deleted successfully ID: CUC{}", id);
                return "redirect:/admin/customer/customers/1";
            }

            return "redirect:/admin/removal/index/" + id;
        }
        return "redirect:/admin/removal/index/" + id;
    }
}
