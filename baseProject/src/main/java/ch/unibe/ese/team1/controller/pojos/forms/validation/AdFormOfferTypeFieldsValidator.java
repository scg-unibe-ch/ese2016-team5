package ch.unibe.ese.team1.controller.pojos.forms.validation;

import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AdFormOfferTypeFieldsValidator implements ConstraintValidator<AdFormOfferTypeFields, PlaceAdForm> {

    boolean isValid;
    ConstraintValidatorContext constraintValidatorContext;

    @Override
    public void initialize(AdFormOfferTypeFields constraintAnnotation) {
    }

    @Override
    public boolean isValid(PlaceAdForm adForm, ConstraintValidatorContext constraintValidatorContext) {
        if (adForm == null) {
            return true;
        }

        this.isValid = true;
        this.constraintValidatorContext = constraintValidatorContext;

        // Rent
        if (adForm.getOfferType() == 0) {
            if (adForm.getMoveInDate().equals("")) {
                error("moveInDate", "Please indicate a move-in date");
            }
            if (adForm.getPrice() < 1) {
                error("price", "Has to be greater than 0");
            }
        }
        
        // Auction
        else if (adForm.getOfferType() == 1) {
            if (adForm.getAuctionEndingDate().length() != 16) {
                error("auctionEndingDate", "Please enter a properly formatted date.");
            } else {
                Calendar adEndingDate = Calendar.getInstance();
                int dayEndingDate = Integer.parseInt(adForm.getAuctionEndingDate().substring(0, 2));
                int monthEndingDate = Integer.parseInt(adForm.getAuctionEndingDate().substring(3, 5));
                int yearEndingDate = Integer.parseInt(adForm.getAuctionEndingDate().substring(6, 10));
                int hourEndingDate = Integer.parseInt(adForm.getAuctionEndingDate().substring(11, 13));
                int minEndingDate = Integer.parseInt(adForm.getAuctionEndingDate().substring(14, 16));
                adEndingDate.set(yearEndingDate, monthEndingDate - 1, dayEndingDate, hourEndingDate, minEndingDate, 0);

                if (adEndingDate == null) {
                    error("auctionEndingDate", "Please enter a properly formatted date.");
                } else {
                    Calendar now = Calendar.getInstance();
                    if (adEndingDate.before(now)) {
                        error("auctionEndingDate", "Please enter a date that's in the future.");
                    }
                }
            }
        } 

        // Direct buy
        else if (adForm.getOfferType() == 2) {
            if (adForm.getDirectBuyPrice() == 0) {
                error("directBuyPrice", "You sure would want to get something for your property?");
            }
        }

        return isValid;
    }

    private void error(String node, String msg) {
        this.isValid = false;
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext
            .buildConstraintViolationWithTemplate(msg)
            .addPropertyNode(node).addConstraintViolation();
    }
}
