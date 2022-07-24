package mycrm.services;

import mycrm.configuration.EmailType;
import mycrm.models.Customer;
import mycrm.models.EmailHistory;
import mycrm.models.EnergyContract;

import java.util.List;

public interface EmailHistoryService {
    List<EmailHistory> checkRenewalEmailSentAutomatically(String emailAddress, String emailType, EnergyContract contract);

    List<EmailHistory> findByCustomer(Customer customer);

    void saveEmailHistory(String emailAddress, EnergyContract contract, String process, EmailType emailType);

    List<EmailHistory> checkRenewalEmailSentAutomaticallyToCustomerToday(String email, String type, EnergyContract contract);
}
