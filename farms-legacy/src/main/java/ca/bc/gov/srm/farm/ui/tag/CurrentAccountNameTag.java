/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;

/**
 * @author awilkinson
 * @created Feb 16, 2011
 */
public class CurrentAccountNameTag extends TextTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 4406852018697415082L;

  /** Creates a new CurrentUserTag object. */
  public CurrentAccountNameTag() {
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

    return currentUser.getAccountName();
  }

}
