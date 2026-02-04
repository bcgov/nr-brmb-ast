/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.diff;

import java.io.Serializable;

import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Mar 16, 2011
 */
public class FieldDiff implements Serializable {

  private static final long serialVersionUID = 3585492756287726066L;

  /** The message key of the field compared */
  private String fieldName;

  /** The value of this field from the existing data */
  private String oldValue;

  /** The value of this field from the new version of the data */
  private String newValue;

  /**
   * @return the fieldName
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * @param fieldName the fieldName to set the value to
   */
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  /**
   * @return the newValue
   */
  public String getNewValue() {
    return newValue;
  }

  /**
   * @param newValue the newValue to set the value to
   */
  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }

  /**
   * @return the oldValue
   */
  public String getOldValue() {
    return oldValue;
  }

  /**
   * @param oldValue the oldValue to set the value to
   */
  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }
  
  /**
   * @param o Object
   * @return boolean
   * @see java.lang.Object#equals(Object)
   */
  @Override
  public boolean equals(Object o) {
    boolean result;
    if(o == null) {
      result = false;
    } else {
      if(o.getClass().isInstance(o)) {
        FieldDiff fd = (FieldDiff) o;
        result = StringUtils.equal(this.fieldName, fd.fieldName);
      } else {
        result = false;
      }
    }
    return result;
  }

  /**
   * @return hash code
   */
  @Override
  public int hashCode() {
    int hash;
    if(fieldName == null) {
      hash = 0;
    } else {
      hash = fieldName.hashCode();
    }
    return hash;
  }
}
