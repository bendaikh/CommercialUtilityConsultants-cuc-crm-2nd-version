package mycrm.search;

import mycrm.functions.UserHelper;
import mycrm.models.Broker;
import mycrm.models.Contract;
import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.Supplier;
import mycrm.models.User;
import mycrm.services.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContractSearchServiceImplTest {

    @InjectMocks
    private ContractSearchServiceImpl contractSearchService;

    @Mock
    private EnergyContractSearchService energyContractSearchService;

    @Mock
    private UserHelper userHelper;

    private Broker broker = new Broker();

    private Supplier britishGas = new Supplier();

    private Broker brokerTwo = new Broker();

    private Supplier npower = new Supplier();

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Mock
    private UserService userService;

    @Mock
    private User mockedUser;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Principal principal1;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(contractSearchService);

        broker.setId(Long.valueOf(54121));
        brokerTwo.setId(Long.valueOf(121));
        britishGas.setId(Long.valueOf(12));
        britishGas.setBusinessName("British Gas");
        npower.setId(Long.valueOf(18));
        npower.setBusinessName("NPower");

        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userService.findUserByUsername(any(String.class))).thenReturn(mockedUser);
    }

    @Test
    public final void shouldReturnSearchedListOfCustomerContractsWhenSearchingByLogType() throws Exception {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setLogType("POTENTIAL");

        GasCustomerContract gasCustomerContract = aGasCustomerContract();
        ElecCustomerContract electricCustomerContract = anElectricCustomerContract();

        List<Contract> gasSearchResults = new ArrayList<>();
        gasSearchResults.add(gasCustomerContract);

        List<Contract> elecSearchResults = new ArrayList<>();
        elecSearchResults.add(electricCustomerContract);

        ContractSearchResult gasContractSearchResult = new ContractSearchResult();
        gasContractSearchResult.setReturnedContracts(gasSearchResults);

        ContractSearchResult elecContractSearchResult = new ContractSearchResult();
        elecContractSearchResult.setReturnedContracts(elecSearchResults);

        when(energyContractSearchService.getEnergyContracts(any(ContractSearch.class), any(int.class))).thenReturn(gasContractSearchResult);

        List<Contract> searchResults = new ArrayList<>();
        searchResults.addAll(gasSearchResults);
        searchResults.addAll(elecSearchResults);

        ContractSearchResult contractSearchResult = contractSearchService.searchCustomerContract(contractSearch, 1);

        List<Contract> returnedCustomerContract = contractSearchResult.getReturnedContracts();

        assertEquals(1, returnedCustomerContract.size());
        assertEquals(searchResults.get(0).getLogType(), returnedCustomerContract.get(0).getLogType());
        assertEquals(searchResults.get(0).getBroker(), returnedCustomerContract.get(0).getBroker());
        assertEquals(searchResults.get(0).getSupplier(), returnedCustomerContract.get(0).getSupplier());
        assertEquals(searchResults.get(0).getSupplyType(), returnedCustomerContract.get(0).getSupplyType());
        assertEquals(searchResults.get(0).getContractID(), returnedCustomerContract.get(0).getContractID());
        assertEquals(searchResults.get(0).getAccountNumber(), returnedCustomerContract.get(0).getAccountNumber());
        assertEquals(searchResults.get(0).getCustomer(), returnedCustomerContract.get(0).getCustomer());
        assertEquals(searchResults.get(0).getCustomerSite(), returnedCustomerContract.get(0).getCustomerSite());
        assertEquals(searchResults.get(0).getStartDate(), returnedCustomerContract.get(0).getStartDate());
        assertEquals(searchResults.get(0).getEndDate(), returnedCustomerContract.get(0).getEndDate());
        assertEquals(searchResults.get(0).getDateCreated(), returnedCustomerContract.get(0).getDateCreated());
        assertEquals(searchResults.get(0).getContractStatus(), returnedCustomerContract.get(0).getContractStatus());
        assertEquals(searchResults.get(0).getCreatedBy(), returnedCustomerContract.get(0).getCreatedBy());
    }

    @Ignore
    @Test
    public final void shouldReturnSearchedListOfCustomerContractsWhenSearchingByBroker() throws Exception {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setBroker(brokerTwo);

        GasCustomerContract gasCustomerContract = aGasCustomerContract();
        ElecCustomerContract electricCustomerContract = anElectricCustomerContract();

        List<Contract> gasSearchResults = new ArrayList<>();
        gasSearchResults.add(gasCustomerContract);

        List<Contract> elecSearchResults = new ArrayList<>();
        elecSearchResults.add(electricCustomerContract);

        ContractSearchResult gasContractSearchResult = new ContractSearchResult();
        gasContractSearchResult.setReturnedContracts(gasSearchResults);

        ContractSearchResult elecContractSearchResult = new ContractSearchResult();
        elecContractSearchResult.setReturnedContracts(elecSearchResults);

        when(energyContractSearchService.getEnergyContracts(any(ContractSearch.class), any(int.class))).thenReturn(gasContractSearchResult);

        List<Contract> searchResults = new ArrayList<>();
        searchResults.add(electricCustomerContract);
        searchResults.add(gasCustomerContract);

        ContractSearchResult contractSearchResult = contractSearchService.searchCustomerContract(contractSearch, 1);

        List<Contract> returnedCustomerContract = contractSearchResult.getReturnedContracts();

        assertEquals(1, returnedCustomerContract.size());
        assertEquals(searchResults.get(0).getBroker(), returnedCustomerContract.get(0).getBroker());
    }

    @Ignore
    @Test
    public final void shouldReturnSearchedListOfCustomerContractsWhenSearchingBySupplyType() throws Exception {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setSupplyType("ELEC");

        GasCustomerContract gasCustomerContract = aGasCustomerContract();
        ElecCustomerContract electricCustomerContract = anElectricCustomerContract();

        List<Contract> gasSearchResults = new ArrayList<>();
        gasSearchResults.add(gasCustomerContract);

        List<Contract> elecSearchResults = new ArrayList<>();
        elecSearchResults.add(electricCustomerContract);

        ContractSearchResult gasContractSearchResult = new ContractSearchResult();
        gasContractSearchResult.setReturnedContracts(gasSearchResults);

        ContractSearchResult elecContractSearchResult = new ContractSearchResult();
        elecContractSearchResult.setReturnedContracts(elecSearchResults);

        when(energyContractSearchService.getEnergyContracts(any(ContractSearch.class), any(int.class))).thenReturn(gasContractSearchResult);

        List<Contract> searchResults = new ArrayList<>();
        searchResults.addAll(elecSearchResults);
        searchResults.addAll(gasSearchResults);

        ContractSearchResult contractSearchResult = contractSearchService.searchCustomerContract(contractSearch, 1);

        List<Contract> returnedCustomerContract = contractSearchResult.getReturnedContracts();

        assertEquals(1, returnedCustomerContract.size());
        assertEquals(searchResults.get(0).getLogType(), returnedCustomerContract.get(0).getLogType());
        assertEquals(searchResults.get(0).getBroker(), returnedCustomerContract.get(0).getBroker());
        assertEquals(searchResults.get(0).getSupplier(), returnedCustomerContract.get(0).getSupplier());
        assertEquals(searchResults.get(0).getSupplyType(), returnedCustomerContract.get(0).getSupplyType());
    }

    @Test
    public final void shouldReturnSearchedListOfCustomerContractsWhenSearchingBySupplier() throws Exception {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setSupplier(britishGas);

        GasCustomerContract gasCustomerContract = aGasCustomerContract();
        ElecCustomerContract electricCustomerContract = anElectricCustomerContract();

        List<Contract> gasSearchResults = new ArrayList<>();
        gasSearchResults.add(gasCustomerContract);

        List<Contract> elecSearchResults = new ArrayList<>();
        elecSearchResults.add(electricCustomerContract);

        ContractSearchResult gasContractSearchResult = new ContractSearchResult();
        gasContractSearchResult.setReturnedContracts(gasSearchResults);

        ContractSearchResult elecContractSearchResult = new ContractSearchResult();
        elecContractSearchResult.setReturnedContracts(elecSearchResults);

        when(energyContractSearchService.getEnergyContracts(any(ContractSearch.class), any(int.class))).thenReturn(gasContractSearchResult);

        List<Contract> searchResults = new ArrayList<>();
        searchResults.addAll(gasSearchResults);
        searchResults.addAll(elecSearchResults);

        ContractSearchResult contractSearchResult = contractSearchService.searchCustomerContract(contractSearch, 1);

        List<Contract> returnedCustomerContract = contractSearchResult.getReturnedContracts();

        assertEquals(1, returnedCustomerContract.size());
        assertEquals(searchResults.get(0).getLogType(), returnedCustomerContract.get(0).getLogType());
        assertEquals(searchResults.get(0).getBroker(), returnedCustomerContract.get(0).getBroker());
        assertEquals(searchResults.get(0).getSupplier(), returnedCustomerContract.get(0).getSupplier());
        assertEquals(searchResults.get(0).getSupplyType(), returnedCustomerContract.get(0).getSupplyType());
    }

    @Ignore
    @Test
    public final void shouldReturnSearchedListOfCustomerContractsWhenSearchCriteriaIsNullOrEmpty() throws Exception {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setLogType(null);
        contractSearch.setBroker(null);
        contractSearch.setSupplyType("");
        contractSearch.setQ("");

        GasCustomerContract gasCustomerContract = aGasCustomerContract();
        ElecCustomerContract electricCustomerContract = anElectricCustomerContract();

        List<Contract> gasSearchResults = new ArrayList<>();
        gasSearchResults.add(gasCustomerContract);

        List<Contract> elecSearchResults = new ArrayList<>();
        elecSearchResults.add(electricCustomerContract);

        ContractSearchResult gasContractSearchResult = new ContractSearchResult();
        gasContractSearchResult.setReturnedContracts(gasSearchResults);

        ContractSearchResult elecContractSearchResult = new ContractSearchResult();
        elecContractSearchResult.setReturnedContracts(elecSearchResults);

        when(energyContractSearchService.getEnergyContracts(any(ContractSearch.class), any(int.class))).thenReturn(gasContractSearchResult);

        List<Contract> searchResults = new ArrayList<>();
        searchResults.addAll(gasSearchResults);
        searchResults.addAll(elecSearchResults);

        ContractSearchResult contractSearchResult = contractSearchService.searchCustomerContract(contractSearch, 1);

        List<Contract> returnedCustomerContract = contractSearchResult.getReturnedContracts();

        assertEquals(2, returnedCustomerContract.size());
    }

    //fixed test
    @Test
    public final void shouldReturnSearchedListOfCustomerContractsWhenSearchingByFromDate() {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setFromDate("2015-01-01");

        GasCustomerContract gasCustomerContract = aGasCustomerContract();

        ContractSearchResult contractSearchResult = new ContractSearchResult();
        contractSearchResult.setReturnedContracts(Arrays.asList(gasCustomerContract));

        List<Contract> searchResults = contractSearchResult.getReturnedContracts();

        when(energyContractSearchService.getEnergyContracts(any(ContractSearch.class), any(int.class))).thenReturn(contractSearchResult);

        ContractSearchResult returnedContractSearchResult = contractSearchService.searchCustomerContract(contractSearch, 1);

        List<Contract> returnedCustomerContract = returnedContractSearchResult.getReturnedContracts();

        assertEquals(1, returnedCustomerContract.size());
        assertEquals(searchResults.get(0).getLogType(), returnedCustomerContract.get(0).getLogType());
        assertEquals(searchResults.get(0).getBroker(), returnedCustomerContract.get(0).getBroker());
        assertEquals(searchResults.get(0).getSupplier(), returnedCustomerContract.get(0).getSupplier());
        assertEquals(searchResults.get(0).getSupplyType(), returnedCustomerContract.get(0).getSupplyType());
    }

    @Ignore
    @Test
    public final void shouldReturnSearchedListOfCustomerContractsWhenSearchingByToDate() throws Exception {

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setToDate("2017-01-01");

        GasCustomerContract gasCustomerContract = aGasCustomerContract();
        ElecCustomerContract electricCustomerContract = anElectricCustomerContract();

        List<Contract> gasSearchResults = new ArrayList<>();
        gasSearchResults.add(gasCustomerContract);

        List<Contract> elecSearchResults = new ArrayList<>();
        elecSearchResults.add(electricCustomerContract);

        ContractSearchResult gasContractSearchResult = new ContractSearchResult();
        gasContractSearchResult.setReturnedContracts(gasSearchResults);

        ContractSearchResult elecContractSearchResult = new ContractSearchResult();
        elecContractSearchResult.setReturnedContracts(elecSearchResults);

        when(energyContractSearchService.getEnergyContracts(any(ContractSearch.class), any(int.class))).thenReturn(gasContractSearchResult);

        List<Contract> searchResults = new ArrayList<>();
        searchResults.addAll(elecSearchResults);
        searchResults.addAll(gasSearchResults);

        ContractSearchResult contractSearchResult = contractSearchService.searchCustomerContract(contractSearch, 1);

        List<Contract> returnedCustomerContract = contractSearchResult.getReturnedContracts();

        assertEquals(1, returnedCustomerContract.size());
        assertEquals(searchResults.get(0).getLogType(), returnedCustomerContract.get(0).getLogType());
        assertEquals(searchResults.get(0).getBroker(), returnedCustomerContract.get(0).getBroker());
        assertEquals(searchResults.get(0).getSupplier(), returnedCustomerContract.get(0).getSupplier());
        assertEquals(searchResults.get(0).getSupplyType(), returnedCustomerContract.get(0).getSupplyType());
    }

    private Customer aCustomer() {
        return new Customer();
    }

    private CustomerSite aCustomerSite() {
        return new CustomerSite();
    }

    private User aCreatedByUser() {
        return new User();
    }

    private Date randomDate() {
        return new Date();
    }

    private Date startDate() throws ParseException {
        return formatter.parse("2015-01-02");
    }

    private Date endDate() throws ParseException {
        return formatter.parse("2016-01-01");
    }

    private ElecCustomerContract anElectricCustomerContract() throws ParseException {
        ElecCustomerContract electricCustomerContract = new ElecCustomerContract();
        electricCustomerContract.setLogType("LIVE");
        electricCustomerContract.setBroker(brokerTwo);
        electricCustomerContract.setSupplier(npower);
        electricCustomerContract.setAccountNumber("654123");
        electricCustomerContract.setCustomer(aCustomer());
        electricCustomerContract.setCustomerSite(aCustomerSite());
        electricCustomerContract.setStartDate(startDate());
        electricCustomerContract.setEndDate(endDate());
        electricCustomerContract.setDateCreated(randomDate());
        electricCustomerContract.setCreatedBy(aCreatedByUser());

        return electricCustomerContract;
    }

    private GasCustomerContract aGasCustomerContract() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setLogType("POTENTIAL");
        gasCustomerContract.setBroker(broker);
        gasCustomerContract.setSupplier(britishGas);
        gasCustomerContract.setAccountNumber("654123");
        gasCustomerContract.setCustomer(aCustomer());
        gasCustomerContract.setCustomerSite(aCustomerSite());
        gasCustomerContract.setDateCreated(randomDate());
        gasCustomerContract.setCreatedBy(aCreatedByUser());

        return gasCustomerContract;
    }

}
