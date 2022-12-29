package com.my1stle.customer.portal.serviceImpl.serviceapi;

import com.my1stle.customer.portal.service.serviceapi.*;
import kong.unirest.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

@Service
public class DefaultScheduleRequestApi implements  ServiceScheduleRequestApi{

    private final ServiceApiCredentials credentials;
    private final ObjectMapper objectMapper;
    final String SCHEDULE_RESOURCE_PATH = "schedule-request";
    final String SCHEDULE_REQUEST_INVALID = "\"Schedule Request Not Valid!\"";


    @Autowired
    public DefaultScheduleRequestApi(ServiceApiCredentials credentials, ObjectMapper objectMapper){
        this.credentials = credentials;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<ScheduleRequestDto> get(String token){

        String endpoint = ServiceApiEndpointUtil.endpoint(this.credentials, SCHEDULE_RESOURCE_PATH);

        HttpResponse<String> response = Unirest.get(endpoint)
                .header("x-api-key", this.credentials.getApiKey())
                .queryString("token",token)
                .asString();

        if(response.getBody().equals(SCHEDULE_REQUEST_INVALID)){
            return Optional.empty();
        }else{
            return Optional.of(parse(response, ScheduleRequestDto.class));

        }
    }

    private <T> T parse(HttpResponse<String> response, Class<T> clazz) {
        try {
            return objectMapper.readValue(response.getBody(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
