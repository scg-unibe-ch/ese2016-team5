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

import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.UserDao;
/**
 * Tests the two Find methods and the change method of the  User Role.
 * Complete Coverage except the generation of the password in UserService, because it just generates a password.
 * @author lorenzo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration


public class UserServiceTest {

	@Autowired
    private UserDao userDao;
	
	@Autowired
	private UserService us;
	
	@Test
	public void changePremiumTest() {
		
		User adolfOgi = createUser("adolf@ogi.ch", "password", "Adolf", "Ogi",
				Gender.MALE);
		adolfOgi.setAboutMe("Wallis rocks");
		userDao.save(adolfOgi);
		
		assertEquals(us.findUserByUsername("adolf@ogi.ch"),adolfOgi);
		assertEquals(us.findUserById(adolfOgi.getId()),adolfOgi);
		
		assertEquals(us.findUserByUsername("adolf@ogi.ch").getUserRoles().iterator().next().getRole(),"ROLE_USER");
		us.changePremium(adolfOgi);
		assertEquals(us.findUserByUsername("adolf@ogi.ch").getUserRoles().iterator().next().getRole(),"ROLE_PREMIUM");
		us.changePremium(adolfOgi);
		assertEquals(us.findUserByUsername("adolf@ogi.ch").getUserRoles().iterator().next().getRole(),"ROLE_USER");
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
