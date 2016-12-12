package ch.unibe.ese.team1.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.UserDao;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.Type;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class BookmarkServiceTest {

	@Autowired
	private UserDao userDao;
	@Autowired
	private AdDao adDao;
	
	@Autowired
	private BookmarkService book;
	
	
	
	@Test
	public void getBookmarkStatusTest(){

		
		User hans = new User();
		
		hans.setUsername("hans@kanns.ch");
		hans.setEmail("hans@kanns.ch");
		hans.setPassword("password");
		hans.setGender(Gender.MALE);
		hans.setFirstName("Hans");
		hans.setLastName("Kanns");
		hans.setAboutMe("Hansi Hinterseer");
		hans.setEnabled(true);
		
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(hans);
		userRoles.add(role);
		hans.setUserRoles(userRoles);
		
		
		List<Ad> AdList = new  ArrayList<Ad>();
		hans.setBookmarkedAds(AdList);
		
		userDao.save(hans);
		
		Ad ad = new Ad();
		
		ad.setAnimals(true);
		Date date = new Date();
		ad.setCreationDate(date);
		ad.setAuctionEndingDate(date);
		ad.setCity("3018 - Bern");
		ad.setPreferences("Test preferences");
		ad.setRoomDescription("Test Room description");
        ad.setDirectBuyPrice(600);
		ad.setSquareFootage(50);
		ad.setTitle("title");
		ad.setStreet("Hauptstrasse 13");
        ad.setType(Type.studio);
		ad.setOfferType(2);
		ad.setSmokers(true);
		ad.setAnimals(false);
		ad.setGarden(true);
		ad.setBalcony(false);
		ad.setCellar(true);
		ad.setFurnished(false);
		ad.setCable(false);
		ad.setGarage(true);
		ad.setInternet(false);
		ad.setUser(hans);
		ad.setZipcode(234);
		ad.setId(3);
		ad.setMoveInDate(date);
		adDao.save(ad);
		
		assertEquals(book.getBookmarkStatus(ad, false, hans),3);
		assertTrue(hans.getBookmarkedAds().contains(ad));
		assertEquals(book.getBookmarkStatus(ad, true, hans),2);
		assertFalse(hans.getBookmarkedAds().contains(ad));
	}
	
	
	
}
