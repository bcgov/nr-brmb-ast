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
package ca.bc.gov.srm.farm.chefs.resource.coverage;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class CoverageVeggieGrid extends ChefsResource {

	private LabelValue vegetables;
	private String unitType;
	private Double acres;
	private Double squareMeters;

	public LabelValue getVegetables() {
		return vegetables;
	}

	public void setVegetables(LabelValue vegetables) {
		this.vegetables = vegetables;
	}

	public String getUnitType() {
    return unitType;
  }

  public void setUnitType(String unitType) {
    this.unitType = unitType;
  }

  public Double getAcres() {
		return acres;
	}

	public void setAcres(Double acres) {
		this.acres = acres;
	}

	public Double getSquareMeters() {
		return squareMeters;
	}

	public void setSquareMeters(Double squareMeters) {
		this.squareMeters = squareMeters;
	}

}
