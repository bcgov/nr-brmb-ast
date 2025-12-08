/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * SearchResults.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public abstract class SearchResults extends Result {

  /** nextRow. */
  private int nextRow = 0;

  /** resultList. */
  private ArrayList resultList = new ArrayList();

  /** startRow. */
  private int startRow = 0;

  /** totalRows. */
  private int totalRows = 0;

  /**
   * getNextRow.
   *
   * @return  The return value.
   */
  public int getNextRow() {
    return nextRow;
  }

  /**
   * getStartRow.
   *
   * @return  The return value.
   */
  public int getStartRow() {
    return startRow;
  }

  /**
   * getTotalRows.
   *
   * @return  The return value.
   */
  public int getTotalRows() {
    return totalRows;
  }

  /**
   * getSearchResults.
   *
   * @return  The return value.
   */
  protected List getResultList() {
    return resultList;
  }

  /**
   * Sets the value for next row.
   *
   * @param  value  Input parameter.
   */
  protected void setNextRow(final int value) {
    this.nextRow = value;
  }

  /**
   * Sets the value for search results.
   *
   * @param  results  Input parameter.
   */
  protected void setResultList(final Object[] results) {
    this.resultList.clear();
    this.resultList.addAll(Arrays.asList(results));
  }

  /**
   * Sets the value for start row.
   *
   * @param  value  Input parameter.
   */
  protected void setStartRow(final int value) {
    this.startRow = value;
  }

  /**
   * Sets the value for total rows.
   *
   * @param  value  Input parameter.
   */
  protected void setTotalRows(final int value) {
    this.totalRows = value;
  }

}
