import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team1.controller.service.SignupService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.UserDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
/**
 * Tests the functionality of the SignupService Class.
 * @author loren
 *
 */
public class SignupServiceTest {

	 @Autowired
	    private UserDao userDao;
	 @Autowired
	    private SignupService ss;
	 @Autowired
	    private UserService us;
	
	@Test
	public void Test() {
		
		SignupForm sf = new SignupForm();
		sf.setEmail("adolf@ogi.ch");
		sf.setPassword("password");
		sf.setFirstName("Adolf");
		sf.setLastName("Ogi");
		sf.setGender(Gender.MALE);

		ss.saveFrom(sf);
		assertTrue(ss.doesUserWithUsernameExist("adolf@ogi.ch"));
		assertFalse(ss.doesUserWithUsernameExist("bdolf@ogi.ch"));
	}

}
