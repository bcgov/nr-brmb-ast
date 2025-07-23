package ca.bc.gov.farms.csv;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z03StatementInfo;

public final class Fipd03 extends FileHandle<Z03StatementInfo> {

    private static final int NUM_COLS = 24;

    public static FileHandle<Z03StatementInfo> read(final InputStream inputStream) throws CSVParserException {
        return new Fipd03(inputStream);
    }

    private Fipd03(final InputStream inputStream) throws CSVParserException {
        super(inputStream);
    }

    @Override
    protected Z03StatementInfo parseLine(String[] cols, int row) throws ParseException {
        if (cols == null) {
            error(ParseError.MISSING_ROW, row, 0);
            return null;
        }

        if (cols.length != NUM_COLS) {
            error(ParseError.INVALID_ROW, row, cols.length);
            return null;
        }

        Z03StatementInfo c = new Z03StatementInfo();

        int i = 1; // skip uk
        c.setParticipantPin(ParseUtils.parseInteger(cols[i++]));
        c.setProgramYear(ParseUtils.parseInteger(cols[i++]));
        i++; // c.setFormVersion(ParseUtils.parseInteger(cols[i++]));
        c.setOperationNumber(ParseUtils.parseInteger(cols[i++]));

        c.setPartnershipPin(ParseUtils.parseInteger(cols[i++]));
        c.setPartnershipName(cols[i++]);
        c.setPartnershipPercent(ParseUtils.parseDouble(cols[i++]));
        c.setFiscalYearStart(cols[i++]);
        c.setFiscalYearEnd(cols[i++]);
        c.setAccountingCode(ParseUtils.parseInteger(cols[i++]));

        c.setIsLandlord(ParseUtils.parseBoolean(cols[i++]));
        c.setIsCropShare(ParseUtils.parseBoolean(cols[i++]));
        c.setIsFeederMember(ParseUtils.parseBoolean(cols[i++]));
        c.setGrossIncome(ParseUtils.parseDouble(cols[i++]));
        c.setExpenses(ParseUtils.parseDouble(cols[i++]));
        c.setNetIncomeBeforeAdj(ParseUtils.parseDouble(cols[i++]));
        c.setOtherDeductions(ParseUtils.parseDouble(cols[i++]));
        c.setInventoryAdjustments(ParseUtils.parseDouble(cols[i++]));
        c.setNetIncomeAfterAdj(ParseUtils.parseDouble(cols[i++]));
        c.setBusinessUseOfHomeExpenses(ParseUtils.parseDouble(cols[i++]));
        c.setNetFarmIncome(ParseUtils.parseDouble(cols[i++]));
        c.setIsCropDisaster(ParseUtils.parseBoolean(cols[i++]));
        c.setIsLivestockDisaster(ParseUtils.parseBoolean(cols[i++]));

        return c;
    }

    @Override
    protected List<String> getExpectedHeaders() {
        return new ArrayList<String>() {
            {
                add("gk_stmt_info");
                add("part_pin");
                add("prgrm_yy");
                add("form_vrsn_num");
                add("incm_expn_stmt_num");
                add("prshp_pin");
                add("prshp_name");
                add("prshp_pct");
                add("stmt_fy_start_date");
                add("stmt_fy_end_date");
                add("stmt_accrl_code");
                add("lndlrd_crop_shr_ind");
                add("tenant_crop_shr_ind");
                add("feeder_member_ind");
                add("oth_grs_incm_amt");
                add("oth_farm_expn_tot");
                add("net_incm_b_adj_amt");
                add("net_incm_ded_amt");
                add("oth_cy_inv_adj_tot");
                add("net_incm_adj_amt");
                add("bus_elig_exp_amt");
                add("net_farm_incm_amt");
                add("crop_disaster_ind");
                add("livestock_disaster_ind");
            }
        };
    }

    @Override
    public int getFileNumber() {
        return FileCacheManager.FIPD_03_NUMBER;
    }

}
