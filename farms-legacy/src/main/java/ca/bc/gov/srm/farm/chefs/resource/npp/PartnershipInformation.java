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

import java.util.Objects;

public class PartnershipInformation {

	// this variable is not use but is in the dataset
	private String parntershipPin;
	private String partnershipPin;
	private String partnershipName;
	private Double partnershipPercentage;
	
	

	public PartnershipInformation() {
		super();
	}

	public PartnershipInformation(String partnershipPin, String partnershipName, Double partnershipPercentage) {
		super();
		this.partnershipPin = partnershipPin;
		this.partnershipName = partnershipName;
		this.partnershipPercentage = partnershipPercentage;
	}

	public String getPartnershipPin() {
		return partnershipPin;
	}

	public void setPartnershipPin(String partnershipPin) {
		this.partnershipPin = partnershipPin;
	}

	public String getPartnershipName() {
		return partnershipName;
	}

	public void setPartnershipName(String partnershipName) {
		this.partnershipName = partnershipName;
	}

	public Double getPartnershipPercentage() {
		return partnershipPercentage;
	}

	public void setPartnershipPercentage(Double partnershipPercentage) {
		this.partnershipPercentage = partnershipPercentage;
	}

	public String getParntershipPin() {
		return parntershipPin;
	}

	public void setParntershipPin(String parntershipPin) {
		this.parntershipPin = parntershipPin;
	}

	@Override
	public int hashCode() {
		return Objects.hash(partnershipName, partnershipPercentage, partnershipPin);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartnershipInformation other = (PartnershipInformation) obj;
		return Objects.equals(partnershipName, other.partnershipName)
		    && Objects.equals(partnershipPercentage, other.partnershipPercentage)
		    && Objects.equals(partnershipPin, other.partnershipPin);
	}

}
