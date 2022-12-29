package com.my1stle.customer.portal.service.session;

import org.baeldung.persistence.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionUserProvider {
	public User getUser() {
		return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
