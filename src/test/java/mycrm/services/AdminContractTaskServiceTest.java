package mycrm.services;

import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.NewSaleTask;
import mycrm.models.ProcessingTask;
import mycrm.repositories.ElecContractRepository;
import mycrm.repositories.GasContractRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminContractTaskServiceTest {

    @Mock
    private AdminContractTaskServiceImpl adminContractTaskService;

    @Mock
    private ElecContractRepository mockElectricContractRepository;

    @Mock
    private GasContractRepository mockGasContractRepository;

    @Mock
    private UtilityContractService mockUtilityContractService;

    @Test
    public void shouldReturnListOfAdminContractTasks() {

        List<GasCustomerContract> gasAdminTasks = new ArrayList<>();
        gasAdminTasks.add(new GasCustomerContract());

        List<ElecCustomerContract> electricAdminTasks = new ArrayList<>();
        electricAdminTasks.add(new ElecCustomerContract());

        when(mockGasContractRepository.findAllGasAdminContractNewSalesTasks()).thenReturn(gasAdminTasks);

        when(mockElectricContractRepository.findAllElectricAdminContractNewSalesTasks()).thenReturn(electricAdminTasks);

        when(mockUtilityContractService.findAllAdminUtilityContractNewSalesTasks()).thenReturn(new ArrayList<>());

        List<NewSaleTask> newSalesTasks = adminContractTaskService.findAllAdminContractNewSalesTasks();

        assertEquals(newSalesTasks.size(), 0);
    }

    @Test
    public void shouldReturnListOfAdminProcessingTasks() {

        when(mockGasContractRepository.findAllGasAdminContractProcessingTasks()).thenReturn(new ArrayList<>());

        when(mockElectricContractRepository.findAllElectricAdminContractProcessingTasks()).thenReturn(new ArrayList<>());

        when(mockUtilityContractService.findAllAdminContractProcessingTasks()).thenReturn(new ArrayList<>());

        List<ProcessingTask> contractsToProcess = adminContractTaskService.findAllContractsToProcess();

        assertEquals(contractsToProcess.size(), 0);
    }
}
