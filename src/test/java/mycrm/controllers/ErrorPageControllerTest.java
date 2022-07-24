package mycrm.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ErrorPageControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private ErrorPageController errorPageController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/access-denied");
        viewResolver.setSuffix(".html");

		mockMvc = MockMvcBuilders.standaloneSetup(errorPageController)
				.setViewResolvers(viewResolver).build();
	}

	@Test
	public void shouldReturnAccessDeniedPage() throws Exception {
		mockMvc.perform(get("/access-denied"))
				.andExpect(status().isOk())
				.andExpect(view().name("access-denied"));
	}

}
