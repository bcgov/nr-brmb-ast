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
package ca.bc.gov.srm.farm.rest;

import java.net.HttpURLConnection;
import java.util.Base64;

import ca.bc.gov.srm.farm.exception.ServiceException;

/**
 * @author awilkinson
 */
public class BasicAuthenticationHandler implements RestAuthenticationHandler {
  
  private String username;
  
  private String password;
  
  protected BasicAuthenticationHandler() {
    // do nothing
  }
  
  public BasicAuthenticationHandler(String username, String password) {
    super();
    this.username = username;
    this.password = password;
  }

  /* (non-Javadoc)
   * @see ca.bc.gov.srm.farm.rest.RestAuthenticationHandler#handleAuthentication(java.net.HttpURLConnection)
   */
  @Override
  public void handleAuthentication(HttpURLConnection conn) throws ServiceException {
    conn.setRequestProperty("Authorization",
        "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
  }

  protected void setUsername(String username) {
    this.username = username;
  }

  protected void setPassword(String password) {
    this.password = password;
  }
  
}
