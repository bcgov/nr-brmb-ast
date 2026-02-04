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

import ca.bc.gov.srm.farm.domain.staging.Z42ParticipantRefYear;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd42 CSV file; static methods are
 * provided to parse one file into a set of Z42ParticipantRefYear objects.
 *
 * <p>Participant Reference Year - Productive Capacity</p>
 *
 * @author  dzwiers
 */
final class Fipd42 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z42ParticipantRefYear objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z42ParticipantRefYear
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd42(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd42(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 7;

  /**
   * Converts the cols for one row into a Z42ParticipantRefYear object.
   *
   * @return  a Z42ParticipantRefYear object
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

    Z42ParticipantRefYear c = new Z42ParticipantRefYear();

    int i = 0;
    c.setProductiveCapacityKey(ParseUtils.integer(cols[i++]));
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setProgramYear(ParseUtils.integer(cols[i++]));
    c.setRefOperationNumber(ParseUtils.integer(cols[i++]));
    c.setProductiveTypeCode(ParseUtils.integer(cols[i++]));
    c.setProductiveCode(ParseUtils.integer(cols[i++]));
    c.setProductiveCapacityUnits(ParseUtils.dbl(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_42_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_42_NAME_PREFIX;
  }

  /**
   * @return  FIPD_42_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_42_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("gk_fipd_as_42");
    r.add("part_pin");
    r.add("ref_yy");
    r.add("ref_stmt_num");
    r.add("prod_type_code");
    r.add("prod_code");
    r.add("prod_cap_amt");

    return r;
  }
}
