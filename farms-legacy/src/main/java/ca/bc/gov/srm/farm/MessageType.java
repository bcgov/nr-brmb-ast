/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;


/**
 * MessageType.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public abstract class MessageType extends BaseObject {

  /** ERROR. */
  private static final String ERROR = "error";

  /** INFO. */
  private static final String INFO = "info";

  /** WARN. */
  private static final String WARN = "warn";

  /** Creates a new MessageType object. */
  private MessageType() {

  }

  /**
   * getCode.
   *
   * @return  The return value.
   */
  public abstract String getCode();

  /**
   * error.
   *
   * @return  The return value.
   */
  public static final MessageType error() {
    return new Error();
  }

  /**
   * getInstance.
   *
   * @param   code  Input parameter.
   *
   * @return  The return value.
   */
  public static final MessageType getInstance(final String code) {
    MessageType result = null;

    if (ERROR.equalsIgnoreCase(code)) {
      result = error();
    } else if (INFO.equalsIgnoreCase(code)) {
      result = information();
    } else if (WARN.equalsIgnoreCase(code)) {
      result = warning();
    } else {
      throw new IllegalArgumentException(
        "Could not create a MessageType instance for code=" + code);
    }

    return result;
  }

  /**
   * information.
   *
   * @return  The return value.
   */
  public static final MessageType information() {
    return new Information();
  }

  /**
   * warning.
   *
   * @return  The return value.
   */
  public static final MessageType warning() {
    return new Warning();
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
    retValue.append("code=");
    retValue.append(getCode()).append(")");

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
    result = hash(result, getCode());

    return result;
  }


  /**
   * Error.
   *
   * @author   $Author: awilkinson $
   * @version  $Revision: 2145 $
   */
  private static class Error extends MessageType {

    /**
     * getCode.
     *
     * @return  The return value.
     */
    @Override
    public String getCode() {
      return ERROR;
    }

  }

  /**
   * Information.
   *
   * @author   $Author: awilkinson $
   * @version  $Revision: 2145 $
   */
  private static class Information extends MessageType {

    /**
     * getCode.
     *
     * @return  The return value.
     */
    @Override
    public String getCode() {
      return INFO;
    }

  }

  /**
   * Warning.
   *
   * @author   $Author: awilkinson $
   * @version  $Revision: 2145 $
   */
  private static class Warning extends MessageType {

    /**
     * getCode.
     *
     * @return  The return value.
     */
    @Override
    public String getCode() {
      return WARN;
    }

  }

}
