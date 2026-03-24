package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z40PrtcpntRefSuplDtl;

public final class Fipd40 extends FileHandle<Z40PrtcpntRefSuplDtl> {

    private static final int NUM_COLS = 18;

    public static FileHandle<Z40PrtcpntRefSuplDtl> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd40(inputStream);
    }

    private Fipd40(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z40PrtcpntRefSuplDtl parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z40PrtcpntRefSuplDtl c = new Z40PrtcpntRefSuplDtl();

        int i = 0;
        c.setPriorYearSupplementalKey(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        i++; // c.setFormVersion(ParseUtils.parseInteger(cols[i++]));
        c.setOperationNumber(ParseUtils.parseInteger(cols[i++]));

        c.setInventoryTypeCode(ParseUtils.parseInteger(cols[i++]));
        c.setInventoryCode(ParseUtils.parseInteger(cols[i++]));
        c.setProductionUnit(ParseUtils.parseInteger(cols[i++]));
        c.setQuantityStart(ParseUtils.parseDouble(cols[i++]));
        c.setStartingPrice(ParseUtils.parseDouble(cols[i++]));
        c.setCropOnFarmAcres(ParseUtils.parseDouble(cols[i++]));
        c.setCropQtyProduced(ParseUtils.parseDouble(cols[i++]));
        c.setQuantityEnd(ParseUtils.parseDouble(cols[i++]));
        c.setEndYearProducerPrice(ParseUtils.parseDouble(cols[i++]));
        c.setIsAcceptProducerPrice(ParseUtils.parseBoolean(cols[i++]));
        c.setEndYearPrice(ParseUtils.parseDouble(cols[i++]));
        c.setAarmReferenceP1Price(ParseUtils.parseDouble(cols[i++]));
        c.setAarmReferenceP2Price(ParseUtils.parseDouble(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_fipd_as_40");
                add("part_pin");
                add("ref_yy");
                add("form_vrsn_num");
                add("incm_expn_stmt_num");
                add("inv_type_code");
                add("inv_code");
                add("unit_type");
                add("qty_start");
                add("start_year_price");
                add("on_farm_acr");
                add("crop_qty_produced");
                add("qty_end");
                add("end_year_prod_price");
                add("end_year_prod_ovrd_ind");
                add("end_year_price");
                add("aarm_start_year_price");
                add("aarm_end_year_price");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return FileCacheManager.FIPD_40_NUMBER;
    }

}
