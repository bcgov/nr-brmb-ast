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
package ca.bc.gov.srm.farm.domain.fifo;

import java.util.List;

public class FifoResults {

  private List<FifoItemResult> fifoItemResults;
  
  private String unexpectedError;

  public List<FifoItemResult> getFifoItemResults() {
    return fifoItemResults;
  }

  public void setFifoItemResults(List<FifoItemResult> fifoItemResults) {
    this.fifoItemResults = fifoItemResults;
  }

  public String getUnexpectedError() {
    return unexpectedError;
  }

  public void setUnexpectedError(String unexpectedError) {
    this.unexpectedError = unexpectedError;
  }

  @Override
  public String toString() {
    return "FifoResults [fifoItemResults=" + fifoItemResults + ", unexpectedError=" + unexpectedError + "]";
  }
  
}