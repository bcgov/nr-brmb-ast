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
package ca.bc.gov.srm.farm.chefs.resource.interim;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class InventoryGridLivestock extends ChefsResource {

	private String openingInventoryUnits;
	private String estimatedEndingInventoryUnits;

	private LabelValue commodity;
	private Double estimatedTotalProduction;

	public String getOpeningInventoryUnits() {
		return openingInventoryUnits;
	}

	public void setOpeningInventoryUnits(String openingInventoryUnits) {
		this.openingInventoryUnits = openingInventoryUnits;
	}

	public String getEstimatedEndingInventoryUnits() {
		return estimatedEndingInventoryUnits;
	}

	public void setEstimatedEndingInventoryUnits(String estimatedEndingInventoryUnits) {
		this.estimatedEndingInventoryUnits = estimatedEndingInventoryUnits;
	}

	public LabelValue getCommodity() {
		return commodity;
	}

	public void setCommodity(LabelValue commodity) {
		this.commodity = commodity;
	}

	public Double getEstimatedTotalProduction() {
		return estimatedTotalProduction;
	}

	public void setEstimatedTotalProduction(Double estimatedTotalProduction) {
		this.estimatedTotalProduction = estimatedTotalProduction;
	}

	@Override
	public String toString() {
		return "InventoryGrid [openingInventoryUnits=" + openingInventoryUnits + ", estimatedEndingInventoryUnits="
		    + estimatedEndingInventoryUnits + ", commodity=" + commodity + "]";
	}

}
