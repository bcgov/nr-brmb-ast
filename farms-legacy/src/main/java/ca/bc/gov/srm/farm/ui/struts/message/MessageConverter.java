/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.struts.message;

import ca.bc.gov.srm.farm.MessageItem;
import ca.bc.gov.srm.farm.message.MessageKeys;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


/**
 * MessageConverter.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public final class MessageConverter {

  /** Creates a new MessageConverter object. */
  private MessageConverter() {

  }

  /**
   * Converts framework-agnostic errors to Struts errors.
   *
   * @param   messages  The parameter value.
   *
   * @return  The return value.
   */
  public static ActionMessages convertMessages(final MessageItem[] messages) {
    return convertMessages(ActionMessages.GLOBAL_MESSAGE, messages);
  }

  /**
   * Converts framework-agnostic errors to Struts errors.
   *
   * @param   key       The parameter value.
   * @param   messages  The parameter value.
   *
   * @return  The return value.
   */
  public static ActionMessages convertMessages(final String key,
    final MessageItem[] messages) {
    ActionErrors actionMessages = new ActionErrors();

    if ((messages == null) || (messages.length == 0)) {
      return actionMessages;
    }

    for (int i = 0; i < messages.length; i++) {
      MessageItem message = messages[i];
      String msgKey = null;
      Object[] args = null;

      //message items may have no key or arguments, just the message...
      if (message.getKey() == null) {
        msgKey = MessageKeys.CUSTOM_MESSAGE;
        args = new String[] {message.getMessage()};
      } else {
        msgKey = message.getKey();
        args = message.getArguments();
      }

      actionMessages.add(key, new ActionMessage(msgKey, args));
    }

    return actionMessages;
  }

}
