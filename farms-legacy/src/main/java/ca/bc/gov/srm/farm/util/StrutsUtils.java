/**
 *
 * Copyright (c) 2026,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.message.MessageKeys;

/**
 */
public final class StrutsUtils {

  /** private constructor */
  private StrutsUtils() {
  }


  public static List<String> convertActionMessagesToStringList(ActionMessages messages) {
    Properties messageProperties = PropertyLoader.loadProperties(MessageKeys.MESSAGES_FILE_PATH);
    
    return convertActionMessagesToStringList(messages, messageProperties);
  }
  
  
  public static List<String> convertActionMessagesToStringList(ActionMessages messages, Properties messageProperties) {
    List<String> errorMessages = new ArrayList<>();
    
    if(!messages.isEmpty()) {
      
      for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
        ActionMessage msg = mi.next();

        String messagePattern = messageProperties.getProperty(msg.getKey());

        if (messagePattern != null) {
            Object[] values = msg.getValues();

            String formattedMessage = (values != null && values.length > 0)
                    ? MessageFormat.format(messagePattern, values)
                    : messagePattern;

            errorMessages.add(formattedMessage);
        }
      }
      
    }
    return errorMessages;
  }
}
