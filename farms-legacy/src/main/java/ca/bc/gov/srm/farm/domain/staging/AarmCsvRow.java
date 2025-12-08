package ca.bc.gov.srm.farm.domain.staging;

/**
 * 
 */
public final class AarmCsvRow {
	/** document me **/
	private Integer aarmKey;
	
	/** document me **/
	private Integer participantPin;
	
	/** document me **/
	private Integer programYear;
	
	/** document me **/
	private Integer operationNumber;
	
	/** document me **/
	private Double partnerPercent;
	
	/** document me **/
	private Integer inventoryTypeCode;

	/** document me **/
	private Integer inventoryCode;

	/** document me **/
	private String inventoryDescription;

	/** document me **/
	private Integer productionUnit;
	
	/** document me **/
	private Double aarmReferenceP1Price;
	
	/** document me **/
	private Double aarmReferenceP2Price;
	
	/** document me **/
	private Double quantityStart;
	
	/** document me **/
	private Double quantityEnd;

	/**
	 * @return the aarmKey
	 */
	public Integer getAarmKey() {
		return aarmKey;
	}

	/**
	 * @param aarmKey the aarmKey to set
	 */
	public void setAarmKey(Integer aarmKey) {
		this.aarmKey = aarmKey;
	}

	/**
	 * @return the aarmReferenceP1Price
	 */
	public Double getAarmReferenceP1Price() {
		return aarmReferenceP1Price;
	}

	/**
	 * @param aarmReferenceP1Price the aarmReferenceP1Price to set
	 */
	public void setAarmReferenceP1Price(Double aarmReferenceP1Price) {
		this.aarmReferenceP1Price = aarmReferenceP1Price;
	}

	/**
	 * @return the aarmReferenceP2Price
	 */
	public Double getAarmReferenceP2Price() {
		return aarmReferenceP2Price;
	}

	/**
	 * @param aarmReferenceP2Price the aarmReferenceP2Price to set
	 */
	public void setAarmReferenceP2Price(Double aarmReferenceP2Price) {
		this.aarmReferenceP2Price = aarmReferenceP2Price;
	}

	/**
	 * @return the inventoryCode
	 */
	public Integer getInventoryCode() {
		return inventoryCode;
	}

	/**
	 * @param inventoryCode the inventoryCode to set
	 */
	public void setInventoryCode(Integer inventoryCode) {
		this.inventoryCode = inventoryCode;
	}

	/**
	 * @return the inventoryDescription
	 */
	public String getInventoryDescription() {
		return inventoryDescription;
	}

	/**
	 * @param inventoryDescription the inventoryDescription to set
	 */
	public void setInventoryDescription(String inventoryDescription) {
		this.inventoryDescription = inventoryDescription;
	}

	/**
	 * @return the inventoryTypeCode
	 */
	public Integer getInventoryTypeCode() {
		return inventoryTypeCode;
	}

	/**
	 * @param inventoryTypeCode the inventoryTypeCode to set
	 */
	public void setInventoryTypeCode(Integer inventoryTypeCode) {
		this.inventoryTypeCode = inventoryTypeCode;
	}

	/**
	 * @return the operationNumber
	 */
	public Integer getOperationNumber() {
		return operationNumber;
	}

	/**
	 * @param operationNumber the operationNumber to set
	 */
	public void setOperationNumber(Integer operationNumber) {
		this.operationNumber = operationNumber;
	}

	/**
	 * @return the participantPin
	 */
	public Integer getParticipantPin() {
		return participantPin;
	}

	/**
	 * @param participantPin the participantPin to set
	 */
	public void setParticipantPin(Integer participantPin) {
		this.participantPin = participantPin;
	}

	/**
	 * @return the partnerPercent
	 */
	public Double getPartnerPercent() {
		return partnerPercent;
	}

	/**
	 * @param partnerPercent the partnerPercent to set
	 */
	public void setPartnerPercent(Double partnerPercent) {
		this.partnerPercent = partnerPercent;
	}

	/**
	 * @return the productionUnit
	 */
	public Integer getProductionUnit() {
		return productionUnit;
	}

	/**
	 * @param productionUnit the productionUnit to set
	 */
	public void setProductionUnit(Integer productionUnit) {
		this.productionUnit = productionUnit;
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
	 * @return the quantityEnd
	 */
	public Double getQuantityEnd() {
		return quantityEnd;
	}

	/**
	 * @param quantityEnd the quantityEnd to set
	 */
	public void setQuantityEnd(Double quantityEnd) {
		this.quantityEnd = quantityEnd;
	}

	/**
	 * @return the quantityStart
	 */
	public Double getQuantityStart() {
		return quantityStart;
	}

	/**
	 * @param quantityStart the quantityStart to set
	 */
	public void setQuantityStart(Double quantityStart) {
		this.quantityStart = quantityStart;
	}
	
}
