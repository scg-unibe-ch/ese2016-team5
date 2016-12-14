package ch.unibe.ese.team1.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.Type;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class MessageServiceTest {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	MessageDao messageDao;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	UserService userService;
	
	static User adolfOgi;
	static User rogerKoeppel;
	static boolean setupDone;
	
	@Before
	public void setup() {
		if(setupDone)
			return;
		else
		// Create user Adolf Ogi
		adolfOgi = createUser("adolf@ogi.ch", "password", "Adolf", "Ogi", Gender.MALE);
		adolfOgi.setAboutMe("Wallis rocks");
		userDao.save(adolfOgi);
		
		// Create user Roger Koeppel
		rogerKoeppel = createUser("roger@koeppel.ch", "password", "Roger", "Koeppel", Gender.MALE);
		rogerKoeppel.setAboutMe("Weltwoche rocks");
		userDao.save(rogerKoeppel);

		adolfOgi = userService.findUserByUsername("adolf@ogi.ch");
		rogerKoeppel = userService.findUserByUsername("roger@koeppel.ch");
		
		// Create 2 new messages
		User recipient = adolfOgi;
		User sender = rogerKoeppel;
		String subject = "SVP";
		String text = "Wir machen doch unser Land kapputt. Lass uns aufhören damit.";
		messageService.sendMessage(sender, recipient, subject, text);

		User recipient2 = adolfOgi;
		User sender2 = rogerKoeppel;
		String subject2 = "Niemals";
		String text2 = "Nein, nein, nein";
		messageService.sendMessage(sender2, recipient2, subject2, text2);
		
		User recipient3 = rogerKoeppel;
		User sender3 = adolfOgi;
		String subject3 = "Früher";
		String text3 = "Ja, da wars noch besser";
		messageService.sendMessage(sender3, recipient3, subject3, text3);
		
		setupDone = true;
	}
	
	@Test
	public void testInbox(){
		ArrayList<Message> messageList = new ArrayList<Message>();
		
		//get inbox
		Iterable<Message> messages = messageService.getInboxForUser(adolfOgi);
		for(Message returnedMessage: messages)
			messageList.add(returnedMessage);
		
		//test inbox
		assertEquals(2, messageList.size());
		assertEquals(adolfOgi, messageList.get(0).getRecipient());
		assertEquals("Nein, nein, nein", messageList.get(1).getText());
		assertEquals(rogerKoeppel, messageList.get(1).getSender());
		assertEquals("SVP", messageList.get(0).getSubject());
	}

	@Test
	public void testOutbox(){
		ArrayList<Message> messageList = new ArrayList<Message>();
		
		//get outbox
		Iterable<Message> messages = messageService.getSentForUser(rogerKoeppel);
		for(Message returnedMessage: messages)
			messageList.add(returnedMessage);
		
		//test outbox
				assertEquals(2, messageList.size());
				assertEquals(adolfOgi, messageList.get(0).getRecipient());
				assertEquals("Nein, nein, nein", messageList.get(1).getText());
				assertEquals(rogerKoeppel, messageList.get(1).getSender());
				assertEquals("SVP", messageList.get(0).getSubject());
	}
	
	@Test
	public void testUnreadAndReadMessages(){
		ArrayList<Message> messageList = new ArrayList<Message>();
		
		//test unread
				assertEquals(1, messageService.unread(rogerKoeppel.getId()));

		//get inbox for adolfOgi
				Iterable<Message> messages = messageService.getInboxForUser(rogerKoeppel);
				for(Message returnedMessage: messages)
					messageList.add(returnedMessage);
				
		//test readMessage
				messageService.readMessage(messageList.get(0).getId());
				assertEquals(0, messageService.unread(rogerKoeppel.getId()));
	}

	//Lean user creating method
	User createUser(String email, String password, String firstName, String lastName, Gender gender) {
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
	
	//Lean message creating method
	Message createMessage(User sender, User recipient, String subject, String text){
		Message message = new Message();
		message.setSender(sender);
		message.setRecipient(recipient);
		message.setSubject(subject);
		message.setText(text);
		return message;
	}
}
