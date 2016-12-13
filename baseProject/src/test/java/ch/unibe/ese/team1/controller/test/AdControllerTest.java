package ch.unibe.ese.team1.controller.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.controller.AdController;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.model.Ad;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import ch.unibe.ese.team1.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team1.controller.service.BookmarkService;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * This class is responsible for testing the AdController class. It uses the
 * mock and inject mocks annotation to mock the service classes. Code coverage
 * of AdController is 99%, while assertions were not tested. There is also no
 * integration testing of the requests.
 * 
 * Messages triggered from the AdController i.e. MessageService class are only
 * verified.
 */
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AdControllerTest {

	@Mock
	private AdService adService;
	@Mock
	private UserService userService;
	@Mock
	private BookmarkService bookmarkService;
	@Mock
	private MessageService messageService;
	@Mock
	private VisitService visitService;

	@InjectMocks
	private AdController adC = new AdController();

	private Ad ad;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ad = mock(Ad.class);
	}

	@Test
	public void modelAndViewAd() {
		Principal principal = mock(Principal.class);
		List<Visit> visits = new ArrayList<>();
		Long id = new Long(1);

		when(principal.getName()).thenReturn("Batman");
		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(visitService.getVisitsByAd(anyObject())).thenReturn(visits);

		ModelAndView model = adC.ad(id, principal);
		assertEquals("adDescription", model.getViewName());
		assertEquals(ad, model.getModel().get("shownAd"));
		assertEquals("Batman", model.getModel().get("loggedInUserEmail"));
		assertEquals(visits, model.getModel().get("visits"));
	}

	@Test
	public void modelAndViewAdNullPrincipal() {
		Principal principal = null;
		List<Visit> visits = new ArrayList<>();
		Long id = new Long(1);

		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(visitService.getVisitsByAd(anyObject())).thenReturn(visits);

		ModelAndView model = adC.ad(id, principal);
		assertEquals("adDescription", model.getViewName());
		assertEquals(ad, model.getModel().get("shownAd"));
		assertEquals("", model.getModel().get("loggedInUserEmail"));
		assertEquals(visits, model.getModel().get("visits"));
	}

	@Test
	public void clearOldAuctions() throws Exception {
		User seller = mock(User.class);
		User bidder = mock(User.class);
		List<Ad> oldAuctions = new ArrayList<>();
		oldAuctions.add(ad);
		when(adService.getOldAuctions()).thenReturn(oldAuctions);

		when(ad.getLastBidder()).thenReturn(bidder);
		when(ad.getId()).thenReturn((long) 1);
		when(ad.getTitle()).thenReturn("title");
		when(ad.getUser()).thenReturn(seller);
		String redirect = adC.clearOldAuctions();

		assertEquals("redirect:/", redirect);
	}

	@Test
	public void clearOldAuctionsBidderNull() throws Exception {
		User user1 = mock(User.class);
		List<Ad> oldAuctions = new ArrayList<>();
		oldAuctions.add(ad);
		when(adService.getOldAuctions()).thenReturn(oldAuctions);

		when(ad.getLastBidder()).thenReturn(null);
		when(ad.getId()).thenReturn((long) 1);
		when(ad.getTitle()).thenReturn("title");
		when(ad.getUser()).thenReturn(user1);
		String redirect = adC.clearOldAuctions();
		assertEquals("redirect:/", redirect);

	}

	@Test
	public void messageSent() {
		MessageForm messageForm = mock(MessageForm.class);
		BindingResult result = mock(BindingResult.class);
		Long id = (long) 1;

		when(result.hasErrors()).thenReturn(false);
		when(adService.getAdById(anyLong())).thenReturn(ad);

		ModelAndView model = adC.messageSent(id, messageForm, result);
		verify(messageService).saveFrom(messageForm);
		assertEquals(ad, model.getModel().get("shownAd"));
	}

	@Test
	public void messageSentBindingResultsError() {
		MessageForm messageForm = mock(MessageForm.class);
		BindingResult result = mock(BindingResult.class);
		Long id = (long) 1;

		when(result.hasErrors()).thenReturn(true);
		when(adService.getAdById(anyLong())).thenReturn(ad);

		ModelAndView model = adC.messageSent(id, messageForm, result);
		verify(messageService, never()).saveFrom(messageForm);
		assertEquals(ad, model.getModel().get("shownAd"));
	}

	@Test
	public void isBookmarked() {
		Principal principal = mock(Principal.class);
		User user = mock(User.class);
		Long idAd = (long) 1;
		Long idAd2 = (long) 2;
		Boolean screening = true;
		Boolean bookmarked = true;
		List<Ad> ads = new ArrayList<>();
		Ad ad2 = mock(Ad.class);
		ads.add(ad2);
		ads.add(ad);

		List<Ad> markedAds = new ArrayList<>();
		markedAds.add(ad);

		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername(anyString())).thenReturn(user);
		when(user.getBookmarkedAds()).thenReturn(markedAds);
		when(adService.getAdsByUser(user)).thenReturn(ads);
		when(ad.getId()).thenReturn(idAd);
		when(ad2.getId()).thenReturn(idAd2);

		int bookmark = adC.isBookmarked(idAd, screening, bookmarked, principal);
		assertEquals(4, bookmark);
	}

	@Test
	public void isBookmarkedInUserListOfBookmarks() {
		Principal principal = mock(Principal.class);
		User user = mock(User.class);
		Long idAd = (long) 1;
		Long idAd2 = (long) 2;
		Boolean screening = true;
		Boolean bookmarked = true;
		List<Ad> ads = new ArrayList<>();
		Ad ad2 = mock(Ad.class);
		ads.add(ad);

		List<Ad> markedAds = new ArrayList<>();
		markedAds.add(ad);
		markedAds.add(ad2);

		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername(anyString())).thenReturn(user);
		when(user.getBookmarkedAds()).thenReturn(markedAds);
		when(adService.getAdsByUser(user)).thenReturn(ads);
		when(ad.getId()).thenReturn(idAd);
		when(ad2.getId()).thenReturn(idAd2);

		int bookmark = adC.isBookmarked(idAd2, screening, bookmarked, principal);
		assertEquals(3, bookmark);
	}

	@Test
	public void isBookmarkedNoAds() {
		Principal principal = mock(Principal.class);
		User user = mock(User.class);
		Long idAd = (long) 1;
		Boolean screening = true;
		Boolean bookmarked = true;
		List<Ad> ads = new ArrayList<>();
		List<Ad> markedAds = new ArrayList<>();

		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername(anyString())).thenReturn(user);
		when(user.getBookmarkedAds()).thenReturn(markedAds);
		when(adService.getAdsByUser(user)).thenReturn(ads);

		int bookmark = adC.isBookmarked(idAd, screening, bookmarked, principal);
		assertEquals(2, bookmark);
	}

	@Test
	public void isBookmarkedUserNull() {
		Principal principal = mock(Principal.class);
		User user = null;
		Long idAd = (long) 1;
		Boolean screening = true;
		Boolean bookmarked = true;

		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername(anyString())).thenReturn(user);

		int bookmark = adC.isBookmarked(idAd, screening, bookmarked, principal);
		assertEquals(1, bookmark);
	}

	@Test
	public void isBookmarkedScreeningFalseBookmarkTrue() {
		Principal principal = mock(Principal.class);
		User user = mock(User.class);
		Boolean screening = false;
		Boolean bookmarked = true;

		List<Ad> markedAds = new ArrayList<>();
		markedAds.add(ad);

		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername(anyString())).thenReturn(user);
		when(user.getBookmarkedAds()).thenReturn(markedAds);
		when(adService.getAdById(anyLong())).thenReturn(ad);

		if (markedAds.isEmpty()) {
			when(bookmarkService.getBookmarkStatus(ad, anyBoolean(), user)).thenReturn(1);
		} else {
			when(bookmarkService.getBookmarkStatus(ad, false, user)).thenReturn(3);
			when(bookmarkService.getBookmarkStatus(ad, true, user)).thenReturn(2);
		}

		int bookmark = adC.isBookmarked((long) 1, screening, bookmarked, principal);
		verify(bookmarkService).getBookmarkStatus(ad, bookmarked, user);
		assertEquals(2, bookmark);
	}

	@Test
	public void isBookmarkedScreeningFalseBookmarkFalse() {
		Principal principal = mock(Principal.class);
		User user = mock(User.class);
		Boolean screening = false;
		Boolean bookmarked = false;

		List<Ad> markedAds = new ArrayList<>();
		markedAds.add(ad);

		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername(anyString())).thenReturn(user);
		when(user.getBookmarkedAds()).thenReturn(markedAds);
		when(adService.getAdById(anyLong())).thenReturn(ad);

		if (markedAds.isEmpty()) {
			when(bookmarkService.getBookmarkStatus(ad, anyBoolean(), user)).thenReturn(1);
		} else {
			when(bookmarkService.getBookmarkStatus(ad, false, user)).thenReturn(3);
			when(bookmarkService.getBookmarkStatus(ad, true, user)).thenReturn(2);
		}

		int bookmark = adC.isBookmarked((long) 1, screening, bookmarked, principal);
		verify(bookmarkService).getBookmarkStatus(ad, bookmarked, user);
		assertEquals(3, bookmark);
	}

	@Test
	public void isBookmarkedPrincipalNull() {
		Principal principal = null;

		int bookmark = adC.isBookmarked((long) 1, true, true, principal);
		assertEquals(0, bookmark);
	}

	@Test
	public void myRooms() {
		Principal principal = mock(Principal.class);
		User user = mock(User.class);
		List<Ad> ads = new ArrayList<>();
		ads.add(ad);

		List<Ad> marked = new ArrayList<>();
		marked.add(ad);

		when(principal.getName()).thenReturn("name");
		when(userService.findUserByUsername(anyString())).thenReturn(user);
		when(adService.getAdsByUser(user)).thenReturn(ads);
		when(user.getBookmarkedAds()).thenReturn(marked);

		ModelAndView model = adC.myRooms(principal);

		assertEquals("myRooms", model.getViewName());
		assertEquals(marked, model.getModel().get("bookmarkedAdvertisements"));
		assertEquals(ads, model.getModel().get("ownAdvertisements"));

	}

	@Test
	public void myRoomsPrincipalNull() {
		Principal principal = null;

		ModelAndView model = adC.myRooms(principal);
		assertEquals("home", model.getViewName());
	}

	@Test
	public void placeBid() {
		Principal principal = mock(Principal.class);
		BindingResult binding = mock(BindingResult.class);
		User userBidder = mock(User.class);
		User userSeller = mock(User.class);
		RedirectAttributes redirect = mock(RedirectAttributes.class);
		Long id = (long) 1;

		when(principal.getName()).thenReturn("bidder");
		when(userService.findUserByUsername("bidder")).thenReturn(userBidder);
		when(userService.findUserByUsername("seller")).thenReturn(userSeller);
		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(ad.getTitle()).thenReturn("title");
		when(ad.getLastBid()).thenReturn(100);
		when(ad.getUser()).thenReturn(userSeller);
		when(userSeller.getUsername()).thenReturn("seller");

		String result = adC.placeBid(ad, binding, principal, redirect, id);
		assertEquals("redirect:/ad?id=1", result);

	}

	@Test
	public void placeBidAdSaved() {
		Principal principal = mock(Principal.class);
		BindingResult binding = mock(BindingResult.class);
		User userBidder = mock(User.class);
		User userSeller = mock(User.class);
		RedirectAttributes redirect = mock(RedirectAttributes.class);
		Long id = (long) 1;

		when(principal.getName()).thenReturn("bidder");
		when(userService.findUserByUsername("bidder")).thenReturn(userBidder);
		when(userService.findUserByUsername("seller")).thenReturn(userSeller);
		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(ad.getTitle()).thenReturn("title");
		when(ad.getLastBid()).thenReturn(100);
		when(ad.getUser()).thenReturn(userSeller);
		when(userSeller.getUsername()).thenReturn("seller");

		adC.placeBid(ad, binding, principal, redirect, id);
		verify(adService).saveBid(ad, userBidder, id);

	}

	@Test
	public void placeBidMessagesTriggered() {
		Principal principal = mock(Principal.class);
		BindingResult binding = mock(BindingResult.class);
		User userBidder = mock(User.class);
		User userSeller = mock(User.class);
		RedirectAttributes redirect = mock(RedirectAttributes.class);
		Long id = (long) 1;

		when(principal.getName()).thenReturn("bidder");
		when(userService.findUserByUsername("bidder")).thenReturn(userBidder);
		when(userService.findUserByUsername("seller")).thenReturn(userSeller);
		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(ad.getTitle()).thenReturn("title");
		when(ad.getLastBid()).thenReturn(100);
		when(ad.getUser()).thenReturn(userSeller);
		when(userSeller.getUsername()).thenReturn("seller");

		adC.placeBid(ad, binding, principal, redirect, id);
		verify(messageService).sendMessage(userSeller, userBidder, "Bidding confirmation: 100 on 'title'",
				"Congratulations! Your bid was accepted!\n\nTitle: title\nBid: 100\nURL: <a href='/ad?id=1'>"
						+ "/ad?id=1</a>");
		verify(messageService).sendMessage(userBidder, userSeller, "New bid: 100 on 'title'",
				"Congratulations! Someone just placed a bid on your property!\n\nTitle: title\nBid: 100\nBidder: bidder\nURL: <a href='/ad?id=1'>/ad?id=1</a>");

	}

	@Test
	public void placeBidRedirectAddFlashAttributes() {
		Principal principal = mock(Principal.class);
		BindingResult binding = mock(BindingResult.class);
		User userBidder = mock(User.class);
		User userSeller = mock(User.class);
		RedirectAttributes redirect = mock(RedirectAttributes.class);
		Long id = (long) 1;

		when(principal.getName()).thenReturn("bidder");
		when(userService.findUserByUsername("bidder")).thenReturn(userBidder);
		when(userService.findUserByUsername("seller")).thenReturn(userSeller);
		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(ad.getTitle()).thenReturn("title");
		when(ad.getLastBid()).thenReturn(100);
		when(ad.getUser()).thenReturn(userSeller);
		when(userSeller.getUsername()).thenReturn("seller");

		adC.placeBid(ad, binding, principal, redirect, id);
		verify(redirect).addFlashAttribute("confirmationMessage", "Congratulations! Your bid was accepted!");

	}

	@Test
	public void directBuy() {
		Principal principal = mock(Principal.class);
		User seller = mock(User.class);
		User buyer = mock(User.class);
		RedirectAttributes redirect = mock(RedirectAttributes.class);
		long id = (long) 1;

		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(ad.getStreet()).thenReturn("street");
		when(ad.getCity()).thenReturn("city");
		when(ad.getUser()).thenReturn(seller);
		when(principal.getName()).thenReturn("buyer");
		when(seller.getUsername()).thenReturn("seller");
		when(userService.findUserByUsername("buyer")).thenReturn(buyer);
		when(userService.findUserByUsername("seller")).thenReturn(seller);

		doNothing().when(ad).setStatus(0);
		doNothing().when(adService).saveAd(ad);

		String result = adC.directBuy(id, principal, redirect);
		assertEquals("redirect:/ad?id=1", result);
	}

	@Test
	public void directBuyMessagesTriggered() {
		Principal principal = mock(Principal.class);
		User seller = mock(User.class);
		User buyer = mock(User.class);
		RedirectAttributes redirect = mock(RedirectAttributes.class);
		long id = (long) 1;
		String buyerText = "Congratulations.\nYou have bought an actual property that exists in the real world. And you have done this with the amazing powers of the Internet. Did you know that in june 2016, over 3.6 billion users were logged on to the Internet? \nYour property is located in city at street. Please contact the user seller to conclude your bargain.\nYours truly, the amazing Flatfindr team\nPS: This  message was automatically created by the Home Portal system.\nPPS: Isn't the Internet amazing?";
		String sellerText = "Congratulations.\nYour actual property that exists in the real world has been bought. And this was done with the amazing powers of the Internet. Did you know that in june 2016, over 3.6 billion users were logged on to the Internet? \nThe property you sold is located in city at street. Please contact the user buyer to conclude your sale.\nYours truly, the amazing Flatfindr team\nPS: This  message was automatically created by the Home Portal system.\nPPS: Isn't the Internet amazing?";

		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(ad.getStreet()).thenReturn("street");
		when(ad.getCity()).thenReturn("city");
		when(ad.getUser()).thenReturn(seller);
		when(principal.getName()).thenReturn("buyer");
		when(seller.getUsername()).thenReturn("seller");
		when(userService.findUserByUsername("buyer")).thenReturn(buyer);
		when(userService.findUserByUsername("seller")).thenReturn(seller);

		doNothing().when(ad).setStatus(0);
		doNothing().when(adService).saveAd(ad);

		adC.directBuy(id, principal, redirect);
		verify(messageService).sendMessage(seller, buyer, "You have bought a property in city at street.", buyerText);
		verify(messageService).sendMessage(buyer, seller, "Your property in city at street has been sold.", sellerText);
	}

	@Test
	public void directBuyRedirectAddFlashAttributes() {
		Principal principal = mock(Principal.class);
		User seller = mock(User.class);
		User buyer = mock(User.class);
		RedirectAttributes redirect = mock(RedirectAttributes.class);
		long id = (long) 1;

		when(adService.getAdById(anyLong())).thenReturn(ad);
		when(ad.getStreet()).thenReturn("street");
		when(ad.getCity()).thenReturn("city");
		when(ad.getUser()).thenReturn(seller);
		when(principal.getName()).thenReturn("buyer");
		when(seller.getUsername()).thenReturn("seller");
		when(userService.findUserByUsername("buyer")).thenReturn(buyer);
		when(userService.findUserByUsername("seller")).thenReturn(seller);

		doNothing().when(ad).setStatus(0);
		doNothing().when(adService).saveAd(ad);

		adC.directBuy(id, principal, redirect);
		verify(redirect).addFlashAttribute("confirmationMessage",
				"You have successfully bought this property. Check your message inbox for further details.");
	}

}