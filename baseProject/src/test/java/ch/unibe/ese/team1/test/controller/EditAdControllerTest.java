package ch.unibe.ese.team1.test.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team1.controller.EditAdController;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.AlertService;
import ch.unibe.ese.team1.controller.service.EditAdService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

/**
 * Tests the EditAdController class. It covers about 54% of the
 * EditAdController. The whole picture process is not tested as discussed in the
 * meeting.
 */
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class EditAdControllerTest {

	@Mock
	AdService adService;
	@Mock
	EditAdService editService;
	@Mock
	UserService userService;
	@Mock
	AlertService alertService;
	@Mock
	ServletContext servletContext;

	@InjectMocks
	EditAdController editC = new EditAdController();

	@Autowired
	WebApplicationContext context;

	private MockMvc mockMvc;
	private Ad ad;
	private final static String IMAGE_DIRECTORY = "";

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		MockitoAnnotations.initMocks(this);
		this.ad = mock(Ad.class);
	}

	@Test
	public void getEditAdPage() throws Exception {
		Principal principal = mock(Principal.class);
		this.mockMvc.perform(get("/profile/editAd").param("id", "1").principal(principal)).andExpect(status().isOk())
				.andExpect(view().name("editAd")).andExpect(model().attributeExists("placeAdForm"));
	}

	@Test
	public void editAdPage() {
		Principal principal = mock(Principal.class);
		PlaceAdForm form = mock(PlaceAdForm.class);
		String path = "path";
		Long id = (long) 1;

		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(editService.fillForm(ad)).thenReturn(form);
		when(servletContext.getRealPath(anyString())).thenReturn("");

		ModelAndView model = editC.editAdPage(id, principal);
		assertEquals("editAd", model.getViewName());
		assertEquals(ad, model.getModel().get("ad"));
		assertEquals(form, model.getModel().get("placeAdForm"));

	}

	@Test
	public void editAdPageWithForm() {
		PlaceAdForm form = mock(PlaceAdForm.class);
		BindingResult results = mock(BindingResult.class);
		Principal principal = mock(Principal.class);
		RedirectAttributes redirectAttr = mock(RedirectAttributes.class);
		User user = mock(User.class);
		Long id = (long) 1;
		List<String> filenames = new ArrayList<>();

		when(results.hasErrors()).thenReturn(false);
		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername("name")).thenReturn(user);
		when(servletContext.getRealPath(anyString())).thenReturn("");
		when(editService.saveFrom(form, filenames, user, id)).thenReturn(ad);
		when(ad.getId()).thenReturn(id);
		doNothing().when(alertService).triggerAlerts(ad);

		ModelAndView model = editC.editAdPageWithForm(form, results, principal, redirectAttr, id);
		assertEquals("redirect:/ad?id=" + id, model.getViewName());

	}

	@Test
	public void editAdPageWithFormRedirectAttributes() {
		PlaceAdForm form = mock(PlaceAdForm.class);
		BindingResult results = mock(BindingResult.class);
		Principal principal = mock(Principal.class);
		RedirectAttributes redirectAttr = mock(RedirectAttributes.class);
		User user = mock(User.class);
		Long id = (long) 1;
		List<String> filenames = new ArrayList<>();

		when(results.hasErrors()).thenReturn(false);
		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername("name")).thenReturn(user);
		when(servletContext.getRealPath(anyString())).thenReturn("");
		when(editService.saveFrom(form, filenames, user, id)).thenReturn(ad);
		when(ad.getId()).thenReturn(id);
		doNothing().when(alertService).triggerAlerts(ad);

		editC.editAdPageWithForm(form, results, principal, redirectAttr, id);
		verify(redirectAttr).addFlashAttribute("confirmationMessage",
				"Ad edited successfully. You can take a look at it below.");
	}

	@Test
	public void editAdPageWithFormBindingHasErrors() {
		PlaceAdForm form = mock(PlaceAdForm.class);
		BindingResult results = mock(BindingResult.class);
		Principal principal = mock(Principal.class);
		RedirectAttributes redirectAttr = mock(RedirectAttributes.class);
		User user = mock(User.class);
		Long id = (long) 1;
		List<String> filenames = new ArrayList<>();

		when(results.hasErrors()).thenReturn(true);
		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername("name")).thenReturn(user);
		when(servletContext.getRealPath(anyString())).thenReturn("");
		when(editService.saveFrom(form, filenames, user, id)).thenReturn(ad);
		when(ad.getId()).thenReturn(id);
		doNothing().when(alertService).triggerAlerts(ad);

		ModelAndView model = editC.editAdPageWithForm(form, results, principal, redirectAttr, id);
		assertEquals("placeAd", model.getViewName());
	}

	@Test
	public void deletePicture() {
		Long adId = (long) 1;
		Long picId = (long) 2;

		editC.deletePictureFromAd(adId, picId);
		verify(editService).deletePictureFromAd(adId, picId);
	}

}