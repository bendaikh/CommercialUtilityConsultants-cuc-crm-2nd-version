package mycrm.search;

import mycrm.models.LandlineContractSearch;
import mycrm.models.LandlineSearchResult;

public interface LandlineContractSearchService {
    LandlineSearchResult searchLandlinesContract(LandlineContractSearch landlineContractSearch, int pageNumber);
}
