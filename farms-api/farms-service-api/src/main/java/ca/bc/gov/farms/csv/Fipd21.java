package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z21ParticipantSuppl;

public final class Fipd21 extends FileHandle<Z21ParticipantSuppl> {

    private static final int NUM_COLS = 14;

    public static FileHandle<Z21ParticipantSuppl> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd21(inputStream);
    }

    private Fipd21(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z21ParticipantSuppl parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z21ParticipantSuppl c = new Z21ParticipantSuppl();

        int i = 0;

        c.setInventoryKey(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        i++; // c.setFormVersion(ParseUtils.parseInteger(cols[i++]));
        c.setOperationNumber(ParseUtils.parseInteger(cols[i++]));

        c.setInventoryTypeCode(ParseUtils.parseInteger(cols[i++]));
        c.setInventoryCode(ParseUtils.parseInteger(cols[i++]));
        c.setCropUnitType(ParseUtils.parseInteger(cols[i++]));
        c.setCropOnFarmAcres(ParseUtils.parseDouble(cols[i++]));
        c.setCropQtyProduced(ParseUtils.parseDouble(cols[i++]));
        c.setQuantityEnd(ParseUtils.parseDouble(cols[i++]));
        c.setEndOfYearPrice(ParseUtils.parseDouble(cols[i++]));
        c.setEndOfYearAmount(ParseUtils.parseDouble(cols[i++]));
        c.setCropUnseedableAcres(ParseUtils.parseDouble(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_fipd_as_21");
                add("part_pin");
                add("prgrm_yy");
                add("form_vrsn_num");
                add("incm_expn_stmt_num");
                add("inv_type_code");
                add("inv_code");
                add("unit_type");
                add("on_farm_acr");
                add("qty_produced");
                add("qty_end");
                add("fair_mrkt_price");
                add("end_of_yy_amt");
                add("unseedable_acr");
            }
        };
    }

}
