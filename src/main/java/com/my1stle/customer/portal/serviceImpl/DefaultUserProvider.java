package com.my1stle.customer.portal.serviceImpl;

import com.my1stle.customer.portal.service.UserProvider;
import org.baeldung.persistence.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserProvider implements UserProvider {

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
