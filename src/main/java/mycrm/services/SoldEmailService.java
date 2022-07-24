package mycrm.services;

import mycrm.models.EnergyContract;

public interface SoldEmailService {
    void sendSoldEmail(EnergyContract contract, String emailAddress);
}
