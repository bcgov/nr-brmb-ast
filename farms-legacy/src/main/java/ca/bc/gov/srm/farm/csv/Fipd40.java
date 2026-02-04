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

import ca.bc.gov.srm.farm.domain.staging.Z40PrtcpntRefSuplDtl;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd40 CSV file; static methods are
 * provided to parse one file into a set of Z40PrtcpntRefSuplDtl objects.
 *
 * <p>Participant Reference Year Crop/Livestock Details</p>
 *
 * @author  dzwiers
 */
final class Fipd40 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z40PrtcpntRefSuplDtl objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z40PrtcpntRefSuplDtl
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd40(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd40(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 18;

  /**
   * Converts the cols for one row into a Z40PrtcpntRefSuplDtl object.
   *
   * @return  a Z40PrtcpntRefSuplDtl object
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

    Z40PrtcpntRefSuplDtl c = new Z40PrtcpntRefSuplDtl();

    int i = 0;
    c.setPriorYearSupplementalKey(ParseUtils.integer(cols[i++]));
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setProgramYear(ParseUtils.integer(cols[i++]));
    i++; // c.setFormVersion(ParseUtils.integer(cols[i++]));
    c.setOperationNumber(ParseUtils.integer(cols[i++]));

    c.setInventoryTypeCode(ParseUtils.integer(cols[i++]));
    c.setInventoryCode(ParseUtils.integer(cols[i++]));
    c.setProductionUnit(ParseUtils.integer(cols[i++]));
    c.setQuantityStart(ParseUtils.dbl(cols[i++]));
    c.setStartingPrice(ParseUtils.dbl(cols[i++]));
    c.setCropOnFarmAcres(ParseUtils.dbl(cols[i++]));
    c.setCropQtyProduced(ParseUtils.dbl(cols[i++]));
    c.setQuantityEnd(ParseUtils.dbl(cols[i++]));
    c.setEndYearProducerPrice(ParseUtils.dbl(cols[i++]));
    c.setIsAcceptProducerPrice(ParseUtils.indicator(cols[i++]));
    c.setEndYearPrice(ParseUtils.dbl(cols[i++]));
    c.setAarmReferenceP1Price(ParseUtils.dbl(cols[i++]));
    c.setAarmReferenceP2Price(ParseUtils.dbl(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_40_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_40_NAME_PREFIX;
  }

  /**
   * @return  FIPD_40_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_40_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("gk_fipd_as_40");
    r.add("part_pin");
    r.add("ref_yy");
    r.add("form_vrsn_num");
    r.add("incm_expn_stmt_num");
    r.add("inv_type_code");
    r.add("inv_code");
    r.add("unit_type");
    r.add("qty_start");
    r.add("start_year_price");
    r.add("on_farm_acr");
    r.add("crop_qty_produced");
    r.add("qty_end");
    r.add("end_year_prod_price");
    r.add("end_year_prod_ovrd_ind");
    r.add("end_year_price");
    r.add("aarm_start_year_price");
    r.add("aarm_end_year_price");

    return r;
  }
}
