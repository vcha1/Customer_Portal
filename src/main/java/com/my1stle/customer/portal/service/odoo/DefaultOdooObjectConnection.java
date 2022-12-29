package com.my1stle.customer.portal.service.odoo;


import com.my1stle.customer.portal.service.odoo.XmlRpcClientFactory;
import com.google.common.collect.ImmutableMap;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Service
public class DefaultOdooObjectConnection implements OdooObjectConnection {
    private final XmlRpcAuthenticationInformation authInfo;
    private final XmlRpcClient xmlRpcClient;

    @Autowired
    public DefaultOdooObjectConnection(
            XmlRpcAuthenticationInformation authInfo,
            XmlRpcClientFactory xmlRpcClientFactory
    ) {
        this.authInfo = authInfo;

        URL serverWithPath;
        try {
            URI uriPath = authInfo.getServerUri().resolve("/xmlrpc/2/object");
            serverWithPath = uriPath.toURL();
        }
        catch (MalformedURLException e) {
            throw new IllegalArgumentException("Server URL is invalid", e);
        }

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(serverWithPath);
        this.xmlRpcClient = xmlRpcClientFactory.createClient(config);
    }

    @Override
    public List<Integer> createObjects(String objectType, List<Map<String, Object>> objects) {
        try {
            List<Object> remoteProcedureArgumentList = Collections.singletonList(objects);
            Object result = this.executeKw(objectType, "create", remoteProcedureArgumentList);

            if(result instanceof Integer) {
                return Collections.singletonList((Integer) result);
            }
            else if(result instanceof Object[]) {
                return Stream.of((Object[]) result)
                        .map(obj -> {
                            if(!(obj instanceof Integer)) {
                                String objType = obj == null ? "N/A" : obj.getClass().getSimpleName();
                                throw new RuntimeException("Failed to interpret creation response member (type was " + objType + ", value was " + obj + ")");
                            }
                            return (Integer)obj;
                        }).collect(Collectors.toList());
            }
            else {
                throw new RuntimeException("Failed to interpret creation response (type was " + result.getClass().getSimpleName() + ")");
            }
        }
        catch (XmlRpcException e) {
            throw new RuntimeException("Failed to create objects", e);
        }
    }

    @Override
    public List<Map<String, ?>> findObjects(String objectType, List<String> fields, List<Object> criteria) {
        try {
            List<Object> remoteProcedureArgumentList = Collections.singletonList(criteria);
            Object tempResult = this.executeKwWithDynamicFields(
                    objectType, "search_read",
                    remoteProcedureArgumentList,
                    ImmutableMap.<String, Object>builder()
                            .put("fields", fields)
                            .build()
            );

            List<Map<String, ?>> results = Stream.of((Object[])tempResult)
                    .map(value -> (Map<String, ?>)value)
                    .collect(Collectors.toList());

            return results;
        }
        catch (XmlRpcException e) {
            throw new RuntimeException("Failed to search", e);
        }
    }

    @Override
    public void updateObject(String objectType, Integer id, Map<String, Object> values) {
        this.updateObjects(objectType, Collections.singletonList(id), values);
    }

    @Override
    public void updateObjects(String objectType, List<Integer> ids, Map<String, Object> values) {
        try {
            List<Object> remoteProcedureArgumentList = Arrays.asList(ids, values);
            this.executeKw(objectType, "write", remoteProcedureArgumentList);
        }
        catch (XmlRpcException e) {
            throw new RuntimeException("Failed to update", e);
        }
    }

    private Object executeKw(String objectType, String keyword, Object payload) throws XmlRpcException {
        return this.xmlRpcClient.execute("execute_kw", asList(
                authInfo.getDatabase(), authInfo.getUserId(), authInfo.getPassword(),
                objectType, keyword,
                payload
        ));
    }

    private Object executeKwWithDynamicFields(String objectType, String keyword, List<Object> remoteProcedureArgumentList, Map<String, Object> dynamicFields) throws XmlRpcException {
        return this.xmlRpcClient.execute("execute_kw", asList(
                authInfo.getDatabase(), authInfo.getUserId(), authInfo.getPassword(),
                objectType, keyword,
                remoteProcedureArgumentList,
                dynamicFields
        ));
    }
}
