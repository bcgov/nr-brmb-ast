/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.list;


/**
 * CodeListView.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class CodeListView extends BaseListView {

  /** label. */
  private String label = null;

  /** value. */
  private String value = null;

  /**
   * Creates a new CodeListView object.
   *
   * @param  aValue  Input parameter to initialize class.
   * @param  aLabel  Input parameter to initialize class.
   */
  public CodeListView(final String aValue, final String aLabel) {
    this.value = aValue;
    this.label = aLabel;
  }

  /**
   * getLabel.
   *
   * @return  The return value.
   */
  @Override
  public String getLabel() {
    return label;
  }

  /**
   * getValue.
   *
   * @return  The return value.
   */
  @Override
  public String getValue() {
    return value;
  }

}
