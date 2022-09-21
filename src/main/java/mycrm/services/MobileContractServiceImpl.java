package mycrm.services;


import mycrm.models.*;
import mycrm.repositories.MobileContractRepository;
import mycrm.repositories.MobileContractSearchRepository;
import mycrm.repositories.SolarContractRepository;
import mycrm.repositories.SolarContractSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MobileContractServiceImpl implements MobileContractService{
    private final LogTypeHistoryService logTypeHistoryService;

    private final MobileContractRepository mobileContractRepository;

    private final MobileContractSearchRepository mobileContractSearchRepository;

    @Autowired
    public MobileContractServiceImpl(MobileContractSearchRepository mobileContractSearchRepository,MobileContractRepository mobileContractRepository,LogTypeHistoryService logTypeHistoryService){
        this.mobileContractRepository = mobileContractRepository;
        this.logTypeHistoryService = logTypeHistoryService;
        this.mobileContractSearchRepository = mobileContractSearchRepository;
    }
    @Override
    public MobileContract save(MobileContract mobileContract) {
        String logType = mobileContract.getLogType();
        String previousLogType = null;

        if (mobileContract.getId() != null && findById(mobileContract.getId()) != null) {
            previousLogType = findById(mobileContract.getId()).getLogType();
        }

        boolean logTypeChanged = hasLogTypeChanged(logType, previousLogType);
        MobileContract savedCustomerContract = mobileContractRepository.save(mobileContract);
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
    public MobileContract findById(Long id) {
        return this.mobileContractRepository.findOne(id);
    }

    @Override
    public List<MobileContract> findMobileContractByCustomerSite(CustomerSite customerSite) {
        List<MobileContract> contracts = this.findByCustomerSite(customerSite);
        contracts.sort(Comparator.comparing(MobileContract::isCurrent).reversed());
        return contracts;
    }

    @Override
    public List<MobileContract> findByCustomerSite(CustomerSite customerSite) {
        return this.mobileContractRepository.findLatestMobileContractByCustomerSite(customerSite.getId());
    }

    @Override
    public MobileSearchResult getMobileContracts(MobileContractSearch mobileContractSearch, int pageNumber) {
        return this.mobileContractSearchRepository.search(mobileContractSearch, pageNumber);
    }

    private boolean hasLogTypeChanged(String logType, String previousLogType) {
        return logType != null && !logType.equals(previousLogType);
    }

    private void saveLogTypeHistory(LogTypeHistory logTypeHistory) {
        this.logTypeHistoryService.save(logTypeHistory);
    }




}
