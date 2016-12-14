package ch.unibe.ese.team1.controller.service;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.UserUpdateService;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.UserDao;
/**
 * Tests the update method.
 * @author loren
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration


public class UserUpdateServiceTest {

	
	   @Autowired
	    private UserDao userDao;
	   @Autowired
	    private UserUpdateService uus;
	   @Autowired
	    private UserService userService;

	@Test
	public void updateFromTest() {

		
		EditProfileForm editProfile = new EditProfileForm();
		
		User adolfOgi = createUser("adolf@ogi.ch", "password", "Adolf", "Ogi",
				Gender.MALE);
		adolfOgi.setAboutMe("Wallis rocks");
		userDao.save(adolfOgi);
		
		editProfile.setFirstName("Dolf");
		editProfile.setLastName("Ogo");
		editProfile.setPassword("word");
		editProfile.setUsername("adolf@ogi.ch");
		editProfile.setAboutMe("Nichts");
		
		uus.updateFrom(editProfile);
		
		assertEquals(userService.findUserByUsername("adolf@ogi.ch").getFirstName(),"Dolf");
		assertEquals(userService.findUserByUsername("adolf@ogi.ch").getLastName(),"Ogo");
		assertEquals(userService.findUserByUsername("adolf@ogi.ch").getPassword(),"word");
		assertEquals(userService.findUserByUsername("adolf@ogi.ch").getAboutMe(),"Nichts");
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
