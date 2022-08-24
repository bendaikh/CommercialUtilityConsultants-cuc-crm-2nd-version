package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.CallbackSearchResult;
import mycrm.models.Contract;
import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.NotedCallback;
import mycrm.services.CustomerNoteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class CallbackSearchServiceImpl implements CallbackSearchService {

    private static final Logger logger = LogManager.getLogger();

    private final UserHelper userHelper;
    private final EnergyContractSearchService energyContractSearchService;
    private final CustomerNoteService customerNoteService;

    @Autowired
    public CallbackSearchServiceImpl(UserHelper userHelper,
                                     EnergyContractSearchService energyContractSearchService,
                                     CustomerNoteService customerNoteService) {
        this.userHelper = userHelper;
        this.energyContractSearchService = energyContractSearchService;
        this.customerNoteService = customerNoteService;
    }

    @Override
    public CallbackSearchResult searchCustomerContract(ContractSearch contractSearch, int pageNumber) {

        logger.info("Starting Callback search {} ", contractSearch.toString());

        ContractSearchResult fulltextSearchResult = energyContractSearchService.getEnergyContracts(adaptContractSearch(contractSearch), pageNumber);

        List<Contract> callbacks = fulltextSearchResult.getReturnedContracts();

//        orderBy(callbacks);

        CallbackSearchResult callbackSearchResult = new CallbackSearchResult();
        callbackSearchResult.setReturnedContracts(adaptContractNote(callbacks));
        callbackSearchResult.setReturnedContractCount(fulltextSearchResult.getReturnedContractCount());
        callbackSearchResult.setTotalContractCount(fulltextSearchResult.getTotalContractCount());
        callbackSearchResult.setTotalPages(fulltextSearchResult.getTotalPages());

        return callbackSearchResult;
    }

    private ContractSearch adaptContractSearch(ContractSearch contractSearch) {

        ContractSearch adaptedContractSearch = new ContractSearch();
        adaptedContractSearch.setQ(contractSearch.getQ());
        adaptedContractSearch.setLogType("CALLBACK"); //callback search
        adaptedContractSearch.setSupplyType(contractSearch.getSupplyType());
        adaptedContractSearch.setSupplier(contractSearch.getSupplier());
        adaptedContractSearch.setFromDate(contractSearch.getFromDate());
        adaptedContractSearch.setToDate(contractSearch.getToDate());
        adaptedContractSearch.setBroker(contractSearch.getBroker());
        adaptedContractSearch.setMonthsRemaining(contractSearch.getMonthsRemaining());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser());

        adaptedContractSearch.setCallbackSearch(true);
        adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(false);
        adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker());
        adaptedContractSearch.setDenyExternalBrokerAccess(false);
        adaptedContractSearch.setCampaign(contractSearch.getCampaign());

        return adaptedContractSearch;
    }

    private List<NotedCallback> adaptContractNote(List<Contract> callbacks) {
        List<NotedCallback> notedCallbackList = new ArrayList<>();

        callbacks.forEach(callback -> {

            NotedCallback notedCallback = new NotedCallback();
            notedCallback.setContract(callback);

            if (callback.getSupplyType().equals("GAS")) {
                notedCallback.setCustomerNotes(customerNoteService.findTop3ByGasCustomerContractOrderByDateCreatedDesc((GasCustomerContract) callback));
            }
            if (callback.getSupplyType().equals("ELEC")) {
                notedCallback.setCustomerNotes(customerNoteService.findTop3ByElecCustomerContractOrderByDateCreatedDesc((ElecCustomerContract) callback));
            }
            notedCallbackList.add(notedCallback);
        });

        return notedCallbackList;
    }
 /*
    private void orderBy(List<Contract> contracts) {
        contracts.sort(Comparator.comparing(Contract::getCallbackDateTime, Comparator.nullsLast(Comparator.naturalOrder())));
    }

    private void filterByUser(List<Contract> callbacks) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        
        if(user.isBroker()) {
            Iterator<Contract> iterator = callbacks.iterator();

            while (iterator.hasNext()) {
                Contract callback = iterator.next();
                if (callback.getBroker() != user.getBroker() && callback.getBroker().getId() != defaultBrokerId) {
                    iterator.remove();
                }
            }
        }
        
        if(user.isExternalBroker()) {
            Iterator<Contract> iterator = callbacks.iterator();

            while (iterator.hasNext()) {
                Contract callback = iterator.next();
                if (callback.getBroker() != user.getBroker()) {
                    iterator.remove();
                }
            }
        }  
    }
*/
}
