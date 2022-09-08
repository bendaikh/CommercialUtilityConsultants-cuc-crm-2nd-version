package mycrm.services;

import mycrm.models.WaterContract;

public interface WaterContractService {
    WaterContract save(WaterContract waterContract);

    WaterContract findById(Long id);
}
