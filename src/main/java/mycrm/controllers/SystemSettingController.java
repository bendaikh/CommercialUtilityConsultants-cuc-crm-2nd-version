package mycrm.controllers;

import mycrm.models.DocumentFolder;
import mycrm.models.EmailSetting;
import mycrm.models.Plugin;
import mycrm.services.DocumentFolderService;
import mycrm.services.DocumentService;
import mycrm.services.EmailSettingService;
import mycrm.services.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class SystemSettingController {

    private final PluginService pluginService;
    private final EmailSettingService emailSettingService;
    private final DocumentFolderService documentFolderService;
    private final DocumentService documentService;

    @Autowired
    public SystemSettingController(PluginService pluginService,
                                   EmailSettingService emailSettingService,
                                   DocumentFolderService documentFolderService, DocumentService documentService) {
        this.pluginService = pluginService;
        this.emailSettingService = emailSettingService;
        this.documentFolderService = documentFolderService;
        this.documentService = documentService;
    }

    @RequestMapping("/admin/system-setting/index")
    public String viewSystemSettings() {
        return "admin/system-setting/index";
    }

    @RequestMapping("/admin/system-setting/plugins")
    public String viewPlugins(Model model) {

        model.addAttribute("plugins", pluginService.findAll());
        return "admin/system-setting/plugins";
    }

    @RequestMapping(value = "/plugin", method = RequestMethod.POST)
    public String savePlugin(Plugin plugin) {

        try {
            pluginService.save(plugin);
        } catch (Exception e) {
            return "redirect:/admin/system-setting/plugins";
        }

        return "redirect:/admin/system-setting/plugins";
    }

    @RequestMapping("/admin/system-setting/plugin/edit-plugin/{id}")
    public String editPlugin(@PathVariable("id") Long id, Model model) {
        Plugin plugin = pluginService.findById(id);

        model.addAttribute("plugin", plugin);
        return "admin/system-setting/edit-plugin";
    }

    @RequestMapping("/admin/system-setting/email-settings/index")
    public String viewEmailSettings(Model model) {
        List<EmailSetting> emailSettingsList = emailSettingService.findAll();

        model.addAttribute("emailSettingsList", emailSettingsList);
        return "admin/system-setting/email-settings/index";
    }

    @RequestMapping("/admin/system-setting/email-settings/edit/{id}")
    public String editEmailSettings(@PathVariable("id") Long id, Model model) {
        EmailSetting emailSetting = emailSettingService.findById(id);

        model.addAttribute("emailSetting", emailSetting);
        return "admin/system-setting/email-settings/edit";
    }

    @RequestMapping(value = "/admin/system-setting/email-settings/save", method = RequestMethod.POST)
    public String saveEmailSettings(EmailSetting emailSetting) {
        try {
            emailSettingService.save(emailSetting);
        } catch (Exception e) {
            return "redirect:/admin/system-setting/email-settings/edit/" + emailSetting.getId();
        }

        return "redirect:/admin/system-setting/email-settings/index";
    }

    // create folder page
    @RequestMapping(value = "/admin/system-setting/document-settings/save-folder", method = RequestMethod.POST)
    public String createFolderPage(DocumentFolder documentFolder) {
        documentFolderService.save(documentFolder);

        return "redirect:/admin/system-setting/document-settings/index";
    }

    // create sub folder page
    @RequestMapping(value = "/admin/system-setting/document-settings/save-subfolder", method = RequestMethod.POST)
    public String createSubFolderPage(Long parentId, String subfolderName) {
        documentFolderService.saveSubfolder(parentId, subfolderName);

        return "redirect:/admin/system-setting/document-settings/index";
    }

    // folder page
    @RequestMapping("/admin/system-setting/document-settings/index")
    public String listDocumentFolders(Model model) {
        model.addAttribute("folders", documentFolderService.documentsFolderList());
        return "admin/system-setting/document-settings/index";
    }

    // folder page
    @RequestMapping("/admin/system-setting/document-settings/edit/{id}")
    public String editDocumentFolders(@PathVariable("id") Long id, Model model) {
        DocumentFolder documentFolder = documentFolderService.findById(id);

        if (documentFolder == null) {
            return "redirect:/admin/system-setting/document-settings/index";
        }

        int numberOfDocuments = documentService.getNumberOfDocumentsInDocumentFolder(documentFolder);
        List<DocumentFolder> parentFolders = documentFolderService.findParentFolders();
        parentFolders.remove(documentFolder);

        boolean hasSubFolders = documentFolderService.hasSubFolders(documentFolder);

        model.addAttribute("numberOfDocuments", numberOfDocuments);
        model.addAttribute("parentFolders", parentFolders);
        model.addAttribute("documentFolder", documentFolder);
        model.addAttribute("hasSubFolders", hasSubFolders);
        return "admin/system-setting/document-settings/edit";
    }

    @RequestMapping("/admin/system-setting/document-settings/delete-folder/{id}")
    public String deleteDocumentFolder(@PathVariable("id") Long id) {
        try {
            documentFolderService.deleteFolder(id);
        } catch (Exception e) {

        }
        return "redirect:/admin/system-setting/document-settings/index";
    }
}
