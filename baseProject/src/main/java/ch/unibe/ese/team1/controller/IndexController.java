package ch.unibe.ese.team1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.log.LogMain;

/**
 * This controller handles request concerning the home page and several other
 * simple pages.
 */
@Controller
public class IndexController {

    LogMain mainlog = new LogMain();

    @Autowired
    private AdService adService;

    /**
     * Displays the home page.
     */
    @RequestMapping(value = "/")
    public ModelAndView index() {
        mainlog.log.warning("IndexController method index received a request");
        ModelAndView model = new ModelAndView("index");
        model.addObject("newest", adService.getNewestAds(4));
        mainlog.log.warning("IndexController method index processed request");
        return model;
    }

    /**
     * Displays the about us page.
     */
    @RequestMapping(value = "/about")
    public ModelAndView about() {
        mainlog.log.warning("IndexController method about received a request");
        return new ModelAndView("about");
    }

    /**
     * Displays the disclaimer page.
     */
    @RequestMapping(value = "/disclaimer")
    public ModelAndView disclaimer() {
        mainlog.log.warning("IndexController method disclaimer received a request");
        return new ModelAndView("disclaimer");
    }
}
