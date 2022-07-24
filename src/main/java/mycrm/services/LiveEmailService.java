package mycrm.services;

import mycrm.models.EnergyContract;

public interface LiveEmailService {
    void sendLiveEmail(EnergyContract contract, String emailAddress);
}
