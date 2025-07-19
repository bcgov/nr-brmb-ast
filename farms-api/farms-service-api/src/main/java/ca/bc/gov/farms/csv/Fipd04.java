package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z04IncomeExpsDtl;

public final class Fipd04 extends FileHandle<Z04IncomeExpsDtl> {

    private static final int NUM_COLS = 8;

    public static FileHandle<Z04IncomeExpsDtl> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd04(inputStream);
    }

    private Fipd04(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z04IncomeExpsDtl parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z04IncomeExpsDtl c = new Z04IncomeExpsDtl();

        int i = 0;
        c.setIncomeExpenseKey(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        i++; // c.setFormVersion(ParseUtils.parseInteger(cols[i++]));
        c.setOperationNumber(ParseUtils.parseInteger(cols[i++]));
        c.setLineCode(ParseUtils.parseInteger(cols[i++]));
        c.setIe(cols[i++]);
        c.setAmount(ParseUtils.parseDouble(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_incm_expn");
                add("part_pin");
                add("prgrm_yy");
                add("form_vrsn_num");
                add("incm_expn_stmt_num");
                add("incm_expn_code");
                add("incm_expn_ind");
                add("incm_expn_amount");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return FileCacheManager.FIPD_04_NUMBER;
    }

}
