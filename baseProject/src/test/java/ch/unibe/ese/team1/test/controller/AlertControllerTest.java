package ch.unibe.ese.team1.test.controller;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AlertControllerTest {
	
	private MockMvc mock;
	
	@Autowired
	WebApplicationContext context;
	
	@Before
	public void setup(){
		this.mock = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void getAlerts() throws Exception{
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("ese@ese.ch");
		
		this.mock.perform(post("/profile/alerts")
				.principal(principal))
			//	.andExpect(model().attributeExists("alerts", "alertForm"))
				.andExpect(status().isOk())
				.andExpect(view().name("alerts"));
	}
	
/*	@Test
	public void postAlertsWithoutErrorsTest() throws Exception{
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("ese@ese.ch");
		
		this.mock.perform(post("/profile/alerts")
				.principal(principal)
				.param("offerType", "1")
				.param("offerType", "1")
				.param("offerType", "1")
				.param("offerType", "1")
				.param("offerType", "1")
				)
				.andExpect(status().isOk())
				.andExpect(view().name("alerts"))
				.andExpect(model().attributeHasNoErrors("alertForm"));
	}*/
}
