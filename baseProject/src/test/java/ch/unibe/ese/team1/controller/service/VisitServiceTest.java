package ch.unibe.ese.team1.controller.service;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.Type;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.UserDao;
import ch.unibe.ese.team1.model.dao.VisitDao;
import ch.unibe.ese.team1.model.dao.VisitEnquiryDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration

/**
 * Tests the functionality of VisitService.
 * Not entierly complete code Coverage.
 * @author lorenzo
 *
 */
public class VisitServiceTest  {

    @Autowired
    private VisitDao visitDao;

    @Autowired
    VisitEnquiryDao visitEnquiryDao;
   
    @Autowired
    VisitService visitService;
    @Autowired
    private AdDao adDao;
    @Autowired
    private UserDao userDao;
	@Test
	public void GetVisitAndtest() throws Exception{
	
		
		User adolfOgi = createUser("adolf@ogi.ch", "password", "Adolf", "Ogi",
				Gender.MALE);
		adolfOgi.setAboutMe("Wallis rocks");
		userDao.save(adolfOgi);
		
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
		
				Visit visit = new Visit();
				visit.setAd(oltenResidence);
				visit.setStartTimestamp(formatter.parse("16.12.2014 10:00"));
				visit.setEndTimestamp(formatter.parse("16.12.2014 12:00"));
				visitDao.save(visit);	
		

				// Asserts that the visit is the same with the ad, visit itself doesn't seem to function, even though its essentially the same.
				assertEquals(visitService.getVisitById(visit.getId()).getAd(),oltenResidence);
				assertEquals(visitService.getVisitsByAd(oltenResidence).iterator().next().getAd(),oltenResidence);
				
				// Not quite sure how to solve this neatly
				assertFalse(visitService.getVisitsForUser(adolfOgi).iterator().hasNext());
				assertFalse(visitService.getVisitorsForVisit(visit.getId()).iterator().hasNext());
				
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
