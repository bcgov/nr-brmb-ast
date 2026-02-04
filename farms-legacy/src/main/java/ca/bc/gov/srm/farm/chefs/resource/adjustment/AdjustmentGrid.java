/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.adjustment;

import java.util.Objects;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

public class AdjustmentGrid extends ChefsResource {

	private Integer lineCode;
	private Integer programYear;
	private Double revisedAmount;
	private Double previousAmount;
	private String description;
	private String sectionOnTheForm;

	public Integer getLineCode() {
		return lineCode;
	}

	public void setLineCode(Integer lineCode) {
		this.lineCode = lineCode;
	}

	public Integer getProgramYear() {
    return programYear;
  }

  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  public Double getRevisedAmount() {
		return revisedAmount;
	}

	public void setRevisedAmount(Double revisedAmount) {
		this.revisedAmount = revisedAmount;
	}

	public Double getPreviousAmount() {
		return previousAmount;
	}

	public void setPreviousAmount(Double previousAmount) {
		this.previousAmount = previousAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSectionOnTheForm() {
		return sectionOnTheForm;
	}

	public void setSectionOnTheForm(String sectionOnTheForm) {
		this.sectionOnTheForm = sectionOnTheForm;
	}


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AdjustmentGrid other = (AdjustmentGrid) obj;
    return Objects.equals(description, other.description) && Objects.equals(lineCode, other.lineCode)
        && Objects.equals(previousAmount, other.previousAmount) && Objects.equals(programYear, other.programYear)
        && Objects.equals(revisedAmount, other.revisedAmount) && Objects.equals(sectionOnTheForm, other.sectionOnTheForm);
  }

}
