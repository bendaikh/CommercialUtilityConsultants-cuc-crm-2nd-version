package mycrm.services;

import mycrm.models.*;
import mycrm.repositories.BroadbandContractRepository;
import mycrm.repositories.BroadbandContractSearchRepository;
import mycrm.repositories.SolarContractRepository;
import mycrm.repositories.SolarContractSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SolarContractServiceImpl implements SolarContractService {
    private final LogTypeHistoryService logTypeHistoryService;

    private final SolarContractRepository solarContractRepository;
    private final SolarContractSearchRepository solarContractSearchRepository;
    @Autowired
    public SolarContractServiceImpl(SolarContractSearchRepository solarContractSearchRepository,SolarContractRepository solarContractRepository,LogTypeHistoryService logTypeHistoryService){
        this.solarContractRepository = solarContractRepository;
        this.logTypeHistoryService = logTypeHistoryService;
        this.solarContractSearchRepository = solarContractSearchRepository;
    }
    @Override
    public SolarContract save(SolarContract solarContract) {
        String logType = solarContract.getLogType();
        String previousLogType = null;

        if (solarContract.getId() != null && findById(solarContract.getId()) != null) {
            previousLogType = findById(solarContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);
        SolarContract savedCustomerContract = solarContractRepository.save(solarContract);
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
    public SolarContract findById(Long id) {
        return this.solarContractRepository.findOne(id);
    }

    @Override
    public List<SolarContract> findSolarContractByCustomerSite(CustomerSite customerSite) {
        List<SolarContract> contracts = this.findByCustomerSite(customerSite);
        contracts.sort(Comparator.comparing(SolarContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<SolarContract> findByCustomerSite(CustomerSite customerSite) {
        return this.solarContractRepository.findLatestSolarContractByCustomerSite(customerSite.getId());
    }

    @Override
    public SolarSearchResult getSolarContracts(SolarContractSearch solarContractSearch, int pageNumber) {
        return this.solarContractSearchRepository.search(solarContractSearch, pageNumber);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }


}
