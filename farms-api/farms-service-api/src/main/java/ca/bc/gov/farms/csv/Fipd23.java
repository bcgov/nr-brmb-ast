package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z23LivestockProdCpct;

public final class Fipd23 extends FileHandle<Z23LivestockProdCpct> {

    private static final int NUM_COLS = 7;

    public static FileHandle<Z23LivestockProdCpct> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd23(inputStream);
    }

    private Fipd23(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z23LivestockProdCpct parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z23LivestockProdCpct c = new Z23LivestockProdCpct();

        int i = 0;
        c.setProductiveCapacityKey(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        i++; // c.setFormVersion(ParseUtils.parseInteger(cols[i++]));
        c.setOperationNumber(ParseUtils.parseInteger(cols[i++]));
        c.setInventoryCode(ParseUtils.parseInteger(cols[i++]));
        c.setProductiveCapacityAmount(ParseUtils.parseDouble(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_fipd_as_23");
                add("part_pin");
                add("prgrm_yy");
                add("form_vrsn_num");
                add("incm_expn_stmt_num");
                add("inv_code");
                add("prod_cap_amt");
            }
        };
    }

}
