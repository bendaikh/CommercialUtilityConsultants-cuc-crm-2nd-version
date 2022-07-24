package mycrm.controllers;

import mycrm.models.Supplier;
import mycrm.services.SupplierService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class SupplierControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private SupplierController supplierController;

	@Mock
	private SupplierService mockSupplierService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(supplierController).build();
	}

	@Test
	public void shouldReturnEditPage() throws Exception {
		Supplier supplier = new Supplier();

		when(mockSupplierService.findById(Long.valueOf(974))).thenReturn(supplier);

		mockMvc.perform(get("/admin/supplier/edit/974")).andExpect(model().attribute("supplier", supplier))
				.andExpect(view().name("admin/supplier/newsupplier")).andExpect(status().isOk());
	}

	@Test
	public void shouldReturnNewSupplierPage() throws Exception {
		mockMvc.perform(get("/admin/supplier/newsupplier"))
				.andExpect(model().attribute("supplier", instanceOf(Supplier.class)))
				.andExpect(view().name("admin/supplier/newsupplier")).andExpect(status().isOk());
	}

	@Test
	public void shouldCreateNewSupplier() throws Exception {
		mockMvc.perform(post("/supplier")).andExpect(model().attribute("supplier", instanceOf(Supplier.class)))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/supplier/suppliers"));
	}

	@Test
	public void shouldViewSupplier() throws Exception {

		Supplier supplier = new Supplier();
		when(mockSupplierService.findById(Long.valueOf(111))).thenReturn(supplier);

		mockMvc.perform(get("/admin/supplier/view/111"))
				.andExpect(model().attribute("supplier", supplier))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/supplier/view"));
	}
	
	@Test
	public void shouldReturnListOfSupplier() throws Exception {
		List<Supplier> suppliers = new ArrayList<>();
		suppliers.add(new Supplier());
        when(mockSupplierService.findAllOrderByBusinessName()).thenReturn(suppliers);

		mockMvc.perform(get("/admin/supplier/suppliers"))
				.andExpect(model().attribute("findAll", hasSize(1)))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/supplier/suppliers"));
	}

}
