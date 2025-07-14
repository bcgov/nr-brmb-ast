package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.ImportBPUDto;

public class BpuFileHandle extends FileHandle<ImportBPUDto> {

    public BpuFileHandle(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    protected ImportBPUDto parseLine(String[] line) {
        int index = 0;

        ImportBPUDto dto = new ImportBPUDto();
        dto.setProgramYear(ParseUtils.parseInteger(line[index++].trim()));
        dto.setMunicipalityCode(line[index++].trim());
        dto.setInventoryItemCode(line[index++].trim());
        dto.setUnitComment(line[index++].trim());
        dto.setYearMinus6Margin(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus5Margin(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus4Margin(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus3Margin(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus2Margin(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus1Margin(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus6Expense(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus5Expense(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus4Expense(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus3Expense(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus2Expense(ParseUtils.parseBigDecimal(line[index++].trim()));
        dto.setYearMinus1Expense(ParseUtils.parseBigDecimal(line[index++].trim()));

        return dto;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("YEAR");
                add("MUNICIPALITY");
                add("INVENTORY_CODE");
                add("UNIT_DESCRIPTION");
                add("YEAR_MINUS_6_MARGIN");
                add("YEAR_MINUS_5_MARGIN");
                add("YEAR_MINUS_4_MARGIN");
                add("YEAR_MINUS_3_MARGIN");
                add("YEAR_MINUS_2_MARGIN");
                add("YEAR_MINUS_1_MARGIN");
                add("YEAR_MINUS_6_EXPENSE");
                add("YEAR_MINUS_5_EXPENSE");
                add("YEAR_MINUS_4_EXPENSE");
                add("YEAR_MINUS_3_EXPENSE");
                add("YEAR_MINUS_2_EXPENSE");
                add("YEAR_MINUS_1_EXPENSE");
            }
        };
    }

}
