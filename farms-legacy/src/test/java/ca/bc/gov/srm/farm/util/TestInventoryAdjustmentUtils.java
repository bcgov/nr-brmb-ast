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
package ca.bc.gov.srm.farm.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.dao.TestModelBuilder;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.ui.struts.calculator.inventory.InventoryForm;
import ca.bc.gov.srm.farm.ui.struts.calculator.inventory.InventoryFormData;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author awilkinson
 * @created Mar 10, 2011
 */
public class TestInventoryAdjustmentUtils {

  private static Logger logger = LoggerFactory.getLogger(TestInventoryAdjustmentUtils.class);


  /**
   * Test save with no changes - no adjustment actions to perform.
   */
  @Test
  public final void testParseAdjustments01() throws Exception {
    Scenario scenario = new Scenario();
    scenario.setYear(2023);
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    InventoryForm form = new InventoryForm();
    FarmingOperation fo = TestModelBuilder.getFarmingOperation1(farmingYear);

    CropItem crop1 = TestModelBuilder.getCropItem6949();
    LivestockItem ls1 = TestModelBuilder.getLivestockItem8912();

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, crop1, true, false);
    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, ls1, true, false);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(0, adjustmentsMap.size());

  }


  /**
   * Test CropItem UPDATE - with reported data
   */
  @Test
  public final void testParseAdjustments02() throws Exception {
    InventoryForm form = new InventoryForm();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario1.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    CropItem crop1 = TestModelBuilder.getCropItem6949();

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, crop1, true, false);

    String lineKey = InventoryFormData.getLineKey(crop1);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("");
    fd1.setTotalPriceStart("5");
    fd1.setTotalQuantityEnd("2400");
    fd1.setTotalPriceEnd("12");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertEquals(1, updateList.size());

    CropItem cropAdj = (CropItem) updateList.get(0);
    assertNotNull(cropAdj);
    assertNotNull(cropAdj.getFarmingOperation());
    assertNotNull(cropAdj.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(-72460), cropAdj.getAdjQuantityProduced());
    assertEquals(null, cropAdj.getAdjQuantityStart());
    assertEquals(new Double(5), cropAdj.getAdjPriceStart());
    assertEquals(new Double(-100), cropAdj.getAdjQuantityEnd());
    assertEquals(new Double(12), cropAdj.getAdjPriceEnd());
  }


  /**
   * Test CropItem DELETE - with reported data
   */
  @Test
  public final void testParseAdjustments03() throws Exception {
    InventoryForm form = new InventoryForm();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario1.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    CropItem crop1 = TestModelBuilder.getCropItem6949();

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, crop1, true, false);

    String lineKey = InventoryFormData.getLineKey(crop1);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setDeleted(true);
    fd1.setTotalQuantityProduced("72460");
    fd1.setTotalQuantityStart("");
    fd1.setTotalPriceStart("");
    fd1.setTotalQuantityEnd("2500");
    fd1.setTotalPriceEnd(null);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertEquals(1, updateList.size());

    CropItem cropAdj = (CropItem) updateList.get(0);
    assertNotNull(cropAdj);
    assertNotNull(cropAdj.getFarmingOperation());
    assertNotNull(cropAdj.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj.getAdjInventoryId());
  }


  /**
   * Test CropItem ADD
   */
  @Test
  public final void testParseAdjustments04() throws Exception {
    InventoryForm form = new InventoryForm();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario1.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    // Note: not populating the form at all.
    // Just adding a new item.

    String lineKey = InventoryFormData.TYPE_NEW + "_" + "00";
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setLineCode("6000");
    fd1.setItemType(InventoryFormData.getCropType());

    fd1.setTotalQuantityProduced("1000");
    fd1.setTotalQuantityStart("100");
    fd1.setTotalPriceStart("200");
    fd1.setTotalQuantityEnd("300");
    fd1.setTotalPriceEnd("400");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertEquals(1, updateList.size());

    CropItem cropAdj = (CropItem) updateList.get(0);
    assertNotNull(cropAdj);
    assertNotNull(cropAdj.getFarmingOperation());
    assertNotNull(cropAdj.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(1000), cropAdj.getAdjQuantityProduced());
    assertEquals(new Double(100), cropAdj.getAdjQuantityStart());
    assertEquals(new Double(200), cropAdj.getAdjPriceStart());
    assertEquals(new Double(300), cropAdj.getAdjQuantityEnd());
    assertEquals(new Double(400), cropAdj.getAdjPriceEnd());
  }


  /**
   * Test Blank Line Code error
   */
  @Test
  public final void testParseAdjustments05() throws Exception {
    InventoryForm form = new InventoryForm();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario1.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    // Note: not populating the form at all.
    // Adding a new item.

    String lineKey = InventoryFormData.TYPE_NEW + "_" + "00";
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setItemType(InventoryFormData.getCropType());

    fd1.setTotalQuantityProduced("1000");
    fd1.setTotalQuantityStart("100");
    fd1.setTotalPriceStart("200");
    fd1.setTotalQuantityEnd("300");
    fd1.setTotalPriceEnd("400");

    ActionMessages errors = new ActionMessages();

    @SuppressWarnings("unused")
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(1, errors.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> messages = errors.get();
    ActionMessage msg = messages.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ERRORS_LINE_CODE_BLANK, msg.getKey());
  }


  /**
   * Bad value error
   */
  @Test
  public final void testParseAdjustments06() throws Exception {
    InventoryForm form = new InventoryForm();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario1.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    // Note: not populating the form at all.
    // Adding a new item.

    String lineKey = InventoryFormData.TYPE_NEW + "_" + "00";
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setLineCode("6000");
    fd1.setItemType(InventoryFormData.getCropType());

    fd1.setTotalQuantityProduced("100000000000"); // value above max, by 1
    fd1.setTotalQuantityStart("-100000000000"); // value below min, by 1
    fd1.setTotalPriceStart("text");
    fd1.setTotalQuantityEnd("text");
    fd1.setTotalPriceEnd("text");

    ActionMessages errors = new ActionMessages();

    @SuppressWarnings("unused")
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(5, errors.size());

    boolean qProd = false;
    boolean qStart = false;
    boolean pStart = false;
    boolean qEnd = false;
    boolean pEnd = false;

    for(@SuppressWarnings("unchecked")
        Iterator<ActionMessage> messages = errors.get(); messages.hasNext(); ) {
      ActionMessage msg = messages.next();
      assertNotNull(msg);

      if(msg.getKey().equals(MessageConstants.ERRORS_ADJUSTED_QUANTITY_PRODUCED)) {
        qProd = true;
      } else if(msg.getKey().equals(MessageConstants.ERRORS_ADJUSTED_QUANTITY_START)) {
        qStart = true;
      } else if(msg.getKey().equals(MessageConstants.ERRORS_ADJUSTED_PRICE_START)) {
        pStart = true;
      } else if(msg.getKey().equals(MessageConstants.ERRORS_ADJUSTED_QUANTITY_END)) {
        qEnd = true;
      } else if(msg.getKey().equals(MessageConstants.ERRORS_ADJUSTED_PRICE_END)) {
        pEnd = true;
      } else {
        assertTrue(false);
      }
    }

    assertEquals(true, qProd);
    assertEquals(true, qStart);
    assertEquals(true, pStart);
    assertEquals(true, qEnd);
    assertEquals(true, pEnd);
  }


  /**
   * Test Missing Units error
   */
  @Test
  public final void testParseAdjustments07() throws Exception {
    InventoryForm form = new InventoryForm();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario1.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    // Note: not populating the form at all.
    // Adding a new item.

    String lineKey = InventoryFormData.TYPE_NEW + "_" + "00";
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setLineCode("6000");
    fd1.setItemType(InventoryFormData.getCropType());
    fd1.setNew(true);

    fd1.setTotalQuantityProduced("1000");
    fd1.setTotalQuantityStart("100");
    fd1.setTotalPriceStart("200");
    fd1.setTotalQuantityEnd("300");
    fd1.setTotalPriceEnd("400");

    ActionMessages errors = new ActionMessages();

    @SuppressWarnings("unused")
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(1, errors.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> messages = errors.get();
    ActionMessage msg = messages.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ERRORS_UNITS_BLANK, msg.getKey());
    assertEquals(true, fd1.isErrorUnits());
  }


  /**
   * Test LivestockItem UPDATE - without reported data
   */
  @Test
  public final void testParseAdjustments08() throws Exception {
    InventoryForm form = new InventoryForm();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario1.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    LivestockItem ls1 = TestModelBuilder.getLivestockItem8912();

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, ls1, true, false);

    String lineKey = InventoryFormData.getLineKey(ls1);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityStart("60");
    fd1.setTotalPriceStart("70");
    fd1.setTotalQuantityEnd("80");
    fd1.setTotalPriceEnd("90");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertEquals(1, updateList.size());

    LivestockItem lsAdj = (LivestockItem) updateList.get(0);
    assertNotNull(lsAdj);
    assertNotNull(lsAdj.getFarmingOperation());
    assertNotNull(lsAdj.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(60), lsAdj.getAdjQuantityStart());
    assertEquals(new Double(70), lsAdj.getAdjPriceStart());
    assertEquals(new Double(80), lsAdj.getAdjQuantityEnd());
    assertEquals(new Double(90), lsAdj.getAdjPriceEnd());
  }


  /**
   * Test LivestockItem DELETE - without reported data
   */
  @Test
  public final void testParseAdjustments09() throws Exception {
    InventoryForm form = new InventoryForm();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario1.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    LivestockItem ls1 = TestModelBuilder.getLivestockItem8912();

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, ls1, true, false);

    String lineKey = InventoryFormData.getLineKey(ls1);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setDeleted(true);
    fd1.setTotalQuantityStart("");
    fd1.setTotalPriceStart("");
    fd1.setTotalQuantityEnd("");
    fd1.setTotalPriceEnd(null);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertEquals(1, updateList.size());

    LivestockItem lsAdj = (LivestockItem) updateList.get(0);
    assertNotNull(lsAdj);
    assertNotNull(lsAdj.getFarmingOperation());
    assertNotNull(lsAdj.getFarmingOperation().getFarmingOperationId());
    assertNotNull(lsAdj.getAdjInventoryId());
  }


  /**
   * LivestockItem for ADD tests
   * Just contains basic info to pass to InventoryFormData.getLineKey()
   */
  private LivestockItem getLivestockItem8000() {
    LivestockItem ls1 = new LivestockItem();
    ls1.setInventoryItemCode("8000");
    ls1.setInventoryItemCodeDescription("Beef; Breeding; Bulls");
    ls1.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
    ls1.setIsMarketCommodity(Boolean.FALSE);
    return ls1;
  }


  /**
   * Test LivestockItem ADD
   */
  @Test
  public final void testParseAdjustments10() throws Exception {
    InventoryForm form = new InventoryForm();
    
    Scenario scenario1 = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario1.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    LivestockItem ls1 = getLivestockItem8000();

    // Note: not populating the form at all.
    // Just adding a new item.

    String lineKey = InventoryFormData.getLineKey(ls1);
    String lineCode = ls1.getInventoryItemCode();
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setLineCode(lineCode);
    fd1.setItemType(InventoryFormData.getLivestockType());

    fd1.setTotalQuantityStart("100");
    fd1.setTotalPriceStart("200");
    fd1.setTotalQuantityEnd("300");
    fd1.setTotalPriceEnd("400");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertEquals(1, updateList.size());

    LivestockItem lsAdj = (LivestockItem) updateList.get(0);
    assertNotNull(lsAdj);
    assertNotNull(lsAdj.getFarmingOperation());
    assertNotNull(lsAdj.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(100), lsAdj.getAdjQuantityStart());
    assertEquals(new Double(200), lsAdj.getAdjPriceStart());
    assertEquals(new Double(300), lsAdj.getAdjQuantityEnd());
    assertEquals(new Double(400), lsAdj.getAdjPriceEnd());
  }


  /**
   * Regular (market commmodity) test.
   * Test save with no changes - no adjustment actions to perform.
   */
  @Test
  public final void testAddCopyForwardAdjs01() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    List<CropItem> cropItems2007 = new ArrayList<>();
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(40));
    cropItem5100_2007.setAdjPriceEnd(new Double(50));
    fo2007.setCropItems(cropItems2007);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(true, errors.isEmpty());
    assertEquals(0, adjustmentsMap.size());
    assertEquals(true, messages.isEmpty());

  }


  /**
   * Regular (market commmodity) test.
   * Test with change, but no copy forward because 2008 start values already match.
   */
  @Test
  public final void testAddCopyForwardAdjs02() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(40));
    cropItem5100_2007.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    fo2007.setCropItems(cropItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2008 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008.setAdjQuantityStart(new Double(60));
    cropItem5100_2008.setAdjPriceStart(new Double(70));
    cropItem5100_2008.setAdjQuantityEnd(new Double(40));
    cropItem5100_2008.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2008 = new ArrayList<>();
    cropItems2008.add(cropItem5100_2008);
    fo2008.setCropItems(cropItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(true, messages.isEmpty());

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    CropItem cropAdj_2007 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(new Double(40), cropAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), cropAdj_2007.getAdjPriceStart());
    assertEquals(new Double(60), cropAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(70), cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test with change, but no copy forward because multiple 2008 records match.
   */
  @Test
  public final void testAddCopyForwardAdjs03() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(40));
    cropItem5100_2007.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    fo2007.setCropItems(cropItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2008_1 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008_1.setAdjQuantityStart(new Double(10));
    cropItem5100_2008_1.setAdjPriceStart(new Double(20));
    cropItem5100_2008_1.setAdjQuantityEnd(new Double(90));
    cropItem5100_2008_1.setAdjPriceEnd(new Double(90));
    CropItem cropItem5100_2008_2 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008_2.setAdjQuantityStart(new Double(5));
    cropItem5100_2008_2.setAdjPriceStart(new Double(15));
    cropItem5100_2008_2.setAdjQuantityEnd(new Double(80));
    cropItem5100_2008_2.setAdjPriceEnd(new Double(70));
    List<CropItem> cropItems2008 = new ArrayList<>();
    cropItems2008.add(cropItem5100_2008_1);
    cropItems2008.add(cropItem5100_2008_2);
    fo2008.setCropItems(cropItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_MULTIPLE_RECORDS_NEXT_YEAR, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(2, msgValues.length);
    assertEquals("5100", msgValues[0]);
    assertEquals("2008", msgValues[1]);

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    CropItem cropAdj_2007 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(new Double(40), cropAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), cropAdj_2007.getAdjPriceStart());
    assertEquals(new Double(60), cropAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(70), cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test with change, but no copy forward because the change was in the program year.
   */
  @Test
  public final void testAddCopyForwardAdjs04() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2009 = scenario.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2009 = TestModelBuilder.getCropItem5100();
    cropItem5100_2009.setAdjQuantityStart(new Double(40));
    cropItem5100_2009.setAdjPriceStart(new Double(50));
    cropItem5100_2009.setAdjQuantityEnd(new Double(40));
    cropItem5100_2009.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2009 = new ArrayList<>();
    cropItems2009.add(cropItem5100_2009);
    fo2009.setCropItems(cropItems2009);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2009, true, false);

    String lineKey = InventoryFormData.getLineKey(cropItem5100_2009);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2009, errors);
    TestUtils.logMessages(logger, errors);
    
    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2009, adjustmentsMap, messages);
    TestUtils.logMessages(logger, messages);
    assertEquals(0, messages.size());

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    CropItem cropAdj_2009 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2009);
    assertNotNull(cropAdj_2009.getFarmingOperation());
    assertNotNull(cropAdj_2009.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2009.getAdjQuantityProduced());
    assertEquals(new Double(40), cropAdj_2009.getAdjQuantityStart());
    assertEquals(new Double(50), cropAdj_2009.getAdjPriceStart());
    assertEquals(new Double(60), cropAdj_2009.getAdjQuantityEnd());
    assertEquals(new Double(70), cropAdj_2009.getAdjPriceEnd());
    assertEquals(new Integer(2009), cropAdj_2009.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test copy forward UPDATE for 2008.
   * No 2007 CRA record (adjustment only).
   * Add New adjustment (no xref ID or adj ID).
   */
  @Test
  public final void testAddCopyForwardAdjs05() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setAdjInventoryId(null);
    cropItem5100_2007.setCommodityXrefId(null);
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(40));
    cropItem5100_2007.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    fo2007.setCropItems(cropItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2008 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008.setAdjInventoryId(new Integer(529730));
    cropItem5100_2008.setAdjQuantityStart(new Double(40));
    cropItem5100_2008.setAdjPriceStart(new Double(50));
    cropItem5100_2008.setAdjQuantityEnd(new Double(40));
    cropItem5100_2008.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2008 = new ArrayList<>();
    cropItems2008.add(cropItem5100_2008);
    fo2008.setCropItems(cropItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList1 = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList1);
    assertEquals(1, addList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_MARKET_COMMODITY, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(4, msgValues.length);
    assertEquals("5100", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("60.000", msgValues[2]);
    assertEquals("$70.00", msgValues[3]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList2 = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList2);
    assertEquals(1, addList2.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    CropItem cropAdj_2007 = (CropItem) addList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(new Double(40), cropAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), cropAdj_2007.getAdjPriceStart());
    assertEquals(new Double(60), cropAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(70), cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    CropItem cropAdj_2008 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("5100", cropAdj_2008.getInventoryItemCode());
    assertEquals(null, cropAdj_2008.getAdjQuantityProduced());
    assertEquals(new Double(60), cropAdj_2008.getAdjQuantityStart());
    assertEquals(new Double(70), cropAdj_2008.getAdjPriceStart());
    assertEquals(new Double(40), cropAdj_2008.getAdjQuantityEnd());
    assertEquals(new Double(50), cropAdj_2008.getAdjPriceEnd());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test copy forward UPDATE for 2008.
   * With 2007 CRA record.
   */
  @Test
  public final void testAddCopyForwardAdjs06() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setReportedQuantityStart(null);
    cropItem5100_2007.setReportedPriceStart(null);
    cropItem5100_2007.setReportedQuantityEnd(new Double(10));
    cropItem5100_2007.setReportedPriceEnd(new Double(25));
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(30));
    cropItem5100_2007.setAdjPriceEnd(new Double(45));
    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    fo2007.setCropItems(cropItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2008 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008.setAdjQuantityStart(new Double(40));
    cropItem5100_2008.setAdjPriceStart(new Double(50));
    cropItem5100_2008.setAdjQuantityEnd(new Double(40));
    cropItem5100_2008.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2008 = new ArrayList<>();
    cropItems2008.add(cropItem5100_2008);
    fo2008.setCropItems(cropItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_MARKET_COMMODITY, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(4, msgValues.length);
    assertEquals("5100", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("60.000", msgValues[2]);
    assertEquals("$70.00", msgValues[3]);

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(2, updateList2.size());

    CropItem cropAdj_2007 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(new Double(40), cropAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), cropAdj_2007.getAdjPriceStart());
    assertEquals(new Double(50), cropAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(45), cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    CropItem cropAdj_2008 = (CropItem) updateList2.get(1);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("5100", cropAdj_2008.getInventoryItemCode());
    assertEquals(null, cropAdj_2008.getAdjQuantityProduced());
    assertEquals(new Double(60), cropAdj_2008.getAdjQuantityStart());
    assertEquals(new Double(70), cropAdj_2008.getAdjPriceStart());
    assertEquals(new Double(40), cropAdj_2008.getAdjQuantityEnd());
    assertEquals(new Double(50), cropAdj_2008.getAdjPriceEnd());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test copy forward UPDATE for 2008.
   * With 2007 CRA record.
   * There are two records with the same code/crop unit.
   * Both are updated.
   * No copy forward because we cannot be sure which should be copied.
   */
  @Test
  public final void testAddCopyForwardAdjs07() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setAdjInventoryId(new Integer(529730));
    cropItem5100_2007.setReportedInventoryId(new Integer(50469));
    cropItem5100_2007.setReportedQuantityStart(null);
    cropItem5100_2007.setReportedPriceStart(null);
    cropItem5100_2007.setReportedQuantityEnd(new Double(10));
    cropItem5100_2007.setReportedPriceEnd(new Double(25));
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(30));
    cropItem5100_2007.setAdjPriceEnd(new Double(45));

    CropItem cropItem5100_2007_2 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007_2.setAdjInventoryId(new Integer(35489));
    cropItem5100_2007_2.setReportedInventoryId(new Integer(85124));
    cropItem5100_2007_2.setReportedQuantityStart(null);
    cropItem5100_2007_2.setReportedPriceStart(null);
    cropItem5100_2007_2.setReportedQuantityEnd(new Double(13));
    cropItem5100_2007_2.setReportedPriceEnd(new Double(23));
    cropItem5100_2007_2.setAdjQuantityStart(new Double(43));
    cropItem5100_2007_2.setAdjPriceStart(new Double(53));
    cropItem5100_2007_2.setAdjQuantityEnd(new Double(33));
    cropItem5100_2007_2.setAdjPriceEnd(new Double(43));

    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    cropItems2007.add(cropItem5100_2007_2);
    fo2007.setCropItems(cropItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2008 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008.setAdjQuantityStart(new Double(40));
    cropItem5100_2008.setAdjPriceStart(new Double(50));
    cropItem5100_2008.setAdjQuantityEnd(new Double(40));
    cropItem5100_2008.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2008 = new ArrayList<>();
    cropItems2008.add(cropItem5100_2008);
    fo2008.setCropItems(cropItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);
    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007_2, true, false);

    String lineKey1 = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey1);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    String lineKey2 = InventoryFormData.getLineKey(cropItem5100_2007_2);
    InventoryFormData fd2 = form.getItem(lineKey2);

    fd2.setTotalQuantityProduced(null);
    fd2.setTotalQuantityStart("47");
    fd2.setTotalPriceStart("57");
    fd2.setTotalQuantityEnd("67");
    fd2.setTotalPriceEnd("77");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(2, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_MULTIPLE_RECORDS_THIS_YEAR, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("5100", msgValues[0]);
    assertEquals("2007", msgValues[1]);
    assertEquals("2008", msgValues[2]);

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(2, updateList2.size());

    CropItem cropAdj_2007_2 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2007_2);
    assertNotNull(cropAdj_2007_2.getFarmingOperation());
    assertNotNull(cropAdj_2007_2.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007_2.getAdjQuantityProduced());
    assertEquals(new Double(40), cropAdj_2007_2.getAdjQuantityStart());
    assertEquals(new Double(50), cropAdj_2007_2.getAdjPriceStart());
    assertEquals(new Double(50), cropAdj_2007_2.getAdjQuantityEnd());
    assertEquals(new Double(45), cropAdj_2007_2.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007_2.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    CropItem cropAdj_2007 = (CropItem) updateList2.get(1);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(new Double(47), cropAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(57), cropAdj_2007.getAdjPriceStart());
    assertEquals(new Double(54), cropAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(54), cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test copy forward UPDATE for 2008.
   * With 2007 CRA record.
   * There are two records with the same code/crop unit.
   * One is updated.
   * No copy forward because we cannot be sure which should be copied.
   */
  @Test
  public final void testAddCopyForwardAdjs08() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setAdjInventoryId(new Integer(529730));
    cropItem5100_2007.setReportedInventoryId(new Integer(50469));
    cropItem5100_2007.setReportedQuantityStart(null);
    cropItem5100_2007.setReportedPriceStart(null);
    cropItem5100_2007.setReportedQuantityEnd(new Double(10));
    cropItem5100_2007.setReportedPriceEnd(new Double(25));
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(30));
    cropItem5100_2007.setAdjPriceEnd(new Double(45));

    CropItem cropItem5100_2007_2 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007_2.setAdjInventoryId(new Integer(35489));
    cropItem5100_2007_2.setReportedInventoryId(new Integer(85124));
    cropItem5100_2007_2.setReportedQuantityStart(null);
    cropItem5100_2007_2.setReportedPriceStart(null);
    cropItem5100_2007_2.setReportedQuantityEnd(new Double(13));
    cropItem5100_2007_2.setReportedPriceEnd(new Double(23));
    cropItem5100_2007_2.setAdjQuantityStart(new Double(43));
    cropItem5100_2007_2.setAdjPriceStart(new Double(53));
    cropItem5100_2007_2.setAdjQuantityEnd(new Double(33));
    cropItem5100_2007_2.setAdjPriceEnd(new Double(43));

    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    cropItems2007.add(cropItem5100_2007_2);
    fo2007.setCropItems(cropItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2008 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008.setAdjQuantityStart(new Double(40));
    cropItem5100_2008.setAdjPriceStart(new Double(50));
    cropItem5100_2008.setAdjQuantityEnd(new Double(40));
    cropItem5100_2008.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2008 = new ArrayList<>();
    cropItems2008.add(cropItem5100_2008);
    fo2008.setCropItems(cropItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);
    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007_2, true, false);

    String lineKey1 = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey1);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_MULTIPLE_RECORDS_THIS_YEAR, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("5100", msgValues[0]);
    assertEquals("2007", msgValues[1]);
    assertEquals("2008", msgValues[2]);

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    CropItem cropAdj_2007 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(new Double(40), cropAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), cropAdj_2007.getAdjPriceStart());
    assertEquals(new Double(50), cropAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(45), cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test copy forward ADD for 2008. No matching record found.
   */
  @Test
  public final void testAddCopyForwardAdjs09() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(40));
    cropItem5100_2007.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    fo2007.setCropItems(cropItems2007);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_MARKET_COMMODITY_NO_RECORD, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(4, msgValues.length);
    assertEquals("5100", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("60.000", msgValues[2]);
    assertEquals("$70.00", msgValues[3]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList);
    assertEquals(1, addList.size());

    CropItem cropAdj_2007 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(new Double(40), cropAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), cropAdj_2007.getAdjPriceStart());
    assertEquals(new Double(60), cropAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(70), cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    CropItem cropAdj_2008 = (CropItem) addList.get(0);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("5100", cropAdj_2008.getInventoryItemCode());
    assertEquals(null, cropAdj_2008.getAdjQuantityProduced());
    assertEquals(new Double(60), cropAdj_2008.getAdjQuantityStart());
    assertEquals(new Double(70), cropAdj_2008.getAdjPriceStart());
    assertEquals(null, cropAdj_2008.getAdjQuantityEnd());
    assertEquals(null, cropAdj_2008.getAdjPriceEnd());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test copy forward ADD for 2008. One match found, no existing adjustment.
   */
  @Test
  public final void testAddCopyForwardAdjs10() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(40));
    cropItem5100_2007.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    fo2007.setCropItems(cropItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2008 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008.setAdjInventoryId(null);
    cropItem5100_2008.setAdjQuantityStart(null);
    cropItem5100_2008.setAdjPriceStart(null);
    cropItem5100_2008.setAdjQuantityEnd(null);
    cropItem5100_2008.setAdjPriceEnd(null);
    cropItem5100_2008.setReportedQuantityStart(new Double(40));
    cropItem5100_2008.setReportedPriceStart(new Double(55));
    List<CropItem> cropItems2008 = new ArrayList<>();
    cropItems2008.add(cropItem5100_2008);
    fo2008.setCropItems(cropItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_MARKET_COMMODITY, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(4, msgValues.length);
    assertEquals("5100", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("60.000", msgValues[2]);
    assertEquals("$70.00", msgValues[3]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList);
    assertEquals(1, addList.size());

    CropItem cropAdj_2007 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(new Double(40), cropAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), cropAdj_2007.getAdjPriceStart());
    assertEquals(new Double(60), cropAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(70), cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    CropItem cropAdj_2008 = (CropItem) addList.get(0);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("5100", cropAdj_2008.getInventoryItemCode());
    assertEquals(null, cropAdj_2008.getAdjQuantityProduced());
    assertEquals(new Double(20), cropAdj_2008.getAdjQuantityStart());
    assertEquals(new Double(15), cropAdj_2008.getAdjPriceStart());
    assertEquals(null, cropAdj_2008.getAdjQuantityEnd());
    assertEquals(null, cropAdj_2008.getAdjPriceEnd());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Non-market commodity test.
   * Test with change, but no copy forward because the item is a non-market commodity
   * and the start quantity already matches.
   */
  @Test
  public final void testAddCopyForwardAdjs11() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    LivestockItem livestockItem7781_2007 = TestModelBuilder.getLivestockItem7781();
    livestockItem7781_2007.setAdjQuantityStart(new Double(40));
    livestockItem7781_2007.setAdjPriceStart(new Double(50));
    livestockItem7781_2007.setAdjQuantityEnd(new Double(40));
    livestockItem7781_2007.setAdjPriceEnd(new Double(50));
    List<LivestockItem> livestockItems2007 = new ArrayList<>();
    livestockItems2007.add(livestockItem7781_2007);
    fo2007.setLivestockItems(livestockItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    LivestockItem livestockItem7781_2008 = TestModelBuilder.getLivestockItem7781();
    livestockItem7781_2008.setAdjQuantityStart(new Double(60));
    livestockItem7781_2008.setAdjPriceStart(new Double(20));
    livestockItem7781_2008.setAdjQuantityEnd(new Double(40));
    livestockItem7781_2008.setAdjPriceEnd(new Double(50));
    List<LivestockItem> livestockItems2008 = new ArrayList<>();
    livestockItems2008.add(livestockItem7781_2008);
    fo2008.setLivestockItems(livestockItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, livestockItem7781_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(livestockItem7781_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(true, messages.isEmpty());

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    LivestockItem livestockAdj_2007 = (LivestockItem) updateList2.get(0);
    assertNotNull(livestockAdj_2007);
    assertNotNull(livestockAdj_2007.getFarmingOperation());
    assertNotNull(livestockAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), livestockAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), livestockAdj_2007.getAdjPriceStart());
    assertEquals(new Double(60), livestockAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(70), livestockAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), livestockAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Non-market commodity test.
   * Test copy forward UPDATE for 2008.
   */
  @Test
  public final void testAddCopyForwardAdjs12() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    LivestockItem livestockItem7781_2007 = TestModelBuilder.getLivestockItem7781();
    livestockItem7781_2007.setAdjQuantityStart(new Double(40));
    livestockItem7781_2007.setAdjPriceStart(new Double(50));
    livestockItem7781_2007.setAdjQuantityEnd(new Double(40));
    livestockItem7781_2007.setAdjPriceEnd(new Double(50));
    List<LivestockItem> livestockItems2007 = new ArrayList<>();
    livestockItems2007.add(livestockItem7781_2007);
    fo2007.setLivestockItems(livestockItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    LivestockItem livestockItem7781_2008 = TestModelBuilder.getLivestockItem7781();
    livestockItem7781_2008.setAdjQuantityStart(null);
    livestockItem7781_2008.setAdjPriceStart(new Double(20));
    livestockItem7781_2008.setAdjQuantityEnd(new Double(30));
    livestockItem7781_2008.setAdjPriceEnd(new Double(50));
    List<LivestockItem> livestockItems2008 = new ArrayList<>();
    livestockItems2008.add(livestockItem7781_2008);
    fo2008.setLivestockItems(livestockItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, livestockItem7781_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(livestockItem7781_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_NON_MARKET_COMMODITY, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("7781", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("60.000", msgValues[2]);

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(2, updateList2.size());

    LivestockItem livestockAdj_2007 = (LivestockItem) updateList2.get(0);
    assertNotNull(livestockAdj_2007);
    assertNotNull(livestockAdj_2007.getFarmingOperation());
    assertNotNull(livestockAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), livestockAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), livestockAdj_2007.getAdjPriceStart());
    assertEquals(new Double(60), livestockAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(70), livestockAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), livestockAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    LivestockItem livestockAdj_2008 = (LivestockItem) updateList2.get(1);
    assertNotNull(livestockAdj_2008);
    assertNotNull(livestockAdj_2008.getFarmingOperation());
    assertNotNull(livestockAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(livestockAdj_2008.getInventoryItemCode());
    assertEquals("7781", livestockAdj_2008.getInventoryItemCode());
    assertEquals(new Double(60), livestockAdj_2008.getAdjQuantityStart());
    assertEquals(new Double(20), livestockAdj_2008.getAdjPriceStart());
    assertEquals(new Double(30), livestockAdj_2008.getAdjQuantityEnd());
    assertEquals(new Double(50), livestockAdj_2008.getAdjPriceEnd());
    assertEquals(new Integer(2008), livestockAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Non-market commodity test.
   * Test copy forward ADD for 2008. One match found, no existing adjustment.
   */
  @Test
  public final void testAddCopyForwardAdjs13() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    LivestockItem livestockItem7781_2007 = TestModelBuilder.getLivestockItem7781();
    livestockItem7781_2007.setAdjQuantityStart(new Double(40));
    livestockItem7781_2007.setAdjPriceStart(new Double(50));
    livestockItem7781_2007.setAdjQuantityEnd(new Double(40));
    livestockItem7781_2007.setAdjPriceEnd(new Double(50));
    List<LivestockItem> livestockItems2007 = new ArrayList<>();
    livestockItems2007.add(livestockItem7781_2007);
    fo2007.setLivestockItems(livestockItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    LivestockItem livestockItem7781_2008 = TestModelBuilder.getLivestockItem7781();
    livestockItem7781_2008.setAdjInventoryId(null);
    livestockItem7781_2008.setReportedQuantityStart(new Double(10));
    livestockItem7781_2008.setReportedPriceStart(new Double(20));
    livestockItem7781_2008.setReportedQuantityEnd(new Double(30));
    livestockItem7781_2008.setReportedPriceEnd(new Double(50));
    livestockItem7781_2008.setAdjQuantityStart(null);
    livestockItem7781_2008.setAdjPriceStart(null);
    livestockItem7781_2008.setAdjQuantityEnd(null);
    livestockItem7781_2008.setAdjPriceEnd(null);
    List<LivestockItem> livestockItems2008 = new ArrayList<>();
    livestockItems2008.add(livestockItem7781_2008);
    fo2008.setLivestockItems(livestockItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, livestockItem7781_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(livestockItem7781_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_NON_MARKET_COMMODITY, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("7781", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("60.000", msgValues[2]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList);
    assertEquals(1, addList.size());

    LivestockItem livestockAdj_2007 = (LivestockItem) updateList2.get(0);
    assertNotNull(livestockAdj_2007);
    assertNotNull(livestockAdj_2007.getFarmingOperation());
    assertNotNull(livestockAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), livestockAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), livestockAdj_2007.getAdjPriceStart());
    assertEquals(new Double(60), livestockAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(70), livestockAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), livestockAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    LivestockItem livestockAdj_2008 = (LivestockItem) addList.get(0);
    assertNotNull(livestockAdj_2008);
    assertNotNull(livestockAdj_2008.getFarmingOperation());
    assertNotNull(livestockAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(livestockAdj_2008.getInventoryItemCode());
    assertEquals("7781", livestockAdj_2008.getInventoryItemCode());
    assertEquals(new Double(50), livestockAdj_2008.getAdjQuantityStart());
    assertEquals(null, livestockAdj_2008.getAdjPriceStart());
    assertEquals(null, livestockAdj_2008.getAdjQuantityEnd());
    assertEquals(null, livestockAdj_2008.getAdjPriceEnd());
    assertEquals(new Integer(2008), livestockAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Non-market commodity test.
   * Test copy forward ADD for 2008. No matching record found.
   */
  @Test
  public final void testAddCopyForwardAdjs14() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    LivestockItem livestockItem7781_2007 = TestModelBuilder.getLivestockItem7781();
    livestockItem7781_2007.setAdjQuantityStart(new Double(40));
    livestockItem7781_2007.setAdjPriceStart(new Double(50));
    livestockItem7781_2007.setAdjQuantityEnd(new Double(40));
    livestockItem7781_2007.setAdjPriceEnd(new Double(50));
    List<LivestockItem> livestockItems2007 = new ArrayList<>();
    livestockItems2007.add(livestockItem7781_2007);
    fo2007.setLivestockItems(livestockItems2007);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, livestockItem7781_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(livestockItem7781_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart("40");
    fd1.setTotalPriceStart("50");
    fd1.setTotalQuantityEnd("60");
    fd1.setTotalPriceEnd("70");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_NON_MARKET_COMMODITY_NO_RECORD, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("7781", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("60.000", msgValues[2]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList);
    assertEquals(1, addList.size());

    LivestockItem livestockAdj_2007 = (LivestockItem) updateList2.get(0);
    assertNotNull(livestockAdj_2007);
    assertNotNull(livestockAdj_2007.getFarmingOperation());
    assertNotNull(livestockAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), livestockAdj_2007.getAdjQuantityStart());
    assertEquals(new Double(50), livestockAdj_2007.getAdjPriceStart());
    assertEquals(new Double(60), livestockAdj_2007.getAdjQuantityEnd());
    assertEquals(new Double(70), livestockAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), livestockAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    LivestockItem livestockAdj_2008 = (LivestockItem) addList.get(0);
    assertNotNull(livestockAdj_2008);
    assertNotNull(livestockAdj_2008.getFarmingOperation());
    assertNotNull(livestockAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(livestockAdj_2008.getInventoryItemCode());
    assertEquals("7781", livestockAdj_2008.getInventoryItemCode());
    assertEquals(new Double(60), livestockAdj_2008.getAdjQuantityStart());
    assertEquals(null, livestockAdj_2008.getAdjPriceStart());
    assertEquals(null, livestockAdj_2008.getAdjQuantityEnd());
    assertEquals(null, livestockAdj_2008.getAdjPriceEnd());
    assertEquals(new Integer(2008), livestockAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test with DELETE, but no copy forward because the adjustment has no CRA record.
   */
  @Test
  public final void testAddCopyForwardAdjs15() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setReportedInventoryId(null);
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(40));
    cropItem5100_2007.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    fo2007.setCropItems(cropItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2008_1 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008_1.setAdjQuantityStart(new Double(10));
    cropItem5100_2008_1.setAdjPriceStart(new Double(20));
    cropItem5100_2008_1.setAdjQuantityEnd(new Double(90));
    cropItem5100_2008_1.setAdjPriceEnd(new Double(90));
    List<CropItem> cropItems2008 = new ArrayList<>();
    cropItems2008.add(cropItem5100_2008_1);
    fo2008.setCropItems(cropItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setDeleted(true);
    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart(null);
    fd1.setTotalPriceStart(null);
    fd1.setTotalQuantityEnd(null);
    fd1.setTotalPriceEnd(null);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> deleteList1 = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertNotNull(deleteList1);
    assertEquals(1, deleteList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(true, messages.isEmpty());

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> deleteList2 = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertNotNull(deleteList2);
    assertEquals(1, deleteList2.size());

    CropItem cropAdj_2007 = (CropItem) deleteList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(null, cropAdj_2007.getAdjQuantityStart());
    assertEquals(null, cropAdj_2007.getAdjPriceStart());
    assertEquals(null, cropAdj_2007.getAdjQuantityEnd());
    assertEquals(null, cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Regular (market commmodity) test.
   * Test with DELETE, and copy forward because it reverts to the CRA record.
   * Test copy forward UPDATE for 2008.
   */
  @Test
  public final void testAddCopyForwardAdjs16() throws Exception {
    InventoryForm form = new InventoryForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2007 = TestModelBuilder.getReferenceScenario(scenario);
    rs2007.setYear(new Integer(2007));
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2007);
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2007 = rs2007.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2007 = TestModelBuilder.getCropItem5100();
    cropItem5100_2007.setAdjInventoryId(new Integer(529730));
    cropItem5100_2007.setReportedInventoryId(new Integer(50469));
    cropItem5100_2007.setReportedQuantityStart(null);
    cropItem5100_2007.setReportedPriceStart(null);
    cropItem5100_2007.setReportedQuantityEnd(new Double(10));
    cropItem5100_2007.setReportedPriceEnd(new Double(25));
    cropItem5100_2007.setAdjQuantityStart(new Double(40));
    cropItem5100_2007.setAdjPriceStart(new Double(50));
    cropItem5100_2007.setAdjQuantityEnd(new Double(30));
    cropItem5100_2007.setAdjPriceEnd(new Double(45));
    List<CropItem> cropItems2007 = new ArrayList<>();
    cropItems2007.add(cropItem5100_2007);
    fo2007.setCropItems(cropItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    CropItem cropItem5100_2008 = TestModelBuilder.getCropItem5100();
    cropItem5100_2008.setAdjQuantityStart(new Double(40));
    cropItem5100_2008.setAdjPriceStart(new Double(50));
    cropItem5100_2008.setAdjQuantityEnd(new Double(40));
    cropItem5100_2008.setAdjPriceEnd(new Double(50));
    List<CropItem> cropItems2008 = new ArrayList<>();
    cropItems2008.add(cropItem5100_2008);
    fo2008.setCropItems(cropItems2008);

    InventoryAdjustmentUtils.populateFormFromInventoryItem(form, cropItem5100_2007, true, false);

    String lineKey = InventoryFormData.getLineKey(cropItem5100_2007);
    InventoryFormData fd1 = form.getItem(lineKey);

    fd1.setTotalQuantityProduced(null);
    fd1.setTotalQuantityStart(null);
    fd1.setTotalPriceStart(null);
    fd1.setTotalQuantityEnd("10");
    fd1.setTotalPriceEnd("25");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> deleteList1 = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertNotNull(deleteList1);
    assertEquals(1, deleteList1.size());

    ActionMessages messages = new ActionMessages();
    InventoryAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());
    @SuppressWarnings("unchecked")
    Iterator<ActionMessage> msgIter = messages.get();
    ActionMessage msg = msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.INV_COPY_FORWARD_MARKET_COMMODITY, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(4, msgValues.length);
    assertEquals("5100", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("10.000", msgValues[2]);
    assertEquals("$25.00", msgValues[3]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> deleteList2 = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertNotNull(deleteList2);
    assertEquals(1, deleteList2.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    CropItem cropAdj_2007 = (CropItem) deleteList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjQuantityProduced());
    assertEquals(null, cropAdj_2007.getAdjQuantityStart());
    assertEquals(null, cropAdj_2007.getAdjPriceStart());
    assertEquals(null, cropAdj_2007.getAdjQuantityEnd());
    assertEquals(null, cropAdj_2007.getAdjPriceEnd());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    CropItem cropAdj_2008 = (CropItem) updateList2.get(0);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("5100", cropAdj_2008.getInventoryItemCode());
    assertEquals(null, cropAdj_2008.getAdjQuantityProduced());
    assertEquals(new Double(10), cropAdj_2008.getAdjQuantityStart());
    assertEquals(new Double(25), cropAdj_2008.getAdjPriceStart());
    assertEquals(new Double(40), cropAdj_2008.getAdjQuantityEnd());
    assertEquals(new Double(50), cropAdj_2008.getAdjPriceEnd());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }

}
