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

import ca.bc.gov.srm.farm.domain.staging.Z28ProdInsuranceRef;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd28 CSV file; static methods are
 * provided to parse one file into a set of Z28ProdInsuranceRef objects.
 *
 * <p>Production Unit Reference</p>
 *
 * @author  dzwiers
 */
final class Fipd28 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z28ProdInsuranceRef objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z28ProdInsuranceRef
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd28(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd28(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 2;

  /**
   * Converts the cols for one row into a Z28ProdInsuranceRef object.
   *
   * @return  a Z28ProdInsuranceRef object
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

    Z28ProdInsuranceRef c = new Z28ProdInsuranceRef();

    c.setProductionUnit(ParseUtils.integer(cols[0]));
    c.setProductionUnitDescription(cols[1]);

    return c;
  }

  /**
   * @return  FIPD_28_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_28_NAME_PREFIX;
  }

  /**
   * @return  FIPD_28_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_28_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("unit_type");
    r.add("unit_type_desc");

    return r;
  }
}
