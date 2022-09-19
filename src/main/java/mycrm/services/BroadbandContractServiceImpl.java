package mycrm.services;

import mycrm.models.*;
import mycrm.repositories.BroadbandContractRepository;
import mycrm.repositories.BroadbandContractSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
@Service
public class BroadbandContractServiceImpl implements BroadbandContractService{

    private final LogTypeHistoryService logTypeHistoryService;

    private final BroadbandContractRepository broadbandContractRepository;
    private final BroadbandContractSearchRepository broadbandContractSearchRepository;
    @Autowired
    public BroadbandContractServiceImpl(BroadbandContractSearchRepository broadbandContractSearchRepository,BroadbandContractRepository broadbandContractRepository,LogTypeHistoryService logTypeHistoryService){
        this.broadbandContractRepository = broadbandContractRepository;
        this.logTypeHistoryService = logTypeHistoryService;
        this.broadbandContractSearchRepository = broadbandContractSearchRepository;
    }
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

    @Override
    public List<BroadbandContract> findBroadbandContractByCustomerSite(CustomerSite customerSite) {
        List<BroadbandContract> contracts = this.findByCustomerSite(customerSite);
        contracts.sort(Comparator.comparing(BroadbandContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<BroadbandContract> findByCustomerSite(CustomerSite customerSite) {
        return this.broadbandContractRepository.findLatestBroadbandContractByCustomerSite(customerSite.getId());
    }

    @Override
    public BroadbandSearchResult getBroadbandContracts(BroadbandContractSearch broadbandContractSearch, int pageNumber) {
        return this.broadbandContractSearchRepository.search(broadbandContractSearch, pageNumber);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }


}
