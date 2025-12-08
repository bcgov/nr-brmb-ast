/**
 * BCeIDService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public interface BCeIDService extends javax.xml.rpc.Service {
    public java.lang.String getBCeIDServiceSoapAddress();

    public ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType getBCeIDServiceSoap() throws javax.xml.rpc.ServiceException;

    public ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType getBCeIDServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getBCeIDServiceSoap12Address();

    public ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType getBCeIDServiceSoap12() throws javax.xml.rpc.ServiceException;

    public ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType getBCeIDServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
