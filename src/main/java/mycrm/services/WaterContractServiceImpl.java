package mycrm.services;

import mycrm.models.*;
import mycrm.repositories.VoipContractSearchRepository;
import mycrm.repositories.WaterContractRepository;
import mycrm.repositories.WaterContractSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class WaterContractServiceImpl implements WaterContractService{

    @Autowired
    private LogTypeHistoryService logTypeHistoryService;
    @Autowired
    private WaterContractRepository waterContractRepository;
    @Autowired
    private WaterContractSearchRepository waterContractSearchRepository;

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

    @Override
    public List<WaterContract> findWaterContractByCustomerSite(CustomerSite customerSite) {
        List<WaterContract> contracts = this.findByCustomerSite(customerSite);
        contracts.sort(Comparator.comparing(WaterContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<WaterContract> findByCustomerSite(CustomerSite customerSite) {
        return this.waterContractRepository.findLatestWaterContractByCustomerSite(customerSite.getId());
    }

    @Override
    public WaterSearchResult getWaterContracts(WaterContractSearch waterContractSearch, int pageNumber) {
        return this.waterContractSearchRepository.search(waterContractSearch, pageNumber);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }
}
