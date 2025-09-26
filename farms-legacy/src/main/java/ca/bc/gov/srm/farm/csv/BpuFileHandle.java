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

import ca.bc.gov.srm.farm.domain.staging.BpuCsvRow;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;


/**
 * 
 */
public final class BpuFileHandle extends FileHandle {
	/** NUM_COLS. */
  private static final int NUM_COLS = 16;

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   * @return  FileHandle which contains the parsed FmvCsvRow objects
   * @throws  CSVParserException  invalid file reference
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new BpuFileHandle(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private BpuFileHandle(final File f) throws CSVParserException {
    super(f);
  }
  

  /**
   * Converts the cols for one row into a BpuCsvRow object.
   *
   * @return  a BpuCsvRow object
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

    BpuCsvRow row = new BpuCsvRow();

    int index = 0;
    row.setProgramYear(ParseUtils.integer(cols[index++].trim()));
    row.setMunicipalityCode(cols[index++].trim());
    row.setInventoryCode(cols[index++].trim());
    row.setUnitDescription(cols[index++].trim());
    row.setYearMinus6Margin(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus5Margin(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus4Margin(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus3Margin(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus2Margin(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus1Margin(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus6Expense(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus5Expense(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus4Expense(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus3Expense(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus2Expense(ParseUtils.dbl(cols[index++].trim()));
    row.setYearMinus1Expense(ParseUtils.dbl(cols[index++].trim()));

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
  protected ArrayList<String> getExpectedHeader() {
    ArrayList<String> headers = new ArrayList<>();

    headers.add("YEAR");
    headers.add("MUNICIPALITY");
    headers.add("INVENTORY_CODE");
    headers.add("UNIT_DESCRIPTION");
    headers.add("YEAR_MINUS_6_MARGIN");
    headers.add("YEAR_MINUS_5_MARGIN");
    headers.add("YEAR_MINUS_4_MARGIN");
    headers.add("YEAR_MINUS_3_MARGIN");
    headers.add("YEAR_MINUS_2_MARGIN");
    headers.add("YEAR_MINUS_1_MARGIN");
    headers.add("YEAR_MINUS_6_EXPENSE");
    headers.add("YEAR_MINUS_5_EXPENSE");
    headers.add("YEAR_MINUS_4_EXPENSE");
    headers.add("YEAR_MINUS_3_EXPENSE");
    headers.add("YEAR_MINUS_2_EXPENSE");
    headers.add("YEAR_MINUS_1_EXPENSE");

    return headers;
  }
}
