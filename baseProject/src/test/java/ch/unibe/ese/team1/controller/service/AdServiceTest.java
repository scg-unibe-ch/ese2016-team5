package ch.unibe.ese.team1.controller.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Gender;

import ch.unibe.ese.team1.model.Type;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.UserDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AnyOf.*;

/**
 * Responsible for testing all aspects of the AdService.class. There are two
 * main aspects to be covered here. Firstly, the creation of ads according to a
 * give place ad form and secondly, the search for specific ads given certain
 * filter criteria. The tests are mostly performed using mocked objects.
 * Nevertheless, for the whole search mechanism, the test data base is used. It
 * covers about 82% of the AdService Class. The missing 18% are mostly
 * assertions which are not to be tested.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
"file:src/main/webapp/WEB-INF/config/springData.xml",
"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
public class AdServiceTest {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private AdDao adDao;
    
    @Autowired
    private GeoDataService geo;
    
    private AdDao adDaoTest;
    private AdService adService;
    private PlaceAdForm adForm;
    private User user;
    private List<String> filePaths;
    private Date earliestMoveIn;
    private Date earliestMoveOut;
    private Date latestMoveIn;
    private Date latestMoveOut;
    private SearchForm search;
    
    @Before
    public void setup() {
        adService = new AdService();
        adForm = mock(PlaceAdForm.class);
        search = mock(SearchForm.class);
        adDaoTest = mock(AdDao.class);
        user = mock(User.class);
        filePaths = new ArrayList<>();
        filePaths.add("/img/test/ad1_1.jpg");
        
        doReturn(null).when(adDaoTest).save(any(Ad.class));
    }
    
    @Test
    public void saveFormAdForBasicRent() {
        mockBasicAdForm();
        adService.setAdDao(adDaoTest);
        adService.saveFrom(adForm, filePaths, user);
        Ad ad = adService.getTestAd();
        
        // Evaluation
        assertEquals(0, ad.getOfferType());
        assertEquals(Type.room, ad.getType());
        assertEquals("Title", ad.getTitle());
        assertEquals("Street", ad.getStreet());
        assertEquals("City", ad.getCity());
        assertEquals(0, ad.getDirectBuyPrice());
        assertEquals(0, ad.getAuctionStartingPrice());
        assertEquals(500, ad.getPricePerMonth());
        assertEquals(100, ad.getSquareFootage());
        assertEquals("Description", ad.getRoomDescription());
        assertEquals("Preferences", ad.getPreferences());
        assertFalse(ad.getSmokers());
        assertFalse(ad.getAnimals());
        assertFalse(ad.getGarden());
        assertFalse(ad.getBalcony());
        assertFalse(ad.getCellar());
        assertFalse(ad.getFurnished());
        assertFalse(ad.getCable());
        assertFalse(ad.getGarage());
        assertFalse(ad.getInternet());
        assertFalse(ad.getDishwasher());
        assertEquals(null, ad.getVisits());
        assertEquals(user, ad.getUser());
    }
    
    @Test
    public void saveFormAdForAdvancedRent() {
        mockAdvancedAdForm();
        adService.setAdDao(adDaoTest);
        adService.saveFrom(adForm, filePaths, user);
        Ad ad = adService.getTestAd();
        
        // Evaluation
        assertEquals(0, ad.getOfferType());
        assertEquals(Type.room, ad.getType());
        assertEquals("Title", ad.getTitle());
        assertEquals("Street", ad.getStreet());
        assertEquals("City", ad.getCity());
        assertEquals(0, ad.getDirectBuyPrice());
        assertEquals(0, ad.getAuctionStartingPrice());
        assertEquals(500, ad.getPricePerMonth());
        assertEquals(100, ad.getSquareFootage());
        assertEquals("Description", ad.getRoomDescription());
        assertEquals("Preferences", ad.getPreferences());
        assertTrue(ad.getSmokers());
        assertTrue(ad.getAnimals());
        assertTrue(ad.getGarden());
        assertTrue(ad.getBalcony());
        assertTrue(ad.getCellar());
        assertTrue(ad.getFurnished());
        assertTrue(ad.getCable());
        assertTrue(ad.getGarage());
        assertTrue(ad.getInternet());
        assertTrue(ad.getDishwasher());
        assertEquals(null, ad.getVisits());
        assertEquals(user, ad.getUser());
    }
    
    @Test
    public void saveFormAdForAuction() {
        mockBasicAdForm();
        adService.setAdDao(adDaoTest);
        when(adForm.getOfferType()).thenReturn(1);
        
        adService.saveFrom(adForm, filePaths, user);
        Ad ad = adService.getTestAd();
        
        assertEquals(1, ad.getOfferType());
    }
    
    @Test
    public void saveFormAdForSale() {
        mockBasicAdForm();
        adService.setAdDao(adDaoTest);
        when(adForm.getOfferType()).thenReturn(2);
        
        adService.saveFrom(adForm, filePaths, user);
        Ad ad = adService.getTestAd();
        
        assertEquals(2, ad.getOfferType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setAvailabilityDateMoveInNull() {
        mockBasicAdForm();
        when(adForm.getMoveInDate()).thenReturn(null);
        when(adForm.getOfferType()).thenReturn(0);
        
        adService.saveFrom(adForm, filePaths, user);
    }
    
    @Test
    public void setAvailabilityDateMoveOutNull() {
        mockBasicAdForm();
        when(adForm.getMoveOutDate()).thenReturn(null);
        when(adForm.getOfferType()).thenReturn(0);
        
        adService.setAdDao(adDaoTest);
        adService.saveFrom(adForm, filePaths, user);
        Ad ad = adService.getTestAd();
        assertEquals(null, ad.getMoveOutDate());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setAvailabilityDateAuctionEndingNull() throws ParseException {
        mockBasicAdForm();
        when(adForm.getAuctionEndingDate()).thenReturn(null);
        when(adForm.getOfferType()).thenReturn(1);
        
        adService.setAdDao(adDaoTest);
        adService.saveFrom(adForm, filePaths, user);
        
    }
    
    @Test
    public void saveFormWithVisits() {
        /*
         *
         * SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy"); Date
         * moveOut = formatter.parse("01-01-1970");
         * when(adForm.getMoveInDate()).thenReturn(null);
         * when(adForm.getMoveOutDate()).thenReturn("01-01-1970");
         */
    }
    
    @Test
    public void saveBid() {
        adService.setAdDao(adDaoTest);
        Ad ad = new Ad();
        Ad spyAd = spy(ad);
        Ad adFormData = mock(Ad.class);
        when(adDaoTest.findOne(anyLong())).thenReturn(spyAd);
        doNothing().when(spyAd).setViews(anyInt());
        when(adFormData.getLastBid()).thenReturn(100);
        
        Ad ad2 = adService.saveBid(adFormData, user, 0);
        
        assertEquals(100, ad2.getLastBid());
        assertEquals(user, ad2.getLastBidder());
        
    }
    
    @Test
    public void filterOfferTypeRent() {
        mockBasicSearchForm(search);
        int[] offerType = { 0 };
        when(search.getOfferType()).thenReturn(offerType);
        adService.filterOfferType();
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertEquals(0, ad.getOfferType());
        }
    }
    
    @Test
    public void filterOfferTypeAuction() {
        mockBasicSearchForm(search);
        int[] offerType = { 1 };
        when(search.getOfferType()).thenReturn(offerType);
        adService.filterOfferType();
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertEquals(1, ad.getOfferType());
        }
    }
    
    @Test
    public void filterOfferTypeSale() {
        mockBasicSearchForm(search);
        int[] offerType = { 2 };
        when(search.getOfferType()).thenReturn(offerType);
        adService.filterOfferType();
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertEquals(2, ad.getOfferType());
        }
    }
    
    @Test
    public void filterOfferTypeRentOrAuction() {
        mockBasicSearchForm(search);
        int[] offerType = { 0, 1 };
        when(search.getOfferType()).thenReturn(offerType);
        adService.filterOfferType();
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertThat(ad.getOfferType(), anyOf(is(0), is(1)));
            
        }
    }
    
    @Test
    public void filterOfferTypeRentOrSale() {
        mockBasicSearchForm(search);
        int[] offerType = { 0, 2 };
        when(search.getOfferType()).thenReturn(offerType);
        adService.filterOfferType();
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertThat(ad.getOfferType(), anyOf(is(0), is(2)));
            
        }
    }
    
    @Test
    public void filterOfferTypeSaleOrAuction() {
        mockBasicSearchForm(search);
        int[] offerType = { 1, 2 };
        when(search.getOfferType()).thenReturn(offerType);
        adService.filterOfferType();
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertThat(ad.getOfferType(), anyOf(is(1), is(2)));
            
        }
    }
    
    @Test
    public void filterOfferTypeInvalid() {
        mockBasicSearchForm(search);
        int[] offerType = { 3 };
        when(search.getOfferType()).thenReturn(offerType);
        adService.filterOfferType();
        List<Ad> filteredAds = adService.getFilteredAds();
        assertTrue(filteredAds.isEmpty());
        
    }
    
    @Test
    public void filterOfferTypeEmpty() {
        mockBasicSearchForm(search);
        int[] offerType = {};
        when(search.getOfferType()).thenReturn(offerType);
        adService.filterOfferType();
        List<Ad> filteredAds = adService.getFilteredAds();
        assertTrue(filteredAds.isEmpty());
        
    }
    
    @Test
    public void filterConstructionTypeRoom() {
        mockBasicSearchForm(search);
        
        String[] types = { "room" };
        int[] offerType = { 0, 1, 2 };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertThat(ad.getOfferType(), anyOf(is(0), is(1), is(2)));
            assertEquals(Type.room, ad.getType()); // verify
            // filterConstructionType
        }
    }
    
    @Test
    public void filterConstructionTypeStudio() {
        mockBasicSearchForm(search);
        
        String[] types = { "studio" };
        int[] offerType = { 0, 1, 2 };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertThat(ad.getOfferType(), anyOf(is(0), is(1), is(2)));
            assertEquals(Type.studio, ad.getType()); // verify
            // filterConstructionType
        }
    }
    
    @Test
    public void filterConstructionTypeProperty() {
        mockBasicSearchForm(search);
        
        String[] types = { "property" };
        int[] offerType = { 0, 1, 2 };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertThat(ad.getOfferType(), anyOf(is(0), is(1), is(2)));
            assertEquals(Type.property, ad.getType()); // verify
            // filterConstructionType
        }
    }
    
    @Test
    public void filterConstructionTypeRoomAndStudioAndProperty() {
        mockBasicSearchForm(search);
        
        String[] types = { "room", "studio", "property" };
        int[] offerType = { 0, 1, 2 };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertThat(ad.getOfferType(), anyOf(is(0), is(1), is(2)));
            assertThat(ad.getType(), anyOf(is(Type.room), is(Type.property), is(Type.studio)));
        }
    }
    
    @Test
    public void filterConstructionTypeInvalid() {
        mockBasicSearchForm(search);
        
        String[] types = { "tower" };
        int[] offerType = { 0, 1, 2 };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertTrue(filteredAds.isEmpty());
        
    }
    
    @Test
    public void filterAdMoveInAndOut() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            Date moveIn = ad.getMoveInDate();
            Date moveOut = ad.getMoveOutDate();
            assertTrue(moveIn.after(earliestMoveIn));
            assertTrue(moveIn.before(latestMoveIn));
            
            if (moveOut != null) { // they don't necessarily have a move out
                // date
                assertTrue(moveOut.after(earliestMoveOut));
                assertTrue(moveOut.before(latestMoveOut));
            }
        }
    }
    
    @Test
    public void filterMoveInOutOfScope() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        when(search.getEarliestMoveInDate()).thenReturn("01-01-2099");
        earliestMoveIn = formatter.parse("01-01-2099");
        
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertTrue(filteredAds.isEmpty());
    }
    
    @Test
    public void filterMoveOutOutOfScope() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        when(search.getLatestMoveOutDate()).thenReturn("01-01-1970");
        when(search.getEarliestMoveOutDate()).thenReturn("01-01-1970");
        latestMoveOut = formatter.parse("01-01-1970");
        
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
    }
    
    /**
     * The Move in date is a mandatory field in the place ad form for renting
     * apartments. So the user can actually not complete the ad creation whilst
     * leaving the move in date blank, so it is not possible to have a null
     * value as the move in date for a renting ad. This is why it should throw a
     * NullPointErexception.
     *
     * @throws ParseException
     */
    @Test(expected = NullPointerException.class)
    public void filterAdMoveInNull() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.setAdDao(adDaoTest);
        Ad ad = mock(Ad.class);
        List<Ad> results = new ArrayList<>();
        results.add(ad);
        when(adDaoTest.findByOfferType(anyInt())).thenReturn(results);
        when(ad.getMoveInDate()).thenReturn(null);
        when(ad.getOfferType()).thenReturn(0);
        when(ad.getType()).thenReturn(Type.room);
        when(ad.getZipcode()).thenReturn(3011);
        when(ad.getCity()).thenReturn("Bern");
        when(ad.getStatus()).thenReturn(1);
        adService.searchAndFilter(search);
        
    }
    
    @Test
    public void filterAdMoveOutNull() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        
        adService.setAdDao(adDaoTest);
        Ad ad = new Ad();
        Ad spyAd = spy(ad);
        List<Ad> results = new ArrayList<>();
        results.add(spyAd);
        when(adDaoTest.findByOfferType(anyInt())).thenReturn(results);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        Date moveIn = formatter.parse("10-12-2016");
        spyAd.setMoveInDate(moveIn);
        spyAd.setType(Type.room);
        spyAd.setOfferType(0);
        spyAd.setCity("Bern");
        spyAd.setZipcode(3011);
        spyAd.setStatus(1);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertEquals(spyAd, filteredAds.get(0));
        assertFalse(filteredAds.isEmpty());
    }
    
    @Test
    public void filterSearchEarliestMoveInNull() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getEarliestMoveInDate()).thenReturn(null);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            Date moveOut = ad.getMoveOutDate();
            if (moveOut != null) { // they don't necessarily have a move out
                // date
                assertTrue(moveOut.after(earliestMoveOut));
                assertTrue(moveOut.before(latestMoveOut));
            }
        }
    }
    
    @Test
    public void filterSearchEarliestNullLatestWithinScope() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getEarliestMoveInDate()).thenReturn(null);
        when(search.getEarliestMoveOutDate()).thenReturn(null);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        when(search.getLatestMoveOutDate()).thenReturn("01-01-1970");
        latestMoveOut = formatter.parse("01-01-1970");
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        
    }
    
    @Test
    public void filterSearchEarliestMoveInNullEarliestMoveOutNull() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getEarliestMoveInDate()).thenReturn(null);
        when(search.getEarliestMoveOutDate()).thenReturn(null);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        
    }
    
    @Test
    public void filterSearchLatestMoveInNullLatestMoveOutNull() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getLatestMoveInDate()).thenReturn(null);
        when(search.getLatestMoveOutDate()).thenReturn(null);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        
    }
    
    @Test
    public void filterLatestNullEarliestOutOfScope() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getLatestMoveInDate()).thenReturn(null);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        when(search.getEarliestMoveInDate()).thenReturn("01-01-2099");
        earliestMoveIn = formatter.parse("01-01-2099");
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertTrue(filteredAds.isEmpty());
    }
    
    @Test(expected = NullPointerException.class)
    public void filterLatestNullMoveInNull() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getLatestMoveInDate()).thenReturn(null);
        
        adService.setAdDao(adDaoTest);
        Ad ad = mock(Ad.class);
        List<Ad> results = new ArrayList<>();
        results.add(ad);
        when(adDaoTest.findByOfferType(anyInt())).thenReturn(results);
        when(ad.getMoveInDate()).thenReturn(null);
        when(ad.getOfferType()).thenReturn(0);
        when(ad.getType()).thenReturn(Type.room);
        when(ad.getZipcode()).thenReturn(3011);
        when(ad.getCity()).thenReturn("Bern");
        when(ad.getStatus()).thenReturn(1);
        adService.searchAndFilter(search);
    }
    
    @Test
    public void filterSearchLatestMoveInNull() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getLatestMoveInDate()).thenReturn(null);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            Date moveOut = ad.getMoveOutDate();
            if (moveOut != null) { // they don't necessarily have a move out
                // date
                assertTrue(moveOut.after(earliestMoveOut));
                assertTrue(moveOut.before(latestMoveOut));
            }
        }
    }
    
    @Test
    public void filterSearchEarliestMoveOutNull() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getEarliestMoveOutDate()).thenReturn(null);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            Date moveIn = ad.getMoveInDate();
            assertTrue(moveIn.after(earliestMoveIn));
            assertTrue(moveIn.before(latestMoveIn));
        }
    }
    
    @Test
    public void filterSearchLatestMoveOutNull() throws ParseException {
        mockBasicSearchForm(search);
        mockSearchFormDates(search);
        int[] offerType = { 0 }; // only rent ads have move in and out
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getLatestMoveOutDate()).thenReturn(null);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            Date moveIn = ad.getMoveInDate();
            assertTrue(moveIn.after(earliestMoveIn));
            assertTrue(moveIn.before(latestMoveIn));
        }
    }
    
    @Test
    public void filterForAvailableStatus() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        for (Ad ad : filteredAds) {
            assertEquals(1, ad.getStatus());
        }
    }
    
    /**
     * Test ad on test database with title Malibu-style Beachhouse has status
     * set to 0. So we check if this ad is listed in the results or if it is
     * properly removed from the search results.
     *
     * @throws ParseException
     */
    @Test
    public void filterForUnAvailableStatus() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertEquals(1, ad.getStatus());
            assertFalse(ad.getTitle().equals("Malibu-style Beachhouse"));
        }
    }
    
    /**
     * There are ads in the vicinity of Bern with Zip Codes 3011 and 3012.
     */
    @Test
    public void filterAdsWithinReach() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getRadius()).thenReturn(5);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertEquals("Bern", ad.getCity());
            int zip = ad.getZipcode();
            assertThat(zip, anyOf(is(3011), is(3012)));
        }
    }
    
    /**
     * There are two ads with Zip code 3011. Setting the radius to 1 should
     * return both ads.
     */
    @Test
    public void filterAdsZeroRadius() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getRadius()).thenReturn(0);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertTrue(filteredAds.isEmpty());
    }
    
    /**
     * List simply remains empty
     */
    @Test
    public void filterAdsNegativeRadius() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getRadius()).thenReturn(-10);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertTrue(filteredAds.isEmpty());
    }
    
    @Test
    public void filterSmoking() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getSmokers()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getSmokers());
        }
    }
    
    @Test
    public void filterAnimals() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getAnimals()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getAnimals());
        }
    }
    
    @Test
    public void filterGarden() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getGarden()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getGarden());
        }
    }
    
    @Test
    public void filterBalcony() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getBalcony()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getBalcony());
        }
    }
    
    @Test
    public void filterCellar() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getCellar()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getCellar());
        }
    }
    
    @Test
    public void filterFurnished() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getFurnished()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getFurnished());
        }
    }
    
    @Test
    public void filterCable() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getCable()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getCable());
        }
    }
    
    @Test
    public void filterGarage() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getGarage()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getGarage());
        }
    }
    
    @Test
    public void filterInternet() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getInternet()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getInternet());
        }
    }
    
    @Test
    public void filterDishwasher() {
        mockBasicSearchForm(search);
        mockSearchFormAllRoomsAndOfferTypes(search);
        when(search.getDishwasher()).thenReturn(true);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertFalse(filteredAds.isEmpty());
        for (Ad ad : filteredAds) {
            assertTrue(ad.getDishwasher());
        }
    }
    
    @Test
    public void sortPremiumUsersBefore() {
        User normalUser = mock(User.class);
        User premUser = mock(User.class);
        
        Ad adNormal = mock(Ad.class);
        Ad adPrem = mock(Ad.class);
        
        mockBasicSearchForm(search);
        int[] offerType = { 0 };
        String[] types = { "room" };
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getType()).thenReturn(types);
        
        List<Ad> results = new ArrayList<>();
        results.add(adNormal);
        results.add(adPrem);
        
        assertEquals(adNormal, results.get(0));
        assertEquals(adPrem, results.get(1));
        
        when(adDaoTest.findByOfferType(anyInt())).thenReturn(results);
        when(adNormal.getOfferType()).thenReturn(0);
        when(adNormal.getType()).thenReturn(Type.room);
        when(adNormal.getZipcode()).thenReturn(3011);
        when(adNormal.getCity()).thenReturn("Bern");
        when(adNormal.getStatus()).thenReturn(1);
        when(adNormal.getUser()).thenReturn(normalUser);
        
        when(adPrem.getOfferType()).thenReturn(0);
        when(adPrem.getType()).thenReturn(Type.room);
        when(adPrem.getZipcode()).thenReturn(3011);
        when(adPrem.getCity()).thenReturn("Bern");
        when(adPrem.getStatus()).thenReturn(1);
        when(adPrem.getUser()).thenReturn(premUser);
        
        when(normalUser.isPremiumUser()).thenReturn(false);
        when(premUser.isPremiumUser()).thenReturn(true);
        
        adService.setAdDao(adDaoTest);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertEquals(2, filteredAds.size());
        assertEquals(adPrem, filteredAds.get(0));
        assertEquals(adNormal, filteredAds.get(1));
        
    }
    
    @Test
    public void sortKeepPremiumUsersInFront() {
        User normalUser = mock(User.class);
        User premUser = mock(User.class);
        
        Ad adNormal = mock(Ad.class);
        Ad adPrem = mock(Ad.class);
        
        mockBasicSearchForm(search);
        int[] offerType = { 0 };
        String[] types = { "room" };
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getType()).thenReturn(types);
        
        List<Ad> results = new ArrayList<>();
        results.add(adPrem);
        results.add(adNormal);
        
        assertEquals(adPrem, results.get(0));
        assertEquals(adNormal, results.get(1));
        
        when(adDaoTest.findByOfferType(anyInt())).thenReturn(results);
        when(adNormal.getOfferType()).thenReturn(0);
        when(adNormal.getType()).thenReturn(Type.room);
        when(adNormal.getZipcode()).thenReturn(3011);
        when(adNormal.getCity()).thenReturn("Bern");
        when(adNormal.getStatus()).thenReturn(1);
        when(adNormal.getUser()).thenReturn(normalUser);
        
        when(adPrem.getOfferType()).thenReturn(0);
        when(adPrem.getType()).thenReturn(Type.room);
        when(adPrem.getZipcode()).thenReturn(3011);
        when(adPrem.getCity()).thenReturn("Bern");
        when(adPrem.getStatus()).thenReturn(1);
        when(adPrem.getUser()).thenReturn(premUser);
        
        when(normalUser.isPremiumUser()).thenReturn(false);
        when(premUser.isPremiumUser()).thenReturn(true);
        
        adService.setAdDao(adDaoTest);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertEquals(2, filteredAds.size());
        assertEquals(adPrem, filteredAds.get(0));
        assertEquals(adNormal, filteredAds.get(1));
        
    }
    
    @Test
    public void sortTwoPremiumUsers() {
        User premUser1 = mock(User.class);
        User premUser2 = mock(User.class);
        
        Ad adPrem1 = mock(Ad.class);
        Ad adPrem2 = mock(Ad.class);
        
        mockBasicSearchForm(search);
        int[] offerType = { 0 };
        String[] types = { "room" };
        when(search.getOfferType()).thenReturn(offerType);
        when(search.getType()).thenReturn(types);
        
        List<Ad> results = new ArrayList<>();
        results.add(adPrem2);
        results.add(adPrem1);
        
        assertEquals(adPrem2, results.get(0));
        assertEquals(adPrem1, results.get(1));
        
        when(adDaoTest.findByOfferType(anyInt())).thenReturn(results);
        when(adPrem1.getOfferType()).thenReturn(0);
        when(adPrem1.getType()).thenReturn(Type.room);
        when(adPrem1.getZipcode()).thenReturn(3011);
        when(adPrem1.getCity()).thenReturn("Bern");
        when(adPrem1.getStatus()).thenReturn(1);
        when(adPrem1.getUser()).thenReturn(premUser1);
        
        when(adPrem2.getOfferType()).thenReturn(0);
        when(adPrem2.getType()).thenReturn(Type.room);
        when(adPrem2.getZipcode()).thenReturn(3011);
        when(adPrem2.getCity()).thenReturn("Bern");
        when(adPrem2.getStatus()).thenReturn(1);
        when(adPrem2.getUser()).thenReturn(premUser2);
        
        when(premUser1.isPremiumUser()).thenReturn(true);
        when(premUser2.isPremiumUser()).thenReturn(true);
        
        adService.setAdDao(adDaoTest);
        adService.searchAndFilter(search);
        List<Ad> filteredAds = adService.getFilteredAds();
        assertEquals(2, filteredAds.size());
        assertEquals(adPrem2, filteredAds.get(0));
        assertEquals(adPrem1, filteredAds.get(1));
        
    }
    
    @Test
    public void getAdsByUser() {
        User ese = userDao.findByUsername("ese@unibe.ch");
        adService.setAdDao(adDao);
        Iterable<Ad> results = adService.getAdsByUser(ese);
        for (Ad ad : results) {
            assertEquals(ese, ad.getUser());
        }
    }
    
    @Test
    public void getNewestAds() throws ParseException {
        Ad adNew = mock(Ad.class);
        Ad adOld = mock(Ad.class);
        
        List<Ad> ads = new ArrayList<>();
        ads.add(adOld);
        ads.add(adNew);
        
        Date creationNew = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        Date creationOld = formatter.parse("01-01-1970");
        
        when(adNew.getCreationDate()).thenReturn(creationNew);
        when(adOld.getCreationDate()).thenReturn(creationOld);
        
        adService.setAdDao(adDaoTest);
        when(adDaoTest.findByStatus(anyInt())).thenReturn(ads);
        
        Iterable<Ad> newest = adService.getNewestAds(1);
        for (Ad ad : newest) {
            assertEquals(adNew, ad);
        }
    }
    
    /**
     * In order to test the saved ad, I need to get it back from the DB again,
     * so these two methods need to be tested together, normally we want to test
     * things isolated of course. Testing just the returned ad from saveFrom()
     * wouldn't answer the question whether the ad has been saved correctly to
     * the db.
     * 
     * @throws ParseException
     */
    @Test
    public void saveFromAndGetById() throws ParseException {
        
        // Preparation
        PlaceAdForm placeAdForm = preparePlaceAdForm();
        
        ArrayList<String> filePaths = new ArrayList<>();
        filePaths.add("/img/test/ad1_1.jpg");
        
        User hans = createUser("hans@kanns.ch", "password", "Hans", "Kanns", Gender.MALE);
        hans.setAboutMe("Hansi Hinterseer");
        userDao.save(hans);
        adService.setAdDao(adDao);
        adService.saveFrom(placeAdForm, filePaths, hans);
        
        Ad ad = new Ad();
        Iterable<Ad> ads = adService.getAllAds();
        Iterator<Ad> iterator = ads.iterator();
        
        while (iterator.hasNext()) {
            ad = iterator.next();
        }
        
        // Testing
        assertEquals(2, ad.getOfferType());
        assertTrue(ad.getSmokers());
        assertFalse(ad.getAnimals());
        assertEquals("Bern", ad.getCity());
        assertEquals(3012, ad.getZipcode());
        assertEquals("Test preferences", ad.getPreferences());
        assertEquals("Test Room description", ad.getRoomDescription());
        assertEquals(600, ad.getPricePerMonth());
        assertEquals(50, ad.getSquareFootage());
        assertEquals("title", ad.getTitle());
        assertEquals("Hauptstrasse 13", ad.getStreet());
        
    }
    
    private User createUser(String email, String password, String firstName, String lastName, Gender gender) {
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
    
    // ----------- Helper Methods to mock all the forms ------------------------
    
    private void mockBasicAdForm() {
        when(adForm.getOfferType()).thenReturn(0);
        when(adForm.getType()).thenReturn(Type.room);
        when(adForm.getTitle()).thenReturn("Title");
        when(adForm.getStreet()).thenReturn("Street");
        when(adForm.getCity()).thenReturn("3000 - City");
        when(adForm.getDirectBuyPrice()).thenReturn(0); // as it is for rent
        when(adForm.getAuctionStartingPrice()).thenReturn(0);
        when(adForm.getPrice()).thenReturn(500);
        when(adForm.getSquareFootage()).thenReturn(100);
        when(adForm.getRoomDescription()).thenReturn("Description");
        when(adForm.getPreferences()).thenReturn("Preferences");
        when(adForm.isSmokers()).thenReturn(false);
        when(adForm.isAnimals()).thenReturn(false);
        when(adForm.getGarden()).thenReturn(false);
        when(adForm.getBalcony()).thenReturn(false);
        when(adForm.getCellar()).thenReturn(false);
        when(adForm.isFurnished()).thenReturn(false);
        when(adForm.getCable()).thenReturn(false);
        when(adForm.getGarage()).thenReturn(false);
        when(adForm.getInternet()).thenReturn(false);
        when(adForm.getDishwasher()).thenReturn(false);
        when(adForm.getMoveInDate()).thenReturn("01-01-1970");
        when(adForm.getMoveOutDate()).thenReturn("01-01-1970");
        when(adForm.getAuctionEndingDate()).thenReturn("01-01-1970 01:01");
        when(adForm.getVisits()).thenReturn(null);
    }
    
    private void mockAdvancedAdForm() {
        mockBasicAdForm();
        when(adForm.isSmokers()).thenReturn(true);
        when(adForm.isAnimals()).thenReturn(true);
        when(adForm.getGarden()).thenReturn(true);
        when(adForm.getBalcony()).thenReturn(true);
        when(adForm.getCellar()).thenReturn(true);
        when(adForm.isFurnished()).thenReturn(true);
        when(adForm.getCable()).thenReturn(true);
        when(adForm.getGarage()).thenReturn(true);
        when(adForm.getInternet()).thenReturn(true);
        when(adForm.getDishwasher()).thenReturn(true);
    }
    
    private void mockBasicSearchForm(SearchForm search) {
        when(search.getCity()).thenReturn("3011 - Bern");
        when(search.getRadius()).thenReturn(10000);
        when(search.getSmokers()).thenReturn(false);
        when(search.getAnimals()).thenReturn(false);
        when(search.getBalcony()).thenReturn(false);
        when(search.getCable()).thenReturn(false);
        when(search.getCellar()).thenReturn(false);
        when(search.getFurnished()).thenReturn(false);
        when(search.getDishwasher()).thenReturn(false);
        when(search.getGarage()).thenReturn(false);
        when(search.getInternet()).thenReturn(false);
        when(search.getGarden()).thenReturn(false);
        
        adService.setAdDao(adDao);
        adService.setGeoDataService(geo);
        adService.setSearchForm(search);
    }
    
    private void mockSearchFormDates(SearchForm search) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        when(search.getEarliestMoveInDate()).thenReturn("01-01-2014");
        earliestMoveIn = formatter.parse("01-01-2014");
        when(search.getLatestMoveInDate()).thenReturn("01-01-2020");
        latestMoveIn = formatter.parse("01-01-2020");
        when(search.getEarliestMoveOutDate()).thenReturn("01-01-1970");
        earliestMoveOut = formatter.parse("01-01-1970");
        when(search.getLatestMoveOutDate()).thenReturn("01-01-2040");
        latestMoveOut = formatter.parse("01-01-2040");
    }
    
    private void mockSearchFormAllRoomsAndOfferTypes(SearchForm search) {
        int[] offerType = { 0, 1, 2 };
        String[] types = { "room", "studio", "tower" };
        when(search.getType()).thenReturn(types);
        when(search.getOfferType()).thenReturn(offerType);
    }
    
    private PlaceAdForm preparePlaceAdForm() {
        PlaceAdForm placeAdForm = new PlaceAdForm();
        placeAdForm.setCity("3012 - Bern");
        placeAdForm.setPreferences("Test preferences");
        placeAdForm.setRoomDescription("Test Room description");
        placeAdForm.setPrice(600);
        placeAdForm.setSquareFootage(50);
        placeAdForm.setTitle("title");
        placeAdForm.setStreet("Hauptstrasse 13");
        placeAdForm.setType(Type.studio);
        placeAdForm.setOfferType(2);
        placeAdForm.setSmokers(true);
        placeAdForm.setAnimals(false);
        placeAdForm.setGarden(true);
        placeAdForm.setBalcony(false);
        placeAdForm.setCellar(true);
        placeAdForm.setFurnished(false);
        placeAdForm.setCable(false);
        placeAdForm.setGarage(true);
        placeAdForm.setInternet(false);
        return placeAdForm;
    }
    
}
