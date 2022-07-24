package mycrm.services;

import mycrm.models.EnergyContract;

public interface RenewalEmailService {
    void sendDailyRenewalEmails(String process);

    void sendManualRenewalEmail(EnergyContract contract, String emailAddress);
}
