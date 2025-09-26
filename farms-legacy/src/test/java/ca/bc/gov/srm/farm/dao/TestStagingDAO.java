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

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
import ca.bc.gov.srm.farm.util.TestUtils;


/**
 * @author  dzwiers
 */
public class TestStagingDAO {

  private static Connection conn;

  private static StagingDAO dao;

  @BeforeAll
  protected static void setUp() throws Exception {
    conn = TestUtils.openConnection();
    dao = new StagingDAO(conn);
  }

  @AfterAll
  protected static void tearDown() throws Exception {

    if (conn != null) {
      conn.rollback();
      conn.close();
    }
  }

  private Z01ParticipantInfo z01 = null;

  {
    z01 = new Z01ParticipantInfo();

    z01.setParticipantPin(new Integer(1467067));
    z01.setSinCtnBn("627314149");
    z01.setFirstName("JO ANNE");
    z01.setLastName("DELICHTE");
    z01.setCorpName("");
    z01.setAddress1("7001 HIGHWAY 6");
    z01.setAddress2("");
    z01.setCity("COLDSTREAM");
    z01.setProvince("BC");
    z01.setPostalCode("V1B3H1");
    z01.setCountry("CAN");

    z01.setParticipantTypeCode(new Integer(1));
    z01.setParticipantLanguage(new Integer(1));
    z01.setParticipantFax("");
    z01.setParticipantPhoneDay("2502605299");
    z01.setParticipantPhoneEvening("2502605299");

    z01.setContactFirstName("");
    z01.setContactLastName("DELICHTE");
    z01.setContactBusinessName("");
    z01.setContactAddress1("");
    z01.setContactAddress2("");
    z01.setContactCity("");
    z01.setContactProvince("");
    z01.setContactPostalCode("");
    z01.setContactPhoneDay("");
    z01.setContactFaxNumber("");

    z01.setPublicOffice(new Integer(0));
    z01.setIdentEffectiveDate("20091223");
  }

  private Z02PartpntFarmInfo z02 = null;

  {
    z02 = new Z02PartpntFarmInfo();

    z02.setParticipantPin(new Integer(1976547));
    z02.setProgramYear(new Integer(2005));
    z02.setFormVersionNumber(new Integer(2));

    z02.setProvinceOfResidence("BC");
    z02.setProvinceOfMainFarmstead("BC");
    z02.setPostmarkDate("20060615");
    z02.setReceivedDate("20060615");

    z02.setIsSoleProprietor(Boolean.FALSE);
    z02.setIsPartnershipMember(Boolean.TRUE);
    z02.setIsCorporateShareholder(Boolean.FALSE);
    z02.setIsCoopMember(Boolean.FALSE);

    z02.setCommonShareTotal(null);
    z02.setFarmYears(new Integer(34));
    z02.setIsLastYearFarming(Boolean.FALSE);
    z02.setFormOriginCode(null);
    z02.setIndustryCode(new Integer(0));
    z02.setParticipantProfileCode(new Integer(3));

    z02.setIsAccrualCashConversion(Boolean.FALSE);
    z02.setIsPerishableCommodities(Boolean.FALSE);
    z02.setIsReceipts(Boolean.FALSE);
    z02.setIsOtherText(Boolean.FALSE);
    z02.setOtherText("");

    z02.setIsAccrualWorksheet(Boolean.FALSE);
    z02.setIsCwbWorksheet(Boolean.FALSE);
    z02.setIsCombinedThisYear(Boolean.FALSE);
    z02.setIsCompletedProdCycle(Boolean.TRUE);
    z02.setIsDisaster(Boolean.FALSE);
    z02.setIsCopyCobToContact(Boolean.FALSE);

    z02.setMunicipalityCode(new Integer(0));
    z02.setFormVersionEffectiveDate("20101105");
  }

  private Z03StatementInfo z03 = null;

  {
    z03 = new Z03StatementInfo();

    z03.setParticipantPin(new Integer(1467067));
    z03.setProgramYear(new Integer(2003));
    z03.setOperationNumber(new Integer(1));

    z03.setPartnershipPin(new Integer(12024782));
    z03.setPartnershipName("");
    z03.setPartnershipPercent(new Double(0.5));
    z03.setFiscalYearStart("20030101");
    z03.setFiscalYearEnd("20031231");
    z03.setAccountingCode(new Integer(2));

    z03.setIsLandlord(Boolean.FALSE);
    z03.setIsCropShare(Boolean.FALSE);
    z03.setIsFeederMember(Boolean.FALSE);
    z03.setGrossIncome(new Double(104410));
    z03.setExpenses(new Double(144067));
    z03.setNetIncomeBeforeAdj(new Double(-39657));
    z03.setOtherDeductions(new Double(0));
    z03.setInventoryAdjustments(new Double(30000));
    z03.setNetIncomeAfterAdj(new Double(-4829));
    z03.setBusinessUseOfHomeExpenses(new Double(0));
    z03.setNetFarmIncome(new Double(-4829));
    z03.setIsCropDisaster(Boolean.FALSE);
    z03.setIsLivestockDisaster(Boolean.FALSE);
  }

  private Z04IncomeExpsDtl z04 = null;

  {
    z04 = new Z04IncomeExpsDtl();

    z04.setIncomeExpenseKey(new Integer(6810));
    z04.setParticipantPin(new Integer(1467067));
    z04.setProgramYear(new Integer(2003));
    z04.setOperationNumber(new Integer(1));

    z04.setLineCode(new Integer(702));
    z04.setIe("I");
    z04.setAmount(new Double(5414));
  }

  private Z05PartnerInfo z05 = null;

  {
    z05 = new Z05PartnerInfo();

    z05.setPartnerInfoKey(new Integer(646025));
    z05.setParticipantPin(new Integer(1467067));
    z05.setProgramYear(new Integer(2003));
    z05.setOperationNumber(new Integer(1));

    z05.setPartnershipPin(new Integer(12024782));
    z05.setPartnerFirstName("DANIEL L");
    z05.setPartnerLastName("DELICHTE");
    z05.setPartnerCorpName(" ");
    z05.setPartnerSinCtnBn("624476941");
    z05.setPartnerPercent(new Double(0.5));
    z05.setPartnerPin(new Integer(22339824));
  }

  private Z21ParticipantSuppl z21 = null;

  {
    z21 = new Z21ParticipantSuppl();

    z21.setInventoryKey(new Integer(1801144));
    z21.setParticipantPin(new Integer(1531227));
    z21.setProgramYear(new Integer(2007));
    z21.setOperationNumber(new Integer(1));

    z21.setInventoryTypeCode(new Integer(1));
    z21.setInventoryCode(new Integer(5195));
    z21.setCropUnitType(new Integer(2));
    z21.setCropOnFarmAcres(new Double(50));
    z21.setCropQtyProduced(new Double(87.7));
    z21.setQuantityEnd(new Double(65));
    z21.setEndOfYearPrice(new Double(159));
    z21.setEndOfYearAmount(null);
  }

  private Z22ProductionInsurance z22 = null;

  {
    z22 = new Z22ProductionInsurance();

    z22.setProductionInsuranceKey(new Integer(52786));
    z22.setParticipantPin(new Integer(3112786));
    z22.setProgramYear(new Integer(2007));
    z22.setOperationNumber(new Integer(1));
    z22.setProductionInsuranceNumber("51062705");
  }

  private Z23LivestockProdCpct z23 = null;

  {
    z23 = new Z23LivestockProdCpct();

    z23.setProductiveCapacityKey(new Integer(99673));
    z23.setParticipantPin(new Integer(1531227));
    z23.setProgramYear(new Integer(2007));
    z23.setOperationNumber(new Integer(1));
    z23.setInventoryCode(new Integer(104));
    z23.setProductiveCapacityAmount(new Double(77));
  }

  private Z28ProdInsuranceRef z28 = null;

  {
    z28 = new Z28ProdInsuranceRef();
    z28.setProductionUnit(new Integer(1));
    z28.setProductionUnitDescription("Pounds");
  }

  private Z29InventoryRef z29 = null;

  {
    z29 = new Z29InventoryRef();

    z29.setInventoryCode(new Integer(2));
    z29.setInventoryDesc("Canadian Wheat Board Payments");
    z29.setInventoryTypeCode(new Integer(5));
    z29.setInventoryTypeDescription("Accounts Payable");
    z29.setInventoryGroupCode(new Integer(2));
    z29.setInventoryGroupDescription("DESC");
    z29.setMarketCommodityInd(Boolean.TRUE);
  }

  private Z40PrtcpntRefSuplDtl z40 = null;

  {
    z40 = new Z40PrtcpntRefSuplDtl();
    z40.setPriorYearSupplementalKey(new Integer(10534809));
    z40.setParticipantPin(new Integer(1531227));
    z40.setProgramYear(new Integer(2003));
    z40.setOperationNumber(new Integer(1));

    z40.setInventoryTypeCode(new Integer(1));
    z40.setInventoryCode(new Integer(6445));
    z40.setProductionUnit(new Integer(2));

    z40.setQuantityStart(new Double(0));
    z40.setStartingPrice(null);
    z40.setCropOnFarmAcres(new Double(145));
    z40.setCropQtyProduced(new Double(197.31));
    z40.setQuantityEnd(new Double(0));
    z40.setEndYearProducerPrice(new Double(0));
    z40.setIsAcceptProducerPrice(Boolean.FALSE);
    z40.setEndYearPrice(new Double(83.93));
    z40.setAarmReferenceP1Price(null);
    z40.setAarmReferenceP2Price(null);
  }

  private Z42ParticipantRefYear z42 = null;

  {
    z42 = new Z42ParticipantRefYear();
    z42.setProductiveCapacityKey(new Integer(369528));
    z42.setParticipantPin(new Integer(23012693));
    z42.setProgramYear(new Integer(2003));
    z42.setRefOperationNumber(new Integer(1));

    z42.setProductiveTypeCode(new Integer(2));
    z42.setProductiveCode(new Integer(10));
    z42.setProductiveCapacityUnits(new Double(32));
  }

  private Z50ParticipntBnftCalc z50 = null;

  {
    z50 = new Z50ParticipntBnftCalc();
    z50.setBenefitCalcKey(new Integer(108951));
    z50.setParticipantPin(new Integer(1467067));
    z50.setProgramYear(new Integer(2007));
    z50.setAgristabilityStatus(new Integer(1));

    z50.setUnadjustedReferenceMargin(new Double(0));
    z50.setAdjustedReferenceMargin(new Double(0));
    z50.setProgramMargin(new Double(0));
    z50.setIsWholeFarm(Boolean.FALSE);
    z50.setIsStructureChange(Boolean.FALSE);
    z50.setStructureChangeAdjAmount(null);
  }

  private Z51ParticipantContrib z51 = null;

  {
    z51 = new Z51ParticipantContrib();
    z51.setContributionKey(new Integer(130462));
    z51.setParticipantPin(new Integer(1467067));
    z51.setProgramYear(new Integer(2007));
    z51.setProvincialContributions(new Double(0));
    z51.setFederalContributions(new Double(0));
    z51.setInterimContributions(new Double(0));
    z51.setProducerShare(new Double(0));
  }

  private Z99ExtractFileCtl z99 = null;

  {
    z99 = new Z99ExtractFileCtl();
    z99.setExtractDate("20090618");
    z99.setExtractFileNumber(new Integer(1));
    z99.setRowCount(new Integer(5238));
  }


  @Test
  public final void testClear() throws SQLException {
    dao.clear();
  }

  @Test
  public final void testInsertZ01() throws SQLException {

    try {
      dao.insert(z01,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ02() throws SQLException {

    try {
      dao.insert(z02,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ03() throws SQLException {

    try {
      dao.insert(z03,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ04() throws SQLException {

    try {
      dao.insert(z04,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ05() throws SQLException {

    try {
      dao.insert(z05,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ21() throws SQLException {

    try {
      dao.insert(z21,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ22() throws SQLException {

    try {
      dao.insert(z22,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ23() throws SQLException {

    try {
      dao.insert(z23,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ28() throws SQLException {

    try {
      dao.insert(z28,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ29() throws SQLException {

    try {
      dao.insert(z29,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ40() throws SQLException {

    try {
      dao.insert(z40,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ42() throws SQLException {

    try {
      dao.insert(z42,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ50() throws SQLException {

    try {
      dao.insert(z50,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ51() throws SQLException {

    try {
      dao.insert(z51,"TEST");
    } catch (SQLException e) {

      if (e.getErrorCode() != 2291) {
        throw e;
      }
    }
  }

  @Test
  public final void testInsertZ99() throws SQLException {
    dao.insert(z99,"TEST");
  }
}
