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
package ca.bc.gov.srm.farm.ui.struts.calculator.incomeexpenses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.DeductionLineItem;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.calculator.AdjustmentGridForm;
import ca.bc.gov.srm.farm.ui.struts.calculator.AdjustmentGridItemData;

/**
 * @author awilkinson
 * @created Jan 3, 2011
 */
public class IncomeExpensesForm extends AdjustmentGridForm {

  private static final long serialVersionUID = -2649172918365403512L;

  private ListView[] lineItems;
  
  private List<Boolean> incomeExpenseFilterValues;

  private List<Boolean> incomeEligibilityFilterValues;
  
  private Map<String, MarginTotal> marginTotalMap;
  
  private String incomeExpenseRadio;
  private String eligibilityRadio;
  private String financialViewRadio;
  
  /** set to true when the Add New button is clicked */
  private boolean addedNew = false;
  private boolean missingStatementAReceivedDates = false;
  
  private String deductionLineItemType;

  private List<DeductionLineItem> deductionLineItems;
  
  /**
   */
  public IncomeExpensesForm() {
    incomeExpenseFilterValues = new ArrayList<>();
    incomeExpenseFilterValues.add(Boolean.TRUE);
    incomeExpenseFilterValues.add(Boolean.FALSE);

    incomeEligibilityFilterValues = new ArrayList<>();
    incomeEligibilityFilterValues.add(Boolean.TRUE);
    incomeEligibilityFilterValues.add(Boolean.FALSE);
  }

  /**
   * @param lineKey String
   * @return IncomeExpenseFormData
   */
  public IncomeExpenseFormData getIncomeExpense(String lineKey) {
    return (IncomeExpenseFormData) getItem(lineKey);
  }

  /**
   * @return new instance of AdjustmentGridItemData
   */
  @Override
  protected AdjustmentGridItemData getNewAdjustmentGridItemData() {
    return new IncomeExpenseFormData();
  }

  /**
   * @return the incomeEligibilityFilterValues
   */
  public List<Boolean> getIncomeEligibilityFilterValues() {
    return incomeEligibilityFilterValues;
  }

  /**
   * @return the incomeExpenseFilterValues
   */
  public List<Boolean> getIncomeExpenseFilterValues() {
    return incomeExpenseFilterValues;
  }

  /**
   * @return the lineItems
   */
  public ListView[] getLineItems() {
    return lineItems;
  }

  /**
   * @param lineItems the lineItems to set the value to
   */
  public void setLineItems(ListView[] lineItems) {
    this.lineItems = lineItems;
  }

  /**
   * @return the marginTotalMap
   */
  public Map<String, MarginTotal> getMarginTotalMap() {
    return marginTotalMap;
  }

  /**
   * @param marginTotalMap the marginTotalMap to set the value to
   */
  public void setMarginTotalMap(Map<String, MarginTotal> marginTotalMap) {
    this.marginTotalMap = marginTotalMap;
  }

  /**
   * @return the eligibilityRadio
   */
  public String getEligibilityRadio() {
    return eligibilityRadio;
  }

  /**
   * @param eligibilityRadio the eligibilityRadio to set the value to
   */
  public void setEligibilityRadio(String eligibilityRadio) {
    this.eligibilityRadio = eligibilityRadio;
  }

  /**
   * @return the financialViewRadio
   */
  public String getFinancialViewRadio() {
    return financialViewRadio;
  }

  /**
   * @param financialViewRadio the financialViewRadio to set the value to
   */
  public void setFinancialViewRadio(String financialViewRadio) {
    this.financialViewRadio = financialViewRadio;
  }

  /**
   * @return the incomeExpenseRadio
   */
  public String getIncomeExpenseRadio() {
    return incomeExpenseRadio;
  }

  /**
   * @param incomeExpenseRadio the incomeExpenseRadio to set the value to
   */
  public void setIncomeExpenseRadio(String incomeExpenseRadio) {
    this.incomeExpenseRadio = incomeExpenseRadio;
  }

  /**
   * @return the addedNew
   */
  public boolean isAddedNew() {
    return addedNew;
  }

  /**
   * @param addedNew the addedNew to set the value to
   */
  public void setAddedNew(boolean addedNew) {
    this.addedNew = addedNew;
  }

  /**
   * @return the deductionLineItemType
   */
  public String getDeductionLineItemType() {
    return deductionLineItemType;
  }

  /**
   * @param deductionLineItemType the deductionLineItemType to set
   */
  public void setDeductionLineItemType(String deductionLineItemType) {
    this.deductionLineItemType = deductionLineItemType;
  }

  /**
   * @return the deductionLineItems
   */
  public List<DeductionLineItem> getDeductionLineItems() {
    return deductionLineItems;
  }

  /**
   * @param deductionLineItems the deductionLineItems to set
   */
  public void setDeductionLineItems(List<DeductionLineItem> deductionLineItems) {
    this.deductionLineItems = deductionLineItems;
  }

  public boolean isMissingStatementAReceivedDates() {
    return missingStatementAReceivedDates;
  }

  public void setMissingStatementAReceivedDates(boolean missingStatementAReceivedDates) {
    this.missingStatementAReceivedDates = missingStatementAReceivedDates;
  }

}
