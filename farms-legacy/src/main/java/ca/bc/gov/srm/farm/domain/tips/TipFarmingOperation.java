package ca.bc.gov.srm.farm.domain.tips;

public class TipFarmingOperation {
  private Integer pin;
  private String producerName;
  private Integer farmOpId;
  private Integer partnershipPin;
  private Integer tipReportDocId;
  private String tipReportStatusCode;
  
  private Boolean isTipParticipant;
  private Boolean isAgriStabilityParticipant;
  
  public Integer getPin() {
    return pin;
  }
  
  public void setPin(Integer pin) {
    this.pin = pin;
  }
  
  public String getProducerName() {
    return producerName;
  }
  
  public void setProducerName(String producerName) {
    this.producerName = producerName;
  }
  
  public Integer getFarmOpId() {
    return farmOpId;
  }
  
  public void setFarmOpId(Integer farmOpId) {
    this.farmOpId = farmOpId;
  }
  
  public Integer getPartnershipPin() {
    return partnershipPin;
  }
  
  public void setPartnershipPin(Integer partnershipPin) {
    this.partnershipPin = partnershipPin;
  }
  
  public Integer getTipReportDocId() {
    return tipReportDocId;
  }
  
  public void setTipReportDocId(Integer tipReportDocId) {
    this.tipReportDocId = tipReportDocId;
  }

  public Boolean getIsTipParticipant() {
    return isTipParticipant;
  }

  public void setIsTipParticipant(Boolean isTipParticipant) {
    this.isTipParticipant = isTipParticipant;
  }

  public Boolean getIsAgriStabilityParticipant() {
    return isAgriStabilityParticipant;
  }

  public void setIsAgriStabilityParticipant(Boolean isAgriStabilityParticipant) {
    this.isAgriStabilityParticipant = isAgriStabilityParticipant;
  }

  public String getTipReportStatusCode() {
    return tipReportStatusCode;
  }

  public void setTipReportStatusCode(String tipReportStatusCode) {
    this.tipReportStatusCode = tipReportStatusCode;
  }
}
