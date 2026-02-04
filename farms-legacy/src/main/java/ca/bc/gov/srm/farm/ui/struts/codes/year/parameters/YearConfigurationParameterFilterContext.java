/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.year.parameters;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class YearConfigurationParameterFilterContext implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer yearFilter;

  public Integer getYearFilter() {
    return yearFilter;
  }

  public void setYearFilter(Integer pYearFilter) {
    yearFilter = pYearFilter;
  }

}
