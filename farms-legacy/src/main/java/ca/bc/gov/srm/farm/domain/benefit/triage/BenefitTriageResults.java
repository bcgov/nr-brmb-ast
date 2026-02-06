/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.benefit.triage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BenefitTriageResults {

  @JsonProperty("fifoItemResults")
  private List<BenefitTriageItemResult> triageItemResults;
  
  private String unexpectedError;
  
  public BenefitTriageResults() {
    triageItemResults = new ArrayList<>();
  }

  public List<BenefitTriageItemResult> getTriageItemResults() {
    return triageItemResults;
  }

  public void setTriageItemResults(List<BenefitTriageItemResult> triageItemResults) {
    this.triageItemResults = triageItemResults;
  }

  public String getUnexpectedError() {
    return unexpectedError;
  }

  public void setUnexpectedError(String unexpectedError) {
    this.unexpectedError = unexpectedError;
  }

  @Override
  public String toString() {
    return "TriageResults [triageItemResults=" + triageItemResults + ", unexpectedError=" + unexpectedError + "]";
  }
  
}