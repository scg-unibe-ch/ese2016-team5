package ch.unibe.ese.team1.controller.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.AdPictureDao;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.IDateTime;
import ch.unibe.ese.team1.model.Type;
import ch.unibe.ese.team1.model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Responsible for testing all aspects of the EditAdService.class. There are two
 * main aspects in the EditAdService class. Firstly, the creation of ads
 * according to a give place ad form and secondly, the picture management. The
 * tests are mostly performed using mocked objects.This test covers about 90% of
 * the EditAdService class. The picture setting is only tested partly, as the
 * visibility of the methods are restricted to private and the AdPictures are
 * instantiated inside the private methods. Without reflection or public setter
 * methods (which is not recommended according to the web), it is barely
 * testable.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
public class EditAdServiceTest {

	@Mock
	private AdService adService;

	@Mock
	private AdDao adDao;

	@Mock
	private AdPictureDao adPictureDao;

	@Mock
	private UserService userService;

	@InjectMocks
	private EditAdService editS = new EditAdService();

	private Ad ad = new Ad();
	private Ad spyAd;
	private PlaceAdForm form;
	private IDateTime dateTime;
	private User user;
	private List<String> filePaths;
	private List<String> filePathEmpty;
	private Date moveIn; 
	private Date moveOut; 

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		spyAd = spy(ad);
		form = mock(PlaceAdForm.class);
		user = mock(User.class);
		dateTime = mock(IDateTime.class);
		filePaths = new ArrayList<>();
		filePathEmpty = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");
	}

	@Test
	public void saveFrom() {
		mockBasicAdForm();
		Date date = new Date();
		List<AdPicture> pics = new ArrayList<>();
		when(adService.getAdById(anyLong())).thenReturn(spyAd);
		when(dateTime.getDate()).thenReturn(date);
		when(spyAd.getPictures()).thenReturn(pics);

		editS.setDateTime(dateTime);
		editS.saveFrom(form, filePathEmpty, user, (long) 1);

		assertEquals(date, spyAd.getCreationDate());
		assertEquals(0, spyAd.getOfferType());
		assertEquals(Type.room, spyAd.getType());
		assertEquals("Title", spyAd.getTitle());
		assertEquals("Street", spyAd.getStreet());
		assertEquals("City", spyAd.getCity());
		assertEquals(0, spyAd.getDirectBuyPrice());
		assertEquals(0, spyAd.getAuctionStartingPrice());
		assertEquals(500, spyAd.getPricePerMonth());
		assertEquals(100, spyAd.getSquareFootage());
		assertEquals("Description", spyAd.getRoomDescription());
		assertEquals("Preferences", spyAd.getPreferences());
		assertFalse(spyAd.getSmokers());
		assertFalse(spyAd.getAnimals());
		assertFalse(spyAd.getGarden());
		assertFalse(spyAd.getBalcony());
		assertFalse(spyAd.getCellar());
		assertFalse(spyAd.getFurnished());
		assertFalse(spyAd.getCable());
		assertFalse(spyAd.getGarage());
		assertFalse(spyAd.getInternet());
		assertFalse(spyAd.getDishwasher());
		assertEquals(null, spyAd.getVisits());
		assertEquals(user, spyAd.getUser());
		verify(adDao).save(spyAd);
	}

	@Test
	public void setVisits() throws ParseException {
		mockBasicAdForm();
		mockFormDates(form);
		Date date = new Date();
		when(dateTime.getDate()).thenReturn(date);

		String visit = "28-02-2014;10:02;13:14";
		List<AdPicture> pics = new ArrayList<>();
		List<String> visits = new ArrayList<>();
		visits.add(visit);

		when(adService.getAdById(anyLong())).thenReturn(spyAd);
		when(dateTime.getDate()).thenReturn(date);
		when(form.getVisits()).thenReturn(visits);
		when(spyAd.getPictures()).thenReturn(pics);

		editS.setDateTime(dateTime);
		editS.saveFrom(form, filePathEmpty, user, (long) 1);
		verify(spyAd).setVisits(anyObject());

	}

	@Test
	public void deletePictureFromAd() {
		Ad ad = new Ad();
		Ad spyAd = spy(ad);
		AdPicture pic = mock(AdPicture.class);
		List<AdPicture> pics = new ArrayList<>();
		pics.add(pic);
		when(spyAd.getPictures()).thenReturn(pics);
		when(adService.getAdById(anyLong())).thenReturn(spyAd);
		when(adPictureDao.findOne(anyLong())).thenReturn(pic);
		editS.deletePictureFromAd((long) 1, (long) 2);
		pics.remove(pic);
		verify(spyAd).setPictures(pics);
		verify(adDao).save(spyAd);
	}

	@Test
	public void fillForm() {
		Ad ad = mock(Ad.class);
		when(ad.getRoomDescription()).thenReturn("description");
		when(ad.getPreferences()).thenReturn("preferences");
		PlaceAdForm form = editS.fillForm(ad);
		assertEquals("description", form.getRoomDescription());
		assertEquals("preferences", form.getPreferences());
	}

	private void mockBasicAdForm() {
		when(form.getOfferType()).thenReturn(0);
		when(form.getType()).thenReturn(Type.room);
		when(form.getTitle()).thenReturn("Title");
		when(form.getStreet()).thenReturn("Street");
		when(form.getCity()).thenReturn("3000 - City");
		when(form.getDirectBuyPrice()).thenReturn(0); // as it is for rent
		when(form.getAuctionStartingPrice()).thenReturn(0);
		when(form.getPrice()).thenReturn(500);
		when(form.getSquareFootage()).thenReturn(100);
		when(form.getRoomDescription()).thenReturn("Description");
		when(form.getPreferences()).thenReturn("Preferences");
		when(form.isSmokers()).thenReturn(false);
		when(form.isAnimals()).thenReturn(false);
		when(form.getGarden()).thenReturn(false);
		when(form.getBalcony()).thenReturn(false);
		when(form.getCellar()).thenReturn(false);
		when(form.isFurnished()).thenReturn(false);
		when(form.getCable()).thenReturn(false);
		when(form.getGarage()).thenReturn(false);
		when(form.getInternet()).thenReturn(false);
		when(form.getDishwasher()).thenReturn(false);
		when(form.getMoveInDate()).thenReturn("01-01-1970");
		when(form.getMoveOutDate()).thenReturn("01-01-1970");
		when(form.getAuctionEndingDate()).thenReturn("01-01-1970 01:01");
		when(form.getVisits()).thenReturn(null);
	}

	private void mockFormDates(PlaceAdForm form) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		when(form.getMoveInDate()).thenReturn("01-01-2014");
		moveIn = formatter.parse("01-01-2014");
		when(form.getMoveOutDate()).thenReturn("01-01-2020");
		moveOut = formatter.parse("01-01-2020");

	}

}
