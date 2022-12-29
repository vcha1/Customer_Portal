package com.my1stle.customer.portal.service.odoo;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfig;

public interface XmlRpcClientFactory {
    XmlRpcClient createClient(XmlRpcClientConfig config);
}