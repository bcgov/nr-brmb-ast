/**
 * BCeIDServiceSoap_BindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.service;


public class BCeIDServiceSoap_BindingSkeleton implements ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType, org.apache.axis.wsdl.Skeleton {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "bceidAccountSearchRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountSearchRequest"), ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("searchBCeIDAccount", _params, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchBCeIDAccountResult"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountSearchResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchBCeIDAccount"));
        _oper.setSoapAction("http://www.bceid.ca/webservices/Client/V7/searchBCeIDAccount");
        _myOperationsList.add(_oper);
        if (_myOperations.get("searchBCeIDAccount") == null) {
            _myOperations.put("searchBCeIDAccount", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("searchBCeIDAccount")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "internalAccountSearchRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountSearchRequest"), ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("searchInternalAccount", _params, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchInternalAccountResult"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountSearchResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchInternalAccount"));
        _oper.setSoapAction("http://www.bceid.ca/webservices/Client/V7/searchInternalAccount");
        _myOperationsList.add(_oper);
        if (_myOperations.get("searchInternalAccount") == null) {
            _myOperations.put("searchInternalAccount", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("searchInternalAccount")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "accountDetailListRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequest"), ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAccountDetailList", _params, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getAccountDetailListResult"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getAccountDetailList"));
        _oper.setSoapAction("http://www.bceid.ca/webservices/Client/V7/getAccountDetailList");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAccountDetailList") == null) {
            _myOperations.put("getAccountDetailList", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAccountDetailList")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "accountDetailRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailRequest"), ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAccountDetail", _params, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getAccountDetailResult"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getAccountDetail"));
        _oper.setSoapAction("http://www.bceid.ca/webservices/Client/V7/getAccountDetail");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAccountDetail") == null) {
            _myOperations.put("getAccountDetail", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAccountDetail")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createSubscriptionBatchRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchRequest"), ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createSubscriptionBatch", _params, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createSubscriptionBatchResult"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createSubscriptionBatch"));
        _oper.setSoapAction("http://www.bceid.ca/webservices/Client/V7/createSubscriptionBatch");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createSubscriptionBatch") == null) {
            _myOperations.put("createSubscriptionBatch", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createSubscriptionBatch")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createAccountRequestRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateAccountRequestRequest"), ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createAccountRequest", _params, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createAccountRequestResult"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateAccountRequestResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createAccountRequest"));
        _oper.setSoapAction("http://www.bceid.ca/webservices/Client/V7/createAccountRequest");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createAccountRequest") == null) {
            _myOperations.put("createAccountRequest", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createAccountRequest")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "subscriptionCodeListRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SubscriptionCodeListRequest"), ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getSubscriptionCodeList", _params, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getSubscriptionCodeListResult"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SubscriptionCodeListResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getSubscriptionCodeList"));
        _oper.setSoapAction("http://www.bceid.ca/webservices/Client/V7/getSubscriptionCodeList");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getSubscriptionCodeList") == null) {
            _myOperations.put("getSubscriptionCodeList", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getSubscriptionCodeList")).add(_oper);
    }

    public BCeIDServiceSoap_BindingSkeleton() {
        this.impl = new ca.bc.gov.srm.farm.subscription.service.BCeIDServiceSoap_BindingImpl();
    }

    public BCeIDServiceSoap_BindingSkeleton(ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType impl) {
        this.impl = impl;
    }
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse searchBCeIDAccount(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchRequest bceidAccountSearchRequest) throws java.rmi.RemoteException
    {
        ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse ret = impl.searchBCeIDAccount(bceidAccountSearchRequest);
        return ret;
    }

    public ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse searchInternalAccount(ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest internalAccountSearchRequest) throws java.rmi.RemoteException
    {
        ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse ret = impl.searchInternalAccount(internalAccountSearchRequest);
        return ret;
    }

    public ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse getAccountDetailList(ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest accountDetailListRequest) throws java.rmi.RemoteException
    {
        ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse ret = impl.getAccountDetailList(accountDetailListRequest);
        return ret;
    }

    public ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse getAccountDetail(ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest accountDetailRequest) throws java.rmi.RemoteException
    {
        ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse ret = impl.getAccountDetail(accountDetailRequest);
        return ret;
    }

    public ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse createSubscriptionBatch(ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchRequest createSubscriptionBatchRequest) throws java.rmi.RemoteException
    {
        ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse ret = impl.createSubscriptionBatch(createSubscriptionBatchRequest);
        return ret;
    }

    public ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse createAccountRequest(ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest createAccountRequestRequest) throws java.rmi.RemoteException
    {
        ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse ret = impl.createAccountRequest(createAccountRequestRequest);
        return ret;
    }

    public ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse getSubscriptionCodeList(ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest subscriptionCodeListRequest) throws java.rmi.RemoteException
    {
        ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse ret = impl.getSubscriptionCodeList(subscriptionCodeListRequest);
        return ret;
    }

}
