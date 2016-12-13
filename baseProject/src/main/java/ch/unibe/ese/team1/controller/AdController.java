package ch.unibe.ese.team1.controller;

import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ch.unibe.ese.team1.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.BookmarkService;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.log.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This class is responsible for handling all requests concerning the displaying
 * and book marking of the ad. Especially in scenarios where the the apartment
 * of the ad is sold or likely. It uses the different service classes such as
 * AdService, UserService, BookmarkService, MessageService and VisitService.
 *
 */
@Controller
public class AdController {

	LogMain mainlog = new LogMain();

	@Autowired
	private AdService adService;

	@Autowired
	private UserService userService;

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private VisitService visitService;

	/**
	 * Gets the description page i.e. the details page of the ad to a given id.
	 * 
	 * @param id
	 *            - Long. The id under which the ad can be found.
	 * @param principal
	 *            - Principal. The principal/user who opened the ad i.e. owns
	 *            it.
	 * @return the ModelAndView containing the description page of the ad.
	 */
	@RequestMapping(value = "/ad", method = RequestMethod.GET)
	public ModelAndView ad(@RequestParam("id") long id, Principal principal) {
		mainlog.log.warning("AdController method ad received a request with the following id: " + id);
		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);
		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());
		String loggedInUserEmail = (principal == null) ? "" : principal.getName();
		model.addObject("loggedInUserEmail", loggedInUserEmail);
		model.addObject("visits", visitService.getVisitsByAd(ad));
		mainlog.log.warning("AdController method ad processed request with the following id: " + id);
		return model;
	}

	/**
	 * Clears or rather closing auctions in case they reached their auction due
	 * date without being sold or if they were successfully sold. Closing will
	 * set the status of the ad to 0 and send the corresponding message to the
	 * seller and if there is a buyer, also to the buyer.
	 * 
	 * @return String - redirects to the index
	 */
	@RequestMapping(value = "/ad/clearOldAuctions", method = RequestMethod.GET)
	public String clearOldAuctions() {
		mainlog.log.warning("AdController method clearOldAuction received a request");

		Iterable<Ad> oldAuctions = adService.getOldAuctions();
		for (Ad ad : oldAuctions) {
			if (ad.getLastBidder() == null) {
				long adId = ad.getId();
				String title = ad.getTitle();
				User seller = ad.getUser();
				String subjectSeller = "Unfortunately your property wasn't sell during the auction";
				String textSeller = "Unfortunately your property wasn't sell during the auction!\n\nTitle: " + title
						+ "\nURL: <a href='/ad?id=" + adId + "'>" + "/ad?id=" + adId + "</a>";
				messageService.sendMessage(seller, seller, subjectSeller, textSeller);
				ad.setStatus(0);
				adService.saveAd(ad);
			} else {
				long adId = ad.getId();
				String title = ad.getTitle();
				int bid = ad.getLastBid();
				User bidder = ad.getLastBidder();
				User seller = ad.getUser();
				String subjectBidder = "Auction won: " + bid + " on '" + title + "'";
				String subjectSeller = "Property sold: " + bid + " on '" + title + "'";
				String textBidder = "Congratulations! You won!\n\nTitle: " + title + "\nBid: " + bid
						+ "\nURL: <a href='/ad?id=" + adId + "'>" + "/ad?id=" + adId + "</a>";
				String textSeller = "Congratulations! Someone bought your property!\n\nTitle: " + title + "\nBid: "
						+ bid + "\nBidder: " + bidder.getUsername() + "\nURL: <a href='/ad?id=" + adId + "'>"
						+ "/ad?id=" + adId + "</a>";
				messageService.sendMessage(seller, bidder, subjectBidder, textBidder);
				messageService.sendMessage(bidder, seller, subjectSeller, textSeller);
				ad.setStatus(0);
				adService.saveAd(ad);
			}
		}
		mainlog.log.warning("AdController method clearOldAuctions processed request");
		return "redirect:/";
	}

	/**
	 * Gets the ad description page for the ad with the given id and also
	 * validates and persists the message passed as post data.
	 */

	/**
	 * Gets the description/detail page of the ad to a given id. Also validates
	 * and persists the message passed as post data.
	 * 
	 * @return the updated model
	 */
	@RequestMapping(value = "/ad", method = RequestMethod.POST)
	public ModelAndView messageSent(@RequestParam("id") long id, @Valid MessageForm messageForm,
			BindingResult bindingResult) {
		mainlog.log.warning("AdController method messageSent received a request with the following id: " + id);

		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);
		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());

		if (!bindingResult.hasErrors()) {
			messageService.saveFrom(messageForm);
		}

		mainlog.log.warning("AdController method messageSent processed request with the following id: " + id);
		return model;
	}

	/**
	 * Checks if the adID passed as post parameter is already inside user's List
	 * bookmarkedAds. In case it is present, true is returned changing the
	 * "Bookmark Ad" button to "Bookmarked". If it is not present it is added to
	 * the List bookmarkedAds.
	 * 
	 * @return 0 and 1 for errors; 3 to update the button to bookmarked 3 and 2
	 *         for bookmarking or undo bookmarking respectively 4 for removing
	 *         button completly (because its the users ad)
	 */
	@RequestMapping(value = "/bookmark", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public int isBookmarked(@RequestParam("id") long id, @RequestParam("screening") boolean screening,
			@RequestParam("bookmarked") boolean bookmarked, Principal principal) {

		mainlog.log.warning("AdController method isBookmarked received a request with the following id: " + id);

		// Should never happen since no bookmark button when not logged in
		if (principal == null) {
			return 0;
		}

		// Should not happen either...
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (user == null) {
			return 1;
		}

		List<Ad> bookmarkedAdsIterable = user.getBookmarkedAds();
		if (screening) {
			for (Ad ownAdIterable : adService.getAdsByUser(user)) {
				if (ownAdIterable.getId() == id) {
					return 4;
				}
			}
			for (Ad adIterable : bookmarkedAdsIterable) {
				if (adIterable.getId() == id) {
					return 3;
				}
			}
			return 2;
		}

		Ad ad = adService.getAdById(id);
		mainlog.log.warning("AdController method isBookmarked processed request with the following id: " + id);
		return bookmarkService.getBookmarkStatus(ad, bookmarked, user);
	}

	/**
	 * Fetches information about bookmarked rooms and own ads and attaches this
	 * information to the myRooms page in order to be displayed.
	 * 
	 * @param principal
	 *            - the user/owner of the ads
	 * @return the updated model containing the book marked and owned ads.
	 */
	@RequestMapping(value = "/profile/myRooms", method = RequestMethod.GET)
	public ModelAndView myRooms(Principal principal) {

		mainlog.log.warning("AdController method myRooms received a request from the principal");

		ModelAndView model;
		User user;
		if (principal != null) {
			model = new ModelAndView("myRooms");
			String username = principal.getName();
			user = userService.findUserByUsername(username);
			Iterable<Ad> ownAds = adService.getAdsByUser(user);
			model.addObject("bookmarkedAdvertisements", user.getBookmarkedAds());
			model.addObject("ownAdvertisements", ownAds);
			return model;
		} else {
			model = new ModelAndView("home");
		}

		mainlog.log.warning("AdController method myRooms processed request from the principal");

		return model;
	}

	/**
	 * Handles the placing of bids
	 */
	@RequestMapping(value = "/profile/placeBid", method = RequestMethod.POST)
	public String placeBid(@ModelAttribute("shownAd") @Validated Ad ad, BindingResult result, Principal principal,
			final RedirectAttributes redirectAttributes, @RequestParam long adId) {

		mainlog.log.warning("AdController method placeBid received a request with the following Adid: " + adId);

		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		adService.saveBid(ad, user, adId);

		Ad activeAd = adService.getAdById(adId);
		String title = activeAd.getTitle();
		int bid = activeAd.getLastBid();
		String bidder = principal.getName();
		String seller = activeAd.getUser().getUsername();
		String subjectBidder = "Bidding confirmation: " + bid + " on '" + title + "'";
		String subjectSeller = "New bid: " + bid + " on '" + title + "'";
		String textBidder = "Congratulations! Your bid was accepted!\n\nTitle: " + title + "\nBid: " + bid
				+ "\nURL: <a href='/ad?id=" + adId + "'>" + "/ad?id=" + adId + "</a>";
		String textSeller = "Congratulations! Someone just placed a bid on your property!\n\nTitle: " + title
				+ "\nBid: " + bid + "\nBidder: " + bidder + "\nURL: <a href='/ad?id=" + adId + "'>" + "/ad?id=" + adId
				+ "</a>";
		User bidderObj = userService.findUserByUsername(bidder);
		User sellerObj = userService.findUserByUsername(seller);

		messageService.sendMessage(sellerObj, bidderObj, subjectBidder, textBidder);
		messageService.sendMessage(bidderObj, sellerObj, subjectSeller, textSeller);

		redirectAttributes.addFlashAttribute("confirmationMessage", "Congratulations! Your bid was accepted!");

		mainlog.log.warning("AdController method placeBid processed request with the following id: " + adId);

		return "redirect:/ad?id=" + adId;

	}

	/**
	 * Handles direct buying of properties by performing the following actions:
	 * 1. Sets ad status to 0 (inactive) 2. Generates two messages to be sent to
	 * both parties and sends them 3. Reloads the page and displays a message
	 */
	@RequestMapping(value = "/profile/DirectBuy", method = RequestMethod.GET)
	public String directBuy(@RequestParam("id") long id, Principal principal, RedirectAttributes redirectAttributes) {
		assert principal != null;
		mainlog.log.warning("AdController method DirectBuy received a request with the following id: " + id);

		Ad ad = adService.getAdById(id);
		ad.setStatus(0);
		adService.saveAd(ad);

		String address = ad.getStreet();
		String city = ad.getCity();
		String buyerUsername = principal.getName();
		String sellerUsername = ad.getUser().getUsername();
		String subjectBuyer = "You have bought a property in " + city + " at " + address + ".";
		String subjectSeller = "Your property in " + city + " at " + address + " has been sold.";
		String textBuyer = "Congratulations.\nYou have bought an actual property that exists in the real world. And you have done this with the amazing powers of the Internet. Did you know that in june 2016, over 3.6 billion users were logged on to the Internet? \nYour property is located in "
				+ city + " at " + address + ". Please contact the user " + sellerUsername
				+ " to conclude your bargain.\nYours truly, the amazing Flatfindr team\nPS: This  message was automatically created by the Home Portal system.\nPPS: Isn't the Internet amazing?";
		String textSeller = "Congratulations.\nYour actual property that exists in the real world has been bought. And this was done with the amazing powers of the Internet. Did you know that in june 2016, over 3.6 billion users were logged on to the Internet? \nThe property you sold is located in "
				+ city + " at " + address + ". Please contact the user " + buyerUsername
				+ " to conclude your sale.\nYours truly, the amazing Flatfindr team\nPS: This  message was automatically created by the Home Portal system.\nPPS: Isn't the Internet amazing?";
		User buyer = userService.findUserByUsername(buyerUsername);
		User seller = userService.findUserByUsername(sellerUsername);

		messageService.sendMessage(seller, buyer, subjectBuyer, textBuyer);
		messageService.sendMessage(buyer, seller, subjectSeller, textSeller);

		redirectAttributes.addFlashAttribute("confirmationMessage",
				"You have successfully bought this property. Check your message inbox for further details.");

		mainlog.log.warning("AdController method DirectBuy processed request with the following id: " + id);

		return "redirect:/ad?id=" + id;
	}

}