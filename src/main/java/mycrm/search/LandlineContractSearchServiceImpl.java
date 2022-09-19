package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.*;
import mycrm.services.LandlineContractService;
import mycrm.services.MerchantServicesService;
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
public class LandlineContractSearchServiceImpl implements LandlineContractSearchService{

    private static final Logger logger = LogManager.getLogger();
    private final LandlineContractService landlineContractService;
    private final UserHelper userHelper;
    @Autowired
    public LandlineContractSearchServiceImpl(LandlineContractService landlineContractService,UserHelper userHelper){
        this.landlineContractService = landlineContractService;
        this.userHelper = userHelper;
    }
    @Override
    public LandlineSearchResult searchLandlinesContract(LandlineContractSearch landlineContractSearch, int pageNumber) {
        LandlineSearchResult fulltextSearchResult = landlineContractService.getLandlineContracts(
                adaptMerchantContractSearch(landlineContractSearch),
                pageNumber);
        List<LandlineContract> contracts = fulltextSearchResult.getReturnedContracts();
        return LandlineSearchResult
                .builder()
                .returnedContracts(contracts)
                .returnedContractCount(fulltextSearchResult.getReturnedContractCount())
                .totalContractCount(fulltextSearchResult.getTotalContractCount())
                .totalPages(fulltextSearchResult.getTotalPages())
                .build();
    }


    private LandlineContractSearch adaptMerchantContractSearch(LandlineContractSearch landlineContractSearch) {
        LandlineContractSearch adaptedContractSearch = new LandlineContractSearch();
        adaptedContractSearch.setQ(landlineContractSearch.getQ());
        adaptedContractSearch.setLogType(landlineContractSearch.getLogType());
        adaptedContractSearch.setFromDate(landlineContractSearch.getFromDate()); // TODO
        adaptedContractSearch.setToDate(landlineContractSearch.getToDate()); // TODO
        adaptedContractSearch.setBroker(landlineContractSearch.getBroker());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser()); // TODO
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker()); // TODO
        adaptedContractSearch.setMonthsRemaining(landlineContractSearch.getMonthsRemaining());

        // contract search
        if (landlineContractSearch.isCustomerContractSearch()) {
            adaptedContractSearch.setLogType(landlineContractSearch.getLogType());
            adaptedContractSearch.setCustomerContractSearch(landlineContractSearch.isCustomerContractSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // lost renewal search
        if (landlineContractSearch.isLostRenewalSearch()) {
            adaptedContractSearch.setLogType(landlineContractSearch.getLogType());
            adaptedContractSearch.setLostRenewalSearch(landlineContractSearch.isLostRenewalSearch());
            adaptedContractSearch.setLostRenewal(landlineContractSearch.isLostRenewal());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // leads
        if (landlineContractSearch.isLeadSearch()) {
            adaptedContractSearch.setLogType("POTENTIAL");
            adaptedContractSearch.setLeadSearch(landlineContractSearch.isLeadSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        //renewals
        if (landlineContractSearch.isRenewalSearch()) {
            adaptedContractSearch.setLogType("LIVE");
            adaptedContractSearch.setRenewalSearch(landlineContractSearch.isRenewalSearch());
            adaptedContractSearch.setMonthRemainingDate(calculateDateByMonthRemaining(landlineContractSearch.getMonthsRemaining()));
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }
        // callbacks
        if (landlineContractSearch.isCallbackSearch()) {
            adaptedContractSearch.setLogType("CALLBACK");
            adaptedContractSearch.setCallbackSearch(landlineContractSearch.isCallbackSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(false);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // my customer search
        if (landlineContractSearch.isMyCustomerSearch()) {
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
