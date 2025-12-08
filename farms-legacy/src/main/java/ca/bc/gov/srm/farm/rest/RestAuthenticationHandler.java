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

import ca.bc.gov.srm.farm.exception.ServiceException;

/**
 * @author awilkinson
 */
public interface RestAuthenticationHandler {

  void handleAuthentication(HttpURLConnection conn) throws ServiceException;

}
