package mycrm.search;

import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;

public interface EnergyContractSearchService {
    ContractSearchResult getEnergyContracts(ContractSearch contractSearch, int pageNumber);
}
