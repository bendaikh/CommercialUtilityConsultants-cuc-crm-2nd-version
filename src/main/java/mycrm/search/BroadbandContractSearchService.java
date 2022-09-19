package mycrm.search;

import mycrm.models.BroadbandContractSearch;
import mycrm.models.BroadbandSearchResult;

public interface BroadbandContractSearchService {
    BroadbandSearchResult searchBroadbandContract(BroadbandContractSearch broadbandContractSearch, int pageNumber);
}
