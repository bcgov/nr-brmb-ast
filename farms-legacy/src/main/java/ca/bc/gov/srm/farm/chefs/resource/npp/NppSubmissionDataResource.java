package ca.bc.gov.srm.farm.chefs.resource.npp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class NppSubmissionDataResource extends ChefsSubmissionDataResource {

  private Boolean existingAccount;
  private Boolean lateParticipant;
  private String firstName;
  private String lastName;
  private String townCity;
  private String address;
  private String province;
  private String email;
  private LabelValue farmType;
  private String corporationName;
  private String firstNameCorporateContact;
  private String lastNameCorporateContact;
  private String municipalityCode;
  private String sinNumber;
  private String telephone;
  private String postalCode;
  private String businessTaxNumberBn;
  private String trustBusinessNumber;
  private String trustNumber;
  private String bandNumber;
  private String accountingCode;
  private Date fiscalYearStart;
  private Date fiscalYearEnd;
  private Integer agriStabilityAgriInvestPin;
  private Boolean noPin;

  private String authorizeThirdParty;
  private String thirdPartyPostalCode;
  private String thirdPartyBusinessName;
  private String thirdPartyLastName;
  private String thirdPartyAddress;
  private String thirdPartyProvince;
  private String thirdPartySignDate;
  private String thirdPartyTownCity;
  private String thirdPartyFirstName;
  private String thirdPartyTelephone;
  private String thirdPartyEmail;
  private String thirdPartyFax;

  private List<Integer> productionInsuranceGrowerNumber = new ArrayList<>();
  private List<NppNurseryGrid> nurseryGrid = new ArrayList<>();
  private Double totalNurserySquareMeters;
  private List<VeggieGrid> veggieGrid = new ArrayList<>();
  private List<NppCropGrid> forageSeedGrid = new ArrayList<>();
  private Double totalForageSeedAcres;
  private List<NppCropGrid> forageBasketGrid = new ArrayList<>();
  private Double totalForageBasketAcres;
  private List<NppCropGrid> cropBasketTypeGrid = new ArrayList<>();
  private List<PartnershipInformation> partnershipInformation = new ArrayList<>();

  private String onBehalfOfParticipant;
  private String signature;
  private String signFirstName;
  private String signLastName;
  private Date signDate;
  private String howDoYouKnowTheParticipant;

  private String signature2;
  private Date signatureDate2;

  private Boolean agreeToTheTermsAndConditions;
  private String doYouHaveMultipleOperations;
  private String unableToCompleteBecauseOfDisaster;
  private String didYouCompleteAProductionCycle;
  private String whatIsYourMainFarmingActivity;
  private String specifyOther;
  private String didYouStartFarmingWithinTheLastSixMonths;
  private String firstYearReporting;

  private Double ChristmasTreesEstablishmentAcres;
  private Double ChristmasTreesEstablishmentAcres1;
  private Double ChristmasTreesEstablishmentAcres2;
  private Double ChristmasTreesEstablishmentAcres3;
  private Double ChristmasTreesEstablishmentAcres4;

  private List<String> commoditiesFarmed;
  private TreeFruitsFarmed treefruitsFarmed;

  private Double bredCow_104;
  private Double honeybees_126;
  private Double pearAcres_5054;
  private Double plumAcres_5056;
  private Double peachAcres_5052;
  private Double pruneAcres_5058;
  private Double apricotAcres_5032;
  private Double hogsFarrowing_145;
  private Double leafCutterBees_129;
  private Double broilersTurkeys_144;
  private Double firstYearAcres_4997;
  private Double nectarineAcres_5048;
  private Double raspberryAcres_5018;
  private Double thirdYearAcres_4782;
  private Double broilersChickens_143;
  private Double fourthYearAcres_4783;
  private Double secondYearAcres_4781;
  private Double plantingYearAcres_4780;
  private Double plantingYearAcres_4800;
  private Double plantingYearAcres_4980;
  private Double plantingYearAcres_4995;
  private Double nonBearingYearAcres_4801;
  private Double nonBearingYearAcres_4981;
  private Double nonBearingYearAcres_4996;
  private Double layersEggsForHatching_108;
  private Double numberOfCustomFedHogs_142;
  private Double dairyOfButterfatPerDay_113;
  private Double feederHogsFedOver50Lbs_124;
  private Double feederHogsFedUpTo50Lbs_125;
  private Double numberOfCustomFedCattle_141;
  private Double layersEggsForConsumption_109;
  private Double feederCattleFedOver900Lbs_106;
  private Double feederCattleFedUpTo900Lbs_105;
  private Double gala5YearProductionAcres_4826;
  private Double gala24YearProductionAcres_4824;
  private Double other5YearProductionAcres_4866;
  private Double secondYearProductionAcres_4998;
  private Double blueberryPlantingYearAcres_5059;
  private Double gala1stYearProductionAcres_4822;
  private Double other24YearProductionAcres_4865;
  private Double other1stYearProductionAcres_4862;
  private Double blueberryNonBearingYearAcres_5060;
  private Double highValue5YearProductionAcres_4816;
  private Double blueberry36YearProductionAcres_5062;
  private Double cranberry1stYearProductionAcres_4991;
  private Double cranberry2ndYearProductionAcres_4992;
  private Double cranberry3rdYearProductionAcres_4993;
  private Double cranberry4thYearProductionAcres_4994;
  private Double highValue1stYearProductionAcres_4812;
  private Double blueberry10thYearProductionAcres_5064;
  private Double cranberryEstablishmentStageAcres_4990;
  private Double blueberry7th9thYearProductionAcres_5063;
  private Double highValue2nd4thYearProductionAcres_4815;
  private Double blueberry1stAnd2ndYearProductionAcres_5061;
  private Double lowDensityCherries7YearProductionAcres_4953;
  private Double highDensityCherries3YearProductionAcres_4956;
  private Double highDensityCherries1stYearProductionAcres_4954;
  private Double highDensityCherries2ndYearProductionAcres_4955;
  private Double lowDensityCherries1stAnd2ndYearProductionAcres_4950;
  private Double lowDensityCherries3rdAnd4thYearProductionAcres_4951;
  private Double lowDensityCherries5thAnd6thYearProductionAcres_4952;

  /* New fields for NPP Draft */
  private List<String> cropsFarmed;
  private List<NppCommodityGrid> berryGrid;
  private Double totalBerryAcres;
  private List<NppCommodityGrid> treeFruitGrid;
  private Double totalTreeFruitAcres;
  private List<NppCommodityGrid> vegetableGrid;
  private Double totalVegetableAcres;
  private List<NppCommodityGrid> grainGrid;
  private Double totalGrainAcres;

  private List<String> livestockFarmed;
  private List<NppCommodityGrid> neCattleGrid;

  private Double productiveCapacityLC123;

  private List<NppCommodityGrid> opdGrid;

  @JsonIgnore
  private String pattern = "yyyy-MM-dd";
  @JsonIgnore
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

  public Boolean getExistingAccount() {
    return existingAccount;
  }

  public void setExistingAccount(Boolean existingAccount) {
    this.existingAccount = existingAccount;
  }

  public Boolean getLateParticipant() {
    return lateParticipant;
  }

  public void setLateParticipant(Boolean lateParticipant) {
    this.lateParticipant = lateParticipant;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getTownCity() {
    return townCity;
  }

  public void setTownCity(String townCity) {
    this.townCity = townCity;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

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

  public String getMunicipalityCode() {
    return municipalityCode;
  }

  public void setMunicipalityCode(String municipalityCode) {
    this.municipalityCode = municipalityCode;
  }

  public String getSinNumber() {
    return sinNumber;
  }

  public void setSinNumber(String sinNumber) {
    this.sinNumber = sinNumber;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getBusinessTaxNumberBn() {
    return businessTaxNumberBn != null ? businessTaxNumberBn.replaceAll("\\s", "") : null;
  }

  public void setBusinessTaxNumberBn(String businessTaxNumberBn) {
    this.businessTaxNumberBn = businessTaxNumberBn;
  }

  public String getTrustNumber() {
    return trustNumber != null ? trustNumber.replaceAll("\\s", "") : null;
  }

  public void setTrustNumber(String trustNumber) {
    this.trustNumber = trustNumber;
  }

  public Date getFiscalYearStart() {
    return fiscalYearStart;
  }

  public void setFiscalYearStart(Date fiscalYearStart) {
    this.fiscalYearStart = fiscalYearStart;
  }

  public Date getFiscalYearEnd() {
    return fiscalYearEnd;
  }

  public void setFiscalYearEnd(Date fiscalYearEnd) {
    this.fiscalYearEnd = fiscalYearEnd;
  }

  public Integer getAgriStabilityAgriInvestPin() {
    return agriStabilityAgriInvestPin;
  }

  public void setAgriStabilityAgriInvestPin(Integer agriStabilityAgriInvestPin) {
    this.agriStabilityAgriInvestPin = agriStabilityAgriInvestPin;
  }

  public Boolean getNoPin() {
    return noPin;
  }

  public void setNoPin(Boolean noPin) {
    this.noPin = noPin;
  }

  public String getAuthorizeThirdParty() {
    return authorizeThirdParty;
  }

  public void setAuthorizeThirdParty(String authorizeThirdParty) {
    this.authorizeThirdParty = authorizeThirdParty;
  }

  public String getThirdPartyPostalCode() {
    return thirdPartyPostalCode;
  }

  public void setThirdPartyPostalCode(String thirdPartyPostalCode) {
    this.thirdPartyPostalCode = thirdPartyPostalCode;
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

  public String getThirdPartyProvince() {
    return thirdPartyProvince;
  }

  public void setThirdPartyProvince(String thirdPartyProvince) {
    this.thirdPartyProvince = thirdPartyProvince;
  }

  public String getThirdPartySignDate() {
    return thirdPartySignDate;
  }

  public void setThirdPartySignDate(String thirdPartySignDate) {
    this.thirdPartySignDate = thirdPartySignDate;
  }

  public String getThirdPartyTownCity() {
    return thirdPartyTownCity;
  }

  public void setThirdPartyTownCity(String thirdPartyTownCity) {
    this.thirdPartyTownCity = thirdPartyTownCity;
  }

  public String getThirdPartyFirstName() {
    return thirdPartyFirstName;
  }

  public void setThirdPartyFirstName(String thirdPartyFirstName) {
    this.thirdPartyFirstName = thirdPartyFirstName;
  }

  public String getThirdPartyTelephone() {
    return thirdPartyTelephone;
  }

  public void setThirdPartyTelephone(String thirdPartyTelephone) {
    this.thirdPartyTelephone = thirdPartyTelephone;
  }

  public String getThirdPartyEmail() {
    return thirdPartyEmail;
  }

  public void setThirdPartyEmail(String thirdPartyEmail) {
    this.thirdPartyEmail = thirdPartyEmail;
  }

  public String getThirdPartyFax() {
    return thirdPartyFax;
  }

  public void setThirdPartyFax(String thirdPartyFax) {
    this.thirdPartyFax = thirdPartyFax;
  }

  public List<Integer> getProductionInsuranceGrowerNumber() {
    return productionInsuranceGrowerNumber;
  }

  public void setProductionInsuranceGrowerNumber(List<Integer> productionInsuranceGrowerNumber) {
    this.productionInsuranceGrowerNumber = productionInsuranceGrowerNumber;
  }

  public List<NppNurseryGrid> getNurseryGrid() {
    return nurseryGrid;
  }

  public void setNurseryGrid(List<NppNurseryGrid> nurseryGrid) {
    this.nurseryGrid = nurseryGrid;
  }

  public Double getTotalNurserySquareMeters() {
    return totalNurserySquareMeters;
  }

  public void setTotalNurserySquareMeters(Double totalNurserySquareMeters) {
    this.totalNurserySquareMeters = totalNurserySquareMeters;
  }

  public List<VeggieGrid> getVeggieGrid() {
    return veggieGrid;
  }

  public void setVeggieGrid(List<VeggieGrid> veggieGrid) {
    this.veggieGrid = veggieGrid;
  }

  public List<NppCropGrid> getForageSeedGrid() {
    return forageSeedGrid;
  }

  public void setForageSeedGrid(List<NppCropGrid> forageSeedGrid) {
    this.forageSeedGrid = forageSeedGrid;
  }

  public Double getTotalForageSeedAcres() {
    return totalForageSeedAcres;
  }

  public void setTotalForageSeedAcres(Double totalForageSeedAcres) {
    this.totalForageSeedAcres = totalForageSeedAcres;
  }

  public List<NppCropGrid> getForageBasketGrid() {
    return forageBasketGrid;
  }

  public void setForageBasketGrid(List<NppCropGrid> forageBasketGrid) {
    this.forageBasketGrid = forageBasketGrid;
  }

  public Double getTotalForageBasketAcres() {
    return totalForageBasketAcres;
  }

  public void setTotalForageBasketAcres(Double totalForageBasketAcres) {
    this.totalForageBasketAcres = totalForageBasketAcres;
  }

  public List<NppCropGrid> getCropBasketTypeGrid() {
    return cropBasketTypeGrid;
  }

  public void setCropBasketTypeGrid(List<NppCropGrid> cropBasketTypeGrid) {
    this.cropBasketTypeGrid = cropBasketTypeGrid;
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

  public String getSignFirstName() {
    return signFirstName;
  }

  public void setSignFirstName(String signFirstName) {
    this.signFirstName = signFirstName;
  }

  public String getSignLastName() {
    return signLastName;
  }

  public void setSignLastName(String signLastName) {
    this.signLastName = signLastName;
  }

  public Date getSignDate() {
    return signDate;
  }

  public void setSignDate(Date signDate) {
    this.signDate = signDate;
  }

  public String getHowDoYouKnowTheParticipant() {
    return howDoYouKnowTheParticipant;
  }

  public void setHowDoYouKnowTheParticipant(String howDoYouKnowTheParticipant) {
    this.howDoYouKnowTheParticipant = howDoYouKnowTheParticipant;
  }

  public String getSignature2() {
    return signature2;
  }

  public void setSignature2(String signature2) {
    this.signature2 = signature2;
  }

  public Date getSignatureDate2() {
    return signatureDate2;
  }

  public void setSignatureDate2(Date signatureDate2) {
    this.signatureDate2 = signatureDate2;
  }

  public Boolean getAgreeToTheTermsAndConditions() {
    return agreeToTheTermsAndConditions;
  }

  public void setAgreeToTheTermsAndConditions(Boolean agreeToTheTermsAndConditions) {
    this.agreeToTheTermsAndConditions = agreeToTheTermsAndConditions;
  }

  public String getDidYouCompleteAProductionCycle() {
    return didYouCompleteAProductionCycle;
  }

  public void setDidYouCompleteAProductionCycle(String didYouCompleteAProductionCycle) {
    this.didYouCompleteAProductionCycle = didYouCompleteAProductionCycle;
  }

  public String getWhatIsYourMainFarmingActivity() {
    return whatIsYourMainFarmingActivity;
  }

  public void setWhatIsYourMainFarmingActivity(String whatIsYourMainFarmingActivity) {
    this.whatIsYourMainFarmingActivity = whatIsYourMainFarmingActivity;
  }

  public String getDidYouStartFarmingWithinTheLastSixMonths() {
    return didYouStartFarmingWithinTheLastSixMonths;
  }

  public void setDidYouStartFarmingWithinTheLastSixMonths(String didYouStartFarmingWithinTheLastSixMonths) {
    this.didYouStartFarmingWithinTheLastSixMonths = didYouStartFarmingWithinTheLastSixMonths;
  }

  public String getFirstYearReporting() {
    return firstYearReporting;
  }

  public void setFirstYearReporting(String firstYearReporting) {
    this.firstYearReporting = firstYearReporting;
  }

  public List<String> getCommoditiesFarmed() {
    return commoditiesFarmed;
  }

  public void setCommoditiesFarmed(List<String> commoditiesFarmed) {
    this.commoditiesFarmed = commoditiesFarmed;
  }

  public TreeFruitsFarmed getTreefruitsFarmed() {
    return treefruitsFarmed;
  }

  public void setTreefruitsFarmed(TreeFruitsFarmed treefruitsFarmed) {
    this.treefruitsFarmed = treefruitsFarmed;
  }

  public Double getHoneybees_126() {
    return honeybees_126;
  }

  public void setHoneybees_126(Double honeybees_126) {
    this.honeybees_126 = honeybees_126;
  }

  public Double getPearAcres_5054() {
    return pearAcres_5054;
  }

  public void setPearAcres_5054(Double pearAcres_5054) {
    this.pearAcres_5054 = pearAcres_5054;
  }

  public Double getPlumAcres_5056() {
    return plumAcres_5056;
  }

  public void setPlumAcres_5056(Double plumAcres_5056) {
    this.plumAcres_5056 = plumAcres_5056;
  }

  public Double getPeachAcres_5052() {
    return peachAcres_5052;
  }

  public void setPeachAcres_5052(Double peachAcres_5052) {
    this.peachAcres_5052 = peachAcres_5052;
  }

  public Double getPruneAcres_5058() {
    return pruneAcres_5058;
  }

  public void setPruneAcres_5058(Double pruneAcres_5058) {
    this.pruneAcres_5058 = pruneAcres_5058;
  }

  public Double getApricotAcres_5032() {
    return apricotAcres_5032;
  }

  public void setApricotAcres_5032(Double apricotAcres_5032) {
    this.apricotAcres_5032 = apricotAcres_5032;
  }

  public Double getHogsFarrowing_145() {
    return hogsFarrowing_145;
  }

  public void setHogsFarrowing_145(Double hogsFarrowing_145) {
    this.hogsFarrowing_145 = hogsFarrowing_145;
  }

  public Double getLeafCutterBees_129() {
    return leafCutterBees_129;
  }

  public void setLeafCutterBees_129(Double leafCutterBees_129) {
    this.leafCutterBees_129 = leafCutterBees_129;
  }

  public Double getBroilersTurkeys_144() {
    return broilersTurkeys_144;
  }

  public void setBroilersTurkeys_144(Double broilersTurkeys_144) {
    this.broilersTurkeys_144 = broilersTurkeys_144;
  }

  public Double getFirstYearAcres_4997() {
    return firstYearAcres_4997;
  }

  public void setFirstYearAcres_4997(Double firstYearAcres_4997) {
    this.firstYearAcres_4997 = firstYearAcres_4997;
  }

  public Double getNectarineAcres_5048() {
    return nectarineAcres_5048;
  }

  public void setNectarineAcres_5048(Double nectarineAcres_5048) {
    this.nectarineAcres_5048 = nectarineAcres_5048;
  }

  public Double getRaspberryAcres_5018() {
    return raspberryAcres_5018;
  }

  public void setRaspberryAcres_5018(Double raspberryAcres_5018) {
    this.raspberryAcres_5018 = raspberryAcres_5018;
  }

  public Double getThirdYearAcres_4782() {
    return thirdYearAcres_4782;
  }

  public void setThirdYearAcres_4782(Double thirdYearAcres_4782) {
    this.thirdYearAcres_4782 = thirdYearAcres_4782;
  }

  public Double getBroilersChickens_143() {
    return broilersChickens_143;
  }

  public void setBroilersChickens_143(Double broilersChickens_143) {
    this.broilersChickens_143 = broilersChickens_143;
  }

  public Double getFourthYearAcres_4783() {
    return fourthYearAcres_4783;
  }

  public void setFourthYearAcres_4783(Double fourthYearAcres_4783) {
    this.fourthYearAcres_4783 = fourthYearAcres_4783;
  }

  public Double getSecondYearAcres_4781() {
    return secondYearAcres_4781;
  }

  public void setSecondYearAcres_4781(Double secondYearAcres_4781) {
    this.secondYearAcres_4781 = secondYearAcres_4781;
  }

  public Double getPlantingYearAcres_4780() {
    return plantingYearAcres_4780;
  }

  public void setPlantingYearAcres_4780(Double plantingYearAcres_4780) {
    this.plantingYearAcres_4780 = plantingYearAcres_4780;
  }

  public Double getPlantingYearAcres_4800() {
    return plantingYearAcres_4800;
  }

  public void setPlantingYearAcres_4800(Double plantingYearAcres_4800) {
    this.plantingYearAcres_4800 = plantingYearAcres_4800;
  }

  public Double getPlantingYearAcres_4995() {
    return plantingYearAcres_4995;
  }

  public void setPlantingYearAcres_4995(Double plantingYearAcres_4995) {
    this.plantingYearAcres_4995 = plantingYearAcres_4995;
  }

  public Double getNonBearingYearAcres_4801() {
    return nonBearingYearAcres_4801;
  }

  public void setNonBearingYearAcres_4801(Double nonBearingYearAcres_4801) {
    this.nonBearingYearAcres_4801 = nonBearingYearAcres_4801;
  }

  public Double getNonBearingYearAcres_4981() {
    return nonBearingYearAcres_4981;
  }

  public void setNonBearingYearAcres_4981(Double nonBearingYearAcres_4981) {
    this.nonBearingYearAcres_4981 = nonBearingYearAcres_4981;
  }

  public Double getNonBearingYearAcres_4996() {
    return nonBearingYearAcres_4996;
  }

  public void setNonBearingYearAcres_4996(Double nonBearingYearAcres_4996) {
    this.nonBearingYearAcres_4996 = nonBearingYearAcres_4996;
  }

  public Double getLayersEggsForHatching_108() {
    return layersEggsForHatching_108;
  }

  public void setLayersEggsForHatching_108(Double layersEggsForHatching_108) {
    this.layersEggsForHatching_108 = layersEggsForHatching_108;
  }

  public Double getNumberOfCustomFedHogs_142() {
    return numberOfCustomFedHogs_142;
  }

  public void setNumberOfCustomFedHogs_142(Double numberOfCustomFedHogs_142) {
    this.numberOfCustomFedHogs_142 = numberOfCustomFedHogs_142;
  }

  public Double getDairyOfButterfatPerDay_113() {
    return dairyOfButterfatPerDay_113;
  }

  public void setDairyOfButterfatPerDay_113(Double dairyOfButterfatPerDay_113) {
    this.dairyOfButterfatPerDay_113 = dairyOfButterfatPerDay_113;
  }

  public Double getFeederHogsFedOver50Lbs_124() {
    return feederHogsFedOver50Lbs_124;
  }

  public void setFeederHogsFedOver50Lbs_124(Double feederHogsFedOver50Lbs_124) {
    this.feederHogsFedOver50Lbs_124 = feederHogsFedOver50Lbs_124;
  }

  public Double getFeederHogsFedUpTo50Lbs_125() {
    return feederHogsFedUpTo50Lbs_125;
  }

  public void setFeederHogsFedUpTo50Lbs_125(Double feederHogsFedUpTo50Lbs_125) {
    this.feederHogsFedUpTo50Lbs_125 = feederHogsFedUpTo50Lbs_125;
  }

  public Double getNumberOfCustomFedCattle_141() {
    return numberOfCustomFedCattle_141;
  }

  public void setNumberOfCustomFedCattle_141(Double numberOfCustomFedCattle_141) {
    this.numberOfCustomFedCattle_141 = numberOfCustomFedCattle_141;
  }

  public Double getLayersEggsForConsumption_109() {
    return layersEggsForConsumption_109;
  }

  public void setLayersEggsForConsumption_109(Double layersEggsForConsumption_109) {
    this.layersEggsForConsumption_109 = layersEggsForConsumption_109;
  }

  public Double getFeederCattleFedOver900Lbs_106() {
    return feederCattleFedOver900Lbs_106;
  }

  public void setFeederCattleFedOver900Lbs_106(Double feederCattleFedOver900Lbs_106) {
    this.feederCattleFedOver900Lbs_106 = feederCattleFedOver900Lbs_106;
  }

  public Double getFeederCattleFedUpTo900Lbs_105() {
    return feederCattleFedUpTo900Lbs_105;
  }

  public void setFeederCattleFedUpTo900Lbs_105(Double feederCattleFedUpTo900Lbs_105) {
    this.feederCattleFedUpTo900Lbs_105 = feederCattleFedUpTo900Lbs_105;
  }

  public Double getGala5YearProductionAcres_4826() {
    return gala5YearProductionAcres_4826;
  }

  public void setGala5YearProductionAcres_4826(Double gala5YearProductionAcres_4826) {
    this.gala5YearProductionAcres_4826 = gala5YearProductionAcres_4826;
  }

  public Double getGala24YearProductionAcres_4824() {
    return gala24YearProductionAcres_4824;
  }

  public void setGala24YearProductionAcres_4824(Double gala24YearProductionAcres_4824) {
    this.gala24YearProductionAcres_4824 = gala24YearProductionAcres_4824;
  }

  public Double getOther5YearProductionAcres_4866() {
    return other5YearProductionAcres_4866;
  }

  public void setOther5YearProductionAcres_4866(Double other5YearProductionAcres_4866) {
    this.other5YearProductionAcres_4866 = other5YearProductionAcres_4866;
  }

  public Double getSecondYearProductionAcres_4998() {
    return secondYearProductionAcres_4998;
  }

  public void setSecondYearProductionAcres_4998(Double secondYearProductionAcres_4998) {
    this.secondYearProductionAcres_4998 = secondYearProductionAcres_4998;
  }

  public Double getBlueberryPlantingYearAcres_5059() {
    return blueberryPlantingYearAcres_5059;
  }

  public void setBlueberryPlantingYearAcres_5059(Double blueberryPlantingYearAcres_5059) {
    this.blueberryPlantingYearAcres_5059 = blueberryPlantingYearAcres_5059;
  }

  public Double getGala1stYearProductionAcres_4822() {
    return gala1stYearProductionAcres_4822;
  }

  public void setGala1stYearProductionAcres_4822(Double gala1stYearProductionAcres_4822) {
    this.gala1stYearProductionAcres_4822 = gala1stYearProductionAcres_4822;
  }

  public Double getOther24YearProductionAcres_4865() {
    return other24YearProductionAcres_4865;
  }

  public void setOther24YearProductionAcres_4865(Double other24YearProductionAcres_4865) {
    this.other24YearProductionAcres_4865 = other24YearProductionAcres_4865;
  }

  public Double getOther1stYearProductionAcres_4862() {
    return other1stYearProductionAcres_4862;
  }

  public void setOther1stYearProductionAcres_4862(Double other1stYearProductionAcres_4862) {
    this.other1stYearProductionAcres_4862 = other1stYearProductionAcres_4862;
  }

  public Double getBlueberryNonBearingYearAcres_5060() {
    return blueberryNonBearingYearAcres_5060;
  }

  public void setBlueberryNonBearingYearAcres_5060(Double blueberryNonBearingYearAcres_5060) {
    this.blueberryNonBearingYearAcres_5060 = blueberryNonBearingYearAcres_5060;
  }

  public Double getHighValue5YearProductionAcres_4816() {
    return highValue5YearProductionAcres_4816;
  }

  public void setHighValue5YearProductionAcres_4816(Double highValue5YearProductionAcres_4816) {
    this.highValue5YearProductionAcres_4816 = highValue5YearProductionAcres_4816;
  }

  public Double getBlueberry36YearProductionAcres_5062() {
    return blueberry36YearProductionAcres_5062;
  }

  public void setBlueberry36YearProductionAcres_5062(Double blueberry36YearProductionAcres_5062) {
    this.blueberry36YearProductionAcres_5062 = blueberry36YearProductionAcres_5062;
  }

  public Double getCranberry1stYearProductionAcres_4991() {
    return cranberry1stYearProductionAcres_4991;
  }

  public void setCranberry1stYearProductionAcres_4991(Double cranberry1stYearProductionAcres_4991) {
    this.cranberry1stYearProductionAcres_4991 = cranberry1stYearProductionAcres_4991;
  }

  public Double getCranberry2ndYearProductionAcres_4992() {
    return cranberry2ndYearProductionAcres_4992;
  }

  public void setCranberry2ndYearProductionAcres_4992(Double cranberry2ndYearProductionAcres_4992) {
    this.cranberry2ndYearProductionAcres_4992 = cranberry2ndYearProductionAcres_4992;
  }

  public Double getCranberry3rdYearProductionAcres_4993() {
    return cranberry3rdYearProductionAcres_4993;
  }

  public void setCranberry3rdYearProductionAcres_4993(Double cranberry3rdYearProductionAcres_4993) {
    this.cranberry3rdYearProductionAcres_4993 = cranberry3rdYearProductionAcres_4993;
  }

  public Double getCranberry4thYearProductionAcres_4994() {
    return cranberry4thYearProductionAcres_4994;
  }

  public void setCranberry4thYearProductionAcres_4994(Double cranberry4thYearProductionAcres_4994) {
    this.cranberry4thYearProductionAcres_4994 = cranberry4thYearProductionAcres_4994;
  }

  public Double getHighValue1stYearProductionAcres_4812() {
    return highValue1stYearProductionAcres_4812;
  }

  public void setHighValue1stYearProductionAcres_4812(Double highValue1stYearProductionAcres_4812) {
    this.highValue1stYearProductionAcres_4812 = highValue1stYearProductionAcres_4812;
  }

  public Double getBlueberry10thYearProductionAcres_5064() {
    return blueberry10thYearProductionAcres_5064;
  }

  public void setBlueberry10thYearProductionAcres_5064(Double blueberry10thYearProductionAcres_5064) {
    this.blueberry10thYearProductionAcres_5064 = blueberry10thYearProductionAcres_5064;
  }

  public Double getCranberryEstablishmentStageAcres_4990() {
    return cranberryEstablishmentStageAcres_4990;
  }

  public void setCranberryEstablishmentStageAcres_4990(Double cranberryEstablishmentStageAcres_4990) {
    this.cranberryEstablishmentStageAcres_4990 = cranberryEstablishmentStageAcres_4990;
  }

  public Double getBlueberry7th9thYearProductionAcres_5063() {
    return blueberry7th9thYearProductionAcres_5063;
  }

  public void setBlueberry7th9thYearProductionAcres_5063(Double blueberry7th9thYearProductionAcres_5063) {
    this.blueberry7th9thYearProductionAcres_5063 = blueberry7th9thYearProductionAcres_5063;
  }

  public Double getHighValue2nd4thYearProductionAcres_4815() {
    return highValue2nd4thYearProductionAcres_4815;
  }

  public void setHighValue2nd4thYearProductionAcres_4815(Double highValue2nd4thYearProductionAcres_4815) {
    this.highValue2nd4thYearProductionAcres_4815 = highValue2nd4thYearProductionAcres_4815;
  }

  public Double getBlueberry1stAnd2ndYearProductionAcres_5061() {
    return blueberry1stAnd2ndYearProductionAcres_5061;
  }

  public void setBlueberry1stAnd2ndYearProductionAcres_5061(Double blueberry1stAnd2ndYearProductionAcres_5061) {
    this.blueberry1stAnd2ndYearProductionAcres_5061 = blueberry1stAnd2ndYearProductionAcres_5061;
  }

  public Double getLowDensityCherries7YearProductionAcres_4953() {
    return lowDensityCherries7YearProductionAcres_4953;
  }

  public void setLowDensityCherries7YearProductionAcres_4953(Double lowDensityCherries7YearProductionAcres_4953) {
    this.lowDensityCherries7YearProductionAcres_4953 = lowDensityCherries7YearProductionAcres_4953;
  }

  public Double getHighDensityCherries3YearProductionAcres_4956() {
    return highDensityCherries3YearProductionAcres_4956;
  }

  public void setHighDensityCherries3YearProductionAcres_4956(Double highDensityCherries3YearProductionAcres_4956) {
    this.highDensityCherries3YearProductionAcres_4956 = highDensityCherries3YearProductionAcres_4956;
  }

  public Double getHighDensityCherries1stYearProductionAcres_4954() {
    return highDensityCherries1stYearProductionAcres_4954;
  }

  public void setHighDensityCherries1stYearProductionAcres_4954(
      Double highDensityCherries1stYearProductionAcres_4954) {
    this.highDensityCherries1stYearProductionAcres_4954 = highDensityCherries1stYearProductionAcres_4954;
  }

  public Double getHighDensityCherries2ndYearProductionAcres_4955() {
    return highDensityCherries2ndYearProductionAcres_4955;
  }

  public void setHighDensityCherries2ndYearProductionAcres_4955(
      Double highDensityCherries2ndYearProductionAcres_4955) {
    this.highDensityCherries2ndYearProductionAcres_4955 = highDensityCherries2ndYearProductionAcres_4955;
  }

  public Double getLowDensityCherries1stAnd2ndYearProductionAcres_4950() {
    return lowDensityCherries1stAnd2ndYearProductionAcres_4950;
  }

  public void setLowDensityCherries1stAnd2ndYearProductionAcres_4950(
      Double lowDensityCherries1stAnd2ndYearProductionAcres_4950) {
    this.lowDensityCherries1stAnd2ndYearProductionAcres_4950 = lowDensityCherries1stAnd2ndYearProductionAcres_4950;
  }

  public Double getLowDensityCherries3rdAnd4thYearProductionAcres_4951() {
    return lowDensityCherries3rdAnd4thYearProductionAcres_4951;
  }

  public void setLowDensityCherries3rdAnd4thYearProductionAcres_4951(
      Double lowDensityCherries3rdAnd4thYearProductionAcres_4951) {
    this.lowDensityCherries3rdAnd4thYearProductionAcres_4951 = lowDensityCherries3rdAnd4thYearProductionAcres_4951;
  }

  public Double getLowDensityCherries5thAnd6thYearProductionAcres_4952() {
    return lowDensityCherries5thAnd6thYearProductionAcres_4952;
  }

  public void setLowDensityCherries5thAnd6thYearProductionAcres_4952(
      Double lowDensityCherries5thAnd6thYearProductionAcres_4952) {
    this.lowDensityCherries5thAnd6thYearProductionAcres_4952 = lowDensityCherries5thAnd6thYearProductionAcres_4952;
  }

  public List<String> getCropsFarmed() {
    return cropsFarmed;
  }

  public void setCropsFarmed(List<String> cropsFarmed) {
    this.cropsFarmed = cropsFarmed;
  }

  public List<NppCommodityGrid> getBerryGrid() {
    return berryGrid;
  }

  public void setBerryGrid(List<NppCommodityGrid> berryGrid) {
    this.berryGrid = berryGrid;
  }

  public Double getTotalBerryAcres() {
    return totalBerryAcres;
  }

  public void setTotalBerryAcres(Double totalBerryAcres) {
    this.totalBerryAcres = totalBerryAcres;
  }

  public List<NppCommodityGrid> getTreeFruitGrid() {
    return treeFruitGrid;
  }

  public void setTreeFruitGrid(List<NppCommodityGrid> treeFruitGrid) {
    this.treeFruitGrid = treeFruitGrid;
  }

  public Double getTotalTreeFruitAcres() {
    return totalTreeFruitAcres;
  }

  public void setTotalTreeFruitAcres(Double totalTreeFruitAcres) {
    this.totalTreeFruitAcres = totalTreeFruitAcres;
  }

  public List<NppCommodityGrid> getVegetableGrid() {
    return vegetableGrid;
  }

  public void setVegetableGrid(List<NppCommodityGrid> vegetableGrid) {
    this.vegetableGrid = vegetableGrid;
  }

  public Double getTotalVegetableAcres() {
    return totalVegetableAcres;
  }

  public void setTotalVegetableAcres(Double totalVegetableAcres) {
    this.totalVegetableAcres = totalVegetableAcres;
  }

  public List<NppCommodityGrid> getGrainGrid() {
    return grainGrid;
  }

  public void setGrainGrid(List<NppCommodityGrid> grainGrid) {
    this.grainGrid = grainGrid;
  }

  public Double getTotalGrainAcres() {
    return totalGrainAcres;
  }

  public void setTotalGrainAcres(Double totalGrainAcres) {
    this.totalGrainAcres = totalGrainAcres;
  }

  public List<String> getLivestockFarmed() {
    return livestockFarmed;
  }

  public void setLivestockFarmed(List<String> livestockFarmed) {
    this.livestockFarmed = livestockFarmed;
  }

  public List<NppCommodityGrid> getNeCattleGrid() {
    return neCattleGrid;
  }

  public void setNeCattleGrid(List<NppCommodityGrid> neCattleGrid) {
    this.neCattleGrid = neCattleGrid;
  }

  public Double getProductiveCapacityLC123() {
    return productiveCapacityLC123;
  }

  public void setProductiveCapacityLC123(Double productiveCapacityLC123) {
    this.productiveCapacityLC123 = productiveCapacityLC123;
  }

  public List<NppCommodityGrid> getOpdGrid() {
    return opdGrid;
  }

  public void setOpdGrid(List<NppCommodityGrid> opdGrid) {
    this.opdGrid = opdGrid;
  }

  public Double getPlantingYearAcres_4980() {
    return plantingYearAcres_4980;
  }

  public void setPlantingYearAcres_4980(Double plantingYearAcres_4980) {
    this.plantingYearAcres_4980 = plantingYearAcres_4980;
  }

  public List<PartnershipInformation> getPartnershipInformation() {
    return partnershipInformation;
  }

  public void setPartnershipInformation(List<PartnershipInformation> partnershipInformation) {
    this.partnershipInformation = partnershipInformation;
  }

  public String getSpecifyOther() {
    return specifyOther;
  }

  public void setSpecifyOther(String specifyOther) {
    this.specifyOther = specifyOther;
  }

  public String getAccountingCode() {
    return accountingCode;
  }

  public void setAccountingCode(String accountingCode) {
    this.accountingCode = accountingCode;
  }

  public String getDoYouHaveMultipleOperations() {
    return doYouHaveMultipleOperations;
  }

  public void setDoYouHaveMultipleOperations(String doYouHaveMultipleOperations) {
    this.doYouHaveMultipleOperations = doYouHaveMultipleOperations;
  }

  public String getUnableToCompleteBecauseOfDisaster() {
    return unableToCompleteBecauseOfDisaster;
  }

  public void setUnableToCompleteBecauseOfDisaster(String unableToCompleteBecauseOfDisaster) {
    this.unableToCompleteBecauseOfDisaster = unableToCompleteBecauseOfDisaster;
  }

  public Double getBredCow_104() {
    return bredCow_104;
  }

  public void setBredCow_104(Double bredCow_104) {
    this.bredCow_104 = bredCow_104;
  }

  public Double getChristmasTreesEstablishmentAcres() {
    return ChristmasTreesEstablishmentAcres;
  }

  public void setChristmasTreesEstablishmentAcres(Double christmasTreesEstablishmentAcres) {
    ChristmasTreesEstablishmentAcres = christmasTreesEstablishmentAcres;
  }

  public Double getChristmasTreesEstablishmentAcres1() {
    return ChristmasTreesEstablishmentAcres1;
  }

  public void setChristmasTreesEstablishmentAcres1(Double christmasTreesEstablishmentAcres1) {
    ChristmasTreesEstablishmentAcres1 = christmasTreesEstablishmentAcres1;
  }

  public Double getChristmasTreesEstablishmentAcres2() {
    return ChristmasTreesEstablishmentAcres2;
  }

  public void setChristmasTreesEstablishmentAcres2(Double christmasTreesEstablishmentAcres2) {
    ChristmasTreesEstablishmentAcres2 = christmasTreesEstablishmentAcres2;
  }

  public Double getChristmasTreesEstablishmentAcres3() {
    return ChristmasTreesEstablishmentAcres3;
  }

  public void setChristmasTreesEstablishmentAcres3(Double christmasTreesEstablishmentAcres3) {
    ChristmasTreesEstablishmentAcres3 = christmasTreesEstablishmentAcres3;
  }

  public Double getChristmasTreesEstablishmentAcres4() {
    return ChristmasTreesEstablishmentAcres4;
  }

  public void setChristmasTreesEstablishmentAcres4(Double christmasTreesEstablishmentAcres4) {
    ChristmasTreesEstablishmentAcres4 = christmasTreesEstablishmentAcres4;
  }

  public LabelValue getFarmType() {
    return farmType;
  }

  public void setFarmType(LabelValue farmType) {
    this.farmType = farmType;
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

}
