package mycrm.controllers;

import mycrm.models.*;
import mycrm.repositories.DocumentRepository;
import mycrm.repositories.MerchantDocumentRepository;
import mycrm.search.MerchantServicesContractSearchService;
import mycrm.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MerchantServicesController {

    public Set<MerchantServicesDocuments> merchantServicesDocuments = new HashSet<>();

    public Set<MerchantServicesDocuments> getMerchantServicesDocuments() {
        return merchantServicesDocuments;
    }

    public void setMerchantServicesDocuments(Set<MerchantServicesDocuments> merchantServicesDocuments) {
        this.merchantServicesDocuments = merchantServicesDocuments;
    }

    @Value("${customer.file.upload.location}")
    private String UPLOAD_DIR;

    private final MerchantDocumentRepository documentRepo;

    private final DoNotRenewReasonService doNotRenewReasonService;
    private static final Logger logger = LogManager.getLogger();

    private final CustomerSiteService customerSiteService;
    private final BrokerService brokerService;
    private final MerchantServicesService merchantServicesService;
    private final MerchantServicesContractSearchService merchantServicesContractSearchService;
    private final UserService userService;

    private final ContractReasonService contractReasonService;

    private final BrokerTransferHistoryService brokerTransferHistoryService;

    @Autowired
    public MerchantServicesController(CustomerSiteService customerSiteService,
                                      BrokerService brokerService,
                                      MerchantServicesService merchantServicesService,
                                      MerchantServicesContractSearchService merchantServicesContractSearchService,
                                      UserService userService,
                                      BrokerTransferHistoryService brokerTransferHistoryService,
                                      ContractReasonService contractReasonService,DoNotRenewReasonService doNotRenewReasonService,MerchantDocumentRepository documentRepo) {
        this.customerSiteService = customerSiteService;
        this.brokerService = brokerService;
        this.merchantServicesService = merchantServicesService;
        this.merchantServicesContractSearchService = merchantServicesContractSearchService;
        this.userService = userService;
        this.brokerTransferHistoryService = brokerTransferHistoryService;
        this.contractReasonService = contractReasonService;
        this.doNotRenewReasonService = doNotRenewReasonService;
        this.documentRepo = documentRepo;
    }

    @RequestMapping("/admin/customer/manage-merchant-services/{customerSiteID}")
    public String merchantServicesContract(@PathVariable String customerSiteID, Model model) {
        List<Broker> brokers = brokerService.findAll();

        MerchantServicesContract merchantServicesContract = new MerchantServicesContract();

        model.addAttribute("brokers", brokers);
        model.addAttribute("doNotRenewReasons",doNotRenewReasonService.findAll());
        model.addAttribute("lostRenewalReasons",contractReasonService.findAll());
        model.addAttribute("customerSite", customerSiteService.findById(Long.valueOf(customerSiteID)));
        model.addAttribute("merchantServicesContract", merchantServicesContract);
        return "admin/customer/manage-merchant-services";
    }
    @RequestMapping(value = "/merchantServicesContractPoup", method = RequestMethod.GET)
    public String saveMerchantServicesContractPop(MerchantServicesDocuments merchantServicesDocuments) {

//        this.merchantServicesDocuments.add(merchantServicesDocuments);
//        this.getMerchantServicesDocuments().add();
        this.getMerchantServicesDocuments().add(merchantServicesDocuments);
        System.out.println(merchantServicesDocuments);
        System.out.println(this.getMerchantServicesDocuments());
        return "redirect:/admin/index";
    }

    @RequestMapping(value = "/merchantServicesContract", method = RequestMethod.POST)
    public String saveMerchantServicesContract(MerchantServicesContract merchantServicesContract) {
        merchantServicesContract.setMerchantServicesDocuments(this.getMerchantServicesDocuments());
        MerchantServicesContract contract = merchantServicesService.save(merchantServicesContract);
        return "redirect:/admin/customer/viewsite/" + contract.getCustomerSite().getId();
    }


    @RequestMapping(value="/uploadMerchantDocument", method= RequestMethod.POST)
    public MerchantServicesDocuments handleFileUpload(
            @RequestParam("file") MultipartFile file,MerchantServicesDocuments uploadMerchantDocument){
        String name = "test11";
        if (!file.isEmpty()) {
            try {

                return uploadMerchantDocument;
            } catch (Exception e) {
                return uploadMerchantDocument;
            }
        } else {
            return uploadMerchantDocument;
        }
    }

/*    @RequestMapping(value = "/uploadMerchantDocument", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file, MerchantServicesDocuments uploadMerchantDocument,Model model)
            throws IOException {

        String filename = StringUtils.getFilename(file.getOriginalFilename());

        Path uploadLocation = Paths.get(UPLOAD_DIR + uploadMerchantDocument.getCustomer().getId() + "\\");

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }

            // This is a security check
            if (filename.contains("..")) {
                throw new RuntimeException(
                        "Cannot store file with relative path outside current directory " + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.createDirectories(uploadLocation);

                uploadMerchantDocument.setFileName(filename);
                uploadMerchantDocument.setFilePath(uploadLocation.toString());

//                logger.info("Filename to upload: {}", filename);
//                logger.info("Upload location: {}", uploadLocation.toString());

                // save document details to database
                MerchantServicesDocuments merchantServicesDocuments = save(uploadMerchantDocument);
                this.getMerchantServicesDocuments().add(merchantServicesDocuments);

                model.addAttribute("uploadMerchantDocument",merchantServicesDocuments);
                Files.copy(inputStream, uploadLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);


            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }

        return "redirect:/unauthorised";

    }*/

    public MerchantServicesDocuments save(MerchantServicesDocuments merchantServicesDocuments) {
        return this.documentRepo.save(merchantServicesDocuments);
    }
    @RequestMapping("/admin/customer/edit-merchant-services/{id}")
    public String editMerchantServicesContract(@PathVariable("id") Long id, Model model) {
        List<Broker> brokers = brokerService.findAll();

        MerchantServicesContract merchantServicesContract = merchantServicesService.findById(id);

        if (merchantServicesContract == null) {
            return "admin/error/404";
        }

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isBroker()) {
            if (!merchantServicesContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract",
                        merchantServicesContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isExternalBroker()) {
            if (!merchantServicesContract.getBroker().equals(user.getBroker())) {
                logger.info("Edit {} Contract - User is not the broker associated to this contract",
                        merchantServicesContract.getSupplyType());
                return "redirect:/unauthorised";
            }
        } else if (user.isLeads()) {
            logger.info("Edit {} Contract - User is not super admin", merchantServicesContract.getSupplyType());
            return "redirect:/unauthorised";
        }

        List<String> transferMessageList =
                brokerTransferHistoryService.findLatestMerchantServicesBrokerTransferHistory(merchantServicesContract);
        model.addAttribute("transferMessageList", transferMessageList);
        model.addAttribute("brokers", brokers);
        model.addAttribute("customerSite", customerSiteService.findById(merchantServicesContract.getCustomerSite().getId()));
        model.addAttribute("merchantServicesContract", merchantServicesContract);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("doNotRenewReasons",doNotRenewReasonService.findAll());
        model.addAttribute("lostRenewalReasons",contractReasonService.findAll());
        return "admin/customer/manage-merchant-services";
    }





    @RequestMapping("/admin/merchant-services/index/{pageNumber}")
    public String viewMerchantServicesHomePage(MerchantServicesContractSearch merchantServicesContractSearch,
                                               Model model,
                                               @PathVariable("pageNumber") int pageNumber) {
        long startTime = System.currentTimeMillis();
        MerchantServicesSearchResult merchantServicesSearchResult =
                merchantServicesContractSearchService.searchMerchantServicesContract(merchantServicesContractSearch,
                        pageNumber);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        merchantServicesSearchResult.getReturnedContracts().forEach(merchantServicesContract -> {
            logger.info("{}", merchantServicesContract.getId());
        });

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("contracts", merchantServicesSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", merchantServicesSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", merchantServicesSearchResult.getTotalPages());
        model.addAttribute("totalContracts", merchantServicesSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/merchant-services/index";
    }

    @RequestMapping("/admin/merchant-services/renewals/{page}")
    public String viewMerchantServiceRenewals(MerchantServicesContractSearch merchantServicesContractSearch,
                                              Model model,
                                              @PathVariable("page") int page) {

        merchantServicesContractSearch.setRenewalSearch(true);

        long startTime = System.currentTimeMillis();
        MerchantServicesSearchResult merchantServicesSearchResult =
                merchantServicesContractSearchService.searchMerchantServicesContract(merchantServicesContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("renewals", merchantServicesSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", merchantServicesSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", merchantServicesSearchResult.getTotalPages());
        model.addAttribute("totalContracts", merchantServicesSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);

        return "admin/merchant-services/renewals";
    }

    @RequestMapping("/admin/merchant-services/callbacks/{page}")
    public String viewMerchantServiceCallbacks(MerchantServicesContractSearch merchantServicesContractSearch,
                                               Model model,
                                               @PathVariable("page") int page) {

        merchantServicesContractSearch.setCallbackSearch(true);

        long startTime = System.currentTimeMillis();
        MerchantServicesSearchResult merchantServicesSearchResult =
                merchantServicesContractSearchService.searchMerchantServicesContract(merchantServicesContractSearch, page);
        long endTime = System.currentTimeMillis();

        long timeTaken = (endTime - startTime);

        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("callbacks", merchantServicesSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", merchantServicesSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", merchantServicesSearchResult.getTotalPages());
        model.addAttribute("totalContracts", merchantServicesSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);

        return "admin/merchant-services/callbacks";
    }

    @RequestMapping("/admin/merchant-services/leads/{page}")
    public String viewMerchantServiceLeads(MerchantServicesContractSearch merchantServicesContractSearch, Model model, @PathVariable("page") int page) {

        merchantServicesContractSearch.setLeadSearch(true);
        long startTime = System.currentTimeMillis();
        MerchantServicesSearchResult merchantServicesSearchResult =
                merchantServicesContractSearchService.searchMerchantServicesContract(merchantServicesContractSearch, page);
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("leads", merchantServicesSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", merchantServicesSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", merchantServicesSearchResult.getTotalPages());
        model.addAttribute("totalContracts", merchantServicesSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/merchant-services/leads";
    }

    @RequestMapping("/admin/merchant-services/lost-renewals/{page}")
    public String viewLostMerchantServiceRenewals(MerchantServicesContractSearch merchantServicesContractSearch, Model model, @PathVariable("page") int page) {

        merchantServicesContractSearch.setLostRenewalSearch(true);
        merchantServicesContractSearch.setLostRenewal(true);
        long startTime = System.currentTimeMillis();
        MerchantServicesSearchResult merchantServicesSearchResult =
                merchantServicesContractSearchService.searchMerchantServicesContract(merchantServicesContractSearch, page);

        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        model.addAttribute("brokers", brokerService.findAll());
        model.addAttribute("lostRenewals", merchantServicesSearchResult.getReturnedContracts());
        model.addAttribute("totalResults", merchantServicesSearchResult.getReturnedContractCount());
        model.addAttribute("totalPages", merchantServicesSearchResult.getTotalPages());
        model.addAttribute("totalContracts", merchantServicesSearchResult.getTotalContractCount());
        model.addAttribute("pageNumber", page);
        model.addAttribute("timeTaken", timeTaken);
        return "admin/merchant-services/lost-renewals";
    }
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/customer/deleteMerchantContract/{id}")
    public String deleteMerchantContract(@PathVariable("id") Long id) {
        MerchantServicesContract merchantServicesContract = merchantServicesService.findById(id);

        if (merchantServicesContract != null) {
            merchantServicesService.deleteById(id);
            return "redirect:/admin/customer/viewsite/" + merchantServicesContract.getCustomerSite().getId();
        }
        return "redirect:/admin/index";
    }
}
