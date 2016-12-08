package ch.unibe.ese.team1.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team1.controller.service.EnquiryService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.log.LogMain;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.VisitEnquiry;
import ch.unibe.ese.team1.model.VisitEnquiryState;

/**
 * Handles all requests concerning enquiries of type
 * {@link ch.unibe.ese.team1.model.VisitEnquiry VisitEnquiry} between users.
 */
@Controller
public class EnquiryController {
	
	LogMain mainlog = new LogMain();

	@Autowired
	private EnquiryService enquiryService;

	@Autowired
	private UserService userService;

	@Autowired
	private VisitService visitService;

	/** Serves the page that displays the enquiries for the logged in user. */
	@RequestMapping(value = "/profile/enquiries")
	public ModelAndView enquiriesPage(Principal principal) {
		mainlog.log.warning("EnquiryController method enquiriesPage received a request with the following principal: " + principal.toString());
		ModelAndView model = new ModelAndView("enquiries");
		User user = userService.findUserByUsername(principal.getName());
		Iterable<VisitEnquiry> usersEnquiries = enquiryService
				.getEnquiriesByRecipient(user);
		model.addObject("enquiries", usersEnquiries);
		mainlog.log.warning("EnquiryController method enquiriesPage processed request with the following principal: " + principal.toString());
		return model;
	}

	/**
	 * Sends an enquiry for the visit with the given id. The sender of the
	 * enquiry will be the currently logged in user.
	 */
	@RequestMapping(value = "/profile/enquiries/sendEnquiryForVisit")
	public @ResponseBody void sendEnquiryForVisit(@RequestParam("id") long id,
			Principal principal) {
		mainlog.log.warning("EnquiryController method sendEnquiryForVisit received a request with the following principal: " + principal.toString());
		Visit visit = visitService.getVisitById(id);
		User user = userService.findUserByUsername(principal.getName());

		VisitEnquiry visitEnquiry = new VisitEnquiry();
		visitEnquiry.setDateSent(new Date());
		visitEnquiry.setSender(user);
		visitEnquiry.setState(VisitEnquiryState.OPEN);
		visitEnquiry.setVisit(visit);

		enquiryService.saveVisitEnquiry(visitEnquiry);
		mainlog.log.warning("EnquiryController method sendEnquiryForVisit processed request with the following principal: " + principal.toString());
	}

	/** Sets the state of the enquiry with the given id to accepted. */
	@RequestMapping(value = "/profile/enquiries/acceptEnquiry", method = RequestMethod.GET)
	public @ResponseBody void acceptEnquiry(@RequestParam("id") long id) {
		mainlog.log.warning("EnquiryController method acceptEnquiry received a request with the following id: " + id);
		enquiryService.acceptEnquiry(id);
	}

	/** Sets the state of the enquiry with the given id to declined. */
	@RequestMapping(value = "/profile/enquiries/declineEnquiry", method = RequestMethod.GET)
	public @ResponseBody void declineEnquiry(@RequestParam("id") long id) {
		mainlog.log.warning("EnquiryController method declineEnquiry received a request with the following id: " + id);
		enquiryService.declineEnquiry(id);
	}

	/**
	 * Reopens the enquiry with the given id, meaning that its state is set to
	 * open again.
	 */
	@RequestMapping(value = "/profile/enquiries/reopenEnquiry", method = RequestMethod.GET)
	public @ResponseBody void reopenEnquiry(@RequestParam("id") long id) {
		mainlog.log.warning("EnquiryController method reopenEnquiry received a request with the following id: " + id);
		enquiryService.reopenEnquiry(id);
	}

	/**
	 * Rates the user with the given id with the given rating. This rating is
	 * associated to the user and persisted.
	 */
	@RequestMapping(value = "/profile/rateUser", method = RequestMethod.GET)
	public @ResponseBody void rateUser(Principal principal,
			@RequestParam("rate") long id, @RequestParam("stars") int rating) {
		mainlog.log.warning("EnquiryController method rateUser received a request with the following principal: " + principal.toString());
		User user = userService.findUserByUsername(principal.getName());
		enquiryService.rate(user, userService.findUserById(id), rating);
		mainlog.log.warning("EnquiryController method rateUser processed request with the following principal: " + principal.toString());
	}

	/**
	 * Returns the rating for the given user that the currently logged in user
	 * has given them.
	 */
	@RequestMapping(value = "/profile/ratingFor", method = RequestMethod.GET)
	public @ResponseBody int ratingFor(Principal principal,
			@RequestParam("user") long id) {
		mainlog.log.warning("EnquiryController method ratingFor received a request with the following principal: " + principal.toString());
		User principe = userService.findUserByUsername(principal.getName());
		User ratee = userService.findUserById(id);
		mainlog.log.warning("EnquiryController method ratingFor processed request with the following principal: " + principal.toString());
		return enquiryService.getRatingByRaterAndRatee(principe, ratee)
				.getRating();
	}
}
