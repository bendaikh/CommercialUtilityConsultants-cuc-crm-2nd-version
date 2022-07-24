package mycrm.services;

import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.models.TpsCheck;
import mycrm.models.TpsContact;
import mycrm.repositories.TpsCheckRepository;
import mycrm.rest.clients.TpsCheckRestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TpsCheckServiceTest {
    @InjectMocks
    private TpsCheckServiceImpl tpsCheckService;

    @Mock
    private TpsCheckRestClient mockTpsCheckRestClient;

    @Mock
    private TpsCheckRepository mockTpsCheckRepository;

    @Mock
    private ContactService mockContactService;

    @Test
    public void shouldCheckCustomerMobileNumber() {
        Customer customer = customerWithMobileNumber();
        TpsCheck tpsCheck = tpsCheck(customer.getMobileNumber(), "safe", 200);

        when(mockTpsCheckRestClient.callTpsApiService(customer.getMobileNumber())).thenReturn(tpsCheck);
        tpsCheckService.checkCustomerNumbers(customer);

        verify(mockTpsCheckRestClient, times(1)).callTpsApiService(customer.getMobileNumber());
        verify(mockTpsCheckRepository, times(1)).save(tpsCheck);
    }

    @Test
    public void shouldCheckCustomerContactNumber() {
        Customer customer = customerWithContactNumber();
        TpsCheck tpsCheck = tpsCheck(customer.getContactNumber(), "safe", 200);

        when(mockTpsCheckRestClient.callTpsApiService(customer.getContactNumber())).thenReturn(tpsCheck);
        tpsCheckService.checkCustomerNumbers(customer);

        verify(mockTpsCheckRestClient, times(1)).callTpsApiService(customer.getContactNumber());
        verify(mockTpsCheckRepository, times(1)).save(tpsCheck);
    }

    @Test
    public void shouldCheckCustomerBothNumbers() {
        Customer customer = customerWithBothNumbers();
        TpsCheck contactNumberTpsCheck = tpsCheck(customer.getContactNumber(), "safe", 200);
        TpsCheck mobileTpsCheck = tpsCheck(customer.getMobileNumber(), "safe", 200);

        when(mockTpsCheckRestClient.callTpsApiService(customer.getMobileNumber())).thenReturn(mobileTpsCheck);
        when(mockTpsCheckRestClient.callTpsApiService(customer.getContactNumber())).thenReturn(contactNumberTpsCheck);
        tpsCheckService.checkCustomerNumbers(customer);

        verify(mockTpsCheckRestClient, times(1)).callTpsApiService(customer.getContactNumber());
        verify(mockTpsCheckRestClient, times(1)).callTpsApiService(customer.getMobileNumber());
        verify(mockTpsCheckRepository, times(1)).save(contactNumberTpsCheck);
        verify(mockTpsCheckRepository, times(1)).save(mobileTpsCheck);
    }

    @Test
    public void shouldSaveTpsCheck() {
        Customer customer = customerWithBothNumbers();
        TpsCheck contactNumberTpsCheck = tpsCheck(customer.getContactNumber(), "safe", 200);

        when(mockTpsCheckRepository.save(any(TpsCheck.class))).thenReturn(contactNumberTpsCheck);

        TpsCheck tpsCheck = tpsCheckService.save(contactNumberTpsCheck);

        verify(mockTpsCheckRepository, times(1)).save(contactNumberTpsCheck);

        assertEquals(tpsCheck.getMessage(), contactNumberTpsCheck.getMessage());
        assertEquals(tpsCheck.getNumber(), contactNumberTpsCheck.getNumber());
        assertEquals(tpsCheck.getStatusCode(), contactNumberTpsCheck.getStatusCode());
    }

    @Test
    public void shouldCheckContactsMobileNumber() {
        Contact contact = contactWithMobileNumber();
        TpsCheck tpsCheck = tpsCheck(contact.getMobileNumber(), "safe", 200);

        when(mockTpsCheckRestClient.callTpsApiService(contact.getMobileNumber())).thenReturn(tpsCheck);

        tpsCheckService.checkContactNumbers(contact);

        verify(mockTpsCheckRestClient, times(1)).callTpsApiService(contact.getMobileNumber());
        verify(mockTpsCheckRepository, times(1)).save(tpsCheck);
    }

    @Test
    public void shouldCheckContactsContactNumber() {
        Contact contact = contactWithContactNumber();
        TpsCheck tpsCheck = tpsCheck(contact.getContactNumber(), "safe", 200);

        when(mockTpsCheckRestClient.callTpsApiService(contact.getContactNumber())).thenReturn(tpsCheck);

        tpsCheckService.checkContactNumbers(contact);

        verify(mockTpsCheckRestClient, times(1)).callTpsApiService(contact.getContactNumber());
        verify(mockTpsCheckRepository, times(1)).save(tpsCheck);
    }

    @Test
    public void shouldCheckContactsBothNumbers() {
        Contact contact = contactWithBothNumbers();
        TpsCheck contactNumberTpsCheck = tpsCheck(contact.getContactNumber(), "safe", 200);
        TpsCheck mobileTpsCheck = tpsCheck(contact.getMobileNumber(), "safe", 200);

        when(mockTpsCheckRestClient.callTpsApiService(contact.getMobileNumber())).thenReturn(mobileTpsCheck);
        when(mockTpsCheckRestClient.callTpsApiService(contact.getContactNumber())).thenReturn(contactNumberTpsCheck);

        tpsCheckService.checkContactNumbers(contact);

        verify(mockTpsCheckRestClient, times(1)).callTpsApiService(contact.getContactNumber());
        verify(mockTpsCheckRestClient, times(1)).callTpsApiService(contact.getMobileNumber());
        verify(mockTpsCheckRepository, times(1)).save(contactNumberTpsCheck);
        verify(mockTpsCheckRepository, times(1)).save(mobileTpsCheck);
    }

    @Test
    public void shouldFindTpsChecksByCustomerMobile() {
        Customer customer = customerWithMobileNumber();
        List<TpsCheck> listOfChecks = Arrays.asList(tpsCheck(customer.getMobileNumber(), "safe", 200));

        when(mockTpsCheckRepository.findOkChecksByNumber(any(String.class))).thenReturn(listOfChecks);
        List<TpsCheck> tpsCheckList = tpsCheckService.findByCustomer(customer);

        assertEquals(tpsCheckList.get(0).getStatusCode(), listOfChecks.get(0).getStatusCode());
        assertEquals(tpsCheckList.get(0).getMessage(), listOfChecks.get(0).getMessage());
        assertEquals(tpsCheckList.get(0).getMessage(), listOfChecks.get(0).getMessage());
    }

    @Test
    public void shouldFindTpsChecksByCustomerContactNumber() {
        Customer customer = customerWithContactNumber();
        List<TpsCheck> listOfChecks = Arrays.asList(tpsCheck(customer.getContactNumber(), "safe", 200));

        when(mockTpsCheckRepository.findOkChecksByNumber(any(String.class))).thenReturn(listOfChecks);
        List<TpsCheck> tpsCheckList = tpsCheckService.findByCustomer(customer);

        assertEquals(tpsCheckList.get(0).getStatusCode(), listOfChecks.get(0).getStatusCode());
        assertEquals(tpsCheckList.get(0).getMessage(), listOfChecks.get(0).getMessage());
        assertEquals(tpsCheckList.get(0).getMessage(), listOfChecks.get(0).getMessage());
    }

    @Test
    public void shouldFindTpsChecksByCustomersContactsNumbers() {
        Contact contact = contactWithBothNumbers();

        List<Contact> contactList = Arrays.asList(contact);

        List<TpsCheck> combinedList = new ArrayList<>();
        combinedList.addAll(Arrays.asList(tpsCheck(contact.getContactNumber(), "safe", 200)));
        combinedList.addAll(Arrays.asList(tpsCheck(contact.getMobileNumber(), "safe", 200)));

        when(mockTpsCheckRepository.findOkChecksByNumber(any(String.class))).thenReturn(combinedList);
        when(mockContactService.findByCustomer(any(Customer.class))).thenReturn(contactList);

        List<TpsCheck> tpsCheckList = tpsCheckService.findByCustomer(contact.getCustomer());

        assertEquals(tpsCheckList.get(0).getStatusCode(), combinedList.get(0).getStatusCode());
        assertEquals(tpsCheckList.get(0).getMessage(), combinedList.get(0).getMessage());
        assertEquals(tpsCheckList.get(0).getMessage(), combinedList.get(0).getMessage());

        assertEquals(tpsCheckList.get(1).getStatusCode(), combinedList.get(1).getStatusCode());
        assertEquals(tpsCheckList.get(1).getMessage(), combinedList.get(1).getMessage());
        assertEquals(tpsCheckList.get(1).getMessage(), combinedList.get(1).getMessage());
    }

    @Test
    public void shouldFindLatestTpsCheck() {
        TpsCheck tpsCheck = tpsCheck("01274987456", "safe", 200);
        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);

        TpsCheck latestTpsCheck = tpsCheckService.findLatestTpsCheck("01274987456");
        assertEquals(latestTpsCheck.getStatusCode(), tpsCheck.getStatusCode());
        assertEquals(latestTpsCheck.getMessage(), tpsCheck.getMessage());
        assertEquals(latestTpsCheck.getMessage(), tpsCheck.getMessage());
    }

    @Test
    public void shouldCheckCustomerContactNumberIsWithinCheckPeriod() {
        Customer customer = customerWithContactNumber();

        LocalDate withinOneMonth = LocalDate.now().minusDays(3);
        TpsCheck contactTpsCheck = tpsCheck(customer.getContactNumber(), "safe", 200);
        contactTpsCheck.setDateCreated(Date.from(withinOneMonth
                .atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant()));

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(contactTpsCheck);
        boolean customerWithinCheckPeriod = tpsCheckService.customerWithinCheckPeriod(customer);

        assertEquals(customerWithinCheckPeriod, true);
    }

    @Test
    public void shouldCheckCustomerMobileNumberIsWithinCheckPeriod() {
        Customer customer = customerWithMobileNumber();

        LocalDate withinOneMonth = LocalDate.now().minusDays(3);
        TpsCheck mobileTpsCheck = tpsCheck(customer.getMobileNumber(), "safe", 200);
        mobileTpsCheck.setDateCreated(Date.from(withinOneMonth
                .atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant()));

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(mobileTpsCheck);
        boolean customerWithinCheckPeriod = tpsCheckService.customerWithinCheckPeriod(customer);

        assertEquals(customerWithinCheckPeriod, true);
    }

    @Test
    public void shouldCheckCustomerIsOutsideCheckPeriod() {
        Customer customer = customerWithBothNumbers();

        LocalDate outsideOneMonth = LocalDate.now().minusMonths(3);
        TpsCheck tpsCheck = tpsCheck(customer.getContactNumber(), "safe", 200);
        tpsCheck.setDateCreated(Date.from(outsideOneMonth
                .atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant()));

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);
        boolean customerWithinCheckPeriod = tpsCheckService.customerWithinCheckPeriod(customerWithBothNumbers());

        assertEquals(customerWithinCheckPeriod, false);
    }

    @Test
    public void shouldReturnFalseWhenCustomerHasNoNumbers() {
        boolean customerWithinCheckPeriod = tpsCheckService.customerWithinCheckPeriod(customerWithBothNumbers());
        assertEquals(customerWithinCheckPeriod, false);
    }

    @Test
    public void shouldReturnGreenStyleWhenContactNumberTpsIsSafe() {
        Customer customer = customerWithContactNumber();
        TpsCheck tpsCheck = tpsCheck(customer.getContactNumber(), "safe", 200);

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);
        String style = tpsCheckService.customerContactNumberStyle(customer);

        assertEquals(style, "success");
    }

    @Test
    public void shouldReturnRedStyleWhenContactNumberTpsIsListed() {
        Customer customer = customerWithContactNumber();
        TpsCheck tpsCheck = tpsCheck(customer.getContactNumber(), "listed", 200);

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);
        String style = tpsCheckService.customerContactNumberStyle(customer);

        assertEquals(style, "danger");
    }

    @Test
    public void shouldReturnGreyStyleWhenContactNumberTpsIsNotFound() {
        Customer customer = customerWithContactNumber();

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(null);
        String style = tpsCheckService.customerContactNumberStyle(customer);

        assertEquals(style, "text-muted");
    }

    @Test
    public void shouldReturnGreyStyleWhenContactNumberTpsIsMissing() {
        Customer customer = new Customer();

        String style = tpsCheckService.customerContactNumberStyle(customer);

        assertEquals(style, "text-muted");
    }

    @Test
    public void shouldReturnGreenStyleWhenMobileNumberTpsIsSafe() {
        Customer customer = customerWithMobileNumber();
        TpsCheck tpsCheck = tpsCheck(customer.getMobileNumber(), "safe", 200);

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);
        String style = tpsCheckService.customerMobileNumberStyle(customer);

        assertEquals(style, "success");
    }

    @Test
    public void shouldReturnRedStyleWhenMobileNumberTpsIsListed() {
        Customer customer = customerWithMobileNumber();
        TpsCheck tpsCheck = tpsCheck(customer.getMobileNumber(), "listed", 200);

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);
        String style = tpsCheckService.customerMobileNumberStyle(customer);

        assertEquals(style, "danger");
    }

    @Test
    public void shouldReturnGreyStyleWhenMobileNumberTpsIsNotFound() {
        Customer customer = customerWithMobileNumber();

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(null);
        String style = tpsCheckService.customerContactNumberStyle(customer);

        assertEquals(style, "text-muted");
    }

    @Test
    public void shouldReturnGreyStyleWhenMobileNumberIsMissing() {
        Customer customer = new Customer();

        String style = tpsCheckService.customerMobileNumberStyle(customer);

        assertEquals(style, "text-muted");
    }

    @Test
    public void shouldReturnTpsChecksForContactsContactNumber() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contactWithContactNumber());

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(null);

        List<TpsContact> tpsContacts = tpsCheckService.buildContactList(contactList);
        assertEquals(tpsContacts.get(0).getContact().getContactNumber(), contactList.get(0).getContactNumber());
        assertEquals(tpsContacts.get(0).getContactNumberStyle(), "text-muted");
        assertEquals(tpsContacts.get(0).getMobileNumberStyle(), "text-muted");
    }

    @Test
    public void shouldReturnTpsChecksForContactsMobileNumber() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contactWithMobileNumber());

        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(null);

        List<TpsContact> tpsContacts = tpsCheckService.buildContactList(contactList);
        assertEquals(tpsContacts.get(0).getContact().getContactNumber(), contactList.get(0).getContactNumber());
        assertEquals(tpsContacts.get(0).getContactNumberStyle(), "text-muted");
        assertEquals(tpsContacts.get(0).getMobileNumberStyle(), "text-muted");
    }

    @Test
    public void shouldReturnTpsChecksForContactsContactNumberWithSafeTps() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contactWithContactNumber());

        TpsCheck tpsCheck = tpsCheck(contactWithContactNumber().getContactNumber(), "safe", 200);
        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);

        List<TpsContact> tpsContacts = tpsCheckService.buildContactList(contactList);
        assertEquals(tpsContacts.get(0).getContact().getContactNumber(), contactList.get(0).getContactNumber());
        assertEquals(tpsContacts.get(0).getContactNumberStyle(), "success");
        assertEquals(tpsContacts.get(0).getMobileNumberStyle(), "text-muted");
    }

    @Test
    public void shouldReturnTpsChecksForContactsMobileNumberWithSafeTps() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contactWithMobileNumber());

        TpsCheck tpsCheck = tpsCheck(contactWithMobileNumber().getMobileNumber(), "safe", 200);
        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);

        List<TpsContact> tpsContacts = tpsCheckService.buildContactList(contactList);
        assertEquals(tpsContacts.get(0).getContact().getContactNumber(), contactList.get(0).getContactNumber());
        assertEquals(tpsContacts.get(0).getContactNumberStyle(), "text-muted");
        assertEquals(tpsContacts.get(0).getMobileNumberStyle(), "success");
    }

    @Test
    public void shouldReturnTpsChecksForContactsContactNumberWithListedTps() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contactWithContactNumber());

        TpsCheck tpsCheck = tpsCheck(contactWithContactNumber().getContactNumber(), "listed", 200);
        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);

        List<TpsContact> tpsContacts = tpsCheckService.buildContactList(contactList);
        assertEquals(tpsContacts.get(0).getContact().getContactNumber(), contactList.get(0).getContactNumber());
        assertEquals(tpsContacts.get(0).getContactNumberStyle(), "danger");
        assertEquals(tpsContacts.get(0).getMobileNumberStyle(), "text-muted");
    }

    @Test
    public void shouldReturnTpsChecksForContactsMobileNumberWithListedTps() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contactWithMobileNumber());

        TpsCheck tpsCheck = tpsCheck(contactWithMobileNumber().getMobileNumber(), "listed", 200);
        when(mockTpsCheckRepository.findLatestTpsCheck(any(String.class))).thenReturn(tpsCheck);

        List<TpsContact> tpsContacts = tpsCheckService.buildContactList(contactList);
        assertEquals(tpsContacts.get(0).getContact().getContactNumber(), contactList.get(0).getContactNumber());
        assertEquals(tpsContacts.get(0).getContactNumberStyle(), "text-muted");
        assertEquals(tpsContacts.get(0).getMobileNumberStyle(), "danger");
    }

    private TpsCheck tpsCheck(String number, String message, Integer statusCode) {
        TpsCheck tpsCheck = new TpsCheck();
        tpsCheck.setId(1l);
        tpsCheck.setMessage(message);
        tpsCheck.setNumber(number);
        tpsCheck.setStatusCode(statusCode);
        tpsCheck.setDateCreated(new Date());

        return tpsCheck;
    }

    private Customer customerWithMobileNumber() {
        Customer customer = new Customer();
        customer.setId(54l);
        customer.setCustomerReference("CUC100100");
        customer.setBusinessName("RandomBusiness");
        customer.setMobileNumber("07987123456");

        return customer;
    }

    private Customer customerWithContactNumber() {
        Customer customer = new Customer();
        customer.setId(54l);
        customer.setCustomerReference("CUC100100");
        customer.setBusinessName("RandomBusiness");
        customer.setContactNumber("01274321456");

        return customer;
    }

    private Customer customerWithBothNumbers() {
        Customer customer = new Customer();
        customer.setId(54l);
        customer.setCustomerReference("CUC100100");
        customer.setBusinessName("RandomBusiness");
        customer.setMobileNumber("07987123456");
        customer.setContactNumber("01274321456");

        return customer;
    }

    private Contact contactWithMobileNumber() {
        Contact contact = new Contact();
        contact.setCustomer(customerWithMobileNumber());
        contact.setMobileNumber("07987123456");

        return contact;
    }

    private Contact contactWithContactNumber() {
        Contact contact = new Contact();
        contact.setCustomer(customerWithContactNumber());
        contact.setContactNumber("01274321456");

        return contact;
    }

    private Contact contactWithBothNumbers() {
        Contact contact = new Contact();
        contact.setCustomer(customerWithBothNumbers());
        contact.setMobileNumber("07987123456");
        contact.setContactNumber("01274321456");

        return contact;
    }
}
