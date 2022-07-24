package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.repositories.CustomerSiteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerSiteServiceJpaImplTest {

    @InjectMocks
    private CustomerSiteServiceJpaImpl service;

    @Mock
    private CustomerSiteRepository mockedCustomerSiteRepository;

    @Mock
    private GasContractService mockGasContractService;

    @Mock
    private ElecContractService mockElectricContractService;

    @Mock
    private UtilityContractService mockUtilityContractService;

    @Mock
    private MerchantServicesService mockMerchantServicesService;

    @Test
    public void shouldFindAllCustomerSite() {
        List<CustomerSite> customerSiteList = new ArrayList<>();
        customerSiteList.add(new CustomerSite());

        when(mockedCustomerSiteRepository.findAll()).thenReturn(customerSiteList);

        List<CustomerSite> findAll = service.findAll();

        assertEquals(findAll, customerSiteList);
    }

    @Test
    public void shouldReturnFiveCustomerSite() {
        List<CustomerSite> customerSiteList = new ArrayList<>();
        customerSiteList.add(new CustomerSite());

        when(mockedCustomerSiteRepository.findLatest5Posts(new PageRequest(0, 5))).thenReturn(customerSiteList);

        List<CustomerSite> findLatest5 = service.findLatest5();

        assertEquals(findLatest5, customerSiteList);
    }

    @Test
    public void shouldFindCustomerSiteById() {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(5L);

        when(mockedCustomerSiteRepository.findOne(Long.valueOf(5))).thenReturn(customerSite);

        CustomerSite findById = service.findById(5L);

        assertEquals(findById, customerSite);
    }

    @Test
    public void shouldSaveCustomerSite() {
        Customer customer = new Customer();

        Set<GasCustomerContract> gasCustomerContracts = new HashSet<>();
        gasCustomerContracts.add(new GasCustomerContract());

        Set<ElecCustomerContract> elecCustomerContracts = new HashSet<>();
        elecCustomerContracts.add(new ElecCustomerContract());

        CustomerSite customerSite = new CustomerSite(customer);
        customerSite.setId(5L);
        customerSite.setVersion(0);
        customerSite.setCustomer(customer);
        customerSite.setSiteName("Site name");
        customerSite.setSiteAddr("Site Address Line 1");
        customerSite.setSiteAddr1("Site Address Line 1");
        customerSite.setSiteCity("Site City");
        customerSite.setSitePostcodeIn("SO1");
        customerSite.setSitePostcodeOut("1OU");
        customerSite.setSiteTelephone("01274987456");
        customerSite.setMpr("12345");
        customerSite.setMpanLineOne("1");
        customerSite.setMpanLineTwo("2");
        customerSite.setMpanLineThree("3");
        customerSite.setMpanBottomLine("000");
        customerSite.setGasCustomerContracts(gasCustomerContracts);
        customerSite.setElecCustomerContracts(elecCustomerContracts);

        when(mockedCustomerSiteRepository.save(any(CustomerSite.class))).thenReturn(customerSite);

        CustomerSite savedCustomer = service.save(customerSite);

        assertEquals(savedCustomer, customerSite);
        assertEquals(savedCustomer.getId(), customerSite.getId());
        assertEquals(savedCustomer.getVersion(), customerSite.getVersion());
        assertEquals(savedCustomer.getCustomer(), customerSite.getCustomer());
        assertEquals(savedCustomer.getSiteName(), customerSite.getSiteName());
        assertEquals(savedCustomer.getSiteAddr(), customerSite.getSiteAddr());
        assertEquals(savedCustomer.getSiteAddr1(), customerSite.getSiteAddr1());
        assertEquals(savedCustomer.getSiteCity(), customerSite.getSiteCity());
        assertEquals(savedCustomer.getSitePostcodeIn(), customerSite.getSitePostcodeIn());
        assertEquals(savedCustomer.getSitePostcodeOut(), customerSite.getSitePostcodeOut());
        assertEquals(savedCustomer.getSiteTelephone(), customerSite.getSiteTelephone());
        assertEquals(savedCustomer.getMpr(), customerSite.getMpr());
        assertEquals(savedCustomer.getMpanLineOne(), customerSite.getMpanLineOne());
        assertEquals(savedCustomer.getMpanLineTwo(), customerSite.getMpanLineTwo());
        assertEquals(savedCustomer.getMpanLineThree(), customerSite.getMpanLineThree());
        assertEquals(savedCustomer.getMpanBottomLine(), customerSite.getMpanBottomLine());
        assertEquals(savedCustomer.getGasCustomerContracts(), customerSite.getGasCustomerContracts());
        assertEquals(savedCustomer.getElecCustomerContracts(), customerSite.getElecCustomerContracts());
    }

    @Test
    public void shouldEditAndSaveCustomerSite() {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(5L);

        when(mockedCustomerSiteRepository.save(any(CustomerSite.class))).thenReturn(customerSite);

        CustomerSite savedCustomerSite = service.save(customerSite);

        assertEquals(savedCustomerSite, customerSite);
    }

    @Test
    public void shouldDeleteCustomerSite() {
        service.deleteById(541L);

        verify(mockedCustomerSiteRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldReturnListOfCustomerSitesByCustomer() {
        Customer customer = new Customer();
        customer.setId(654L);

        List<CustomerSite> customerSiteList = new ArrayList<>();
        customerSiteList.add(new CustomerSite());

        when(mockedCustomerSiteRepository.findByCustomer(customer)).thenReturn(customerSiteList);

        List<CustomerSite> findByCustomer = service.findByCustomer(customer);

        assertEquals(findByCustomer, customerSiteList);
    }

    @Test
    public void shouldDeleteCustomerSiteByCustomer() {
        Customer customer = new Customer();
        customer.setId(22L);

        service.deleteByCustomer(customer);

        verify(mockedCustomerSiteRepository, times(1))
                .deleteByCustomer(any(Customer.class));
    }

    @Test
    public void shouldReturnCustomerSitesWithContracts() {
        Customer customer = new Customer();
        customer.setId(22L);

        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(98L);
        customerSite.setCustomer(customer);

        List<CustomerSite> customerSiteList = new ArrayList<>();
        customerSiteList.add(customerSite);

        when(service.findByCustomer(any(Customer.class))).thenReturn(customerSiteList);

        service.customerSitesWithContracts(customer);

        verify(mockGasContractService, times(1))
                .findLatestGasCustomerContractBySiteOrderedByLiveFirst(any(Long.class));

        verify(mockElectricContractService, times(1))
                .findLatestElecCustomerContractBySiteOrderedByLiveFirst(any(Long.class));

        verify(mockUtilityContractService, times(1))
                .findSolarContractByCustomerSite(any(CustomerSite.class));

        verify(mockUtilityContractService, times(1))
                .findWaterContractByCustomerSite(any(CustomerSite.class));

        verify(mockUtilityContractService, times(1))
                .findVoipContractByCustomerSite(any(CustomerSite.class));

        verify(mockUtilityContractService, times(1))
                .findLandlineContractByCustomerSite(any(CustomerSite.class));

        verify(mockUtilityContractService, times(1))
                .findMobileContractByCustomerSite(any(CustomerSite.class));

        verify(mockUtilityContractService, times(1))
                .findBroadbandContractByCustomerSite(any(CustomerSite.class));

    }

    @Test
    public void shouldReturnSuccessfulResponseWhenTransferCustomerSite() {
        Customer previousCustomer = new Customer();
        previousCustomer.setId(33L);
        previousCustomer.setBusinessName("Test");

        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(2L);
        customerSite.setCustomer(previousCustomer);

        Customer selectedCustomer = new Customer();
        selectedCustomer.setId(24L);

        when(mockedCustomerSiteRepository.save(any(CustomerSite.class))).thenReturn(customerSite);

        CustomerSite site = service.transferCustomerSite(customerSite, selectedCustomer);

        assertEquals(selectedCustomer.getId(), site.getCustomer().getId());
    }

}
