/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.crm;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.exception.ServiceException;

/**
 * @author awilkinson
 */
public final class CrmTokenFetcher {

  private Logger logger = LoggerFactory.getLogger(CrmTokenFetcher.class);

  private static CrmTokenFetcher instance = new CrmTokenFetcher();

  private String authority;
  private String clientId;
  private String secret;
  private String scope;
  
  private boolean initialized = false;
  
  private ConfidentialClientApplication app;
  
  private IAuthenticationResult authResult;
  
  private CrmTokenFetcher() {
    // private constructor
  }
  
  public static CrmTokenFetcher getInstance() {
    return instance;
  }

  public String getToken() throws ServiceException {

    try {
      initialize();
      
      boolean fetchNewToken = needNewToken();
      if(fetchNewToken) {
        authResult = getAccessTokenByClientCredentialGrant();
      }
      
    } catch (Exception e) {
      logger.error("Error getting CRM Dynamics access token: ", e);
      throw new ServiceException("Failed to get Dynamics access token: ", e);
    }
    
    return authResult.accessToken();
  }
  
  /**
   * If the token will expire in the next 5 minutes then get a new one just to be safe.
   * Tokens expire one hour after they are issued.
   */
  private boolean needNewToken() {
    boolean needNew = true;

    if(authResult != null) {
      
      final long minute = 60 * 1000;
      final int five = 5;
      long minutesLeft = (authResult.expiresOnDate().getTime() - new Date().getTime()) / minute;
      
      if(minutesLeft > five) {
        needNew = false;
      }
    }
    
    return needNew;
  }


  private IAuthenticationResult getAccessTokenByClientCredentialGrant() throws InterruptedException, ExecutionException {
    
    logger.debug("Fetching new CRM-dynamics token...");
    
    // With client credentials flows the scope is ALWAYS of the shape "resource/.default", as the
    // application permissions need to be set statically (in the portal), and then granted by a tenant administrator
    ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(Collections.singleton(scope)).build();
    
    CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
    return future.get();
  }


  private void initialize() throws MalformedURLException {
    
    if(!initialized) {
      
      ConfigurationUtility cu = ConfigurationUtility.getInstance();

      String dynamicsUrl = cu.getValue(ConfigurationKeys.CRM_DYNAMICS_URL);

      authority = cu.getValue(ConfigurationKeys.CRM_AUTHORITY_URL);
      clientId = cu.getValue(ConfigurationKeys.CRM_CLIENT_ID);
      secret = cu.getValue(ConfigurationKeys.CRM_CLIENT_SECRET);
      scope = dynamicsUrl + "/.default";
      
      app = ConfidentialClientApplication.builder(
                clientId,
                ClientCredentialFactory.createFromSecret(secret))
                .authority(authority)
                .build();
      
      initialized = true;
    }
  }

}
