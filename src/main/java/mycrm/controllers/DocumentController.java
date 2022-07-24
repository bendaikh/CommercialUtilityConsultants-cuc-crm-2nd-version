package mycrm.controllers;

import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.models.Document;
import mycrm.models.DocumentFolder;
import mycrm.services.ContactService;
import mycrm.services.CustomerService;
import mycrm.services.DocumentFolderService;
import mycrm.services.DocumentService;
import mycrm.services.LinkedAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class DocumentController {

    private static final Logger logger = LogManager.getLogger();

    private final DocumentService documentService;
    private final CustomerService customerService;
    private final ContactService contactService;
    private final LinkedAccountService linkedAccountService;
    private final DocumentFolderService documentFolderService;

    @Autowired
    public DocumentController(DocumentService documentService,
                              CustomerService customerService,
                              ContactService contactService,
                              LinkedAccountService linkedAccountService,
                              DocumentFolderService documentFolderService) {
        this.documentService = documentService;
        this.customerService = customerService;
        this.contactService = contactService;
        this.linkedAccountService = linkedAccountService;
        this.documentFolderService = documentFolderService;
    }

    // open a document from file system
    @ResponseBody
    @RequestMapping("admin/customer/opendocument/{id}")
    public ResponseEntity<Resource> openDocument(HttpServletResponse response, @PathVariable("id") Long id)
            throws FileNotFoundException {

        Document document = documentService.findById(id);

        if (document == null) {
            return ResponseEntity.badRequest().build();
        }

        Resource file = documentService.loadAsResource(document);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    // document upload page
    @RequestMapping(value = {
            "admin/customer/customerdocuments/{id}",
            "admin/customer/customerdocuments/{id}/{heading}"
    })
    public String viewCustomerDocuments(@PathVariable("id") Long id,
                                        @PathVariable(value = "heading", required = false) Long heading,
                                        Model model) {

        Customer customer = customerService.findById(id);
        List<Contact> contactList = contactService.findByCustomer(customer);

        if (heading == null) {
            heading = Long.valueOf(0);
        }

        model.addAttribute("documents", documentService.findByCustomerAndDirectory(customer, heading));
        model.addAttribute("heading", heading);
        model.addAttribute("selectedDocumentFolder", documentFolderService.findById(heading));
        model.addAttribute("customer", customer);
        model.addAttribute("contactList", contactList);
        model.addAttribute("linkedAccountsSize", linkedAccountService.findByCustomer(customer).size());
        model.addAttribute("documentDirectory", new DocumentFolder());
        model.addAttribute("folders", documentFolderService.documentsFolderList());

        return "admin/customer/customerdocuments";
    }

    // document upload page
    @RequestMapping(value = {"admin/customer/uploaddocument/{id}", "admin/customer/uploaddocument/{id}/{heading}"})
    public String fileUploadPage(@PathVariable("id") Long id,
                                 @PathVariable(value = "heading", required = false) Long heading,
                                 Model model) {

        Customer customer = customerService.findById(id);

        model.addAttribute("customer", customer);
        model.addAttribute("linkedAccountsSize", linkedAccountService.findByCustomer(customer).size());

        if (heading != null && heading > 0) {
            model.addAttribute("heading", heading);
            model.addAttribute("folderName", documentFolderService.folderNameById(heading));
        } else {
            model.addAttribute("heading", "0");
            model.addAttribute("folderName", "Home Folder");
        }

        return "admin/customer/uploaddocument";
    }

    // document upload facility
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile, Document document)
            throws IOException {

        documentService.storeFile(uploadfile, document);

        return "redirect:/admin/customer/customerdocuments/" + document.getCustomer().getId();
    } // method uploadFile

    //delete document
    @RequestMapping("admin/customer/deleteDocument/{id}")
    public String deleteDocument(@PathVariable("id") Long id) {
        Document document = documentService.findById(id);

        if (document != null) {
            Customer customer = document.getCustomer();
            try {
                documentService.deleteById(id);
                logger.info("Document deleted");
            } catch (Exception e) {
                logger.info("Unable to delete document due to {}", e.getMessage());
            }

            return "redirect:/admin/customer/customerdocuments/" + customer.getId();
        }
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/customer/edit-document/{id}")
    public String editDocument(@PathVariable("id") Long id, Model model) {
        Document document = documentService.findById(id);

        if (document == null) {
            return "redirect:/admin/";
        }

        Customer customer = document.getCustomer();

        model.addAttribute("linkedAccountsSize", linkedAccountService.findByCustomer(customer).size());
        model.addAttribute("folders", documentFolderService.documentsFolderList());
        model.addAttribute("customer", customer);
        model.addAttribute("document", document);

        return "admin/customer/edit-document";
    }

    @RequestMapping(value = "/admin/customer/save-document", method = RequestMethod.POST)
    public String updateDocument(Long id, String fileTitle, Long documentFolder, String validFrom, String validTo, String documentStatus) {

        Date documentValidFrom = null;
        Date documentValidTo = null;

        try {
            documentValidFrom = StringUtils.hasText(validFrom) ? new SimpleDateFormat("yyyy-MM-dd").parse(validFrom) : null;
            documentValidTo = StringUtils.hasText(validTo) ? new SimpleDateFormat("yyyy-MM-dd").parse(validTo) : null;
        } catch (Exception e) {
            logger.info("Unable to parse date");
        }

        Document updatedDocument = documentService.updateDocument(id, fileTitle, documentFolder, documentValidFrom, documentValidTo, documentStatus);
        return "redirect:/admin/customer/customerdocuments/"
                + updatedDocument.getCustomer().getId()
                + "/" + updatedDocument.getDocumentFolder();
    }
}
