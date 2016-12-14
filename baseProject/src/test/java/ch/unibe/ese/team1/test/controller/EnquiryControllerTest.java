package ch.unibe.ese.team1.test.controller;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.team1.controller.EnquiryController;
import ch.unibe.ese.team1.controller.service.EnquiryService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.Type;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.VisitEnquiry;
import ch.unibe.ese.team1.model.VisitEnquiryState;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.RatingDao;
import ch.unibe.ese.team1.model.dao.UserDao;
import ch.unibe.ese.team1.model.dao.VisitDao;
import ch.unibe.ese.team1.model.dao.VisitEnquiryDao;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Tests the Enquiry Controller functionality.
 * The methods of  acceptEnquiry, declineEnquiry, reopenEnquiry and rateUser are already in the EnquiryService Class, 
 * so they don't need to be tested again.
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration


public class EnquiryControllerTest {

	@Autowired
	private EnquiryController enquiryController;
	
	@Autowired
	private EnquiryService enquiryService;

	@Autowired
	private UserService userService;

	@Autowired
	private VisitService visitService;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	VisitDao visitDao;
	
	@Autowired
	VisitEnquiryDao visitEnquiryDao;
	
	@Autowired
	AdDao adDao;
	@Autowired
	RatingDao rd;
	
	@Test
	public void enquiresPageTest() throws Exception{
		
		User adolfOgi = createUser("adolf@ogi.ch", "password", "Adolf", "Ogi",
				Gender.MALE);
		adolfOgi.setAboutMe("Wallis rocks");
		userDao.save(adolfOgi);
		
		User blocher = createUser("christoph@blocher.eu", "svp", "Christoph", "Blocher", Gender.MALE);
		blocher.setAboutMe("I own you");
		userDao.save(blocher);
		
		//save an ad
		Date date = new Date();
		Ad oltenResidence= new Ad();
		oltenResidence.setZipcode(4600);
		oltenResidence.setMoveInDate(date);
		oltenResidence.setCreationDate(date);
		oltenResidence.setPricePerMonth(1200);
		oltenResidence.setSquareFootage(42);
        oltenResidence.setType(Type.room);
		oltenResidence.setSmokers(true);
		oltenResidence.setAnimals(false);
		oltenResidence.setRoomDescription("blah");
		oltenResidence.setPreferences("blah");
		oltenResidence.setUser(adolfOgi);
		oltenResidence.setTitle("Olten Residence");
		oltenResidence.setStreet("Florastr. 100");
		oltenResidence.setCity("Olten");
		oltenResidence.setGarden(false);
		oltenResidence.setBalcony(false);
		oltenResidence.setCellar(false);
		oltenResidence.setFurnished(false);
		oltenResidence.setCable(false);
		oltenResidence.setGarage(false);
		oltenResidence.setInternet(false);
		adDao.save(oltenResidence);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		
		//ad two possible visiting times ("visits") to the ad
		Visit visit = new Visit();
		visit.setAd(oltenResidence);
		visit.setStartTimestamp(formatter.parse("16.12.2014 10:00"));
		visit.setEndTimestamp(formatter.parse("16.12.2014 12:00"));
		visitDao.save(visit);

		Visit visit2 = new Visit();
		visit2.setAd(oltenResidence);
		visit2.setStartTimestamp(formatter.parse("18.12.2014 10:00"));
		visit2.setEndTimestamp(formatter.parse("18.12.2014 12:00"));
		visitDao.save(visit2);
		
		//Ogi is enquiring about Blocher's apartment
		VisitEnquiry enquiry = new VisitEnquiry();
		enquiry.setVisit(visit);
		enquiry.setSender(adolfOgi);
		enquiry.setState(VisitEnquiryState.OPEN);
		visitEnquiryDao.save(enquiry);
		
		Iterable<VisitEnquiry> ogiEnquiries = visitEnquiryDao.findBySender(adolfOgi);
		ArrayList<VisitEnquiry> ogiEnquiryList = new ArrayList<VisitEnquiry>();
		for(VisitEnquiry venq: ogiEnquiries)
			ogiEnquiryList.add(venq);
		
		long venqID = ogiEnquiryList.get(0).getId();
		
		
		Principal test = Mockito.mock(Principal.class);
		when(test.getName()).thenReturn("adolf@ogi.ch");

		
		// Kann man nicht wirklich testen
		//enquiryController.sendEnquiryForVisit(id, principal);
		//enquiryController.enquiriesPage(principal)

		
	}
	User createUser(String email, String password, String firstName,
			String lastName, Gender gender) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);
		return user;
	}
}
