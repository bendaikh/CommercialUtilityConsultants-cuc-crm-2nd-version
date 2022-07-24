package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.Contract;
import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.models.Customer;
import mycrm.models.UtilityContract;
import mycrm.models.UtilitySearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@Primary
public class ContractSearchServiceImpl implements ContractSearchService {

    private final UserHelper userHelper;
    private final EnergyContractSearchService energyContractSearchService;
    private final UtilitySearchService utilitySearchService;

    @Autowired
    public ContractSearchServiceImpl(UserHelper userHelper,
                                     EnergyContractSearchService energyContractSearchService,
                                     UtilitySearchService utilitySearchService) {
        this.userHelper = userHelper;
        this.energyContractSearchService = energyContractSearchService;
        this.utilitySearchService = utilitySearchService;
    }


    @Override
    public Set<Customer> searchMyCustomers(ContractSearch contractSearch) {
        ContractSearchResult contractSearchResult = energyContractSearchService.getEnergyContracts(adaptMyCustomerSearch(contractSearch), -1);
        UtilitySearchResult utilitySearchResult = utilitySearchService.searchUtilityContracts(adaptMyCustomerSearch(contractSearch), -1);
        List<Contract> returnedEnergyContracts = contractSearchResult.getReturnedContracts();
        List<UtilityContract> returnedUtilityContracts = utilitySearchResult.getReturnedContracts();

//        restrictAccessForBrokers(returnedEnergyContracts);
//        restrictAccessForExternalBroker(returnedEnergyContracts);
//        orderBy(returnedEnergyContracts);

        Iterator<Contract> energyContractsIterator = returnedEnergyContracts.iterator();
        Iterator<UtilityContract> utilityContractIterator = returnedUtilityContracts.iterator();

        Set<Customer> customerList = new HashSet<>();

        while (energyContractsIterator.hasNext()) {
            Customer customer = energyContractsIterator.next().getCustomer();
            customerList.add(customer);
        }

        while (utilityContractIterator.hasNext()) {
            Customer customer = utilityContractIterator.next().getCustomerSite().getCustomer();
            customerList.add(customer);
        }

        return customerList;

    }

    @Override
    public ContractSearchResult searchCustomerContract(ContractSearch contractSearch, int pageNumber) {

        ContractSearchResult fulltextSearchResult = energyContractSearchService.getEnergyContracts(adaptCustomerContractSearch(contractSearch), pageNumber);

        List<Contract> customerContracts = fulltextSearchResult.getReturnedContracts();

//        orderBy(customerContracts);
//        restrictAccessForExternalBroker(customerContracts);

        ContractSearchResult contractSearchResult = new ContractSearchResult();
        contractSearchResult.setReturnedContracts(customerContracts);
        contractSearchResult.setReturnedContractCount(fulltextSearchResult.getReturnedContractCount());
        contractSearchResult.setTotalContractCount(fulltextSearchResult.getTotalContractCount());
        contractSearchResult.setTotalPages(fulltextSearchResult.getTotalPages());

        return contractSearchResult;
    }

    private ContractSearch adaptCustomerContractSearch(ContractSearch contractSearch) {

        ContractSearch adaptedContractSearch = new ContractSearch();
        adaptedContractSearch.setQ(contractSearch.getQ());
        adaptedContractSearch.setLogType(contractSearch.getLogType());
        adaptedContractSearch.setSupplyType(contractSearch.getSupplyType());
        adaptedContractSearch.setSupplier(contractSearch.getSupplier());
        adaptedContractSearch.setFromDate(contractSearch.getFromDate());
        adaptedContractSearch.setToDate(contractSearch.getToDate());
        adaptedContractSearch.setBroker(contractSearch.getBroker());
        adaptedContractSearch.setMonthsRemaining(contractSearch.getMonthsRemaining());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser());

        adaptedContractSearch.setCustomerContractSearch(true);
        adaptedContractSearch.setShowExternalBrokerOwnContractsOnly(true);
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker());
        adaptedContractSearch.setDenyExternalBrokerAccess(false);
        adaptedContractSearch.setCampaign(contractSearch.getCampaign());

        return adaptedContractSearch;
    }

    private ContractSearch adaptMyCustomerSearch(ContractSearch contractSearch) {

        ContractSearch adaptedContractSearch = new ContractSearch();
        adaptedContractSearch.setQ(contractSearch.getQ());
        adaptedContractSearch.setLogType(contractSearch.getLogType());
        adaptedContractSearch.setSupplyType(contractSearch.getSupplyType());
        adaptedContractSearch.setSupplier(contractSearch.getSupplier());
        adaptedContractSearch.setFromDate(contractSearch.getFromDate());
        adaptedContractSearch.setToDate(contractSearch.getToDate());
        adaptedContractSearch.setBroker(contractSearch.getBroker());
        adaptedContractSearch.setMonthsRemaining(contractSearch.getMonthsRemaining());
        adaptedContractSearch.setLoggedInUser(userHelper.getLoggedInUser());

        adaptedContractSearch.setShowInternalBrokerOwnContractsOnly(false);
        adaptedContractSearch.setShowExternalBrokerOwnContractsOnly(true);
        adaptedContractSearch.setDenyExternalBrokerAccess(false);
        adaptedContractSearch.setPendingBroker(userHelper.getDefaultBroker());
        adaptedContractSearch.setMyCustomerSearch(true);

        return adaptedContractSearch;
    }

//    private void orderBy(List<Contract> customerContract) {
//        customerContract
//                .sort(Comparator.comparing(Contract::getDateCreated, Comparator.nullsLast(Comparator.reverseOrder())));
//    }

/*
    private void restrictAccessForBrokers(List<Contract> contracts) {
        User user = getLoggedInUser();

        if (user.isBroker() || user.isExternalBroker()) {
            Iterator<Contract> iterator = contracts.iterator();

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
