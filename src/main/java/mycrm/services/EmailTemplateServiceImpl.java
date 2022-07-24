package mycrm.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import mycrm.configuration.EmailType;
import mycrm.mail.EmailService;
import mycrm.models.Customer;
import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.DiallerAPIResponse;
import mycrm.models.EmailSetting;
import mycrm.models.EnergyContract;
import mycrm.models.User;
import mycrm.upload.DiallerDatasetType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Primary
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private static Logger logger = LogManager.getLogger();

    private final EmailService emailService;
    private final Configuration configuration;
    private final EmailSettingService emailSettingService;

    @Autowired
    public EmailTemplateServiceImpl(EmailService emailService, Configuration configuration, EmailSettingService emailSettingService) {
        this.emailService = emailService;
        this.configuration = configuration;
        this.emailSettingService = emailSettingService;
    }

    @Override
    public void sendTaggedCustomerNoteNotification(CustomerNote customerNote) throws Exception {
        User taggedUser = customerNote.getTaggedUser();

        String toEmail = taggedUser.getEmail();

        if (!StringUtils.hasText(toEmail)) {
            return;
        }

        String createdByEmail = customerNote.getCreatedBy().getEmail();

        if (!StringUtils.hasText(createdByEmail)) {
            return;
        }

        if (toEmail.equals(createdByEmail)) {
            return;
        }

        String subject = getSubject(customerNote.getCustomer());

        String noteLink = "http://mbserver:5668/admin/customer/customernotes/"
                + customerNote.getCustomer().getId()
                + "/?noteId=" + customerNote.getId();

        Map<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        model.put("to", taggedUser.getFirstName());
        model.put("sender", "Commercial Utility Consultants");
        model.put("noteLink", noteLink);

        configuration.setClassForTemplateLoading(this.getClass(), "/templates/email-templates");
        Template template = configuration.getTemplate("note-notification.ftl");

        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        emailService.sendMimeMessage(new String[]{toEmail}, subject, text);
    }

    @Override
    public void sendTaggedCustomerChildNoteNotification(CustomerChildNote customerChildNote) throws Exception {

        String toEmail = customerChildNote.getReplyTaggedUser().getEmail();

        if (!StringUtils.hasText(toEmail)) {
            return;
        }

        String createdByEmail = customerChildNote.getCreatedBy().getEmail();

        if (!StringUtils.hasText(createdByEmail)) {
            return;
        }

        if (toEmail.equals(createdByEmail)) {
            return;
        }

        String subject = getSubject(customerChildNote.getCustomer());

        String noteLink = "http://mbserver:5668/admin/customer/customernotes/"
                + customerChildNote.getCustomer().getId()
                + "/?noteId=" + customerChildNote.getCustomerNote().getId() + "&childNoteId=" + customerChildNote.getId();

        Map<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        model.put("to", customerChildNote.getReplyTaggedUser().getFirstName());
        model.put("sender", "Commercial Utility Consultants");
        model.put("noteLink", noteLink);

        configuration.setClassForTemplateLoading(this.getClass(), "/templates/email-templates");
        Template template = configuration.getTemplate("note-notification.ftl");

        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        emailService.sendMimeMessage(new String[]{toEmail}, subject, text);
    }

    @Override
    public void sendDiallerFailedEmailAlert(DiallerAPIResponse diallerAPIResponse, DiallerDatasetType type) throws Exception {
        String subject = type.getMethod().toUpperCase() + ": Dialler Upload failed";

        Map<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        model.put("to", "Sir");
        model.put("sender", "Commercial Utility Consultants");
        model.put("message", "Uploads to the Dialler were not successful when attempting to " + type.getMethod() + ". " + diallerAPIResponse.toString());

        configuration.setClassForTemplateLoading(this.getClass(), "/templates/email-templates");
        Template template = configuration.getTemplate("basic-email.ftl");

        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        String to[] = {"mohammed@commercialutilityconsultants.co.uk"};

        emailService.sendMimeMessage(to, subject, text);
    }

    @Override
    public void sendDiallerUploadNotNeeded(String message) throws Exception {
        String subject = "Dialler Upload Stats";

        Map<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        model.put("to", "Sir");
        model.put("sender", "Commercial Utility Consultants");
        model.put("message", message);

        configuration.setClassForTemplateLoading(this.getClass(), "/templates/email-templates");
        Template template = configuration.getTemplate("basic-email.ftl");

        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        String to[] = {"mohammed@commercialutilityconsultants.co.uk"};

        emailService.sendMimeMessage(to, subject, text);
    }

    @Override
    public void sendDiallerSuccessEmailAlert(DiallerAPIResponse response, DiallerDatasetType type) throws Exception {
        String subject = type.getMethod().toUpperCase() + ": Dialler Upload Successful";

        Map<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        model.put("to", "Sir");
        model.put("sender", "Commercial Utility Consultants");
        model.put("message",
                "Uploads to the Dialler were successful in the " + type.getMethod() + " process. "
                        + response.getTotal() + " records uploaded. "
                        + (StringUtils.hasText(response.getInfo()) ? response.getInfo() : ""));

        configuration.setClassForTemplateLoading(this.getClass(), "/templates/email-templates");
        Template template = configuration.getTemplate("basic-email.ftl");

        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        String to[] = {"mohammed@commercialutilityconsultants.co.uk"};

        emailService.sendMimeMessage(to, subject, text);
    }

    @Override
    public boolean sendSoldEmail(String customerReference, String emailAddress, String accountNumber) {
        EmailSetting emailSetting = emailSettingService.getEmailSetting(EmailType.SOLD.getType());

        String subject = "Welcome to the Family!";

        String emailTemplate = "";

        if ("SIMPLE".equals(emailSetting.getEmailFormat())) {
            emailTemplate = "sold-notification-simple.ftl";
        }
        if ("ENHANCED".equals(emailSetting.getEmailFormat())) {
            emailTemplate = "sold-notification-enhanced.ftl";
        }

        return sendTemplatedEmail(emailTemplate, emailSetting.getEmailType(), getEmailContentMap(customerReference, accountNumber), subject, emailAddress);
    }

    @Override
    public boolean sendRenewalEmail(String customerReference, String emailAddress, String accountNumber) {
        EmailSetting emailSetting = emailSettingService.getEmailSetting(EmailType.RENEWAL.getType());

        String subject = "It's Renewal Time!";

        String emailTemplate = "";

        if ("SIMPLE".equals(emailSetting.getEmailFormat())) {
            emailTemplate = "renewal-notification-simple.ftl";
        }
        if ("ENHANCED".equals(emailSetting.getEmailFormat())) {
            emailTemplate = "renewal-notification-enhanced.ftl";
        }

        return sendTemplatedEmail(emailTemplate, emailSetting.getEmailType(), getEmailContentMap(customerReference, accountNumber), subject, emailAddress);
    }

    @Override
    public boolean sendGoLiveEmail(String customerReference, String emailAddress, String accountNumber) {
        EmailSetting emailSetting = emailSettingService.getEmailSetting(EmailType.LIVE.getType());

        String subject = "You've Gone Live!";

        String emailTemplate = "";

        if ("SIMPLE".equals(emailSetting.getEmailFormat())) {
            emailTemplate = "live-notification-simple.ftl";
        }
        if ("ENHANCED".equals(emailSetting.getEmailFormat())) {
            emailTemplate = "live-notification-enhanced.ftl";
        }

        return sendTemplatedEmail(emailTemplate, emailSetting.getEmailType(), getEmailContentMap(customerReference, accountNumber), subject, emailAddress);
    }

    @Override
    public boolean sendObjectedEmail(String customerReference, String emailAddress, String accountNumber) {
        EmailSetting emailSetting = emailSettingService.getEmailSetting(EmailType.OBJECTED.getType());

        String subject = "Well that didn't work!";

        String emailTemplate = "";

        if ("SIMPLE".equals(emailSetting.getEmailFormat())) {
            emailTemplate = "objected-notification-simple.ftl";
        }
        if ("ENHANCED".equals(emailSetting.getEmailFormat())) {
            emailTemplate = "objected-notification-enhanced.ftl";
        }

        return sendTemplatedEmail(emailTemplate, emailSetting.getEmailType(), getEmailContentMap(customerReference, accountNumber), subject, emailAddress);
    }

    private boolean sendTemplatedEmail(String emailTemplate,
                                       String emailType,
                                       Map<String, Object> model,
                                       String subject,
                                       String emailAddress) {
        try {

            configuration.setClassForTemplateLoading(this.getClass(), "/templates/email-templates");

            Template template = configuration.getTemplate(emailTemplate);

            if (template != null) {
                String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                String to[] = {emailAddress};
                emailService.sendMimeMessage(to, subject, text);
                return true;
            }
        } catch (Exception e) {
            logger.info("Failed to send {} email.", emailType);
        }
        return false;
    }


    private String getSubject(Customer customer) {
        return customer.getCustomerReference() + " - " + customer.getBusinessName() + ": Tagged Note notification...";
    }

    private Map<String, Object> getEmailContentMap(String customerReference, String accountNumber) {
        Map<String, Object> model = new HashMap<>();
        model.put("customerReference", customerReference);
        model.put("siteReference", accountNumber);
        return model;
    }

    @Override
    public void sendMissingOutEmail() throws Exception {
        //check email settings now
        String subject = "You're Missing Out!";
    }

    @Override
    public void sendWelcomeEmail(EnergyContract energyContract) throws Exception {
        //check email settings now
        String subject = "Welcome to the Family!";
    }


}
