package ca.bc.gov.farms.csv;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public abstract class FileHandle<T> {

    protected String[] actualHeaders = {};

    private List<T> records = new ArrayList<>();

    protected int row = 0;

    protected FileHandle(InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                CSVReader csvReader = new CSVReader(inputStreamReader)) {
            String[] line = null;
            while ((line = csvReader.readNext()) != null) {
                row++;
                if (actualHeaders == null || actualHeaders.length == 0) {
                    actualHeaders = line; // First line is the header
                    row++;
                } else {
                    T record = parseLine(line, row);
                    records.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing CSV file", e);
        } finally {
            // reset inputStream for next read
            if (inputStream instanceof ByteArrayInputStream) {
                ((ByteArrayInputStream) inputStream).reset();
            }
        }
    }

    protected abstract T parseLine(String[] cols, int row) throws ParseException;

    protected abstract List<String> getExpectedHeaders();

    public String[] validate() {
        List<String> fileErrors = new ArrayList<>();
        List<String> expectedHeaders = getExpectedHeaders();

        // compare header
        for (int i = 0; i < actualHeaders.length; i++) {
            if (actualHeaders[i] != null && !"".equals(actualHeaders[i].trim())
                    && !expectedHeaders.remove(actualHeaders[i].trim())) {
                fileErrors.add("Extra Header found: " + actualHeaders[i]);
            }
        }

        for (String expectedHeader : expectedHeaders) {
            fileErrors.add("Header not found: " + expectedHeader);
        }

        if (fileErrors.size() <= 0) {
            return null;
        }

        return fileErrors.toArray(new String[fileErrors.size()]);
    }

    public List<T> getRecords() {
        return records;
    }

    protected enum ParseError {
        MISSING_ROW,
        INVALID_ROW
    }

    protected final void error(final ParseError parseError, final int row, final int offset) throws ParseException {
        switch (parseError) {
            case MISSING_ROW:
                throw new ParseException("Row " + row + " does not contain any data", offset);
            case INVALID_ROW:
                throw new ParseException("Row " + row + " does not contain the correct number of columns", offset);
        }
    }

    public abstract int getFileNumber();

    public final int getRowsRead() {
        return row;
    }
}
