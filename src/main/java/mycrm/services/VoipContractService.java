package mycrm.services;

import mycrm.models.LandlineContract;
import mycrm.models.VoipContract;

public interface VoipContractService {

    VoipContract save(VoipContract voipContract);

    VoipContract findById(Long id);
}
