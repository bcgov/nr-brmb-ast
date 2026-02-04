package ca.bc.gov.srm.farm.domain.codes;

public class ExpectedProduction extends AbstractCode {
  private Integer id;
  private Double expectedProductionPerAcre;
  
  private String inventoryItemCode;
  private String cropUnitCode;
  private String inventoryItemCodeDescription;
  private String municipalityCodeDescription;
  private String cropUnitCodeDescription;
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public Double getExpectedProductionPerAcre() {
    return expectedProductionPerAcre;
  }
  
  public void setExpectedProductionPerAcre(Double expectedProductionPerAcre) {
    this.expectedProductionPerAcre = expectedProductionPerAcre;
  }
  
  public String getInventoryItemCode() {
    return inventoryItemCode;
  }
  
  public void setInventoryItemCode(String inventoryItemCode) {
    this.inventoryItemCode = inventoryItemCode;
  }
  
  public String getCropUnitCode() {
    return cropUnitCode;
  }
  
  public void setCropUnitCode(String cropUnitCode) {
    this.cropUnitCode = cropUnitCode;
  }

  public String getInventoryItemCodeDescription() {
    return inventoryItemCodeDescription;
  }

  public void setInventoryItemCodeDescription(String inventoryItemCodeDescription) {
    this.inventoryItemCodeDescription = inventoryItemCodeDescription;
  }

  public String getMunicipalityCodeDescription() {
    return municipalityCodeDescription;
  }

  public void setMunicipalityCodeDescription(String municipalityCodeDescription) {
    this.municipalityCodeDescription = municipalityCodeDescription;
  }

  public String getCropUnitCodeDescription() {
    return cropUnitCodeDescription;
  }

  public void setCropUnitCodeDescription(String cropUnitCodeDescription) {
    this.cropUnitCodeDescription = cropUnitCodeDescription;
  }  
}
