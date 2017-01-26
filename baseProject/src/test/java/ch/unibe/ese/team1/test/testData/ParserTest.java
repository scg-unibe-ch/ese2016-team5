
package ch.unibe.ese.team1.test.testData;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.controller.service.AdService;

public class ParserTest {
	
	@Autowired
	private AdService adService;
	
	@Test(expected = NullPointerException.class)
	public void testLatestMoveInDate() {
		SearchForm searchForm = new SearchForm();
		int[] offerType = {2};
		String[] propertyType = {"Room", "Studio"};
		
		searchForm.setType(propertyType);
		searchForm.setOfferType(offerType);
		searchForm.setRadius(5);
		searchForm.setPrice(5000000);
		searchForm.setCity("3012 - Bern");
		searchForm.setLatestMoveInDate("12-10-2014");
		adService.queryResults(searchForm);
	}
}
