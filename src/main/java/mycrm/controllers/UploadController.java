package mycrm.controllers;

import mycrm.services.UploadService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

    private static Logger logger = LogManager.getLogger();

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/data/upload")
    public String dataUploadView() {
        return "admin/data/upload";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping(value = "/uploadCustomers", method = RequestMethod.POST)
    public String uploadFile(@RequestPart(value = "uploadDataFile") MultipartFile multiPartFile, Model model) throws Exception {
        logger.info("Starting upload process");
        String unsuccessfulImportsLink = uploadService.uploadFile(multiPartFile);
        logger.info("Completed upload process");

        model.addAttribute("unsuccessfulImportsLink", unsuccessfulImportsLink);
        return "admin/data/importStatus";
    }

//    @PreAuthorize("hasAuthority('SUPERADMIN')")
//    @RequestMapping(value = "/uploadCustomers", method = RequestMethod.POST)
//    public ModelAndView uploadFileAndDownloadCSV(@RequestPart(value = "uploadDataFile") MultipartFile multiPartFile) throws IOException, ImportTooLargeException {
//        logger.info("Starting upload process");
//        List<CustomerImportEntity> unsuccessfulImports = uploadService.uploadFile(multiPartFile);
//        logger.info("Completed upload process");
//
//        Map<String, Object> model = new HashMap<>();
//        model.put("unsuccessfulImports", unsuccessfulImports);
//
//        //model.addAttribute("unsuccessfulImports", unsuccessfulImports);
//        return new ModelAndView(new UnsuccessfulImportCsv(), model);
//    }


}
