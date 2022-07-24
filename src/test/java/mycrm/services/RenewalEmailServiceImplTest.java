package mycrm.services;

import mycrm.configuration.EmailProcess;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.EmailHistory;
import mycrm.models.GasCustomerContract;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RenewalEmailServiceImplTest {

    @InjectMocks
    private RenewalEmailServiceImpl renewalEmailService;

    @Mock
    private GasContractService gasContractService;

    @Mock
    private ElecContractService elecContractService;

    @Mock
    private EmailTemplateService emailTemplateService;

    @Mock
    private EmailHistoryService emailHistoryService;

    @Mock
    private CustomerDataService customerDataService;

    @Test
    public void shouldNotSendRenewalEmailsWhenNoRenewalsDue() {
        when(gasContractService.findAllRenewableContracts()).thenReturn(new ArrayList<>());
        when(elecContractService.findAllRenewableContracts()).thenReturn(new ArrayList<>());

        renewalEmailService.sendDailyRenewalEmails(EmailProcess.AUTOMATED.getName());

        verify(customerDataService, times(0)).getCustomerEmail(any(Customer.class));
        verify(emailTemplateService, times(0)).sendRenewalEmail(any(String.class), any(String.class), any(String.class));
//        verify(emailHistoryService, times(0)).save(any(EmailHistory.class));

    }

    @Test
    public void shouldNotSendGasRenewalEmailsWhenGasRenewalsDueButNoEmails() {

        List<GasCustomerContract> gasRenewals = new ArrayList<>();
        gasRenewals.add(aGasContract());

        when(gasContractService.findAllRenewableContracts()).thenReturn(gasRenewals);
        when(elecContractService.findAllRenewableContracts()).thenReturn(new ArrayList<>());
        when(customerDataService.getCustomerEmail(any(Customer.class))).thenReturn(new ArrayList<>());

        renewalEmailService.sendDailyRenewalEmails(EmailProcess.AUTOMATED.getName());

        verify(customerDataService, times(1)).getCustomerEmail(any(Customer.class));
        verify(emailTemplateService, times(0)).sendRenewalEmail(any(String.class), any(String.class), any(String.class));
        //verify(emailHistoryService, times(0)).save(any(EmailHistory.class));

    }

    @Ignore
    @Test
    public void shouldNotSendGasRenewalEmailsWhenGasRenewalsDueButEmailsFound() {

        List<GasCustomerContract> gasRenewals = new ArrayList<>();
        gasRenewals.add(aGasContract());

        List<String> customerEmails = new ArrayList<>();
        customerEmails.add("blah@test.com");

        when(gasContractService.findAllRenewableContracts()).thenReturn(gasRenewals);
        when(elecContractService.findAllRenewableContracts()).thenReturn(new ArrayList<>());
        when(customerDataService.getCustomerEmail(any(Customer.class))).thenReturn(customerEmails);

//        when(emailHistoryService.checkGasRenewalEmailSentAutomatically(any(String.class),
//                any(String.class),
//                any(GasCustomerContract.class))).thenReturn(emailHistoryList());

        renewalEmailService.sendDailyRenewalEmails(EmailProcess.AUTOMATED.getName());

        verify(customerDataService, times(1)).getCustomerEmail(any(Customer.class));
        verify(emailTemplateService, times(0)).sendRenewalEmail(any(String.class), any(String.class), any(String.class));
//        verify(emailHistoryService, times(0)).save(any(EmailHistory.class));

    }

    private List<EmailHistory> emailHistoryList() {
        EmailHistory emailHistory = new EmailHistory();
        emailHistory.setId(1l);

        return Arrays.asList(emailHistory);
    }

    private GasCustomerContract aGasContract() {
        GasCustomerContract contract = new GasCustomerContract();
        contract.setId(1l);
        contract.setLogType("LIVE");
        contract.setCustomer(aCustomer());
        contract.setCustomerSite(aCustomerSite());

        return contract;
    }

    private CustomerSite aCustomerSite() {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(1l);
        customerSite.setSitePostcodeIn("LS1");
        return customerSite;
    }

    private Customer aCustomer() {
        Customer customer = new Customer();
        customer.setId(2l);
        customer.setCustomerReference("CUC12345678");

        return customer;
    }
}
