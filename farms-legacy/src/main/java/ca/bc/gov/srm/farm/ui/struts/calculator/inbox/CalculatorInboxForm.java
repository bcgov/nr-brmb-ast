/**
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.inbox;

import java.util.List;

import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.ListServiceConstants;
import ca.bc.gov.srm.farm.ui.domain.CalculatorInboxItem;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;

/**
 * @author awilkinson
 * @created Dec 15, 2010
 */
public class CalculatorInboxForm extends CalculatorForm {

  private static final long serialVersionUID = -2645502921365403799L;

  private List<CalculatorInboxItem> inboxItems;
  
  private List<ListView> programYearSelectOptions;
  
  private List<ListView> assignedToSelectOptions;

  /**
   * @return the inboxItems
   */
  public List<CalculatorInboxItem> getInboxItems() {
    return inboxItems;
  }

  /**
   * @param inboxItems the inboxItems to set
   */
  public void setInboxItems(List<CalculatorInboxItem> inboxItems) {
    this.inboxItems = inboxItems;
  }

  /**
   * @return the assignedToSelectOptions
   */
  public List<ListView> getAssignedToSelectOptions() {
    return assignedToSelectOptions;
  }

  /**
   * @param assignedToSelectOptions the assignedToSelectOptions to set
   */
  public void setAssignedToSelectOptions(List<ListView> assignedToSelectOptions) {
    this.assignedToSelectOptions = assignedToSelectOptions;
  }

  /**
   * @return the programYearSelectOptions
   */
  public List<ListView> getProgramYearSelectOptions() {
    return programYearSelectOptions;
  }

  /**
   * @param programYearSelectOptions the programYearSelectOptions to set the value to
   */
  public void setProgramYearSelectOptions(List<ListView> programYearSelectOptions) {
    this.programYearSelectOptions = programYearSelectOptions;
  }

  /**
   * @return boolean indicating whether or not the state filtering checkboxes
   * should be unchecked and disabled
   */
  public boolean getStateFiltersDiabled() {
    boolean result = false;
    String inboxSearchType = getInboxSearchType();
    if(inboxSearchType != null) {
      result = inboxSearchType.equals(ListServiceConstants.CALCULATOR_INBOX_SEARCH_TYPE_READY);
    }
    return result;
  }
}
