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

import ch.unibe.ese.team1.controller.SearchController;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.model.Ad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests the SearchController class i.e. the search and the result methods of
 * it. It covers about 80% of the code of SearchController. The ModelAttribute
 * is not required to be tested (missing 20%).
 *
 */
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchControllerTest {

	@Mock
	private AdService adService;

	@InjectMocks
	private SearchController searchC = new SearchController();

	private Ad ad;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.ad = mock(Ad.class);
	}

	@Test
	public void searchAd() {
		ModelAndView model = searchC.searchAd();
		assertEquals("searchAd", model.getViewName());
	}

	@Test
	public void results() {
		SearchForm form = mock(SearchForm.class);
		BindingResult result = mock(BindingResult.class);
		List<Ad> ads = new ArrayList<>();
		ads.add(ad);
		when(result.hasErrors()).thenReturn(false);
		when(adService.searchAndFilter(form)).thenReturn(ads);

		ModelAndView model = searchC.results(form, result);
		assertEquals("results", model.getViewName());
		assertEquals(ads, model.getModel().get("results"));
	}

	@Test
	public void resultsBindingHasErrors() {
		SearchForm form = mock(SearchForm.class);
		BindingResult result = mock(BindingResult.class);
		List<Ad> ads = new ArrayList<>();
		ads.add(ad);
		when(result.hasErrors()).thenReturn(true);
		when(adService.searchAndFilter(form)).thenReturn(ads);

		ModelAndView model = searchC.results(form, result);
		assertEquals("searchAd", model.getViewName());
	}

}
