package mycrm.search;


import mycrm.functions.UserHelper;
import mycrm.models.*;
import mycrm.services.SolarContractService;
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
public class SolarContractSearchServiceImpl implements SolarContractSearchService {
    private static final Logger logger = LogManager.getLogger();
    private final SolarContractService solarContractService;
    private final UserHelper userHelper;
    @Autowired
    public SolarContractSearchServiceImpl(SolarContractService solarContractService,UserHelper userHelper){
        this.solarContractService = solarContractService;
        this.userHelper = userHelper;
    }
    @Override
    public SolarSearchResult searchSolarContract(SolarContractSearch solarContractSearch, int pageNumber) {
        SolarSearchResult fulltextSearchResult = solarContractService.getSolarContracts(
                adaptSolarContractSearch(solarContractSearch),
                pageNumber);
        List<SolarContract> contracts = fulltextSearchResult.getReturnedContracts();
        return SolarSearchResult
                .builder()
                .returnedContracts(contracts)
                .returnedContractCount(fulltextSearchResult.getReturnedContractCount())
                .totalContractCount(fulltextSearchResult.getTotalContractCount())
                .totalPages(fulltextSearchResult.getTotalPages())
                .build();
    }


    private SolarContractSearch adaptSolarContractSearch(SolarContractSearch solarContractSearch) {
        SolarContractSearch adaptedContractSearch = new SolarContractSearch();
        adaptedContractSearch.setQ(solarContractSearch.getQ());
        adaptedContractSearch.setLogType(solarContractSearch.getLogType());
        adaptedContractSearch.setFromDate(solarContractSearch.getFromDate()); // TODO
        adaptedContractSearch.setToDate(solarContractSearch.getToDate()); // TODO
        adaptedContractSearch.setBroker(solarContractSearch.getBroker());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser()); // TODO
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker()); // TODO
        adaptedContractSearch.setMonthsRemaining(solarContractSearch.getMonthsRemaining());

        // contract search
        if (solarContractSearch.isCustomerContractSearch()) {
            adaptedContractSearch.setLogType(solarContractSearch.getLogType());
            adaptedContractSearch.setCustomerContractSearch(solarContractSearch.isCustomerContractSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // lost renewal search
        if (solarContractSearch.isLostRenewalSearch()) {
            adaptedContractSearch.setLogType(solarContractSearch.getLogType());
            adaptedContractSearch.setLostRenewalSearch(solarContractSearch.isLostRenewalSearch());
            adaptedContractSearch.setLostRenewal(solarContractSearch.isLostRenewal());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // leads
        if (solarContractSearch.isLeadSearch()) {
            adaptedContractSearch.setLogType("POTENTIAL");
            adaptedContractSearch.setLeadSearch(solarContractSearch.isLeadSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        //renewals
        if (solarContractSearch.isRenewalSearch()) {
            adaptedContractSearch.setLogType("LIVE");
            adaptedContractSearch.setRenewalSearch(solarContractSearch.isRenewalSearch());
            adaptedContractSearch.setMonthRemainingDate(calculateDateByMonthRemaining(solarContractSearch.getMonthsRemaining()));
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }
        // callbacks
        if (solarContractSearch.isCallbackSearch()) {
            adaptedContractSearch.setLogType("CALLBACK");
            adaptedContractSearch.setCallbackSearch(solarContractSearch.isCallbackSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(false);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // my customer search
        if (solarContractSearch.isMyCustomerSearch()) {
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
