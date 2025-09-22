/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;


/**
 * ApplicationCodeTag.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class ApplicationCodeTag extends TextTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 6135131369226798445L;

  /** Creates a new ApplicationCodeTag object. */
  public ApplicationCodeTag() {
    super();
  }

  /**
   * The offical accronym is FARM, but the users want to see FARMS (S is for
   * System).
   *
   * @return  The return value.
   */
  @Override
  public String text() {
    ConfigurationUtility config = ConfigurationUtility.getInstance();
    String key = ConfigurationKeys.APPLICATION_CODE;
    String result = config.getValue(key) + "S";

    return result;
  }

}
