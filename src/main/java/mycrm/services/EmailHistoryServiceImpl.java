package mycrm.services;

import mycrm.configuration.EmailType;
import mycrm.models.Customer;
import mycrm.models.ElecCustomerContract;
import mycrm.models.EmailHistory;
import mycrm.models.EnergyContract;
import mycrm.models.GasCustomerContract;
import mycrm.repositories.EmailHistoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class EmailHistoryServiceImpl implements EmailHistoryService {

    private static final Logger logger = LogManager.getLogger();

    private final EmailHistoryRepository emailHistoryRepository;

    @Autowired
    public EmailHistoryServiceImpl(EmailHistoryRepository emailHistoryRepository) {
        this.emailHistoryRepository = emailHistoryRepository;
    }

    @Override
    public void saveEmailHistory(String emailAddress, EnergyContract contract, String process, EmailType emailType) {
        logger.info("Logging history of renewal email.");

        EmailHistory emailHistory = new EmailHistory();
        emailHistory.setEmailAddress(emailAddress);
        emailHistory.setEmailType(emailType.getType());
        emailHistory.setDateCreated(LocalDateTime.now());
        emailHistory.setProcess(process);
        emailHistory.setCustomer(contract.getCustomer());

        if (contract instanceof GasCustomerContract) {
            emailHistory.setGasCustomerContract((GasCustomerContract) contract);
        }
        if (contract instanceof ElecCustomerContract) {
            emailHistory.setElecCustomerContract((ElecCustomerContract) contract);
        }

        save(emailHistory);
    }

    @Override
    public List<EmailHistory> checkRenewalEmailSentAutomaticallyToCustomerToday(String emailAddress, String emailType, EnergyContract contract) {
        LocalDate todaysDate = LocalDate.now();
        return this.emailHistoryRepository.findByCustomerAndDateCreated(contract.getCustomer().getId().toString(), todaysDate.toString());
    }

    @Override
    public List<EmailHistory> checkRenewalEmailSentAutomatically(String emailAddress, String emailType, EnergyContract contract) {
        if (contract instanceof GasCustomerContract) {
            return this.emailHistoryRepository.checkGasRenewalEmailSentAutomatically(emailAddress, emailType, (GasCustomerContract) contract);
        }
        if (contract instanceof ElecCustomerContract) {
            return this.emailHistoryRepository.checkElectricRenewalEmailSentAutomatically(emailAddress, emailType, (ElecCustomerContract) contract);
        }
        return new ArrayList<>();
    }

    @Override
    public List<EmailHistory> findByCustomer(Customer customer) {
        return this.emailHistoryRepository.findByCustomer(customer);
    }

    private void save(EmailHistory emailHistory) {
        this.emailHistoryRepository.save(emailHistory);
    }
}
