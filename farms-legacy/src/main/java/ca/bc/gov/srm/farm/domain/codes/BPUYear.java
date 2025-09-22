/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

/**
 * 
 */
public class BPUYear {
  private Integer bpuId; // ID of parent
  private Integer year;
  private Double averageMargin;
  private Double averageExpense;
  private Integer revisionCount;
  
	/**
	 * @return the averageMargin
	 */
	public Double getAverageMargin() {
		return averageMargin;
	}
	
	/**
	 * @param averageMargin the averageMargin to set
	 */
	public void setAverageMargin(Double averageMargin) {
		this.averageMargin = averageMargin;
	}
	
	/**
	 * @return the averageExpense
	 */
	public Double getAverageExpense() {
		return averageExpense;
	}
	
	/**
	 * @param averageExpense the averageExpense to set
	 */
	public void setAverageExpense(Double averageExpense) {
		this.averageExpense = averageExpense;
	}
	
	/**
	 * @return the bpuId
	 */
	public Integer getBpuId() {
		return bpuId;
	}
	/**
	 * @param bpuId the bpuId to set
	 */
	public void setBpuId(Integer bpuId) {
		this.bpuId = bpuId;
	}

	/**
	 * @return the revisionCount
	 */
	public Integer getRevisionCount() {
		return revisionCount;
	}
	
	/**
	 * @param revisionCount the revisionCount to set
	 */
	public void setRevisionCount(Integer revisionCount) {
		this.revisionCount = revisionCount;
	}
	
	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	
	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

  @Override
  public String toString() {
    return "BPUYear [bpuId=" + bpuId + ", year=" + year + ", averageMargin=" + averageMargin + ", averageExpense=" + averageExpense
        + ", revisionCount=" + revisionCount + "]";
  }
}
