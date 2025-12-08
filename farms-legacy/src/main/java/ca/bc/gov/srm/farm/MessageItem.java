/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;


import java.text.MessageFormat;


/**
 * MessageItem.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class MessageItem extends BaseObject {

  /** arguments. */
  private Object[] arguments;

  /** key. */
  private String key;

  /** messageType. */
  private MessageType messageType;

  /** pattern. */
  private String pattern;

  /** throwable. */
  private Throwable throwable;


  /**
   * getArguments.
   *
   * @return  The return value.
   */
  public Object[] getArguments() {
    return arguments;
  }

  /**
   * getKey.
   *
   * @return  The return value.
   */
  public String getKey() {
    return key;
  }

  /**
   * getMessage.
   *
   * @return  The return value.
   */
  public String getMessage() {
    String result = null;

    if ((arguments != null) && (arguments.length > 0)) {
      result = MessageFormat.format(pattern, arguments);
    } else {
      result = pattern;
    }

    return result;
  }

  /**
   * getMessageType.
   *
   * @return  The return value.
   */
  public MessageType getMessageType() {
    return messageType;
  }

  /**
   * getPattern.
   *
   * @return  The return value.
   */
  public String getPattern() {
    return pattern;
  }

  /**
   * getThrowable.
   *
   * @return  The return value.
   */
  public Throwable getThrowable() {
    return throwable;
  }

  /**
   * Sets the value for arguments.
   *
   * @param  value  arguments Input parameter.
   */
  public void setArguments(final Object[] value) {
    this.arguments = value;
  }

  /**
   * Sets the value for key.
   *
   * @param  value  key Input parameter.
   */
  public void setKey(final String value) {
    this.key = value;
  }

  /**
   * Sets the value for message type.
   *
   * @param  value  messageType Input parameter.
   */
  public void setMessageType(final MessageType value) {
    this.messageType = value;
  }

  /**
   * Sets the value for message type.
   *
   * @param  value  code Input parameter.
   */
  public void setMessageType(final String value) {
    this.messageType = MessageType.getInstance(value);
  }

  /**
   * Sets the value for pattern.
   *
   * @param  value  pattern Input parameter.
   */
  public void setPattern(final String value) {
    this.pattern = value;
  }

  /**
   * Sets the value for throwable.
   *
   * @param  value  throwable Input parameter.
   */
  public void setThrowable(final Throwable value) {
    this.throwable = value;
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
    retValue.append("messageType=");
    retValue.append(messageType).append(",");
    retValue.append("message=");
    retValue.append(getMessage()).append(")");

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
    result = hash(result, messageType);
    result = hash(result, getMessage());

    return result;
  }

}
