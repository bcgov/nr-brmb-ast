/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.dao.TestModelBuilder;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.diff.AccrualDiff;
import ca.bc.gov.srm.farm.domain.diff.CropDiff;
import ca.bc.gov.srm.farm.domain.diff.FarmingOperationDiff;
import ca.bc.gov.srm.farm.domain.diff.FieldDiff;
import ca.bc.gov.srm.farm.domain.diff.IncomeExpenseDiff;
import ca.bc.gov.srm.farm.domain.diff.LivestockDiff;
import ca.bc.gov.srm.farm.domain.diff.ProductiveUnitDiff;
import ca.bc.gov.srm.farm.domain.diff.ProgramYearVersionDiff;
import ca.bc.gov.srm.farm.message.MessageUtility;
import ca.bc.gov.srm.farm.service.DiffReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author awilkinson
 * @created Mar 21, 2011
 */
public class TestDiffReportService {


  /**
   * Test the matches methods
   */
  @Test
  public final void testMatches() throws Exception {

    // Inventory
    CropItem cropItem1 = TestModelBuilder.getCropItem6949();
    assertTrue(cropItem1.equals(cropItem1));

    CropItem cropItem2 = TestModelBuilder.getCropItem6949();
    cropItem2.setReportedQuantityProduced(new Double(50));
    assertFalse(cropItem1.equals(cropItem2));

    CropItem cropItem3 = TestModelBuilder.getCropItem6949();
    cropItem3.setReportedPriceEnd(new Double(221));
    assertFalse(cropItem1.equals(cropItem3));

    CropItem cropItem4 = TestModelBuilder.getCropItem6949();
    cropItem4.setInventoryItemCode("16");
    cropItem4.setInventoryItemCodeDescription("Unseedable Acres");
    assertFalse(cropItem1.equals(cropItem4));


    LivestockItem livestock1 = TestModelBuilder.getLivestockItem8912();
    assertTrue(livestock1.equals(livestock1));

    LivestockItem livestock2 = TestModelBuilder.getLivestockItem8912();
    livestock2.setReportedPriceStart(new Double(132));
    assertFalse(livestock1.equals(livestock2));

    // Accruals
    InputItem input1 = TestModelBuilder.getInputItem9661();
    assertTrue(input1.equals(input1));

    InputItem input2 = TestModelBuilder.getInputItem9661();
    input2.setReportedStartOfYearAmount(new Double(150));
    input2.setReportedEndOfYearAmount(new Double(250));


    // Income/Expenses
    IncomeExpense ie1 = TestModelBuilder.getIncome132();
    assertTrue(ie1.matches(ie1));

    IncomeExpense ie2 = TestModelBuilder.getIncome132();
    ie2.setIsExpense(Boolean.TRUE);
    assertFalse(ie1.matches(ie2));

    IncomeExpense ie3 = TestModelBuilder.getIncome9574();
    assertFalse(ie1.matches(ie3));

    IncomeExpense ie4 = TestModelBuilder.getExpense132();
    assertTrue(ie4.matches(ie4));

    IncomeExpense ie5 = TestModelBuilder.getExpense9574();
    assertFalse(ie4.matches(ie5));


    // Productive Units
    ProductiveUnitCapacity puc1 = TestModelBuilder.getPuc104();
    assertTrue(puc1.matches(puc1));

    ProductiveUnitCapacity puc2 = TestModelBuilder.getPuc6949();
    assertFalse(puc1.matches(puc2));

    ProductiveUnitCapacity puc3 = TestModelBuilder.getPuc104();
    puc3.setInventoryItemCode("7202");
    puc3.setInventoryItemCodeDescription("Potatoes; Pre-elite");
    assertFalse(puc1.matches(puc3));

    ProductiveUnitCapacity puc4 = TestModelBuilder.getPuc6949();
    puc4.setStructureGroupCode("145");
    puc4.setStructureGroupCodeDescription("Hogs, Farrowing");
    assertFalse(puc2.matches(puc4));

    assertFalse(puc1.matches(puc4));
    assertFalse(puc2.matches(puc3));


    // FieldDiff
    FieldDiff fd1 = new FieldDiff();
    fd1.setFieldName(MessageConstants.FIELD_ACCOUNTING_CODE);
    FieldDiff fd2 = new FieldDiff();
    fd1.setFieldName(MessageConstants.FIELD_ACCRUAL_CASH_CONVERSION);

    assertTrue(fd1.equals(fd1));
    assertFalse(fd1.equals(fd2));
    assertFalse(fd2.equals(fd1));
  }


  /**
   * Test the field diffs for a FarmingYear.
   * Make sure no field diffs are found when diffing the same FarmingYear.
   */
  @Test
  public final void testPyvFieldDiffs1() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario1();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FieldDiff> pyvFieldDiffs = pyvDiff.getFieldDiffs();

    assertEquals(0, pyvFieldDiffs.size());
  }


  /**
   * Test the field diffs for a FarmingYear.
   * Make sure all field differences are found.
   * Also test that isLocallyUpdated is set on ProgramYearVersionDiff.
   */
  @Test
  public final void testPyvFieldDiffs2() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario2();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FieldDiff> pyvFieldDiffs = pyvDiff.getFieldDiffs();

    assertEquals(24, pyvFieldDiffs.size());

    assertNotNull(pyvDiff.getIsLocallyUpdated());
    assertTrue(pyvDiff.getIsLocallyUpdated().booleanValue());

    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_FARM_YEARS, "2", "15");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_FEDERAL_STATUS, "1", "3");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_PROVINCE_OF_RESIDENCE, "BC", "AB");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_PROVINCE_OF_MAIN_FARMSTEAD, "BC", "AB");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_OTHER_TEXT, "other text", "");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_MUNICIPALITY, "51", "35");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_POST_MARK_DATE, "2009-03-10", "2009-11-11");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_RECEIVED_DATE, null, "2009-06-05");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_COMMON_SHARE_TOTAL, "0", "");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_PARTICIPANT_PROFILE, "3", "1");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_SOLE_PROPRIETOR, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_COMPLETED_PROD_CYCLE, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_CORPORATE_SHAREHOLDER, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_DISASTER, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_PARTNERSHIP_MEMBER, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_LAST_YEAR_FARMING, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_CO_OP_MEMBER, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_ACCRUAL_CASH_CONVERSION, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_COMBINED_THIS_YEAR, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_CAN_SEND_COB_TO_REP, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_CWB_WORKSHEET, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_RECEIPT, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_ACCRUAL_WORKSHEET, "N", "Y");
    checkFieldDiffValues(pyvFieldDiffs, MessageConstants.FIELD_PERISHABLE_COMMODITIES, "N", "Y");
  }


  /**
   * @param pyvFieldDiffs
   * @param fieldName
   * @param oldValue
   * @param newValue
   */
  private void checkFieldDiffValues(List<FieldDiff> pyvFieldDiffs, String fieldName, String oldValue, String newValue) {
    MessageUtility msgs = MessageUtility.getInstance();
    FieldDiff fdFind = new FieldDiff();
    fdFind.setFieldName(msgs.getPattern(fieldName));
    int index = pyvFieldDiffs.indexOf(fdFind);
    FieldDiff fd = pyvFieldDiffs.get(index);
    assertEquals(oldValue, fd.getOldValue(), fd.getFieldName() + " old value.");
    assertEquals(newValue, fd.getNewValue(), fd.getFieldName() + " new value.");
  }


  /**
   * Test the field diffs for a FarmingOperation.
   * Make sure no field diffs are found when diffing the same FarmingOperation.
   */
  @Test
  public final void testFarmingOperationFieldDiffs1() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario1();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<FieldDiff> foFieldDiffs1 = foDiff1.getFieldDiffs();
    List<FieldDiff> foFieldDiffs2 = foDiff2.getFieldDiffs();

    assertEquals(0, foFieldDiffs1.size());
    assertEquals(0, foFieldDiffs2.size());
  }


  /**
   * Test the field diffs for a FarmingOperation.
   * Make sure all field differences are found.
   * Also test that isLocallyUpdated is set on FarmingOperationDiff.
   */
  @Test
  public final void testFarmingOperationFieldDiffs2() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario2();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<FieldDiff> foFieldDiffs1 = foDiff1.getFieldDiffs();
    List<FieldDiff> foFieldDiffs2 = foDiff2.getFieldDiffs();

    assertEquals(0, foFieldDiffs1.size());
    assertEquals(20, foFieldDiffs2.size());

    assertNotNull(foDiff1.getIsLocallyUpdated());
    assertNotNull(foDiff2.getIsLocallyUpdated());
    assertFalse(foDiff1.getIsLocallyUpdated().booleanValue());
    assertTrue(foDiff2.getIsLocallyUpdated().booleanValue());

    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_SCHEDULE, "B", "C");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_ACCOUNTING_CODE, "2", "1");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_FISCAL_START, null, "2009-01-01");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_FISCAL_END, null, "2009-12-31");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_CROP_SHARE, "N", "Y");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_FEEDER_MEMBER, "N", "Y");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_PARTNERSHIP_PIN, "1", "20");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_PARTNERSHIP_NAME, "Friends and I", "My Friends and Me");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_PARTNERSHIP_PERCENT, "0.0", "0.03");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_CROP_DISASTER, "N", "Y");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_LIVESTOCK_DISASTER, "N", "Y");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_LANDLORD, "N", "Y");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_EXPENSES, "150000.0", "160814.0");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_BUSINESS_USE_HOME_EXPENSE, "0.0", "1.0");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_OTHER_DEDUCTIONS, "0.0", "2.0");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_INVENTORY_ADJUSTMENTS, "60000.0", "59000.0");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_GROSS_INCOME, "95000.0", "96826.0");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_NET_FARM_INCOME, "4000.0", "3093.0");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_NET_INCOME_BEFORE_ADJ, "65000.0", "63988.0");
    checkFieldDiffValues(foFieldDiffs2, MessageConstants.FIELD_NET_INCOME_AFTER_ADJ, "4000.0", "3093.0");
  }


  /**
   * Test case where there is one more FarmingOperation in the new data than the old.
   */
  @Test
  public final void testFarmingOperationAdded() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario1();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();
    
    List<FarmingOperation> latestFOs = latestFarmingYear.getFarmingOperations();
    assertEquals(2, latestFOs.size());
    FarmingOperation addedFO = TestModelBuilder.getFarmingOperation3(latestFarmingYear);
    addedFO.setOperationNumber(new Integer(3));
    addedFO.setSchedule("C");
    latestFOs.add(addedFO);

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(3, foDiffs.size());

    FarmingOperationDiff foDiff3 = foDiffs.get(2);

    List<FieldDiff> foFieldDiffs3 = foDiff3.getFieldDiffs();

    assertNotNull(foFieldDiffs3);
    assertEquals(0, foFieldDiffs3.size());

    assertNotNull(foDiff3.getOldOpExists());
    assertNotNull(foDiff3.getNewOpExists());
    assertFalse(foDiff3.getOldOpExists().booleanValue());
    assertTrue(foDiff3.getNewOpExists().booleanValue());
  }


  /**
   * Test case where there is one fewer FarmingOperation in the new data than the old.
   */
  @Test
  public final void testFarmingOperationRemoved() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario1();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();
    
    List<FarmingOperation> latestFOs = latestFarmingYear.getFarmingOperations();
    assertEquals(2, latestFOs.size());
    latestFOs.remove(1);

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<FieldDiff> foFieldDiffs2 = foDiff2.getFieldDiffs();

    assertNotNull(foFieldDiffs2);
    assertEquals(0, foFieldDiffs2.size());

    assertNotNull(foDiff2.getOldOpExists());
    assertNotNull(foDiff2.getNewOpExists());
    assertTrue(foDiff2.getOldOpExists().booleanValue());
    assertFalse(foDiff2.getNewOpExists().booleanValue());
  }


  /**
   * Test the lists ProductiveUnitDiff for each FarmingOperation.
   * Make no differences are found when diffing the same farming operations.
   */
  @Test
  public final void testProductiveUnitDiffs1() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario1();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<ProductiveUnitDiff> pucDiffs1 = foDiff1.getProductiveUnitDiffs();
    List<ProductiveUnitDiff> pucDiffs2 = foDiff2.getProductiveUnitDiffs();

    assertNotNull(pucDiffs1);
    assertNotNull(pucDiffs2);
    assertEquals(0, pucDiffs1.size());
    assertEquals(0, pucDiffs2.size());
  }


  /**
   * Test the lists ProductiveUnitDiff for each FarmingOperation.
   * Make sure all field differences are found.
   */
  @Test
  public final void testProductiveUnitDiffs2() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario2();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<ProductiveUnitDiff> pucDiffs1 = foDiff1.getProductiveUnitDiffs();
    List<ProductiveUnitDiff> pucDiffs2 = foDiff2.getProductiveUnitDiffs();

    assertNotNull(pucDiffs1);
    assertNotNull(pucDiffs2);
    assertEquals(0, pucDiffs1.size());
    assertEquals(3, pucDiffs2.size());

    int checkedDiffCount = 0;
    for(ProductiveUnitDiff d : pucDiffs2) {
      assertNotNull(d);
      assertNotNull(d.getCode());
      String info = "code: " + d.getCode();
      if(d.getCode().equals("6949")) {
        checkedDiffCount++;
        assertEquals(Double.valueOf(2738.320d), d.getOldValue(), info);
        assertEquals(new Double(2838.513d), d.getNewValue(), info);
      } else if(d.getCode().equals("104")) {
        checkedDiffCount++;
        assertEquals(new Double(10d), d.getOldValue(), info);
        assertEquals(null, d.getNewValue(), info);
      } else if(d.getCode().equals("113")) {
        checkedDiffCount++;
        assertEquals(null, d.getOldValue(), info);
        assertEquals(new Double(200d), d.getNewValue(), info);
      }
    }
    assertEquals(3, checkedDiffCount);
  }


  /**
   * Test the lists IncomeExpenseDiff for each FarmingOperation.
   * Make no differences are found when diffing the same farming operations.
   */
  @Test
  public final void testIncomeExpenseDiffs1() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario1();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<IncomeExpenseDiff> ieDiffs1 = foDiff1.getIncomeExpenseDiffs();
    List<IncomeExpenseDiff> ieDiffs2 = foDiff2.getIncomeExpenseDiffs();

    assertNotNull(ieDiffs1);
    assertNotNull(ieDiffs2);
    assertEquals(0, ieDiffs1.size());
    assertEquals(0, ieDiffs2.size());
  }


  /**
   * Test the lists IncomeExpenseDiff for each FarmingOperation.
   * Make sure all field differences are found.
   */
  @Test
  public final void testIncomeExpenseDiffs2() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario2();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<IncomeExpenseDiff> ieDiffs1 = foDiff1.getIncomeExpenseDiffs();
    List<IncomeExpenseDiff> ieDiffs2 = foDiff2.getIncomeExpenseDiffs();

    assertNotNull(ieDiffs1);
    assertNotNull(ieDiffs2);
    assertEquals(0, ieDiffs1.size());
    assertEquals(3, ieDiffs2.size());

    int checkedDiffCount = 0;
    for(IncomeExpenseDiff d : ieDiffs2) {
      assertNotNull(d);
      assertNotNull(d.getCode());
      assertNotNull(d.getIsExpense());
      boolean isExpense = d.getIsExpense().booleanValue();
      String info = "code: " + d.getCode() + ", isExpense: " + isExpense;

      if(d.getCode().equals("132") && ! isExpense) {
        checkedDiffCount++;
        assertEquals(new Double(16000), d.getOldValue(), info);
        assertEquals(new Double(17000), d.getNewValue(), info);
      } else if(d.getCode().equals("9574") && ! isExpense) {
        checkedDiffCount++;
        assertEquals(new Double(-4000), d.getOldValue(), info);
        assertEquals(null, d.getNewValue(), info);
      } else if(d.getCode().equals("9574") && isExpense) {
        checkedDiffCount++;
        assertEquals(null, d.getOldValue(), info);
        assertEquals(new Double(5000), d.getNewValue(), info);
      }
    }
    assertEquals(3, checkedDiffCount);
  }


  /**
   * Test the lists AccrualDiff for each FarmingOperation.
   * Make no differences are found when diffing the same farming operations.
   */
  @Test
  public final void testAccrualDiffs1() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario1();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<AccrualDiff> inputDiffs1 = foDiff1.getInputDiffs();
    List<AccrualDiff> inputDiffs2 = foDiff2.getInputDiffs();
    List<AccrualDiff> receivableDiffs1 = foDiff1.getReceivableDiffs();
    List<AccrualDiff> receivableDiffs2 = foDiff2.getReceivableDiffs();
    List<AccrualDiff> payableDiffs1 = foDiff1.getPayableDiffs();
    List<AccrualDiff> payableDiffs2 = foDiff2.getPayableDiffs();

    assertNotNull(inputDiffs1);
    assertNotNull(inputDiffs2);
    assertNotNull(receivableDiffs1);
    assertNotNull(receivableDiffs2);
    assertNotNull(payableDiffs1);
    assertNotNull(payableDiffs2);

    assertEquals(0, inputDiffs1.size());
    assertEquals(0, inputDiffs2.size());
    assertEquals(0, receivableDiffs1.size());
    assertEquals(0, receivableDiffs2.size());
    assertEquals(0, payableDiffs1.size());
    assertEquals(0, payableDiffs2.size());
  }


  /**
   * Test the lists AccrualDiff for each FarmingOperation.
   * Make sure all field differences are found.
   */
  @Test
  public final void testAccrualDiffs2() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario2();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<AccrualDiff> inputDiffs1 = foDiff1.getInputDiffs();
    List<AccrualDiff> inputDiffs2 = foDiff2.getInputDiffs();
    List<AccrualDiff> receivableDiffs1 = foDiff1.getReceivableDiffs();
    List<AccrualDiff> receivableDiffs2 = foDiff2.getReceivableDiffs();
    List<AccrualDiff> payableDiffs1 = foDiff1.getPayableDiffs();
    List<AccrualDiff> payableDiffs2 = foDiff2.getPayableDiffs();

    assertNotNull(inputDiffs1);
    assertNotNull(inputDiffs2);
    assertNotNull(receivableDiffs1);
    assertNotNull(receivableDiffs2);
    assertNotNull(payableDiffs1);
    assertNotNull(payableDiffs2);

    assertEquals(0, inputDiffs1.size());
    assertEquals(3, inputDiffs2.size());
    assertEquals(0, receivableDiffs1.size());
    assertEquals(1, receivableDiffs2.size());
    assertEquals(0, payableDiffs1.size());
    assertEquals(2, payableDiffs2.size());

    final String ADDED = MessageConstants.ACTION_ADDED;
    final String REMOVED = MessageConstants.ACTION_REMOVED;


    int checkedDiffCount = 0;
    for(AccrualDiff d : inputDiffs2) {
      String code = d.getCode();
      String action = d.getAction();
      assertNotNull(d);
      assertNotNull(code);
      assertNotNull(action);
      String info = "code: " + code;
      if(code.equals("9661") && action.equals(REMOVED)) {
        checkedDiffCount++;
        assertEquals(new Double(100), d.getStartValue(), info);
        assertEquals(new Double(200), d.getEndValue(), info);
      } else if(code.equals("9661") && action.equals(ADDED)) {
        checkedDiffCount++;
        assertEquals(new Double(300), d.getStartValue(), info);
        assertEquals(new Double(400), d.getEndValue(), info);
      } else if(code.equals("9662") && action.equals(ADDED)) {
        checkedDiffCount++;
        assertEquals(null, d.getStartValue(), info);
        assertEquals(new Double(420), d.getEndValue(), info);
      }
    }
    assertEquals(3, checkedDiffCount);


    checkedDiffCount = 0;
    for(AccrualDiff d : receivableDiffs2) {
      String code = d.getCode();
      String action = d.getAction();
      assertNotNull(d);
      assertNotNull(code);
      assertNotNull(action);
      String info = "code: " + code;
      if(code.equals("132") && action.equals(REMOVED)) {
        checkedDiffCount++;
        assertEquals(new Double(910), d.getStartValue(), info);
        assertEquals(null, d.getEndValue(), info);
      }
    }
    assertEquals(1, checkedDiffCount);


    checkedDiffCount = 0;
    for(AccrualDiff d : payableDiffs2) {
      String code = d.getCode();
      String action = d.getAction();
      assertNotNull(d);
      assertNotNull(code);
      assertNotNull(action);
      String info = "code: " + code;
      if(code.equals("8") && action.equals(ADDED)) {
        checkedDiffCount++;
        assertEquals(new Double(250), d.getStartValue(), info);
        assertEquals(new Double(250), d.getEndValue(), info);
      } else if(code.equals("8") && action.equals(REMOVED)) {
        checkedDiffCount++;
        assertEquals(new Double(250), d.getStartValue(), info);
        assertEquals(null, d.getEndValue(), info);
      }
    }
    assertEquals(2, checkedDiffCount);

  }


  /**
   * Test the lists CropDiff and LivestockDiff for each FarmingOperation.
   * Make no differences are found when diffing the same farming operations.
   */
  @Test
  public final void testInventoryDiffs1() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario1();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<CropDiff> cropDiffs1 = foDiff1.getCropDiffs();
    List<CropDiff> cropDiffs2 = foDiff2.getCropDiffs();
    
    List<LivestockDiff> livestockDiffs1 = foDiff1.getLivestockDiffs();
    List<LivestockDiff> livestockDiffs2 = foDiff2.getLivestockDiffs();

    assertNotNull(cropDiffs1);
    assertNotNull(cropDiffs2);
    assertNotNull(livestockDiffs1);
    assertNotNull(livestockDiffs2);

    assertEquals(0, cropDiffs1.size());
    assertEquals(0, cropDiffs2.size());
    assertEquals(0, livestockDiffs1.size());
    assertEquals(0, livestockDiffs2.size());
  }


  /**
   * Test the lists CropDiff and LivestockDiff for each FarmingOperation.
   * Make sure all field differences are found.
   */
  @Test
  public final void testInventoryDiffs2() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    Scenario scenario2 = TestModelBuilder.getScenario2();
    
    FarmingYear curFarmingYear = scenario1.getFarmingYear();
    FarmingYear latestFarmingYear = scenario2.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);
    assertEquals(2, foDiffs.size());

    FarmingOperationDiff foDiff1 = foDiffs.get(0);
    FarmingOperationDiff foDiff2 = foDiffs.get(1);

    List<CropDiff> cropDiffs1 = foDiff1.getCropDiffs();
    List<CropDiff> cropDiffs2 = foDiff2.getCropDiffs();
    
    List<LivestockDiff> livestockDiffs1 = foDiff1.getLivestockDiffs();
    List<LivestockDiff> livestockDiffs2 = foDiff2.getLivestockDiffs();

    assertNotNull(cropDiffs1);
    assertNotNull(cropDiffs2);
    assertNotNull(livestockDiffs1);
    assertNotNull(livestockDiffs2);

    assertEquals(0, cropDiffs1.size());
    assertEquals(2, cropDiffs2.size());
    assertEquals(0, livestockDiffs1.size());
    assertEquals(4, livestockDiffs2.size());

    final String ADDED = MessageConstants.ACTION_ADDED;
    final String REMOVED = MessageConstants.ACTION_REMOVED;


    int checkedDiffCount = 0;
    for(CropDiff d : cropDiffs2) {
      String code = d.getCode();
      String unit = d.getCropUnitCode();
      String action = d.getAction();
      assertNotNull(d);
      assertNotNull(code);
      assertNotNull(action);
      String info = "code: " + code;
      if(code.equals("6000") && unit.equals("64") && action.equals(REMOVED)) {
        checkedDiffCount++;
        assertEquals(new Double(100), d.getQuantityProduced(), info);
        assertEquals(new Double(1), d.getQuantityStart(), info);
        assertEquals(new Double(200), d.getPriceStart(), info);
        assertEquals(new Double(2), d.getQuantityEnd(), info);
        assertEquals(new Double(150), d.getPriceEnd(), info);
      } else if(code.equals("6000") && unit.equals("2") && action.equals(ADDED)) {
        checkedDiffCount++;
        assertEquals(new Double(100), d.getQuantityProduced());
        assertEquals(new Double(1), d.getQuantityStart(), info);
        assertEquals(new Double(200), d.getPriceStart(), info);
        assertEquals(new Double(2), d.getQuantityEnd(), info);
        assertEquals(new Double(150), d.getPriceEnd(), info);
      }
    }
    assertEquals(2, checkedDiffCount);


    checkedDiffCount = 0;
    for(LivestockDiff d : livestockDiffs2) {
      String code = d.getCode();
      String action = d.getAction();
      assertNotNull(d);
      assertNotNull(code);
      assertNotNull(action);
      String info = "code: " + code;
      if(code.equals("4005") && action.equals(REMOVED)) {
        checkedDiffCount++;
        assertEquals(new Double(9), d.getQuantityStart(), info);
        assertEquals(new Double(8), d.getPriceStart(), info);
        assertEquals(null, d.getQuantityEnd(), info);
        assertEquals(null, d.getPriceEnd(), info);
      } else if(code.equals("4005") && action.equals(ADDED)) {
        checkedDiffCount++;
        assertEquals(new Double(9), d.getQuantityStart(), info);
        assertEquals(new Double(8), d.getPriceStart(), info);
        assertEquals(new Double(7), d.getQuantityEnd(), info);
        assertEquals(new Double(6), d.getPriceEnd(), info);
      } else if(code.equals("4001") && action.equals(REMOVED)) {
        checkedDiffCount++;
        assertEquals(new Double(5), d.getQuantityStart(), info);
        assertEquals(new Double(6), d.getPriceStart(), info);
        assertEquals(new Double(7), d.getQuantityEnd(), info);
        assertEquals(new Double(8), d.getPriceEnd(), info);
      } else if(code.equals("7600") && action.equals(ADDED)) {
        checkedDiffCount++;
        assertEquals(null, d.getQuantityStart(), info);
        assertEquals(null, d.getPriceStart(), info);
        assertEquals(new Double(30), d.getQuantityEnd(), info);
        assertEquals(new Double(40), d.getPriceEnd(), info);
      }
    }
    assertEquals(4, checkedDiffCount);

  }


  /**
   * Test that hasLocallyGeneratedOperations is set if curFarmingYear
   * has any farming operations created by the user.
   */
  @Test
  public final void testLocallyGeneratedOperations() throws Exception {
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    Scenario scenario3 = TestModelBuilder.getScenario3();
    Scenario scenario1 = TestModelBuilder.getScenario1();
    
    FarmingYear curFarmingYear = scenario3.getFarmingYear();
    FarmingYear latestFarmingYear = scenario1.getFarmingYear();

    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    List<FarmingOperationDiff> foDiffs = pyvDiff.getFarmingOperationDiffs();

    assertNotNull(foDiffs);

    // Ensure that locally generated operations have not
    // been included in the operation diffs
    assertEquals(2, foDiffs.size());

    assertNotNull(pyvDiff.getHasLocallyGeneratedOperations());
    assertTrue(pyvDiff.getHasLocallyGeneratedOperations().booleanValue());
  }

}
