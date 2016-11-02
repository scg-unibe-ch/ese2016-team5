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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This controller handles all requests concerning displaying ads and
 * bookmarking them.
 */
@Controller
public class AdController {

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

	/** Gets the ad description page for the ad with the given id. */
	@RequestMapping(value = "/ad", method = RequestMethod.GET)
	public ModelAndView ad(@RequestParam("id") long id, Principal principal) {
		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);
		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());

		String loggedInUserEmail = (principal == null) ? "" : principal
				.getName();
		model.addObject("loggedInUserEmail", loggedInUserEmail);

		model.addObject("visits", visitService.getVisitsByAd(ad));

		return model;
	}

	/**
	 * Gets the ad description page for the ad with the given id and also
	 * validates and persists the message passed as post data.
	 */
	@RequestMapping(value = "/ad", method = RequestMethod.POST)
	public ModelAndView messageSent(@RequestParam("id") long id,
			@Valid MessageForm messageForm, BindingResult bindingResult) {

		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);
		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());

		if (!bindingResult.hasErrors()) {
			messageService.saveFrom(messageForm);
		}
		return model;
	}

	/**
	 * Checks if the adID passed as post parameter is already inside user's
	 * List bookmarkedAds. In case it is present, true is returned changing
	 * the "Bookmark Ad" button to "Bookmarked". If it is not present it is
	 * added to the List bookmarkedAds.
	 * 
	 * @return 0 and 1 for errors; 3 to update the button to bookmarked 3 and 2
	 *         for bookmarking or undo bookmarking respectively 4 for removing
	 *         button completly (because its the users ad)
	 */
	@RequestMapping(value = "/bookmark", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public int isBookmarked(@RequestParam("id") long id,
			@RequestParam("screening") boolean screening,
			@RequestParam("bookmarked") boolean bookmarked, Principal principal) {
		// should never happen since no bookmark button when not logged in
		if (principal == null) {
			return 0;
		}
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (user == null) {
			// that should not happen...
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

		return bookmarkService.getBookmarkStatus(ad, bookmarked, user);
	}

	/**
	 * Fetches information about bookmarked rooms and own ads and attaches this
	 * information to the myRooms page in order to be displayed.
	 */
	@RequestMapping(value = "/profile/myRooms", method = RequestMethod.GET)
	public ModelAndView myRooms(Principal principal) {
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

		return model;
	}
        
        /**
         * Handles the placing of bids
         */
        @RequestMapping(value = "/profile/placeBid", method = RequestMethod.POST)
        public String placeBid(@ModelAttribute("shownAd") @Validated Ad ad, BindingResult result, Principal principal, final RedirectAttributes redirectAttributes, @RequestParam long adId) {
            
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
            String textBidder = "Congratulations! Your bid was accepted!\n\nTitle: " + title + "\nBid: " + bid + "\nURL: <a href='http://localhost:8080/ad?id=" + adId + "'>" + "http://localhost:8080/ad?id=" + adId + "</a>";
            String textSeller = "Congratulations! Someone just placed a bid on your property!\n\nTitle: " + title + "\nBid: " + bid + "\nBidder: " + bidder + "\nURL: <a href='http://localhost:8080/ad?id=" + adId + "'>" + "http://localhost:8080/ad?id=" + adId + "</a>";
            User bidderObj = userService.findUserByUsername(bidder);
            User sellerObj = userService.findUserByUsername(seller);

            messageService.sendMessage(sellerObj, bidderObj, subjectBidder, textBidder);
            messageService.sendMessage(bidderObj, sellerObj, subjectSeller, textSeller);

            redirectAttributes.addFlashAttribute("confirmationMessage", "Congratulations! Your bid was accepted!");           
            
            return "redirect:/ad?id=" + adId;

	}
        
        /**
         * Handles direct buying of properties by performing the following actions:
         * 1. Sets ad status to 0 (inactive)
         * 2. Generates two messages to be sent to both parties and sends them
         * 3. Reloads the page and displays a message
         */
        @RequestMapping(value = "/profile/DirectBuy", method = RequestMethod.GET)
    	public String DirectBuy(@RequestParam("id") long id, Principal principal, RedirectAttributes redirectAttributes) {
        	
        	Ad ad = adService.getAdById(id);
        	ad.setStatus(0);
        	
        	String address = ad.getStreet();
        	String city = ad.getCity();
        	String buyerUsername = principal.getName();
        	String sellerUsername = ad.getUser().getUsername();
        	String subjectBuyer = "You have bought a property in "+city+" at "+address+".";
        	String subjectSeller = "Your property in "+city+" at "+address+" has been sold.";
        	String textBuyer = "Congratulations.\nYou have bought an actual property that exists in the real world. And you have done this with the amazing powers of the Internet. Did you know that in june 2016, over 3.6 billion users were logged on to the Internet? \nYour property is located in "+city+" at"+address+". Please contact the user "+sellerUsername+" to conclude your bargain.\nYours truly, the amazing Flatfindr team\nPS: This  message was automatically created by the Flatfindr system.\nPPS: Isn't the Internet amazing?";
        	String textSeller = "Congratulations.\nYour actual property that exists in the real world has been bought. And this was done with the amazing powers of the Internet. Did you know that in june 2016, over 3.6 billion users were logged on to the Internet? \nThe property you sold is located in "+city+" at"+address+". Please contact the user "+buyerUsername+" to conclude your sale.\nYours truly, the amazing Flatfindr team\nPS: This  message was automatically created by the Flatfindr system.\nPPS: Isn't the Internet amazing?";
        	User buyer = userService.findUserByUsername(buyerUsername);
        	User seller = userService.findUserByUsername(sellerUsername);
        	
        	messageService.sendMessage(seller, buyer, subjectBuyer, textBuyer);
        	messageService.sendMessage(buyer, seller, subjectSeller, textSeller);
        	
        	redirectAttributes.addFlashAttribute("confirmationMessage", "You have successfully bought this property. Check your message inbox for further details.");
        	return "redirect:/ad?id=" + id;
        }

}