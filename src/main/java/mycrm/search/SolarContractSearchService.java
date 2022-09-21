package mycrm.search;

import mycrm.models.*;

public interface SolarContractSearchService {
    SolarSearchResult searchSolarContract(SolarContractSearch solarContractSearch, int pageNumber);

}
