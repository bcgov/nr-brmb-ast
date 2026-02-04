/**
 * BCeIDServiceSoap12Stub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDServiceSoap12Stub extends org.apache.axis.client.Stub implements ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[7];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchBCeIDAccount");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "bceidAccountSearchRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountSearchRequest"), ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountSearchResponse"));
        oper.setReturnClass(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchBCeIDAccountResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchInternalAccount");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "internalAccountSearchRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountSearchRequest"), ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountSearchResponse"));
        oper.setReturnClass(ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchInternalAccountResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAccountDetailList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "accountDetailListRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequest"), ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponse"));
        oper.setReturnClass(ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getAccountDetailListResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAccountDetail");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "accountDetailRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailRequest"), ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailResponse"));
        oper.setReturnClass(ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getAccountDetailResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createSubscriptionBatch");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createSubscriptionBatchRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchRequest"), ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchResponse"));
        oper.setReturnClass(ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createSubscriptionBatchResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createAccountRequest");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createAccountRequestRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateAccountRequestRequest"), ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateAccountRequestResponse"));
        oper.setReturnClass(ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createAccountRequestResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getSubscriptionCodeList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "subscriptionCodeListRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SubscriptionCodeListRequest"), ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SubscriptionCodeListResponse"));
        oper.setReturnClass(ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getSubscriptionCodeListResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

    }

    public BCeIDServiceSoap12Stub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public BCeIDServiceSoap12Stub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public BCeIDServiceSoap12Stub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">createAccountRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateAccountRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">createAccountRequestResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponseType1.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">createSubscriptionBatch");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatch.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">createSubscriptionBatchResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponseType0.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getAccountDetail");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.GetAccountDetail.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getAccountDetailList");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.GetAccountDetailList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getAccountDetailListResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.GetAccountDetailListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getAccountDetailResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.GetAccountDetailResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getSubscriptionCodeList");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.GetSubscriptionCodeList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getSubscriptionCodeListResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.GetSubscriptionCodeListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">searchBCeIDAccount");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SearchBCeIDAccount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">searchBCeIDAccountResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SearchBCeIDAccountResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">searchInternalAccount");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SearchInternalAccount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">searchInternalAccountResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SearchInternalAccountResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequestItem");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequestItem.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponseItem");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponseItem.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "ArrayOfAccountDetailListRequestItem");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequestItem[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequestItem");
            qName2 = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequestItem");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "ArrayOfAccountDetailListResponseItem");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponseItem[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponseItem");
            qName2 = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponseItem");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "ArrayOfAnyType");
            cachedSerQNames.add(qName);
            cls = java.lang.Object[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType");
            qName2 = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "anyType");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "ArrayOfBCeIDAccount");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccount[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccount");
            qName2 = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccount");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "ArrayOfBCeIDSubscription");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSubscription[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscription");
            qName2 = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscription");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "ArrayOfCreateSubscriptionBatchResponseItem");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponseItem[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchResponseItem");
            qName2 = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchResponseItem");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccount");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountContact");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContact.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountContactPreferenceType");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContactPreferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountContactPreferenceTypeCode");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContactPreferenceTypeCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountMatch");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountMatch.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountSearchRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountSearchResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountState");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountState.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountType");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountTypeCode");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAddress");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDAddress.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBNHubBusinessType");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDBNHubBusinessType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBNHubBusinessTypeCode");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDBNHubBusinessTypeCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBoolean");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusiness");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDBusiness.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusinessMatch");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessMatch.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusinessState");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessState.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusinessType");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusinessTypeCode");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessTypeCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDDateTime");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDDateTime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDIndividualIdentity");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDIndividualIdentity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDName");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDPropertyBase");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDPropertyBase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSearchableAccountType");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableAccountType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSearchableBusinessNumberVerified");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessNumberVerified.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSearchableBusinessState");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessState.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSearchableBusinessType");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDString.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscription");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSubscription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionBatch");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionBatch.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionCodeType");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionScope");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionScope.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionStatus");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDTypeBase");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.BCeIDTypeBase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateAccountRequestRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateAccountRequestResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchResponseItem");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponseItem.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "FailureCode");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.FailureCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountMatch");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.InternalAccountMatch.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountSearchRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountSearchResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyUsing");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.MatchPropertyUsing.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "PaginationRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.PaginationRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "PaginationResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.PaginationResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "RequestBase");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.RequestBase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "ResponseBase");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.ResponseBase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "ResponseCode");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.ResponseCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SearchRequestBase");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SearchRequestBase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SearchResponseBase");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SearchResponseBase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortBCeIDAccountOnProperty");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SortBCeIDAccountOnProperty.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortDirection");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SortDirection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortInternalAccountOnProperty");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SortInternalAccountOnProperty.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortOfSortBCeIDAccountOnProperty");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SortOfSortBCeIDAccountOnProperty.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortOfSortInternalAccountOnProperty");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SortOfSortInternalAccountOnProperty.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SubscriptionCodeListRequest");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SubscriptionCodeListResponse");
            cachedSerQNames.add(qName);
            cls = ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse searchBCeIDAccount(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchRequest bceidAccountSearchRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.bceid.ca/webservices/Client/V7/searchBCeIDAccount");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchBCeIDAccount"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {bceidAccountSearchRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse searchInternalAccount(ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest internalAccountSearchRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.bceid.ca/webservices/Client/V7/searchInternalAccount");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchInternalAccount"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {internalAccountSearchRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse getAccountDetailList(ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest accountDetailListRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.bceid.ca/webservices/Client/V7/getAccountDetailList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getAccountDetailList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {accountDetailListRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse getAccountDetail(ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest accountDetailRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.bceid.ca/webservices/Client/V7/getAccountDetail");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getAccountDetail"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {accountDetailRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse createSubscriptionBatch(ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchRequest createSubscriptionBatchRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.bceid.ca/webservices/Client/V7/createSubscriptionBatch");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createSubscriptionBatch"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {createSubscriptionBatchRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse createAccountRequest(ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest createAccountRequestRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.bceid.ca/webservices/Client/V7/createAccountRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createAccountRequest"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {createAccountRequestRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse getSubscriptionCodeList(ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest subscriptionCodeListRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.bceid.ca/webservices/Client/V7/getSubscriptionCodeList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getSubscriptionCodeList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {subscriptionCodeListRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
