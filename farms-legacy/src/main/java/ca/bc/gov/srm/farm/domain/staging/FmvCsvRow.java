package ca.bc.gov.srm.farm.domain.staging;

/**
 * 
 */
public final class FmvCsvRow {
	/** document me **/
	private Integer programYear;
	/** document me **/
	private Integer period;
	/** document me **/
	private String municipalityCode;
	/** document me **/
	private String inventoryCode;
	/** document me **/
	private String unitCode;
	/** document me **/
	private Double averagePrice;
	/** document me **/
	private Double percentVariance;

	/**
	 * @return the averagePrice
	 */
	public Double getAveragePrice() {
		return averagePrice;
	}

	/**
	 * @param averagePrice
	 *          the averagePrice to set
	 */
	public void setAveragePrice(Double averagePrice) {
		this.averagePrice = averagePrice;
	}

	/**
	 * @return the inventoryCode
	 */
	public String getInventoryCode() {
		return inventoryCode;
	}

	/**
	 * @param inventoryCode
	 *          the inventoryCode to set
	 */
	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}

	/**
	 * @return the municipalityCode
	 */
	public String getMunicipalityCode() {
		return municipalityCode;
	}

	/**
	 * @param municipalityCode
	 *          the municipalityCode to set
	 */
	public void setMunicipalityCode(String municipalityCode) {
		this.municipalityCode = municipalityCode;
	}

	/**
	 * @return the percentVariance
	 */
	public Double getPercentVariance() {
		return percentVariance;
	}

	/**
	 * @param percentVariance
	 *          the percentVariance to set
	 */
	public void setPercentVariance(Double percentVariance) {
		this.percentVariance = percentVariance;
	}

	/**
	 * @return the period
	 */
	public Integer getPeriod() {
		return period;
	}

	/**
	 * @param period
	 *          the period to set
	 */
	public void setPeriod(Integer period) {
		this.period = period;
	}

	/**
	 * @return the programYear
	 */
	public Integer getProgramYear() {
		return programYear;
	}

	/**
	 * @param programYear
	 *          the programYear to set
	 */
	public void setProgramYear(Integer programYear) {
		this.programYear = programYear;
	}

	/**
	 * @return the unitCode
	 */
	public String getUnitCode() {
		return unitCode;
	}

	/**
	 * @param unitCode
	 *          the unitCode to set
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

}
