/**
 * BCeIDServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDServiceLocator extends org.apache.axis.client.Service implements ca.bc.gov.srm.farm.subscription.client.BCeIDService {

    public BCeIDServiceLocator() {
    }


    public BCeIDServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BCeIDServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BCeIDServiceSoap
    private java.lang.String BCeIDServiceSoap_address = "https://gws1.test.bceid.ca/webservices/Client/V7/BCeIDService.asmx";

    public java.lang.String getBCeIDServiceSoapAddress() {
        return BCeIDServiceSoap_address;
    }
    
    public void setBCeIDServiceSoapAddress(java.lang.String value) {
      BCeIDServiceSoap_address = value;
  }

    // The WSDD service name defaults to the port name.
    private java.lang.String BCeIDServiceSoapWSDDServiceName = "BCeIDServiceSoap";

    public java.lang.String getBCeIDServiceSoapWSDDServiceName() {
        return BCeIDServiceSoapWSDDServiceName;
    }

    public void setBCeIDServiceSoapWSDDServiceName(java.lang.String name) {
        BCeIDServiceSoapWSDDServiceName = name;
    }

    public ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType getBCeIDServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BCeIDServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBCeIDServiceSoap(endpoint);
    }

    public ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType getBCeIDServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_BindingStub _stub = new ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getBCeIDServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBCeIDServiceSoapEndpointAddress(java.lang.String address) {
        BCeIDServiceSoap_address = address;
    }


    // Use to get a proxy class for BCeIDServiceSoap12
    private java.lang.String BCeIDServiceSoap12_address = "https://gws1.test.bceid.ca/webservices/Client/V7/BCeIDService.asmx";

    public java.lang.String getBCeIDServiceSoap12Address() {
        return BCeIDServiceSoap12_address;
    }
    
    public void setBCeIDServiceSoap12Address(java.lang.String value) {
      BCeIDServiceSoap12_address = value;
  }

    // The WSDD service name defaults to the port name.
    private java.lang.String BCeIDServiceSoap12WSDDServiceName = "BCeIDServiceSoap12";

    public java.lang.String getBCeIDServiceSoap12WSDDServiceName() {
        return BCeIDServiceSoap12WSDDServiceName;
    }

    public void setBCeIDServiceSoap12WSDDServiceName(java.lang.String name) {
        BCeIDServiceSoap12WSDDServiceName = name;
    }

    public ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType getBCeIDServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BCeIDServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBCeIDServiceSoap12(endpoint);
    }

    public ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType getBCeIDServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap12Stub _stub = new ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getBCeIDServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBCeIDServiceSoap12EndpointAddress(java.lang.String address) {
        BCeIDServiceSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_BindingStub _stub = new ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_BindingStub(new java.net.URL(BCeIDServiceSoap_address), this);
                _stub.setPortName(getBCeIDServiceSoapWSDDServiceName());
                return _stub;
            }
            if (ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap12Stub _stub = new ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap12Stub(new java.net.URL(BCeIDServiceSoap12_address), this);
                _stub.setPortName(getBCeIDServiceSoap12WSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("BCeIDServiceSoap".equals(inputPortName)) {
            return getBCeIDServiceSoap();
        }
        else if ("BCeIDServiceSoap12".equals(inputPortName)) {
            return getBCeIDServiceSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDServiceSoap"));
            ports.add(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDServiceSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BCeIDServiceSoap".equals(portName)) {
            setBCeIDServiceSoapEndpointAddress(address);
        }
        else 
if ("BCeIDServiceSoap12".equals(portName)) {
            setBCeIDServiceSoap12EndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
