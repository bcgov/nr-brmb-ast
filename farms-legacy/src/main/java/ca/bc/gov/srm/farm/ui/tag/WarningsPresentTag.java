/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.ui.struts.message.ActionWarnings;


/**
 * WarningsPresentTag.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class WarningsPresentTag
  extends org.apache.struts.taglib.logic.MessagesPresentTag {

  /** serialVersionUID. */
  private static final long serialVersionUID = -5489931780576154024L;

  /** Creates a new WarningsPresentTag object. */
  public WarningsPresentTag() {
    super();
    setName(ActionWarnings.WARNING_KEY);
  }

}
