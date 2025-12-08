/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;


/**
 * CurrentUserTag.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class CurrentUserTag extends TextTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 4406852018697415082L;

  /** Creates a new CurrentUserTag object. */
  public CurrentUserTag() {
    super();
  }

  /**
   * text.
   *
   * @return  The return value.
   */
  @Override
  public String text() {
    User currentUser = CurrentUser.getUser();

    return currentUser.getUserId();
  }

}
