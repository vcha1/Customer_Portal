package com.my1stle.customer.portal.application.listener;

import com.my1stle.customer.portal.service.serviceapi.ExistingServiceUserDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;
import com.my1stle.customer.portal.service.serviceapi.ServiceUserDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceUserRole;
import com.my1stle.customer.portal.service.serviceapi.ServiceUsersApi;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class OnSuccessLoginExportAsServiceApiUser implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final ServiceUsersApi serviceUsersApi;
    private final UserRepository userRepository;

    @Autowired
    public OnSuccessLoginExportAsServiceApiUser(ServiceUsersApi serviceUsersApi, UserRepository userRepository) {
        this.serviceUsersApi = serviceUsersApi;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(final InteractiveAuthenticationSuccessEvent onSuccessfulLoginEvent) {
        User currentUser = (User) onSuccessfulLoginEvent.getAuthentication().getPrincipal();
        if (null == currentUser.getServiceApiUserId()) {
            export(currentUser);
            refreshSecurityContext(currentUser, onSuccessfulLoginEvent);
        }
    }

    /**
     * @param currentUser current user
     * @implNote side effect persists {@link User#serviceApiUserId} once user has been exported successfully
     */
    private void export(User currentUser) {
        ExistingServiceUserDto existingServiceUserDto = createServiceApiUserFrom(currentUser);
        currentUser.setServiceApiUserId(existingServiceUserDto.getId());
        this.userRepository.save(currentUser);
    }

    private void refreshSecurityContext(User currentUser, InteractiveAuthenticationSuccessEvent onSuccessfulLoginEvent) {
        Authentication authentication = onSuccessfulLoginEvent.getAuthentication();
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(currentUser, authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    /**
     * @param currentUser currentUser
     * @return existingServiceUserDto
     * @implNote if user already exists tries to retrieve existin user by email
     */
    private ExistingServiceUserDto createServiceApiUserFrom(User currentUser) {
        String firstName = currentUser.getFirstName();
        String lastName = currentUser.getLastName();
        String email = currentUser.getEmail();
        ServiceUserDto serviceUserDto = new ServiceUserDto(firstName, lastName, email, Collections.singleton(ServiceUserRole.CUSTOMER));
        try {
            return this.serviceUsersApi.create(serviceUserDto);
        } catch (ServiceApiException e) {
            if (e.getHttpStatus().equals(HttpStatus.CONFLICT)) {
                return findByEmail(serviceUserDto.getEmail());
            }
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ExistingServiceUserDto findByEmail(String email) {
        try {
            return this.serviceUsersApi.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException(String.format("Unable to find existing service api user : %s", email)));
        } catch (ServiceApiException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
