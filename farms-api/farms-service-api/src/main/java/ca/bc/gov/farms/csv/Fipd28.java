package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z28ProdInsuranceRef;

public final class Fipd28 extends FileHandle<Z28ProdInsuranceRef> {

    private static final int NUM_COLS = 2;

    public static FileHandle<Z28ProdInsuranceRef> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd28(inputStream);
    }

    private Fipd28(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z28ProdInsuranceRef parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z28ProdInsuranceRef c = new Z28ProdInsuranceRef();

        c.setProductionUnit(ParseUtils.parseInteger(cols[0]));
        c.setProductionUnitDescription(cols[1]);

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("unit_type");
                add("unit_type_desc");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return FileCacheManager.FIPD_28_NUMBER;
    }

}
