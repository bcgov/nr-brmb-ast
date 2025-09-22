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

import ca.bc.gov.srm.farm.domain.staging.FmvCsvRow;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * 
 */
public final class FmvFileHandle extends FileHandle {
	/** NUM_COLS. */
  private static final int NUM_COLS = 7;

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed FmvCsvRow objects
   *
   * @throws  CSVParserException  invalid file reference
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new FmvFileHandle(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private FmvFileHandle(final File f) throws CSVParserException {
    super(f);
  }
  

  /**
   * Converts the cols for one row into a FmvCsvRow object.
   *
   * @return  a FmvCsvRow object
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

    FmvCsvRow row = new FmvCsvRow();

    int index = 0;
    row.setProgramYear(ParseUtils.integer(cols[index++].trim()));
    row.setPeriod(ParseUtils.integer(cols[index++].trim()));
    row.setMunicipalityCode(cols[index++].trim());
    row.setInventoryCode(cols[index++].trim());
    row.setUnitCode(cols[index++].trim());
    row.setAveragePrice(ParseUtils.dbl(cols[index++].trim()));
    row.setPercentVariance(ParseUtils.dbl(cols[index++].trim()));

    return row;
  }

  /**
   * @return  some dummy value
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return "FMV";
  }

  /**
   * @return some dummy value
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
  protected ArrayList getExpectedHeader() {
    ArrayList headers = new ArrayList();

    headers.add("YEAR");
    headers.add("PERIOD");
    headers.add("MUNICIPALITY");
    headers.add("INVENTORY_CODE");
    headers.add("UNIT_CODE");
    headers.add("VALUE");
    headers.add("PERCENT_VARIANCE");

    return headers;
  }
}
