package mycrm.services;

import mycrm.models.GasConsumption;
import mycrm.models.GasCustomerContract;
import mycrm.repositories.GasConsumptionRepository;
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
public class GasConsumptionServiceJpaImplTest {

    @InjectMocks
    private GasConsumptionServiceJpaImpl gasConsumptionServiceJpaImpl;

    @Mock
    private GasConsumption mockedGasConsumption;

    @Mock
    private GasConsumptionRepository mockedGasConsumptionRepository;

    @Test
    public void shouldFindAllGasConsumption() {
        List<GasConsumption> gasConsumptionList = new ArrayList<>();
        gasConsumptionList.add(new GasConsumption());

        when(mockedGasConsumptionRepository.findAll()).thenReturn(gasConsumptionList);

        List<GasConsumption> findAll = gasConsumptionServiceJpaImpl.findAll();

        assertEquals(findAll, gasConsumptionList);
    }

    @Test
    public void shouldFindGasConsumptionById() {
        GasConsumption gasConsumption = new GasConsumption();
        gasConsumption.setId(Long.valueOf(5));

        when(mockedGasConsumptionRepository.findOne(Long.valueOf(5))).thenReturn(gasConsumption);

        GasConsumption findById = gasConsumptionServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(findById, gasConsumption);
    }

    @Test
    public void shouldSaveGasConsumption() {
        GasConsumption gasConsumption = new GasConsumption();
        gasConsumption.setId(Long.valueOf(5));

        when(mockedGasConsumptionRepository.save(any(GasConsumption.class))).thenReturn(gasConsumption);

        GasConsumption savedGasConsumption = gasConsumptionServiceJpaImpl.create(gasConsumption);

        assertEquals(savedGasConsumption, gasConsumption);
    }

    @Test
    public void shouldEditAndSaveGasConsumption() {
        GasConsumption gasConsumption = new GasConsumption();
        gasConsumption.setId(Long.valueOf(5));

        when(mockedGasConsumptionRepository.save(any(GasConsumption.class))).thenReturn(gasConsumption);

        GasConsumption savedGasConsumption = gasConsumptionServiceJpaImpl.edit(gasConsumption);

        assertEquals(savedGasConsumption, gasConsumption);
    }

    @Test
    public void shouldDeleteGasConsumption() {
        gasConsumptionServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedGasConsumptionRepository, times(1)).delete(Long.valueOf(541));
    }
    
    @Test
    public void shouldReturnListOfGasConsumptionByGasCustomerContract() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(54l);
        
        List<GasConsumption> gasConsumptionList = new ArrayList<>();
        gasConsumptionList.add(mockedGasConsumption);
        
        when(mockedGasConsumptionRepository.findByGasCustomerContract(gasCustomerContract)).thenReturn(gasConsumptionList);
        
        List<GasConsumption> findByGasCustomerContract = gasConsumptionServiceJpaImpl.findByGasCustomerContract(gasCustomerContract);
        
        assertEquals(findByGasCustomerContract, gasConsumptionList);
        
        
    }

}
