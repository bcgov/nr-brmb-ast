package ca.bc.gov.srm.farm.domain.codes;

public class SectorDetailCode extends AbstractCode {
  
  private String sectorCode;
  private String sectorCodeDescription;
  private String sectorDetailCodeDescription;
  private String sectorDetailCode;
  
  public String getSectorCode() {
    return sectorCode;
  }

  public void setSectorCode(String sectorCode) {
    this.sectorCode = sectorCode;
  }

  public String getSectorCodeDescription() {
    return sectorCodeDescription;
  }

  public void setSectorCodeDescription(String sectorCodeDescription) {
    this.sectorCodeDescription = sectorCodeDescription;
  }

  public String getSectorDetailCodeDescription() {
    return sectorDetailCodeDescription;
  }

  public void setSectorDetailCodeDescription(String sectorDetailCodeDescription) {
    this.sectorDetailCodeDescription = sectorDetailCodeDescription;
  }

  public String getSectorDetailCode() {
    return sectorDetailCode;
  }

  public void setSectorDetailCode(String sectorDetailCode) {
    this.sectorDetailCode = sectorDetailCode;
  }

  @Override
  public String getDescription() {
    return getSectorDetailCodeDescription();
  }
  
  @Override
  public void setDescription(String description) {
    setSectorDetailCodeDescription(description);
  }
  
  public boolean isMixed() {
    return SectorDetailCodes.isMixed(sectorDetailCode);
  }
}
