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
package ca.bc.gov.srm.farm.dao;

import java.io.IOException;
import java.io.Writer;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.codes.BPU;
import ca.bc.gov.srm.farm.domain.codes.BPUYear;
import ca.bc.gov.srm.farm.domain.codes.Code;
import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.domain.codes.ConfigurationParameter;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversionItem;
import ca.bc.gov.srm.farm.domain.codes.DocumentTemplate;
import ca.bc.gov.srm.farm.domain.codes.ExpectedProduction;
import ca.bc.gov.srm.farm.domain.codes.FMV;
import ca.bc.gov.srm.farm.domain.codes.FMVPeriod;
import ca.bc.gov.srm.farm.domain.codes.FarmSubtype;
import ca.bc.gov.srm.farm.domain.codes.FarmType3;
import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCode;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemCode;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemDetail;
import ca.bc.gov.srm.farm.domain.codes.InventoryXref;
import ca.bc.gov.srm.farm.domain.codes.LineItemCode;
import ca.bc.gov.srm.farm.domain.codes.MarketRatePremium;
import ca.bc.gov.srm.farm.domain.codes.MunicipalityCode;
import ca.bc.gov.srm.farm.domain.codes.SectorCode;
import ca.bc.gov.srm.farm.domain.codes.SectorDetailCode;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCode;
import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.domain.codes.TipLineItem;
import ca.bc.gov.srm.farm.domain.codes.YearConfigurationParameter;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;


/**
 * @author awilkinson
 * @created Aug 15, 2011
 */
@SuppressWarnings("resource")
public class CodesWriteDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_CODES_WRITE_PKG";

  // Line Item procs
  private static final String CREATE_LINE_ITEM_PROC = "CREATE_LINE_ITEM";
  private static final int CREATE_LINE_ITEM_PARAM = 17;
  private static final String UPDATE_LINE_ITEM_PROC = "UPDATE_LINE_ITEM";
  private static final int UPDATE_LINE_ITEM_PARAM = 19;
  private static final String IN_USE_LINE_ITEM_PROC = "IN_USE_LINE_ITEM";
  private static final int IN_USE_LINE_ITEM_PARAM = 2;
  private static final String DELETE_LINE_ITEM_PROC = "DELETE_LINE_ITEM";
  private static final int DELETE_LINE_ITEM_PARAM = 4;
  private static final String COPY_YEAR_LINE_ITEMS_PROC = "COPY_YEAR_LINE_ITEMS";
  private static final int COPY_YEAR_LINE_ITEMS_PARAM = 2;
  
  // FMV procs
  private static final String CREATE_FMV_PROC = "CREATE_FMV";
  private static final int CREATE_FMV_PARAM = 8;
  private static final String UPDATE_FMV_PROC = "UPDATE_FMV";
  private static final int UPDATE_FMV_PARAM = 10;
  private static final String IN_USE_FMV_PROC = "IN_USE_FMV";
  private static final int IN_USE_FMV_PARAM = 4;
  private static final String DELETE_FMV_PROC = "DELETE_FMV";
  private static final int DELETE_FMV_PARAM = 4;
  
  // BPU proc
  private static final String UPDATE_BPU_YEAR_PROC = "UPDATE_BPU_YEAR";
  private static final String IN_USE_BPU_PROC = "IN_USE_BPU";
  private static final String DELETE_BPU_PROC = "DELETE_BPU";
  private static final String CREATE_BPU_PROC = "CREATE_BPU";
  private static final String CREATE_BPU_YEAR_PROC = "CREATE_BPU_YEAR";

  // Municipality procs
  private static final String CREATE_MUNICIPALITY_CODE_PROC = "CREATE_MUNICIPALITY_CODE";
  private static final int CREATE_MUNICIPALITY_CODE_PARAM = 6;
  private static final String UPDATE_MUNICIPALITY_CODE_PROC = "UPDATE_MUNICIPALITY_CODE";
  private static final int UPDATE_MUNICIPALITY_CODE_PARAM = 7;
  private static final String IN_USE_MUNICIPALITY_CODE_PROC = "IN_USE_MUNICIPALITY_CODE";
  private static final int IN_USE_MUNICIPALITY_CODE_PARAM = 1;
  private static final String DELETE_MUNICIPALITY_CODE_PROC = "DELETE_MUNICIPALITY_CODE";
  private static final int DELETE_MUNICIPALITY_CODE_PARAM = 2;

  // Inventory Item procs
  private static final String CREATE_INVENTORY_ITEM_CODE_PROC = "CREATE_INVENTORY_ITEM_CODE";
  private static final int CREATE_INVENTORY_ITEM_CODE_PARAM = 6;
  private static final String UPDATE_INVENTORY_ITEM_CODE_PROC = "UPDATE_INVENTORY_ITEM_CODE";
  private static final int UPDATE_INVENTORY_ITEM_CODE_PARAM = 7;
  private static final String IN_USE_INVENTORY_ITEM_CODE_PROC = "IN_USE_INVENTORY_ITEM_CODE";
  private static final int IN_USE_INVENTORY_ITEM_CODE_PARAM = 1;
  private static final String DELETE_INVENTORY_ITEM_CODE_PROC = "DELETE_INVENTORY_ITEM_CODE";
  private static final int DELETE_INVENTORY_ITEM_CODE_PARAM = 2;
  private static final String CREATE_INVENTORY_ITEM_DETAIL_PROC = "CREATE_INVENTORY_ITEM_DETAIL";
  private static final String UPDATE_INVENTORY_ITEM_DETAIL_PROC = "UPDATE_INVENTORY_ITEM_DETAIL";
  // Inventory Xref procs
  private static final String CREATE_INVENTORY_XREF_PROC = "CREATE_INVENTORY_XREF";
  private static final int CREATE_INVENTORY_XREF_PARAM = 5;
  private static final String UPDATE_INVENTORY_XREF_PROC = "UPDATE_INVENTORY_XREF";
  private static final int UPDATE_INVENTORY_XREF_PARAM = 5;
  private static final String IN_USE_INVENTORY_XREF_PROC = "IN_USE_INVENTORY_XREF";
  private static final int IN_USE_INVENTORY_XREF_PARAM = 1;
  private static final String DELETE_INVENTORY_XREF_PROC = "DELETE_INVENTORY_XREF";
  private static final int DELETE_INVENTORY_XREF_PARAM = 2;
  // Market Rate Premium procs
  private static final String CREATE_MARKET_RATE_PREMIUM_PROC = "CREATE_MARKET_RATE_PREMIUM";
  private static final int CREATE_MARKET_RATE_PREMIUM_PARAM = 6;
  private static final String UPDATE_MARKET_RATE_PREMIUM_PROC = "UPDATE_MARKET_RATE_PREMIUM";
  private static final int UPDATE_MARKET_RATE_PREMIUM_PARAM = 8;
  private static final String DELETE_MARKET_RATE_PREMIUM_PROC = "DELETE_MARKET_RATE_PREMIUM";
  private static final int DELETE_MARKET_RATE_PREMIUM_PARAM = 2;

  // Structure Group procs
  private static final String CREATE_STRUCTURE_GROUP_CODE_PROC = "CREATE_STRUCTURE_GROUP_CODE";
  private static final int CREATE_STRUCTURE_GROUP_CODE_PARAM = 6;
  private static final String UPDATE_STRUCTURE_GROUP_CODE_PROC = "UPDATE_STRUCTURE_GROUP_CODE";
  private static final int UPDATE_STRUCTURE_GROUP_CODE_PARAM = 7;
  private static final String IN_USE_STRUCTURE_GROUP_CODE_PROC = "IN_USE_STRUCTURE_GROUP_CODE";
  private static final int IN_USE_STRUCTURE_GROUP_CODE_PARAM = 1;
  private static final String DELETE_STRUCTURE_GROUP_CODE_PROC = "DELETE_STRUCTURE_GROUP_CODE";
  private static final int DELETE_STRUCTURE_GROUP_CODE_PARAM = 2;

  private static final int CREATE_GENERIC_CODE_PARAM = 5;
  private static final int UPDATE_GENERIC_CODE_PARAM = 6;
  private static final int IN_USE_GENERIC_CODE_PARAM = 1;
  private static final int DELETE_GENERIC_CODE_PARAM = 2;

  // Create procs for generic codes
  private static final String CREATE_CROP_UNIT_CODE_PROC = "CREATE_CROP_UNIT_CODE";
  private static final String CREATE_FARM_TYPE_CODE_PROC = "CREATE_FARM_TYPE_CODE";
  private static final String CREATE_FEDERAL_ACCOUNTING_CODE_PROC = "CREATE_FEDERAL_ACCOUNTING_CODE";
  private static final String CREATE_FEDERAL_STATUS_CODE_PROC = "CREATE_FEDERAL_STATUS_CODE";
  private static final String CREATE_PARTICIPANT_CLASS_CODE_PROC = "CREATE_PARTICIPANT_CLASS_CODE";
  private static final String CREATE_PARTICIPANT_LANG_CODE_PROC = "CREATE_PARTICIPANT_LANG_CODE";
  private static final String CREATE_PARTICIPANT_PROF_CODE_PROC = "CREATE_PARTICIPANT_PROF_CODE";
  
  
  // Crop Unit Conversion procs
  private static final String CREATE_CROP_UNIT_DEFAULT_PROC = "CREATE_CROP_UNIT_DEFAULT";
  private static final int CREATE_CROP_UNIT_DEFAULT_PARAM = 3;
  private static final String UPDATE_CROP_UNIT_DEFAULT_PROC = "UPDATE_CROP_UNIT_DEFAULT";
  private static final int UPDATE_CROP_UNIT_DEFAULT_PARAM = 4;
  private static final String DELETE_CROP_UNIT_DEFAULT_PROC = "DELETE_CROP_UNIT_DEFAULT";
  private static final int DELETE_CROP_UNIT_DEFAULT_PARAM = 1;
  
  private static final String CREATE_CROP_UNIT_CONVRSN_FACTR_PROC = "CREATE_CROP_UNIT_CONVRSN_FACTR";
  private static final int CREATE_CROP_UNIT_CONVRSN_FACTR_PARAM = 4;
  private static final String DELETE_CROP_UNIT_CONVRSN_FACTR_PROC = "DELETE_CROP_UNIT_CONVRSN_FACTR";
  private static final int DELETE_CROP_UNIT_CONVRSN_FACTR_PARAM = 2;
  private static final String RECALCULATE_FMVS_PROC = "RECALCULATE_FMVS";
  private static final int RECALCULATE_FMVS_PARAM = 3;
  
  // income range procs
  private static final String CREATE_INCOME_RANGE_PROC = "CREATE_INCOME_RANGE";
  private static final String DELETE_FARM_TYPE_DEFAULT_INCOME_RANGE_PROC = "DELETE_FARM_TYPE_DEFAULT_INCOME_RANGE";
  private static final int DELETE_FARM_TYPE_DEFAULT_INCOME_RANGE_PARAM = 0;
  
  // farm type 3 procs
  private static final String CREATE_FARM_TYPE_3_PROC = "CREATE_FARM_TYPE_3";
  private static final int CREATE_FARM_TYPE_3_PARAM = 3;
  private static final String UPDATE_FARM_TYPE_3_PROC = "UPDATE_FARM_TYPE_3";
  private static final int UPDATE_FARM_TYPE_3_PARAM = 3;  
  private static final String DELETE_FARM_TYPE_3_PROC = "DELETE_FARM_TYPE_3";
  private static final int DELETE_FARM_TYPE_3_PARAM = 1;
  
  // farm type 2 procs
  private static final String CREATE_FARM_TYPE_2_PROC = "CREATE_FARM_TYPE_2";
  private static final int CREATE_FARM_SUBTYPE_A_ITEM_CODE_PARAM = 4;
  private static final String UPDATE_FARM_TYPE_2_PROC = "UPDATE_FARM_TYPE_2";
  private static final int UPDATE_FARM_TYPE_2_PARAM = 4;
  private static final String DELETE_FARM_TYPE_2_PROC = "DELETE_FARM_TYPE_2";
  private static final int DELETE_FARM_TYPE_2_PARAM = 1;
  
  // farm type 1 procs
  private static final String CREATE_FARM_TYPE_1_PROC = "CREATE_FARM_TYPE_1";
  private static final int CREATE_FARM_TYPE_1_PARAM = 4;
  private static final String UPDATE_FARM_TYPE_1_PROC = "UPDATE_FARM_TYPE_1";
  private static final int UPDATE_FARM_TYPE_1_PARAM = 4;
  private static final String DELETE_FARM_TYPE_1_PROC = "DELETE_FARM_TYPE_1";
  private static final int DELETE_FARM_TYPE_1_PARAM = 1;
  
  // Sector, Sector Detail procs
  private static final String UPDATE_SECTOR_CODE_PROC = "UPDATE_SECTOR_CODE";
  private static final String CREATE_SECTOR_DETAIL_CODE_PROC = "CREATE_SECTOR_DETAIL_CODE";
  private static final String UPDATE_SECTOR_DETAIL_CODE_PROC = "UPDATE_SECTOR_DETAIL_CODE";
  private static final String DELETE_SECTOR_DETAIL_CODE_PROC = "DELETE_SECTOR_DETAIL_CODE";
  

  private static final Map<String, String> CREATE_PROC_MAP = new HashMap<>();
  static {
    CREATE_PROC_MAP.put(CodeTables.CROP_UNIT, CREATE_CROP_UNIT_CODE_PROC);
    CREATE_PROC_MAP.put(CodeTables.FARM_TYPE, CREATE_FARM_TYPE_CODE_PROC);
    CREATE_PROC_MAP.put(CodeTables.FEDERAL_ACCOUNTING, CREATE_FEDERAL_ACCOUNTING_CODE_PROC);
    CREATE_PROC_MAP.put(CodeTables.FEDERAL_STATUS, CREATE_FEDERAL_STATUS_CODE_PROC);
    CREATE_PROC_MAP.put(CodeTables.PARTICIPANT_CLASS, CREATE_PARTICIPANT_CLASS_CODE_PROC);
    CREATE_PROC_MAP.put(CodeTables.PARTICIPANT_LANGUAGE, CREATE_PARTICIPANT_LANG_CODE_PROC);
    CREATE_PROC_MAP.put(CodeTables.PARTICIPANT_PROFILE, CREATE_PARTICIPANT_PROF_CODE_PROC);
  }

  // Update procs for generic codes
  private static final String UPDATE_CROP_UNIT_CODE_PROC = "UPDATE_CROP_UNIT_CODE";
  private static final String UPDATE_FARM_TYPE_CODE_PROC = "UPDATE_FARM_TYPE_CODE";
  private static final String UPDATE_FEDERAL_ACCOUNTING_CODE_PROC = "UPDATE_FEDERAL_ACCOUNTING_CODE";
  private static final String UPDATE_FEDERAL_STATUS_CODE_PROC = "UPDATE_FEDERAL_STATUS_CODE";
  private static final String UPDATE_PARTICIPANT_CLASS_CODE_PROC = "UPDATE_PARTICIPANT_CLASS_CODE";
  private static final String UPDATE_PARTICIPANT_LANG_CODE_PROC = "UPDATE_PARTICIPANT_LANG_CODE";
  private static final String UPDATE_PARTICIPANT_PROF_CODE_PROC = "UPDATE_PARTICIPANT_PROF_CODE";
  private static final String UPDATE_TRIAGE_QUEUE_CODE_PROC = "UPDATE_TRIAGE_QUEUE_CODE";

  private static final Map<String, String> UPDATE_PROC_MAP = new HashMap<>();
  static {
    UPDATE_PROC_MAP.put(CodeTables.CROP_UNIT, UPDATE_CROP_UNIT_CODE_PROC);
    UPDATE_PROC_MAP.put(CodeTables.FARM_TYPE, UPDATE_FARM_TYPE_CODE_PROC);
    UPDATE_PROC_MAP.put(CodeTables.FEDERAL_ACCOUNTING, UPDATE_FEDERAL_ACCOUNTING_CODE_PROC);
    UPDATE_PROC_MAP.put(CodeTables.FEDERAL_STATUS, UPDATE_FEDERAL_STATUS_CODE_PROC);
    UPDATE_PROC_MAP.put(CodeTables.PARTICIPANT_CLASS, UPDATE_PARTICIPANT_CLASS_CODE_PROC);
    UPDATE_PROC_MAP.put(CodeTables.PARTICIPANT_LANGUAGE, UPDATE_PARTICIPANT_LANG_CODE_PROC);
    UPDATE_PROC_MAP.put(CodeTables.PARTICIPANT_PROFILE, UPDATE_PARTICIPANT_PROF_CODE_PROC);
    UPDATE_PROC_MAP.put(CodeTables.TRIAGE_QUEUE, UPDATE_TRIAGE_QUEUE_CODE_PROC);
  }
  
  // In use procs for generic codes
  private static final String IN_USE_CROP_UNIT_CODE_PROC = "IN_USE_CROP_UNIT_CODE";
  private static final String IN_USE_FARM_TYPE_CODE_PROC = "IN_USE_FARM_TYPE_CODE";
  private static final String IN_USE_FEDERAL_ACCOUNTING_CODE_PROC = "IN_USE_FEDERAL_ACCOUNTING_CODE";
  private static final String IN_USE_FEDERAL_STATUS_CODE_PROC = "IN_USE_FEDERAL_STATUS_CODE";
  private static final String IN_USE_PARTICIPANT_CLASS_CODE_PROC = "IN_USE_PARTICIPANT_CLASS_CODE";
  private static final String IN_USE_PARTICIPANT_LANG_CODE_PROC = "IN_USE_PARTICIPANT_LANG_CODE";
  private static final String IN_USE_PARTICIPANT_PROF_CODE_PROC = "IN_USE_PARTICIPANT_PROF_CODE";
  
  private static final Map<String, String> IN_USE_PROC_MAP = new HashMap<>();
  static {
    IN_USE_PROC_MAP.put(CodeTables.CROP_UNIT, IN_USE_CROP_UNIT_CODE_PROC);
    IN_USE_PROC_MAP.put(CodeTables.FARM_TYPE, IN_USE_FARM_TYPE_CODE_PROC);
    IN_USE_PROC_MAP.put(CodeTables.FEDERAL_ACCOUNTING, IN_USE_FEDERAL_ACCOUNTING_CODE_PROC);
    IN_USE_PROC_MAP.put(CodeTables.FEDERAL_STATUS, IN_USE_FEDERAL_STATUS_CODE_PROC);
    IN_USE_PROC_MAP.put(CodeTables.PARTICIPANT_CLASS, IN_USE_PARTICIPANT_CLASS_CODE_PROC);
    IN_USE_PROC_MAP.put(CodeTables.PARTICIPANT_LANGUAGE, IN_USE_PARTICIPANT_LANG_CODE_PROC);
    IN_USE_PROC_MAP.put(CodeTables.PARTICIPANT_PROFILE, IN_USE_PARTICIPANT_PROF_CODE_PROC);
  }

  // Delete procs for generic codes
  private static final String DELETE_CROP_UNIT_CODE_PROC = "DELETE_CROP_UNIT_CODE";
  private static final String DELETE_FARM_TYPE_CODE_PROC = "DELETE_FARM_TYPE_CODE";
  private static final String DELETE_FEDERAL_ACCOUNTING_CODE_PROC = "DELETE_FEDERAL_ACCOUNTING_CODE";
  private static final String DELETE_FEDERAL_STATUS_CODE_PROC = "DELETE_FEDERAL_STATUS_CODE";
  private static final String DELETE_PARTICIPANT_CLASS_CODE_PROC = "DELETE_PARTICIPANT_CLASS_CODE";
  private static final String DELETE_PARTICIPANT_LANG_CODE_PROC = "DELETE_PARTICIPANT_LANG_CODE";
  private static final String DELETE_PARTICIPANT_PROF_CODE_PROC = "DELETE_PARTICIPANT_PROF_CODE";

  private static final Map<String, String> DELETE_PROC_MAP = new HashMap<>();
  static {
    DELETE_PROC_MAP.put(CodeTables.CROP_UNIT, DELETE_CROP_UNIT_CODE_PROC);
    DELETE_PROC_MAP.put(CodeTables.FARM_TYPE, DELETE_FARM_TYPE_CODE_PROC);
    DELETE_PROC_MAP.put(CodeTables.FEDERAL_ACCOUNTING, DELETE_FEDERAL_ACCOUNTING_CODE_PROC);
    DELETE_PROC_MAP.put(CodeTables.FEDERAL_STATUS, DELETE_FEDERAL_STATUS_CODE_PROC);
    DELETE_PROC_MAP.put(CodeTables.PARTICIPANT_CLASS, DELETE_PARTICIPANT_CLASS_CODE_PROC);
    DELETE_PROC_MAP.put(CodeTables.PARTICIPANT_LANGUAGE, DELETE_PARTICIPANT_LANG_CODE_PROC);
    DELETE_PROC_MAP.put(CodeTables.PARTICIPANT_PROFILE, DELETE_PARTICIPANT_PROF_CODE_PROC);
  }
  
  // fruit veg code procs
  private static final String UPDATE_FRUIT_VEG_CODE_PROC = "UPDATE_FRUIT_VEG_CODE";
  private static final int UPDATE_FRUIT_VEG_CODE_PARAM = 4;
  private static final String CREATE_FRUIT_VEG_CODE_PROC = "CREATE_FRUIT_VEG_CODE";
  private static final int CREATE_FRUIT_VEG_CODE_PARAM = 4;
  private static final String DELETE_FRUIT_VEG_CODE_PROC = "DELETE_FRUIT_VEG_CODE";
  private static final int DELETE_FRUIT_VEG_CODE_PARAM = 1;
  
  // expected prod item procs
  private static final String CREATE_EXPECTED_PRODUCTION_PROC = "CREATE_EXPECTED_PRODUCTION";
  private static final int CREATE_EXPECTED_PRODUCTION_PARAM = 4;
  private static final String UPDATE_EXPECTED_PRODUCTION_VALUE_PROC = "UPDATE_EXPECTED_PRODUCTION_VALUE";
  private static final int UPDATE_EXPECTED_PRODUCTION_VALUE_PARAM = 3;
  private static final String DELETE_EXPECTED_PRODUCTION_PROC = "DELETE_EXPECTED_PRODUCTION";
  private static final int DELETE_EXPECTED_PRODUCTION_PARAM = 1;
  
  // config param procs
  private static final String UPDATE_CONFIGURATION_PARAMETER_PROC = "UPDATE_CONFIGURATION_PARAMETER";
  private static final int UPDATE_CONFIGURATION_PARAMETER_PARAM = 3;
  
  // year config param procs
  private static final String UPDATE_YEAR_CONFIGURATION_PARAM_PROC = "UPDATE_YEAR_CONFIGURATION_PARAM";
  private static final int UPDATE_YEAR_CONFIGURATION_PARAM_PARAM = 3;
  
  // Tip Line Items
  private static final String UPDATE_TIP_LINE_ITEM_PROC = "UPDATE_TIP_LINE_ITEM";
  private static final int UPDATE_TIP_LINE_ITEM_PARAM = 11;
  private static final String DELETE_TIP_LINE_ITEM_PROC = "DELETE_TIP_LINE_ITEM";
  private static final int DELETE_TIP_LINE_ITEM_PARAM = 1;
  private static final String CREATE_TIP_LINE_ITEM_PROC = "CREATE_TIP_LINE_ITEM";
  private static final int CREATE_TIP_LINE_ITEM_PARAM = 10;
  
  // Document Template procs
  private static final String UPDATE_DOCUMENT_TEMPLATE_PROC = "UPDATE_DOCUMENT_TEMPLATE";
  private static final int UPDATE_DOCUMENT_TEMPLATE_PARAM = 2;
  
  private static final String IN_USE_SECTOR_DETAIL_CODE_PROC = "IN_USE_SECTOR_DETAIL_CODE";
  
  
  public void createGenericCode(final Transaction transaction,
      final String codeTable,
      final Code code,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    String procName = getCodeTableCreateProcName(codeTable);

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + procName, CREATE_GENERIC_CODE_PARAM, false);

      int param = 1;
      proc.setString(param++, code.getCode());
      proc.setString(param++, code.getDescription());
      proc.setDate(param++, code.getEstablishedDate());
      proc.setDate(param++, code.getExpiryDate());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param codeTable codeTable
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateGenericCode(final Transaction transaction,
      final String codeTable,
      final Code code,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    String procName = getCodeTableUpdateProcName(codeTable);
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + procName, UPDATE_GENERIC_CODE_PARAM, false);
      
      int param = 1;
      proc.setString(param++, code.getCode());
      proc.setString(param++, code.getDescription());
      proc.setDate(param++, code.getEstablishedDate());
      proc.setDate(param++, code.getExpiryDate());
      proc.setInt(param++, code.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param codeTable codeTable
   * @param code code
   * @return boolean
   * @throws DataAccessException On Exception
   */
  public boolean isGenericCodeInUse(final Transaction transaction,
      final String codeTable,
      final String code)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    String procName = getCodeTableInUseProcName(codeTable);
    int inUseInt;
    boolean result = false;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + procName, IN_USE_GENERIC_CODE_PARAM, Types.INTEGER);
      
      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      inUseInt = proc.getInt(1);
      result = inUseInt == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }

    return result;
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param codeTable codeTable
   * @param code code
   * @param revisionCount revisionCount
   * @throws DataAccessException On Exception
   */
  public void deleteGenericCode(final Transaction transaction,
      final String codeTable,
      final String code,
      final Integer revisionCount)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    String procName = getCodeTableDeleteProcName(codeTable);
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + procName, DELETE_GENERIC_CODE_PARAM, false);
      
      int param = 1;
      proc.setString(param++, code);
      proc.setInt(param++, revisionCount);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  /**
   * @param transaction Transaction
   * @param year Integer
   * @param lineItem Integer
   * @throws DataAccessException On Exception
   * 
   * @return boolean
   */
  public boolean isLineItemInUse(final Transaction transaction,
      final Integer year,
      final Integer lineItem)
  throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int inUseInt;
    boolean result = false;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + IN_USE_LINE_ITEM_PROC, IN_USE_LINE_ITEM_PARAM, Types.INTEGER);

      int param = 1;
      proc.setInt(param++, year);
      proc.setInt(param++, lineItem);
      proc.execute();

      inUseInt = proc.getInt(1);
      result = inUseInt == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }

    return result;
  }


  /**
   * 
   * @param transaction transaction
   * @param lineItem lineItem
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createLineItem(final Transaction transaction,
      final LineItemCode lineItem,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_LINE_ITEM_PROC, CREATE_LINE_ITEM_PARAM, false);

      int param = 1;
      proc.setInt(param++, lineItem.getProgramYear());
      proc.setInt(param++, lineItem.getLineItem());
      proc.setString(param++, lineItem.getDescription());
      proc.setString(param++, getIndicatorYN(lineItem.getIsEligible()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsEligibleRefYears()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsYardage()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsProgramPayment()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsContractWork()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsSupplyManagedCommodity()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsExcludeFromRevenueCalculation()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsIndustryAverageExpense()));
      proc.setDate(param++, lineItem.getEstablishedDate());
      proc.setDate(param++, lineItem.getExpiryDate());
      proc.setString(param++, lineItem.getSectorDetailCode());
      proc.setString(param++, lineItem.getFruitVegCodeName());
      proc.setString(param++, lineItem.getCommodityTypeCode());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param lineItem lineItem
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateLineItem(final Transaction transaction,
      final LineItemCode lineItem,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_LINE_ITEM_PROC, UPDATE_LINE_ITEM_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, lineItem.getLineItemId());
      proc.setInt(param++, lineItem.getProgramYear());
      proc.setInt(param++, lineItem.getLineItem());
      proc.setString(param++, lineItem.getDescription());
      proc.setString(param++, getIndicatorYN(lineItem.getIsEligible()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsEligibleRefYears()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsYardage()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsProgramPayment()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsContractWork()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsSupplyManagedCommodity()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsExcludeFromRevenueCalculation()));
      proc.setString(param++, getIndicatorYN(lineItem.getIsIndustryAverageExpense()));
      proc.setDate(param++, lineItem.getEstablishedDate());
      proc.setDate(param++, lineItem.getExpiryDate());
      proc.setString(param++, lineItem.getSectorDetailCode());
      proc.setInt(param++, lineItem.getRevisionCount());
      proc.setString(param++, lineItem.getFruitVegCodeName());
      proc.setString(param++, lineItem.getCommodityTypeCode());
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param lineItemId lineItemId
   * @param year year
   * @param lineItem lineItem
   * @param revisionCount revisionCount
   * @throws DataAccessException On Exception
   */
  public void deleteLineItem(final Transaction transaction,
      final Integer lineItemId,
      final Integer year,
      final Integer lineItem,
      final Integer revisionCount)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_LINE_ITEM_PROC, DELETE_LINE_ITEM_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, lineItemId);
      proc.setInt(param++, year);
      proc.setInt(param++, lineItem);
      proc.setInt(param++, revisionCount);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * @param transaction transaction
   * @param toYear toYear
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void copyYearLineItems(final Transaction transaction,
      final Integer toYear,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + COPY_YEAR_LINE_ITEMS_PROC, COPY_YEAR_LINE_ITEMS_PARAM, false);

      int param = 1;
      proc.setInt(param++, toYear);
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  /**
   * @param transaction transaction
   * @param year year
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @return boolean
   * @throws DataAccessException On Exception
   */
  public boolean isFMVInUse(final Transaction transaction,
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode)
  throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int inUseInt;
    boolean result = false;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + IN_USE_FMV_PROC, IN_USE_FMV_PARAM, Types.INTEGER);

      int param = 1;
      proc.setInt(param++, year);
      proc.setString(param++, inventoryItemCode);
      proc.setString(param++, municipalityCode);
      proc.setString(param++, cropUnitCode);
      proc.execute();

      inUseInt = proc.getInt(1);
      result = inUseInt == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }

    return result;
  }


  /**
   * 
   * @param transaction transaction
   * @param fmv fmv
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createFMV(final Transaction transaction,
      final FMV fmv,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_FMV_PROC, CREATE_FMV_PARAM, false);

      int param;

      FMVPeriod[] periods = fmv.getPeriods();
      for(int ii = 0; ii < FMV.NUMBER_OF_PERIODS; ii++) {
        FMVPeriod period = periods[ii];
        
        if(period.getPrice() != null
            && period.getPercentVariance() != null) {
          param = 1;
          proc.setInt(param++, fmv.getProgramYear());
          proc.setInt(param++, period.getPeriod());
          proc.setDouble(param++, period.getPrice());
          proc.setDouble(param++, period.getPercentVariance());
          proc.setString(param++, fmv.getInventoryItemCode());
          proc.setString(param++, fmv.getMunicipalityCode());
          proc.setString(param++, fmv.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
      }
      
      proc.executeBatch();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param fmv fmv
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateFMV(final Transaction transaction,
      final FMV fmv,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FMV_PROC, UPDATE_FMV_PARAM, false);
      

      int param;

      FMVPeriod[] periods = fmv.getPeriods();
      for(int ii = 0; ii < FMV.NUMBER_OF_PERIODS; ii++) {
        FMVPeriod period = periods[ii];
        
        param = 1;
        proc.setInt(param++, period.getFairMarketValueId());
        proc.setInt(param++, fmv.getProgramYear());
        proc.setInt(param++, period.getPeriod());
        proc.setDouble(param++, period.getPrice());
        proc.setDouble(param++, period.getPercentVariance());
        proc.setString(param++, fmv.getInventoryItemCode());
        proc.setString(param++, fmv.getMunicipalityCode());
        proc.setString(param++, fmv.getCropUnitCode());
        proc.setInt(param++, period.getRevisionCount());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param year year
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @throws DataAccessException On Exception
   */
  public void deleteFMV(final Transaction transaction,
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_FMV_PROC, DELETE_FMV_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, year);
      proc.setString(param++, inventoryItemCode);
      proc.setString(param++, municipalityCode);
      proc.setString(param++, cropUnitCode);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createMunicipalityCode(final Transaction transaction,
      final MunicipalityCode code,
      final String user)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    
    List<String> officeCodes = code.getRegionalOfficeCodes();

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_MUNICIPALITY_CODE_PROC, CREATE_MUNICIPALITY_CODE_PARAM, false);

      Array oracleArray = createStringOracleArray(connection, officeCodes);

      int param = 1;
      proc.setString(param++, code.getCode());
      proc.setString(param++, code.getDescription());
      proc.setDate(param++, code.getEstablishedDate());
      proc.setDate(param++, code.getExpiryDate());
      proc.setArray(param++, oracleArray);
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public void updateMunicipalityCode(final Transaction transaction,
      final MunicipalityCode code,
      final String user)
  throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    
    List<String> officeCodes = code.getRegionalOfficeCodes();
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_MUNICIPALITY_CODE_PROC, UPDATE_MUNICIPALITY_CODE_PARAM, false);
      
      Array oracleArray = createStringOracleArray(connection, officeCodes);
      
      int param = 1;
      proc.setString(param++, code.getCode());
      proc.setString(param++, code.getDescription());
      proc.setDate(param++, code.getEstablishedDate());
      proc.setDate(param++, code.getExpiryDate());
      proc.setInt(param++, code.getRevisionCount());
      proc.setArray(param++, oracleArray);
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param code code
   * @return boolean
   * @throws DataAccessException On Exception
   */
  public boolean isMunicipalityCodeInUse(final Transaction transaction,
      final String code)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    int inUseInt;
    boolean result = false;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + IN_USE_MUNICIPALITY_CODE_PROC, IN_USE_MUNICIPALITY_CODE_PARAM, Types.INTEGER);
      
      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      inUseInt = proc.getInt(1);
      result = inUseInt == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }

    return result;
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param revisionCount revisionCount
   * @throws DataAccessException On Exception
   */
  public void deleteMunicipalityCode(final Transaction transaction,
      final String code,
      final Integer revisionCount)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_MUNICIPALITY_CODE_PROC, DELETE_MUNICIPALITY_CODE_PARAM, false);
      
      int param = 1;
      proc.setString(param++, code);
      proc.setInt(param++, revisionCount);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  
  /**
   * 
   * @param transaction transaction
   * @param bpu bpu
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createBPU(final Transaction transaction,
      final BPU bpu,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
    	final int numParams = 5;
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_BPU_PROC, numParams, false);

      int param = 1;
      proc.registerOutParameter(param, Types.INTEGER);
      
      proc.setInt(param++, (Integer) null);
      proc.setInt(param++, bpu.getProgramYear());
      proc.setString(param++, bpu.getInvSgCode());
      proc.setString(param++, bpu.getMunicipalityCode());
      proc.setString(param++, user);
      proc.execute();

      param = 1;
      bpu.setBpuId(new Integer(proc.getInt(param)));
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  /**
   * @param transaction transaction
   * @param bpu bpu
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createBPUYears(final Transaction transaction,
      final BPU bpu,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
    	final int numParams = 5;
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_BPU_YEAR_PROC, numParams, false);

      BPUYear[] years = bpu.getYears();
      for(int ii = 0; ii < years.length; ii++) {
      	BPUYear year = years[ii];
        
        int param = 1;
        proc.setInt(param++, bpu.getBpuId());
        proc.setInt(param++, year.getYear());
        proc.setDouble(param++, year.getAverageMargin());
        proc.setDouble(param++, year.getAverageExpense());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  
  /**
   * 
   * @param transaction transaction
   * @param year year
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateBPUYear(final Transaction transaction,
      final BPUYear year,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
    	final int numParams = 6;
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_BPU_YEAR_PROC, numParams, false);

      int param = 1;
      proc.setInt(param++, year.getBpuId());
      proc.setInt(param++, year.getYear());
      proc.setDouble(param++, year.getAverageMargin());
      proc.setDouble(param++, year.getAverageExpense());
      proc.setInt(param++, year.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();
      
      year.setRevisionCount(new Integer(year.getRevisionCount().intValue()+1));
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  
  /**
   * @param transaction transaction
   * @param bpuId bpuId
   * @return boolean
   * @throws DataAccessException On Exception
   */
  public boolean isBPUInUse(
  		final Transaction transaction,
      final Integer bpuId)
  throws DataAccessException {
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    boolean result = false;

    try {
      final int numParams = 1;
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + IN_USE_BPU_PROC, numParams, Types.INTEGER);

      int param = 1;
      proc.setInt(param++, bpuId);
      proc.execute();

      int inUseInt = proc.getInt(1);
      result = inUseInt == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }

    return result;
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param bpuId bpuId
   * @throws DataAccessException On Exception
   */
  public void deleteBPU(
  		final Transaction transaction,
      final Integer bpuId)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
    	final int numParams = 1;
      proc = new DAOStoredProcedure(
      		connection, 
      		PACKAGE_NAME + "." + DELETE_BPU_PROC, 
      		numParams, 
      		false);
      
      int param = 1;
      proc.setInt(param++, bpuId);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createInventoryItemCode(final Transaction transaction,
      final InventoryItemCode code,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_INVENTORY_ITEM_CODE_PROC, CREATE_INVENTORY_ITEM_CODE_PARAM, false);

      int param = 1;
      proc.setString(param++, code.getCode());
      proc.setString(param++, code.getDescription());
      proc.setString(param++, code.getRollupInventoryItemCode());
      proc.setDate(param++, code.getEstablishedDate());
      proc.setDate(param++, code.getExpiryDate());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateInventoryItemCode(final Transaction transaction,
      final InventoryItemCode code,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_INVENTORY_ITEM_CODE_PROC, UPDATE_INVENTORY_ITEM_CODE_PARAM, false);
      
      int param = 1;
      proc.setString(param++, code.getCode());
      proc.setString(param++, code.getDescription());
      proc.setString(param++, code.getRollupInventoryItemCode());
      proc.setDate(param++, code.getEstablishedDate());
      proc.setDate(param++, code.getExpiryDate());
      proc.setInt(param++, code.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param code code
   * @return boolean
   * @throws DataAccessException On Exception
   */
  public boolean isInventoryItemCodeInUse(final Transaction transaction,
      final String code)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    int inUseInt;
    boolean result = false;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + IN_USE_INVENTORY_ITEM_CODE_PROC, IN_USE_INVENTORY_ITEM_CODE_PARAM, Types.INTEGER);
      
      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      inUseInt = proc.getInt(1);
      result = inUseInt == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }

    return result;
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param revisionCount revisionCount
   * @throws DataAccessException On Exception
   */
  public void deleteInventoryItemCode(final Transaction transaction,
      final String code,
      final Integer revisionCount)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_INVENTORY_ITEM_CODE_PROC, DELETE_INVENTORY_ITEM_CODE_PARAM, false);
      
      int param = 1;
      proc.setString(param++, code);
      proc.setInt(param++, revisionCount);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  public void createInventoryItemDetails(final Transaction transaction,
      final List<InventoryItemDetail> detailsList,
      final String user)
      throws DataAccessException {

    final int paramCount = 9;
    Connection connection = getConnection(transaction);

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_INVENTORY_ITEM_DETAIL_PROC, paramCount, false); ) {
      
      for(InventoryItemDetail detail : detailsList) {

        int param = 1;
        proc.setString(param++, detail.getInventoryItemCode());
        proc.setInt(param++, detail.getProgramYear());
        proc.setIndicator(param++, detail.getIsEligible());
        proc.setString(param++, detail.getFruitVegCodeName());      
        proc.setInt(param++, detail.getLineItem());
        proc.setDouble(param++, detail.getInsurableValue());
        proc.setDouble(param++, detail.getPremiumRate());
        proc.setString(param++, detail.getCommodityTypeCodeName());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  public void updateInventoryItemDetails(final Transaction transaction,
      final List<InventoryItemDetail> detailsList,
      final String user)
  throws DataAccessException {
    
    final int paramCount = 9;
    Connection connection = getConnection(transaction);
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_INVENTORY_ITEM_DETAIL_PROC, paramCount, false);) {
      
      for(InventoryItemDetail detail : detailsList) {

        int param = 1;
        proc.setInt(param++, detail.getInventoryItemDetailId());
        proc.setIndicator(param++, detail.getIsEligible());
        proc.setInt(param++, detail.getRevisionCount());
        proc.setString(param++, detail.getFruitVegCodeName());
        proc.setInt(param++, detail.getLineItem());
        proc.setDouble(param++, detail.getInsurableValue());
        proc.setDouble(param++, detail.getPremiumRate());
        proc.setString(param++, detail.getCommodityTypeCodeName());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  /**
   * @param transaction Transaction
   * @param commodityXrefId commodityXrefId
   * @throws DataAccessException On Exception
   * 
   * @return boolean
   */
  public boolean isInventoryXrefInUse(final Transaction transaction,
      final Integer commodityXrefId)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    int inUseInt;
    boolean result = false;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + IN_USE_INVENTORY_XREF_PROC, IN_USE_INVENTORY_XREF_PARAM, Types.INTEGER);

      int param = 1;
      proc.setInt(param++, commodityXrefId);
      proc.execute();

      inUseInt = proc.getInt(1);
      result = inUseInt == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }

    return result;
  }


  /**
   * 
   * @param transaction transaction
   * @param xref xref
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createInventoryXref(final Transaction transaction,
      final InventoryXref xref,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_INVENTORY_XREF_PROC, CREATE_INVENTORY_XREF_PARAM, Types.INTEGER);

      int param = 1;
      proc.setString(param++, xref.getInventoryClassCode());
      proc.setString(param++, xref.getInventoryItemCode());
      proc.setString(param++, xref.getInventoryGroupCode());
      proc.setString(param++, getIndicatorYN(xref.getIsMarketCommodity()));
      proc.setString(param++, user);
      proc.execute();
      
      xref.setCommodityXrefId(new Integer(proc.getInt(1)));

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param xref xref
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateInventoryXref(final Transaction transaction,
      final InventoryXref xref,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_INVENTORY_XREF_PROC, UPDATE_INVENTORY_XREF_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, xref.getCommodityXrefId());
      proc.setString(param++, xref.getInventoryGroupCode());
      proc.setString(param++, getIndicatorYN(xref.getIsMarketCommodity()));
      proc.setInt(param++, xref.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param commodityXrefId commodityXrefId
   * @param revisionCount revisionCount
   * @throws DataAccessException On Exception
   */
  public void deleteInventoryXref(final Transaction transaction,
      final Integer commodityXrefId,
      final Integer revisionCount)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_INVENTORY_XREF_PROC, DELETE_INVENTORY_XREF_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, commodityXrefId);
      proc.setInt(param++, revisionCount);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   *
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createMarketRatePremium(final Transaction transaction,
      final MarketRatePremium mrp,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_MARKET_RATE_PREMIUM_PROC, CREATE_MARKET_RATE_PREMIUM_PARAM, false);

      int param = 1;
      proc.setBigDecimal(param++, mrp.getMinTotalPremiumAmount());
      proc.setBigDecimal(param++, mrp.getMaxTotalPremiumAmount());
      proc.setBigDecimal(param++, mrp.getRiskChargeFlatAmount());
      proc.setBigDecimal(param++, mrp.getRiskChargePercentagePremium());
      proc.setBigDecimal(param++, mrp.getAdjustChargeFlatAmount());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   *
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateMarketRatePremium(final Transaction transaction,
      final MarketRatePremium mrp,
      final String user)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_MARKET_RATE_PREMIUM_PROC, UPDATE_MARKET_RATE_PREMIUM_PARAM, false);

      int param = 1;
      proc.setLong(param++, mrp.getMarketRatePremiumId());
      proc.setBigDecimal(param++, mrp.getMinTotalPremiumAmount());
      proc.setBigDecimal(param++, mrp.getMaxTotalPremiumAmount());
      proc.setBigDecimal(param++, mrp.getRiskChargeFlatAmount());
      proc.setBigDecimal(param++, mrp.getRiskChargePercentagePremium());
      proc.setBigDecimal(param++, mrp.getAdjustChargeFlatAmount());
      proc.setInt(param++, mrp.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   *
   * @param transaction transaction
   * @param code code
   * @param revisionCount revisionCount
   * @throws DataAccessException On Exception
   */
  public void deleteMarketRatePremium(final Transaction transaction,
      final Long marketRatePremiumId,
      final Integer revisionCount)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_MARKET_RATE_PREMIUM_PROC, DELETE_MARKET_RATE_PREMIUM_PARAM, false);

      int param = 1;
      proc.setLong(param++, marketRatePremiumId);
      proc.setInt(param++, revisionCount);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createStructureGroupCode(final Transaction transaction,
      final StructureGroupCode code,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_STRUCTURE_GROUP_CODE_PROC, CREATE_STRUCTURE_GROUP_CODE_PARAM, false);

      int param = 1;
      proc.setString(param++, code.getCode());
      proc.setString(param++, code.getDescription());
      proc.setString(param++, code.getRollupStructureGroupCode());
      proc.setDate(param++, code.getEstablishedDate());
      proc.setDate(param++, code.getExpiryDate());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateStructureGroupCode(final Transaction transaction,
      final StructureGroupCode code,
      final String user)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_STRUCTURE_GROUP_CODE_PROC, UPDATE_STRUCTURE_GROUP_CODE_PARAM, false);

      int param = 1;
      proc.setString(param++, code.getCode());
      proc.setString(param++, code.getDescription());
      proc.setString(param++, code.getRollupStructureGroupCode());
      proc.setDate(param++, code.getEstablishedDate());
      proc.setDate(param++, code.getExpiryDate());
      proc.setInt(param++, code.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * @param transaction transaction
   * @param code code
   * @param boolean
   * @throws DataAccessException On Exception
   */
  public boolean isStructureGroupCodeInUse(final Transaction transaction,
      final String code)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    int inUseInt;
    boolean result = false;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + IN_USE_STRUCTURE_GROUP_CODE_PROC, IN_USE_STRUCTURE_GROUP_CODE_PARAM, Types.INTEGER);

      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      inUseInt = proc.getInt(1);
      result = inUseInt == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }

    return result;
  }


  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param revisionCount revisionCount
   * @throws DataAccessException On Exception
   */
  public void deleteStructureGroupCode(final Transaction transaction,
      final String code,
      final Integer revisionCount)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_STRUCTURE_GROUP_CODE_PROC, DELETE_STRUCTURE_GROUP_CODE_PARAM, false);

      int param = 1;
      proc.setString(param++, code);
      proc.setInt(param++, revisionCount);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  public void createCropUnitDefault(final Transaction transaction,
      final CropUnitConversion cropUnitConversion,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_CROP_UNIT_DEFAULT_PROC, CREATE_CROP_UNIT_DEFAULT_PARAM, false);

      int param = 1;
      proc.setString(param++, cropUnitConversion.getInventoryItemCode());
      proc.setString(param++, cropUnitConversion.getDefaultCropUnitCode());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  public void updateCropUnitDefault(final Transaction transaction,
      final CropUnitConversion cropUnitConversion,
      final String user)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_CROP_UNIT_DEFAULT_PROC, UPDATE_CROP_UNIT_DEFAULT_PARAM, false);

      int param = 1;
      proc.setString(param++, cropUnitConversion.getInventoryItemCode());
      proc.setString(param++, cropUnitConversion.getDefaultCropUnitCode());
      proc.setInt(param++, cropUnitConversion.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public void deleteCropUnitDefault(final Transaction transaction,
      final String inventoryItemCode)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_CROP_UNIT_DEFAULT_PROC, DELETE_CROP_UNIT_DEFAULT_PARAM, false);

      int param = 1;
      proc.setString(param++, inventoryItemCode);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public void createCropUnitConversionItems(final Transaction transaction,
      final CropUnitConversion cropUnitConversion,
      final String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_CROP_UNIT_CONVRSN_FACTR_PROC, CREATE_CROP_UNIT_CONVRSN_FACTR_PARAM, false);
      
      int param;
      
      for(CropUnitConversionItem item : cropUnitConversion.getConversionItems()) {
        
        param = 1;
        proc.setString(param++, cropUnitConversion.getInventoryItemCode());
        proc.setString(param++, item.getTargetCropUnitCode());
        proc.setDouble(param++, item.getConversionFactor().doubleValue());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public void deleteCropUnitConversionItems(final Transaction transaction,
      final String inventoryItemCode,
      final String targetCropUnitCode)
          throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_CROP_UNIT_CONVRSN_FACTR_PROC, DELETE_CROP_UNIT_CONVRSN_FACTR_PARAM, false);

      int param = 1;
      proc.setString(param++, inventoryItemCode);
      proc.setString(param++, targetCropUnitCode);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public void recalculateFmvs(final Transaction transaction,
      final Integer programYear,
      final String inventoryItemCode,
      final String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + RECALCULATE_FMVS_PROC, RECALCULATE_FMVS_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, programYear);
      proc.setString(param++, inventoryItemCode);
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * @param codeTable codeTable
   * @return String
   */
  private static String getCodeTableCreateProcName(String codeTable) {
    return CREATE_PROC_MAP.get(codeTable);
  }

  /**
   * @param codeTable codeTable
   * @return String
   */
  private static String getCodeTableUpdateProcName(String codeTable) {
    return UPDATE_PROC_MAP.get(codeTable);
  }

  /**
   * @param codeTable codeTable
   * @return String
   */
  private static String getCodeTableInUseProcName(String codeTable) {
    return IN_USE_PROC_MAP.get(codeTable);
  }
  
  /**
   * @param codeTable codeTable
   * @return String
   */
  private static String getCodeTableDeleteProcName(String codeTable) {
    return DELETE_PROC_MAP.get(codeTable);
  }
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateFruitVegCode(final Transaction transaction,
      final FruitVegTypeCode fruitVegCode,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FRUIT_VEG_CODE_PROC, UPDATE_FRUIT_VEG_CODE_PARAM, false);
    
      int param = 1;
      proc.setString(param++, fruitVegCode.getName());
      proc.setString(param++, fruitVegCode.getDescription());
      proc.setDouble(param++, fruitVegCode.getVarianceLimit().toString());
      proc.setString(param++, user);
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createFruitVegCode(final Transaction transaction,
      final FruitVegTypeCode fruitVegCode,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_FRUIT_VEG_CODE_PROC, CREATE_FRUIT_VEG_CODE_PARAM, false);
    
      int param = 1;
      proc.setString(param++, fruitVegCode.getName());
      proc.setString(param++, fruitVegCode.getDescription());
      proc.setDouble(param++, fruitVegCode.getVarianceLimit().toString());
      proc.setString(param++, user);
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }

  public void deleteFruitVegCode(final Transaction transaction,
      final FruitVegTypeCode fruitVegCode)
    throws DataAccessException {
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_FRUIT_VEG_CODE_PROC, DELETE_FRUIT_VEG_CODE_PARAM, false);
    
      proc.setString(1, fruitVegCode.getName());
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }    
  }
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateExpectedProductionItem(final Transaction transaction,
      final ExpectedProduction expectedProduction,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_EXPECTED_PRODUCTION_VALUE_PROC, UPDATE_EXPECTED_PRODUCTION_VALUE_PARAM, false);
    
      int param = 1;
      proc.setInt(param++, expectedProduction.getId());
      proc.setDouble(param++, expectedProduction.getExpectedProductionPerAcre());
      proc.setString(param++, user);
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void createExectedProductionItem(final Transaction transaction,
      final ExpectedProduction expectedProduction,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_EXPECTED_PRODUCTION_PROC, CREATE_EXPECTED_PRODUCTION_PARAM, false);
      
    
      int param = 1;
      proc.setString(param++, expectedProduction.getCropUnitCode());
      proc.setString(param++, expectedProduction.getInventoryItemCode());
      proc.setDouble(param++, expectedProduction.getExpectedProductionPerAcre());
      proc.setString(param++, user);
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  public void deleteExpectedProductionItem(final Transaction transaction,
      final ExpectedProduction expectedProduction)
    throws DataAccessException {
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_EXPECTED_PRODUCTION_PROC, DELETE_EXPECTED_PRODUCTION_PARAM, false);
    
      proc.setInt(param++, expectedProduction.getId());
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }    
  }
  
  
  public void updateConfigurationParameter(final Transaction transaction,
      final ConfigurationParameter configParam,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_CONFIGURATION_PARAMETER_PROC, UPDATE_CONFIGURATION_PARAMETER_PARAM, false);
    
      int param = 1;
      proc.setString(param++, configParam.getName());
      proc.setString(param++, configParam.getValue());
      proc.setString(param++, user);
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  
  public void updateYearConfigurationParam(final Transaction transaction,
      final YearConfigurationParameter configParam,
      final String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_YEAR_CONFIGURATION_PARAM_PROC, UPDATE_YEAR_CONFIGURATION_PARAM_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, configParam.getId());
      proc.setString(param++, configParam.getValue());
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public void createIncomeRange(final Transaction transaction,
      final List<TipFarmTypeIncomeRange> ranges,
      final Integer farmType3Id,
      final Integer farmType2Id,
      final Integer farmType1Id,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    final int paramCount = 7;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_INCOME_RANGE_PROC, paramCount, false);
    
      int param = 1;
      for (TipFarmTypeIncomeRange range : ranges) {
        param = 1;
        proc.setInt(param++, farmType3Id);
        proc.setInt(param++, farmType2Id);
        proc.setInt(param++, farmType1Id);
        proc.setDouble(param++, range.getRangeHigh());
        proc.setDouble(param++, range.getRangeLow());
        proc.setDouble(param++, range.getMinimumGroupCount());
        proc.setString(param++, user);
        proc.addBatch();
      }

      proc.executeBatch();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public final Integer createFarmType3(final Transaction transaction,
      final FarmType3 farmType,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer id = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_FARM_TYPE_3_PROC, CREATE_FARM_TYPE_3_PARAM, false);
    
      int param = 1;
      proc.registerOutParameter(param, Types.INTEGER);
      proc.setInt(param++, 0);
      proc.setString(param++, farmType.getFarmTypeName().trim());
      proc.setString(param++, user);
      proc.execute();
      id = proc.getInt(1);
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return id;
  }

  
  /**
   * 
   * @param transaction transaction
   * @param IncomeRanges ranges
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void deleteDefaultFarmTypeIncomeRange(final Transaction transaction) throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_FARM_TYPE_DEFAULT_INCOME_RANGE_PROC, DELETE_FARM_TYPE_DEFAULT_INCOME_RANGE_PARAM, false);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  

  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateFarmType3(final Transaction transaction,
      final FarmType3 farmType,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_TYPE_3_PROC, UPDATE_FARM_TYPE_3_PARAM, false);
    
      int param = 1;
      proc.setInt(param++, farmType.getFarmTypeId());
      proc.setString(param++, farmType.getFarmTypeName().trim());
      proc.setString(param++, user);
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  public void deleteFarmType3(final Transaction transaction,
      final FarmType3 farmType)
    throws DataAccessException {
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_FARM_TYPE_3_PROC, DELETE_FARM_TYPE_3_PARAM, false);
    
      proc.setString(1, farmType.getFarmTypeId());
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }    
  }
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public Integer createFarmType2(final Transaction transaction,
      final FarmSubtype farmSubtype,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer id = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_FARM_TYPE_2_PROC, CREATE_FARM_SUBTYPE_A_ITEM_CODE_PARAM, false); 
      
      int param = 1;
      proc.registerOutParameter(param, Types.INTEGER);
      proc.setInt(param++, 0);
      proc.setInt(param++, farmSubtype.getParentId());
      proc.setString(param++, farmSubtype.getName().trim());
      proc.setString(param++, user);
      proc.execute();
      
      id = proc.getInt(1);
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    return id;
  }
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateFarmType2(final Transaction transaction,
      final FarmSubtype farmSubtype,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_TYPE_2_PROC, UPDATE_FARM_TYPE_2_PARAM, false);
    
      int param = 1;
      proc.setString(param++, farmSubtype.getName().trim());
      proc.setInt(param++, farmSubtype.getId());
      proc.setString(param++, user);
      proc.setInt(param++, farmSubtype.getParentId());
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  public void deleteFarmType2(final Transaction transaction,
      final FarmSubtype farmSubtype)
    throws DataAccessException {
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_FARM_TYPE_2_PROC, DELETE_FARM_TYPE_2_PARAM, false);
    
      proc.setInt(1, farmSubtype.getId());
      
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }    
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public Integer createFarmType1(final Transaction transaction,
      final FarmSubtype farmSubtype,
      final String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer id = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_FARM_TYPE_1_PROC, CREATE_FARM_TYPE_1_PARAM, false);
    
      int param = 1;
      proc.registerOutParameter(param, Types.INTEGER);
      proc.setInt(param++, 0);
      proc.setString(param++, farmSubtype.getName().trim());
      proc.setInt(param++, farmSubtype.getParentId());
      proc.setString(param++, user);
      proc.execute();
      
      id = proc.getInt(1);
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    return id;
  }
  
  /**
   * 
   * @param transaction transaction
   * @param code code
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateFarmType1(final Transaction transaction,
      final FarmSubtype farmSubtype,
      final String user,
      final Integer farmType2Id)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_TYPE_1_PROC, UPDATE_FARM_TYPE_1_PARAM, false);
    
      int param = 1;
      proc.setString(param++, farmSubtype.getName().trim());
      proc.setInt(param++, farmType2Id);
      proc.setString(param++, user);
      proc.setInt(param++, farmSubtype.getId());
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  public void deleteFarmType1(final Transaction transaction,
      final FarmSubtype farmSubtype)
    throws DataAccessException {
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_FARM_TYPE_1_PROC, DELETE_FARM_TYPE_1_PARAM, false);
    
      proc.setInt(1, farmSubtype.getId());
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }    
  }
  

  public void updateTipLineItem(final Transaction transaction,
      final FarmSubtype farmSubtype,
      final String user,
      final TipLineItem lineItem)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_TIP_LINE_ITEM_PROC, UPDATE_TIP_LINE_ITEM_PARAM, false);
    
      int param = 1;
      
      proc.setInt(param++, lineItem.getId());
      proc.setInt(param++, lineItem.getLineItem());
      proc.setIndicator(param++, lineItem.getIsUsedInOpCost());
      proc.setIndicator(param++, lineItem.getIsUsedInDirectExpense());
      proc.setIndicator(param++, lineItem.getIsUsedInMachineryExpense());
      proc.setIndicator(param++, lineItem.getIsUsedInLandAndBuildingExpense());
      proc.setIndicator(param++, lineItem.getIsUsedInOverheadExpense());
      proc.setIndicator(param++, lineItem.getIsProgramPaymentForTips());
      proc.setIndicator(param++, lineItem.getIsOther());
      proc.setInt(param++, farmSubtype == null ? null : farmSubtype.getId());
      proc.setString(param++, user);
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  /**
   * 
   * @param transaction transaction
   * @param FarmSubType farmSubtype
   * @param String user
   * @param TipLineItem lineItem
   * DataAccessException On Exception
   */
  public void createTipLineItem(final Transaction transaction,
      final FarmSubtype farmSubtype,
      final String user,
      final TipLineItem lineItem)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_TIP_LINE_ITEM_PROC, CREATE_TIP_LINE_ITEM_PARAM, false);
    
      int param = 1;
      
      proc.setInt(param++, lineItem.getLineItem());
      proc.setIndicator(param++, lineItem.getIsUsedInOpCost());
      proc.setIndicator(param++, lineItem.getIsUsedInDirectExpense());
      proc.setIndicator(param++, lineItem.getIsUsedInMachineryExpense());
      proc.setIndicator(param++, lineItem.getIsUsedInLandAndBuildingExpense());
      proc.setIndicator(param++, lineItem.getIsUsedInOverheadExpense());
      proc.setIndicator(param++, lineItem.getIsProgramPaymentForTips());
      proc.setIndicator(param++, lineItem.getIsOther());
      proc.setInt(param++, farmSubtype == null ? null : farmSubtype.getId());
      proc.setString(param++, user);
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  /**
   * 
   * @param transaction transaction
   * @param  Integer id
   * @throws DataAccessException On Exception
   */
  public void deleteTipLineItem(final Transaction transaction, final Integer id)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_TIP_LINE_ITEM_PROC, DELETE_TIP_LINE_ITEM_PARAM, false);
    
      int param = 1;
      
      proc.setInt(param++, id);
      proc.execute();
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public void updateDocumentTemplate(final Transaction transaction,
      final DocumentTemplate documentTemplate,
      final String user)
      throws DataAccessException, IOException {

    Connection connection = getConnection(transaction);
    Clob clob = null;

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_DOCUMENT_TEMPLATE_PROC, UPDATE_DOCUMENT_TEMPLATE_PARAM, true);) {
    
      int param = 1;
      proc.setString(param++, documentTemplate.getTemplateName());
      proc.setString(param++, user);
      proc.execute();
      
      try(ResultSet resultSet = proc.getResultSet();) {

        if (resultSet.next()) {
          clob = resultSet.getClob(1);
          try(Writer writer = clob.setCharacterStream(0);) {
            writer.write(documentTemplate.getTemplateContent());
            writer.flush();
          }
        }
      }
    
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }


  public void updateSectorCode(Transaction transaction,
      SectorCode sectorCode,
      String user)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    final int paramCount = 5;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_SECTOR_CODE_PROC, paramCount, false); ) {

      int param = 1;
      proc.setString(param++, sectorCode.getCode());
      proc.setString(param++, sectorCode.getDescription());
      proc.setDate(param++, sectorCode.getExpiryDate());
      proc.setInt(param++, sectorCode.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }


  public void createSectorDetailCode(Transaction transaction,
      SectorDetailCode sectorDetailCode,
      String user)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    final int paramCount = 5;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_SECTOR_DETAIL_CODE_PROC, paramCount, false); ){

      int param = 1;
      proc.setString(param++, sectorDetailCode.getSectorCode());
      proc.setString(param++, sectorDetailCode.getSectorDetailCode());
      proc.setString(param++, sectorDetailCode.getDescription());
      proc.setDate(param++, sectorDetailCode.getExpiryDate());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }


  public void updateSectorDetailCode(Transaction transaction,
      SectorDetailCode sectorDetailCode,
      String user)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    final int paramCount = 6;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_SECTOR_DETAIL_CODE_PROC, paramCount, false); ) {

      int param = 1;
      proc.setString(param++, sectorDetailCode.getSectorCode());
      proc.setString(param++, sectorDetailCode.getSectorDetailCode());
      proc.setString(param++, sectorDetailCode.getDescription());
      proc.setDate(param++, sectorDetailCode.getExpiryDate());
      proc.setInt(param++, sectorDetailCode.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  public boolean isSectorDetailCodeInUse(Transaction transaction, String code) throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    int inUseInt;
    boolean result = false;
    final int paramCount = 1;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + IN_USE_SECTOR_DETAIL_CODE_PROC, paramCount, Types.INTEGER); ) {
      
      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      inUseInt = proc.getInt(1);
      result = inUseInt == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }

    return result;
  }
  
  
  public void deleteSectorDetailCode(Transaction transaction,
      String sectorDetailCode,
      Integer revisionCount)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    final int paramCount = 2;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_SECTOR_DETAIL_CODE_PROC, paramCount, false); ) {

      int param = 1;
      proc.setString(param++, sectorDetailCode);
      proc.setInt(param++, revisionCount);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
}
