package mycrm.views.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.GasCustomerContract;
import mycrm.models.Supplier;
import mycrm.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class GasWelcomeLetterTest {

    @InjectMocks
    private GasWelcomeLetter gasWelcomeLetter;

    @Mock
    private PdfWriter writer;

    @Mock
    private Document document;

    @Test
    public void shouldReturnGasPdfWithCustomerFirstnameWithNoRates() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        GasCustomerContract gasCustomerContract = aGasCustomerContractWithoutRates();

        Map<String, Object> model = new HashMap<>();
        model.put("gasCustomerContract", gasCustomerContract);

        gasWelcomeLetter.buildPdfDocument(model, document, writer, request, response);
    }
    
    @Test
    public void shouldReturnGasPdfWithCustomerFirstnameWithRates() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        GasCustomerContract gasCustomerContract = aGasCustomerContractWithRates();

        Map<String, Object> model = new HashMap<>();
        model.put("gasCustomerContract", gasCustomerContract);

        gasWelcomeLetter.buildPdfDocument(model, document, writer, request, response);
    }
    
    @Test
    public void shouldReturnGasPdfWithCustomerBusinessNameWithRates() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        GasCustomerContract gasCustomerContract = aGasCustomerContractWithRatesOnBusinessName();

        Map<String, Object> model = new HashMap<>();
        model.put("gasCustomerContract", gasCustomerContract);

        gasWelcomeLetter.buildPdfDocument(model, document, writer, request, response);
    }
    

    private GasCustomerContract aGasCustomerContractWithoutRates() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setAccountNumber("123456");
        gasCustomerContract.setCustomer(aCustomer());
        gasCustomerContract.setCustomerSite(aCustomerSite());
        gasCustomerContract.setCreatedBy(aCreatedBy());
        gasCustomerContract.setSupplier(aSupplier());
        gasCustomerContract.setStartDate(aDate());
        gasCustomerContract.setBroker(aBroker());

        return gasCustomerContract;
    }
    
    private GasCustomerContract aGasCustomerContractWithRates() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setAccountNumber("123456");
        gasCustomerContract.setCustomer(aCustomer());
        gasCustomerContract.setCustomerSite(aCustomerSite());
        gasCustomerContract.setCreatedBy(aCreatedBy());
        gasCustomerContract.setSupplier(aSupplier());
        gasCustomerContract.setStartDate(aDate());
        gasCustomerContract.setStandingCharge(25.1);
        gasCustomerContract.setUnitRate(71.1);
        gasCustomerContract.setBroker(aBroker());

        return gasCustomerContract;
    }
    
    private GasCustomerContract aGasCustomerContractWithRatesOnBusinessName() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setAccountNumber("123456");
        gasCustomerContract.setCustomer(aBusinessCustomer());
        gasCustomerContract.setCustomerSite(aCustomerSite());
        gasCustomerContract.setCreatedBy(aCreatedBy());
        gasCustomerContract.setSupplier(aSupplier());
        gasCustomerContract.setStartDate(aDate());
        gasCustomerContract.setStandingCharge(25.1);
        gasCustomerContract.setUnitRate(71.1);
        gasCustomerContract.setBroker(aBroker());

        return gasCustomerContract;
    }

    private Date aDate() {
        return new Date();
    }

    private User aCreatedBy() {
        User user = new User();
        user.setFirstName("John");
        return user;
    }

    private CustomerSite aCustomerSite() {
        CustomerSite customerSite = new CustomerSite();
        customerSite.setId(Long.valueOf(5));
        customerSite.setVersion(0);
        customerSite.setCustomer(aCustomer());
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
        return customerSite;
    }

    private Customer aCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Imran");
        customer.setLastName("Miskeen");
        customer.setBusinessName("CEC");
        customer.setId(Long.valueOf(5));
        customer.setVersion(1);
        customer.setCustomerReference("Customer Reference");
        customer.setBusinessAddr("Addr 1");
        customer.setBusinessAddr1("Addr 2");
        customer.setBusinessCity("City");
        customer.setBusinessPostcodeIn("BB1");
        customer.setBusinessPostcodeOut("1AA");
        customer.setLtdRegNo("123456");
        customer.setSoleTrader(true);
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
        return customer;
    }
    
    private Customer aBusinessCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("");
        customer.setBusinessName("CEC");
        customer.setId(Long.valueOf(5));
        customer.setVersion(1);
        customer.setCustomerReference("Customer Reference");
        customer.setBusinessAddr("Addr 1");
        customer.setBusinessAddr1("Addr 2");
        customer.setBusinessCity("City");
        customer.setBusinessPostcodeIn("BB1");
        customer.setBusinessPostcodeOut("1AA");
        customer.setLtdRegNo("123456");
        customer.setSoleTrader(false);
        customer.setContactNumber("01274 987456");
        customer.setMobileNumber("0741254785");
        customer.setEmailAddress("imy@imy.com");
        customer.setWebAddress("www.webadress.com");
        customer.setDob(new Date());
        return customer;
    }
    
    private Supplier aSupplier() {
        Supplier supplier = new Supplier();
        supplier.setId(Long.valueOf(5));
        supplier.setVersion(1);
        supplier.setSupplierReference("reference");
        supplier.setBusinessName("businessname");
        supplier.setBusinessAddr("addr");
        supplier.setBusinessAddr1("addr1");
        supplier.setBusinessCity("city");
        supplier.setBusinessPostcodeIn("IN1");
        supplier.setBusinessPostcodeOut("OUT1");
        supplier.setFirstName("firstname");
        supplier.setLastName("lastname");
        supplier.setContactNumber("012374");
        supplier.setMobileNumber("07548");
        supplier.setEmailAddress("email@email.com");
        supplier.setDob("1984-01-01");
        return supplier;
    }
    
    private Broker aBroker() {
        Broker broker = new Broker();
        broker.setId(42l);
        broker.setFirstName("BrokerName");        
        return broker;        
    }
}
