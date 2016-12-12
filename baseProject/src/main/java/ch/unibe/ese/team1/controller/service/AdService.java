package ch.unibe.ese.team1.controller.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.DateTime;
import ch.unibe.ese.team1.model.IDateTime;
import ch.unibe.ese.team1.model.comp.Location;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;

/**
 * This class is responsible for all operations concerning the placement and
 * retrieval of ads. This means creating an ad from a given place ad form and
 * afterwards search for ads and filter search results to only show specific
 * ads. It uses the AdDao interface to find ads in bulk for the search and
 * filter mechanism.
 *
 */
@Service
public class AdService {
    
    @Autowired
    private AdDao adDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private AlertDao alertDao;
    
    @Autowired
    private MessageDao messageDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GeoDataService geoDataService;
    
    private List<Ad> filteredAds;
    private Calendar calendar;
    private IDateTime now;
    private SearchForm searchForm;
    private Ad testAd; // for testing only
    
    public AdService() {
        this.filteredAds = new ArrayList<Ad>();
        this.calendar = Calendar.getInstance();
        this.now = new DateTime();
    }
        
    /**
     * Creates an ad according to the form the user has filled out for ad
     * placement. All the characteristics concerning the ad that were set in the
     * place ad form are transferred to the actual ad object.
     *
     * @param placeAdForm
     *            the form to take the data from
     * @param filePaths
     *            list of the file paths the pictures are saved under
     * @param user
     *            currently logged in user
     */
    @Transactional
    public Ad saveFrom(PlaceAdForm placeAdForm, List<String> filePaths, User user) {
        assert placeAdForm != null && user != null;
        
        Ad ad = new Ad();
        setTestAd(ad);
        
        // Set Meta-data
        ad.setCreationDate(now.getDate());
        ad.setOfferType(placeAdForm.getOfferType());
        ad.setType(placeAdForm.getType());
        
        // Set crucial Ad-Info
        ad.setTitle(placeAdForm.getTitle());
        ad.setStreet(placeAdForm.getStreet());
        // take the zipcode - first four digits
        String zip = placeAdForm.getCity().substring(0, 4);
        ad.setZipcode(Integer.parseInt(zip));
        ad.setCity(placeAdForm.getCity().substring(7));
        
        // Set Price
        ad.setDirectBuyPrice(placeAdForm.getDirectBuyPrice());
        ad.setAuctionStartingPrice(placeAdForm.getAuctionStartingPrice());
        ad.setPricePerMonth(placeAdForm.getPrice());
        
        // Set Characteristics & Preferences
        ad.setSquareFootage(placeAdForm.getSquareFootage());
        ad.setRoomDescription(placeAdForm.getRoomDescription());
        ad.setPreferences(placeAdForm.getPreferences());
        ad.setSmokers(placeAdForm.isSmokers());
        ad.setAnimals(placeAdForm.isAnimals());
        ad.setGarden(placeAdForm.getGarden());
        ad.setBalcony(placeAdForm.getBalcony());
        ad.setCellar(placeAdForm.getCellar());
        ad.setFurnished(placeAdForm.isFurnished());
        ad.setCable(placeAdForm.getCable());
        ad.setGarage(placeAdForm.getGarage());
        ad.setInternet(placeAdForm.getInternet());
        ad.setDishwasher(placeAdForm.getDishwasher());
        
        setAvailabilityDate(placeAdForm, ad);
        setPicture(filePaths, ad); // adds the pictures paths to the ad
        setVisits(placeAdForm, ad); // adds visiting times to the ad
        ad.setUser(user);
        adDao.save(ad);
        
        assert ad != null;
        return ad;
    }
    
    /**
     * Saves the last bid that was made on the ad object i.e. the currently
     * highest bid.
     *
     * @param adFormData
     *            - the affected ad that was bit on
     * @param user
     *            - logged in user i.e currently highest bidder
     * @param adId
     *            - the id of the ad affected
     *
     * @return the ad with the currently highest bidder updated
     */
    @Transactional
    public Ad saveBid(Ad adFormData, User user, long adId) {
        assert adFormData != null && user != null && adId >= 0;
        Ad ad = getAdById(adId);
        ad.setLastBid(adFormData.getLastBid());
        ad.setLastBidder(user);
        Calendar calendar = Calendar.getInstance();
        ad.setLastBidDate(calendar.getTime());
        adDao.save(ad);
        
        assert ad != null;
        return ad;
    }
    
    /**
     * Returns all ads that match the parameters given by the search form. It
     * does so by first selecting all ads matching the selected offer types i.e.
     * for rent, for sale or for auction. Afterwards, this list is filtered i.e.
     * reduced by removing all ads not matching the rest of the criterias such
     * as wifi or dishwasher.
     *
     * @param searchForm
     *            the form to take the search parameters from
     * @return an Iterable of all search/filter results
     */
    @Transactional
    public Iterable<Ad> searchAndFilter(SearchForm searchForm) {
        assert searchForm != null;
        setSearchForm(searchForm);
        
        filterOfferType();
        filterConstructionType();
        filterAdsWithinReach();
        filterAvailabilityDates();
        filterSmoking();
        filterAnimals();
        filterGarden();
        filterBalcony();
        filterCellar();
        filterFurnished();
        filterGarage();
        filterInternet();
        filterCable();
        filterDishwasher();
        sortPremiumUsers();
        
        return filteredAds;
    }
    
    /**
     * Adds all ads with the selected offer type i.e. for sale, auction or rent
     * to the list of possible search results. This list gives the basis for any
     * further filtering i.e. all other filtering will be done by simply
     * removing ads out of this list, if they don't match the criteria specified
     * in the search form.
     *
     * @return the list of ads matching the selected offer type from the search
     *         form
     */
    protected void filterOfferType() {
        assert searchForm != null;
        List<Ad> rentAds = new ArrayList<>();
        List<Ad> auctionAds = new ArrayList<>();
        List<Ad> salesAds = new ArrayList<>();
        
        int[] offerType = searchForm.getOfferType();
        for (int i = 0; i < offerType.length; i++) {
            if (offerType[i] == 0) {
                Iterable<Ad> rentResults = adDao.findByOfferType(0);
                for (Ad ad : rentResults) {
                    rentAds.add(ad);
                }
                filteredAds.addAll(rentAds);
            }
            if (offerType[i] == 1) {
                Iterable<Ad> auctionResults = adDao.findByOfferType(1);
                for (Ad ad : auctionResults) {
                    auctionAds.add(ad);
                }
                filteredAds.addAll(auctionAds);
            }
            if (offerType[i] == 2) {
                Iterable<Ad> salesResults = adDao.findByOfferType(2);
                for (Ad ad : salesResults) {
                    salesAds.add(ad);
                }
                filteredAds.addAll(salesAds);
            }
        }
    }
    
    /**
     * Filters out any ads with a construction type that does not match the ones
     * specified in the search form. Construction Types can be either room,
     * studio or property.
     *
     * @param locatedResults
     *            - List of ads i.e. the results of previous filtering. The ads
     *            that don't match the construction type are removed from this
     *            list.
     * @return the updated list of filtered ads only containing the specified
     *         construction types
     */
    private void filterConstructionType() {
        assert searchForm != null;
        String[] type = searchForm.getType();
        Iterator<Ad> typeIterator = filteredAds.iterator();
        while (typeIterator.hasNext()) {
            Ad ad = typeIterator.next();
            boolean remove = true;
            for (String t : type) {
                if (ad.getType().name().equals(t)) {
                    remove = false;
                }
            }
            if (remove) {
                typeIterator.remove();
            }
        }
    }
    
    /**
     * Filters the list of ads to only contain ads matching the earliest and
     * latest move in and move out date. It does so, by removing the ads that do
     * not match the criteria. If those entries were left blank in the search
     * form, then the list is returned un-filtered.
     */
    private void filterAvailabilityDates() {
        assert searchForm != null;
        Date earliestInDate = null;
        Date latestInDate = null;
        Date earliestOutDate = null;
        Date latestOutDate = null;
        
        // parse move-in and move-out dates
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            earliestInDate = formatter.parse(searchForm.getEarliestMoveInDate());
        } catch (Exception e) {
        }
        try {
            latestInDate = formatter.parse(searchForm.getLatestMoveInDate());
        } catch (Exception e) {
        }
        try {
            earliestOutDate = formatter.parse(searchForm.getEarliestMoveOutDate());
        } catch (Exception e) {
        }
        try {
            latestOutDate = formatter.parse(searchForm.getLatestMoveOutDate());
        } catch (Exception e) {
        }
        
        // Only get results with status = 1 (available)
        Iterator<Ad> statusIterator = filteredAds.iterator();
        while (statusIterator.hasNext()) {
            Ad ad = statusIterator.next();
            if (ad.getStatus() != 1) {
                statusIterator.remove();
            }
        }
        
        // filtering by dates
        filteredAds = validateDate(filteredAds, true, earliestInDate, latestInDate);
        filteredAds = validateDate(filteredAds, false, earliestOutDate, latestOutDate);
    }
    
    /**
     * Filters out all ads that are not in the specified radius around the
     * chosen city. Both city and radius are defined in the search form.
     */
    private void filterAdsWithinReach() {
        assert searchForm != null;
        String city = searchForm.getCity().substring(7); // filter out zipcode
        
        // get the location that the user searched for and take the one with the
        // lowest zip code
        Location searchedLocation = geoDataService.getLocationsByCity(city).get(0);
        
        final int earthRadiusKm = 6380;
        List<Location> locations = geoDataService.getAllLocations();
        double radSinLat = Math.sin(Math.toRadians(searchedLocation.getLatitude()));
        double radCosLat = Math.cos(Math.toRadians(searchedLocation.getLatitude()));
        double radLong = Math.toRadians(searchedLocation.getLongitude());
        
        /*
         * calculate the distances (Java 8) and collect all matching zipcodes.
         * The distance is calculated using the law of cosines.
         * http://www.movable-type.co.uk/scripts/latlong.html
         */
        List<Integer> zipcodes = locations.parallelStream().filter(location -> {
            double radLongitude = Math.toRadians(location.getLongitude());
            double radLatitude = Math.toRadians(location.getLatitude());
            double distance = Math.acos(radSinLat * Math.sin(radLatitude)
                                        + radCosLat * Math.cos(radLatitude) * Math.cos(radLong - radLongitude)) * earthRadiusKm;
            return distance < searchForm.getRadius();
        }).map(location -> location.getZip()).collect(Collectors.toList());
        
        filteredAds = filteredAds.stream().filter(ad -> zipcodes.contains(ad.getZipcode()))
        .collect(Collectors.toList());
    }
    
    private void filterSmoking() {
        assert searchForm != null;
        if (searchForm.getSmokers()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getSmokers()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void filterAnimals() {
        assert searchForm != null;
        if (searchForm.getAnimals()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getAnimals()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void filterGarden() {
        assert searchForm != null;
        if (searchForm.getGarden()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getGarden()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void filterBalcony() {
        assert searchForm != null;
        if (searchForm.getBalcony()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getBalcony()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void filterCellar() {
        assert searchForm != null;
        if (searchForm.getCellar()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getCellar()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void filterFurnished() {
        assert searchForm != null;
        if (searchForm.getFurnished()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getFurnished()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void filterCable() {
        assert searchForm != null;
        if (searchForm.getCable()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getCable()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void filterGarage() {
        assert searchForm != null;
        if (searchForm.getGarage()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getGarage()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void filterInternet() {
        assert searchForm != null;
        if (searchForm.getInternet()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getInternet()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void filterDishwasher() {
        assert searchForm != null;
        if (searchForm.getDishwasher()) {
            Iterator<Ad> iterator = filteredAds.iterator();
            while (iterator.hasNext()) {
                Ad ad = iterator.next();
                if (!ad.getDishwasher()) {
                    iterator.remove();
                }
            }
        }
    }
    
    /**
     * Sorts the list of found ads (matching the criteria from the search form),
     * so that ads published by premium users are listed higher than the ads
     * published by non premium users.
     */
    private void sortPremiumUsers() {
        Collections.sort(filteredAds, new Comparator<Ad>() {
            @Override
            public int compare(Ad ad1, Ad ad2) {
                boolean ad1Premium = ad1.getUser().isPremiumUser();
                boolean ad2Premium = ad2.getUser().isPremiumUser();
                if (ad1Premium && !ad2Premium) {
                    return -1;
                } else if (!ad1Premium && ad2Premium) {
                    return 1;
                }
                return 0;
            }
        });
    }
    
    private List<Ad> validateDate(List<Ad> ads, boolean inOrOut, Date earliestDate, Date latestDate) {
        if (ads.size() > 0) {
            // Move in dates - Both earliest AND a latest date to compare to
            if (earliestDate != null) {
                if (latestDate != null) {
                    Iterator<Ad> iterator = ads.iterator();
                    while (iterator.hasNext()) {
                        Ad ad = iterator.next();
                        if (inOrOut || (!inOrOut && ad.getDate(inOrOut) != null)) {
                            if (ad.getDate(inOrOut).compareTo(earliestDate) < 0
                                || ad.getDate(inOrOut).compareTo(latestDate) > 0) {
                                iterator.remove();
                            }
                        }
                    }
                }
                // only an earliest date
                else {
                    Iterator<Ad> iterator = ads.iterator();
                    while (iterator.hasNext()) {
                        Ad ad = iterator.next();
                        if (inOrOut) {
                            if (ad.getDate(inOrOut).compareTo(earliestDate) < 0)
                                iterator.remove();
                        }
                    }
                }
            }
            // only a latest date
            else if (latestDate != null) {
                Iterator<Ad> iterator = ads.iterator();
                while (iterator.hasNext()) {
                    Ad ad = iterator.next();
                    if (!inOrOut && ad.getDate(inOrOut) != null) {
                        if ((ad.getDate(inOrOut).compareTo(latestDate) > 0))
                            iterator.remove();
                    }
                }
            } else {
            }
        }
        return ads;
    }
    
    /**
     * Returns all ads that were placed by the given user.
     */
    public Iterable<Ad> getAdsByUser(User user) {
        assert user != null;
        return adDao.findByUser(user);
    }
    
    /**
     * Returns the newest ads in the database. Used to display the latest ads on
     * the home screen.
     *
     * @param newest
     *            - Integer > 0 defines how many ads should be displayed in
     *            latest section
     */
    @Transactional
    public Iterable<Ad> getNewestAds(int newest) {
        assert newest >= 0; // Pre-condition
        
        Iterable<Ad> allAds = adDao.findByStatus(1);
        List<Ad> ads = new ArrayList<Ad>();
        for (Ad ad : allAds)
            ads.add(ad);
        Collections.sort(ads, new Comparator<Ad>() {
            @Override
            public int compare(Ad ad1, Ad ad2) {
                return ad2.getCreationDate().compareTo(ad1.getCreationDate());
            }
        });
        
        List<Ad> fourNewest = new ArrayList<Ad>();
        for (int i = 0; i < newest; i++)
            fourNewest.add(ads.get(i));
        return fourNewest;
    }
    
    /**
     * Gets the ad that has the given id.
     *
     * @param id
     *            the id that should be searched for
     * @return the found ad or null, if no ad with this id exists
     */
    @Transactional
    public Ad getAdById(long id) {
        assert id >= 0;
        Ad ad = adDao.findOne(id);
        ad.setViews(ad.getViews() + 1);
        adDao.save(ad);
        return ad;
    }
    
    /**
     * Sets the time aspect of the ad depending on the offer type. In case of an
     * apartment for rent, the time aspect is the move in and out date is set.
     * In case of an apartment for auction the due date of the auction is set.
     *
     * @param placeAdForm
     *            - the form specifying the ad's characteristics
     * @param ad
     *            - the ad object defined by the place ad form
     */
    private void setAvailabilityDate(PlaceAdForm placeAdForm, Ad ad) {
        assert placeAdForm != null && ad != null;
        try {
            if (placeAdForm.getOfferType() == 0) {
                if (placeAdForm.getMoveInDate() == null) {
                    throw new IllegalArgumentException("Move in date must not be empty");
                } else {
                    setMoveInDate(placeAdForm, ad);
                }
                if (placeAdForm.getMoveOutDate() != null) {
                    setMoveOutDate(placeAdForm, ad);
                }
            }
            if (placeAdForm.getOfferType() == 1) {
                if (placeAdForm.getAuctionEndingDate() == null) {
                    throw new IllegalArgumentException("Ending date must not be empty");
                } else {
                    setAuctionEndingDate(placeAdForm, ad);
                }
            }
        } catch (NumberFormatException e) {
        }
    }
    
    /**
     * Sets the possible visiting times of the apartment (subject of the ad). It
     * is specified by the place ad form.
     *
     * @param placeAdForm
     *            - the form specifying the ad's characteristics
     * @param ad
     *            - the ad object defined by the place ad form
     */
    private void setVisits(PlaceAdForm placeAdForm, Ad ad) {
        assert placeAdForm != null && ad != null;
        List<Visit> visits = new LinkedList<>();
        List<String> visitStrings = placeAdForm.getVisits();
        if (visitStrings != null) {
            for (String visitString : visitStrings) {
                Visit visit = new Visit();
                // format is 28-02-2014;10:02;13:14
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String[] parts = visitString.split(";");
                String startTime = parts[0] + " " + parts[1];
                String endTime = parts[0] + " " + parts[2];
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = dateFormat.parse(startTime);
                    endDate = dateFormat.parse(endTime);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                
                visit.setStartTimestamp(startDate);
                visit.setEndTimestamp(endDate);
                visit.setAd(ad);
                visits.add(visit);
            }
            
            ad.setVisits(visits);
        }
    }
    
    /**
     * Saves paths to picture files, which should be uploaded at this point
     *
     * @param filePaths
     *            - the paths to the picture files
     * @param ad
     *            - the ad to which the pictures are referring to
     */
    private void setPicture(List<String> filePaths, Ad ad) {
        assert ad != null;
        List<AdPicture> pictures = new ArrayList<>();
        for (String filePath : filePaths) {
            AdPicture picture = new AdPicture();
            picture.setFilePath(filePath);
            pictures.add(picture);
        }
        ad.setPictures(pictures);
    }
    
    /**
     * Sets the ending date of an ad with offer type 'auction'. This is the
     * date, where you can no longer bid on an ad, and by which it'll be sold to
     * the current highest bidder.
     * 
     * @param placeAdForm
     *            - the form specifying the ad's characteristics
     * @param ad
     *            - the ad object defined by the place ad form
     */
    private void setAuctionEndingDate(PlaceAdForm placeAdForm, Ad ad) {
        assert placeAdForm != null && ad != null;
        int dayEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(0, 2));
        int monthEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(3, 5));
        int yearEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(6, 10));
        int hourEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(11, 13));
        int minEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(14, 16));
        calendar.set(yearEndingDate, monthEndingDate - 1, dayEndingDate, hourEndingDate, minEndingDate, 0);
        ad.setAuctionEndingDate(calendar.getTime());
    }
    
    /**
     * Sets the move in date of an ad with offer type 'rent'. This is the date
     * by which the renter can move into the apartment.
     * 
     * @param placeAdForm
     *            - the form specifying the ad's characteristics
     * @param ad
     *            - the ad object defined by the place ad form
     */
    private void setMoveInDate(PlaceAdForm placeAdForm, Ad ad) {
        int dayMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(0, 2));
        int monthMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(3, 5));
        int yearMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(6, 10));
        calendar.set(yearMoveIn, monthMoveIn - 1, dayMoveIn);
        ad.setMoveInDate(calendar.getTime());
    }
    
    /**
     * Sets the move out date of an ad with offer type 'rent'. This is the date
     * by which the renter has to leave the apartment again.
     * 
     * @param placeAdForm
     *            - the form specifying the ad's characteristics
     * @param ad
     *            - the ad object defined by the place ad form
     */
    private void setMoveOutDate(PlaceAdForm placeAdForm, Ad ad) {
        assert placeAdForm != null && ad != null;
        int dayMoveOut = Integer.parseInt(placeAdForm.getMoveOutDate().substring(0, 2));
        int monthMoveOut = Integer.parseInt(placeAdForm.getMoveOutDate().substring(3, 5));
        int yearMoveOut = Integer.parseInt(placeAdForm.getMoveOutDate().substring(6, 10));
        calendar.set(yearMoveOut, monthMoveOut - 1, dayMoveOut);
        ad.setMoveOutDate(calendar.getTime());
    }
    
    void setSearchForm(SearchForm searchForm) {
        this.searchForm = searchForm;
    }
    
    @Transactional
    public Iterable<Ad> getOldAuctions() {
        Date date = new Date();
        Iterable<Ad> oldAuctions = adDao.findByStatusAndAuctionEndingDateBefore(1, date);
        return oldAuctions;
    }
    
    @Transactional
    public void saveAd(Ad ad) {
        adDao.save(ad);
    }
    
    /**
     * Checks if the email of a user is already contained in the given string.
     *
     * @param email
     *            the email string to search for
     * @param alreadyAdded
     *            the string of already added emails, which should be searched
     *            in
     *
     * @return true if the email has been added already, false otherwise
     */
    public Boolean checkIfAlreadyAdded(String email, String alreadyAdded) {
        email = email.toLowerCase();
        alreadyAdded = alreadyAdded.replaceAll("\\s+", "").toLowerCase();
        String delimiter = "[:;]+";
        String[] toBeTested = alreadyAdded.split(delimiter);
        for (int i = 0; i < toBeTested.length; i++) {
            if (email.equals(toBeTested[i])) {
                return true;
            }
        }
        return false;
    }
    
    // ------------For Testing purposes only ------------------------
    
    private void setTestAd(Ad ad) {
        testAd = ad;
    }
    
    protected Ad getTestAd() {
        return testAd;
    }
    
    protected void setAdDao(AdDao adDao) {
        this.adDao = adDao;
    }
    
    /** Returns all ads in the database */
    @Transactional
    public Iterable<Ad> getAllAds() {
        return adDao.findAll();
    }
    
    protected List<Ad> getFilteredAds() {
        return this.filteredAds;
    }
    
    protected void setGeoDataService(GeoDataService geo) {
        this.geoDataService = geo;
    }
}
