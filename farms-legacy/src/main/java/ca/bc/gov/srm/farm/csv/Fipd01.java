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

import ca.bc.gov.srm.farm.domain.staging.Z01ParticipantInfo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;


/**
 * The object represents one row of a Fipd01 CSV file; static methods are
 * provided to parse one file into a set of Fipd01 objects.
 *
 * <p>Participant Info</p>
 *
 * @author  dzwiers
 */
final class Fipd01 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  FileHandle which contains the parsed Z01ParticipantInfo objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z01ParticipantInfo
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd01(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd01(final File f) throws CSVParserException {
    super(f);
  }
  
  /** NUM_COLS (With new cell phone numbers) */
  private static final int NUM_COLS = 33;

  /** LEGACY NUM_COLS (Without the cell phone numbers) */
  private static final int NUM_COLS_LEGACY = 31;

  /**
   * Converts the cols for one row into a Fipd01 object.
   *
   * @param   cols  to parse
   *
   * @return  a Z01ParticipantInfo object
   *
   * @throws  ParseException  invalid data found
   *
   * @see     Z01ParticipantInfo
   */
  @Override
  protected Object parse(final String[] cols) throws ParseException {

    if (cols == null) {
      error(MISSING_ROW, 0);

      return null;
    }

    if (cols.length != NUM_COLS_LEGACY && cols.length != NUM_COLS) {
      error(INVALID_ROW, cols.length);

      return null;
    }

    Z01ParticipantInfo c = new Z01ParticipantInfo();

    int i = 1; // skip uk
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setSinCtnBn(cols[i++]);
    
    c.setFirstName(cols[i++]);
    c.setLastName(cols[i++]);
    c.setCorpName(cols[i++]);
    c.setAddress1(cols[i++]);
    c.setAddress2(cols[i++]);
    c.setCity(cols[i++]);
    c.setProvince(cols[i++]);
    c.setPostalCode(cols[i++]);
    c.setCountry(cols[i++]);
    c.setParticipantTypeCode(ParseUtils.integer(cols[i++]));
    c.setParticipantLanguage(ParseUtils.integer(cols[i++]));
    c.setParticipantFax(cols[i++]);
    c.setParticipantPhoneDay(cols[i++]);
    c.setParticipantPhoneEvening(cols[i++]);
    c.setContactFirstName(cols[i++]);
    c.setContactLastName(cols[i++]);
    c.setContactBusinessName(cols[i++]);
    c.setContactAddress1(cols[i++]);
    c.setContactAddress2(cols[i++]);
    c.setContactCity(cols[i++]);
    c.setContactProvince(cols[i++]);
    c.setContactPostalCode(cols[i++]);
    c.setContactPhoneDay(cols[i++]);
    c.setContactFaxNumber(cols[i++]);
    c.setPublicOffice(ParseUtils.integer(cols[i++]));
    c.setIdentEffectiveDate(cols[i++]);
    c.setParticipantEmail(cols[i++]);
    c.setBusinessNumber(cols[i++]);
    
    if (cols.length == NUM_COLS) {
      c.setParticipantPhoneCell(cols[i++]);
      c.setContactPhoneCell(cols[i++]);
    }

    return c;
  }

  /**
   * @return  FIPD_01_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_01_NAME_PREFIX;
  }

  /**
   * @return  FIPD_01_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_01_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   */

  protected ArrayList<String> getLegacyExpectedHeader() {
    ArrayList<String> r = new ArrayList<>();

    r.add("gk_participant");
    r.add("part_pin");
    r.add("sin_bn");
    r.add("part_gname");
    r.add("part_sname");
    r.add("corp_name");
    r.add("part_line1_addr");
    r.add("part_line2_addr");
    r.add("part_city");
    r.add("part_prov");
    r.add("part_pstl_code");
    r.add("part_cntry_code");
    r.add("part_type_code");
    r.add("part_lang_code");
    r.add("part_fax_num");
    r.add("part_day_tel_num");
    r.add("part_eve_tel_num");
    r.add("cnct_first_name");
    r.add("cntc_last_name");
    r.add("cntct_name");
    r.add("cntct_line1_addr");
    r.add("cntct_line2_addr");
    r.add("cntct_city");
    r.add("cntct_prov");
    r.add("cntct_pstl_code");
    r.add("cntct_day_tel_num");
    r.add("cntct_fax_num");
    r.add("pblc_offc_aafc_empl_ind");
    r.add("ident_effective_date");
    r.add("part_email");
    r.add("bn");

    return r;
  }
  
  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList<String> getExpectedHeader() {
    ArrayList<String> r = new ArrayList<>();

    r.add("gk_participant");
    r.add("part_pin");
    r.add("sin_bn");
    r.add("part_gname");
    r.add("part_sname");
    r.add("corp_name");
    r.add("part_line1_addr");
    r.add("part_line2_addr");
    r.add("part_city");
    r.add("part_prov");
    r.add("part_pstl_code");
    r.add("part_cntry_code");
    r.add("part_type_code");
    r.add("part_lang_code");
    r.add("part_fax_num");
    r.add("part_day_tel_num");
    r.add("part_eve_tel_num");
    r.add("cnct_first_name");
    r.add("cntc_last_name");
    r.add("cntct_name");
    r.add("cntct_line1_addr");
    r.add("cntct_line2_addr");
    r.add("cntct_city");
    r.add("cntct_prov");
    r.add("cntct_pstl_code");
    r.add("cntct_day_tel_num");
    r.add("cntct_fax_num");
    r.add("pblc_offc_aafc_empl_ind");
    r.add("ident_effective_date");
    r.add("part_email");
    r.add("bn");
    r.add("part_cell_num");
    r.add("cntct_cell_num");

    return r;
  }
  
  /**
   * This method validates the file header.
   *
   * @return  Set of errors when the header in the file does not match the
   *          header in the spec.
   */
  @Override
  public final String[] validate() {
    ArrayList<String> results = new ArrayList<>();
    ArrayList<String> headers = new ArrayList<>(getExpectedHeader());

    // load header
    try {
      load();
    } catch (IOException | ParseException e) {
      logger.error("Unexpected error: ", e);
      excep = new CSVParserException(row, getFileNumber().intValue(), e);
    }
    
    if (header.length == NUM_COLS_LEGACY) {
      headers = new ArrayList<>(getLegacyExpectedHeader());
    }

    // compare header
    for (int i = 0; i < header.length; i++) {

      if ((header[i] != null) && !"".equals(header[i].trim())
          && !headers.remove(header[i].trim())) {
        results.add("Extra Header found: " + header[i]);
      }
    }

    for (String curHeader : headers) {
      results.add("Header not found: " + curHeader);
    }

    if (results.size() <= 0) {
      return null;
    }

    return results.toArray(new String[results.size()]);
  }

}
