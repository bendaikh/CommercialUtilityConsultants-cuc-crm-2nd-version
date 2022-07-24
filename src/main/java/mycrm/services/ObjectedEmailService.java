package mycrm.services;

import mycrm.models.EnergyContract;

public interface ObjectedEmailService {
    void sendObjectedEmail(EnergyContract contract, String emailAddress);
}
