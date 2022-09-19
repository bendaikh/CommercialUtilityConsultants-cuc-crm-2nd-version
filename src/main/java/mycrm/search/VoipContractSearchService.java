package mycrm.search;

import mycrm.models.VoipContractSearch;
import mycrm.models.VoipSearchResult;

public interface VoipContractSearchService {
    VoipSearchResult searchVoipContract(VoipContractSearch voipContractSearch, int pageNumber);
}
