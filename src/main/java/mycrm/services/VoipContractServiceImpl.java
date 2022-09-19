package mycrm.services;


import mycrm.models.*;
import mycrm.repositories.VoipContractRepository;
import mycrm.repositories.VoipContractSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class VoipContractServiceImpl implements VoipContractService{

    @Autowired
    private VoipContractRepository voipContractRepository;
    @Autowired
    private LogTypeHistoryService logTypeHistoryService;
    @Autowired
    private VoipContractSearchRepository voipContractSearchRepository;

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

    @Override
    public List<VoipContract> findVoipContractByCustomerSite(CustomerSite customerSite) {
        List<VoipContract> contracts = this.findByCustomerSite(customerSite);
        contracts.sort(Comparator.comparing(VoipContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<VoipContract> findByCustomerSite(CustomerSite customerSite) {
        return this.voipContractRepository.findLatestVoipContractByCustomerSite(customerSite.getId());
    }

    @Override
    public VoipSearchResult getVoipContracts(VoipContractSearch voipContractSearch, int pageNumber) {
        return this.voipContractSearchRepository.search(voipContractSearch, pageNumber);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }
}
