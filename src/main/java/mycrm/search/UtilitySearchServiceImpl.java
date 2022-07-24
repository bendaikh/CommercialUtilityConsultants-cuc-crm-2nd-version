package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.ContractSearch;
import mycrm.models.NotedUtilityCallback;
import mycrm.models.UtilityCallbackSearchResult;
import mycrm.models.UtilityContract;
import mycrm.models.UtilitySearchResult;
import mycrm.repositories.UtilityContractSearchRepository;
import mycrm.services.CustomerNoteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class UtilitySearchServiceImpl implements UtilitySearchService {

    private static final Logger logger = LogManager.getLogger();

    private final UtilityContractSearchRepository repository;
    private final UserHelper userHelper;
    private final CustomerNoteService customerNoteService;

    @Autowired
    public UtilitySearchServiceImpl(UtilityContractSearchRepository repository,
                                    UserHelper userHelper,
                                    CustomerNoteService customerNoteService) {
        this.repository = repository;
        this.userHelper = userHelper;
        this.customerNoteService = customerNoteService;
    }

    @Override
    public UtilitySearchResult searchUtilityContracts(ContractSearch contractSearch, int pageNumber) {
        logger.info("Starting Utility Contract search {} ", contractSearch.toString());

        UtilitySearchResult utilityContracts = this.repository.search(adaptContractSearch(contractSearch), pageNumber);

        List<UtilityContract> contracts = utilityContracts.getReturnedContracts();

        UtilitySearchResult utilitySearchResult = new UtilitySearchResult();
        utilitySearchResult.setReturnedContracts(contracts);
        utilitySearchResult.setReturnedContractCount(utilityContracts.getReturnedContractCount());
        utilitySearchResult.setTotalContractCount(utilityContracts.getTotalContractCount());
        utilitySearchResult.setTotalPages(utilityContracts.getTotalPages());

        return utilitySearchResult;
    }

    @Override
    public UtilityCallbackSearchResult searchNotedCallbackContracts(ContractSearch contractSearch, int pageNumber) {
        logger.info("Starting Utility Callback search");

        UtilitySearchResult utilityContracts = this.repository.search(adaptContractSearch(contractSearch), pageNumber);

        List<UtilityContract> utilityCallbacks = utilityContracts.getReturnedContracts();

        UtilityCallbackSearchResult utilityCallbackSearchResult = new UtilityCallbackSearchResult();
        utilityCallbackSearchResult.setReturnedContracts(adaptContractNote(utilityCallbacks));
        utilityCallbackSearchResult.setReturnedContractCount(utilityContracts.getReturnedContractCount());
        utilityCallbackSearchResult.setTotalContractCount(utilityContracts.getTotalContractCount());
        utilityCallbackSearchResult.setTotalPages(utilityContracts.getTotalPages());

        return utilityCallbackSearchResult;
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
        adaptedContractSearch.setCampaign(contractSearch.getCampaign());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser());
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker());

        // contract search
        if (contractSearch.isCustomerContractSearch()) {
            adaptedContractSearch.setLogType(contractSearch.getLogType());
            adaptedContractSearch.setCustomerContractSearch(contractSearch.isCustomerContractSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // lost renewal search
        if (contractSearch.isLostRenewalSearch()) {
            adaptedContractSearch.setLogType(contractSearch.getLogType());
            adaptedContractSearch.setLostRenewalSearch(contractSearch.isLostRenewalSearch());
            adaptedContractSearch.setLostRenewal(contractSearch.isLostRenewal());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // leads
        if (contractSearch.isLeadSearch()) {
            adaptedContractSearch.setLogType("POTENTIAL");
            adaptedContractSearch.setLeadSearch(contractSearch.isLeadSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        //renewals
        if (contractSearch.isRenewalSearch()) {
            adaptedContractSearch.setLogType("LIVE");
            adaptedContractSearch.setRenewalSearch(contractSearch.isRenewalSearch());
            adaptedContractSearch.setMonthRemainingDate(calculateDateByMonthRemaining(contractSearch.getMonthsRemaining()));
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }
        // callbacks
        if (contractSearch.isCallbackSearch()) {
            adaptedContractSearch.setLogType("CALLBACK");
            adaptedContractSearch.setCallbackSearch(contractSearch.isCallbackSearch());
            adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(false);
            adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
            adaptedContractSearch.setDenyExternalBrokerAccess(false);
        }

        // my customer search
        if (contractSearch.isMyCustomerSearch()) {
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

    private List<NotedUtilityCallback> adaptContractNote(List<UtilityContract> callbacks) {
        List<NotedUtilityCallback> notedCallbackList = new ArrayList<>();

        callbacks.forEach(callback -> {

            NotedUtilityCallback notedCallback = new NotedUtilityCallback();
            notedCallback.setContract(callback);
            notedCallback.setCustomerNotes(
                    customerNoteService.findTop3ByUtilityContractOrderByDateCreatedDesc(callback));
            notedCallbackList.add(notedCallback);
        });

        return notedCallbackList;
    }
}
