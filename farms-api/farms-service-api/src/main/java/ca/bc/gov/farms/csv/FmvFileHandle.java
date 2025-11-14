package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.ImportFMVDto;

public class FmvFileHandle extends FileHandle<ImportFMVDto> {

    public FmvFileHandle(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    protected ImportFMVDto parseLine(String[] cols, int row) throws ParseException {
        int index = 0;

        ImportFMVDto dto = new ImportFMVDto();
        dto.setProgramYear(ParseUtils.parseInteger(cols[index++].trim()));
        dto.setPeriod(ParseUtils.parseInteger(cols[index++].trim()));
        dto.setMunicipalityCode(cols[index++].trim());
        dto.setInventoryItemCode(cols[index++].trim());
        dto.setCropUnitCode(cols[index++].trim());
        dto.setAveragePrice(ParseUtils.parseBigDecimal(cols[index++].trim()));
        dto.setPercentVariance(ParseUtils.parseBigDecimal(cols[index++].trim()));
        dto.setFileLocation(cols[index++].trim());

        return dto;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("YEAR");
                add("PERIOD");
                add("MUNICIPALITY");
                add("INVENTORY_CODE");
                add("UNIT_CODE");
                add("VALUE");
                add("PERCENT_VARIANCE");
                add("FILE_LOCATION");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return 0;
    }

}
