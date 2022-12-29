package com.my1stle.customer.portal.web.controller.onboarding;

import com.my1stle.customer.portal.service.onboarding.OnboardingService;
import org.baeldung.persistence.model.User;
import org.baeldung.web.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

@Controller
@RequestMapping(value = "/onboarding")
public class CustomerOnboardingController {

    public static final String BASE_PATH = CustomerOnboardingController.class.getAnnotation(RequestMapping.class).value()[0];
    private static final String VIDEO_PATH = "/video";
    private static final String QUESTIONS_PATH = "/questions";

    public static final String START_ONBOARDING_PATH = VIDEO_PATH;

    private OnboardingService onboardingService;
    private MessageSource messages;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOnboardingController.class);

    @Autowired
    public CustomerOnboardingController(@Qualifier("customerOnboardingService") OnboardingService onboardingService, MessageSource messages) {
        this.onboardingService = onboardingService;
        this.messages = messages;
    }

    @GetMapping(value = "")
    public RedirectView redirectToStartOnboarding(@AuthenticationPrincipal User currentUser) {

        return getRedirectView(currentUser, START_ONBOARDING_PATH);

    }

    @GetMapping(value = VIDEO_PATH)
    public String viewVideo(@AuthenticationPrincipal User currentUser,
                            @RequestParam(name = "watch_again", required = false, defaultValue = "false") Boolean watchAgain) {

        if (!onboardingService.isRequiredToCompleteOnboarding(currentUser) || onboardingService.hasCompletedOnboarding(currentUser)) {
            LOGGER.info("{} has already completed onboarding or is not required to complete the onboarding process.", currentUser.getEmail());
            return "redirect:/homepage.html";
        } else if (currentUser.getOnboarding().getCompletedOnboardingVideoDateTime() != null) {

            if (watchAgain) {
                return "onboarding/video";
            }

            LOGGER.info("{} has already completed watching onboarding video. Redirecting...", currentUser.getEmail());
            return String.format("redirect:%s%s", BASE_PATH, QUESTIONS_PATH);

        } else {
            return "onboarding/video";
        }

    }

    @PostMapping(value = VIDEO_PATH)
    public RedirectView markVideoWatched(@AuthenticationPrincipal User currentUser) {

        onboardingService.completeOnboardingVideo(currentUser, ZonedDateTime.now());
        return getRedirectView(currentUser, QUESTIONS_PATH);

    }

    @GetMapping(value = QUESTIONS_PATH)
    public String viewQuiz(@AuthenticationPrincipal User currentUser, Model model) {

        if (!onboardingService.isRequiredToCompleteOnboarding(currentUser) || onboardingService.hasCompletedOnboarding(currentUser)) {
            LOGGER.info("{} has already completed onboarding or is not required to complete the onboarding process.", currentUser.getEmail());
            return "redirect:/homepage.html";
        } else {
            model.addAttribute("questions", onboardingService.getOnboardingQuestions());
            return "onboarding/quiz";
        }

    }

    @PostMapping(value = QUESTIONS_PATH)
    @ResponseBody
    public ResponseEntity<Object> completeQuiz(@AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {

        try {
            onboardingService.completeOnboardingForm(currentUser, ZonedDateTime.now());
        } catch (Exception e) {
            final GenericResponse bodyOfResponse = new GenericResponse(messages.getMessage("message.error", null, httpServletRequest.getLocale()), e.getMessage());
            return new ResponseEntity<Object>(bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        redirectAttributes.addAttribute("onboarding", true);
        return new ResponseEntity<Object>(new GenericResponse("success"), new HttpHeaders(), HttpStatus.OK);

    }

    private RedirectView getRedirectView(@AuthenticationPrincipal User currentUser, String path) {

        if (!onboardingService.isRequiredToCompleteOnboarding(currentUser) || onboardingService.hasCompletedOnboarding(currentUser)) {
            LOGGER.info("{} has already completed onboarding or is not required to complete the onboarding process.", currentUser.getEmail());
            return new RedirectView("/homepage.html");
        }

        return new RedirectView(String.format("%s%s", BASE_PATH, path));
    }

}
