package mycrm.services;

import mycrm.models.BroadbandContract;
import mycrm.models.LandlineContract;

public interface BroadbandContractService {

    BroadbandContract save(BroadbandContract broadbandContract);

    BroadbandContract findById(Long id);

}
