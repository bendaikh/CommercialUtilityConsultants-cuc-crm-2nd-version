package mycrm.search;


import mycrm.functions.UserHelper;
import mycrm.models.*;
import mycrm.services.BroadbandContractService;
import mycrm.services.VoipContractService;
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
public class VoipContractSearchServiceImpl implements VoipContractSearchService{
    private static final Logger logger = LogManager.getLogger();
    private final VoipContractService voipContractService;
    private final UserHelper userHelper;
    @Autowired
    public VoipContractSearchServiceImpl(VoipContractService voipContractService,UserHelper userHelper){
        this.voipContractService = voipContractService;
        this.userHelper = userHelper;
    }
    @Override
    public VoipSearchResult searchVoipContract(VoipContractSearch voipContractSearch, int pageNumber) {
        VoipSearchResult fulltextSearchResult = voipContractService.getVoipContracts(
                adaptVoipContractSearch(voipContractSearch),
                pageNumber);
        List<VoipContract> contracts = fulltextSearchResult.getReturnedContracts();
        return VoipSearchResult
                .builder()
                .returnedContracts(contracts)
                .returnedContractCount(fulltextSearchResult.getReturnedContractCount())
                .totalContractCount(fulltextSearchResult.getTotalContractCount())
                .totalPages(fulltextSearchResult.getTotalPages())
                .build();
    }


    private VoipContractSearch adaptVoipContractSearch(VoipContractSearch voipContractSearch) {
        VoipContractSearch adaptedContractSearch = new VoipContractSearch();
        adaptedContractSearch.setQ(voipContractSearch.getQ());
        adaptedContractSearch.setLogType(voipContractSearch.getLogType());
        adaptedContractSearch.setFromDate(voipContractSearch.getFromDate()); // TODO
        adaptedContractSearch.setToDate(voipContractSearch.getToDate()); // TODO
        adaptedContractSearch.setBroker(voipContractSearch.getBroker());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser()); // TODO
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker()); // TODO
        adaptedContractSearch.setMonthsRemaining(voipContractSearch.getMonthsRemaining());

        // contract search
        if (voipContractSearch.isCustomerContractSearch()) {
            adaptedContractSearch.setLogType(voipContractSearch.getLogType());
            adaptedContractSearch.setCustomerContractSearch(voipContractSearch.isCustomerContractSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // lost renewal search
        if (voipContractSearch.isLostRenewalSearch()) {
            adaptedContractSearch.setLogType(voipContractSearch.getLogType());
            adaptedContractSearch.setLostRenewalSearch(voipContractSearch.isLostRenewalSearch());
            adaptedContractSearch.setLostRenewal(voipContractSearch.isLostRenewal());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // leads
        if (voipContractSearch.isLeadSearch()) {
            adaptedContractSearch.setLogType("POTENTIAL");
            adaptedContractSearch.setLeadSearch(voipContractSearch.isLeadSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        //renewals
        if (voipContractSearch.isRenewalSearch()) {
            adaptedContractSearch.setLogType("LIVE");
            adaptedContractSearch.setRenewalSearch(voipContractSearch.isRenewalSearch());
            adaptedContractSearch.setMonthRemainingDate(calculateDateByMonthRemaining(voipContractSearch.getMonthsRemaining()));
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }
        // callbacks
        if (voipContractSearch.isCallbackSearch()) {
            adaptedContractSearch.setLogType("CALLBACK");
            adaptedContractSearch.setCallbackSearch(voipContractSearch.isCallbackSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(false);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // my customer search
        if (voipContractSearch.isMyCustomerSearch()) {
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
