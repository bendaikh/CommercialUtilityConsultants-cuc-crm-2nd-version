package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.Role;
import mycrm.models.Supplier;
import mycrm.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewCustomerServiceImplTest {

    @InjectMocks
    private NewCustomerServiceImpl newCustomerServiceImpl;

    @Mock
    private SupplierService supplierService;

    @Mock
    private CustomerSiteService customerSiteService;

    @Mock
    private BrokerService brokerService;

    @Mock
    private GasContractService gasContractService;

    @Mock
    private ElecContractService elecContractService;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserService userService;

    @Test
    public void shouldCreateNewCustomerLead() {

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Principal principal1 = mock(Principal.class);
        when(authentication.getPrincipal()).thenReturn(principal1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ReflectionTestUtils.setField(newCustomerServiceImpl, "defaultBrokerId", Long.valueOf(5));
        ReflectionTestUtils.setField(newCustomerServiceImpl, "defaultSupplierId", Long.valueOf(10));


        Customer customer = new Customer();
        customer.setId(10013000l);

        CustomerSite customerSite = new CustomerSite();
        customerSite.setCustomer(customer);

        Supplier supplier = new Supplier();
        supplier.setId(Long.valueOf(10));

        Broker broker = new Broker();
        broker.setId(Long.valueOf(5));

        Role brokerRole = new Role();
        brokerRole.setId(3);
        brokerRole.setRole("BROKER");


        Set<Role> roles = new HashSet<>();
        roles.add(brokerRole);

        User user = new User();
        user.setId(14l);
        user.setBroker(broker);
        user.setRoles(roles);

        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setBroker(broker);
        gasCustomerContract.setSupplier(supplier);
        gasCustomerContract.setCustomerSite(customerSite);
        gasCustomerContract.setCustomer(customer);

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setBroker(broker);
        elecCustomerContract.setSupplier(supplier);
        elecCustomerContract.setCustomerSite(customerSite);
        elecCustomerContract.setCustomer(customer);

        when(brokerService.findById(Long.valueOf(5))).thenReturn(broker);
        when(supplierService.findById(Long.valueOf(10))).thenReturn(supplier);
        when(customerSiteService.save(customerSite)).thenReturn(customerSite);
        when(gasContractService.save(gasCustomerContract)).thenReturn(gasCustomerContract);
        when(elecContractService.save(elecCustomerContract)).thenReturn(elecCustomerContract);
        when(customerService.findById(Long.valueOf(10013000))).thenReturn(customer);
        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        newCustomerServiceImpl.createNewCustomerLead(customer, true, true);

        verify(customerService, times(1)).findById(any(Long.class));
        verify(customerSiteService, times(1)).save(any(CustomerSite.class));
        verify(supplierService, times(1)).findById(any(Long.class));
        verify(brokerService, times(1)).findById(any(Long.class));
        verify(gasContractService, times(1)).save(any(GasCustomerContract.class));
        verify(elecContractService, times(1)).save(any(ElecCustomerContract.class));

    }

}
