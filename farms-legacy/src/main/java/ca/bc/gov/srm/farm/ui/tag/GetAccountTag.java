/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.cache.CacheKeys;


/**
 * GetAccountTag.
 */
public class GetAccountTag extends GetUserCacheTag {

  /** serialVersionUID. */
  private static final long serialVersionUID = 2261821241481744643L;

  /**
   * @return  CacheKeys key
   */
  @Override
  protected String getKey() {
    return CacheKeys.CURRENT_ACCOUNT;
  }
}
