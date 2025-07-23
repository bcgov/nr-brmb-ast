package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z22ProductionInsurance;

public final class Fipd22 extends FileHandle<Z22ProductionInsurance> {

    private static final int NUM_COLS = 6;

    public static FileHandle<Z22ProductionInsurance> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd22(inputStream);
    }

    private Fipd22(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z22ProductionInsurance parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z22ProductionInsurance c = new Z22ProductionInsurance();

        int i = 0;
        c.setProductionInsuranceKey(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        i++; // c.setFormVersion(ParseUtils.parseInteger(cols[i++]));
        c.setOperationNumber(ParseUtils.parseInteger(cols[i++]));
        c.setProductionInsuranceNumber(cols[i++]);

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_fipd_as_22");
                add("part_pin");
                add("prgrm_yy");
                add("form_vrsn_num");
                add("incm_expn_stmt_num");
                add("prod_ins_num");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return FileCacheManager.FIPD_22_NUMBER;
    }

}
