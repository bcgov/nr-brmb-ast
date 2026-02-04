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

import ca.bc.gov.srm.farm.dao.TestModelBuilder;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.PayableItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.ui.struts.calculator.accruals.AccrualFormData;
import ca.bc.gov.srm.farm.ui.struts.calculator.accruals.AccrualsForm;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author awilkinson
 * @created Apr 5, 2011
 */
public class TestAccrualsAdjustmentUtils {


  /**
   * Test save with no changes - no adjustment actions to perform.
   */
  @Test
  public final void testParseAdjustments1() throws Exception {
    AccrualsForm form = new AccrualsForm();
    
    Scenario scenario = TestModelBuilder.getScenario1();
    FarmingYear farmingYear = scenario.getFarmingYear();
    FarmingOperation fo = farmingYear.getFarmingOperationByNumber(1);

    InputItem input1 = TestModelBuilder.getInputItem9661();
    PayableItem payable1 = TestModelBuilder.getPayableItem9714();

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, input1, true);
    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, payable1, true);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(0, adjustmentsMap.size());
  }


  /**
   * Test UPDATE - with reported data
   */
  @Test
  public final void testParseAdjustments2() throws Exception {
    Scenario scenario = new Scenario();
    scenario.setYear(2023);
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    AccrualsForm form = new AccrualsForm();
    FarmingOperation fo = TestModelBuilder.getFarmingOperation1(farmingYear);

    InputItem input1 = TestModelBuilder.getInputItem9662();

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, input1, true);

    String lineKey = AccrualFormData.getLineKey(input1);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("361.25");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertEquals(1, updateList.size());

    InputItem inputAdj = (InputItem) updateList.get(0);
    assertNotNull(inputAdj);
    assertNotNull(inputAdj.getFarmingOperation());
    assertNotNull(inputAdj.getFarmingOperation().getFarmingOperationId());
    assertNotNull(inputAdj.getAdjInventoryId());
    assertEquals(new Double(40), inputAdj.getAdjStartOfYearAmount());
    assertEquals(new Double(-58.75), inputAdj.getAdjEndOfYearAmount());
  }


  /**
   * Test UPDATE - without reported data
   */
  @Test
  public final void testParseAdjustments3() throws Exception {
    Scenario scenario = new Scenario();
    scenario.setYear(2023);
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    AccrualsForm form = new AccrualsForm();
    FarmingOperation fo = TestModelBuilder.getFarmingOperation1(farmingYear);

    PayableItem payable1 = TestModelBuilder.getPayableItem9714();

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, payable1, true);

    String lineKey = AccrualFormData.getLineKey(payable1);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("220");
    fd1.setTotalEndOfYearAmount("30");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertEquals(1, updateList.size());

    PayableItem payableAdj = (PayableItem) updateList.get(0);
    assertNotNull(payableAdj);
    assertNotNull(payableAdj.getFarmingOperation());
    assertNotNull(payableAdj.getFarmingOperation().getFarmingOperationId());
    assertNotNull(payableAdj.getAdjInventoryId());
    assertEquals(new Double(220), payableAdj.getAdjStartOfYearAmount());
    assertEquals(new Double(30), payableAdj.getAdjEndOfYearAmount());
  }


  /**
   * Test ADD - with reported data
   */
  @Test
  public final void testParseAdjustments4() throws Exception {
    Scenario scenario = new Scenario();
    scenario.setYear(2023);
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    AccrualsForm form = new AccrualsForm();
    FarmingOperation fo = TestModelBuilder.getFarmingOperation1(farmingYear);

    InputItem input1 = TestModelBuilder.getInputItem9661();

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, input1, true);

    String lineKey = AccrualFormData.getLineKey(input1);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("200");
    fd1.setTotalEndOfYearAmount("250");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertEquals(1, addList.size());

    InputItem inputAdj = (InputItem) addList.get(0);
    assertNotNull(inputAdj);
    assertNotNull(inputAdj.getFarmingOperation());
    assertNotNull(inputAdj.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(100), inputAdj.getAdjStartOfYearAmount());
    assertEquals(new Double(50), inputAdj.getAdjEndOfYearAmount());
  }


  /**
   * Test ADD - without reported data
   */
  @Test
  public final void testParseAdjustments5() throws Exception {
    Scenario scenario = new Scenario();
    scenario.setYear(2023);
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    AccrualsForm form = new AccrualsForm();
    FarmingOperation fo = TestModelBuilder.getFarmingOperation1(farmingYear);

    // Note: not populating the form at all.
    // Just adding a new item.

    String lineKey = AccrualFormData.TYPE_NEW + "_" + "00";
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setLineCode("9661");
    fd1.setItemType(AccrualFormData.getInputType());

    fd1.setTotalStartOfYearAmount("200");
    fd1.setTotalEndOfYearAmount("250");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertEquals(1, addList.size());

    InputItem inputAdj = (InputItem) addList.get(0);
    assertNotNull(inputAdj);
    assertNotNull(inputAdj.getFarmingOperation());
    assertNotNull(inputAdj.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(200), inputAdj.getAdjStartOfYearAmount());
    assertEquals(new Double(250), inputAdj.getAdjEndOfYearAmount());
  }


  /**
   * Test DELETE - with reported data
   */
  @Test
  public final void testParseAdjustments6() throws Exception {
    Scenario scenario = new Scenario();
    scenario.setYear(2023);
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    AccrualsForm form = new AccrualsForm();
    FarmingOperation fo = TestModelBuilder.getFarmingOperation1(farmingYear);

    InputItem input1 = TestModelBuilder.getInputItem9662();

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, input1, true);

    String lineKey = AccrualFormData.getLineKey(input1);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setDeleted(true);
    fd1.setTotalStartOfYearAmount("");
    fd1.setTotalEndOfYearAmount("420");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertEquals(1, updateList.size());

    InputItem inputAdj = (InputItem) updateList.get(0);
    assertNotNull(inputAdj);
    assertNotNull(inputAdj.getFarmingOperation());
    assertNotNull(inputAdj.getFarmingOperation().getFarmingOperationId());
    assertNotNull(inputAdj.getAdjInventoryId());
  }


  /**
   * Test DELETE - without reported data
   */
  @Test
  public final void testParseAdjustments7() throws Exception {
    Scenario scenario = new Scenario();
    scenario.setYear(2023);
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    AccrualsForm form = new AccrualsForm();
    FarmingOperation fo = TestModelBuilder.getFarmingOperation1(farmingYear);

    PayableItem input1 = TestModelBuilder.getPayableItem9714();

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, input1, true);

    String lineKey = AccrualFormData.getLineKey(input1);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setDeleted(true);
    fd1.setTotalStartOfYearAmount("");
    fd1.setTotalEndOfYearAmount(null);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> updateList = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertEquals(1, updateList.size());

    PayableItem inputAdj = (PayableItem) updateList.get(0);
    assertNotNull(inputAdj);
    assertNotNull(inputAdj.getFarmingOperation());
    assertNotNull(inputAdj.getFarmingOperation().getFarmingOperationId());
    assertNotNull(inputAdj.getAdjInventoryId());
  }


  /**
   * Test Blank Line Code error
   */
  @Test
  public final void testParseAdjustments8() throws Exception {
    Scenario scenario = new Scenario();
    scenario.setYear(2023);
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    AccrualsForm form = new AccrualsForm();
    FarmingOperation fo = TestModelBuilder.getFarmingOperation1(farmingYear);

    // Note: not populating the form at all.
    // Adding a new item.

    String lineKey = AccrualFormData.TYPE_NEW + "_" + "00";
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setItemType(AccrualFormData.getInputType());

    fd1.setTotalStartOfYearAmount("100");
    fd1.setTotalEndOfYearAmount(null);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(1, errors.size());

    @SuppressWarnings("rawtypes")
    Iterator messages = errors.get();
    ActionMessage msg = (ActionMessage) messages.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ERRORS_LINE_CODE_BLANK, msg.getKey());

    assertEquals(1, adjustmentsMap.size());
  }


  /**
   * Bad value error
   */
  @Test
  public final void testParseInventoryAdjustments6() throws Exception {
    Scenario scenario = new Scenario();
    scenario.setYear(2023);
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    AccrualsForm form = new AccrualsForm();
    FarmingOperation fo = TestModelBuilder.getFarmingOperation1(farmingYear);

    // Note: not populating the form at all.
    // Adding a new item.

    String lineKey = AccrualFormData.TYPE_NEW + "_" + "00";
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setLineCode("9661");
    fd1.setItemType(AccrualFormData.getReceivableType());

    fd1.setTotalStartOfYearAmount("-100000000000"); // value below min, by 1
    fd1.setTotalEndOfYearAmount("text");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo, errors);

    assertEquals(2, errors.size());

    boolean start = false;
    boolean end = false;

    for(@SuppressWarnings("rawtypes")
    Iterator messages = errors.get(); messages.hasNext(); ) {
      ActionMessage msg = (ActionMessage) messages.next();
      assertNotNull(msg);

      if(msg.getKey().equals(MessageConstants.ERRORS_ADJUSTED_START_VALUE)) {
        start = true;
      } else if(msg.getKey().equals(MessageConstants.ERRORS_ADJUSTED_END_VALUE)) {
        end = true;
      } else {
        assertTrue(false);
      }
    }

    assertEquals(true, start);
    assertEquals(true, end);
    assertEquals(0, adjustmentsMap.size());
  }


  /**
   * Regular (market commmodity) test.
   * Test save with no changes - no adjustment actions to perform.
   */
  @Test
  public final void testAddCopyForwardAdjs01() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    List<InputItem> inputItems2007 = new ArrayList<>();
    InventoryItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(40));
    fo2007.setInputItems(inputItems2007);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(true, errors.isEmpty());
    assertEquals(0, adjustmentsMap.size());
    assertEquals(true, messages.isEmpty());

  }


  /**
   * Test with change, but no copy forward because 2008 start value already matches.
   */
  @Test
  public final void testAddCopyForwardAdjs02() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InputItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2007 = new ArrayList<>();
    inputItems2007.add(inputItem50_2007);
    fo2007.setInputItems(inputItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2008 = TestModelBuilder.getInputItem50();
    inputItem50_2008.setAdjInventoryId(new Integer(527130));
    inputItem50_2008.setAdjStartOfYearAmount(new Double(60));
    inputItem50_2008.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2008 = new ArrayList<>();
    inputItems2008.add(inputItem50_2008);
    fo2008.setInputItems(inputItems2008);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);

    String lineKey = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("60");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(true, messages.isEmpty());

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    InventoryItem cropAdj_2007 = updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(new Double(60), cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test with change, but no copy forward because multiple 2008 records match.
   */
  @Test
  public final void testAddCopyForwardAdjs03() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InputItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2007 = new ArrayList<>();
    inputItems2007.add(inputItem50_2007);
    fo2007.setInputItems(inputItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2008_1 = TestModelBuilder.getInputItem50();
    inputItem50_2008_1.setAdjStartOfYearAmount(new Double(10));
    inputItem50_2008_1.setAdjEndOfYearAmount(new Double(90));
    InputItem inputItem50_2008_2 = TestModelBuilder.getInputItem50();
    inputItem50_2008_2.setAdjStartOfYearAmount(new Double(5));
    inputItem50_2008_2.setAdjEndOfYearAmount(new Double(80));
    List<InputItem> inputItems2008 = new ArrayList<>();
    inputItems2008.add(inputItem50_2008_1);
    inputItems2008.add(inputItem50_2008_2);
    fo2008.setInputItems(inputItems2008);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);

    String lineKey = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("60");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());

    @SuppressWarnings("rawtypes")
    Iterator msgIter = messages.get();
    ActionMessage msg = (ActionMessage) msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ACCRUAL_COPY_FORWARD_MULTIPLE_RECORDS_NEXT_YEAR, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(2, msgValues.length);
    assertEquals("50", msgValues[0]);
    assertEquals("2008", msgValues[1]);

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    InventoryItem cropAdj_2007 = updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(new Double(60), cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test with change, but no copy forward because the change was in the program year.
   */
  @Test
  public final void testAddCopyForwardAdjs04() throws Exception {
    AccrualsForm form = new AccrualsForm();
    Scenario scenario = TestModelBuilder.getScenario1();
    ReferenceScenario rs2008 = TestModelBuilder.getReferenceScenario(scenario);
    rs2008.setYear(new Integer(2008));

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    referenceScenarios.add(rs2008);
    scenario.setReferenceScenarios(referenceScenarios);

    FarmingOperation fo2009 = scenario.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2009 = TestModelBuilder.getInputItem50();
    inputItem50_2009.setAdjInventoryId(new Integer(489730));
    inputItem50_2009.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2009.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2009 = new ArrayList<>();
    inputItems2009.add(inputItem50_2009);
    fo2009.setInputItems(inputItems2009);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2009, true);

    String lineKey = AccrualFormData.getLineKey(inputItem50_2009);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("60");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2009, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2009, adjustmentsMap, messages);

    assertEquals(true, messages.isEmpty());

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    InventoryItem cropAdj_2009 = updateList2.get(0);
    assertNotNull(cropAdj_2009);
    assertNotNull(cropAdj_2009.getFarmingOperation());
    assertNotNull(cropAdj_2009.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), cropAdj_2009.getAdjStartOfYearAmount());
    assertEquals(new Double(60), cropAdj_2009.getAdjEndOfYearAmount());
    assertEquals(new Integer(2009), cropAdj_2009.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test copy forward UPDATE for 2008.
   * No 2007 CRA record (adjustment only).
   * Add New adjustment (no xref ID or adj ID).
   */
  @Test
  public final void testAddCopyForwardAdjs05() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InventoryItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(null);
    inputItem50_2007.setCommodityXrefId(null);
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(40));

    List<InputItem> inputItems2007 = new ArrayList<>();
    fo2007.setInputItems(inputItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2008 = TestModelBuilder.getInputItem50();
    inputItem50_2008.setAdjInventoryId(new Integer(527130));
    inputItem50_2008.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2008.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2008 = new ArrayList<>();
    inputItems2008.add(inputItem50_2008);
    fo2008.setInputItems(inputItems2008);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);

    String lineKey = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("60");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList1 = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList1);
    assertEquals(1, addList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());

    @SuppressWarnings("rawtypes")
    Iterator msgIter = messages.get();
    ActionMessage msg = (ActionMessage) msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ACCRUAL_COPY_FORWARD, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("50", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("$60.00", msgValues[2]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList2 = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList2);
    assertEquals(1, addList2.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    InventoryItem cropAdj_2007 = addList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(new Double(60), cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    InventoryItem cropAdj_2008 = updateList2.get(0);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("50", cropAdj_2008.getInventoryItemCode());
    assertEquals(new Double(60), cropAdj_2008.getAdjStartOfYearAmount());
    assertEquals(new Double(40), cropAdj_2008.getAdjEndOfYearAmount());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test copy forward UPDATE for 2008.
   * With 2007 CRA record.
   */
  @Test
  public final void testAddCopyForwardAdjs06() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InputItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setReportedInventoryId(new Integer(14756));
    inputItem50_2007.setReportedStartOfYearAmount(null);
    inputItem50_2007.setReportedEndOfYearAmount(new Double(10));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(30));
    List<InputItem> inputItems2007 = new ArrayList<>();
    inputItems2007.add(inputItem50_2007);
    fo2007.setInputItems(inputItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2008 = TestModelBuilder.getInputItem50();
    inputItem50_2008.setAdjInventoryId(new Integer(527130));
    inputItem50_2008.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2008.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2008 = new ArrayList<>();
    inputItems2008.add(inputItem50_2008);
    fo2008.setInputItems(inputItems2008);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);

    String lineKey = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("60");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());

    @SuppressWarnings("rawtypes")
    Iterator msgIter = messages.get();
    ActionMessage msg = (ActionMessage) msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ACCRUAL_COPY_FORWARD, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("50", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("$60.00", msgValues[2]);

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(2, updateList2.size());

    InventoryItem cropAdj_2007 = updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(new Double(50), cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    InventoryItem cropAdj_2008 = updateList2.get(1);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("50", cropAdj_2008.getInventoryItemCode());
    assertEquals(new Double(60), cropAdj_2008.getAdjStartOfYearAmount());
    assertEquals(new Double(40), cropAdj_2008.getAdjEndOfYearAmount());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test copy forward UPDATE for 2008.
   * With 2007 CRA record.
   * There are two records with the same code/crop unit.
   * Both are updated.
   * No copy forward because we cannot be sure which should be copied.
   */
  @Test
  public final void testAddCopyForwardAdjs07() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InputItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setReportedInventoryId(new Integer(14756));
    inputItem50_2007.setReportedStartOfYearAmount(null);
    inputItem50_2007.setReportedEndOfYearAmount(new Double(10));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(30));

    InputItem inputItem50_2007_2 = TestModelBuilder.getInputItem50();
    inputItem50_2007_2.setAdjInventoryId(new Integer(35489));
    inputItem50_2007_2.setReportedInventoryId(new Integer(85124));
    inputItem50_2007_2.setReportedStartOfYearAmount(null);
    inputItem50_2007_2.setReportedEndOfYearAmount(new Double(13));
    inputItem50_2007_2.setAdjStartOfYearAmount(new Double(43));
    inputItem50_2007_2.setAdjEndOfYearAmount(new Double(33));

    List<InputItem> inputItems2007 = new ArrayList<>();
    inputItems2007.add(inputItem50_2007);
    inputItems2007.add(inputItem50_2007_2);
    fo2007.setInputItems(inputItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2008 = TestModelBuilder.getInputItem50();
    inputItem50_2008.setAdjInventoryId(new Integer(527130));
    inputItem50_2008.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2008.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2008 = new ArrayList<>();
    inputItems2008.add(inputItem50_2008);
    fo2008.setInputItems(inputItems2008);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);
    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007_2, true);

    String lineKey1 = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey1);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("60");

    String lineKey2 = AccrualFormData.getLineKey(inputItem50_2007_2);
    AccrualFormData fd2 = form.getItem(lineKey2);

    fd2.setTotalStartOfYearAmount("47");
    fd2.setTotalEndOfYearAmount("67");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(2, updateList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());

    @SuppressWarnings("rawtypes")
    Iterator msgIter = messages.get();
    ActionMessage msg = (ActionMessage) msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ACCRUAL_COPY_FORWARD_MULTIPLE_RECORDS_THIS_YEAR, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("50", msgValues[0]);
    assertEquals("2007", msgValues[1]);
    assertEquals("2008", msgValues[2]);

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(2, updateList2.size());

    InventoryItem cropAdj_2007 = updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(47), cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(new Double(54), cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    InventoryItem cropAdj_2007_2 = updateList2.get(1);
    assertNotNull(cropAdj_2007_2);
    assertNotNull(cropAdj_2007_2.getFarmingOperation());
    assertNotNull(cropAdj_2007_2.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), cropAdj_2007_2.getAdjStartOfYearAmount());
    assertEquals(new Double(50), cropAdj_2007_2.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007_2.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test copy forward UPDATE for 2008.
   * With 2007 CRA record.
   * There are two records with the same code/crop unit.
   * One is updated.
   * No copy forward because we cannot be sure which should be copied.
   */
  @Test
  public final void testAddCopyForwardAdjs08() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InputItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setReportedInventoryId(new Integer(14756));
    inputItem50_2007.setReportedStartOfYearAmount(null);
    inputItem50_2007.setReportedEndOfYearAmount(new Double(10));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(30));

    InputItem inputItem50_2007_2 = TestModelBuilder.getInputItem50();
    inputItem50_2007_2.setAdjInventoryId(new Integer(35489));
    inputItem50_2007_2.setReportedInventoryId(new Integer(85124));
    inputItem50_2007_2.setReportedStartOfYearAmount(null);
    inputItem50_2007_2.setReportedEndOfYearAmount(new Double(13));
    inputItem50_2007_2.setAdjStartOfYearAmount(new Double(43));
    inputItem50_2007_2.setAdjEndOfYearAmount(new Double(33));

    List<InputItem> inputItems2007 = new ArrayList<>();
    inputItems2007.add(inputItem50_2007);
    inputItems2007.add(inputItem50_2007_2);
    fo2007.setInputItems(inputItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2008 = TestModelBuilder.getInputItem50();
    inputItem50_2008.setAdjInventoryId(new Integer(527130));
    inputItem50_2008.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2008.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2008 = new ArrayList<>();
    inputItems2008.add(inputItem50_2008);
    fo2008.setInputItems(inputItems2008);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);
    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007_2, true);

    String lineKey1 = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey1);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("60");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());

    @SuppressWarnings("rawtypes")
    Iterator msgIter = messages.get();
    ActionMessage msg = (ActionMessage) msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ACCRUAL_COPY_FORWARD_MULTIPLE_RECORDS_THIS_YEAR, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("50", msgValues[0]);
    assertEquals("2007", msgValues[1]);
    assertEquals("2008", msgValues[2]);

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    InventoryItem cropAdj_2007 = updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(new Double(50), cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test copy forward ADD for 2008. No matching record found.
   */
  @Test
  public final void testAddCopyForwardAdjs09() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InputItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2007 = new ArrayList<>();
    inputItems2007.add(inputItem50_2007);
    fo2007.setInputItems(inputItems2007);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);

    String lineKey = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("60");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());

    @SuppressWarnings("rawtypes")
    Iterator msgIter = messages.get();
    ActionMessage msg = (ActionMessage) msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ACCRUAL_COPY_FORWARD_NO_RECORD, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("50", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("$60.00", msgValues[2]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList);
    assertEquals(1, addList.size());

    InventoryItem cropAdj_2007 = updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(new Double(60), cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    InventoryItem cropAdj_2008 = addList.get(0);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("50", cropAdj_2008.getInventoryItemCode());
    assertEquals(new Double(60), cropAdj_2008.getAdjStartOfYearAmount());
    assertEquals(null, cropAdj_2008.getAdjEndOfYearAmount());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test copy forward ADD for 2008. One match found, no existing adjustment.
   */
  @Test
  public final void testAddCopyForwardAdjs10() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InputItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2007 = new ArrayList<>();
    inputItems2007.add(inputItem50_2007);
    fo2007.setInputItems(inputItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2008 = TestModelBuilder.getInputItem50();
    inputItem50_2008.setAdjInventoryId(null);
    inputItem50_2007.setReportedInventoryId(new Integer(14756));
    inputItem50_2008.setAdjStartOfYearAmount(null);
    inputItem50_2008.setAdjEndOfYearAmount(null);
    inputItem50_2008.setReportedStartOfYearAmount(new Double(40));
    List<InputItem> inputItems2008 = new ArrayList<>();
    inputItems2008.add(inputItem50_2008);
    fo2008.setInputItems(inputItems2008);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);

    String lineKey = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount("40");
    fd1.setTotalEndOfYearAmount("60");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList1 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList1);
    assertEquals(1, updateList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());

    @SuppressWarnings("rawtypes")
    Iterator msgIter = messages.get();
    ActionMessage msg = (ActionMessage) msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ACCRUAL_COPY_FORWARD, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("50", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("$60.00", msgValues[2]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_ADD));
    List<InventoryItem> addList = adjustmentsMap.get(AdjustmentService.ACTION_ADD);
    assertNotNull(addList);
    assertEquals(1, addList.size());

    InventoryItem cropAdj_2007 = updateList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(new Double(40), cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(new Double(60), cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    InventoryItem cropAdj_2008 = addList.get(0);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("50", cropAdj_2008.getInventoryItemCode());
    assertEquals(new Double(20), cropAdj_2008.getAdjStartOfYearAmount());
    assertEquals(null, cropAdj_2008.getAdjEndOfYearAmount());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test with DELETE, but no copy forward because the adjustment has no CRA record.
   */
  @Test
  public final void testAddCopyForwardAdjs11() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InputItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2007 = new ArrayList<>();
    inputItems2007.add(inputItem50_2007);
    fo2007.setInputItems(inputItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2008_1 = TestModelBuilder.getInputItem50();
    inputItem50_2008_1.setAdjStartOfYearAmount(new Double(10));
    inputItem50_2008_1.setAdjEndOfYearAmount(new Double(90));
    List<InputItem> inputItems2008 = new ArrayList<>();
    inputItems2008.add(inputItem50_2008_1);
    fo2008.setInputItems(inputItems2008);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);

    String lineKey = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setDeleted(true);
    fd1.setTotalStartOfYearAmount(null);
    fd1.setTotalEndOfYearAmount(null);

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> deleteList1 = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertNotNull(deleteList1);
    assertEquals(1, deleteList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(true, messages.isEmpty());

    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> deleteList2 = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertNotNull(deleteList2);
    assertEquals(1, deleteList2.size());

    InventoryItem cropAdj_2007 = deleteList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(null, cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }


  /**
   * Test with DELETE, and copy forward because it reverts to the CRA record.
   * Test copy forward UPDATE for 2008.
   */
  @Test
  public final void testAddCopyForwardAdjs12() throws Exception {
    AccrualsForm form = new AccrualsForm();
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
    InputItem inputItem50_2007 = TestModelBuilder.getInputItem50();
    inputItem50_2007.setAdjInventoryId(new Integer(529730));
    inputItem50_2007.setReportedInventoryId(new Integer(50469));
    inputItem50_2007.setReportedStartOfYearAmount(null);
    inputItem50_2007.setReportedEndOfYearAmount(new Double(10));
    inputItem50_2007.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2007.setAdjEndOfYearAmount(new Double(30));
    List<InputItem> inputItems2007 = new ArrayList<>();
    inputItems2007.add(inputItem50_2007);
    fo2007.setInputItems(inputItems2007);

    FarmingOperation fo2008 = rs2008.getFarmingYear().getFarmingOperationBySchedule("A");
    InputItem inputItem50_2008 = TestModelBuilder.getInputItem50();
    inputItem50_2008.setAdjInventoryId(new Integer(527130));
    inputItem50_2008.setAdjStartOfYearAmount(new Double(40));
    inputItem50_2008.setAdjEndOfYearAmount(new Double(40));
    List<InputItem> inputItems2008 = new ArrayList<>();
    inputItems2008.add(inputItem50_2008);
    fo2008.setInputItems(inputItems2008);

    AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, inputItem50_2007, true);

    String lineKey = AccrualFormData.getLineKey(inputItem50_2007);
    AccrualFormData fd1 = form.getItem(lineKey);

    fd1.setTotalStartOfYearAmount(null);
    fd1.setTotalEndOfYearAmount("10");

    ActionMessages errors = new ActionMessages();
    Map<String, List<InventoryItem>> adjustmentsMap = AccrualsAdjustmentUtils.parseAdjustments(form, fo2007, errors);

    assertEquals(true, errors.isEmpty());
    assertEquals(1, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> deleteList1 = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertNotNull(deleteList1);
    assertEquals(1, deleteList1.size());

    ActionMessages messages = new ActionMessages();
    AccrualsAdjustmentUtils.addCopyForwardAdjustments(fo2007, adjustmentsMap, messages);

    assertEquals(1, messages.size());

    @SuppressWarnings("rawtypes")
    Iterator msgIter = messages.get();
    ActionMessage msg = (ActionMessage) msgIter.next();
    assertNotNull(msg);
    assertEquals(MessageConstants.ACCRUAL_COPY_FORWARD, msg.getKey());
    Object[] msgValues = msg.getValues();
    assertEquals(3, msgValues.length);
    assertEquals("50", msgValues[0]);
    assertEquals("2008", msgValues[1]);
    assertEquals("$10.00", msgValues[2]);

    assertEquals(2, adjustmentsMap.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_DELETE));
    List<InventoryItem> deleteList2 = adjustmentsMap.get(AdjustmentService.ACTION_DELETE);
    assertNotNull(deleteList2);
    assertEquals(1, deleteList2.size());
    assertEquals(true, adjustmentsMap.keySet().contains(AdjustmentService.ACTION_UPDATE));
    List<InventoryItem> updateList2 = adjustmentsMap.get(AdjustmentService.ACTION_UPDATE);
    assertNotNull(updateList2);
    assertEquals(1, updateList2.size());

    InventoryItem cropAdj_2007 = deleteList2.get(0);
    assertNotNull(cropAdj_2007);
    assertNotNull(cropAdj_2007.getFarmingOperation());
    assertNotNull(cropAdj_2007.getFarmingOperation().getFarmingOperationId());
    assertEquals(null, cropAdj_2007.getAdjStartOfYearAmount());
    assertEquals(null, cropAdj_2007.getAdjEndOfYearAmount());
    assertEquals(new Integer(2007), cropAdj_2007.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());

    InventoryItem cropAdj_2008 = updateList2.get(0);
    assertNotNull(cropAdj_2008);
    assertNotNull(cropAdj_2008.getFarmingOperation());
    assertNotNull(cropAdj_2008.getFarmingOperation().getFarmingOperationId());
    assertNotNull(cropAdj_2008.getInventoryItemCode());
    assertEquals("50", cropAdj_2008.getInventoryItemCode());
    assertEquals(new Double(10), cropAdj_2008.getAdjStartOfYearAmount());
    assertEquals(new Double(40), cropAdj_2008.getAdjEndOfYearAmount());
    assertEquals(new Integer(2008), cropAdj_2008.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear());
  }

}
