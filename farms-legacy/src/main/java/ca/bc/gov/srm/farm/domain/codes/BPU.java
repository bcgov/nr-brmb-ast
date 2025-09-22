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

import java.util.Arrays;

/**
 * A BPU can be associated with an inventory code, or a structure group code
 */
public class BPU {

  public static final int NUMBER_OF_YEARS = 6;

  private Integer bpuId;
  private Integer programYear;
  private String invSgCode;
  private String invSgCodeDescription;
  private String municipalityCode;
  private String municipalityCodeDescription;
  private String invSgType;

  private BPUYear[] years = new BPUYear[NUMBER_OF_YEARS];
  
  
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
	 * @return the invSgCode
	 */
	public String getInvSgCode() {
		return invSgCode;
	}

	/**
	 * @param invSgCode the invSgCode to set
	 */
	public void setInvSgCode(String invSgCode) {
		this.invSgCode = invSgCode;
	}

	/**
	 * @return the invSgDescription
	 */
	public String getInvSgCodeDescription() {
		return invSgCodeDescription;
	}

	/**
	 * @param invSgDescription the invSgDescription to set
	 */
	public void setInvSgCodeDescription(String invSgDescription) {
		this.invSgCodeDescription = invSgDescription;
	}

	/**
	 * @return the invSgType
	 */
	public String getInvSgType() {
		return invSgType;
	}

	/**
	 * @param invSgType the invSgType to set
	 */
	public void setInvSgType(String invSgType) {
		this.invSgType = invSgType;
	}

	/**
	 * @return the municipalityCode
	 */
	public String getMunicipalityCode() {
		return municipalityCode;
	}

	/**
	 * @param municipalityCode the municipalityCode to set
	 */
	public void setMunicipalityCode(String municipalityCode) {
		this.municipalityCode = municipalityCode;
	}

	/**
	 * @return the municipalityCodeDescription
	 */
	public String getMunicipalityCodeDescription() {
		return municipalityCodeDescription;
	}

	/**
	 * @param municipalityCodeDescription the municipalityCodeDescription to set
	 */
	public void setMunicipalityCodeDescription(String municipalityCodeDescription) {
		this.municipalityCodeDescription = municipalityCodeDescription;
	}

	/**
	 * @return the programYear
	 */
	public Integer getProgramYear() {
		return programYear;
	}

	/**
	 * @param programYear the programYear to set
	 */
	public void setProgramYear(Integer programYear) {
		this.programYear = programYear;
	}

	/**
	 * @return the years
	 */
	public BPUYear[] getYears() {
		return years;
	}

	/**
	 * @param years the years to set
	 */
	public void setYears(BPUYear[] years) {
		this.years = years;
	}

  @Override
  public String toString() {
    return "BPU [bpuId=" + bpuId + ", programYear=" + programYear + ", invSgCode=" + invSgCode + ", invSgCodeDescription=" + invSgCodeDescription
        + ", municipalityCode=" + municipalityCode + ", municipalityCodeDescription=" + municipalityCodeDescription + ", invSgType=" + invSgType
        + ", years=" + Arrays.toString(years) + "]";
  }

}
