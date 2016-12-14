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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.team1.model.User;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests the MessageController class
 */
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MessageControllerTest {
	
	private MockMvc mock;
	
	@Autowired
	WebApplicationContext context;
	
	@Before
	public void setup(){
		
		this.mock = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void getMessages() throws Exception{
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("ese@ese.ch");
		
		this.mock.perform(get("/profile/messages")
				.principal(principal))
				.andExpect(model().attributeExists("messages", "messageForm"))
				.andExpect(status().isOk())
				.andExpect(view().name("messages"));
	}
	
	@Test
	public void sendMessages() throws Exception{
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("ese@ese.ch");
		
		User recipient = mock(User.class);
		when(recipient.getUsername()).thenReturn("test@test.ch");
		
		this.mock.perform(post("/profile/messages/sendMessage")
				.principal(principal)
				.param("subject","hallo")
				.param("text", "velo")
				.param("recipient", recipient.getUsername())
				.param("principal", principal.getName())
				);
	}
	
}
