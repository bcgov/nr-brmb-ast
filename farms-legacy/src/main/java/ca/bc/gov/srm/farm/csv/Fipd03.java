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
package ca.bc.gov.srm.farm.csv;

import ca.bc.gov.srm.farm.domain.staging.Z03StatementInfo;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd03 CSV file; static methods are
 * provided to parse one file into a set of Fipd03 objects.
 *
 * <p>Statement A/B Information</p>
 *
 * @author  dzwiers
 */
final class Fipd03 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z03StatementInfo objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z03StatementInfo
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd03(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd03(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 24;

  /**
   * Converts the cols for one row into a Z03StatementInfo object.
   *
   * @return  a Z03StatementInfo object
   *
   * @param   cols  to parse
   *
   * @throws  ParseException  invalid data found
   */
  @Override
  protected Object parse(final String[] cols) throws ParseException {

    if (cols == null) {
      error(MISSING_ROW, 0);

      return null;
    }

    if (cols.length != NUM_COLS) {
      error(INVALID_ROW, cols.length);

      return null;
    }

    Z03StatementInfo c = new Z03StatementInfo();

    int i = 1; // skip uk
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setProgramYear(ParseUtils.integer(cols[i++]));
    i++; // c.setFormVersion(ParseUtils.integer(cols[i++]));
    c.setOperationNumber(ParseUtils.integer(cols[i++]));

    c.setPartnershipPin(ParseUtils.integer(cols[i++]));
    c.setPartnershipName(cols[i++]);
    c.setPartnershipPercent(ParseUtils.dbl(cols[i++]));
    c.setFiscalYearStart(cols[i++]);
    c.setFiscalYearEnd(cols[i++]);
    c.setAccountingCode(ParseUtils.integer(cols[i++]));

    c.setIsLandlord(ParseUtils.indicator(cols[i++]));
    c.setIsCropShare(ParseUtils.indicator(cols[i++]));
    c.setIsFeederMember(ParseUtils.indicator(cols[i++]));
    c.setGrossIncome(ParseUtils.dbl(cols[i++]));
    c.setExpenses(ParseUtils.dbl(cols[i++]));
    c.setNetIncomeBeforeAdj(ParseUtils.dbl(cols[i++]));
    c.setOtherDeductions(ParseUtils.dbl(cols[i++]));
    c.setInventoryAdjustments(ParseUtils.dbl(cols[i++]));
    c.setNetIncomeAfterAdj(ParseUtils.dbl(cols[i++]));
    c.setBusinessUseOfHomeExpenses(ParseUtils.dbl(cols[i++]));
    c.setNetFarmIncome(ParseUtils.dbl(cols[i++]));
    c.setIsCropDisaster(ParseUtils.indicator(cols[i++]));
    c.setIsLivestockDisaster(ParseUtils.indicator(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_03_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_03_NAME_PREFIX;
  }

  /**
   * @return  FIPD_03_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_03_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("gk_stmt_info");
    r.add("part_pin");
    r.add("prgrm_yy");
    r.add("form_vrsn_num");
    r.add("incm_expn_stmt_num");
    r.add("prshp_pin");
    r.add("prshp_name");
    r.add("prshp_pct");
    r.add("stmt_fy_start_date");
    r.add("stmt_fy_end_date");
    r.add("stmt_accrl_code");
    r.add("lndlrd_crop_shr_ind");
    r.add("tenant_crop_shr_ind");
    r.add("feeder_member_ind");
    r.add("oth_grs_incm_amt");
    r.add("oth_farm_expn_tot");
    r.add("net_incm_b_adj_amt");
    r.add("net_incm_ded_amt");
    r.add("oth_cy_inv_adj_tot");
    r.add("net_incm_adj_amt");
    r.add("bus_elig_exp_amt");
    r.add("net_farm_incm_amt");
    r.add("crop_disaster_ind");
    r.add("livestock_disaster_ind");

    return r;
  }
}
