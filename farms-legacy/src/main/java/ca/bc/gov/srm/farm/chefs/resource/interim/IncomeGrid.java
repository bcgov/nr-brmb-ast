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

public class IncomeGrid extends ChefsResource {

	private LabelValue incomeCategories;
	private Double estimatedTotalReceived;

	public LabelValue getIncomeCategories() {
		return incomeCategories;
	}

	public void setIncomeCategories(LabelValue incomeCategories) {
		this.incomeCategories = incomeCategories;
	}

	public Double getEstimatedTotalReceived() {
		return estimatedTotalReceived;
	}

	public void setEstimatedTotalReceived(Double estimatedTotalReceived) {
		this.estimatedTotalReceived = estimatedTotalReceived;
	}

	@Override
	public String toString() {
		return "IncomeGrid [incomeCategories=" + incomeCategories + ", estimatedTotalReceived=" + estimatedTotalReceived
		    + "]";
	}

}
