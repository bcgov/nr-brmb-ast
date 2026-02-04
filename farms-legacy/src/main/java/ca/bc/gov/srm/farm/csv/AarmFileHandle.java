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

import ca.bc.gov.srm.farm.domain.staging.AarmCsvRow;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;


/**
 * 
 */
public final class AarmFileHandle extends FileHandle {
	/** NUM_COLS. */
  private static final int NUM_COLS = 13;

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   * @return  FileHandle which contains the parsed FmvCsvRow objects
   * @throws  CSVParserException  invalid file reference
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new AarmFileHandle(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private AarmFileHandle(final File f) throws CSVParserException {
    super(f);
  }
  
  /**
   * @return true if the CSV file has a header row.
   */
  @Override
  protected boolean csvFileHasHeader() {
  	return false;
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

    AarmCsvRow row = new AarmCsvRow();

    int index = 0;
    row.setAarmKey(ParseUtils.integer(cols[index++].trim()));
    row.setParticipantPin(ParseUtils.integer(cols[index++].trim()));
    row.setProgramYear(ParseUtils.integer(cols[index++].trim()));
    row.setOperationNumber(ParseUtils.integer(cols[index++].trim()));
    row.setPartnerPercent(ParseUtils.dbl(cols[index++].trim()));
    row.setInventoryTypeCode(ParseUtils.integer(cols[index++].trim()));
    row.setInventoryCode(ParseUtils.integer(cols[index++].trim()));
    row.setInventoryDescription(cols[index++].trim());
    row.setProductionUnit(ParseUtils.integer(cols[index++].trim()));
    row.setAarmReferenceP1Price(ParseUtils.dbl(cols[index++].trim()));
    row.setAarmReferenceP2Price(ParseUtils.dbl(cols[index++].trim()));
    row.setQuantityStart(ParseUtils.dbl(cols[index++].trim()));
    row.setQuantityEnd(ParseUtils.dbl(cols[index++].trim()));

    return row;
  }

  /**
   * @return  some dummy value
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return "AARM";
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

    return headers;
  }
}
