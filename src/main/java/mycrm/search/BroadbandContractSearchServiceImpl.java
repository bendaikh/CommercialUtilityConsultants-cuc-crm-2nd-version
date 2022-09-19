package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.*;
import mycrm.services.BroadbandContractService;
import mycrm.services.LandlineContractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Primary
public class BroadbandContractSearchServiceImpl implements BroadbandContractSearchService {
    private static final Logger logger = LogManager.getLogger();
    private final BroadbandContractService broadbandContractService;
    private final UserHelper userHelper;
    @Autowired
    public BroadbandContractSearchServiceImpl(BroadbandContractService broadbandContractService,UserHelper userHelper){
        this.broadbandContractService = broadbandContractService;
        this.userHelper = userHelper;
    }
    @Override
    public BroadbandSearchResult searchBroadbandContract(BroadbandContractSearch broadbandContractSearch, int pageNumber) {
        BroadbandSearchResult fulltextSearchResult = broadbandContractService.getBroadbandContracts(
                adaptBroadbandContractSearch(broadbandContractSearch),
                pageNumber);
        List<BroadbandContract> contracts = fulltextSearchResult.getReturnedContracts();
        return BroadbandSearchResult
                .builder()
                .returnedContracts(contracts)
                .returnedContractCount(fulltextSearchResult.getReturnedContractCount())
                .totalContractCount(fulltextSearchResult.getTotalContractCount())
                .totalPages(fulltextSearchResult.getTotalPages())
                .build();
    }


    private BroadbandContractSearch adaptBroadbandContractSearch(BroadbandContractSearch broadbandContractSearch) {
        BroadbandContractSearch adaptedContractSearch = new BroadbandContractSearch();
        adaptedContractSearch.setQ(broadbandContractSearch.getQ());
        adaptedContractSearch.setLogType(broadbandContractSearch.getLogType());
        adaptedContractSearch.setFromDate(broadbandContractSearch.getFromDate()); // TODO
        adaptedContractSearch.setToDate(broadbandContractSearch.getToDate()); // TODO
        adaptedContractSearch.setBroker(broadbandContractSearch.getBroker());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser()); // TODO
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker()); // TODO
        adaptedContractSearch.setMonthsRemaining(broadbandContractSearch.getMonthsRemaining());

        // contract search
        if (broadbandContractSearch.isCustomerContractSearch()) {
            adaptedContractSearch.setLogType(broadbandContractSearch.getLogType());
            adaptedContractSearch.setCustomerContractSearch(broadbandContractSearch.isCustomerContractSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // lost renewal search
        if (broadbandContractSearch.isLostRenewalSearch()) {
            adaptedContractSearch.setLogType(broadbandContractSearch.getLogType());
            adaptedContractSearch.setLostRenewalSearch(broadbandContractSearch.isLostRenewalSearch());
            adaptedContractSearch.setLostRenewal(broadbandContractSearch.isLostRenewal());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // leads
        if (broadbandContractSearch.isLeadSearch()) {
            adaptedContractSearch.setLogType("POTENTIAL");
            adaptedContractSearch.setLeadSearch(broadbandContractSearch.isLeadSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        //renewals
        if (broadbandContractSearch.isRenewalSearch()) {
            adaptedContractSearch.setLogType("LIVE");
            adaptedContractSearch.setRenewalSearch(broadbandContractSearch.isRenewalSearch());
            adaptedContractSearch.setMonthRemainingDate(calculateDateByMonthRemaining(broadbandContractSearch.getMonthsRemaining()));
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }
        // callbacks
        if (broadbandContractSearch.isCallbackSearch()) {
            adaptedContractSearch.setLogType("CALLBACK");
            adaptedContractSearch.setCallbackSearch(broadbandContractSearch.isCallbackSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(false);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // my customer search
        if (broadbandContractSearch.isMyCustomerSearch()) {
            adaptedContractSearch.setShowInternalBrokerOwnContractsOnly(false);
            adaptedContractSearch.setShowExternalBrokerOwnContractsOnly(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        return adaptedContractSearch;
    }
    private String calculateDateByMonthRemaining(int monthsRemaining) {
        if (monthsRemaining > 0) {
            LocalDate dateWithMonthAdded = LocalDate.now().plusMonths((monthsRemaining + 1));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String formattedDate = dateWithMonthAdded.format(formatter);

            logger.info("Date calculated by {} months remaining to be {} ", monthsRemaining, formattedDate);

            return formattedDate;
        }
        return null;
    }
}
