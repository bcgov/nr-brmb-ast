/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.service;

import java.util.Date;
import java.util.List;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.ListView;


/**
 * ListService.
 */
public interface ListService {

  /** SERVER_LIST. */
  public static final String SERVER_LIST = "server.list.";
  
  public static final String REGIONAL_OFFICE = "regional.offices";

  public static final String INC_EXP = "income.expense";

  public static final String INC_EXP_ALLOWABLE = "income.expense.allowable";

  public static final String INVENTORY_FIELDS = "inventory.fields";

  public static final String INVENTORY_CLASS = "inventory.classes";
  
  public static final String EXPORT_CLASS = "export.classes";

  /** Key for retrieving farm type codes and descriptions */
  public static final String FARM_TYPE = "farm.types";

  /** Key for retrieving participant class codes and descriptions */
  public static final String PARTICIPANT_CLASS = "participant.classes";

  /** Key for retrieving participant profile codes and descriptions */
  public static final String PARTICIPANT_PROFILE = "participant.profiles";

  /** Key for retrieving federal accounting codes and descriptions */
  public static final String FEDERAL_ACCOUNTING = "federal.accounting";

  /** Key for retrieving federal status codes and descriptions */
  public static final String FEDERAL_STATUS = "federal.statuses";
  
  /** Key for retrieving municipality codes and descriptions */
  public static final String MUNICIPALITY = "municipalities";
  
  /** Key for retrieving scenario state codes and descriptions */
  public static final String SCENARIO_STATE = "scenario.states";
  
  /** Key for retrieving scenario category codes and descriptions */
  public static final String SCENARIO_CATEGORY = "scenario.categories";
  
  /** Key for retrieving scenario class codes and descriptions */
  public static final String SCENARIO_CLASS = "scenario.classes";
  
  /** Key for retrieving inventory item codes and descriptions */
  public static final String INVENTORY_ITEM = "inventory.items";
  
  /** Key for retrieving inventory item codes and descriptions */
  public static final String PUC_INVENTORY_ITEM = "puc.inventory.items";
  
  public static final String INVENTORY_GROUP = "inventory.group.codes";
  
  /** Key for retrieving the current years line items */
  public static final String PROGRAM_YEAR_LINE_ITEMS = "program.year.line.items";
  
  /** Key for retrieving structure group codes and descriptions */
  public static final String STRUCTURE_GROUP = "structure.groups";
  
  /** Key for retrieving crop unit codes and descriptions */
  public static final String CROP_UNIT = "crop.units";
  
  /** Key for retrieving participant class codes and descriptions */
  public static final String CALCULATOR_INBOX_SEARCH_TYPE = "calculator.inbox.search.type";

  public static final String PRODUCTION_CAPACITY_TYPES =
    "production.capacity.types";
  
  public static final String LINE_ITEMS = "line.items";
  
  public static final String SECTOR = "sectors";
  
  public static final String SECTOR_DETAIL = "sector.details";

  public static final String INVENTORY_VALID_ITEM = "inventory.valid.items";

  public static final String GENERIC_CODE_TABLE = "generic.code.tables";
  
  public static final String ADMIN_YEAR = "admin.years";
  
  public static final String TRIAGE_QUEUE = "triage.queue.codes";
  
  public static final String CHEF_SUBMSSN_STATUS_CODE = "chef.submssn.status.codes";

  public static final String VERIFIERS = "VERIFIERS";

  public static final String EXPIRE_DATE = ".expire.date";

  public static final int ONE_HOUR_DURATION = 1;
  

  /**
   * loadLists.
   *
   * @throws  ServiceException  On exception.
   */
  void loadLists() throws ServiceException;

  /**
   * @param  scenario  the scenario
   * @throws  ServiceException  on exception
   */
  void setLineItems(Scenario scenario) throws ServiceException;
  
  /**
   * @param tableName tableName
   * @throws ServiceException On Exception
   */
  void refreshCodeTableList(final String tableName) throws ServiceException;

  ListView[] getListArray(String listName);
  
  List<ListView> getList(String listName);

  ListView[] getListWithExpireDate(String listName, String user) throws ServiceException;

  Date getExpirationDate(String listName);

  void setExpirationDate(String listName, Date date);

}
