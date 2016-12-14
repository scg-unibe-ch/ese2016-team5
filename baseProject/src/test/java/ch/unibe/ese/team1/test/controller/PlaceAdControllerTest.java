package ch.unibe.ese.team1.test.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.controller.PlaceAdController;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.AlertService;
import ch.unibe.ese.team1.model.Ad;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team1.controller.pojos.PictureUploader;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.service.BookmarkService;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.model.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests the PlaceAdController class which is responsible for the requests placing the ad. 
 * It covers about 42% of it's code. Remaining % are the ModelAttribute and picture related 
 * topics (as agreed in the meeting).
 *
 */
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class PlaceAdControllerTest {
	
	@Mock
    private AlertService alertService;

	@Mock
    private ServletContext servletContext;

	@Mock
    private MessageService messageService;

	@Mock
    private VisitService visitService;

	@Mock
    private BookmarkService bookmarkService;

	@Mock
    private UserService userService;

	@Mock
    private AdService adService;
	
	@InjectMocks
	private PlaceAdController placeC = new PlaceAdController(); 
	
	private Ad ad; 
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		ad = mock(Ad.class);
	}
	
	@Test
	public void placeAd() throws IOException{
		when(servletContext.getRealPath(anyString())).thenReturn(""); 
		ModelAndView model = placeC.placeAd(); 
		assertEquals("placeAd", model.getViewName()); 
	}
	
	@Test
	public void create(){
		BindingResult result = mock(BindingResult.class);
		Principal principal = mock(Principal.class); 
		User user = mock(User.class); 
		PictureUploader uploader = mock(PictureUploader.class); 
		PlaceAdForm form = mock(PlaceAdForm.class); 
		RedirectAttributes redirect = mock(RedirectAttributes.class); 
		
		placeC.setPictureUploader(uploader);
		List<String> files = new ArrayList<>();
		files.add("file1"); 
		files.add("file2"); 
		
		when(result.hasErrors()).thenReturn(false);
		when(principal.getName()).thenReturn("name"); 
		when(userService.findUserByUsername("name")).thenReturn(user); 
		when(uploader.getFileNames()).thenReturn(files); 
		when(adService.saveFrom(form, files, user)).thenReturn(ad); 
		when(ad.getId()).thenReturn((long)1); 
		doNothing().when(alertService).triggerAlerts(ad);
		
		ModelAndView model = placeC.create(form, result, redirect, principal);
		verify(redirect).addFlashAttribute("confirmationMessage",
                    "Ad placed successfully. You can take a look at it below."); 
		assertEquals("redirect:/ad?id=1", model.getViewName());
	}
	
	@Test
	public void createHasErrors(){
		BindingResult result = mock(BindingResult.class);
		Principal principal = mock(Principal.class); 
		User user = mock(User.class); 
		PictureUploader uploader = mock(PictureUploader.class); 
		PlaceAdForm form = mock(PlaceAdForm.class); 
		RedirectAttributes redirect = mock(RedirectAttributes.class); 
		
		placeC.setPictureUploader(uploader);
		List<String> files = new ArrayList<>();
		files.add("file1"); 
		files.add("file2"); 
		
		when(result.hasErrors()).thenReturn(true);
		
		ModelAndView model = placeC.create(form, result, redirect, principal); 
		assertEquals("placeAd", model.getViewName());
	}
	

}
