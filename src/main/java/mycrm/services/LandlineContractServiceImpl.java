package mycrm.services;

import mycrm.models.LandlineContract;
import mycrm.models.LogTypeHistory;
import mycrm.models.MerchantServicesContract;
import mycrm.repositories.LandlineContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LandlineContractServiceImpl implements LandlineContractService{

    @Autowired
    private LogTypeHistoryService logTypeHistoryService;

    @Autowired
    LandlineContractRepository landlineContractRepository;
    @Override
    public LandlineContract save(LandlineContract landlineContract) {
        String logType = landlineContract.getLogType();
        String previousLogType = null;

        if (landlineContract.getId() != null && findById(landlineContract.getId()) != null) {
            previousLogType = findById(landlineContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);
        LandlineContract savedCustomerContract = landlineContractRepository.save(landlineContract);
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
    public LandlineContract findById(Long id) {
        return this.landlineContractRepository.findOne(id);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }

}
