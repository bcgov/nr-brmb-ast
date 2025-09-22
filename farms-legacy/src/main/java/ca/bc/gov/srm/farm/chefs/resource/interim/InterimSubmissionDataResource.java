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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.srm.farm.chefs.resource.common.CattleGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.CropGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.CustomFeedGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.GrainGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.IncomeExpenseGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.LivestockGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.NurseryGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.OtherPucGrid;
import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class InterimSubmissionDataResource extends ChefsSubmissionDataResource {

  private String participantName;
  private Integer agriStabilityAgriInvestPin;
  private String telephone;
  private String businessStructure;
  private String email;
  private Date fiscalYearStart;
  private Date fiscalYearEnd;
  private LabelValue municipalityCode;

  private LabelValue farmType;
  private String sinNumber;
  private String businessTaxNumberBn;
  private String trustNumber;
  private String trustBusinessNumber;
  private String bandNumber;

  private String provinceTerritoryOfMainFarmstead;

  private List<String> cropsFarmed;
  private List<String> livestockFarmed;
  private List<IncomeExpenseGrid> allowableIncomeGrid;
  private List<IncomeExpenseGrid> allowableExpensesGrid;
  private Double totalAllowableIncome;
  private Double totalAllowableExpenses;

  private List<CropGrid> berryGrid = new ArrayList<>();
  private List<CropGrid> vegetableGrid = new ArrayList<>();
  private List<CropGrid> treeFruitGrid = new ArrayList<>();
  private List<GrainGrid> grainGrid = new ArrayList<>();
  private List<NurseryGrid> nurseryGrid = new ArrayList<>();
  private List<CropGrid> neHorticultureGrid = new ArrayList<>();

  private List<CattleGrid> cattleGrid = new ArrayList<>();
  private List<LivestockGrid> poultryGrid = new ArrayList<>();
  private List<LivestockGrid> swineGrid = new ArrayList<>();
  private List<LivestockGrid> otherGrid = new ArrayList<>();
  private List<OtherPucGrid> opdGrid = new ArrayList<>();
  private List<CustomFeedGrid> customFedGrid = new ArrayList<>();

  @JsonProperty("whyAreYouApplyingForAnInterimPayment")
  private String reasonForApplying;

  private List<IncomeGrid> incomeGrid = new ArrayList<>();
  private List<InventoryGridLivestock> inventoryGridLivestock = new ArrayList<>();
  private List<ExpenseGrid> expenseGrid = new ArrayList<>();
  private List<ProductionGrid> productionGrid = new ArrayList<>();
  private CommoditiesFarmed commoditiesFarmed;

  private Double productiveCapacityLC104;
  private Double productiveCapacityLC105;
  private Double productiveCapacityLC106;
  private Double productiveCapacityLC108;
  private Double productiveCapacityLC109;
  private Double productiveCapacityLC123;
  private Double productiveCapacityLC124;
  private Double productiveCapacityLC125;
  private Double productiveCapacityLC141;
  private Double productiveCapacityLC142;
  private Double productiveCapacityLC143;
  private Double productiveCapacityLC144;
  private Double productiveCapacityLC145;
  private Double productiveCapacityLC4001;

  private String specifyOtherLivestock;
  private String doYouHaveUninsuredAcres;
  private String doYouHaveProductionInsuranceForTheProgramYear;
  private String hasYourProductiveCapacityChangedDuringTheProgramYear;
  private String haveYouSubmittedACompleteAgriStabilityApplicationForThePreviousYear;
  private String haveYouCompletedAProductionCycleAndSixMonthsOfFarmingActivityThisFiscalYear;
  private String wereYouUnableToCompleteAProductionCycleDueToCircumstancesBeyondYourControl;

  private String onBehalfOfParticipant;
  private String signatureFirstName;
  private String signatureLastName;
  private String signatureDate;
  private String howDoYouKnowTheParticipant;

  @JsonIgnore
  private String pattern = "yyyy-MM-dd";
  @JsonIgnore
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

  public String getParticipantName() {
    return participantName;
  }

  public void setParticipantName(String participantName) {
    this.participantName = participantName;
  }

  public Integer getAgriStabilityAgriInvestPin() {
    return agriStabilityAgriInvestPin;
  }

  public void setAgriStabilityAgriInvestPin(Integer agriStabilityAgriInvestPin) {
    this.agriStabilityAgriInvestPin = agriStabilityAgriInvestPin;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getBusinessStructure() {
    return businessStructure;
  }

  public void setBusinessStructure(String businessStructure) {
    this.businessStructure = businessStructure;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBusinessTaxNumberBn() {
    return businessTaxNumberBn != null ? businessTaxNumberBn.replaceAll("\\s", "") : null;
  }

  public void setBusinessTaxNumberBn(String businessTaxNumberBn) {
    this.businessTaxNumberBn = businessTaxNumberBn;
  }

  public String getSinNumber() {
    return sinNumber;
  }

  public void setSinNumber(String sinNumber) {
    this.sinNumber = sinNumber;
  }
  
  public String getFiscalYearEnd() {
    return simpleDateFormat.format(fiscalYearEnd);
  }

  public Date getFiscalYearEndDate() {
    return fiscalYearEnd;
  }

  public void setFiscalYearEnd(Date fiscalYearEnd) {
    this.fiscalYearEnd = fiscalYearEnd;
  }

  public List<IncomeGrid> getIncomeGrid() {
    return incomeGrid;
  }

  public void setIncomeGrid(List<IncomeGrid> incomeGrid) {
    this.incomeGrid = incomeGrid;
  }

  public List<InventoryGridLivestock> getInventoryGridLivestock() {
    return inventoryGridLivestock;
  }

  public void setInventoryGridLivestock(List<InventoryGridLivestock> inventoryGridLivestock) {
    this.inventoryGridLivestock = inventoryGridLivestock;
  }

  public CommoditiesFarmed getCommoditiesFarmed() {
    return commoditiesFarmed;
  }

  public void setCommoditiesFarmed(CommoditiesFarmed commoditiesFarmed) {
    this.commoditiesFarmed = commoditiesFarmed;
  }

  public List<ExpenseGrid> getExpenseGrid() {
    return expenseGrid;
  }

  public void setExpenseGrid(List<ExpenseGrid> expenseGrid) {
    this.expenseGrid = expenseGrid;
  }

  public List<ProductionGrid> getProductionGrid() {
    return productionGrid;
  }

  public void setProductiongrid(List<ProductionGrid> productionGrid) {
    this.productionGrid = productionGrid;
  }

  public Double getProductiveCapacityLC104() {
    return productiveCapacityLC104;
  }

  public void setProductiveCapacityLC104(Double productiveCapacityLC104) {
    this.productiveCapacityLC104 = productiveCapacityLC104;
  }

  public Double getProductiveCapacityLC105() {
    return productiveCapacityLC105;
  }

  public void setProductiveCapacityLC105(Double productiveCapacityLC105) {
    this.productiveCapacityLC105 = productiveCapacityLC105;
  }

  public Double getProductiveCapacityLC106() {
    return productiveCapacityLC106;
  }

  public void setProductiveCapacityLC106(Double productiveCapacityLC106) {
    this.productiveCapacityLC106 = productiveCapacityLC106;
  }

  public Double getProductiveCapacityLC108() {
    return productiveCapacityLC108;
  }

  public void setProductiveCapacityLC108(Double productiveCapacityLC108) {
    this.productiveCapacityLC108 = productiveCapacityLC108;
  }

  public Double getProductiveCapacityLC109() {
    return productiveCapacityLC109;
  }

  public void setProductiveCapacityLC109(Double productiveCapacityLC109) {
    this.productiveCapacityLC109 = productiveCapacityLC109;
  }

  public Double getProductiveCapacityLC123() {
    return productiveCapacityLC123;
  }

  public void setProductiveCapacityLC123(Double productiveCapacityLC123) {
    this.productiveCapacityLC123 = productiveCapacityLC123;
  }

  public Double getProductiveCapacityLC141() {
    return productiveCapacityLC141;
  }

  public void setProductiveCapacityLC141(Double productiveCapacityLC141) {
    this.productiveCapacityLC141 = productiveCapacityLC141;
  }

  public Double getProductiveCapacityLC142() {
    return productiveCapacityLC142;
  }

  public void setProductiveCapacityLC142(Double productiveCapacityLC142) {
    this.productiveCapacityLC142 = productiveCapacityLC142;
  }

  public Double getProductiveCapacityLC143() {
    return productiveCapacityLC143;
  }

  public void setProductiveCapacityLC143(Double productiveCapacityLC143) {
    this.productiveCapacityLC143 = productiveCapacityLC143;
  }

  public Double getProductiveCapacityLC144() {
    return productiveCapacityLC144;
  }

  public void setProductiveCapacityLC144(Double productiveCapacityLC144) {
    this.productiveCapacityLC144 = productiveCapacityLC144;
  }

  public Double getProductiveCapacityLC145() {
    return productiveCapacityLC145;
  }

  public void setProductiveCapacityLC145(Double productiveCapacityLC145) {
    this.productiveCapacityLC145 = productiveCapacityLC145;
  }

  public Double getProductiveCapacityLC4001() {
    return productiveCapacityLC4001;
  }

  public void setProductiveCapacityLC4001(Double productiveCapacityLC4001) {
    this.productiveCapacityLC4001 = productiveCapacityLC4001;
  }

  public String getFiscalYearStart() {
    return simpleDateFormat.format(fiscalYearStart);
  }

  public Date getFiscalYearStartDate() {
    return fiscalYearStart;
  }

  public void setFiscalYearStart(Date fiscalYearStart) {
    this.fiscalYearStart = fiscalYearStart;
  }

  public LabelValue getMunicipalityCode() {
    return municipalityCode;
  }

  public void setMunicipalityCode(LabelValue municipalityCode) {
    this.municipalityCode = municipalityCode;
  }

  public String getDoYouHaveUninsuredAcres() {
    return doYouHaveUninsuredAcres;
  }

  public void setDoYouHaveUninsuredAcres(String doYouHaveUninsuredAcres) {
    this.doYouHaveUninsuredAcres = doYouHaveUninsuredAcres;
  }

  public String getDoYouHaveProductionInsuranceForTheProgramYear() {
    return doYouHaveProductionInsuranceForTheProgramYear;
  }

  public void setDoYouHaveProductionInsuranceForTheProgramYear(String doYouHaveProductionInsuranceForTheProgramYear) {
    this.doYouHaveProductionInsuranceForTheProgramYear = doYouHaveProductionInsuranceForTheProgramYear;
  }

  public String getHasYourProductiveCapacityChangedDuringTheProgramYear() {
    return hasYourProductiveCapacityChangedDuringTheProgramYear;
  }

  public void setHasYourProductiveCapacityChangedDuringTheProgramYear(String hasYourProductiveCapacityChangedDuringTheProgramYear) {
    this.hasYourProductiveCapacityChangedDuringTheProgramYear = hasYourProductiveCapacityChangedDuringTheProgramYear;
  }

  public String getHaveYouSubmittedACompleteAgriStabilityApplicationForThePreviousYear() {
    return haveYouSubmittedACompleteAgriStabilityApplicationForThePreviousYear;
  }

  public void setHaveYouSubmittedACompleteAgriStabilityApplicationForThePreviousYear(
      String haveYouSubmittedACompleteAgriStabilityApplicationForThePreviousYear) {
    this.haveYouSubmittedACompleteAgriStabilityApplicationForThePreviousYear = haveYouSubmittedACompleteAgriStabilityApplicationForThePreviousYear;
  }

  public String getHaveYouCompletedAProductionCycleAndSixMonthsOfFarmingActivityThisFiscalYear() {
    return haveYouCompletedAProductionCycleAndSixMonthsOfFarmingActivityThisFiscalYear;
  }

  public void setHaveYouCompletedAProductionCycleAndSixMonthsOfFarmingActivityThisFiscalYear(
      String haveYouCompletedAProductionCycleAndSixMonthsOfFarmingActivityThisFiscalYear) {
    this.haveYouCompletedAProductionCycleAndSixMonthsOfFarmingActivityThisFiscalYear = haveYouCompletedAProductionCycleAndSixMonthsOfFarmingActivityThisFiscalYear;
  }

  public String getOnBehalfOfParticipant() {
    return onBehalfOfParticipant;
  }

  public void setOnBehalfOfParticipant(String onBehalfOfParticipant) {
    this.onBehalfOfParticipant = onBehalfOfParticipant;
  }

  public String getSignatureFirstName() {
    return signatureFirstName;
  }

  public void setSignatureFirstName(String signatureFirstName) {
    this.signatureFirstName = signatureFirstName;
  }

  public String getSignatureLastName() {
    return signatureLastName;
  }

  public void setSignatureLastName(String signatureLastName) {
    this.signatureLastName = signatureLastName;
  }

  public String getSignatureDate() {
    return signatureDate;
  }

  public void setSignatureDate(String signatureDate) {
    this.signatureDate = signatureDate;
  }

  public String getHowDoYouKnowTheParticipant() {
    return howDoYouKnowTheParticipant;
  }

  public void setHowDoYouKnowTheParticipant(String howDoYouKnowTheParticipant) {
    this.howDoYouKnowTheParticipant = howDoYouKnowTheParticipant;
  }

  public SimpleDateFormat getSimpleDateFormat() {
    return simpleDateFormat;
  }

  public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
    this.simpleDateFormat = simpleDateFormat;
  }

  public void setProductionGrid(List<ProductionGrid> productionGrid) {
    this.productionGrid = productionGrid;
  }

  public String getReasonForApplying() {
    return reasonForApplying;
  }

  public void setReasonForApplying(String reasonForApplying) {
    this.reasonForApplying = reasonForApplying;
  }

  public String getSpecifyOtherLivestock() {
    return specifyOtherLivestock;
  }

  public void setSpecifyOtherLivestock(String specifyOtherLivestock) {
    this.specifyOtherLivestock = specifyOtherLivestock;
  }

  public LabelValue getFarmType() {
    return farmType;
  }

  public void setFarmType(LabelValue farmType) {
    this.farmType = farmType;
  }

  public String getTrustNumber() {
    return trustNumber;
  }

  public void setTrustNumber(String trustNumber) {
    this.trustNumber = trustNumber;
  }

  public String getTrustBusinessNumber() {
    return trustBusinessNumber;
  }

  public void setTrustBusinessNumber(String trustBusinessNumber) {
    this.trustBusinessNumber = trustBusinessNumber;
  }

  public String getBandNumber() {
    return bandNumber;
  }

  public void setBandNumber(String bandNumber) {
    this.bandNumber = bandNumber;
  }

  public List<String> getCropsFarmed() {
    return cropsFarmed;
  }

  public void setCropsFarmed(List<String> cropsFarmed) {
    this.cropsFarmed = cropsFarmed;
  }

  public String getProvinceTerritoryOfMainFarmstead() {
    return provinceTerritoryOfMainFarmstead;
  }

  public void setProvinceTerritoryOfMainFarmstead(String provinceTerritoryOfMainFarmstead) {
    this.provinceTerritoryOfMainFarmstead = provinceTerritoryOfMainFarmstead;
  }

  public List<String> getLivestockFarmed() {
    return livestockFarmed;
  }

  public void setLivestockFarmed(List<String> livestockFarmed) {
    this.livestockFarmed = livestockFarmed;
  }

  public List<IncomeExpenseGrid> getAllowableIncomeGrid() {
    return allowableIncomeGrid;
  }

  public void setAllowableIncomeGrid(List<IncomeExpenseGrid> allowableIncomeGrid) {
    this.allowableIncomeGrid = allowableIncomeGrid;
  }

  public List<IncomeExpenseGrid> getAllowableExpensesGrid() {
    return allowableExpensesGrid;
  }

  public void setAllowableExpensesGrid(List<IncomeExpenseGrid> allowableExpensesGrid) {
    this.allowableExpensesGrid = allowableExpensesGrid;
  }

  public Double getTotalAllowableIncome() {
    return totalAllowableIncome;
  }

  public void setTotalAllowableIncome(Double totalAllowableIncome) {
    this.totalAllowableIncome = totalAllowableIncome;
  }

  public Double getTotalAllowableExpenses() {
    return totalAllowableExpenses;
  }

  public void setTotalAllowableExpenses(Double totalAllowableExpenses) {
    this.totalAllowableExpenses = totalAllowableExpenses;
  }

  public List<CropGrid> getBerryGrid() {
    return berryGrid;
  }

  public void setBerryGrid(List<CropGrid> berryGrid) {
    this.berryGrid = berryGrid;
  }

  public List<CropGrid> getVegetableGrid() {
    return vegetableGrid;
  }

  public void setVegetableGrid(List<CropGrid> vegetableGrid) {
    this.vegetableGrid = vegetableGrid;
  }

  public List<CropGrid> getTreeFruitGrid() {
    return treeFruitGrid;
  }

  public void setTreeFruitGrid(List<CropGrid> treeFruitGrid) {
    this.treeFruitGrid = treeFruitGrid;
  }

  public List<GrainGrid> getGrainGrid() {
    return grainGrid;
  }

  public void setGrainGrid(List<GrainGrid> grainGrid) {
    this.grainGrid = grainGrid;
  }

  public List<NurseryGrid> getNurseryGrid() {
    return nurseryGrid;
  }

  public void setNurseryGrid(List<NurseryGrid> nurseryGrid) {
    this.nurseryGrid = nurseryGrid;
  }

  public List<CropGrid> getNeHorticultureGrid() {
    return neHorticultureGrid;
  }

  public void setNeHorticultureGrid(List<CropGrid> neHorticultureGrid) {
    this.neHorticultureGrid = neHorticultureGrid;
  }

  public List<CattleGrid> getCattleGrid() {
    return cattleGrid;
  }

  public void setCattleGrid(List<CattleGrid> cattleGrid) {
    this.cattleGrid = cattleGrid;
  }

  public List<LivestockGrid> getPoultryGrid() {
    return poultryGrid;
  }

  public void setPoultryGrid(List<LivestockGrid> poultryGrid) {
    this.poultryGrid = poultryGrid;
  }

  public List<LivestockGrid> getSwineGrid() {
    return swineGrid;
  }

  public void setSwineGrid(List<LivestockGrid> swineGrid) {
    this.swineGrid = swineGrid;
  }

  public List<LivestockGrid> getOtherGrid() {
    return otherGrid;
  }

  public void setOtherGrid(List<LivestockGrid> otherGrid) {
    this.otherGrid = otherGrid;
  }

  public List<OtherPucGrid> getOpdGrid() {
    return opdGrid;
  }

  public void setOpdGrid(List<OtherPucGrid> opdGrid) {
    this.opdGrid = opdGrid;
  }

  public List<CustomFeedGrid> getCustomFedGrid() {
    return customFedGrid;
  }

  public void setCustomFedGrid(List<CustomFeedGrid> customFedGrid) {
    this.customFedGrid = customFedGrid;
  }

  public Double getProductiveCapacityLC124() {
    return productiveCapacityLC124;
  }

  public void setProductiveCapacityLC124(Double productiveCapacityLC124) {
    this.productiveCapacityLC124 = productiveCapacityLC124;
  }

  public Double getProductiveCapacityLC125() {
    return productiveCapacityLC125;
  }

  public void setProductiveCapacityLC125(Double productiveCapacityLC125) {
    this.productiveCapacityLC125 = productiveCapacityLC125;
  }

  public String getWereYouUnableToCompleteAProductionCycleDueToCircumstancesBeyondYourControl() {
    return wereYouUnableToCompleteAProductionCycleDueToCircumstancesBeyondYourControl;
  }

  public void setWereYouUnableToCompleteAProductionCycleDueToCircumstancesBeyondYourControl(
      String wereYouUnableToCompleteAProductionCycleDueToCircumstancesBeyondYourControl) {
    this.wereYouUnableToCompleteAProductionCycleDueToCircumstancesBeyondYourControl = wereYouUnableToCompleteAProductionCycleDueToCircumstancesBeyondYourControl;
  }

}
