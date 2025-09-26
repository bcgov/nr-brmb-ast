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

import java.net.HttpURLConnection;

import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.rest.RestAuthenticationHandler;

/**
 * @author awilkinson
 */
public class CrmAuthenticationHandler implements RestAuthenticationHandler {

  /* (non-Javadoc)
   * @see ca.bc.gov.srm.farm.rest.RestAuthenticationHandler#handleAuthentication()
   */
  @Override
  public void handleAuthentication(HttpURLConnection conn) throws ServiceException {
    
    CrmTokenFetcher tokenFetcher = CrmTokenFetcher.getInstance();

    String token = tokenFetcher.getToken();
    
    conn.setRequestProperty("Authorization", "Bearer " + token);
  }

}
