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

import ca.bc.gov.srm.farm.domain.staging.Z21ParticipantSuppl;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;


/**
 * The object represents one row of a Fipd21 CSV file; static methods are
 * provided to parse one file into a set of Z21ParticipantSuppl objects.
 *
 * <p>Participant Supplemental Details</p>
 *
 * @author  dzwiers
 */
final class Fipd21 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z21ParticipantSuppl objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z21ParticipantSuppl
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd21(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd21(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 14;

  /**
   * Converts the cols for one row into a Z21ParticipantSuppl object.
   *
   * @return  a Z21ParticipantSuppl object
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

    Z21ParticipantSuppl c = new Z21ParticipantSuppl();

    int i = 0;

    c.setInventoryKey(ParseUtils.integer(cols[i++]));
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setProgramYear(ParseUtils.integer(cols[i++]));
    i++; // c.setFormVersion(ParseUtils.integer(cols[i++]));
    c.setOperationNumber(ParseUtils.integer(cols[i++]));

    c.setInventoryTypeCode(ParseUtils.integer(cols[i++]));
    c.setInventoryCode(ParseUtils.integer(cols[i++]));
    c.setCropUnitType(ParseUtils.integer(cols[i++]));
    c.setCropOnFarmAcres(ParseUtils.dbl(cols[i++]));
    c.setCropQtyProduced(ParseUtils.dbl(cols[i++]));
    c.setQuantityEnd(ParseUtils.dbl(cols[i++]));
    c.setEndOfYearPrice(ParseUtils.dbl(cols[i++]));
    c.setEndOfYearAmount(ParseUtils.dbl(cols[i++]));
    c.setCropUnseedableAcres(ParseUtils.dbl(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_21_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_21_NAME_PREFIX;
  }

  /**
   * @return  FIPD_21_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_21_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList<String> getExpectedHeader() {
    ArrayList<String> r = new ArrayList<>();

    r.add("gk_fipd_as_21");
    r.add("part_pin");
    r.add("prgrm_yy");
    r.add("form_vrsn_num");
    r.add("incm_expn_stmt_num");
    r.add("inv_type_code");
    r.add("inv_code");
    r.add("unit_type");
    r.add("on_farm_acr");
    r.add("qty_produced");
    r.add("qty_end");
    r.add("fair_mrkt_price");
    r.add("end_of_yy_amt");
    r.add("unseedable_acr");

    return r;
  }
}
