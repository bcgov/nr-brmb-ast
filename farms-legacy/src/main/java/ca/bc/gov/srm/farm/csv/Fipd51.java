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

import ca.bc.gov.srm.farm.domain.staging.Z51ParticipantContrib;

import java.io.File;

import java.text.ParseException;

import java.util.ArrayList;


/**
 * The object represents one row of a Fipd51 CSV file; static methods are
 * provided to parse one file into a set of Z51ParticipantContrib objects.
 *
 * <p>Participant Reference Year - Productive Capacity</p>
 *
 * @author  dzwiers
 */
final class Fipd51 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z51ParticipantContrib objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z51ParticipantContrib
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd51(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd51(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 7;

  /**
   * Converts the cols for one row into a Z51ParticipantContrib object.
   *
   * @return  a Z51ParticipantContrib object
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

    Z51ParticipantContrib c = new Z51ParticipantContrib();

    int i = 0;

    c.setContributionKey(ParseUtils.integer(cols[i++]));
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setProgramYear(ParseUtils.integer(cols[i++]));
    c.setProvincialContributions(ParseUtils.dbl(cols[i++]));
    c.setFederalContributions(ParseUtils.dbl(cols[i++]));
    c.setInterimContributions(ParseUtils.dbl(cols[i++]));
    c.setProducerShare(ParseUtils.dbl(cols[i++]));

    return c;
  }

  /**
   * @return  FIPD_51_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_51_NAME_PREFIX;
  }

  /**
   * @return  FIPD_51_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_51_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList getExpectedHeader() {
    ArrayList r = new ArrayList();

    r.add("gk_fipd_as_51");
    r.add("part_pin");
    r.add("prgrm_yy");
    r.add("prov_cntrbn");
    r.add("fed_cntrbn");
    r.add("intrm_cntrbn_amt");
    r.add("prod_shr_amt");

    return r;
  }
}
