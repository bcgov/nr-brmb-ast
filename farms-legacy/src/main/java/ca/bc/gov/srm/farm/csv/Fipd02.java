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

import ca.bc.gov.srm.farm.domain.staging.Z02PartpntFarmInfo;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;


/**
 * The object represents one row of a Fipd02 CSV file; static methods are
 * provided to parse one file into a set of Fipd02 objects.
 *
 * <p>Participant Farming Info</p>
 *
 * @author  dzwiers
 */
final class Fipd02 extends FileHandle {

  /**
   * Parses and filters a file based on the path and pin set provided.
   *
   * @param   f  File to load
   *
   * @return  Iterator which contains the parsed Z02PartpntFarmInfo objects
   *
   * @throws  CSVParserException  invalid file reference
   *
   * @see     Z02PartpntFarmInfo
   */
  public static FileHandle read(final File f) throws CSVParserException {
    return new Fipd02(f);
  }

  /**
   * @param   f  File to load
   *
   * @throws  CSVParserException  invalid file reference
   */
  private Fipd02(final File f) throws CSVParserException {
    super(f);
  }

  /** NUM_COLS. */
  private static final int NUM_COLS = 36;

  /**
   * Converts the cols for one row into a Z02PartpntFarmInfo object.
   *
   * @return  a Z02PartpntFarmInfo object
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

    Z02PartpntFarmInfo c = new Z02PartpntFarmInfo();

    int i = 1; // skip uk
    c.setParticipantPin(ParseUtils.integer(cols[i++]));
    c.setProgramYear(ParseUtils.integer(cols[i++]));
    c.setFormVersionNumber(ParseUtils.integer(cols[i++]));

    c.setProvinceOfResidence(cols[i++]);
    c.setProvinceOfMainFarmstead(cols[i++]);
    c.setPostmarkDate(cols[i++]);
    c.setReceivedDate(cols[i++]);

    c.setIsSoleProprietor(ParseUtils.indicator(cols[i++]));
    c.setIsPartnershipMember(ParseUtils.indicator(cols[i++]));
    c.setIsCorporateShareholder(ParseUtils.indicator(cols[i++]));
    c.setIsCoopMember(ParseUtils.indicator(cols[i++]));

    c.setCommonShareTotal(ParseUtils.integer(cols[i++]));
    c.setFarmYears(ParseUtils.integer(cols[i++]));
    c.setIsLastYearFarming(ParseUtils.indicator(cols[i++]));
    c.setFormOriginCode(ParseUtils.integer(cols[i++]));
    c.setIndustryCode(ParseUtils.integer(cols[i++]));
    c.setParticipantProfileCode(ParseUtils.integer(cols[i++]));

    c.setIsAccrualCashConversion(ParseUtils.indicator(cols[i++]));
    c.setIsPerishableCommodities(ParseUtils.indicator(cols[i++]));
    c.setIsReceipts(ParseUtils.indicator(cols[i++]));
    c.setIsOtherText(ParseUtils.indicator(cols[i++]));
    c.setOtherText(cols[i++]);

    c.setIsAccrualWorksheet(ParseUtils.indicator(cols[i++]));
    c.setIsCwbWorksheet(ParseUtils.indicator(cols[i++]));
    c.setIsCombinedThisYear(ParseUtils.indicator(cols[i++]));
    c.setIsCompletedProdCycle(ParseUtils.indicator(cols[i++]));
    c.setIsDisaster(ParseUtils.indicator(cols[i++]));
    c.setIsCopyCobToContact(ParseUtils.indicator(cols[i++]));

    c.setMunicipalityCode(ParseUtils.integer(cols[i++]));

    // skip legal descriptors
    i++;
    i++;
    i++;
    i++;
    i++;
    
    c.setFormVersionEffectiveDate(cols[i++]);

    return c;
  }

  /**
   * @return  FIPD_02_NAME_PREFIX.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileName()
   */
  @Override
  public String getFileName() {
    return FileCacheManager.FIPD_02_NAME_PREFIX;
  }

  /**
   * @return  FIPD_02_NUMBER.
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getFileNumber()
   */
  @Override
  public Integer getFileNumber() {
    return new Integer(FileCacheManager.FIPD_02_NUMBER);
  }

  /**
   * @return  ArrayList
   *
   * @see     ca.bc.gov.srm.farm.csv.FileHandle#getExpectedHeader()
   */
  @Override
  protected ArrayList<String> getExpectedHeader() {
    ArrayList<String> r = new ArrayList<>();

    r.add("gk_farm_info");
    r.add("part_pin");
    r.add("prgrm_yy");
    r.add("form_vrsn_num");
    r.add("part_res_prov_code");
    r.add("part_fmstd_prov_code");
    r.add("form_pstmrk_date");
    r.add("form_rcvd_date");
    r.add("sole_prop_ind");
    r.add("prshp_ind");
    r.add("corp_shlr_ind");
    r.add("coop_mem_ind");
    r.add("corp_shr_tot");
    r.add("farm_years");
    r.add("last_farm_incm_yy");
    r.add("form_org_code");
    r.add("industy_code");
    r.add("part_profile_code");
    r.add("acc_to_cash_ind");
    r.add("persih_comm_ind");
    r.add("receipts_ind");
    r.add("other_ind");
    r.add("other_text");
    r.add("acc_ref_mrgn_wrksht_ind");
    r.add("cwb_adj_wrksht_ind");
    r.add("combined_type_ind");
    r.add("completed_prdctn_cycle_ind");
    r.add("disaster_circumstances_ind");
    r.add("cntct_rqr_cob_ind");
    r.add("mncplty_code");
    r.add("legal_land_desc_qtr");
    r.add("legal_land_desc_sec");
    r.add("legal_land_desc_twp");
    r.add("legal_land_desc_rng");
    r.add("legal_land_desc_mer");
    r.add("form_version_effective_date");


    return r;
  }
}
