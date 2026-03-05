/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import ca.bc.gov.srm.farm.domain.staging.Z01ParticipantInfo;
import ca.bc.gov.srm.farm.domain.staging.Z02PartpntFarmInfo;
import ca.bc.gov.srm.farm.domain.staging.Z03StatementInfo;
import ca.bc.gov.srm.farm.domain.staging.Z04IncomeExpsDtl;
import ca.bc.gov.srm.farm.domain.staging.Z05PartnerInfo;
import ca.bc.gov.srm.farm.domain.staging.Z21ParticipantSuppl;
import ca.bc.gov.srm.farm.domain.staging.Z22ProductionInsurance;
import ca.bc.gov.srm.farm.domain.staging.Z23LivestockProdCpct;
import ca.bc.gov.srm.farm.domain.staging.Z28ProdInsuranceRef;
import ca.bc.gov.srm.farm.domain.staging.Z29InventoryRef;
import ca.bc.gov.srm.farm.domain.staging.Z40PrtcpntRefSuplDtl;
import ca.bc.gov.srm.farm.domain.staging.Z42ParticipantRefYear;
import ca.bc.gov.srm.farm.domain.staging.Z50ParticipntBnftCalc;
import ca.bc.gov.srm.farm.domain.staging.Z51ParticipantContrib;
import ca.bc.gov.srm.farm.domain.staging.Z99ExtractFileCtl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Interface to the Oracle Staging Package.
 *
 * @author  dzwiers
 */
public class StagingDAO {

  private static final String PACKAGE_NAME = "FARMS_STAGING_PKG";
  
  private static final String CLEAR_PROC = "CLEAR";

  private static final String INSERT_Z01_PROC = "INSERT_Z01";

  private static final int INSERT_Z01_PARAM = 35;

  private static final String INSERT_Z02_PROC = "INSERT_Z02";

  private static final int INSERT_Z02_PARAM = 31;

  private static final String INSERT_Z03_PROC = "INSERT_Z03";

  private static final int INSERT_Z03_PARAM = 23;

  private static final String INSERT_Z04_PROC = "INSERT_Z04";

  private static final int INSERT_Z04_PARAM = 8;

  private static final String INSERT_Z05_PROC = "INSERT_Z05";

  private static final int INSERT_Z05_PARAM = 12;

  private static final String INSERT_Z21_PROC = "INSERT_Z21";

  private static final int INSERT_Z21_PARAM = 14;

  private static final String INSERT_Z22_PROC = "INSERT_Z22";

  private static final int INSERT_Z22_PARAM = 6;

  private static final String INSERT_Z23_PROC = "INSERT_Z23";

  private static final int INSERT_Z23_PARAM = 7;

  private static final String INSERT_Z28_PROC = "INSERT_Z28";

  private static final int INSERT_Z28_PARAM = 3;

  private static final String INSERT_Z29_PROC = "INSERT_Z29";

  private static final int INSERT_Z29_PARAM = 8;

  private static final String INSERT_Z40_PROC = "INSERT_Z40";

  private static final int INSERT_Z40_PARAM = 18;

  private static final String INSERT_Z42_PROC = "INSERT_Z42";

  private static final int INSERT_Z42_PARAM = 8;

  private static final String INSERT_Z50_PROC = "INSERT_Z50";

  private static final int INSERT_Z50_PARAM = 11;

  private static final String INSERT_Z51_PROC = "INSERT_Z51";

  private static final int INSERT_Z51_PARAM = 8;

  private static final String INSERT_Z99_PROC = "INSERT_Z99";

  private static final int INSERT_Z99_PARAM = 4;

  private static final String UPDATE_STATUS_PROC = "UPDATE_STATUS";

  private static final int UPDATE_STATUS_PARAM = 2;
  
  private static final String UPDATE_STATUS_NON_AUTONOMOUS_PROC = "UPDATE_STATUS_NON_AUTONOMOUS";
  
  private static final int UPDATE_STATUS_NON_AUTONOMOUS_PARAM = 2;

  /** conn. */
  private Connection conn = null;
  
  @SuppressWarnings("unused")
  private Connection neverUse = null;

  /**
   * Creates a new StagingDAO object.
   *
   * @param  c  Input parameter to initialize class.
   */
  public StagingDAO(final Connection c) {
    neverUse = c;
    this.conn = c;
  }

  /**
   * Closes all open procs.
   *
   * @throws  SQLException  SQLException
   */
  public final void close() throws SQLException {
    SQLException err = null;

    if (z01Proc != null) {

      try {
        z01Proc.close();
      } catch (SQLException e) {
        err = e;
      }
    }

    if (z02Proc != null) {

      try {
        z02Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z03Proc != null) {

      try {
        z03Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z04Proc != null) {

      try {
        z04Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z05Proc != null) {

      try {
        z05Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    close2(err);
  }

  /**
   * Continuation because of checkstyle.
   *
   * see     this.close()
   *
   * @param   e2  e2
   *
   * @throws  SQLException  On Exception
   */
  private void close2(final SQLException e2) throws SQLException {
    SQLException err = e2;

    if (z21Proc != null) {

      try {
        z21Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z22Proc != null) {

      try {
        z22Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z23Proc != null) {

      try {
        z23Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z28Proc != null) {

      try {
        z28Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z29Proc != null) {

      try {
        z29Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z40Proc != null) {

      try {
        z40Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z42Proc != null) {

      try {
        z42Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z50Proc != null) {

      try {
        z50Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z51Proc != null) {

      try {
        z51Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (z99Proc != null) {

      try {
        z99Proc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    if (statusProc != null) {

      try {
        statusProc.close();
      } catch (SQLException e) {

        if (err != null) {
          err = e;
        }
      }
    }

    // throw first problem, but close all procs
    if (err != null) {
      throw err;
    }
  }

  /**
   * clear.
   *
   * @throws  SQLException  On exception.
   */
  public final void clear() throws SQLException {
    @SuppressWarnings("resource")
    DAOStoredProcedure proc = null;

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + CLEAR_PROC, 0,
          false);
      proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      if (proc != null) {
        proc.close();
      }
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }  

  /** cached for repetative use. */
  private DAOStoredProcedure z01Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z02Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z03Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z04Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z05Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z21Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z22Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z23Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z28Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z29Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z40Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z42Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z50Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z51Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure z99Proc = null;

  /** cached for repetative use. */
  private DAOStoredProcedure statusProc = null;
  
  /** cached for repetative use. */
  private DAOStoredProcedure statusNonAutonomousProc = null;

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z01ParticipantInfo obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z01Proc == null) {
        z01Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z01_PROC,
            INSERT_Z01_PARAM, false);
      } else {
        z01Proc.clearParameters();
      }

      int c = 1;

      z01Proc.setInt(c++, obj.getParticipantPin());
      z01Proc.setString(c++, obj.getSinCtnBn());
      z01Proc.setString(c++, obj.getSin());
      z01Proc.setString(c++, obj.getBusinessNumber());
      z01Proc.setString(c++, obj.getTrustNumber());
      z01Proc.setShort(c++, obj.getParticipantTypeCode() == null ? null : obj.getParticipantTypeCode().shortValue());
      z01Proc.setShort(c++, obj.getParticipantLanguage() == null ? null : obj.getParticipantLanguage().shortValue());
      z01Proc.setString(c++, obj.getFirstName());
      z01Proc.setString(c++, obj.getLastName());
      z01Proc.setString(c++, obj.getCorpName());
      z01Proc.setString(c++, obj.getAddress1());
      z01Proc.setString(c++, obj.getAddress2());
      z01Proc.setString(c++, obj.getCity());
      z01Proc.setString(c++, obj.getProvince());
      z01Proc.setString(c++, obj.getPostalCode());
      z01Proc.setString(c++, obj.getCountry());
      z01Proc.setString(c++, obj.getParticipantFax());
      z01Proc.setString(c++, obj.getParticipantPhoneDay());
      z01Proc.setString(c++, obj.getParticipantPhoneEvening());
      z01Proc.setString(c++, obj.getParticipantPhoneCell());
      z01Proc.setString(c++, obj.getParticipantEmail());
      z01Proc.setString(c++, obj.getContactFirstName());
      z01Proc.setString(c++, obj.getContactLastName());
      z01Proc.setString(c++, obj.getContactBusinessName());
      z01Proc.setString(c++, obj.getContactAddress1());
      z01Proc.setString(c++, obj.getContactAddress2());
      z01Proc.setString(c++, obj.getContactCity());
      z01Proc.setString(c++, obj.getContactProvince());
      z01Proc.setString(c++, obj.getContactPostalCode());
      z01Proc.setString(c++, obj.getContactPhoneDay());
      z01Proc.setString(c++, obj.getContactFaxNumber());
      z01Proc.setString(c++, obj.getContactPhoneCell());
      z01Proc.setShort(c++, obj.getPublicOffice() == null ? null : obj.getPublicOffice().shortValue());
      z01Proc.setString(c++, obj.getIdentEffectiveDate());
      z01Proc.setString(c++, userId);

      z01Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z02PartpntFarmInfo obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z02Proc == null) {
        z02Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z02_PROC,
            INSERT_Z02_PARAM, false);
      } else {
        z02Proc.clearParameters();
      }

      int c = 1;

      z02Proc.setInt(c++, obj.getParticipantPin());
      z02Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z02Proc.setShort(c++, obj.getFormVersionNumber() == null ? null : obj.getFormVersionNumber().shortValue());

      z02Proc.setString(c++, obj.getProvinceOfResidence());
      z02Proc.setString(c++, obj.getProvinceOfMainFarmstead());
      z02Proc.setString(c++, obj.getPostmarkDate());
      z02Proc.setString(c++, obj.getReceivedDate());

      z02Proc.setIndicator(c++, obj.isSoleProprietor());
      z02Proc.setIndicator(c++, obj.isPartnershipMember());
      z02Proc.setIndicator(c++, obj.isCorporateShareholder());
      z02Proc.setIndicator(c++, obj.isCoopMember());

      z02Proc.setInt(c++, obj.getCommonShareTotal());
      z02Proc.setShort(c++, obj.getFarmYears() == null ? null : obj.getFarmYears().shortValue());
      z02Proc.setIndicator(c++, obj.isLastYearFarming());
      z02Proc.setShort(c++, obj.getFormOriginCode() == null ? null : obj.getFormOriginCode().shortValue());
      z02Proc.setInt(c++, obj.getIndustryCode());
      z02Proc.setShort(c++, obj.getParticipantProfileCode() == null ? null : obj.getParticipantProfileCode().shortValue());

      z02Proc.setIndicator(c++, obj.isAccrualCashConversion());
      z02Proc.setIndicator(c++, obj.isPerishableCommodities());
      z02Proc.setIndicator(c++, obj.isReceipts());
      z02Proc.setIndicator(c++, obj.isOtherText());
      z02Proc.setString(c++, obj.getOtherText());

      z02Proc.setIndicator(c++, obj.isAccrualWorksheet());
      z02Proc.setIndicator(c++, obj.isCwbWorksheet());
      z02Proc.setIndicator(c++, obj.isCombinedThisYear());
      z02Proc.setIndicator(c++, obj.isCompletedProdCycle());
      z02Proc.setIndicator(c++, obj.isDisaster());
      z02Proc.setIndicator(c++, obj.isCopyCobToContact());

      z02Proc.setShort(c++, obj.getMunicipalityCode() == null ? null : obj.getMunicipalityCode().shortValue());
      z02Proc.setString(c++, obj.getFormVersionEffectiveDate());
      z02Proc.setString(c++, userId);

      z02Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z03StatementInfo obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z03Proc == null) {
        z03Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z03_PROC,
            INSERT_Z03_PARAM, false);
      } else {
        z03Proc.clearParameters();
      }

      int c = 1;

      z03Proc.setShort(c++, obj.getOperationNumber() == null ? null : obj.getOperationNumber().shortValue());
      z03Proc.setInt(c++, obj.getParticipantPin());
      z03Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());

      z03Proc.setInt(c++, obj.getPartnershipPin());
      z03Proc.setString(c++, obj.getPartnershipName());
      z03Proc.setBigDecimal(c++, obj.getPartnershipPercent() == null ? null : BigDecimal.valueOf(obj.getPartnershipPercent()));
      z03Proc.setString(c++, obj.getFiscalYearStart());
      z03Proc.setString(c++, obj.getFiscalYearEnd());
      z03Proc.setShort(c++, obj.getAccountingCode() == null ? null : obj.getAccountingCode().shortValue());

      z03Proc.setIndicator(c++, obj.isLandlord());
      z03Proc.setIndicator(c++, obj.isCropShare());
      z03Proc.setIndicator(c++, obj.isFeederMember());
      z03Proc.setBigDecimal(c++, obj.getGrossIncome() == null ? null : BigDecimal.valueOf(obj.getGrossIncome()));
      z03Proc.setBigDecimal(c++, obj.getExpenses() == null ? null : BigDecimal.valueOf(obj.getExpenses()));
      z03Proc.setBigDecimal(c++, obj.getNetIncomeBeforeAdj() == null ? null : BigDecimal.valueOf(obj.getNetIncomeBeforeAdj()));
      z03Proc.setBigDecimal(c++, obj.getOtherDeductions() == null ? null : BigDecimal.valueOf(obj.getOtherDeductions()));
      z03Proc.setBigDecimal(c++, obj.getInventoryAdjustments() == null ? null : BigDecimal.valueOf(obj.getInventoryAdjustments()));
      z03Proc.setBigDecimal(c++, obj.getNetIncomeAfterAdj() == null ? null : BigDecimal.valueOf(obj.getNetIncomeAfterAdj()));
      z03Proc.setBigDecimal(c++, obj.getBusinessUseOfHomeExpenses() == null ? null : BigDecimal.valueOf(obj.getBusinessUseOfHomeExpenses()));
      z03Proc.setBigDecimal(c++, obj.getNetFarmIncome() == null ? null : BigDecimal.valueOf(obj.getNetFarmIncome()));
      z03Proc.setIndicator(c++, obj.isCropDisaster());
      z03Proc.setIndicator(c++, obj.isLivestockDisaster());
      z03Proc.setString(c++, userId);

      z03Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z04IncomeExpsDtl obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z04Proc == null) {
        z04Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z04_PROC,
            INSERT_Z04_PARAM, false);
      } else {
        z04Proc.clearParameters();
      }

      int c = 1;

      z04Proc.setLong(c++, obj.getIncomeExpenseKey() == null ? null : obj.getIncomeExpenseKey().longValue());
      z04Proc.setInt(c++, obj.getParticipantPin());
      z04Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z04Proc.setShort(c++, obj.getOperationNumber() == null ? null : obj.getOperationNumber().shortValue());
      z04Proc.setShort(c++, obj.getLineCode() == null ? null : obj.getLineCode().shortValue());
      z04Proc.setString(c++, obj.getIe());
      z04Proc.setBigDecimal(c++, obj.getAmount() == null ? null : BigDecimal.valueOf(obj.getAmount()));
      z04Proc.setString(c++, userId);

      z04Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z05PartnerInfo obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z05Proc == null) {
        z05Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z05_PROC,
            INSERT_Z05_PARAM, false);
      } else {
        z05Proc.clearParameters();
      }

      int c = 1;

      z05Proc.setLong(c++, obj.getPartnerInfoKey() == null ? null : obj.getPartnerInfoKey().longValue());
      z05Proc.setInt(c++, obj.getParticipantPin());
      z05Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z05Proc.setShort(c++, obj.getOperationNumber() == null ? null : obj.getOperationNumber().shortValue());

      z05Proc.setInt(c++, obj.getPartnershipPin());
      z05Proc.setString(c++, obj.getPartnerFirstName());
      z05Proc.setString(c++, obj.getPartnerLastName());
      z05Proc.setString(c++, obj.getPartnerCorpName());
      z05Proc.setString(c++, obj.getPartnerSinCtnBn());
      z05Proc.setBigDecimal(c++, obj.getPartnerPercent() == null ? null : BigDecimal.valueOf(obj.getPartnerPercent()));
      z05Proc.setInt(c++, obj.getPartnerPin());
      z05Proc.setString(c++, userId);

      z05Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z21ParticipantSuppl obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z21Proc == null) {
        z21Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z21_PROC,
            INSERT_Z21_PARAM, false);
      } else {
        z21Proc.clearParameters();
      }

      int c = 1;

      z21Proc.setLong(c++, obj.getInventoryKey() == null ? null : obj.getInventoryKey().longValue());
      z21Proc.setInt(c++, obj.getParticipantPin());
      z21Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z21Proc.setShort(c++, obj.getOperationNumber() == null ? null : obj.getOperationNumber().shortValue());
      z21Proc.setShort(c++, obj.getInventoryTypeCode() == null ? null : obj.getInventoryTypeCode().shortValue());
      z21Proc.setInt(c++, obj.getInventoryCode());
      z21Proc.setShort(c++, obj.getCropUnitType() == null ? null : obj.getCropUnitType().shortValue());
      z21Proc.setBigDecimal(c++, obj.getCropOnFarmAcres() == null ? null : BigDecimal.valueOf(obj.getCropOnFarmAcres()));
      z21Proc.setBigDecimal(c++, obj.getCropQtyProduced() == null ? null : BigDecimal.valueOf(obj.getCropQtyProduced()));
      z21Proc.setBigDecimal(c++, obj.getQuantityEnd() == null ? null : BigDecimal.valueOf(obj.getQuantityEnd()));
      z21Proc.setBigDecimal(c++, obj.getEndOfYearPrice() == null ? null : BigDecimal.valueOf(obj.getEndOfYearPrice()));
      z21Proc.setBigDecimal(c++, obj.getEndOfYearAmount() == null ? null : BigDecimal.valueOf(obj.getEndOfYearAmount()));
      z21Proc.setBigDecimal(c++, obj.getCropUnseedableAcres() == null ? null : BigDecimal.valueOf(obj.getCropUnseedableAcres()));
      z21Proc.setString(c++, userId);

      z21Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z22ProductionInsurance obj,
    final String userId) throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z22Proc == null) {
        z22Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z22_PROC,
            INSERT_Z22_PARAM, false);
      } else {
        z22Proc.clearParameters();
      }

      int c = 1;

      z22Proc.setLong(c++, obj.getProductionInsuranceKey() == null ? null : obj.getProductionInsuranceKey().longValue());
      z22Proc.setInt(c++, obj.getParticipantPin());
      z22Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z22Proc.setShort(c++, obj.getOperationNumber() == null ? null : obj.getOperationNumber().shortValue());
      z22Proc.setString(c++, obj.getProductionInsuranceNumber());
      z22Proc.setString(c++, userId);

      z22Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z23LivestockProdCpct obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z23Proc == null) {
        z23Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z23_PROC,
            INSERT_Z23_PARAM, false);
      } else {
        z23Proc.clearParameters();
      }

      int c = 1;

      z23Proc.setLong(c++, obj.getProductiveCapacityKey() == null ? null : obj.getProductiveCapacityKey().longValue());
      z23Proc.setInt(c++, obj.getParticipantPin());
      z23Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z23Proc.setShort(c++, obj.getOperationNumber() == null ? null : obj.getOperationNumber().shortValue());
      z23Proc.setInt(c++, obj.getInventoryCode());
      z23Proc.setBigDecimal(c++, obj.getProductiveCapacityAmount() == null ? null : BigDecimal.valueOf(obj.getProductiveCapacityAmount()));
      z23Proc.setString(c++, userId);

      z23Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z28ProdInsuranceRef obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z28Proc == null) {
        z28Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z28_PROC,
            INSERT_Z28_PARAM, false);
      } else {
        z28Proc.clearParameters();
      }

      int c = 1;

      z28Proc.setShort(c++, obj.getProductionUnit() == null ? null : obj.getProductionUnit().shortValue());
      z28Proc.setString(c++, obj.getProductionUnitDescription());
      z28Proc.setString(c++, userId);

      z28Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z29InventoryRef obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z29Proc == null) {
        z29Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z29_PROC,
            INSERT_Z29_PARAM, false);
      } else {
        z29Proc.clearParameters();
      }

      int c = 1;

      z29Proc.setInt(c++, obj.getInventoryCode());
      z29Proc.setShort(c++, obj.getInventoryTypeCode() == null ? null : obj.getInventoryTypeCode().shortValue());
      z29Proc.setString(c++, obj.getInventoryDesc());
      z29Proc.setString(c++, obj.getInventoryTypeDescription());
      z29Proc.setShort(c++, obj.getInventoryGroupCode() == null ? null : obj.getInventoryGroupCode().shortValue());
      z29Proc.setString(c++, obj.getInventoryGroupDescription());
      z29Proc.setIndicator(c++, obj.getMarketCommodityInd());
      z29Proc.setString(c++, userId);

      z29Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z40PrtcpntRefSuplDtl obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z40Proc == null) {
        z40Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z40_PROC,
            INSERT_Z40_PARAM, false);
      } else {
        z40Proc.clearParameters();
      }

      int c = 1;

      z40Proc.setLong(c++, obj.getPriorYearSupplementalKey() == null ? null : obj.getPriorYearSupplementalKey().longValue());
      z40Proc.setInt(c++, obj.getParticipantPin());
      z40Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z40Proc.setShort(c++, obj.getOperationNumber() == null ? null : obj.getOperationNumber().shortValue());
      z40Proc.setShort(c++, obj.getProductionUnit() == null ? null : obj.getProductionUnit().shortValue());
      z40Proc.setShort(c++, obj.getInventoryTypeCode() == null ? null : obj.getInventoryTypeCode().shortValue());
      z40Proc.setInt(c++, obj.getInventoryCode());
      z40Proc.setBigDecimal(c++, obj.getQuantityStart() == null ? null : BigDecimal.valueOf(obj.getQuantityStart()));
      z40Proc.setBigDecimal(c++, obj.getStartingPrice() == null ? null : BigDecimal.valueOf(obj.getStartingPrice()));
      z40Proc.setBigDecimal(c++, obj.getCropOnFarmAcres() == null ? null : BigDecimal.valueOf(obj.getCropOnFarmAcres()));
      z40Proc.setBigDecimal(c++, obj.getCropQtyProduced() == null ? null : BigDecimal.valueOf(obj.getCropQtyProduced()));
      z40Proc.setBigDecimal(c++, obj.getQuantityEnd() == null ? null : BigDecimal.valueOf(obj.getQuantityEnd()));
      z40Proc.setBigDecimal(c++, obj.getEndYearProducerPrice() == null ? null : BigDecimal.valueOf(obj.getEndYearProducerPrice()));
      z40Proc.setIndicator(c++, obj.isAcceptProducerPrice());
      z40Proc.setBigDecimal(c++, obj.getEndYearPrice() == null ? null : BigDecimal.valueOf(obj.getEndYearPrice()));
      z40Proc.setBigDecimal(c++, obj.getAarmReferenceP1Price() == null ? null : BigDecimal.valueOf(obj.getAarmReferenceP1Price()));
      z40Proc.setBigDecimal(c++, obj.getAarmReferenceP2Price() == null ? null : BigDecimal.valueOf(obj.getAarmReferenceP2Price()));
      z40Proc.setString(c++, userId);

      z40Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z42ParticipantRefYear obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z42Proc == null) {
        z42Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z42_PROC,
            INSERT_Z42_PARAM, false);
      } else {
        z42Proc.clearParameters();
      }

      int c = 1;

      z42Proc.setLong(c++, obj.getProductiveCapacityKey() == null ? null : obj.getProductiveCapacityKey().longValue());
      z42Proc.setShort(c++, obj.getRefOperationNumber() == null ? null : obj.getRefOperationNumber().shortValue());
      z42Proc.setInt(c++, obj.getParticipantPin());
      z42Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z42Proc.setShort(c++, obj.getProductiveTypeCode() == null ? null : obj.getProductiveTypeCode().shortValue());
      z42Proc.setInt(c++, obj.getProductiveCode());
      z42Proc.setBigDecimal(c++, obj.getProductiveCapacityUnits() == null ? null : BigDecimal.valueOf(obj.getProductiveCapacityUnits()));
      z42Proc.setString(c++, userId);

      z42Proc.execute();

      conn.commit();
    } catch (SQLException e){
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z50ParticipntBnftCalc obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z50Proc == null) {
        z50Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z50_PROC,
            INSERT_Z50_PARAM, false);
      } else {
        z50Proc.clearParameters();
      }

      int c = 1;

      z50Proc.setLong(c++, obj.getBenefitCalcKey() == null ? null : obj.getBenefitCalcKey().longValue());
      z50Proc.setInt(c++, obj.getParticipantPin());
      z50Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z50Proc.setShort(c++, obj.getAgristabilityStatus() == null ? null : obj.getAgristabilityStatus().shortValue());
      z50Proc.setBigDecimal(c++, obj.getUnadjustedReferenceMargin() == null ? null : BigDecimal.valueOf(obj.getUnadjustedReferenceMargin()));
      z50Proc.setBigDecimal(c++, obj.getAdjustedReferenceMargin() == null ? null : BigDecimal.valueOf(obj.getAdjustedReferenceMargin()));
      z50Proc.setBigDecimal(c++, obj.getProgramMargin() == null ? null : BigDecimal.valueOf(obj.getProgramMargin()));
      z50Proc.setIndicator(c++, obj.isWholeFarm());
      z50Proc.setIndicator(c++, obj.isStructureChange());
      z50Proc.setBigDecimal(c++, obj.getStructureChangeAdjAmount() == null ? null : BigDecimal.valueOf(obj.getStructureChangeAdjAmount()));
      z50Proc.setString(c++, userId);

      z50Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z51ParticipantContrib obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z51Proc == null) {
        z51Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z51_PROC,
            INSERT_Z51_PARAM, false);
      } else {
        z51Proc.clearParameters();
      }

      int c = 1;

      z51Proc.setLong(c++, obj.getContributionKey() == null ? null : obj.getContributionKey().longValue());
      z51Proc.setInt(c++, obj.getParticipantPin());
      z51Proc.setShort(c++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      z51Proc.setBigDecimal(c++, obj.getProvincialContributions() == null ? null : BigDecimal.valueOf(obj.getProvincialContributions()));
      z51Proc.setBigDecimal(c++, obj.getFederalContributions() == null ? null : BigDecimal.valueOf(obj.getFederalContributions()));
      z51Proc.setBigDecimal(c++, obj.getInterimContributions() == null ? null : BigDecimal.valueOf(obj.getInterimContributions()));
      z51Proc.setBigDecimal(c++, obj.getProducerShare() == null ? null : BigDecimal.valueOf(obj.getProducerShare()));
      z51Proc.setString(c++, userId);

      z51Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final Z99ExtractFileCtl obj, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (z99Proc == null) {
        z99Proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + INSERT_Z99_PROC,
            INSERT_Z99_PARAM, false);
      } else {
        z99Proc.clearParameters();
      }

      int c = 1;

      z99Proc.setShort(c++, obj.getExtractFileNumber() == null ? null : obj.getExtractFileNumber().shortValue());
      z99Proc.setString(c++, obj.getExtractDate());
      z99Proc.setInt(c++, obj.getRowCount());
      z99Proc.setString(c++, userId);

      z99Proc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * @param pImportVersionId Integer
   * @param pString String
   * @throws SQLException  SQLException
   */
  public void status(Integer pImportVersionId, String pString) throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (statusProc == null) {
        statusProc = new DAOStoredProcedure(conn,
            "FARMS_IMPORT_PKG." + UPDATE_STATUS_PROC,
            UPDATE_STATUS_PARAM, false);
      } else {
        statusProc.clearParameters();
      }

      int c = 1;

      statusProc.setLong(c++, pImportVersionId == null ? null : pImportVersionId.longValue());
      statusProc.setString(c++, pString);

      statusProc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }
  
  /**
   * @param pImportVersionId Integer
   * @param pString String
   * @throws SQLException  SQLException
   */
  public void statusNonAutonomous(Integer pImportVersionId, String pString) throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      if (statusNonAutonomousProc == null) {
        statusNonAutonomousProc = new DAOStoredProcedure(conn,
            "FARMS_IMPORT_PKG." + UPDATE_STATUS_NON_AUTONOMOUS_PROC,
            UPDATE_STATUS_NON_AUTONOMOUS_PARAM, false);
      } else {
        statusNonAutonomousProc.clearParameters();
      }
      
      int c = 1;
      
      statusNonAutonomousProc.setLong(c++, pImportVersionId == null ? null : pImportVersionId.longValue());
      statusNonAutonomousProc.setString(c++, pString);
      
      statusNonAutonomousProc.execute();

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }
}
