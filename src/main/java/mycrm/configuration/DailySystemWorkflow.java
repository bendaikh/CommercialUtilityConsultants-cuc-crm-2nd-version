package mycrm.configuration;

import mycrm.models.EmailSetting;
import mycrm.models.Plugin;
import mycrm.services.DiallerExportService;
import mycrm.services.EmailSettingService;
import mycrm.services.PluginService;
import mycrm.services.RenewalEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class DailySystemWorkflow {
    private static final Logger logger = LogManager.getLogger();
    private final DiallerExportService diallerExportService;
    private final RenewalEmailService renewalEmailService;
    private final EmailSettingService emailSettingService;

    private final PluginService pluginService;
    @Value("${dialler.provider.name}")
    private String diallerProviderName;

    @Autowired
    public DailySystemWorkflow(DiallerExportService diallerExportService,
                               RenewalEmailService renewalEmailService,
                               EmailSettingService emailSettingService,
                               PluginService pluginService) {
        this.diallerExportService = diallerExportService;
        this.renewalEmailService = renewalEmailService;
        this.emailSettingService = emailSettingService;
        this.pluginService = pluginService;
    }

    // every 24 hours
    @Scheduled(cron = "0 30 6 * * ?")
    public void runDailyDialler() {

        Plugin diallerPlugin = pluginService.getPluginByApi(diallerProviderName);
        if (diallerPlugin == null) {
            logger.info("--------------------------- Dialler export didn't run! -------------------------");
            return;
        }

        if (diallerPlugin.isEnabled()) {

            logger.info("--------------------------- Starting Dialler export -------------------------");

            diallerExportService.exportLatestDiallerData();

            logger.info("--------------------------- Ending Dialler export -------------------------");
        } else {
            logger.info("--------------------------- Dialler disabled -------------------------");
        }
    }

    //@Scheduled(fixedDelay = 30000)
    @Scheduled(cron = "0 30 7 * * ?")
    public void runDailyRenewalEmails() {

        EmailSetting renewalEmailSettings = getEmailSettings(EmailType.RENEWAL);
        if (renewalEmailSettings == null) {
            logger.info("--------------------------- Renewal Email Service didn't run! -------------------------");
            return;
        }

        if (renewalEmailSettings.isEnabled()) {
            logger.info("--------------------------- Starting Renewal Email Service -------------------------");

            renewalEmailService.sendDailyRenewalEmails(EmailProcess.AUTOMATED.getName());

            logger.info("--------------------------- Ending Renewal Email Service -------------------------");
        } else {
            logger.info("--------------------------- Renewal Email Disabled  -------------------------");
        }


    }

    private EmailSetting getEmailSettings(EmailType emailType) {
        EmailSetting emailSetting = this.emailSettingService.getEmailSetting(emailType.getType());
        if (emailSetting != null) {
            return emailSetting;
        }

        logger.info("Unable to find {} email settings", emailType.getType());
        return null;
    }
}
