/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;


/**
 * Result.
 *
 * <p>The Result class is used to return complex data from a call. The
 * statusType field allows for flexibility to indicate if the call was
 * successful, or failed, or other. The statusMessages can contain informational
 * messages, warning messages or error messages and give finer grained view of
 * the status.</p>
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public abstract class Result extends BaseObject {

  /** messageItemList. */
  private MessageItemList messageItemList = new MessageItemList();

  /** statusType. */
  private StatusType statusType;

  /**
   * addStatusMessage.
   *
   * @param  messageItem  The parameter value.
   */
  public void addStatusMessage(final MessageItem messageItem) {
    messageItemList.add(messageItem);
  }


  /**
   * addStatusMessages.
   *
   * @param  messageItems  The parameter value.
   */
  public void addStatusMessages(final MessageItem[] messageItems) {
    messageItemList.addAll(messageItems);
  }


  /**
   * getStatusMessageItems.
   *
   * @return  The return value.
   */
  public MessageItemList getStatusMessageItems() {
    return messageItemList;
  }


  /**
   * getStatusMessages.
   *
   * @return  The return value.
   */
  public MessageItem[] getStatusMessages() {
    return messageItemList.getMessages();
  }


  /**
   * getStatusType.
   *
   * @return  The return value.
   */
  public StatusType getStatusType() {
    return statusType;
  }

  /**
   * Sets the value for status type.
   *
   * @param  value  statusType Input parameter.
   */
  public void setStatusType(final StatusType value) {
    this.statusType = value;
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
    retValue.append("statusType=");
    retValue.append(statusType).append(",");
    retValue.append("messageItemList=");
    retValue.append(messageItemList).append(")");

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
    result = hash(result, statusType);
    result = hash(result, messageItemList);

    return result;
  }
}
