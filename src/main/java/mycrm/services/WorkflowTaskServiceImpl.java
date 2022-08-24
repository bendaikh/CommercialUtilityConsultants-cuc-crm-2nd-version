package mycrm.services;

import mycrm.models.EnergyContract;
import mycrm.models.ObjectedTask;
import mycrm.models.User;
import mycrm.models.UtilityContract;
import mycrm.models.VerbalTask;
import mycrm.repositories.ElecContractRepository;
import mycrm.repositories.GasContractRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class WorkflowTaskServiceImpl implements WorkflowTaskService {
    private final GasContractRepository gasContractRepository;
    private final ElecContractRepository elecContractRepository;
    private final UtilityContractService utilityContractService;

    public WorkflowTaskServiceImpl(GasContractRepository gasContractRepository,
                                   ElecContractRepository elecContractRepository,
                                   UtilityContractService utilityContractService) {
        this.gasContractRepository = gasContractRepository;
        this.elecContractRepository = elecContractRepository;
        this.utilityContractService = utilityContractService;
    }

    @Override
    public List<ObjectedTask> findAllObjectedContracts() {
        List<ObjectedTask> objectedTasks = new ArrayList<>();
        objectedEnergyContracts().forEach(energyContract -> objectedTasks.add(ObjectedTask
                .builder()
                .id(energyContract.getId())
                .broker(energyContract.getBroker())
                .customer(energyContract.getCustomer())
                .createdBy((User) energyContract.getCreatedBy())
                .customerSite(energyContract.getCustomerSite())
                .dateCreated(energyContract.getDateCreated())
                .startDate(energyContract.getStartDate())
                .endDate(energyContract.getEndDate())
                .supplier(energyContract.getSupplier())
                .supplyType(energyContract.getSupplyType())
                .accountNumber(energyContract.getAccountNumber())
                .build()));

        objectedUtilityContracts().forEach(utilityContract -> objectedTasks.add(ObjectedTask
                .builder()
                .id(utilityContract.getId())
                .broker(utilityContract.getBroker())
                .customer(utilityContract.getCustomerSite().getCustomer())
                .createdBy(utilityContract.getCreatedBy())
                .customerSite(utilityContract.getCustomerSite())
                .dateCreated(utilityContract.getDateCreated())
                .startDate(utilityContract.getStartDate())
                .endDate(utilityContract.getEndDate())
                .supplier(utilityContract.getSupplier())
                .supplyType(utilityContract.getUtilityType())
                .accountNumber(utilityContract.getAccountNumber())
                .build()));

        return objectedTasks;
    }

    @Override
    public List<VerbalTask> findAllVerbalContracts() {
        List<VerbalTask> verbalTasks = new ArrayList<>();
        verbalEnergyContracts().forEach(energyContract -> verbalTasks.add(VerbalTask
                .builder()
                .id(energyContract.getId())
                .broker(energyContract.getBroker())
                .customer(energyContract.getCustomer())
                .createdBy((User) energyContract.getCreatedBy())
                .customerSite(energyContract.getCustomerSite())
                .dateCreated(energyContract.getDateCreated())
                .startDate(energyContract.getStartDate())
                .endDate(energyContract.getEndDate())
                .supplier(energyContract.getSupplier())
                .supplyType(energyContract.getSupplyType())
                .accountNumber(energyContract.getAccountNumber())
                .build()));

        verbalUtilityContracts().forEach(utilityContract -> verbalTasks.add(VerbalTask
                .builder()
                .id(utilityContract.getId())
                .broker(utilityContract.getBroker())
                .customer(utilityContract.getCustomerSite().getCustomer())
                .createdBy(utilityContract.getCreatedBy())
                .customerSite(utilityContract.getCustomerSite())
                .dateCreated(utilityContract.getDateCreated())
                .startDate(utilityContract.getStartDate())
                .endDate(utilityContract.getEndDate())
                .supplier(utilityContract.getSupplier())
                .supplyType(utilityContract.getUtilityType())
                .accountNumber(utilityContract.getAccountNumber())
                .build()));

        return verbalTasks;
    }

    private List<EnergyContract> objectedEnergyContracts() {
        List<EnergyContract> energyContracts = new ArrayList<>();
        energyContracts.addAll(gasContractRepository.findAllObjectedGasContracts());
        energyContracts.addAll(elecContractRepository.findAllObjectedElectricityContracts());
        return energyContracts;
    }

    private List<UtilityContract> objectedUtilityContracts() {
        return this.utilityContractService.findAllObjectedUtilityContracts();
    }

    private List<EnergyContract> verbalEnergyContracts() {
        List<EnergyContract> energyContracts = new ArrayList<>();
        energyContracts.addAll(gasContractRepository.findAllVerbalGasContracts());
        energyContracts.addAll(elecContractRepository.findAllVerbalElectricityContracts());
        return energyContracts;
    }

    private List<UtilityContract> verbalUtilityContracts() {
        return this.utilityContractService.findAllVerbalUtilityContracts();
    }
}
