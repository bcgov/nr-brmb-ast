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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.codes.BPU;
import ca.bc.gov.srm.farm.domain.codes.BPUYear;
import ca.bc.gov.srm.farm.domain.codes.Code;
import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCode;
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
import ca.bc.gov.srm.farm.domain.codes.TipBenchmarkInfo;
import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.domain.codes.TipLineItem;
import ca.bc.gov.srm.farm.domain.codes.YearConfigurationParameter;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * @author awilkinson
 * @created Aug 15, 2011
 */
public class CodesReadDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_CODES_READ_PKG";


  // Line Item procs
  private static final String READ_LINE_ITEMS_PROC = "READ_LINE_ITEMS";
  private static final int READ_LINE_ITEMS_PARAM = 2;
  
  // FMV procs
  private static final String READ_FMV_PROC = "READ_FMV";
  private static final int READ_FMV_PARAM = 4;
  private static final String EXPORT_FMV_PROC = "EXPORT_FMV";
  private static final int EXPORT_FMV_PARAM = 5;
  private static final String EXPORT_MISSING_FMV_PROC = "EXPORT_MISSING_FMV";
  private static final int EXPORT_MISSING_FMV_PARAM = 1;
  
  //BPU procs
  private static final String READ_BPU_PROC = "READ_BPU";
  private static final int READ_BPU_PARAM = 1;
  private static final String EXPORT_BPU_PROC = "EXPORT_BPU";
  private static final int EXPORT_BPU_PARAM = 4;
  private static final String EXPORT_MISSING_BPU_PROC = "EXPORT_MISSING_BPU";
  private static final int EXPORT_MISSING_BPU_PARAM = 1;

  // Municipality procs
  private static final String READ_MUNICIPALITY_CODES_PROC = "READ_MUNICIPALITY_CODES";
  private static final int READ_MUNICIPALITY_CODES_PARAM = 1;
  private static final String READ_MUNICIPALITY_OFFICE_CODES_PROC = "READ_MUNICIPALITY_OFFICE_CODES";
  private static final int READ_MUNICIPALITY_OFFICE_CODES_PARAM = 1;

  // Line Item procs
  private static final String READ_INVENTORY_ITEM_CODES_PROC = "READ_INVENTORY_ITEM_CODES";
  private static final int READ_INVENTORY_ITEM_CODES_PARAM = 1;
  private static final String READ_INVENTORY_ITEM_DETAILS_PROC = "READ_INVENTORY_ITEM_DETAILS";
  private static final int READ_INVENTORY_ITEM_DETAILS_PARAM = 2;
  private static final String READ_INVENTORY_XREFS_PROC = "READ_INVENTORY_XREFS";
  private static final int READ_INVENTORY_XREFS_PARAM = 3;

  // Market Rate Premium procs
  private static final String READ_MARKET_RATE_PREMIUMS_PROC = "READ_MARKET_RATE_PREMIUMS";
  private static final int READ_MARKET_RATE_PREMIUMS_PARAM = 1;

  // Structure Group procs
  private static final String READ_STRUCTURE_GROUP_CODES_PROC = "READ_STRUCTURE_GROUP_CODES";
  private static final int READ_STRUCTURE_GROUP_CODES_PARAM = 1;

  // Program Years
  private static final String READ_PROGRAM_YEARS_PROC = "READ_PROGRAM_YEARS";
  private static final int READ_PROGRAM_YEARS_PARAM = 0;
  

  private static final int READ_GENERIC_CODES_PARAM = 1;
  
  // Read procs for generic codes
  private static final String READ_CROP_UNIT_CODES_PROC = "READ_CROP_UNIT_CODES";
  private static final String READ_FARM_TYPE_CODES_PROC = "READ_FARM_TYPE_CODES";
  private static final String READ_FEDERAL_ACCOUNTING_CODES_PROC = "READ_FEDERAL_ACCOUNTING_CODES";
  private static final String READ_FEDERAL_STATUS_CODES_PROC = "READ_FEDERAL_STATUS_CODES";
  private static final String READ_PARTICIPANT_CLASS_CODES_PROC = "READ_PARTICIPANT_CLASS_CODES";
  private static final String READ_PARTICIPANT_LANG_CODES_PROC = "READ_PARTICIPANT_LANG_CODES";
  private static final String READ_PARTICIPANT_PROFILE_CODES_PROC = "READ_PARTICIPANT_PROFILE_CODES";
  private static final String READ_REGIONAL_OFFICE_CODES_PROC = "READ_REGIONAL_OFFICE_CODES";
  private static final String READ_TRIAGE_QUEUE_CODES_PROC = "READ_TRIAGE_QUEUE_CODES";
  
  // Crop Unit Conversion procs
  private static final String READ_CROP_UNIT_CONVERSIONS_PROC = "READ_CROP_UNIT_CONVERSIONS";
  private static final int READ_CROP_UNIT_CONVERSIONS_PARAM = 1;
  
  // Fruit veg code procs
  private static final String READ_FRUIT_VEG_CODES_PROC = "READ_FRUIT_VEG_CODES";
  private static final int READ_FRUIT_VEG_CODES_PARAM = 0;
  private static final String READ_VARIANCE_LIMIT_FOR_FRUIT_VEG_CODE_PROC = "READ_VARIANCE_LIMIT_FOR_FRUIT_VEG_CODE";
  private static final int READ_VARIANCE_LIMIT_FOR_FRUIT_VEG_CODE_PARAM = 1;
  private static final String CHECK_FRUIT_VEG_CODE_IN_USE_PROC = "CHECK_FRUIT_VEG_CODE_IN_USE";
  private static final int CHECK_FRUIT_VEG_CODE_IN_USE_PARAM = 1;
  
  // Commodity Type Code procs
  private static final String READ_COMMODITY_TYPE_CODES_PROC = "READ_COMMODITY_TYPE_CODES";
  private static final int READ_COMMODITY_TYPE_CODES_PARAM = 0;
  
  // Expected production procs
  private static final String READ_EXPECTED_PRODUCTION_ITEMS_PROC = "READ_EXPECTED_PRODUCTION_ITEMS";
  private static final int READ_EXPECTED_PRODUCTION_ITEMS_PARAM = 1;
  private static final String CHECK_EXPECTED_PRODUCTION_ITEM_EXISTS_PROC = "CHECK_EXPECTED_PRODUCTION_ITEM_EXISTS";
  private static final int CHECK_EXPECTED_PRODUCTION_ITEM_EXISTS_PARAM = 1;
  
  // configuration parameter procs
  private static final String READ_CONFIGURATION_PARAMETERS_PROC = "READ_CONFIGURATION_PARAMETERS";
  private static final int READ_CONFIGURATION_PARAMETERS_PARAM = 0;
  private static final String READ_CONFIGURATION_PARAMETER_PROC = "READ_CONFIGURATION_PARAMETER";
  private static final int READ_CONFIGURATION_PARAMETER_PARAM = 2;
  
  // year configuration parameter procs
  private static final String READ_YEAR_CONFIGURATION_PARAMS_PROC = "READ_YEAR_CONFIGURATION_PARAMS";
  private static final int READ_YEAR_CONFIGURATION_PARAMS_PARAM = 1;
  private static final String READ_YEAR_CONFIGURATION_PARAM_PROC = "READ_YEAR_CONFIGURATION_PARAM";
  private static final int READ_YEAR_CONFIGURATION_PARAM_PARAM = 1;
  
  private static final String CHECK_LINE_ITEM_EXISTS_FOR_PROGRAM_YEAR_PROC = "CHECK_LINE_ITEM_EXISTS_FOR_PROGRAM_YEAR";
  private static final int CHECK_LINE_ITEM_EXISTS_FOR_PROGRAM_YEAR_PARAM = 2;
  
  // Farm type 3 procs
  private static final String READ_FARM_TYPE_3_CODES_PROC = "READ_FARM_TYPE_3_CODES";
  private static final int READ_FARM_TYPE_3_CODES_PARAM = 0;
  private static final String CHECK_FARM_TYPE_3_IN_USE_PROC = "CHECK_FARM_TYPE_3_IN_USE";
  private static final int CHECK_FARM_TYPE_3_IN_USE_PARAM = 1;
  private static final String READ_FARM_TYPE_3_INCOME_RANGE_PROC = "READ_FARM_TYPE_3_INCOME_RANGE";
  private static final int READ_FARM_TYPE_3_INCOME_RANGE_PARAM = 1;
  
  // Farm type 2 procs
  private static final String READ_FARM_TYPE_2_CODES_PROC = "READ_FARM_TYPE_2_CODES";
  private static final int READ_FARM_TYPE_2_CODES_PARAM = 0;
  private static final String CHECK_FARM_TYPE_2_IN_USE_PROC = "CHECK_FARM_TYPE_2_IN_USE";
  private static final int CHECK_FARM_TYPE_2_IN_USE_PARAM = 1;
  private static final String READ_FARM_TYPE_2_INCOME_RANGE_PROC = "READ_FARM_TYPE_2_INCOME_RANGE";
  private static final int READ_FARM_TYPE_2_INCOME_RANGE_PARAM = 2;
  
  // Farm type 1 procs
  private static final String READ_FARM_TYPE_1_CODES_PROC = "READ_FARM_TYPE_1_CODES";
  private static final int READ_FARM_TYPE_1_CODES_PARAM = 0;
  private static final String CHECK_FARM_TYPE_1_IN_USE_PROC = "CHECK_FARM_TYPE_1_IN_USE";
  private static final int CHECK_FARM_TYPE_1_IN_USE_PARAM = 1;
  private static final String READ_FARM_TYPE_1_INCOME_RANGE_PROC = "READ_FARM_TYPE_1_INCOME_RANGE";
  private static final int READ_FARM_TYPE_1_INCOME_RANGE_PARAM = 3;
  
  // Farm type income range
  private static final String READ_FARM_TYPE_DEFAULT_INCOME_RANGE_PROC = "READ_FARM_TYPE_DEFAULT_INCOME_RANGE";
  private static final int READ_FARM_TYPE_DEFAULT_INCOME_RANGE_PARAM = 0;
  
  // Tips Line Items
  private static final String READ_FARM_TIP_LINE_ITEMS_PROC = "READ_TIP_LINE_ITEMS";
  private static final int READ_FARM_TIP_LINE_ITEMS_PARAM = 0;
  private static final String READ_FARM_TIP_LINE_ITEM_PROC = "READ_TIP_LINE_ITEM";
  private static final int READ_FARM_TIP_LINE_ITEM_PARAM = 1;
  private static final String CHECK_TIP_LINE_ITEM_EXISTS_PROC = "CHECK_TIP_LINE_ITEM_EXISTS";
  private static final int CHECK_TIP_LINE_ITEM_EXISTS_PARAM = 1;
  private static final String READ_TIP_BENCHMARK_INFO_PROC = "READ_TIP_BENCHMARK_INFO";
  
  // Document Teplate procs
  private static final String READ_DOCUMENT_TEMPLATES_PROC = "READ_DOCUMENT_TEMPLATES";
  private static final int READ_DOCUMENT_TEMPLATES_PARAM = 1;
  
  private static final String READ_SECTOR_CODES_PROC = "READ_SECTOR_CODES";
  private static final int READ_SECTOR_PARAM = 1;
  private static final String READ_SECTOR_DETAIL_CODES_PROC = "READ_SECTOR_DETAIL_CODES";
  private static final int READ_SECTOR_DETAIL_PARAM = 1;
  
  private static final Map<String, String> READ_PROC_MAP = new HashMap<>();
  static {
    READ_PROC_MAP.put(CodeTables.CROP_UNIT, READ_CROP_UNIT_CODES_PROC);
    READ_PROC_MAP.put(CodeTables.FARM_TYPE, READ_FARM_TYPE_CODES_PROC);
    READ_PROC_MAP.put(CodeTables.FEDERAL_ACCOUNTING, READ_FEDERAL_ACCOUNTING_CODES_PROC);
    READ_PROC_MAP.put(CodeTables.FEDERAL_STATUS, READ_FEDERAL_STATUS_CODES_PROC);
    READ_PROC_MAP.put(CodeTables.PARTICIPANT_CLASS, READ_PARTICIPANT_CLASS_CODES_PROC);
    READ_PROC_MAP.put(CodeTables.PARTICIPANT_LANGUAGE, READ_PARTICIPANT_LANG_CODES_PROC);
    READ_PROC_MAP.put(CodeTables.PARTICIPANT_PROFILE, READ_PARTICIPANT_PROFILE_CODES_PROC);
    READ_PROC_MAP.put(CodeTables.REGIONAL_OFFICE, READ_REGIONAL_OFFICE_CODES_PROC);
    READ_PROC_MAP.put(CodeTables.TRIAGE_QUEUE, READ_TRIAGE_QUEUE_CODES_PROC);
  }


  /**
   * @param transaction Transaction
   * @param codeTable codeTable
   * @param code code
   * @throws DataAccessException On Exception
   * 
   * @return List of type Code
   */
  @SuppressWarnings("resource")
  public List<Code> readGenericCodes(final Transaction transaction,
      final String codeTable,
      final String code)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<Code> codes = null;
    String procName = getCodeTableReadProcName(codeTable);

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + procName, READ_GENERIC_CODES_PARAM, true);
      
      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      rs = proc.getResultSet();

      codes = new ArrayList<>();

      while (rs.next()) {
        Code item = new Code();
        item.setCode(getString(rs, "code"));
        item.setDescription(getString(rs, "description"));
        item.setEstablishedDate(getDate(rs, "established_date"));
        item.setExpiryDate(getDate(rs, "expiry_date"));
        item.setRevisionCount(getInteger(rs, "revision_count"));
        
        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }
  
  
  /**
   * @param codeTable codeTable
   * @return String
   */
  private String getCodeTableReadProcName(String codeTable) {
    return READ_PROC_MAP.get(codeTable);
  }


  /**
   * @param transaction Transaction
   * @param year Integer
   * @param lineItem Integer
   * @throws DataAccessException On Exception
   * 
   * @return List of type LineItemCode
   */
  @SuppressWarnings("resource")
  public List<LineItemCode> readLineItems(final Transaction transaction,
      final Integer year,
      final Integer lineItem)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<LineItemCode> codes = null;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_LINE_ITEMS_PROC, READ_LINE_ITEMS_PARAM, true);

      int param = 1;
      proc.setInt(param++, year);
      proc.setInt(param++, lineItem);
      proc.execute();

      rs = proc.getResultSet();

      codes = new ArrayList<>();

      while (rs.next()) {
        LineItemCode item = new LineItemCode();
        item.setLineItemId(getInteger(rs, "line_item_id"));
        item.setProgramYear(getInteger(rs, "program_year"));
        item.setLineItem(getInteger(rs, "line_item"));
        item.setDescription(getString(rs, "description"));
        item.setProvince(getString(rs, "province"));
        item.setIsEligible(Boolean.valueOf(getIndicator(rs, "eligibility_ind")));
        item.setIsEligibleRefYears(Boolean.valueOf(getIndicator(rs, "eligibility_for_ref_years_ind")));
        item.setIsYardage(Boolean.valueOf(getIndicator(rs, "yardage_ind")));
        item.setIsProgramPayment(Boolean.valueOf(getIndicator(rs, "program_payment_ind")));
        item.setIsContractWork(Boolean.valueOf(getIndicator(rs, "contract_work_ind")));
        item.setIsSupplyManagedCommodity(Boolean.valueOf(getIndicator(rs, "supply_managed_commodity_ind")));
        item.setIsExcludeFromRevenueCalculation(Boolean.valueOf(getIndicator(rs, "exclude_from_revenue_calc_ind")));
        item.setIsIndustryAverageExpense(Boolean.valueOf(getIndicator(rs, "industry_average_expense_ind")));
        item.setEstablishedDate(getDate(rs, "established_date"));
        item.setExpiryDate(getDate(rs, "expiry_date"));
        item.setRevisionCount(getInteger(rs, "revision_count"));
        item.setSectorDetailLineItemId(getInteger(rs, "sector_detail_line_item_id"));
        item.setSectorCode(getString(rs, "sector_code"));
        item.setSectorCodeDescription(getString(rs, "sector_code_description"));
        item.setSectorDetailCode(getString(rs, "sector_detail_code"));
        item.setSectorDetailCodeDescription(getString(rs, "sector_detail_code_description"));
        item.setFruitVegCodeName(getString(rs, "fruit_veg_type_code"));
        item.setFruitVegCodeDescription(getString(rs, "Fruit_Veg_Type_Code_Desc"));
        item.setCommodityTypeCode(getString(rs, "commodity_type_code"));
        item.setCommodityTypeCodeDescription(getString(rs, "Commodity_Type_Code_Desc"));
        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }


  /**
   * @param transaction Transaction
   * @param year year
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @throws DataAccessException On Exception
   * 
   * @return List<FMV>
   */
  @SuppressWarnings("resource")
  public List<FMV> readFMVs(final Transaction transaction,
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<FMV> fmvs = null;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FMV_PROC, READ_FMV_PARAM, true);

      int param = 1;
      proc.setInt(param++, year);
      proc.setString(param++, inventoryItemCode);
      proc.setString(param++, municipalityCode);
      proc.setString(param++, cropUnitCode);
      proc.execute();

      rs = proc.getResultSet();

      fmvs = new ArrayList<>();

      FMV fmv = null;
      
      int c;
      int periodIndex;

      while (rs.next()) {
        c = 1;

        Integer programYear = getInteger(rs, c++);
        String itemCode = getString(rs, c++);
        String muniCode = getString(rs, c++);
        String unitCode = getString(rs, c++);

        boolean sameAsPrevious =
          fmv != null
          && fmv.getProgramYear().equals(programYear)
          && fmv.getInventoryItemCode().equals(itemCode)
          && fmv.getMunicipalityCode().equals(muniCode)
          && fmv.getCropUnitCode().equals(unitCode);

        if(fmv == null || !sameAsPrevious) {
          fmv = new FMV();
          fmvs.add(fmv);
          fmv.setProgramYear(programYear);
          fmv.setInventoryItemCode(itemCode);
          fmv.setMunicipalityCode(muniCode);
          fmv.setCropUnitCode(unitCode);
          fmv.setInventoryItemCodeDescription(getString(rs, c++));
          fmv.setMunicipalityCodeDescription(getString(rs, c++));
          fmv.setCropUnitCodeDescription(getString(rs, c++));
          fmv.setDefaultCropUnitCode(getString(rs, c++));
          fmv.setDefaultCropUnitCodeDescription(getString(rs, c++));
        } else {
          c++; // InventoryItemCodeDescription
          c++; // MunicipalityCodeDescription
          c++; // CropUnitCodeDescription
          c++; // DefaultCropUnitCode
          c++; // DefaultCropUnitCodeDescription
        }

        FMVPeriod period = new FMVPeriod();
        period.setFairMarketValueId(getInteger(rs, c++));
        period.setPeriod(getInteger(rs, c++));
        period.setPrice(getDouble(rs, c++));
        period.setPercentVariance(getDouble(rs, c++));
        period.setRevisionCount(getInteger(rs, c++));
        
        FMVPeriod[] periods = fmv.getPeriods();
        periodIndex = period.getPeriod().intValue() - 1;
        periods[periodIndex] = period;
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return fmvs;
  }
  
  
  /**
   * @param transaction Transaction
   * @param year year
   * @param inventoryItemCodeFilter inventoryItemCodeFilter
   * @param inventoryItemDescriptionFilter inventoryItemDescriptionFilter
   * @param municipalityDescriptionFilter municipalityDescriptionFilter
   * @param cropUnitDescriptionFilter cropUnitDescriptionFilter
   * @throws DataAccessException On Exception
   * 
   * @return List<String>
   */
  @SuppressWarnings("resource")
  public List<String> exportFMVs(final Transaction transaction,
      final Integer year,
      final String inventoryItemCodeFilter,
      final String inventoryItemDescriptionFilter,
      final String municipalityDescriptionFilter,
      final String cropUnitDescriptionFilter)
          throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<String> fmvExportRecords = new ArrayList<>();
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + EXPORT_FMV_PROC, EXPORT_FMV_PARAM, true);
      
      int param = 1;
      proc.setInt(param++, year);
      proc.setString(param++, inventoryItemCodeFilter);
      proc.setString(param++, inventoryItemDescriptionFilter);
      proc.setString(param++, municipalityDescriptionFilter);
      proc.setString(param++, cropUnitDescriptionFilter);
      proc.execute();
      
      rs = proc.getResultSet();
      
      
      while (rs.next()) {
        String exportRecord = getString(rs, 1);
        fmvExportRecords.add(exportRecord);
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return fmvExportRecords;
  }
  
  
  /**
   * @param transaction Transaction
   * @param year year
   * @throws DataAccessException On Exception
   * 
   * @return List<String>
   */
  @SuppressWarnings("resource")
  public List<String> exportMissingFMVs(final Transaction transaction,
      final Integer year)
          throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<String> fmvExportRecords = new ArrayList<>();
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + EXPORT_MISSING_FMV_PROC, EXPORT_MISSING_FMV_PARAM, true);
      
      int param = 1;
      proc.setInt(param++, year);
      proc.execute();
      
      rs = proc.getResultSet();
      
      
      while (rs.next()) {
        String exportRecord = getString(rs, 1);
        fmvExportRecords.add(exportRecord);
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return fmvExportRecords;
  }


  /**
   * @param transaction Transaction
   * @param code code
   * @throws DataAccessException On Exception
   * 
   * @return List of type Code
   */
  @SuppressWarnings("resource")
  public List<MunicipalityCode> readMunicipalityCodes(final Transaction transaction,
      final String code)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<MunicipalityCode> codes = null;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_MUNICIPALITY_CODES_PROC, READ_MUNICIPALITY_CODES_PARAM, true);
      
      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      rs = proc.getResultSet();

      codes = new ArrayList<>();

      while (rs.next()) {
        MunicipalityCode item = new MunicipalityCode();
        item.setCode(getString(rs, "code"));
        item.setDescription(getString(rs, "description"));
        item.setEstablishedDate(getDate(rs, "established_date"));
        item.setExpiryDate(getDate(rs, "expiry_date"));
        item.setRevisionCount(getInteger(rs, "revision_count"));
        
        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }
  
  
  /**
   * @param transaction Transaction
   * @param code code
   * @throws DataAccessException On Exception
   * 
   * @return Map of municipality code to list of regional office codes
   */
  @SuppressWarnings("resource")
  public Map<String, List<String>> readMunicipalityOfficeCodes(final Transaction transaction,
      final String code)
  throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    Map<String, List<String>> muniOfficeCodes = new HashMap<>();
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_MUNICIPALITY_OFFICE_CODES_PROC, READ_MUNICIPALITY_OFFICE_CODES_PARAM, true);
      
      int param = 1;
      proc.setString(param++, code);
      proc.execute();
      
      rs = proc.getResultSet();
      
      while (rs.next()) {
        String mc = getString(rs, "municipality_code");
        String roc = getString(rs, "regional_office_code");
        
        List<String> officeCodes = muniOfficeCodes.get(mc);
        if(officeCodes == null) {
          officeCodes = new ArrayList<>();
          muniOfficeCodes.put(mc, officeCodes);
        }
        officeCodes.add(roc);
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return muniOfficeCodes;
  }

  
  
  
  
  /**
   * @param transaction Transaction
   * @param year year
   * @throws DataAccessException On Exception
   * 
   * @return List of type BPU
   */
  @SuppressWarnings("resource")
  public List<BPU> readBPUs(final Transaction transaction, final Integer year)
  throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<BPU> bpus = new ArrayList<>();
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_BPU_PROC, READ_BPU_PARAM, true);

      int param = 1;
      proc.setInt(param++, year);
      proc.execute();

      rs = proc.getResultSet();

      BPU bpu = null;
      
      int c = 1;
      int yearIndex = 0;
      Integer prevBpuId = null;

      while (rs.next()) {
        c = 1;

        Integer bpuId = getInteger(rs, c++);
 
        if(prevBpuId == null || !bpuId.equals(prevBpuId)) {
        	bpu = new BPU();
        	bpus.add(bpu);
        	
        	bpu.setBpuId(bpuId);
        	bpu.setProgramYear(getInteger(rs, c++));
        	bpu.setInvSgCode(getString(rs, c++));
        	bpu.setInvSgCodeDescription(getString(rs, c++));
        	bpu.setInvSgType(getString(rs, c++));
        	bpu.setMunicipalityCode(getString(rs, c++));
        	bpu.setMunicipalityCodeDescription(getString(rs, c++));
        	
        	yearIndex = 0;
        	prevBpuId = bpuId;
        } else {
          final int six = 6;
          c += six;
        }

        BPUYear bpuYear = new BPUYear();
        bpuYear.setBpuId(bpuId);
        bpuYear.setYear(getInteger(rs, c++));
        bpuYear.setAverageMargin(getDouble(rs, c++));
        bpuYear.setAverageExpense(getDouble(rs, c++));
        bpuYear.setRevisionCount(getInteger(rs, c++));
        
        if(bpu != null) {
          bpu.getYears()[yearIndex] = bpuYear;
        }
        yearIndex++;
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return bpus;
  }
  
  
  /**
   * @param transaction Transaction
   * @param year year
   * @param inventoryItemCodeFilter inventoryItemCodeFilter
   * @param inventoryItemDescriptionFilter inventoryItemDescriptionFilter
   * @param municipalityDescriptionFilter municipalityDescriptionFilter
   * @throws DataAccessException On Exception
   * 
   * @return List<String>
   */
  @SuppressWarnings("resource")
  public List<String> exportBPUs(final Transaction transaction,
      final Integer year,
      final String inventoryItemCodeFilter,
      final String inventoryItemDescriptionFilter,
      final String municipalityDescriptionFilter)
          throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<String> bpuExportRecords = new ArrayList<>();
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + EXPORT_BPU_PROC, EXPORT_BPU_PARAM, true);
      
      int param = 1;
      proc.setInt(param++, year);
      proc.setString(param++, inventoryItemCodeFilter);
      proc.setString(param++, inventoryItemDescriptionFilter);
      proc.setString(param++, municipalityDescriptionFilter);
      proc.execute();
      
      rs = proc.getResultSet();
      
      
      while (rs.next()) {
        String exportRecord = getString(rs, 1);
        bpuExportRecords.add(exportRecord);
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return bpuExportRecords;
  }
  
  
  /**
   * @param transaction Transaction
   * @param year year
   * @throws DataAccessException On Exception
   * 
   * @return List<String>
   */
  @SuppressWarnings("resource")
  public List<String> exportMissingBPUs(final Transaction transaction,
      final Integer year)
          throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<String> bpuExportRecords = new ArrayList<>();
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + EXPORT_MISSING_BPU_PROC, EXPORT_MISSING_BPU_PARAM, true);
      
      int param = 1;
      proc.setInt(param++, year);
      proc.execute();
      
      rs = proc.getResultSet();
      
      
      while (rs.next()) {
        String exportRecord = getString(rs, 1);
        bpuExportRecords.add(exportRecord);
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return bpuExportRecords;
  }


  /**
   * @param transaction Transaction
   * @param code code
   * @throws DataAccessException On Exception
   * 
   * @return List of type Code
   */
  @SuppressWarnings("resource")
  public List<InventoryItemCode> readInventoryItemCodes(final Transaction transaction,
      final String code)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<InventoryItemCode> codes = null;
    Map<String, InventoryItemCode> codesMap = new HashMap<>();
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_INVENTORY_ITEM_CODES_PROC, READ_INVENTORY_ITEM_CODES_PARAM, true);
      
      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      rs = proc.getResultSet();

      codes = new ArrayList<>();

      while (rs.next()) {
        InventoryItemCode item = new InventoryItemCode();
        String itemCode = getString(rs, "code");
        item.setCode(itemCode);
        item.setDescription(getString(rs, "description"));
        item.setRollupInventoryItemCode(getString(rs, "rollup_inventory_item_code"));
        item.setRollupInventoryItemCodeDescription(getString(rs, "rollup_inventory_item_desc"));
        item.setEstablishedDate(getDate(rs, "established_date"));
        item.setExpiryDate(getDate(rs, "expiry_date"));
        item.setRevisionCount(getInteger(rs, "revision_count"));
        
        codes.add(item);
        codesMap.put(itemCode, item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_INVENTORY_ITEM_DETAILS_PROC, READ_INVENTORY_ITEM_DETAILS_PARAM, true);
      
      Integer programYearParam = null;
      int param = 1;
      proc.setString(param++, code);
      proc.setInt(param++, programYearParam);
      proc.execute();
      
      rs = proc.getResultSet();
      
      while (rs.next()) {
        InventoryItemDetail detail = new InventoryItemDetail();
        detail.setInventoryItemDetailId(getInteger(rs, "inventory_item_detail_id"));
        
        String itemCode = getString(rs, "inventory_item_code");
        Double insurableValue = getDouble(rs, "insurable_value");
        Double premiumRate = getDouble(rs, "premium_rate");
        Integer programYear = getInteger(rs, "program_year");
        
        detail.setInventoryItemCode(itemCode);
        detail.setProgramYear(programYear);
        detail.setInsurableValue(insurableValue);
        detail.setPremiumRate(premiumRate);
        detail.setIsEligible(Boolean.valueOf(getIndicator(rs, "eligibility_ind")));
        detail.setFruitVegCodeName(getString(rs, "fruit_veg_type_code"));
        detail.setLineItem(getString(rs, "line_item"));
        detail.setCommodityTypeCodeName(getString(rs, "commodity_type_code"));
        detail.setRevisionCount(getInteger(rs, "revision_count"));
        
        InventoryItemCode item = codesMap.get(itemCode);
        item.getDetails().put(programYear, detail);
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }


  /**
   * @param transaction Transaction
   * @param inventoryClassCode inventoryClassCode
   * @param inventoryItemCode inventoryItemCode
   * @param commodityXrefId Integer
   * @throws DataAccessException On Exception
   * 
   * @return List of type InventoryXref
   */
  @SuppressWarnings("resource")
  public List<InventoryXref> readInventoryXrefs(final Transaction transaction,
      final String inventoryClassCode,
      final String inventoryItemCode,
      final Integer commodityXrefId)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<InventoryXref> codes = null;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_INVENTORY_XREFS_PROC, READ_INVENTORY_XREFS_PARAM, true);

      int param = 1;
      proc.setString(param++, inventoryClassCode);
      proc.setString(param++, inventoryItemCode);
      proc.setInt(param++, commodityXrefId);
      proc.execute();

      rs = proc.getResultSet();

      codes = new ArrayList<>();

      while (rs.next()) {
        InventoryXref item = new InventoryXref();
        item.setCommodityXrefId(getInteger(rs, "agristabilty_cmmdty_xref_id"));
        item.setInventoryItemCode(getString(rs, "inventory_item_code"));
        item.setInventoryItemCodeDescription(getString(rs, "inventory_item_code_desc"));
        item.setIsMarketCommodity(Boolean.valueOf(getIndicator(rs, "market_commodity_ind")));
        item.setInventoryClassCode(getString(rs, "inventory_class_code"));
        item.setInventoryClassCodeDescription(getString(rs, "inventory_class_code_desc"));
        item.setInventoryGroupCode(getString(rs, "inventory_group_code"));
        item.setInventoryGroupCodeDescription(getString(rs, "inventory_group_code_desc"));
        item.setRevisionCount(getInteger(rs, "revision_count"));

        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }


  /**
   * @param transaction Transaction
   * @param code code
   * @throws DataAccessException On Exception
   *
   * @return List of type MarketRatePremium
   */
  @SuppressWarnings("resource")
  public List<MarketRatePremium> readMarketRatePremiums(final Transaction transaction,
      final Long id)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<MarketRatePremium> codes = null;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_MARKET_RATE_PREMIUMS_PROC, READ_MARKET_RATE_PREMIUMS_PARAM, true);

      int param = 1;
      proc.setLong(param++, id);
      proc.execute();

      rs = proc.getResultSet();

      codes = new ArrayList<>();

      while (rs.next()) {
        MarketRatePremium item = new MarketRatePremium();
        item.setMarketRatePremiumId(getLong(rs, "market_rate_premium_id"));
        item.setMinTotalPremiumAmount(getBigDecimal(rs, "min_total_premium_amount"));
        item.setMaxTotalPremiumAmount(getBigDecimal(rs, "max_total_premium_amount"));
        item.setRiskChargeFlatAmount(getBigDecimal(rs, "risk_charge_flat_amount"));
        item.setRiskChargePercentagePremium(getBigDecimal(rs, "risk_charge_pct_premium"));
        item.setAdjustChargeFlatAmount(getBigDecimal(rs, "adjust_charge_flat_amount"));
        item.setRevisionCount(getInteger(rs, "revision_count"));

        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }

    return codes;
  }


  /**
   * @param transaction Transaction
   * @param code code
   * @throws DataAccessException On Exception
   * 
   * @return List of type Code
   */
  @SuppressWarnings("resource")
  public List<StructureGroupCode> readStructureGroupCodes(final Transaction transaction,
      final String code)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<StructureGroupCode> codes = null;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_STRUCTURE_GROUP_CODES_PROC, READ_STRUCTURE_GROUP_CODES_PARAM, true);

      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      rs = proc.getResultSet();

      codes = new ArrayList<>();

      while (rs.next()) {
        StructureGroupCode item = new StructureGroupCode();
        item.setCode(getString(rs, "code"));
        item.setDescription(getString(rs, "description"));
        item.setRollupStructureGroupCode(getString(rs, "rollup_structure_group_code"));
        item.setRollupStructureGroupCodeDescription(getString(rs, "rollup_structure_group_desc"));
        item.setEstablishedDate(getDate(rs, "established_date"));
        item.setExpiryDate(getDate(rs, "expiry_date"));
        item.setRevisionCount(getInteger(rs, "revision_count"));

        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }

    return codes;
  }
  
  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List of type Integer
   */
  @SuppressWarnings("resource")
  public List<Integer> readProgramYears(final Transaction transaction)
          throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<Integer> programYears = null;
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_PROGRAM_YEARS_PROC, READ_PROGRAM_YEARS_PARAM, true);
      
      proc.execute();
      
      rs = proc.getResultSet();
      
      programYears = new ArrayList<>();
      
      while (rs.next()) {
        Integer year = getInteger(rs, "program_year");
        
        programYears.add(year);
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return programYears;
  }


  /**
   * @param transaction Transaction
   * @param inventoryItemCode inventoryItemCode
   * @throws DataAccessException On Exception
   * 
   * @return List<FMV>
   */
  @SuppressWarnings("resource")
  public List<CropUnitConversion> readCropUnitConversions(final Transaction transaction,
      final String inventoryItemCode)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<CropUnitConversion> cropUnitConversions = null;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_CROP_UNIT_CONVERSIONS_PROC, READ_CROP_UNIT_CONVERSIONS_PARAM, true);

      int param = 1;
      proc.setString(param++, inventoryItemCode);
      proc.execute();

      rs = proc.getResultSet();

      cropUnitConversions = new ArrayList<>();

      CropUnitConversion unitConversion = null;
      
      int c;

      while (rs.next()) {
        c = 1;

        String itemCode = getString(rs, c++);
        String defaultUnitCode = getString(rs, c++);

        boolean sameAsPrevious =
          unitConversion != null
          && unitConversion.getInventoryItemCode().equals(itemCode)
          && unitConversion.getDefaultCropUnitCode().equals(defaultUnitCode);

        if(unitConversion == null || !sameAsPrevious) {
          unitConversion = new CropUnitConversion();
          cropUnitConversions.add(unitConversion);
          
          unitConversion.setInventoryItemCode(itemCode);
          unitConversion.setDefaultCropUnitCode(defaultUnitCode);
          
          unitConversion.setInventoryItemCodeDescription(getString(rs, c++));
          unitConversion.setDefaultCropUnitCodeDescription(getString(rs, c++));
          unitConversion.setRevisionCount(getInteger(rs, c++));
        } else {
          c++; // InventoryItemCodeDescription
          c++; // DefaultCropUnitCodeDescription
          c++; // RevisionCount
        }

        String targetCropUnitCode = getString(rs, c++);
        if(targetCropUnitCode != null) { // It's possible to have a default unit with no conversion factors
          
          CropUnitConversionItem conversionItem = new CropUnitConversionItem();
          conversionItem.setTargetCropUnitCode(targetCropUnitCode);
          conversionItem.setTargetCropUnitCodeDescription(getString(rs, c++));
          conversionItem.setConversionFactor(rs.getBigDecimal(c++));
          
          unitConversion.getConversionItems().add(conversionItem);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return cropUnitConversions;
  }

  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List<FruitVegTypeCode>
   */
  @SuppressWarnings("resource")
  public List<FruitVegTypeCode> readFruitVegCodes(final Transaction transaction) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<FruitVegTypeCode> codes = null;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FRUIT_VEG_CODES_PROC, READ_FRUIT_VEG_CODES_PARAM, true);
      
      proc.execute();
      rs = proc.getResultSet();
      codes = new ArrayList<>();

      while (rs.next()) {
        FruitVegTypeCode item = new FruitVegTypeCode();        
        item.setName(getString(rs, "fruit_veg_type_code"));
        item.setDescription(getString(rs, "description"));
        item.setEstablishedDate(getDate(rs, "established_date"));
        item.setExpiryDate(getDate(rs, "expiry_date"));
        item.setRevisionCount(getInteger(rs, "revision_count"));
        item.setVarianceLimit(getDouble(rs,"revenue_variance_limit"));
        
        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }
  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List<CommodityTypeCode>
   */
  @SuppressWarnings("resource")
  public List<CommodityTypeCode> readCommodityTypeCodes(final Transaction transaction) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<CommodityTypeCode> codes = null;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_COMMODITY_TYPE_CODES_PROC, READ_COMMODITY_TYPE_CODES_PARAM, true);
      
      proc.execute();
      rs = proc.getResultSet();
      codes = new ArrayList<>();

      while (rs.next()) {
        CommodityTypeCode item = new CommodityTypeCode();
        item.setCode(getString(rs, "commodity_type_code"));
        item.setDescription(getString(rs, "description"));

        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }
  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return Double variance limit for FruitVegCode
   */
  @SuppressWarnings("resource")
  public Double readVarianceLimitForFruitVegCode(final FruitVegTypeCode fruitVegCode, final Transaction transaction) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    Double varianceLimit = 0.0;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_VARIANCE_LIMIT_FOR_FRUIT_VEG_CODE_PROC, READ_VARIANCE_LIMIT_FOR_FRUIT_VEG_CODE_PARAM, true);
      
      int param = 1;
      proc.setString(param++, fruitVegCode.getName());
      
      proc.execute();
      rs = proc.getResultSet();
      
      while (rs.next()) {
        varianceLimit = getDouble(rs,"");
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return varianceLimit;
  }
  
  
  @SuppressWarnings("resource")
  public boolean checkFruitVegCodeInUse(final FruitVegTypeCode fruitVegCode, final Transaction transaction) throws DataAccessException {
    boolean inUse = false;
  
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CHECK_FRUIT_VEG_CODE_IN_USE_PROC, CHECK_FRUIT_VEG_CODE_IN_USE_PARAM, true);
      
      proc.setString(param++, fruitVegCode.getName());
      
      proc.execute();
      rs = proc.getResultSet();
      inUse = rs.next();

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return inUse;
  }
  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List<ExpectedProduction>
   */
  @SuppressWarnings("resource")
  public List<ExpectedProduction> readExpectedProductionItems(final Transaction transaction, Integer id) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<ExpectedProduction> codes = null;
    int param = 1;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_EXPECTED_PRODUCTION_ITEMS_PROC, READ_EXPECTED_PRODUCTION_ITEMS_PARAM, true);
      
      proc.setInt(param++, id);
      
      proc.execute();
      rs = proc.getResultSet();
      codes = new ArrayList<>();

      while (rs.next()) {
        ExpectedProduction item = new ExpectedProduction();      
        item.setId(getInteger(rs, "expected_production_id"));
        item.setExpectedProductionPerAcre(getDouble(rs, "expected_prodctn_per_prod_unit"));
        item.setInventoryItemCode(getString(rs, "inventory_item_code"));
        item.setCropUnitCode(getString(rs, "crop_unit_code"));
        item.setId(getInteger(rs, "expected_production_id"));
        item.setEstablishedDate(getDate(rs, "when_created"));
        item.setRevisionCount(getInteger(rs, "revision_count"));
        item.setInventoryItemCodeDescription(getString(rs, "inventory_description"));
        item.setCropUnitCodeDescription(getString(rs, "crop_description"));
        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }
  
  
  @SuppressWarnings("resource")
  public boolean checkExpectedProductionItemExists(final ExpectedProduction expectedProduction, final Transaction transaction) throws DataAccessException {
    boolean inUse = false;
  
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CHECK_EXPECTED_PRODUCTION_ITEM_EXISTS_PROC, CHECK_EXPECTED_PRODUCTION_ITEM_EXISTS_PARAM, true);
      
      proc.setString(param++, expectedProduction.getInventoryItemCode());
      
      proc.execute();
      rs = proc.getResultSet();
      inUse = rs.next();

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return inUse;
  }
  
  
  @SuppressWarnings("resource")
  public boolean checkLineItemExistsForProgramYear(final Integer programYear, final Integer lineItem, final Transaction transaction) throws DataAccessException {
    boolean exists = false;
  
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CHECK_LINE_ITEM_EXISTS_FOR_PROGRAM_YEAR_PROC, CHECK_LINE_ITEM_EXISTS_FOR_PROGRAM_YEAR_PARAM, true);
      
      proc.setInt(param++, lineItem);
      proc.setInt(param++, programYear);
      
      proc.execute();
      rs = proc.getResultSet();
      exists = rs.next();

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return exists;
  }
  
  
  @SuppressWarnings("resource")
  public List<ConfigurationParameter> readConfigurationParameters(final Transaction transaction) throws DataAccessException {
    Connection connection = getOracleConnection(transaction);
    List<ConfigurationParameter> parameters = null;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_CONFIGURATION_PARAMETERS_PROC, READ_CONFIGURATION_PARAMETERS_PARAM, true);) {
      
      proc.execute();
      
      try (ResultSet rs = proc.getResultSet();) {
        parameters = new ArrayList<>();
  
        while (rs.next()) {
          ConfigurationParameter parameter = new ConfigurationParameter();  
          parameter.setId(getInteger(rs, "configuration_parameter_id"));
          parameter.setEstablishedDate(getDate(rs, "when_created"));
          parameter.setName(getString(rs, "parameter_name"));
          parameter.setValue(getString(rs, "parameter_value"));
          parameter.setSensitiveDataInd(getIndicator(rs, "sensitive_data_ind"));
          parameter.setType(getString(rs, "config_param_type_code"));
          parameter.setRevisionCount(getInteger(rs, "revision_count"));
          parameter.setTypeDescription(getString(rs, "description"));
          parameters.add(parameter);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return parameters;    
  }
  
  
  @SuppressWarnings("resource")
  public ConfigurationParameter readConfigurationParameter(final Transaction transaction, Integer id, String name) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    ConfigurationParameter parameter = new ConfigurationParameter();
    int param = 1;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_CONFIGURATION_PARAMETER_PROC, READ_CONFIGURATION_PARAMETER_PARAM, true);
      
      proc.setInt(param++, id);
      proc.setString(param++, name);
      
      proc.execute();
      rs = proc.getResultSet();

      while (rs.next()) {
        parameter.setId(getInteger(rs, "configuration_parameter_id"));
        parameter.setEstablishedDate(getDate(rs, "when_created"));
        parameter.setName(getString(rs, "parameter_name"));
        parameter.setValue(getString(rs, "parameter_value"));
        parameter.setSensitiveDataInd(getIndicator(rs, "sensitive_data_ind"));
        parameter.setType(getString(rs, "config_param_type_code"));
        parameter.setTypeDescription(getString(rs, "description"));
        parameter.setRevisionCount(getInteger(rs, "revision_count"));
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return parameter;
  }     
  
  
  @SuppressWarnings("resource")
  public List<YearConfigurationParameter> readYearConfigurationParams(Transaction transaction,
      Integer programYear) throws DataAccessException {
    Connection connection = getOracleConnection(transaction);
    List<YearConfigurationParameter> parameters = null;
    int param = 1;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + READ_YEAR_CONFIGURATION_PARAMS_PROC, READ_YEAR_CONFIGURATION_PARAMS_PARAM, true);) {
      
      proc.setInt(param++, programYear);
      proc.execute();
      
      try (ResultSet rs = proc.getResultSet();) {
        parameters = new ArrayList<>();
        
        while (rs.next()) {
          YearConfigurationParameter parameter = new YearConfigurationParameter();  
          parameter.setId(getInteger(rs, "year_configuration_param_id"));
          parameter.setProgramYear(getInteger(rs, "program_year"));
          parameter.setEstablishedDate(getDate(rs, "when_created"));
          parameter.setName(getString(rs, "parameter_name"));
          parameter.setValue(getString(rs, "parameter_value"));
          parameter.setType(getString(rs, "config_param_type_code"));
          parameter.setRevisionCount(getInteger(rs, "revision_count"));
          parameter.setTypeDescription(getString(rs, "description"));
          parameters.add(parameter);
        }
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return parameters;    
  }
  
  
  @SuppressWarnings("resource")
  public YearConfigurationParameter readYearConfigurationParam(Transaction transaction, Integer id) throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    YearConfigurationParameter parameter = new YearConfigurationParameter();
    int param = 1;
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_YEAR_CONFIGURATION_PARAM_PROC, READ_YEAR_CONFIGURATION_PARAM_PARAM, true);
      
      proc.setInt(param++, id);
      
      proc.execute();
      rs = proc.getResultSet();
      
      while (rs.next()) {
        parameter.setId(getInteger(rs, "year_configuration_param_id"));
        parameter.setProgramYear(getInteger(rs, "program_year"));
        parameter.setName(getString(rs, "parameter_name"));
        parameter.setValue(getString(rs, "parameter_value"));
        parameter.setType(getString(rs, "config_param_type_code"));
        parameter.setEstablishedDate(getDate(rs, "when_created"));
        parameter.setTypeDescription(getString(rs, "description"));
        parameter.setRevisionCount(getInteger(rs, "revision_count"));
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return parameter;
  }     

  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List of type Code
   */
  @SuppressWarnings("resource")
  public List<FarmType3> readFarmType3Codes(final Transaction transaction) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<FarmType3> codes = null;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FARM_TYPE_3_CODES_PROC, READ_FARM_TYPE_3_CODES_PARAM, true);
      
      proc.execute();
      rs = proc.getResultSet();
      codes = new ArrayList<>();

      while (rs.next()) {
        String itemCode = getString(rs, "code");
        FarmType3 item = new FarmType3(itemCode);
        item.setEstablishedDate(getDate(rs, "when_created"));
        item.setRevisionCount(getInteger(rs, "revision_count"));
        item.setFarmTypeId(getInteger(rs, "tip_farm_type_3_lookup_id"));
        
        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }
  
  @SuppressWarnings("resource")
  public boolean checkFarmType3InUse(final FarmType3 farmType, final Transaction transaction) throws DataAccessException {
    boolean inUse = false;
  
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CHECK_FARM_TYPE_3_IN_USE_PROC, CHECK_FARM_TYPE_3_IN_USE_PARAM, true);
      
      proc.setInt(param++, farmType.getFarmTypeId());
      
      proc.execute();
      rs = proc.getResultSet();
      inUse = rs.next();

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }    
    
    return inUse;
  }
  
  
  @SuppressWarnings("resource")
  public List<TipFarmTypeIncomeRange> readFarmType3IncomeRange(Transaction transaction, Integer farmType3Id) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    List<TipFarmTypeIncomeRange> defaultRange = new ArrayList<>();
    List<TipFarmTypeIncomeRange> customRange = new ArrayList<>();
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FARM_TYPE_3_INCOME_RANGE_PROC, READ_FARM_TYPE_3_INCOME_RANGE_PARAM, true);
      
      proc.setInt(param++, farmType3Id);
      
      proc.execute();
      rs = proc.getResultSet();

      while (rs.next()) {
        TipFarmTypeIncomeRange range = new TipFarmTypeIncomeRange();
        range.setRangeHigh(getDouble(rs, "Range_High"));
        range.setRangeLow(getDouble(rs, "Range_Low"));
        range.setMinimumGroupCount(getInteger(rs, "Minimum_Group_Count"));
        
        if (getInteger(rs, "Tip_Farm_Type_3_Lookup_Id") == null) {
          range.setIsDefault(true);
          defaultRange.add(range);
        } else {
          range.setIsDefault(false);
          customRange.add(range);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    if (customRange.size() == 0) {
      return defaultRange;
    }
    
    return customRange;
  }
  
  
  @SuppressWarnings("resource")
  public List<TipFarmTypeIncomeRange> readFarmType2IncomeRange(Transaction transaction, Integer farmType2Id, Integer farmType3Id) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    List<TipFarmTypeIncomeRange> defaultRange = new ArrayList<>();
    List<TipFarmTypeIncomeRange> inheritedRange = new ArrayList<>();
    List<TipFarmTypeIncomeRange> customRange = new ArrayList<>();
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FARM_TYPE_2_INCOME_RANGE_PROC, READ_FARM_TYPE_2_INCOME_RANGE_PARAM, true);
      
      proc.setInt(param++, farmType2Id);
      proc.setInt(param++, farmType3Id);
      
      proc.execute();
      rs = proc.getResultSet();

      while (rs.next()) {
        TipFarmTypeIncomeRange range = new TipFarmTypeIncomeRange();
        range.setRangeHigh(getDouble(rs, "Range_High"));
        range.setRangeLow(getDouble(rs, "Range_Low"));
        range.setMinimumGroupCount(getInteger(rs, "Minimum_Group_Count"));
        
        if (getInteger(rs, "Tip_Farm_Type_2_Lookup_Id") != null) {
          range.setIsDefault(false);
          range.setIsInherited(false);
          range.setInheritedFromId(getInteger(rs, "Tip_Farm_Type_2_Lookup_Id"));
          customRange.add(range);
        } else if (getInteger(rs, "Tip_Farm_Type_3_Lookup_Id") != null) {
          range.setIsDefault(false);
          range.setIsInherited(true);
          range.setInheritedFromId(getInteger(rs, "Tip_Farm_Type_3_Lookup_Id"));
          inheritedRange.add(range);
        } else {
          range.setIsDefault(true);
          range.setIsInherited(false);
          defaultRange.add(range);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    if (customRange.size() != 0) {
      return customRange;
    } else if (inheritedRange.size() != 0) {
      return inheritedRange;
    }
    return defaultRange;
  }
  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List of type Code
   */
  @SuppressWarnings("resource")
  public List<TipFarmTypeIncomeRange> readFarmTypeDefaultIncomeRange(final Transaction transaction) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    
    List<TipFarmTypeIncomeRange> defaultRange = new ArrayList<>();
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FARM_TYPE_DEFAULT_INCOME_RANGE_PROC, READ_FARM_TYPE_DEFAULT_INCOME_RANGE_PARAM, true);
      
      proc.execute();
      rs = proc.getResultSet();

      while (rs.next()) {
        TipFarmTypeIncomeRange range = new TipFarmTypeIncomeRange();
        range.setRangeHigh(getDouble(rs, "Range_High"));
        range.setRangeLow(getDouble(rs, "Range_Low"));
        range.setMinimumGroupCount(getInteger(rs, "Minimum_Group_Count"));
        range.setIsDefault(true);
        range.setIsInherited(false);
        
        defaultRange.add(range);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return defaultRange;
  }
  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List of type Code
   */
  @SuppressWarnings("resource")
  public List<TipFarmTypeIncomeRange> readFarmType1IncomeRange(
      Transaction transaction, 
      Integer farmType1Id, 
      Integer farmType2Id,
      Integer farmType3Id)
          throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    List<TipFarmTypeIncomeRange> defaultRange = new ArrayList<>();
    List<TipFarmTypeIncomeRange> inheritedRangeFarmType2 = new ArrayList<>();
    List<TipFarmTypeIncomeRange> inheritedRangeFarmType3 = new ArrayList<>();
    List<TipFarmTypeIncomeRange> customRange = new ArrayList<>();
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FARM_TYPE_1_INCOME_RANGE_PROC, READ_FARM_TYPE_1_INCOME_RANGE_PARAM, true);
      
      proc.setInt(param++, farmType1Id);
      proc.setInt(param++, farmType2Id);
      proc.setInt(param++, farmType3Id);
      
      proc.execute();
      rs = proc.getResultSet();

      while (rs.next()) {
        TipFarmTypeIncomeRange range = new TipFarmTypeIncomeRange();
        range.setRangeHigh(getDouble(rs, "Range_High"));
        range.setRangeLow(getDouble(rs, "Range_Low"));
        range.setMinimumGroupCount(getInteger(rs, "Minimum_Group_Count"));
        
        if (getInteger(rs, "Tip_Farm_Type_1_Lookup_Id") != null) {
          range.setIsDefault(false);
          range.setIsInherited(false);
          customRange.add(range);
        } else if (getInteger(rs, "Tip_Farm_Type_2_Lookup_Id") != null) {
          range.setIsDefault(false);
          range.setIsInherited(true);
          range.setInheritedFromId(getInteger(rs, "Tip_Farm_Type_2_Lookup_Id"));
          inheritedRangeFarmType2.add(range);
        } else if (getInteger(rs, "Tip_Farm_Type_3_Lookup_Id") != null){
          range.setIsDefault(false);
          range.setIsInherited(true);
          range.setInheritedFromId(getInteger(rs, "Tip_Farm_Type_3_Lookup_Id"));
          inheritedRangeFarmType3.add(range);
        } else {
          range.setIsDefault(true);
          range.setIsInherited(false);
          defaultRange.add(range);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    if (customRange.size() != 0) {
      return customRange;
    } else if (inheritedRangeFarmType2.size() != 0) {
      return inheritedRangeFarmType2;
    } else if (inheritedRangeFarmType3.size() != 0) {
      return inheritedRangeFarmType3;
    }
    return defaultRange;
  }
  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List of type Code
   */
  @SuppressWarnings("resource")
  public List<FarmSubtype> readFarmType2Codes(final Transaction transaction) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<FarmSubtype> codes = null;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FARM_TYPE_2_CODES_PROC, READ_FARM_TYPE_2_CODES_PARAM, true);
      
      proc.execute();
      rs = proc.getResultSet();
      codes = new ArrayList<>();

      while (rs.next()) {
        FarmSubtype item = new FarmSubtype();
        item.setName(getString(rs, "farm_type_2_name"));
        item.setEstablishedDate(getDate(rs, "when_created"));
        item.setRevisionCount(getInteger(rs, "revision_count"));
        item.setParentName(getString(rs, "farm_type_3_name"));
        item.setParentId(getInteger(rs, "tip_farm_type_3_lookup_id"));
        item.setId(getInteger(rs, "tip_farm_type_2_lookup_id"));
        
        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }
  
  @SuppressWarnings("resource")
  public boolean checkFarmType2InUse(
      final FarmSubtype farmSubtype, 
      final Transaction transaction) 
      throws DataAccessException {
    
    boolean inUse = false;
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CHECK_FARM_TYPE_2_IN_USE_PROC, CHECK_FARM_TYPE_2_IN_USE_PARAM, true);
      
      proc.setInt(param++, farmSubtype.getId());
      
      proc.execute();
      rs = proc.getResultSet();
      inUse = rs.next();

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }    
    
    return inUse;    
  }

  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List of type Code
   */
  @SuppressWarnings("resource")
  public List<FarmSubtype> readFarmType1ItemCodes(final Transaction transaction) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    
    List<FarmSubtype> codes = null;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FARM_TYPE_1_CODES_PROC, READ_FARM_TYPE_1_CODES_PARAM, true);
      
      proc.execute();
      rs = proc.getResultSet();
      codes = new ArrayList<>();

      while (rs.next()) {
        FarmSubtype item = new FarmSubtype();
        item.setName(getString(rs, "farm_type_1_name"));
        item.setEstablishedDate(getDate(rs, "when_created"));
        item.setRevisionCount(getInteger(rs, "revision_count"));
        item.setId(getInteger(rs, "tip_farm_type_1_lookup_id"));
        item.setParentId(getInteger(rs, "tip_farm_type_2_lookup_id"));
        item.setParentName(getString(rs, "farm_type_2_name"));
        item.setSecondaryParentId(getInteger(rs, "tip_farm_type_3_lookup_id"));
        item.setSecondaryParentName(getString(rs, "farm_type_3_name"));
        
        codes.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return codes;
  }
  
  @SuppressWarnings("resource")
  public boolean checkFarmType1InUse(
      final FarmSubtype farmSubtype, 
      final Transaction transaction) 
      throws DataAccessException {
    
    boolean inUse = false;
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CHECK_FARM_TYPE_1_IN_USE_PROC, CHECK_FARM_TYPE_1_IN_USE_PARAM, true);
      
      proc.setInt(param++, farmSubtype.getId());
      
      proc.execute();
      rs = proc.getResultSet();
      inUse = rs.next();

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }    
    
    return inUse;    
  }  
  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return List of type TipLineItem
   */
  @SuppressWarnings("resource")
  public List<TipLineItem> readTipLineItems(final Transaction transaction) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<TipLineItem> lineItems = new ArrayList<>();
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FARM_TIP_LINE_ITEMS_PROC, READ_FARM_TIP_LINE_ITEMS_PARAM, true);
      
      proc.execute();
      rs = proc.getResultSet();

      while (rs.next()) {
        TipLineItem item = new TipLineItem();
        item.setId(getInteger(rs, "tip_line_item_id"));
        item.setLineItem(getInteger(rs, "line_item"));
        item.setIsUsedInOpCost(getIndicator(rs, "operating_cost_ind"));
        item.setIsUsedInDirectExpense(getIndicator(rs, "direct_expense_ind"));
        item.setIsUsedInMachineryExpense(getIndicator(rs, "machinery_expense_ind"));
        item.setIsUsedInLandAndBuildingExpense(getIndicator(rs, "land_and_building_expense_ind"));
        item.setIsUsedInOverheadExpense(getIndicator(rs, "overhead_expense_ind"));
        item.setIsProgramPaymentForTips(getIndicator(rs, "program_payment_for_tips_ind"));
        item.setIsOther(getIndicator(rs, "other_ind"));
        item.setFarmSubtypeBLookupId(getInteger(rs, "tip_farm_type_1_lookup_id"));
        item.setFarmSubtypeBName(getString(rs, "farm_type_1_name"));
        item.setDescription(getString(rs, "description"));
        lineItems.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return lineItems;
  }
  
  /**
   * @param transaction Transaction
   * @throws DataAccessException On Exception
   * 
   * @return TipLineItem
   */
  @SuppressWarnings("resource")
  public TipLineItem readTipLineItem(final Transaction transaction, Integer id) throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    TipLineItem lineItem = new TipLineItem();
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_FARM_TIP_LINE_ITEM_PROC, READ_FARM_TIP_LINE_ITEM_PARAM, true);
      
      proc.setInt(1, id);
      proc.execute();
      rs = proc.getResultSet();

      while (rs.next()) {
        lineItem.setId(getInteger(rs, "tip_line_item_id"));
        lineItem.setLineItem(getInteger(rs, "line_item"));
        lineItem.setIsUsedInOpCost(getIndicator(rs, "operating_cost_ind"));
        lineItem.setIsUsedInDirectExpense(getIndicator(rs, "direct_expense_ind"));
        lineItem.setIsUsedInMachineryExpense(getIndicator(rs, "machinery_expense_ind"));
        lineItem.setIsUsedInLandAndBuildingExpense(getIndicator(rs, "land_and_building_expense_ind"));
        lineItem.setIsUsedInOverheadExpense(getIndicator(rs, "overhead_expense_ind"));
        lineItem.setIsProgramPaymentForTips(getIndicator(rs, "program_payment_for_tips_ind"));
        lineItem.setIsOther(getIndicator(rs, "other_ind"));
        lineItem.setFarmSubtypeBLookupId(getInteger(rs, "tip_farm_type_1_lookup_id"));
        lineItem.setFarmSubtypeBName(getString(rs, "farm_type_1_name"));
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return lineItem;
  }
  
  
  @SuppressWarnings("resource")
  public List<TipBenchmarkInfo> readTipBenchmarkInfos(Transaction transaction) throws DataAccessException {
    Connection connection = getOracleConnection(transaction);
    List<TipBenchmarkInfo> parameters = new ArrayList<>();
    
    try {
      
      final int paramCount = 0;
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_TIP_BENCHMARK_INFO_PROC, paramCount, true); ) {
        
        
        proc.execute();
        
        try(ResultSet rs = proc.getResultSet();) {
          
          while (rs.next()) {
            TipBenchmarkInfo benchmarkInfo = new TipBenchmarkInfo();      
            benchmarkInfo.setYear(getInteger(rs, "Year"));
            benchmarkInfo.setOperationCount(getInteger(rs, "Operation_Count"));
            benchmarkInfo.setGeneratedDate(getDate(rs, "Generated_Date"));
            parameters.add(benchmarkInfo);
          }
        }
        
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return parameters;    
  }
  
  @SuppressWarnings("resource")
  public boolean checkTipLineItemExists(
      final Integer lineItem, 
      final Transaction transaction) 
      throws DataAccessException {
    
    boolean inUse = false;
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    int param = 1;
    ResultSet rs = null;
    
    try {
      
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CHECK_TIP_LINE_ITEM_EXISTS_PROC, CHECK_TIP_LINE_ITEM_EXISTS_PARAM, true);
      
      proc.setInt(param++, lineItem);
      
      proc.execute();
      rs = proc.getResultSet();
      inUse = rs.next();

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }    
    
    return inUse;
  }
  
  
  @SuppressWarnings("resource")
  public List<DocumentTemplate> readDocumentTemplates(final Transaction transaction, String templateName) throws DataAccessException {
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<DocumentTemplate> parameters = null;
    int param = 1;
    
    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_DOCUMENT_TEMPLATES_PROC, READ_DOCUMENT_TEMPLATES_PARAM, true);
      
      proc.setString(param++, templateName);
      
      proc.execute();
      rs = proc.getResultSet();
      parameters = new ArrayList<>();

      while (rs.next()) {
        DocumentTemplate docuemntTemplate = new DocumentTemplate();      
        docuemntTemplate.setTemplateName(getString(rs, "template_name"));
        docuemntTemplate.setTemplateContent(getString(rs, "template_content"));
        parameters.add(docuemntTemplate);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return parameters;    
  }


  @SuppressWarnings("resource")
  public List<SectorCode> readSectorCodes(final Transaction transaction,
      final String code)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    List<SectorCode> codes = new ArrayList<>();
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + READ_SECTOR_CODES_PROC, READ_SECTOR_PARAM, true);) {

      int param = 1;
      proc.setString(param++, code);
      proc.execute();

      try(ResultSet rs = proc.getResultSet(); ) {
        
        while (rs.next()) {
          SectorCode item = new SectorCode();
          item.setCode(getString(rs, "sector_code"));
          item.setDescription(getString(rs, "description"));
          item.setEstablishedDate(getDate(rs, "established_date"));
          item.setExpiryDate(getDate(rs, "expiry_date"));
          item.setRevisionCount(getInteger(rs, "revision_count"));
          
          codes.add(item);
        }
        
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return codes;
  }
  
  
  @SuppressWarnings("resource")
  public List<SectorDetailCode> readSectorDetailCodes(final Transaction transaction,
      final String code)
          throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    List<SectorDetailCode> codes = new ArrayList<>();
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + READ_SECTOR_DETAIL_CODES_PROC, READ_SECTOR_DETAIL_PARAM, true);) {
      
      int param = 1;
      proc.setString(param++, code);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet(); ) {
        
        while (rs.next()) {
          SectorDetailCode item = new SectorDetailCode();
          item.setSectorCode(getString(rs, "sector_code"));
          item.setSectorCodeDescription(getString(rs, "sector_code_desc"));
          item.setSectorDetailCode(getString(rs, "sector_detail_code"));
          item.setDescription(getString(rs, "sector_detail_code_desc"));
          item.setEstablishedDate(getDate(rs, "established_date"));
          item.setExpiryDate(getDate(rs, "expiry_date"));
          item.setRevisionCount(getInteger(rs, "revision_count"));
          
          codes.add(item);
        }
        
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return codes;
  }
}
