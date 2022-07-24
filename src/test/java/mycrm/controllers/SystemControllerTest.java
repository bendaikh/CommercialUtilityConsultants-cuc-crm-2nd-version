package mycrm.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class SystemControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private SystemController systemController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(systemController).build();
	}

	@Test
	public void shouldAccessSystemView() throws Exception {
		mockMvc.perform(get("/admin/system/index")).andExpect(status().isOk())
				.andExpect(view().name("admin/system/index"));
	}

}
