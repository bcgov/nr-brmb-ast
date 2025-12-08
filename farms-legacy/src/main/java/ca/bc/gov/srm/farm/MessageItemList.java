/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


/**
 * MessageItemList.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class MessageItemList extends BaseObject {

  /** messagesItemList. */
  private List messagesItemList = new ArrayList();


  /**
   * add.
   *
   * @param  messageItem  The parameter value.
   */
  public void add(final MessageItem messageItem) {

    if (messageItem != null) {
      messagesItemList.add(messageItem);
    }
  }


  /**
   * addAll.
   *
   * @param  messageItemList  The parameter value.
   */
  public void addAll(final MessageItemList messageItemList) {

    if (messagesItemList != null) {
      addAll(messageItemList.getMessages());
    }
  }


  /**
   * addAll.
   *
   * @param  messageItems  The parameter value.
   */
  public void addAll(final MessageItem[] messageItems) {

    if ((messageItems != null) && (messageItems.length > 0)) {

      for (int i = 0; i < messageItems.length; i++) {
        add(messageItems[i]);
      }
    }
  }


  /** clear. */
  public void clear() {
    messagesItemList.clear();
  }


  /**
   * contains.
   *
   * @param   messageItem  The parameter value.
   *
   * @return  The return value.
   */
  public boolean contains(final MessageItem messageItem) {
    return messagesItemList.contains(messageItem);
  }


  /**
   * get.
   *
   * @param   index  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem get(final int index) {
    ListIterator iter = messagesItemList.listIterator(index);

    return (MessageItem) iter.next();
  }


  /**
   * getErrorMessages.
   *
   * @return  The return value.
   */
  public MessageItem[] getErrorMessages() {
    return getMessagesOfType(MessageType.error());
  }


  /**
   * getInformationMessages.
   *
   * @return  The return value.
   */
  public MessageItem[] getInformationMessages() {
    return getMessagesOfType(MessageType.information());
  }


  /**
   * getMessages.
   *
   * @return  The return value.
   */
  public MessageItem[] getMessages() {
    MessageItem[] result = new MessageItem[0];

    if ((messagesItemList != null) && (messagesItemList.size() > 0)) {
      result = (MessageItem[]) messagesItemList.toArray(
          new MessageItem[messagesItemList.size()]);
    }

    return result;
  }


  /**
   * getMessagesOfType.
   *
   * @param   messageType  Input parameter.
   *
   * @return  The return value.
   */
  public MessageItem[] getMessagesOfType(final MessageType messageType) {
    List items = new ArrayList();
    ListIterator iter = messagesItemList.listIterator();

    while (iter.hasNext()) {
      MessageItem item = (MessageItem) iter.next();

      if (item.getMessageType().equals(messageType)) {
        items.add(item);
      }
    }

    return (MessageItem[]) items.toArray(new MessageItem[items.size()]);
  }


  /**
   * getWarningMessages.
   *
   * @return  The return value.
   */
  public MessageItem[] getWarningMessages() {
    return getMessagesOfType(MessageType.warning());
  }


  /**
   * hasErrors.
   *
   * @return  The return value.
   */
  public boolean hasErrors() {
    return (getErrorMessages().length > 0);
  }


  /**
   * hasInformationMessages.
   *
   * @return  The return value.
   */
  public boolean hasInformationMessages() {
    return (getInformationMessages().length > 0);
  }


  /**
   * hasWarnings.
   *
   * @return  The return value.
   */
  public boolean hasWarnings() {
    return (getWarningMessages().length > 0);
  }


  /**
   * remove.
   *
   * @param   index  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem remove(final int index) {
    return (MessageItem) messagesItemList.remove(index);
  }


  /**
   * remove.
   *
   * @param   messageItem  The parameter value.
   *
   * @return  The return value.
   */
  public boolean remove(final MessageItem messageItem) {
    return messagesItemList.remove(messageItem);
  }


  /**
   * set.
   *
   * @param   index        The parameter value.
   * @param   messageItem  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem set(final int index, final MessageItem messageItem) {
    return (MessageItem) messagesItemList.set(index, messageItem);
  }

  /**
   * Sets the value for messages.
   *
   * @param  messageItems  Input parameter.
   */
  public void setMessages(final MessageItem[] messageItems) {
    this.messagesItemList = new ArrayList();

    if ((messageItems != null) && (messageItems.length > 0)) {

      for (int i = 0; i < messageItems.length; i++) {
        add(messageItems[i]);
      }
    }
  }

  /**
   * toString.
   *
   * @return  The return value.
   */
  @Override
  public String toString() {
    StringBuffer retValue = new StringBuffer();
    retValue.append(classnameToString()).append("(");
    retValue.append("hasErrors=");
    retValue.append(hasErrors()).append(",");
    retValue.append("hasWarnings=");
    retValue.append(hasWarnings()).append(",");
    retValue.append("hasInformation=");
    retValue.append(hasInformationMessages()).append(",");
    retValue.append(arrayToString("messageItems", getMessages())).append(")");

    return retValue.toString();
  }

  /**
   * generateHashCode.
   *
   * @return  The return value.
   */
  @Override
  protected int generateHashCode() {
    int result = HASH_SEED;
    result = hash(result, getClass().getName());
    result = hash(result, getMessages());

    return result;
  }
}
