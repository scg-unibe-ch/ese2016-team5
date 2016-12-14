package ch.unibe.ese.team1.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ch.unibe.ese.team1.controller.pojos.PictureUploader;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.AlertService;
import ch.unibe.ese.team1.controller.service.BookmarkService;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.log.LogMain;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.comp.PictureMeta;
import ch.unibe.ese.team1.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller handles all requests concerning placing ads.
 *
 */
@Controller
public class PlaceAdController {

    LogMain mainlog = new LogMain();

    public static final String IMAGE_DIRECTORY = "/img/ads";

    /**
     * Used for generating a JSON representation of a given object.
     */
    private ObjectMapper objectMapper;

    /**
     * Used for uploading ad pictures. As long as the user did not place the ad
     * completely, the same picture uploader is used. Once the ad was placed,
     * this uploader is renewed.
     */
    private PictureUploader pictureUploader;

    /**
     * The place ad form that is shared between several requests, so that the
     * user only has to enter the data once. If an ad is placed, this form is
     * reset.
     */
    private PlaceAdForm placeAdForm;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private MessageService messageService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    /**
     * Shows the place ad form.
     */
    @RequestMapping(value = "/profile/placeAd", method = RequestMethod.GET)
    public ModelAndView placeAd() throws IOException {
        mainlog.log.warning("PlaceAdController method placeAd received a request");
        ModelAndView model = new ModelAndView("placeAd");

        String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
        if (pictureUploader == null) {
            pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
        }
        mainlog.log.warning("PlaceAdController method placeAd processed a request");
        return model;
    }

    /**
     * Uploads the pictures that are attached as multipart files to the request.
     * The JSON representation, that is returned, is generated manually because
     * the jQuery Fileupload plugin requires this special format.
     *
     * @return A JSON representation of the uploaded files
     */
    @RequestMapping(value = "/profile/placeAd/uploadPictures", method = RequestMethod.POST)
    public @ResponseBody
    String uploadPictures(MultipartHttpServletRequest request) {
        mainlog.log.warning("PlaceAdController method uploadPictures received a request with the following request: " + request.toString());
        List<MultipartFile> pictures = new LinkedList<>();
        Iterator<String> iter = request.getFileNames();

        while (iter.hasNext()) {
            pictures.add(request.getFile(iter.next()));
        }

        if (pictureUploader == null) {
            String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
            pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
        }
        List<PictureMeta> uploadedPicturesMeta = pictureUploader.upload(pictures);

        objectMapper = new ObjectMapper();
        String jsonResponse = "{\"files\": ";
        try {
            jsonResponse += objectMapper.writeValueAsString(uploadedPicturesMeta);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        jsonResponse += "}";
        mainlog.log.warning("PlaceAdController method uploadPictures processed request with the following request: " + request.toString());
        return jsonResponse;
    }

    /**
     * Validates the place ad form and persists the ad if successful. On
     * success, a redirect to the ad description page of the just created ad is
     * issued. If there were validation errors, the place ad form is displayed
     * again.
     */
    @RequestMapping(value = "/profile/placeAd", method = RequestMethod.POST)
    public ModelAndView create(@Valid PlaceAdForm placeAdForm,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Principal principal) {
        mainlog.log.warning("PlaceAdController method create received a request with the following principal: " + principal.toString());
        ModelAndView model = new ModelAndView("placeAd");
        if (!result.hasErrors()) {
            String username = principal.getName();
            User user = userService.findUserByUsername(username);

            List<String> fileNames = pictureUploader.getFileNames();
            Ad ad = adService.saveFrom(placeAdForm, fileNames, user);

            // triggers all alerts that match the placed ad
            alertService.triggerAlerts(ad);

            // reset the place ad form
            this.placeAdForm = null;
            // reset the picture uploader
            this.pictureUploader = null;

            model = new ModelAndView("redirect:/ad?id=" + ad.getId());
            redirectAttributes.addFlashAttribute("confirmationMessage",
                    "Ad placed successfully. You can take a look at it below.");
        } else {
            model = new ModelAndView("placeAd");
        }
        mainlog.log.warning("PlaceAdController method create processed request with the following principal: " + principal.toString());
        return model;
    }

    /**
     * Gets the descriptions for the pictures that were uploaded with the
     * current picture uploader.
     *
     * @return a list of picture descriptions or null if no pictures were
     * uploaded
     */
    @RequestMapping(value = "/profile/placeAd/getUploadedPictures", method = RequestMethod.POST)
    public @ResponseBody
    List<PictureMeta> getUploadedPictures() {
        mainlog.log.warning("PlaceAdController method getUploadedPictures received a request");
        if (pictureUploader == null) {
            return null;
        }
        mainlog.log.warning("PlaceAdController method getUploadedPictures processed a request");
        return pictureUploader.getUploadedPictureMetas();
    }

    /**
     * Deletes the uploaded picture at the given relative url (relative to the
     * webapp folder).
     */
    @RequestMapping(value = "/profile/placeAd/deletePicture", method = RequestMethod.POST)
    public @ResponseBody
    void deleteUploadedPicture(@RequestParam String url) {
        mainlog.log.warning("PlaceAdController method deleteUploadedPicture received a request with the following url: " + url);
        if (pictureUploader != null) {
            String realPath = servletContext.getRealPath(url);
            pictureUploader.deletePicture(url, realPath);
        }
        mainlog.log.warning("PlaceAdController method deleteUploadedPicture processed a request with the following url: " + url);
    }

    @ModelAttribute("placeAdForm")
    public PlaceAdForm placeAdForm() {
        mainlog.log.warning("PlaceAdController method placeAdForm received a request");
        if (placeAdForm == null) {
            placeAdForm = new PlaceAdForm();
        }
        mainlog.log.warning("PlaceAdController method placeAdForm processed a request");
        return placeAdForm;
    }
    
    
    //--------------for testing only -----------------------------
    public void setPictureUploader(PictureUploader uploader){
    	this.pictureUploader = uploader; 
    }
}
