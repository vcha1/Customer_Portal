package com.my1stle.customer.portal.service.serviceapi;

import java.util.Optional;

public interface ServiceUsersApi {

    Optional<ExistingServiceUserDto> findById(long id) throws ServiceApiException;

    Optional<ExistingServiceUserDto> findByEmail(String email) throws ServiceApiException;

    ExistingServiceUserDto create(ServiceUserDto serviceUserDto) throws ServiceApiException;

}