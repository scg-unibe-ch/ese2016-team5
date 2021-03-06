package ch.unibe.ese.team1.controller.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.IDateTime;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.AdPictureDao;

/**
 * Provides the services for editing ads in the database such as saving the
 * form, deleting pictures or filling the form. It uses the AdService class to
 * find the ads to edit and the PlaceAdForm class to get the information about
 * the ad.
 */
@Service
public class EditAdService {

	@Autowired
	private AdService adService;

	@Autowired
	private AdDao adDao;

	@Autowired
	private AdPictureDao adPictureDao;

	@Autowired
	private UserService userService;

	private IDateTime dateTime;

	/**
	 * Handles persisting an edited ad to the database.
	 *
	 * @param placeAdForm
	 *            the form to take the data from
	 * @param a
	 *            list of the file paths the pictures are saved under
	 * @param the
	 *            currently logged in user
	 */
	@Transactional
	public Ad saveFrom(PlaceAdForm placeAdForm, List<String> filePaths, User user, long adId) {
		Ad ad = adService.getAdById(adId);

		Date now = dateTime.getDate();
		ad.setCreationDate(now);

		ad.setTitle(placeAdForm.getTitle());
		ad.setStreet(placeAdForm.getStreet());
		ad.setType(placeAdForm.getType());

		// take the zipcode - first four digits
		String zip = placeAdForm.getCity().substring(0, 4);
		ad.setZipcode(Integer.parseInt(zip));
		ad.setCity(placeAdForm.getCity().substring(7));
		setAvailability(placeAdForm, ad);

		ad.setDirectBuyPrice(placeAdForm.getDirectBuyPrice());
		ad.setAuctionStartingPrice(placeAdForm.getAuctionStartingPrice());
		ad.setOfferType(placeAdForm.getOfferType());
		ad.setPricePerMonth(placeAdForm.getPrice());
		ad.setSquareFootage(placeAdForm.getSquareFootage());
		ad.setRoomDescription(placeAdForm.getRoomDescription());
		ad.setPreferences(placeAdForm.getPreferences());

		// ad description values
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
		ad.setUser(user);

		setPictures(filePaths, ad);
		setVisits(placeAdForm, ad);

		adDao.save(ad);
		return ad;
	}

	private void setVisits(PlaceAdForm placeAdForm, Ad ad) {
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

			if (ad.getVisits() != null) {
				// add existing visit
				for (Visit visit : ad.getVisits()) {
					visits.add(visit);
				}
			}
			ad.setVisits(visits);
		}
	}

	/**
	 * Saves the path to the picture files, the pictures are assumed to be
	 * uploaded at this point.
	 * 
	 * @param filePaths
	 *            - the paths to be added to the picutre files. List<String>
	 * @param ad
	 *            - the ad where the filepaths will be added.
	 */
	private void setPictures(List<String> filePaths, Ad ad) {
		List<AdPicture> pictures = new ArrayList<>();
		for (String filePath : filePaths) {
			AdPicture picture = new AdPicture();
			picture.setFilePath(filePath);
			pictures.add(picture);
		}
		// add existing pictures
		for (AdPicture pic : ad.getPictures()) {
			pictures.add(pic);
		}
		ad.setPictures(pictures);
	}

	/**
	 * Sets either the move in/out date or the auction ending date depending on
	 * the offer type (rent or auction).
	 * 
	 * @param placeAdForm
	 *            - the form where the dates are defined
	 * @param ad
	 *            - the ad to which the dates correspond
	 */
	private void setAvailability(PlaceAdForm placeAdForm, Ad ad) {
		Calendar calendar = Calendar.getInstance();
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12
		try {
			if (placeAdForm.getMoveInDate().length() >= 1) {
				int dayMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(0, 2));
				int monthMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(3, 5));
				int yearMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(6, 10));
				calendar.set(yearMoveIn, monthMoveIn - 1, dayMoveIn);
				ad.setMoveInDate(calendar.getTime());
			}

			if (placeAdForm.getMoveOutDate().length() >= 1) {
				int dayMoveOut = Integer.parseInt(placeAdForm.getMoveOutDate().substring(0, 2));
				int monthMoveOut = Integer.parseInt(placeAdForm.getMoveOutDate().substring(3, 5));
				int yearMoveOut = Integer.parseInt(placeAdForm.getMoveOutDate().substring(6, 10));
				calendar.set(yearMoveOut, monthMoveOut - 1, dayMoveOut);
				ad.setMoveOutDate(calendar.getTime());
			}

			if (placeAdForm.getAuctionEndingDate().length() >= 1) {
				int dayEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(0, 2));
				int monthEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(3, 5));
				int yearEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(6, 10));
				int hourEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(11, 13));
				int minEndingDate = Integer.parseInt(placeAdForm.getAuctionEndingDate().substring(14, 16));
				calendar.set(yearEndingDate, monthEndingDate - 1, dayEndingDate, hourEndingDate, minEndingDate, 0);
				ad.setAuctionEndingDate(calendar.getTime());
			}
		} catch (NumberFormatException e) {
		}
	}

	/**
	 * Removes the picture with the given id from the list of pictures in the ad
	 * with the given id.
	 */
	@Transactional
	public void deletePictureFromAd(long adId, long pictureId) {
		Ad ad = adService.getAdById(adId);
		List<AdPicture> pictures = ad.getPictures();
		AdPicture picture = adPictureDao.findOne(pictureId);
		pictures.remove(picture);
		ad.setPictures(pictures);
		adDao.save(ad);
	}

	/**
	 * Fills a Form with the data of an ad.
	 */
	public PlaceAdForm fillForm(Ad ad) {
		PlaceAdForm adForm = new PlaceAdForm();

		adForm.setRoomDescription(ad.getRoomDescription());
		adForm.setPreferences(ad.getPreferences());

		return adForm;
	}

	public void setDateTime(IDateTime date) {
		this.dateTime = date;
	}

}
