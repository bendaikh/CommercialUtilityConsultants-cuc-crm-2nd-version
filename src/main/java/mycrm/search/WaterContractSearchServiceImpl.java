package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.*;
import mycrm.services.VoipContractService;
import mycrm.services.WaterContractService;
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
public class WaterContractSearchServiceImpl implements WaterContractSearchService{
    private static final Logger logger = LogManager.getLogger();
    private final WaterContractService waterContractService;
    private final UserHelper userHelper;
    @Autowired
    public WaterContractSearchServiceImpl(WaterContractService waterContractService,UserHelper userHelper){
        this.waterContractService = waterContractService;
        this.userHelper = userHelper;
    }
    @Override
    public WaterSearchResult searchWaterContract(WaterContractSearch waterContractSearch, int pageNumber) {
        WaterSearchResult fulltextSearchResult = waterContractService.getWaterContracts(
                adaptWaterContractSearch(waterContractSearch),
                pageNumber);
        List<WaterContract> contracts = fulltextSearchResult.getReturnedContracts();
        return WaterSearchResult
                .builder()
                .returnedContracts(contracts)
                .returnedContractCount(fulltextSearchResult.getReturnedContractCount())
                .totalContractCount(fulltextSearchResult.getTotalContractCount())
                .totalPages(fulltextSearchResult.getTotalPages())
                .build();
    }


    private WaterContractSearch adaptWaterContractSearch(WaterContractSearch waterContractSearch) {
        WaterContractSearch adaptedContractSearch = new WaterContractSearch();
        adaptedContractSearch.setQ(waterContractSearch.getQ());
        adaptedContractSearch.setLogType(waterContractSearch.getLogType());
        adaptedContractSearch.setFromDate(waterContractSearch.getFromDate()); // TODO
        adaptedContractSearch.setToDate(waterContractSearch.getToDate()); // TODO
        adaptedContractSearch.setBroker(waterContractSearch.getBroker());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser()); // TODO
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker()); // TODO
        adaptedContractSearch.setMonthsRemaining(waterContractSearch.getMonthsRemaining());

        // contract search
        if (waterContractSearch.isCustomerContractSearch()) {
            adaptedContractSearch.setLogType(waterContractSearch.getLogType());
            adaptedContractSearch.setCustomerContractSearch(waterContractSearch.isCustomerContractSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // lost renewal search
        if (waterContractSearch.isLostRenewalSearch()) {
            adaptedContractSearch.setLogType(waterContractSearch.getLogType());
            adaptedContractSearch.setLostRenewalSearch(waterContractSearch.isLostRenewalSearch());
            adaptedContractSearch.setLostRenewal(waterContractSearch.isLostRenewal());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // leads
        if (waterContractSearch.isLeadSearch()) {
            adaptedContractSearch.setLogType("POTENTIAL");
            adaptedContractSearch.setLeadSearch(waterContractSearch.isLeadSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        //renewals
        if (waterContractSearch.isRenewalSearch()) {
            adaptedContractSearch.setLogType("LIVE");
            adaptedContractSearch.setRenewalSearch(waterContractSearch.isRenewalSearch());
            adaptedContractSearch.setMonthRemainingDate(calculateDateByMonthRemaining(waterContractSearch.getMonthsRemaining()));
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }
        // callbacks
        if (waterContractSearch.isCallbackSearch()) {
            adaptedContractSearch.setLogType("CALLBACK");
            adaptedContractSearch.setCallbackSearch(waterContractSearch.isCallbackSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(false);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // my customer search
        if (waterContractSearch.isMyCustomerSearch()) {
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
