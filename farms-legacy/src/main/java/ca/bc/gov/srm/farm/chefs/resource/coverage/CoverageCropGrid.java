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

public class CoverageCropGrid extends ChefsResource {

	private LabelValue crop;
	private Double acres;
	private Double squareMeters;

	public LabelValue getCrop() {
		return crop;
	}

	public void setCrop(LabelValue crop) {
		this.crop = crop;
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
