package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z02PartpntFarmInfo;

public final class Fipd02 extends FileHandle<Z02PartpntFarmInfo> {

    private static final int NUM_COLS = 36;

    public static FileHandle<Z02PartpntFarmInfo> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd02(inputStream);
    }

    private Fipd02(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z02PartpntFarmInfo parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z02PartpntFarmInfo c = new Z02PartpntFarmInfo();

        int i = 1; // skip uk
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        c.setFormVersionNumber(ParseUtils.parseInteger(cols[i++]));

        c.setProvinceOfResidence(cols[i++]);
        c.setProvinceOfMainFarmstead(cols[i++]);
        c.setPostmarkDate(cols[i++]);
        c.setReceivedDate(cols[i++]);

        c.setIsSoleProprietor(ParseUtils.parseBoolean(cols[i++]));
        c.setIsPartnershipMember(ParseUtils.parseBoolean(cols[i++]));
        c.setIsCorporateShareholder(ParseUtils.parseBoolean(cols[i++]));
        c.setIsCoopMember(ParseUtils.parseBoolean(cols[i++]));

        c.setCommonShareTotal(ParseUtils.parseInteger(cols[i++]));
        c.setFarmYears(ParseUtils.parseInteger(cols[i++]));
        c.setIsLastYearFarming(ParseUtils.parseBoolean(cols[i++]));
        c.setFormOriginCode(ParseUtils.parseInteger(cols[i++]));
        c.setIndustryCode(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantProfileCode(ParseUtils.parseInteger(cols[i++]));

        c.setIsAccrualCashConversion(ParseUtils.parseBoolean(cols[i++]));
        c.setIsPerishableCommodities(ParseUtils.parseBoolean(cols[i++]));
        c.setIsReceipts(ParseUtils.parseBoolean(cols[i++]));
        c.setIsOtherText(ParseUtils.parseBoolean(cols[i++]));
        c.setOtherText(cols[i++]);

        c.setIsAccrualWorksheet(ParseUtils.parseBoolean(cols[i++]));
        c.setIsCwbWorksheet(ParseUtils.parseBoolean(cols[i++]));
        c.setIsCombinedThisYear(ParseUtils.parseBoolean(cols[i++]));
        c.setIsCompletedProdCycle(ParseUtils.parseBoolean(cols[i++]));
        c.setIsDisaster(ParseUtils.parseBoolean(cols[i++]));
        c.setIsCopyCobToContact(ParseUtils.parseBoolean(cols[i++]));

        c.setMunicipalityCode(ParseUtils.parseInteger(cols[i++]));

        // skip legal descriptors
        i++;
        i++;
        i++;
        i++;
        i++;

        c.setFormVersionEffectiveDate(cols[i++]);

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_farm_info");
                add("part_pin");
                add("prgrm_yy");
                add("form_vrsn_num");
                add("part_res_prov_code");
                add("part_fmstd_prov_code");
                add("form_pstmrk_date");
                add("form_rcvd_date");
                add("sole_prop_ind");
                add("prshp_ind");
                add("corp_shlr_ind");
                add("coop_mem_ind");
                add("corp_shr_tot");
                add("farm_years");
                add("last_farm_incm_yy");
                add("form_org_code");
                add("industy_code");
                add("part_profile_code");
                add("acc_to_cash_ind");
                add("persih_comm_ind");
                add("receipts_ind");
                add("other_ind");
                add("other_text");
                add("acc_ref_mrgn_wrksht_ind");
                add("cwb_adj_wrksht_ind");
                add("combined_type_ind");
                add("completed_prdctn_cycle_ind");
                add("disaster_circumstances_ind");
                add("cntct_rqr_cob_ind");
                add("mncplty_code");
                add("legal_land_desc_qtr");
                add("legal_land_desc_sec");
                add("legal_land_desc_twp");
                add("legal_land_desc_rng");
                add("legal_land_desc_mer");
                add("form_version_effective_date");
            }
        };
    }

}
