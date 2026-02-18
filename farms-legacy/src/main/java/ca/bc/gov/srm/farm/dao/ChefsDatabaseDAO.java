/**
 * Copyright (c) 2023,
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmssnCrmEntity;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;


/**
 * @author awilkinson
 * @created Oct 4, 2023
 */
public class ChefsDatabaseDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_CHEFS_PKG";

  private static final String CREATE_SUBMISSION_PROC = "CREATE_SUBMISSION";
  private static final String UPDATE_SUBMISSION_PROC = "UPDATE_SUBMISSION";
  private static final String DELETE_SUBMISSION_PROC = "DELETE_SUBMISSION";
  private static final String READ_SUBMISSIONS_BY_FORM_TYPE_PROC = "READ_SUBMISSIONS_BY_FORM_TYPE";
  private static final String READ_SUBMISSIONS_BY_GUID_PROC = "READ_SUBMISSIONS_BY_GUID";
  private static final String UPDATE_SCENARIO_SUBMISSION_ID_PROC = "UPDATE_SCENARIO_SUBMISSION_ID";
  
  private static final String ADD_PUC_PROC = "ADD_PUC";
  
  private static final String ADD_INCOME_EXPENSE_PROC = "ADD_INCOME_EXPENSE";
  
  private static final String ADD_INVENTORY_PROC = "ADD_INV";
  
  private static final String READ_CRM_ENTITY_GUIDS_BY_SUBMISSION_GUID_PROC = "READ_CRM_ENTITY_GUIDS_BY_SUBMISSION_GUID";
  private static final String CREATE_CRM_ENTITY_PROC = "CREATE_CRM_ENTITY";
  private static final String READ_EXISTING_SUBMISSIONS_FOR_PIN_AND_YEAR_PROC = "READ_EXISTING_SUBMISSIONS_FOR_PIN_AND_YEAR";
  
  public void createSubmission(Connection connection,
      ChefsSubmission submission,
      String user)
      throws DataAccessException {
    
    Collection<ChefsSubmission> submissions = new ArrayList<>();
    submissions.add(submission);
    createSubmissions(connection, submissions, user);
  }
  
  public void createSubmissions(Connection connection,
      Collection<ChefsSubmission> submissions,
      String user)
      throws DataAccessException {

    final int paramCount = 7;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_SUBMISSION_PROC, paramCount, false); ){

      for (ChefsSubmission submission : submissions) {
        
        int param = 1;
        proc.setString(param++, submission.getSubmissionGuid());
        proc.setString(param++, submission.getValidationTaskGuid());
        proc.setString(param++, submission.getMainTaskGuid());
        proc.setString(param++, submission.getBceidFormInd());
        proc.setString(param++, submission.getFormTypeCode());
        proc.setString(param++, submission.getSubmissionStatusCode());
        proc.setString(param++, user);
        
        if(submissions.size() > 1) {
          proc.addBatch();
        } else {
          proc.execute();
        }
      }
      
      if(submissions.size() > 1) {
        proc.executeBatch();
      }

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  public void updateSubmission(Connection connection,
      ChefsSubmission submission,
      String user)
      throws DataAccessException {
    
    Collection<ChefsSubmission> submissions = new ArrayList<>();
    submissions.add(submission);
    updateSubmissions(connection, submissions, user);
  }


  public void updateSubmissions(Connection connection,
      Collection<ChefsSubmission> submissions,
      String user)
  throws DataAccessException {
    
    final int paramCount = 6;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_SUBMISSION_PROC, paramCount, false); ) {

      for (ChefsSubmission submission : submissions) {
        
        int param = 1;
        proc.setInt(param++, submission.getSubmissionId());
        proc.setString(param++, submission.getValidationTaskGuid());
        proc.setString(param++, submission.getMainTaskGuid());
        proc.setString(param++, submission.getBceidFormInd());
        proc.setString(param++, submission.getSubmissionStatusCode());
        proc.setString(param++, user);
        
        if(submissions.size() > 1) {
          proc.addBatch();
        } else {
          proc.execute();
        }
      }
      
      if(submissions.size() > 1) {
        proc.executeBatch();
      }
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }


  public void deleteSubmission(Connection connection,
      String submissionGuid)
  throws DataAccessException {
    
    deleteSubmissions(connection, submissionGuid);
  }


  public void deleteSubmissions(Connection connection,
      String... submissionGuids)
  throws DataAccessException {

    if(submissionGuids != null) {
      deleteSubmissions(connection, Arrays.asList(submissionGuids));
    }
  }
  

  public void deleteSubmissions(Connection connection,
      Collection<String> submissionGuids)
  throws DataAccessException {
    
    if(submissionGuids == null || submissionGuids.isEmpty()) {
      return;
    }
    
    final int paramCount = 1;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_SUBMISSION_PROC, paramCount, false); ) {

      int guidCount = 0;
      for (String submissionGuid : submissionGuids) {
        
        if(submissionGuid != null) {
          guidCount++;
          int param = 1;
          proc.setString(param++, submissionGuid);
          
          if(submissionGuids.size() > 1) {
            proc.addBatch();
          } else {
            proc.execute();
          }
        }
      }
      
      if(guidCount > 1) {
        proc.executeBatch();
      }
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  public List<ChefsSubmission> readSubmissionsByFormType(Connection connection,
      String formTypeCode)
      throws DataAccessException {

    final int paramCount = 1;
    List<ChefsSubmission> submissions = new ArrayList<>();

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_SUBMISSIONS_BY_FORM_TYPE_PROC, paramCount, true); ) {

      int param = 1;
      proc.setString(param++, formTypeCode);
      proc.execute();

      try (ResultSet rs = proc.getResultSet(); ) {

        while (rs.next()) {
          String submissionGuid = getString(rs, "Chef_Submission_Guid");
          
          ChefsSubmission submission = new ChefsSubmission();
          submission.setSubmissionId(getInteger(rs, "Chef_Submission_Id"));
          submission.setSubmissionGuid(submissionGuid);
          submission.setValidationTaskGuid(getString(rs, "Validation_Task_Guid"));
          submission.setMainTaskGuid(getString(rs, "Main_Task_Guid"));
          submission.setFormTypeCode(getString(rs, "Chef_Form_Type_Code"));
          submission.setFormTypeDescription(getString(rs, "Chef_Form_Type_Description"));
          submission.setSubmissionStatusCode(getString(rs, "Chef_Submssn_Status_Code"));
          submission.setRevisionCount(getInteger(rs, "Revision_Count"));
          submission.setBceidFormInd(getString(rs, "Bceid_Form_Ind"));
          submission.setCreatedDate(getDate(rs, "When_Created"));
          submission.setUpdatedDate(getDate(rs, "When_Updated"));
          submissions.add(submission);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return submissions;
  }
  
  
  public ChefsSubmission readSubmissionByGuid(Connection connection,
      String submissionGuid)
      throws DataAccessException {

    List<String> submissionGuidList = new ArrayList<>();
    submissionGuidList.add(submissionGuid);

    Map<String, ChefsSubmission> submissionRecordMap = readSubmissionsByGuid(connection, submissionGuidList);
    ChefsSubmission submission = submissionRecordMap.get(submissionGuid);
    return submission;
  }
  
  
  public Map<String, ChefsSubmission> readSubmissionsByGuid(Connection connection,
      Collection<String> submissionGuids)
      throws DataAccessException {

    final int paramCount = 1;
    Map<String, ChefsSubmission> submissionMap = null;

    List<String> submissionGuidList = new ArrayList<>();
    submissionGuidList.addAll(submissionGuids);

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_SUBMISSIONS_BY_GUID_PROC, paramCount, true); ) {

      Array oracleArray = createGuidOracleArray(connection, submissionGuidList);
      
      int param = 1;
      proc.setArray(param++, oracleArray);
      proc.execute();

      submissionMap = submissionResultSetToMap(proc);

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return submissionMap;
  }


  private Map<String, ChefsSubmission> submissionResultSetToMap(DAOStoredProcedure proc) throws SQLException {
    Map<String, ChefsSubmission> submissionMap;
    try (ResultSet rs = proc.getResultSet(); ) {

      submissionMap = new HashMap<>();
 
      while (rs.next()) {
        String submissionGuid = getString(rs, "Chef_Submission_Guid");
        
        ChefsSubmission submission = new ChefsSubmission();
        submission.setSubmissionId(getInteger(rs, "Chef_Submission_Id"));
        submission.setSubmissionGuid(submissionGuid);
        submission.setValidationTaskGuid(getString(rs, "Validation_Task_Guid"));
        submission.setMainTaskGuid(getString(rs, "Main_Task_Guid"));
        submission.setFormTypeCode(getString(rs, "Chef_Form_Type_Code"));
        submission.setFormTypeDescription(getString(rs, "Chef_Form_Type_Description"));
        submission.setSubmissionStatusCode(getString(rs, "Chef_Submssn_Status_Code"));
        submission.setRevisionCount(getInteger(rs, "Revision_Count"));
        submission.setBceidFormInd(getString(rs, "Bceid_Form_Ind"));
        submission.setCreatedDate(getDate(rs, "When_Created"));
        submission.setUpdatedDate(getDate(rs, "When_Updated"));
        submissionMap.put(submissionGuid, submission);
      }
    }
    return submissionMap;
  }


  public void updateScenarioSubmissionId(Transaction transaction,
      Integer scenarioId,
      Integer submissionId,
      String user)
          throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    updateScenarioSubmissionId(connection, scenarioId, submissionId, user);
  }
    
  public void updateScenarioSubmissionId(Connection connection,
      Integer scenarioId,
      Integer submissionId,
      String user)
  throws DataAccessException {
    
    final int paramCount = 3;
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_SCENARIO_SUBMISSION_ID_PROC, paramCount, false); ) {

      int param = 1;
      proc.setInt(param++, scenarioId);
      proc.setInt(param++, submissionId);
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  

  public void createProductiveUnitCapacities(
      final Transaction transaction,
      final List<ProductiveUnitCapacity> pucList,
      final String user)
      throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    
    final int paramCount = 6;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "." + ADD_PUC_PROC, paramCount, false); ) {

      for (ProductiveUnitCapacity puc : pucList) {
        int param = 1;
        FarmingOperation operation = puc.getFarmingOperation();

        proc.setDouble(param++, puc.getAdjAmount());
        proc.setInt(param++, operation.getFarmingOperationId());
        proc.setString(param++, puc.getStructureGroupCode());
        proc.setString(param++, puc.getInventoryItemCode());
        proc.setString(param++, ParticipantDataSrcCodes.LOCAL);
        proc.setString(param++, user);

        proc.addBatch();
      }

      proc.executeBatch();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  public void createIncomeExpenses(
      final Transaction transaction,
      final List<IncomeExpense> incomeExpenseList,
      final String user)
  throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    
    final int paramCount = 5;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "." + ADD_INCOME_EXPENSE_PROC, paramCount, false); ) {

      for (IncomeExpense ie : incomeExpenseList) {
        int param = 1;
        FarmingOperation operation = ie.getFarmingOperation();

        proc.setDouble(param++, ie.getAdjAmount());
        proc.setString(param++, getIndicatorYN(ie.getIsExpense()));
        proc.setInt(param++, operation.getFarmingOperationId());
        proc.setInt(param++, ie.getLineItem().getLineItem());
        proc.setString(param++, user);

        proc.addBatch();
      }

      proc.executeBatch();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }


  public void createInventoryItems(
      final Transaction transaction,
      final List<InventoryItem> inventoryList,
      final String user)
      throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    
    final int paramCount = 16;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "." + ADD_INVENTORY_PROC, paramCount, false); ) {

      for (InventoryItem item : inventoryList) {
        FarmingOperation operation = item.getFarmingOperation();

        // this is mostly here to avoid casts and allow for easy checks
        CropItem cropItem = null;
        String inventoryClassCode = item.getInventoryClassCode();
        if (inventoryClassCode.equals(InventoryClassCodes.CROP)) {
          cropItem = (CropItem) item;
        }
        int param = 1;

        proc.setInt(param++, operation.getFarmingOperationId());
        proc.setDouble(param++, item.getAdjPriceStart());
        proc.setDouble(param++, item.getAdjPriceEnd());
        proc.setDouble(param++, item.getAdjEndYearProducerPrice());
        proc.setDouble(param++, item.getAdjQuantityStart());
        proc.setDouble(param++, item.getAdjQuantityEnd());
        proc.setDouble(param++, item.getAdjStartOfYearAmount());
        proc.setDouble(param++, item.getAdjEndOfYearAmount());

        if (cropItem != null) {
          proc.setDouble(param++, cropItem.getAdjQuantityProduced());
          proc.setString(param++, cropItem.getCropUnitCode());
          proc.setDouble(param++, cropItem.getOnFarmAcres());
          proc.setDouble(param++, cropItem.getUnseedableAcres());
        } else if (inventoryClassCode.equals(InventoryClassCodes.LIVESTOCK)) {
          proc.setNull(param++, Types.NUMERIC);
          proc.setString(param++, CropUnitCodes.getLivestockUnitCode(item.getInventoryItemCode()));
          proc.setNull(param++, Types.VARCHAR);
          proc.setNull(param++, Types.VARCHAR);
        } else if(item.isAccrual()) {
          proc.setNull(param++, Types.NUMERIC);
          proc.setNull(param++, Types.VARCHAR);
          proc.setNull(param++, Types.VARCHAR);
          proc.setNull(param++, Types.VARCHAR);
        } else {
          throw new UnsupportedOperationException("Unknown inventory class code: " + inventoryClassCode);
        }
        proc.setInt(param++, item.getCommodityXrefId());
        proc.setString(param++, item.getInventoryItemCode());
        proc.setString(param++, inventoryClassCode);
        proc.setString(param++, user);

        proc.addBatch();
      }

      proc.executeBatch();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  public void createCrmEntity(Connection connection, ChefsSubmssnCrmEntity entity, String user) throws DataAccessException {

    final int paramCount = 4;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "." + CREATE_CRM_ENTITY_PROC, paramCount, false);) {

      int param = 1;
      proc.setString(param++, entity.getCrmEntityGuid());
      proc.setString(param++, entity.getCrmEntityTypeCode());
      proc.setInt(param++, entity.getChefSubmissionId());
      proc.setString(param++, user);
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  public Map<String, ChefsSubmssnCrmEntity> readCrmEntityGuidsBySubmissionGuid(Connection connection,
      String submissionGuid)
      throws DataAccessException {

    final int paramCount = 1;
    Map<String, ChefsSubmssnCrmEntity> submissionMap = null;

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_CRM_ENTITY_GUIDS_BY_SUBMISSION_GUID_PROC, paramCount, true); ) {

      int param = 1;
      proc.setString(param++, submissionGuid);
      proc.execute();

      submissionMap = crmEntityResultSetToMap(proc);

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return submissionMap;
  }
  
  private Map<String, ChefsSubmssnCrmEntity> crmEntityResultSetToMap(DAOStoredProcedure proc) throws SQLException {
    
    Map<String, ChefsSubmssnCrmEntity> submissionCrmEntityMap = new HashMap<>();
    try (ResultSet rs = proc.getResultSet();) {

      while (rs.next()) {
        String crmEntityGuid = getString(rs, "Crm_Entity_Guid");

        ChefsSubmssnCrmEntity submissionCrmEntity = new ChefsSubmssnCrmEntity();
        submissionCrmEntity.setChefSubmssnCrmEntityId(getInteger(rs, "Chef_Submssn_Crm_Entity_Id"));
        submissionCrmEntity.setCrmEntityGuid(crmEntityGuid);
        submissionCrmEntity.setChefSubmissionId(getInteger(rs, "Chef_Submission_Id"));
        submissionCrmEntity.setCrmEntityTypeCode(getString(rs, "Crm_Entity_Type_Code"));
        submissionCrmEntity.setRevisionCount(getInteger(rs, "Revision_Count"));
        submissionCrmEntity.setCreatedDate(getDate(rs, "When_Created"));
        submissionCrmEntity.setUpdatedDate(getDate(rs, "When_Updated"));
        submissionCrmEntityMap.put(crmEntityGuid, submissionCrmEntity);
      }
    }
    return submissionCrmEntityMap;
  }
  
  
  public Collection<String> readSubmissionGuidsForPinAndProgramYear(Connection connection,
      String formTypeCode,
      Integer participantPin,
      Integer programYear,
      String submissionGuid)
      throws DataAccessException {

    final int paramCount = 4;
    Collection<String> submissionGuids = new ArrayList<>();

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_EXISTING_SUBMISSIONS_FOR_PIN_AND_YEAR_PROC, paramCount, true); ) {

      int param = 1;
      proc.setString(param++, formTypeCode);
      proc.setInt(param++, participantPin);
      proc.setInt(param++, programYear);
      proc.setString(param++, submissionGuid);
      proc.execute();

      try (ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          String curSubmissionGuid = getString(rs, "Chef_Submission_Guid");

          submissionGuids.add(curSubmissionGuid);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return submissionGuids;
  }

}
