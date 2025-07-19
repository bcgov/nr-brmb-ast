package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z29InventoryRef;

public final class Fipd29 extends FileHandle<Z29InventoryRef> {

    private static final int NUM_COLS = 7;

    public static FileHandle<Z29InventoryRef> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd29(inputStream);
    }

    private Fipd29(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z29InventoryRef parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z29InventoryRef c = new Z29InventoryRef();

        int i = 0;
        c.setInventoryCode(ParseUtils.parseInteger(cols[i++]));
        c.setInventoryDesc(cols[i++]);
        c.setInventoryTypeCode(ParseUtils.parseInteger(cols[i++]));
        c.setInventoryTypeDescription(cols[i++]);
        c.setInventoryGroupCode(ParseUtils.parseInteger(cols[i++]));
        c.setInventoryGroupDescription(cols[i++]);
        c.setMarketCommodityInd(ParseUtils.parseBoolean(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("inv_code");
                add("inv_code_desc");
                add("inv_type_code");
                add("inv_type_desc");
                add("inv_grp_code");
                add("inv_grp_desc");
                add("market_commodity_ind");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return FileCacheManager.FIPD_29_NUMBER;
    }

}
