package mycrm.services;

import mycrm.configuration.EmailProcess;
import mycrm.configuration.EmailType;
import mycrm.models.EnergyContract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Primary
public class LiveEmailServiceImpl implements LiveEmailService {

    private static final Logger logger = LogManager.getLogger();
    private final EmailTemplateService emailTemplateService;
    private final EmailHistoryService emailHistoryService;
    private final CustomerDataService customerDataService;
    private final EmailType emailType = EmailType.LIVE;

    @Autowired
    public LiveEmailServiceImpl(EmailTemplateService emailTemplateService,
                                EmailHistoryService emailHistoryService, CustomerDataService customerDataService) {
        this.emailTemplateService = emailTemplateService;
        this.emailHistoryService = emailHistoryService;
        this.customerDataService = customerDataService;
    }


    @Override
    public void sendLiveEmail(EnergyContract contract, String emailAddress) {

        if (!StringUtils.hasText(emailAddress)) {
            logger.info("No email address for manual live email");
            return;
        }

        String customerReference = contract.getCustomer().getCustomerReference();
        String accountNumber = contract.getAccountNumber();

        logger.info("Sending Go Live email.");
        boolean isEmailSent = sendGoLiveEmailFromTemplateService(emailAddress, customerReference, accountNumber);
        if (isEmailSent) {
            emailHistoryService.saveEmailHistory(emailAddress, contract, EmailProcess.MANUAL.getName(), emailType);
        }
        logger.info("Go Live email sent.");
    }

    // unused
    private void collateCustomerDetailsFromContract(EnergyContract contract, String process) {
        String customerReference = contract.getCustomer().getCustomerReference();
        String accountNumber = contract.getAccountNumber();

        List<String> customerEmails = customerDataService.getCustomerEmail(contract.getCustomer());
        customerEmails.forEach(email -> {

            logger.info("Email address found: {} ", email);
            logger.info("Sending Go Live email.");
            boolean isEmailSent = sendGoLiveEmailFromTemplateService(email, customerReference, accountNumber);
            if (isEmailSent) {
                emailHistoryService.saveEmailHistory(email, contract, process, emailType);
            }
            logger.info("Go Live email sent.");
        });
    }

    private boolean sendGoLiveEmailFromTemplateService(String emailAddress, String customerReference, String accountNumber) {
        try {
            return emailTemplateService.sendGoLiveEmail(customerReference, emailAddress, accountNumber);
        } catch (Exception e) {
            logger.error("Failed to send Go Live email to: {} {}", emailAddress, e.getMessage());
            return false;
        }
    }
}
