/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.PayableItem;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.ProductionInsurance;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.WholeFarmParticipant;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.util.DateUtils;

/**
 * 
 * @author awilkinson
 * @created Nov 24, 2010
 *
 */
public final class TestModelBuilder {

  private static final int PARTICIPANT_PIN = 631986734;
  
  /**
   * private constructor because this is a utility class
   */
  private TestModelBuilder() {
  }

  public static Scenario getScenario1() {
    int pin = PARTICIPANT_PIN;
    int programYear = 2009;
    Scenario s = new Scenario();

    s.setYear(new Integer(programYear));
    s.setScenarioNumber(new Integer(2));
    s.setRevisionCount(new Integer(1));
    s.setScenarioStateCode("IP");
    s.setScenarioStateCodeDescription("In Progress");
    s.setAssignedToUserId("IDIR\\AHOPKINS");
    s.setAssignedToUserGuid("AHOPKINS000000000000000000000000");
    s.setIsDefaultInd(Boolean.TRUE);
    s.setWhenUpdatedTimestamp(DateUtils.getTimestamp(2010, 6, 5, 0, 0, 0, 0));
    s.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);

    Client client = getClient();
    client.setParticipantPin(new Integer(pin));
    s.setClient(client);
    s.setFarmingYear(getFarmingYear1(s));
    s.setScenarioMetaDataList(getScenarioMetaDataList(programYear));

    return s;
  }
  
  public static Scenario getScenario2() {
    int pin = PARTICIPANT_PIN;
    int programYear = 2009;
    Scenario s = new Scenario();
    
    s.setYear(new Integer(programYear));
    s.setScenarioNumber(new Integer(3));
    s.setRevisionCount(new Integer(1));
    s.setScenarioStateCode("IP");
    s.setScenarioStateCodeDescription("In Progress");
    s.setAssignedToUserId("IDIR\\AHOPKINS");
    s.setAssignedToUserGuid("AHOPKINS000000000000000000000000");
    s.setIsDefaultInd(Boolean.TRUE);
    s.setWhenUpdatedTimestamp(DateUtils.getTimestamp(2010, 6, 5, 0, 0, 0, 0));
    s.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    Client client = getClient();
    client.setParticipantPin(new Integer(pin));
    s.setClient(client);
    s.setFarmingYear(getFarmingYear2(s));
    s.setScenarioMetaDataList(getScenarioMetaDataList(programYear));
    
    return s;
  }

  public static Scenario getScenario3() {
    int pin = PARTICIPANT_PIN;
    int programYear = 2009;
    Scenario s = new Scenario();

    s.setYear(new Integer(programYear));
    s.setScenarioNumber(new Integer(2));
    s.setRevisionCount(new Integer(1));
    s.setScenarioStateCode("IP");
    s.setScenarioStateCodeDescription("In Progress");
    s.setAssignedToUserId("IDIR\\AHOPKINS");
    s.setAssignedToUserGuid("AHOPKINS000000000000000000000000");
    s.setIsDefaultInd(Boolean.TRUE);
    s.setWhenUpdatedTimestamp(DateUtils.getTimestamp(2010, 6, 5, 0, 0, 0, 0));
    s.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);

    Client client = getClient();
    client.setParticipantPin(new Integer(pin));
    s.setClient(client);
    s.setFarmingYear(getFarmingYear3(s));
    s.setScenarioMetaDataList(getScenarioMetaDataList(programYear));

    return s;
  }
  
  /**
   * @param scenario 
   * @return ReferenceScenario
   */
  public static ReferenceScenario getReferenceScenario(Scenario scenario) {
    ReferenceScenario r = new ReferenceScenario();
    
    r.setScenarioId(new Integer(2008123));
    r.setScenarioNumber(new Integer(2));
    r.setScenarioTypeCode(ScenarioTypeCodes.REF);
    r.setScenarioTypeCodeDescription("Reference");
    r.setIsDefaultInd(Boolean.FALSE);
    r.setScenarioDate(new Date());
    r.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    r.setParentScenario(scenario);
    
    FarmingYear farmingYear = getFarmingYear2(r);
    r.setFarmingYear(farmingYear);
    
    List<FarmingOperation> operations = new ArrayList<>();
    FarmingOperation fo1 = getFarmingOperation1(farmingYear);
    operations.add(fo1);

    fo1.setCropItems(new ArrayList<CropItem>());
    fo1.setLivestockItems(new ArrayList<LivestockItem>());
    fo1.setInputItems(new ArrayList<InputItem>());
    fo1.setReceivableItems(new ArrayList<ReceivableItem>());
    fo1.setPayableItems(new ArrayList<PayableItem>());
    fo1.setFarmingOperationPartners(getFarmingOperationPartners());
    fo1.setIncomeExpenses(getIncomeExpenses1());
    fo1.setMargin(getMargin());
    fo1.setProductionInsurances(getProductionInsurances1());
    fo1.setProductiveUnitCapacities(getProductiveUnitCapacities1());

    
    farmingYear.setFarmingOperations(operations);

    
    return r;
  }
  
  /**
   * @return Client
   */
  public static Client getClient() {
    Client c = new Client();
    c.setClientId(new Integer(1));
    c.setFederalIdentifier("631986734");
    c.setParticipantClassCode("1");
    c.setParticipantClassCodeDescription("Individual");
    c.setOwner(getOwnerPerson());
    c.setContact(getContactPerson());
    c.setRevisionCount(new Integer(1));
    return c;
  }

  /**
   * @return Person
   */
  public static Person getOwnerPerson() {
    Person p = new Person();
    p.setAddressLine1("2221 ENS ROAD");
    p.setCity("VANDERHOOF");
    p.setCountry("CAN");
    p.setDaytimePhone("2505674946");
    p.setEveningPhone("2505674946");
    p.setFaxNumber("2505674946");
    p.setFirstName("KENNETH");
    p.setLastName("ISAAC");
    p.setPersonId(new Integer(1));
    p.setPostalCode("V0J3A1");
    p.setProvinceState("BC");
    p.setRevisionCount(new Integer(1));
    return p;
  }

  /**
   * @return Person
   */
  public static Person getContactPerson() {
    Person p = new Person();
    p.setAddressLine1("HIGHWAY 5");
    p.setCity("ROBLIN");
    p.setCorpName("WIEBES ACCOUNTING");
    p.setCountry("CAN");
    p.setDaytimePhone("2505674946");
    p.setEveningPhone("2505674946");
    p.setFaxNumber("2505674946");
    p.setPersonId(new Integer(2));
    p.setPostalCode("R0L1P0");
    p.setProvinceState("MB");
    p.setRevisionCount(new Integer(1));
    
    return p;
  }
  
  
  /**
   * @param s 
   * @return FarmingYear
   */
  public static FarmingYear getFarmingYear1(Scenario s) {
    FarmingYear f = new FarmingYear();
    s.setFarmingYear(f);

    f.setProgramYearVersionId(new Integer(1234));
    f.setProgramYearId(new Integer(53379));
    f.setProgramYearVersionNumber(new Integer(2));
    f.setRevisionCount(new Integer(1));
    f.setIsLocallyUpdated(Boolean.TRUE);

    f.setBenefit(getBenefit());
    f.setFarmingOperations(getFarmingOperations1(f));
    f.setMarginTotal(getMarginTotal());
    f.setWholeFarmParticipants(getWholeFarmParticipant());

    f.setAgristabFedStsCode(new Integer(1));
    f.setAgristabFedStsCodeDescription("Processing Complete - verified data");
    f.setCommonShareTotal(new Integer(0));
    f.setFarmYears(new Integer(2));
    f.setMunicipalityCode("51");
    f.setMunicipalityCodeDescription("Bulkley-Nechako");
    f.setOtherText("other text");
    f.setParticipantProfileCode("3");
    f.setParticipantProfileCodeDescription("Agri-Stability & Agri-Invest");
    f.setProvinceOfMainFarmstead("BC");
    f.setProvinceOfResidence("BC");
    f.setPostMarkDate(DateUtils.getTimestamp(2009, 2, 10, 0, 0, 0, 0));
    f.setCraStatementAReceivedDate(null);
    
    // Booleans
    f.setIsAccrualCashConversion(Boolean.FALSE);
    f.setIsAccrualWorksheet(Boolean.FALSE);
    f.setIsCanSendCobToRep(Boolean.FALSE);
    f.setIsCombinedFarm(Boolean.FALSE);
    f.setIsCompletedProdCycle(Boolean.FALSE);
    f.setIsCoopMember(Boolean.FALSE);
    f.setIsCorporateShareholder(Boolean.FALSE);
    f.setIsCwbWorksheet(Boolean.FALSE);
    f.setIsDisaster(Boolean.FALSE);
    f.setIsLastYearFarming(Boolean.FALSE);
    f.setIsPartnershipMember(Boolean.FALSE);
    f.setIsPerishableCommodities(Boolean.FALSE);
    f.setIsReceipts(Boolean.FALSE);
    f.setIsSoleProprietor(Boolean.FALSE);

    return f;
  }
  
  
  /**
   * @param s 
   * @return FarmingYear
   */
  public static FarmingYear getFarmingYear2(ReferenceScenario s) {
    FarmingYear f = new FarmingYear();
    
    s.setFarmingYear(f);
    
    f.setProgramYearVersionId(new Integer(2345));
    f.setProgramYearId(new Integer(54379));
    f.setProgramYearVersionNumber(new Integer(3));
    f.setRevisionCount(new Integer(1));
    f.setIsLocallyUpdated(Boolean.FALSE);
    
    f.setBenefit(getBenefit());
    f.setFarmingOperations(getFarmingOperations2(f));
    f.setMarginTotal(getMarginTotal());
    f.setWholeFarmParticipants(getWholeFarmParticipant());
    
    f.setAgristabFedStsCode(new Integer(3));
    f.setAgristabFedStsCodeDescription("In Progress");
    f.setCommonShareTotal(null);
    f.setFarmYears(new Integer(15));
    f.setMunicipalityCode("35");
    f.setMunicipalityCodeDescription("Central Okanagan");
    f.setOtherText(null);
    f.setParticipantProfileCode("1");
    f.setParticipantProfileCodeDescription("Agri-Stability Only");
    f.setProvinceOfMainFarmstead("AB");
    f.setProvinceOfResidence("AB");
    f.setPostMarkDate(DateUtils.getTimestamp(2009, 10, 11, 0, 0, 0, 0));
    f.setCraStatementAReceivedDate(DateUtils.getTimestamp(2009, 5, 5, 0, 0, 0, 0));
    
    // Booleans
    f.setIsAccrualCashConversion(Boolean.TRUE);
    f.setIsAccrualWorksheet(Boolean.TRUE);
    f.setIsCanSendCobToRep(Boolean.TRUE);
    f.setIsCombinedFarm(Boolean.TRUE);
    f.setIsCompletedProdCycle(Boolean.TRUE);
    f.setIsCoopMember(Boolean.TRUE);
    f.setIsCorporateShareholder(Boolean.TRUE);
    f.setIsCwbWorksheet(Boolean.TRUE);
    f.setIsDisaster(Boolean.TRUE);
    f.setIsLastYearFarming(Boolean.TRUE);
    f.setIsPartnershipMember(Boolean.TRUE);
    f.setIsPerishableCommodities(Boolean.TRUE);
    f.setIsReceipts(Boolean.TRUE);
    f.setIsSoleProprietor(Boolean.TRUE);
    
    return f;
  }
  
  
  /**
   * @param s 
   * @return FarmingYear
   */
  public static FarmingYear getFarmingYear3(Scenario s) {
    FarmingYear f = new FarmingYear();
    s.setFarmingYear(f);

    f.setProgramYearVersionId(new Integer(1234));
    f.setProgramYearId(new Integer(53379));
    f.setProgramYearVersionNumber(new Integer(2));
    f.setRevisionCount(new Integer(1));
    f.setIsLocallyUpdated(Boolean.TRUE);

    f.setBenefit(getBenefit());
    f.setFarmingOperations(getFarmingOperations3(f));
    f.setMarginTotal(getMarginTotal());
    f.setWholeFarmParticipants(getWholeFarmParticipant());

    f.setAgristabFedStsCode(new Integer(1));
    f.setAgristabFedStsCodeDescription("Processing Complete - verified data");
    f.setCommonShareTotal(new Integer(0));
    f.setFarmYears(new Integer(2));
    f.setMunicipalityCode("51");
    f.setMunicipalityCodeDescription("Bulkley-Nechako");
    f.setOtherText("other text");
    f.setParticipantProfileCode("3");
    f.setParticipantProfileCodeDescription("Agri-Stability & Agri-Invest");
    f.setProvinceOfMainFarmstead("BC");
    f.setProvinceOfResidence("BC");
    f.setPostMarkDate(DateUtils.getTimestamp(2009, 2, 10, 0, 0, 0, 0));
    f.setCraStatementAReceivedDate(null);
    
    // Booleans
    f.setIsAccrualCashConversion(Boolean.FALSE);
    f.setIsAccrualWorksheet(Boolean.FALSE);
    f.setIsCanSendCobToRep(Boolean.FALSE);
    f.setIsCombinedFarm(Boolean.FALSE);
    f.setIsCompletedProdCycle(Boolean.FALSE);
    f.setIsCoopMember(Boolean.FALSE);
    f.setIsCorporateShareholder(Boolean.FALSE);
    f.setIsCwbWorksheet(Boolean.FALSE);
    f.setIsDisaster(Boolean.FALSE);
    f.setIsLastYearFarming(Boolean.FALSE);
    f.setIsPartnershipMember(Boolean.FALSE);
    f.setIsPerishableCommodities(Boolean.FALSE);
    f.setIsReceipts(Boolean.FALSE);
    f.setIsSoleProprietor(Boolean.FALSE);

    return f;
  }
  
  
  /**
   * @return FarmingYear
   */
  public static FarmingYear getFarmingYear3() {
    FarmingYear f = new FarmingYear();
    
    ReferenceScenario r = new ReferenceScenario();
    r.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    r.setFarmingYear(f);

    f.setProgramYearVersionId(new Integer(3456));
    f.setProgramYearId(new Integer(54349));
    f.setProgramYearVersionNumber(new Integer(4));
    f.setRevisionCount(new Integer(1));
    f.setIsLocallyUpdated(Boolean.FALSE);
    
    f.setBenefit(getBenefit());
    f.setFarmingOperations(getFarmingOperations3(f));
    f.setMarginTotal(getMarginTotal());
    f.setWholeFarmParticipants(getWholeFarmParticipant());
    
    f.setAgristabFedStsCode(new Integer(3));
    f.setAgristabFedStsCodeDescription("In Progress");
    f.setCommonShareTotal(null);
    f.setFarmYears(new Integer(15));
    f.setMunicipalityCode("35");
    f.setMunicipalityCodeDescription("Central Okanagan");
    f.setOtherText(null);
    f.setParticipantProfileCode("1");
    f.setParticipantProfileCodeDescription("Agri-Stability Only");
    f.setProvinceOfMainFarmstead("AB");
    f.setProvinceOfResidence("AB");
    f.setPostMarkDate(DateUtils.getTimestamp(2009, 10, 11, 0, 0, 0, 0));
    f.setCraStatementAReceivedDate(DateUtils.getTimestamp(2009, 5, 5, 0, 0, 0, 0));
    
    // Booleans
    f.setIsAccrualCashConversion(Boolean.TRUE);
    f.setIsAccrualWorksheet(Boolean.TRUE);
    f.setIsCanSendCobToRep(Boolean.TRUE);
    f.setIsCombinedFarm(Boolean.TRUE);
    f.setIsCompletedProdCycle(Boolean.TRUE);
    f.setIsCoopMember(Boolean.TRUE);
    f.setIsCorporateShareholder(Boolean.TRUE);
    f.setIsCwbWorksheet(Boolean.TRUE);
    f.setIsDisaster(Boolean.TRUE);
    f.setIsLastYearFarming(Boolean.TRUE);
    f.setIsPartnershipMember(Boolean.TRUE);
    f.setIsPerishableCommodities(Boolean.TRUE);
    f.setIsReceipts(Boolean.TRUE);
    f.setIsSoleProprietor(Boolean.TRUE);
    
    return f;
  }

  /**
   * @return WholeFarmParticipant
   */
  public static List<WholeFarmParticipant> getWholeFarmParticipant() {
    List<WholeFarmParticipant> list = new ArrayList<>();
    return list;
  }

  /**
   * @return MarginTotal
   */
  public static MarginTotal getMarginTotal() {
    return null;
  }

  public static FarmingOperation getFarmingOperation1(FarmingYear farmingYear) {
    FarmingOperation f = new FarmingOperation();
    List<FarmingOperation> farmingOperations = new ArrayList<>();
    farmingOperations.add(f);
    farmingYear.setFarmingOperations(farmingOperations);
    
    f.setAccountingCode("2");
    f.setAccountingCodeDescription("Cash");
    f.setPartnershipName("KENNETH & DARLENE ISAAC");
    
    // Integers
    f.setFarmingOperationId(new Integer(1));
    f.setOperationNumber(new Integer(1));
    f.setSchedule("A");
    f.setPartnershipPin(new Integer(1));
    f.setRevisionCount(new Integer(1));

    // Doubles
    f.setBusinessUseHomeExpense(new Double(0.0d));
    f.setFarmingExpenses(new Double(160814d));
    f.setGrossIncome(new Double(96826d));
    f.setInventoryAdjustments(new Double(59000d));
    f.setNetFarmIncome(new Double(3093d));
    f.setNetIncomeAfterAdj(new Double(3093d));
    f.setNetIncomeBeforeAdj(new Double(63988d));
    f.setOtherDeductions(new Double(0.0d));
    f.setPartnershipPercent(new Double(0.0d));

    // Booleans
    f.setIsCropDisaster(Boolean.FALSE);
    f.setIsCropShare(Boolean.TRUE);
    f.setIsFeederMember(Boolean.TRUE);
    f.setIsLandlord(Boolean.FALSE);
    f.setIsLivestockDisaster(Boolean.FALSE);
    f.setIsLocallyUpdated(Boolean.FALSE);
    f.setIsLocallyGenerated(Boolean.FALSE);
    
    // Dates
    f.setFiscalYearStart(DateUtils.getTimestamp(2009, 0, 1, 0, 0, 0, 0));
    final int december = 11;
    f.setFiscalYearEnd(DateUtils.getTimestamp(2009, december, 31, 0, 0, 0, 0));

    // built objects
    f.setCropItems(getCropItems1());
    f.setLivestockItems(getLivestockItems1());
    f.setInputItems(getInputItems1());
    f.setReceivableItems(getReceivableItems1());
    f.setPayableItems(getPayableItems1());
    f.setFarmingOperationPartners(getFarmingOperationPartners());
    f.setIncomeExpenses(getIncomeExpenses1());
    f.setMargin(getMargin());
    f.setProductionInsurances(getProductionInsurances1());
    f.setProductiveUnitCapacities(getProductiveUnitCapacities1());

    return f;
  }
  
  /**
   * @param f2 
   * @return FarmingOperation
   */
  public static FarmingOperation getFarmingOperation2(FarmingYear farmingYear) {
    FarmingOperation f = new FarmingOperation();
    
    List<FarmingOperation> farmingOperations = new ArrayList<>();
    farmingOperations.add(f);
    farmingYear.setFarmingOperations(farmingOperations);

    f.setAccountingCode("2");
    f.setAccountingCodeDescription("Cash");
    f.setPartnershipName("Friends and I");
    
    // Integers
    f.setFarmingOperationId(new Integer(2));
    f.setOperationNumber(new Integer(2));
    f.setSchedule("B");
    f.setPartnershipPin(new Integer(1));
    f.setRevisionCount(new Integer(1));

    // Doubles
    f.setBusinessUseHomeExpense(new Double(0.0d));
    f.setFarmingExpenses(new Double(150000d));
    f.setGrossIncome(new Double(95000d));
    f.setInventoryAdjustments(new Double(60000d));
    f.setNetFarmIncome(new Double(4000d));
    f.setNetIncomeAfterAdj(new Double(4000d));
    f.setNetIncomeBeforeAdj(new Double(65000));
    f.setOtherDeductions(new Double(0.0d));
    f.setPartnershipPercent(new Double(0.0d));
    
    // Booleans
    f.setIsCropDisaster(Boolean.FALSE);
    f.setIsCropShare(Boolean.FALSE);
    f.setIsFeederMember(Boolean.FALSE);
    f.setIsLandlord(Boolean.FALSE);
    f.setIsLivestockDisaster(Boolean.FALSE);
    f.setIsLocallyUpdated(Boolean.TRUE);
    f.setIsLocallyGenerated(Boolean.FALSE);
    
    // Dates
    f.setFiscalYearStart(null);
    f.setFiscalYearEnd(null);
    
    // built objects
    f.setCropItems(getCropItems1());
    f.setLivestockItems(getLivestockItems1());
    f.setInputItems(getInputItems1());
    f.setReceivableItems(getReceivableItems1());
    f.setPayableItems(getPayableItems1());
    f.setFarmingOperationPartners(getFarmingOperationPartners());
    f.setIncomeExpenses(getIncomeExpenses1());
    f.setMargin(getMargin());
    f.setProductionInsurances(getProductionInsurances1());
    f.setProductiveUnitCapacities(getProductiveUnitCapacities1());
    
    return f;
  }
  
  /**
   * @param farmingYear 
   * @return FarmingOperation
   */
  public static FarmingOperation getFarmingOperation3(FarmingYear farmingYear) {
    FarmingOperation f = new FarmingOperation();
    
    List<FarmingOperation> farmingOperations = new ArrayList<>();
    farmingOperations.add(f);
    farmingYear.setFarmingOperations(farmingOperations);
    
    f.setAccountingCode("1");
    f.setAccountingCodeDescription("Accrual");
    f.setPartnershipName("My Friends and Me");
    
    // Integers
    f.setFarmingOperationId(new Integer(2));
    f.setOperationNumber(new Integer(2));
    f.setSchedule("C");
    f.setPartnershipPin(new Integer(20));
    f.setRevisionCount(new Integer(1));

    // Doubles
    f.setBusinessUseHomeExpense(new Double(1.0d));
    f.setFarmingExpenses(new Double(160814d));
    f.setGrossIncome(new Double(96826d));
    f.setInventoryAdjustments(new Double(59000d));
    f.setNetFarmIncome(new Double(3093d));
    f.setNetIncomeAfterAdj(new Double(3093d));
    f.setNetIncomeBeforeAdj(new Double(63988d));
    f.setOtherDeductions(new Double(2.0d));
    f.setPartnershipPercent(new Double(0.03d));
    
    // Booleans
    f.setIsCropDisaster(Boolean.TRUE);
    f.setIsCropShare(Boolean.TRUE);
    f.setIsFeederMember(Boolean.TRUE);
    f.setIsLandlord(Boolean.TRUE);
    f.setIsLivestockDisaster(Boolean.TRUE);
    f.setIsLocallyUpdated(Boolean.FALSE);
    f.setIsLocallyGenerated(Boolean.FALSE);
    
    // Dates
    f.setFiscalYearStart(DateUtils.getTimestamp(2009, 0, 1, 0, 0, 0, 0));
    f.setFiscalYearEnd(DateUtils.getTimestamp(2009, 11, 31, 0, 0, 0, 0));
    
    // built objects
    f.setCropItems(getCropItems2());
    f.setLivestockItems(getLivestockItems2());
    f.setInputItems(getInputItems2());
    f.setReceivableItems(getReceivableItems2());
    f.setPayableItems(getPayableItems2());
    f.setFarmingOperationPartners(getFarmingOperationPartners());
    f.setIncomeExpenses(getIncomeExpenses2());
    f.setMargin(getMargin());
    f.setProductionInsurances(getProductionInsurances1());
    f.setProductiveUnitCapacities(getProductiveUnitCapacities2());
    
    return f;
  }
  
  /**
   * @param farmingYear 
   * @return FarmingOperation
   */
  public static FarmingOperation getFarmingOperation4(FarmingYear farmingYear) {
    FarmingOperation f = new FarmingOperation();
    
    List<FarmingOperation> farmingOperations = new ArrayList<>();
    farmingOperations.add(f);
    farmingYear.setFarmingOperations(farmingOperations);
    
    f.setAccountingCode("1");
    f.setAccountingCodeDescription("Accrual");
    f.setPartnershipName("My Friends and Some Other Friends");
    
    // Integers
    f.setFarmingOperationId(new Integer(3));
    f.setOperationNumber(new Integer(3));
    f.setSchedule("D");
    f.setPartnershipPin(new Integer(40));
    f.setRevisionCount(new Integer(1));
    
    // Doubles
    f.setBusinessUseHomeExpense(new Double(1.0d));
    f.setFarmingExpenses(new Double(160814d));
    f.setGrossIncome(new Double(96826d));
    f.setInventoryAdjustments(new Double(59000d));
    f.setNetFarmIncome(new Double(3093d));
    f.setNetIncomeAfterAdj(new Double(3093d));
    f.setNetIncomeBeforeAdj(new Double(63988d));
    f.setOtherDeductions(new Double(2.0d));
    f.setPartnershipPercent(new Double(0.03d));
    
    // Booleans
    f.setIsCropDisaster(Boolean.TRUE);
    f.setIsCropShare(Boolean.TRUE);
    f.setIsFeederMember(Boolean.TRUE);
    f.setIsLandlord(Boolean.TRUE);
    f.setIsLivestockDisaster(Boolean.TRUE);
    f.setIsLocallyUpdated(Boolean.FALSE);
    f.setIsLocallyGenerated(Boolean.TRUE);
    
    // Dates
    f.setFiscalYearStart(DateUtils.getTimestamp(2009, 0, 1, 0, 0, 0, 0));
    f.setFiscalYearEnd(DateUtils.getTimestamp(2009, 11, 31, 0, 0, 0, 0));
    
    // built objects
    f.setCropItems(getCropItems2());
    f.setLivestockItems(getLivestockItems2());
    f.setInputItems(getInputItems2());
    f.setReceivableItems(getReceivableItems2());
    f.setPayableItems(getPayableItems2());
    f.setFarmingOperationPartners(getFarmingOperationPartners());
    f.setIncomeExpenses(getIncomeExpenses2());
    f.setMargin(getMargin());
    f.setProductionInsurances(getProductionInsurances1());
    f.setProductiveUnitCapacities(getProductiveUnitCapacities2());
    
    return f;
  }

  /**
   * @return List
   */
  public static List<ProductiveUnitCapacity> getProductiveUnitCapacities1() {
    List<ProductiveUnitCapacity> list = new ArrayList<>();
    list.add(getPuc6949());
    list.add(getPuc104());
    list.add(getPuc145());
    return list;
  }
  
  /**
   * @return List
   */
  public static List<ProductiveUnitCapacity> getProductiveUnitCapacities2() {
    List<ProductiveUnitCapacity> list = new ArrayList<>();

    ProductiveUnitCapacity puc6949 = getPuc6949();
    puc6949.setReportedAmount(new Double(2838.513d));

    list.add(puc6949);
    list.add(getPuc113());
    list.add(getPuc145());

    return list;
  }

  /**
   * @return List
   */
  public static List<ProductionInsurance> getProductionInsurances1() {
    List<ProductionInsurance> list = new ArrayList<>();
    ProductionInsurance pi1 = new ProductionInsurance();
    pi1.setIsLocallyUpdated(Boolean.FALSE);
    pi1.setProductionInsuranceId(new Integer(1));
    pi1.setProductionInsuranceNumber("125534 06");
    pi1.setRevisionCount(new Integer(1));
    list.add(pi1);
    return list;
  }

  /**
   * @return Margin
   */
  public static Margin getMargin() {
    return null;
  }

  /**
   * @return List
   */
  public static List<IncomeExpense> getIncomeExpenses1() {
    List<IncomeExpense> list = new ArrayList<>();
    list.add(getIncome132());
    list.add(getIncome9574());
    list.add(getExpense132());
    return list;
  }
  
  /**
   * @return List
   */
  public static List<IncomeExpense> getIncomeExpenses2() {
    List<IncomeExpense> list = new ArrayList<>();
    IncomeExpense income132 = getIncome132();
    income132.setReportedAmount(new Double(17000));
    list.add(income132);
    list.add(getExpense132());
    list.add(getExpense9574());
    return list;
  }

  /**
   * @return List
   */
  public static List<FarmingOperationPartner> getFarmingOperationPartners() {
    List<FarmingOperationPartner> list = new ArrayList<>();
    return list;
  }

  /**
   * @return List
   */
  public static List<CropItem> getCropItems1() {
    List<CropItem> list = new ArrayList<>();
    list.add(getCropItem6949());
    list.add(getCropItem6000UnitOther());
    return list;
  }
  
  /**
   * @return List
   */
  public static List<CropItem> getCropItems2() {
    List<CropItem> list = new ArrayList<>();
    list.add(getCropItem6949());
    list.add(getCropItem6000UnitTonnes());
    return list;
  }

  /**
   * @return List
   */
  public static List<LivestockItem> getLivestockItems1() {
    List<LivestockItem> list = new ArrayList<>();
    list.add(getLivestockItem4005());
    list.add(getLivestockItem4001());
    return list;
  }
  
  /**
   * @return List
   */
  public static List<LivestockItem> getLivestockItems2() {
    List<LivestockItem> list = new ArrayList<>();
    
    LivestockItem livestockItem4005 = getLivestockItem4005();
    livestockItem4005.setReportedQuantityEnd(new Double(7));
    livestockItem4005.setReportedPriceEnd(new Double(6));
    
    list.add(livestockItem4005);
    list.add(getLivestockItem7600());
    return list;
  }
  
  /**
   * @return List
   */
  public static List<InputItem> getInputItems1() {
    List<InputItem> list = new ArrayList<>();
    list.add(getInputItem9661());
    list.add(getInputItem9662());
    return list;
  }

  /**
   * @return List
   */
  public static List<ReceivableItem> getReceivableItems1() {
    List<ReceivableItem> list = new ArrayList<>();
    list.add(getReceivableItem132());
    return list;
  }

  /**
   * @return List
   */
  public static List<PayableItem> getPayableItems1() {
    List<PayableItem> list = new ArrayList<>();
    list.add(getPayableItem9663());
    list.add(getPayableItem8());
    return list;
  }
  
  /**
   * @return List
   */
  public static List<InputItem> getInputItems2() {
    List<InputItem> list = new ArrayList<>();

    InputItem inputItem9661 = getInputItem9661();
    inputItem9661.setReportedStartOfYearAmount(new Double(300));
    inputItem9661.setReportedEndOfYearAmount(new Double(400));

    list.add(inputItem9661);
    list.add(getInputItem9662());
    list.add(getInputItem9662());
    return list;
  }
  
  /**
   * @return List
   */
  public static List<ReceivableItem> getReceivableItems2() {
    List<ReceivableItem> list = new ArrayList<>();
    return list;
  }
  
  /**
   * @return List
   */
  public static List<PayableItem> getPayableItems2() {
    List<PayableItem> list = new ArrayList<>();
    PayableItem payableItem8 = getPayableItem8();
    payableItem8.setReportedEndOfYearAmount(new Double(250));

    list.add(getPayableItem9663());
    list.add(payableItem8);
    return list;
  }

  /**
   * @param f 
   * @return List
   */
  public static List<FarmingOperation> getFarmingOperations1(FarmingYear f) {
    List<FarmingOperation> operations = new ArrayList<>();
    operations.add(getFarmingOperation1(f));
    operations.add(getFarmingOperation2(f));
    return operations;
  }
  
  /**
   * @return List
   */
  public static List<FarmingOperation> getFarmingOperations2(FarmingYear farmingYear) {
    List<FarmingOperation> operations = new ArrayList<>();
    operations.add(getFarmingOperation1(farmingYear));
    operations.add(getFarmingOperation3(farmingYear));
    return operations;
  }
  
  /**
   * @return List
   */
  public static List<FarmingOperation> getFarmingOperations3(FarmingYear farmingYear) {
    List<FarmingOperation> operations = new ArrayList<>();
    operations.add(getFarmingOperation1(farmingYear));
    operations.add(getFarmingOperation3(farmingYear));
    operations.add(getFarmingOperation4(farmingYear));
    return operations;
  }
  
  /**
   * @return Benefit
   */
  public static Benefit getBenefit() {
    Benefit b = new Benefit();
    return b;
  }
  
  public static List<ScenarioMetaData> getScenarioMetaDataList(int programYear) {
    List<ScenarioMetaData> scenarioMetaDataList = new ArrayList<>();
    scenarioMetaDataList.add(getScenarioMetaData1(programYear));
    scenarioMetaDataList.add(getScenarioMetaData2(programYear));
    scenarioMetaDataList.add(getScenarioMetaData3(programYear));
    scenarioMetaDataList.add(getScenarioMetaData4(programYear));
    return scenarioMetaDataList;
  }
  
  public static ScenarioMetaData getScenarioMetaData1(int programYear) {
    ScenarioMetaData m = new ScenarioMetaData();
    m.setProgramYear(new Integer(programYear));
    m.setProgramYearVersion(new Integer(1));
    m.setScenarioNumber(new Integer(1));
    m.setScenarioTypeCode("CRA");
    m.setScenarioTypeDescription("CRA");
    m.setScenarioCreatedDate(DateUtils.getTimestamp(programYear, 3, 19, 0, 0, 0, 0));
    m.setScenarioStateCode("REC");
    m.setScenarioStateDescription("Received");
    m.setScenarioDescription(null);
    m.setScenarioCreatedBy("IDIR\\PEJONES");
    
    return m;
  }
  
  public static ScenarioMetaData getScenarioMetaData2(int programYear) {
    ScenarioMetaData m = new ScenarioMetaData();
    m.setProgramYear(new Integer(programYear));
    m.setProgramYearVersion(new Integer(1));
    m.setScenarioNumber(new Integer(2));
    m.setScenarioTypeCode("USER");
    m.setScenarioTypeDescription("User");
    m.setScenarioCreatedDate(DateUtils.getTimestamp(programYear, 3, 21, 0, 0, 0, 0));
    m.setScenarioStateCode(ScenarioStateCodes.AMENDED);
    m.setScenarioStateDescription("Amended");
    m.setScenarioDescription("My first scenario.");
    m.setScenarioCreatedBy("IDIR\\AHOPKINS");
    
    return m;
  }
  
  public static ScenarioMetaData getScenarioMetaData3(int programYear) {
    ScenarioMetaData m = new ScenarioMetaData();
    m.setProgramYear(new Integer(programYear));
    m.setProgramYearVersion(new Integer(2));
    m.setScenarioNumber(new Integer(3));
    m.setScenarioTypeCode("CRA");
    m.setScenarioTypeDescription("CRA");
    m.setScenarioCreatedDate(DateUtils.getTimestamp(programYear, 3, 24, 0, 0, 0, 0));
    m.setScenarioStateCode("REC");
    m.setScenarioStateDescription("Received");
    m.setScenarioDescription(null);
    m.setScenarioCreatedBy("IDIR\\PEJONES");
    
    return m;
  }
  
  public static ScenarioMetaData getScenarioMetaData4(int programYear) {
    ScenarioMetaData m = new ScenarioMetaData();
    m.setProgramYear(new Integer(programYear));
    m.setProgramYearVersion(new Integer(2));
    m.setScenarioNumber(new Integer(4));
    m.setScenarioTypeCode("USER");
    m.setScenarioTypeDescription("User");
    m.setScenarioCreatedDate(DateUtils.getTimestamp(programYear, 3, 30, 0, 0, 0, 0));
    m.setScenarioStateCode("IP");
    m.setScenarioStateDescription("In Progress");
    m.setScenarioDescription("New CRA Data received.");
    m.setScenarioCreatedBy("IDIR\\AHOPKINS");
    
    return m;
  }


  /**
   * CropItem for UPDATE and DELETE tests - with reported data
   */
  public static CropItem getCropItem6949() {
    CropItem crop1 = new CropItem();
    crop1.setInventoryItemCode("6949");
    crop1.setInventoryItemCodeDescription("Flowers, Fresh Cut, Greenhouse");
    crop1.setInventoryClassCode(InventoryClassCodes.CROP);
    crop1.setCropUnitCode("64");
    crop1.setCropUnitCodeDescription("Other");

    crop1.setAdjInventoryId(new Integer(529730));
    crop1.setReportedInventoryId(new Integer(50469));
    crop1.setAdjustedByUserId("IDIR\\AHOPKINS");
    crop1.setRevisionCount(new Integer(1));
    crop1.setCommodityXrefId(new Integer(1764));
    
    crop1.setReportedQuantityStart(null);
    crop1.setReportedPriceStart(null);
    crop1.setReportedQuantityEnd(new Double(2500));
    crop1.setReportedPriceEnd(null);

    crop1.setAdjQuantityStart(new Double(20));
    crop1.setAdjPriceStart(new Double(30));
    crop1.setAdjQuantityEnd(new Double(40));
    crop1.setAdjPriceEnd(new Double(50));
    
    crop1.setFmvStart(new Double(5.14));
    crop1.setFmvEnd(new Double(5.14));
    crop1.setFmvVariance(new Double(30));
    
    crop1.setAdjQuantityProduced(new Double(10));
    crop1.setReportedQuantityProduced(new Double(72460));
    crop1.setIsEligible(Boolean.TRUE);
    return crop1;
  }
  
  public static CropItem getCropItem6000UnitOther() {
    CropItem crop1 = new CropItem();
    crop1.setInventoryItemCode("6000");
    crop1.setInventoryItemCodeDescription("Wheat");
    crop1.setInventoryClassCode(InventoryClassCodes.CROP);
    crop1.setCropUnitCode("64");
    crop1.setCropUnitCodeDescription("Other");
    
    crop1.setReportedInventoryId(new Integer(50957));
    crop1.setCommodityXrefId(new Integer(1534));
    
    crop1.setReportedQuantityStart(new Double(1));
    crop1.setReportedPriceStart(new Double(200));
    crop1.setReportedQuantityEnd(new Double(2));
    crop1.setReportedPriceEnd(new Double(150));
    
    crop1.setFmvStart(new Double(172));
    crop1.setFmvEnd(new Double(136));
    crop1.setFmvVariance(new Double(30));
    
    crop1.setReportedQuantityProduced(new Double(100));
    crop1.setIsEligible(Boolean.TRUE);
    return crop1;
  }
  
  public static CropItem getCropItem6000UnitTonnes() {
    CropItem crop1 = new CropItem();
    crop1.setInventoryItemCode("6000");
    crop1.setInventoryItemCodeDescription("Wheat");
    crop1.setInventoryClassCode(InventoryClassCodes.CROP);
    crop1.setCropUnitCode("2");
    crop1.setCropUnitCodeDescription("Tonnes");
    
    crop1.setReportedInventoryId(new Integer(51957));
    crop1.setCommodityXrefId(new Integer(1534));
    
    crop1.setReportedQuantityStart(new Double(1));
    crop1.setReportedPriceStart(new Double(200));
    crop1.setReportedQuantityEnd(new Double(2));
    crop1.setReportedPriceEnd(new Double(150));
    
    crop1.setFmvStart(new Double(172));
    crop1.setFmvEnd(new Double(136));
    crop1.setFmvVariance(new Double(30));
    
    crop1.setReportedQuantityProduced(new Double(100));
    crop1.setIsEligible(Boolean.TRUE);
    return crop1;
  }


  /**
   * CropItem for copy forward on save tests - with reported data
   */
  public static CropItem getCropItem5100() {
    CropItem crop1 = new CropItem();
    crop1.setInventoryItemCode("5100");
    crop1.setInventoryItemCodeDescription("Barley");
    crop1.setInventoryClassCode(InventoryClassCodes.CROP);
    crop1.setCropUnitCode("64");
    crop1.setCropUnitCodeDescription("Other");

    crop1.setAdjInventoryId(new Integer(529730));
    crop1.setReportedInventoryId(new Integer(50469));
    crop1.setAdjustedByUserId("IDIR\\AHOPKINS");
    crop1.setRevisionCount(new Integer(1));
    crop1.setCommodityXrefId(new Integer(1764));
    
    crop1.setReportedQuantityStart(null);
    crop1.setReportedPriceStart(null);
    crop1.setReportedQuantityEnd(null);
    crop1.setReportedPriceEnd(null);

    crop1.setAdjQuantityStart(null);
    crop1.setAdjPriceStart(null);
    crop1.setAdjQuantityEnd(null);
    crop1.setAdjPriceEnd(null);
    
    crop1.setFmvStart(new Double(5.14));
    crop1.setFmvEnd(new Double(5.14));
    crop1.setFmvVariance(new Double(30));
    
    crop1.setAdjQuantityProduced(null);
    crop1.setReportedQuantityProduced(null);
    crop1.setIsEligible(Boolean.TRUE);
    return crop1;
  }
  
  
  /**
   * LivestockItem for UPDATE and DELETE tests - without reported data
   */
  public static LivestockItem getLivestockItem8912() {
    LivestockItem ls1 = new LivestockItem();
    ls1.setInventoryItemCode("8912");
    ls1.setInventoryItemCodeDescription("Goats; Kids");
    ls1.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
    ls1.setIsMarketCommodity(Boolean.TRUE);
    
    ls1.setAdjInventoryId(new Integer(12345));
    ls1.setReportedInventoryId(null);
    ls1.setAdjustedByUserId("IDIR\\AHOPKINS");
    ls1.setRevisionCount(new Integer(2));
    ls1.setCommodityXrefId(new Integer(2040));
    
    ls1.setReportedQuantityStart(null);
    ls1.setReportedPriceStart(null);
    ls1.setReportedQuantityEnd(null);
    ls1.setReportedPriceEnd(null);

    ls1.setAdjQuantityStart(new Double(20));
    ls1.setAdjPriceStart(new Double(30));
    ls1.setAdjQuantityEnd(new Double(40));
    ls1.setAdjPriceEnd(new Double(50));
    
    ls1.setFmvStart(new Double(5.14));
    ls1.setFmvEnd(new Double(5.14));
    ls1.setFmvVariance(new Double(30));
    
    return ls1;
  }
  
  public static LivestockItem getLivestockItem4005() {
    LivestockItem ls1 = new LivestockItem();
    ls1.setInventoryItemCode("4005");
    ls1.setInventoryItemCodeDescription("Historical Feeder Cattle");
    ls1.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
    ls1.setIsMarketCommodity(Boolean.TRUE);
    
    ls1.setReportedInventoryId(new Integer(52957));
    ls1.setCommodityXrefId(new Integer(1176));
    
    ls1.setReportedQuantityStart(new Double(9));
    ls1.setReportedPriceStart(new Double(8));
    ls1.setReportedQuantityEnd(null);
    ls1.setReportedPriceEnd(null);
    
    ls1.setFmvStart(null);
    ls1.setFmvEnd(null);
    ls1.setFmvVariance(null);
    
    return ls1;
  }

  public static LivestockItem getLivestockItem4001() {
    LivestockItem ls1 = new LivestockItem();
    ls1.setInventoryItemCode("4001");
    ls1.setInventoryItemCodeDescription("Livestock Inventory Default");
    ls1.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
    ls1.setIsMarketCommodity(Boolean.TRUE);
    
    ls1.setReportedInventoryId(new Integer(53957));
    ls1.setCommodityXrefId(new Integer(1172));
    
    ls1.setReportedQuantityStart(new Double(5));
    ls1.setReportedPriceStart(new Double(6));
    ls1.setReportedQuantityEnd(new Double(7));
    ls1.setReportedPriceEnd(new Double(8));
    
    ls1.setFmvStart(null);
    ls1.setFmvEnd(null);
    ls1.setFmvVariance(null);
    
    return ls1;
  }
  
  public static LivestockItem getLivestockItem7600() {
    LivestockItem ls1 = new LivestockItem();
    ls1.setInventoryItemCode("7600");
    ls1.setInventoryItemCodeDescription("Beeswax");
    ls1.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
    ls1.setIsMarketCommodity(Boolean.TRUE);
    
    ls1.setReportedInventoryId(new Integer(54957));
    ls1.setCommodityXrefId(new Integer(1898));
    
    ls1.setReportedQuantityStart(null);
    ls1.setReportedPriceStart(null);
    ls1.setReportedQuantityEnd(new Double(30));
    ls1.setReportedPriceEnd(new Double(40));
    
    ls1.setFmvStart(null);
    ls1.setFmvEnd(null);
    ls1.setFmvVariance(null);
    
    return ls1;
  }
  
  /**
   * LivestockItem for copy forward on save tests - without reported data
   */
  public static LivestockItem getLivestockItem7781() {
    LivestockItem ls1 = new LivestockItem();
    ls1.setInventoryItemCode("7781");
    ls1.setInventoryItemCodeDescription("Ostrich; Cull; Roosters");
    ls1.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
    ls1.setIsMarketCommodity(Boolean.FALSE);
    
    ls1.setReportedInventoryId(new Integer(37957));
    ls1.setAdjInventoryId(new Integer(37727));
    ls1.setCommodityXrefId(new Integer(1411));
    
    ls1.setAdjQuantityStart(null);
    ls1.setAdjPriceStart(null);
    ls1.setAdjQuantityEnd(new Double(55));
    ls1.setAdjPriceEnd(new Double(23));
    
    ls1.setFmvStart(null);
    ls1.setFmvEnd(null);
    ls1.setFmvVariance(null);
    
    return ls1;
  }
  
  
  public static InputItem getInputItem9661() {
    InputItem ii = new InputItem();
    ii.setInventoryItemCode("9661");
    ii.setInventoryItemCodeDescription("Containers and twine");
    ii.setInventoryClassCode(InventoryClassCodes.INPUT);
    ii.setIsEligible(Boolean.TRUE);
    
    ii.setReportedInventoryId(new Integer(1456));
    ii.setCommodityXrefId(new Integer(2081));
    
    ii.setReportedStartOfYearAmount(new Double(100));
    ii.setReportedEndOfYearAmount(new Double(200));
    
    return ii;
  }
  
  public static InputItem getInputItem9662() {
    InputItem ii = new InputItem();
    ii.setInventoryItemCode("9662");
    ii.setInventoryItemCodeDescription("Fertilizers and lime");
    ii.setInventoryClassCode(InventoryClassCodes.INPUT);
    ii.setIsEligible(Boolean.TRUE);

    ii.setReportedInventoryId(new Integer(2456));
    ii.setAdjInventoryId(new Integer(4456));
    ii.setCommodityXrefId(new Integer(2081));
    
    ii.setReportedStartOfYearAmount(null);
    ii.setReportedEndOfYearAmount(new Double(420));
    
    ii.setAdjStartOfYearAmount(new Double(25));
    ii.setAdjEndOfYearAmount(new Double(75));
    
    return ii;
  }
  
  public static InputItem getInputItem50() {
    InputItem ii = new InputItem();
    ii.setInventoryItemCode("50");
    ii.setInventoryItemCodeDescription("Safflower");
    ii.setInventoryClassCode(InventoryClassCodes.INPUT);
    ii.setIsEligible(Boolean.TRUE);
    
    ii.setCommodityXrefId(new Integer(252));
    
    return ii;
  }
  
  public static ReceivableItem getReceivableItem132() {
    ReceivableItem ii = new ReceivableItem();
    ii.setInventoryItemCode("132");
    ii.setInventoryItemCodeDescription("Bedding Plants");
    ii.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
    ii.setIsEligible(Boolean.TRUE);
    
    ii.setReportedInventoryId(new Integer(3456));
    ii.setCommodityXrefId(new Integer(2081));
    
    ii.setReportedStartOfYearAmount(new Double(910));
    ii.setReportedEndOfYearAmount(null);
    
    return ii;
  }
  
  public static PayableItem getPayableItem9663() {
    PayableItem ii = new PayableItem();
    ii.setInventoryItemCode("9663");
    ii.setInventoryItemCodeDescription("Pesticides");
    ii.setInventoryClassCode(InventoryClassCodes.PAYABLE);
    ii.setIsEligible(Boolean.TRUE);
    
    ii.setReportedInventoryId(new Integer(4456));
    ii.setCommodityXrefId(new Integer(2081));
    
    ii.setReportedStartOfYearAmount(new Double(1000));
    ii.setReportedEndOfYearAmount(new Double(2000));
    
    return ii;
  }
  
  public static PayableItem getPayableItem8() {
    PayableItem ii = new PayableItem();
    ii.setInventoryItemCode("8");
    ii.setInventoryItemCodeDescription("Canary Seed");
    ii.setInventoryClassCode(InventoryClassCodes.PAYABLE);
    ii.setIsEligible(Boolean.TRUE);
    
    ii.setReportedInventoryId(new Integer(5456));
    ii.setCommodityXrefId(new Integer(2081));
    
    ii.setReportedStartOfYearAmount(new Double(250));
    ii.setReportedEndOfYearAmount(null);
    
    return ii;
  }
  
  public static PayableItem getPayableItem9714() {
    PayableItem ii = new PayableItem();
    ii.setInventoryItemCode("9714");
    ii.setInventoryItemCodeDescription("Mineral and salts");
    ii.setInventoryClassCode(InventoryClassCodes.PAYABLE);
    ii.setIsEligible(Boolean.TRUE);
    
    ii.setReportedInventoryId(null);
    ii.setAdjInventoryId(new Integer(8497));
    ii.setCommodityXrefId(new Integer(2090));

    ii.setReportedStartOfYearAmount(null);
    ii.setReportedEndOfYearAmount(null);
    
    ii.setAdjStartOfYearAmount(new Double(236));
    ii.setAdjEndOfYearAmount(new Double(139));
    
    return ii;
  }

  
  public static LineItem getLineItem132() {
    LineItem li = new LineItem();
    li.setLineItemId(new Integer(30398));
    li.setLineItem(new Integer(132));
    li.setDescription("Bedding plants");
    li.setIsEligible(Boolean.TRUE);
    li.setIsContractWork(Boolean.FALSE);
    li.setIsManualExpense(Boolean.FALSE);
    li.setIsProgramPayment(Boolean.FALSE);
    li.setIsYardage(Boolean.FALSE);
    li.setIsSupplyManagedCommodity(Boolean.FALSE);
    li.setProgramYear(new Integer(2009));
    li.setRevisionCount(new Integer(1));
    return li;
  }

  
  public static LineItem getLineItem9574() {
    LineItem li = new LineItem();
    li.setLineItemId(new Integer(30398));
    li.setLineItem(new Integer(9574));
    li.setDescription("Resales; rebates; and GST/HST for allowable expenses");
    li.setIsEligible(Boolean.TRUE);
    li.setIsContractWork(Boolean.FALSE);
    li.setIsManualExpense(Boolean.FALSE);
    li.setIsProgramPayment(Boolean.FALSE);
    li.setIsYardage(Boolean.FALSE);
    li.setIsSupplyManagedCommodity(Boolean.FALSE);
    li.setProgramYear(new Integer(2009));
    li.setRevisionCount(new Integer(1));
    return li;
  }

  
  private static IncomeExpense getIncomeExpense132() {
    IncomeExpense ie = new IncomeExpense();
    LineItem li = getLineItem132();
    ie.setLineItem(li);
    ie.setIsExpense(Boolean.FALSE);
    
    return ie;
  }

  
  private static IncomeExpense getIncomeExpense9574() {
    IncomeExpense ie = new IncomeExpense();
    LineItem li = getLineItem9574();
    ie.setLineItem(li);
    ie.setIsExpense(Boolean.FALSE);
    
    return ie;
  }

  public static IncomeExpense getIncome132() {
    IncomeExpense ie = getIncomeExpense132();
    ie.setReportedAmount(new Double(16000));
    ie.setReportedIncomeExpenseId(new Integer(2234));
    ie.setIsExpense(Boolean.FALSE);
    return ie;
  }
  
  public static IncomeExpense getExpense132() {
    IncomeExpense ie = getIncomeExpense132();
    ie.setReportedAmount(new Double(20000));
    ie.setReportedIncomeExpenseId(new Integer(3234));
    ie.setIsExpense(Boolean.TRUE);
    return ie;
  }

  public static IncomeExpense getIncome9574() {
    IncomeExpense ie = getIncomeExpense9574();
    ie.setReportedIncomeExpenseId(new Integer(4234));
    ie.setReportedAmount(new Double(-4000));
    ie.setIsExpense(Boolean.FALSE);
    return ie;
  }
  
  public static IncomeExpense getExpense9574() {
    IncomeExpense ie = getIncomeExpense9574();
    ie.setReportedAmount(new Double(5000));
    ie.setReportedIncomeExpenseId(new Integer(5234));
    ie.setIsExpense(Boolean.TRUE);
    return ie;
  }
  
  public static ProductiveUnitCapacity getPuc6949() {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setInventoryItemCode("6949");
    puc.setInventoryItemCodeDescription("Flowers, Fresh Cut, Greenhouse");
    puc.setReportedAmount(new Double(2738.320d));
    puc.setReportedProductiveUnitCapacityId(new Integer(8753));
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }
  
  public static ProductiveUnitCapacity getPuc104() {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setStructureGroupCode("104");
    puc.setStructureGroupCodeDescription("Cattle");
    puc.setReportedAmount(new Double(10));
    puc.setReportedProductiveUnitCapacityId(new Integer(9953));
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }
  
  public static ProductiveUnitCapacity getPuc113() {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setStructureGroupCode("113");
    puc.setStructureGroupCodeDescription("Dairy Quota, Butterfat");
    puc.setReportedAmount(new Double(200));
    puc.setReportedProductiveUnitCapacityId(new Integer(2453));
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }
  
  public static ProductiveUnitCapacity getPuc145() {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setStructureGroupCode("145");
    puc.setStructureGroupCodeDescription("Hogs, Farrowing");
    puc.setReportedAmount(new Double(0));
    puc.setReportedProductiveUnitCapacityId(new Integer(2453));
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }

}
