package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z51ParticipantContrib;

public final class Fipd51 extends FileHandle<Z51ParticipantContrib> {

    private static final int NUM_COLS = 7;

    public static FileHandle<Z51ParticipantContrib> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd51(inputStream);
    }

    private Fipd51(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z51ParticipantContrib parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z51ParticipantContrib c = new Z51ParticipantContrib();

        int i = 0;

        c.setContributionKey(ParseUtils.parseInteger(cols[i++]));
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        c.setProvincialContributions(ParseUtils.parseDouble(cols[i++]));
        c.setFederalContributions(ParseUtils.parseDouble(cols[i++]));
        c.setInterimContributions(ParseUtils.parseDouble(cols[i++]));
        c.setProducerShare(ParseUtils.parseDouble(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_fipd_as_51");
                add("part_pin");
                add("prgrm_yy");
                add("prov_cntrbn");
                add("fed_cntrbn");
                add("intrm_cntrbn_amt");
                add("prod_shr_amt");
            }
        };
    }

}
