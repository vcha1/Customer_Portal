package com.my1stle.customer.portal.serviceImpl.serviceapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my1stle.customer.portal.service.serviceapi.ErrorDto;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceUserDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiCredentials;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;
import com.my1stle.customer.portal.service.serviceapi.ServiceUserDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceUsersApi;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultUsersApi implements ServiceUsersApi {

    private final ServiceApiCredentials credentials;
    private final ObjectMapper objectMapper;

    private final static String USERS_RESOURCE_PATH = "users";

    @Autowired
    public DefaultUsersApi(ServiceApiCredentials credentials, ObjectMapper objectMapper) {
        this.credentials = credentials;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<ExistingServiceUserDto> findById(long id) throws ServiceApiException {

        HttpResponse<String> response = Unirest.get(endpoint().concat("/{userId}"))
                .header("x-api-key", this.credentials.getApiKey())
                .routeParam("userId", String.valueOf(id))
                .asString();

        if (response.isSuccess()) {
            return Optional.of(parse(response, ExistingServiceUserDto.class));
        }

        ErrorDto errorDto = parse(response, ErrorDto.class);
        throw new ServiceApiException(errorDto.getMessage(), HttpStatus.valueOf(response.getStatus()));

    }

    @Override
    public Optional<ExistingServiceUserDto> findByEmail(String email) throws ServiceApiException {

        HttpResponse<String> response = Unirest.get(endpoint())
                .header("x-api-key", this.credentials.getApiKey())
                .queryString("email", email)
                .asString();

        List<ExistingServiceUserDto> existingServiceUserDtos = parseAsListOf(response, new TypeReference<List<ExistingServiceUserDto>>() {
        });
        if (existingServiceUserDtos.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(existingServiceUserDtos.get(0));
        }

    }


    @Override
    public ExistingServiceUserDto create(ServiceUserDto serviceUserDto) throws ServiceApiException {

        HttpResponse<String> response = Unirest.post(endpoint())
                .header("x-api-key", this.credentials.getApiKey())
                .body(serviceUserDto)
                .asString();

        if (response.isSuccess()) {
            return parse(response, ExistingServiceUserDto.class);
        }

        ErrorDto errorDto = parse(response, ErrorDto.class);
        throw new ServiceApiException(errorDto.getMessage(), HttpStatus.valueOf(response.getStatus()));


    }


    private String endpoint() {
        return ServiceApiEndpointUtil.endpoint(this.credentials, USERS_RESOURCE_PATH);
    }

    private <T> T parse(HttpResponse<String> response, Class<T> clazz) {
        try {
            return objectMapper.readValue(response.getBody(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private <T> List<T> parseAsListOf(HttpResponse<String> response, TypeReference<List<T>> typeReference) {

        try {
            return objectMapper.readValue(response.getBody(), typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

}
