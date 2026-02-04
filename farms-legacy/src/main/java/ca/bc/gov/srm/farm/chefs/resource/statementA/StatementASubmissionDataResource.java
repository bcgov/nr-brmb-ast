/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.statementA;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.chefs.resource.common.IncomeExpenseGrid;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalAccruals;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalBaseDataResource;

public class StatementASubmissionDataResource extends SupplementalBaseDataResource {

  private String corporationName;
  private String firstNameCorporateContact;
  private String lastNameCorporateContact;
  private Integer agriStabilityAgriInvestPin;
  private String email;
  private String accountingMethod;
  private Double farmingOperationPercentage;
  private List<StatementAPartner> partnershipGrid;
  private List<StatementANonParticipantPartner> nonParticipantPartnershipGrid;
  private List<StatementACombined> combinedGrid;
  private String address;
  private String townCity;
  private String province;
  private String postalCode;
  private String telephone;
  private String onBehalfOfParticipant;

  private String signature;
  private String signatureFirstName;
  private String signatureLastName;
  private String signatureDate;
  private String howDoYouKnowTheParticipant;

  private Date fiscalYearStart;
  private Date fiscalYearEnd;

  private Integer numberOfYearsFarmed;
  private String provinceTerritoryOfMainFarmstead;
  private String businessStructure;
  private String was2024FinalYearOfFarming;
  private String completedProductionCycle;
  private String unableToCompleteBecauseOfDisaster;
  private String doYouHaveMultipleOperations;
  private String decreasedParticipant;
  private Date dateOfDeath;

  private LabelValue farmType;
  private String sinNumber;
  private String businessTaxNumber;
  private String trustNumber;
  private String trustBusinessNumber;
  private String bandNumber;

  private Double totalAllowableIncome;
  private Double totalNonAllowableIncome;
  private Double totalAllowableIncomeSummary;
  private Double totalNonAllowableIncomeSummary;
  private Double totalIncome;
  private Double totalAllowableExpenses;
  private Double totalNonAllowableExpenses;
  private Double totalAllowableExpensesSummary;
  private Double totalNonAllowableExpensesSummary;
  private Double totalExpenses;
  private Double SummaryOfIncome;
  private Double SummaryOfExpenses;
  private Double netIncomeBeforeAdjustments;
  private Double optionalAdjustments;
  private Double mandatoryAdjustments;
  private Double totalAdjustments;
  private Double netIncomeAfterAdjustments;

  private SupplementalAccruals checkAllThatApply;
  private LabelValue typeOfAnimalCustomFed;

  private List<IncomeExpenseGrid> allowableIncomeGrid;
  private List<IncomeExpenseGrid> nonAllowablesGrid;
  private List<IncomeExpenseGrid> allowableExpensesGrid;
  private List<IncomeExpenseGrid> nonAllowableExpensesGrid;

  private String authorizeThirdParty;
  private String thirdPartyFirstName;
  private String thirdPartyBusinessName;
  private String thirdPartyLastName;
  private String thirdPartyAddress;
  private String thirdPartyTownCity;
  private String thirdPartyProvince;
  private String thirdPartyPostalCode;
  private String thirdPartyTelephone;
  private String thirdPartyFax;
  private String thirdPartyEmail;
  private String copyOfCOB;

  @JsonIgnore
  private String pattern = "yyyy-MM-dd";
  @JsonIgnore
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

  public String getCorporationName() {
    return corporationName;
  }

  public void setCorporationName(String corporationName) {
    this.corporationName = corporationName;
  }

  public String getFirstNameCorporateContact() {
    return firstNameCorporateContact;
  }

  public void setFirstNameCorporateContact(String firstNameCorporateContact) {
    this.firstNameCorporateContact = firstNameCorporateContact;
  }

  public String getLastNameCorporateContact() {
    return lastNameCorporateContact;
  }

  public void setLastNameCorporateContact(String lastNameCorporateContact) {
    this.lastNameCorporateContact = lastNameCorporateContact;
  }

  public Integer getAgriStabilityAgriInvestPin() {
    return agriStabilityAgriInvestPin;
  }

  public void setAgriStabilityAgriInvestPin(Integer agriStabilityAgriInvestPin) {
    this.agriStabilityAgriInvestPin = agriStabilityAgriInvestPin;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAccountingMethod() {
    return accountingMethod;
  }

  public void setAccountingMethod(String accountingMethod) {
    this.accountingMethod = accountingMethod;
  }

  public Double getFarmingOperationPercentage() {
    return farmingOperationPercentage;
  }

  public void setFarmingOperationPercentage(Double farmingOperationPercentage) {
    this.farmingOperationPercentage = farmingOperationPercentage;
  }

  public List<StatementAPartner> getPartnershipGrid() {
    return partnershipGrid;
  }

  public void setPartnershipGrid(List<StatementAPartner> partnershipGrid) {
    this.partnershipGrid = partnershipGrid;
  }

  public List<StatementANonParticipantPartner> getNonParticipantPartnershipGrid() {
    return nonParticipantPartnershipGrid;
  }

  public void setNonParticipantPartnershipGrid(List<StatementANonParticipantPartner> nonParticipantPartnershipGrid) {
    this.nonParticipantPartnershipGrid = nonParticipantPartnershipGrid;
  }

  public List<StatementACombined> getCombinedGrid() {
    return combinedGrid;
  }

  public void setCombinedGrid(List<StatementACombined> combinedGrid) {
    this.combinedGrid = combinedGrid;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getTownCity() {
    return townCity;
  }

  public void setTownCity(String townCity) {
    this.townCity = townCity;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getOnBehalfOfParticipant() {
    return onBehalfOfParticipant;
  }

  public void setOnBehalfOfParticipant(String onBehalfOfParticipant) {
    this.onBehalfOfParticipant = onBehalfOfParticipant;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
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

  public String getFiscalYearStart() {
    return simpleDateFormat.format(fiscalYearStart);
  }

  public Date getFiscalYearStartDate() {
    return fiscalYearStart;
  }

  public void setFiscalYearStart(Date fiscalYearStart) {
    this.fiscalYearStart = fiscalYearStart;
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

  public String getProvinceTerritoryOfMainFarmstead() {
    return provinceTerritoryOfMainFarmstead;
  }

  public void setProvinceTerritoryOfMainFarmstead(String provinceTerritoryOfMainFarmstead) {
    this.provinceTerritoryOfMainFarmstead = provinceTerritoryOfMainFarmstead;
  }

  public String getBusinessStructure() {
    return businessStructure;
  }

  public void setBusinessStructure(String businessStructure) {
    this.businessStructure = businessStructure;
  }

  public Integer getNumberOfYearsFarmed() {
    return numberOfYearsFarmed;
  }

  public void setNumberOfYearsFarmed(Integer numberOfYearsFarmed) {
    this.numberOfYearsFarmed = numberOfYearsFarmed;
  }

  public String getWas2024FinalYearOfFarming() {
    return was2024FinalYearOfFarming;
  }

  public void setWas2024FinalYearOfFarming(String was2024FinalYearOfFarming) {
    this.was2024FinalYearOfFarming = was2024FinalYearOfFarming;
  }

  public String getCompletedProductionCycle() {
    return completedProductionCycle;
  }

  public void setCompletedProductionCycle(String completedProductionCycle) {
    this.completedProductionCycle = completedProductionCycle;
  }

  public String getUnableToCompleteBecauseOfDisaster() {
    return unableToCompleteBecauseOfDisaster;
  }

  public void setUnableToCompleteBecauseOfDisaster(String unableToCompleteBecauseOfDisaster) {
    this.unableToCompleteBecauseOfDisaster = unableToCompleteBecauseOfDisaster;
  }

  public String getDoYouHaveMultipleOperations() {
    return doYouHaveMultipleOperations;
  }

  public void setDoYouHaveMultipleOperations(String doYouHaveMultipleOperations) {
    this.doYouHaveMultipleOperations = doYouHaveMultipleOperations;
  }

  public String getDecreasedParticipant() {
    return decreasedParticipant;
  }

  public void setDecreasedParticipant(String decreasedParticipant) {
    this.decreasedParticipant = decreasedParticipant;
  }

  public Date getDateOfDeath() {
    return dateOfDeath;
  }

  public void setDateOfDeath(Date dateOfDeath) {
    this.dateOfDeath = dateOfDeath;
  }

  public LabelValue getFarmType() {
    return farmType;
  }

  public void setFarmType(LabelValue farmType) {
    this.farmType = farmType;
  }

  public String getSinNumber() {
    return sinNumber;
  }

  public void setSinNumber(String sinNumber) {
    this.sinNumber = sinNumber;
  }

  public String getBusinessTaxNumber() {
    return businessTaxNumber;
  }

  public void setBusinessTaxNumber(String businessTaxNumber) {
    this.businessTaxNumber = businessTaxNumber;
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

  public Double getTotalAllowableIncome() {
    return totalAllowableIncome;
  }

  public void setTotalAllowableIncome(Double totalAllowableIncome) {
    this.totalAllowableIncome = totalAllowableIncome;
  }

  public Double getTotalNonAllowableIncome() {
    return totalNonAllowableIncome;
  }

  public void setTotalNonAllowableIncome(Double totalNonAllowableIncome) {
    this.totalNonAllowableIncome = totalNonAllowableIncome;
  }

  public Double getTotalAllowableIncomeSummary() {
    return totalAllowableIncomeSummary;
  }

  public void setTotalAllowableIncomeSummary(Double totalAllowableIncomeSummary) {
    this.totalAllowableIncomeSummary = totalAllowableIncomeSummary;
  }

  public Double getTotalNonAllowableIncomeSummary() {
    return totalNonAllowableIncomeSummary;
  }

  public void setTotalNonAllowableIncomeSummary(Double totalNonAllowableIncomeSummary) {
    this.totalNonAllowableIncomeSummary = totalNonAllowableIncomeSummary;
  }

  public Double getTotalIncome() {
    return totalIncome;
  }

  public void setTotalIncome(Double totalIncome) {
    this.totalIncome = totalIncome;
  }

  public Double getTotalAllowableExpenses() {
    return totalAllowableExpenses;
  }

  public void setTotalAllowableExpenses(Double totalAllowableExpenses) {
    this.totalAllowableExpenses = totalAllowableExpenses;
  }

  public Double getTotalNonAllowableExpenses() {
    return totalNonAllowableExpenses;
  }

  public void setTotalNonAllowableExpenses(Double totalNonAllowableExpenses) {
    this.totalNonAllowableExpenses = totalNonAllowableExpenses;
  }

  public Double getTotalAllowableExpensesSummary() {
    return totalAllowableExpensesSummary;
  }

  public void setTotalAllowableExpensesSummary(Double totalAllowableExpensesSummary) {
    this.totalAllowableExpensesSummary = totalAllowableExpensesSummary;
  }

  public Double getTotalNonAllowableExpensesSummary() {
    return totalNonAllowableExpensesSummary;
  }

  public void setTotalNonAllowableExpensesSummary(Double totalNonAllowableExpensesSummary) {
    this.totalNonAllowableExpensesSummary = totalNonAllowableExpensesSummary;
  }

  public Double getTotalExpenses() {
    return totalExpenses;
  }

  public void setTotalExpenses(Double totalExpenses) {
    this.totalExpenses = totalExpenses;
  }

  public Double getSummaryOfIncome() {
    return SummaryOfIncome;
  }

  public void setSummaryOfIncome(Double summaryOfIncome) {
    SummaryOfIncome = summaryOfIncome;
  }

  public Double getSummaryOfExpenses() {
    return SummaryOfExpenses;
  }

  public void setSummaryOfExpenses(Double summaryOfExpenses) {
    SummaryOfExpenses = summaryOfExpenses;
  }

  public Double getNetIncomeBeforeAdjustments() {
    return netIncomeBeforeAdjustments;
  }

  public void setNetIncomeBeforeAdjustments(Double netIncomeBeforeAdjustments) {
    this.netIncomeBeforeAdjustments = netIncomeBeforeAdjustments;
  }

  public Double getOptionalAdjustments() {
    return optionalAdjustments;
  }

  public void setOptionalAdjustments(Double optionalAdjustments) {
    this.optionalAdjustments = optionalAdjustments;
  }

  public Double getMandatoryAdjustments() {
    return mandatoryAdjustments;
  }

  public void setMandatoryAdjustments(Double mandatoryAdjustments) {
    this.mandatoryAdjustments = mandatoryAdjustments;
  }

  public Double getTotalAdjustments() {
    return totalAdjustments;
  }

  public void setTotalAdjustments(Double totalAdjustments) {
    this.totalAdjustments = totalAdjustments;
  }

  public Double getNetIncomeAfterAdjustments() {
    return netIncomeAfterAdjustments;
  }

  public void setNetIncomeAfterAdjustments(Double netIncomeAfterAdjustments) {
    this.netIncomeAfterAdjustments = netIncomeAfterAdjustments;
  }

  public SupplementalAccruals getCheckAllThatApply() {
    return checkAllThatApply;
  }

  public void setCheckAllThatApply(SupplementalAccruals checkAllThatApply) {
    this.checkAllThatApply = checkAllThatApply;
  }

  public LabelValue getTypeOfAnimalCustomFed() {
    return typeOfAnimalCustomFed;
  }

  public void setTypeOfAnimalCustomFed(LabelValue typeOfAnimalCustomFed) {
    this.typeOfAnimalCustomFed = typeOfAnimalCustomFed;
  }

  public List<IncomeExpenseGrid> getAllowableIncomeGrid() {
    return allowableIncomeGrid;
  }

  public void setAllowableIncomeGrid(List<IncomeExpenseGrid> allowableIncomeGrid) {
    this.allowableIncomeGrid = allowableIncomeGrid;
  }

  public List<IncomeExpenseGrid> getNonAllowablesGrid() {
    return nonAllowablesGrid;
  }

  public void setNonAllowablesGrid(List<IncomeExpenseGrid> nonAllowablesGrid) {
    this.nonAllowablesGrid = nonAllowablesGrid;
  }

  public List<IncomeExpenseGrid> getAllowableExpensesGrid() {
    return allowableExpensesGrid;
  }

  public void setAllowableExpensesGrid(List<IncomeExpenseGrid> allowableExpensesGrid) {
    this.allowableExpensesGrid = allowableExpensesGrid;
  }

  public List<IncomeExpenseGrid> getNonAllowableExpensesGrid() {
    return nonAllowableExpensesGrid;
  }

  public void setNonAllowableExpensesGrid(List<IncomeExpenseGrid> nonAllowableExpensesGrid) {
    this.nonAllowableExpensesGrid = nonAllowableExpensesGrid;
  }

  public String getAuthorizeThirdParty() {
    return authorizeThirdParty;
  }

  public void setAuthorizeThirdParty(String authorizeThirdParty) {
    this.authorizeThirdParty = authorizeThirdParty;
  }

  public String getThirdPartyFirstName() {
    return thirdPartyFirstName;
  }

  public void setThirdPartyFirstName(String thirdPartyFirstName) {
    this.thirdPartyFirstName = thirdPartyFirstName;
  }

  public String getThirdPartyBusinessName() {
    return thirdPartyBusinessName;
  }

  public void setThirdPartyBusinessName(String thirdPartyBusinessName) {
    this.thirdPartyBusinessName = thirdPartyBusinessName;
  }

  public String getThirdPartyLastName() {
    return thirdPartyLastName;
  }

  public void setThirdPartyLastName(String thirdPartyLastName) {
    this.thirdPartyLastName = thirdPartyLastName;
  }

  public String getThirdPartyAddress() {
    return thirdPartyAddress;
  }

  public void setThirdPartyAddress(String thirdPartyAddress) {
    this.thirdPartyAddress = thirdPartyAddress;
  }

  public String getThirdPartyTownCity() {
    return thirdPartyTownCity;
  }

  public void setThirdPartyTownCity(String thirdPartyTownCity) {
    this.thirdPartyTownCity = thirdPartyTownCity;
  }

  public String getThirdPartyProvince() {
    return thirdPartyProvince;
  }

  public void setThirdPartyProvince(String thirdPartyProvince) {
    this.thirdPartyProvince = thirdPartyProvince;
  }

  public String getThirdPartyPostalCode() {
    return thirdPartyPostalCode;
  }

  public void setThirdPartyPostalCode(String thirdPartyPostalCode) {
    this.thirdPartyPostalCode = thirdPartyPostalCode;
  }

  public String getThirdPartyTelephone() {
    return thirdPartyTelephone;
  }

  public void setThirdPartyTelephone(String thirdPartyTelephone) {
    this.thirdPartyTelephone = thirdPartyTelephone;
  }

  public String getThirdPartyFax() {
    return thirdPartyFax;
  }

  public void setThirdPartyFax(String thirdPartyFax) {
    this.thirdPartyFax = thirdPartyFax;
  }

  public String getThirdPartyEmail() {
    return thirdPartyEmail;
  }

  public void setThirdPartyEmail(String thirdPartyEmail) {
    this.thirdPartyEmail = thirdPartyEmail;
  }

  public String getCopyOfCOB() {
    return copyOfCOB;
  }

  public void setCopyOfCOB(String copyOfCOB) {
    this.copyOfCOB = copyOfCOB;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public SimpleDateFormat getSimpleDateFormat() {
    return simpleDateFormat;
  }

  public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
    this.simpleDateFormat = simpleDateFormat;
  }

}
