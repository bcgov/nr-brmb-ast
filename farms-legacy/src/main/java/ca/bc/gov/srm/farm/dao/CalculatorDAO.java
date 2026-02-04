/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationImportOption;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.NewParticipant;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.ProductionInsurance;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.enrolment.EnwEnrolment;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * @author awilkinson
 */
public class CalculatorDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_CALCULATOR_PKG";
  

  private static final String UPDATE_SCENARIO_REVISION_COUNT_PROC = "UPDATE_SC_REV";
  private static final int UPDATE_SCENARIO_REVISION_COUNT_PARAM = 3;

  private static final String UPDATE_CLIENT_PROC = "UPDATE_CLIENT";
  private static final int UPDATE_CLIENT_PARAM = 8;
  
  private static final String UPDATE_PROGRAM_YEAR_VERSION_PROC = "UPDATE_PROGRAM_YEAR_VERSION";
  private static final int UPDATE_PROGRAM_YEAR_VERSION_PARAM = 27;
  
  private static final String GET_NEW_OP_SCHEDULE_PROC = "GET_NEW_OP_SCHEDULE";
  private static final int GET_NEW_OP_SCHEDULE_PARAM = 1;
  
  private static final String CREATE_OPERATION_PROC = "CREATE_OPERATION";
  private static final int CREATE_OPERATION_PARAM = 24;
  
  private static final String UPDATE_OPERATION_PROC = "UPDATE_OPERATION";
  private static final int UPDATE_OPERATION_PARAM = 23;
  
  private static final String DELETE_OPERATION_PROC = "DELETE_OPERATION";
  private static final int DELETE_OPERATION_PARAM = 5;
  
  private static final String UPDATE_PRODUCTION_INSURANCE_PROC = "UPDATE_PI";
  private static final int UPDATE_PRODUCTION_INSURANCE_PARAM = 3;
  
  private static final String UPDATE_SCENARIO_PROC = "UPDATE_SCENARIO";
  private static final int UPDATE_SCENARIO_PARAM = 8;
  
  private static final String UPDATE_PARTICIPANT_DATA_SRC_PROC = "UPDATE_SCENARIO_PARTICIPNT_DATA_SRC_CODE";
  private static final int UPDATE_PARTICIPANT_DATA_SRC_PARAM = 3;
  
  private static final String READ_OPERATIONS_FOR_PUC_IMPORT_PROC = "READ_OPERATIONS_FOR_PUC_IMPORT";
  
  private static final String READ_OPERATIONS_FOR_INVENTORY_IMPORT_PROC = "READ_OPERATIONS_FOR_INVENTORY_IMPORT";
  
  private static final String READ_OPERATIONS_FOR_ACCRUAL_IMPORT_PROC = "READ_OPERATIONS_FOR_ACCRUAL_IMPORT";
  
  private static final String SAVE_SCENARIO_AS_NEW_PROC = "SAVE_SCENARIO_AS_NEW";
  
  private static final String CREATE_REFERENCE_SCENARIO_PROC = "CREATE_REF_SCENARIO";
  private static final int CREATE_REFERENCE_SCENARIO_PARAM = 5;
  
  private static final String ASSIGN_TO_USER_PROC = "ASSIGN_TO_USER";
  private static final int ASSIGN_TO_USER_PARAM = 4;

  private static final String CREATE_YEAR_PROC = "CREATE_YEAR";
  private static final String UPDATE_SCENARIO_PYV_PROC = "UPDATE_SC_PYV";
  private static final int UPDATE_SCENARIO_PYV_PARAM = 5;
  
  private static final String UPDATE_OPERATION_SCHEDULE_PROC = "UPDATE_OP_SCHEDULE";
  private static final int UPDATE_OPERATION_SCHEDULE_PARAM = 4;

  private static final String ADD_SCENARIO_LOG_PROC = "SC_LOG";
  private static final int ADD_SCENARIO_LOG_PARAM = 3;
  
  private static final String DELETE_BPU_XREFS_PROC = "delete_bpu_xrefs";
  
  private static final String SAVE_BPU_XREF_PROC = "save_bpu_xref";
  
  private static final String PIN_EXISTS_PROC = "PIN_EXISTS";
  private static final int PIN_EXISTS_PARAM = 1;
  
  private static final String PIN_CHECKED_OUT_BY_USER_PROC = "PIN_CHECKED_OUT_BY_USER";
  private static final int PIN_CHECKED_OUT_BY_USER_PARAM = 2;
  
  private static final String MATCHING_SC_EXISTS_PROC = "MATCHING_SC_EXISTS";
  private static final int MATCHING_SC_EXISTS_PARAM = 4;
  
  private static final String CF_HAS_ACCOUNTING_CODE_ERROR_PROC = "CF_HAS_ACCOUNTING_CODE_ERROR";
  private static final int CF_HAS_ACCOUNTING_CODE_ERROR_PARAM = 3;
  
  private static final String CF_MATCHES_VERIFIED_PROC = "CF_MATCHES_VERIFIED";
  private static final int CF_MATCHES_VERIFIED_PARAM = 2;
  
  private static final String CF_REF_YEARS_MISMATCH_ERROR_PROC = "CF_REF_YEARS_MISMATCH_ERROR";
  private static final int CF_REF_YEARS_MISMATCH_ERROR_PARAM = 3;
  
  private static final String COMBINED_FARM_ADD_PROC = "COMBINED_FARM_ADD";
  private static final int COMBINED_FARM_ADD_PARAM = 4;
  
  private static final String COMBINED_FARM_REMOVE_PROC = "COMBINED_FARM_REMOVE";
  private static final int COMBINED_FARM_REMOVE_PARAM = 3;
  
  private static final String COMBINED_FARM_UPDATE_PROC = "COMBINED_FARM_UPDATE";
  private static final int COMBINED_FARM_UPDATE_PARAM = 5;
  
  private static final String UPDATE_PIN_REV_COUNTS_PROC = "UPDATE_PIN_REV_COUNTS";
  
  private static final String UPDATE_CASH_MARGINS_IND_PROC = "UPDATE_CASH_MARGINS_IND";
  private static final int UPDATE_CASH_MARGINS_IND_PARAM = 4;

  private static final String UPDATE_NON_PARTICIPANT_IND_PROC = "UPDATE_NON_PARTICIPANT_IND";
  private static final int UPDATE_NON_PARTICIPANT_IND_PARAM = 4;
  
  private static final String UPDATE_LATE_PARTICIPANT_IND_PROC = "UPDATE_LATE_PARTICIPANT_IND";
  private static final int UPDATE_LATE_PARTICIPANT_IND_PARAM = 4;

  private static final String UPDATE_FARM_TYPE_PROC = "UPDATE_FARM_TYPE";

  private static final String UPDATE_PY_LOCAL_RECEIVED_DATES_PROC = "UPDATE_PY_LOCAL_RECEIVED_DATES";
  
  private static final String CREATE_ENW_ENROLMENT_PROC = "CREATE_ENW_ENROLMENT";
  private static final int CREATE_ENW_ENROLMENT_PARAM = 44;
  
  private static final String UPDATE_ENW_ENROLMENT_PROC = "UPDATE_ENW_ENROLMENT";
  private static final int UPDATE_ENW_ENROLMENT_PARAM = 45;
  
  private static final String CREATE_PERSON_PROC = "CREATE_PERSON";
  private static final int CREATE_PERSON_PARAM = 15;
  
  private static final String UPDATE_PERSON_PROC = "UPDATE_PERSON";
  private static final int UPDATE_PERSON_PARAM = 17;
  
  private static final String CREATE_CLIENT_PROC = "CREATE_CLIENT";
  private static final int CREATE_CLIENT_PARAM = 8;
  
  private static final String CREATE_PY_PROC = "CREATE_PY";
  private static final int CREATE_PY_PARAM = 3;
  
  private static final String CREATE_PYV_PROC = "CREATE_PYV";
  private static final int CREATE_PYV_PARAM = 3;
  
  private static final String CREATE_SCENARIO_PROC = "CREATE_SCENARIO";
  
  private static final String CREATE_PARTNER_PROC = "CREATE_PARTNER";
  private static final int CREATE_PARTNER_PARAM = 8;
  
  private static final String UPDATE_PARTNER_PROC = "UPDATE_PARTNER";
  private static final int UPDATE_PARTNER_PARAM = 10;
  
  private static final String DELETE_PARTNER_PROC = "DELETE_PARTNER";
  private static final int DELETE_PARTNER_PARAM = 2;
  
  private static final String READ_ALL_PARTNERS_PROC = "READ_ALL_PARTNERS";
  private static final int READ_ALL_PARTNERS_PARAM = 0;
  
  private static final String COPY_SCENARIO_PYV_PROC = "COPY_SCENARIO_PYV";
  
  private static final String UPSERT_SCENARIO_CONFIG_PARAM_PROC = "UPSERT_SCENARIO_CONFIG_PARAM";
  
  private static final String COPY_FORWARD_YEAR_CONFIG_PROC = "COPY_FORWARD_YEAR_CONFIG";
  private static final int COPY_FORWARD_YEAR_CONFIG_PARAM = 2;
  
  private static final String DELETE_USER_SCENARIO_PROC = "DELETE_USER_SCENARIO";
  public void updateClient(final Transaction transaction,
      final Client client,
      final String user)
      throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_CLIENT_PROC, UPDATE_CLIENT_PARAM, false);

      int param = 1;
      proc.setInt(param++, client.getClientId());
      proc.setString(param++, client.getParticipantClassCode());
      proc.setInt(param++, client.getParticipantPin());
      proc.setString(param++, client.getSin());
      proc.setString(param++, client.getBusinessNumber());
      proc.setString(param++, client.getTrustNumber());
      proc.setInt(param++, client.getRevisionCount());
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
   * @param farmingYear FarmingYear
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void updateFarmingYear(final Transaction transaction,
      final FarmingYear farmingYear,
      final String user)
      throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_PROGRAM_YEAR_VERSION_PROC, UPDATE_PROGRAM_YEAR_VERSION_PARAM, false);

      int param = 1;
      proc.setInt(param++, farmingYear.getProgramYearVersionId());
      proc.setInt(param++, farmingYear.getFarmYears());
      proc.setInt(param++, farmingYear.getCommonShareTotal());
      proc.setString(param++, String.valueOf(farmingYear.getAgristabFedStsCode()));
      proc.setString(param++, farmingYear.getMunicipalityCode());
      proc.setString(param++, farmingYear.getParticipantProfileCode());
      proc.setString(param++, farmingYear.getProvinceOfResidence());
      proc.setString(param++, farmingYear.getProvinceOfMainFarmstead());
      proc.setString(param++, farmingYear.getOtherText());
      proc.setString(param++, getIndicatorYN(farmingYear.getIsAccrualCashConversion()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsAccrualWorksheet()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsCanSendCobToRep()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsCombinedFarm()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsCompletedProdCycle()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsCoopMember()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsCorporateShareholder()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsCwbWorksheet()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsDisaster()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsLastYearFarming()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsPartnershipMember()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsPerishableCommodities()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsReceipts()));
      proc.setString(param++, getIndicatorYN(farmingYear.getIsSoleProprietor()));
      proc.setDate(param++, farmingYear.getPostMarkDate());
      proc.setDate(param++, farmingYear.getCraStatementAReceivedDate());
      proc.setInt(param++, farmingYear.getRevisionCount());
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
   * @param clientId clientId
   * @return schedule
   * @throws DataAccessException On Exception 
   */
  @SuppressWarnings("resource")
  public String getNewOperationSchedule(
      final Transaction transaction,
      final Integer clientId)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    String schedule = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + GET_NEW_OP_SCHEDULE_PROC, GET_NEW_OP_SCHEDULE_PARAM, Types.VARCHAR);
      
      int param = 1;
      proc.setInt(param++, clientId);
      proc.execute();
      
      schedule = proc.getString(1);
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return schedule;
  }
  
  
  @SuppressWarnings("resource")
  public void createFarmingOperation(
      final Transaction transaction,
      final FarmingOperation fo,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    Integer scenarioId = null;
    
    if(fo.getFarmingYear().getReferenceScenario() != null
        && fo.getFarmingYear().getReferenceScenario().getParentScenario() != null) {
      scenarioId = fo.getFarmingYear().getReferenceScenario().getParentScenario().getScenarioId();
    }
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_OPERATION_PROC, CREATE_OPERATION_PARAM, Types.INTEGER);) {
      
      int param = 1;
      proc.setInt(param++, fo.getFarmingYear().getProgramYearVersionId());
      proc.setInt(param++, scenarioId);
      proc.setInt(param++, fo.getOperationNumber());
      proc.setString(param++, fo.getSchedule());
      proc.setString(param++, fo.getAccountingCode());
      proc.setDate(param++, fo.getFiscalYearStart());
      proc.setDate(param++, fo.getFiscalYearEnd());
      proc.setInt(param++, fo.getPartnershipPin());
      proc.setString(param++, fo.getPartnershipName());
      proc.setDouble(param++, fo.getPartnershipPercent());
      proc.setString(param++, getIndicatorYN(fo.getIsCropDisaster()));
      proc.setString(param++, getIndicatorYN(fo.getIsCropShare()));
      proc.setString(param++, getIndicatorYN(fo.getIsFeederMember()));
      proc.setString(param++, getIndicatorYN(fo.getIsLandlord()));
      proc.setString(param++, getIndicatorYN(fo.getIsLivestockDisaster()));
      proc.setDouble(param++, fo.getBusinessUseHomeExpense());
      proc.setDouble(param++, fo.getFarmingExpenses());
      proc.setDouble(param++, fo.getGrossIncome());
      proc.setDouble(param++, fo.getInventoryAdjustments());
      proc.setDouble(param++, fo.getNetFarmIncome());
      proc.setDouble(param++, fo.getNetIncomeAfterAdj());
      proc.setDouble(param++, fo.getNetIncomeBeforeAdj());
      proc.setDouble(param++, fo.getOtherDeductions());
      proc.setString(param++, user);
      proc.execute();
      
      fo.setFarmingOperationId(new Integer(proc.getInt(1)));
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param fo fo
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void updateFarmingOperation(
      final Transaction transaction,
      final FarmingOperation fo,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_OPERATION_PROC, UPDATE_OPERATION_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, fo.getFarmingOperationId());
      proc.setInt(param++, fo.getOperationNumber());
      proc.setString(param++, fo.getAccountingCode());
      proc.setDate(param++, fo.getFiscalYearStart());
      proc.setDate(param++, fo.getFiscalYearEnd());
      proc.setInt(param++, fo.getPartnershipPin());
      proc.setString(param++, fo.getPartnershipName());
      proc.setDouble(param++, fo.getPartnershipPercent());
      proc.setString(param++, getIndicatorYN(fo.getIsCropDisaster()));
      proc.setString(param++, getIndicatorYN(fo.getIsCropShare()));
      proc.setString(param++, getIndicatorYN(fo.getIsFeederMember()));
      proc.setString(param++, getIndicatorYN(fo.getIsLandlord()));
      proc.setString(param++, getIndicatorYN(fo.getIsLivestockDisaster()));
      proc.setDouble(param++, fo.getBusinessUseHomeExpense());
      proc.setDouble(param++, fo.getFarmingExpenses());
      proc.setDouble(param++, fo.getGrossIncome());
      proc.setDouble(param++, fo.getInventoryAdjustments());
      proc.setDouble(param++, fo.getNetFarmIncome());
      proc.setDouble(param++, fo.getNetIncomeAfterAdj());
      proc.setDouble(param++, fo.getNetIncomeBeforeAdj());
      proc.setDouble(param++, fo.getOtherDeductions());
      proc.setInt(param++, fo.getRevisionCount());
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
   * @param farmingOperationId farmingOperationId
   * @param programYearVersionId programYearVersionId
   * @param scenarioId scenarioId
   * @param operationRevisionCount operationRevisionCount
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void deleteFarmingOperation(
      final Transaction transaction,
      final Integer farmingOperationId,
      final Integer programYearVersionId,
      final Integer scenarioId,
      final Integer operationRevisionCount,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_OPERATION_PROC, DELETE_OPERATION_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, farmingOperationId);
      proc.setInt(param++, programYearVersionId);
      proc.setInt(param++, scenarioId);
      proc.setInt(param++, operationRevisionCount);
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
   * @param fo FarmingOperation
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void updateProductionInsurances(
      final Transaction transaction,
      final FarmingOperation fo,
      final String user)
  throws DataAccessException {

    List<ProductionInsurance> productionInsurances = fo.getProductionInsurances();
    if(productionInsurances != null && ! productionInsurances.isEmpty()) {
      
      List<String> codeList = new ArrayList<>(productionInsurances.size());
      for(ProductionInsurance pi : productionInsurances) {
        codeList.add(pi.getProductionInsuranceNumber());
      }
  
      @SuppressWarnings("resource")
      Connection connection = getOracleConnection(transaction);
      DAOStoredProcedure proc = null;
      
      try {
        proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + UPDATE_PRODUCTION_INSURANCE_PROC, UPDATE_PRODUCTION_INSURANCE_PARAM, false);
  
  
        Array oracleArray = createStringOracleArray(transaction, codeList);
  
        int param = 1;
        proc.setInt(param++, fo.getFarmingOperationId());
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
  }
  

  /**
   * @param transaction transaction
   * @param scenario scenario
   * @param newStateCode newStateCode
   * @param stateChangeReason stateChangeReason
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void updateScenario(
      final Transaction transaction,
      final Scenario scenario,
      final String newStateCode,
      final String stateChangeReason,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_SCENARIO_PROC, UPDATE_SCENARIO_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, scenario.getScenarioId());
      proc.setString(param++, scenario.getDescription());
      proc.setString(param++, newStateCode);
      proc.setString(param++, stateChangeReason);
      proc.setString(param++, scenario.getScenarioCategoryCode());
      proc.setString(param++, getIndicatorYN(scenario.getIsDefaultInd()));
      proc.setInt(param++, scenario.getVerifierUserId());
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
   * @param scenario scenario
   * @param participantDataSrcCode participantDataSrcCode
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void updateScenarioParticipantDataSrcCode(
      final Transaction transaction,
      final Scenario scenario,
      final String participantDataSrcCode,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_PARTICIPANT_DATA_SRC_PROC, UPDATE_PARTICIPANT_DATA_SRC_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, scenario.getScenarioId());
      proc.setString(param++, participantDataSrcCode);
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
   * @param participantPin participantPin
   * @param year year
   * @param excludedProgramYearVersionNumber excludedProgramYearVersionNumber
   * @throws DataAccessException On Exception 
   */
  @SuppressWarnings("resource")
  public  List<FarmingOperationImportOption> readOperationsForProductiveUnitsImport(
      final Transaction transaction,
      final int participantPin,
      final int year,
      final Integer excludedProgramYearVersionNumber)
  throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    List<FarmingOperationImportOption> schedules = new ArrayList<>();

    final int paramCount = 3;
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, 
        PACKAGE_NAME + "." + READ_OPERATIONS_FOR_PUC_IMPORT_PROC, paramCount, true)) {

      int param = 1;
      proc.setInt(param++, participantPin);
      proc.setInt(param++, year);
      proc.setInt(param++, excludedProgramYearVersionNumber);
      proc.execute();

      try (ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          FarmingOperationImportOption op = new FarmingOperationImportOption();
          op.setProgramYearVersion(getInteger(rs, "Program_Year_Version_Number"));
          op.setScenarioNumber(getInteger(rs, "Scenario_Number"));
          op.setScenarioClassCode(getString(rs, "Scenario_Class_Code"));
          op.setScenarioClassDescription(getString(rs, "Scenario_Class_Desc"));
          op.setScenarioCategoryCode(getString(rs, "Scenario_Category_Code"));
          op.setScenarioCategoryDescription(getString(rs, "Scenario_Category_Code_Desc"));
          op.setScenarioStateCode(getString(rs, "Scenario_State_Code"));
          op.setScenarioStateDescription(getString(rs, "Scenario_State_Desc"));
          op.setScenarioCreatedDate(getDate(rs, "When_Created"));
          op.setAlignmentKey(getString(rs, "Alignment_Key"));
          op.setPartnershipPin(getInteger(rs, "Partnership_Pin"));
          op.setPartnershipName(getString(rs, "Partnership_Name"));
          op.setFarminOperationId(getInteger(rs, "Farming_Operation_Id"));
          op.setHasCraProductiveUnits(getIndicator(rs, "Has_CRA_Productive_Units"));
          op.setHasLocalProductiveUnits(getIndicator(rs, "Has_Local_Productive_Units"));
          schedules.add(op);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } 
    return schedules;
  }
  
  /**
   * @param transaction transaction
   * @param participantPin participantPin
   * @param year year
   * @param excludedProgramYearVersionNumber excludedProgramYearVersionNumber
   * @throws DataAccessException On Exception 
   */
  @SuppressWarnings("resource")
  public  List<FarmingOperationImportOption> readOperationsForInventoryImport(
      final Transaction transaction,
      final int participantPin,
      final int year,
      final Integer excludedProgramYearVersionNumber)
  throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    List<FarmingOperationImportOption> schedules = new ArrayList<>();

    final int paramCount = 3;
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, 
        PACKAGE_NAME + "." + READ_OPERATIONS_FOR_INVENTORY_IMPORT_PROC, paramCount, true)) {

      int param = 1;
      proc.setInt(param++, participantPin);
      proc.setInt(param++, year);
      proc.setInt(param++, excludedProgramYearVersionNumber);
      proc.execute();

      try (ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          FarmingOperationImportOption op = new FarmingOperationImportOption();
          op.setProgramYearVersion(getInteger(rs, "Program_Year_Version_Number"));
          op.setScenarioNumber(getInteger(rs, "Scenario_Number"));
          op.setScenarioClassCode(getString(rs, "Scenario_Class_Code"));
          op.setScenarioClassDescription(getString(rs, "Scenario_Class_Desc"));
          op.setScenarioCategoryCode(getString(rs, "Scenario_Category_Code"));
          op.setScenarioCategoryDescription(getString(rs, "Scenario_Category_Code_Desc"));
          op.setScenarioStateCode(getString(rs, "Scenario_State_Code"));
          op.setScenarioStateDescription(getString(rs, "Scenario_State_Desc"));
          op.setScenarioCreatedDate(getDate(rs, "When_Created"));
          op.setAlignmentKey(getString(rs, "Alignment_Key"));
          op.setPartnershipPin(getInteger(rs, "Partnership_Pin"));
          op.setPartnershipName(getString(rs, "Partnership_Name"));
          op.setFarminOperationId(getInteger(rs, "Farming_Operation_Id"));
          schedules.add(op);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } 
    return schedules;
  }


  /**
   * @param transaction transaction
   * @param participantPin participantPin
   * @param year year
   * @param excludedProgramYearVersionNumber excludedProgramYearVersionNumber
   * @throws DataAccessException On Exception 
   */
  @SuppressWarnings("resource")
  public  List<FarmingOperationImportOption> readOperationsForAccrualImport(
      final Transaction transaction,
      final int participantPin,
      final int year,
      final Integer excludedProgramYearVersionNumber)
  throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    List<FarmingOperationImportOption> schedules = new ArrayList<>();

    final int paramCount = 3;
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, 
        PACKAGE_NAME + "." + READ_OPERATIONS_FOR_ACCRUAL_IMPORT_PROC, paramCount, true)) {

      int param = 1;
      proc.setInt(param++, participantPin);
      proc.setInt(param++, year);
      proc.setInt(param++, excludedProgramYearVersionNumber);
      proc.execute();

      try (ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          FarmingOperationImportOption op = new FarmingOperationImportOption();
          op.setProgramYearVersion(getInteger(rs, "Program_Year_Version_Number"));
          op.setScenarioNumber(getInteger(rs, "Scenario_Number"));
          op.setScenarioClassCode(getString(rs, "Scenario_Class_Code"));
          op.setScenarioClassDescription(getString(rs, "Scenario_Class_Desc"));
          op.setScenarioCategoryCode(getString(rs, "Scenario_Category_Code"));
          op.setScenarioCategoryDescription(getString(rs, "Scenario_Category_Code_Desc"));
          op.setScenarioStateCode(getString(rs, "Scenario_State_Code"));
          op.setScenarioStateDescription(getString(rs, "Scenario_State_Desc"));
          op.setScenarioCreatedDate(getDate(rs, "When_Created"));
          op.setAlignmentKey(getString(rs, "Alignment_Key"));
          op.setPartnershipPin(getInteger(rs, "Partnership_Pin"));
          op.setPartnershipName(getString(rs, "Partnership_Name"));
          op.setFarminOperationId(getInteger(rs, "Farming_Operation_Id"));
          schedules.add(op);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } 
    return schedules;
  }


  /**
   * @param transaction transaction
   * @param scenarioId scenarioId
   * @param scenarioRevisionCount scenarioRevisionCount
   * @param user user
   * @param userGuid userGuid
   * @throws DataAccessException On Exception 
   */
  public void assignToUser(
      final Transaction transaction,
      final Integer scenarioId,
      final Integer scenarioRevisionCount,
      final String userGuid,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + ASSIGN_TO_USER_PROC, ASSIGN_TO_USER_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, scenarioId);
      proc.setInt(param++, scenarioRevisionCount);
      proc.setString(param++, userGuid);
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
   * @return Integer scenarioNumber
   */
  @SuppressWarnings("resource")
  public Integer saveScenarioAsNew(
      Transaction transaction,
      Integer scenarioId,
      String scenarioTypeCode,
      String scenarioCategoryCode,
      String applicationVersion,
      String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    return saveScenarioAsNew(connection, scenarioId, scenarioTypeCode, scenarioCategoryCode, applicationVersion, user);
  }


  /**
   * @return Integer scenarioNumber
   */
  public Integer saveScenarioAsNew(
      Connection connection,
      Integer scenarioId,
      String scenarioTypeCode,
      String scenarioCategoryCode,
      String applicationVersion,
      String user)
  throws DataAccessException {
    
    Integer scenarioNumber = null;
    
    final int paramCount = 5;
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + SAVE_SCENARIO_AS_NEW_PROC, paramCount, Types.INTEGER); ) {
      
      int param = 1;
      proc.setInt(param++, scenarioId);
      proc.setString(param++, scenarioTypeCode);
      proc.setString(param++, scenarioCategoryCode);
      proc.setString(param++, applicationVersion);
      proc.setString(param++, user);
      proc.execute();
      
      scenarioNumber = (Integer) proc.getResult();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
    return scenarioNumber;
  }
  
  
  @SuppressWarnings("resource")
  public Integer createReferenceScenario(
      final Transaction transaction,
      final Scenario forScenario,
      final Integer fromScenarioId,
      final String scenarioStateCode,
      final String applicationVersion,
      final String user)
  throws DataAccessException {
    
    Integer newScenarioId = null;
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_REFERENCE_SCENARIO_PROC, CREATE_REFERENCE_SCENARIO_PARAM, Types.INTEGER);
      
      int param = 1;
      proc.setInt(param++, forScenario.getScenarioId());
      proc.setInt(param++, fromScenarioId);
      proc.setString(param++, scenarioStateCode);
      proc.setString(param++, applicationVersion);
      proc.setString(param++, user);
      proc.execute();
      
      newScenarioId = new Integer(proc.getInt(1));
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return newScenarioId;
  }
  
  
  @SuppressWarnings("resource")
  public Integer createYear(
      final Transaction transaction,
      final Integer pin,
      final Integer programYearToCreate,
      final Integer numOperations,
      final String scenarioClassCode,
      final String scenarioCategoryCode,
      final String user) throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    return createYear(connection, pin, programYearToCreate, numOperations, scenarioClassCode, scenarioCategoryCode, user);
  }


  public Integer createYear(
      final Connection connection,
      final Integer pin,
      final Integer programYearToCreate,
      final Integer numOperations,
      final String scenarioClassCode,
      final String scenarioCategoryCode,
      final String user) throws DataAccessException {

    Integer programYearVersionId = null;

    final int paramCount = 6;
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_YEAR_PROC, paramCount, Types.INTEGER); ) {

      int c = 1;

      proc.setInt(c++, pin);
      proc.setInt(c++, programYearToCreate);
      proc.setInt(c++, numOperations);
      proc.setString(c++, scenarioClassCode);
      proc.setString(c++, scenarioCategoryCode);
      proc.setString(c++, user);

      proc.execute();
      programYearVersionId = new Integer(proc.getInt(1));

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
    return programYearVersionId;
  }
  
  
  /**
   * @param transaction transaction
   * @param scenarioId Integer
   * @param newPyvNumber the new program year version number to update the scenario to
   * @param pyvKeepOldData if true, then copy the data from the old PYV to the new PYV
   * @param opNumsKeepOldData if an operation number is in the list,
   *                          then copy the data from the old operation to the new operation
   * @param user String
   * @return Integer scenarioNumber
   * @throws DataAccessException DataAccessException
   */
  @SuppressWarnings("resource")
  public Integer updateScenarioPyVersion(
      final Transaction transaction,
      final Integer scenarioId,
      final Integer newPyvNumber,
      final Boolean pyvKeepOldData,
      final List<Integer> opNumsKeepOldData,
      final String user) throws DataAccessException {
    
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer newScenarioNumber = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_SCENARIO_PYV_PROC, UPDATE_SCENARIO_PYV_PARAM, Types.INTEGER);
      
      Array oracleArray = createNumbersOracleArray(transaction, opNumsKeepOldData);
      
      int c = 1;
      
      proc.setInt(c++, scenarioId);
      proc.setInt(c++, newPyvNumber);
      proc.setString(c++, getIndicatorYN(pyvKeepOldData));
      proc.setArray(c++, oracleArray);
      proc.setString(c++, user);
      
      proc.execute();
      newScenarioNumber = new Integer(proc.getInt(1));
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return newScenarioNumber;
  }
  
  
  /**
   * @param transaction transaction
   * @param farmingOperations List<FarmingOperation>
   * @param user String
   * @throws DataAccessException On Exception
   */
  public void updateOperationAlignment(
      final Transaction transaction,
      final List<FarmingOperation> farmingOperations,
      final String user) throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_OPERATION_SCHEDULE_PROC, UPDATE_OPERATION_SCHEDULE_PARAM, false);
      
      for(FarmingOperation fo : farmingOperations) {
      
        int c = 1;
        
        proc.setInt(c++, fo.getFarmingOperationId());
        proc.setString(c++, fo.getSchedule());
        proc.setInt(c++, fo.getRevisionCount());
        proc.setString(c++, user);
        
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
   * @param transaction transaction
   * @param scenarioId scenarioId
   * @param revisionCount revisionCount
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void updateScenarioRevisionCount(
      final Transaction transaction,
      final Integer scenarioId,
      final Integer revisionCount,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_SCENARIO_REVISION_COUNT_PROC, UPDATE_SCENARIO_REVISION_COUNT_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, scenarioId);
      proc.setInt(param++, revisionCount);
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
   * @param scenario scenario
   * @param logMessage logMessage
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void addScenarioLog(
      final Transaction transaction,
      final Scenario scenario,
      final String logMessage,
      final String user)
  throws DataAccessException {
    
    addScenarioLog(
        transaction,
        scenario.getScenarioId(),
        logMessage,
        user);
  }
  
  
  /**
   * @param transaction transaction
   * @param scenarioId scenarioId
   * @param logMessage logMessage
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void addScenarioLog(
      final Transaction transaction,
      final Integer scenarioId,
      final String logMessage,
      final String user)
          throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + ADD_SCENARIO_LOG_PROC, ADD_SCENARIO_LOG_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, scenarioId);
      proc.setString(param++, logMessage);
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
   * @param scenarioId scenarioId
   * @throws DataAccessException On Exception 
   */
  public void deleteBpuXrefs(
      final Transaction transaction,
      final Integer scenarioId)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    final int paramCount = 1;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_BPU_XREFS_PROC, paramCount, false);
      
      proc.setInt(paramCount, scenarioId);
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
   * @param scenarioId scenarioId
   * @param bpuIds bpuIds
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void saveBpuXrefs(
      final Transaction transaction,
      final Integer scenarioId,
      final List<Integer> bpuIds,
      final String scenarioBpuPurposeCode,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    final int paramCount = 4;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + SAVE_BPU_XREF_PROC, paramCount, false);
      
      for(Integer bpuId : bpuIds) {
      	
      	int param = 1;
      	proc.setInt(param++, scenarioId);
      	proc.setInt(param++, bpuId);
        proc.setString(param++, scenarioBpuPurposeCode);
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
   * @param transaction transaction
   * @param pin Integer
   * @return boolean
   * @throws DataAccessException DataAccessException
   */
  @SuppressWarnings("resource")
  public boolean pinExists(
      final Transaction transaction,
      final Integer pin) throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer pinExistsInteger = null;
    boolean exists = false;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + PIN_EXISTS_PROC, PIN_EXISTS_PARAM, Types.INTEGER);

      int c = 1;

      proc.setInt(c++, pin);

      proc.execute();
      pinExistsInteger = new Integer(proc.getInt(1));
      
      exists = pinExistsInteger.intValue() == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return exists;
  }


  /**
   * @param transaction transaction
   * @param pin Integer
   * @param userGuid String
   * @return boolean
   * @throws DataAccessException DataAccessException
   */
  @SuppressWarnings("resource")
  public boolean pinCheckedOutByUser(
      final Transaction transaction,
      final Integer pin,
      final String userGuid) throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer pinCheckedOutByUserInteger = null;
    boolean result = false;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + PIN_CHECKED_OUT_BY_USER_PROC, PIN_CHECKED_OUT_BY_USER_PARAM, Types.INTEGER);

      int c = 1;

      proc.setInt(c++, pin);
      proc.setString(c++, userGuid);

      proc.execute();
      pinCheckedOutByUserInteger = new Integer(proc.getInt(1));
      
      result = pinCheckedOutByUserInteger.intValue() == 1;

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return result;
  }
  
  
  /**
   * @param transaction transaction
   * @param pin Integer
   * @param programYear Integer
   * @param municipalityCode municipalityCode
   * @param scenarioCategoryCode scenarioCategoryCode
   * @return boolean
   * @throws DataAccessException DataAccessException
   */
  @SuppressWarnings("resource")
  public boolean matchingScenarioExists(
      final Transaction transaction,
      final Integer pin,
      final Integer programYear,
      final String municipalityCode,
      final String scenarioCategoryCode) throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer pinExistsInteger = null;
    boolean exists = false;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + MATCHING_SC_EXISTS_PROC, MATCHING_SC_EXISTS_PARAM, Types.INTEGER);
      
      int c = 1;
      
      proc.setInt(c++, pin);
      proc.setInt(c++, programYear);
      proc.setString(c++, municipalityCode);
      proc.setString(c++, scenarioCategoryCode);
      
      proc.execute();
      pinExistsInteger = new Integer(proc.getInt(1));
      
      exists = pinExistsInteger.intValue() == 1;
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return exists;
  }
  
  
  /**
   * @param transaction transaction
   * @param pin Integer
   * @param programYear Integer
   * @param scenarioId Integer
   * @return boolean
   * @throws DataAccessException DataAccessException
   */
  @SuppressWarnings("resource")
  public boolean combinedFarmHasAccountingCodeError(
      final Transaction transaction,
      final Integer pin,
      final Integer programYear,
      final Integer scenarioId) throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer resultInteger = null;
    boolean result = false;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CF_HAS_ACCOUNTING_CODE_ERROR_PROC, CF_HAS_ACCOUNTING_CODE_ERROR_PARAM, Types.INTEGER);
      
      int c = 1;
      
      proc.setInt(c++, pin);
      proc.setInt(c++, programYear);
      proc.setInt(c++, scenarioId);
      
      proc.execute();
      resultInteger = new Integer(proc.getInt(1));
      
      result = resultInteger.intValue() == 1;
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return result;
  }
  
  
  /**
   * @param transaction transaction
   * @param scenarioId Integer
   * @param verifiedCombinedFarmNumber Integer
   * @return boolean
   * @throws DataAccessException DataAccessException
   */
  @SuppressWarnings("resource")
  public boolean combinedFarmMatchesVerified(
      final Transaction transaction,
      final Integer scenarioId,
      final Integer verifiedCombinedFarmNumber) throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer resultInteger = null;
    boolean result = false;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CF_MATCHES_VERIFIED_PROC, CF_MATCHES_VERIFIED_PARAM, Types.INTEGER);
      
      int c = 1;
      
      proc.setInt(c++, scenarioId);
      proc.setInt(c++, verifiedCombinedFarmNumber);
      
      proc.execute();
      resultInteger = new Integer(proc.getInt(1));
      
      result = resultInteger.intValue() == 1;
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return result;
  }
  
  
  /**
   * @param transaction transaction
   * @param pin Integer
   * @param programYear Integer
   * @param scenarioId Integer
   * @return boolean
   * @throws DataAccessException DataAccessException
   */
  @SuppressWarnings("resource")
  public boolean combinedFarmReferenceYearSetMismatchError(
      final Transaction transaction,
      final Integer pin,
      final Integer programYear,
      final Integer scenarioId) throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer resultInteger = null;
    boolean result = false;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CF_REF_YEARS_MISMATCH_ERROR_PROC, CF_REF_YEARS_MISMATCH_ERROR_PARAM, Types.INTEGER);
      
      int c = 1;
      
      proc.setInt(c++, pin);
      proc.setInt(c++, programYear);
      proc.setInt(c++, scenarioId);
      
      proc.execute();
      resultInteger = new Integer(proc.getInt(1));
      
      result = resultInteger.intValue() == 1;
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return result;
  }
  
  
  /**
   * @param transaction transaction
   * @param participantPin participantPin
   * @param programYear programYear
   * @param combinedFarmNumber combinedFarmNumber
   * @param scenarioNumber scenarioNumber
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void updateCombinedFarmScenario(
      final Transaction transaction,
      final Integer participantPin,
      final Integer programYear,
      final Integer combinedFarmNumber,
      final Integer scenarioNumber,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + COMBINED_FARM_UPDATE_PROC, COMBINED_FARM_UPDATE_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, participantPin);
      proc.setInt(param++, programYear);
      proc.setInt(param++, combinedFarmNumber);
      proc.setInt(param++, scenarioNumber);
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
   * @param currentScenarioId currentScenarioId
   * @param pinToAdd pinToAdd
   * @param programYear programYear
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void addToCombinedFarm(
      final Transaction transaction,
      final Integer currentScenarioId,
      final Integer pinToAdd,
      final Integer programYear,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + COMBINED_FARM_ADD_PROC, COMBINED_FARM_ADD_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, currentScenarioId);
      proc.setInt(param++, pinToAdd);
      proc.setInt(param++, programYear);
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
   * @param scenarioIdToRemove scenarioIdToRemove
   * @param combinedFarmNumber combinedFarmNumber
   * @param user user
   * @throws DataAccessException On Exception 
   */
  public void removeFromCombinedFarm(
      final Transaction transaction,
      final Integer scenarioIdToRemove,
      final Integer combinedFarmNumber,
      final String user)
          throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + COMBINED_FARM_REMOVE_PROC, COMBINED_FARM_REMOVE_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, scenarioIdToRemove);
      proc.setInt(param++, combinedFarmNumber);
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public void updatePinRevisionCounts(
      final Transaction transaction,
      final List<Integer> participantPins,
      final Integer programYear,
      final boolean flagReasonabilityTestsStale,
      final String user)
          throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getOracleConnection(transaction);
    final int paramCount = 4;
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_PIN_REV_COUNTS_PROC, paramCount, false); ) {
      
      Array oracleArray = createNumbersOracleArray(transaction, participantPins);
      
      int param = 1;
      proc.setArray(param++, oracleArray);
      proc.setInt(param++, programYear);
      proc.setIndicator(param++, flagReasonabilityTestsStale);
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }

  /**
   * @param   transaction    transaction
   * @param   scenarioId  scenarioId
   * @param   scenarioRevisionCount  scenarioRevisionCount
   * @param   cashMarginsInd  cashMarginsInd
   * @param   userId  userId
   * @throws  DataAccessException  on exception
   */
  public void writeCashMarginsInd(
    final Transaction transaction,
    final Integer scenarioId,
    final Boolean cashMarginsInd,
    final Date cashMarginsOptInDate,
    final String userId) 
  throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_CASH_MARGINS_IND_PROC, UPDATE_CASH_MARGINS_IND_PARAM, false);

      int index = 1;
      proc.setInt(index++, scenarioId);
      proc.setIndicator(index++, cashMarginsInd);
      proc.setDate(index++, cashMarginsOptInDate);
      proc.setString(index++, userId);
      
      proc.execute();
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }

  /**
   * @param   transaction    transaction
   * @param   scenarioId  scenarioId
   * @param   scenarioRevisionCount  scenarioRevisionCount
   * @param   nonParticipantInd  nonParticipantInd
   * @param   userId  userId
   * @throws  DataAccessException  on exception
   */
  public void writeNonParticipantInd(
    final Transaction transaction,
    final Integer scenarioId,
    final Integer scenarioRevisionCount,
    final Boolean nonParticipantInd,
    final String userId) 
  throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_NON_PARTICIPANT_IND_PROC, UPDATE_NON_PARTICIPANT_IND_PARAM, false);

      int index = 1;
      proc.setInt(index++, scenarioId);
      proc.setInt(index++, scenarioRevisionCount);
      proc.setIndicator(index++, nonParticipantInd);
      proc.setString(index++, userId);
      
      proc.execute();
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }

  
  /**
   * @param   transaction    transaction
   * @param   scenarioId  scenarioId
   * @param   scenarioRevisionCount  scenarioRevisionCount
   * @param   lateParticipant  lateParticipant
   * @param   userId  userId
   * @throws  DataAccessException  on exception
   */
  public void writeLateParticipantInd(
      final Transaction transaction,
      final Integer scenarioId,
      final Integer scenarioRevisionCount,
      final Boolean lateParticipant,
      final String userId) 
          throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_LATE_PARTICIPANT_IND_PROC, UPDATE_LATE_PARTICIPANT_IND_PARAM, false);
      
      int index = 1;
      proc.setInt(index++, scenarioId);
      proc.setInt(index++, scenarioRevisionCount);
      proc.setIndicator(index++, lateParticipant);
      proc.setString(index++, userId);
      
      proc.execute();
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  public void updateFarmType(
      final Transaction transaction,
      final Scenario scenario,
      final String farmType, final String user)
    throws DataAccessException {

      @SuppressWarnings("resource")
      Connection connection = getConnection(transaction);
      final int paramCount = 4;

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + UPDATE_FARM_TYPE_PROC, paramCount, false); ) {

        int param = 1;
        proc.setInt(param++, scenario.getScenarioId());
        proc.setInt(param++, scenario.getFarmingYear().getProgramYearId());
        proc.setString(param++, farmType);
        proc.setString(param++, user);
        proc.execute();

      } catch (SQLException e) {
        logSqlException(e);
        handleException(e);
      }

  }


  public void updateProgramYearLocalReceivedDates(
      final Transaction transaction,
      final Scenario scenario,
      final Date localStatementAReceivedDate,
      final Date localSupplementalReceivedDate, final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    final int paramCount = 5;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_PY_LOCAL_RECEIVED_DATES_PROC, paramCount, false); ) {
      
      int param = 1;
      proc.setInt(param++, scenario.getScenarioId());
      proc.setInt(param++, scenario.getFarmingYear().getProgramYearId());
      proc.setDate(param++, localStatementAReceivedDate);
      proc.setDate(param++, localSupplementalReceivedDate);
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
  }
  
  
  public void createEnwEnrolment(final Transaction transaction,
      final EnwEnrolment enwEnrolment,
      final String user)
          throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_ENW_ENROLMENT_PROC, CREATE_ENW_ENROLMENT_PARAM, false);
      
      int param = 1;
      proc.setInt(param++, enwEnrolment.getScenario().getScenarioId());
      proc.setInt(param++, enwEnrolment.getEnrolmentYear());
      proc.setDouble(param++, enwEnrolment.getEnrolmentFee());
      proc.setDouble(param++, enwEnrolment.getContributionMargin());
      
      proc.setIndicator(param++, enwEnrolment.getBenefitCalculated());
      proc.setIndicator(param++, enwEnrolment.getProxyMarginsCalculated());
      proc.setIndicator(param++, enwEnrolment.getManualMarginsCalculated());
      proc.setIndicator(param++, enwEnrolment.getHasProductiveUnits());
      proc.setIndicator(param++, enwEnrolment.getHasBpus());
      
      proc.setDouble(param++, enwEnrolment.getBenefitEnrolmentFee());
      proc.setDouble(param++, enwEnrolment.getBenefitContributionMargin());
      proc.setDouble(param++, enwEnrolment.getProxyEnrolmentFee());
      proc.setDouble(param++, enwEnrolment.getProxyContributionMargin());
      proc.setDouble(param++, enwEnrolment.getManualEnrolmentFee());
      proc.setDouble(param++, enwEnrolment.getManualContributionMargin());
      
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus2());
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus3());
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus4());
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus5());
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus6());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus2Used());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus3Used());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus4Used());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus5Used());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus6Used());
      
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus2());
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus3());
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus4());
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus5());
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus6());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus2Used());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus3Used());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus4Used());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus5Used());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus6Used());
      
      proc.setDouble(param++, enwEnrolment.getProxyMarginYearMinus2());
      proc.setDouble(param++, enwEnrolment.getProxyMarginYearMinus3());
      proc.setDouble(param++, enwEnrolment.getProxyMarginYearMinus4());
      
      proc.setDouble(param++, enwEnrolment.getManualMarginYearMinus2());
      proc.setDouble(param++, enwEnrolment.getManualMarginYearMinus3());
      proc.setDouble(param++, enwEnrolment.getManualMarginYearMinus4());
      
      proc.setDouble(param++, enwEnrolment.getCombinedFarmPercent());
      proc.setString(param++, enwEnrolment.getEnrolmentCalculationTypeCode());
      proc.setString(param++, user);
      
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public void updateEnwEnrolment(final Transaction transaction,
      final EnwEnrolment enwEnrolment,
      final String user)
      throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_ENW_ENROLMENT_PROC, UPDATE_ENW_ENROLMENT_PARAM, false);

      int param = 1;
      proc.setInt(param++, enwEnrolment.getScenario().getScenarioId());
      proc.setInt(param++, enwEnrolment.getEnrolmentYear());
      proc.setDouble(param++, enwEnrolment.getEnrolmentFee());
      proc.setDouble(param++, enwEnrolment.getContributionMargin());
      
      proc.setIndicator(param++, enwEnrolment.getBenefitCalculated());
      proc.setIndicator(param++, enwEnrolment.getProxyMarginsCalculated());
      proc.setIndicator(param++, enwEnrolment.getManualMarginsCalculated());
      proc.setIndicator(param++, enwEnrolment.getHasProductiveUnits());
      proc.setIndicator(param++, enwEnrolment.getHasBpus());
      
      proc.setDouble(param++, enwEnrolment.getBenefitEnrolmentFee());
      proc.setDouble(param++, enwEnrolment.getBenefitContributionMargin());
      proc.setDouble(param++, enwEnrolment.getProxyEnrolmentFee());
      proc.setDouble(param++, enwEnrolment.getProxyContributionMargin());
      proc.setDouble(param++, enwEnrolment.getManualEnrolmentFee());
      proc.setDouble(param++, enwEnrolment.getManualContributionMargin());
      
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus2());
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus3());
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus4());
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus5());
      proc.setDouble(param++, enwEnrolment.getMarginYearMinus6());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus2Used());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus3Used());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus4Used());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus5Used());
      proc.setIndicator(param++, enwEnrolment.getMarginYearMinus6Used());
      
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus2());
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus3());
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus4());
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus5());
      proc.setDouble(param++, enwEnrolment.getBenefitMarginYearMinus6());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus2Used());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus3Used());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus4Used());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus5Used());
      proc.setIndicator(param++, enwEnrolment.getBenefitMarginYearMinus6Used());
      
      proc.setDouble(param++, enwEnrolment.getProxyMarginYearMinus2());
      proc.setDouble(param++, enwEnrolment.getProxyMarginYearMinus3());
      proc.setDouble(param++, enwEnrolment.getProxyMarginYearMinus4());
      
      proc.setDouble(param++, enwEnrolment.getManualMarginYearMinus2());
      proc.setDouble(param++, enwEnrolment.getManualMarginYearMinus3());
      proc.setDouble(param++, enwEnrolment.getManualMarginYearMinus4());
      
      proc.setDouble(param++, enwEnrolment.getCombinedFarmPercent());
      proc.setString(param++, enwEnrolment.getEnrolmentCalculationTypeCode());
      proc.setInt(param++, enwEnrolment.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  @SuppressWarnings("resource")
  public Integer createPerson(final Transaction transaction,
      final Person person,
      final String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    Integer personId = null;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_PERSON_PROC, CREATE_PERSON_PARAM, Types.INTEGER);) {
      
      int param = 1;
      proc.setString(param++, person.getAddressLine1());
      proc.setString(param++, person.getAddressLine2());
      proc.setString(param++, person.getCity());
      proc.setString(param++, person.getCorpName());
      proc.setString(param++, person.getCountry());
      proc.setString(param++, person.getDaytimePhone());
      proc.setString(param++, person.getEveningPhone());
      proc.setString(param++, person.getFaxNumber());
      proc.setString(param++, person.getCellNumber());
      proc.setString(param++, person.getFirstName());
      proc.setString(param++, person.getLastName());
      proc.setString(param++, person.getPostalCode());
      proc.setString(param++, person.getProvinceState());
      proc.setString(param++, person.getEmailAddress());
      proc.setString(param++, user);
      proc.execute();
      
      personId = proc.getInt(1);
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
    return personId;
  }
  
  
  @SuppressWarnings("resource")
  public void updatePerson(final Transaction transaction,
      final Person person,
      final String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + UPDATE_PERSON_PROC, UPDATE_PERSON_PARAM, false);) {
      
      int param = 1;
      proc.setInt(param++, person.getPersonId());
      proc.setString(param++, person.getAddressLine1());
      proc.setString(param++, person.getAddressLine2());
      proc.setString(param++, person.getCity());
      proc.setString(param++, person.getCorpName());
      proc.setString(param++, person.getCountry());
      proc.setString(param++, person.getDaytimePhone());
      proc.setString(param++, person.getEveningPhone());
      proc.setString(param++, person.getFaxNumber());
      proc.setString(param++, person.getCellNumber());
      proc.setString(param++, person.getFirstName());
      proc.setString(param++, person.getLastName());
      proc.setString(param++, person.getPostalCode());
      proc.setString(param++, person.getProvinceState());
      proc.setString(param++, person.getEmailAddress());
      proc.setInt(param++, person.getRevisionCount());
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  @SuppressWarnings("resource")
  public Integer createNewParticipant(final Transaction transaction,
      final NewParticipant participant,
      final String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    Integer clientId = null;
    Client client = participant.getClient();
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_CLIENT_PROC, CREATE_CLIENT_PARAM, Types.INTEGER);) {
      
      int param = 1;
      proc.setInt(param++, client.getParticipantPin());
      proc.setString(param++, client.getSin());
      proc.setString(param++, client.getBusinessNumber());
      proc.setString(param++, client.getTrustNumber());
      proc.setString(param++, client.getParticipantClassCode());
      proc.setInt(param++, client.getOwner().getPersonId());
      proc.setInt(param++, client.getContact().getPersonId());
      proc.setString(param++, user);
      proc.execute();
      
      clientId = proc.getInt(1);
      client.setClientId(clientId);
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
    return clientId;
  }
  
  
  @SuppressWarnings("resource")
  public Integer createProgramYear(Transaction transaction,
      Integer clientId,
      Integer year,
      String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    Integer programYearId = null;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + CREATE_PY_PROC, CREATE_PY_PARAM, Types.INTEGER);) {
      
      int param = 1;
      proc.setInt(param++, clientId);
      proc.setInt(param++, year);
      proc.setString(param++, user);
      proc.execute();
      
      programYearId = proc.getInt(1);
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
    return programYearId;
  }
  
  
  @SuppressWarnings("resource")
  public Integer createProgramYearVersion(Transaction transaction,
      Integer programYearId,
      String municipalityCode,
      String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    Integer programYearVersionId = null;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + CREATE_PYV_PROC, CREATE_PYV_PARAM, Types.INTEGER);) {
      
      int param = 1;
      proc.setInt(param++, programYearId);
      proc.setString(param++, municipalityCode);
      proc.setString(param++, user);
      proc.execute();
      
      programYearVersionId = proc.getInt(1);
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
    return programYearVersionId;
  }
  
  
  @SuppressWarnings("resource")
  public Integer createScenario(Transaction transaction,
      Integer programYearVersionId,
      String scenarioClassCode,
      String scenarioCategoryCode,
      String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    Integer scenarioId = null;
    
    final int paramCount = 4;
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + CREATE_SCENARIO_PROC, paramCount, Types.INTEGER); ) {
      
      int param = 1;
      proc.setInt(param++, programYearVersionId);
      proc.setString(param++, scenarioClassCode);
      proc.setString(param++, scenarioCategoryCode);
      proc.setString(param++, user);
      proc.execute();
      
      scenarioId = proc.getInt(1);
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
    return scenarioId;
  }
  
  
  @SuppressWarnings("resource")
  public void createPartners(Transaction transaction,
      List<FarmingOperationPartner> partners,
      String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + CREATE_PARTNER_PROC, CREATE_PARTNER_PARAM, false);) {
      
      for (FarmingOperationPartner p : partners) {
        
        int param = 1;
        proc.setBigDecimal(param++, p.getPartnerPercent());
        proc.setInt(param++, p.getParticipantPin());
        proc.setString(param++, p.getPartnerSin());
        proc.setString(param++, p.getFirstName());
        proc.setString(param++, p.getLastName());
        proc.setString(param++, p.getCorpName());
        proc.setInt(param++, p.getFarmingOperation().getFarmingOperationId());
        proc.setString(param++, user);
        
        proc.addBatch();
      }
      
      proc.executeBatch();
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  @SuppressWarnings("resource")
  public void updatePartners(Transaction transaction,
      List<FarmingOperationPartner> partners,
      String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + UPDATE_PARTNER_PROC, UPDATE_PARTNER_PARAM, false);) {
      
      for (FarmingOperationPartner p : partners) {
        
        int param = 1;
        proc.setInt(param++, p.getFarmingOperationPartnerId());
        proc.setBigDecimal(param++, p.getPartnerPercent());
        proc.setInt(param++, p.getParticipantPin());
        proc.setString(param++, p.getPartnerSin());
        proc.setString(param++, p.getFirstName());
        proc.setString(param++, p.getLastName());
        proc.setString(param++, p.getCorpName());
        proc.setInt(param++, p.getFarmingOperation().getFarmingOperationId());
        proc.setInt(param++, p.getRevisionCount());
        proc.setString(param++, user);
        
        proc.addBatch();
      }
      
      proc.executeBatch();
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  @SuppressWarnings("resource")
  public void deletePartners(Transaction transaction,
      List<FarmingOperationPartner> partners)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + DELETE_PARTNER_PROC, DELETE_PARTNER_PARAM, false);) {
      
      for (FarmingOperationPartner partner : partners) {
        
        int param = 1;
        proc.setInt(param++, partner.getFarmingOperationPartnerId());
        proc.setInt(param++, partner.getRevisionCount());
        
        proc.addBatch();
      }
      
      proc.executeBatch();
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }


  @SuppressWarnings("resource")
  public List<FarmingOperationPartner> readAllPartners(Transaction transaction)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    List<FarmingOperationPartner> partners = new ArrayList<>();

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + READ_ALL_PARTNERS_PROC, READ_ALL_PARTNERS_PARAM, true)) {

      proc.execute();

      try(ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          FarmingOperationPartner p = new FarmingOperationPartner();
          p.setParticipantPin(getInteger(rs, "Partnership_Pin"));
          p.setPartnerPercent(rs.getBigDecimal("Partner_Percent"));
          p.setFirstName(getString(rs, "First_Name"));
          p.setLastName(getString(rs, "Last_Name"));
          p.setCorpName(getString(rs, "Corp_Name"));
          partners.add(p);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return partners;
  }


  @SuppressWarnings("resource")
  public Integer copyProgramYearVersion(
      final Transaction transaction,
      final Integer scenarioId,
      final String user) throws DataAccessException {

    Connection connection = getConnection(transaction);
    Integer newProgramYearVersionNumber = null;

    final int paramCount = 2;
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + COPY_SCENARIO_PYV_PROC, paramCount, Types.INTEGER);) {

      int c = 1;

      proc.setInt(c++, scenarioId);
      proc.setString(c++, user);

      proc.execute();
      newProgramYearVersionNumber = new Integer(proc.getInt(1));

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
    return newProgramYearVersionNumber;
  }
  
  
  public void upsertScenarioParameters(
      Transaction transaction,
      Integer scenarioId,
      Map<String, String> parameters,
      String user) throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    
    try {
      final int paramCount = 4;
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPSERT_SCENARIO_CONFIG_PARAM_PROC, paramCount, false);
      
      for(String parameterName : parameters.keySet()) {
        String parameterValue = parameters.get(parameterName);
      
        int c = 1;
        
        proc.setInt(c++, scenarioId);
        proc.setString(c++, parameterName);
        proc.setString(c++, parameterValue);
        proc.setString(c++, user);
        
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
  
  
  @SuppressWarnings("resource")
  public Integer copyForwardYearConfig(Transaction transaction, Integer programYear, String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    Integer rowsCreated = null;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + COPY_FORWARD_YEAR_CONFIG_PROC, COPY_FORWARD_YEAR_CONFIG_PARAM, Types.INTEGER);) {
      
      int param = 1;
      proc.setInt(param++, programYear);
      proc.setString(param++, user);
      proc.execute();
      
      rowsCreated = proc.getInt(1);
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
    return rowsCreated;
  }
  
  
  /**
   * Deletes a scenario with Scenario Class Code USER.
   * Only to be used by Unit Tests.
   */
  public void deleteUserScenario(
      final Connection connection,
      final Integer scenarioId)
  throws DataAccessException {
    
    final int paramCount = 1;
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_USER_SCENARIO_PROC, paramCount, false); ) {
      
      int param = 1;
      proc.setInt(param++, scenarioId);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }

}
