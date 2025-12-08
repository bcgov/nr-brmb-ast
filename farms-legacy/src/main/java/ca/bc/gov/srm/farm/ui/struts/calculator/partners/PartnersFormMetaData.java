/**
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.partners;

import java.util.List;

/**
 * @author awilkinson
 */
public class PartnersFormMetaData {

  private List<String> operationSchedules;

  public List<String> getOperationSchedules() {
    return operationSchedules;
  }

  public void setOperationSchedules(List<String> operationSchedules) {
    this.operationSchedules = operationSchedules;
  }
}
