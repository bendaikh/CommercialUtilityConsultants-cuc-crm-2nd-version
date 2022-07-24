package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.Supplier;
import mycrm.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Primary
public class NewCustomerServiceImpl implements NewCustomerService {

    @Value("${default.broker.id}")
    private Long defaultBrokerId;

    @Value("${default.supplier.id}")
    private Long defaultSupplierId;

    private final SupplierService supplierService;
    private final CustomerSiteService customerSiteService;
    private final BrokerService brokerService;
    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final CustomerService customerService;
    private final UserService userService;

    @Autowired
    public NewCustomerServiceImpl(SupplierService supplierService, CustomerSiteService customerSiteService,
                                  BrokerService brokerService, GasContractService gasContractService, ElecContractService elecContractService,
                                  CustomerService customerService, UserService userService) {
        this.supplierService = supplierService;
        this.customerSiteService = customerSiteService;
        this.brokerService = brokerService;
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.customerService = customerService;
        this.userService = userService;
    }

    @Override
    public void createNewCustomerLead(Customer customer, Boolean createGas, Boolean createElectricity) {

        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        updateCustomerReferenceNumber(customer);

        CustomerSite customerSite = new CustomerSite();
        customerSite.setCustomer(customer);
        customerSite.setSiteName(customer.getBusinessName());
        customerSite.setSiteAddr(customer.getBusinessAddr());
        customerSite.setSiteAddr1(customer.getBusinessAddr1());
        customerSite.setSiteCity(customer.getBusinessCity());
        customerSite.setSitePostcodeIn(customer.getBusinessPostcodeIn());
        customerSite.setSitePostcodeOut(customer.getBusinessPostcodeOut());
        customerSite.setSiteTelephone(customer.getContactNumber());
        customerSite.setMpr("0");
        customerSite.setMpanLineOne("0");
        customerSite.setMpanLineTwo("0");
        customerSite.setMpanLineThree("0");
        customerSite.setMpanBottomLine("0");
        customerSite.setPropertyType("UNKNOWN");

        CustomerSite generatedCustomerSite = customerSiteService.save(customerSite);

        Supplier supplier = supplierService.findById(defaultSupplierId);
        Broker broker = brokerService.findById(defaultBrokerId);

        if (user.isBroker()) {
            broker = user.getBroker();
        }

        if (createGas) {
            GasCustomerContract gasCustomerContract = new GasCustomerContract();
            gasCustomerContract.setAccountNumber("Pending");
            gasCustomerContract.setCustomer(customer);
            gasCustomerContract.setCustomerSite(generatedCustomerSite);
            gasCustomerContract.setLogType("POTENTIAL");
            gasCustomerContract.setSupplier(supplier);
            gasCustomerContract.setBroker(broker);
            gasCustomerContract.setContractReason(null);
            gasContractService.save(gasCustomerContract);
        }

        if (createElectricity) {
            ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
            elecCustomerContract.setAccountNumber("Pending");
            elecCustomerContract.setCustomer(customer);
            elecCustomerContract.setCustomerSite(generatedCustomerSite);
            elecCustomerContract.setLogType("POTENTIAL");
            elecCustomerContract.setSupplier(supplier);
            elecCustomerContract.setBroker(broker);
            elecCustomerContract.setContractReason(null);
            elecCustomerContract.setMpanLineOne("0");
            elecCustomerContract.setMpanLineTwo("0");
            elecCustomerContract.setMpanLineThree("0");
            elecContractService.save(elecCustomerContract);
        }

    }

    @Override
    public void updateCustomerReferenceNumber(Customer customer) {
        Customer updateCustomer = customerService.findById(customer.getId());
        updateCustomer.setCustomerReference("CUC" + updateCustomer.getId());

        customerService.save(updateCustomer);
    }

}
