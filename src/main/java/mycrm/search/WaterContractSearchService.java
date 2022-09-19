package mycrm.search;

import mycrm.models.VoipContractSearch;
import mycrm.models.VoipSearchResult;
import mycrm.models.WaterContractSearch;
import mycrm.models.WaterSearchResult;

public interface WaterContractSearchService {
    WaterSearchResult searchWaterContract(WaterContractSearch waterContractSearch, int pageNumber);
}
