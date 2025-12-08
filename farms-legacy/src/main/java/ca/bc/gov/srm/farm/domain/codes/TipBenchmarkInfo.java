package ca.bc.gov.srm.farm.domain.codes;

import java.util.Date;

public class TipBenchmarkInfo {
  
  private Integer year;
  private Integer operationCount;
  private Date generatedDate;

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Integer getOperationCount() {
    return operationCount;
  }

  public void setOperationCount(Integer operationCount) {
    this.operationCount = operationCount;
  }

  public Date getGeneratedDate() {
    return generatedDate;
  }

  public void setGeneratedDate(Date generatedDate) {
    this.generatedDate = generatedDate;
  }
  
}
