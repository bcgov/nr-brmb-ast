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

import ca.bc.gov.srm.farm.domain.staging.Z50ParticipntBnftCalc;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd50 CSV file; static methods are
 * provided to parse one file into a set of Z50ParticipntBnftCalc objects.
 *
 * <p>Participant Reference Year - Productive Capacity</p>
 *
 * @author  dzwiers
 */
final class Fipd50 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z50ParticipntBnftCalc objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z50ParticipntBnftCalc
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd50(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd50(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 12;

  /**
   * Converts the cols for one row into a Z50ParticipntBnftCalc object.
   *
   * @return  a Z50ParticipntBnftCalc object
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

    Z50ParticipntBnftCalc c = new Z50ParticipntBnftCalc();

    int i = 0;

    c.setBenefitCalcKey(ParseUtils.integer(cols[i++]));
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setProgramYear(ParseUtils.integer(cols[i++]));
    c.setAgristabilityStatus(ParseUtils.integer(cols[i++]));
    c.setUnadjustedReferenceMargin(ParseUtils.dbl(cols[i++]));
    c.setAdjustedReferenceMargin(ParseUtils.dbl(cols[i++]));
    c.setProgramMargin(ParseUtils.dbl(cols[i++]));
    c.setIsWholeFarm(ParseUtils.indicator(cols[i++]));
    c.setIsStructureChange(ParseUtils.indicator(cols[i++]));
    c.setStructureChangeAdjAmount(ParseUtils.dbl(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_50_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_50_NAME_PREFIX;
  }

  /**
   * @return  FIPD_50_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_50_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("gk_fipd_as_50");
    r.add("part_pin");
    r.add("prgrm_yy");
    r.add("as_proc_stat");
    r.add("unadj_ref_mrgn");
    r.add("adj_ref_mrgn");
    r.add("cy_prgm_mrgn");
    r.add("wf_ind");
    r.add("strc_chng_ind");
    r.add("struct_chng_adj_amt");
    r.add("vld_ftr_en_ind");
    r.add("vld_ftr_ref_mrgn_ind");

    return r;
  }
}
