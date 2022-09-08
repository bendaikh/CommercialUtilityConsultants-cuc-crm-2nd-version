package mycrm.services;


import mycrm.models.LandlineContract;
import mycrm.models.LogTypeHistory;
import mycrm.models.VoipContract;
import mycrm.repositories.VoipContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoipContractServiceImpl implements VoipContractService{

    @Autowired
    private VoipContractRepository voipContractRepository;
    @Autowired
    private LogTypeHistoryService logTypeHistoryService;
    @Override
    public VoipContract save(VoipContract voipContract) {
        String logType = voipContract.getLogType();
        String previousLogType = null;

        if (voipContract.getId() != null && findById(voipContract.getId()) != null) {
            previousLogType = findById(voipContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);
        VoipContract savedCustomerContract = voipContractRepository.save(voipContract);
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
    public VoipContract findById(Long id) {
        return this.voipContractRepository.findOne(id);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }
}
