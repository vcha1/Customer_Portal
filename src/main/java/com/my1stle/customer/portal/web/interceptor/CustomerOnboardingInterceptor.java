package com.my1stle.customer.portal.web.interceptor;

import com.my1stle.customer.portal.service.onboarding.OnboardingService;
import com.my1stle.customer.portal.web.controller.onboarding.CustomerOnboardingController;
import org.apache.commons.lang3.StringUtils;
import org.baeldung.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This implementation checks if the current user
 * has completed the onboarding process. If user has not
 * completed onboarding process, redirects to onboarding
 */
@Component
public class CustomerOnboardingInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOnboardingInterceptor.class);
    private String onboardingPath = CustomerOnboardingController.BASE_PATH;

    private OnboardingService onboardingService;

    @Autowired
    public CustomerOnboardingInterceptor(@Qualifier("customerOnboardingService") OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (null == authentication) {
            return true;
        }

        if (authentication instanceof AnonymousAuthenticationToken) {
            return true;
        }


        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRoles().stream().anyMatch(role -> StringUtils.equalsIgnoreCase(role.getName(), "ROLE_USER"))) {

            String requestPath = request.getRequestURI();

            if (StringUtils.startsWithAny(requestPath, "/css", "/js/", "/updatePassword", "/user", "/login", "/logout", "/error", "/dist", "/image", onboardingPath)) {
                return true;
            }

            if(!onboardingService.isRequiredToCompleteOnboarding(currentUser)) {
                return true;
            }

            if (!onboardingService.hasCompletedOnboarding(currentUser)) {
                LOGGER.info("{} has not completed onboarding. Redirecting...", currentUser.getEmail());
                response.sendRedirect(onboardingPath);
                return false;
            }

        }

        return true;

    }

}
