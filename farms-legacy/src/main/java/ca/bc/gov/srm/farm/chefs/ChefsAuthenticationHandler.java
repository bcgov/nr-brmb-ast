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
package ca.bc.gov.srm.farm.chefs;

import ca.bc.gov.srm.farm.rest.BasicAuthenticationHandler;

/**
 * @author awilkinson
 */
public class ChefsAuthenticationHandler extends BasicAuthenticationHandler {

  public ChefsAuthenticationHandler(ChefsFormCredentials creds) {
    setUsername(creds.getFormId());
    setPassword(creds.getApiKey());
  }

}
