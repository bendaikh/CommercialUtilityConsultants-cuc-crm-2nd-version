package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.ContractSearch;
import mycrm.models.UtilityContract;
import mycrm.models.UtilitySearchResult;
import mycrm.repositories.UtilityContractSearchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UtilitySearchServiceImplTest {

    @InjectMocks
    private UtilitySearchServiceImpl utilitySearchService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Principal principal;

    @Mock
    private UtilityContractSearchRepository repository;

    @Mock
    private UserHelper userHelper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(utilitySearchService);

//        when(authentication.getPrincipal()).thenReturn(principal);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldReturnUtilityContractsSearchResult() {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setLogType("POTENTIAL");
        contractSearch.setCustomerContractSearch(true);

        List<UtilityContract> contracts = new ArrayList<>();

        UtilitySearchResult x = new UtilitySearchResult();
        x.setReturnedContracts(contracts);
        when(repository.search(anyObject(), anyInt())).thenReturn(x);

        UtilitySearchResult utilitySearchResult =
                utilitySearchService.searchUtilityContracts(contractSearch, 1);

        assertEquals(0, utilitySearchResult.getTotalPages());
        assertEquals(0, utilitySearchResult.getReturnedContractCount());
        assertEquals(0, utilitySearchResult.getTotalContractCount());
    }

    @Test
    public void shouldReturnLostUtilityRenewalsSearchResult() {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setLogType("POTENTIAL");
        contractSearch.setLostRenewalSearch(true);
        contractSearch.setLostRenewal(true);

        List<UtilityContract> contracts = new ArrayList<>();

        UtilitySearchResult x = new UtilitySearchResult();
        x.setReturnedContracts(contracts);
        when(repository.search(anyObject(), anyInt())).thenReturn(x);

        UtilitySearchResult utilitySearchResult =
                utilitySearchService.searchUtilityContracts(contractSearch, 1);

        assertEquals(0, utilitySearchResult.getTotalPages());
        assertEquals(0, utilitySearchResult.getReturnedContractCount());
        assertEquals(0, utilitySearchResult.getTotalContractCount());
    }

    @Test
    public void shouldReturnUtilityLeadsSearchResult() {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setLeadSearch(true);

        List<UtilityContract> contracts = new ArrayList<>();

        UtilitySearchResult x = new UtilitySearchResult();
        x.setReturnedContracts(contracts);
        when(repository.search(anyObject(), anyInt())).thenReturn(x);

        UtilitySearchResult utilitySearchResult =
                utilitySearchService.searchUtilityContracts(contractSearch, 1);

        assertEquals(0, utilitySearchResult.getTotalPages());
        assertEquals(0, utilitySearchResult.getReturnedContractCount());
        assertEquals(0, utilitySearchResult.getTotalContractCount());
    }

    @Test
    public void shouldReturnUtilityRenewalsSearchResult() {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setRenewalSearch(true);
        contractSearch.setMonthsRemaining(2);

        List<UtilityContract> contracts = new ArrayList<>();

        UtilitySearchResult x = new UtilitySearchResult();
        x.setReturnedContracts(contracts);
        when(repository.search(anyObject(), anyInt())).thenReturn(x);

        UtilitySearchResult utilitySearchResult =
                utilitySearchService.searchUtilityContracts(contractSearch, 1);

        assertEquals(0, utilitySearchResult.getTotalPages());
        assertEquals(0, utilitySearchResult.getReturnedContractCount());
        assertEquals(0, utilitySearchResult.getTotalContractCount());
    }

    @Test
    public void shouldReturnUtilityCallbackSearchResult() {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setCallbackSearch(true);

        List<UtilityContract> contracts = new ArrayList<>();

        UtilitySearchResult x = new UtilitySearchResult();
        x.setReturnedContracts(contracts);
        when(repository.search(anyObject(), anyInt())).thenReturn(x);

        UtilitySearchResult utilitySearchResult =
                utilitySearchService.searchUtilityContracts(contractSearch, 1);

        assertEquals(0, utilitySearchResult.getTotalPages());
        assertEquals(0, utilitySearchResult.getReturnedContractCount());
        assertEquals(0, utilitySearchResult.getTotalContractCount());
    }


}
