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
package ca.bc.gov.srm.farm.chefs.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ca.bc.gov.srm.farm.rest.RestResource;

/**
 * Superclass for CHEFS REST API resources
 * 
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChefsResource extends RestResource {

  // Superclass for CHEFS REST API resources
}
