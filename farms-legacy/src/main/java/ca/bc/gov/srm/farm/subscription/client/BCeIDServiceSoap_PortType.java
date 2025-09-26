/**
 * BCeIDServiceSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public interface BCeIDServiceSoap_PortType extends java.rmi.Remote {

    /**
     * Search for all BCeID accounts.
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse searchBCeIDAccount(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchRequest bceidAccountSearchRequest) throws java.rmi.RemoteException;

    /**
     * Search for all internal accounts.
     */
    public ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse searchInternalAccount(ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest internalAccountSearchRequest) throws java.rmi.RemoteException;

    /**
     * Get details for a given list of accounts.
     */
    public ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse getAccountDetailList(ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest accountDetailListRequest) throws java.rmi.RemoteException;

    /**
     * Get details for a given account.
     */
    public ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse getAccountDetail(ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest accountDetailRequest) throws java.rmi.RemoteException;
    public ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse createSubscriptionBatch(ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchRequest createSubscriptionBatchRequest) throws java.rmi.RemoteException;
    public ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse createAccountRequest(ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest createAccountRequestRequest) throws java.rmi.RemoteException;
    public ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse getSubscriptionCodeList(ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest subscriptionCodeListRequest) throws java.rmi.RemoteException;
}
