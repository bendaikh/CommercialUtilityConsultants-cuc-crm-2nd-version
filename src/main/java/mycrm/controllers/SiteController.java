package mycrm.controllers;

import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.CustomerSiteWithContracts;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.MerchantServicesContract;
import mycrm.models.UtilityContract;
import mycrm.services.ContactService;
import mycrm.services.CustomerDataService;
import mycrm.services.CustomerService;
import mycrm.services.CustomerSiteService;
import mycrm.services.CustomerSiteTransferHistoryService;
import mycrm.services.ElecContractService;
import mycrm.services.GasContractService;
import mycrm.services.LinkedAccountService;
import mycrm.services.MerchantServicesService;
import mycrm.services.UtilityContractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SiteController {

    private static Logger logger = LogManager.getLogger();

    private final CustomerSiteService customerSiteService;
    private final CustomerService customerService;
    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final ContactService contactService;
    private final CustomerDataService customerDataService;
    private final UtilityContractService utilityContractService;
    private final LinkedAccountService linkedAccountService;
    private final CustomerSiteTransferHistoryService customerSiteTransferHistoryService;
    private final MerchantServicesService merchantServicesService;

    @Autowired
    public SiteController(CustomerService customerService, CustomerSiteService customerSiteService,
                          GasContractService gasContractService, ElecContractService elecContractService,
                          ContactService contactService, CustomerDataService customerDataService,
                          UtilityContractService utilityContractService, LinkedAccountService linkedAccountService,
                          CustomerSiteTransferHistoryService customerSiteTransferHistoryService,
                          MerchantServicesService merchantServicesService) {
        this.customerService = customerService;
        this.customerSiteService = customerSiteService;
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.contactService = contactService;
        this.customerDataService = customerDataService;
        this.utilityContractService = utilityContractService;
        this.linkedAccountService = linkedAccountService;
        this.customerSiteTransferHistoryService = customerSiteTransferHistoryService;
        this.merchantServicesService = merchantServicesService;
    }

    //view customer sites
    @RequestMapping("/admin/customer/customersites/{id}")
    public String viewCustomerSites(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findById(id);
        List<CustomerSite> customerSite = customerSiteService.findByCustomer(customer);
//        List<GasCustomerContract> latestGasCustomerContract = gasContractService.findLatestGasCustomerContractByCustomer(customer.getId());
//        List<ElecCustomerContract> latestElecCustomerContract = elecContractService.findLatestElecCustomerContractByCustomer(customer.getId());
        List<Contact> contactList = contactService.findByCustomer(customer);

        List<CustomerSiteWithContracts> customerSitesWithContracts = customerSiteService.customerSitesWithContracts(customer);

        model.addAttribute("customer", customer);
        model.addAttribute("customerSites", customerSite);
//        model.addAttribute("latestGasCustomerContract", latestGasCustomerContract);
//        model.addAttribute("latestElecCustomerContract", latestElecCustomerContract);
        model.addAttribute("contactList", contactList);
        model.addAttribute("customerSitesWithContracts", customerSitesWithContracts);
        model.addAttribute("linkedAccountsSize", linkedAccountService.findByCustomer(customer).size());
        return "admin/customer/customersites";
    }

    // open site and view all the site details
    @RequestMapping("/admin/customer/viewsite/{id}")
    public String viewSite(@PathVariable("id") Long id, Model model) {

        CustomerSite customerSite = customerSiteService.findById(id);
        List<GasCustomerContract> gasCustomerContracts = gasContractService.findByCustomerSite(customerSite);
        List<ElecCustomerContract> elecCustomerContracts = elecContractService.findByCustomerSite(customerSite);
        List<UtilityContract> solarContracts = utilityContractService.findSolarContractByCustomerSite(customerSite);
        List<UtilityContract> waterContracts = utilityContractService.findWaterContractByCustomerSite(customerSite);
        List<UtilityContract> voipContracts = utilityContractService.findVoipContractByCustomerSite(customerSite);
        List<UtilityContract> landlineContracts = utilityContractService.findLandlineContractByCustomerSite(customerSite);
        List<UtilityContract> mobileContracts = utilityContractService.findMobileContractByCustomerSite(customerSite);
        List<UtilityContract> broadbandContracts = utilityContractService.findBroadbandContractByCustomerSite(customerSite);
        List<MerchantServicesContract> merchantServicesContracts = merchantServicesService.findMerchantServicesContractByCustomerSite(customerSite);
        List<String> siteTransferMessage = customerSiteTransferHistoryService.findCustomerSiteTransferHistoryByCustomerSite(customerSite);

        model.addAttribute("customerSite", customerSite);
        model.addAttribute("gasCustomerContracts", gasCustomerContracts);
        model.addAttribute("elecCustomerContracts", elecCustomerContracts);
        model.addAttribute("solarContracts", solarContracts);
        model.addAttribute("waterContracts", waterContracts);
        model.addAttribute("voipContracts", voipContracts);
        model.addAttribute("landlineContracts", landlineContracts);
        model.addAttribute("mobileContracts", mobileContracts);
        model.addAttribute("broadbandContracts", broadbandContracts);
        model.addAttribute("merchantServicesContracts", merchantServicesContracts);
        model.addAttribute("siteTransferMessage", siteTransferMessage);

        return "admin/customer/viewsite";
    }

    // edit site
    @RequestMapping("/admin/customer/editsite/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {

        CustomerSite customerSite = customerSiteService.findById(id);

        model.addAttribute("customerDetails", customerSite.getCustomer());
        model.addAttribute("customerSite", customerSite);
        model.addAttribute("customerId", customerSite.getCustomer().getId());
        return "admin/customer/newsite";
    }

    // add new site view
    @RequestMapping("/admin/customer/newsite/{customerId}")
    public String newCustomerSites(@PathVariable("customerId") Long customerId, Model model) {
        model.addAttribute("customerDetails", customerService.findById(customerId));
        model.addAttribute("customerSite", new CustomerSite());
        return "admin/customer/newsite";
    }

    // create site
    @RequestMapping(value = "/customerSite", method = RequestMethod.POST)
    public String saveCustomerSites(CustomerSite customerSite) {
        CustomerSite newCustomerSite = customerSiteService.save(customerSite);
        return "redirect:/admin/customer/viewsite/" + newCustomerSite.getId();
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/customer/deleteSite/{id}")
    public String deleteSite(@PathVariable("id") Long id) {
        CustomerSite customerSite = customerSiteService.findById(id);
        if (customerSite != null) {
            Long customerId = customerSite.getCustomer().getId();
            customerSiteService.deleteById(id);
            logger.info("Customer site delete");

            return "redirect:/admin/customer/customersites/" + customerId;
        }

        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/electric-contract-popup/{id}")
    public String viewElectricContractPopup(@PathVariable("id") Long id, Model model) {
        ElecCustomerContract electricContract = elecContractService.findById(id);
        if (electricContract != null) {
            model.addAttribute("electricContract", electricContract);
            return "admin/customer/electric-contract-popup";
        }
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/merchant-services-contract-popup/{id}")
    public String viewMerchantServicesContractPopup(@PathVariable("id") Long id, Model model) {
        MerchantServicesContract merchantServicesContract = merchantServicesService.findById(id);
        if (merchantServicesContract != null) {
            model.addAttribute("merchantServicesContract", merchantServicesContract);
            return "admin/customer/merchant-services-contract-popup";
        }
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/gas-contract-popup/{id}")
    public String viewGasContractPopup(@PathVariable("id") Long id, Model model) {
        GasCustomerContract gasContract = gasContractService.findById(id);
        if (gasContract != null) {
            model.addAttribute("gasContract", gasContract);
            return "admin/customer/gas-contract-popup";
        }
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/utility-contract-popup/{id}")
    public String viewUtilityContractPopup(@PathVariable("id") Long id, Model model) {
        UtilityContract utilityContract = utilityContractService.findById(id);
        if (utilityContract != null) {
            model.addAttribute("utilityContract", utilityContract);
            return "admin/customer/utility-contract-popup";
        }
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/gas-communication-popup/{id}")
    public String viewGasCommunicationPopup(@PathVariable("id") Long id, Model model) {
        GasCustomerContract gasContract = gasContractService.findById(id);
        if (gasContract != null) {
            model.addAttribute("allCustomerEmails",
                    customerDataService.getAllCustomerEmails(gasContract.getCustomer()));
            model.addAttribute("gasContract", gasContract);
            return "admin/customer/gas-communication-popup";
        }
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/electric-communication-popup/{id}")
    public String viewElectricCommunicationPopup(@PathVariable("id") Long id, Model model) {
        ElecCustomerContract electricContract = elecContractService.findById(id);
        if (electricContract != null) {
            model.addAttribute("allCustomerEmails",
                    customerDataService.getAllCustomerEmails(electricContract.getCustomer()));
            model.addAttribute("electricContract", electricContract);
            return "admin/customer/electric-communication-popup";
        }
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/utility-communication-popup/{id}")
    public String viewUtilityCommunicationPopup(@PathVariable("id") Long id, Model model) {
        UtilityContract utilityContract = utilityContractService.findById(id);
        if (utilityContract != null) {
            model.addAttribute("allCustomerEmails",
                    customerDataService.getAllCustomerEmails(utilityContract.getCustomerSite().getCustomer()));
            model.addAttribute("utilityContract", utilityContract);
            return "admin/customer/utility-communication-popup";
        }
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/transfer-site/{id}")
    public String viewTransferSitePage(@PathVariable("id") Long id, Model model) {
        CustomerSite customerSite = customerSiteService.findById(id);

        if (customerSite == null) {
            return "redirect:/admin/customer/editsite/" + id;
        }
        model.addAttribute("customerDetails", customerSite.getCustomer());
        model.addAttribute("customerSite", customerSite);
        return "admin/customer/transfer-site";
    }

    @RequestMapping(value = "/admin/customer/transfer-customer-site")
    public String transferCustomerSite(@RequestParam Long selectedCustomerSite,
                                       @RequestParam Long selectedCustomer) {

        CustomerSite cs = customerSiteService.findById(selectedCustomerSite);
        Customer c = customerService.findById(selectedCustomer);

        if (cs == null || c == null) {
            return "redirect:/admin/customer/viewsite/" + selectedCustomerSite;
        }

        CustomerSite customerSite = customerSiteService.transferCustomerSite(cs, c);
        return "redirect:/admin/customer/viewsite/" + customerSite.getId();
    }

    @RequestMapping("/admin/customer/customer-search")
    public String viewCustomerSearchPanel(@RequestParam(value = "customerSite", required = false) Long customerSite,
                                          @RequestParam(value = "q", required = false) String q,
                                          Model model) {
        if (StringUtils.hasText(q)) {
            Customer searchResult = customerService.findByCustomerReference(q);
            model.addAttribute("searchResult", searchResult);
        }
        model.addAttribute("customerSite", customerSiteService.findById(customerSite));
        return "admin/customer/components/customer-search";
    }

}
