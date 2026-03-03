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

import java.math.BigDecimal;
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
  private static final String PACKAGE_NAME = "FARMS_ENROLMENT_WRITE_PKG";

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
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + GENERATE_ENROLMENTS_PROC, GENERATE_ENROLMENTS_PARAM, true)) {
    
        Integer[] pinArray = pins.toArray(new Integer[pins.size()]);
    
        Array oracleArray = createNumbersOracleArray(conn, pinArray);
    
        int param = 1;
        proc.setShort(param++, enrolmentYear == null ? null : enrolmentYear.shortValue());
        proc.setIndicator(param++, createTaskInBarn);
        proc.setString(param++, user);
        proc.setArray(param++, oracleArray);
        proc.execute();
    
        try (ResultSet rs = proc.getResultSet();) {
        
          while(rs.next()) {
            pinsForCalculation.add(getInteger(rs, 1));
          }
        }
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + UPDATE_STAGING_PROC, UPDATE_STAGING_PARAM, false);) {
        
        for(EnrolmentStaging enrolment : enrolments) {
          int param = 1;
          proc.setInt(param++, enrolment.getPin());
          proc.setShort(param++, enrolment.getEnrolmentYear() == null ? null : enrolment.getEnrolmentYear().shortValue());
          proc.setBigDecimal(param++, enrolment.getEnrolmentFee() == null ? null : BigDecimal.valueOf(enrolment.getEnrolmentFee()));
          proc.setIndicator(param++, enrolment.getFailedToGenerate());
          proc.setIndicator(param++, enrolment.getIsError());
          proc.setString(param++, enrolment.getFailedReason());
          proc.setBigDecimal(param++, enrolment.getContributionMarginAverage() == null ? null : BigDecimal.valueOf(enrolment.getContributionMarginAverage()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus2() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus2()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus3() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus3()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus4() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus4()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus5() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus5()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus6() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus6()));
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus2Used());
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus3Used());
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus4Used());
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus5Used());
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus6Used());
          proc.setIndicator(param++, enrolment.getIsGeneratedFromCra());
          proc.setIndicator(param++, enrolment.getIsGeneratedFromEnw());
          proc.setIndicator(param++, enrolment.getIsCreateTaskInBarn());
          proc.setBigDecimal(param++, enrolment.getCombinedFarmPercent() == null ? null : BigDecimal.valueOf(enrolment.getCombinedFarmPercent()));
          proc.setLong(param++, enrolment.getMarginScenarioId() == null ? null : enrolment.getMarginScenarioId().longValue());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }

  }
  
  
  public void updateOperational(
      final List<Enrolment> enrolments,
      final String user)
  throws DataAccessException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + UPSERT_OPERATIONAL_PROC, UPSERT_OPERATIONAL_PARAM, false);) {
        
        for(Enrolment enrolment : enrolments) {
          int param = 1;
          proc.setLong(param++, enrolment.getClientId() == null ? null : enrolment.getClientId().longValue());
          proc.setShort(param++, enrolment.getEnrolmentYear() == null ? null : enrolment.getEnrolmentYear().shortValue());
          proc.setBigDecimal(param++, enrolment.getEnrolmentFee() == null ? null : BigDecimal.valueOf(enrolment.getEnrolmentFee()));
          proc.setIndicator(param++, enrolment.getFailedToGenerate());
          proc.setIndicator(param++, enrolment.getIsGeneratedFromCra());
          proc.setIndicator(param++, enrolment.getIsGeneratedFromEnw());
          proc.setString(param++, enrolment.getFailedReason());
          proc.setDate(param++, enrolment.getGeneratedDate());
          proc.setBigDecimal(param++, enrolment.getContributionMarginAverage() == null ? null : BigDecimal.valueOf(enrolment.getContributionMarginAverage()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus2() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus2()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus3() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus3()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus4() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus4()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus5() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus5()));
          proc.setBigDecimal(param++, enrolment.getMarginYearMinus6() == null ? null : BigDecimal.valueOf(enrolment.getMarginYearMinus6()));
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus2Used());
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus3Used());
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus4Used());
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus5Used());
          proc.setIndicator(param++, enrolment.getIsMarginYearMinus6Used());
          proc.setIndicator(param++, enrolment.getIsCreateTaskInBarn());
          proc.setBigDecimal(param++, enrolment.getCombinedFarmPercent() == null ? null : BigDecimal.valueOf(enrolment.getCombinedFarmPercent()));
          proc.setLong(param++, enrolment.getMarginScenarioId() == null ? null : enrolment.getMarginScenarioId().longValue());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }

  }
  
  
  /**
   * @throws DataAccessException On Exception
   */
  public void copyStagingToOperational()
  throws DataAccessException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + COPY_STAGING_TO_OPERATIONAL_PROC, COPY_STAGING_TO_OPERATIONAL_PARAM, false);) {
        
        proc.execute();
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }

}
