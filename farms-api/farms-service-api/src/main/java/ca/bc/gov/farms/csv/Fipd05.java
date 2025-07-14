package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z05PartnerInfo;

public final class Fipd05 extends FileHandle<Z05PartnerInfo> {

    private static final int NUM_COLS = 12;

    public static FileHandle<Z05PartnerInfo> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd05(inputStream);
    }

    private Fipd05(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z05PartnerInfo parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z05PartnerInfo c = new Z05PartnerInfo();

        int i = 0;
        c.setPartnerInfoKey(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        i++; // c.setFormVersion(ParseUtils.parseInteger(cols[i++]));
        c.setOperationNumber(ParseUtils.parseInteger(cols[i++]));
        c.setPartnershipPin(ParseUtils.parseInteger(cols[i++]));
        c.setPartnerFirstName(cols[i++]);
        c.setPartnerLastName(cols[i++]);
        c.setPartnerCorpName(cols[i++]);
        c.setPartnerSinCtnBn(cols[i++]);
        c.setPartnerPercent(ParseUtils.parseDouble(cols[i++]));
        c.setPartnerPin(ParseUtils.parseInteger(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_prtnr_info");
                add("part_pin");
                add("prgrm_yy");
                add("form_vrsn_num");
                add("incm_expn_stmt_num");
                add("prshp_pin");
                add("prtnr_gname");
                add("prtnr_sname");
                add("prtnr_corp_name");
                add("prtnr_sin_bn");
                add("prtnr_pct");
                add("prtnr_pin");
            }
        };
    }

}
