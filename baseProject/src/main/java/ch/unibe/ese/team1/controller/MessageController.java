package ch.unibe.ese.team1.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team1.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.log.LogMain;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.User;

/** This controller handles all requests concerning messaging. */
@Controller
public class MessageController {
	
	LogMain mainlog = new LogMain();

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	/**
	 * Shows the messages page for the currently logged in user. The inbox of
	 * the user is shown.
	 */
	@RequestMapping(value = "/profile/messages", method = RequestMethod.GET)
	public ModelAndView messages(Principal principal) {
		mainlog.log.warning("MessageController method messages received a request with the following principal: " + principal.toString());
		ModelAndView model = new ModelAndView("messages");
		User user = userService.findUserByUsername(principal.getName());
		model.addObject("messageForm", new MessageForm());
		model.addObject("messages", messageService.getInboxForUser(user));
		mainlog.log.warning("MessageController method messages processed request with the following principal: " + principal.toString());
		return model;
	}

	/**
	 * Gets all messages in the inbox for the currently logged in user.
	 */
	@RequestMapping(value = "/profile/message/inbox", method = RequestMethod.POST)
	public @ResponseBody Iterable<Message> getInbox(Principal principal) {
		mainlog.log.warning("MessageController method getInbox received a request with the following principal: " + principal.toString());
		User user = userService.findUserByUsername(principal.getName());
		mainlog.log.warning("MessageController method getInbox processed request with the following principal: " + principal.toString());
		return messageService.getInboxForUser(user);
	}

	/** Gets all messages in the sent folder for the currently logged in user. */
	@RequestMapping(value = "/profile/message/sent", method = RequestMethod.POST)
	public @ResponseBody Iterable<Message> getSent(Principal principal) {
		mainlog.log.warning("MessageController method getSent received a request with the following principal: " + principal.toString());
		User user = userService.findUserByUsername(principal.getName());
		mainlog.log.warning("MessageController method getSent processed request with the following principal: " + principal.toString());
		return messageService.getSentForUser(user);
	}

	/** Gets the message with the given id */
	@RequestMapping(value = "/profile/messages/getMessage", method = RequestMethod.GET)
	public @ResponseBody Message getMessage(@RequestParam Long id) {
		mainlog.log.warning("MessageController method getMessage received a request with the following id: " + id);
		return messageService.getMessage(id);
	}

	/**
	 * Shows the messages page and validates and/or saves the message passed a
	 * post data.
	 */
	@RequestMapping(value = "/profile/messages", method = RequestMethod.POST)
	public ModelAndView messageSent(@Valid MessageForm messageForm,
			BindingResult bindingResult, Principal principal) {
		mainlog.log.warning("MessageController method messageSent received a request with the following principal: " + principal.toString());
		ModelAndView model = new ModelAndView("messages");
		if (!bindingResult.hasErrors()) {
			messageService.saveFrom(messageForm);
			User user = userService.findUserByUsername(principal.getName());
			model.addObject("messageForm", new MessageForm());
			model.addObject("messages", messageService.getInboxForUser(user));
		}
		mainlog.log.warning("MessageController method messageSent processed request with the following principal: " + principal.toString());
		return model;
	}
	
	/**
	 * Sets the MessageState of a given Message to "READ".
	 */
	@RequestMapping(value="/profile/readMessage", method = RequestMethod.GET)
	public @ResponseBody void readMessage(@RequestParam("id") long id) {
		mainlog.log.warning("MessageController method readMessage received a request with the following id: " + id);
		messageService.readMessage(id);
	}
	
	/**
	 * Returns the number of unread messages of the logged in user.
	 */
	@RequestMapping(value="/profile/unread", method = RequestMethod.GET)
	public @ResponseBody int unread(Principal principal) {
		mainlog.log.warning("MessageController method unread received a request with the following principal: " + principal.toString());
		long id = userService.findUserByUsername(principal.getName()).getId();
		mainlog.log.warning("MessageController method unread processed request with the following principal: " + principal.toString());
		return messageService.unread(id);
	}

	/**
	 * Checks if the email passed as post parameter is a valid email. In case it
	 * is valid, the email address is returned. If it is not, a error message is
	 * returned.
	 */
	@RequestMapping(value = "/profile/messages/validateEmail", method = RequestMethod.POST)
	@ResponseBody
	public String validateEmail(@RequestParam String email) {
		mainlog.log.warning("MessageController method validateEmail received a request with the following email: " + email);
		User user = userService.findUserByUsername(email);
		if (user == null) {
			mainlog.log.warning("MessageController method validateEmail processed request with the following email: " + email);
			return "This user does not exist.";
		} else {
			mainlog.log.warning("MessageController method validateEmail processed request with the following email: " + email);
			return user.getEmail();
		}
	}

	/** Sends a message with the passed parameters */
	@RequestMapping(value = "/profile/messages/sendMessage", method = RequestMethod.POST)
	public @ResponseBody void sendMessage(@RequestParam String subject,
			@RequestParam String text, @RequestParam String recipientEmail,
			Principal principal) {
		mainlog.log.warning("MessageController method sendMessage received a request with the following principal: " + principal.toString());
		User recipient = userService.findUserByUsername(recipientEmail);
		User sender = userService.findUserByUsername(principal.getName());
		messageService.sendMessage(sender, recipient, subject, text);
		mainlog.log.warning("MessageController method sendMessage processed request with the following principal: " + principal.toString());
	}

}
