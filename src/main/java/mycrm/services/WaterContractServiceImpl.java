package mycrm.services;

import mycrm.models.LandlineContract;
import mycrm.models.LogTypeHistory;
import mycrm.models.WaterContract;
import mycrm.repositories.WaterContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaterContractServiceImpl implements WaterContractService{

    @Autowired
    private LogTypeHistoryService logTypeHistoryService;
    @Autowired
    private WaterContractRepository waterContractRepository;

    @Override
    public WaterContract save(WaterContract waterContract) {
        String logType = waterContract.getLogType();
        String previousLogType = null;

        if (waterContract.getId() != null && findById(waterContract.getId()) != null) {
            previousLogType = findById(waterContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);
        WaterContract savedCustomerContract = waterContractRepository.save(waterContract);
        if (logTypeChanged) {
            saveLogTypeHistory(LogTypeHistory
                    .builder()
                    .contractId(savedCustomerContract.getId())
                    .supplyType(savedCustomerContract.getSupplyType())
                    .previousLogType(previousLogType)
                    .logType(logType)
                    .build());
        }
        return savedCustomerContract;
    }

    @Override
    public WaterContract findById(Long id) {
        return this.waterContractRepository.findOne(id);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }
}
