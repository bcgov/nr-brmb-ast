package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z99ExtractFileCtl;

public final class Fipd99 extends FileHandle<Z99ExtractFileCtl> {

    private static final int NUM_COLS = 3;

    public static FileHandle<Z99ExtractFileCtl> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd99(inputStream);
    }

    private Fipd99(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z99ExtractFileCtl parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z99ExtractFileCtl c = new Z99ExtractFileCtl();

        int i = 0;

        c.setExtractDate(cols[i++]);
        c.setExtractFileNumber(ParseUtils.parseInteger(cols[i++]));
        c.setRowCount(ParseUtils.parseInteger(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("extract_date");
                add("extract_file_num");
                add("extract_row_cnt");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return FileCacheManager.FIPD_99_NUMBER;
    }

}
