/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 * 
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.farmingoperation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * 
 * @author   Vivid Solutions
 *
 */
public class FarmingOperationForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2645752781365403100L;


  private List<ListView> scheduleOptions;
  private String schedule;
  private String operationNumber;
  private String businessUseHomeExpense;
  private String expenses;
  private String fiscalYearEnd;
  private String fiscalYearStart;
  private String grossIncome;
  private String inventoryAdjustments;
  private boolean cropShare;
  private boolean feederMember;
  private boolean landlord;
  private String netFarmIncome;
  private String netIncomeAfterAdj;
  private String netIncomeBeforeAdj;
  private String otherDeductions;
  private String partnershipName;
  private String partnershipPercent;
  private String partnershipPin;
  private String proRatePercent;
  
  private boolean locallyGenerated;
  
  private boolean isNew;
  private boolean pyvHasVerifiedScenario;
  
  private Integer tipReportDocId;

  /**
   * isCropDisaster identifies if the productive capacity decreased due to
   * disaster circumstances.
   */
  private boolean cropDisaster;

  /**
   * isLivestockDisaster identifies if productive capacity decreased due to
   * disaster circumstances.
   */
  private boolean livestockDisaster;

  /**
   * agristabAccountingCode is a unique code for the object
   * agristabAccountingCode described as a numeric code used to uniquely
   * identify the method of Accounting to RCT and NISA. Examples of codes and
   * descriptions are 1- Accrual, 2-Cash.
   */
  private String agristabAccountingCode;

  /**
   * operationRevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private String operationRevisionCount;

  /** The Production Insurance Numbers associated with this FarmingOperation. */
  private String productionInsuranceNumber1;
  private String productionInsuranceNumber2;
  private String productionInsuranceNumber3;
  private String productionInsuranceNumber4;
  
  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setCropShare(false);
    setCropDisaster(false);
    setFeederMember(false);
    setLivestockDisaster(false);
    setLandlord(false);
  }

  /**
   * @return the agristabAccountingCode
   */
  public String getAgristabAccountingCode() {
    return agristabAccountingCode;
  }

  /**
   * @param agristabAccountingCode the agristabAccountingCode to set
   */
  public void setAgristabAccountingCode(String agristabAccountingCode) {
    this.agristabAccountingCode = agristabAccountingCode;
  }

  /**
   * @return the businessUseHomeExpense
   */
  public String getBusinessUseHomeExpense() {
    return businessUseHomeExpense;
  }

  /**
   * @param businessUseHomeExpense the businessUseHomeExpense to set
   */
  public void setBusinessUseHomeExpense(String businessUseHomeExpense) {
    this.businessUseHomeExpense = businessUseHomeExpense;
  }

  /**
   * @return the expenses
   */
  public String getExpenses() {
    return expenses;
  }

  /**
   * @param expenses the expenses to set
   */
  public void setExpenses(String expenses) {
    this.expenses = expenses;
  }

  /**
   * @return the fiscalYearEnd
   */
  public String getFiscalYearEnd() {
    return fiscalYearEnd;
  }

  /**
   * @param fiscalYearEnd the fiscalYearEnd to set
   */
  public void setFiscalYearEnd(String fiscalYearEnd) {
    this.fiscalYearEnd = fiscalYearEnd;
  }

  /**
   * @return the fiscalYearStart
   */
  public String getFiscalYearStart() {
    return fiscalYearStart;
  }

  /**
   * @param fiscalYearStart the fiscalYearStart to set
   */
  public void setFiscalYearStart(String fiscalYearStart) {
    this.fiscalYearStart = fiscalYearStart;
  }

  /**
   * @return the grossIncome
   */
  public String getGrossIncome() {
    return grossIncome;
  }

  /**
   * @param grossIncome the grossIncome to set
   */
  public void setGrossIncome(String grossIncome) {
    this.grossIncome = grossIncome;
  }

  /**
   * @return the inventoryAdjustments
   */
  public String getInventoryAdjustments() {
    return inventoryAdjustments;
  }

  /**
   * @param inventoryAdjustments the inventoryAdjustments to set
   */
  public void setInventoryAdjustments(String inventoryAdjustments) {
    this.inventoryAdjustments = inventoryAdjustments;
  }

  /**
   * @return the isCropDisaster
   */
  public boolean isCropDisaster() {
    return cropDisaster;
  }

  /**
   * @param isCropDisaster the isCropDisaster to set
   */
  public void setCropDisaster(boolean isCropDisaster) {
    this.cropDisaster = isCropDisaster;
  }

  /**
   * @return the isCropShare
   */
  public boolean isCropShare() {
    return cropShare;
  }

  /**
   * @param isCropShare the isCropShare to set
   */
  public void setCropShare(boolean isCropShare) {
    this.cropShare = isCropShare;
  }

  /**
   * @return the isFeederMember
   */
  public boolean isFeederMember() {
    return feederMember;
  }

  /**
   * @param isFeederMember the isFeederMember to set
   */
  public void setFeederMember(boolean isFeederMember) {
    this.feederMember = isFeederMember;
  }

  /**
   * @return the isLandlord
   */
  public boolean isLandlord() {
    return landlord;
  }

  /**
   * @param isLandlord the isLandlord to set
   */
  public void setLandlord(boolean isLandlord) {
    this.landlord = isLandlord;
  }

  /**
   * @return the isLivestockDisaster
   */
  public boolean isLivestockDisaster() {
    return livestockDisaster;
  }

  /**
   * @param isLivestockDisaster the isLivestockDisaster to set
   */
  public void setLivestockDisaster(boolean isLivestockDisaster) {
    this.livestockDisaster = isLivestockDisaster;
  }

  /**
   * @return the netFarmIncome
   */
  public String getNetFarmIncome() {
    return netFarmIncome;
  }

  /**
   * @param netFarmIncome the netFarmIncome to set
   */
  public void setNetFarmIncome(String netFarmIncome) {
    this.netFarmIncome = netFarmIncome;
  }

  /**
   * @return the netIncomeAfterAdj
   */
  public String getNetIncomeAfterAdj() {
    return netIncomeAfterAdj;
  }

  /**
   * @param netIncomeAfterAdj the netIncomeAfterAdj to set
   */
  public void setNetIncomeAfterAdj(String netIncomeAfterAdj) {
    this.netIncomeAfterAdj = netIncomeAfterAdj;
  }

  /**
   * @return the netIncomeBeforeAdj
   */
  public String getNetIncomeBeforeAdj() {
    return netIncomeBeforeAdj;
  }

  /**
   * @param netIncomeBeforeAdj the netIncomeBeforeAdj to set
   */
  public void setNetIncomeBeforeAdj(String netIncomeBeforeAdj) {
    this.netIncomeBeforeAdj = netIncomeBeforeAdj;
  }

  /**
   * @return the operationRevisionCount
   */
  public String getOperationRevisionCount() {
    return operationRevisionCount;
  }

  /**
   * @param operationRevisionCount the operationRevisionCount to set
   */
  public void setOperationRevisionCount(String operationRevisionCount) {
    this.operationRevisionCount = operationRevisionCount;
  }

  /**
   * @return the otherDeductions
   */
  public String getOtherDeductions() {
    return otherDeductions;
  }

  /**
   * @param otherDeductions the otherDeductions to set
   */
  public void setOtherDeductions(String otherDeductions) {
    this.otherDeductions = otherDeductions;
  }

  /**
   * @return the partnershipName
   */
  public String getPartnershipName() {
    return partnershipName;
  }

  /**
   * @param partnershipName the partnershipName to set
   */
  public void setPartnershipName(String partnershipName) {
    this.partnershipName = partnershipName;
  }

  /**
   * @return the partnershipPercent
   */
  public String getPartnershipPercent() {
    return partnershipPercent;
  }

  /**
   * @param partnershipPercent the partnershipPercent to set
   */
  public void setPartnershipPercent(String partnershipPercent) {
    this.partnershipPercent = partnershipPercent;
  }

  /**
   * @return the partnershipPin
   */
  public String getPartnershipPin() {
    return partnershipPin;
  }

  /**
   * @param partnershipPin the partnershipPin to set
   */
  public void setPartnershipPin(String partnershipPin) {
    this.partnershipPin = partnershipPin;
  }

  /**
   * @return the productionInsuranceNumber1
   */
  public String getProductionInsuranceNumber1() {
    return productionInsuranceNumber1;
  }

  /**
   * @param productionInsuranceNumber1 the productionInsuranceNumber1 to set
   */
  public void setProductionInsuranceNumber1(String productionInsuranceNumber1) {
    this.productionInsuranceNumber1 = productionInsuranceNumber1;
  }

  /**
   * @return the productionInsuranceNumber2
   */
  public String getProductionInsuranceNumber2() {
    return productionInsuranceNumber2;
  }

  /**
   * @param productionInsuranceNumber2 the productionInsuranceNumber2 to set
   */
  public void setProductionInsuranceNumber2(String productionInsuranceNumber2) {
    this.productionInsuranceNumber2 = productionInsuranceNumber2;
  }

  /**
   * @return the productionInsuranceNumber3
   */
  public String getProductionInsuranceNumber3() {
    return productionInsuranceNumber3;
  }

  /**
   * @param productionInsuranceNumber3 the productionInsuranceNumber3 to set
   */
  public void setProductionInsuranceNumber3(String productionInsuranceNumber3) {
    this.productionInsuranceNumber3 = productionInsuranceNumber3;
  }

  /**
   * @return the productionInsuranceNumber4
   */
  public String getProductionInsuranceNumber4() {
    return productionInsuranceNumber4;
  }

  /**
   * @param productionInsuranceNumber4 the productionInsuranceNumber4 to set
   */
  public void setProductionInsuranceNumber4(String productionInsuranceNumber4) {
    this.productionInsuranceNumber4 = productionInsuranceNumber4;
  }

  /**
   * @return the operationNumber
   */
  public String getOperationNumber() {
    return operationNumber;
  }

  /**
   * @param operationNumber the operationNumber to set
   */
  public void setOperationNumber(String operationNumber) {
    this.operationNumber = operationNumber;
  }

  /**
   * @return the proRatePercent
   */
  public String getProRatePercent() {
    return proRatePercent;
  }

  /**
   * @param proRatePercent the proRatePercent to set
   */
  public void setProRatePercent(String proRatePercent) {
    this.proRatePercent = proRatePercent;
  }

  /**
   * @return the schedule
   */
  public String getSchedule() {
    return schedule;
  }

  /**
   * @param schedule the schedule to set the value to
   */
  public void setSchedule(String schedule) {
    this.schedule = schedule;
  }

  /**
   * @return the scheduleOptions
   */
  public List<ListView> getScheduleOptions() {
    return scheduleOptions;
  }

  /**
   * @param scheduleOptions the scheduleOptions to set the value to
   */
  public void setScheduleOptions(List<ListView> scheduleOptions) {
    this.scheduleOptions = scheduleOptions;
  }

  /**
   * Gets locallyGenerated
   *
   * @return the locallyGenerated
   */
  public boolean isLocallyGenerated() {
    return locallyGenerated;
  }

  /**
   * Sets locallyGenerated
   *
   * @param locallyGenerated the locallyGenerated to set
   */
  public void setLocallyGenerated(boolean locallyGenerated) {
    this.locallyGenerated = locallyGenerated;
  }

  /**
   * Gets isNew
   *
   * @return the isNew
   */
  public boolean isNew() {
    return isNew;
  }

  /**
   * Sets isNew
   *
   * @param newVal the isNew to set
   */
  public void setNew(boolean newVal) {
    this.isNew = newVal;
  }

  /**
   * @return the pyvHasVerifiedScenario
   */
  public boolean isPyvHasVerifiedScenario() {
    return pyvHasVerifiedScenario;
  }

  /**
   * @param pyvHasVerifiedScenario the pyvHasVerifiedScenario to set
   */
  public void setPyvHasVerifiedScenario(boolean pyvHasVerifiedScenario) {
    this.pyvHasVerifiedScenario = pyvHasVerifiedScenario;
  }

  public boolean isTipReportGenerated() {
    if (this.tipReportDocId != null) {
      return true;
    }
    return false;
  }

  public Integer getTipReportDocId() {
    return tipReportDocId;
  }

  public void setTipReportDocId(Integer tipReportDocId) {
    this.tipReportDocId = tipReportDocId;
  }
}
