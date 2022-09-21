package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.*;
import mycrm.services.MobileContractService;
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
public class MobileContractSearchServiceImpl implements MobileContractSearchService {
    private static final Logger logger = LogManager.getLogger();
    private final MobileContractService mobileContractService;
    private final UserHelper userHelper;
    @Autowired
    public MobileContractSearchServiceImpl(MobileContractService mobileContractService,UserHelper userHelper){
        this.mobileContractService = mobileContractService;
        this.userHelper = userHelper;
    }
    @Override
    public MobileSearchResult searchMobileContract(MobileContractSearch mobileContractSearch, int pageNumber) {
        MobileSearchResult fulltextSearchResult = mobileContractService.getMobileContracts(
                adaptMobileContractSearch(mobileContractSearch),
                pageNumber);
        List<MobileContract> contracts = fulltextSearchResult.getReturnedContracts();
        return MobileSearchResult
                .builder()
                .returnedContracts(contracts)
                .returnedContractCount(fulltextSearchResult.getReturnedContractCount())
                .totalContractCount(fulltextSearchResult.getTotalContractCount())
                .totalPages(fulltextSearchResult.getTotalPages())
                .build();
    }


    private MobileContractSearch adaptMobileContractSearch(MobileContractSearch mobileContractSearch) {
        MobileContractSearch adaptedContractSearch = new MobileContractSearch();
        adaptedContractSearch.setQ(mobileContractSearch.getQ());
        adaptedContractSearch.setLogType(mobileContractSearch.getLogType());
        adaptedContractSearch.setFromDate(mobileContractSearch.getFromDate()); // TODO
        adaptedContractSearch.setToDate(mobileContractSearch.getToDate()); // TODO
        adaptedContractSearch.setBroker(mobileContractSearch.getBroker());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser()); // TODO
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker()); // TODO
        adaptedContractSearch.setMonthsRemaining(mobileContractSearch.getMonthsRemaining());

        // contract search
        if (mobileContractSearch.isCustomerContractSearch()) {
            adaptedContractSearch.setLogType(mobileContractSearch.getLogType());
            adaptedContractSearch.setCustomerContractSearch(mobileContractSearch.isCustomerContractSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // lost renewal search
        if (mobileContractSearch.isLostRenewalSearch()) {
            adaptedContractSearch.setLogType(mobileContractSearch.getLogType());
            adaptedContractSearch.setLostRenewalSearch(mobileContractSearch.isLostRenewalSearch());
            adaptedContractSearch.setLostRenewal(mobileContractSearch.isLostRenewal());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // leads
        if (mobileContractSearch.isLeadSearch()) {
            adaptedContractSearch.setLogType("POTENTIAL");
            adaptedContractSearch.setLeadSearch(mobileContractSearch.isLeadSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        //renewals
        if (mobileContractSearch.isRenewalSearch()) {
            adaptedContractSearch.setLogType("LIVE");
            adaptedContractSearch.setRenewalSearch(mobileContractSearch.isRenewalSearch());
            adaptedContractSearch.setMonthRemainingDate(calculateDateByMonthRemaining(mobileContractSearch.getMonthsRemaining()));
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }
        // callbacks
        if (mobileContractSearch.isCallbackSearch()) {
            adaptedContractSearch.setLogType("CALLBACK");
            adaptedContractSearch.setCallbackSearch(mobileContractSearch.isCallbackSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(false);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // my customer search
        if (mobileContractSearch.isMyCustomerSearch()) {
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
