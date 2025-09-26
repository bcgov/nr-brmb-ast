/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.ui.struts.message.ActionWarnings;


/**
 * WarningsTag.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class WarningsTag extends org.apache.struts.taglib.html.MessagesTag {

  /** serialVersionUID. */
  private static final long serialVersionUID = -8054261771400883230L;

  /** Creates a new WarningsTag object. */
  public WarningsTag() {
    super();
    setName(ActionWarnings.WARNING_KEY);
  }

  /**
   * Sets the value for message.
   *
   * @param  message  Input parameter.
   */
  @Override
  public void setMessage(final String message) {
    // prevent setting of message because it's not relevant
  }

}
