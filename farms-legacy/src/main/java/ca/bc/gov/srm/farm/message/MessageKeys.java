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

import java.util.Locale;


/**
 * MessageKeys.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5662 $
 */
public final class MessageKeys {

  /** Creates a new MessageKeys object. */
  private MessageKeys() {

  }

  /** MESSAGE_KEY_LOCALE. */
  public static final Locale MESSAGE_KEY_LOCALE = new Locale("en", "CA");
  
  public static final String MESSAGES_FILE_PATH = "config/applicationResources.properties";

  /** EXCEPTION_CHECK_CONSTRAINT. */
  public static final String EXCEPTION_CHECK_CONSTRAINT =
    "exception.check.constraint";

  /** EXCEPTION_CHILD_RECORD_EXISTS. */
  public static final String EXCEPTION_CHILD_RECORD_EXISTS =
    "exception.child.record.exists";

  /** EXCEPTION_DATA_NOT_CURRENT. */
  public static final String EXCEPTION_DATA_NOT_CURRENT =
    "exception.data.not.current";

  /** EXCEPTION_DUPLICATE_DATA. */
  public static final String EXCEPTION_DUPLICATE_DATA =
    "exception.duplicate.data";

  /** CUSTOM_MESSAGE. */
  public static final String CUSTOM_MESSAGE = "custom.message";

}
