/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.Cache;
import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.dao.ListDAO;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ListServiceConstants;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.UserService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.transaction.TransactionProvider;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;
import ca.bc.gov.srm.farm.util.PropertyLoader;


/**
 * ListServiceImpl.
 */
final class TestListServiceImpl extends BaseService implements ListService {

  private Logger log = LoggerFactory.getLogger(getClass());

  
  @Override
  public void setLineItems(Scenario scenario) throws ServiceException {
    // not currently needed for unit test
  }



  /**
   * loadLists.
   *
   * @throws  ServiceException  On exception.
   */
  @Override
  public void loadLists() throws ServiceException {
    clearCache();
    createHardCodedLists();
    createListsFromDatabase();
  }


  /**
   * Create lists from database code tables.
   *
   * @throws  ServiceException  on exception
   */
  private void createListsFromDatabase() throws ServiceException {
    ListView[] list = null;

    //
    // This application doesn't have many code lists because the domain
    // model objects were made to have the descriptions for codes as well
    // as the code. Almost all the "selects" that appear in the webapp
    // contain hard coded lists that can't be data driven.
    //

    list = new ListView[] {};
    cacheApplicationList(PARTICIPANT_CLASS, list);

    list = new ListView[] {};
    cacheApplicationList(FARM_TYPE, list);

    list = new ListView[] {};
    cacheApplicationList(PARTICIPANT_PROFILE, list);

    list = new ListView[] {};
    cacheApplicationList(FEDERAL_STATUS, list);

    list = new ListView[] {};
    cacheApplicationList(FEDERAL_ACCOUNTING, list);
    
    list = new ListView[] {};
    cacheApplicationList(MUNICIPALITY, list);
    
    list = new ListView[] {};
    cacheApplicationList(SCENARIO_STATE, list);

    list = new ListView[] {
        new CodeListView("CRAR","CRA Received"),
        new CodeListView("NCRA","New CRA Data"),
        new CodeListView("CS","Comparison Scenario"),
        new CodeListView("ENW","Enrolment Notice Workflow"),
        new CodeListView("AADJ","Administrative Adjustment"),
        new CodeListView("PADJ","Producer Adjustment"),
        new CodeListView("INT","Interim"),
        new CodeListView("FIN","Final"),
        new CodeListView("UNK","Unknown"),
        new CodeListView("LOCL_ENTRY","Local Data Entry"),
        new CodeListView("PRE_VERIFI","Pre-Verification"),
        new CodeListView("NOL","Notice of Loss"),
        new CodeListView("CHEFR","CHEF Received"),
        new CodeListView("CN","Coverage Notice"),
        new CodeListView("CHEF_ADJ","CHEF Adjustment"),
        new CodeListView("CHEF_CN","CHEF Coverage Notice"),
        new CodeListView("CHEF_INTRM","CHEF Interim"),
        new CodeListView("CHEF_SUPP","CHEF Local Supplemental"),
        new CodeListView("CHEF_NPP","CHEF New Participant Package"),
        new CodeListView("CHEF_NOL","CHEF Notice of Loss"),
        new CodeListView("CHEF_STA","CHEF Statement A"),
    };
    cacheApplicationList(SCENARIO_CATEGORY, list);
    
    list = new ListView[] {
        new CodeListView("DA_ZPP", "Data Assessment: Zero Payment - Pass"),
        new CodeListView("DA_AZF", "Data Assessment: Abbotsford - Zero - Fail"),
        new CodeListView("DA_AU100K ", "Data Assessment: Abbotsford - Under 100K"),
        new CodeListView("DA_KZF", "Data Assessment: Kelowna - Zero - Fail"),
        new CodeListView("DA_KU100K ", "Data Assessment: Kelowna - Under 100K"),
        new CodeListView("DA_VS ", "Data Assessment: Verification Specialist"),
    };
    cacheApplicationList(TRIAGE_QUEUE, list);
    
    list = new ListView[] {};
    cacheApplicationList(INVENTORY_CLASS, list);
    
    list = new ListView[] {};
    cacheApplicationList(SCENARIO_CLASS, list);
    
    list = new ListView[] {};
    cacheApplicationList(INVENTORY_ITEM, list);
    
    list = new ListView[] {};
    cacheApplicationList(PUC_INVENTORY_ITEM, list);
    
    list = new ListView[] {};
    cacheApplicationList(STRUCTURE_GROUP, list);
    
    list = new ListView[] {};
    cacheApplicationList(CROP_UNIT, list);
    
    list = new ListView[] {};
    cacheApplicationList(INVENTORY_VALID_ITEM, list);
    
    list = new ListView[] {};
    cacheApplicationList(INVENTORY_GROUP, list);
    
    list = new ListView[] {};
    cacheApplicationList(REGIONAL_OFFICE, list);
    
    list = new ListView[] {};
    cacheApplicationList(EXPORT_CLASS, list);
    
    list = new ListView[] {};
    cacheApplicationList(SECTOR, list);
    
    list = new ListView[] {};
    cacheApplicationList(SECTOR_DETAIL, list);
      
    list = new ListView[] {};
    cacheApplicationList(PROGRAM_YEAR_LINE_ITEMS, list);

    list = new ListView[] {
        new CodeListView("1","Verifier1"),
        new CodeListView("2","Verifier2"),
    };
    cacheApplicationList(VERIFIERS, list);
    Date expiryDate = DateUtils.addMinutes(new Date(), -5);
    log.debug("TestListService expiryDate : " + expiryDate.toString());
    cacheApplicationListExpiryDate(VERIFIERS, expiryDate);
    
  }

  /**
   * cacheList.
   *
   * @param  key   The parameter value.
   * @param  item  The parameter value.
   */
  private void cacheApplicationList(final String key, final ListView[] item) {
    CacheFactory.getApplicationCache().addItem(SERVER_LIST + key, item);
  }
  
  private void cacheApplicationListExpiryDate(final String key, final Date expiryDate) {
    CacheFactory.getApplicationCache().addItem(SERVER_LIST + key + EXPIRE_DATE, expiryDate);
  }

  /**
   * clearCache.
   */
  private void clearCache() {
    Cache cache = CacheFactory.getApplicationCache();
    cache.removeItem(SERVER_LIST + INC_EXP);
    cache.removeItem(SERVER_LIST + INC_EXP_ALLOWABLE);
    cache.removeItem(SERVER_LIST + INVENTORY_FIELDS);
    cache.removeItem(SERVER_LIST + PRODUCTION_CAPACITY_TYPES);
    cache.removeItem(SERVER_LIST + INVENTORY_CLASS);
    cache.removeItem(SERVER_LIST + EXPORT_CLASS);
    cache.removeItem(SERVER_LIST + PARTICIPANT_CLASS);
    cache.removeItem(SERVER_LIST + PARTICIPANT_PROFILE);
    cache.removeItem(SERVER_LIST + FEDERAL_ACCOUNTING);
    cache.removeItem(SERVER_LIST + FEDERAL_STATUS);
    cache.removeItem(SERVER_LIST + MUNICIPALITY);
    cache.removeItem(SERVER_LIST + SCENARIO_STATE);
    cache.removeItem(SERVER_LIST + SCENARIO_CLASS);
    cache.removeItem(SERVER_LIST + INVENTORY_ITEM);
    cache.removeItem(SERVER_LIST + STRUCTURE_GROUP);
    cache.removeItem(SERVER_LIST + REGIONAL_OFFICE);
    cache.removeItem(SERVER_LIST + PUC_INVENTORY_ITEM);
    cache.removeItem(SERVER_LIST + CROP_UNIT);
    cache.removeItem(SERVER_LIST + CALCULATOR_INBOX_SEARCH_TYPE);
    cache.removeItem(SERVER_LIST + LINE_ITEMS);
    cache.removeItem(SERVER_LIST + SECTOR);
    cache.removeItem(SERVER_LIST + SECTOR_DETAIL);
    cache.removeItem(SERVER_LIST + INVENTORY_VALID_ITEM);
    cache.removeItem(SERVER_LIST + GENERIC_CODE_TABLE);
    cache.removeItem(SERVER_LIST + INVENTORY_GROUP);
    cache.removeItem(SERVER_LIST + VERIFIERS);
    cache.removeItem(SERVER_LIST + FARM_TYPE);
  }


  /** These are not database driven. Just hard code them. */
  private void createHardCodedLists() {
    CodeListView[] codeList = null;

    int index = 0;
    final int numIncExpCodes = 2;
    codeList = new CodeListView[numIncExpCodes];
    codeList[index++] = new CodeListView(
        ListServiceConstants.INCOME_CODE,
        "Income");
    codeList[index++] = new CodeListView(
        ListServiceConstants.EXPENSE_CODE,
        "Expense");
    cacheApplicationList(INC_EXP, codeList);

    index = 0;

    final int numAllowableCodes = 3;
    codeList = new CodeListView[numAllowableCodes];
    codeList[index++] = new CodeListView(
        ListServiceConstants.ALL_CODE,
        "All");
    codeList[index++] = new CodeListView(
        ListServiceConstants.ALLOW_CODE,
        "Allowable");
    codeList[index++] = new CodeListView(
        ListServiceConstants.NON_ALLOWABLE_CODE,
        "Non-Allowable");
    cacheApplicationList(INC_EXP_ALLOWABLE, codeList);

    index = 0;

    final int numInvFieldsCodes = 4;
    codeList = new CodeListView[numInvFieldsCodes];
    codeList[index++] = new CodeListView(
        ListServiceConstants.QUANTITY_PRODUCED,
        "Quantity Produced");
    codeList[index++] = new CodeListView(
        ListServiceConstants.QUANTITY_END,
        "Quantity End");
    codeList[index++] = new CodeListView(
        ListServiceConstants.EOY_PRICE,
        "End of Year Price");
    codeList[index++] = new CodeListView(
        ListServiceConstants.EOY_AMOUNT,
        "End of Year Amount");
    cacheApplicationList(INVENTORY_FIELDS, codeList);

    index = 0;

    final int numCapCodes = 2;
    codeList = new CodeListView[numCapCodes];
    codeList[index++] = new CodeListView(
        ListServiceConstants.CROP_PRODUCTIVE_CAPACITY,
        "Crop Productive Capacity");
    codeList[index++] = new CodeListView(
        ListServiceConstants.LIVESTOCK_PRODUCTIVE_CAPACITY,
        "Livestock Productive Capacity");
    cacheApplicationList(PRODUCTION_CAPACITY_TYPES, codeList);

    index = 0;
    
    final int numSearchTypes = 3;
    codeList = new CodeListView[numSearchTypes];
    codeList[index++] = new CodeListView(
        ListServiceConstants.CALCULATOR_INBOX_SEARCH_TYPE_USER,
    "Checked Out By Me");
    codeList[index++] = new CodeListView(
        ListServiceConstants.CALCULATOR_INBOX_SEARCH_TYPE_ALL_USERS,
    "All Users");
    codeList[index++] = new CodeListView(
        ListServiceConstants.CALCULATOR_INBOX_SEARCH_TYPE_READY,
    "Ready");
    cacheApplicationList(CALCULATOR_INBOX_SEARCH_TYPE, codeList);

    index = 0;

    String appPropertiesPath = "config/applicationResources.properties";
    Properties props = PropertyLoader.loadProperties(appPropertiesPath);
    final int numCodeTables = CodeTables.GENERIC_CODE_TABLES.length;
    codeList = new CodeListView[numCodeTables];
    codeList[index++] =
      new CodeListView(CodeTables.CROP_UNIT, props.getProperty(CodeTables.CROP_UNIT));
    codeList[index++] =
        new CodeListView(CodeTables.FARM_TYPE, props.getProperty(CodeTables.FARM_TYPE));
    codeList[index++] =
      new CodeListView(CodeTables.FEDERAL_ACCOUNTING, props.getProperty(CodeTables.FEDERAL_ACCOUNTING));
    codeList[index++] =
      new CodeListView(CodeTables.FEDERAL_STATUS, props.getProperty(CodeTables.FEDERAL_STATUS));
    codeList[index++] =
      new CodeListView(CodeTables.PARTICIPANT_CLASS, props.getProperty(CodeTables.PARTICIPANT_CLASS));
    codeList[index++] =
      new CodeListView(CodeTables.PARTICIPANT_LANGUAGE, props.getProperty(CodeTables.PARTICIPANT_LANGUAGE));
    codeList[index++] =
      new CodeListView(CodeTables.PARTICIPANT_PROFILE, props.getProperty(CodeTables.PARTICIPANT_PROFILE));
    codeList[index++] =
        new CodeListView(CodeTables.TRIAGE_QUEUE, props.getProperty(CodeTables.TRIAGE_QUEUE));
    cacheApplicationList(GENERIC_CODE_TABLE, codeList);

    index = 0;
    
    int[] adminYears = ProgramYearUtils.getAdminYears();
    codeList = new CodeListView[adminYears.length];
    for(int ii = 0; ii < adminYears.length; ii++) {
      String year = Integer.toString(adminYears[ii]);
      CodeListView clv = new CodeListView(year, year);
      codeList[index++] = clv;
    }
    cacheApplicationList(ADMIN_YEAR, codeList);
  }

  /**
   * @param tableName tableName
   * @throws ServiceException On Exception
   */
  @Override
  public void refreshCodeTableList(final String tableName) throws ServiceException {

    Transaction transaction = null;
    TransactionProvider transactionProvider = null;
    ListView[] list = null;

    try {
      transactionProvider = TransactionProvider.getInstance();
      transaction = transactionProvider.getTransaction(BusinessAction.system());

      ListDAO listDao = new ListDAO();

      if(CodeTables.FARM_TYPE.equals(tableName)) {
        list = listDao.getFarmTypeCodes(transaction);
        cacheApplicationList(FARM_TYPE, list);
      } else if(CodeTables.PARTICIPANT_CLASS.equals(tableName)) {
        list = listDao.getParticipantClassCodes(transaction);
        cacheApplicationList(PARTICIPANT_CLASS, list);
      } else if(CodeTables.PARTICIPANT_PROFILE.equals(tableName)) {
        list = listDao.getParticipantProfileCodes(transaction);
        cacheApplicationList(PARTICIPANT_PROFILE, list);
      } else if(CodeTables.FEDERAL_STATUS.equals(tableName)) {
        list = listDao.getFederalStatusCodes(transaction);
        cacheApplicationList(FEDERAL_STATUS, list);
      } else if(CodeTables.FEDERAL_ACCOUNTING.equals(tableName)) {
        list = listDao.getFederalAccountingCodes(transaction);
        cacheApplicationList(FEDERAL_ACCOUNTING, list);
      } else if(CodeTables.MUNICIPALITY.equals(tableName)) {
        list = listDao.getMunicipalityCodes(transaction);
        cacheApplicationList(MUNICIPALITY, list);
      } else if(CodeTables.REGIONAL_OFFICE.equals(tableName)) {
        list = listDao.getRegionalOfficeCodes(transaction);
        cacheApplicationList(REGIONAL_OFFICE, list);
      } else if(CodeTables.CROP_UNIT.equals(tableName)) {
        list = listDao.getCropUnitCodes(transaction);
        cacheApplicationList(CROP_UNIT, list);
      } else if(CodeTables.STRUCTURE_GROUP.equals(tableName)) {
        list = listDao.getStructureGroupCodes(transaction);
        cacheApplicationList(STRUCTURE_GROUP, list);
        list = listDao.getPucInventoryItemCodes(transaction);
        cacheApplicationList(PUC_INVENTORY_ITEM, list);
      } else if(CodeTables.INVENTORY_ITEM.equals(tableName)) {
        list = listDao.getPucInventoryItemCodes(transaction);
        cacheApplicationList(PUC_INVENTORY_ITEM, list);
        list = listDao.getInventoryItemCodes(transaction);
        cacheApplicationList(INVENTORY_ITEM, list);
        list = listDao.getInventoryValidItems(transaction);
        cacheApplicationList(INVENTORY_VALID_ITEM, list);
      } else if(CodeTables.TRIAGE_QUEUE.equals(tableName)) {
        list = listDao.getTriageQueueCodes(transaction);
        cacheApplicationList(TRIAGE_QUEUE, list);
      } else if(ListServiceConstants.VERIFIERS.equals(tableName)) {
        list = listDao.getVerifiers(transaction);
        cacheApplicationList(VERIFIERS, list);
        Date ExpiryDate = DateUtils.addHours(new Date(), ONE_HOUR_DURATION);
        log.debug("refreshCodeTableList ExpiryDate : " + ExpiryDate.toString());
        cacheApplicationListExpiryDate(VERIFIERS, ExpiryDate);
      } else if(CodeTables.PARTICIPANT_LANGUAGE.equals(tableName)) {
        // ignore since there is currently no cached list for this
      } else {
        throw new UnsupportedOperationException(
            "refreshCodeTableList() is not supported for code table: " + tableName);
      }
    } catch (DataAccessException e) {
      log.error("Data Access Exception during refreshCodeTableList(): " + e.getMessage(),
        e);
      transactionProvider.rollback(transaction);
      throw new ServiceException(e);
    } catch (ProviderException e) {
      log.error("Provider Exception during refreshCodeTableList(): " + e.getMessage(), e);
      transactionProvider.rollback(transaction);
      throw new ServiceException(e);
    } finally {
      if(transactionProvider != null) {
        transactionProvider.close(transaction);
      }
    }
  }

  @Override
  public ListView[] getListArray(String listName) {
    return CacheFactory.getApplicationCache().getItem(SERVER_LIST + listName, ListView[].class);
  }
  
  @Override
  public List<ListView> getList(String listName) {
    log.debug("listName: " + listName);
    return Arrays.asList(getListArray(listName));
  }

  @Override
  public Date getExpirationDate(String listName) {
    return CacheFactory.getApplicationCache().getItem(SERVER_LIST + listName + EXPIRE_DATE, Date.class);
  }
  
  @Override
  public void setExpirationDate(String listName, Date date) {
    cacheApplicationListExpiryDate(listName, date);
  }
  
  @Override
  public ListView[] getListWithExpireDate(String listName, String user) throws ServiceException {

    Date expireDate = getExpirationDate(listName);
    log.debug("getListWithExpireDate date=" + expireDate);
    if ((expireDate == null || expireDate.before(new Date())) && ListServiceConstants.VERIFIERS.equals(listName)) {
      log.debug(listName + " list is expire.");
      UserService userService = ServiceFactory.getUserService();
      userService.syncVerifiers(user);
      refreshCodeTableList(listName);
    }
    return getListArray(listName);
  }
}
