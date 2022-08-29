package mycrm.services;

import mycrm.models.*;
import mycrm.repositories.ElecContractRepository;
import mycrm.repositories.GasContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Primary
public class AdminContractTaskServiceImpl implements AdminContractTaskService {

    private final ElecContractRepository elecContractRepository;
    private final GasContractRepository gasContractRepository;
    private final UtilityContractService utilityContractService;

    private final MerchantServicesService merchantServicesService;

    @Autowired
    public AdminContractTaskServiceImpl(ElecContractRepository elecContractRepository,
                                        GasContractRepository gasContractRepository,
                                        UtilityContractService utilityContractService,
                                        MerchantServicesService merchantServicesService) {
        this.elecContractRepository = elecContractRepository;
        this.gasContractRepository = gasContractRepository;
        this.utilityContractService = utilityContractService;
        this.merchantServicesService = merchantServicesService;
    }

    @Override
    public List<NewSaleTask> findAllAdminContractNewSalesTasks() {
        List<NewSaleTask> newSaleTasks = new ArrayList<>(mergeNewSalesTasks());
        //ORDER BY start_date
        newSaleTasks.sort(Comparator.comparing(NewSaleTask::getStartDate,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return newSaleTasks;
    }

    @Override
    public List<ProcessingTask> findAllContractsToProcess() {
        List<ProcessingTask> processingTasks = new ArrayList<>(mergeProcessingTasks());
        //ORDER BY start_date
        processingTasks.sort(Comparator.comparing(ProcessingTask::getStartDate,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return processingTasks;
    }

    private List<ProcessingTask> mergeProcessingTasks() {
        List<ProcessingTask> processingTasks = new ArrayList<>();
        processingTasks.addAll(processingTasksFromEnergyContracts());
        processingTasks.addAll(processingTasksFromUtilityContracts());
        return processingTasks;
    }

    private List<ProcessingTask> processingTasksFromEnergyContracts() {
        List<ProcessingTask> processingTasks = new ArrayList<>();
        energyContractsProcessingList().forEach(energyContract -> processingTasks.add(ProcessingTask
                .builder()
                .id(energyContract.getId())
                .supplyType(energyContract.getSupplyType())
                .customer(energyContract.getCustomer())
                .customerSite(energyContract.getCustomerSite())
                .startDate(energyContract.getStartDate())
                .endDate(energyContract.getEndDate())
                .contractSentToCustomer(energyContract.isContractSentToCustomer())
                .contractReceived(energyContract.isContractReceived())
                .supplier(energyContract.getSupplier())
                .broker(energyContract.getBroker())
                .dateCreated(energyContract.getDateCreated())
                .createdBy((User) energyContract.getCreatedBy())
                .accountNumber(energyContract.getAccountNumber())
                .build()));
        return processingTasks;
    }

    private List<ProcessingTask> processingTasksFromUtilityContracts() {
        List<ProcessingTask> processingTasks = new ArrayList<>();
        utilityContractsProcessingList().forEach(utilityContract -> processingTasks.add(ProcessingTask
                .builder()
                .id(utilityContract.getId())
                .supplyType(utilityContract.getUtilityType())
                .customer(utilityContract.getCustomerSite().getCustomer())
                .customerSite(utilityContract.getCustomerSite())
                .startDate(utilityContract.getStartDate())
                .endDate(utilityContract.getEndDate())
                .contractSentToCustomer(utilityContract.isContractSentToCustomer())
                .contractReceived(utilityContract.isContractReceived())
                .supplier(utilityContract.getSupplier())
                .broker(utilityContract.getBroker())
                .dateCreated(utilityContract.getDateCreated())
                .createdBy(utilityContract.getCreatedBy())
                .accountNumber(utilityContract.getAccountNumber())
                .build()));

        merchatContractsProcessingList().forEach(merchantContract -> processingTasks.add(ProcessingTask
                .builder()
                .id(merchantContract.getId())
                .supplyType(merchantContract.getSupplyType())
                .customer(merchantContract.getCustomerSite().getCustomer())
                .customerSite(merchantContract.getCustomerSite())
                .startDate(merchantContract.getStartDate())
                .endDate(merchantContract.getEndDate())
                .contractSentToCustomer(merchantContract.isContractSentToCustomer())
                .contractReceived(merchantContract.isContractReceived())
                .supplier(new Supplier())
                .broker(merchantContract.getBroker())
                .dateCreated(merchantContract.getDateCreated())
                .createdBy(merchantContract.getCreatedBy())
                .accountNumber(merchantContract.getVatNumber())
                .build()));
        return processingTasks;
    }

    private List<EnergyContract> energyContractsProcessingList() {
        List<EnergyContract> energyContracts = new ArrayList<>();
        energyContracts.addAll(elecContractRepository.findAllElectricAdminContractProcessingTasks());
        energyContracts.addAll(gasContractRepository.findAllGasAdminContractProcessingTasks());
        return energyContracts;
    }

    private List<UtilityContract> utilityContractsProcessingList() {
        return this.utilityContractService.findAllAdminContractProcessingTasks();
    }

    private List<MerchantServicesContract> merchatContractsProcessingList() {
        return this.merchantServicesService.findAllAdminContractProcessingTasks();
    }


    private List<NewSaleTask> mergeNewSalesTasks() {
        List<NewSaleTask> newSaleTasks = new ArrayList<>();
        newSaleTasks.addAll(newSaleTasksFromEnergyContracts());
        newSaleTasks.addAll(newSaleTasksFromMerchantServiceContracts());
        return newSaleTasks;
    }

    private List<NewSaleTask> newSaleTasksFromUtilityContracts() {
        List<NewSaleTask> newSaleTasks = new ArrayList<>();
        utilityContractNewSalesList().forEach(utilityContract -> newSaleTasks.add(NewSaleTask
                .builder()
                .id(utilityContract.getId())
                .customer(utilityContract.getCustomerSite().getCustomer())
                .customerSite(utilityContract.getCustomerSite())
                .startDate(utilityContract.getStartDate())
                .supplyType(utilityContract.getUtilityType())
                .welcomePackSentToCustomer(utilityContract.isWelcomePackSentToCustomer())
                .previousSupplyTerminated(utilityContract.isPreviousSupplyTerminated())
                .build()));
        return newSaleTasks;
    }
    private List<NewSaleTask> newSaleTasksFromMerchantServiceContracts() {
        List<NewSaleTask> newSaleTasks = new ArrayList<>();
        merchantServiceContractNewSalesList().forEach(merchantServiceContract -> newSaleTasks.add(NewSaleTask
                .builder()
                .id(merchantServiceContract.getId())
                .customer(merchantServiceContract.getCustomerSite().getCustomer())
                .customerSite(merchantServiceContract.getCustomerSite())
                .startDate(merchantServiceContract.getStartDate())
                .supplyType(merchantServiceContract.getSupplyType())
                .welcomePackSentToCustomer(merchantServiceContract.isWelcomePackSentToCustomer())
                .previousSupplyTerminated(merchantServiceContract.isPreviousSupplyTerminated())
                .build()));
        return newSaleTasks;
    }

    private List<NewSaleTask> newSaleTasksFromEnergyContracts() {
        List<NewSaleTask> newSaleTasks = new ArrayList<>();
        energyContractNewSalesList().forEach(energyContract -> newSaleTasks.add(NewSaleTask
                .builder()
                .id(energyContract.getId())
                .customer(energyContract.getCustomer())
                .customerSite(energyContract.getCustomerSite())
                .startDate(energyContract.getStartDate())
                .supplyType(energyContract.getSupplyType())
                .welcomePackSentToCustomer(energyContract.isWelcomePackSentToCustomer())
                .previousSupplyTerminated(energyContract.isPreviousSupplyTerminated())
                .build()));
        return newSaleTasks;
    }

    private List<EnergyContract> energyContractNewSalesList() {
        List<EnergyContract> energyContracts = new ArrayList<>();
        energyContracts.addAll(elecContractRepository.findAllElectricAdminContractNewSalesTasks());
        energyContracts.addAll(gasContractRepository.findAllGasAdminContractNewSalesTasks());
        return energyContracts;
    }

    private List<UtilityContract> utilityContractNewSalesList() {
        return this.utilityContractService.findAllAdminUtilityContractNewSalesTasks();
    }
    private List<MerchantServicesContract> merchantServiceContractNewSalesList() {
        return this.merchantServicesService.findAllAdminMerchantServiceContractNewSalesTasks();
    }

}
