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

import ca.bc.gov.srm.farm.domain.staging.Z99ExtractFileCtl;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd99 CSV file; static methods are
 * provided to parse one file into a set of Z99ExtractFileCtl objects.
 *
 * <p>AgriStability Combined PINs</p>
 *
 * @author  dzwiers
 */
final class Fipd99 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z99ExtractFileCtl objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z99ExtractFileCtl
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd99(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd99(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 3;

  /**
   * Converts the cols for one row into a Z99ExtractFileCtl object.
   *
   * @return  a Z99ExtractFileCtl object
   *
   * @param   cols  to parse
   *
   * @throws  ParseException  Parse Error
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

    Z99ExtractFileCtl c = new Z99ExtractFileCtl();

    int i = 0;

    c.setExtractDate(cols[i++]);
    c.setExtractFileNumber(ParseUtils.integer(cols[i++]));
    c.setRowCount(ParseUtils.integer(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_99_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_99_NAME_PREFIX;
  }

  /**
   * @return  FIPD_99_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_99_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("extract_date");
    r.add("extract_file_num");
    r.add("extract_row_cnt");

    return r;
  }
}
