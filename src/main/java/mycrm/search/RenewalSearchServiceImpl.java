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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Primary
public class RenewalSearchServiceImpl implements RenewalSearchService {

    private static Logger logger = LogManager.getLogger();

    private final EnergyContractSearchService energyContractSearchService;
    private final UserHelper userHelper;

    @Autowired
    public RenewalSearchServiceImpl(EnergyContractSearchService energyContractSearchService,
                                    UserHelper userHelper) {
        this.energyContractSearchService = energyContractSearchService;
        this.userHelper = userHelper;
    }

    @Override
    public ContractSearchResult searchCustomerContract(ContractSearch contractSearch, int pageNumber) {

        logger.info("Starting Renewal search {}", contractSearch.toString());

        ContractSearchResult fulltextSearchResult = energyContractSearchService.getEnergyContracts(adaptContractSearch(contractSearch), pageNumber);

        List<Contract> renewals = fulltextSearchResult.getReturnedContracts();

        //filterRenewals(renewals);
        //filterRenewed(renewals);
        //filterMonthsRemaining(contractSearch, renewals);
//        filterByUser(renewals);
//        orderBy(renewals);

        ContractSearchResult contractSearchResult = new ContractSearchResult();
        contractSearchResult.setReturnedContracts(renewals);
        contractSearchResult.setReturnedContractCount(fulltextSearchResult.getReturnedContractCount());
        contractSearchResult.setTotalContractCount(fulltextSearchResult.getTotalContractCount());
        contractSearchResult.setTotalPages(fulltextSearchResult.getTotalPages());

        return contractSearchResult;
    }

    private ContractSearch adaptContractSearch(ContractSearch contractSearch) {

        ContractSearch adaptedContractSearch = new ContractSearch();
        adaptedContractSearch.setQ(contractSearch.getQ());
        adaptedContractSearch.setLogType("LIVE"); //renewals search
        adaptedContractSearch.setSupplyType(contractSearch.getSupplyType());
        adaptedContractSearch.setSupplier(contractSearch.getSupplier());
        adaptedContractSearch.setFromDate(contractSearch.getFromDate());
        adaptedContractSearch.setToDate(contractSearch.getToDate());
        adaptedContractSearch.setBroker(contractSearch.getBroker());
        adaptedContractSearch.setMonthsRemaining(contractSearch.getMonthsRemaining());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser());

        adaptedContractSearch.setRenewalSearch(true);
        adaptedContractSearch.setShowInternalBrokerOwnAndPendingContracts(true);
        adaptedContractSearch.setShowExternalBrokerOwnAndPendingContracts(true);
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker());
        adaptedContractSearch.setDenyExternalBrokerAccess(false);
        adaptedContractSearch.setMonthRemainingDate(calculateDateByMonthRemaining(contractSearch.getMonthsRemaining()));
        adaptedContractSearch.setCampaign(contractSearch.getCampaign());

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

//    private void orderBy(List<Contract> contracts) {
//        contracts.sort(Comparator.comparing(Contract::getEndDate, Comparator.nullsLast(Comparator.naturalOrder())));
//    }

//    private void filterRenewed(List<Contract> renewals) {
//        Iterator<Contract> iterator = renewals.iterator();
//
//        while (iterator.hasNext()) {
//            Contract renewal = iterator.next();
//            if (renewal.isContractRenewed()) {
//                iterator.remove();
//            }
//        }
//    }
/*
    private void filterRenewals(List<Contract> renewals) {
        Iterator<Contract> iterator = renewals.iterator();

        while (iterator.hasNext()) {
            Contract renewal = iterator.next();
            if (!renewal.isRenewable()) {
                iterator.remove();
            }
        }
    }


    private void filterMonthsRemaining(ContractSearch contractSearch, List<Contract> renewals) {

        if (contractSearch.getMonthsRemaining() > 0) {
            Iterator<Contract> iterator = renewals.iterator();

            while (iterator.hasNext()) {
                Contract renewal = iterator.next();
//                logger.info("Renewal loop: " + renewal.getContractID());
                // remove any renewals more than the search value
                if (renewal.getMonthsRemaining() > contractSearch.getMonthsRemaining()) {
                    iterator.remove();
                }
            }
        }

    }
*/

/*
    private void filterByUser(List<Contract> contracts) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

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
    }
*/

}
