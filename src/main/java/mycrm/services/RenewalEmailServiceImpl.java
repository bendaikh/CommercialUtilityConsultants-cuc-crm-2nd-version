package mycrm.services;

import mycrm.configuration.EmailProcess;
import mycrm.configuration.EmailType;
import mycrm.models.ElecCustomerContract;
import mycrm.models.EmailHistory;
import mycrm.models.EnergyContract;
import mycrm.models.GasCustomerContract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Primary
public class RenewalEmailServiceImpl implements RenewalEmailService {

    private static final Logger logger = LogManager.getLogger();
    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final EmailTemplateService emailTemplateService;
    private final EmailHistoryService emailHistoryService;
    private final CustomerDataService customerDataService;
    private final EmailType emailType = EmailType.RENEWAL;

    @Autowired
    public RenewalEmailServiceImpl(GasContractService gasContractService,
                                   ElecContractService elecContractService,
                                   EmailTemplateService emailTemplateService,
                                   EmailHistoryService emailHistoryService,
                                   CustomerDataService customerDataService) {
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.emailTemplateService = emailTemplateService;
        this.emailHistoryService = emailHistoryService;
        this.customerDataService = customerDataService;
    }

    @Override
    public void sendDailyRenewalEmails(String process) {
        List<GasCustomerContract> gasRenewalsList = gasContractService.findAllRenewableContracts();
        List<ElecCustomerContract> elecRenewalsList = elecContractService.findAllRenewableContracts();

        gasRenewalsList.forEach(gasCustomerContract -> {
            collateCustomerDetailsFromContract(gasCustomerContract, process);
        });

        elecRenewalsList.forEach(elecCustomerContract -> {
            collateCustomerDetailsFromContract(elecCustomerContract, process);
        });
    }

    @Override
    public void sendManualRenewalEmail(EnergyContract contract, String emailAddress) {

        if (!StringUtils.hasText(emailAddress)) {
            logger.info("No email address for manual renewal email");
            return;
        }

        String customerReference = contract.getCustomer().getCustomerReference();
        String accountNumber = contract.getAccountNumber();

        logger.info("Sending renewal email.");
        boolean isEmailSent = sendRenewalEmailFromTemplateService(emailAddress, customerReference, accountNumber);
        if (isEmailSent) {
            emailHistoryService.saveEmailHistory(emailAddress, contract, EmailProcess.MANUAL.getName(), emailType);
        }
        logger.info("Renewal email sent.");
    }

    private void collateCustomerDetailsFromContract(EnergyContract contract, String process) {
        String customerReference = contract.getCustomer().getCustomerReference();
        String accountNumber = contract.getAccountNumber();

        // send to all emails for this customer
        // List<String> customerEmails = customerDataService.getAllCustomerEmails(contract.getCustomer());

        List<String> customerEmails = customerDataService.getCustomerEmail(contract.getCustomer());
        customerEmails.forEach(email -> {

            // check if an automated email has already been sent - this allows for MANUAL emails to be sent at request
            if ("AUTOMATED".equals(process)) {
                if (hasEmailBeenSentAutomatically(contract, email)) {
                    logger.info("Email already sent");
                    return;
                }
            }

            logger.info("Sending renewal email.");
            boolean isEmailSent = sendRenewalEmailFromTemplateService(email, customerReference, accountNumber);
            if (isEmailSent) {
                emailHistoryService.saveEmailHistory(email, contract, process, emailType);
            }
            logger.info("Renewal email sent.");
        });
    }

    private boolean hasEmailBeenSentAutomatically(EnergyContract contract, String email) {
        boolean emailAlreadySent = false;

//        List<EmailHistory> emailHistory = emailHistoryService.checkRenewalEmailSentAutomatically(email, emailType.getType(), contract);
        List<EmailHistory> emailHistory = emailHistoryService.checkRenewalEmailSentAutomaticallyToCustomerToday(email, emailType.getType(), contract);
        if (!emailHistory.isEmpty()) {
            emailAlreadySent = true;
        }

        return emailAlreadySent;
    }

    private boolean sendRenewalEmailFromTemplateService(String emailAddress, String customerReference, String accountNumber) {
        try {
            return emailTemplateService.sendRenewalEmail(customerReference, emailAddress, accountNumber);
        } catch (Exception e) {
            logger.error("Failed to send renewal email to: {} {}", emailAddress, e.getMessage());
            return false;
        }
    }
}
