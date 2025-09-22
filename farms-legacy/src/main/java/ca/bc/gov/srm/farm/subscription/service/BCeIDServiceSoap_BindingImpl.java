/**
 * BCeIDServiceSoap_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.subscription.client.*;


public class BCeIDServiceSoap_BindingImpl implements ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType{
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private static HashMap activatedSubscriptions = new HashMap();
	private static int numCalls = 0;
	
	private static final String INACTIVE_GUID = "INACTIVE_GUID";
	
	static {
		activatedSubscriptions.put("000-000-000-000", "AHOPKINS000000000000000000000000");
	}
	
  public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse searchBCeIDAccount(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchRequest bceidAccountSearchRequest) throws java.rmi.RemoteException {
  	return null;
  }

  public ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchResponse searchInternalAccount(ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest internalAccountSearchRequest) throws java.rmi.RemoteException {
  	return null;
  }

  public ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponse getAccountDetailList(ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest accountDetailListRequest) throws java.rmi.RemoteException { 
  	return null;
  }

  public ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse getAccountDetail(ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest accountDetailRequest) throws java.rmi.RemoteException {
  	return null;
  }
  
  public ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse createAccountRequest(ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest createAccountRequestRequest) throws java.rmi.RemoteException {
  	return null;
  }

  public ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse createSubscriptionBatch(ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchRequest createSubscriptionBatchRequest) throws java.rmi.RemoteException {
  	log.debug("BCeIDServiceSoap_BindingImpl.createSubscriptionBatch"); 
  	
  	Object[] objs = createSubscriptionBatchRequest.getBatch().getSubscriptionList();
  	BCeIDSubscription subscription = (BCeIDSubscription) objs[0]; // assume only one object
  	
  	boolean createdOk = createSubscription(subscription);
  	
  	CreateSubscriptionBatchResponse response = new CreateSubscriptionBatchResponse();
  	CreateSubscriptionBatchResponseItem[] responseItems = new CreateSubscriptionBatchResponseItem[1];
  	ResponseCode responseCode = ResponseCode.Success;
  	FailureCode failureCode = FailureCode.Void;
  	String message = "test sucess response";
  	
  	if(!createdOk) {
  		responseCode = ResponseCode.Failed;
  		failureCode = FailureCode.NoResults;
  		message = "test failure response";
  	}
  	
  	responseItems[0] = new CreateSubscriptionBatchResponseItem();
  	responseItems[0].setCode(responseCode);
  	responseItems[0].setFailureCode(failureCode);
  	responseItems[0].setMessage(message);
  	responseItems[0].setSubscription(subscription);
  	
  	response.setCode(responseCode);
  	response.setFailureCode(failureCode);
  	response.setMessage(message);
  	response.setResponseItemList(responseItems);
  	
  	return response;
  }
  
  private boolean createSubscription(BCeIDSubscription subscription) {
  	numCalls++;
  	
  	// pretend to fail every fourth time
  	if(numCalls % 4 == 0) {
  		return false;
  	}
  	
  	Date now = new Date();
  	String subscriptionCode = "" + now.getTime();
  	
  	//
  	// When the user activates the key using the BCeID GUI
  	// then his GUID gets mapped to the key. We can't 
  	// simulate this, so when getSubscriptionCodeList is called,
  	// just pretend the GUID being passed was just used to activate 
  	// the subscription.
  	// 
  	activatedSubscriptions.put(subscriptionCode, INACTIVE_GUID);
  	
  	subscription.setOsKey("WHAT_IS_THIS");
  	subscription.setSubscriptionCode(subscriptionCode);
  	subscription.setBatchNumber(1);
  	subscription.setBatchItemNumber(1);
  	
  	return true;
  }

  public ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse getSubscriptionCodeList(ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest subscriptionCodeListRequest) throws java.rmi.RemoteException {
  	log.debug("BCeIDServiceSoap_BindingImpl.getSubscriptionCodeList"); 
  	
  	String userGuid = subscriptionCodeListRequest.getUserGuid();
  	BCeIDSubscription[] subscriptions = getActiveSubscriptions(userGuid);
  	
  	SubscriptionCodeListResponse response = new SubscriptionCodeListResponse();
  	response.setCode(ResponseCode.Success);
  	response.setFailureCode(FailureCode.Void);
  	response.setMessage(null);
  	response.setSubscriptionList(subscriptions);
  	
  	return response;
  }
  
  
  private BCeIDSubscription[] getActiveSubscriptions(String userGuid) {
  	List codes = getCodesForGuid(userGuid);
  	BCeIDSubscription[] subscriptions = new BCeIDSubscription[codes.size()];
  	
  	BCeIDAccount account = new BCeIDAccount();
  	account.setGuid(new BCeIDString(true, userGuid));
  	
  	for(int ii = 0; ii < codes.size(); ii++) {
  		String subscriptionCode = (String) codes.get(ii);
  		subscriptions[ii] = new BCeIDSubscription();
  		
    	subscriptions[ii].setAccount(account);
    	subscriptions[ii].setCodeType(BCeIDSubscriptionCodeType.Regular);
    	subscriptions[ii].setScope(BCeIDSubscriptionScope.Business);
    	subscriptions[ii].setStatus(BCeIDSubscriptionStatus.Activated);
    	subscriptions[ii].setActivationExpiryDate(Calendar.getInstance());
    	subscriptions[ii].setCreatedOnDateTime(Calendar.getInstance());
    	subscriptions[ii].setDownloadDateTime(Calendar.getInstance());
    	subscriptions[ii].setSubscriptionCode(subscriptionCode);
  	}
  	
  	return subscriptions;
  }
  
  
  private List getCodesForGuid(String guidToLookFor) {
  	ArrayList codes = new ArrayList();
  	Iterator iter = activatedSubscriptions.keySet().iterator();
  	
  	while(iter.hasNext()) {
  		String code = (String) iter.next();
  		String guid = (String) activatedSubscriptions.get(code);
  		
  		if(guidToLookFor.equals(guid)) {
  			codes.add(code);
  		} else if(INACTIVE_GUID.equals(guid)) {
  			// pretend this user activate this code
  			codes.add(code);
  			activatedSubscriptions.put(code, guidToLookFor);
  		}
  	}
  	
  	return codes;
  }
}
