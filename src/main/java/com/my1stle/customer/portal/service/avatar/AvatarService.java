package com.my1stle.customer.portal.service.avatar;

import org.baeldung.persistence.model.User;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public interface AvatarService {

    /**
     *
     * @param user
     * @return resource representing the user's avatar
     */
    Resource getAvatar(User user);


    /**
     *
     * @param user
     * @param inputStream
     * @return resource representing the user's avatar
     */
    Resource setAvatar(User user, InputStream inputStream);

}