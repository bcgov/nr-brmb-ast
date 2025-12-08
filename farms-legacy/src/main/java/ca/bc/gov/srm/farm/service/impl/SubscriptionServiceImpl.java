/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.dao.SubscriptionDAO;
import ca.bc.gov.srm.farm.domain.ClientSubscription;
import ca.bc.gov.srm.farm.domain.codes.SubscriptionStatusCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.GenerateSubscriptionResponse;
import ca.bc.gov.srm.farm.service.SubscriptionService;
import ca.bc.gov.srm.farm.subscription.client.BCeIDAccount;
import ca.bc.gov.srm.farm.subscription.client.BCeIDAccountType;
import ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode;
import ca.bc.gov.srm.farm.subscription.client.BCeIDServiceLocator;
import ca.bc.gov.srm.farm.subscription.client.BCeIDServiceSoap_PortType;
import ca.bc.gov.srm.farm.subscription.client.BCeIDSubscription;
import ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionBatch;
import ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType;
import ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionScope;
import ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionStatus;
import ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchRequest;
import ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse;
import ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponseItem;
import ca.bc.gov.srm.farm.subscription.client.RequestBase;
import ca.bc.gov.srm.farm.subscription.client.ResponseCode;
import ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest;
import ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListResponse;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.domain.AuthorizedUser;
import ca.bc.gov.srm.farm.user.UserFinder;
import ca.bc.gov.webade.user.WebADEUserInfo;


/**
 * SubscriptionServiceImpl.
 */
final class SubscriptionServiceImpl extends BaseService
  implements SubscriptionService {

  private Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * Get the authorized users for this client.
   *
   * @param   pin pin
   * @return List<AuthorizedUser>
   * @throws  ServiceException  on exception
   */
  @Override
  public List<?> getAuthorizedUsers(final Integer pin)
  throws ServiceException {
  	
  	List userList = null;
    Transaction transaction = null;
    SubscriptionDAO dao = new SubscriptionDAO();

    try {
      transaction = openTransaction();

      userList = dao.getAuthorizedUsers(transaction, pin);

      getNameAndPhone(userList);
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return userList;
  }


  /**
   * Get the subscriptions for this client.
   *
   * @param   pin  pin
   * @return List<ClientSubscription>
   * @throws  ServiceException  on exception
   */
  @Override
  public List getSubscriptions(final Integer pin)
  throws ServiceException {
  	List subList = null;
    Transaction transaction = null;
    SubscriptionDAO dao = new SubscriptionDAO();

    try {
      transaction = openTransaction();

      subList = dao.getSubscriptions(transaction, pin);

    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return subList;
  }


  /**
   * change the status of the subscription to "Revoked"
   *
   * @param   clientSubscriptionId  the subscription ID
   * @param   revisionCount         the revision count
   *
   * @throws  ServiceException  on exception
   */
  @Override
  public void revokeSubscription(
  	final Integer clientSubscriptionId,
    final Integer revisionCount) 
  throws ServiceException {
    Transaction transaction = null;
    SubscriptionDAO dao = new SubscriptionDAO();
    String newStatusCode = SubscriptionStatusCodes.REVOKED;
    String userid = CurrentUser.getUser().getUserId();

    try {
      transaction = openTransaction();
      dao.updateSubscriptionStatus(
        transaction,
        clientSubscriptionId,
        newStatusCode,
        revisionCount,
        userid);
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * change the status of the subscription to "Invalidated"
   *
   * @param   clientSubscriptionId  the subscription ID
   * @param   revisionCount         the revision count
   *
   * @throws  ServiceException  on exception
   */
  @Override
  public void invalidateSubscription(
  	final Integer clientSubscriptionId,
    final Integer revisionCount
  ) throws ServiceException {
  	
    Transaction transaction = null;
    SubscriptionDAO dao = new SubscriptionDAO();
    String newStatusCode = SubscriptionStatusCodes.INVALIDATED;
    String userid = CurrentUser.getUser().getUserId();

    try {
      transaction = openTransaction();
      dao.updateSubscriptionStatus(
        transaction,
        clientSubscriptionId,
        newStatusCode,
        revisionCount,
        userid);
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param   agristabilityClientId  ID
   * @param   pin                    pin
   *
   * @return  the response
   *
   * @throws  ServiceException  on exception
   */
  @Override
  public GenerateSubscriptionResponse generateSubscription(
    final Integer agristabilityClientId,
    final Integer pin
  ) throws ServiceException {
    GenerateSubscriptionResponse genResponse =
      new GenerateSubscriptionResponse();
    Transaction transaction = null;
    SubscriptionDAO dao = new SubscriptionDAO();

    try {

      //
      // use the web service to create a new subscription number.
      //
      BCeIDServiceSoap_PortType service = createWebService();
      CreateSubscriptionBatchRequest serviceRequest = createBatchRequest(
          genResponse, pin);
      CreateSubscriptionBatchResponse serviceResponse =
        service.createSubscriptionBatch(serviceRequest);

      if (ResponseCode.Failed.equals(serviceResponse.getCode())) {
        genResponse.getErrorMessages().add(serviceResponse.getMessage());

        return genResponse;
      }

      CreateSubscriptionBatchResponseItem[] responseItems =
        serviceResponse.getResponseItemList();

      if (ResponseCode.Failed.equals(responseItems[0].getCode())) {
        genResponse.getErrorMessages().add(serviceResponse.getMessage());

        return genResponse;
      }

      //
      // insert a farm_client_subscriptions row.
      //
      BCeIDSubscription sub = responseItems[0].getSubscription();
      ClientSubscription cs = new ClientSubscription();
      logger.debug("subscription number: " + sub.getSubscriptionCode());

      cs.setGeneratedByUserid(CurrentUser.getUser().getUserId());
      cs.setActivationExpiryDate(sub.getActivationExpiryDate().getTime());
      cs.setSubscriptionNumber(sub.getSubscriptionCode());

      transaction = openTransaction();
      dao.insertSubscription(transaction, agristabilityClientId, cs);

      genResponse.setClientSubscriptionId(cs.getClientSubscriptionId());
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return genResponse;
  }


  /**
   * Ask the BCeID web service for the active subscriptions, then update any
   * matching farm_client_subscriptions entries.
   *
   * @param   errorMessages  messages generated by the subscription web service
   *
   * @return  the number of subscriptions activated
   *
   * @throws  ServiceException  on exception
   */
  @Override
  public int activateSubscriptions(final List errorMessages)
    throws ServiceException {
    Transaction transaction = null;
    SubscriptionDAO dao = new SubscriptionDAO();
    int numActivated = 0;
    String guid = CurrentUser.getUser().getGuid();
    String userid = CurrentUser.getUser().getUserId();

    try {
      //
      // use the web service to get the activated subscriptions
      //
      BCeIDServiceSoap_PortType service = createWebService();
      SubscriptionCodeListRequest request = createSubscriptionCodeListRequest(
          guid);
      SubscriptionCodeListResponse response = service.getSubscriptionCodeList(
          request);

      if (ResponseCode.Failed.equals(response.getCode())) {
        errorMessages.add(response.getMessage());

        return numActivated;
      }

      BCeIDSubscription[] subscriptions = response.getSubscriptionList();

      if (subscriptions != null) {
        transaction = openTransaction();
        
        transaction.begin();

        // find or create a farm_agristability_represntves row
        Integer repId = dao.getRepresentativeId(transaction, guid);

        if (repId == null) {
          repId = dao.insertRepresentative(transaction, guid, userid);
        }

        // for each subscription number update the matching 'GEN'
        // farm_client_subscriptions rows
        for (int ii = 0; ii < subscriptions.length; ii++) {
          String subNumber = subscriptions[ii].getSubscriptionCode();

          int numUpdated = dao.activateSubscriptions(transaction, repId,
              subNumber, userid);
          numActivated += numUpdated;
        }

        transaction.commit();
      }

    } catch (Exception e) {
    	if(transaction != null) {
    		transaction.rollback();
    	}
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return numActivated;
  }


  /**
   * @param   guid  user GUID
   *
   * @return  SubscriptionCodeListRequest
   */
  private SubscriptionCodeListRequest createSubscriptionCodeListRequest(
    final String guid) {
    SubscriptionCodeListRequest request = new SubscriptionCodeListRequest();

    setCommonRequestParameters(request);
    request.setUserGuid(guid);

    return request;
  }


  /**
   * Use webade to get the user name and phone number.
   *
   * @param  users  users
   */
  private void getNameAndPhone(List users) {
    final String unavailable = "Not Available";

    for (int ii = 0; ii < users.size(); ii++) {
    	AuthorizedUser user = (AuthorizedUser) users.get(ii);
      user.setUserName(unavailable);
      user.setUserPhoneNumber(unavailable);

      try {
        String guid = user.getUserGuid();
        WebADEUserInfo userInfo = UserFinder.findUserByGuid(guid);

        logger.debug("userInfo: " + userInfo);

        if (userInfo != null) {
          if (!StringUtils.isBlank(userInfo.getDisplayName())) {
            user.setUserName(userInfo.getDisplayName());
          }

          if (!StringUtils.isBlank(userInfo.getPhoneNumber())) {
            user.setUserPhoneNumber(userInfo.getPhoneNumber());
          }
        }
      } catch (Exception e) {
        logger.error("Unexpected error: ", e);
      }
    }
  }

  /**
   * function to make ConfigurationUtility lines shorter.
   *
   * @param   key  key
   *
   * @return  property value
   */
  private String getConfigValue(final String key) {
    return ConfigurationUtility.getInstance().getValue(key);
  }

  /**
   * @return  the BCeID service
   *
   * @throws  Exception  on Exception
   */
  private BCeIDServiceSoap_PortType createWebService() throws Exception {
    BCeIDServiceSoap_PortType service;
    BCeIDServiceLocator locator = new BCeIDServiceLocator();
    String url = getConfigValue(ConfigurationKeys.BCEID_WEB_SERVICE_URL);
    
    locator.setBCeIDServiceSoapAddress(url);
    locator.setBCeIDServiceSoap12Address(url);

    String userId = getConfigValue(ConfigurationKeys.BCEID_USER_ID);
    String userPwd = getConfigValue(ConfigurationKeys.BCEID_USER_PASSWORD);

    service = locator.getBCeIDServiceSoap();
    ((Stub) service)._setProperty(Call.USERNAME_PROPERTY, userId);
    ((Stub) service)._setProperty(Call.PASSWORD_PROPERTY, userPwd);

    return service;
  }

  /**
   * @param   genResponse  the response
   * @param   pin          pin
   *
   * @return  a request for a new subscription
   */
  private CreateSubscriptionBatchRequest createBatchRequest(
    GenerateSubscriptionResponse genResponse, final Integer pin) {
    String expStr = getConfigValue(ConfigurationKeys.BCEID_EXPIRY_PERIOD);
    int expiryPeriodInDays = Integer.parseInt(expStr);

    CreateSubscriptionBatchRequest request =
      new CreateSubscriptionBatchRequest();

    setCommonRequestParameters(request);

    BCeIDSubscriptionBatch batch = new BCeIDSubscriptionBatch();
    BCeIDSubscription[] subList = new BCeIDSubscription[1];

    subList[0] = createSubscription(expiryPeriodInDays, pin);
    batch.setCreatedOn(Calendar.getInstance());
    batch.setDeletedOn(Calendar.getInstance());
    batch.setDescription("FARMS subscription"); // must be less than 25 characters
    batch.setType(BCeIDSubscriptionCodeType.Regular);
    batch.setSubscriptionList(subList);

    request.setBatch(batch);

    return request;
  }


  /**
   * @param   expiryPeriodInDays  period in days
   * @param   pin                 pin
   *
   * @return  BCeIDSubscription
   */
  private BCeIDSubscription createSubscription(int expiryPeriodInDays,
    final Integer pin) {
    BCeIDSubscription subscription = new BCeIDSubscription();
    Calendar now = Calendar.getInstance();
    Calendar expiryDate = Calendar.getInstance();
    expiryDate.add(Calendar.DAY_OF_YEAR, expiryPeriodInDays);

    subscription.setCodeType(BCeIDSubscriptionCodeType.Regular);
    subscription.setScope(BCeIDSubscriptionScope.Individual);
    subscription.setStatus(BCeIDSubscriptionStatus.Void);
    subscription.setActivationExpiryDate(expiryDate);
    subscription.setInitialActivationPeriod("" + expiryPeriodInDays);
    subscription.setCreatedOnDateTime(now);
    subscription.setDownloadDateTime(now);

    subscription.setQuestion("What is your personal identification number?");
    subscription.setAnswer(pin.toString());

    String onlineServiceId = getConfigValue(
        ConfigurationKeys.BCEID_ONLINE_SERVICE_ID);
    subscription.setOsKey(onlineServiceId);

    BCeIDAccountType accType = new BCeIDAccountType();
    accType.setIsAllowed(true);
    accType.setCode(BCeIDAccountTypeCode.Business);

    BCeIDAccount account = new BCeIDAccount();
    account.setType(accType);
    subscription.setAccount(account);

    return subscription;
  }


  /**
   * Set fields common to all RequestBases.
   *
   * @param  request  request
   */
  private void setCommonRequestParameters(RequestBase request) {
    String onlineServiceId = getConfigValue(
        ConfigurationKeys.BCEID_ONLINE_SERVICE_ID);
    request.setOnlineServiceId(onlineServiceId);

    // userId looks like idir\jxjobson. We only want the jxjobson part.
    String userId = getConfigValue(ConfigurationKeys.BCEID_USER_ID);
    String choppedId = userId.substring(userId.indexOf("\\") + 1,
        userId.length());
    request.setRequesterUserId(choppedId);

    request.setRequesterAccountTypeCode(BCeIDAccountTypeCode.Internal); // IDIR
  }
}
