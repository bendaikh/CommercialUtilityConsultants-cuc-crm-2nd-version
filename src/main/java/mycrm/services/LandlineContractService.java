package mycrm.services;

import mycrm.models.LandlineContract;
import mycrm.models.MerchantServicesContract;

public interface LandlineContractService {
    LandlineContract save(LandlineContract landlineContract);

    LandlineContract findById(Long id);
}
