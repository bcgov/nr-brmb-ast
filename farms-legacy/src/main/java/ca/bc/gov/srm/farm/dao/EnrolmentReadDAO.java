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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.enrolment.EnrolmentCombinedFarmOwner;
import ca.bc.gov.srm.farm.domain.enrolment.EnrolmentPartner;
import ca.bc.gov.srm.farm.domain.staging.EnrolmentStaging;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * @author awilkinson
 * @created Dec 6, 2010
 */
public class EnrolmentReadDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_ENROLMENT_READ_PKG";

  private static final String READ_ENROLMENTS_PROC = "READ_ENROLMENTS";

  private static final String READ_STAGING_RESULTS_PROC = "READ_STAGING_RESULTS";
  
  private static final String READ_STAGING_PROC = "READ_STAGING";
  
  private static final String READ_CSV_PROC = "READ_CSV";
  
  private static final String READ_TRANSFER_PROC = "READ_TRANSFER";
  
  private static final String READ_TRANSFER_PARTNERS_PROC = "READ_TRANSFER_PARTNERS";
  
  private static final String READ_TRANSFER_COMBINED_FARM_OWNERS_PROC = "READ_TRANSFER_COMBINED_FARM_OWNERS";


  /**
   * @param transaction transaction
   * @param enrolmentYear enrolmentYear
   * @param regionCode regionCode
   * @throws DataAccessException On Exception
   * 
   * @return List of type Enrolment
   */
  @SuppressWarnings("resource")
  public List<Enrolment> getEnrolments(final Transaction transaction,
      final Integer enrolmentYear,
      final String regionCode)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    List<Enrolment> enrolments = new ArrayList<>();
    final int paramCount = 2;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_ENROLMENTS_PROC, paramCount , true); ) {

      int param = 1;
      proc.setInt(param++, enrolmentYear);
      proc.setString(param++, regionCode);
      proc.execute();

      try (ResultSet rs = proc.getResultSet(); ) {

        while (rs.next()) {
          Enrolment e = new Enrolment();
          e.setClientId(getInteger(rs, "agristability_client_id"));
          e.setPin(getInteger(rs, "participant_pin"));
          e.setProducerName(getString(rs, "producer_name"));
          e.setScenarioState(getString(rs, "scenario_state"));
          e.setFailedToGenerate(getIndicator(rs, "failed_to_generate_ind"));
          e.setFailedReason(getString(rs, "failed_reason"));
          e.setEnrolmentId(getInteger(rs, "program_enrolment_id"));
          e.setEnrolmentYear(getInteger(rs, "enrolment_year"));
          e.setEnrolmentFee(getDouble(rs, "enrolment_fee"));
          e.setGeneratedDate(getDate(rs, "generated_date"));
          e.setIsGeneratedFromCra(getIndicator(rs, "generated_from_cra_ind"));
          e.setIsGeneratedFromEnw(getIndicator(rs, "generated_from_enw_ind"));
          e.setCombinedFarmPercent(getDouble(rs, "combined_farm_percent"));
          e.setWhenUpdated(getDate(rs, "when_updated"));
          e.setRevisionCount(getInteger(rs, "enrolment_revision_count"));
          
          enrolments.add(e);
        }
      }

      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return enrolments;
  }
  
  
  /**
   * @param transaction transaction
   * @throws DataAccessException On Exception
   * 
   * @return List of type Enrolment
   */
  @SuppressWarnings("resource")
  public List<EnrolmentStaging> getStagingResults(final Transaction transaction)
      throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    return getStagingResults(connection);
  }
  
  
  /**
   * @param connection connection
   * @throws DataAccessException On Exception
   * 
   * @return List of type Enrolment
   */
  public List<EnrolmentStaging> getStagingResults(final Connection connection)
      throws DataAccessException {
    
    List<EnrolmentStaging> enrolments = null;
    
    int readStagingResultsParam = 0;
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + READ_STAGING_RESULTS_PROC, readStagingResultsParam , true);){
      
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
      
        enrolments = new ArrayList<>();
        
        while (rs.next()) {
          EnrolmentStaging e = new EnrolmentStaging();
          e.setPin(getInteger(rs, "participant_pin"));
          e.setFailedToGenerate(getIndicator(rs, "failed_to_generate_ind"));
          e.setIsError(getIndicator(rs, "error_ind"));
          e.setFailedReason(getString(rs, "failed_reason"));
          
          enrolments.add(e);
        }
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return enrolments;
  }
  
  
  /**
   * @param connection connection
   * @throws DataAccessException On Exception
   * 
   * @return List of type Enrolment
   */
  public List<EnrolmentStaging> getStaging(final Connection connection)
  throws DataAccessException {
    
    List<EnrolmentStaging> enrolments = null;
    
    int readStagingParam = 0;
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_STAGING_PROC, readStagingParam , true);) {
      
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
      
        enrolments = new ArrayList<>();
        
        while (rs.next()) {
          EnrolmentStaging e = new EnrolmentStaging();
          e.setPin(getInteger(rs, "participant_pin"));
          e.setEnrolmentYear(getInteger(rs, "enrolment_year"));
          e.setEnrolmentFee(getDouble(rs, "enrolment_fee"));
          e.setFailedToGenerate(getIndicator(rs, "failed_to_generate_ind"));
          e.setIsError(getIndicator(rs, "error_ind"));
          e.setFailedReason(getString(rs, "failed_reason"));
          e.setGeneratedDate(getDate(rs, "generated_date"));
          e.setIsGeneratedFromCra(getIndicator(rs, "generated_from_cra_ind"));
          e.setIsGeneratedFromEnw(getIndicator(rs, "generated_from_enw_ind"));
          e.setContributionMarginAverage(getDouble(rs, "contribution_margin_average"));
          e.setMarginYearMinus2(getDouble(rs, "margin_year_minus_2"));
          e.setMarginYearMinus3(getDouble(rs, "margin_year_minus_3"));
          e.setMarginYearMinus4(getDouble(rs, "margin_year_minus_4"));
          e.setMarginYearMinus5(getDouble(rs, "margin_year_minus_5"));
          e.setMarginYearMinus6(getDouble(rs, "margin_year_minus_6"));
          e.setIsMarginYearMinus2Used(getIndicator(rs, "margin_year_minus_2_ind"));
          e.setIsMarginYearMinus3Used(getIndicator(rs, "margin_year_minus_3_ind"));
          e.setIsMarginYearMinus4Used(getIndicator(rs, "margin_year_minus_4_ind"));
          e.setIsMarginYearMinus5Used(getIndicator(rs, "margin_year_minus_5_ind"));
          e.setIsMarginYearMinus6Used(getIndicator(rs, "margin_year_minus_6_ind"));
          e.setCombinedFarmPercent(getDouble(rs, "combined_farm_percent"));
          e.setIsCreateTaskInBarn(getIndicator(rs, "create_task_in_barn_ind"));
          e.setMarginScenarioId(getInteger(rs, "agristability_scenario_id"));
          
          enrolments.add(e);
        }
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return enrolments;
  }


  public void writeCsv(final Transaction transaction,
      final Integer enrolmentYear,
      final List<Integer> pins,
      final Format[] columnFormats,
      final File csvFile)
      throws DataAccessException, IOException {

    int readCsvParam = 2;
    try(FileOutputStream fos = new FileOutputStream(csvFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        Connection connection = getOracleConnection(transaction);
        DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + READ_CSV_PROC, readCsvParam , true);) {
      
      CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
      
      Integer[] pinArray = pins.toArray(new Integer[pins.size()]);
      
      Array oracleArray = createNumbersOracleArray(connection, pinArray);
      
      int param = 1;
      proc.setInt(param++, enrolmentYear);
      proc.setArray(param++, oracleArray);
      proc.execute();

      try(ResultSet rs = proc.getResultSet();) {
  
        writer.writeAll(rs, true, columnFormats);
        writer.flush();
        osw.flush();
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
  }


  /**
   * @param connection connection
   * @param enrolmentYear enrolmentYear
   * @param pins pins
   * @throws DataAccessException On Exception
   * 
   * @return List of type Enrolment
   */
  public List<Enrolment> getEnrolmentsForTransfer(final Connection connection,
      final Integer enrolmentYear,
      final List<Integer> pins)
      throws DataAccessException {

    List<Enrolment> enrolments = new ArrayList<>();
    List<Integer> enrolmentIds = new ArrayList<>();

    int readTransferParam = 2;
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_TRANSFER_PROC, readTransferParam, true);) {

      Integer[] pinArray = pins.toArray(new Integer[pins.size()]);

      Array oracleArray = createNumbersOracleArray(connection, pinArray);
      
      int param = 1;
      proc.setInt(param++, enrolmentYear);
      proc.setArray(param++, oracleArray);
      proc.execute();

      try(ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          Enrolment e = new Enrolment();
          e.setClientId(getInteger(rs, "agristability_client_id"));
          e.setPin(getInteger(rs, "participant_pin"));
          e.setEnrolmentId(getInteger(rs, "program_enrolment_id"));
          e.setEnrolmentYear(getInteger(rs, "enrolment_year"));
          e.setEnrolmentFee(getDouble(rs, "enrolment_fee"));
          e.setFailedToGenerate(getIndicator(rs, "failed_to_generate_ind"));
          e.setFailedReason(getString(rs, "failed_reason"));
          e.setGeneratedDate(getDate(rs, "generated_date"));
          e.setIsGeneratedFromCra(getIndicator(rs, "generated_from_cra_ind"));
          e.setIsGeneratedFromEnw(getIndicator(rs, "generated_from_enw_ind"));
          e.setContributionMarginAverage(getDouble(rs, "contribution_margin_average"));
          e.setMarginYearMinus2(getDouble(rs, "margin_year_minus_2"));
          e.setMarginYearMinus3(getDouble(rs, "margin_year_minus_3"));
          e.setMarginYearMinus4(getDouble(rs, "margin_year_minus_4"));
          e.setMarginYearMinus5(getDouble(rs, "margin_year_minus_5"));
          e.setMarginYearMinus6(getDouble(rs, "margin_year_minus_6"));
          e.setIsMarginYearMinus2Used(getIndicator(rs, "margin_year_minus_2_ind"));
          e.setIsMarginYearMinus3Used(getIndicator(rs, "margin_year_minus_3_ind"));
          e.setIsMarginYearMinus4Used(getIndicator(rs, "margin_year_minus_4_ind"));
          e.setIsMarginYearMinus5Used(getIndicator(rs, "margin_year_minus_5_ind"));
          e.setIsMarginYearMinus6Used(getIndicator(rs, "margin_year_minus_6_ind"));
          e.setIsCreateTaskInBarn(getIndicator(rs, "create_task_in_barn_ind"));
          e.setIsLateParticipant(getIndicator(rs, "late_participant_ind"));
          e.setRevisionCount(getInteger(rs, "revision_count"));
          e.setSectorCodeDescription(getString(rs, "sector_code_desc"));
          e.setSectorDetailCodeDescription(getString(rs, "sector_detail_code_desc"));
          e.setIsInCombinedFarm(getIndicator(rs, "Is_In_Combined_Farm"));
          e.setCombinedFarmPercent(getDouble(rs, "combined_farm_percent"));
          
          enrolments.add(e);
          enrolmentIds.add(e.getEnrolmentId());
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    
    int readTransferPartnersParam = 1;
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + READ_TRANSFER_PARTNERS_PROC, readTransferPartnersParam , true);) {
      
      Integer[] enrolmentIdArray = enrolmentIds.toArray(new Integer[pins.size()]);
      Array oracleArray = createNumbersOracleArray(connection, enrolmentIdArray);
      
      int param = 1;
      proc.setArray(param++, oracleArray);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          
          Integer enrolmentId = getInteger(rs, "Program_Enrolment_Id");
          String partnershipName = getString(rs, "Partnership_Name");
          Integer partnershipPin = getInteger(rs, "Partnership_Pin");
          BigDecimal partnershipPercent = rs.getBigDecimal("Partnership_Percent");
          
          for (Enrolment enrolment : enrolments) {
            if(enrolment.getEnrolmentId().equals(enrolmentId)) {
              EnrolmentPartner partner = new EnrolmentPartner();
              partner.setEnrolment(enrolment);
              partner.setPartnershipName(partnershipName);
              partner.setPartnershipPercent(partnershipPercent);
              partner.setPartnershipPin(partnershipPin);
              enrolment.getEnrolmentPartners().add(partner);
              break;
            }
          }
          
        }
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    
    int readTransferCombinedFarmOwnersParam = 1;
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + READ_TRANSFER_COMBINED_FARM_OWNERS_PROC, readTransferCombinedFarmOwnersParam , true);) {
      
      Integer[] enrolmentIdArray = enrolmentIds.toArray(new Integer[pins.size()]);
      Array oracleArray = createNumbersOracleArray(connection, enrolmentIdArray);
      
      int param = 1;
      proc.setArray(param++, oracleArray);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          
          Integer enrolmentId = getInteger(rs, "Program_Enrolment_Id");
          Integer participantPin = getInteger(rs, "Participant_Pin");
          BigDecimal combinedFarmPercent  = rs.getBigDecimal("Combined_Farm_Percent");
          
          for (Enrolment enrolment : enrolments) {
            if(enrolment.getEnrolmentId().equals(enrolmentId)) {
              EnrolmentCombinedFarmOwner combinedFarmOwner = new EnrolmentCombinedFarmOwner();
              combinedFarmOwner.setEnrolment(enrolment);
              combinedFarmOwner.setParticipantPin(participantPin);
              combinedFarmOwner.setCombinedFarmPercent(combinedFarmPercent);
              enrolment.getCombinedFarmOwners().add(combinedFarmOwner);
              break;
            }
          }
          
        }
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return enrolments;
  }

}
