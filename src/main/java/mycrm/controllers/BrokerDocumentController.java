package mycrm.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import mycrm.models.Broker;
import mycrm.models.BrokerDocument;
import mycrm.services.BrokerDocumentService;
import mycrm.services.BrokerService;

@Controller
public class BrokerDocumentController {

    private static Logger logger = LogManager.getLogger();

    private final BrokerDocumentService brokerDocumentService;
    private final BrokerService brokerService;

    @Autowired
    public BrokerDocumentController(BrokerDocumentService brokerDocumentService, BrokerService brokerService) {
        this.brokerDocumentService = brokerDocumentService;
        this.brokerService = brokerService;
    }

    // open a document from file system
    @ResponseBody
    @RequestMapping("admin/broker/opendocument/{id}")
    public ResponseEntity<Resource> openDocument(HttpServletResponse response, @PathVariable("id") Long id)
            throws FileNotFoundException {

        BrokerDocument brokerDocument = brokerDocumentService.findById(id);

        if (brokerDocument == null) {
            return ResponseEntity.badRequest().build();
        }

        Resource file = brokerDocumentService.loadAsResource(brokerDocument);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    // view broker documents
    @RequestMapping("/admin/broker/brokerdocuments/{id}")
    public String viewBrokerDocuments(@PathVariable("id") Long id, Model model) {
        Broker broker = brokerService.findById(id);

        List<BrokerDocument> brokerDocuments = brokerDocumentService.findByBroker(broker);

        model.addAttribute("broker", broker);
        model.addAttribute("brokerDocuments", brokerDocuments);

        return "admin/broker/brokerdocuments";
    }

    // document upload page
    @RequestMapping("/admin/broker/uploaddocument/{id}")
    public String brokerFileUploadPage(@PathVariable("id") Long id, Model model) {

        model.addAttribute("broker", brokerService.findById(id));

        return "admin/broker/uploaddocument";
    }

    // document upload facility
    @RequestMapping(value = "/uploadBrokerFile", method = RequestMethod.POST)
    public String uploadBrokerFile(@RequestParam("uploadfile") MultipartFile uploadfile, BrokerDocument brokerDocument)
            throws IOException {

        logger.info("Upload Broker File - attempting to save file");

        brokerDocumentService.storeFile(uploadfile, brokerDocument);

        return "redirect:/admin/broker/brokerdocuments/" + brokerDocument.getBroker().getId();
    } // method uploadFile
}