package mycrm.datamanipulation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mycrm.functions.GasConsumptionCalculation;
import mycrm.models.GasConsumption;

public class GasConsumptionCalculationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void canInstantiateGasConsumptionCalculation() {
		GasConsumptionCalculation gasConsumptionCalculation = new GasConsumptionCalculation();
		assertNotNull(gasConsumptionCalculation);		
	}
	
	@Test
	public void canInstantiateGasConsumptionCalculationWithGasConsumption() {
		List<GasConsumption> gasConsumptions = new ArrayList<>();
		
		GasConsumption gasConsumption = new GasConsumption();
		gasConsumption.setId(1l);
		gasConsumption.setAnnualConsumption(60000l);
		gasConsumption.setVersion(1);
		
		GasConsumption gasConsumption2 = new GasConsumption();
		gasConsumption2.setId(3l);
		gasConsumption2.setAnnualConsumption(60000l);
		
		gasConsumptions.add(gasConsumption);
		gasConsumptions.add(gasConsumption2);
		
		GasConsumptionCalculation gasConsumptionCalculation = new GasConsumptionCalculation();
		gasConsumptionCalculation.calculateTotalGasConsumption(gasConsumptions);
		
		assertNotNull(gasConsumptionCalculation);
		
		assertEquals(120000, 120000);
		assertEquals(gasConsumption.getVersion(), gasConsumptions.get(0).getVersion());
		
	}

}
