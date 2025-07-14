package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z01ParticipantInfo;

public final class Fipd01 extends FileHandle<Z01ParticipantInfo> {

    /** NUM_COLS (With new cell phone numbers) */
    private static final int NUM_COLS = 33;

    /** LEGACY NUM_COLS (Without the cell phone numbers) */
    private static final int NUM_COLS_LEGACY = 31;

    public static FileHandle<Z01ParticipantInfo> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd01(inputStream);
    }

    private Fipd01(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z01ParticipantInfo parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS_LEGACY && cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z01ParticipantInfo c = new Z01ParticipantInfo();

        int i = 1; // skip uk
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
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
        c.setParticipantTypeCode(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantLanguage(ParseUtils.parseInteger(cols[i++]));
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
        c.setPublicOffice(ParseUtils.parseInteger(cols[i++]));
        c.setIdentEffectiveDate(cols[i++]);
        c.setParticipantEmail(cols[i++]);
        c.setBusinessNumber(cols[i++]);

        if (cols.length == NUM_COLS) {
            c.setParticipantPhoneCell(cols[i++]);
            c.setContactPhoneCell(cols[i++]);
        }

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_participant");
                add("part_pin");
                add("sin_bn");
                add("part_gname");
                add("part_sname");
                add("corp_name");
                add("part_line1_addr");
                add("part_line2_addr");
                add("part_city");
                add("part_prov");
                add("part_pstl_code");
                add("part_cntry_code");
                add("part_type_code");
                add("part_lang_code");
                add("part_fax_num");
                add("part_day_tel_num");
                add("part_eve_tel_num");
                add("cnct_first_name");
                add("cntc_last_name");
                add("cntct_name");
                add("cntct_line1_addr");
                add("cntct_line2_addr");
                add("cntct_city");
                add("cntct_prov");
                add("cntct_pstl_code");
                add("cntct_day_tel_num");
                add("cntct_fax_num");
                add("pblc_offc_aafc_empl_ind");
                add("ident_effective_date");
                add("part_email");
                add("bn");
            }
        };
    }

}
