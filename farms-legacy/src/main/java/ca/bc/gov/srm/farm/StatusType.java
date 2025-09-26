/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;

/**
 * StatusType.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public abstract class StatusType extends BaseObject {

  /** APPROVED. */
  private static final String APPROVED = "approved";

  /** DENIED. */
  private static final String DENIED = "denied";

  /** FAILURE. */
  private static final String FAILURE = "failure";

  /** SUCCESS. */
  private static final String SUCCESS = "success";

  /** Creates a new StatusType object. */
  private StatusType() {

  }

  /**
   * getCode.
   *
   * @return  The return value.
   */
  public abstract String getCode();

  /**
   * approved.
   *
   * @return  The return value.
   */
  public static final StatusType approved() {
    return new Approved();
  }

  /**
   * denied.
   *
   * @return  The return value.
   */
  public static final StatusType denied() {
    return new Denied();
  }

  /**
   * failure.
   *
   * @return  The return value.
   */
  public static final StatusType failure() {
    return new Failure();
  }

  /**
   * success.
   *
   * @return  The return value.
   */
  public static final StatusType success() {
    return new Success();
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
   * Approved.
   *
   * @author   $Author: awilkinson $
   * @version  $Revision: 2145 $
   */
  public static class Approved extends StatusType {

    /**
     * getCode.
     *
     * @return  The return value.
     */
    @Override
    public String getCode() {
      return APPROVED;
    }

  }

  /**
   * Denied.
   *
   * @author   $Author: awilkinson $
   * @version  $Revision: 2145 $
   */
  public static class Denied extends StatusType {

    /**
     * getCode.
     *
     * @return  The return value.
     */
    @Override
    public String getCode() {
      return DENIED;
    }

  }

  /**
   * Failure.
   *
   * @author   $Author: awilkinson $
   * @version  $Revision: 2145 $
   */
  public static class Failure extends StatusType {

    /**
     * getCode.
     *
     * @return  The return value.
     */
    @Override
    public String getCode() {
      return FAILURE;
    }

  }

  /**
   * Success.
   *
   * @author   $Author: awilkinson $
   * @version  $Revision: 2145 $
   */
  public static class Success extends StatusType {

    /**
     * getCode.
     *
     * @return  The return value.
     */
    @Override
    public String getCode() {
      return SUCCESS;
    }

  }

}
