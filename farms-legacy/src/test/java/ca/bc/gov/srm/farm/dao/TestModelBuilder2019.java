/**
 * Copyright (c) 2018,
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
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
 * @author awilkinson
 * @created Dec 6, 2018
 */
public final class TestModelBuilder2019 {

  private static final int PARTICIPANT_PIN = 631986734;
  
  private static int id = 1;
  
  private static Map<String, String> pucs = new HashMap<>();
  static {
    pucs.put("5060", "Blueberries; High bush (Non-Bearing Year)");
    pucs.put("5061", "Blueberries; High bush (Years 1 to 2 of Production)");
    pucs.put("5062", "Blueberries; High bush (Years 3 to 6 of Production)");
    pucs.put("5063", "Blueberries; High bush (Years 7 to 9 of Production)");
    pucs.put("5064", "Blueberries; High bush (Years 10+ of Production)");
  }
  
  /**
   * private constructor because this is a utility class
   */
  private TestModelBuilder2019() {
  }
  
  public static Scenario getScenario(int programYear) {
    int pin = PARTICIPANT_PIN;
    Scenario s = new Scenario();
    
    Client client = getClient();
    client.setParticipantPin(new Integer(pin));
    s.setClient(client);
    
    s.setYear(new Integer(programYear));
    s.setScenarioNumber(new Integer(2));
    s.setRevisionCount(1);
    s.setScenarioStateCode("IP");
    s.setScenarioStateCodeDescription("In Progress");
    s.setAssignedToUserId("IDIR\\AHOPKINS");
    s.setAssignedToUserGuid("AHOPKINS000000000000000000000000");
    s.setIsDefaultInd(true);
    s.setWhenUpdatedTimestamp(DateUtils.getTimestamp(programYear, 6, 5, 0, 0, 0, 0));
    s.setScenarioMetaDataList(getScenarioMetaDataList(programYear));
    
    {
      FarmingYear farmingYear = getFarmingYear(programYear);
      s.setFarmingYear(farmingYear);
    }
    
    return s;
  }
  
  /**
   * @return ReferenceScenario
   */
  public static ReferenceScenario getReferenceScenario(int year) {
    ReferenceScenario r = new ReferenceScenario();
    
    FarmingYear farmingYear = getFarmingYear(year);
    r.setFarmingYear(farmingYear);
    r.setScenarioId(nextId());
    r.setScenarioNumber(new Integer(2));
    r.setScenarioTypeCode(ScenarioTypeCodes.REF);
    r.setScenarioTypeCodeDescription("Reference");
    r.setIsDefaultInd(false);
    r.setScenarioDate(new Date());
    r.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<FarmingOperation> operations = new ArrayList<>();
    farmingYear.setFarmingOperations(operations);

    
    return r;
  }
  
  /**
   * @return Client
   */
  public static Client getClient() {
    Client c = new Client();
    c.setClientId(nextId());
    c.setFederalIdentifier("631986734");
    c.setParticipantClassCode("1");
    c.setParticipantClassCodeDescription("Individual");
    c.setOwner(getOwnerPerson());
    c.setContact(getContactPerson());
    c.setRevisionCount(1);
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
    p.setPersonId(nextId());
    p.setPostalCode("V0J3A1");
    p.setProvinceState("BC");
    p.setRevisionCount(1);
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
    p.setPersonId(nextId());
    p.setPostalCode("R0L1P0");
    p.setProvinceState("MB");
    p.setRevisionCount(1);
    
    return p;
  }
  
  
  public static FarmingYear getFarmingYear(int year) {
    FarmingYear f = new FarmingYear();

    f.setProgramYearVersionId(nextId());
    f.setProgramYearId(nextId());
    f.setProgramYearVersionNumber(new Integer(2));
    f.setRevisionCount(1);
    f.setIsLocallyUpdated(true);

    f.setWholeFarmParticipants(getWholeFarmParticipant());

    f.setAgristabFedStsCode(1);
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
    f.setPostMarkDate(DateUtils.getTimestamp(year-1, 2, 10, 0, 0, 0, 0));
    f.setCraStatementAReceivedDate(null);
    
    // Booleans
    f.setIsAccrualCashConversion(false);
    f.setIsAccrualWorksheet(false);
    f.setIsCanSendCobToRep(false);
    f.setIsCombinedFarm(false);
    f.setIsCompletedProdCycle(false);
    f.setIsCoopMember(false);
    f.setIsCorporateShareholder(false);
    f.setIsCwbWorksheet(false);
    f.setIsDisaster(false);
    f.setIsLastYearFarming(false);
    f.setIsPartnershipMember(false);
    f.setIsPerishableCommodities(false);
    f.setIsReceipts(false);
    f.setIsSoleProprietor(false);

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
   * @return FarmingOperation
   */
  public static FarmingOperation getFarmingOperation(int year, String schedule, int operationNumber) {
    FarmingOperation f = new FarmingOperation();
    f.setAccountingCode("2");
    f.setAccountingCodeDescription("Cash");
    f.setPartnershipName("KENNETH & DARLENE ISAAC");
    
    // Integers
    f.setFarmingOperationId(nextId());
    f.setOperationNumber(operationNumber);
    f.setSchedule(schedule);
    f.setPartnershipPin(1);
    f.setRevisionCount(1);

    // Doubles
    f.setBusinessUseHomeExpense(0.0d);
    f.setFarmingExpenses(160814d);
    f.setGrossIncome(96826d);
    f.setInventoryAdjustments(59000d);
    f.setNetFarmIncome(3093d);
    f.setNetIncomeAfterAdj(3093d);
    f.setNetIncomeBeforeAdj(63988d);
    f.setOtherDeductions(0.0d);
    f.setPartnershipPercent(0.50d);

    // Booleans
    f.setIsCropDisaster(false);
    f.setIsCropShare(true);
    f.setIsFeederMember(true);
    f.setIsLandlord(false);
    f.setIsLivestockDisaster(false);
    f.setIsLocallyUpdated(false);
    f.setIsLocallyGenerated(false);
    
    // Dates
    f.setFiscalYearStart(DateUtils.getTimestamp(year, 0, 1, 0, 0, 0, 0));
    final int december = 11;
    f.setFiscalYearEnd(DateUtils.getTimestamp(year, december, 31, 0, 0, 0, 0));

    return f;
  }

  /**
   * @return List
   */
  public static List<ProductionInsurance> getProductionInsurances1() {
    List<ProductionInsurance> list = new ArrayList<>();
    ProductionInsurance pi1 = new ProductionInsurance();
    pi1.setIsLocallyUpdated(false);
    pi1.setProductionInsuranceId(nextId());
    pi1.setProductionInsuranceNumber("125534 06");
    pi1.setRevisionCount(1);
    list.add(pi1);
    return list;
  }

  /**
   * @return List
   */
  public static List<FarmingOperationPartner> getFarmingOperationPartners() {
    List<FarmingOperationPartner> list = new ArrayList<>();
    return list;
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
    m.setProgramYearVersion(1);
    m.setScenarioNumber(1);
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
    m.setProgramYearVersion(1);
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


  public static CropItem getCropItem6000UnitOther() {
    CropItem crop1 = new CropItem();
    crop1.setInventoryItemCode("6000");
    crop1.setInventoryItemCodeDescription("Wheat");
    crop1.setInventoryClassCode(InventoryClassCodes.CROP);
    crop1.setCropUnitCode("64");
    crop1.setCropUnitCodeDescription("Other");
    
    crop1.setReportedInventoryId(new Integer(50957));
    crop1.setCommodityXrefId(new Integer(1534));
    
    crop1.setIsEligible(true);
    return crop1;
  }
  
  public static CropItem getCropItem6000UnitTonnes() {
    CropItem crop1 = new CropItem();
    crop1.setInventoryItemCode("6000");
    crop1.setInventoryItemCodeDescription("Wheat");
    crop1.setInventoryClassCode(InventoryClassCodes.CROP);
    crop1.setCropUnitCode("2");
    crop1.setCropUnitCodeDescription("Tonnes");
    
    crop1.setReportedInventoryId(51957);
    crop1.setCommodityXrefId(1534);
    
    crop1.setIsEligible(true);
    return crop1;
  }


  public static CropItem getCropItem5100() {
    CropItem crop1 = new CropItem();
    crop1.setInventoryItemCode("5100");
    crop1.setInventoryItemCodeDescription("Barley");
    crop1.setInventoryClassCode(InventoryClassCodes.CROP);
    crop1.setCropUnitCode("64");
    crop1.setCropUnitCodeDescription("Other");

    crop1.setAdjInventoryId(529730);
    crop1.setReportedInventoryId(50469);
    crop1.setAdjustedByUserId("IDIR\\AHOPKINS");
    crop1.setRevisionCount(1);
    crop1.setCommodityXrefId(1327);
    
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
    ls1.setIsMarketCommodity(true);
    
    ls1.setAdjInventoryId(12345);
    ls1.setReportedInventoryId(null);
    ls1.setAdjustedByUserId("IDIR\\AHOPKINS");
    ls1.setRevisionCount(2);
    ls1.setCommodityXrefId(2040);
    
    ls1.setReportedQuantityStart(null);
    ls1.setReportedPriceStart(null);
    ls1.setReportedQuantityEnd(null);
    ls1.setReportedPriceEnd(null);

    ls1.setAdjQuantityStart(20.0);
    ls1.setAdjPriceStart(30.0);
    ls1.setAdjQuantityEnd(40.0);
    ls1.setAdjPriceEnd(50.0);
    
    ls1.setFmvStart(5.14);
    ls1.setFmvEnd(5.14);
    ls1.setFmvVariance(30.0);
    
    return ls1;
  }
  
  public static LivestockItem getLivestockItem4005() {
    LivestockItem ls1 = new LivestockItem();
    ls1.setInventoryItemCode("4005");
    ls1.setInventoryItemCodeDescription("Historical Feeder Cattle");
    ls1.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
    ls1.setIsMarketCommodity(true);
    
    ls1.setReportedInventoryId(52957);
    ls1.setCommodityXrefId(1176);
    
    return ls1;
  }

  public static LivestockItem getLivestockItem4001() {
    LivestockItem ls1 = new LivestockItem();
    ls1.setInventoryItemCode("4001");
    ls1.setInventoryItemCodeDescription("Livestock Inventory Default");
    ls1.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
    ls1.setIsMarketCommodity(true);
    
    ls1.setReportedInventoryId(53957);
    ls1.setCommodityXrefId(1172);
    
    return ls1;
  }
  
  public static LivestockItem getLivestockItem7600() {
    LivestockItem ls1 = new LivestockItem();
    ls1.setInventoryItemCode("7600");
    ls1.setInventoryItemCodeDescription("Beeswax");
    ls1.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
    ls1.setIsMarketCommodity(true);
    
    ls1.setReportedInventoryId(54957);
    ls1.setCommodityXrefId(1898);
    
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
    ls1.setIsMarketCommodity(false);
    
    ls1.setReportedInventoryId(37957);
    ls1.setAdjInventoryId(37727);
    ls1.setCommodityXrefId(1411);
    
    return ls1;
  }
  
  
  public static InputItem getInputItem9661() {
    InputItem ii = new InputItem();
    ii.setInventoryItemCode("9661");
    ii.setInventoryItemCodeDescription("Containers and twine");
    ii.setInventoryClassCode(InventoryClassCodes.INPUT);
    ii.setIsEligible(true);
    
    ii.setReportedInventoryId(1456);
    ii.setCommodityXrefId(2081);
    
    return ii;
  }
  
  public static InputItem getInputItem9662() {
    InputItem ii = new InputItem();
    ii.setInventoryItemCode("9662");
    ii.setInventoryItemCodeDescription("Fertilizers and lime");
    ii.setInventoryClassCode(InventoryClassCodes.INPUT);
    ii.setIsEligible(true);

    ii.setReportedInventoryId(2456);
    ii.setAdjInventoryId(4456);
    ii.setCommodityXrefId(2083);
    
    return ii;
  }
  
  public static InputItem getInputItem50() {
    InputItem ii = new InputItem();
    ii.setInventoryItemCode("50");
    ii.setInventoryItemCodeDescription("Safflower");
    ii.setInventoryClassCode(InventoryClassCodes.INPUT);
    ii.setIsEligible(true);
    
    ii.setCommodityXrefId(252);
    
    return ii;
  }
  
  public static ReceivableItem getReceivableItem132() {
    ReceivableItem ii = new ReceivableItem();
    ii.setInventoryItemCode("132");
    ii.setInventoryItemCodeDescription("Bedding Plants");
    ii.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
    ii.setIsEligible(true);
    
    ii.setCommodityXrefId(72);
    
    return ii;
  }
  
  public static PayableItem getPayableItem9663() {
    PayableItem ii = new PayableItem();
    ii.setInventoryItemCode("9663");
    ii.setInventoryItemCodeDescription("Pesticides");
    ii.setInventoryClassCode(InventoryClassCodes.PAYABLE);
    ii.setIsEligible(true);
    
    ii.setReportedInventoryId(4456);
    ii.setCommodityXrefId(2084);
    
    return ii;
  }
  
  public static PayableItem getPayableItem8() {
    PayableItem ii = new PayableItem();
    ii.setInventoryItemCode("8");
    ii.setInventoryItemCodeDescription("Canary Seed");
    ii.setInventoryClassCode(InventoryClassCodes.PAYABLE);
    ii.setIsEligible(true);
    
    ii.setReportedInventoryId(5456);
    ii.setCommodityXrefId(163);
    
    return ii;
  }
  
  public static PayableItem getPayableItem9714() {
    PayableItem ii = new PayableItem();
    ii.setInventoryItemCode("9714");
    ii.setInventoryItemCodeDescription("Mineral and salts");
    ii.setInventoryClassCode(InventoryClassCodes.PAYABLE);
    ii.setIsEligible(true);
    
    ii.setReportedInventoryId(null);
    ii.setAdjInventoryId(8497);
    ii.setCommodityXrefId(2090);
    
    return ii;
  }

  
  public static LineItem getLineItem132(int programYear) {
    LineItem li = new LineItem();
    li.setLineItemId(nextId());
    li.setLineItem(132);
    li.setDescription("Bedding plants");
    li.setIsEligible(true);
    li.setIsEligibleRefYears(false);
    li.setIsContractWork(false);
    li.setIsManualExpense(false);
    li.setIsProgramPayment(false);
    li.setIsYardage(false);
    li.setIsSupplyManagedCommodity(false);
    li.setIsExcludeFromRevenueCalculation(false);
    li.setIsIndustryAverageExpense(false);
    li.setProgramYear(programYear);
    li.setRevisionCount(1);
    return li;
  }
  
  
  public static LineItem getLineItem67(int programYear) {
    LineItem li = new LineItem();
    li.setLineItemId(nextId());
    li.setLineItem(67);
    li.setDescription("Blueberries");
    li.setIsEligible(true);
    li.setIsEligibleRefYears(false);
    li.setIsContractWork(false);
    li.setIsManualExpense(false);
    li.setIsProgramPayment(false);
    li.setIsYardage(false);
    li.setIsSupplyManagedCommodity(false);
    li.setIsExcludeFromRevenueCalculation(false);
    li.setIsIndustryAverageExpense(false);
    li.setProgramYear(programYear);
    li.setRevisionCount(1);
    return li;
  }
  
  
  public static LineItem getLineItem426(int programYear) {
    LineItem li = new LineItem();
    li.setLineItemId(nextId());
    li.setLineItem(426);
    li.setDescription("Cost of Production Payment (COP)");
    li.setIsEligible(true);
    li.setIsEligibleRefYears(false);
    li.setIsContractWork(false);
    li.setIsManualExpense(false);
    li.setIsProgramPayment(true);
    li.setIsYardage(false);
    li.setIsSupplyManagedCommodity(false);
    li.setIsExcludeFromRevenueCalculation(false);
    li.setIsIndustryAverageExpense(false);
    li.setProgramYear(programYear);
    li.setRevisionCount(1);
    return li;
  }

  
  public static LineItem getLineItem9574(int programYear) {
    LineItem li = new LineItem();
    li.setLineItemId(nextId());
    li.setLineItem(9574);
    li.setDescription("Resales; rebates; and GST/HST for allowable expenses");
    li.setIsEligible(true);
    li.setIsContractWork(false);
    li.setIsManualExpense(false);
    li.setIsProgramPayment(false);
    li.setIsYardage(false);
    li.setIsSupplyManagedCommodity(false);
    li.setIsExcludeFromRevenueCalculation(false);
    li.setIsIndustryAverageExpense(false);
    li.setProgramYear(programYear);
    li.setRevisionCount(1);
    return li;
  }
  
  public static ProductiveUnitCapacity getPucWithInventoryCode(String inventoryItemCode, double reportedAmount) {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setInventoryItemCode(inventoryItemCode);
    puc.setInventoryItemCodeDescription(pucs.get(inventoryItemCode));
    puc.setReportedAmount(reportedAmount);
    puc.setReportedProductiveUnitCapacityId(nextId());
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }
  
  public static ProductiveUnitCapacity getPucWithStructureGroupCode(String structureGroupCode, double reportedAmount) {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setStructureGroupCode(structureGroupCode);
    puc.setStructureGroupCodeDescription(pucs.get(structureGroupCode));
    puc.setReportedAmount(reportedAmount);
    puc.setReportedProductiveUnitCapacityId(nextId());
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }
  
  public static ProductiveUnitCapacity getPuc6949(double reportedAmount) {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setInventoryItemCode("6949");
    puc.setInventoryItemCodeDescription("Flowers, Fresh Cut, Greenhouse");
    puc.setReportedAmount(reportedAmount);
    puc.setReportedProductiveUnitCapacityId(nextId());
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }
  
  public static ProductiveUnitCapacity getPuc104(double reportedAmount) {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setStructureGroupCode("104");
    puc.setStructureGroupCodeDescription("Cattle");
    puc.setReportedAmount(reportedAmount);
    puc.setReportedProductiveUnitCapacityId(nextId());
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }
  
  public static ProductiveUnitCapacity getPuc113(double reportedAmount) {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setStructureGroupCode("113");
    puc.setStructureGroupCodeDescription("Dairy Quota, Butterfat");
    puc.setReportedAmount(reportedAmount);
    puc.setReportedProductiveUnitCapacityId(nextId());
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }
  
  public static ProductiveUnitCapacity getPuc145(double reportedAmount) {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setStructureGroupCode("145");
    puc.setStructureGroupCodeDescription("Hogs, Farrowing");
    puc.setReportedAmount(reportedAmount);
    puc.setReportedProductiveUnitCapacityId(nextId());
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }

  public static Integer nextId() {
    return Integer.valueOf(id++);
  }
}
