package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.CustomerSiteTransferHistory;
import mycrm.models.CustomerSiteWithContracts;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.MerchantServicesContract;
import mycrm.models.UtilityContract;
import mycrm.repositories.CustomerSiteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Primary
public class CustomerSiteServiceJpaImpl implements CustomerSiteService {

    private static Logger logger = LogManager.getLogger();

    private final CustomerSiteRepository sitesRepo;
    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final UtilityContractService utilityContractService;
    private final CustomerSiteTransferHistoryService customerSiteTransferHistoryService;
    private final MerchantServicesService merchantServicesService;

    @Autowired
    public CustomerSiteServiceJpaImpl(CustomerSiteRepository sitesRepo,
                                      GasContractService gasContractService,
                                      ElecContractService elecContractService,
                                      UtilityContractService utilityContractService,
                                      CustomerSiteTransferHistoryService customerSiteTransferHistoryService,
                                      MerchantServicesService merchantServicesService) {
        this.sitesRepo = sitesRepo;
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.utilityContractService = utilityContractService;
        this.customerSiteTransferHistoryService = customerSiteTransferHistoryService;
        this.merchantServicesService = merchantServicesService;
    }

    @Override
    public List<CustomerSite> findAll() {
        return this.sitesRepo.findAll();
    }

    @Override
    public List<CustomerSite> findLatest5() {
        return this.sitesRepo.findLatest5Posts(new PageRequest(0, 5));
    }

    @Override
    public CustomerSite findById(Long id) {
        return this.sitesRepo.findOne(id);
    }

    @Override
    public CustomerSite save(CustomerSite customerSite) {
        return this.sitesRepo.save(customerSite);
    }

    @Override
    public List<CustomerSiteWithContracts> customerSitesWithContracts(Customer customer) {

        List<CustomerSiteWithContracts> customerSitesWithContracts = new ArrayList<>();

        List<CustomerSite> customerSites = this.findByCustomer(customer);

        customerSites.forEach(customerSite -> {
            CustomerSiteWithContracts siteWithContracts = new CustomerSiteWithContracts();
            siteWithContracts.setSite(customerSite);

            List<GasCustomerContract> gasContracts =
                    gasContractService.findLatestGasCustomerContractBySiteOrderedByLiveFirst(customerSite.getId());
            gasContracts.sort(Comparator.comparing(GasCustomerContract::isCurrent).reversed());

            List<ElecCustomerContract> electricContracts =
                    elecContractService.findLatestElecCustomerContractBySiteOrderedByLiveFirst(customerSite.getId());
            electricContracts.sort(Comparator.comparing(ElecCustomerContract::isCurrent).reversed());

            List<UtilityContract> solarContracts = utilityContractService.findSolarContractByCustomerSite(customerSite);
            List<UtilityContract> waterContracts = utilityContractService.findWaterContractByCustomerSite(customerSite);
            List<UtilityContract> voipContracts = utilityContractService.findVoipContractByCustomerSite(customerSite);
            List<UtilityContract> landlineContracts = utilityContractService.findLandlineContractByCustomerSite(customerSite);
            List<UtilityContract> mobileContracts = utilityContractService.findMobileContractByCustomerSite(customerSite);
            List<UtilityContract> broadbandContracts = utilityContractService.findBroadbandContractByCustomerSite(customerSite);

            List<MerchantServicesContract> merchantServicesContracts = merchantServicesService.findMerchantServicesContractByCustomerSite(customerSite);

            if (gasContracts.size() > 0) {
                siteWithContracts.setGasContract(gasContracts.get(0));
            }

            if (electricContracts.size() > 0) {
                siteWithContracts.setElectricContract(electricContracts.get(0));
            }

            if (solarContracts.size() > 0) {
                siteWithContracts.setSolarContract(solarContracts.get(0));
            }

            if (waterContracts.size() > 0) {
                siteWithContracts.setWaterContract(waterContracts.get(0));
            }

            if (voipContracts.size() > 0) {
                siteWithContracts.setVoipContract(voipContracts.get(0));
            }

            if (landlineContracts.size() > 0) {
                siteWithContracts.setLandlineContract(landlineContracts.get(0));
            }

            if (mobileContracts.size() > 0) {
                siteWithContracts.setMobileContract(mobileContracts.get(0));
            }

            if (broadbandContracts.size() > 0) {
                siteWithContracts.setBroadbandContract(broadbandContracts.get(0));
            }

            if (merchantServicesContracts.size() > 0) {
                siteWithContracts.setMerchantServicesContract(merchantServicesContracts.get(0));
            }

            customerSitesWithContracts.add(siteWithContracts);
        });
        return customerSitesWithContracts;
    }

    @Override
    public void deleteById(Long id) {
        this.sitesRepo.delete(id);
    }

    @Override
    public void deleteByCustomer(Customer customer) {
        this.sitesRepo.deleteByCustomer(customer);
    }

    @Override
    public CustomerSite transferCustomerSite(CustomerSite customerSite, Customer selectedCustomer) {
        Customer previousCustomer = customerSite.getCustomer();

        try {
            // find and update gas contracts
            List<GasCustomerContract> gasCustomerContracts = gasContractService.findByCustomerSite(customerSite);
            gasCustomerContracts.forEach(gasCustomerContract -> {
                gasCustomerContract.setCustomer(selectedCustomer);
                gasContractService.save(gasCustomerContract);
            });

            // find and update electric contracts
            List<ElecCustomerContract> elecCustomerContracts = elecContractService.findByCustomerSite(customerSite);
            elecCustomerContracts.forEach(elecCustomerContract -> {
                elecCustomerContract.setCustomer(selectedCustomer);
                elecContractService.save(elecCustomerContract);
            });

            // update selectedCustomer site
            customerSite.setCustomer(selectedCustomer);
            CustomerSite updatedCustomerSite = save(customerSite);

            // finally save the history
            customerSiteTransferHistoryService.save(CustomerSiteTransferHistory
                    .builder()
                    .customerId(selectedCustomer.getId())
                    .customerSiteId(customerSite.getId())
                    .previousCustomerId(previousCustomer.getId())
                    .previousBusinessName(previousCustomer.getBusinessName())
                    .build());

            return updatedCustomerSite;
        } catch (Exception e) {
            logger.info("Unable to transfer site {}", e.getMessage());
            return customerSite;
        }
    }

    @Override
    public List<CustomerSite> findByCustomer(Customer customer) {
        return this.sitesRepo.findByCustomer(customer);
    }

}
