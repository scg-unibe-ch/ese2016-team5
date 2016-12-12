package ch.unibe.ese.team1.controller;

import java.security.Principal;
import java.util.ArrayList;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ch.unibe.ese.team1.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team1.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team1.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.SignupService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.UserUpdateService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.log.LogMain;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.UserDao;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * Handles all requests concerning user accounts and profiles.
 */
@Controller
public class ProfileController {

    LogMain mainlog = new LogMain();

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private static final String CLIENT = "68304295039-fvgg84j5itsnfupqsfsbeb8ogu2d0vlg.apps.googleusercontent.com";

    @Autowired
    private SignupService signupService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUpdateService userUpdateService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private AdService adService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDetailsManager userManager;

    /**
     * Process token from Google
     */
    @RequestMapping(value = "/tokensignin", method = RequestMethod.POST)
    public @ResponseBody
    String googleLogin(@RequestParam("token") String token) throws GeneralSecurityException, IOException {
        
        mainlog.log.warning("ProfileController method googleLogin received a request with the following token: " + token);
        
        JacksonFactory jacksonFactory = new JacksonFactory();
        NetHttpTransport transport = new NetHttpTransport();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
            .setAudience(Arrays.asList(CLIENT))
            .setIssuer("accounts.google.com")
            .build();

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String lastName = (String) payload.get("family_name");
            String firstName = (String) payload.get("given_name");
            String email = payload.getEmail();

            // See if user exists
            User user = userService.findUserByUsername(email);

            // If not, create entry
            if (user == null) {
                String password = userService.createPassword();

                user = new User();
                user.setEmail(email);
                user.setUsername(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPassword(password);
                user.setEnabled(true);
                user.setGender(Gender.UNKNOWN);

                Set<UserRole> userRoles = new HashSet<>();
                UserRole role = new UserRole();
                role.setRole(DEFAULT_ROLE);
                role.setUser(user);
                userRoles.add(role);
                user.setUserRoles(userRoles);

                userDao.save(user);
            }
            mainlog.log.warning("ProfileController method googleLogin processed a request with the following token: " + token);
            return "{'status':'success', 'email':'" + user.getUsername() + "', 'password':'" + user.getPassword() + "'}";

        } else {
            mainlog.log.warning("ProfileController method googleLogin caused an error with the following token: " + token);
            return "{'status':'error', message:'Invalid Token'";
        }
    }

    /**
     * Returns the login page.
     */
    @RequestMapping(value = "/login")
    public ModelAndView loginPage() {
        mainlog.log.warning("ProfileController method loginPage received a request");
        ModelAndView model = new ModelAndView("login");
        mainlog.log.warning("ProfileController method loginPage processed a request");
        return model;
    }

    /**
     * Returns the signup page.
     */
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signupPage() {
        mainlog.log.warning("ProfileController method signupPage received a request");
        ModelAndView model = new ModelAndView("signup");
        model.addObject("signupForm", new SignupForm());
        mainlog.log.warning("ProfileController method signupPage processed a request");
        return model;
    }

    /**
     * Validates the signup form and on success persists the new user.
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView signupResultPage(@Valid SignupForm signupForm,
            BindingResult bindingResult) {
        mainlog.log.warning("ProfileController method signupResultPage received a request with the following principal: " + signupForm.toString());
        ModelAndView model;
        if (!bindingResult.hasErrors()) {
            signupService.saveFrom(signupForm);
            model = new ModelAndView("login");
            model.addObject("confirmationMessage", "Signup complete!");
        } else {
            model = new ModelAndView("signup");
            model.addObject("signupForm", signupForm);
        }
        mainlog.log.warning("ProfileController method signupResultPage processed request with the following principal: " + signupForm.toString());
        return model;
    }

    /**
     * Checks and returns whether a user with the given email already exists.
     */
    @RequestMapping(value = "/signup/doesEmailExist", method = RequestMethod.POST)
    public @ResponseBody
    boolean doesEmailExist(@RequestParam String email) {
        mainlog.log.warning("ProfileController method doesEmailExist received a request with the following email: " + email);
        return signupService.doesUserWithUsernameExist(email);
    }

    /**
     * Shows the edit profile page.
     */
    @RequestMapping(value = "/profile/editProfile", method = RequestMethod.GET)
    public ModelAndView editProfilePage(Principal principal) {
        mainlog.log.warning("AProfileController method editProfilePage received a request with the following principal: " + principal.toString());
        ModelAndView model = new ModelAndView("editProfile");
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        model.addObject("editProfileForm", new EditProfileForm());
        model.addObject("currentUser", user);
        mainlog.log.warning("AProfileController method editProfilePage processed request with the following principal: " + principal.toString());
        return model;
    }

    /**
     * Handles the request for editing the user profile.
     */
    @RequestMapping(value = "/profile/editProfile", method = RequestMethod.POST)
    public ModelAndView editProfileResultPage(
            @Valid EditProfileForm editProfileForm,
            BindingResult bindingResult, Principal principal) {
        mainlog.log.warning("ProfileController method editProfileResultPage received a request with the following principal: " + principal.toString());
        ModelAndView model;
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        if (!bindingResult.hasErrors()) {
            userUpdateService.updateFrom(editProfileForm);
            model = new ModelAndView("updatedProfile");
            model.addObject("message", "Your Profile has been updated!");
            model.addObject("currentUser", user);
            mainlog.log.warning("ProfileController method editProfileResultPage processed request with the following principal: " + principal.toString());
            return model;
        } else {
            model = new ModelAndView("updatedProfile");
            model.addObject("message",
                    "Something went wrong, please contact the WebAdmin if the problem persists!");
            mainlog.log.warning("ProfileController method editProfileResultPage caused an error with the following principal: " + principal.toString());
            return model;
        }
    }

    /**
     * Displays the public profile of the user with the given id.
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView user(@RequestParam("id") long id, Principal principal) {
        mainlog.log.warning("ProfileController method user received a request with the following id: " + id);
        ModelAndView model = new ModelAndView("user");
        User user = userService.findUserById(id);
        if (principal != null) {
            String username = principal.getName();
            User user2 = userService.findUserByUsername(username);
            long principalID = user2.getId();
            model.addObject("principalID", principalID);
        }
        model.addObject("user", user);
        model.addObject("messageForm", new MessageForm());
        mainlog.log.warning("ProfileController method user processed request with the following id: " + id);
        return model;
    }

    /**
     * Displays the schedule page of the currently logged in user.
     */
    @RequestMapping(value = "/profile/schedule", method = RequestMethod.GET)
    public ModelAndView schedule(Principal principal) {
        mainlog.log.warning("ProfileController method schedule received a request with the following principal: " + principal.toString());
        ModelAndView model = new ModelAndView("schedule");
        User user = userService.findUserByUsername(principal.getName());

        // visits, i.e. when the user sees someone else's property
        Iterable<Visit> visits = visitService.getVisitsForUser(user);
        model.addObject("visits", visits);

        // presentations, i.e. when the user presents a property
        Iterable<Ad> usersAds = adService.getAdsByUser(user);
        ArrayList<Visit> usersPresentations = new ArrayList<Visit>();

        for (Ad ad : usersAds) {
            try {
                usersPresentations.addAll((ArrayList<Visit>) visitService
                        .getVisitsByAd(ad));
            } catch (Exception e) {
            }
        }

        model.addObject("presentations", usersPresentations);
        mainlog.log.warning("ProfileController method schedule processed request with the following principal: " + principal.toString());
        return model;
    }

    /**
     * Returns the visitors page for the visit with the given id.
     */
    @RequestMapping(value = "/profile/visitors", method = RequestMethod.GET)
    public ModelAndView visitors(@RequestParam("visit") long id) {
        mainlog.log.warning("ProfileController method visitors received a request with the following id: " + id);
        ModelAndView model = new ModelAndView("visitors");
        Visit visit = visitService.getVisitById(id);
        Iterable<User> visitors = visit.getSearchers();

        model.addObject("visitors", visitors);

        Ad ad = visit.getAd();
        model.addObject("ad", ad);
        mainlog.log.warning("ProfileController method visitors processed request with the following id: " + id);
        return model;
    }

    /**
     * Returns the visitors page for the visit with the given id.
     */
    @RequestMapping(value = "/profile/changePremiumStatus", method = RequestMethod.GET)
    public String changePremiumStatus(Principal principal, @RequestParam("redirectUri") String redirectUri) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        userService.changePremium(user);
        return "redirect:" + redirectUri;
    }
}
