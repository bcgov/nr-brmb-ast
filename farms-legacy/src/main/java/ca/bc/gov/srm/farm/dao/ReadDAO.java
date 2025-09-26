/**
 * Copyright (c) 2009,
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import ca.bc.gov.srm.farm.chefs.ChefsConfigurationUtil;
import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.CombinedFarmClient;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.PayableItem;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.ProductionInsurance;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioLog;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.ScenarioStateAudit;
import ca.bc.gov.srm.farm.domain.WholeFarmParticipant;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversionItem;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.enrolment.EnwEnrolment;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.webade.dbpool.WrapperConnection;
import oracle.jdbc.driver.OracleConnection;

/**
 * @author dzwiers
 */
public class ReadDAO {

  private static final String NUM_COLLECTION_TYPE_NAME = "FARM_ID_TBL";
  private static final String CODE_COLLECTION_TYPE_NAME = "FARM_CD_TBL";

  private static final String PACKAGE_NAME = "FARM_READ_PKG";

  private static final String READ_OPERATION_PROC = "READ_PYV_OP";
  private static final int READ_OPERATION_PARAM = 1;

  private static final String READ_CLIENT_PROC = "READ_CLIENT";
  private static final int READ_CLIENT_PARAM = 1;

  private static final String READ_OPERATION_PART_PROC = "READ_OP_PART";
  private static final int READ_OPERATION_PART_PARAM = 1;

  private static final String READ_BPU_ALL_PROC = "READ_BPU_ALL";
  private static final int READ_BPU_ALL_PARAM = 4;
  
  private static final String READ_BPU_XREF_PROC = "READ_BPU_XREF";

  private static final String READ_PROGRAM_YEAR_META_PROC = "READ_PY_META";
  private static final int READ_PROGRAM_YEAR_META_PARAM = 2;

  private static final String READ_PROGRAM_YEAR_ID_PROC = "READ_PY_ID";
  private static final int READ_PROGRAM_YEAR_ID_PARAM = 4;
  
  private static final String READ_PROGRAM_YEAR_ID_BY_CLIENT_ID_PROC = "READ_PY_ID_BY_CLIENT_ID";
  private static final int READ_PROGRAM_YEAR_ID_BY_CLIENT_ID_PARAM = 2;

  private static final String READ_PROGRAM_YEAR_VER_PROC = "READ_PYV";
  private static final int READ_PROGRAM_YEAR_VER_PARAM = 1;

  private static final String READ_PRODUCTION_INSURANCE_PROC = "READ_OP_PI";
  private static final int READ_PRODUCTION_INSURANCE_PARAM = 1;

  private static final String READ_SCENARIO_PROC = "READ_PYV_SC";
  private static final int READ_SCENARIO_PARAM = 1;

  private static final String READ_VERIFICATION_NOTES = "READ_VERIFICATION_NOTES";
  private static final int READ_VERIFICATION_NOTES_PARAM = 1;

  private static final String READ_WHOLE_FARM_PROC = "READ_PYV_WF";
  private static final int READ_WHOLE_FARM_PARAM = 1;

  private static final String READ_CLAIM_PROC = "READ_SC_CLM";
  private static final int READ_CLAIM_PARAM = 1;
  
  private static final String READ_STATE_AUDITS_PROC = "READ_SC_STATE_AUDITS";
  private static final int READ_STATE_AUDITS_PARAM = 1;
  
  private static final String READ_SC_LOGS_PROC = "READ_SC_LOGS";
  private static final int READ_SC_LOGS_PARAM = 1;

  private static final String READ_MARGIN_PROC = "READ_SC_MGN";
  private static final int READ_MARGIN_PARAM = 1;

  private static final String READ_INV_PROC = "READ_INV";
  private static final int READ_INV_PARAM = 3;
  
  private static final String READ_CROP_UNIT_CONVERSIONS_PROC = "READ_CROP_UNIT_CONVERSIONS";
  private static final int READ_CROP_UNIT_CONVERSIONS_PARAM = 2;  

  private static final String READ_OP_FMV_PROC = "READ_OP_FMV";
  private static final int READ_OP_FMV_PARAM = 1;
  
  private static final String READ_OP_SINGLE_FMV_PROC = "READ_OP_SINGLE_FMV";
  private static final int READ_OP_SINGLE_FMV_PARAM = 3;
  
  private static final String READ_OP_FMV_PREV_YEAR_PROC = "READ_OP_FMV_PREV_YEAR";
  private static final int READ_OP_FMV_PREV_YEAR_PARAM = 1;

  private static final String READ_PUC_PROC = "READ_PUC";
  private static final String READ_IE_PROC = "READ_IE";
  private static final int READ_IE_PARAM = 4;

  private static final String READ_TOT_MGN_PROC = "READ_SC_TOT_MGN";
  private static final int READ_TOT_MGN_PARAM = 1;
  
  private static final String READ_COB_GEN_DATE_PROC = "READ_COB_GEN_DATE";
  private static final int READ_COB_GEN_DATE_PARAM = 1;
  
  private static final String READ_ENROLMENT_PROC = "READ_ENROLMENT";
  private static final int READ_ENROLMENT_PARAM = 2;
  
  private static final String READ_ENW_ENROLMENT_PROC = "READ_ENW_ENROLMENT";
  private static final int READ_ENW_ENROLMENT_PARAM = 1;
  
  private static final String READ_COMBINED_FARM_CLIENTS_PROC = "READ_COMBINED_FARM_CLIENTS";
  private static final int READ_COMBINED_FARM_CLIENTS_PARAM = 1;

  private static final String READ_FARM_TYPE_PROC = "READ_FARM_TYPE";
  private static final int READ_FARM_TYPE_PARAM = 1;
  
  private Connection conn = null;
  
  @SuppressWarnings("unused")
  private Connection neverUse = null;

  /**
   * @param c Input parameter to initialize class.
   */
  public ReadDAO(final Connection c) {
    neverUse = c;
    if (c instanceof WrapperConnection) {
      WrapperConnection wc = (WrapperConnection) c;
      this.conn = wc.getWrappedConnection();
    } else {
      this.conn = c;
    }
  }

  /*
   * FUNCTION READ_CLIENT(ppin in
   * farm_agristability_clients.participant_pin%type) RETURN SYS_REFCURSOR;
   */
  /**
   * @param pin
   *          Integer
   * 
   * @return AgristabilityClient
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final Client readClient(final Integer pin) throws SQLException {
    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn,
          PACKAGE_NAME + "." + READ_CLIENT_PROC, READ_CLIENT_PARAM, true);

      int c = 1;
      proc.setInt(c++, pin);
      proc.execute();

      rs = proc.getResultSet();

      Client ac = null;

      if (rs.next()) {
        ac = new Client();
        c = 1;

        ac.setClientId(getInteger(rs, c++));
        ac.setFederalIdentifier(getString(rs, c++));
        ac.setSin(getString(rs, c++));
        ac.setBusinessNumber(getString(rs, c++));
        ac.setTrustNumber(getString(rs, c++));
        ac.setParticipantPin(getInteger(rs, c++));
        ac.setIdentEffectiveDate(getDate(rs, c++));
        ac.setIsPublicOffice(getIndicator(rs, c++));
        ac.setIsLocallyUpdated(getIndicator(rs, c++));
        ac.setParticipantLangCode(getString(rs, c++));
        ac.setParticipantLangCodeDescription(getString(rs, c++));
        ac.setParticipantClassCode(getString(rs, c++));
        ac.setParticipantClassCodeDescription(getString(rs, c++));
        ac.setRevisionCount(getInteger(rs, c++));

        if (getInteger(rs, c) != null) {
          Person cl = new Person();
          ac.setOwner(cl);
          cl.setPersonId(getInteger(rs, c++));
          cl.setAddressLine1(getString(rs, c++));
          cl.setAddressLine2(getString(rs, c++));
          cl.setCity(getString(rs, c++));
          cl.setCorpName(getString(rs, c++));
          cl.setDaytimePhone(getString(rs, c++));
          cl.setEveningPhone(getString(rs, c++));
          cl.setFaxNumber(getString(rs, c++));
          cl.setCellNumber(getString(rs, c++));
          cl.setFirstName(getString(rs, c++));
          cl.setLastName(getString(rs, c++));
          cl.setPostalCode(getString(rs, c++));
          cl.setProvinceState(getString(rs, c++));
          cl.setCountry(getString(rs, c++));
          cl.setEmailAddress(getString(rs, c++));
          cl.setRevisionCount(getInteger(rs, c++));
        }

        if (getInteger(rs, c) != null) {
          Person cl = new Person();
          ac.setContact(cl);
          cl.setPersonId(getInteger(rs, c++));
          cl.setAddressLine1(getString(rs, c++));
          cl.setAddressLine2(getString(rs, c++));
          cl.setCity(getString(rs, c++));
          cl.setCorpName(getString(rs, c++));
          cl.setDaytimePhone(getString(rs, c++));
          cl.setEveningPhone(getString(rs, c++));
          cl.setFaxNumber(getString(rs, c++));
          cl.setCellNumber(getString(rs, c++));
          cl.setFirstName(getString(rs, c++));
          cl.setLastName(getString(rs, c++));
          cl.setPostalCode(getString(rs, c++));
          cl.setProvinceState(getString(rs, c++));
          cl.setCountry(getString(rs, c++));
          cl.setEmailAddress(getString(rs, c++));
          cl.setRevisionCount(getInteger(rs, c++));
        }
      }

      if (rs.next()) {
        throw new SQLException("Too many Agristability Clients found.");
      }

      return ac;
    } finally {

    	close(rs, proc);
    }
  }

  /**
   * @param pin
   *          Integer
   * @param year
   *          Integer
   * 
   * @return List<ScenarioMetaData>
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final List<ScenarioMetaData> readProgramYearMetadata(final Integer pin,
      final Integer year) throws SQLException {
    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_PROGRAM_YEAR_META_PROC, READ_PROGRAM_YEAR_META_PARAM, true);

      int c = 1;
      proc.setInt(c++, pin);
      proc.setInt(c++, year);
      proc.execute();

      rs = proc.getResultSet();

      ArrayList<ScenarioMetaData> l = new ArrayList<>();

      while (rs.next()) {

        ScenarioMetaData pyv = new ScenarioMetaData();
        
        c = 1;
        
        pyv.setScenarioId(getInteger(rs, c++));
        pyv.setCalcVersion(getString(rs,c++));
        pyv.setDefaultInd(getIndicator(rs, c++));
        pyv.setProgramYear(getInteger(rs, c++));
        pyv.setScenarioStateCode(getString(rs,c++));
        pyv.setScenarioStateDescription(getString(rs,c++)); 
        pyv.setProgramYearVersion(getInteger(rs, c++)); 
        pyv.setProgramYearVersionId(getInteger(rs, c++));
        pyv.setRevisionCount(getInteger(rs, c++));
        pyv.setScenarioCreatedDate(getDate(rs, c++));
        pyv.setScenarioDescription(getString(rs,c++));
        pyv.setScenarioCreatedBy(getString(rs,c++));
        pyv.setScenarioNumber(getInteger(rs, c++));
        pyv.setScenarioTypeCode(getString(rs,c++));
        pyv.setScenarioTypeDescription(getString(rs,c++));
        pyv.setScenarioCategoryCode(getString(rs,c++));
        pyv.setScenarioCategoryDescription(getString(rs,c++));
        pyv.setCombinedFarmNumber(getInteger(rs,c++));
        pyv.setChefsFormSubmissionId(getInteger(rs,c++));
        pyv.setChefsFormSubmissionGuid(getString(rs,c++));
        pyv.setParticipantDataSrcCode(getString(rs,c++));
        pyv.setMunicipalityCode(getString(rs,c++));
        
        String chefsFormSubmissionGuid = pyv.getChefsFormSubmissionGuid();
        if(chefsFormSubmissionGuid != null) {
          ChefsConfigurationUtil chefsConfigUtil = ChefsConfigurationUtil.getInstance();
          String chefsViewSubmissionUrl = chefsConfigUtil.getViewSubmissionUrl(chefsFormSubmissionGuid);
          
          pyv.setChefsViewSubmissionUrl(chefsViewSubmissionUrl);
        }
        
        l.add(pyv);
      }

      return l;
    } finally {

    	close(rs, proc);
    }
  }

  /**
   * 
   * @param rs ResultSet
   * @param sc Scenario Return by reference
   * @return ReferenceScenario
   * @throws SQLException
   *           SQLException
   */
  private ReferenceScenario buildPyv(ResultSet rs, Scenario sc) throws SQLException {

    int c = 1;

    Integer programYearId = getInteger(rs, c++); // should be set
    Integer year = getInteger(rs, c++); // should exist

    // save these for below - need pyv_id first
    String assignedToUserGuid = getString(rs, c++);
    String assignedToUserid = getString(rs, c++);
    Integer programYearRevisionCount = getInteger(rs, c++);
    Date programYearWhenCreated = getDate(rs, c++);

    Integer programYearVersionId = getInteger(rs, c++);

    ReferenceScenario rsc = null;

    // find the appropriate objects
    if (sc != null && sc.getYear()!=null && sc.getYear().equals(year)) {
      rsc = sc;
      sc.setAssignedToUserGuid(assignedToUserGuid);
      sc.setAssignedToUserId(assignedToUserid);
      if (programYearRevisionCount != null) {
        sc.setProgramYearRevisionCount(programYearRevisionCount);
        sc.setProgramYearWhenCreatedTimestamp(programYearWhenCreated);
      }
    } else {
      if (sc != null && sc.getReferenceScenarios() != null) {
        for (ReferenceScenario t : sc.getReferenceScenarios()) {
          if (t != null && t.getYear()!=null && t.getYear().equals(year)) {
            rsc = t;
          }
        }
      }
    }

    if (rsc == null) {
      rsc = new ReferenceScenario();
    }

    if (rsc.getFarmingYear() == null) {
      rsc.setFarmingYear(new FarmingYear());
    }
    FarmingYear fy = rsc.getFarmingYear();

    // check ids
    if (rsc.getFarmingYear().getProgramYearId() == null) {
      rsc.getFarmingYear().setProgramYearId(programYearId);
    } else {
      if (!rsc.getFarmingYear().getProgramYearId().equals(programYearId)) {
        throw new SQLException("Program Year Id Inconsistency Encountered: "
            + programYearId + " " + rsc.getFarmingYear().getProgramYearId());
      }
    }

    if (fy.getProgramYearVersionId() == null) {
      fy.setProgramYearVersionId(programYearVersionId);
    } else {
      if (!fy.getProgramYearVersionId().equals(programYearVersionId)) {
        throw new SQLException(
            "Program Year Version Id Inconsistency Encountered: "
                + programYearVersionId + " " + fy.getProgramYearVersionId());
      }
    }

    fy.setProgramYearVersionNumber(getInteger(rs, c++));
    fy.setFormVersionNumber(getInteger(rs, c++));
    fy.setCommonShareTotal(getInteger(rs, c++));
    fy.setFarmYears(getInteger(rs, c++));
    fy.setIsAccrualWorksheet(getIndicator(rs, c++));
    fy.setIsCompletedProdCycle(getIndicator(rs, c++));
    fy.setIsCwbWorksheet(getIndicator(rs, c++));
    fy.setIsPerishableCommodities(getIndicator(rs, c++));
    fy.setIsReceipts(getIndicator(rs, c++));
    fy.setIsAccrualCashConversion(getIndicator(rs, c++));
    fy.setIsCombinedFarm(getIndicator(rs, c++));
    fy.setIsCoopMember(getIndicator(rs, c++));
    fy.setIsCorporateShareholder(getIndicator(rs, c++));
    fy.setIsDisaster(getIndicator(rs, c++));
    fy.setIsPartnershipMember(getIndicator(rs, c++));
    fy.setIsSoleProprietor(getIndicator(rs, c++));
    fy.setOtherText(getString(rs, c++));
    fy.setPostMarkDate(getDate(rs, c++));
    fy.setProvinceOfResidence(getString(rs, c++));
    fy.setCraStatementAReceivedDate(getDate(rs, c++));
    fy.setIsLastYearFarming(getIndicator(rs, c++));
    fy.setDescriptionOfChange(getString(rs, c++));
    fy.setIsCanSendCobToRep(getIndicator(rs, c++));
    fy.setProvinceOfMainFarmstead(getString(rs, c++));
    fy.setIsLocallyUpdated(getIndicator(rs, c++));
    fy.setParticipantProfileCode(getString(rs, c++));
    fy.setParticipantProfileCodeDescription(getString(rs, c++));
    fy.setMunicipalityCode(getString(rs, c++));
    fy.setMunicipalityCodeDescription(getString(rs, c++));
    fy.setAgristabFedStsCode(getInteger(rs, c++));
    fy.setAgristabFedStsCodeDescription(getString(rs, c++));
    fy.setIsNonParticipant(getIndicator(rs, c++));
    fy.setIsLateParticipant(getIndicator(rs, c++));
    fy.setIsCashMargins(getIndicator(rs, c++));
    fy.setCashMarginsOptInDate(getDate(rs, c++));
    fy.setRevisionCount(getInteger(rs, c++));

    if (getInteger(rs, c) != null) {
      ImportVersion iv = new ImportVersion();
      rsc.setImportVersion(iv);

      iv.setImportVersionId(getInteger(rs, c++));
      iv.setImportedByUser(getString(rs, c++));
      iv.setDescription(getString(rs, c++));
      iv.setAuditInfo(getString(rs, c++));
      iv.setImportFileName(getString(rs, c++));
      iv.setImportControlFileDate(getDate(rs, c++));
      iv.setImportControlFileInfo(getString(rs, c++));
      iv.setImportDate(getDate(rs, c++));
      c++; // importstatecode;
      iv.setRevisionCount(getInteger(rs, c++));
    }
    
    return rsc;
  }

  /**
   * @param pin
   *          Integer
   * @param year
   *          Integer
   * @param scnum
   *          Integer
   * @param pMode String
   * 
   * @return int[][]
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final int[][] readPyIds(final Integer pin, final Integer year,
      final Integer scnum, String pMode) throws SQLException {
    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_PROGRAM_YEAR_ID_PROC, READ_PROGRAM_YEAR_ID_PARAM, true);

      int c = 1;
      proc.setInt(c++, pin);
      proc.setInt(c++, year);
      proc.setInt(c++, scnum);
      proc.setString(c++, pMode);
      proc.execute();

      rs = proc.getResultSet();

      ArrayList<int[]> l = new ArrayList<>();

      while (rs.next()) {

        // year
        // program_year_id
        // program_year_version_id
        // agristability_scenario_id

        c = 1;
        int pyear = rs.getInt(c++);
        int programyearid = rs.getInt(c++);
        int programyearversionid = rs.getInt(c++);
        int agristabilityscenarioid = rs.getInt(c++);

        l.add(new int[] { pyear, programyearid, programyearversionid,
            agristabilityscenarioid });

      }

      if (l.size() > 0) {
        return l.toArray(new int[l.size()][]);
      }

      return null;
    } finally {

    	close(rs, proc);
    }
  }
  
  /**
   * @param pyvid
   *          Integer[]
   * @param sc Scenario
   * 
   *          Return by reference
   * 
   * @throws SQLException
   *           SQLException
   */
  public final void readProgramYear(final Integer[] pyvid, Scenario sc)
      throws SQLException {
    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_PROGRAM_YEAR_VER_PROC, READ_PROGRAM_YEAR_VER_PARAM, true);

      int c = 1;
      Array oracleArray = createNumbersOracleArray(pyvid);
      proc.setArray(c++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      while (rs.next()) {
        buildPyv(rs, sc);
      }

    } finally {

    	close(rs, proc);
    }
  }

  /**
   * @param programYearVersions
   *          Integer[]
   * 
   * @return HashMap <pyv_id, List<Operation>>
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, List<FarmingOperation>> readOperation(final Integer[] programYearVersions)
      throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_OPERATION_PROC, READ_OPERATION_PARAM, true);

      int c = 1;
      Array oracleArray = createNumbersOracleArray(programYearVersions);
      proc.setArray(c++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, List<FarmingOperation>> r = new HashMap<>();

      while (rs.next()) {
        FarmingOperation op = new FarmingOperation();

        c = 1;

        op.setFarmingOperationId(getInteger(rs, c++));
        op.setBusinessUseHomeExpense(getDouble(rs, c++));
        op.setFarmingExpenses(getDouble(rs, c++));
        op.setFiscalYearEnd(getDate(rs, c++));
        op.setFiscalYearStart(getDate(rs, c++));
        op.setGrossIncome(getDouble(rs, c++));
        op.setInventoryAdjustments(getDouble(rs, c++));
        op.setIsCropShare(getIndicator(rs, c++));
        op.setIsFeederMember(getIndicator(rs, c++));
        op.setIsLandlord(getIndicator(rs, c++));
        op.setNetFarmIncome(getDouble(rs, c++));
        op.setNetIncomeAfterAdj(getDouble(rs, c++));
        op.setNetIncomeBeforeAdj(getDouble(rs, c++));
        op.setOtherDeductions(getDouble(rs, c++));
        op.setPartnershipName(getString(rs, c++));
        op.setPartnershipPercent(getDouble(rs, c++));
        op.setPartnershipPin(getInteger(rs, c++));
        op.setOperationNumber(getInteger(rs, c++));
        op.setIsCropDisaster(getIndicator(rs, c++));
        op.setIsLivestockDisaster(getIndicator(rs, c++));
        op.setIsLocallyUpdated(getIndicator(rs, c++));
        op.setIsLocallyGenerated(getIndicator(rs, c++));
        op.setAccountingCode(getString(rs, c++));
        op.setAccountingCodeDescription(getString(rs, c++));
        Integer pyvId = getInteger(rs, c++);
        op.setSchedule(getString(rs, c++));
        op.setTipReportDocId(getInteger(rs, c++));
        op.setTipReportGeneratedDate(getDate(rs, c++));
        op.setRevisionCount(getInteger(rs, c++));

        List<FarmingOperation> l = r.get(pyvId);
        if (l == null) {
          l = new ArrayList<>();
          r.put(pyvId, l);
        }
        l.add(op);
      }
      
      return r;
    } finally {

    	close(rs, proc);
    }
  }

  /* FUNCTION READ_OP_PART(op_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param operationIds
   *          Integer[]
   * 
   * @return HashMap <op_id, List<FarmingOperationPartner>>
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, List<FarmingOperationPartner>> readOperationPartners(final Integer[] operationIds)
      throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_OPERATION_PART_PROC, READ_OPERATION_PART_PARAM, true);

      int c = 1;
      Array oracleArray = createNumbersOracleArray(operationIds);
      proc.setArray(c++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, List<FarmingOperationPartner>> r = new HashMap<>();

      while (rs.next()) {
        FarmingOperationPartner op = new FarmingOperationPartner();

        c = 1;

        op.setFarmingOperationPartnerId(getInteger(rs, c++));
        op.setPartnerPercent(rs.getBigDecimal(c++));
        op.setParticipantPin(getInteger(rs, c++));
        op.setPartnerSin(getString(rs, c++));
        op.setFirstName(getString(rs, c++));
        op.setLastName(getString(rs, c++));
        op.setCorpName(getString(rs, c++));
        Integer opId = getInteger(rs, c++);
        op.setRevisionCount(getInteger(rs, c++));

        List<FarmingOperationPartner> l = r.get(opId);
        if (l == null) {
          l = new ArrayList<>();
          r.put(opId, l);
        }
        l.add(op);
      }

      return r;
    } finally {

    	close(rs, proc);
    }
  }

  /* FUNCTION READ_OP_PI(op_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param operationIds
   *          Integer[]
   * 
   * @return ProductionInsurance[]
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, List<ProductionInsurance>> readOperationProductionInsurance(
      final Integer[] operationIds) throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_PRODUCTION_INSURANCE_PROC, READ_PRODUCTION_INSURANCE_PARAM,
          true);

      int c = 1;
      Array oracleArray = createNumbersOracleArray(operationIds);
      proc.setArray(c++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, List<ProductionInsurance>> r = new HashMap<>();

      while (rs.next()) {
        ProductionInsurance op = new ProductionInsurance();

        c = 1;

        op.setProductionInsuranceId(getInteger(rs, c++));
        op.setProductionInsuranceNumber(getString(rs, c++));
        op.setIsLocallyUpdated(getIndicator(rs, c++));
        Integer opId = getInteger(rs, c++);
        op.setRevisionCount(getInteger(rs, c++));

        List<ProductionInsurance> l = r.get(opId);
        if (l == null) {
          l = new ArrayList<>();
          r.put(opId, l);
        }
        l.add(op);
      }

      return r;
    } finally {

    	close(rs, proc);
    }
  }

  /* FUNCTION READ_PYV_SC(pyv_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param scids
   *          Integer[]
   * 
   * @param sc
   *          Scenario return by reference
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final void readScenario(final Integer[] scids, Scenario sc)
      throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_SCENARIO_PROC, READ_SCENARIO_PARAM, true);

      int c = 1;
      Array oracleArray = createNumbersOracleArray(scids);
      proc.setArray(c++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      while (rs.next()) {

        c = 1;
        Integer scId = getInteger(rs, c++);

        // find the correct object first
        ReferenceScenario rss = null;
        if (sc.getScenarioId() != null && sc.getScenarioId().equals(scId)) {
          rss = sc;
        } else {
          if (sc.getReferenceScenarios() != null) {
            rss = findReferenceScenario(sc, scId);
          }
        }

        if (rss == null) {
          throw new SQLException("Reference Scenario not found for " + scId);
        }

        rss.setScenarioNumber(getInteger(rs, c++));
        rss.setBenefitsCalculatorVersion(getString(rs, c++));
        rss.setScenarioCreatedBy(getString(rs, c++));
        rss.setDescription(getString(rs, c++));
        rss.setIsDefaultInd(getIndicator(rs, c++));
        rss.setScenarioDate(getDate(rs, c++));
        c++; // skip ProgramYearVersionId
        rss.setUsedInCalc(getIndicator(rs, c++));
        rss.setIsDeemedFarmingYear(getIndicator(rs, c++));
        rss.setScenarioTypeCode(getString(rs, c++));
        rss.setScenarioTypeCodeDescription(getString(rs, c++));
        rss.setParticipantDataSrcCode(getString(rs, c++));
        
        if (rss == sc) {
          sc.setScenarioStateCode(getString(rs, c++));
          sc.setScenarioStateCodeDescription(getString(rs, c++));
          sc.setScenarioCategoryCode(getString(rs, c++));
          sc.setScenarioCategoryCodeDescription(getString(rs, c++));
          sc.setRevisionCount(getInteger(rs, c++));
          sc.setWhenUpdatedTimestamp(getDate(rs, c++));
          sc.setCraStatementAReceivedDate(getDate(rs, c++));
          sc.setCraSupplementalReceivedDate(getDate(rs, c++));
          sc.setLocalStatementAReceivedDate(getDate(rs, c++));
          sc.setLocalSupplementalReceivedDate(getDate(rs, c++));
          sc.setChefsSubmissionId(getInteger(rs, c++));
          sc.setCrmTaskGuid(getString(rs, c++));
          sc.setChefsSubmissionGuid(getString(rs, c++));
          sc.setIsInCombinedFarmInd(getIndicator(rs, c++));
          sc.setCombinedFarmNumber(getInteger(rs, c++));
          sc.setChefsFormTypeCode(getString(rs, c++));
          sc.setVerifierUserId(getInteger(rs, c++));
          sc.setVerifiedByEmail(getString(rs, c++));
          sc.setVerifierAccountName(getString(rs, c++));
        }

      }
      
      if(sc.getIsInCombinedFarmInd() == null) {
        sc.setIsInCombinedFarmInd(false);
      }

    } finally {
    	close(rs, proc);
    }
  }

	/**
	 * @param sc sc
	 * @param scId scId
	 * @return the ReferenceScenario
	 */
	private ReferenceScenario findReferenceScenario(Scenario sc, Integer scId) {
		ReferenceScenario rss = null;
		
		for (ReferenceScenario t : sc.getReferenceScenarios()) {
		  if (t != null && t.getScenarioId().equals(scId)) {
		    rss = t;
		    break;
		  }
		}
		
		return rss;
	}

  
  public final String[] readVerificationNotes(Integer pyId) throws SQLException {
    final int numberOfNotes = 3;
    String verificationNotes[] = new String[numberOfNotes];

    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn,
        PACKAGE_NAME + "." + READ_VERIFICATION_NOTES, READ_VERIFICATION_NOTES_PARAM, true);) {

      int c = 1;
      proc.setInt(c, pyId);
      proc.execute();

      try(ResultSet rs = proc.getResultSet();) {

        if (rs.next()) {
          c = 1;
          verificationNotes[c-1] = getString(rs, c++);
          verificationNotes[c-1] = getString(rs, c++);
          verificationNotes[c-1] = getString(rs, c++);
        }
      }

      return verificationNotes;
    }
  }


  /* FUNCTION READ_PYV_WF(pyv_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param programYearVersions
   *          Integer[]
   * 
   * @return WholeFarmParticipant[]
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, List<WholeFarmParticipant>> readWholeFarmParticipant(
      final Integer[] programYearVersions) throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_WHOLE_FARM_PROC, READ_WHOLE_FARM_PARAM, true);

      int c = 1;
      Array oracleArray = createNumbersOracleArray(programYearVersions);
      proc.setArray(c++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, List<WholeFarmParticipant>> r = new HashMap<>();

      while (rs.next()) {
        WholeFarmParticipant wp = new WholeFarmParticipant();

        c = 1;

        wp.setWholeFarmParticipantId(getInteger(rs, c++));
        wp.setWholeFarmCombinedPin(getInteger(rs, c++));
        wp.setIsWholeFarmCombPinAdd(getIndicator(rs, c++));
        wp.setIsWholeFarmCombPinRemove(getIndicator(rs, c++));
        Integer pyvId = getInteger(rs, c++);
        wp.setRevisionCount(getInteger(rs, c++));

        List<WholeFarmParticipant> l = r.get(pyvId);
        if (l == null) {
          l = new ArrayList<>();
          r.put(pyvId, l);
        }
        l.add(wp);
      }

      return r;
    } finally {

    	close(rs, proc);
    }
  }

  /* FUNCTION READ_SC_CLM(sc_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param scenarioIds
   *          Integer[]
   * 
   * @return HashMap<Sc Id, Benefit>
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, Benefit> readScenarioClaim(final Integer[] scenarioIds)
      throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + READ_CLAIM_PROC,
          READ_CLAIM_PARAM, true);

      int c = 1;
      Array oracleArray = createNumbersOracleArray(scenarioIds);
      proc.setArray(c++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, Benefit> r = new HashMap<>();

      while (rs.next()) {
        Benefit cl = new Benefit();

        c = 1;

        cl.setClaimId(getInteger(rs, c++));
        cl.setAdministrativeCostShare(getDouble(rs, c++));
        cl.setLateApplicationPenalty(getDouble(rs, c++));
        cl.setMaximumContribution(getDouble(rs, c++));
        cl.setOutstandingFees(getDouble(rs, c++));
        cl.setProgramYearMargin(getDouble(rs, c++));
        cl.setProdInsurDeemedBenefit(getDouble(rs, c++));
        cl.setProgramYearPaymentsReceived(getDouble(rs, c++));
        cl.setAllocatedReferenceMargin(getDouble(rs, c++));
        cl.setRepaymentOfCashAdvances(getDouble(rs, c++));
        cl.setTotalPayment(getDouble(rs, c++));
        cl.setSupplyManagedCommoditiesAdj(getDouble(rs, c++));
        cl.setProducerShare(getDouble(rs, c++));
        cl.setFederalContributions(getDouble(rs, c++));
        cl.setProvincialContributions(getDouble(rs, c++));
        cl.setInterimContributions(getDouble(rs, c++));
        cl.setWholeFarmAllocation(getDouble(rs, c++));
        cl.setTier2MarginDecline(getDouble(rs, c++));
        cl.setTier3MarginDecline(getDouble(rs, c++));
        cl.setTier2Benefit(getDouble(rs, c++));
        cl.setTier3Benefit(getDouble(rs, c++));
        cl.setMarginDecline(getDouble(rs, c++));
        cl.setNegativeMarginDecline(getDouble(rs, c++));
        cl.setNegativeMarginBenefit(getDouble(rs, c++));
        cl.setTotalBenefit(getDouble(rs, c++));
        cl.setAdjustedReferenceMargin(getDouble(rs, c++));
        cl.setUnadjustedReferenceMargin(getDouble(rs, c++));
        cl.setReferenceMarginLimit(getDouble(rs, c++));
        cl.setReferenceMarginLimitCap(getDouble(rs, c++));
        cl.setReferenceMarginLimitForBenefitCalc(getDouble(rs, c++));
        cl.setStructuralChangeMethodCode(getString(rs, c++));
        cl.setStructuralChangeMethodCodeDescription(getString(rs, c++)); 
        cl.setExpenseStructuralChangeMethodCode(getString(rs, c++));
        cl.setExpenseStructuralChangeMethodCodeDescription(getString(rs, c++));

        Integer scId = getInteger(rs, c++);

        cl.setTier2Trigger(getDouble(rs, c++));
        cl.setTier3Trigger(getDouble(rs, c++));
        cl.setBenefitBeforeDeductions(getDouble(rs, c++));
        cl.setBenefitAfterProdInsDeduction(getDouble(rs, c++));
        cl.setAppliedBenefitPercent(getDouble(rs, c++));
        cl.setBenefitAfterAppliedBenefitPercent(getDouble(rs, c++));
        cl.setInterimBenefitPercent(getDouble(rs, c++));
        cl.setBenefitAfterInterimDeduction(getDouble(rs, c++));
        cl.setPaymentCap(getDouble(rs, c++));
        cl.setBenefitAfterPaymentCap(getDouble(rs, c++));
        cl.setLateEnrolmentPenalty(getDouble(rs, c++));
        cl.setBenefitAfterLateEnrolmentPenalty(getDouble(rs, c++));
        cl.setLateEnrolmentPenaltyAfterAppliedBenefitPercent(getDouble(rs, c++));
        
        cl.setStandardBenefit(getDouble(rs, c++));
        cl.setEnhancedMarginDecline(getDouble(rs, c++));
        cl.setEnhancedReferenceMarginForBenefitCalculation(getDouble(rs, c++));
        cl.setEnhancedPositiveMarginDecline(getDouble(rs, c++));
        cl.setEnhancedPositiveMarginBenefit(getDouble(rs, c++));
        cl.setEnhancedNegativeMarginDecline(getDouble(rs, c++));
        cl.setEnhancedNegativeMarginBenefit(getDouble(rs, c++));
        cl.setEnhancedBenefitBeforeDeductions(getDouble(rs, c++));
        cl.setEnhancedBenefitAfterProdInsDeduction(getDouble(rs, c++));
        cl.setEnhancedBenefitAfterInterimDeduction(getDouble(rs, c++));
        cl.setEnhancedBenefitAfterPaymentCap(getDouble(rs, c++));
        cl.setEnhancedTotalBenefit(getDouble(rs, c++));
        cl.setEnhancedAdditionalBenefit(getDouble(rs, c++));
        cl.setEnhancedLateEnrolmentPenalty(getDouble(rs, c++));
        cl.setEnhancedBenefitAfterLateEnrolmentPenalty(getDouble(rs, c++));
        cl.setEnhancedBenefitAfterAppliedBenefitPercent(getDouble(rs, c++));
        cl.setEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent(getDouble(rs, c++));
        cl.setRatioAdjustedReferenceMargin(getDouble(rs, c++));
        cl.setAdditiveAdjustedReferenceMargin(getDouble(rs, c++));
        cl.setRevisionCount(getInteger(rs, c++));
        cl.setProdInsurDeemedBenefitManuallyCalculated(getIndicator(rs, c++));

        r.put(scId, cl);
      }

      return r;
    } finally {

    	close(rs, proc);
    }
  }
  
  /* FUNCTION READ_SC_STATE_AUDITS(Sc_Id in NUMBER) RETURN SYS_REFCURSOR; */
  /**
   * @param scenarioId Integer
   * 
   * @return HashMap<Sc Id, Benefit>
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final List<ScenarioStateAudit> readScenarioStateAudits(final Integer scenarioId)
  throws SQLException {
    
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    
    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + READ_STATE_AUDITS_PROC,
          READ_STATE_AUDITS_PARAM, true);
      
      int c = 1;
      proc.setInt(c++, scenarioId);
      proc.execute();
      
      rs = proc.getResultSet();
      
      List<ScenarioStateAudit> audits = new ArrayList<>();
      
      while (rs.next()) {
        ScenarioStateAudit ssa = new ScenarioStateAudit();
        
        c = 1;
        
        ssa.setScenarioStateAudtId(getInteger(rs, c++));
        ssa.setScenarioStateCode(getString(rs, c++));
        ssa.setScenarioStateCodeDesc(getString(rs, c++));
        ssa.setStateChangeReason(getString(rs, c++));
        ssa.setStateChangedByUserId(getString(rs, c++));
        ssa.setStateChangeTimestamp(getDate(rs, c++));
        ssa.setRevisionCount(getInteger(rs, c++));
        
        audits.add(ssa);
      }
      
      return audits;
    } finally {
      
      close(rs, proc);
    }
  }
  
  
  /* FUNCTION READ_SC_STATE_LOGS(Sc_Id in NUMBER) RETURN SYS_REFCURSOR; */
  /**
   * @param scenarioId Integer
   * 
   * @return HashMap<Sc Id, Benefit>
   * 
   * @throws SQLException
   *           SQLException
   */
  public final List<ScenarioLog> readScenarioLogs(final Integer scenarioId)
  throws SQLException {
  	List<ScenarioLog> logs = new ArrayList<>();
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    
    try {
      proc = new DAOStoredProcedure(
      		conn, 
      		PACKAGE_NAME + "." + READ_SC_LOGS_PROC,
          READ_SC_LOGS_PARAM, 
          true);
      
      int c = 1;
      proc.setInt(c++, scenarioId);
      proc.execute();
      rs = proc.getResultSet();
      
      while (rs.next()) {
        ScenarioLog log = new ScenarioLog();
        
        c = 1;
        log.setLogMessage(getString(rs, c++));
        log.setUserId(getString(rs, c++));
        log.setLogDate(getDate(rs, c++));
        
        logs.add(log);
      }
    } finally {
      close(rs, proc);
    }
    
    return logs;
  }

  /* FUNCTION READ_SC_TOT_MGN(sc_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param scenarioIds
   *          Integer[]
   * 
   * @return HashMap <sc_id, MarginTotal>
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, MarginTotal> readScenarioTotalMargin(final Integer[] scenarioIds)
      throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_TOT_MGN_PROC, READ_TOT_MGN_PARAM, true);

      int c = 1;
      Array oracleArray = createNumbersOracleArray(scenarioIds);
      proc.setArray(c++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, MarginTotal> r = new HashMap<>();

      while (rs.next()) {
        MarginTotal mt = new MarginTotal();

        c = 1;

        mt.setMarginTotalId(getInteger(rs, c++));
        mt.setAccrualAdjsCropInventory(getDouble(rs, c++));
        mt.setAccrualAdjsLvstckInventory(getDouble(rs, c++));
        mt.setAccrualAdjsPayables(getDouble(rs, c++));
        mt.setAccrualAdjsPurchasedInputs(getDouble(rs, c++));
        mt.setAccrualAdjsReceivables(getDouble(rs, c++));
        mt.setStructuralChangeAdjs(getDouble(rs, c++));
        mt.setTotalAllowableExpenses(getDouble(rs, c++));
        mt.setTotalAllowableIncome(getDouble(rs, c++));
        mt.setUnadjustedProductionMargin(getDouble(rs, c++));
        mt.setProductionMargAccrAdjs(getDouble(rs, c++));
        mt.setProductionMargAftStrChangs(getDouble(rs, c++));
        
        Integer scId = getInteger(rs, c++);
        mt.setFiscalYearProRateAdj(getDouble(rs, c++));
        
        mt.setFarmSizeRatio(getDouble(rs, c++));
        mt.setExpenseFarmSizeRatio(getDouble(rs, c++));
        
        //
        // With 2.1.2 users wanted the structural change notable indicator for 
        // in progress benefits to be recalculated. To do this the field
        // was set to null in the database, and the benefit calculator uses
        // that as a sign that it must be recalcualted.
        //
        mt.setIsStructuralChangeNotable(getOptionalIndicator(rs, c++));
        mt.setBpuLeadInd(getIndicator(rs, c++));
        mt.setSupplyManagedCommodityIncome(getDouble(rs, c++));
        mt.setUnadjustedAllowableIncome(getDouble(rs, c++));
        mt.setYardageIncome(getDouble(rs, c++));
        mt.setProgramPaymentIncome(getDouble(rs, c++));
        mt.setTotalUnallowableIncome(getDouble(rs, c++));
        mt.setUnadjustedAllowableExpenses(getDouble(rs, c++));
        mt.setYardageExpenses(getDouble(rs, c++));
        mt.setContractWorkExpenses(getDouble(rs, c++));
        mt.setManualExpenses(getDouble(rs, c++));
        mt.setTotalUnallowableExpenses(getDouble(rs, c++));
        mt.setDeferredProgramPayments(getDouble(rs, c++));
        mt.setExpenseAccrualAdjs(getDouble(rs, c++));
        mt.setExpenseStructuralChangeAdjs(getDouble(rs, c++));
        mt.setExpensesAfterStructuralChange(getDouble(rs, c++));
        mt.setRatioStructuralChangeAdjs(getDouble(rs, c++));
        mt.setAdditiveStructuralChangeAdjs(getDouble(rs, c++));
        mt.setRatioProductionMargAftStrChangs(getDouble(rs, c++));
        mt.setAdditiveProductionMargAftStrChangs(getDouble(rs, c++));
        mt.setIsRatioStructuralChangeNotable(getOptionalIndicator(rs, c++));
        mt.setIsAdditiveStructuralChangeNotable(getOptionalIndicator(rs, c++));
        
        mt.setRevisionCount(getInteger(rs, c++));
        
        r.put(scId, mt);
      }

      return r;
    } finally {
    	close(rs, proc);
    }
  }

  /* FUNCTION READ_SC_MGN(sc_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param scIds Integer[]
   * 
   * @return HashMap <op_id, Margin>
   * 
   * @throws SQLException SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, Margin> readScenarioMargin(final Integer[] scIds)
      throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn,
          PACKAGE_NAME + "." + READ_MARGIN_PROC, READ_MARGIN_PARAM, true);

      int c = 1;
      Array oracleArray = createNumbersOracleArray(scIds);
      proc.setArray(c++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, Margin> r = new HashMap<>();

      while (rs.next()) {
        Margin m = new Margin();

        c = 1;

        m.setMarginId(getInteger(rs, c++));
        m.setAccrualAdjsCropInventory(getDouble(rs, c++));
        m.setAccrualAdjsLvstckInventory(getDouble(rs, c++));
        m.setAccrualAdjsPayables(getDouble(rs, c++));
        m.setAccrualAdjsPurchasedInputs(getDouble(rs, c++));
        m.setAccrualAdjsReceivables(getDouble(rs, c++));
        m.setTotalAllowableExpenses(getDouble(rs, c++));
        m.setTotalAllowableIncome(getDouble(rs, c++));
        m.setUnadjustedProductionMargin(getDouble(rs, c++));
        m.setProductionMargAccrAdjs(getDouble(rs, c++));
        
        Integer opId = getInteger(rs, c++);
        c++; // skip AgristabilityScenarioId
        
        m.setSupplyManagedCommodityIncome(getDouble(rs, c++));
        m.setUnadjustedAllowableIncome(getDouble(rs, c++));
        m.setYardageIncome(getDouble(rs, c++));
        m.setProgramPaymentIncome(getDouble(rs, c++));
        m.setTotalUnallowableIncome(getDouble(rs, c++));
        m.setUnadjustedAllowableExpenses(getDouble(rs, c++));
        m.setYardageExpenses(getDouble(rs, c++));
        m.setContractWorkExpenses(getDouble(rs, c++));
        m.setManualExpenses(getDouble(rs, c++));
        m.setTotalUnallowableExpenses(getDouble(rs, c++));
        m.setDeferredProgramPayments(getDouble(rs, c++));
        m.setExpenseAccrualAdjs(getDouble(rs, c++));
        m.setRevisionCount(getInteger(rs, c++));
        m.setProdInsurDeemedSubtotal(getDouble(rs, c++));
        m.setProdInsurDeemedTotal(getDouble(rs, c++));

        r.put(opId, m);
      }

      if(r.size() > 0) {
        return r;
      }
      return null;
    } finally {
    	close(rs, proc);
    }
  }

  
  /**
   * @param operationIds Integer[]
   * @param scenarioIds Integer[]
   * 
   * @return HashMap<String, CropUnitConversion> 
   * will return a HashMap of Inventory Item code to CropUnitConversion object
   * 
   * @throws SQLException
   *           
   */  
  @SuppressWarnings("resource")
  public final HashMap<String, CropUnitConversion> readSupplementalInvCropConvInfo(final Integer[] operationIds,
      final Integer[] scenarioIds) throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + READ_CROP_UNIT_CONVERSIONS_PROC,
          READ_CROP_UNIT_CONVERSIONS_PARAM, true);

      int c = 1;
      Array oracleArrayOperationIds = createNumbersOracleArray(operationIds);
      proc.setArray(c++, oracleArrayOperationIds);

      Array oracleArrayScenarioIds = createNumbersOracleArray(scenarioIds);
      proc.setArray(c++, oracleArrayScenarioIds);

      proc.execute();

      rs = proc.getResultSet();

      HashMap<String, CropUnitConversion> invCropConversions = new HashMap<>();

      while (rs.next()) {
        c = 1;

        String inventoryItemCode = getString(rs, c++);
        String defaultCropUnitCode = getString(rs, c++);
        String defaultCropUnitCodeDescription = getString(rs, c++);
        String targetCropUnitCode = getString(rs, c++);
        String targetCropUnitCodeDescription = getString(rs, c++);
        BigDecimal conversionFactor = getBigDecimal(rs, c++);
        
        CropUnitConversion cropUnitConversion = invCropConversions.get(inventoryItemCode);
        if (cropUnitConversion == null) {
          cropUnitConversion = new CropUnitConversion();
          cropUnitConversion.setInventoryItemCode(inventoryItemCode);
          cropUnitConversion.setDefaultCropUnitCode(defaultCropUnitCode);
          cropUnitConversion.setDefaultCropUnitCodeDescription(defaultCropUnitCodeDescription);
          invCropConversions.put(inventoryItemCode, cropUnitConversion);
        }
        
        CropUnitConversionItem cropUnitConversionItem = new CropUnitConversionItem();
        cropUnitConversionItem.setConversionFactor(conversionFactor);
        cropUnitConversionItem.setTargetCropUnitCode(targetCropUnitCode);
        cropUnitConversionItem.setTargetCropUnitCodeDescription(targetCropUnitCodeDescription);
        
        cropUnitConversion.getConversionItems().add(cropUnitConversionItem);
        
      }

      return invCropConversions;
    } finally {
        close(rs, proc);
    }
  }
  
  

  /* FUNCTION READ_OP_INV(op_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param operationIds Integer[]
   * @param scenarioIds Integer[]
   * 
   * @return HashMap<op_id,[List<InventoryItem>]> Array is [Crop, Livestock,
   *         Payable, Input, Receivable]
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, List<Object>[]> readSupplementalInv(final Integer[] operationIds,
      final Integer[] scenarioIds,
      final Date verifiedDate) throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + READ_INV_PROC,
          READ_INV_PARAM, true);

      int c = 1;
      Array oracleArrayOperationIds = createNumbersOracleArray(operationIds);
      proc.setArray(c++, oracleArrayOperationIds);

      Array oracleArrayScenarioIds = createNumbersOracleArray(scenarioIds);
      proc.setArray(c++, oracleArrayScenarioIds);
      
      proc.setDate(c++, verifiedDate);

      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, List<Object>[]> r = new HashMap<>();

      while (rs.next()) {

        c = 1;

        Integer opId = getInteger(rs, c++);
        Integer xrefId = getInteger(rs, c++);

        String invClassCode = getString(rs, c++);
        String invClassDesc = getString(rs, c++);
        String invItemCode = getString(rs, c++);
        String invItemDesc = getString(rs, c++);
        String rollupInvItemCode = getString(rs, c++);
        String rollupInvItemDesc = getString(rs, c++);
        String invGroupCode = getString(rs, c++);
        String invGroupDesc = getString(rs, c++);
        Boolean isEligible = getIndicator(rs, c++);
        String commodityTypeCode = getString(rs, c++);
        String commodityTypeCodeDescription = getString(rs, c++);
        String fruitVegTypeCode = getString(rs, c++);
        String fruitVegTypeCodeDesc = getString(rs, c++);
        Integer lineItem = getInteger(rs, c++);
        String lineItemDesc = getString(rs, c++);
        String multiStageCommodityCode = getString(rs, c++);
        String multiStageCommodityCodeDesc = getString(rs, c++);
        Double revenueVarianceLimit = getDouble(rs, c++);
        if (revenueVarianceLimit != null) {
          revenueVarianceLimit = revenueVarianceLimit.doubleValue()/100;
        }
        String cropUnitCode = getString(rs, c++);
        String cropUnitDesc = getString(rs, c++);
        Boolean isMarketCommodity = getIndicator(rs, c++);

        // these are mostly here to avoid casts and allow for easy checks
        CropItem cropItem = null;
        LivestockItem liveStockItem = null;
        PayableItem payableItem = null;
        InputItem inputItem = null;
        ReceivableItem receivableItem = null;

        InventoryItem inventoryItem = null;

        // Extract the 5 lists ... and init where required.
        List<Object>[] lists = r.get(opId);
        // assume if the list array exists I created it
        if (lists == null) {
          lists = new List[5];
          lists[0] = new ArrayList<>();
          lists[1] = new ArrayList<>();
          lists[2] = new ArrayList<>();
          lists[3] = new ArrayList<>();
          lists[4] = new ArrayList<>();

          r.put(opId, lists);
        }

        // figure out what to build ...
        if (invClassCode == null || InventoryClassCodes.UNKNOWN.equals(invClassCode)) {
          // per CB Dec 16.
          throw new SQLException(
              "Inventory Class Code of Unknown not supported");
        } else if (InventoryClassCodes.CROP.equals(invClassCode)) {
          cropItem = new CropItem();
          inventoryItem = cropItem;
          lists[0].add(inventoryItem);
        } else if (InventoryClassCodes.LIVESTOCK.equals(invClassCode)) {
          liveStockItem = new LivestockItem();
          inventoryItem = liveStockItem;
          lists[1].add(inventoryItem);
        } else if (InventoryClassCodes.PAYABLE.equals(invClassCode)) {
          payableItem = new PayableItem();
          inventoryItem = payableItem;
          lists[2].add(inventoryItem);
        } else if (InventoryClassCodes.INPUT.equals(invClassCode)) {
          inputItem = new InputItem();
          inventoryItem = inputItem;
          lists[3].add(inventoryItem);
        } else if (InventoryClassCodes.RECEIVABLE.equals(invClassCode)) {
          receivableItem = new ReceivableItem();
          inventoryItem = receivableItem;
          lists[4].add(inventoryItem);
        } else {
          throw new SQLException("Unknown Inventory Class Code "
              + invClassCode + " was found.");
        }

        inventoryItem.setCommodityXrefId(xrefId);
        inventoryItem.setInventoryClassCode(invClassCode);
        inventoryItem.setInventoryClassCodeDescription(invClassDesc);
        inventoryItem.setInventoryItemCode(invItemCode);
        inventoryItem.setInventoryItemCodeDescription(invItemDesc);
        inventoryItem.setRollupInventoryItemCode(rollupInvItemCode);
        inventoryItem.setRollupInventoryItemCodeDescription(rollupInvItemDesc);
        inventoryItem.setInventoryGroupCode(invGroupCode);
        inventoryItem.setInventoryGroupCodeDescription(invGroupDesc);
        inventoryItem.setIsEligible(isEligible);
        inventoryItem.setCommodityTypeCode(commodityTypeCode);
        inventoryItem.setCommodityTypeCodeDescription(commodityTypeCodeDescription);
        inventoryItem.setFruitVegTypeCode(fruitVegTypeCode);
        inventoryItem.setFruitVegTypeCodeDescription(fruitVegTypeCodeDesc);
        inventoryItem.setMultiStageCommodityCode(multiStageCommodityCode);
        inventoryItem.setMultiStageCommodityCodeDescription(multiStageCommodityCodeDesc);
        inventoryItem.setLineItem(lineItem);
        inventoryItem.setLineItemDescription(lineItemDesc);
        inventoryItem.setRevenueVarianceLimit(revenueVarianceLimit);

        if (cropItem != null) {
          cropItem.setCropUnitCode(cropUnitCode);
          cropItem.setCropUnitCodeDescription(cropUnitDesc);
        }
        
        if(liveStockItem != null) {
          liveStockItem.setIsMarketCommodity(isMarketCommodity);
        }

        // CRA Data
        inventoryItem.setReportedInventoryId(getInteger(rs, c++));

        inventoryItem.setReportedPriceStart(getDouble(rs, c++));
        inventoryItem.setReportedPriceEnd(getDouble(rs, c++));
        inventoryItem.setReportedEndYearProducerPrice(getDouble(rs, c++));
        inventoryItem.setReportedQuantityEnd(getDouble(rs, c++));
        inventoryItem.setReportedQuantityStart(getDouble(rs, c++));
        inventoryItem.setReportedStartOfYearAmount(getDouble(rs, c++));
        inventoryItem.setReportedEndOfYearAmount(getDouble(rs, c++));

        if (cropItem != null) {
          cropItem.setReportedQuantityProduced(getDouble(rs, c++));
          cropItem.setOnFarmAcres(getDouble(rs, c++));
          cropItem.setUnseedableAcres(getDouble(rs, c++));
        } else {
          c++;
          c++;
          c++;
        }

        inventoryItem.setReportedIsAcceptProducerPrice(getIndicator(rs, c++));
        inventoryItem.setReportedAarmReferenceP1Price(getDouble(rs, c++));
        inventoryItem.setReportedAarmReferenceP2Price(getDouble(rs, c++));
        // skip Cra_Revision_Count
        c++;

        // Adjusted Data
        inventoryItem.setAdjInventoryId(getInteger(rs, c++));

        inventoryItem.setAdjPriceStart(getDouble(rs, c++));
        inventoryItem.setAdjPriceEnd(getDouble(rs, c++));
        inventoryItem.setAdjEndYearProducerPrice(getDouble(rs, c++));
        inventoryItem.setAdjQuantityEnd(getDouble(rs, c++));
        inventoryItem.setAdjQuantityStart(getDouble(rs, c++));
        inventoryItem.setAdjStartOfYearAmount(getDouble(rs, c++));
        inventoryItem.setAdjEndOfYearAmount(getDouble(rs, c++));

        if (cropItem != null) {
          cropItem.setAdjQuantityProduced(getDouble(rs, c++));
        } else {
          c++;
        }
        c++; // skip Adj_On_Farm_Acres
        c++; // skip Adj_Unseedable_Acres

        inventoryItem.setAdjIsAcceptProducerPrice(getIndicator(rs, c++));
        inventoryItem.setAdjAarmReferenceP1Price(getDouble(rs, c++));
        inventoryItem.setAdjAarmReferenceP2Price(getDouble(rs, c++));
        // skip Agristability_Scenario_Id
        c++;
        inventoryItem.setAdjustedByUserId(getString(rs, c++));
        inventoryItem.setRevisionCount(getInteger(rs, c++));
      }

      return r;
    } finally {

    	close(rs, proc);
    }
  }

  /**
   * @param opId Integer
   * @return HashMap <incCode,List<FmvFullResult>> set of
   *         [cropUnit,start,end,variance,yearlyAvg,prevYearEndPrice]
   * @throws SQLException SQLException
   */
  public HashMap<String, List<FmvFullResult>> readFairMarketValue(Integer opId) throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    HashMap<String, List<FmvFullResult>> r;

    try {
      proc = new DAOStoredProcedure(conn,
          PACKAGE_NAME + "." + READ_OP_FMV_PROC, READ_OP_FMV_PARAM, true);

      int c = 1;
      proc.setInt(c++, opId);
      proc.execute();

      rs = proc.getResultSet();
      
      r = readFMVResultSet(opId, rs);
    } finally {

      close(rs, proc);
    }
    
    readFairMarketValuePreviousYear(opId, r);

    if (r.size() > 0) {
      return r;
    }

    return null;
  }
  
  /**
   * @param opId Integer
   * @return HashMap <incCode,List<FmvFullResult>> set of
   *         [cropUnit,start,end,variance,yearlyAvg,prevYearEndPrice]
   * @throws SQLException SQLException
   */
  public HashMap<String, List<FmvFullResult>> readFairMarketValue(Integer opId,
      String inventoryItemCode,
      String cropUnitCode) throws SQLException {
    
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    
    HashMap<String, List<FmvFullResult>> r;
    
    try {
      proc = new DAOStoredProcedure(conn,
          PACKAGE_NAME + "." + READ_OP_SINGLE_FMV_PROC, READ_OP_SINGLE_FMV_PARAM, true);
      
      int c = 1;
      proc.setInt(c++, opId);
      proc.setString(c++, inventoryItemCode);
      proc.setString(c++, cropUnitCode);
      proc.execute();
      
      rs = proc.getResultSet();
      
      r = readFMVResultSet(opId, rs);
    } finally {
      
      close(rs, proc);
    }
    
    readFairMarketValuePreviousYear(opId, r);
    
    if (r.size() > 0) {
      return r;
    }
    
    return null;
  }
  
  public Integer readProgramYearId(Integer clientId, Integer programYear) throws SQLException {
    
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    Integer programYearId = null;
    
    try {
      proc = new DAOStoredProcedure(conn,
          PACKAGE_NAME + "." + READ_PROGRAM_YEAR_ID_BY_CLIENT_ID_PROC, READ_PROGRAM_YEAR_ID_BY_CLIENT_ID_PARAM, true);
      
      int c = 1;
      proc.setInt(c++, clientId);
      proc.setInt(c++, programYear);
      proc.execute();
      
      rs = proc.getResultSet();
      
      while (rs.next()) {
        programYearId = getInteger(rs, 1);
      }
      
    } finally {
      
      close(rs, proc);
    }
    return programYearId;
  }

  @SuppressWarnings({ "null" })
  private HashMap<String, List<FmvFullResult>> readFMVResultSet(Integer opId, ResultSet rs) throws SQLException {
    HashMap<String, List<FmvFullResult>> r;
    int c;
    r = new HashMap<>();
    
    FmvSingleResult prev = null; // previous FMV result
    FmvSingleResult prevPrev = null; // previous to previous FMV result

    while (rs.next()) {

      c = 1;
      
      FmvSingleResult fmv = new FmvSingleResult();

      fmv.setInvCode(getString(rs, c++));
      fmv.setCropUnit(getString(rs, c++));
      c++; // ignore Period
      fmv.setPrice(getDouble(rs, c++));
      fmv.setVariance(getDouble(rs, c++));
      fmv.setYearAvg(getDouble(rs, c++));
      
      if(fmv.getInvCode() == null || fmv.getCropUnit() == null || fmv.getPrice() == null
          || fmv.getVariance() == null) {
        throw new SQLException("Found null values for FMVs for opId: " + opId);
      }
      
      // only process pairs of rows (inventory code and crop unit code combination).
      // Ignore singles. Throw an error if there are more than two.
      if(prev != null && prev.getInvCode().equals(fmv.getInvCode())
          && prev.getCropUnit().equals(fmv.getCropUnit())) {
        if(prevPrev != null && prev.getInvCode().equals(prevPrev.getInvCode())
            && prev.getCropUnit().equals(prevPrev.getCropUnit())) {
          throw new SQLException(
              "Expected FMVs to be returned as pairs or singles. Found more than two for opId: " + opId);
        }
        if(fmv.getYearAvg() == null) {
          throw new SQLException("Null year average found for second FMV in pair for opId: " + opId);
        }
        
        List<FmvFullResult> fmvList = r.get(fmv.getInvCode());
        if(fmvList == null){
          fmvList = new ArrayList<>();
          r.put(fmv.getInvCode(), fmvList);
        }
        fmvList.add(new FmvFullResult(fmv.getInvCode(), fmv.getCropUnit(), prev.getPrice(), fmv.getPrice(),
            fmv.getVariance(), fmv.getYearAvg(), null ));
      }
      
      prevPrev = prev;
      prev = fmv;
    }
    return r;
  }
  
  /**
   * @param opId Integer
   * @param r HashMap
   * @throws SQLException SQLException
   */
  @SuppressWarnings("resource")
  private void readFairMarketValuePreviousYear(Integer opId, HashMap<String, List<FmvFullResult>> r) throws SQLException {
    
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    
    try {
      proc = new DAOStoredProcedure(conn,
          PACKAGE_NAME + "." + READ_OP_FMV_PREV_YEAR_PROC, READ_OP_FMV_PREV_YEAR_PARAM, true);
      
      int c = 1;
      proc.setInt(c++, opId);
      proc.execute();
      
      rs = proc.getResultSet();
      
      while (rs.next()) {
        
        c = 1;
        
        String invCode = getString(rs, c++);
        String cropUnit = getString(rs, c++);
        Double prevYearEndPrice = getDouble(rs, c++);
        
        if(invCode == null || cropUnit == null || prevYearEndPrice == null) {
          throw new SQLException("Found null values for FMVs for opId: " + opId);
        }
        
        List<FmvFullResult> fmvList = r.get(invCode);
        if(fmvList == null){
          fmvList = new ArrayList<>();
          r.put(invCode, fmvList);
        }
        FmvFullResult fmvValues = null;
        for(Iterator<FmvFullResult> fi = fmvList.iterator(); fi.hasNext(); ) {
          FmvFullResult curFmv = fi.next();
          if(cropUnit.equals(curFmv.getCropUnit())) {
            fmvValues = curFmv;
            break;
          }
        }

        if(fmvValues != null) {
          fmvValues.setPreviousYearEnd(prevYearEndPrice);
        } else {
          fmvList.add(new FmvFullResult(invCode, cropUnit, null, null,
              null, null, prevYearEndPrice ));
        }
      }
      
    } finally {
      close(rs, proc);
    }
  }
  
  
  private class FmvSingleResult {
    private String invCode;
    private String cropUnit;
    private Double price;
    private Double variance;
    private Double yearAvg;
    
    FmvSingleResult() {
    }
    
    public String getCropUnit() {
      return cropUnit;
    }
    
    public void setCropUnit(String cropUnit) {
      this.cropUnit = cropUnit;
    }
    
    public String getInvCode() {
      return invCode;
    }
    
    public void setInvCode(String invCode) {
      this.invCode = invCode;
    }
    
    public Double getPrice() {
      return price;
    }
    
    public void setPrice(Double price) {
      this.price = price;
    }
    
    public Double getVariance() {
      return variance;
    }
    
    public void setVariance(Double variance) {
      this.variance = variance;
    }
    
    public Double getYearAvg() {
      return yearAvg;
    }
    
    public void setYearAvg(Double yearAvg) {
      this.yearAvg = yearAvg;
    }
  }
  
  
  public static class FmvFullResult {
    private String invCode;
    private String cropUnit;
    private Double startPrice;
    private Double endPrice;
    private Double variance;
    private Double yearAvg;
    private Double previousYearEnd;
    
    final int iUnit = 0;
    final int iFmvStart = 1;
    final int iFmvEnd = 2;
    final int iFmvVariance = 3;
    final int iFmvAverage = 4;
    final int iFmvPreviousYearEnd = 5;
    
    public FmvFullResult(
        String invCode,
        String cropUnit,
        Double startPrice,
        Double endPrice,
        Double variance,
        Double yearAvg,
        Double previousYearEnd) {
      this.cropUnit = cropUnit;
      this.startPrice = startPrice;
      this.endPrice = endPrice;
      this.variance = variance;
      this.yearAvg = yearAvg;
      this.previousYearEnd = previousYearEnd;
      this.invCode = invCode;
    }

    public Double getStartPrice() {
      return startPrice;
    }

    public void setStartPrice(Double startPrice) {
      this.startPrice = startPrice;
    }

    public Double getEndPrice() {
      return endPrice;
    }

    public void setEndPrice(Double endPrice) {
      this.endPrice = endPrice;
    }

    public Double getPreviousYearEnd() {
      return previousYearEnd;
    }

    public void setPreviousYearEnd(Double previousYearEnd) {
      this.previousYearEnd = previousYearEnd;
    }

    public String getCropUnit() {
      return cropUnit;
    }
    
    public void setCropUnit(String cropUnit) {
      this.cropUnit = cropUnit;
    }
    
    public String getInvCode() {
      return invCode;
    }
    
    public void setInvCode(String invCode) {
      this.invCode = invCode;
    }
    
    public Double getVariance() {
      return variance;
    }
    
    public void setVariance(Double variance) {
      this.variance = variance;
    }
    
    public Double getYearAvg() {
      return yearAvg;
    }
    
    public void setYearAvg(Double yearAvg) {
      this.yearAvg = yearAvg;
    }
  }


  /* FUNCTION READ_OP_PUC(op_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param operationIds Integer[]
   * @param scenarioIds Integer[]
   * @return ProductiveUnitCapacity[]
   * @throws SQLException SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, List<ProductiveUnitCapacity>> readProductiveUnitCapacity(final Integer[] operationIds,
      final Integer[] scenarioIds) throws SQLException {

    final int paramCount = 2;
    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + READ_PUC_PROC,
          paramCount, true);

      int c = 1;
      Array oracleArrayOperationIds = createNumbersOracleArray(operationIds);
      proc.setArray(c++, oracleArrayOperationIds);

      Array oracleArrayScenarioIds = createNumbersOracleArray(scenarioIds);
      proc.setArray(c++, oracleArrayScenarioIds);

      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, List<ProductiveUnitCapacity>> r = new HashMap<>();

      while (rs.next()) {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();

        c = 1;

        Integer opId = getInteger(rs, c++);
        puc.setStructureGroupCode(getString(rs, c++));
        puc.setStructureGroupCodeDescription(getString(rs, c++));
        puc.setRollupStructureGroupCode(getString(rs, c++));
        puc.setRollupStructureGroupCodeDescription(getString(rs, c++));
        puc.setInventoryItemCode(getString(rs, c++));
        puc.setInventoryItemCodeDescription(getString(rs, c++));
        puc.setRollupInventoryItemCode(getString(rs, c++));
        puc.setRollupInventoryItemCodeDescription(getString(rs, c++));
        puc.setCommodityTypeCode(getString(rs, c++));
        puc.setCommodityTypeCodeDescription(getString(rs, c++));
        puc.setFruitVegTypeCode(getString(rs, c++));
        puc.setFruitVegTypeCodeDescription(getString(rs, c++));
        puc.setMultiStageCommodityCode(getString(rs, c++));
        puc.setMultiStageCommodityCodeDescription(getString(rs, c++));
        puc.setExpectedProductionCropUnitCode(getString(rs, c++));
        puc.setExpectedProductionPerProductiveUnit(getBigDecimal(rs, c++));

        puc.setReportedProductiveUnitCapacityId(getInteger(rs, c++));
        puc.setReportedAmount(getDouble(rs, c++));
        c++; // skip cra rev count

        puc.setAdjProductiveUnitCapacityId(getInteger(rs, c++));
        puc.setAdjAmount(getDouble(rs, c++));
        c++; // skip scenario id
        puc.setParticipantDataSrcCode(getString(rs, c++));
        puc.setAdjustedByUserId(getString(rs, c++));
        puc.setRevisionCount(getInteger(rs, c++));
        puc.setOnFarmAcres(getDouble(rs, c++));
        puc.setUnseedableAcres(getDouble(rs, c++));

        List<ProductiveUnitCapacity> l = r.get(opId);
        if (l == null) {
          l = new ArrayList<>();
          r.put(opId, l);
        }
        l.add(puc);
        
        if(puc.getStructureGroupCode() == null && puc.getInventoryItemCode() == null) {
          throw new SQLException("Found null structureGroupCode and inventoryItem for PUC for opId: " + opId +
              " adjProductiveUnitCapacityId: " + puc.getAdjProductiveUnitCapacityId() +
              " reportedProductiveUnitCapacityId: " + puc.getReportedProductiveUnitCapacityId());
        }
      }

      return r;
    } finally {

    	close(rs, proc);
    }
  }

  /* FUNCTION READ_OP_IE(op_ids in FARM_ID_TBL) RETURN SYS_REFCURSOR; */
  /**
   * @param operationIds Integer[]
   * @param scenarioIds Integer[]
   * @param programYear programYear
   * @param verifiedDate Date
   * @return ReportedIncomeExpense[]
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final HashMap<Integer, List<IncomeExpense>> readIncomeExpense(
      final Integer[] operationIds,
      final Integer[] scenarioIds,
      final int programYear,
      final Date verifiedDate) throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + READ_IE_PROC,
          READ_IE_PARAM, true);

      int c = 1;
      Array oracleArrayOperationIds = createNumbersOracleArray(operationIds);
      proc.setArray(c++, oracleArrayOperationIds);

      Array oracleArrayScenarioIds = createNumbersOracleArray(scenarioIds);
      proc.setArray(c++, oracleArrayScenarioIds);

      proc.setInt(c++, programYear);
      proc.setDate(c++, verifiedDate);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<Integer, List<IncomeExpense>> r = new HashMap<>();

      while (rs.next()) {
        IncomeExpense ie = new IncomeExpense();
        LineItem li = new LineItem();
        ie.setLineItem(li);

        c = 1;

        Integer opId = getInteger(rs, c++);

        li.setProgramYear(getInteger(rs, c++));
        li.setLineItemId(getInteger(rs, c++));
        li.setLineItem(getInteger(rs, c++));
        li.setDescription(getString(rs, c++));
        li.setProvince(getString(rs, c++));
        li.setIsEligible(getIndicator(rs, c++));
        li.setIsEligibleRefYears(getIndicator(rs, c++));
        li.setIsYardage(getIndicator(rs, c++));
        li.setIsProgramPayment(getIndicator(rs, c++));
        li.setIsContractWork(getIndicator(rs, c++));
        li.setIsManualExpense(getIndicator(rs, c++));
        li.setIsSupplyManagedCommodity(getIndicator(rs, c++));
        li.setIsExcludeFromRevenueCalculation(getIndicator(rs, c++));
        li.setIsIndustryAverageExpense(getIndicator(rs, c++));
        li.setCommodityTypeCode(getString(rs, c++));
        li.setCommodityTypeCodeDescription(getString(rs, c++));
        li.setFruitVegTypeCode(getString(rs, c++));
        li.setFruitVegTypeCodeDescription(getString(rs, c++));
        c++; // Established_Date
        c++; // Expiry_Date
        li.setRevisionCount(getInteger(rs, c++));

        ie.setReportedIncomeExpenseId(getInteger(rs, c++));
        ie.setReportedAmount(getDouble(rs, c++));
        ie.setIsExpense(getIndicator(rs, c++));

        c++; // Cra_Revision_Count

        ie.setAdjIncomeExpenseId(getInteger(rs, c++));
        ie.setAdjAmount(getDouble(rs, c++));
        c++; // skip scenario id
        ie.setAdjustedByUserId(getString(rs, c++));
        ie.setRevisionCount(getInteger(rs, c++));

        List<IncomeExpense> l = r.get(opId);
        if(l == null){
          l = new ArrayList<>();
          r.put(opId, l);
        }
        l.add(ie);
      }

      return r;
    } finally {

    	close(rs, proc);
    }
  }

  /**
   * @param rs
   *          ResultSet
   * @param i
   *          int
   * 
   * @return String
   * 
   * @throws SQLException
   *           SQLException
   */
  private String getString(final ResultSet rs, final int i) throws SQLException {
    return rs.getString(i);
  }

  /**
   * @param rs
   *          ResultSet
   * @param i
   *          int
   * 
   * @return Boolean
   * 
   * @throws SQLException
   *           SQLException
   */
  private Boolean getIndicator(final ResultSet rs, final int i)
      throws SQLException {
    String value = rs.getString(i);
    Boolean result = Boolean.FALSE;

    try {
      result = Boolean.valueOf(DataParseUtils.parseBoolean(value));
    } catch (ParseException e) {
      throw new SQLException(e.toString());
    }

    return result;
  }
  
  
  /**
   * @param rs
   *          ResultSet
   * @param i
   *          int
   * 
   * @return Boolean
   * 
   * @throws SQLException
   *           SQLException
   */
  private Boolean getOptionalIndicator(final ResultSet rs, final int i)
      throws SQLException {
    String value = rs.getString(i);
    Boolean result = null;

    try {
    	if (!StringUtils.isBlank(value)) {
    		result = Boolean.valueOf(DataParseUtils.parseBoolean(value));
    	}
    } catch (ParseException e) {
      throw new SQLException(e.toString());
    }

    return result;
  }
  

  /**
   * @param rs ResultSet
   * @param i int
   * @return Date
   * @throws SQLException SQLException
   */
  private Date getDate(final ResultSet rs, final int i) throws SQLException {
    Date result = null;
    Timestamp timestamp = rs.getTimestamp(i);
    
    if(timestamp != null) {
      result = new Date(timestamp.getTime());
    }
    
    return result;
  }

  /**
   * @param rs ResultSet
   * @param i int
   * @return Double
   * @throws SQLException SQLException
   */
  private Double getDouble(final ResultSet rs, final int i) throws SQLException {
    double v = rs.getDouble(i);

    if (rs.wasNull()) {
      return null;
    }

    return Double.valueOf(v);
  }

  private BigDecimal getBigDecimal(final ResultSet rs, final int i) throws SQLException {
    String v = rs.getString(i);
    
    if (rs.wasNull()) {
      return null;
    }
    
    return new BigDecimal(v);
  }
  
  /**
   * @param rs
   *          ResultSet
   * @param i
   *          int
   * 
   * @return Integer
   * 
   * @throws SQLException
   *           SQLException
   */
  private Integer getInteger(final ResultSet rs, final int i)
      throws SQLException {
    int v = rs.getInt(i);

    if (rs.wasNull()) {
      return null;
    }

    return Integer.valueOf(v);
  }

  /**
   * 
   * @param pInvCodes pInvCodes
   * @param pStructCodes List
   * @param scid
   *          Integer
   * @param baseyear
   *          Integer
   * @return HashMap[] [HashMap<InvCode, BPU>,HashMap<StructCode, BPU>]
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public HashMap<String, BasePricePerUnit>[] readBasePricePerUnit(List<String> pInvCodes, List<String> pStructCodes,
      Integer scid, Integer baseyear) throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      Array oracleArrayInventoryCodes = createStringOracleArray(pInvCodes);
      Array oracleArrayStructureGroupCodes = createStringOracleArray(pStructCodes);
      
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_BPU_ALL_PROC, READ_BPU_ALL_PARAM, true);

      int c = 1;
      proc.setInt(c++, scid);
      proc.setArray(c++, oracleArrayInventoryCodes);
      proc.setArray(c++, oracleArrayStructureGroupCodes);

      proc.setInt(c++, baseyear);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<String, BasePricePerUnit> r1 = new HashMap<>();
      HashMap<String, BasePricePerUnit> r2 = new HashMap<>();

      if (rs.next()) {
        processBpuResults(rs, r1, r2);
      }

      @SuppressWarnings("unchecked")
      HashMap<String, BasePricePerUnit>[] result = new HashMap[2];
      result[0] = r1.size() > 0 ? r1 : null;
      result[1] = r2.size() > 0 ? r2 : null;
      return result;
    } finally {
    	close(rs, proc);
    }
  }
  
  
  
  /**
   * 
   * @param scIds scIds
   * @param scenarioBpuPurposeCode scenarioBpuPurposeCode
   * @return HashMap[] [HashMap<InvCode, BPU>,HashMap<StructCode, BPU>]
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public HashMap<String, BasePricePerUnit>[] readBasePricePerUnitXrefs(List<Integer> scIds, String scenarioBpuPurposeCode) throws SQLException {
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    final int paramCount = 2;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_BPU_XREF_PROC, paramCount, true);

      Integer[] scIdArray = scIds.toArray(new Integer[scIds.size()]);
      Array oracleArray = createNumbersOracleArray(scIdArray);
      
      int c = 1;
      proc.setArray(c++, oracleArray);
      proc.setString(c++, scenarioBpuPurposeCode);
      proc.execute();

      rs = proc.getResultSet();

      HashMap<String, BasePricePerUnit> r1 = new HashMap<>();
      HashMap<String, BasePricePerUnit> r2 = new HashMap<>();

      if (rs.next()) {
        processBpuResults(rs, r1, r2);
      }

      @SuppressWarnings("unchecked")
      HashMap<String, BasePricePerUnit>[] result = new HashMap[2];
      result[0] = r1.size() > 0 ? r1 : null;
      result[1] = r2.size() > 0 ? r2 : null;
      return result;
    } finally {
    	close(rs, proc);
    }
  }
  
  

	/**
	 * @param rs rs
	 * @param r1 r1
	 * @param r2 r2
	 * @throws SQLException On SQLException
	 */
	private void processBpuResults(ResultSet rs, HashMap<String, BasePricePerUnit> r1, HashMap<String, BasePricePerUnit> r2) throws SQLException {
		boolean cont = true;
		while (cont) {
		  BasePricePerUnit bpu = new BasePricePerUnit();

		  int c = 1;
		  bpu.setBasePricePerUnitId(getInteger(rs, c++));
		  bpu.setMunicipalityCode(getString(rs, c++));
		  bpu.setInventoryCode(getString(rs, c++));
		  bpu.setStructureGroupCode(getString(rs, c++));
		  bpu.setComment(getString(rs, c++));
		  bpu.setRevisionCount(getInteger(rs, c++));

		  if (bpu.getInventoryCode() != null) {
		    r1.put(bpu.getInventoryCode(), bpu);
		  }
		  if (bpu.getStructureGroupCode() != null) {
		    r2.put(bpu.getStructureGroupCode(), bpu);
		  }

		  if (bpu.getBasePricePerUnitId() != null) {
        List<BasePricePerUnitYear> years = new ArrayList<>();

		    while (cont	&& bpu.getBasePricePerUnitId().equals(getInteger(rs, 1))) {
		      c = 7;

		      BasePricePerUnitYear by = new BasePricePerUnitYear();

		      by.setYear(getInteger(rs, c++));
		      by.setMargin(getDouble(rs, c++));
		      by.setExpense(getDouble(rs, c++));
		      by.setRevisionCount(getInteger(rs, c++));

		      years.add(by);

		      if (!rs.next()) {
		        cont = false;
		      }
		    }

		    bpu.setBasePricePerUnitYears(years);
		  }
		}
	}

  /* FUNCTION READ_COB_GEN_DATE(Scenario_Id in NUMBER) RETURN SYS_REFCURSOR; */
  /**
   * @param scenarioId Integer
   * @return Date
   * @throws SQLException On Exception
   */
  @SuppressWarnings("resource")
  public final Date readCobGenerationDate(final Integer scenarioId)
      throws SQLException {

    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    
    Date cobGenerationDate = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_COB_GEN_DATE_PROC, READ_COB_GEN_DATE_PARAM, true);

      int c = 1;
      proc.setInt(c++, scenarioId);
      proc.execute();

      rs = proc.getResultSet();

      if (rs.next()) {
        c = 1;
        cobGenerationDate = getDate(rs, c++);
      }

      return cobGenerationDate;
    } finally {
      close(rs, proc);
    }
  }
  
  /* FUNCTION READ_ENROLMENT(Pin in NUMBER, Enrolment_Year in NUMBER) RETURN SYS_REFCURSOR; */
  /**
   * @param pin Integer
   * @param enrolmentYear Integer
   * @return Enrolment
   * @throws SQLException On Exception
   */
  @SuppressWarnings("resource")
  public final Enrolment readEnrolment(final Integer pin, final Integer enrolmentYear)
  throws SQLException {
    
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    
    Enrolment e = null;
    
    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_ENROLMENT_PROC, READ_ENROLMENT_PARAM, true);
      
      int c = 1;
      proc.setInt(c++, pin);
      proc.setInt(c++, enrolmentYear);
      proc.execute();
      
      rs = proc.getResultSet();
      
      if (rs.next()) {
        e = new Enrolment();
        c = 1;
        e.setClientId(getInteger(rs, c++));
        e.setPin(getInteger(rs, c++));
        e.setFailedToGenerate(getIndicator(rs, c++));
        e.setFailedReason(getString(rs, c++));
        e.setEnrolmentId(getInteger(rs, c++));
        e.setEnrolmentYear(getInteger(rs, c++));
        e.setEnrolmentFee(getDouble(rs, c++));
        e.setGeneratedDate(getDate(rs, c++));
        e.setContributionMarginAverage(getDouble(rs, c++));
        e.setMarginYearMinus2(getDouble(rs, c++));
        e.setMarginYearMinus3(getDouble(rs, c++));
        e.setMarginYearMinus4(getDouble(rs, c++));
        e.setMarginYearMinus5(getDouble(rs, c++));
        e.setMarginYearMinus6(getDouble(rs, c++));
        e.setIsMarginYearMinus2Used(getIndicator(rs, c++));
        e.setIsMarginYearMinus3Used(getIndicator(rs, c++));
        e.setIsMarginYearMinus4Used(getIndicator(rs, c++));
        e.setIsMarginYearMinus5Used(getIndicator(rs, c++));
        e.setIsMarginYearMinus6Used(getIndicator(rs, c++));
        e.setIsGeneratedFromCra(getIndicator(rs, c++));
        e.setIsGeneratedFromEnw(getIndicator(rs, c++));
        e.setIsCreateTaskInBarn(getIndicator(rs, c++));
        e.setRevisionCount(getInteger(rs, c++));
      }
      
      return e;
    } finally {
      close(rs, proc);
    }
  }
  
  /* FUNCTION READ_ENW_ENROLMENT(Agristability_Scenario_Id in NUMBER) RETURN SYS_REFCURSOR; */
  @SuppressWarnings("resource")
  public final EnwEnrolment readEnwEnrolment(final Integer scenarioId)
      throws SQLException {
    
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    
    EnwEnrolment e = null;
    
    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_ENW_ENROLMENT_PROC, READ_ENW_ENROLMENT_PARAM, true);
      
      int c = 1;
      proc.setInt(c++, scenarioId);
      proc.execute();
      
      rs = proc.getResultSet();
      
      if (rs.next()) {
        e = new EnwEnrolment();
        c = 1;
        e.setEnwEnrolmentId(getInteger(rs, c++));
        e.setEnrolmentYear(getInteger(rs, c++));
        e.setEnrolmentFee(getDouble(rs, c++));
        e.setContributionMargin(getDouble(rs, c++));
        
        e.setBenefitCalculated(getIndicator(rs, c++));
        e.setProxyMarginsCalculated(getIndicator(rs, c++));
        e.setManualMarginsCalculated(getIndicator(rs, c++));
        e.setHasProductiveUnits(getIndicator(rs, c++));
        e.setHasBpus(getIndicator(rs, c++));
        
        e.setBenefitEnrolmentFee(getDouble(rs, c++));
        e.setBenefitContributionMargin(getDouble(rs, c++));
        e.setProxyEnrolmentFee(getDouble(rs, c++));
        e.setProxyContributionMargin(getDouble(rs, c++));
        e.setManualEnrolmentFee(getDouble(rs, c++));
        e.setManualContributionMargin(getDouble(rs, c++));
        
        e.setMarginYearMinus2(getDouble(rs, c++));
        e.setMarginYearMinus3(getDouble(rs, c++));
        e.setMarginYearMinus4(getDouble(rs, c++));
        e.setMarginYearMinus5(getDouble(rs, c++));
        e.setMarginYearMinus6(getDouble(rs, c++));
        e.setMarginYearMinus2Used(getIndicator(rs, c++));
        e.setMarginYearMinus3Used(getIndicator(rs, c++));
        e.setMarginYearMinus4Used(getIndicator(rs, c++));
        e.setMarginYearMinus5Used(getIndicator(rs, c++));
        e.setMarginYearMinus6Used(getIndicator(rs, c++));
        
        e.setBenefitMarginYearMinus2(getDouble(rs, c++));
        e.setBenefitMarginYearMinus3(getDouble(rs, c++));
        e.setBenefitMarginYearMinus4(getDouble(rs, c++));
        e.setBenefitMarginYearMinus5(getDouble(rs, c++));
        e.setBenefitMarginYearMinus6(getDouble(rs, c++));
        e.setBenefitMarginYearMinus2Used(getIndicator(rs, c++));
        e.setBenefitMarginYearMinus3Used(getIndicator(rs, c++));
        e.setBenefitMarginYearMinus4Used(getIndicator(rs, c++));
        e.setBenefitMarginYearMinus5Used(getIndicator(rs, c++));
        e.setBenefitMarginYearMinus6Used(getIndicator(rs, c++));
        
        e.setProxyMarginYearMinus2(getDouble(rs, c++));
        e.setProxyMarginYearMinus3(getDouble(rs, c++));
        e.setProxyMarginYearMinus4(getDouble(rs, c++));
        
        e.setManualMarginYearMinus2(getDouble(rs, c++));
        e.setManualMarginYearMinus3(getDouble(rs, c++));
        e.setManualMarginYearMinus4(getDouble(rs, c++));
        
        e.setCombinedFarmPercent(getDouble(rs, c++));
        e.setEnrolmentCalculationTypeCode(getString(rs, c++));
        e.setRevisionCount(getInteger(rs, c++));
      }
      
      return e;
    } finally {
      close(rs, proc);
    }
  }


  /**
   * @param combinedFarmNumber Integer
   * 
   * @return List<CombinedFarmClient>
   * 
   * @throws SQLException
   *           SQLException
   */
  @SuppressWarnings("resource")
  public final List<CombinedFarmClient> readCombinedFarmClients(final Integer combinedFarmNumber) throws SQLException {
    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_COMBINED_FARM_CLIENTS_PROC, READ_COMBINED_FARM_CLIENTS_PARAM, true);

      int c = 1;
      proc.setInt(c++, combinedFarmNumber);
      proc.execute();

      rs = proc.getResultSet();

      List<CombinedFarmClient> l = new ArrayList<>();

      while (rs.next()) {

        CombinedFarmClient cfc = new CombinedFarmClient();
        
        c = 1;
        
        cfc.setScenarioId(getInteger(rs, c++));
        cfc.setScenarioNumber(getInteger(rs, c++));
        cfc.setScenarioRevisionCount(getInteger(rs, c++));
        cfc.setParticipantPin(getInteger(rs, c++));
        cfc.setFirstName(getString(rs,c++));
        cfc.setLastName(getString(rs,c++));
        cfc.setCorpName(getString(rs,c++));
        cfc.setTotalBenefit(getDouble(rs, c++));
        
        l.add(cfc);
      }

      return l;
    } finally {

      close(rs, proc);
    }
  }

  
  /**
   * @param scenario Scenario
   * @throws SQLException SQLException
   */
  public final void readFarmType(Scenario scenario) throws SQLException {
    DAOStoredProcedure proc = null;
    ResultSet rs = null;

    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_FARM_TYPE_PROC, READ_FARM_TYPE_PARAM, true);

      int c = 1;
      proc.setInt(c, scenario.getScenarioId());
      proc.execute();

      rs = proc.getResultSet();

      if (rs.next()) {
        scenario.setFarmTypeCode(getString(rs, c++));
        scenario.setFarmTypeCodeDescription(getString(rs, c++));
      }

    } finally {
      close(rs, proc);
    }
  }
   
  private void close(ResultSet rs, DAOStoredProcedure proc) throws SQLException{
  	if (rs != null) {
      rs.close();
    }

    if (proc != null) {
      proc.close();
    }
  }

  protected OracleConnection getOracleConnection() {
    OracleConnection oracleConnection = (OracleConnection) conn;
    return oracleConnection;
  }

  @SuppressWarnings("resource")
  protected Array createStringOracleArray(List<String> values) throws SQLException {
    return getOracleConnection().createOracleArray(CODE_COLLECTION_TYPE_NAME, values.toArray());
  }
  
  @SuppressWarnings("resource")
  protected Array createNumbersOracleArray(List<Integer> values) throws SQLException {
    return getOracleConnection().createOracleArray(NUM_COLLECTION_TYPE_NAME, values.toArray());
  }

  @SuppressWarnings("resource")
  protected Array createCodesOracleArray(String[] values) throws SQLException {
    return getOracleConnection().createOracleArray(CODE_COLLECTION_TYPE_NAME, values);
  }
  
  @SuppressWarnings("resource")
  protected Array createNumbersOracleArray(Integer[] values) throws SQLException {
    return getOracleConnection().createOracleArray(NUM_COLLECTION_TYPE_NAME, values);
  }
}
