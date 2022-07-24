package mycrm.controllers;

import mycrm.functions.GasConsumptionCalculation;
import mycrm.models.ElecCustomerContract;
import mycrm.models.ElectricityConsumption;
import mycrm.models.GasConsumption;
import mycrm.models.GasCustomerContract;
import mycrm.services.ElecContractService;
import mycrm.services.ElectricityConsumptionService;
import mycrm.services.GasConsumptionService;
import mycrm.services.GasContractService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ConsumptionControllerTest {

	private MockMvc mockMvc;

	@Mock
	private GasConsumptionService mockGasConsumptionService;

	@Mock
	private ElectricityConsumptionService mockElectricityConsumptionService;

	@Mock
	private GasContractService mockGasContractService;

	@Mock
	private ElecContractService mockElecContractService;

	@InjectMocks
	private ConsumptionController consumptionController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(consumptionController).build();
	}

	@Test
	public void shouldReturnAnnualGasConsumption() throws Exception {

		mockMvc.perform(get("/admin/customer/annualGasConsumption").param("id", "1")).andExpect(status().isOk())
				.andExpect(model().attribute("passedVariable", Long.valueOf(1)))
				.andExpect(model().attribute("annualGasConsumption", instanceOf(GasConsumption.class)))
				.andExpect(view().name("/admin/customer/annualconsumption :: gas"));
	}

	@Test
	public void shouldReturnAnnualElectricityConsumption() throws Exception {

		mockMvc.perform(get("/admin/customer/annualElectricityConsumption").param("id", "1")).andExpect(status().isOk())
				.andExpect(model().attribute("passedVariable", Long.valueOf(1)))
				.andExpect(model().attribute("annualElectricityConsumption", instanceOf(ElectricityConsumption.class)))
				.andExpect(view().name("/admin/customer/annualconsumption :: electricity"));
	}

	@Test
	public void shouldUpdateAnnualGasConsumption() throws Exception {

		GasCustomerContract gasCustomerContract = new GasCustomerContract();
		gasCustomerContract.setId(Long.valueOf(55));

		GasConsumption g = new GasConsumption();

		when(mockGasConsumptionService.create(any(GasConsumption.class))).thenReturn(g);

		mockMvc.perform(post("/updateAnnualGasConsumption").param("id", "3").param("gasCustomerContract.id", "1")
				.param("annualConsumption", "10000").param("consumptionType", "gas")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(redirectedUrl("/admin/customer/gasconsumption/1"));
	}

	@Test
	public void shouldUpdateAnnualElectricityConsumption() throws Exception {

		ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
		elecCustomerContract.setId(Long.valueOf(88));

		ElectricityConsumption e = new ElectricityConsumption();

		when(mockElectricityConsumptionService.create(any(ElectricityConsumption.class))).thenReturn(e);

		mockMvc.perform(post("/updateAnnualElectricityConsumption").param("id", "3")
				.param("elecCustomerContract.id", "165").param("annualConsumption", "50000")
				.param("consumptionType", "electricity").contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(redirectedUrl("/admin/customer/electicityconsumption/165"));
	}

	@Test
	public void shouldReturnAnnualGasConsumptionForASite() throws Exception {

		GasCustomerContract gasCustomerContract = new GasCustomerContract();
		gasCustomerContract.setId(Long.valueOf(265));
		
		GasConsumption gasConsumption = new GasConsumption();
		gasConsumption.setAnnualConsumption(50000l);

		List<GasCustomerContract> gasCustomerContracts = new ArrayList<>();
		gasCustomerContracts.add(new GasCustomerContract());

		List<GasConsumption> gasConsumptionList = new ArrayList<>();
		gasConsumptionList.add(gasConsumption);

		Long totalGasConsumption = new GasConsumptionCalculation().calculateTotalGasConsumption(gasConsumptionList);
		
		when(mockGasContractService.findById(Long.valueOf(265))).thenReturn(gasCustomerContract);
		when(mockGasConsumptionService.findByGasCustomerContract(any(GasCustomerContract.class)))
				.thenReturn(gasConsumptionList);

		mockMvc.perform(get("/admin/customer/gasconsumption/265")).andExpect(status().isOk())
				.andExpect(model().attribute("gasCustomerContract", instanceOf(GasCustomerContract.class)))
				.andExpect(model().attribute("gasCustomerContractId", Long.valueOf(265)))
				.andExpect(model().attribute("gasConsumptionList", hasSize(1)))
				.andExpect(model().attribute("annualGasConsumption", instanceOf(GasConsumption.class)))
				.andExpect(model().attribute("totalGasConsumption", totalGasConsumption))
				.andExpect(view().name("/admin/customer/gasconsumption"));

	}
	
	@Test
	public void shouldReturnAnnualElectricityConsumptionForASite() throws Exception {
	
		ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
		elecCustomerContract.setId(Long.valueOf(365));
		
		when(mockElecContractService.findById(Long.valueOf(365))).thenReturn(elecCustomerContract );

		mockMvc.perform(get("/admin/customer/elecconsumption/365")).andExpect(status().isOk())
				.andExpect(model().attribute("elecCustomerContract", instanceOf(ElecCustomerContract.class)))
				.andExpect(model().attribute("annualElectricityConsumption", instanceOf(ElectricityConsumption.class)))
				.andExpect(view().name("/admin/customer/elecconsumption"));
	}
}
