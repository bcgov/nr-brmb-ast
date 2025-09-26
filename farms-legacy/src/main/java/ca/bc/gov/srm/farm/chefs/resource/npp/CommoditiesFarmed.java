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
package ca.bc.gov.srm.farm.chefs.resource.npp;

public class CommoditiesFarmed {

	private Boolean crops;
	private Boolean livestock;

	public Boolean getCrops() {
		return crops;
	}

	public void setCrops(Boolean crops) {
		this.crops = crops;
	}

	public Boolean getLivestock() {
		return livestock;
	}

	public void setLivestock(Boolean livestock) {
		this.livestock = livestock;
	}

	@Override
	public String toString() {
		return "CommoditiesFarmed [crops=" + crops + ", livestock=" + livestock + "]";
	}

}
