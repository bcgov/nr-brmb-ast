/**
 * 
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.sc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.domain.calculator.StructuralChangeRow;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;


/**
 * Form for screen 930.
 */
public class StructuralChangeForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2649172988005403512L;
  
  private final int maxNumYears = 10;
  private boolean[] isLead = new boolean[maxNumYears];
  
  private boolean[] usedInRatioCalc;
  private boolean[] usedInAdditiveCalc;
  
  private String structuralChangeMethod;
  private String expenseStructuralChangeMethod;

  private List<StructuralChangeRow> rows = null;

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    
    for(int ii = 0; ii< isLead.length; ii++) {
      isLead[ii] = false;
    }
  }

  /**
   * @return the rows
   */
  public List<StructuralChangeRow> getRows() {
    return rows;
  }

  /**
   * @param rows the rows to set
   */
  public void setRows(List<StructuralChangeRow> rows) {
    this.rows = rows;
  }

  /**
   * @return the structuralChangeMethod
   */
  public String getStructuralChangeMethod() {
    return structuralChangeMethod;
  }

  /**
   * @param structuralChangeMethod the structuralChangeMethod to set
   */
  public void setStructuralChangeMethod(String structuralChangeMethod) {
    this.structuralChangeMethod = structuralChangeMethod;
  }
  
  /**
   * Not used for 2024 forward.
   * 
   * @return the expenseStructuralChangeMethod
   */
  public String getExpenseStructuralChangeMethod() {
    return expenseStructuralChangeMethod;
  }

  /**
   * Not used for 2024 forward.
   * 
   * @param expenseStructuralChangeMethod the expenseStructuralChangeMethod to set
   */
  public void setExpenseStructuralChangeMethod(String expenseStructuralChangeMethod) {
    this.expenseStructuralChangeMethod = expenseStructuralChangeMethod;
  }
  
  /**
   * @return isLead array
   */
  public boolean[] getIsLead() {
    return isLead;
  }
  
  /**
   * only gets called when isLead is true;
   * 
   * @param index index
   * @param value value
   */
  public void setIsLead(int index, boolean value) {
    isLead[index] = value;
  }

  public boolean[] getUsedInRatioCalc() {
    return usedInRatioCalc;
  }

  public void setUsedInRatioCalc(boolean[] usedInRatioCalc) {
    this.usedInRatioCalc = usedInRatioCalc;
  }

  public boolean[] getUsedInAdditiveCalc() {
    return usedInAdditiveCalc;
  }

  public void setUsedInAdditiveCalc(boolean[] usedInAddCalc) {
    this.usedInAdditiveCalc = usedInAddCalc;
  }
  
}
