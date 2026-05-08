package ca.bc.gov.farms.services;

public final class EnrolmentCalculationMessages {

    public static final String REASON_INSUFFICIENT_REFERENCE_MARGIN_DATA =
            "Insufficient reference margin data for this participant.";
    public static final String REASON_ZERO_MARGINS =
            "Must have non-zero margins for the three most recent years.";
    public static final String REASON_FOUND_TOO_MANY_MARGINS =
            "Found too many margins.";
    public static final String REASON_OVERSIZE_MARGIN =
            "A margin in excess of 100 million dollars was detected.";
    public static final String ERROR_ENW_COMBINED_FARM_PERCENT =
            "Benefit calculation failed so Combined Farm % has not been calculated.";

    private EnrolmentCalculationMessages() {
    }
}
