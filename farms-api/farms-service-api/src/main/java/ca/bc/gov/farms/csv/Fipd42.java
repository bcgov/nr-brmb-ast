package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z42ParticipantRefYear;

public final class Fipd42 extends FileHandle<Z42ParticipantRefYear> {

    private static final int NUM_COLS = 7;

    public static FileHandle<Z42ParticipantRefYear> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd42(inputStream);
    }

    private Fipd42(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z42ParticipantRefYear parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z42ParticipantRefYear c = new Z42ParticipantRefYear();

        int i = 0;
        c.setProductiveCapacityKey(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        c.setRefOperationNumber(ParseUtils.parseInteger(cols[i++]));
        c.setProductiveTypeCode(ParseUtils.parseInteger(cols[i++]));
        c.setProductiveCode(ParseUtils.parseInteger(cols[i++]));
        c.setProductiveCapacityUnits(ParseUtils.parseDouble(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_fipd_as_42");
                add("part_pin");
                add("ref_yy");
                add("ref_stmt_num");
                add("prod_type_code");
                add("prod_code");
                add("prod_cap_amt");
            }
        };
    }

}
