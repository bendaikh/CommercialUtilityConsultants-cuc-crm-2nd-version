package mycrm.services;

import mycrm.models.EnergyContract;
import mycrm.models.MerchantServicesContract;
import mycrm.models.PendingTerminationTask;
import mycrm.models.UtilityContract;
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
public class AdminContractTerminationTaskServiceImpl implements AdminContractTerminationTaskService {

    private final ElecContractRepository elecContractRepository;
    private final GasContractRepository gasContractRepository;
    private final UtilityContractService utilityContractService;

    private final MerchantServicesService merchantServicesService;

    @Autowired
    public AdminContractTerminationTaskServiceImpl(ElecContractRepository elecContractRepository,
                                                   GasContractRepository gasContractRepository,
                                                   UtilityContractService utilityContractService, MerchantServicesService merchantServicesService) {
        this.elecContractRepository = elecContractRepository;
        this.gasContractRepository = gasContractRepository;
        this.utilityContractService = utilityContractService;
        this.merchantServicesService = merchantServicesService;
    }

    @Override
    public List<PendingTerminationTask> findAllContractsToTerminate() {
        List<PendingTerminationTask> pendingTerminationTasks = new ArrayList<>(mergePendingTerminationTasks());

        //ORDER BY end_date ASC
        pendingTerminationTasks.sort(Comparator.comparing(PendingTerminationTask::getEndDate, Comparator.nullsLast(Comparator.naturalOrder())));
        return pendingTerminationTasks;
    }

    private List<PendingTerminationTask> mergePendingTerminationTasks() {
        List<PendingTerminationTask> pendingTerminationTasks = new ArrayList<>();
        pendingTerminationTasks.addAll(pendingTerminationTasksFromUtilityContracts());
        pendingTerminationTasks.addAll(pendingTerminationTasksFromEnergyContracts());
        return pendingTerminationTasks;
    }

    private List<PendingTerminationTask> pendingTerminationTasksFromUtilityContracts() {
        List<PendingTerminationTask> pendingTerminationTasks = new ArrayList<>();
        utilityContractList().forEach(utilityContract -> pendingTerminationTasks.add(PendingTerminationTask
                .builder()
                .id(utilityContract.getId())
                .customer(utilityContract.getCustomerSite().getCustomer())
                .supplyType(utilityContract.getUtilityType())
                .startDate(utilityContract.getStartDate())
                .endDate(utilityContract.getEndDate())
                .currentSupplyTerminated(utilityContract.isCurrentSupplyTerminated())
                .build()));

        merchantContractList().forEach(merchantContract -> pendingTerminationTasks.add(PendingTerminationTask
                .builder()
                .id(merchantContract.getId())
                .customer(merchantContract.getCustomerSite().getCustomer())
                .supplyType(merchantContract.getSupplyType())
                .startDate(merchantContract.getStartDate())
                .endDate(merchantContract.getEndDate())
                .currentSupplyTerminated(merchantContract.isCurrentSupplyTerminated())
                .build()));
        return pendingTerminationTasks;
    }

    private List<PendingTerminationTask> pendingTerminationTasksFromEnergyContracts() {
        List<PendingTerminationTask> pendingTerminationTasks = new ArrayList<>();
        energyContractsList().forEach(energyContract -> pendingTerminationTasks.add(PendingTerminationTask
                .builder()
                .id(energyContract.getId())
                .customer(energyContract.getCustomer())
                .supplyType(energyContract.getSupplyType())
                .startDate(energyContract.getStartDate())
                .endDate(energyContract.getEndDate())
                .currentSupplyTerminated(energyContract.isCurrentSupplyTerminated())
                .build()));
        return pendingTerminationTasks;
    }

    private List<EnergyContract> energyContractsList() {
        List<EnergyContract> energyContracts = new ArrayList<>();
        energyContracts.addAll(elecContractRepository.findAllElectricContractsToTerminate());
        energyContracts.addAll(gasContractRepository.findAllGasContractsToTerminate());
        return energyContracts;
    }

    private List<UtilityContract> utilityContractList() {
        return new ArrayList<>(utilityContractService.findAllUtilityContractsToTerminate());
    }

    private List<MerchantServicesContract> merchantContractList() {
        return new ArrayList<>(merchantServicesService.findAllMerchantContractsToTerminate());
    }
}
