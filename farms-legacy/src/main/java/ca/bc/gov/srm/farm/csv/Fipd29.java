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

import ca.bc.gov.srm.farm.domain.staging.Z29InventoryRef;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd29 CSV file; static methods are
 * provided to parse one file into a set of Z29InventoryRef objects.
 *
 * <p>Inventory Code Reference</p>
 *
 * @author  dzwiers
 */
final class Fipd29 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z29InventoryRef objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z29InventoryRef
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd29(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd29(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 7;

  /**
   * Converts the cols for one row into a Z29InventoryRef object.
   *
   * @return  a Z29InventoryRef object
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

    Z29InventoryRef c = new Z29InventoryRef();

    int i = 0;
    c.setInventoryCode(ParseUtils.integer(cols[i++]));
    c.setInventoryDesc(cols[i++]);
    c.setInventoryTypeCode(ParseUtils.integer(cols[i++]));
    c.setInventoryTypeDescription(cols[i++]);
    c.setInventoryGroupCode(ParseUtils.integer(cols[i++]));
    c.setInventoryGroupDescription(cols[i++]);
    c.setMarketCommodityInd(ParseUtils.indicator(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_29_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_29_NAME_PREFIX;
  }

  /**
   * @return  FIPD_29_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_29_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("inv_code");
    r.add("inv_code_desc");
    r.add("inv_type_code");
    r.add("inv_type_desc");
    r.add("inv_grp_code");
    r.add("inv_grp_desc");
    r.add("market_commodity_ind");

    return r;
  }
}
