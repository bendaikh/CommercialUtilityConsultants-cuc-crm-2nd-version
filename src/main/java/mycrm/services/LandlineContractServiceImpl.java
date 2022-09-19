package mycrm.services;

import mycrm.models.*;
import mycrm.repositories.LandlineContractRepository;
import mycrm.repositories.LandlineContractSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LandlineContractServiceImpl implements LandlineContractService{

    @Autowired
    private LogTypeHistoryService logTypeHistoryService;

    @Autowired
    LandlineContractRepository landlineContractRepository;
    @Autowired
    LandlineContractSearchRepository landlineContractSearchRepository;
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

    @Override
    public List<LandlineContract> findLandlineContractByCustomerSite(CustomerSite customerSite) {
        List<LandlineContract> contracts = this.findByCustomerSite(customerSite);
        contracts.sort(Comparator.comparing(LandlineContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<LandlineContract> findByCustomerSite(CustomerSite customerSite) {
        return this.landlineContractRepository.findLatestLandlineContractByCustomerSite(customerSite.getId());
    }

    @Override
    public LandlineSearchResult getLandlineContracts(LandlineContractSearch landlineContractSearch, int pageNumber) {
        return this.landlineContractSearchRepository.search(landlineContractSearch, pageNumber);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }

}
