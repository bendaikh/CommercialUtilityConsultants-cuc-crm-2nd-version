package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.configuration.UtilityType;
import mycrm.models.Broker;
import mycrm.models.BrokerContractPackage;
import mycrm.models.BrokerElectricContract;
import mycrm.models.BrokerGasContract;
import mycrm.models.BrokerMerchantServicesContract;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.BrokerUtilityContract;
import mycrm.models.Contract;
import mycrm.models.Customer;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.MerchantServicesContract;
import mycrm.models.UtilityContract;
import mycrm.repositories.BrokerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
public class BrokerServiceJpaImpl implements BrokerService {

    private static final Logger logger = LogManager.getLogger();

    private final GasContractService gasContractService;
    private final ElecContractService electricContractService;
    @Autowired
    private BrokerRepository brokerRepo;
    private final UtilityContractService utilityContractService;
    private final MerchantServicesService merchantServicesService;

    @Autowired
    public BrokerServiceJpaImpl(GasContractService gasContractService,
                                ElecContractService electricContractService,
                                UtilityContractService utilityContractService,
                                MerchantServicesService merchantServicesService) {
        this.gasContractService = gasContractService;
        this.electricContractService = electricContractService;
        this.utilityContractService = utilityContractService;
        this.merchantServicesService = merchantServicesService;
    }

    @Override
    public List<Broker> findAll() {
        return this.brokerRepo.findAll();
    }

    @Override
    public List<Broker> findLatest5() {
        return null;// this.brokerRepo.findLatest5Posts(new PageRequest(0,5));
    }

    @Override
    public Broker findById(Long id) {
        return this.brokerRepo.findOne(id);
    }

    @Override
    public Broker save(Broker broker) {
        return this.brokerRepo.save(broker);
    }

    @Override
    public Broker edit(Broker broker) {
        return this.brokerRepo.save(broker);
    }

    @Override
    public void deleteById(Long id) {
        this.brokerRepo.delete(id);
    }

    @Override
    public List<Customer> findCustomersByBroker(Broker broker) {
        Set<Customer> customerSet = new HashSet<>();

        List<GasCustomerContract> findGasByBroker = gasContractService.findByBroker(broker);
        findGasByBroker.sort(Comparator.comparing(Contract::getDateCreated));
        List<ElecCustomerContract> findElecByBroker = electricContractService.findByBroker(broker);
        findElecByBroker.sort(Comparator.comparing(Contract::getDateCreated));

        customerSet.addAll(findGasByBroker.stream().map(Contract::getCustomer).collect(Collectors.toSet()));
        customerSet.addAll(findElecByBroker.stream().map(Contract::getCustomer).collect(Collectors.toSet()));

        //for ordering purposes flip back to a list - a set takes out duplicates
        List<Customer> customerList = new ArrayList<>(customerSet);
        customerList.sort(Comparator.comparing(Customer::getBusinessName));

        return customerList;
    }

    @Override
    public BrokerContractPackage findBrokerContracts(Broker broker) {
        return BrokerContractPackage
                .builder()
                .gasContracts(buildGasContracts(broker))
                .electricityContracts(buildElectricContracts(broker))
                .solarContracts(buildUtilityContracts(broker, UtilityType.SOLAR))
                .waterContracts(buildUtilityContracts(broker, UtilityType.WATER))
                .voipContracts(buildUtilityContracts(broker, UtilityType.VOIP))
                .landlineContracts(buildUtilityContracts(broker, UtilityType.LANDLINE))
                .mobileContracts(buildUtilityContracts(broker, UtilityType.MOBILE))
                .broadbandContracts(buildUtilityContracts(broker, UtilityType.BROADBAND))
                .merchantServicesContracts(buildMerchantServicesContracts(broker))
                .build();
    }

    @Override
    public BrokerContractPackage findBrokerContractStats(Broker broker) {
        return BrokerContractPackage
                .builder()
                .gasContracts(buildGasContractStats(broker))
                .electricityContracts(buildElectricContractStats(broker))
                .solarContracts(buildUtilityContractStats(broker, UtilityType.SOLAR))
                .waterContracts(buildUtilityContractStats(broker, UtilityType.WATER))
                .voipContracts(buildUtilityContractStats(broker, UtilityType.VOIP))
                .landlineContracts(buildUtilityContractStats(broker, UtilityType.LANDLINE))
                .mobileContracts(buildUtilityContractStats(broker, UtilityType.MOBILE))
                .broadbandContracts(buildUtilityContractStats(broker, UtilityType.BROADBAND))
                .merchantServicesContracts(buildMerchantServicesContractStats(broker))
                .build();
    }

    @Override
    public boolean transferBrokerContracts(BrokerTransferUpdate brokerTransferUpdate) {

        if (isGasContract(brokerTransferUpdate.getSupplyType())) {
            return transferGasContracts(brokerTransferUpdate);
        } else if (isElectricContract(brokerTransferUpdate.getSupplyType())) {
            return transferElectricContracts(brokerTransferUpdate);
        } else if (isValidUtility(brokerTransferUpdate.getSupplyType())) {
            return transferUtilityContracts(brokerTransferUpdate);
        } else if (isMerchantServicesContract(brokerTransferUpdate.getSupplyType())) {
            return transferMerchantServicesContracts(brokerTransferUpdate);
        }

        return false;
    }

    private boolean transferGasContracts(BrokerTransferUpdate brokerTransferUpdate) {
        boolean successful = false;

        try {
            gasContractService.transferBrokerContracts(
                    getGasContracts(brokerTransferUpdate.getPreviousBroker(), brokerTransferUpdate.getLogType()),
                    brokerTransferUpdate);
            successful = true;
        } catch (Exception e) {
            logger.info("Error when transferring gas contracts {}", e.getMessage());
        }

        return successful;
    }

    private boolean transferElectricContracts(BrokerTransferUpdate brokerTransferUpdate) {
        boolean successful = false;
        try {
            electricContractService.transferBrokerContracts(
                    getElectricContracts(brokerTransferUpdate.getPreviousBroker(), brokerTransferUpdate.getLogType()),
                    brokerTransferUpdate);
            successful = true;
        } catch (Exception e) {
            logger.info("Error when transferring electricity contracts {}", e.getMessage());
        }

        return successful;
    }

    private boolean transferUtilityContracts(BrokerTransferUpdate brokerTransferUpdate) {
        boolean successful = false;
        try {
            utilityContractService.transferBrokerContracts(
                    getUtilityContracts(
                            brokerTransferUpdate.getPreviousBroker(),
                            brokerTransferUpdate.getLogType(),
                            getUtilityTypeFromSupplyType(brokerTransferUpdate.getSupplyType())), brokerTransferUpdate);
            successful = true;
        } catch (Exception e) {
            logger.info("Error when transferring {} contracts {}",
                    brokerTransferUpdate.getSupplyType(),
                    e.getMessage()
            );
        }

        return successful;
    }

    private boolean transferMerchantServicesContracts(BrokerTransferUpdate brokerTransferUpdate) {
        boolean successful = false;
        try {
            merchantServicesService.transferBrokerContracts(
                    getMerchantServicesContracts(
                            brokerTransferUpdate.getPreviousBroker(),
                            brokerTransferUpdate.getLogType()), brokerTransferUpdate);
            successful = true;
        } catch (Exception e) {
            logger.info("Error when transferring {} contracts {}",
                    brokerTransferUpdate.getSupplyType(),
                    e.getMessage()
            );
        }

        return successful;
    }

    private UtilityType getUtilityTypeFromSupplyType(String supplyType) {
        for (UtilityType u : UtilityType.values()) {
            if (u.name().equals(supplyType)) {
                return u;
            }
        }
        return null;
    }

    private boolean isValidUtility(String utilityType) {
        for (UtilityType u : UtilityType.values()) {
            if (u.name().equals(utilityType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isGasContract(String supplyType) {
        return supplyType.equals("GAS") || supplyType.equals("gas");
    }

    private boolean isElectricContract(String supplyType) {
        return supplyType.equals("ELEC") || supplyType.equals("elec");
    }

    private boolean isMerchantServicesContract(String supplyType) {
        return supplyType.equals("MERCHANT_SERVICES");
    }

    private List<BrokerGasContract> buildGasContracts(Broker broker) {
        List<BrokerGasContract> brokerGasContracts = new ArrayList<>();
        // loop through all log types
        for (LogType logType : LogType.values()) {
            List<GasCustomerContract> gasContracts = getGasContracts(broker, logType);
            brokerGasContracts.add(BrokerGasContract
                    .builder()
                    .contracts(gasContracts)
                    .logType(logType)
                    .supplyType("GAS")
                    .build());
        }
        return brokerGasContracts;
    }

    private List<BrokerElectricContract> buildElectricContracts(Broker broker) {
        List<BrokerElectricContract> brokerElectricContracts = new ArrayList<>();
        // loop through all log types
        for (LogType logType : LogType.values()) {
            List<ElecCustomerContract> electricContracts = getElectricContracts(broker, logType);
            brokerElectricContracts.add(BrokerElectricContract
                    .builder()
                    .contracts(electricContracts)
                    .logType(logType)
                    .supplyType("ELEC")
                    .build());
        }
        return brokerElectricContracts;
    }

    private List<BrokerMerchantServicesContract> buildMerchantServicesContracts(Broker broker) {
        List<BrokerMerchantServicesContract> brokerMerchantServicesContracts = new ArrayList<>();
        // loop through all log types
        for (LogType logType : LogType.values()) {
            List<MerchantServicesContract> merchantServicesContracts = getMerchantServicesContracts(broker, logType);
            brokerMerchantServicesContracts.add(BrokerMerchantServicesContract
                    .builder()
                    .contracts(merchantServicesContracts)
                    .logType(logType)
                    .supplyType("MERCHANT_SERVICES")
                    .build());
        }
        return brokerMerchantServicesContracts;
    }

    private List<BrokerUtilityContract> buildUtilityContracts(Broker broker, UtilityType utilityType) {
        List<BrokerUtilityContract> brokerUtilityContracts = new ArrayList<>();
        // loop through all log types
        for (LogType logType : LogType.values()) {
            List<UtilityContract> utilityContracts = getUtilityContracts(broker, logType, utilityType);
            brokerUtilityContracts.add(BrokerUtilityContract
                    .builder()
                    .contracts(utilityContracts)
                    .logType(logType)
                    .supplyType(utilityType.toString())
                    .build());
        }
        return brokerUtilityContracts;
    }

    private List<BrokerGasContract> buildGasContractStats(Broker broker) {
        List<BrokerGasContract> brokerGasContracts = new ArrayList<>();
        // loop through all log types
        for (LogType logType : LogType.values()) {

            brokerGasContracts.add(BrokerGasContract
                    .builder()
                    .numberOfContracts(getGasContractsCount(broker, logType))
                    .logType(logType)
                    .supplyType("GAS")
                    .build());
        }
        return brokerGasContracts;
    }

    private List<BrokerElectricContract> buildElectricContractStats(Broker broker) {
        List<BrokerElectricContract> brokerElectricContracts = new ArrayList<>();
        // loop through all log types
        for (LogType logType : LogType.values()) {
            brokerElectricContracts.add(BrokerElectricContract
                    .builder()
                    .numberOfContracts(getElectricContractsCount(broker, logType))
                    .logType(logType)
                    .supplyType("ELEC")
                    .build());
        }
        return brokerElectricContracts;
    }

    private List<BrokerMerchantServicesContract> buildMerchantServicesContractStats(Broker broker) {
        List<BrokerMerchantServicesContract> merchantServicesContracts = new ArrayList<>();
        // loop through all log types
        for (LogType logType : LogType.values()) {

            merchantServicesContracts.add(BrokerMerchantServicesContract
                    .builder()
                    .numberOfContracts(getMerchantServicesContractsCount(broker, logType))
                    .logType(logType)
                    .supplyType("MERCHANT_SERVICES")
                    .build());
        }
        return merchantServicesContracts;
    }

    private List<BrokerUtilityContract> buildUtilityContractStats(Broker broker, UtilityType utilityType) {
        List<BrokerUtilityContract> brokerUtilityContracts = new ArrayList<>();
        // loop through all log types
        for (LogType logType : LogType.values()) {
            brokerUtilityContracts.add(BrokerUtilityContract
                    .builder()
                    .numberOfContracts(getUtilityContractsCount(broker, logType, utilityType))
                    .logType(logType)
                    .supplyType(utilityType.toString())
                    .build());
        }
        return brokerUtilityContracts;
    }

    private List<GasCustomerContract> getGasContracts(Broker broker, LogType logType) {
        return this.gasContractService.findByBrokerAndLogType(broker, logType);
    }

    private List<ElecCustomerContract> getElectricContracts(Broker broker, LogType logType) {
        return this.electricContractService.findByBrokerAndLogType(broker, logType);
    }

    private List<UtilityContract> getUtilityContracts(Broker broker, LogType logType, UtilityType utilityType) {
        return this.utilityContractService.findByBrokerAndLogTypeAndUtilityType(broker, logType, utilityType);
    }

    private List<MerchantServicesContract> getMerchantServicesContracts(Broker broker, LogType logType) {
        return this.merchantServicesService.findByBrokerAndLogTypeAndUtilityType(broker, logType);
    }

    private int getGasContractsCount(Broker broker, LogType logType) {
        return this.gasContractService.countByBrokerAndLogType(broker, logType);
    }

    private int getElectricContractsCount(Broker broker, LogType logType) {
        return this.electricContractService.countByBrokerAndLogType(broker, logType);
    }

    private int getUtilityContractsCount(Broker broker, LogType logType, UtilityType utilityType) {
        return this.utilityContractService.countByBrokerAndLogTypeAndUtilityType(broker, logType, utilityType);
    }

    private int getMerchantServicesContractsCount(Broker broker, LogType logType) {
        return this.merchantServicesService.countByBrokerAndLogType(broker, logType);
    }

}
