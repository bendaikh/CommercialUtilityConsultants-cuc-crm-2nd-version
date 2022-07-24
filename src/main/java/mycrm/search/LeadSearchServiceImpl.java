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
public class LeadSearchServiceImpl implements LeadSearchService {

    private static Logger logger = LogManager.getLogger();

    private final UserHelper userHelper;
    private final EnergyContractSearchService energyContractSearchService;

    @Autowired
    public LeadSearchServiceImpl(UserHelper userHelper,
                                 EnergyContractSearchService energyContractSearchService) {
        this.userHelper = userHelper;
        this.energyContractSearchService = energyContractSearchService;
    }

    @Override
    public ContractSearchResult searchCustomerContract(ContractSearch contractSearch, int pageNumber) {

        logger.info("Starting Lead search {} ", contractSearch.toString());

        ContractSearchResult fulltextSearchResult = energyContractSearchService.getEnergyContracts(adaptContractSearch(contractSearch), pageNumber);

        List<Contract> leads = fulltextSearchResult.getReturnedContracts();

        //filterByUser(leads);
//        orderBy(leads);

        ContractSearchResult contractSearchResult = new ContractSearchResult();
        contractSearchResult.setReturnedContracts(leads);
        contractSearchResult.setReturnedContractCount(fulltextSearchResult.getReturnedContractCount());
        contractSearchResult.setTotalContractCount(fulltextSearchResult.getTotalContractCount());
        contractSearchResult.setTotalPages(fulltextSearchResult.getTotalPages());

        return contractSearchResult;
    }

    private ContractSearch adaptContractSearch(ContractSearch contractSearch) {

        ContractSearch adaptedContractSearch = new ContractSearch();
        adaptedContractSearch.setQ(contractSearch.getQ());
        adaptedContractSearch.setLogType("POTENTIAL"); //leads search
        adaptedContractSearch.setSupplyType(contractSearch.getSupplyType());
        adaptedContractSearch.setSupplier(contractSearch.getSupplier());
        adaptedContractSearch.setFromDate(contractSearch.getFromDate());
        adaptedContractSearch.setToDate(contractSearch.getToDate());
        adaptedContractSearch.setBroker(contractSearch.getBroker());
        adaptedContractSearch.setMonthsRemaining(contractSearch.getMonthsRemaining());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser());

        adaptedContractSearch.setLeadSearch(true);
        adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
        adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker());
        adaptedContractSearch.setDenyExternalBrokerAccess(false);
        adaptedContractSearch.setCampaign(contractSearch.getCampaign());

        return adaptedContractSearch;
    }

//    private void orderBy(List<Contract> customerContract) {
//        customerContract.sort(Comparator.comparing(Contract::getEndDate, Comparator.nullsLast(Comparator.naturalOrder())));
//    }

/*
    private void filterByUser(List<Contract> contracts) {
        User user = getLoggedInUser();

        if (user.isBroker()) {
            Iterator<Contract> iterator = contracts.iterator();

            while (iterator.hasNext()) {
                Contract contract = iterator.next();
                if (contract.getBroker() != user.getBroker() && contract.getBroker().getId() != defaultBrokerId) {
                    iterator.remove();
                }
            }
        }

        if (user.isExternalBroker()) {
            Iterator<Contract> iterator = contracts.iterator();

            while (iterator.hasNext()) {
                Contract contract = iterator.next();
                if (contract.getBroker() != user.getBroker()) {
                    iterator.remove();
                }
            }
        }
    } */
}
