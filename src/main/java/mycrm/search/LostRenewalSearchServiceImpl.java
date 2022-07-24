package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.Contract;
import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class LostRenewalSearchServiceImpl implements LostRenewalSearchService {

    private static final Logger logger = LogManager.getLogger();

    private final EnergyContractSearchService energyContractSearchService;
    private final UserHelper userHelper;

    @Autowired
    public LostRenewalSearchServiceImpl(EnergyContractSearchService energyContractSearchService,
                                        UserHelper userHelper) {
        this.energyContractSearchService = energyContractSearchService;
        this.userHelper = userHelper;
    }

    @Override
    public ContractSearchResult searchCustomerContract(ContractSearch contractSearch, int pageNumber) {
        logger.info("Starting Lost Renewal search {}", contractSearch.toString());

        ContractSearchResult fulltextSearchResult = energyContractSearchService.getEnergyContracts(adaptContractSearch(contractSearch), pageNumber);

        List<Contract> lostRenewals = fulltextSearchResult.getReturnedContracts();

        ContractSearchResult contractSearchResult = new ContractSearchResult();
        contractSearchResult.setReturnedContracts(lostRenewals);
        contractSearchResult.setReturnedContractCount(fulltextSearchResult.getReturnedContractCount());
        contractSearchResult.setTotalContractCount(fulltextSearchResult.getTotalContractCount());
        contractSearchResult.setTotalPages(fulltextSearchResult.getTotalPages());

        return contractSearchResult;
    }

    private ContractSearch adaptContractSearch(ContractSearch contractSearch) {

        ContractSearch adaptedContractSearch = new ContractSearch();
        adaptedContractSearch.setQ(contractSearch.getQ());
        adaptedContractSearch.setLogType(contractSearch.getLogType());
        adaptedContractSearch.setSupplyType(contractSearch.getSupplyType());
        adaptedContractSearch.setSupplier(contractSearch.getSupplier());
        adaptedContractSearch.setFromDate(contractSearch.getFromDate());
        adaptedContractSearch.setToDate(contractSearch.getToDate());
        adaptedContractSearch.setBroker(contractSearch.getBroker());
        adaptedContractSearch.setMonthsRemaining(contractSearch.getMonthsRemaining());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser());

        adaptedContractSearch.setLostRenewalSearch(true);
        adaptedContractSearch.setLostRenewal(true);
        adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
        adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker());
        adaptedContractSearch.setDenyExternalBrokerAccess(false);
        adaptedContractSearch.setCampaign(contractSearch.getCampaign());

        return adaptedContractSearch;
    }
}
