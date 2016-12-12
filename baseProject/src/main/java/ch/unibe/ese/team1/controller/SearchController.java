package ch.unibe.ese.team1.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.log.LogMain;

/**
 * Handles all requests concerning the search for ads.
 */
@Controller
public class SearchController {

    LogMain mainlog = new LogMain();

    @Autowired
    private AdService adService;

    /**
     * The search form that is used for searching. It is saved between request
     * so that users don't have to enter their search parameters multiple times.
     */
    private SearchForm searchForm;

    /**
     * Shows the search ad page.
     */
    @RequestMapping(value = "/searchAd", method = RequestMethod.GET)
    public ModelAndView searchAd() {
        mainlog.log.warning("SearchController method searchAd received a request");
        ModelAndView model = new ModelAndView("searchAd");
        mainlog.log.warning("SearchController method searchAd processed request");
        return model;
    }

    /**
     * Gets the results when filtering the ads in the database by the parameters
     * in the search form.
     */
    @RequestMapping(value = "/results", method = RequestMethod.POST)
    public ModelAndView results(@Valid SearchForm searchForm,
            BindingResult result) {
        mainlog.log.warning("SearchController method results received a request with the following searchForm: " + searchForm.toString());
        if (!result.hasErrors()) {
            ModelAndView model = new ModelAndView("results");
            model.addObject("results", adService.searchAndFilter(searchForm));
            mainlog.log.warning("SearchController method results caused an error with the following searchForm: " + searchForm.toString());
            return model;
        } else {
            // go back
            mainlog.log.warning("SearchController method results processed request with the following searchForm: " + searchForm.toString());
            return searchAd();
        }
    }

    @ModelAttribute
    public SearchForm getSearchForm() {
        mainlog.log.warning("SearchController method getSearchForm received a request");
        if (searchForm == null) {
            searchForm = new SearchForm();
        }
        mainlog.log.warning("SearchController method getSearchForm processed a request");
        return searchForm;
    }
}
