/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.cache.CacheKeys;


/**
 * GetClientListTag.
 */
public class GetClientListTag extends GetUserCacheTag {

  /** DOCUMENT ME! */
  private static final long serialVersionUID = -5128369727193574521L;

  /**
   * @return  CacheKeys key
   */
  @Override
  protected String getKey() {
    return CacheKeys.CURRENT_CLIENTS;
  }

}
