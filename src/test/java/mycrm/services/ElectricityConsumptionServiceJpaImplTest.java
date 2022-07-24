package mycrm.services;

import mycrm.configuration.ConsumptionType;
import mycrm.models.ElecCustomerContract;
import mycrm.models.ElectricityConsumption;
import mycrm.repositories.ElectricityConsumptionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ElectricityConsumptionServiceJpaImplTest {

    @InjectMocks
    private ElectricityConsumptionServiceJpaImpl electricityConsumptionServiceJpaImpl;

    @Mock
    private ElectricityConsumption mockedElectricityConsumption;

    @Mock
    private ElectricityConsumptionRepository mockedElectricityConsumptionRepository;

    @Test
    public void shouldFindAllElectricityConsumption() {
        List<ElectricityConsumption> electricityConsumptionList = new ArrayList<>();
        electricityConsumptionList.add(new ElectricityConsumption());

        when(mockedElectricityConsumptionRepository.findAll()).thenReturn(electricityConsumptionList);

        List<ElectricityConsumption> findAll = electricityConsumptionServiceJpaImpl.findAll();

        assertEquals(electricityConsumptionList, findAll);
    }

    @Test
    public void shouldFindElectricityConsumptionById() {
        ElectricityConsumption electricityConsumption = anElectricityConsumption();

        when(mockedElectricityConsumptionRepository.findOne(Long.valueOf(5))).thenReturn(electricityConsumption);

        ElectricityConsumption findById = electricityConsumptionServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(electricityConsumption, findById);
        assertEquals(electricityConsumption.getAnnualConsumption(), findById.getAnnualConsumption(), 1);
        assertEquals(electricityConsumption.getId(), findById.getId());
        assertEquals(electricityConsumption.getVersion(), findById.getVersion());
        assertEquals(electricityConsumption.getConsumptionType(), findById.getConsumptionType());
        assertEquals(electricityConsumption.getElecCustomerContract(), findById.getElecCustomerContract());
        
    }

    @Test
    public void shouldSaveElectricityConsumption() {
        ElectricityConsumption electricityConsumption = new ElectricityConsumption();
        electricityConsumption.setId(Long.valueOf(5));

        when(mockedElectricityConsumptionRepository.save(any(ElectricityConsumption.class)))
                .thenReturn(electricityConsumption);

        ElectricityConsumption savedElectricityConsumption = electricityConsumptionServiceJpaImpl
                .create(electricityConsumption);

        assertEquals(electricityConsumption, savedElectricityConsumption);
    }

    @Test
    public void shouldEditAndSaveElectricityConsumption() {
        ElectricityConsumption electricityConsumption = new ElectricityConsumption();
        electricityConsumption.setId(Long.valueOf(5));

        when(mockedElectricityConsumptionRepository.save(any(ElectricityConsumption.class)))
                .thenReturn(electricityConsumption);

        ElectricityConsumption savedElectricityConsumption = electricityConsumptionServiceJpaImpl
                .edit(electricityConsumption);

        assertEquals(electricityConsumption, savedElectricityConsumption);
    }

    @Test
    public void shouldDeleteElectricityConsumption() {
        electricityConsumptionServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedElectricityConsumptionRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldReturnListOfElectricityConsumptionByGasCustomerContract() {
        ElecCustomerContract electricityCustomerContract = new ElecCustomerContract();
        electricityCustomerContract.setId(54l);

        List<ElectricityConsumption> electricityConsumptionList = new ArrayList<>();
        electricityConsumptionList.add(mockedElectricityConsumption);

        when(mockedElectricityConsumptionRepository.findByElecCustomerContract(electricityCustomerContract))
                .thenReturn(electricityConsumptionList);

        List<ElectricityConsumption> findByGasCustomerContract = electricityConsumptionServiceJpaImpl
                .findByElecCustomerContract(electricityCustomerContract);

        assertEquals(electricityConsumptionList, findByGasCustomerContract);
    }    

    private ElectricityConsumption anElectricityConsumption() {
        ElectricityConsumption electricityConsumption = new ElectricityConsumption();
        electricityConsumption.setId(Long.valueOf(5));
        electricityConsumption.setVersion(1);
        electricityConsumption.setAnnualConsumption(10000);
        electricityConsumption.setElecCustomerContract(anElecCustomerContract());
        electricityConsumption.setConsumptionType(ConsumptionType.ACTUAL.toString());
        return electricityConsumption;
    }

    private ElecCustomerContract anElecCustomerContract() {
        return new ElecCustomerContract();
    }

}
