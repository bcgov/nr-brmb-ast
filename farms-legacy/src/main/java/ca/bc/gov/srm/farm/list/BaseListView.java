/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.list;

import ca.bc.gov.srm.farm.BaseObject;

/**
 * BaseListView.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public abstract class BaseListView extends BaseObject implements ListView {

  /**
   * toString.
   *
   * @return  The return value.
   */
  @Override
  public String toString() {
    StringBuffer retValue = new StringBuffer();
    retValue.append(classnameToString()).append("(");
    retValue.append("value=");
    retValue.append(getValue()).append(",");
    retValue.append("label=");
    retValue.append(getLabel()).append(")");

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

    //need to include class name to differentiate ids...
    result = hash(result, classnameToString());
    result = hash(result, getValue());
    result = hash(result, getLabel());

    return result;
  }

}
