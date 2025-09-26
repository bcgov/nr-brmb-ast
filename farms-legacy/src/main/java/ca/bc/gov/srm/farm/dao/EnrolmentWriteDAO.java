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
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.staging.EnrolmentStaging;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.webade.dbpool.WrapperConnection;

/**
 * @author awilkinson
 * @created Dec 14, 2010
 */
public class EnrolmentWriteDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_ENROLMENT_WRITE_PKG";

  private static final String GENERATE_ENROLMENTS_PROC = "GENERATE_ENROLMENTS";

  private static final int GENERATE_ENROLMENTS_PARAM = 4;
  
  private static final String UPDATE_STAGING_PROC = "UPDATE_STAGING";
  
  private static final int UPDATE_STAGING_PARAM = 23;
  
  private static final String COPY_STAGING_TO_OPERATIONAL_PROC = "COPY_STAGING_TO_OPERATIONAL";
  
  private static final int COPY_STAGING_TO_OPERATIONAL_PARAM = 0;
  
  private static final String UPSERT_OPERATIONAL_PROC = "UPSERT_OPERATIONAL";
  
  private static final int UPSERT_OPERATIONAL_PARAM = 23;

  private Connection conn = null;
  private Connection neverUse = null;


  /**
   * @param c connection
   */
  public EnrolmentWriteDAO(final Connection c) {
    neverUse = c;
    if(neverUse != null) {
      if (c instanceof WrapperConnection) {
        WrapperConnection wc = (WrapperConnection) c;
        this.conn = wc.getWrappedConnection();
      } else {
        this.conn = c;
      }
    }
  }

  /**
   * @param enrolmentYear Integer
   * @param createTaskInBarn createTaskInBarn
   * @param pins List of type Integer
   * @param user String
   * @return List<Integer pin>
   * @throws DataAccessException On Exception
   */
  public List<Integer> generateEnrolmentsStaging(
      final Integer enrolmentYear,
      Boolean createTaskInBarn,
      final List<Integer> pins, final String user)
      throws DataAccessException {

    List<Integer> pinsForCalculation = new ArrayList<>();

    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + GENERATE_ENROLMENTS_PROC, GENERATE_ENROLMENTS_PARAM, true)) {
  
      Integer[] pinArray = pins.toArray(new Integer[pins.size()]);
  
      Array oracleArray = createNumbersOracleArray(conn, pinArray);
  
      int param = 1;
      proc.setInt(param++, enrolmentYear);
      proc.setIndicator(param++, createTaskInBarn);
      proc.setString(param++, user);
      proc.setArray(param++, oracleArray);
      proc.execute();
  
      try (ResultSet rs = proc.getResultSet();) {
      
        while(rs.next()) {
          pinsForCalculation.add(getInteger(rs, 1));
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

    return pinsForCalculation;
  }
  
  
  /**
   * @param enrolments List<EnrolmentStaging>
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateStaging(
      final List<EnrolmentStaging> enrolments,
      final String user)
  throws DataAccessException {
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + UPDATE_STAGING_PROC, UPDATE_STAGING_PARAM, false);) {
      
      for(EnrolmentStaging enrolment : enrolments) {
        int param = 1;
        proc.setInt(param++, enrolment.getPin());
        proc.setInt(param++, enrolment.getEnrolmentYear());
        proc.setDouble(param++, enrolment.getEnrolmentFee());
        proc.setIndicator(param++, enrolment.getFailedToGenerate());
        proc.setIndicator(param++, enrolment.getIsError());
        proc.setString(param++, enrolment.getFailedReason());
        proc.setDouble(param++, enrolment.getContributionMarginAverage());
        proc.setDouble(param++, enrolment.getMarginYearMinus2());
        proc.setDouble(param++, enrolment.getMarginYearMinus3());
        proc.setDouble(param++, enrolment.getMarginYearMinus4());
        proc.setDouble(param++, enrolment.getMarginYearMinus5());
        proc.setDouble(param++, enrolment.getMarginYearMinus6());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus2Used());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus3Used());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus4Used());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus5Used());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus6Used());
        proc.setIndicator(param++, enrolment.getIsGeneratedFromCra());
        proc.setIndicator(param++, enrolment.getIsGeneratedFromEnw());
        proc.setIndicator(param++, enrolment.getIsCreateTaskInBarn());
        proc.setDouble(param++, enrolment.getCombinedFarmPercent());
        proc.setInt(param++, enrolment.getMarginScenarioId());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

  }
  
  
  public void updateOperational(
      final List<Enrolment> enrolments,
      final String user)
  throws DataAccessException {
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + UPSERT_OPERATIONAL_PROC, UPSERT_OPERATIONAL_PARAM, false);) {
      
      for(Enrolment enrolment : enrolments) {
        int param = 1;
        proc.setInt(param++, enrolment.getClientId());
        proc.setInt(param++, enrolment.getEnrolmentYear());
        proc.setDouble(param++, enrolment.getEnrolmentFee());
        proc.setIndicator(param++, enrolment.getFailedToGenerate());
        proc.setIndicator(param++, enrolment.getIsGeneratedFromCra());
        proc.setIndicator(param++, enrolment.getIsGeneratedFromEnw());
        proc.setString(param++, enrolment.getFailedReason());
        proc.setDate(param++, enrolment.getGeneratedDate());
        proc.setDouble(param++, enrolment.getContributionMarginAverage());
        proc.setDouble(param++, enrolment.getMarginYearMinus2());
        proc.setDouble(param++, enrolment.getMarginYearMinus3());
        proc.setDouble(param++, enrolment.getMarginYearMinus4());
        proc.setDouble(param++, enrolment.getMarginYearMinus5());
        proc.setDouble(param++, enrolment.getMarginYearMinus6());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus2Used());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus3Used());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus4Used());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus5Used());
        proc.setIndicator(param++, enrolment.getIsMarginYearMinus6Used());
        proc.setIndicator(param++, enrolment.getIsCreateTaskInBarn());
        proc.setDouble(param++, enrolment.getCombinedFarmPercent());
        proc.setInt(param++, enrolment.getMarginScenarioId());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

  }
  
  
  /**
   * @throws DataAccessException On Exception
   */
  public void copyStagingToOperational()
  throws DataAccessException {
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + COPY_STAGING_TO_OPERATIONAL_PROC, COPY_STAGING_TO_OPERATIONAL_PARAM, false);) {
      
      proc.execute();
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
  }

}
