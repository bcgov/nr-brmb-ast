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
package ca.bc.gov.srm.farm.ui.struts.calculator.diff;

import java.util.HashMap;
import java.util.Map;

/**
 * @author awilkinson
 * @created Mar 22, 2011
 */
public class DiffReportFormData {

  private Integer diffYear;

  private Integer newPyvNumber;

  private Boolean pyvLocallyUpdated;

  private Boolean pyvAcceptNewData;

  private Boolean pyvHasDifferences;

  private Boolean newCRAMissingOperations;

  private Boolean hasLocallyGeneratedOperations;

  private Map<Integer, Boolean> operationLocallyUpdated = new HashMap<>();

  private Map<Integer, Boolean> operationAcceptNewData = new HashMap<>();

  private Map<Integer, Boolean> operationHasFieldDifferences = new HashMap<>();
  
  private boolean readOnly;
  
  private Integer scenarioRevisionCount;

  public Integer getDiffYear() {
    return diffYear;
  }

  public void setDiffYear(Integer diffYear) {
    this.diffYear = diffYear;
  }

  public Integer getNewPyvNumber() {
    return newPyvNumber;
  }

  public void setNewPyvNumber(Integer newPyvNumber) {
    this.newPyvNumber = newPyvNumber;
  }

  public Boolean getPyvLocallyUpdated() {
    return pyvLocallyUpdated;
  }

  public void setPyvLocallyUpdated(Boolean pyvLocallyUpdated) {
    this.pyvLocallyUpdated = pyvLocallyUpdated;
  }

  public Boolean getPyvAcceptNewData() {
    return pyvAcceptNewData;
  }

  public void setPyvAcceptNewData(Boolean pyvAcceptNewData) {
    this.pyvAcceptNewData = pyvAcceptNewData;
  }

  public Boolean getPyvHasDifferences() {
    return pyvHasDifferences;
  }

  public void setPyvHasDifferences(Boolean pyvHasDifferences) {
    this.pyvHasDifferences = pyvHasDifferences;
  }

  public Boolean getNewCRAMissingOperations() {
    return newCRAMissingOperations;
  }

  public void setNewCRAMissingOperations(Boolean newCRAMissingOperations) {
    this.newCRAMissingOperations = newCRAMissingOperations;
  }

  public Boolean getHasLocallyGeneratedOperations() {
    return hasLocallyGeneratedOperations;
  }

  public void setHasLocallyGeneratedOperations(Boolean hasLocallyGeneratedOperations) {
    this.hasLocallyGeneratedOperations = hasLocallyGeneratedOperations;
  }

  public Map<Integer, Boolean> getOperationLocallyUpdated() {
    return operationLocallyUpdated;
  }

  public void setOperationLocallyUpdated(Map<Integer, Boolean> operationLocallyUpdated) {
    this.operationLocallyUpdated = operationLocallyUpdated;
  }

  public Map<Integer, Boolean> getOperationAcceptNewData() {
    return operationAcceptNewData;
  }

  public void setOperationAcceptNewData(Map<Integer, Boolean> operationAcceptNewData) {
    this.operationAcceptNewData = operationAcceptNewData;
  }

  public Map<Integer, Boolean> getOperationHasFieldDifferences() {
    return operationHasFieldDifferences;
  }

  public void setOperationHasFieldDifferences(Map<Integer, Boolean> operationHasFieldDifferences) {
    this.operationHasFieldDifferences = operationHasFieldDifferences;
  }
  
  public Integer getScenarioRevisionCount() {
    return scenarioRevisionCount;
  }

  public void setScenarioRevisionCount(Integer scenarioRevisionCount) {
    this.scenarioRevisionCount = scenarioRevisionCount;
  }
 
  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public void setOperationLocallyUpdated(int opNum, String value) {
    operationLocallyUpdated.put(new Integer(opNum), Boolean.valueOf(value));
  }

  public void setOperationAcceptNewData(int opNum, String value) {
    operationAcceptNewData.put(new Integer(opNum), Boolean.valueOf(value));
  }

  public void setOperationHasFieldDifferences(int opNum, String value) {
    operationHasFieldDifferences.put(new Integer(opNum), Boolean.valueOf(value));
  }
}
