package com.my1stle.customer.portal.service.odoo;

public class OdooClient {
    XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
    XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
    OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);
}
