/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.message;

import ca.bc.gov.srm.farm.Provider;
import ca.bc.gov.srm.farm.exception.MessageNotFoundException;

import java.util.Locale;


/**
 * MessageProvider.
 *
 * @author   $Author: dzwiers $
 * @version  $Revision: 256 $
 */
public abstract class MessageProvider extends Provider {

  /**
   * getPattern.
   *
   * @param   locale  Input parameter.
   * @param   key     Input parameter.
   *
   * @return  The return value.
   *
   * @throws  MessageNotFoundException  On exception.
   */
  protected abstract String getPattern(Locale locale, String key)
    throws MessageNotFoundException;

}
