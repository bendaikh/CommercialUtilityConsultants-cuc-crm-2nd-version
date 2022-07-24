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
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class CrmControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private CrmController crmController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/greeting");
        viewResolver.setSuffix(".html");

		mockMvc = MockMvcBuilders.standaloneSetup(crmController)
				.setViewResolvers(viewResolver).build();
	}

	@Test
	public void shouldReturnGreetingPage() throws Exception {
		mockMvc.perform(get("/greeting"))
		.andExpect(status().isOk())
		.andExpect(view().name("greeting"));
	}

}
