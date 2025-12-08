/**
 *
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.csv;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;

import ca.bc.gov.srm.farm.domain.staging.IvprCsvRow;

/**
 *
 */
public final class IvprFileHandle extends FileHandle {
  /** NUM_COLS. */
  private static final int NUM_COLS = 5;

  /**
   * Parses and filters a file based on the path provided.
   *
   * @param   f  File to load
   * @return  FileHandle which contains the parsed IvprCsvRow objects
   * @throws  CSVParserException  invalid file reference
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new IvprFileHandle(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private IvprFileHandle(final File f) throws CSVParserException {
    super(f);
  }


  /**
   * Converts the cols for one row into a IvprCsvRow object.
   *
   * @return  a IvprCsvRow object
   *
   * @param   cols  to parse
   *
   * @throws  ParseException  Parse Error
   */
  @Override
  protected Object parse(final String[] cols) throws ParseException{

    if (cols == null) {
      error(MISSING_ROW, 0);
      return null;
    }

    if (cols.length != NUM_COLS) {
      error(INVALID_ROW, cols.length);
      return null;
    }

    IvprCsvRow row = new IvprCsvRow();

    int index = 0;
    row.setProgramYear(ParseUtils.integer(cols[index++]));
    row.setInventoryItemCode(cols[index++].trim());
    row.setInventoryItemCodeDescription(cols[index++].trim());
    row.setInsurableValue(ParseUtils.dbl(cols[index++]));
    row.setPremiumRate(ParseUtils.dbl(cols[index++]));

    return row;
  }

  /**
   * @return  some dummy value
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return "IVPR";
  }

  /**
   * @return  some dummy value
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(0);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList<String> getExpectedHeader() {
    ArrayList<String> headers = new ArrayList<String>();

    headers.add("Year");
    headers.add("Code");
    headers.add("Description");
    headers.add("IV");
    headers.add("PR");

    return headers;
  }
}
