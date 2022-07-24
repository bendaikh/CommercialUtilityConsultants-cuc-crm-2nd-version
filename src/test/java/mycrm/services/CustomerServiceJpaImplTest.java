package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.CustomerNote;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.repositories.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceJpaImplTest {

    @InjectMocks
    private CustomerServiceJpaImpl customerServiceJpaImpl;

    @Mock
    private Customer mockedCustomer;

    @Mock
    private CustomerRepository mockedCustomerRepository;

    @Test
    public void shouldFindAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer());

        when(mockedCustomerRepository.findAll()).thenReturn(customerList);

        List<Customer> findAll = customerServiceJpaImpl.findAll();

        assertEquals(findAll, customerList);
    }

    @Test
    public void shouldReturnFiveCustomers() {
        List<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer());

        when(mockedCustomerRepository.findLatest5(new PageRequest(0, 5))).thenReturn(customerList);

        List<Customer> findLatest5 = customerServiceJpaImpl.findLatest5();

        assertEquals(findLatest5, customerList);
    }

    @Test
    public void shouldFindCustomersById() {
        Set<GasCustomerContract> gasCustomerContracts = null;
        Set<ElecCustomerContract> elecCustomerContracts = null;
        Set<CustomerSite> customerSite = null;
        Set<CustomerNote> customerNote = null;
        
        Customer customer = new Customer();
        customer.setId(Long.valueOf(5));
        customer.setVersion(1);
        customer.setCustomerReference("Customer Reference");
        customer.setBusinessName("Business Name");
        customer.setBusinessAddr("Addr 1");
        customer.setBusinessAddr1("Addr 2");
        customer.setBusinessCity("City");
        customer.setBusinessPostcodeIn("BB1");
        customer.setBusinessPostcodeOut("1AA");
        customer.setLtdRegNo("123456");
        customer.setSoleTrader(true);
        customer.setFirstName("First name");
        customer.setLastName("Last name");
        customer.setStAddr("st addr");
        customer.setStAddr1("st addr1");
        customer.setStCity("City");
        customer.setStPostcodeIn("in");
        customer.setStPostcodeOut("out");
        customer.setContactNumber("01274 987456");
        customer.setMobileNumber("0741254785");
        customer.setEmailAddress("imy@imy.com");
        customer.setWebAddress("www.webadress.com");
        customer.setDob(new Date());
        customer.setElecCustomerContracts(elecCustomerContracts);
        customer.setGasCustomerContracts(gasCustomerContracts);
        customer.setCustomerSites(customerSite);
        customer.setCustomerNotes(customerNote);

        when(mockedCustomerRepository.findOne(Long.valueOf(5))).thenReturn(customer);

        Customer returnedCustomer = customerServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(customer, returnedCustomer);

        assertEquals(customer.getId(), returnedCustomer.getId());
        assertEquals(customer.getVersion(), returnedCustomer.getVersion());
        assertEquals(customer.getCustomerReference(), returnedCustomer.getCustomerReference());
        assertEquals(customer.getBusinessName(), returnedCustomer.getBusinessName());
        assertEquals(customer.getBusinessAddr(), returnedCustomer.getBusinessAddr());
        assertEquals(customer.getBusinessAddr1(), returnedCustomer.getBusinessAddr1());
        assertEquals(customer.getBusinessCity(), returnedCustomer.getBusinessCity());
        assertEquals(customer.getBusinessPostcodeIn(), returnedCustomer.getBusinessPostcodeIn());
        assertEquals(customer.getBusinessPostcodeOut(), returnedCustomer.getBusinessPostcodeOut());
        assertEquals(customer.getLtdRegNo(), returnedCustomer.getLtdRegNo());
        assertEquals(customer.isSoleTrader(), returnedCustomer.isSoleTrader());
        assertEquals(customer.getFirstName(), returnedCustomer.getFirstName());
        assertEquals(customer.getLastName(), returnedCustomer.getLastName());
        assertEquals(customer.getStAddr(), returnedCustomer.getStAddr());
        assertEquals(customer.getStAddr1(), returnedCustomer.getStAddr1());
        assertEquals(customer.getStCity(), returnedCustomer.getStCity());
        assertEquals(customer.getStPostcodeIn(), returnedCustomer.getStPostcodeIn());
        assertEquals(customer.getStPostcodeOut(), returnedCustomer.getStPostcodeOut());
        assertEquals(customer.getContactNumber(), returnedCustomer.getContactNumber());
        assertEquals(customer.getMobileNumber(), returnedCustomer.getMobileNumber());
        assertEquals(customer.getEmailAddress(), returnedCustomer.getEmailAddress());
        assertEquals(customer.getWebAddress(), returnedCustomer.getWebAddress());
        assertEquals(customer.getDob(), returnedCustomer.getDob());
        assertEquals(customer.getElecCustomerContracts(), returnedCustomer.getElecCustomerContracts());
        assertEquals(customer.getGasCustomerContracts(), returnedCustomer.getGasCustomerContracts());
        assertEquals(customer.getCustomerNotes(), returnedCustomer.getCustomerNotes());
        assertEquals(customer.getCustomerSites(), returnedCustomer.getCustomerSites());
    }

    @Test
    public void shouldSaveCustomers() {
        Customer customer = new Customer();
        customer.setId(Long.valueOf(5));

        when(mockedCustomerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerServiceJpaImpl.save(customer);

        assertEquals(savedCustomer, customer);
    }

    @Test
    public void shouldEditAndSaveCustomers() {
        Customer customer = new Customer();
        customer.setId(Long.valueOf(5));

        when(mockedCustomerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerServiceJpaImpl.edit(customer);

        assertEquals(savedCustomer, customer);
    }

    @Test
    public void shouldDeleteCustomers() {
        customerServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedCustomerRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldFindByCustomerReference() {
        Customer customer = new Customer();
        customer.setId(22L);
        customer.setCustomerReference("CUC12345677");

        when(mockedCustomerRepository.findByCustomerReference(any(String.class))).thenReturn(customer);

        Customer foundCustomer = customerServiceJpaImpl.findByCustomerReference("CUC12345677");

        assertEquals(customer.getCustomerReference(), foundCustomer.getCustomerReference());
    }

    @Test
    public void shouldReturnNullWhenCustomerNotFoundByReference() {
        when(mockedCustomerRepository.findByCustomerReference(any(String.class))).thenReturn(null);

        Customer foundCustomer = customerServiceJpaImpl.findByCustomerReference("CUC12345677");

        assertNull(foundCustomer);
    }
}
