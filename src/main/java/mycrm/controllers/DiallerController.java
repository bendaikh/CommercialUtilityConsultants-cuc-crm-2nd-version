package mycrm.controllers;

import mycrm.models.Plugin;
import mycrm.services.DiallerExportService;
import mycrm.services.PluginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DiallerController {
    private static Logger logger = LogManager.getLogger();

    private final PluginService pluginService;

    private final DiallerExportService diallerExportService;
    @Value("${dialler.provider.name}")
    private String diallerProviderName;

    @Autowired
    public DiallerController(DiallerExportService diallerExportService, PluginService pluginService) {
        this.diallerExportService = diallerExportService;
        this.pluginService = pluginService;
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/data/export")
    public String dataExportView() {
        return "admin/data/export";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/runDialler")
    public String getAllDiallerData() {
        diallerExportService.exportAllDiallerData();
        return "redirect:/admin/data/export";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/runLatestDialler")
    public String getLatestDiallerData() {
        diallerExportService.exportLatestDiallerData();
        return "redirect:/admin/data/export";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/data/edit-dialler-config")
    public String editDiallerConfig(Model model) {
        Plugin plugin = pluginService.getPluginByApi(diallerProviderName);
        model.addAttribute("datasets", diallerExportService.getDatasets());
        model.addAttribute("primaryDataset", plugin.getPrimaryDataset());
        return "admin/data/edit-dialler-config";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/data/update-dataset/{dataset}/{status}")
    public String updateDataset(@PathVariable String dataset, @PathVariable String status) {
        diallerExportService.updateDataset(dataset, status);
        return "redirect:/admin/data/edit-dialler-config";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/admin/data/update-primary-dataset/{dataset}")
    public String updatePrimaryDataset(@PathVariable String dataset) {

        pluginService.updatePrimaryDataset(diallerProviderName, dataset);
        return "redirect:/admin/data/edit-dialler-config";
    }
}
