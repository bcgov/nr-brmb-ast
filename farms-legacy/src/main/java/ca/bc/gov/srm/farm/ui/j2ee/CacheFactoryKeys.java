/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.j2ee;

import javax.servlet.http.HttpSession;


/**
 * CacheFactoryKeys.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2142 $
 */
final class CacheFactoryKeys {

  /** Creates a new CacheFactoryKeys object. */
  private CacheFactoryKeys() {
  }

  static String createUserKey(final HttpSession session) {
    return session.getId();
  }
}
