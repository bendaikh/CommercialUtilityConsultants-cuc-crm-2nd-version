package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.MerchantServicesContract;
import mycrm.models.MerchantServicesContractSearch;
import mycrm.models.MerchantServicesSearchResult;
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
public class MerchantServicesContractSearchServiceImpl implements MerchantServicesContractSearchService {
    private static final Logger logger = LogManager.getLogger();
    private final MerchantServicesService merchantServicesService;
    private final UserHelper userHelper;

    @Autowired
    public MerchantServicesContractSearchServiceImpl(MerchantServicesService merchantServicesService, UserHelper userHelper) {
        this.merchantServicesService = merchantServicesService;
        this.userHelper = userHelper;
    }

    @Override
    public MerchantServicesSearchResult searchMerchantServicesContract(MerchantServicesContractSearch merchantServicesContractSearch, int pageNumber) {
        MerchantServicesSearchResult fulltextSearchResult = merchantServicesService.getMerchantServicesContracts(
                adaptMerchantContractSearch(merchantServicesContractSearch),
                pageNumber);

        List<MerchantServicesContract> contracts = fulltextSearchResult.getReturnedContracts();

        return MerchantServicesSearchResult
                .builder()
                .returnedContracts(contracts)
                .returnedContractCount(fulltextSearchResult.getReturnedContractCount())
                .totalContractCount(fulltextSearchResult.getTotalContractCount())
                .totalPages(fulltextSearchResult.getTotalPages())
                .build();
    }

    private MerchantServicesContractSearch adaptMerchantContractSearch(MerchantServicesContractSearch merchantServicesContractSearch) {
        MerchantServicesContractSearch adaptedContractSearch = new MerchantServicesContractSearch();
        adaptedContractSearch.setQ(merchantServicesContractSearch.getQ());
        adaptedContractSearch.setLogType(merchantServicesContractSearch.getLogType());
        adaptedContractSearch.setFromDate(merchantServicesContractSearch.getFromDate()); // TODO
        adaptedContractSearch.setToDate(merchantServicesContractSearch.getToDate()); // TODO
        adaptedContractSearch.setBroker(merchantServicesContractSearch.getBroker());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser()); // TODO
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker()); // TODO
        adaptedContractSearch.setMonthsRemaining(merchantServicesContractSearch.getMonthsRemaining());

        // contract search
        if (merchantServicesContractSearch.isCustomerContractSearch()) {
            adaptedContractSearch.setLogType(merchantServicesContractSearch.getLogType());
            adaptedContractSearch.setCustomerContractSearch(merchantServicesContractSearch.isCustomerContractSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // lost renewal search
        if (merchantServicesContractSearch.isLostRenewalSearch()) {
            adaptedContractSearch.setLogType(merchantServicesContractSearch.getLogType());
            adaptedContractSearch.setLostRenewalSearch(merchantServicesContractSearch.isLostRenewalSearch());
            adaptedContractSearch.setLostRenewal(merchantServicesContractSearch.isLostRenewal());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // leads
        if (merchantServicesContractSearch.isLeadSearch()) {
            adaptedContractSearch.setLogType("POTENTIAL");
            adaptedContractSearch.setLeadSearch(merchantServicesContractSearch.isLeadSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        //renewals
        if (merchantServicesContractSearch.isRenewalSearch()) {
            adaptedContractSearch.setLogType("LIVE");
            adaptedContractSearch.setRenewalSearch(merchantServicesContractSearch.isRenewalSearch());
            adaptedContractSearch.setMonthRemainingDate(calculateDateByMonthRemaining(merchantServicesContractSearch.getMonthsRemaining()));
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }
        // callbacks
        if (merchantServicesContractSearch.isCallbackSearch()) {
            adaptedContractSearch.setLogType("CALLBACK");
            adaptedContractSearch.setCallbackSearch(merchantServicesContractSearch.isCallbackSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(false);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // my customer search
        if (merchantServicesContractSearch.isMyCustomerSearch()) {
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
