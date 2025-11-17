package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.ImportIVPRDto;

public class IvprFileHandle extends FileHandle<ImportIVPRDto> {

    public IvprFileHandle(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    protected ImportIVPRDto parseLine(String[] cols, int row) throws ParseException {
        int index = 0;

        ImportIVPRDto dto = new ImportIVPRDto();
        dto.setProgramYear(ParseUtils.parseInteger(cols[index++].trim()));
        dto.setInventoryItemCode(cols[index++].trim());
        dto.setInventoryItemDesc(cols[index++].trim());
        dto.setInsurableValue(ParseUtils.parseBigDecimal(cols[index++].trim()));
        dto.setPremiumRate(ParseUtils.parseBigDecimal(cols[index++].trim()));
        dto.setFileLocation(cols[index++].trim());

        return dto;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("Year");
                add("Code");
                add("Description");
                add("IV");
                add("PR");
                add("FileLocation");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return 0;
    }

}
