package mycrm.services;

import mycrm.models.BroadbandContract;
import mycrm.models.LandlineContract;
import mycrm.models.LogTypeHistory;
import mycrm.repositories.BroadbandContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BroadbandContractServiceImpl implements BroadbandContractService{
    @Autowired
    private LogTypeHistoryService logTypeHistoryService;
    @Autowired
    BroadbandContractRepository broadbandContractRepository;

    @Override
    public BroadbandContract save(BroadbandContract broadbandContract) {
        String logType = broadbandContract.getLogType();
        String previousLogType = null;

        if (broadbandContract.getId() != null && findById(broadbandContract.getId()) != null) {
            previousLogType = findById(broadbandContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);
        BroadbandContract savedCustomerContract = broadbandContractRepository.save(broadbandContract);
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
    public BroadbandContract findById(Long id) {
        return this.broadbandContractRepository.findOne(id);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }


}
