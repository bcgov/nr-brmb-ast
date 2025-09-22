package ca.bc.gov.srm.farm.ui.struts.codes.reasonability.expectedproductions;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.ExpectedProduction;

public class ExpectedProductionsForm extends ValidatorForm {
  
  private static final long serialVersionUID = 1L;
  
  private List<ExpectedProduction> expectedProductionItems;
  private int numExpectedProductionItems;
  
  private Integer expectedProductionId;
  private String expectedProductionPerAcre; 
  
  private String inventoryItemCode;
  private String cropUnitCode;
  private String inventoryItemCodeDescription;
  private String municipalityCodeDescription;
  private String cropUnitCodeDescription;
  private String cropUnitCodeSelect;
  private String defaultCropUnitCode;

  
  private String inventoryCodeFilter;
  private String municipalityFilter;
  private String cropUnitFilter;
  private String inventoryDescFilter;
  
  private String inventorySearchInput;
  
  private String inventoryType;
  
  private boolean isSetFilterContext;
  
  private boolean isNew = false;

  
  public List<ExpectedProduction> getExpectedProductionItems() {
    return expectedProductionItems;
  }
  
  public void setExpectedProductionItems(List<ExpectedProduction> expectedProductionItems) {
    this.expectedProductionItems = expectedProductionItems;
  }
  
  public Integer getExpectedProductionId() {
    return expectedProductionId;
  }
  
  public void setExpectedProductionId(Integer expectedProductionId) {
    this.expectedProductionId = expectedProductionId;
  }
  
  public String getExpectedProductionPerAcre() {
    return expectedProductionPerAcre;
  }
  
  public void setExpectedProductionPerAcre(String expectedProductionPerAcre) {
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
  
  public int getNumExpectedProductionItems() {
    return numExpectedProductionItems;
  }
  
  public void setNumExpectedProductionItems(int numExpectedProductionItems) {
    this.numExpectedProductionItems = numExpectedProductionItems;
  }

  public String getInventoryCodeFilter() {
    return inventoryCodeFilter;
  }

  public void setInventoryCodeFilter(String inventoryCodeFilter) {
    this.inventoryCodeFilter = inventoryCodeFilter;
  }

  public String getMunicipalityFilter() {
    return municipalityFilter;
  }

  public void setMunicipalityFilter(String municipalityFilter) {
    this.municipalityFilter = municipalityFilter;
  }

  public String getCropUnitFilter() {
    return cropUnitFilter;
  }

  public void setCropUnitFilter(String cropUnitFilter) {
    this.cropUnitFilter = cropUnitFilter;
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

  public String getInventoryDescFilter() {
    return inventoryDescFilter;
  }

  public void setInventoryDescFilter(String inventoryDescFilter) {
    this.inventoryDescFilter = inventoryDescFilter;
  }

  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  public String getInventorySearchInput() {
    return inventorySearchInput;
  }

  public void setInventorySearchInput(String inventorySearchInput) {
    this.inventorySearchInput = inventorySearchInput;
  }

  public String getCropUnitCodeSelect() {
    return cropUnitCodeSelect;
  }

  public void setCropUnitCodeSelect(String cropUnitCodeSelect) {
    this.cropUnitCodeSelect = cropUnitCodeSelect;
  }

  public String getDefaultCropUnitCode() {
    return defaultCropUnitCode;
  }

  public void setDefaultCropUnitCode(String defaultCropUnitCode) {
    this.defaultCropUnitCode = defaultCropUnitCode;
  }

  public String getInventoryType() {
    return inventoryType;
  }

  public void setInventoryType(String inventoryType) {
    this.inventoryType = inventoryType;
  }

  public boolean isSetFilterContext() {
    return isSetFilterContext;
  }

  public void setSetFilterContext(boolean isSetFilterContext) {
    this.isSetFilterContext = isSetFilterContext;
  }

}
