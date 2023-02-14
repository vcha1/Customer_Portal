package com.my1stle.customer.portal.serviceImpl.serviceapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my1stle.customer.portal.service.serviceapi.CommentDto;
import com.my1stle.customer.portal.service.serviceapi.ErrorDto;
import com.my1stle.customer.portal.service.serviceapi.ExistingAttachmentDto;
import com.my1stle.customer.portal.service.serviceapi.ExistingCommentDto;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceCaseDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiCredentials;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;
import com.my1stle.customer.portal.service.serviceapi.ServiceCaseDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceCaseStatus;
import com.my1stle.customer.portal.service.serviceapi.ServiceCasesApi;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DefaultServiceCasesApi implements ServiceCasesApi {

    private final ServiceApiCredentials credentials;
    private final ObjectMapper objectMapper;

    private final static String CASES_RESOURCE_PATH = "cases";

    @Autowired
    public DefaultServiceCasesApi(ServiceApiCredentials credentials, ObjectMapper objectMapper) {
        this.credentials = credentials;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<ExistingServiceCaseDto> get(long id) throws ServiceApiException {
        System.out.println("get Case Id");

        HttpResponse<String> response = Unirest.get(endpoint().concat("/{caseId}"))
                .header("x-api-key", this.credentials.getApiKey())
                .routeParam("caseId", String.valueOf(id))
                .asString();
        if (response.isSuccess()) {
            return Optional.of(parse(response, ExistingServiceCaseDto.class));
        } else if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            return Optional.empty();
        } else {
            throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));
        }

    }


    @Override
    public List<ExistingServiceCaseDto> getByOdooIdTest(String odooId) throws ServiceApiException {
        System.out.println("get Odoo " + odooId);

        HttpResponse<String> response = Unirest.get(endpoint())
                .header("x-api-key", this.credentials.getApiKey())
                //.queryString("externalId", odooId)
                .queryString("odooId", odooId)
                .asString();

        if (response.isSuccess()) {
            return getExistingServiceCasesDto(response);
        }

        throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));

    }


    @Override
    public List<ExistingServiceCaseDto> getByExternalId(String externalId) throws ServiceApiException {
        System.out.println("get by external id " + externalId);

        HttpResponse<String> response = Unirest.get(endpoint())
                .header("x-api-key", this.credentials.getApiKey())
                .queryString("externalId", externalId)
                .asString();

        if (response.isSuccess()) {
            return getExistingServiceCasesDto(response);
        }

        throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));

    }

    @Override
    public List<ExistingServiceCaseDto> getByStatus(ServiceCaseStatus status) throws ServiceApiException {
        System.out.println("get by status");
        HttpResponse<String> response = Unirest.get(endpoint())
                .header("x-api-key", this.credentials.getApiKey())
                .queryString("status", status.name())
                .asString();

        if (response.isSuccess()) {
            return getExistingServiceCasesDto(response);
        }

        throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));
    }

    @Override
    public ExistingServiceCaseDto create(ServiceCaseDto serviceCaseDto) throws ServiceApiException {
        //System.out.println("create");

        HttpResponse<String> response = Unirest.post(endpoint())
                .header("x-api-key", this.credentials.getApiKey())
                .body(serviceCaseDto)
                .asString();

        if (response.isSuccess()) {
            return parse(response, ExistingServiceCaseDto.class);
        }

        throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));

    }

    @Override
    public ExistingServiceCaseDto update(long caseId, ServiceCaseDto caseDto) throws ServiceApiException {
        System.out.println("update");
        HttpResponse<String> response = Unirest.put(endpoint().concat("/{caseId}"))
                .header("x-api-key", this.credentials.getApiKey())
                .routeParam("caseId", String.valueOf(caseId))
                .body(caseDto)
                .asString();

        if (response.isSuccess()) {
            return parse(response, ExistingServiceCaseDto.class);
        } else {
            throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));
        }
    }

    @Override
    public ExistingCommentDto updateComment(long caseId, long commentId, CommentDto commentDto) throws ServiceApiException {
        System.out.println("update comment");
        Map<String, Object> routeParams = new HashMap<>();

        routeParams.put("caseId", String.valueOf(caseId));
        routeParams.put("commentId", String.valueOf(commentId));

        HttpResponse<String> response = Unirest.put(endpoint().concat("/{caseId}").concat("/comments/{commentId}"))
                .header("x-api-key", this.credentials.getApiKey())
                .routeParam(routeParams)
                .body(commentDto)
                .asString();

        if (response.isSuccess()) {
            return parse(response, ExistingCommentDto.class);
        } else {
            throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));
        }
    }

    @Override
    public ExistingCommentDto addComment(long caseId, CommentDto commentDto) throws ServiceApiException {
        System.out.println("add comment");
        Map<String, Object> routeParams = new HashMap<>();

        routeParams.put("caseId", String.valueOf(caseId));

        HttpResponse<String> response = Unirest.post(endpoint().concat("/{caseId}").concat("/comments"))
                .header("x-api-key", this.credentials.getApiKey())
                .routeParam(routeParams)
                .body(commentDto)
                .asString();

        if (response.isSuccess()) {
            return parse(response, ExistingCommentDto.class);
        } else {
            throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));
        }
    }

    @Override
    public ExistingAttachmentDto addAttachment(long caseId, long ownerId, String name, Resource resource) throws ServiceApiException {
        System.out.println("add attachment");

        HttpResponse<String> response = Unirest.post(endpoint().concat("/{caseId}").concat("/attachments"))
                .header("x-api-key", this.credentials.getApiKey())
                .header("x-user-id", String.valueOf(ownerId))
                .header("x-file-name", name)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
                .routeParam("caseId", String.valueOf(caseId))
                .body(convert(resource))
                .asString();

        if (response.isSuccess()) {
            return parse(response, ExistingAttachmentDto.class);
        }

        throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));

    }

    @Override
    public List<ExistingCommentDto> getComments(long caseId) throws ServiceApiException {
        System.out.println("get comments");
        Map<String, Object> routeParams = new HashMap<>();
        routeParams.put("caseId", String.valueOf(caseId));

        HttpResponse<String> response = Unirest.get(endpoint().concat("/{caseId}").concat("/comments"))
                .header("x-api-key", this.credentials.getApiKey())
                .routeParam(routeParams)
                .asString();

        if (response.isSuccess()) {
            return getExistingCommentDtos(response);
        }

        throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));
    }


    @Override
    public Optional<ExistingCommentDto> getCommentBy(long caseId, long commentId) throws ServiceApiException {
        System.out.println("get comment by");
        Map<String, Object> routeParams = new HashMap<>();
        routeParams.put("caseId", String.valueOf(caseId));
        routeParams.put("commentId", String.valueOf(commentId));

        HttpResponse<String> response = Unirest.get(endpoint().concat("/{caseId}").concat("/comments/{commentId}"))
                .header("x-api-key", this.credentials.getApiKey())
                .routeParam(routeParams)
                .asString();

        if (response.isSuccess()) {
            return Optional.of(parse(response, ExistingCommentDto.class));
        } else if (isNotFound(response)) {
            return Optional.empty();
        } else {
            throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));
        }

    }

    @Override
    public List<ExistingAttachmentDto> getAttachments(long caseId) throws ServiceApiException {
        System.out.println("get attachments");
        Map<String, Object> routeParams = new HashMap<>();
        routeParams.put("caseId", String.valueOf(caseId));

        HttpResponse<String> response = Unirest.get(endpoint().concat("/{caseId}").concat("/attachments"))
                .header("x-api-key", this.credentials.getApiKey())
                .routeParam(routeParams)
                .asString();

        if (response.isSuccess()) {
            return getExistingAttachmentDtos(response);
        }

        throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));
    }

    @Override
    public Optional<ExistingAttachmentDto> getAttachmentBy(long caseId, long attachmentId) throws ServiceApiException {
        System.out.println("get attachment by");
        Map<String, Object> routeParams = new HashMap<>();
        routeParams.put("caseId", String.valueOf(caseId));
        routeParams.put("attachmentId", String.valueOf(attachmentId));

        HttpResponse<String> response = Unirest.get(endpoint().concat("/{caseId}").concat("/attachments/{attachmentId}"))
                .header("x-api-key", this.credentials.getApiKey())
                .routeParam(routeParams)
                .asString();

        if (response.isSuccess()) {
            return Optional.of(parse(response, ExistingAttachmentDto.class));
        } else if (isNotFound(response)) {
            return Optional.empty();
        } else {
            throw new ServiceApiException(parse(response, ErrorDto.class).getMessage(), HttpStatus.valueOf(response.getStatus()));
        }
    }


    private String endpoint() {
        return ServiceApiEndpointUtil.endpoint(this.credentials, CASES_RESOURCE_PATH);
    }

    private List<ExistingServiceCaseDto> getExistingServiceCasesDto(HttpResponse<String> response) {
        return parseAsListOf(response, new TypeReference<List<ExistingServiceCaseDto>>() {
        });
    }

    private List<ExistingCommentDto> getExistingCommentDtos(HttpResponse<String> response) {
        return parseAsListOf(response, new TypeReference<List<ExistingCommentDto>>() {
        });
    }

    private List<ExistingAttachmentDto> getExistingAttachmentDtos(HttpResponse<String> response) {
        return parseAsListOf(response, new TypeReference<List<ExistingAttachmentDto>>() {
        });
    }

    private boolean isNotFound(HttpResponse<String> response) {
        return HttpStatus.NOT_FOUND.equals(HttpStatus.valueOf(response.getStatus()));
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

    private byte[] convert(Resource resource) {
        try {
            return FileCopyUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
