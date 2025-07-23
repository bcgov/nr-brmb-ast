package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z50ParticipntBnftCalc;

public final class Fipd50 extends FileHandle<Z50ParticipntBnftCalc> {

    private static final int NUM_COLS = 12;

    public static FileHandle<Z50ParticipntBnftCalc> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd50(inputStream);
    }

    private Fipd50(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z50ParticipntBnftCalc parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z50ParticipntBnftCalc c = new Z50ParticipntBnftCalc();

        int i = 0;

        c.setBenefitCalcKey(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        c.setAgristabilityStatus(ParseUtils.parseInteger(cols[i++]));
        c.setUnadjustedReferenceMargin(ParseUtils.parseDouble(cols[i++]));
        c.setAdjustedReferenceMargin(ParseUtils.parseDouble(cols[i++]));
        c.setProgramMargin(ParseUtils.parseDouble(cols[i++]));
        c.setIsWholeFarm(ParseUtils.parseBoolean(cols[i++]));
        c.setIsStructureChange(ParseUtils.parseBoolean(cols[i++]));
        c.setStructureChangeAdjAmount(ParseUtils.parseDouble(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_fipd_as_50");
                add("part_pin");
                add("prgrm_yy");
                add("as_proc_stat");
                add("unadj_ref_mrgn");
                add("adj_ref_mrgn");
                add("cy_prgm_mrgn");
                add("wf_ind");
                add("strc_chng_ind");
                add("struct_chng_adj_amt");
                add("vld_ftr_en_ind");
                add("vld_ftr_ref_mrgn_ind");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return FileCacheManager.FIPD_50_NUMBER;
    }

}
