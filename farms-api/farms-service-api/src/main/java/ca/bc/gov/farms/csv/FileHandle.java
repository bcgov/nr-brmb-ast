package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public abstract class FileHandle<T> {

    private String[] actualHeaders = {};

    private List<T> records = new ArrayList<>();

    protected FileHandle(InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                CSVReader csvReader = new CSVReader(inputStreamReader)) {
            String[] line = null;
            while ((line = csvReader.readNext()) != null) {
                if (actualHeaders == null) {
                    actualHeaders = line; // First line is the header
                } else {
                    T record = parseLine(line);
                    records.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    protected abstract T parseLine(String[] line);

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
}
