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

import ca.bc.gov.srm.farm.domain.staging.Z05PartnerInfo;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd05 CSV file; static methods are
 * provided to parse one file into a set of Fipd05 objects.
 *
 * <p>Statement A/B Partner Information</p>
 *
 * @author  dzwiers
 */
final class Fipd05 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z05PartnerInfo objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z05PartnerInfo
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd05(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd05(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 12;

  /**
   * Converts the cols for one row into a Z05PartnerInfo object.
   *
   * @return  a Z05PartnerInfo object
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

    Z05PartnerInfo c = new Z05PartnerInfo();

    int i = 0;
    c.setPartnerInfoKey(ParseUtils.integer(cols[i++]));
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setProgramYear(ParseUtils.integer(cols[i++]));
    i++; // c.setFormVersion(ParseUtils.integer(cols[i++]));
    c.setOperationNumber(ParseUtils.integer(cols[i++]));
    c.setPartnershipPin(ParseUtils.integer(cols[i++]));
    c.setPartnerFirstName(cols[i++]);
    c.setPartnerLastName(cols[i++]);
    c.setPartnerCorpName(cols[i++]);
    c.setPartnerSinCtnBn(cols[i++]);
    c.setPartnerPercent(ParseUtils.dbl(cols[i++]));
    c.setPartnerPin(ParseUtils.integer(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_05_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_05_NAME_PREFIX;
  }

  /**
   * @return  FIPD_05_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_05_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("gk_prtnr_info");
    r.add("part_pin");
    r.add("prgrm_yy");
    r.add("form_vrsn_num");
    r.add("incm_expn_stmt_num");
    r.add("prshp_pin");
    r.add("prtnr_gname");
    r.add("prtnr_sname");
    r.add("prtnr_corp_name");
    r.add("prtnr_sin_bn");
    r.add("prtnr_pct");
    r.add("prtnr_pin");

    return r;
  }
}
