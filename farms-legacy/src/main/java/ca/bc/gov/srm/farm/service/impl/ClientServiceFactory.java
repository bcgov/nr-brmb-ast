/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.*;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.dao.ReadDAO;
import ca.bc.gov.srm.farm.dao.ReadDAO.FmvFullResult;
import ca.bc.gov.srm.farm.dao.ReasonabilityReadDAO;
import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.PreVerificationChecklist;
import ca.bc.gov.srm.farm.domain.ProducedItem;
import ca.bc.gov.srm.farm.domain.ProductionInsurance;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioLog;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.ScenarioStateAudit;
import ca.bc.gov.srm.farm.domain.WholeFarmParticipant;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioBpuPurposeCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.service.PreVerificationService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ReferenceScenarioComparator;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.webade.dbpool.WrapperConnection;
import oracle.jdbc.OracleConnection;

/**
 * @author dzwiers
 */
public final class ClientServiceFactory implements ClientService {

  private ReadDAO dao = null;
  private ReasonabilityReadDAO reasonabilityDAO = null;

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceFactory.class);

  private ClientServiceFactory(final OracleConnection pConnection) {
    dao = new ReadDAO(pConnection);
    reasonabilityDAO = new ReasonabilityReadDAO(pConnection);
  }

  @SuppressWarnings("resource")
  public static ClientService getInstance(final Connection pConnection) {

    if (pConnection instanceof WrapperConnection) {
      WrapperConnection wr = (WrapperConnection) pConnection;

      if (wr.getWrappedConnection() instanceof OracleConnection) {
        return new ClientServiceFactory((OracleConnection) wr
            .getWrappedConnection());
      }
      return null;
    }
    
    if (pConnection instanceof OracleConnection) {
      return new ClientServiceFactory((OracleConnection) pConnection);
    }
    return null;
  }
  
  /**
   * @param pPin int
   * @param pProgramYear int
   * @param pScenarioNumber Integer
   * @param mode String 
   * @return Scenario
   * @throws ServiceException ServiceException
   * @see ca.bc.gov.srm.farm.service.ClientService#getClientInfoWithHistory(int, int, Integer, String)
   */
  @Override
  public Scenario getClientInfoWithHistory(final int pPin,
      final int pProgramYear, final Integer pScenarioNumber, final String mode) throws ServiceException {
    return getClientInfo(pPin, pProgramYear, pScenarioNumber, mode, true);
  }

  /**
   * @param pPin int
   * @param pProgramYear int
   * @param pScenarioNumber Integer
   * @param mode String 
   * @return Scenario
   * @throws ServiceException ServiceException
   * @see ca.bc.gov.srm.farm.service.ClientService#getClientInfoWithHistory(int, int, Integer, String)
   */
  @Override
  public Scenario getClientInfoWithoutHistory(final int pPin,
      final int pProgramYear, final Integer pScenarioNumber, final String mode) throws ServiceException {
    return getClientInfo(pPin, pProgramYear, pScenarioNumber, mode, false);
  }

  /**
   * @param pPin int
   * @param pProgramYear int
   * @param pScenarioNumber Integer
   * @param mode String 
   * @param includeHistory boolean
   * @return Scenario
   * @throws ServiceException ServiceException
   * @see ca.bc.gov.srm.farm.service.ClientService#getClientInfoWithHistory(int, int, Integer, String)
   */
  private Scenario getClientInfo(final int pPin, final int pProgramYear, final Integer pScenarioNumber,
      final String mode, final boolean includeHistory) throws ServiceException {

    Client client = null;
    List<ScenarioMetaData> metadata = null;
    try {
      client = dao.readClient(new Integer(pPin));

      metadata = dao.readProgramYearMetadata(new Integer(pPin), new Integer(pProgramYear));
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario program year meta data", e);

      throw new ServiceException(e);
    }

    if(mode == null || !(mode.equals(ClientService.COMP_FIRST_MODE)
                         || mode.equals(ClientService.DEF_FIRST_MODE)
                         || mode.equals(ClientService.ENROLMENT_MODE))){
      String reason = "Undefined Scenario selection Mode - received: " + mode;
      LOGGER.error(reason);
      throw new ServiceException(reason);
    }
    
    Scenario sc = new Scenario();
    sc.setYear(new Integer(pProgramYear));
    sc.setClient(client);
    sc.setScenarioMetaDataList(metadata);

    int[][] ids = null;
    try {
      int[][] allIds = dao.readPyIds(new Integer(pPin), new Integer(pProgramYear),
          pScenarioNumber, mode);
      if(includeHistory) {
        ids = allIds;
      } else {
        ids = new int[1][allIds[0].length];
        for(int ii = 0; ii < allIds.length; ii++) {
          if((new Integer(pProgramYear)).equals(new Integer(allIds[ii][0]))) {
            ids[0] = allIds[ii];
          }
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario IDs", e);

      sc.setScenarioNumber(pScenarioNumber);
      sc.setReferenceScenarios(new ArrayList<ReferenceScenario>());
      return sc;
    }

    // ids is [year][py_id][pyv_id][sc_id]
    // step one is to create the data structure
    // step two is to populate it with attributes
    List<ReferenceScenario> referenceScenarios = new ArrayList<>();

    if (ids == null) {
      sc.setScenarioNumber(pScenarioNumber);
      sc.setReferenceScenarios(new ArrayList<ReferenceScenario>());
      sc.setIsInCombinedFarmInd(false);
      return sc;
    }

    Integer[] pyvIds = new Integer[ids.length];
    Integer[] scIds = new Integer[ids.length];

    for (int i = 0; i < ids.length; i++) {
      ReferenceScenario ref = null;
      if (ids[i] != null) {

        pyvIds[i] = new Integer(ids[i][2]);
        scIds[i] = new Integer(ids[i][3]);

        if ((new Integer(pProgramYear)).equals(new Integer(ids[i][0]))) {
          ref = sc;
        } else {
          ref = new ReferenceScenario();
          referenceScenarios.add(ref);
        }

        ref.setYear(new Integer(ids[i][0]));
        ref.setScenarioId(new Integer(ids[i][3]));
        ref.setFarmingYear(new FarmingYear());
        ref.getFarmingYear().setProgramYearId(new Integer(ids[i][1]));
        ref.getFarmingYear().setProgramYearVersionId(new Integer(ids[i][2]));
      }
    }

    Collections.sort(referenceScenarios,ReferenceScenarioComparator.getInstance());
    sc.setReferenceScenarios(referenceScenarios);

    try {
      if(sc.getScenarioId() != null) {
        List<ScenarioStateAudit> scenarioStateAudits = dao.readScenarioStateAudits(sc.getScenarioId());
        sc.setScenarioStateAudits(scenarioStateAudits);
        
        List<ScenarioLog> scenarioLogs = dao.readScenarioLogs(sc.getScenarioId());
        sc.setScenarioLogs(scenarioLogs);
      }
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario logs", e);

      return sc;
    }
    
    try {
      // return by reference
      dao.readProgramYear(pyvIds, sc);
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario program year", e);
      
      return sc;
    }

    Date verifiedDate = null;
    try {
      // return by reference
      dao.readScenario(scIds, sc);

      if (ScenarioStateCodes.VERIFIED.equals(sc.getScenarioStateCode())) {
        verifiedDate = sc.getWhenUpdatedTimestamp();
      }
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario and reference scenarios", e);
      throw new ServiceException(e);
    }

    try {
      loadWholeFarms(pyvIds, sc);
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario whole farms", e);
      throw new ServiceException(e);
    }

    HashMap<Integer, FarmingOperation> opMap = null;
    try {
      opMap = loadOperations(pyvIds, sc);
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario farming operations", e);
      throw new ServiceException(e);
    }

    try {
      // loads operations children
      Integer[] opIds = opMap.keySet().toArray(
          new Integer[opMap.keySet().size()]);

      loadOperationalPartn(opIds, opMap);
      loadProductionIns(opIds, opMap);
      
      loadProdUnit(opIds, scIds, opMap);
      loadSuppInv(opIds, scIds, opMap, verifiedDate);
      loadInvCropConvInfo(opIds, scIds, opMap);
      loadIncomeExpense(opIds, scIds, opMap, pProgramYear, verifiedDate);

      loadClaim(scIds, sc);
      loadTotalMargin(scIds, sc);
      loadMargin(scIds, opMap);
      
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario data", e);
      throw new ServiceException(e);
    }

    try {
      if(sc.getFarmingYear()!=null && sc.getFarmingYear().getProgramYearId() != null){
        // only get comments for program year scenario
        String[] verificationNotes = dao.readVerificationNotes(sc.getFarmingYear().getProgramYearId());
        int index = 0;
        
        sc.setInterimVerificationNotes(verificationNotes[index++]);
        sc.setFinalVerificationNotes(verificationNotes[index++]);
        sc.setAdjustmentVerificationNotes(verificationNotes[index++]);
      }
    } catch (SQLException e) {
      LOGGER.error("Error reading verification notes", e);
      throw new ServiceException(e);
    }
    
    try {
      if(sc.getFarmingYear()!=null && sc.getFarmingYear().getProgramYearId() != null){
        // return by reference
        dao.readFarmType(sc);
      }
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario sector", e);
      throw new ServiceException(e);
    }

    // bpu
    try {
      loadBPU(sc);
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario BPUs", e);
      throw new ServiceException(e);
    }

    // fmv
    try {
      loadFMV(sc);
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario FMVs", e);
      throw new ServiceException(e);
    }
    
    try {
      if(sc.getScenarioId() != null) {
        sc.setBenefitDocCreatedDate(dao.readCobGenerationDate(sc.getScenarioId()));
      }
    } catch (SQLException e) {
      LOGGER.error("Error reading COB generation date", e);
      throw new ServiceException(e);
    }
    
    try {
      sc.setEnrolment(dao.readEnrolment(pPin, pProgramYear));
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario Enrolment", e);
      throw new ServiceException(e);
    }
    
    try {
      sc.setEnwEnrolment(dao.readEnwEnrolment(sc.getScenarioId()));
    } catch (SQLException e) {
      LOGGER.error("Error reading scenario Enrolment", e);
      throw new ServiceException(e);
    }
    
    try {
      if(sc.getCombinedFarmNumber() != null) {
        sc.setCombinedFarmClients(dao.readCombinedFarmClients(sc.getCombinedFarmNumber()));
      }
    } catch (SQLException e) {
      LOGGER.error("Error reading combined farm clients", e);
      throw new ServiceException(e);
    }
    
    try {
      sc.setReasonabilityTestResults(reasonabilityDAO.readReasonabilityTestResults(sc, verifiedDate));
    } catch (SQLException e) {
      LOGGER.error("Error reading reasonability test results", e);
      throw new ServiceException(e);
    }

    if(sc.isPreVerification()) {
      PreVerificationService preVerificationService = ServiceFactory.getPreVerificationService();
      PreVerificationChecklist preVerificationChecklist = preVerificationService.getPreVerificationChecklist(sc);
      sc.setPreVerificationChecklist(preVerificationChecklist);
    }
    
    return sc;
  }

  /**
   * @param sc Scenario
   * @throws SQLException On Exception
   */
  private void loadFMV(Scenario sc) throws SQLException {
    if(sc == null) {
      return;
    }

    // FMVs are loaded for each operation individually - makes it simpler
    for( ReferenceScenario ref : sc.getAllScenarios()) {
      if(ref.getFarmingYear()!=null && ref.getFarmingYear().getFarmingOperationCount() > 0) {
        for(FarmingOperation op : ref.getFarmingYear().getFarmingOperations()){
          if(op!=null && ((op.getCropItems() != null && op.getCropItems().size()>0) || (op.getLivestockItems() != null && op.getLivestockItems().size()>0))) {
            // There is at least one return value possible, so do the query
            HashMap<String, List<ReadDAO.FmvFullResult>> fmvs = dao.readFairMarketValue(op.getFarmingOperationId());
            if(fmvs != null){
              for(ProducedItem ci : op.getProducedItems()){
                List<FmvFullResult> fmv = fmvs.get(ci.getInventoryItemCode());
                if(fmv!=null && fmv.size() > 0){
                  if(fmv.size() > 1 && ci instanceof LivestockItem){
                    throw new SQLException("More than one FMV found for a livestock - means there was two crop unit types found. Scenario Id: "
                        + ref.getScenarioId());
                  }
                  for(FmvFullResult f : fmv){
                    if(f!=null && cropUnitMatches(ci, f.getCropUnit())){
                      ci.setFmvStart(f.getStartPrice());
                      ci.setFmvEnd(f.getEndPrice());
                      ci.setFmvVariance(f.getVariance());
                      ci.setFmvAverage(f.getYearAvg());
                      ci.setFmvPreviousYearEnd(f.getPreviousYearEnd());
                    }
                  }
                }
              }
              
            }
          } 
        }
      }
    }

    boolean hasAppleInventory = ScenarioUtils.hasInventoryOfFruitVegType(sc, FruitVegTypeCodes.APPLE);
    if(hasAppleInventory) {
      final String inventoryCode = InventoryItemCodes.APPLES;
      for(FarmingOperation op : sc.getFarmingYear().getFarmingOperations()){
        HashMap<String,List<FmvFullResult>> fmvs = dao.readFairMarketValue(op.getFarmingOperationId(),
            inventoryCode, CropUnitCodes.POUNDS);
        
        if(fmvs != null){
          List<FmvFullResult> fmv = fmvs.get(inventoryCode);
          if(fmv != null) {
            for(FmvFullResult f : fmv) {
              String cropUnitCode = f.getCropUnit();
              if(CropUnitCodes.POUNDS.equals(cropUnitCode)) {
                Double applePrice = f.getEndPrice();
                if(applePrice == null) {
                  applePrice = f.getStartPrice();
                }
                op.setAppleFmvPrice(applePrice);
                break;
              }
            }
          }
        }
      }
    }
    
    boolean hasCherryInventory = ScenarioUtils.hasInventoryOfFruitVegType(sc, FruitVegTypeCodes.CHERRY);
    if(hasCherryInventory) {
      final String inventoryCode = InventoryItemCodes.CHERRIES;
      for(FarmingOperation op : sc.getFarmingYear().getFarmingOperations()){
        HashMap<String,List<FmvFullResult>> fmvs = dao.readFairMarketValue(op.getFarmingOperationId(),
            inventoryCode, CropUnitCodes.POUNDS);
        
        if(fmvs != null){
          List<FmvFullResult> fmv = fmvs.get(inventoryCode);
          if(fmv != null) {
            for(FmvFullResult f : fmv) {
              String cropUnitCode = f.getCropUnit();
              if(CropUnitCodes.POUNDS.equals(cropUnitCode)) {
                Double price = f.getEndPrice();
                if(price == null) {
                  price = f.getStartPrice();
                }
                op.setCherryFmvPrice(price);
                break;
              }
            }
          }
        }
      }
    }
    
    if(ScenarioUtils.hasProductiveUnitsWithStructureGroupCode(sc, StructureGroupCodes.CHICKEN_BROILERS)) {
      for(FarmingOperation op : sc.getFarmingYear().getFarmingOperations()){
        HashMap<String, List<ReadDAO.FmvFullResult>> fmvs = dao.readFairMarketValue(op.getFarmingOperationId(),
            InventoryItemCodes.POU_BR_CHICKEN, CropUnitCodes.getLivestockUnitCode(InventoryItemCodes.POU_BR_CHICKEN));
        Double chickenBroilerFMVPrice = getFMVForInvCode(fmvs, InventoryItemCodes.POU_BR_CHICKEN);
        op.setChickenBroilerFMVPrice(chickenBroilerFMVPrice);
      }
    }
    
    if(ScenarioUtils.hasProductiveUnitsWithStructureGroupCode(sc, StructureGroupCodes.TURKEY_BROILERS)) {
      for(FarmingOperation op : sc.getFarmingYear().getFarmingOperations()){
        HashMap<String, List<ReadDAO.FmvFullResult>> fmvs = dao.readFairMarketValue(op.getFarmingOperationId(),
            InventoryItemCodes.TURKEYS_6KG_TO_8KG, CropUnitCodes.getLivestockUnitCode(InventoryItemCodes.TURKEYS_6KG_TO_8KG));
        Double turkeyBroilerFMVPrice = getFMVForInvCode(fmvs, InventoryItemCodes.TURKEYS_6KG_TO_8KG);
        op.setTurkeyBroilerFMVPrice(turkeyBroilerFMVPrice);
      }
    }
    
    if(ScenarioUtils.hasProductiveUnitsWithStructureGroupCode(sc, StructureGroupCodes.CHICKEN_EGGS_HATCH)) {
      for(FarmingOperation op : sc.getFarmingYear().getFarmingOperations()){
        HashMap<String, List<ReadDAO.FmvFullResult>> fmvs = dao.readFairMarketValue(op.getFarmingOperationId(),
            InventoryItemCodes.CHICKEN_EGGS_HATCH, CropUnitCodes.getLivestockUnitCode(InventoryItemCodes.CHICKEN_EGGS_HATCH));
        Double chickenEggsHatchFMVPrice = getFMVForInvCode(fmvs, InventoryItemCodes.CHICKEN_EGGS_HATCH);
        op.setChickenEggsHatchFMVPrice(chickenEggsHatchFMVPrice);
      }
    }
    
    if(ScenarioUtils.hasProductiveUnitsWithStructureGroupCode(sc, StructureGroupCodes.CHICKEN_EGGS_CONSUMPTION)) {
      for(FarmingOperation op : sc.getFarmingYear().getFarmingOperations()){
        HashMap<String, List<ReadDAO.FmvFullResult>> fmvs = dao.readFairMarketValue(op.getFarmingOperationId(),
            InventoryItemCodes.CHICKEN_EGGS_CONSUMP, CropUnitCodes.getLivestockUnitCode(InventoryItemCodes.CHICKEN_EGGS_HATCH));
        Double chickenEggsConsumpFMVPrice = getFMVForInvCode(fmvs, InventoryItemCodes.CHICKEN_EGGS_CONSUMP);
        op.setChickenEggsConsumpFMVPrice(chickenEggsConsumpFMVPrice);
      }
    }
  }
  
  /* Returns the first FMV val end found for the inventory code */
  private Double getFMVForInvCode(HashMap<String, List<ReadDAO.FmvFullResult>> fmvs, String invItemCode) {
    Double fairMarketVal = null;
    
    if(fmvs != null){
      List<FmvFullResult> fmv = fmvs.get(invItemCode);
      if (fmv != null) {
        for(FmvFullResult f : fmv) {
          fairMarketVal = f.getEndPrice();
          if(fairMarketVal == null) {
            fairMarketVal = f.getStartPrice();
          }
          break;
        }
      }
    }
    
    return fairMarketVal;
  }

  private boolean cropUnitMatches(ProducedItem producedItem, Object cropUnitCode) {
    boolean result = false;
    if(cropUnitCode != null) {
      if(producedItem instanceof CropItem) {
        CropItem cropItem = (CropItem) producedItem;
        result = cropUnitCode.equals(cropItem.getCropUnitCode());
      } else if (producedItem instanceof LivestockItem){
        LivestockItem livestockItem = (LivestockItem) producedItem;
        result = CropUnitCodes.getLivestockUnitCode(livestockItem.getInventoryItemCode()).equals(cropUnitCode);
      }
    }
    return result;
  }

  /**
   * @param sc Scenario
   * @throws SQLException
   *           SQLException
   */
  private void loadBPU(Scenario sc) throws SQLException {

    if (sc == null) {
      return;
    }
    
    //
    List<Integer> scenarioIds = new ArrayList<>();
    
    // we need two lists of codes - structures and inventories
    List<String> invCodes = new ArrayList<>();
    List<String> structCodes = new ArrayList<>();
    
    getBpuCodes(sc.getFarmingYear(), invCodes, structCodes);
    scenarioIds.add(sc.getScenarioId());
    
    if(sc.getReferenceScenarios()!=null && sc.getReferenceScenarios().size()>0){
      for(ReferenceScenario r : sc.getReferenceScenarios()){
        if(r!= null){
          getBpuCodes(r.getFarmingYear(), invCodes, structCodes);
          scenarioIds.add(r.getScenarioId());
        }
      }
    }
    
    boolean isCoverageNotice = sc.categoryIsOneOf(COVERAGE_NOTICE);
    boolean isCompletedCoverageNotice = sc.stateIsOneOf(COMPLETED) && isCoverageNotice;
    
    HashMap<String, BasePricePerUnit>[] bpus;
    HashMap<String, BasePricePerUnit>[] earlierPyBpus = null; // BPU set for the program year minus 4
    
    if(sc.stateIsOneOf(VERIFIED, AMENDED) || isCompletedCoverageNotice) {
      //
      // Get the BPUs that were used to calculate the benefit when it was verified
      //
      bpus = dao.readBasePricePerUnitXrefs(scenarioIds, ScenarioBpuPurposeCodes.STANDARD);
      
      if(isCoverageNotice) {
        earlierPyBpus = dao.readBasePricePerUnitXrefs(scenarioIds, ScenarioBpuPurposeCodes.COVERAGE_NOTICE_ADDITIONAL);
      }
      
    } else {
      //
      // Get the BPUs for the given year
      //
      bpus = dao.readBasePricePerUnit(invCodes, structCodes, sc.getScenarioId(), sc.getYear());
      
      if(isCoverageNotice) {
        final int earlierBpuSetYear = sc.getYear() - CalculatorConfig.COVERAGE_NOTICE_ADDITIONAL_BPU_YEARS;
        earlierPyBpus = dao.readBasePricePerUnit(invCodes, structCodes, sc.getScenarioId(), earlierBpuSetYear);
      }
    }

    HashMap<String, BasePricePerUnit> invBpus = bpus[0];
    HashMap<String, BasePricePerUnit> structBpus = bpus[1];
    
    sc.setInventoryBpus(invBpus);
    sc.setStructureGroupBpus(structBpus);

    if(earlierPyBpus != null) {
      HashMap<String, BasePricePerUnit> earlierPyInvBpus = earlierPyBpus[0];
      HashMap<String, BasePricePerUnit> earlierPyStructBpus = earlierPyBpus[1];
      
      sc.setEarlierProgramYearInventoryBpus(earlierPyInvBpus);
      sc.setEarlierProgramYearStructureGroupBpus(earlierPyStructBpus);
      
      addEarlierBpuYears(sc.getYear(), invBpus, earlierPyInvBpus);
      addEarlierBpuYears(sc.getYear(), structBpus, earlierPyStructBpus);
    }
    
    setBpus(sc.getFarmingYear(), invBpus, structBpus);

    if(sc.getReferenceScenarios()!=null && sc.getReferenceScenarios().size()>0){
      for(ReferenceScenario r : sc.getReferenceScenarios()){
        if(r!= null){
          setBpus(r.getFarmingYear(), invBpus, structBpus);
        }
      }
    }
  }

  private void addEarlierBpuYears(Integer programYear,
      HashMap<String, BasePricePerUnit> structBpus,
      HashMap<String, BasePricePerUnit> earlierPyStructBpus) {
    
    if(structBpus != null && earlierPyStructBpus != null) {
      final int maxBpuYear = programYear - CalculatorConfig.BPU_YEARS_IN_A_SET - 1;
      final int minBpuYear = maxBpuYear - CalculatorConfig.COVERAGE_NOTICE_ADDITIONAL_BPU_YEARS + 1;
      
      for(String lineCode : structBpus.keySet()) {
        BasePricePerUnit bpu = structBpus.get(lineCode);
        
        List<BasePricePerUnitYear> bpuYears = bpu.getBasePricePerUnitYears();      
        BasePricePerUnit earlierBpu = earlierPyStructBpus.get(lineCode);
        
        if(earlierBpu != null) {
          List<BasePricePerUnitYear> earlierBpuYears = earlierBpu.getBasePricePerUnitYears();
          for(BasePricePerUnitYear earlierBpuYear : earlierBpuYears) {
            if(earlierBpuYear.getYear() >= minBpuYear && earlierBpuYear.getYear() <= maxBpuYear) {
              bpuYears.add(earlierBpuYear);
            }
          }
        }
      }
    }
  }
  
  
  /**
   * 
   * @param fy fy
   * @param invCodes invCodes
   * @param structCodes structCodes
   */
  private void getBpuCodes(FarmingYear fy, List<String> invCodes, List<String> structCodes) {
    if (fy == null || fy.getFarmingOperationCount() == 0) {
      return;
    }

    invCodes.addAll(
      fy.getFarmingOperations().stream()
        .flatMap(op -> op.getAllProductiveUnitCapacitiesForStructureChange().stream())
        .filter(puc -> puc.getInventoryItemCode() != null)
        .map(puc -> puc.getInventoryItemCode())
        .distinct()
        .collect(Collectors.toList())
    );

    structCodes.addAll(
      fy.getFarmingOperations().stream()
        .flatMap(op -> op.getAllProductiveUnitCapacitiesForStructureChange().stream())
        .filter(puc -> puc.getStructureGroupCode() != null)
        .map(puc -> puc.getStructureGroupCode())
        .distinct()
        .collect(Collectors.toList())
    );
  }
  
  
  /**
   * Update ProductiveUnitCapacities with their  BasePricePerUnits
   * @param fy fy
   * @param invBpus invBpus
   * @param structBpus structBpus
   */
  private void setBpus(FarmingYear fy, HashMap<String, BasePricePerUnit> invBpus, HashMap<String, BasePricePerUnit> structBpus) {
    if (fy == null || fy.getFarmingOperationCount() == 0) {
      return;
    }

    fy.getFarmingOperations().stream()
      .flatMap(op -> op.getAllProductiveUnitCapacitiesForStructureChange().stream())
      .filter(puc -> puc != null)
      .forEach(puc -> {
        if (invBpus != null && puc.getInventoryItemCode() != null) {
          BasePricePerUnit bpu = invBpus.get(puc.getInventoryItemCode());
          if (bpu != null) {
            puc.setBasePricePerUnit(bpu);
          }
        }
        if (structBpus != null && puc.getStructureGroupCode() != null) {
          BasePricePerUnit bpu = structBpus.get(puc.getStructureGroupCode());
          if (bpu != null) {
            puc.setBasePricePerUnit(bpu);
          }
        }
      });
  }

  /**
   * 
   * @param pyvIds Integer[]
   * @param sc Scenario
   * @return HashMap
   * @throws SQLException
   *           SQLException
   */
  private HashMap<Integer, FarmingOperation> loadOperations(final Integer[] pyvIds, final Scenario sc) throws SQLException {
    HashMap<Integer, List<FarmingOperation>> map = dao.readOperation(pyvIds);

    List<FarmingOperation> operations = new ArrayList<>();
    if (map != null) {
      for (Integer pyvId : map.keySet()) {
        if (pyvId != null) {
          List<FarmingOperation> l = map.get(pyvId);
          Collections.sort(l, new Comparator<FarmingOperation>() {
            @Override
            public int compare(FarmingOperation o1, FarmingOperation o2) {
              FarmingOperation op1 = o1;
              FarmingOperation op2 = o2;
              return op1.getSchedule().compareTo(op2.getSchedule());
            }
          });
          if (l != null) {
            // find pyv
            if (sc.getFarmingYear() != null
                && pyvId.equals(sc.getFarmingYear().getProgramYearVersionId())) {
              if (sc.getFarmingYear().getFarmingOperations() != null) {
                throw new SQLException(
                    "Farming operations for the Current Scenario already exist!");
              }
              sc.getFarmingYear().setFarmingOperations(l);
              operations.addAll(l);
            } else {
              if (sc.getReferenceScenarios() != null) {
                for (ReferenceScenario rs : sc.getReferenceScenarios()) {
                  if (rs.getFarmingYear() != null
                      && pyvId.equals(rs.getFarmingYear()
                          .getProgramYearVersionId())) {
                    if (rs.getFarmingYear().getFarmingOperations() != null) {
                      throw new SQLException(
                          "Farming operations for a Reference Scenario already exist!");
                    }
                    rs.getFarmingYear().setFarmingOperations(l);
                    operations.addAll(l);
                  }
                }
              }
            }
          }
        }
      }

      HashMap<String, List<FarmingOperation>> opMap = new HashMap<>();
      HashMap<Integer, FarmingOperation> opMap2 = new HashMap<>();
      for (FarmingOperation op : operations) {
        if (op != null) {
          opMap2.put(op.getFarmingOperationId(),op);
          List<FarmingOperation> l = opMap.get(op.getSchedule());
          if(l == null){
            l = new ArrayList<>();
            opMap.put(op.getSchedule(),l);
          }
          l.add(op);
        }
      }
      
      for(List<FarmingOperation> l : opMap.values()) {
        
        Collections.sort(l, new Comparator<FarmingOperation>(){

          @Override
          public int compare(FarmingOperation pO1, FarmingOperation pO2) {
            FarmingOperation op1 = pO1;
            FarmingOperation op2 = pO2;

            FarmingYear fy1 = op1!=null?op1.getFarmingYear():null;
            FarmingYear fy2 = op2!=null?op2.getFarmingYear():null;

            if(fy1 == null){
              return fy2 == null?0:-1;
            }
            
            if(fy2 == null){
              return 1;
            }

            ReferenceScenario rs1 = fy1.getReferenceScenario();
            ReferenceScenario rs2 = fy2.getReferenceScenario();

            if(rs1 == null){
              return rs2 == null?0:-1;
            }
            
            if(rs2 == null){
              return 1;
            }

            Integer i1 = rs1.getYear();
            Integer i2 = rs2.getYear();

            if(i1 == null){
              return i2 == null?0:-1;
            }
            
            if(i2 == null){
              return 1;
            }
            
            return i1.compareTo(i2);
          }
          
        });
        
        // ordered oldest to newest
        Iterator<FarmingOperation> j = l.iterator();
        FarmingOperation opPrev = j.next();
        while(j.hasNext()){
          FarmingOperation op = j.next();
          if(op!=null){
            if(opPrev != null){
              op.setPreviousYearFarmingOperation(opPrev);
            }
            opPrev = op;
          }
        }
      }
      
      return opMap2;
    }
    return null;
  }
  
  /**
   * @param opIds Integer[]
   * @param scIds Integer[]
   * @param opMap HashMap
   * 
   * @throws SQLException
   *           
   * Will add a CropUnitConversion to each Inventory Crop Items contained within FarmingOperation         
   */  
  private void loadInvCropConvInfo(final Integer[] opIds, final Integer[] scIds, final HashMap<Integer, FarmingOperation> opMap) 
    throws SQLException {
    
    HashMap<String, CropUnitConversion> invCropConversions = dao.readSupplementalInvCropConvInfo(opIds, scIds);
    
    for (Integer opId: opMap.keySet()) {
      
      FarmingOperation op = opMap.get(opId);
      if (op.getCropItems() == null) {
        // If there are no crop items in this Farming Operation, move on to next one
        continue;
      }
      
      for (CropItem cropItem : op.getCropItems()) {        
        if (invCropConversions.containsKey(cropItem.getInventoryItemCode())) {
          cropItem.setCropUnitConversion(invCropConversions.get(cropItem.getInventoryItemCode()));
        }
      }                           
    }        
  }

  /**
   * @param opIds Integer[]
   * @param scIds Integer[]
   * @param opMap HashMap
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void loadSuppInv(final Integer[] opIds, final Integer[] scIds, final HashMap<Integer, FarmingOperation> opMap,
      final Date verifiedDate)
      throws SQLException {
    HashMap<Integer, List<Object>[]> opp = dao.readSupplementalInv(opIds, scIds, verifiedDate);

    if (opp != null) {
      for(Integer opId : opp.keySet()){
        if(opId != null){
          List[] opps = opp.get(opId);
          if(opps!=null){
            FarmingOperation op = opMap.get(opId);
            if(op == null){
              throw new SQLException("Mis-matched Operation Ids found for "+opId);
            }
  
            if (op.getInventoryItems().size()>0) {
              throw new SQLException(
                  "Duplicate sets of Farming Inventory found for "
                      + opId);
            }
            // Array is [Crop, Livestock, Payable, Input, Receivable]
            op.setCropItems(opps[0]);
            op.setLivestockItems(opps[1]);
            op.setPayableItems(opps[2]);
            op.setInputItems(opps[3]);
            op.setReceivableItems(opps[4]);
          }
        }
      }
    }
  }

  /**
   * @param opIds Integer[]
   * @param scIds Integer[]
   * @param opMap HashMap
   * 
   * @throws SQLException
   *           SQLException
   */
  private void loadProdUnit(final Integer[] opIds, final Integer[] scIds, final HashMap<Integer, FarmingOperation> opMap)
      throws SQLException {
    HashMap<Integer, List<ProductiveUnitCapacity>> opp = dao.readProductiveUnitCapacity(opIds, scIds);

    if (opp != null) {
      for (Integer opId : opp.keySet()) {
        if (opId != null) {
          List<ProductiveUnitCapacity> pucs = opp.get(opId);
          if (pucs != null) {
            FarmingOperation op = opMap.get(opId);
            if (op == null) {
              throw new SQLException("Mis-matched Operation Ids found for "
                  + opId);
            }

            if (op.getAllProductiveUnitCapacitiesForStructureChange() != null && !op.getAllProductiveUnitCapacitiesForStructureChange().isEmpty()) {
              throw new SQLException(
                  "Duplicate sets of Farming Productive Units found for "
                      + opId);
            }
            
            op.setProductiveUnitCapacities(pucs);
          }
        }
      }
    }
  }

  /**
   * @param opIds Integer[]
   * @param scIds Integer[]
   * @param opMap HashMap
   * @param programYear programYear
   * @param verifiedDate Date
   * @throws SQLException
   *           SQLException
   */
  private void loadIncomeExpense(
      final Integer[] opIds,
      final Integer[] scIds,
      final HashMap<Integer, FarmingOperation> opMap,
      final int programYear,
      final Date verifiedDate) throws SQLException {
    HashMap<Integer, List<IncomeExpense>> opp = dao.readIncomeExpense(opIds, scIds, programYear, verifiedDate);

    if (opp != null) {
      for (Integer opId : opp.keySet()) {
        if (opId != null) {
          List<IncomeExpense> ies = opp.get(opId);
          if (ies != null) {
            FarmingOperation op = opMap.get(opId);
            if (op == null) {
              throw new SQLException("Mis-matched Operation Ids found for "
                  + opId);
            }

            if (op.getIncomeExpenses() != null) {
              throw new SQLException(
                  "Duplicate sets of Income Expenses found for "
                      + opId);
            }
            
            op.setIncomeExpenses(ies);

          }
        }
      }
    }
  }

  /**
   * @param opIds Integer[]
   * @param opMap HashMap
   * 
   * @throws SQLException
   *           SQLException
   */
  private void loadProductionIns(final Integer[] opIds, final HashMap<Integer, FarmingOperation> opMap)
      throws SQLException {
    HashMap<Integer, List<ProductionInsurance>> opp = dao.readOperationProductionInsurance(opIds);

    if (opp != null) {
      for (Integer opId : opp.keySet()) {
        if (opId != null) {
          List<ProductionInsurance> opps = opp.get(opId);
          if (opps != null) {
            FarmingOperation op = opMap.get(opId);
            if (op == null) {
              throw new SQLException("Mis-matched Operation Ids found for "
                  + opId);
            }
            if (op.getProductionInsurances() != null) {
              throw new SQLException(
                  "Duplicate sets of Farming Production Insurance found for "
                      + opId);
            }

            op.setProductionInsurances(opps);
          }
        }
      }
    }
  }

  /**
   * @param opIds Integer[]
   * @param opMap HashMap <op_id, FarmingOperation>
   * 
   * @throws SQLException
   *           SQLException
   */
  private void loadOperationalPartn(final Integer[] opIds, final HashMap<Integer, FarmingOperation> opMap)
      throws SQLException {
    HashMap<Integer, List<FarmingOperationPartner>> opp = dao.readOperationPartners(opIds);

    if (opp != null) {
      for (Integer opId : opp.keySet()) {
        if (opId != null) {
          List<FarmingOperationPartner> opps = opp.get(opId);
          if (opps != null) {
            FarmingOperation op = opMap.get(opId);
            if (op == null) {
              throw new SQLException("Mis-matched Operation Ids found for "
                  + opId);
            }
            if (op.getFarmingOperationPartners() != null) {
              throw new SQLException(
                  "Duplicate sets of Farming Operation Partners found for "
                      + opId);
            }

            op.setFarmingOperationPartners(opps);
          }
        }
      }
    }
    
    for(FarmingOperation op : opMap.values()) {
      if(op.getFarmingOperationPartners() == null) {
        op.setFarmingOperationPartners(new ArrayList<FarmingOperationPartner>());
      }
    }
  }

  /**
   * @param scIds Integer[]
   * @param opMap HashMap
   * 
   * @throws SQLException SQLException
   */
  private void loadMargin(final Integer[] scIds, final HashMap<Integer, FarmingOperation> opMap)
      throws SQLException {
    HashMap<Integer, Margin> opp = dao.readScenarioMargin(scIds);

    if (opp != null) {
      for (Integer opId : opp.keySet()) {
        if (opId != null) {
          Margin m = opp.get(opId);
          if (m != null) {
            FarmingOperation op = opMap.get(opId);
            if (op == null) {
              throw new SQLException("Mis-matched Operation Ids found for "
                  + opId);
            }
            if (op.getMargin() != null) {
              throw new SQLException("Duplicate set of Margins found for "
                  + opId);
            }

            op.setMargin(m);
          }
        }
      }
    }
  }

  /**
   * @param scIds
   *          Integer[]
   * @param sc
   *          Scenario
   * 
   * @throws SQLException
   *           SQLException
   */
  private void loadClaim(final Integer[] scIds, final Scenario sc)
      throws SQLException {
    HashMap<Integer, Benefit> claims = dao.readScenarioClaim(scIds);

    for (Integer key : claims.keySet()) {
      if (key != null) {
        Benefit c = claims.get(key);
        if (c != null) {
          // find a Scenario for it ...
          if (key.equals(sc.getScenarioId())) {
            if (sc.getFarmingYear() != null) {
              sc.getFarmingYear().setBenefit(c);
            }
          } else {
            if (sc.getReferenceScenarios() != null) {
              for (ReferenceScenario rsc : sc.getReferenceScenarios()) {
                if (rsc != null && key.equals(rsc.getScenarioId())) {
                  if (rsc.getFarmingYear() != null) {
                    rsc.getFarmingYear().setBenefit(c);
                  }
                }
              }
            }
          }
        }
      }
    }

  }

  /**
   * @param scIds
   *          Integer[]
   * @param sc
   *          Scenario
   * 
   * @throws SQLException
   *           SQLException
   */
  private void loadTotalMargin(final Integer[] scIds, final Scenario sc)
      throws SQLException {
    HashMap<Integer, MarginTotal> totals = dao.readScenarioTotalMargin(scIds);

    for (Integer key : totals.keySet()) {
      if (key != null) {
        MarginTotal c = totals.get(key);
        if (c != null) {
          // find a Scenario for it ...
          if (key.equals(sc.getScenarioId())) {
            if (sc.getFarmingYear() != null) {
              sc.getFarmingYear().setMarginTotal(c);
            }
          } else {
            if (sc.getReferenceScenarios() != null) {
              for (ReferenceScenario rsc : sc.getReferenceScenarios()) {
                if (rsc != null && key.equals(rsc.getScenarioId())) {
                  if (rsc.getFarmingYear() != null) {
                    rsc.getFarmingYear().setMarginTotal(c);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * @param pyvIds Integer[]
   * @param sc Scenario
   * 
   * @throws SQLException
   *           SQLException
   */
  private void loadWholeFarms(final Integer[] pyvIds, final Scenario sc)
      throws SQLException {
    HashMap<Integer, List<WholeFarmParticipant>> wf = dao.readWholeFarmParticipant(pyvIds);

    for (Integer key : wf.keySet()) {
      if (key != null) {
        List<WholeFarmParticipant> l = wf.get(key);
        if (l != null) {
          // find a Scenario for it ...
          if (sc.getFarmingYear() != null && key.equals(sc.getFarmingYear().getProgramYearVersionId())) {
              sc.getFarmingYear().setWholeFarmParticipants(l);
          } else {
            if (sc.getReferenceScenarios() != null) {
              for (ReferenceScenario rsc : sc.getReferenceScenarios()) {
                if (rsc.getFarmingYear() != null && key.equals(rsc.getFarmingYear().getProgramYearVersionId())) {
                    rsc.getFarmingYear().setWholeFarmParticipants(l);
                  }
              }
            }
          }
        }
      }
    }
  }

}
