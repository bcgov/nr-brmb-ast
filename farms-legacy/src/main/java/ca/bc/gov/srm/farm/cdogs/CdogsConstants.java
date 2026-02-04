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
package ca.bc.gov.srm.farm.cdogs;

public final class CdogsConstants {

  public static final String HEADER_ENTITY_URL = "OData-EntityId";
  
  public static final String HEALTH_CHECK_ENDPOINT = "health";
  public static final String FILE_TYPES_ENDPOINT = "fileTypes";
  public static final String EXISTING_TEMPLATE_ENDPOINT = "template/%s/render";
  public static final String EXISTING_TEMPLATE_CACHE_ENDPOINT = "template/%s";
  
  private CdogsConstants() {
    // private constructor
  }
}
