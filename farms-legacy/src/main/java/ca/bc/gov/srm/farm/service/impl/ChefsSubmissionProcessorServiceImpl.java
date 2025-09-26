package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.resource.adjustment.AdjustmentSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.cashMargin.CashMarginsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.common.CattleGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.CropGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.GrainGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.IncomeExpenseGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.InputGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.LivestockGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.NurseryGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.OtherPucGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.PayableGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.ReceivablesGrid;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageCommodityGrid;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageCropGrid;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageNurseryGrid;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageVeggieGrid;
import ca.bc.gov.srm.farm.chefs.resource.interim.ExpenseGrid;
import ca.bc.gov.srm.farm.chefs.resource.interim.IncomeGrid;
import ca.bc.gov.srm.farm.chefs.resource.interim.InterimSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.interim.InventoryGridLivestock;
import ca.bc.gov.srm.farm.chefs.resource.interim.ProductionGrid;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppCropGrid;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppNurseryGrid;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.VeggieGrid;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalBaseDataResource;
import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.ChefsDatabaseDAO;
import ca.bc.gov.srm.farm.dao.ReadDAO;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.PayableItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.LineItemCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ChefsSubmissionProcessorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.transaction.Transaction;

public class ChefsSubmissionProcessorServiceImpl extends BaseService implements ChefsSubmissionProcessorService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public Integer createAdjustmentChefsScenario(AdjustmentSubmissionDataResource data, Integer clientId,
      Integer programYear, Integer scenarioIdToCopy, String applicationVersion, String user) throws ServiceException {

    logMethodStart(logger);

    CalculatorDAO calculatorDAO = new CalculatorDAO();
    Transaction transaction = null;
    Integer adjustmentScenarioNumber;

    try {
      transaction = openTransaction();
      transaction.begin();

      adjustmentScenarioNumber = calculatorDAO.saveScenarioAsNew(transaction, scenarioIdToCopy, ScenarioTypeCodes.USER, 
          ScenarioCategoryCodes.UNKNOWN, applicationVersion, user);

      transaction.commit();

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      if (transaction != null) {
        transaction.rollback();
      }
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return adjustmentScenarioNumber;
  }

  @Override
  public Integer createInterimChefsScenario(InterimSubmissionDataResource data, Integer clientId, Integer programYear,
      String applicationVersion, Integer submissionId, String user) throws ServiceException {

    logMethodStart(logger);

    CalculatorDAO calculatorDAO = new CalculatorDAO();
    ChefsDatabaseDAO chefsDatabaseDao = new ChefsDatabaseDAO();
    Transaction transaction = null;
    Integer programYearVersionId;
    Integer chefsScenarioId;
    Integer userScenarioNumber;

    try {
      transaction = openTransaction();
      transaction.begin();

      Integer programYearId = getProgramYearId(transaction, clientId, programYear);

      if (programYearId == null) {
        programYearId = calculatorDAO.createProgramYear(transaction, clientId, programYear, user);
      }

      programYearVersionId = calculatorDAO.createProgramYearVersion(transaction, programYearId,
          data.getMunicipalityCode().getValue(), user);

      FarmingOperation farmingOperation = createFarmingOperation(data.getFiscalYearStartDate(),
          data.getFiscalYearEndDate(), programYearVersionId);
      calculatorDAO.createFarmingOperation(transaction, farmingOperation, user);

      List<ProductiveUnitCapacity> pucList = createPucList(data, farmingOperation);
      createProductiveUnitCapacities(transaction, pucList, user);

      List<IncomeExpense> incomeList = createIncomeList(data.getIncomeGrid(), farmingOperation);
      createIncomeExpenses(transaction, incomeList, user);

      List<IncomeExpense> expensesList = createExpensesList(data.getExpenseGrid(), farmingOperation);
      createIncomeExpenses(transaction, expensesList, user);

      List<InventoryItem> inventoryItemList = createInventoryList(data.getInventoryGridLivestock(),
          data.getProductionGrid(), farmingOperation);
      createInventoryItems(transaction, inventoryItemList, user);
      
      // For CHEFS V2
      List<IncomeExpense> incomeExpensesListV2 = createInterimIncomeExpensesList(data, farmingOperation);
      createIncomeExpenses(transaction, incomeExpensesListV2, user);
      
      List<InventoryItem> inventoryItemListV2 = createInterimInventoryList(data, farmingOperation);
      createInventoryItems(transaction, inventoryItemListV2, user);

      chefsScenarioId = calculatorDAO.createScenario(transaction, programYearVersionId, ScenarioTypeCodes.CHEF,
          ScenarioCategoryCodes.CHEF_INTRM, user);
      
      @SuppressWarnings("resource")
      Connection connection = (Connection) transaction.getDatastore();
      chefsDatabaseDao.updateScenarioSubmissionId(connection, chefsScenarioId, submissionId, user);
      
      userScenarioNumber = calculatorDAO.saveScenarioAsNew(transaction, chefsScenarioId, ScenarioTypeCodes.USER,
          ScenarioCategoryCodes.UNKNOWN, applicationVersion, user);

      transaction.commit();

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      if (transaction != null) {
        transaction.rollback();
      }
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return userScenarioNumber;
  }

  @Override
  public Integer createNppSupplementalData(NppSubmissionDataResource data, Integer clientId, Integer programYear,
      String applicationVersion, boolean createdParticipant, String user) throws ServiceException {

    logMethodStart(logger);
    CalculatorDAO calculatorDAO = new CalculatorDAO();
    Transaction transaction = null;
    Integer chefsScenarioId = null;

    try {
      transaction = openTransaction();
      transaction.begin();
      
      FarmingOperation farmingOperation;

      if (createdParticipant) {
        CalculatorService calculatorService = ServiceFactory.getCalculatorService();
        Scenario scenario = calculatorService.loadScenario(data.getAgriStabilityAgriInvestPin(), programYear, 1);
        farmingOperation = scenario.getFarmingYear().getFarmingOperationByNumber(1);

        chefsScenarioId = scenario.getScenarioId();

      } else {
        Integer programYearId = getProgramYearId(transaction, clientId, programYear);
        if (programYearId == null) {
          programYearId = calculatorDAO.createProgramYear(transaction, clientId, programYear, user);
        }
        Integer programYearVersionId = calculatorDAO.createProgramYearVersion(transaction, programYearId, data.getMunicipalityCode(), user);

        farmingOperation = createFarmingOperation(data.getFiscalYearStart(), data.getFiscalYearEnd(), programYearVersionId);
        calculatorDAO.createFarmingOperation(transaction, farmingOperation, user);

        chefsScenarioId = calculatorDAO.createScenario(transaction, programYearVersionId, ScenarioTypeCodes.CHEF, ScenarioCategoryCodes.CHEF_NPP,
            user);
      }
      List<ProductiveUnitCapacity> pucList = createPucList(data, farmingOperation);
      createProductiveUnitCapacities(transaction, pucList, user);
      
      logger.debug("scenario: " + chefsScenarioId);

      transaction.commit();

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      if (transaction != null) {
        transaction.rollback();
      }
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return chefsScenarioId;
  }
  
  @Override
  public Integer createSupplementalChefsScenario(SupplementalBaseDataResource data, Integer clientId, Integer programYear,
      String applicationVersion, String municipalityCode, String user,  String scenarioCategoryCode) throws ServiceException {

    logMethodStart(logger);

    CalculatorDAO calculatorDAO = new CalculatorDAO();
    Transaction transaction = null;
    Integer programYearVersionId;
    Integer chefsScenarioId;

    try {
      transaction = openTransaction();
      transaction.begin();

      Integer programYearId = getProgramYearId(transaction, clientId, programYear);

      if (programYearId == null) {
        programYearId = calculatorDAO.createProgramYear(transaction, clientId, programYear, user);
      }

      programYearVersionId = calculatorDAO.createProgramYearVersion(transaction, programYearId,
          municipalityCode, user);

      FarmingOperation farmingOperation = createFarmingOperation(null,
          null, programYearVersionId);
      calculatorDAO.createFarmingOperation(transaction, farmingOperation, user);
      
      List<ProductiveUnitCapacity> pucList = createPucList(data, farmingOperation);
      createProductiveUnitCapacities(transaction, pucList, user);

      List<InventoryItem> inventoryItemList = createSupplementalInventoryList(data, farmingOperation);
      createInventoryItems(transaction, inventoryItemList, user);

      chefsScenarioId = calculatorDAO.createScenario(transaction, programYearVersionId, ScenarioTypeCodes.CHEF,
          scenarioCategoryCode, user);
      
      transaction.commit();

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      if (transaction != null) {
        transaction.rollback();
      }
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return chefsScenarioId;
  }

  @Override
  public Integer createCashMarginChefsScenario(CashMarginsSubmissionDataResource data, Integer clientId, Integer programYear,
      String applicationVersion, String municipalityCode, Integer submissionId, String user) throws ServiceException {

    logMethodStart(logger);

    CalculatorDAO calculatorDAO = new CalculatorDAO();
    ChefsDatabaseDAO chefsDatabaseDao = new ChefsDatabaseDAO();
    Transaction transaction = null;
    Integer programYearVersionId;
    Integer chefsScenarioId;

    try {
      transaction = openTransaction();
      transaction.begin();

      Integer programYearId = getProgramYearId(transaction, clientId, programYear);

      if (programYearId == null) {
        programYearId = calculatorDAO.createProgramYear(transaction, clientId, programYear, user);
      }
      programYearVersionId = calculatorDAO.createProgramYearVersion(transaction, programYearId, municipalityCode, user);

      FarmingOperation farmingOperation = createFarmingOperation(null, null, programYearVersionId);
      calculatorDAO.createFarmingOperation(transaction, farmingOperation, user);

      chefsScenarioId = calculatorDAO.createScenario(transaction, programYearVersionId, ScenarioTypeCodes.CHEF, ScenarioCategoryCodes.CHEF_CM,
          user);
      
      @SuppressWarnings("resource")
      Connection connection = (Connection) transaction.getDatastore();
      chefsDatabaseDao.updateScenarioSubmissionId(connection, chefsScenarioId, submissionId, user);

      transaction.commit();

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      if (transaction != null) {
        transaction.rollback();
      }
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return chefsScenarioId;
  }

  private void createIncomeExpenses(Transaction transaction, List<IncomeExpense> incomeExpenseList, String user)
      throws ServiceException {

    ChefsDatabaseDAO chefsDAO = new ChefsDatabaseDAO();
    try {

      chefsDAO.createIncomeExpenses(transaction, incomeExpenseList, user);

    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      throw e;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

  }

  private void createInventoryItems(Transaction transaction, final List<InventoryItem> inventoryList, final String user)
      throws ServiceException {

    ChefsDatabaseDAO chefsDAO = new ChefsDatabaseDAO();
    try {

      chefsDAO.createInventoryItems(transaction, inventoryList, user);

    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      throw e;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings("resource")
  private Integer getProgramYearId(Transaction transaction, Integer clientId, Integer programYear) throws ServiceException {
    Integer programYearId;
    Connection connection = (Connection) transaction.getDatastore();
    ReadDAO readDao = new ReadDAO(connection);
    try {
      programYearId = readDao.readProgramYearId(clientId, programYear);
    } catch (SQLException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }
    return programYearId;
  }

  private FarmingOperation createFarmingOperation(Date fiscalStart, Date fiscalEnd,
      Integer programYearVersionId) {

    FarmingYear farmingYear = new FarmingYear();
    farmingYear.setProgramYearVersionId(programYearVersionId);

    FarmingOperation fo = new FarmingOperation();
    fo.setFarmingYear(farmingYear);
    fo.setOperationNumber(1);
    fo.setSchedule("A");
    fo.setFiscalYearStart(fiscalStart);
    fo.setFiscalYearEnd(fiscalEnd);
    fo.setAccountingCode(null);
    fo.setPartnershipPin(0);
    fo.setPartnershipName(null);
    fo.setPartnershipPercent(1.0);
    fo.setIsCropDisaster(false);
    fo.setIsCropShare(false);
    fo.setIsFeederMember(false);
    fo.setIsLandlord(false);
    fo.setIsLivestockDisaster(false);
    fo.setBusinessUseHomeExpense(0.0);
    fo.setFarmingExpenses(0.0);
    fo.setGrossIncome(0.0);
    fo.setInventoryAdjustments(0.0);
    fo.setNetFarmIncome(0.0);
    fo.setNetIncomeAfterAdj(0.0);
    fo.setNetIncomeBeforeAdj(0.0);
    fo.setOtherDeductions(0.0);

    return fo;
  }

  private List<ProductiveUnitCapacity> createPucList(InterimSubmissionDataResource data,
      FarmingOperation farmingOperation) {

    Map<String, Double> productiveCapacityMap = getProductiveCapacityMap(data);

    List<ProductiveUnitCapacity> productiveUnitCapacityList = new ArrayList<>();

    for (Map.Entry<String, Double> entry : productiveCapacityMap.entrySet()) {
      if (entry.getValue() != null && entry.getValue() > 0) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setAdjAmount(entry.getValue());
        puc.setStructureGroupCode(entry.getKey());
        puc.setFarmingOperation(farmingOperation);
        productiveUnitCapacityList.add(puc);
      }
    }

    List<ProductionGrid> productionGrids = data.getProductionGrid();

    for (ProductionGrid crop : productionGrids) {
      if (crop.getCrop().getValue() != null && calculateAdjAmount(crop) > 0) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();

        puc.setAdjAmount(calculateAdjAmount(crop));
        puc.setOnFarmAcres(crop.getProductiveArea());
        puc.setUnseedableAcres(crop.getUnseedableAcres());
        puc.setInventoryItemCode(crop.getCrop().getValue());
        puc.setFarmingOperation(farmingOperation);
        productiveUnitCapacityList.add(puc);
      }
    }
    
    // CHEFS V2
    for (OtherPucGrid other : data.getOpdGrid()) {
      if (other != null && other.getSelectOtherLivestock() != null && other.getSelectOtherLivestock().getValue() != null
          && other.getOtherLivestockNumber() != null && other.getOtherLivestockNumber() > 0) {
        logger.debug(other.toString());
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setAdjAmount(other.getOtherLivestockNumber().doubleValue());
        puc.setStructureGroupCode(other.getSelectOtherLivestock().getValue());
        puc.setFarmingOperation(farmingOperation);
        productiveUnitCapacityList.add(puc);
      }
    }

    addSupplementalCropsToPucList(productiveUnitCapacityList, data.getBerryGrid(), farmingOperation);
    addSupplementalCropsToPucList(productiveUnitCapacityList, data.getTreeFruitGrid(), farmingOperation);
    addSupplementalCropsToPucList(productiveUnitCapacityList, data.getVegetableGrid(), farmingOperation);
    addSupplementalCropsToPucList(productiveUnitCapacityList, data.getNeHorticultureGrid(), farmingOperation);
    addSupplementalNurseryToPucList(productiveUnitCapacityList, data.getNurseryGrid(), farmingOperation);
    addSupplementalGrainsToPucList(productiveUnitCapacityList, data.getGrainGrid(), farmingOperation);

    return productiveUnitCapacityList;

  }

  private Double calculateAdjAmount(ProductionGrid crop) {

    double unseedable = crop.getUnseedableAcres() != null ? crop.getUnseedableAcres() : 0;
    return crop.getProductiveArea() + unseedable;
  }

  private static Map<String, Double> getProductiveCapacityMap(InterimSubmissionDataResource data) {
    Map<String, Double> productiveCapacityMap = new HashMap<>();

    productiveCapacityMap.put("104", data.getProductiveCapacityLC104());
    productiveCapacityMap.put("105", data.getProductiveCapacityLC105());
    productiveCapacityMap.put("106", data.getProductiveCapacityLC106());
    productiveCapacityMap.put("108", data.getProductiveCapacityLC108());
    productiveCapacityMap.put("109", data.getProductiveCapacityLC109());
    productiveCapacityMap.put("123", data.getProductiveCapacityLC123());
    productiveCapacityMap.put("124", data.getProductiveCapacityLC124());
    productiveCapacityMap.put("125", data.getProductiveCapacityLC125());
    productiveCapacityMap.put("141", data.getProductiveCapacityLC141());
    productiveCapacityMap.put("142", data.getProductiveCapacityLC142());
    productiveCapacityMap.put("143", data.getProductiveCapacityLC143());
    productiveCapacityMap.put("144", data.getProductiveCapacityLC144());
    productiveCapacityMap.put("145", data.getProductiveCapacityLC145());
    productiveCapacityMap.put("4001", data.getProductiveCapacityLC4001());
    
    return productiveCapacityMap;
  }

  private List<InventoryItem> createInventoryList(List<InventoryGridLivestock> inventoryGridLivestocks,
      List<ProductionGrid> productionGrids, FarmingOperation farmingOperation) {

    List<InventoryItem> inventoryList = new ArrayList<>();

    for (ProductionGrid productionGrid : productionGrids) {
      if (productionGrid.getCrop().getValue() != null) {

        InventoryItem inventoryItem = createCropInventoryItemObject(productionGrid);
        if (productionGrid.getOpeningInventoryUnits() != null) {
          inventoryItem.setAdjQuantityStart(productionGrid.getOpeningInventoryUnits());
        }
        if (productionGrid.getEstimatedEndingInventoryUnits() != null) {
          inventoryItem.setAdjQuantityEnd(productionGrid.getEstimatedEndingInventoryUnits());
        }
        inventoryItem.setInventoryItemCode(productionGrid.getCrop().getValue());
        inventoryItem.setFarmingOperation(farmingOperation);

        inventoryList.add(inventoryItem);
      }
    }

    for (InventoryGridLivestock inventoryGrid : inventoryGridLivestocks) {
      if (inventoryGrid.getCommodity().getValue() != null) {
        InventoryItem inventoryItem = createLivestockInventoryItemObject();
        if (!StringUtils.isEmpty(inventoryGrid.getOpeningInventoryUnits())) {
          inventoryItem.setAdjQuantityStart(Double.valueOf(inventoryGrid.getOpeningInventoryUnits()));
        }
        if (!StringUtils.isEmpty(inventoryGrid.getEstimatedEndingInventoryUnits())) {
          inventoryItem.setAdjQuantityEnd(Double.valueOf(inventoryGrid.getEstimatedEndingInventoryUnits()));
        }
        inventoryItem.setInventoryItemCode(inventoryGrid.getCommodity().getValue());
        inventoryItem.setFarmingOperation(farmingOperation);

        inventoryList.add(inventoryItem);
      }
    }

    return inventoryList;

  }

  private List<IncomeExpense> createExpensesList(List<ExpenseGrid> expenseGrids, FarmingOperation farmingOperation) {

    List<IncomeExpense> expensesList = new ArrayList<>();

    Double otherAllowableAmount = 0.0;
    IncomeExpense otherAllowableItem = new IncomeExpense();
    for (ExpenseGrid expenseGrid : expenseGrids) {
      LineItem lineItem = new LineItem();
      if (expenseGrid.getExpenseCategories() != null && expenseGrid.getExpenseCategories().getValue() != null
          && !expenseGrid.getExpenseCategories().getValue().equals("otherAllowableExpensesSpecify")
          && expenseGrid.getEstimatedTotalExpenseAmount() != null) {
        lineItem.setLineItem(Integer.valueOf(expenseGrid.getExpenseCategories().getValue()));
        lineItem.setIsEligible(true);

        if (Integer.valueOf(expenseGrid.getExpenseCategories().getValue()) == LineItemCodes.OTHER_SPECIFY_9896) {
          otherAllowableAmount += expenseGrid.getEstimatedTotalExpenseAmount();
        } else {
          IncomeExpense item = new IncomeExpense();
          item.setFarmingOperation(farmingOperation);
          item.setLineItem(lineItem);
          item.setAdjAmount(expenseGrid.getEstimatedTotalExpenseAmount());
          item.setIsExpense(true);
          expensesList.add(item);
        }
      }
    }

    if (otherAllowableAmount > 0) {
      LineItem lineItem = new LineItem();
      lineItem.setLineItem(LineItemCodes.OTHER_SPECIFY_9896);
      lineItem.setIsEligible(true);
      otherAllowableItem.setLineItem(lineItem);
      otherAllowableItem.setFarmingOperation(farmingOperation);
      otherAllowableItem.setAdjAmount(otherAllowableAmount);
      otherAllowableItem.setIsExpense(true);
      expensesList.add(otherAllowableItem);
    }

    return expensesList;

  }

  private List<IncomeExpense> createIncomeList(List<IncomeGrid> incomeGrids, FarmingOperation farmingOperation) {

    List<IncomeExpense> incomeList = new ArrayList<>();

    Double otherAllowableAmount = 0.0;
    IncomeExpense otherAllowableItem = new IncomeExpense();
    for (IncomeGrid incomeGrid : incomeGrids) {
      if (incomeGrid.getIncomeCategories() != null && incomeGrid.getEstimatedTotalReceived() != null) {
        LineItem lineItem = new LineItem();
        lineItem.setLineItem(Integer.valueOf(incomeGrid.getIncomeCategories().getValue()));
        lineItem.setIsEligible(true);

        if (Integer.valueOf(incomeGrid.getIncomeCategories().getValue()) == LineItemCodes.OTHER_SPECIFY_9600) {
          otherAllowableAmount += incomeGrid.getEstimatedTotalReceived();
        } else {
          IncomeExpense item = new IncomeExpense();
          item.setFarmingOperation(farmingOperation);
          item.setLineItem(lineItem);
          item.setAdjAmount(incomeGrid.getEstimatedTotalReceived());
          item.setIsExpense(false);
          incomeList.add(item);
        }
      }
    }

    if (otherAllowableAmount > 0) {
      LineItem lineItem = new LineItem();
      lineItem.setLineItem(LineItemCodes.OTHER_SPECIFY_9600);
      lineItem.setIsEligible(true);
      otherAllowableItem.setLineItem(lineItem);
      otherAllowableItem.setFarmingOperation(farmingOperation);
      otherAllowableItem.setAdjAmount(otherAllowableAmount);
      otherAllowableItem.setIsExpense(false);
      incomeList.add(otherAllowableItem);
    }

    return incomeList;

  }

  private InventoryItem createCropInventoryItemObject(ProductionGrid pg) {

    CropItem cropItem = new CropItem();
    cropItem.setInventoryClassCode(InventoryClassCodes.CROP);

    cropItem.setOnFarmAcres(pg.getProductiveArea());
    cropItem.setUnseedableAcres(pg.getUnseedableAcres());
    cropItem.setAdjQuantityProduced(pg.getEstimatedProductionIntendedForSale());
    cropItem.setCropUnitCode(pg.getCropUnit().getValue());

    return cropItem;
  }

  private InventoryItem createLivestockInventoryItemObject() {

    LivestockItem livestockItem = new LivestockItem();
    livestockItem.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);

    return livestockItem;
  }

  private List<ProductiveUnitCapacity> createPucList(NppSubmissionDataResource data,
      FarmingOperation farmingOperation) {

    Map<String, Double> productiveCapacityMap = getProductiveCapacityMap(data);

    List<ProductiveUnitCapacity> productiveUnitCapacityList = new ArrayList<>();

    for (Map.Entry<String, Double> entry : productiveCapacityMap.entrySet()) {
      if (entry.getValue() != null && entry.getValue() > 0) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setAdjAmount(entry.getValue());
        puc.setFarmingOperation(farmingOperation);
        if (entry.getKey().length() == 3) {
          puc.setStructureGroupCode(entry.getKey());
        } else {
          puc.setInventoryItemCode(entry.getKey());
        }
        productiveUnitCapacityList.add(puc);

      }
    }

    for (NppNurseryGrid nursery : data.getNurseryGrid()) {
      if (nursery.getCommodity() != null && nursery.getCommodity().getLabel() != null) {

        Double adjAmount = 0.0;
        if (nursery.getSquareMeters() != null) {
          adjAmount = nursery.getSquareMeters();
        } else if (nursery.getNumberOfPlants() != null) {
          adjAmount = nursery.getNumberOfPlants().doubleValue();
        }
        
        if (adjAmount > 0) {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          puc.setInventoryItemCode(nursery.getCommodity().getValue());
          puc.setFarmingOperation(farmingOperation);
          puc.setAdjAmount(adjAmount);
          productiveUnitCapacityList.add(puc);
        }
      }
    }

    for (NppCropGrid crop : data.getForageBasketGrid()) {
      String cropCodeValue = getNumericPrefix(crop.getCrop());
      if (cropCodeValue != null && !cropCodeValue.isEmpty() &&
          crop.getAcres() != null && crop.getAcres() > 0) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setInventoryItemCode(cropCodeValue);
        puc.setFarmingOperation(farmingOperation);
        puc.setAdjAmount(crop.getAcres());
        productiveUnitCapacityList.add(puc);
      }
    }

    for (NppCropGrid crop : data.getForageSeedGrid()) {
      String cropCodeValue = getNumericPrefix(crop.getCrop());
      if (cropCodeValue != null && !cropCodeValue.isEmpty() && 
          crop.getAcres() != null && crop.getAcres() > 0) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setInventoryItemCode(cropCodeValue);
        puc.setFarmingOperation(farmingOperation);
        puc.setAdjAmount(crop.getAcres());
        productiveUnitCapacityList.add(puc);
      }
    }

    for (NppCropGrid crop : data.getCropBasketTypeGrid()) {
      String cropCodeValue = getNumericPrefix(crop.getCrop());
      if (cropCodeValue != null && !cropCodeValue.isEmpty() &&
          crop.getAcres() != null && crop.getAcres() > 0) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setInventoryItemCode(cropCodeValue);
        puc.setFarmingOperation(farmingOperation);
        puc.setAdjAmount(crop.getAcres());
        productiveUnitCapacityList.add(puc);
      }
    }

    for (VeggieGrid veggie : data.getVeggieGrid()) {
      if (veggie.getVegetables() != null && veggie.getVegetables().getLabel() != null) {
        
        Double adjAmount = 0.0;
        if (veggie.getSquareMeters() != null) {
          adjAmount = veggie.getSquareMeters();
        } else if (veggie.getAcres() != null) {
          adjAmount = veggie.getAcres();
        }
        
        if (adjAmount > 0) {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          puc.setInventoryItemCode(veggie.getVegetables().getValue());
          puc.setFarmingOperation(farmingOperation);
          puc.setAdjAmount(adjAmount);
          productiveUnitCapacityList.add(puc);
        }
      }
    }

    return productiveUnitCapacityList;

  }

  private String getNumericPrefix(String s) {
    String result = null;
    
    if(s != null) {
      Pattern pattern = Pattern.compile("^(\\d+)");
      Matcher matcher = pattern.matcher(s);
      
      if(matcher.find()) {
        result = matcher.group(0);
      }
    }
    
    return result;
  }

  private static Map<String, Double> getProductiveCapacityMap(NppSubmissionDataResource data) {
    Map<String, Double> productiveCapacityMap = new HashMap<>();

    productiveCapacityMap.put("104", data.getBredCow_104());
    productiveCapacityMap.put("105", data.getFeederCattleFedUpTo900Lbs_105());
    productiveCapacityMap.put("106", data.getFeederCattleFedOver900Lbs_106());
    productiveCapacityMap.put("108", data.getLayersEggsForHatching_108());
    productiveCapacityMap.put("109", data.getLayersEggsForConsumption_109());
    productiveCapacityMap.put("113", data.getDairyOfButterfatPerDay_113());
    productiveCapacityMap.put("124", data.getFeederHogsFedOver50Lbs_124());
    productiveCapacityMap.put("125", data.getFeederHogsFedUpTo50Lbs_125());
    productiveCapacityMap.put("126", data.getHoneybees_126());
    productiveCapacityMap.put("129", data.getLeafCutterBees_129());
    productiveCapacityMap.put("141", data.getNumberOfCustomFedCattle_141());
    productiveCapacityMap.put("142", data.getNumberOfCustomFedHogs_142());
    productiveCapacityMap.put("143", data.getBroilersChickens_143());
    productiveCapacityMap.put("144", data.getBroilersTurkeys_144());
    productiveCapacityMap.put("145", data.getHogsFarrowing_145());

    productiveCapacityMap.put("5054", data.getPearAcres_5054());
    productiveCapacityMap.put("5056", data.getPlumAcres_5056());
    productiveCapacityMap.put("5052", data.getPeachAcres_5052());
    productiveCapacityMap.put("5058", data.getPruneAcres_5058());
    productiveCapacityMap.put("5032", data.getApricotAcres_5032());
    productiveCapacityMap.put("4997", data.getFirstYearAcres_4997());
    productiveCapacityMap.put("5048", data.getNectarineAcres_5048());
    productiveCapacityMap.put("5018", data.getRaspberryAcres_5018());
    productiveCapacityMap.put("4782", data.getThirdYearAcres_4782());
    productiveCapacityMap.put("4783", data.getFourthYearAcres_4783());
    productiveCapacityMap.put("4781", data.getSecondYearAcres_4781());
    productiveCapacityMap.put("4780", data.getPlantingYearAcres_4780());
    productiveCapacityMap.put("4800", data.getPlantingYearAcres_4800());
    productiveCapacityMap.put("4980", data.getPlantingYearAcres_4980());
    productiveCapacityMap.put("4995", data.getPlantingYearAcres_4995());
    productiveCapacityMap.put("4801", data.getNonBearingYearAcres_4801());
    productiveCapacityMap.put("4981", data.getNonBearingYearAcres_4981());
    productiveCapacityMap.put("4996", data.getNonBearingYearAcres_4996());
    productiveCapacityMap.put("4824", data.getGala24YearProductionAcres_4824());
    productiveCapacityMap.put("4826", data.getGala5YearProductionAcres_4826());
    productiveCapacityMap.put("4866", data.getOther5YearProductionAcres_4866());
    productiveCapacityMap.put("4998", data.getSecondYearProductionAcres_4998());
    productiveCapacityMap.put("5059", data.getBlueberryPlantingYearAcres_5059());
    productiveCapacityMap.put("4822", data.getGala1stYearProductionAcres_4822());
    productiveCapacityMap.put("4865", data.getOther24YearProductionAcres_4865());
    productiveCapacityMap.put("4862", data.getOther1stYearProductionAcres_4862());
    productiveCapacityMap.put("5060", data.getBlueberryNonBearingYearAcres_5060());
    productiveCapacityMap.put("4816", data.getHighValue5YearProductionAcres_4816());
    productiveCapacityMap.put("5062", data.getBlueberry36YearProductionAcres_5062());
    productiveCapacityMap.put("4991", data.getCranberry1stYearProductionAcres_4991());
    productiveCapacityMap.put("4992", data.getCranberry2ndYearProductionAcres_4992());
    productiveCapacityMap.put("4993", data.getCranberry3rdYearProductionAcres_4993());
    productiveCapacityMap.put("4994", data.getCranberry4thYearProductionAcres_4994());
    productiveCapacityMap.put("4812", data.getHighValue1stYearProductionAcres_4812());
    productiveCapacityMap.put("5064", data.getBlueberry10thYearProductionAcres_5064());
    productiveCapacityMap.put("4990", data.getCranberryEstablishmentStageAcres_4990());
    productiveCapacityMap.put("5063", data.getBlueberry7th9thYearProductionAcres_5063());
    productiveCapacityMap.put("4815", data.getHighValue2nd4thYearProductionAcres_4815());
    productiveCapacityMap.put("5061", data.getBlueberry1stAnd2ndYearProductionAcres_5061());
    productiveCapacityMap.put("4953", data.getLowDensityCherries7YearProductionAcres_4953());
    productiveCapacityMap.put("4956", data.getHighDensityCherries3YearProductionAcres_4956());
    productiveCapacityMap.put("4954", data.getHighDensityCherries1stYearProductionAcres_4954());
    productiveCapacityMap.put("4955", data.getHighDensityCherries2ndYearProductionAcres_4955());
    productiveCapacityMap.put("4950", data.getLowDensityCherries1stAnd2ndYearProductionAcres_4950());
    productiveCapacityMap.put("4951", data.getLowDensityCherries3rdAnd4thYearProductionAcres_4951());
    productiveCapacityMap.put("4952", data.getLowDensityCherries5thAnd6thYearProductionAcres_4952());

    productiveCapacityMap.put("6960", data.getChristmasTreesEstablishmentAcres());
    productiveCapacityMap.put("6961", data.getChristmasTreesEstablishmentAcres1());
    productiveCapacityMap.put("6962", data.getChristmasTreesEstablishmentAcres2());
    productiveCapacityMap.put("6963", data.getChristmasTreesEstablishmentAcres3());
    productiveCapacityMap.put("6964", data.getChristmasTreesEstablishmentAcres4());

    return productiveCapacityMap;
  }
  
  private void createProductiveUnitCapacities(Transaction transaction, List<ProductiveUnitCapacity> pucList, String user)
      throws DataAccessException {
    
    ChefsDatabaseDAO chefsDAO = new ChefsDatabaseDAO();
    chefsDAO.createProductiveUnitCapacities(transaction, pucList, user);
  }
  
  private List<ProductiveUnitCapacity> createPucList(SupplementalBaseDataResource data, FarmingOperation farmingOperation) {

    Map<String, Double> productiveCapacityMap = getProductiveCapacityMap(data);

    List<ProductiveUnitCapacity> productiveUnitCapacityList = new ArrayList<>();

    for (Map.Entry<String, Double> entry : productiveCapacityMap.entrySet()) {
      if (entry.getValue() != null && entry.getValue() > 0) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setAdjAmount(entry.getValue());
        puc.setStructureGroupCode(entry.getKey());
        puc.setFarmingOperation(farmingOperation);
        productiveUnitCapacityList.add(puc);
      }
    }
    
    for (OtherPucGrid other : data.getOpdGrid()) {
      if (other != null && other.getSelectOtherLivestock() != null && other.getSelectOtherLivestock().getValue() != null
          && other.getOtherLivestockNumber() != null && other.getOtherLivestockNumber() > 0) {
        logger.debug(other.toString());
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setAdjAmount(other.getOtherLivestockNumber().doubleValue());
        puc.setStructureGroupCode(other.getSelectOtherLivestock().getValue());
        puc.setFarmingOperation(farmingOperation);
        productiveUnitCapacityList.add(puc);
      }
    }

    addSupplementalCropsToPucList(productiveUnitCapacityList, data.getBerryGrid(), farmingOperation);
    addSupplementalCropsToPucList(productiveUnitCapacityList, data.getTreeFruitGrid(), farmingOperation);
    addSupplementalCropsToPucList(productiveUnitCapacityList, data.getVegetableGrid(), farmingOperation);
    addSupplementalCropsToPucList(productiveUnitCapacityList, data.getNeHorticultureGrid(), farmingOperation);
    addSupplementalNurseryToPucList(productiveUnitCapacityList, data.getNurseryGrid(), farmingOperation);
    addSupplementalGrainsToPucList(productiveUnitCapacityList, data.getGrainGrid(), farmingOperation);

    return productiveUnitCapacityList;

  }
  
  private List<InventoryItem> createInterimInventoryList(InterimSubmissionDataResource data, FarmingOperation farmingOperation) {

    List<InventoryItem> inventoryList = new ArrayList<>();

    addSupplementalCropsToInventoryList(data.getBerryGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getTreeFruitGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getVegetableGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getNeHorticultureGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getNurseryGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getGrainGrid(), inventoryList, farmingOperation);

    for (CattleGrid cattleGrid : data.getCattleGrid()) {
      if (cattleGrid != null && cattleGrid.getCattleType() != null && cattleGrid.getCattleType().getValue() != null) {
        InventoryItem inventoryItem = createLivestockInventoryItemObject();
        if (cattleGrid.getEndingInventoryCattle() != null) {
          inventoryItem.setAdjQuantityEnd(cattleGrid.getEndingInventoryCattle());
        }
        if (cattleGrid.getEndingFmvCattle() != null) {
          inventoryItem.setAdjPriceEnd(cattleGrid.getEndingFmvCattle());
        }
        
        inventoryItem.setInventoryItemCode(cattleGrid.getCattleType().getValue());
        inventoryItem.setFarmingOperation(farmingOperation);

        inventoryList.add(inventoryItem);
      }
    }
    
    addSupplementalLivestockToInventoryList(data.getPoultryGrid(), inventoryList, farmingOperation);
    addSupplementalLivestockToInventoryList(data.getSwineGrid(), inventoryList, farmingOperation);
    addSupplementalLivestockToInventoryList(data.getOtherGrid(), inventoryList, farmingOperation);
    
    return inventoryList;
  }
  
  private List<InventoryItem> createSupplementalInventoryList(SupplementalBaseDataResource data, FarmingOperation farmingOperation) {

    List<InventoryItem> inventoryList = new ArrayList<>();

    addSupplementalCropsToInventoryList(data.getBerryGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getTreeFruitGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getVegetableGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getNeHorticultureGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getNurseryGrid(), inventoryList, farmingOperation);
    addSupplementalCropsToInventoryList(data.getGrainGrid(), inventoryList, farmingOperation);

    for (CattleGrid cattleGrid : data.getCattleGrid()) {
      if (cattleGrid != null && cattleGrid.getCattleType() != null && cattleGrid.getCattleType().getValue() != null) {
        InventoryItem inventoryItem = createLivestockInventoryItemObject();
        if (cattleGrid.getEndingInventoryCattle() != null) {
          inventoryItem.setAdjQuantityEnd(cattleGrid.getEndingInventoryCattle());
        }
        if (cattleGrid.getEndingFmvCattle() != null) {
          inventoryItem.setAdjPriceEnd(cattleGrid.getEndingFmvCattle());
        }
        
        inventoryItem.setInventoryItemCode(cattleGrid.getCattleType().getValue());
        inventoryItem.setFarmingOperation(farmingOperation);

        inventoryList.add(inventoryItem);
      }
    }
    
    addSupplementalLivestockToInventoryList(data.getPoultryGrid(), inventoryList, farmingOperation);
    addSupplementalLivestockToInventoryList(data.getSwineGrid(), inventoryList, farmingOperation);
    addSupplementalLivestockToInventoryList(data.getOtherGrid(), inventoryList, farmingOperation);
    
    addSupplementalInputToInventoryList(data.getInputGrid(), inventoryList, farmingOperation);
    addSupplementalReceivableToInventoryList(data.getReceivablesGrid(), inventoryList, farmingOperation);
    addSupplementalPayableToInventoryList(data.getExpensesGrid(), inventoryList, farmingOperation);
    
    return inventoryList;
  }

  private InventoryItem createSupplementalCropInventoryItemObject(Double quantityProduced, String units) {

    CropItem cropItem = new CropItem();
    cropItem.setInventoryClassCode(InventoryClassCodes.CROP);

    cropItem.setAdjQuantityProduced(quantityProduced);
    cropItem.setCropUnitCode(units);

    return cropItem;
  }

  private static Map<String, Double> getProductiveCapacityMap(SupplementalBaseDataResource data) {
    Map<String, Double> productiveCapacityMap = new HashMap<>();

    productiveCapacityMap.put("104", data.getNumberOfCowsThatCalved());
    productiveCapacityMap.put("105", data.getNumberFeedersUnder9());
    productiveCapacityMap.put("106", data.getNumberFeedersOver9());
    productiveCapacityMap.put("108", data.getEggsForHatchingLC108());
    productiveCapacityMap.put("109", data.getEggsForConsumptionLC109());
    productiveCapacityMap.put("143", data.getChickenBroilersLC143());
    productiveCapacityMap.put("144", data.getTurkeyBroilersLC144());
    productiveCapacityMap.put("123", data.getNumberOfSowsThatFarrowedLC123());
    productiveCapacityMap.put("124", data.getNumberOfHogsFedUpTo50LbsLC124());
    productiveCapacityMap.put("125", data.getNumberOfHogsFedOver50LbsFeedersLC125());
    return productiveCapacityMap;
  }

  private void addSupplementalGrainsToPucList(List<ProductiveUnitCapacity> productiveUnitCapacityList, List<GrainGrid> crops,
      FarmingOperation farmingOperation) {
    
    for (GrainGrid crop : crops) {
      if (crop.getAcres() != null && crop.getCommodity() != null) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setAdjAmount(crop.getAcres());
        puc.setOnFarmAcres(null);
        puc.setUnseedableAcres(null);
        puc.setInventoryItemCode(crop.getCommodity().getValue());
        puc.setFarmingOperation(farmingOperation);
        productiveUnitCapacityList.add(puc);
      }
    }
  }
  
  private <T extends LivestockGrid> void addSupplementalLivestockToInventoryList(List<T> livestockGridList, List<InventoryItem> inventoryList,
      FarmingOperation farmingOperation) {

    for (LivestockGrid livestockGrid : livestockGridList) {
      if (livestockGrid.getCommodity() != null && livestockGrid.getCommodity().getValue() != null) {
        InventoryItem inventoryItem = createLivestockInventoryItemObject();
        if (livestockGrid.getEndingInventory() != null) {
          inventoryItem.setAdjQuantityEnd(livestockGrid.getEndingInventory());
        }
        if (livestockGrid.getEndingFmv() != null) {
          inventoryItem.setAdjPriceEnd(livestockGrid.getEndingFmv());
        }
        inventoryItem.setInventoryItemCode(livestockGrid.getCommodity().getValue());
        inventoryItem.setFarmingOperation(farmingOperation);

        inventoryList.add(inventoryItem);
      }
    }
  }
  
  private <T extends CropGrid> void addSupplementalCropsToInventoryList(List<T> cropGridList, List<InventoryItem> inventoryList,
      FarmingOperation farmingOperation) {

    for (CropGrid cropGrid : cropGridList) {
      if (cropGrid != null && cropGrid.getCommodity() != null) {

        InventoryItem inventoryItem = null;
        if (cropGrid.getUnits() != null) {
          inventoryItem = createSupplementalCropInventoryItemObject(cropGrid.getQuantityProduced(), cropGrid.getUnits().getValue());
        } else {
          inventoryItem = createSupplementalCropInventoryItemObject(cropGrid.getQuantityProduced(), null);
        }
        
        inventoryItem.setInventoryItemCode(cropGrid.getCommodity().getValue());
        inventoryItem.setFarmingOperation(farmingOperation);

        inventoryList.add(inventoryItem);
      }
    }
  }
  
  private void addSupplementalCropsToPucList(List<ProductiveUnitCapacity> productiveUnitCapacityList, List<CropGrid> crops,
      FarmingOperation farmingOperation) {
    
    for (CropGrid crop : crops) {
      if (crop.getAcres() != null && crop.getCommodity() != null && crop.getCommodity().getValue() != null) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setAdjAmount(crop.getAcres());
        puc.setOnFarmAcres(null);
        puc.setUnseedableAcres(null);
        puc.setInventoryItemCode(crop.getCommodity().getValue());
        puc.setFarmingOperation(farmingOperation);
        productiveUnitCapacityList.add(puc);
      }
    }
  }
  
  private void addSupplementalNurseryToPucList(List<ProductiveUnitCapacity> productiveUnitCapacityList, List<NurseryGrid> nurseryGrid,
      FarmingOperation farmingOperation) {
    for (NurseryGrid crop : nurseryGrid) {
      if (crop.getCommodity() != null && crop.getCommodity().getValue() != null) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        if (crop.getSquareMeters() != null) {
          puc.setAdjAmount(Double.valueOf(crop.getSquareMeters()));
        }
        puc.setOnFarmAcres(null);
        puc.setUnseedableAcres(null);
        puc.setInventoryItemCode(crop.getCommodity().getValue());
        puc.setFarmingOperation(farmingOperation);
        productiveUnitCapacityList.add(puc);
      }
    }
  }

  private void addSupplementalInputToInventoryList(List<InputGrid> inputList, List<InventoryItem> inventoryList,
      FarmingOperation farmingOperation) {

    if (inputList != null) {
      for (InputGrid input : inputList) {
        if (input.getInput() != null && input.getInput().getValue() != null) {
          InventoryItem inventoryItem = createAccrualInventoryItemObject(InventoryClassCodes.INPUT);

          if (input.getAmountRemainingAtYearEnd() != null) {
            inventoryItem.setAdjEndOfYearAmount(input.getAmountRemainingAtYearEnd());
          }
          inventoryItem.setInventoryItemCode(input.getInput().getValue());
          inventoryItem.setFarmingOperation(farmingOperation);

          inventoryList.add(inventoryItem);
        }
      }
    }
  }

  private void addSupplementalReceivableToInventoryList(List<ReceivablesGrid> receivableList, List<InventoryItem> inventoryList,
      FarmingOperation farmingOperation) {

    if (receivableList != null) {
      for (ReceivablesGrid receivable : receivableList) {
        if (receivable != null && receivable.getIncomeSource() != null && receivable.getIncomeSource().getValue() != null) {
          InventoryItem inventoryItem = createAccrualInventoryItemObject(InventoryClassCodes.RECEIVABLE);
          String inventoryItemCode = receivable.getIncomeSource().getValue();

          if (receivable.getIncomeReceivedAfterYearEnd() != null) {
            inventoryItem.setAdjEndOfYearAmount(receivable.getIncomeReceivedAfterYearEnd());
          }
          inventoryItem.setInventoryItemCode(inventoryItemCode);
          inventoryItem.setFarmingOperation(farmingOperation);

          inventoryList.add(inventoryItem);
        }
      }
    }
  }

  private void addSupplementalPayableToInventoryList(List<PayableGrid> payableList, List<InventoryItem> inventoryList,
      FarmingOperation farmingOperation) {

    if (payableList != null) {
      for (PayableGrid payable : payableList) {
        if (payable.getSourceOfExpense() != null && payable.getSourceOfExpense().getValue() != null) {
          InventoryItem inventoryItem = createAccrualInventoryItemObject(InventoryClassCodes.PAYABLE);
          String inventoryItemCode = payable.getSourceOfExpense().getValue();

          if (payable.getProgramExpensesNotPaidByYearEnd() != null) {
            inventoryItem.setAdjEndOfYearAmount(payable.getProgramExpensesNotPaidByYearEnd());
          }
          inventoryItem.setInventoryItemCode(inventoryItemCode);
          inventoryItem.setFarmingOperation(farmingOperation);

          inventoryList.add(inventoryItem);
        }
      }
    }
  }

  private InventoryItem createAccrualInventoryItemObject(String inventoryClassCode) {

    InventoryItem item = null;
    switch (inventoryClassCode) {
    case InventoryClassCodes.INPUT:
      item = new InputItem();
      item.setInventoryClassCode(InventoryClassCodes.INPUT);
      break;
    case InventoryClassCodes.RECEIVABLE:
      item = new ReceivableItem();
      item.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      break;
    case InventoryClassCodes.PAYABLE:
      item = new PayableItem();
      item.setInventoryClassCode(InventoryClassCodes.PAYABLE);
      break;
    }

    return item;
  }

  @Override
  public Integer createCoverageChefsScenario(CoverageSubmissionDataResource data, Integer clientId, Integer programYear, String applicationVersion,
      String municipalityCode, String user) throws ServiceException {
    
    logMethodStart(logger);

    CalculatorDAO calculatorDAO = new CalculatorDAO();
    Transaction transaction = null;
    Integer programYearVersionId;
    Integer chefsScenarioId;
    Integer userScenarioNumber;

    try {
      transaction = openTransaction();
      transaction.begin();

      Integer programYearId = getProgramYearId(transaction, clientId, programYear);

      if (programYearId == null) {
        programYearId = calculatorDAO.createProgramYear(transaction, clientId, programYear, user);
      }

      programYearVersionId = calculatorDAO.createProgramYearVersion(transaction, programYearId,
          municipalityCode, user);
      
      FarmingOperation farmingOperation = createFarmingOperation(null,
          null, programYearVersionId);
      calculatorDAO.createFarmingOperation(transaction, farmingOperation, user);

      List<ProductiveUnitCapacity> pucList = createPucList(data, farmingOperation);
      createProductiveUnitCapacities(transaction, pucList, user);

      chefsScenarioId = calculatorDAO.createScenario(transaction, programYearVersionId, ScenarioTypeCodes.CHEF,
          ScenarioCategoryCodes.CHEF_CN, user);
      userScenarioNumber = calculatorDAO.saveScenarioAsNew(transaction, chefsScenarioId, ScenarioTypeCodes.USER,
          ScenarioCategoryCodes.COVERAGE_NOTICE, applicationVersion, user);

      transaction.commit();

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      if (transaction != null) {
        transaction.rollback();
      }
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return userScenarioNumber;
  }

  private List<ProductiveUnitCapacity> createPucList(CoverageSubmissionDataResource data, FarmingOperation farmingOperation) {

    Map<String, Double> productiveCapacityMap = getProductiveCapacityMap(data);

    List<ProductiveUnitCapacity> productiveUnitCapacityList = new ArrayList<>();

    for (Map.Entry<String, Double> entry : productiveCapacityMap.entrySet()) {
      if (entry.getValue() != null && entry.getValue() > 0) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setAdjAmount(entry.getValue());
        puc.setFarmingOperation(farmingOperation);
        if (entry.getKey().length() == 3) {
          puc.setStructureGroupCode(entry.getKey());
        } else {
          puc.setInventoryItemCode(entry.getKey());
        }
        productiveUnitCapacityList.add(puc);

      }
    }
    
    getProductiveCapacityFromCoverageCropGrid(data.getForageBasketGrid(), farmingOperation, productiveUnitCapacityList);
    getProductiveCapacityFromCoverageCropGrid(data.getForageSeedGrid(), farmingOperation, productiveUnitCapacityList);
    getProductiveCapacityFromCoverageCropGrid(data.getCropBasketTypeGrid(), farmingOperation, productiveUnitCapacityList);

    getProductiveCapacityFromCoverageNurseryGrid(data.getNurseryGrid(), farmingOperation, productiveUnitCapacityList);
    getProductiveCapacityFromCoverageVeggieGrid(data.getVeggieGrid(), farmingOperation, productiveUnitCapacityList);

    return productiveUnitCapacityList;
  }

  private void getProductiveCapacityFromCoverageCropGrid(List<CoverageCropGrid> cropGrid, FarmingOperation farmingOperation,
      List<ProductiveUnitCapacity> productiveUnitCapacityList) {
    if (cropGrid == null) {
      return;
    }
    
    for (CoverageCropGrid crop : cropGrid) {
      if (crop.getCrop() != null && !crop.getCrop().getValue().isEmpty() &&
          crop.getAcres() != null && crop.getAcres() > 0) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        puc.setInventoryItemCode(crop.getCrop().getValue());
        puc.setFarmingOperation(farmingOperation);
        puc.setAdjAmount(crop.getAcres());
        productiveUnitCapacityList.add(puc);
      }
    }
  }
  
  private void getProductiveCapacityFromCoverageNurseryGrid(List<CoverageNurseryGrid> nurseryGrid, FarmingOperation farmingOperation,
      List<ProductiveUnitCapacity> productiveUnitCapacityList) {
    
    if (nurseryGrid == null) {
      return;
    }
    
    for (CoverageNurseryGrid nursery : nurseryGrid) {
      if (nursery.getCommodity() != null && nursery.getCommodity().getLabel() != null) {

        Double adjAmount = 0.0;
        if (nursery.getSquareMeters() != null) {
          adjAmount = nursery.getSquareMeters();
        } else if (nursery.getNumberOfPlants() != null) {
          adjAmount = nursery.getNumberOfPlants().doubleValue();
        }
        
        if (adjAmount > 0) {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          puc.setInventoryItemCode(nursery.getCommodity().getValue());
          puc.setFarmingOperation(farmingOperation);
          puc.setAdjAmount(adjAmount);
          productiveUnitCapacityList.add(puc);
        }
      }
    }
  }
  
  private void getProductiveCapacityFromCoverageVeggieGrid(List<CoverageVeggieGrid> veggieGrid, FarmingOperation farmingOperation,
      List<ProductiveUnitCapacity> productiveUnitCapacityList) {
    
    if (veggieGrid == null) {
      return;
    }
    
    for (CoverageVeggieGrid veggie : veggieGrid) {
      if (veggie.getVegetables() != null && veggie.getVegetables().getLabel() != null) {
        
        Double adjAmount = 0.0;
        if (veggie.getSquareMeters() != null) {
          adjAmount = veggie.getSquareMeters();
        } else if (veggie.getAcres() != null) {
          adjAmount = veggie.getAcres();
        }
        
        if (adjAmount > 0) {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          puc.setInventoryItemCode(veggie.getVegetables().getValue());
          puc.setFarmingOperation(farmingOperation);
          puc.setAdjAmount(adjAmount);
          productiveUnitCapacityList.add(puc);
        }
      }
    }
  }
  
  private static Map<String, Double> getProductiveCapacityMap(CoverageSubmissionDataResource data) {
    Map<String, Double> productiveCapacityMap = new HashMap<>();

    productiveCapacityMap.put("104", data.getBredCow_104());
    productiveCapacityMap.put("105", data.getFeederCattleFedUpTo900Lbs_105());
    productiveCapacityMap.put("106", data.getFeederCattleFedOver900Lbs_106());
    productiveCapacityMap.put("108", data.getLayersEggsForHatching_108());
    productiveCapacityMap.put("109", data.getLayersEggsForConsumption_109());
    productiveCapacityMap.put("113", data.getDairyOfButterfatPerDay_113());
    productiveCapacityMap.put("124", data.getFeederHogsFedOver50Lbs_124());
    productiveCapacityMap.put("125", data.getFeederHogsFedUpTo50Lbs_125());
    productiveCapacityMap.put("126", data.getHoneybees_126());
    productiveCapacityMap.put("129", data.getLeafCutterBees_129());
    productiveCapacityMap.put("141", data.getNumberOfCustomFedCattle_141());
    productiveCapacityMap.put("142", data.getNumberOfCustomFedHogs_142());
    productiveCapacityMap.put("143", data.getBroilersChickens_143());
    productiveCapacityMap.put("144", data.getBroilersTurkeys_144());
    productiveCapacityMap.put("145", data.getHogsFarrowing_145());

    productiveCapacityMap.put("5054", data.getPearAcres_5054());
    productiveCapacityMap.put("5056", data.getPlumAcres_5056());
    productiveCapacityMap.put("5052", data.getPeachAcres_5052());
    productiveCapacityMap.put("5058", data.getPruneAcres_5058());
    productiveCapacityMap.put("5032", data.getApricotAcres_5032());
    productiveCapacityMap.put("4997", data.getFirstYearAcres_4997());
    productiveCapacityMap.put("5048", data.getNectarineAcres_5048());
    productiveCapacityMap.put("5018", data.getRaspberryAcres_5018());
    productiveCapacityMap.put("4782", data.getThirdYearAcres_4782());
    productiveCapacityMap.put("4783", data.getFourthYearAcres_4783());
    productiveCapacityMap.put("4781", data.getSecondYearAcres_4781());
    productiveCapacityMap.put("4780", data.getPlantingYearAcres_4780());
    productiveCapacityMap.put("4800", data.getPlantingYearAcres_4800());
    productiveCapacityMap.put("4980", data.getPlantingYearAcres_4980());
    productiveCapacityMap.put("4995", data.getPlantingYearAcres_4995());
    productiveCapacityMap.put("4801", data.getNonBearingYearAcres_4801());
    productiveCapacityMap.put("4981", data.getNonBearingYearAcres_4981());
    productiveCapacityMap.put("4996", data.getNonBearingYearAcres_4996());
    productiveCapacityMap.put("4824", data.getGala24YearProductionAcres_4824());
    productiveCapacityMap.put("4826", data.getGala5YearProductionAcres_4826());
    productiveCapacityMap.put("4866", data.getOther5YearProductionAcres_4866());
    productiveCapacityMap.put("4998", data.getSecondYearProductionAcres_4998());
    productiveCapacityMap.put("5059", data.getBlueberryPlantingYearAcres_5059());
    productiveCapacityMap.put("4822", data.getGala1stYearProductionAcres_4822());
    productiveCapacityMap.put("4865", data.getOther24YearProductionAcres_4865());
    productiveCapacityMap.put("4862", data.getOther1stYearProductionAcres_4862());
    productiveCapacityMap.put("5060", data.getBlueberryNonBearingYearAcres_5060());
    productiveCapacityMap.put("4816", data.getHighValue5YearProductionAcres_4816());
    productiveCapacityMap.put("5062", data.getBlueberry36YearProductionAcres_5062());
    productiveCapacityMap.put("4991", data.getCranberry1stYearProductionAcres_4991());
    productiveCapacityMap.put("4992", data.getCranberry2ndYearProductionAcres_4992());
    productiveCapacityMap.put("4993", data.getCranberry3rdYearProductionAcres_4993());
    productiveCapacityMap.put("4994", data.getCranberry4thYearProductionAcres_4994());
    productiveCapacityMap.put("4812", data.getHighValue1stYearProductionAcres_4812());
    productiveCapacityMap.put("5064", data.getBlueberry10thYearProductionAcres_5064());
    productiveCapacityMap.put("4990", data.getCranberryEstablishmentStageAcres_4990());
    productiveCapacityMap.put("5063", data.getBlueberry7th9thYearProductionAcres_5063());
    productiveCapacityMap.put("4815", data.getHighValue2nd4thYearProductionAcres_4815());
    productiveCapacityMap.put("5061", data.getBlueberry1stAnd2ndYearProductionAcres_5061());
    productiveCapacityMap.put("4953", data.getLowDensityCherries7YearProductionAcres_4953());
    productiveCapacityMap.put("4956", data.getHighDensityCherries3YearProductionAcres_4956());
    productiveCapacityMap.put("4954", data.getHighDensityCherries1stYearProductionAcres_4954());
    productiveCapacityMap.put("4955", data.getHighDensityCherries2ndYearProductionAcres_4955());
    productiveCapacityMap.put("4950", data.getLowDensityCherries1stAnd2ndYearProductionAcres_4950());
    productiveCapacityMap.put("4951", data.getLowDensityCherries3rdAnd4thYearProductionAcres_4951());
    productiveCapacityMap.put("4952", data.getLowDensityCherries5thAnd6thYearProductionAcres_4952());

    productiveCapacityMap.put("6960", data.getChristmasTreesEstablishmentAcres_6960());
    productiveCapacityMap.put("6961", data.getChristmasTreesEstablishmentAcres_6961());
    productiveCapacityMap.put("6962", data.getChristmasTreesEstablishmentAcres_6962());
    productiveCapacityMap.put("6963", data.getChristmasTreesEstablishmentAcres_6963());
    productiveCapacityMap.put("6964", data.getChristmasTreesEstablishmentAcres_6964());

    if (data.getTreeFruitGrid() != null) {
      for (CoverageCommodityGrid coverageCommodityGrid : data.getTreeFruitGrid()) {
        if(coverageCommodityGrid.getCommodity() != null) {
          String key = coverageCommodityGrid.getCommodity().getValue();
          Double value = coverageCommodityGrid.getAcres();
          productiveCapacityMap.put(key, value);
        }
      }
    }

    if (data.getBerryGrid() != null) {
      for (CoverageCommodityGrid coverageCommodityGrid : data.getBerryGrid()) {
        if(coverageCommodityGrid.getCommodity() != null) {
          String key = coverageCommodityGrid.getCommodity().getValue();
          Double value = coverageCommodityGrid.getAcres();
          productiveCapacityMap.put(key, value);
        }
      }
    }

    if (data.getGrainGrid() != null) {
      for (CoverageCommodityGrid coverageCommodityGrid : data.getGrainGrid()) {
        if(coverageCommodityGrid.getCommodity() != null) {
          String key = coverageCommodityGrid.getCommodity().getValue();
          Double value = coverageCommodityGrid.getAcres();
          productiveCapacityMap.put(key, value);
        }
      }
    }

    if (data.getLivestockGrid() != null) {
      for (CoverageCommodityGrid coverageCommodityGrid : data.getLivestockGrid()) {
        if(coverageCommodityGrid.getCommodity() != null) {
          String key = coverageCommodityGrid.getCommodity().getValue();
          Double value = coverageCommodityGrid.getAcres();
          productiveCapacityMap.put(key, value);
        }
      }
    }

    return productiveCapacityMap;
  }
  
  @Override
  public Integer createStatementAChefsScenario(StatementASubmissionDataResource data, Integer clientId, Integer programYear,
      String applicationVersion, String municipalityCode, String user,  String scenarioCategoryCode) throws ServiceException {

    logMethodStart(logger);

    CalculatorDAO calculatorDAO = new CalculatorDAO();
    Transaction transaction = null;
    Integer programYearVersionId;
    Integer chefsScenarioId;

    try {
      transaction = openTransaction();
      transaction.begin();

      Integer programYearId = getProgramYearId(transaction, clientId, programYear);

      if (programYearId == null) {
        programYearId = calculatorDAO.createProgramYear(transaction, clientId, programYear, user);
      }

      programYearVersionId = calculatorDAO.createProgramYearVersion(transaction, programYearId,
          municipalityCode, user);

      FarmingOperation farmingOperation = createFarmingOperation(null,
          null, programYearVersionId);
      calculatorDAO.createFarmingOperation(transaction, farmingOperation, user);
      
      List<ProductiveUnitCapacity> pucList = createPucList(data, farmingOperation);
      createProductiveUnitCapacities(transaction, pucList, user);

      List<InventoryItem> inventoryItemList = createSupplementalInventoryList(data, farmingOperation);
      createInventoryItems(transaction, inventoryItemList, user);
      
      List<IncomeExpense> incomeExpensesList = createStatementAIncomeExpensesList(data, farmingOperation);
      createIncomeExpenses(transaction, incomeExpensesList, user);

      chefsScenarioId = calculatorDAO.createScenario(transaction, programYearVersionId, ScenarioTypeCodes.CHEF,
          scenarioCategoryCode, user);
      
      transaction.commit();

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      if (transaction != null) {
        transaction.rollback();
      }
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return chefsScenarioId;
  }
  
  private List<IncomeExpense> createStatementAIncomeExpensesList(StatementASubmissionDataResource data, FarmingOperation farmingOperation) {

    List<IncomeExpense> incomeExpenseList = new ArrayList<>();

    AddToIncomeExpenseList(data.getAllowableIncomeGrid(), farmingOperation, incomeExpenseList, true, false);
    AddToIncomeExpenseList(data.getNonAllowablesGrid(), farmingOperation, incomeExpenseList, false, false);

    AddToIncomeExpenseList(data.getAllowableExpensesGrid(), farmingOperation, incomeExpenseList, true, true);
    AddToIncomeExpenseList(data.getNonAllowableExpensesGrid(), farmingOperation, incomeExpenseList, false, true);

    return incomeExpenseList;

  }

  private void AddToIncomeExpenseList(List<IncomeExpenseGrid> statementAIncomeExpenses, FarmingOperation farmingOperation,
      List<IncomeExpense> incomeList, boolean isEligible, boolean isExpense) {

    Double otherAllowableAmount = 0.0;
    int lineItemCode = 0;
    IncomeExpense otherAllowableItem = new IncomeExpense();

    if (statementAIncomeExpenses != null && statementAIncomeExpenses.size() > 0) {
      for (IncomeExpenseGrid income : statementAIncomeExpenses) {
        if (income.getCategory() != null && income.getCategory().getValue() != null && income.getAmount() != null) {
          if (!isExpense && Integer.valueOf(income.getCategory().getValue()) == LineItemCodes.OTHER_SPECIFY_9600) {
            otherAllowableAmount += income.getAmount();
            lineItemCode = LineItemCodes.OTHER_SPECIFY_9600;
          } else if (isExpense && Integer.valueOf(income.getCategory().getValue()) == LineItemCodes.OTHER_SPECIFY_9896) {
            otherAllowableAmount += income.getAmount();
            lineItemCode = LineItemCodes.OTHER_SPECIFY_9896;
          } else {
            incomeList.add(createIncomeExpense(income, isEligible, isExpense, farmingOperation));
          }
        }
      }
    }

    if (otherAllowableAmount > 0) {
      LineItem lineItem = new LineItem();
      lineItem.setLineItem(lineItemCode);
      lineItem.setIsEligible(isEligible);
      otherAllowableItem.setAdjAmount(otherAllowableAmount);
      otherAllowableItem.setFarmingOperation(farmingOperation);
      otherAllowableItem.setLineItem(lineItem);
      otherAllowableItem.setIsExpense(isExpense);
      incomeList.add(otherAllowableItem);
    }
  }

  private IncomeExpense createIncomeExpense(IncomeExpenseGrid incomeExpense, boolean isEligible, boolean isExpense,
      FarmingOperation farmingOperation) {
    LineItem lineItem = new LineItem();
    lineItem.setLineItem(Integer.valueOf(incomeExpense.getCategory().getValue()));
    lineItem.setIsEligible(isEligible);

    IncomeExpense item = new IncomeExpense();
    item.setFarmingOperation(farmingOperation);
    item.setLineItem(lineItem);
    item.setAdjAmount(incomeExpense.getAmount());
    item.setIsExpense(isExpense);

    return item;
  }
  
  private List<IncomeExpense> createInterimIncomeExpensesList(InterimSubmissionDataResource data, FarmingOperation farmingOperation) {

    List<IncomeExpense> incomeExpenseList = new ArrayList<>();

    AddToIncomeExpenseList(data.getAllowableIncomeGrid(), farmingOperation, incomeExpenseList, true, false);
    AddToIncomeExpenseList(data.getAllowableExpensesGrid(), farmingOperation, incomeExpenseList, true, true);

    return incomeExpenseList;

  }

}
