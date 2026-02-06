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

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

public class PartnershipInformation extends ChefsResource {

	private String partnershipPin;
	private String partnershipName;
	private Double partnershipPercentage;
	
	private String partnershipCorporationName;
	private String partnershipFirstName;
	private String partnershipLastName;

	public PartnershipInformation() {
		super();
	}

  public PartnershipInformation(String partnershipPin, String partnershipCorporationName, String partnershipFirstName, String partnershipLastName,
      Double partnershipPercentage) {
    super();
    this.partnershipPin = partnershipPin;
    this.partnershipCorporationName = partnershipCorporationName;
    this.partnershipFirstName = partnershipFirstName;
    this.partnershipLastName = partnershipLastName;
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

	public String getPartnershipCorporationName() {
    return partnershipCorporationName;
  }

  public void setPartnershipCorporationName(String partnershipCorporationName) {
    this.partnershipCorporationName = partnershipCorporationName;
  }

  public String getPartnershipFirstName() {
    return partnershipFirstName;
  }

  public void setPartnershipFirstName(String partnershipFirstName) {
    this.partnershipFirstName = partnershipFirstName;
  }

  public String getPartnershipLastName() {
    return partnershipLastName;
  }

  public void setPartnershipLastName(String partnershipLastName) {
    this.partnershipLastName = partnershipLastName;
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
		return Objects.equals(partnershipCorporationName, other.partnershipCorporationName)
		    && Objects.equals(partnershipFirstName, other.partnershipFirstName)
        && Objects.equals(partnershipLastName, other.partnershipLastName)
        && Objects.equals(partnershipPercentage, other.partnershipPercentage)
		    && Objects.equals(partnershipPin, other.partnershipPin);
	}

  @Override
  public String toString() {
    return "PartnershipInformation [partnershipPin=" + partnershipPin + ", partnershipPercentage="
        + partnershipPercentage + ", partnershipCorporationName=" + partnershipCorporationName + ", partnershipFirstName=" + partnershipFirstName
        + ", partnershipLastName=" + partnershipLastName + "]";
  }

}
