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

import ca.bc.gov.srm.farm.domain.staging.Z23LivestockProdCpct;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd23 CSV file; static methods are
 * provided to parse one file into a set of Z23LivestockProdCpct objects.
 *
 * <p>Livestock Productive Capacity</p>
 *
 * @author  dzwiers
 */
final class Fipd23 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z23LivestockProdCpct objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z23LivestockProdCpct
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd23(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd23(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 7;

  /**
   * Converts the cols for one row into a Z23LivestockProdCpct object.
   *
   * @return  a Z23LivestockProdCpct object
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

    Z23LivestockProdCpct c = new Z23LivestockProdCpct();

    int i = 0;
    c.setProductiveCapacityKey(ParseUtils.integer(cols[i++]));
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setProgramYear(ParseUtils.integer(cols[i++]));
    i++; // c.setFormVersion(ParseUtils.integer(cols[i++]));
    c.setOperationNumber(ParseUtils.integer(cols[i++]));
    c.setInventoryCode(ParseUtils.integer(cols[i++]));
    c.setProductiveCapacityAmount(ParseUtils.dbl(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_23_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_23_NAME_PREFIX;
  }

  /**
   * @return  FIPD_23_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_23_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("gk_fipd_as_23");
    r.add("part_pin");
    r.add("prgrm_yy");
    r.add("form_vrsn_num");
    r.add("incm_expn_stmt_num");
    r.add("inv_code");
    r.add("prod_cap_amt");

    return r;
  }
}
